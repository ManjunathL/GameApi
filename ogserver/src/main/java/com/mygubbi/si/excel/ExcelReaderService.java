package com.mygubbi.si.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by Sunil on 27-04-2016.
 */

public class ExcelReaderService
{
    private String filename;
    private int sheetNumber;
    private boolean skipFirstRow;
    private int[] columnsToRead;
    private ExcelRowHandler rowHandler;
    private int rowsToSkip;

    public ExcelReaderService(String filename, int sheetNumber, boolean skipFirstRow, int[] columnsToRead,
                              ExcelRowHandler rowHandler)
    {
        this.filename = filename;
        this.sheetNumber = sheetNumber;
        this.skipFirstRow = skipFirstRow;
        this.columnsToRead = columnsToRead;
        this.rowHandler = rowHandler;
    }

    public void read()
    {
        Workbook wb = null;
        try
        {
            File file = new File(this.filename);
            if (file.exists())
            {
                wb = new XSSFWorkbook(new BufferedInputStream(new FileInputStream(this.filename)));
            }
            else
            {
                wb = new XSSFWorkbook(new BufferedInputStream(getClass().getResourceAsStream(this.filename)));
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
            return;
        }

        Sheet sheet = wb.getSheetAt(this.sheetNumber);
        System.out.println("Processing sheet - " + sheet.getSheetName());

        int startRow = sheet.getFirstRowNum() + (this.skipFirstRow ? 1 : 0);
        int endRow = sheet.getLastRowNum();
        System.out.println("Reading rows from " + startRow + " to " + endRow);

        int numberOfColumns = this.columnsToRead.length;
        String[] data = new String[numberOfColumns];
        for (int rowNum = startRow; rowNum <= endRow; rowNum++)
        {
            for (int i = 0; i< numberOfColumns; i++)
            {
                data[i] = null;
            }
            Row row = sheet.getRow(rowNum);
            for (int cellIndex = 0; cellIndex < numberOfColumns; cellIndex++)
            {
                try
                {
                    Cell cell = row.getCell(this.columnsToRead[cellIndex]);
                    if (cell == null) continue;
                    if (cell.getCellType() == Cell.CELL_TYPE_FORMULA)
                    {
                        switch(cell.getCachedFormulaResultType()) {
                            case Cell.CELL_TYPE_NUMERIC:
                                data[cellIndex] = new Double(cell.getNumericCellValue()).toString();
                                break;
                            case Cell.CELL_TYPE_STRING:
                                data[cellIndex] = cell.getRichStringCellValue().getString();
                                break;
                        }
                    }
                    else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
                    {
                        data[cellIndex] = new Double(cell.getNumericCellValue()).toString();
                    }
                    else
                    {
                        data[cellIndex] = cell.getStringCellValue();
                    }
                }
                catch (Exception e)
                {
                    System.out.println("Error reading cell (" + rowNum + "," + this.columnsToRead[cellIndex] + "). Error:" + e.getMessage() );
                    throw e;
                }
            }
            this.rowHandler.handle(data);
        }

        this.rowHandler.done();
        try
        {
            wb.close();
        }
        catch (IOException e)
        {
            //Ignore
        }
    }
}
