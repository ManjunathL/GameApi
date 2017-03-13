package com.mygubbi.route;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Created by Mehbub on 08-03-2017.
 */
public class PartnerHandler extends AbstractRouteHandler {

    private final static Logger LOG = LogManager.getLogger(PartnerHandler.class);

    public PartnerHandler(Vertx vertx) {
        super(vertx);
        this.route().handler(BodyHandler.create());
        this.get("/developerDetails").handler(this::developerDetail);
        this.get("/projectDetails").handler(this::projectDetail);
        this.get("/towerDetails").handler(this::towerDetail);
        this.get("/floorPlanDetails").handler(this::floorPlanDetail);
        this.get("/packageDetails").handler(this::packageDetail);
    }
    private void developerDetail(RoutingContext context){

        String builderName = context.request().getParam("developer_name");


        JsonObject params = new JsonObject().put("developer_name", builderName);
        this.fetchProductsAndSend(context, "builder.select.developerDetails", params);
    }
    private void fetchProductsAndSend(RoutingContext context, String queryId, JsonObject paramsData){

        Integer id = LocalCache.getInstance().store(new QueryData(queryId, paramsData));
        LOG.info("Executing query:" + queryId + " | " + paramsData);
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (selectData == null || selectData.rows == null)
                    {
                        sendError(context, "Did not find products for " + paramsData.toString() + ". Error:" + selectData.errorMessage);
                    }
                    else
                    {
                        String result = selectData.rows.toString();
                        sendJsonResponse(context, result );

                        //                     context.response().putHeader("content-type", "application/json").end(selectData.getJsonDataRows("productJson").encodePrettily());

                    }
                });
    }
    private void projectDetail(RoutingContext context){

        String builderName = context.request().getParam("developer_name");


        JsonObject params = new JsonObject().put("developer_name", builderName);
        this.fetchProjectDetailsAndSend(context, "builder.select.projectDetails", params);
    }
    private void fetchProjectDetailsAndSend(RoutingContext context, String queryId, JsonObject paramsData){

        Integer id = LocalCache.getInstance().store(new QueryData(queryId, paramsData));
        LOG.info("Executing query:" + queryId + " | " + paramsData);
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (selectData == null || selectData.rows == null)
                    {
                        sendError(context, "Did not find products for " + paramsData.toString() + ". Error:" + selectData.errorMessage);
                    }
                    else
                    {
                        String result = selectData.rows.toString();
                        sendJsonResponse(context, result );

                        //                     context.response().putHeader("content-type", "application/json").end(selectData.getJsonDataRows("productJson").encodePrettily());

                    }
                });
    }
    private void towerDetail(RoutingContext context){

        String builderName = context.request().getParam("developer_name");
        String projectName = context.request().getParam("project_name");


        JsonObject params = new JsonObject().put("developer_name", builderName)
                                            .put("project_name", projectName);
        this.fetchtowerDetailsAndSend(context, "builder.select.towerDetails", params);
    }
    private void fetchtowerDetailsAndSend(RoutingContext context, String queryId, JsonObject paramsData){

        Integer id = LocalCache.getInstance().store(new QueryData(queryId, paramsData));
        LOG.info("Executing query:" + queryId + " | " + paramsData);
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (selectData == null || selectData.rows == null)
                    {
                        sendError(context, "Did not find products for " + paramsData.toString() + ". Error:" + selectData.errorMessage);
                    }
                    else
                    {
                        String result = selectData.rows.toString();
                        sendJsonResponse(context, result );

                        //                     context.response().putHeader("content-type", "application/json").end(selectData.getJsonDataRows("productJson").encodePrettily());

                    }
                });
    }
    private void floorPlanDetail(RoutingContext context){

        //String builderName = context.request().getParam("developer_name");
        String blockId = context.request().getParam("blockId");
        String blockName = context.request().getParam("block_name");
        String apartmentNumber = context.request().getParam("apartment_number");


        JsonObject params = new JsonObject().put("blockId", blockId)
                                            .put("block_name", blockName)
                                            .put("apartment_number", apartmentNumber);
        this.fetchfloorPlanDetailsAndSend(context, "builder.select.floorPlanDetails", params);
    }
    private void fetchfloorPlanDetailsAndSend(RoutingContext context, String queryId, JsonObject paramsData){

        Integer id = LocalCache.getInstance().store(new QueryData(queryId, paramsData));
        LOG.info("Executing query:" + queryId + " | " + paramsData);
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (selectData == null || selectData.rows == null)
                    {
                        sendError(context, "Did not find products for " + paramsData.toString() + ". Error:" + selectData.errorMessage);
                    }
                    else
                    {
                        String result = selectData.rows.toString();
                        sendJsonResponse(context, result );

                        //                     context.response().putHeader("content-type", "application/json").end(selectData.getJsonDataRows("productJson").encodePrettily());

                    }
                });
    }
    private void packageDetail(RoutingContext context){

        //String builderName = context.request().getParam("developer_name");
        String floorPlanId = context.request().getParam("floorPlanId");



        JsonObject params = new JsonObject().put("floorPlanId", floorPlanId);
        this.fetchPackageDetailsAndSend(context, "builder.select.packageDetails", params);
    }
    private void fetchPackageDetailsAndSend(RoutingContext context, String queryId, JsonObject paramsData){

        Integer id = LocalCache.getInstance().store(new QueryData(queryId, paramsData));
        LOG.info("Executing query:" + queryId + " | " + paramsData);
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (selectData == null || selectData.rows == null)
                    {
                        sendError(context, "Did not find products for " + paramsData.toString() + ". Error:" + selectData.errorMessage);
                    }
                    else
                    {
                        String result = selectData.rows.toString();
                        sendJsonResponse(context, result );

                        //                     context.response().putHeader("content-type", "application/json").end(selectData.getJsonDataRows("productJson").encodePrettily());

                    }
                });
    }
}
