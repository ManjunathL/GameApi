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
    private ExcelStyles styles;

    public ExcelSheetProcessor(Sheet sheet, ExcelStyles styles, ExcelCellProcessor cellProcessor)
    {
        this.sheet = sheet;
        this.cellProcessor = cellProcessor;
        this.styles = styles;
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

    public void createDataRowInDataSheet(int rowNum, Object[] data)
    {
        this.createRowInDataSheet(rowNum, data, false);
    }

    public void createTitleRowInDataSheet(int rowNum, Object[] data)
    {
        this.createRowInDataSheet(rowNum, data, true);
    }

    private void createRowInDataSheet(int rowNum, Object[] data, boolean isTitle)
    {
        if (rowNum < this.sheet.getLastRowNum())
        {
            this.sheet.shiftRows(rowNum, this.sheet.getLastRowNum(), 1);
        }

        Row dataRow = this.sheet.createRow(rowNum);

        if (data == null || data.length == 0) return;

        int lastCell = data.length;
        for (int cellNum = 0; cellNum < lastCell; cellNum++)
        {
            Object value = data[cellNum];
            if (value == null)
            {
                continue;
            }
            if (value instanceof String)
            {
                this.setTextValueInCell(isTitle, dataRow, cellNum, (String) value);
            }
            else if (value instanceof Integer)
            {
                this.setNumericValueInCell(isTitle, dataRow, cellNum, ((Integer) value).doubleValue());
            }
            else if (value instanceof Double)
            {
                this.setNumericValueInCell(isTitle, dataRow, cellNum, (Double) value);
            }
            else if (value instanceof Long)
            {
                this.setNumericValueInCell(isTitle, dataRow, cellNum, ((Long) value).doubleValue());
            }
            else
            {
                this.setTextValueInCell(isTitle, dataRow, cellNum, value.toString());
            }
        }
    }

    private void setNumericValueInCell(boolean isTitle, Row dataRow, int cellNum, Double numberValue)
    {
        Cell cell = dataRow.createCell(cellNum, Cell.CELL_TYPE_NUMERIC);
        cell.setCellValue(numberValue);
        if (isTitle)
        {
            cell.setCellStyle(this.styles.getBoldStyle());
        }
    }

    private void setTextValueInCell(boolean isTitle, Row dataRow, int cellNum, String value)
    {
        String textValue = value;
        if (StringUtils.isNonEmpty(textValue))
        {
            Cell cell = dataRow.createCell(cellNum, Cell.CELL_TYPE_STRING);
            cell.setCellValue(textValue);
            if (isTitle)
            {
                cell.setCellStyle(this.styles.getBoldStyle());
            }
        }
    }


}
