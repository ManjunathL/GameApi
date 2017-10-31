package com.mygubbi.game.proposal;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import com.mygubbi.game.proposal.model.AccHwComponent;
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

import java.util.*;

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
        LOG.info("CRM ID : " + crmId);
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


        Map<String, JsonObject> servicesMap = new LinkedHashMap<>();

        for (ProductLineItem productLineItem : productLineItems)
        {
            String concat = productLineItem.getRoomCode() + " :" + productLineItem.getTitle();
            JsonObject put = new JsonObject().put("room", productLineItem.getRoomCode()).put("type", PRODUCT).put("title", productLineItem.getTitle());
            servicesMap.put(concat,put);

        }

        for (ProductAddon productAddon : productAddons)
        {
            if (productAddon.isCustomAddon())
            {
                String concat = productAddon.getRoomCode() + " :" + productAddon.getCustomAddonCategory() + " :" + productAddon.getProduct();
                JsonObject put = new JsonObject().put("room", productAddon.getRoomCode()).put("type", SERVICE).put("title", productAddon.getCustomAddonCategory() + " :" + productAddon.getProduct());
                servicesMap.put(concat,put);
            }
            else if (productAddon.isCounterTop() && productAddon.getProductTypeCode().equals(FALSE_CEILING))
            {
                continue;
            }
            else if (!(productAddon.isAccessory()))
            {
                String concat = productAddon.getRoomCode() + " :" + productAddon.getProductTypeCode() + " :" + productAddon.getProductSubtypeCode() + " :" + productAddon.getProduct();
                String product = stringCompare(productAddon.getProductSubtypeCode(),productAddon.getProduct());


                if (!(productAddon.getScopeDisplayFlag().equalsIgnoreCase("no"))) {
                if (servicesMap.containsKey(concat))
                {
                    concat = productAddon.getProductTypeCode() + " :" + product + " :" + productAddon.getREMARKS();
                    JsonObject put = new JsonObject().put("room", productAddon.getRoomCode()).put("type", SERVICE).put("title", productAddon.getProductTypeCode() + " :" + productAddon.getProductSubtypeCode() + " :" + productAddon.getProduct() + " :" + productAddon.getREMARKS());
                    servicesMap.put(concat,put);
                }
                else {
                        JsonObject put = new JsonObject().put("room", productAddon.getRoomCode()).put("type", SERVICE).put("title", product + " :" + productAddon.getProduct());
                        servicesMap.put(concat,put);
                    }
                }
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
        if (counterTops.size() != 0)
        {
            String product = stringCompare(counterTops.get(0).getProductSubtypeCode(),counterTops.get(0).getProduct());
            String concat =  counterTops.get(0).getRoomCode() + " :" + counterTops.get(0).getProductTypeCode() + " :" + product;
            JsonObject put = new JsonObject().put("room", counterTops.get(0).getRoomCode()).put("type", SERVICE).put("title", counterTops.get(0).getProductTypeCode() + " :" +product);
            servicesMap.put(concat,put);
        }

        List<JsonObject> list = new ArrayList<JsonObject>(servicesMap.values());

        LOG.info("Scope of Services size for CRM : " + routingContext.request().getParam("crmId") + " :" + list.size());

        if (list.size() == 0)
        {
            LOG.error("Error");
            sendError(routingContext,"error in retrieving product and addons:");
        }
        else {
            LOG.info("Success");
            sendJsonResponse(routingContext,list.toString());
        }
    }

    private String stringCompare(String s1, String s2)
    {
        if (s1.equals(s2)) return s1;
        else return s1 + " :" + s2;
    }


}
