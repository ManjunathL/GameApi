package com.mygubbi.game.proposal.sow;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import com.mygubbi.game.proposal.model.Proposal;
import com.mygubbi.game.proposal.model.ProposalHeader;
import com.mygubbi.game.proposal.model.ProposalSOW;
import io.vertx.core.AsyncResult;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by User on 10-07-2017.
 */
public class SOWWriteToDatabase {

    private static int[] cell_Services = {3,5,7,9,11,13,15};

    private final static Logger LOG = LogManager.getLogger(SOWWriteToDatabase.class);


    public void writeToDB(String file, int proposalId, String version)
    {

        Integer id = LocalCache.getInstance().store(new QueryData("proposal.header", new JsonObject().put("id",proposalId)));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag)
                    {
                        LOG.error("Error in getting  proposal header. " + resultData.errorMessage, resultData.error);
                    }
                    else {
                        JsonObject proposalHeader = resultData.rows.get(0);
                        readFromExcelAndWriteToDB(file, proposalId, version, proposalHeader);

                    }
                });



    }

    private void readFromExcelAndWriteToDB(String file, int proposalId, String version, JsonObject proposalHeader) {
        try {
            FileInputStream newFile = new FileInputStream(new File(
                    file));
            XSSFWorkbook workbook = new XSSFWorkbook(newFile);
            XSSFSheet sheet = workbook.getSheetAt(0);
            String spaceType = null;
            String L1s01code = null;
            String room =  null;
            int noOfRows = sheet.getLastRowNum();
            LOG.debug("No of rows :" + noOfRows);

            XSSFRow xssfRow_remarks = sheet.getRow(1);
            XSSFCell xssfCell_remarks = xssfRow_remarks.getCell(7);
            String remarks = xssfCell_remarks.getStringCellValue();
            String remarks_raw = xssfCell_remarks.getRawValue();
            LOG.debug("Remarks total :" + remarks);
            LOG.debug("Remarks raw :" + remarks_raw);
            if (version.equals("1.0"))
            {
                proposalHeader.put("newsowremarks",remarks);
                updateProposalHeader(proposalHeader, version);

            }
            else
            {
                proposalHeader.put("newsowremarks",remarks);
                updateProposalHeader(proposalHeader, version);
            }

            for (int i = 3;i < noOfRows ; i++)
            {
                XSSFRow xssfRow = sheet.getRow(i);


                int count = 0;
                String[] services_value = new String[128];


                if (!(xssfRow.getCell(2).getStringCellValue().equals("") || xssfRow.getCell(2).getStringCellValue().isEmpty()))
                {
                    for(Integer services_cell : cell_Services)
                    {
                        XSSFCell xssfCell = xssfRow.getCell(services_cell);

                        if (!(xssfRow.getCell(0).getStringCellValue().equals("")) || xssfRow.getCell(0).getStringCellValue().isEmpty())
                        {
                            spaceType = xssfRow.getCell(16).getStringCellValue();
                            room = xssfRow.getCell(17).getStringCellValue();
                            L1s01code = xssfRow.getCell(18).getStringCellValue();
                        }


                        if (!(xssfCell == null))
                        {
                            services_value[count] = xssfCell.getStringCellValue();
                            System.out.println("Services string value :" + count  + " : " + services_value[count] );
                            count = count + 1;
                        }
                    }
                    ProposalSOW proposal_sow = new ProposalSOW();
                    proposal_sow.setProposalId(proposalId);
                    proposal_sow.setVersion(version);
                    proposal_sow.setSpaceType(spaceType);
                    proposal_sow.setRoom(room);
                    proposal_sow.setL1S01(services_value[0]);
                    proposal_sow.setL2S01(services_value[1]);
                    proposal_sow.setL2S02(services_value[2]);
                    proposal_sow.setL2S03(services_value[3]);
                    proposal_sow.setL2S04(services_value[4]);
                    proposal_sow.setL2S05(services_value[5]);
                    proposal_sow.setL2S06(services_value[6]);
                    proposal_sow.setL1S01Code(L1s01code);


                    /*updateSOwRecord(proposal_sow);*/
                    System.out.println("Proposal_sow :" + proposal_sow.toString());

                    updateSOwRecord(proposal_sow);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateSOwRecord(ProposalSOW proposal_sow)
    {
        String query = "proposal.sow.update";
        Integer id = LocalCache.getInstance().store(new QueryData(query, proposal_sow));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag || resultData.updateResult.getUpdated() == 0) {
                        LOG.error("Error in inserting line item in the audit table. " + resultData.errorMessage, resultData.error);
                        return;
                    } else {
                        LOG.info("Inserted sow Record " + proposal_sow.toString());
                    }
                });
    }

    private void updateProposalHeader(JsonObject proposalHeaderObject, String sowVersion)
    {
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
                        LOG.error("Error in updating remarks in the proposal table " + resultData.errorMessage, resultData.error);
                        return;
                    } else {
                        LOG.info("Updated remarks for proposal " + proposalHeaderObject.toString());
                    }
                });
    }
}
