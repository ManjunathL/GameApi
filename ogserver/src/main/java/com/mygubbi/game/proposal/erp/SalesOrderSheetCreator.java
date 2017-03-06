package com.mygubbi.game.proposal.erp;

import com.mygubbi.game.proposal.ProductModule;
import com.mygubbi.game.proposal.quote.AssembledProductInQuote;
import com.mygubbi.game.proposal.quote.QuoteData;
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
    private QuoteData quoteData;

    public SalesOrderSheetCreator(Sheet soSheet, QuoteData quoteData, ExcelStyles styles)
    {
        this.soSheet = soSheet;
        this.styles = styles;
        this.product = quoteData.getAssembledProducts().get(0);
        this.quoteData = quoteData;
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

//        currentRow = this.writeRecords(currentRow, this.product.getAggregatedModules());
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
            this.sheetProcessor.createDataRowInDataSheet(currentRow, new Object[]{currentRow, modulePart.code, modulePart.uom, modulePart.quantity , modulePart.title , modulePart.catalogCode});
            currentRow++;
        }
        return currentRow;
    }

}
