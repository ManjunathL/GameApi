package com.mygubbi.game.proposal.jobcard;

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
public class PanelSheetCreator implements ExcelCellProcessor
{
    private final static Logger LOG = LogManager.getLogger(PanelSheetCreator.class);

    private QuoteData quoteData;
    private Sheet sheet;
    private ExcelStyles styles;
    private AssembledProductInQuote product;
    private ExcelSheetProcessor sheetProcessor;

    public PanelSheetCreator(Sheet sheet, QuoteData quoteData, AssembledProductInQuote product, ExcelStyles styles)
    {
        this.sheet = sheet;
        this.quoteData = quoteData;
        this.styles = styles;
        this.product = product;
    }

    public void prepare()
    {
        this.sheetProcessor = new ExcelSheetProcessor(this.sheet, this.styles, this);
        this.sheetProcessor.process();
    }

    @Override
    public Object getValueForKey(String key)
    {
        Object value = this.quoteData.getValue(key);
        if (value == null)
        {
            return this.product.getValue(key);
        }
        return value;
    }

    @Override
    public void processCell(Cell cell, String cellValue)
    {
        switch (cellValue)
        {
            case "Carcass Panels":
                this.fillCarcassPanels(this.product.getAggregatedCarcassPanels(), cell.getRow().getRowNum() + 1, "No Carcass Panels.");
                break;

            case "Shutter Panels":
                this.fillShutterPanels(this.product.getAggregatedShutterPanels(), cell.getRow().getRowNum() + 1, "No Shutter Panels.");
                break;

            default:
                break;
        }

    }

    private int fillShutterPanels(List<AssembledProductInQuote.ModulePanelComponent> panels, int currentRow, String defaultMessage)
    {
        if (panels == null || panels.isEmpty())
        {
            currentRow++;
            this.sheetProcessor.createDataRowInDataSheet(currentRow, new String[]{defaultMessage});
            return currentRow;
        }


        int seq = 1;
        for (AssembledProductInQuote.ModulePanelComponent panel : panels)
        {
            currentRow++;
            this.sheetProcessor.createDataRowInDataSheet(currentRow, new Object[]{seq, panel.title, panel.height, panel.width,
                    panel.thickness, panel.quantity, panel.edgebinding, panel.area, panel.design, panel.color, panel.dimension});
            seq++;
        }
        return currentRow;
    }

    private int fillCarcassPanels(List<AssembledProductInQuote.ModulePanelComponent> panels, int currentRow, String defaultMessage)
    {
        if (panels == null || panels.isEmpty())
        {
            currentRow++;
            this.sheetProcessor.createDataRowInDataSheet(currentRow, new String[]{defaultMessage});
            return currentRow;
        }

        int seq = 1;
        for (AssembledProductInQuote.ModulePanelComponent panel : panels)
        {
            currentRow++;
            this.sheetProcessor.createDataRowInDataSheet(currentRow, new Object[]{seq, panel.title, panel.width,
                    panel.height, panel.thickness, panel.quantity, panel.edgebinding, panel.area, panel.dimension});
            seq++;
        }
        return currentRow;
    }
}
