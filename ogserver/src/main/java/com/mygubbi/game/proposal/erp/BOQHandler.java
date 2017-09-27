package com.mygubbi.game.proposal.erp;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.config.ConfigHolder;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import com.mygubbi.game.proposal.ModuleDataService;
import com.mygubbi.game.proposal.model.*;
import com.mygubbi.game.proposal.price.RateCardService;
import com.mygubbi.game.proposal.quote.AssembledProductInQuote;
import com.mygubbi.route.AbstractRouteHandler;
import com.mygubbi.si.excel.ExcelCellProcessor;
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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jooq.lambda.Seq;
import org.jooq.lambda.tuple.Tuple;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.jooq.lambda.tuple.Tuple.tuple;


public class BOQHandler extends AbstractRouteHandler{

    public static int[] planner_input = {17,18,19,20,21,22,23};
    public static int[] details = {24, 25 ,26, 27, 28, 29, 30, 31};

    private List<QueryData> updateQueries = new ArrayList<>();
    private List<QueryData> insertQueries = new ArrayList<>();



    private String userId = null;

    private final static Logger LOG = LogManager.getLogger(BOQHandler.class);

    public DriveServiceProvider driveServiceProvider = new DriveServiceProvider();

    public BOQHandler(Vertx vertx) {
        super(vertx);
        this.route().handler(BodyHandler.create());
        this.post("/saveboqfile").handler(this::saveBoqFile);
        this.post("/createsoextract").handler(this::generateSo);
    }

    private void saveBoqFile(RoutingContext routingContext)
    {
        JsonObject jsonObject = routingContext.getBodyAsJson();
        String file_id = jsonObject.getString("id");
        int proposalId = jsonObject.getInteger("proposalId");
        userId = jsonObject.getString("userId");
        String path = ConfigHolder.getInstance().getStringValue("boq_dir","/mnt/game/proposal/");
        path = path + proposalId +"/boq_downloaded.xlsx";
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
//                LOG.debug("XSSF row get last cell num :" + xssfRow.getLastCellNum());
//                LOG.debug("XSSF row num :" + xssfRow.getRowNum());

                String plannerErpCode = xssfRow.getCell(planner_input[0]).getStringCellValue();
                /*ERPMaster erpMaster = ModuleDataService.getInstance().getErpMaster(plannerErpCode);
                if (erpMaster == null)
                {
                    JsonObject res = new JsonObject();
                    res.put("status", "Failure");
                    res.put("comments", "Could not update ");
                    LOG.info(res.toString());
                    sendError(routingContext, res.toString());
                }
*/

                if (xssfRow.getCell(details[0]).getStringCellValue().equals("") || xssfRow.getCell(details[1]).getStringCellValue().equals("") )
                {
//                    LOG.debug("returning :" + xssfRow.getRowNum());

                }
                else {


                    ProposalBOQ proposal_boq = new ProposalBOQ();


                    proposal_boq.setSpaceType(xssfRow.getCell(details[0]).getStringCellValue());
                    proposal_boq.setRoom(xssfRow.getCell(details[1]).getStringCellValue());
                    proposal_boq.setCategory(xssfRow.getCell(details[2]).getStringCellValue());
                    proposal_boq.setProductService(xssfRow.getCell(details[3]).getStringCellValue());
                    proposal_boq.setProductId(Integer.parseInt(xssfRow.getCell(details[4]).getStringCellValue()));
                    proposal_boq.setModuleSeq(Integer.parseInt(xssfRow.getCell(details[5]).getStringCellValue()));
                    proposal_boq.setMgCode(xssfRow.getCell(details[6]).getStringCellValue());
                    proposal_boq.setDSOErpItemCode(xssfRow.getCell(details[7]).getStringCellValue());

                    proposal_boq.setPlannerErpCode(plannerErpCode);
                    proposal_boq.setPlannerReferencePartNo(xssfRow.getCell(planner_input[1]).getStringCellValue());
                    proposal_boq.setPlannerDescription(xssfRow.getCell(planner_input[2]).getStringCellValue());
                    proposal_boq.setPlannerUom(xssfRow.getCell(planner_input[3]).getStringCellValue());
                    proposal_boq.setPlannerRate(Double.parseDouble(xssfRow.getCell(planner_input[4]).getRawValue()));
                    proposal_boq.setPlannerQty(Double.parseDouble((xssfRow.getCell(planner_input[5]).getRawValue())));
                    proposal_boq.setPlannerPrice(Double.parseDouble(xssfRow.getCell(planner_input[6]).getRawValue()));

                    proposal_boq.put("proposalId",proposalId);

//                    LOG.debug("Adding query :" + proposal_boq);

                    updateQueries.add(new QueryData("proposal.boq.update",proposal_boq));
                }
                LOG.debug("first row value : " + xssfRow.getCell(0).getStringCellValue());


            }

