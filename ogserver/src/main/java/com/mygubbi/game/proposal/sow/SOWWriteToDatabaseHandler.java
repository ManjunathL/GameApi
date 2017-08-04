package com.mygubbi.game.proposal.sow;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.config.ConfigHolder;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import com.mygubbi.game.proposal.model.Proposal;
import com.mygubbi.game.proposal.model.ProposalHeader;
import com.mygubbi.game.proposal.model.ProposalSOW;
import com.mygubbi.route.AbstractRouteHandler;
import com.mygubbi.si.gdrive.DriveServiceProvider;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by User on 10-07-2017.
 */
public class SOWWriteToDatabaseHandler  extends AbstractRouteHandler {

    private static int[] cell_Services = {3,5,7,9,11,13,15};
    private static int[] cell_Services_titleText = {2,4,7,9,11,13,15};
    private List<Integer> cell_Services_title = new ArrayList<Integer>();


    private final static Logger LOG = LogManager.getLogger(SOWWriteToDatabaseHandler.class);

    public DriveServiceProvider serviceProvider;

    public SOWWriteToDatabaseHandler(Vertx vertx) {
        super(vertx);
        this.route().handler(BodyHandler.create());
        this.post("/savesowfile").handler(this::saveSowFile);
    }

    private void saveSowFile(RoutingContext routingContext)
    {
        JsonObject jsonObject = routingContext.getBodyAsJson();
        LOG.debug("Json object :0 " + jsonObject);
        String file_id = jsonObject.getString("id");
        int proposalId = jsonObject.getInteger("proposalId");
        String version = jsonObject.getString("version");
        String path = ConfigHolder.getInstance().getStringValue("proposal_docs_folder","/mnt/game/proposal")+
                "/"+proposalId+"/"+ConfigHolder.getInstance().getStringValue("sow_downloaded_xls_format","sow.xlsx");//"D:/Mygubbi GAME/sow_downloaded.xlsx";
        this.serviceProvider = new DriveServiceProvider();
        this.serviceProvider.downloadFile(file_id, path, DriveServiceProvider.TYPE_XLS);
//        this.sowWriteToDatabase = new SOWWriteToDatabase();
        writeToDB(routingContext,path,proposalId,version);
//        sendJsonResponse(routingContext,jsonObject.toString());
    }

