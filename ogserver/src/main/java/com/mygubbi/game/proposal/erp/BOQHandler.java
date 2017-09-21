package com.mygubbi.game.proposal.erp;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import com.mygubbi.game.proposal.model.*;
import com.mygubbi.game.proposal.price.RateCardService;
import com.mygubbi.game.proposal.quote.AssembledProductInQuote;
import com.mygubbi.route.AbstractRouteHandler;
import com.mygubbi.si.gdrive.DriveFile;
import com.mygubbi.si.gdrive.DriveServiceProvider;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jooq.lambda.Seq;
import org.jooq.lambda.tuple.Tuple;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.jooq.lambda.tuple.Tuple.tuple;


public class BOQHandler extends AbstractRouteHandler {

    public static int[] planner_input = {15,16,17,18,19,20};
    public static int[] details = {20, 21 ,22, 23, 24, 25};

    private List<QueryData> updateQueries = new ArrayList<>();



    private String userId = null;

    private final static Logger LOG = LogManager.getLogger(BOQHandler.class);

    public DriveServiceProvider driveServiceProvider = new DriveServiceProvider();

    public BOQHandler(Vertx vertx) {
        super(vertx);
        this.route().handler(BodyHandler.create());
        this.post("/saveboqfile").handler(this::saveBoqFile);
        this.post("/createsoextract").handler(this::createsoextract);
    }

    private void saveBoqFile(RoutingContext routingContext)
    {
        JsonObject jsonObject = routingContext.getBodyAsJson();
        String file_id = jsonObject.getString("id");
        int proposalId = jsonObject.getInteger("proposalId");
        userId = jsonObject.getString("userId");
        LOG.debug("Save boq file :" + userId);
        String path = "D:/Mygubbi GAME/boq_downloaded.xlsx";
        this.driveServiceProvider = new DriveServiceProvider();
        this.driveServiceProvider.downloadFile(file_id, path, DriveServiceProvider.TYPE_XLS);
        writeToDB(routingContext,path,proposalId);
    }



    public void writeToDB(RoutingContext routingContext,String file, int proposalId)
    {

        try {
            FileInputStream newFile = new FileInputStream(new File(
                    file));
            XSSFWorkbook workbook = new XSSFWorkbook(newFile);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int noOfRows = sheet.getPhysicalNumberOfRows();
            LOG.debug("No of rows :" + noOfRows);
            for (int i = 3;i < sheet.getPhysicalNumberOfRows() ; i++)
            {
                XSSFRow xssfRow = sheet.getRow(i);
                LOG.debug("XSSF row get last cell num :" + xssfRow.getLastCellNum());
                LOG.debug("XSSF row num :" + xssfRow.getRowNum());

                if (xssfRow.getCell(details[0]).getStringCellValue().equals("") || xssfRow.getCell(details[1]).getStringCellValue().equals("") || xssfRow.getCell(details[2]).getStringCellValue().equals(""))
                {
                    LOG.debug("returning :" + xssfRow.getRowNum());

                }
                else {
                    ProposalBOQ proposal_boq = new ProposalBOQ();

                    proposal_boq.setSpaceType(xssfRow.getCell(details[0]).getStringCellValue());
                    proposal_boq.setRoom(xssfRow.getCell(details[1]).getStringCellValue());
                    proposal_boq.setProductService(xssfRow.getCell(details[2]).getStringCellValue());
                    proposal_boq.setMgCode(xssfRow.getCell(details[3]).getStringCellValue());
                    proposal_boq.setDSOErpItemCode(xssfRow.getCell(details[4]).getStringCellValue());

                    proposal_boq.setPlannerErpCode(xssfRow.getCell(planner_input[0]).getStringCellValue());
                    proposal_boq.setPlannerReferencePartNo(xssfRow.getCell(planner_input[1]).getStringCellValue());
                    proposal_boq.setPlannerDescription(xssfRow.getCell(planner_input[2]).getStringCellValue());
                    proposal_boq.setPlannerUom(xssfRow.getCell(planner_input[3]).getStringCellValue());
                    proposal_boq.setPlannerRate(Double.parseDouble(xssfRow.getCell(planner_input[4]).getStringCellValue()));
                    proposal_boq.setPlannerQty(Double.parseDouble(xssfRow.getCell(planner_input[5]).getStringCellValue()));
                    proposal_boq.setPlannerPrice(Double.parseDouble(xssfRow.getCell(planner_input[5]).getStringCellValue()));

                    proposal_boq.put("proposalId",proposalId);

                    LOG.debug("Adding query :" + proposal_boq);

                    updateQueries.add(new QueryData("proposal.boq.update",proposal_boq));
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
            JsonObject res = new JsonObject();
            res.put("status", "Failure");
            res.put("comments", "Could not update ");
            LOG.info(res.toString());
            sendJsonResponse(routingContext, res.toString());

        }
        updateSowRecords(routingContext,updateQueries);

    }

    private void updateSowRecords (RoutingContext routingContext,List<QueryData> queryDatas)
    {
        LOG.debug("Inside update sow records" + queryDatas.size());

        Integer id = LocalCache.getInstance().store(queryDatas);
        VertxInstance.get().eventBus().send(DatabaseService.MULTI_DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    List<QueryData> resultData = (List<QueryData>) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.get(0).errorFlag) {
                        LOG.error("Error in inserting line item in the audit table. " + resultData.get(0).errorMessage, resultData.get(0).error);
                        JsonObject res = new JsonObject();
                        res.put("status", "Failure");
                        res.put("comments", "Could not update ");
                        LOG.info(res.toString());
                        sendError(routingContext, res.toString());
                        return;
                    } else {
                        LOG.debug("Update queries executed successfully");
                        JsonObject res = new JsonObject();
                        res.put("status", "Success");
                        res.put("comments", "Successfully uploaded BOQ");
                        LOG.info(res.toString());
                        sendJsonResponse(routingContext, res.toString());
                    }
                });
    }

