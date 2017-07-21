package com.mygubbi.game.proposal.erp;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import com.mygubbi.game.proposal.model.ProposalSOW;
import io.vertx.core.AsyncResult;
import io.vertx.core.eventbus.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;




public class BOQWriteToDatabase {

    public static int[] planner_input = {14,15,16,17,18};
    public static int[] dso_input = {8, 9,10,11,12,13};
    public static int[] details = {19, 20 ,21, 22, 23};

    private final static Logger LOG = LogManager.getLogger(BOQWriteToDatabase.class);


    public void writeToDB(String file, int proposalId)
    {

        try {
            FileInputStream newFile = new FileInputStream(new File(
                    file));
            XSSFWorkbook workbook = new XSSFWorkbook(newFile);
            XSSFSheet sheet = workbook.getSheetAt(0);
            String spaceType = null;
            String product = null;
            String room =  null;
            int noOfRows = sheet.getLastRowNum();
            LOG.debug("No of rows :" + noOfRows);
            for (int i = 3;i < noOfRows ; i++)
            {
                XSSFRow xssfRow = sheet.getRow(i);

                if (!(xssfRow.getCell(0).getStringCellValue().equals("")) || xssfRow.getCell(0).getStringCellValue().isEmpty())
                {
                    spaceType = xssfRow.getCell(0).getStringCellValue();
                    product = xssfRow.getCell(1).getStringCellValue();
                    room = xssfRow.getCell(2).getStringCellValue();
                }
                int count = 0;
                String[] services_value = new String[128];


                /*if (!(xssfRow.getCell(3).getStringCellValue().equals("") || xssfRow.getCell(3).getStringCellValue().isEmpty()))
                {
for(Integer services_cell : 5)
                    {
                        //XSSFCell xssfCell = xssfRow.getCell(services_cell);


                        if (!(xssfCell == null))
                        {
                            services_value[count] = xssfCell.getStringCellValue();
                            System.out.println("Services string value :" + count  + " : " + services_value[count] );
                            count = count + 1;
                        }
                    }

                    ProposalSOW proposal_sow = new ProposalSOW();
                    proposal_sow.setProposalId(proposalId);
                    proposal_sow.setVersion(1.0);
                    proposal_sow.setSpaceType(spaceType);
                    proposal_sow.setProductId(1);
                    proposal_sow.setProduct(product);
                    proposal_sow.setRoom(room);
                    proposal_sow.setL1S01(services_value[0]);
                    proposal_sow.setL2S01(services_value[1]);
                    proposal_sow.setL2S02(services_value[2]);
                    proposal_sow.setL2S03(services_value[3]);
                    proposal_sow.setL2S04(services_value[4]);
                    proposal_sow.setL2S05(services_value[5]);
                    proposal_sow.setL2S06(services_value[6]);


createSowRecord(proposal_sow);

                    System.out.println("Proposal_sow :" + proposal_sow.toString());

 Connection con = DriverManager.getConnection(
                        "jdbc:mysql://ogdemodb.cyn8wqrk6sdc.ap-southeast-1.rds.amazonaws.com/mg_report","admin", "OG$#gubi32");

                int insert = con.createStatement().executeUpdate("INSERT INTO proposal_sow (proposalId, version, spaceType, productId, product, room," +
                        " L1S01,L2S01,L2S02,L2S03,L2S04,L2S05,L2S06,L2S07 ) VALUES (" +
                        + proposal_sow.getProposalId() + "," + proposal_sow.getVERSION() + "," + "'" + proposal_sow.getSpaceType() + "'" + ","
                        + proposal_sow.getProductId()  + "," + "'" + proposal_sow.getPRODUCT() + "'" + "," + "'" + proposal_sow.getROOM() + "'" + "," + "'" +
                        proposal_sow.getL1S01() + "'" + "," + "'" + proposal_sow.getL2S01() + "'" + "," + "'" + proposal_sow.getL2S02() +"'" + "," + "'" +
                        proposal_sow.getL2S03() + "'" + "," + "'" + proposal_sow.getL2S04() + "'" + "," + "'" + proposal_sow.getL2S05() +"'" + "," + "'" +
                        proposal_sow.getL2S06() + "'" + "," + "'"+ proposal_sow.getL2S07() + "'"+ ")");



                    createSowRecord(proposal_sow);
                }*/
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void createSowRecord (ProposalSOW proposal_sow)
    {
        String query = "proposal.sow.create";
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
}
