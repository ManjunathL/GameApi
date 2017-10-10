package com.mygubbi.game.proposal;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.StringUtils;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import com.mygubbi.game.proposal.model.ProposalHeader;
import com.mygubbi.game.proposal.model.ProposalVersion;
import com.mygubbi.game.proposal.quote.AssembledProductInQuote;
import com.mygubbi.game.proposal.quote.QuoteData;
import com.mygubbi.route.AbstractRouteHandler;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Created by User on 09-10-2017.
 */
public class FFApiHandler extends AbstractRouteHandler {

    private final static Logger LOG = LogManager.getLogger(FFApiHandler.class);

    private static final String PRODUCT = "Product";
    private static final String ADDON = "Addon";

    public FFApiHandler(Vertx vertx) {
        super(vertx);
        this.post("/getscopeofwork").handler(this::getScopeOfWorkDetails);
    }

    private void getScopeOfWorkDetails(RoutingContext routingContext) {
        if (!isRequestAuthenticated(routingContext)) return;
        getProposalHeader(routingContext);
    }

    private void getProposalHeader(RoutingContext routingContext) {
        JsonObject jsonObject = routingContext.getBodyAsJson();
        LOG.debug("Json object :" + routingContext.request().getParam("quoteNo"));
//        LOG.debug("Json object :" + jsonObject.encodePrettily());
        Integer id = LocalCache.getInstance().store(new QueryData("proposal.detail.bycrmid", new JsonObject().put("quoteNo", routingContext.request().getParam("quoteNo"))));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag || resultData.rows == null || resultData.rows.isEmpty()) {
                       sendError(routingContext, "Proposal not found" );
                        LOG.error("Proposal not found for id:" + jsonObject.getString("quoteNo"));
                    } else {
                        ProposalHeader proposalHeader = new ProposalHeader(resultData.rows.get(0));
                        getLatestVersion(proposalHeader,routingContext);

                    }
                });
    }

    private void getLatestVersion(ProposalHeader proposalHeader,RoutingContext routingContext)
    {

        JsonObject jsonObject = routingContext.getBodyAsJson();
        LOG.debug("Json object :" + routingContext.request().getParam("quoteNo"));
//        LOG.debug("Json object :" + jsonObject.encodePrettily());
        Integer id = LocalCache.getInstance().store(new QueryData("proposal.version.recentpublishedversion",proposalHeader));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag || resultData.rows == null || resultData.rows.isEmpty()) {
                        sendError(routingContext, "Proposal doesnt have any published or confirmed versions:" );
//                        LOG.error("Proposal not found for id:" + jsonObject.getString("quoteNo"));
                    } else {
                        ProposalVersion proposalVersion = new ProposalVersion(resultData.rows.get(0));
                        getProductAndAddons(proposalHeader,routingContext,proposalVersion);

                    }
                });
    }

    private void getProductAndAddons(ProposalHeader proposalHeader,RoutingContext routingContext,ProposalVersion proposalVersion)
    {

        List<QueryData> queryDatas = new ArrayList<>();
        LOG.debug("proposal header : " + proposalHeader);
        queryDatas.add(new QueryData("proposal.version.products.select", new JsonObject().put("proposalId", proposalHeader.getId()).put("version",proposalVersion.getVersion())));
        queryDatas.add(new QueryData("proposal.version.addons.select", new JsonObject().put("proposalId", proposalHeader.getId()).put("version",proposalVersion.getVersion())));

        Integer id = LocalCache.getInstance().store(queryDatas);
        VertxInstance.get().eventBus().send(DatabaseService.MULTI_DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    List<QueryData> resultData = (List<QueryData>) LocalCache.getInstance().remove(selectResult.result().body());

                    if (resultData.get(0).errorFlag || resultData.get(1).errorFlag)
                    {
                        sendError(routingContext, "error in retrieving product and addons:" );
                        LOG.error("error in retrieving product and addons for id:" + proposalHeader.getId());
                    }
                    else
                    {
                        List<ProductLineItem> products = new ArrayList<ProductLineItem>();
                        for (JsonObject json : resultData.get(0).rows) {
                            products.add(new ProductLineItem(json));
                        }
                        List<ProductAddon> addons = new ArrayList<ProductAddon>();
                        for (JsonObject json : resultData.get(1).rows) {
                            addons.add(new ProductAddon(json));
                        }

                        LOG.debug("Product size : " + products.size());
                        LOG.debug("Addon size : " + addons.size());

                        collectObjects(routingContext,proposalHeader,products,addons);
                    }
                });
    }

    private void collectObjects(RoutingContext routingContext, ProposalHeader proposalHeader, List<ProductLineItem> productLineItems,List<ProductAddon> productAddons)
    {


        List<JsonObject> listOfServicesTest = new ArrayList<>();

        for (ProductLineItem productLineItem : productLineItems)
        {
            LOG.debug("Product Line item : " + productLineItem);
            listOfServicesTest.add(new JsonObject().put("room",productLineItem.getRoomCode()).put("type",PRODUCT).put("title",productLineItem.getTitle()));
        }

        for (ProductAddon productAddon : productAddons)
        {
            if (productAddon.isAccessory()) return;
            listOfServicesTest.add(new JsonObject().put("room",productAddon.getRoomCode()).put("type",ADDON).put("title",productAddon.getProductTypeCode() + " :" + productAddon.getProductSubtypeCode() + " :" + productAddon.getProduct()));
        }

        LOG.debug("List of servieces size : " + listOfServicesTest.size());

        if (listOfServicesTest.size() != 0)
        {
            sendJsonResponse(routingContext,listOfServicesTest.toString());
        }
        else
        {
            sendJsonResponse(routingContext,new JsonObject().put("status","Error in retrieving SOW").toString());
        }


    }


    private boolean isRequestAuthenticated(RoutingContext routingContext){
        final String authorization = routingContext.request().getHeader("Authorization");
        LOG.debug("values :" + authorization);
        if (authorization != null && authorization.startsWith("Basic")) {
            // Authorization: Basic base64credentials
            String base64Credentials = authorization.substring("Basic".length()).trim();
            String credentials = new String(Base64.getDecoder().decode(base64Credentials),
                    Charset.forName("UTF-8"));
            // credentials = username:password
            if (StringUtils.isNonEmpty(credentials)) {
                final String[] values = credentials.split(":", 2);
                LOG.debug("values :" + credentials);
                if (values[0].equals("game") && values[1].equals("Mygubbi"))
                    return true;
            }
        }
        sendError(routingContext.response(), "Credentials not valid");
        return false;
    }
}
