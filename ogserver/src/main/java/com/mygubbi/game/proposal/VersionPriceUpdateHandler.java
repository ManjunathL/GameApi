package com.mygubbi.game.proposal;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import com.mygubbi.game.proposal.model.NewPriceMaster;
import com.mygubbi.game.proposal.model.ProposalVersion;
import com.mygubbi.game.proposal.price.AddOrSubtractHikePriceService;
import com.mygubbi.game.proposal.price.FinishChangeService;
import com.mygubbi.game.proposal.price.ProposalVersionPriceUpdateService;
import com.mygubbi.game.proposal.price.ProposalVersionPriceUpdateServiceCopy;
import com.mygubbi.route.AbstractRouteHandler;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by User on 17-04-2017.
 */
public class VersionPriceUpdateHandler extends AbstractRouteHandler {

    private final static Logger LOG = LogManager.getLogger(VersionPriceUpdateHandler.class);



    public VersionPriceUpdateHandler(Vertx vertx) {
        super(vertx);
        this.route().handler(BodyHandler.create());
        this.post("/updatepricefornewproposal").handler(this::updatePriceForNewProposal);
        this.post("/updatefinishandpriceforproposals").handler(this::updateFinishAndPriceForProposals);
        this.post("/addorsubtracthikeforproposal").handler(this::addOrSubtractHikeForProposal);
        this.post("/updatefinishandpriceforproposal").handler(this::updateFinishAndPricePriceForOneProposal);
        this.post("/addorsubtracthikeforallproposal").handler(this::addOrSubtractHikeForAllProposals);

    }

    private void updateFinishAndPricePriceForOneProposal(RoutingContext context){
        JsonObject productJson = context.getBodyAsJson();
        LOG.debug("Proposal Json :" + productJson);
        updatePriceProposal(context,productJson);
    }

    private void updatePriceProposal(RoutingContext context, JsonObject proposalJson){
        LOG.debug("Product Json :" + proposalJson.encodePrettily());
        Integer id = LocalCache.getInstance().store(new QueryData("version.getversiondetails", proposalJson));
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
                        VertxInstance.get().eventBus().send(FinishChangeService.UPDATE_FINISH_COPY, pvId,
                                (AsyncResult<Message<Integer>> dataresult) ->
                                {
                                    sendJsonResponse(context,new JsonObject().put("status","Successfully Started Updater").toString());
                                });
                    }
                });
    }
    private void addOrSubtractHikeForAllProposals(RoutingContext context){
        LOG.info("routingContext = "+context.getBodyAsJson());
        Integer id1 = LocalCache.getInstance().store(context.getBodyAsJson());
        VertxInstance.get().eventBus().send(AddOrSubtractHikePriceService.UPDATE_DISCOUNT_OR_HIKE_FOR_ALL_PROPOSALS, id1,
                (AsyncResult<Message<Integer>> dataresult) ->
                {
                    sendJsonResponse(context,new JsonObject().put("status","Successfully Started Updater").toString());
                });
    }

    private void addOrSubtractHikeForProposal(RoutingContext context){
        LOG.info("routingContext = "+context.getBodyAsJson());
        //pass proposalID and version
        Integer id = LocalCache.getInstance().store(new QueryData("proposal.select.newPriceMaster", context.getBodyAsJson()));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    LOG.debug("Result data :" + resultData.getParams());
                    if ((resultData.rows == null || resultData.rows.isEmpty())) {
                        sendError(context, "Error in getting  proposal.");
                        LOG.error("Error in getting  proposal." + resultData.errorMessage, resultData.error);
                    } else {
                        Integer id1 = LocalCache.getInstance().store(new NewPriceMaster(resultData.rows.get(0)));
                        VertxInstance.get().eventBus().send(AddOrSubtractHikePriceService.UPDATE_DISCOUNT_OR_HIKE_FOR_PROPOSALS, id1,
                                (AsyncResult<Message<Integer>> dataresult) -> {
                                    sendJsonResponse(context, new JsonObject().put("status", "Successfully Started AddOrSubtractHikePriceService").toString());
                                });
                    }
                });
    }

    private void updateFinishAndPriceForProposals(RoutingContext routingContext){
        LOG.info("routingContext = "+routingContext.getBodyAsJson());
        Integer id1 = LocalCache.getInstance().store(routingContext.getBodyAsJson());
        VertxInstance.get().eventBus().send(FinishChangeService.UPDATE_FINISH_COPY_FOR_PROPOSALS, id1,
                (AsyncResult<Message<Integer>> dataresult) ->
                {
                    sendJsonResponse(routingContext,new JsonObject().put("status","Successfully Started Updater").toString());
                });
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
