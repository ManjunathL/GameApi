package com.mygubbi.game.proposal.erp;

import com.mygubbi.game.proposal.ProductModule;
import com.mygubbi.game.proposal.quote.AssembledProductInQuote;
import com.mygubbi.si.excel.ExcelCellProcessor;
import com.mygubbi.si.excel.ExcelSheetProcessor;
import com.mygubbi.si.excel.ExcelStyles;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

/**
 * Created by Sunil on 22-05-2016.
 */
public class SalesOrderSheetCreator implements ExcelCellProcessor
{
    private final static Logger LOG = LogManager.getLogger(SalesOrderSheetCreator.class);

    private final AssembledProductInQuote product;
    private Sheet soSheet;
    private ExcelStyles styles;
    private ExcelSheetProcessor sheetProcessor;

    public SalesOrderSheetCreator(Sheet soSheet, AssembledProductInQuote product, ExcelStyles styles)
    {
        this.soSheet = soSheet;
        this.styles = styles;
        this.product = product;
    }

    public void prepare()
    {
        this.sheetProcessor = new ExcelSheetProcessor(this.soSheet, this.styles, this);
        this.sheetProcessor.process();
    }

    @Override
    public Object getValueForKey(String key)
    {
        return "";
    }

    @Override
    public void processCell(Cell cell, String cellValue)
    {
        switch (cellValue)
        {
            case "SrNo":
                this.fillSheet(cell.getRow().getRowNum() + 1);
                break;

            default:
                break;
        }

    }

    private int fillSheet(int currentRow)
    {
        List<ProductModule> modules = this.product.getModules();
        if (modules == null || modules.isEmpty())
        {
            currentRow++;
            this.sheetProcessor.createDataRowInDataSheet(currentRow, new String[]{"No components"});
            return currentRow;
        }

        currentRow = this.writeRecords(currentRow, this.product.getAggregatedModules());
        currentRow = this.writeRecords(currentRow, this.product.getAggregatedAccessoryPackPanels());
        currentRow = this.writeRecords(currentRow, this.product.getAggregatedAccessoryPackHardware());
        currentRow = this.writeRecords(currentRow, this.product.getAggregatedAccessoryPackAccessories());
        currentRow = this.writeRecords(currentRow, this.product.getAggregatedAccessoryAddons());
        currentRow = this.writeRecords(currentRow, this.product.getAggregatedAddons());

        return currentRow;
    }

    private int writeRecords(int currentRow, List<AssembledProductInQuote.ModulePart> parts)
    {
        for (AssembledProductInQuote.ModulePart modulePart : parts)
        {
            this.sheetProcessor.createDataRowInDataSheet(currentRow, new Object[]{currentRow, modulePart.code, modulePart.uom, modulePart.quantity});
            currentRow++;
        }
        return currentRow;
    }

/*
    private void createDataRowInDataSheet(int rowNum, String [] data, CellStyle style)
    {
        this.createRowInDataSheet(rowNum, data, false, style);
    }

    private void createRowInDataSheet(int rowNum, String [] data, boolean isTitle, CellStyle style)
    {
        if (rowNum < this.soSheet.getLastRowNum())
        {
            this.soSheet.shiftRows(rowNum, this.soSheet.getLastRowNum(), 1);
        }
        Row dataRow = this.soSheet.createRow(rowNum);
        dataRow.setRowStyle(style);
        int lastCell = data.length;
        for (int cellNum = 0; cellNum < lastCell; cellNum++)
        {
            String value = data[cellNum];
            if (StringUtils.isNonEmpty(value))
            {
                Cell cell = dataRow.createCell(cellNum, Cell.CELL_TYPE_STRING);
                cell.setCellValue(value);
                if (isTitle)
                {
                    cell.setCellStyle(this.styles.getBoldStyle());
                }
            }
        }
    }
*/

}
