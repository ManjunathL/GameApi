/*
package com.mygubbi.game.proposal.erp;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.StringUtils;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import com.mygubbi.game.proposal.model.ProposalBOQ;
import com.mygubbi.si.excel.ExcelCellProcessor;
import com.mygubbi.si.excel.ExcelSheetProcessor;
import io.vertx.core.AsyncResult;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

*/
/**
 * Created by User on 26-09-2017.
 *//*

public class SaveBoqLineItems implements ExcelCellProcessor {

    public static int[] planner_input = {17,18,19,20,21,22};
    public static int[] details = {22, 23 ,24, 25, 26, 27};



    private ExcelSheetProcessor sheetProcessor;
    @Override
    public Object getValueForKey(String key) {
        return null;
    }

    @Override
    public void processCell(Cell cell, String cellValue) {
        switch (cellValue)
        {
            case "Space Type":
                //int currentRow = this.fillAssembledProducts(cell.getRow().getRowNum());
                this.fillModules(cell.getRow().getRowNum() + 1);
                break;

            case "ADDONS":
                this.fillAddons(cell.getRow().getRowNum() + 2);
                break;

            default:
                break;
        }
    }

    public void writeToDB(RoutingContext routingContext, String file, int proposalId)
    {

        try {
            FileInputStream newFile = new FileInputStream(new File(
                    file));
            XSSFWorkbook workbook = new XSSFWorkbook(newFile);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int noOfRows = sheet.getPhysicalNumberOfRows();
//            LOG.debug("No of rows :" + noOfRows);
            for (int i = 3;i < sheet.getPhysicalNumberOfRows() ; i++)
            {
                XSSFRow xssfRow = sheet.getRow(i);
//                LOG.debug("XSSF row get last cell num :" + xssfRow.getLastCellNum());
//                LOG.debug("XSSF row num :" + xssfRow.getRowNum());

                if (xssfRow.getCell(details[0]).getStringCellValue().equals("") || xssfRow.getCell(details[1]).getStringCellValue().equals("") || xssfRow.getCell(details[2]).getStringCellValue().equals(""))
                {
//                    LOG.debug("returning :" + xssfRow.getRowNum());

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

//                    LOG.debug("Adding query :" + proposal_boq);

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
//        LOG.debug("Inside update sow records" + queryDatas.size());

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




}
*/