    public void writeToDB(RoutingContext context,String file, int proposalId, String version)
    {


        Integer id = LocalCache.getInstance().store(new QueryData("proposal.header", new JsonObject().put("id",proposalId)));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag)
                    {
                        JsonObject res = new JsonObject();
                        res.put("status","Failure");
                        res.put("comments",resultData.errorMessage);
                        LOG.error("Error in getting  proposal header. " + resultData.errorMessage, resultData.error);
                        LOG.info(res.toString());
                        sendJsonResponse(context,res.toString());
                        return;

                    }
                    else {
                        JsonObject proposalHeader = resultData.rows.get(0);
                        readFromExcelAndWriteToDB(context,file,proposalId,version,resultData.rows.get(0));

                    }
                });

}

    private void readFromExcelAndWriteToDB(RoutingContext context,String file, int proposalId, String version, JsonObject proposalHeader) {
        try {

           FileInputStream newFile = new FileInputStream(new File(
                    file));
            XSSFWorkbook workbook = new XSSFWorkbook(newFile);
            XSSFSheet sheet = workbook.getSheetAt(0);

            XSSFRow xssfRow_remarks = sheet.getRow(1);
            XSSFCell xssfCell_remarks = xssfRow_remarks.getCell(7);
            String remarks = xssfCell_remarks.getStringCellValue();
            String remarks_raw = xssfCell_remarks.getRawValue();
            LOG.debug("Remarks total :" + remarks);
            LOG.debug("Remarks raw :" + remarks_raw);
            if (version.equals("1.0"))
            {
                proposalHeader.put("newsowremarks",remarks);
                updateProposalHeader(context,proposalHeader, proposalId,version,sheet);

            }
            else
            {
                proposalHeader.put("newsowremarks",remarks);
                updateProposalHeader(context,proposalHeader,proposalId, version,sheet);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void updateSOwRecord(RoutingContext context,List<QueryData> queryDatas)
    {
        Integer id = LocalCache.getInstance().store(queryDatas);
        VertxInstance.get().eventBus().send(DatabaseService.MULTI_DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    List<QueryData> resultData = (List<QueryData>) LocalCache.getInstance().remove(selectResult.result().body());

                    boolean updateSuccess = true;
                    for(int i=0;i<resultData.size();i++) {
                        if (resultData.get(i).errorFlag) {
                            updateSuccess = false;
                        }
                    }

                    if(!updateSuccess){
                        JsonObject res = new JsonObject();
                        res.put("status", "Failure");
                        res.put("comments", "Error in inserting line item in the audit table.");
                        LOG.info(res.toString());
                        sendJsonResponse(context, res.toString());
                        return;
                    }else {
                        LOG.info("Inserted sow Record to DB Successfully.");
                        JsonObject res = new JsonObject();
                        res.put("status", "Success");
                        res.put("comments", "Successfully uploaded scope of services.");
                        LOG.info(res.toString());
                        sendJsonResponse(context, res.toString());
                    }
                });
    }

    private void updateProposalHeader(RoutingContext context,JsonObject proposalHeaderObject, int proposalId,String sowVersion, XSSFSheet sheet)
    {
        List<String> service_Combo_val = new ArrayList<>();

        service_Combo_val.add("Mygubbi");
        service_Combo_val.add("Client");
        service_Combo_val.add("NA");


        String query;
        if (sowVersion.equals("1.0"))
        {
            query = "proposal.header.updatesowremarksv1";
        }
        else
        {
            query = "proposal.header.updatesowremarksv2";
        }
        Integer id = LocalCache.getInstance().store(new QueryData(query, proposalHeaderObject));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag || resultData.updateResult.getUpdated() == 0) {

                        JsonObject res = new JsonObject();
                        res.put("status","Failure");
                        res.put("comments","Error in updating remarks in the proposal table "+ resultData.errorMessage);
                        LOG.info(res.toString());
                        sendJsonResponse(context,res.toString());
                        return;
                    } else
                    {
                        String spaceType = null;
                        String L1s01code = null;
                        String room = null;
                        int noOfRows = sheet.getLastRowNum();
                        LOG.debug("No of rows :" + noOfRows);
                        List<QueryData> queryDatas =new ArrayList<>();
                        String db_query = "proposal.sow.update";

                        for (int i = 3; i < noOfRows; i++) {
                            XSSFRow xssfRow = sheet.getRow(i);


                            int count = 0;
                            Boolean isServiceMadeTrue = false;
                            List<String> services_value = new ArrayList<>();
                            cell_Services_title.add(2);
                            cell_Services_title.add(4);
                            cell_Services_title.add(6);
                            cell_Services_title.add(8);
                            cell_Services_title.add(10);
                            cell_Services_title.add(12);
                            cell_Services_title.add(14);


                            if (!(xssfRow.getCell(2).getStringCellValue().equals("") || xssfRow.getCell(2).getStringCellValue().isEmpty())) {
                                for (Integer services_cell : cell_Services) {
                                    XSSFCell xssfCell = xssfRow.getCell(services_cell);
                                    String first_level_service = xssfRow.getCell(3).getStringCellValue();
                                    LOG.info("first_level_service = " + first_level_service);
                                    if (!(xssfRow.getCell(0).getStringCellValue().equals("")) || xssfRow.getCell(0).getStringCellValue().isEmpty()) {
                                        spaceType = xssfRow.getCell(16).getStringCellValue();
                                        room = xssfRow.getCell(17).getStringCellValue();
                                        L1s01code = xssfRow.getCell(18).getStringCellValue();
                                    }

                                    if (first_level_service.length() == 0) {
                                        JsonObject res = new JsonObject();
                                        res.put("status", "Failure");
                                        res.put("comments", "Its mandatory to answer all the basic (level 1) questions.");
                                        LOG.info(res.toString());
                                        sendJsonResponse(context, res.toString());
                                        return;
                                    }


                                    if (!(xssfCell == null)) {
                                        services_value.add(count,xssfCell.getStringCellValue());


                                        LOG.info("IS service is True ?? = " + isServiceMadeTrue);
                                        if ((count == 0) && (services_value.get(count).equalsIgnoreCase("Yes"))) {
                                            //set service flag to true
                                            isServiceMadeTrue = true;

                                        }
                                        if (isServiceMadeTrue && count > 0) {

                                            LOG.info("COUNT = "+count+",services_value.get(count) == "+services_value.get(count));

                                            XSSFCell xssfCell1Text = xssfRow.getCell(cell_Services_title.get(count));



                                            if (services_value.get(count).equals("") && !xssfCell1Text.getStringCellValue().equals("")) {
                                                JsonObject res = new JsonObject();
                                                res.put("status", "Failure");
                                                res.put("comments", "Please answer all the related services -" + xssfRow.getCell(2).getStringCellValue());
                                                LOG.info(res.toString());
                                                sendJsonResponse(context, res.toString());
                                                return;
                                            }
                                            if (service_Combo_val.contains(services_value.get(count))) {
                                                System.out.println("Services string value :" + count + " : " + services_value.get(count));

                                            } else {
                                                if(services_value.get(count).length() > 0) {
                                                    JsonObject res = new JsonObject();
                                                    res.put("status", "Failure");
                                                    res.put("comments", "Please answer level 2 questions for the selected level 1 question -" + xssfRow.getCell(2).getStringCellValue());
                                                    LOG.info(res.toString());
                                                    sendJsonResponse(context, res.toString());
                                                    return;
                                                }
                                            }

                                        }
                                        count = count + 1;

                                    }
                                }

                                //check any of the level 2 services selected is mygubbi
                                boolean isGubbi = false;
                                LOG.info("services_value.size() = "+services_value.size());
                                for (int index = 0; index < services_value.size(); index++) {
                                    LOG.info("In services_value[index] = "+services_value.get(index)+", "+service_Combo_val.get(0));
                                    if ((services_value.get(index).equalsIgnoreCase(service_Combo_val.get(0)))&&
                                            (services_value.get(0).equalsIgnoreCase("Yes"))) {
                                        isGubbi = true;
                                    }
                                }
                                LOG.info("isGubbi - "+isGubbi);
                                if (!isGubbi && (services_value.get(0).equalsIgnoreCase("Yes"))) {
                                    JsonObject res = new JsonObject();
                                    res.put("status", "Failure");
                                    res.put("comments", "Atleast one of the level 2 questions should have Mygubbi as the scope for the chosen level 1 question - " + xssfRow.getCell(2).getStringCellValue());
                                    LOG.info(res.toString());
                                    sendJsonResponse(context, res.toString());
                                    return;
                                }else {
                                    ProposalSOW proposal_sow = new ProposalSOW();
                                    proposal_sow.setProposalId(proposalId);
                                    proposal_sow.setVersion(sowVersion);
                                    proposal_sow.setSpaceType(spaceType);
                                    proposal_sow.setRoom(room);
                                    proposal_sow.setL1S01(services_value.get(0));
                                    if (proposal_sow.getL1S01().equals("No"))
                                    {
                                        proposal_sow.setL2S01("");
                                        proposal_sow.setL2S02("");
                                        proposal_sow.setL2S03("");
                                        proposal_sow.setL2S04("");
                                        proposal_sow.setL2S05("");
                                        proposal_sow.setL2S06("");
                                    }
                                    else {
                                        proposal_sow.setL2S01(services_value.get(1));
                                        proposal_sow.setL2S02(services_value.get(2));
                                        proposal_sow.setL2S03(services_value.get(3));
                                        proposal_sow.setL2S04(services_value.get(4));
                                        proposal_sow.setL2S05(services_value.get(5));
                                        proposal_sow.setL2S06(services_value.get(6));
                                    }
                                    proposal_sow.setL1S01Code(L1s01code);


                    /*updateSOwRecord(proposal_sow);*/
                                    System.out.println("Proposal_sow :" + proposal_sow.toString());

                                    queryDatas.add(new QueryData(db_query,proposal_sow));
                                }
                            }
                        }

                        updateSOwRecord(context,queryDatas);
                    }

                });

    }
}
