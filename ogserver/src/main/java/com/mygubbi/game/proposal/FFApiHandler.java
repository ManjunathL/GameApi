package com.mygubbi.game.proposal;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import com.mygubbi.game.proposal.model.Proposal;
import com.mygubbi.game.proposal.model.ProposalHeader;
import com.mygubbi.game.proposal.model.ProposalVersion;
import com.mygubbi.route.AbstractRouteHandler;
import com.mygubbi.si.crm.CrmApiHandler;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 09-10-2017.
 */
public class FFApiHandler extends AbstractRouteHandler {

    private final static Logger LOG = LogManager.getLogger(FFApiHandler.class);

    private static final String PRODUCT = "Product";
    private static final String SERVICE = "Service";
    private static final String FALSE_CEILING = "False Ceiling";

    public FFApiHandler(Vertx vertx) {
        super(vertx);
        this.post("/getscopeofwork").handler(this::getScopeOfWorkDetails);
    }

    private void getScopeOfWorkDetails(RoutingContext routingContext) {
        CrmApiHandler crmApiHandler = new CrmApiHandler(vertx);
        boolean requestAuthenticated = crmApiHandler.isRequestAuthenticated(routingContext);
        if (!requestAuthenticated) return;
        getProductAndAddons(routingContext);
    }

    private void getProductAndAddons(RoutingContext routingContext)
    {

        List<QueryData> queryDatas = new ArrayList<>();
        String crmId = routingContext.request().getParam("crmId");
        LOG.debug("CRM ID : " + crmId);
        if (crmId == null || crmId.isEmpty())
        {
            sendError(routingContext, "error in retrieving product and addons for SAL NO :" + crmId );
            LOG.error("error in retrieving product and addons for SAL No:" + crmId);
        }
        queryDatas.add(new QueryData("proposal.product.scopeofwork", new JsonObject().put("crmId", crmId)));
        queryDatas.add(new QueryData("proposal.addon.scopeofwork", new JsonObject().put("crmId",crmId)));

        Integer id = LocalCache.getInstance().store(queryDatas);
        VertxInstance.get().eventBus().send(DatabaseService.MULTI_DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    List<QueryData> resultData = (List<QueryData>) LocalCache.getInstance().remove(selectResult.result().body());

                    if (resultData.get(0).errorFlag || resultData.get(1).errorFlag)
                    {
                        sendError(routingContext, "error in retrieving product and addons for SAL NO :" + crmId );
                        LOG.error("error in retrieving product and addons for SAL No:" + crmId);
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

                        collectObjects(routingContext,products,addons);
                    }
                });
    }

    private void collectObjects(RoutingContext routingContext, List<ProductLineItem> productLineItems,List<ProductAddon> productAddons)
    {

        List<JsonObject> listOfServices = new ArrayList<>();

        for (ProductLineItem productLineItem : productLineItems)
        {
            listOfServices.add(new JsonObject().put("room",productLineItem.getRoomCode()).put("type",PRODUCT).put("title",productLineItem.getTitle() + " : " + productLineItem.getRoomCode()));
        }

        for (ProductAddon productAddon : productAddons)
        {
            if (productAddon.isCustomAddon())
            {
                listOfServices.add(new JsonObject().put("room",productAddon.getRoomCode()).put("type", SERVICE).put("title",productAddon.getCustomAddonCategory() + " :" + productAddon.getProduct()));
            }
            else if (productAddon.isCounterTop() && productAddon.getProductTypeCode().equals(FALSE_CEILING))
            {
                continue;
            }
            else if (!(productAddon.isAccessory()))
            {
                if (!(productAddon.getScopeDisplayFlag().equalsIgnoreCase("no"))) listOfServices.add(new JsonObject().put("room",productAddon.getRoomCode()).put("type", SERVICE).put("title",productAddon.getProductTypeCode() + " :" + productAddon.getProductSubtypeCode() + " :" + productAddon.getProduct()));
            }
        }
        List<ProductAddon> counterTops = new ArrayList<>();

        for (ProductAddon productAddon : productAddons)
        {
            if (productAddon.isCounterTop() && productAddon.getProductTypeCode().equals(FALSE_CEILING))
            {
                counterTops.add(productAddon);
            }
        }
        if (counterTops.size() != 0) listOfServices.add(new JsonObject().put("room",counterTops.get(0).getRoomCode()).put("type", SERVICE).put("title", counterTops.get(0).getProductTypeCode() + " :" + counterTops.get(0).getProductSubtypeCode() + " :" + counterTops.get(0).getProduct()));

        if (listOfServices.size() == 0)
        {
            LOG.debug("Error");
            sendError(routingContext,"error in retrieving product and addons:");
        }
        else {
            LOG.debug("Success");
            sendJsonResponse(routingContext,listOfServices.toString());
        }
    }


}