            testmethod(proposalId, sheet);


        } catch (IOException e) {
            e.printStackTrace();
            JsonObject res = new JsonObject();
            res.put("status", "Failure");
            res.put("comments", "Could not update ");
            LOG.info(res.toString());
            sendJsonResponse(routingContext, res.toString());

        }
        List<QueryData> totalQueryDatas = new ArrayList<>();
        totalQueryDatas.addAll(updateQueries);
        totalQueryDatas.addAll(insertQueries);

        updateSowRecords(routingContext,totalQueryDatas);

    }

    private void testmethod(int proposalId, XSSFSheet sheet) {

        int currentRow = 0;

        for (int e = 0; e < sheet.getPhysicalNumberOfRows() ; e ++)
        {
            XSSFRow xssfRowTest = sheet.getRow(e);
            if (xssfRowTest.getCell(0).getStringCellValue().equals("ADDITIONAL ITEMS"))
            {
                LOG.debug("Setting current row : " + e+1);
                currentRow = e +1;

            }

        }

        LOG.debug("Current row : " + currentRow);

            for (int j = currentRow;j < sheet.getPhysicalNumberOfRows() ; j++) {
                XSSFRow xssfRow = sheet.getRow(j);


                if (xssfRow.getCell(0).getStringCellValue().equals("") || xssfRow.getCell(0).getStringCellValue().equals("") ) {
//                    LOG.debug("returning :" + xssfRow.getRowNum());
                    LOG.debug("Returning for row : " + j);
                } else {
                    LOG.debug("Additional items");
                    ProposalBOQ proposal_boq = new ProposalBOQ();

                    proposal_boq.setProposalId(proposalId);
                    proposal_boq.setSpaceType(xssfRow.getCell(0).getStringCellValue());
                    proposal_boq.setRoom(xssfRow.getCell(1).getStringCellValue());
                    proposal_boq.setCategory("Modular Products");
                    proposal_boq.setProductId(Integer.parseInt(xssfRow.getCell(3).getStringCellValue()));
                    proposal_boq.setProductService(xssfRow.getCell(4).getStringCellValue());
                    proposal_boq.setModuleSeq(Integer.parseInt(xssfRow.getCell(5).getStringCellValue()));
                    String stringCellValue = xssfRow.getCell(6).getStringCellValue();
                    String[] split = stringCellValue.split(":");

                    proposal_boq.setMgCode(split[1]);
                    proposal_boq.setCustomCheck("");
                    proposal_boq.setCustomRemarks("");
                    proposal_boq.setItemCategory("");

                    proposal_boq.setDSOErpItemCode("");
                    proposal_boq.setDSOItemSeq(0);
                    proposal_boq.setDSOReferencePartNo("");
                    proposal_boq.setDSODescription("");
                    proposal_boq.setDSOUom("");
                    proposal_boq.setDSORate(0);
                    proposal_boq.setDSOQty(0);
                    proposal_boq.setDSOPrice(0);

                    proposal_boq.setPlannerErpCode(xssfRow.getCell(planner_input[0]).getStringCellValue());
                    proposal_boq.setPlannerReferencePartNo(xssfRow.getCell(planner_input[1]).getStringCellValue());
                    proposal_boq.setPlannerDescription(xssfRow.getCell(planner_input[2]).getStringCellValue());
                    proposal_boq.setPlannerUom(xssfRow.getCell(planner_input[3]).getStringCellValue());
                    proposal_boq.setPlannerRate(Double.parseDouble(xssfRow.getCell(planner_input[4]).getStringCellValue()));
                    proposal_boq.setPlannerQty(Double.parseDouble(xssfRow.getCell(planner_input[5]).getStringCellValue()));
                    proposal_boq.setPlannerPrice(Double.parseDouble(xssfRow.getCell(planner_input[6]).getStringCellValue()));


                    insertQueries.add(new QueryData("proposal.boq.create", proposal_boq));

                }
            }
    }

    private void updateSowRecords (RoutingContext routingContext,List<QueryData> queryDatasForUpdate)
    {
//        LOG.debug("Inside update sow records" + queryDatasForUpdate.size());

        Integer id = LocalCache.getInstance().store(queryDatasForUpdate);
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
//                        LOG.debug("Update queries executed successfully");
                        JsonObject res = new JsonObject();
                        res.put("status", "Success");
                        res.put("comments", "Successfully uploaded BOQ");
//                        LOG.info(res.toString());
                        sendJsonResponse(routingContext, res.toString());
                    }
                });
    }



    private void generateSo(RoutingContext routingContext)
    {
            int count = 0;
            JsonObject quoteRequestJson = routingContext.getBodyAsJson();
            Integer id = LocalCache.getInstance().store(quoteRequestJson);
            VertxInstance.get().eventBus().send(SOCreatorService.CREATE_SO_OUTPUT, id,  new DeliveryOptions().setSendTimeout(120000),
                    (AsyncResult<Message<Integer>> result) -> {
                        JsonObject response = (JsonObject) LocalCache.getInstance().remove(result.result().body());
                        LOG.debug("Response after creating so extract :" + response);
                        sendJsonResponse(routingContext, response.toString());
                    });
    }

}