    private void createsoextract(RoutingContext routingContext)
    {
        JsonObject jsonObject = routingContext.getBodyAsJson();
        int proposalId = jsonObject.getInteger("proposalId");
        userId = jsonObject.getString("userId");
        Integer id = LocalCache.getInstance().store(new QueryData("proposal.boq.select.products.all",jsonObject));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,  new DeliveryOptions().setSendTimeout(120000),
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag || resultData.rows.size() == 0) {
                        LOG.error("Error in inserting line item in the audit table. " + resultData.errorMessage, resultData.error);
                        JsonObject res = new JsonObject();
                        res.put("status", "Failure");
                        res.put("comments", "Could not update ");
                        LOG.info(res.toString());
                        sendError(routingContext, res.toString());
                        return;
                    } else {
                        List<ProposalBOQ> proposal_boqs_products = new ArrayList<>();
                        List<ProposalBOQ> proposal_boqs_addons = new ArrayList<>();
                        List<ProposalBOQ> proposal_boqs_services = new ArrayList<>();

                        List<JsonObject> jsonObjects = resultData.rows;
                        for (JsonObject jsonObject1 : jsonObjects)
                        {
                            ProposalBOQ proposal_boq = new ProposalBOQ(jsonObject1);
                            if (proposal_boq.getcategory().equals("Modular Products"))
                            {
                                proposal_boqs_products.add(proposal_boq);
                            }
                            else if (proposal_boq.getcategory().equals("Services"))
                            {
                                proposal_boqs_services.add(proposal_boq);
                            }
                            else
                            {
                                proposal_boqs_addons.add(proposal_boq);
                            }

                        }

                       /* LOG.debug("Modular products size :" + proposal_boqs_products.size());
                        LOG.debug("Services size :" + proposal_boqs_services.size());
                        LOG.debug("Addons size :" + proposal_boqs_addons.size());*/

                        generateSo(routingContext, proposal_boqs_products,proposal_boqs_addons,proposal_boqs_services);

                    }
                });
    }

    private void generateSo(RoutingContext routingContext, List<ProposalBOQ> boqProductObjects, List<ProposalBOQ> boqAddonObjects, List<ProposalBOQ> boqServiceObjects) {

        DriveFile driveFile = null;
        List<String> outputFiles = new ArrayList<>();

        if (!(boqProductObjects.size() == 0))
        {
//            LOG.debug("generateSo:" + boqProductObjects.size());
            outputFiles = generateSoForProduct(boqProductObjects);
        }
//        LOG.debug("Output files size :" + outputFiles.size());
/*
        if (!(boqAddonObjects.size() == 0))
        {
            String outputFileForAddon = generateSoForAddon(routingContext,boqAddonObjects);
            outputFiles.add(outputFileForAddon);
        }
        if (!(boqServiceObjects.size() == 0))
        {
            String outputFileForService = generateSoForServices(routingContext,boqServiceObjects);
            outputFiles.add(outputFileForService);
        }*/

        try {
            LOG.debug("Before calling method :" + userId);
            driveFile = this.driveServiceProvider.createFolder(outputFiles,"BOQ",userId);
        } catch (Exception e) {

            LOG.debug("Exception newly found" + e.getMessage());

        }

        JsonObject res = new JsonObject();
        res.put("status", "Success");
        res.put("driveWebViewLink", driveFile.getWebViewLink());
        res.put("id", driveFile.getId());
        res.put("boqStatus","Yes");
        res.put("proposalId", boqProductObjects.get(0).getProposalId());
        LOG.info(res.toString());

        updateProposalHeader(routingContext,res);

    }

   /* private String generateSoForAddon(RoutingContext routingContext, List<ProposalBOQ> proposal_boqs_addons) {

        return new SOExtractTemplateCreator(proposal_boqs_addons).create();
    }

    private String generateSoForServices(RoutingContext routingContext, List<ProposalBOQ> proposal_boqs_addons) {

        return new SOExtractTemplateCreator(proposal_boqs_addons).create();
    }*/

    private List<String> generateSoForProduct(List<ProposalBOQ> boqProductObjects) {

//        LOG.debug("generateSoForProduct:" + boqProductObjects.size());
        Map<SpaceRoomProduct,List<ProposalBOQ>> spaceRoomProducts = getDistinctSpaceRoomProducts(boqProductObjects);

//        LOG.debug("Distinct space room product :" + spaceRoomProducts.size());

        List<String> outputFiles = new ArrayList<>();

        for (SpaceRoomProduct spaceRoomProduct : spaceRoomProducts.keySet()) {

            List<ProposalBOQ> proposalBoqAsPerProduct = spaceRoomProducts.get(spaceRoomProduct);
//            LOG.debug("Proposal BOQ as per product size : " + proposalBoqAsPerProduct.size() + " :" + proposalBoqAsPerProduct.get(0).getProductId());
            List<SOPart> soPartsList = new ArrayList<>();

            for (ProposalBOQ proposalBOQ : proposalBoqAsPerProduct) {
                SOPart soPart = new SOPart(proposalBOQ.getPlannerErpItemCode(), proposalBOQ.getPlannerReferencePartNo(), proposalBOQ.getPlannerUom(), proposalBOQ.getPlannerDescription(), proposalBOQ.getPlannerQty());
                soPartsList.add(soPart);
            }


            Map<String,SOPart> soPartMap = new HashMap<>();

            for (SOPart soPart : soPartsList) {

                if (soPartMap.containsKey(soPart.getErpCode()))
                {
                    SOPart soPart1 = soPartMap.get(soPart.getErpCode());
                    double qty = soPart1.getQty() + soPart.getQty();
                    soPart.setQty(qty);
                    soPartMap.put(soPart.getErpCode(),soPart);
                }
                else {
                    soPartMap.put(soPart.getErpCode(),soPart);
                }

            }



            String outputFile = new SOExtractTemplateCreator(soPartsList, spaceRoomProduct.getProductId(), proposalBoqAsPerProduct.get(0).getProposalId()).create();
            outputFiles.add(outputFile);
        }
        return outputFiles;
    }

    private void updateProposalHeader(RoutingContext routingContext, JsonObject res) {
        Integer id = LocalCache.getInstance().store(new QueryData("proposal.header.boqupdate", res));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id, new DeliveryOptions().setSendTimeout(120000),
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag || resultData.updateResult.getUpdated() == 0)
                    {
                        sendError(routingContext,"Could not update proposal header for " + res.getInteger("proposalId"));
                        LOG.error("Proposal not found for id:" + res.getInteger("proposalId"));
                    }
                    else
                    {
                        sendJsonResponse(routingContext,res.toString());
                    }
                });

    }


    private Map<SpaceRoomProduct,List<ProposalBOQ>> getDistinctSpaceRoomProducts(List<ProposalBOQ> proposalBoqs)
    {
        Map<SpaceRoomProduct, List<ProposalBOQ>> spaceRoomProductMap = new HashMap<>();
        for (ProposalBOQ boq : proposalBoqs)
        {
            SpaceRoomProduct spaceRoom = new SpaceRoomProduct(boq);
            if (!spaceRoomProductMap.containsKey(spaceRoom))
            {
                spaceRoomProductMap.put(spaceRoom,new ArrayList<>());
            }
            spaceRoomProductMap.get(spaceRoom).add(boq);
        }
        return spaceRoomProductMap;
    }



}






