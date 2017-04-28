package com.mygubbi.game.proposal;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import com.mygubbi.game.proposal.model.ProposalVersion;
import com.mygubbi.game.proposal.price.ProposalVersionPriceUpdateService;
import com.mygubbi.route.AbstractRouteHandler;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Created by User on 17-04-2017.
 */
public class VersionPriceUpdateHandler extends AbstractRouteHandler {

    private final static Logger LOG = LogManager.getLogger(VersionPriceUpdateHandler.class);



    public VersionPriceUpdateHandler(Vertx vertx) {
        super(vertx);
        this.route().handler(BodyHandler.create());
        this.post("/updatepricefornewproposal").handler(this::updatePriceForNewProposal);

    }

    private void updatePriceForNewProposal(RoutingContext routingContext)
    {
        JsonObject productJson = routingContext.getBodyAsJson();
        LOG.debug("Proposal Json :" + productJson);
        updatePrice(routingContext,productJson);
    }


    private void updatePrice(RoutingContext context, JsonObject proposalJson)
        {
            LOG.debug("Product Json :" + proposalJson.encodePrettily());
            Integer id = LocalCache.getInstance().store(new QueryData("proposal.version.getdraftversion", proposalJson));
            VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                    (AsyncResult<Message<Integer>> selectResult) -> {
                        QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                        LOG.debug("Result data :" + resultData.getParams());
                        if (resultData.errorFlag)
                        {
                            sendError(context, "Error in updating  proposal." + proposalJson.getInteger("id"));
                            LOG.error("Error in updating product line item in the proposal. " + resultData.errorMessage, resultData.error);
                        }
                        else
                        {
                                ProposalVersion proposalVersion = new ProposalVersion(resultData.rows.get(0));
                                Integer pvId = LocalCache.getInstance().store(proposalVersion);
                                VertxInstance.get().eventBus().send(ProposalVersionPriceUpdateService.UPDATE_VERSION_PRICE, pvId,
                                        (AsyncResult<Message<Integer>> dataresult) ->
                                        {
                                            ProposalVersion result = (ProposalVersion) LocalCache.getInstance().remove(dataresult.result().body());
                                            if (result == null)
                                            {
                                                sendError(context,"Error in copying proposal version");
                                            }
                                            else
                                            {
                                                sendJsonResponse(context, proposalJson.toString());
                                            }
                                        });
                        }
                    });
        }
}
