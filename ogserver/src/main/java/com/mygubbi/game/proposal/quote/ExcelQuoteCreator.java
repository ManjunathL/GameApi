package com.mygubbi.game.proposal.quote;

import com.mygubbi.si.excel.ExcelWorkbookManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Sunil on 22-05-2016.
 */
public class ExcelQuoteCreator
{
    private final static Logger LOG = LogManager.getLogger(ExcelQuoteCreator.class);

    private String quoteXls;
    private QuoteData quoteData;
    private ExcelWorkbookManager workbookManager;

    public ExcelQuoteCreator(String quoteXls, QuoteData quoteData)
    {
        this.quoteXls = quoteXls;
        this.quoteData = quoteData;
        this.workbookManager = new ExcelWorkbookManager(this.quoteXls);
    }

    public void prepareQuote()
    {
        new QuotationSheetCreator(this.workbookManager.getSheetByName("Quote"), this.quoteData, this.workbookManager.getStyles()).prepare();
        new DataSheetCreator(this.workbookManager.getSheetByName("Data"), this.quoteData, this.workbookManager.getStyles()).prepare();
        this.closeWorkbook();
    }

    private void closeWorkbook()
    {
        this.workbookManager.closeWorkbook();
    }


}
