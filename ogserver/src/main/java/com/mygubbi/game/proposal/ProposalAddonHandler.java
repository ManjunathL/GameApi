package com.mygubbi.game.proposal;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import com.mygubbi.game.proposal.model.PriceMaster;
import com.mygubbi.game.proposal.price.RateCardService;
import com.mygubbi.route.AbstractRouteHandler;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Date;

/**
 * Created by sunil on 25-04-2016.
 */
public class ProposalAddonHandler extends AbstractRouteHandler {
    private final static Logger LOG = LogManager.getLogger(ProposalAddonHandler.class);

    Date priceDate;

    public ProposalAddonHandler(Vertx vertx) {
        super(vertx);
        this.route().handler(BodyHandler.create());
        this.post("/createnew").handler(this::copyAddonsFromVersion);
        this.post("/createnewfromoldproposal").handler(this::copyAddonsFromOldProposal);
        this.get("/getprice").handler(this::getAddonPrice);

    }


    private void copyAddonsFromVersion(RoutingContext routingContext) {
        JsonObject addonJson = routingContext.getBodyAsJson();
        LOG.debug("Get body as Json :" + addonJson.encodePrettily());
        Integer id = LocalCache.getInstance().store(new QueryData("proposal.addon.createnew", addonJson));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());

                    LOG.debug("resultData.updateResult.getUpdated() 1:" + resultData.updateResult.getUpdated());

                    if (resultData.errorFlag || resultData.updateResult.getUpdated() == 0) {
                        sendError(routingContext, "Error in copying addon line item in the proposal.");
                        LOG.error("Error in copying addon line item in the proposal. " + resultData.errorMessage, resultData.error);
                    } else {
                        sendJsonResponse(routingContext, addonJson.toString());
                    }
                });
    }

    private void copyAddonsFromOldProposal(RoutingContext routingContext)
    {
        JsonObject versionJson = routingContext.getBodyAsJson();
        LOG.debug("Get body as Json :" + versionJson.encodePrettily());
        Integer id = LocalCache.getInstance().store(new QueryData("proposal.addon.createnewfromoldproposal", versionJson));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    LOG.debug("resultData.updateResult.getUpdated() 2:" + resultData.updateResult.getUpdated());
                    if (resultData.errorFlag || resultData.updateResult.getUpdated() == 0)
                    {
                        sendError(routingContext, "Error in copying product line item in the proposal.");
                        LOG.error("Error in copying product line item in the proposal. " + resultData.errorMessage, resultData.error);
                    }
                    else
                    {
                        sendJsonResponse(routingContext, versionJson.toString());
                    }
                });
    }

    private void getAddonPrice(RoutingContext routingContext) {
        String versionJson1 = routingContext.get("code");
        String versionJson2 = routingContext.get("priceDate");
        String versionJson3 = routingContext.get("city");

        LOG.debug("COde :" + versionJson1 + "|" + versionJson2 + "|" + versionJson3 + "|" );


       this.priceDate = Date.valueOf(versionJson2.substring(0, 9));

        PriceMaster addonRate = RateCardService.getInstance().getAddonRate(versionJson1, this.priceDate, versionJson3);
        if (addonRate == null || addonRate.getPrice() == 0) {
             LOG.error("Error in retrieving addon price");
        } else {
            sendJsonResponse(routingContext, addonRate.toString());
        }

    }
}
