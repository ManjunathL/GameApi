/*
package com.mygubbi.game.proposal;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

import com.mygubbi.game.proposal.model.ProposalSOW;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.*;



public class CopyToNewSheet {

    public static int[] kitchen_rows = { 0, 1,2,3,4, 5, 6,7 };
    public static int[] utility_rows = {0, 1,2,3,4, 5, 6,7 ,8, 9,10,11};
    public static int[] cell_Services = {4,6,8,10,12,14,16};
    public static final String DINING_ROWS = "{ 12, 13,14,15}";

    private final static Logger LOG = LogManager.getLogger(CopyToNewSheet.class);




    public static void main(String[] args) {
            try {
                List<String> rooms = new ArrayList<>();
                rooms.add("Kitchen");
                rooms.add("Wardrobe");
                rooms.add("Utility");
                int[] rows = new int[10000];
                int row_size = 0;
                // excel files
                FileInputStream excellFile1 = new FileInputStream(new File(
                        "D:\\Mygubbi GAME\\TestNew.xlsx"));

                // input row numbers and column numbers
                int[] icols = { 0, 1, 2, 3,4,5,6,7,8,9,10,11,12,13,14,15,16 };

                // Create Workbook instance holding reference to .xlsx file
                XSSFWorkbook workbook = new XSSFWorkbook(excellFile1);

                // Get first/desired sheet from the workbook
                XSSFSheet sheet = workbook.getSheetAt(0);

                // add sheet2 to sheet1
                XSSFWorkbook outWorkbook = null;
                for (String productCategory : rooms)
                {
                    if (productCategory.equals("Kitchen"))
                    {


                        for (int i=0; i < kitchen_rows.length ; i++)
                        {
                            System.out.println("rows :" + rows[i] + ":" + kitchen_rows[i]);

                            rows[i] = kitchen_rows[i];
                        }
                        row_size = sheet.getLastRowNum();
                        System.out.println("Row size :" + row_size);
                        outWorkbook = getFilteredWorkBook(sheet,kitchen_rows,icols);
                    }
                    if (productCategory.equals("Utility"))
                    {
                        for (int i = 0 ; i< utility_rows.length ; i++)
                        {
                            rows[row_size+i] = utility_rows[i]  ;
                        }
                        outWorkbook = getFilteredWorkBook(sheet,utility_rows,icols);
                    }
                }



                excellFile1.close();

                // save merged file
                File outFile = new File("D:\\Mygubbi GAME\\TestCopy12.xlsx");
                if (!outFile.exists()) {
                    outFile.createNewFile();
                }
                FileOutputStream out = new FileOutputStream(outFile);
                outWorkbook.write(out);
                out.close();
                System.out.println("Files were merged succussfully");

                readValuesFromExcel();


            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        private static XSSFWorkbook getFilteredWorkBook(XSSFSheet sheet,
                                                        int[] irows, int[] icols) {
            // create New workbook
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet outSheet = workbook.createSheet();
            Map<Integer, XSSFCellStyle> styleMap = new HashMap<Integer, XSSFCellStyle>();
            int i = 0;

            // get rows with given row numbers
            for (int rowNum : irows) {
                if (rowNum >= sheet.getFirstRowNum()
                        && rowNum <= sheet.getLastRowNum()) {

                    // create new row
                    XSSFRow outRow = outSheet.createRow(i);
                    XSSFRow row = sheet.getRow(rowNum);
                    int j = 0;

                    // get columns with given column numbers
                    for (int colNum : icols) {
                        if (colNum >= sheet.getRow(0).getFirstCellNum()
                                && colNum <= sheet.getRow(0).getLastCellNum()) {

                            // create new column
                            XSSFCell outCell = outRow.createCell(j);
                            XSSFCell cell = row.getCell(colNum);
                            if (cell != null) {
                                j++;
                                if (cell.getSheet().getWorkbook() == outCell
                                        .getSheet().getWorkbook()) {
                                    outCell.setCellStyle(cell.getCellStyle());
                                } else {
                                    int stHashCode = cell.getCellStyle().hashCode();
                                    XSSFCellStyle newCellStyle = styleMap
                                            .get(stHashCode);
                                    if (newCellStyle == null) {
                                        newCellStyle = outCell.getSheet()
                                                .getWorkbook().createCellStyle();
                                        newCellStyle.cloneStyleFrom(cell
                                                .getCellStyle());


                                        styleMap.put(stHashCode, newCellStyle);
                                    }
                                    outCell.setCellStyle(newCellStyle);

                                }

                                switch (cell.getCellType()) {
                                    case XSSFCell.CELL_TYPE_FORMULA:
                                        outCell.setCellFormula(cell.getCellFormula());
                                        break;
                                    case XSSFCell.CELL_TYPE_NUMERIC:
                                        outCell.setCellValue(cell.getNumericCellValue());
                                        break;
                                    case XSSFCell.CELL_TYPE_STRING:
                                        outCell.setCellValue(cell.getStringCellValue());
                                        break;
                                    case XSSFCell.CELL_TYPE_BLANK:
                                        outCell.setCellType(XSSFCell.CELL_TYPE_BLANK);
                                        break;
                                    case XSSFCell.CELL_TYPE_BOOLEAN:
                                        outCell.setCellValue(cell.getBooleanCellValue());
                                        break;
                                    case XSSFCell.CELL_TYPE_ERROR:
                                        outCell.setCellErrorValue(cell
                                                .getErrorCellValue());
                                        break;
                                    default:
                                        outCell.setCellValue(cell.getStringCellValue());
                                        break;
                                }

                            }

                        }
                    }

                    i++;

                    int sheetRows = outSheet.getLastRowNum();
                    int noOfCols = row.getLastCellNum();

                    for (int m=0; m < sheetRows; m++)
                    {
                        for (int n=0; n<noOfCols; n++)
                        {
                            if (outRow.getCell(n).getStringCellValue().startsWith("L2S")) {
                                createDropDownListForLevel1(outSheet,m,n,"col");
                            }
                    }
                        createDropDownListForLevel1(outSheet,m,0,"row");
                    }
                }
            }
            return workbook;

        }

    private static void createDropDownListForLevel1(XSSFSheet worksheet,int rownum,int colnum, String type) {

        if (type.equals("row")) {
            XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(worksheet);
            XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint)
                    dvHelper.createExplicitListConstraint(new String[]{"Yes", "No"});
            CellRangeAddressList addressList = new CellRangeAddressList(rownum, rownum, 4, 4);
            XSSFDataValidation validation = (XSSFDataValidation) dvHelper.createValidation(
                    dvConstraint, addressList);
            validation.setShowErrorBox(true);
            worksheet.addValidationData(validation);
        }

        else
        {
            XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(worksheet);
            XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint)
                    dvHelper.createExplicitListConstraint(new String[]{"Mygubbi", "Client", "NA"});
            CellRangeAddressList addressList = new CellRangeAddressList(rownum, rownum, colnum, colnum);
            XSSFDataValidation validation = (XSSFDataValidation)dvHelper.createValidation(
                    dvConstraint, addressList);
            validation.setShowErrorBox(true);
            worksheet.addValidationData(validation);
        }
    }

  */
