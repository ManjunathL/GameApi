package com.mygubbi.si.excel;

import com.mygubbi.common.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * Created by test on 08-06-2016.
 */
public class ExcelSheetProcessor
{
    private final static Logger LOG = LogManager.getLogger(ExcelSheetProcessor.class);

    private Sheet sheet;
    private ExcelCellProcessor cellProcessor;

    public ExcelSheetProcessor(Sheet sheet, ExcelCellProcessor cellProcessor)
    {
        this.sheet = sheet;
        this.cellProcessor = cellProcessor;
    }

    public void process()
    {
        int firstRow = this.sheet.getFirstRowNum();
        int lastRow = this.sheet.getLastRowNum();

        int rowNum = 0;
        int cellNum = 0;
        String cellValue = null;
        try
        {
            for (rowNum = lastRow; rowNum >= firstRow; rowNum--)
            {
                Row row = this.sheet.getRow(rowNum);
                if (row == null) continue;
                int lastCell = row.getLastCellNum();
                for (cellNum = row.getFirstCellNum(); cellNum < lastCell; cellNum++)
                {
                    Cell cell = row.getCell(cellNum);
                    if (cell == null) continue;
                    if (cell.getCellType() == Cell.CELL_TYPE_STRING)
                    {
                        cellValue = cell.getStringCellValue().trim();
                        this.handleCell(cell, cellValue);
                        cellValue = null;
                    }
                }
            }
        }
        catch (Exception e)
        {
            String message = "Error processing cell (" + rowNum + "," + cellNum + ") in sheet " + sheet.getSheetName()
                    + ". Cell value: " + cellValue + " - Error:" + e.getMessage();
            LOG.error(message, e);
            throw new RuntimeException(message, e);
        }
    }

    private void handleCell(Cell cell, String cellValue)
    {
        if (StringUtils.isEmpty(cellValue)) return;

        if (cellValue.charAt(0) == '$')
        {
            String key = cellValue.substring(1);
            this.replaceCellValue(cell, this.cellProcessor.getValueForKey(key));
        }
        else
        {
            this.cellProcessor.processCell(cell, cellValue);
        }
    }

    private void replaceCellValue(Cell cell, Object value)
    {
        if (value == null) value = "";
        if (value instanceof String)
        {
            cell.setCellValue((String) value);
        }
        else if (value instanceof Double)
        {
            cell.setCellValue((Double) value);
        }
        else
        {
            cell.setCellValue(value.toString());
        }
    }
}
