package com.mygubbi.game.proposal.jobcard;

import com.mygubbi.game.proposal.ProductAddon;
import com.mygubbi.game.proposal.quote.AssembledProductInQuote;
import com.mygubbi.game.proposal.quote.QuoteData;
import com.mygubbi.si.excel.ExcelCellProcessor;
import com.mygubbi.si.excel.ExcelSheetProcessor;
import com.mygubbi.si.excel.ExcelStyles;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

/**
 * Created by Sunil on 22-05-2016.
 */
public class AddonsSheetCreator implements ExcelCellProcessor
{
    private final static Logger LOG = LogManager.getLogger(AddonsSheetCreator.class);

    private QuoteData quoteData;
    private Sheet accSheet;
    private ExcelStyles styles;
    private AssembledProductInQuote product;
    private ExcelSheetProcessor sheetProcessor;

    public AddonsSheetCreator(Sheet accSheet, QuoteData quoteData, AssembledProductInQuote product, ExcelStyles styles)
    {
        this.accSheet = accSheet;
        this.quoteData = quoteData;
        this.styles = styles;
        this.product = product;
    }

    public void prepare()
    {
        this.sheetProcessor = new ExcelSheetProcessor(this.accSheet, this.styles, this);
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
            case "Accessories":
                this.fillAddons(this.product.getAddonAccessories(), cell.getRow().getRowNum() + 1, "No Accessories.");
                break;

            case "Appliances":
                this.fillAddons(this.product.getAppliances(), cell.getRow().getRowNum() + 1, "No Appliances.");
                break;

            case "Counter Top":
                this.fillAddons(this.product.getCounterTops(), cell.getRow().getRowNum() + 1, "No Countertops.");
                break;

            case "Services":
                this.fillAddons(this.product.getServices(), cell.getRow().getRowNum() + 1, "No Services.");
                break;

            default:
                break;
        }

    }

    private int fillAddons(List<ProductAddon> addons, int currentRow, String defaultMessage)
    {
        if (addons == null || addons.isEmpty())
        {
            currentRow++;
            this.sheetProcessor.createDataRowInDataSheet(currentRow, new String[]{defaultMessage});
            return currentRow;
        }

        int seq = 1;
        for (ProductAddon addon : addons)
        {
            currentRow++;
            this.sheetProcessor.createDataRowInDataSheet(currentRow, new Object[]{seq, addon.getTitle(), addon.getBrandCode(),
                    addon.getCatalogueCode(), addon.getUom(), addon.getQuantity()});
            seq++;
        }
        return currentRow;
    }
}
