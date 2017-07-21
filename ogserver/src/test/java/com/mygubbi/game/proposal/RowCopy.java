package com.mygubbi.game.proposal;

/**
 * Created by User on 04-07-2017.
 */
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class RowCopy {

    public static void main(String[] args) throws Exception{
        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream("D:\\Mygubbi GAME\\TestNew.xlsx"));
        XSSFSheet sheet = workbook.getSheet("sow");

        XSSFWorkbook new_workbook = new XSSFWorkbook();
        XSSFSheet outSheet = workbook.createSheet();

        for (int i = 0;i < sheet.getLastRowNum() ; i++)
        {
            XSSFRow xssfRow = sheet.getRow(i);
            XSSFCell xssfCell = xssfRow.getCell(0);
            if (xssfCell.getStringCellValue().equals("Kitchen"))
            {
                copyRow(workbook, sheet, 0, 1,new_workbook,outSheet);

            }
        }


        File outFile = new File("D:\\Mygubbi GAME\\TestCopy9.xlsx");
        if (!outFile.exists()) {
            outFile.createNewFile();
        }
        FileOutputStream out = new FileOutputStream(outFile);
        new_workbook.write(out);
        out.close();
    }



    private static void copyRow(XSSFWorkbook workbook, XSSFSheet worksheet, int sourceRowNum, int destinationRowNum, XSSFWorkbook newWorkBook,XSSFSheet newWorkSheet ) {
        // Get the source / new row
        XSSFRow newRow = newWorkSheet.getRow(destinationRowNum);
        XSSFRow sourceRow = worksheet.getRow(sourceRowNum);


        // If the row exist in destination, push down all rows by 1 else create a new row
        if (newRow != null) {
            worksheet.shiftRows(destinationRowNum, worksheet.getLastRowNum(), 1);
        } else {
            newRow = worksheet.createRow(destinationRowNum);
        }




        // Loop through source columns to add to new row
        for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
            // Grab a copy of the old/new cell
            XSSFCell oldCell = sourceRow.getCell(i);
            XSSFCell newCell = newRow.createCell(i);

            // If the old cell is null jump to next cell
            if (oldCell == null) {
                newCell = null;
                continue;
            }

            // Copy style from old cell and apply to new cell
            XSSFCellStyle newCellStyle = workbook.createCellStyle();
            newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
/*
            newCell.setCellStyle(newCellStyle);
*/

            System.out.println("Cell style :" + oldCell.getCellType());
            System.out.println("Cell value :" + oldCell.getStringCellValue());

           /* if (newCell.getStringCellValue().equals("Kitchen"))
            {
                worksheet.removeRow(newCell.getRow());
            }*/

            if (oldCell.getCellType() == 3) {
                System.out.println("Hey");

                createDropDownListForLevel1(worksheet);
                /*createDropDownListForLevel2(worksheet, newCell.getColumnIndex());*/
            }

            // If there is a cell comment, copy
            if (oldCell.getCellComment() != null) {
                newCell.setCellComment(oldCell.getCellComment());
            }

            // If there is a cell hyperlink, copy
            if (oldCell.getHyperlink() != null) {
                newCell.setHyperlink(oldCell.getHyperlink());
            }

            // Set the cell data type
            newCell.setCellType(oldCell.getCellType());

            // Set the cell data value
            switch (oldCell.getCellType()) {
                case Cell.CELL_TYPE_BLANK:
                    newCell.setCellValue(oldCell.getStringCellValue());
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    newCell.setCellValue(oldCell.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_ERROR:
                    newCell.setCellErrorValue(oldCell.getErrorCellValue());
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    newCell.setCellFormula(oldCell.getCellFormula());
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    newCell.setCellValue(oldCell.getNumericCellValue());
                    break;
                case Cell.CELL_TYPE_STRING:
                    newCell.setCellValue(oldCell.getRichStringCellValue());
                    break;
            }
        }
        // If there are are any merged regions in the source row, copy to new row
        for (int k = 0; k < worksheet.getNumMergedRegions(); k++) {
            CellRangeAddress cellRangeAddress = worksheet.getMergedRegion(k);
            if (cellRangeAddress.getFirstRow() == sourceRow.getRowNum()) {
                CellRangeAddress newCellRangeAddress = new CellRangeAddress(newRow.getRowNum(),
                        (newRow.getRowNum() +
                                (cellRangeAddress.getLastRow() - cellRangeAddress.getFirstRow()
                                )),
                        cellRangeAddress.getFirstColumn(),
                        cellRangeAddress.getLastColumn());
                worksheet.addMergedRegion(newCellRangeAddress);
            }
        }
    }

    private static void createDropDownListForLevel1(XSSFSheet worksheet) {
        XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(worksheet);
        XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint)
                dvHelper.createExplicitListConstraint(new String[]{"Yes", "No"});
        CellRangeAddressList addressList = new CellRangeAddressList(1, 1, 4, 4);
        XSSFDataValidation validation = (XSSFDataValidation)dvHelper.createValidation(
                dvConstraint, addressList);
        validation.setShowErrorBox(true);
        worksheet.addValidationData(validation);
    }

    private static void createDropDownListForLevel2(XSSFSheet worksheet, int col) {
        XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(worksheet);
        XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint)
                dvHelper.createExplicitListConstraint(new String[]{"Mygubbi", "Client", "NA"});
        CellRangeAddressList addressList = new CellRangeAddressList(1, 1, col, col);
        XSSFDataValidation validation = (XSSFDataValidation)dvHelper.createValidation(
                dvConstraint, addressList);
        validation.setShowErrorBox(true);
        worksheet.addValidationData(validation);
    }
}