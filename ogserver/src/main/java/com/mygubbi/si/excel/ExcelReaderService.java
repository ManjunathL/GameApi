package com.mygubbi.si.excel;

import com.mygubbi.game.proposal.ProductModule;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
            wb = new XSSFWorkbook(new BufferedInputStream(getClass().getResourceAsStream(this.filename)));
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return;
        }

        List<ProductModule> productModules = new ArrayList<>();

        Sheet sheet = wb.getSheetAt(this.sheetNumber);
        System.out.println("Processing sheet - " + sheet.getSheetName());

        int firstRow = this.skipFirstRow ? 1 : 0;

        int startRow = sheet.getFirstRowNum() + (this.skipFirstRow ? 1 : 0);
        int endRow = sheet.getLastRowNum();
        System.out.println("Reading rows from " + startRow + " to " + endRow);

        String[] data = new String[this.columnsToRead.length];
        for (int rowNum = startRow; rowNum <= endRow; rowNum++)
        {
            Row row = sheet.getRow(rowNum);
            for (int cellIndex = 0; cellIndex < this.columnsToRead.length; cellIndex++)
            {
                try
                {
                    data[cellIndex] = row.getCell(this.columnsToRead[cellIndex]).getStringCellValue();
                }
                catch (Exception e)
                {
                    System.out.println("Error reading cell (" + rowNum + "," + this.columnsToRead[cellIndex] + "). Error:" + e.getMessage() );
                    throw e;
                }
            }
            this.rowHandler.handle(data);
        }

        try
        {
            wb.close();
        }
        catch (IOException e)
        {
            //Ignore
        }
    }

    private int getInteger(Row row, int index)
    {
        return new Double(row.getCell(index).getNumericCellValue()).intValue();
    }

    private String getColor(Row row)
    {
        Cell colorCell = row.getCell(9);
        String color = null;
        if (colorCell.getCellType() == Cell.CELL_TYPE_STRING)
        {
            color = colorCell.getStringCellValue();
        }
        else if (colorCell.getCellType() == Cell.CELL_TYPE_NUMERIC)
        {
            color = String.valueOf(new Double(colorCell.getNumericCellValue()).intValue());
        }
        return color;
    }

    public static void main(String[] args)
    {
        new ExcelReaderService("/testdata/KDMax-ModuleMaster.xlsx", 0, true, new int[]{7,8,9,10,11,12,13}, null).read();
    }
}