/*  private static void createSowRecord(ProposalSOW proposal_sow)
    {

        String query = "proposal.sow.create";
        Integer id = LocalCache.getInstance().store(new QueryData(query, proposal_sow));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag || resultData.updateResult.getUpdated() == 0)
                    {
                        LOG.error("Error in inserting line item in the audit table. " + resultData.errorMessage, resultData.error);
                    }
                    else
                    {
                        LOG.info("Inserted sow Record " + proposal_sow.toString());
                    }
                });
    }*//*


    private static void readValuesFromExcel()
    {

        try {
            FileInputStream newFile = new FileInputStream(new File(
                    "D:\\Mygubbi GAME\\TestCopy11.xlsx"));
            XSSFWorkbook workbook = new XSSFWorkbook(newFile);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int noOfRows = sheet.getLastRowNum();
            for (int i = 0;i < noOfRows ; i++)
            {
                XSSFRow xssfRow = sheet.getRow(i);
                int count = 0;
                String[] services_value = new String[128];

                for(Integer services_cell : cell_Services)
                    {
                        XSSFCell xssfCell = xssfRow.getCell(services_cell);


                        if (!(xssfCell == null))
                        {
                            services_value[count] = xssfCell.getStringCellValue();
                            System.out.println("Services string value :" + count  + " : " + services_value[count] );
                            count = count + 1;
                        }

                    }
                ProposalSOW proposal_sow = new ProposalSOW();
                proposal_sow.setProposalId(1);
                proposal_sow.setVersion(0.1);
                proposal_sow.setSpaceType("MBR");
                proposal_sow.setProductId(1);
                proposal_sow.setProduct("Hinged Wardrobe");
                proposal_sow.setRoom("S Master Bed Room");
                proposal_sow.setL1S01(services_value[0]);
                proposal_sow.setL2S01(services_value[1]);
                proposal_sow.setL2S02(services_value[2]);
                proposal_sow.setL2S03(services_value[3]);
                proposal_sow.setL2S04(services_value[4]);
                proposal_sow.setL2S05(services_value[5]);
                proposal_sow.setL2S06(services_value[6]);


                        */
/*createSowRecord(proposal_sow);*//*

                System.out.println("Proposal_sow :" + proposal_sow.toString());

                Connection con = DriverManager.getConnection(
                        "jdbc:mysql://ogdemodb.cyn8wqrk6sdc.ap-southeast-1.rds.amazonaws.com/mg_report","admin", "OG$#gubi32");

                int insert = con.createStatement().executeUpdate("INSERT INTO proposal_sow (proposalId, version, spaceType, productId, product, room," +
                        " L1S01,L2S01,L2S02,L2S03,L2S04,L2S05,L2S06,L2S07 ) VALUES (" +
                         + proposal_sow.getProposalId() + "," + proposal_sow.getVERSION() + "," + "'" + proposal_sow.getSpaceType() + "'" + ","
                         + proposal_sow.getProductId()  + "," + "'" + proposal_sow.getPRODUCT() + "'" + "," + "'" + proposal_sow.getROOM() + "'" + "," + "'" +
                        proposal_sow.getL1S01() + "'" + "," + "'" + proposal_sow.getL2S01() + "'" + "," + "'" + proposal_sow.getL2S02() +"'" + "," + "'" +
                        proposal_sow.getL2S03() + "'" + "," + "'" + proposal_sow.getL2S04() + "'" + "," + "'" + proposal_sow.getL2S05() +"'" + "," + "'" +
                        proposal_sow.getL2S06() + "'" + ")");

                if (insert == 1)
                {
                    System.out.println("Inserted record");
                }
                System.out.println("Count :" + count );
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }





}
*/
