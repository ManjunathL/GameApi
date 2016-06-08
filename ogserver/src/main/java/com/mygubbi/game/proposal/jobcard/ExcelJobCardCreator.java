package com.mygubbi.game.proposal.jobcard;

import com.mygubbi.game.proposal.quote.QuoteData;
import com.mygubbi.si.excel.ExcelWorkbookManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Sunil on 22-05-2016.
 */
public class ExcelJobCardCreator
{
    private final static Logger LOG = LogManager.getLogger(ExcelJobCardCreator.class);

    private String jobCardXls;
    private QuoteData quoteData;
    private ExcelWorkbookManager workbookManager;

    public ExcelJobCardCreator(String jobCardXls, QuoteData quoteData)
    {
        this.jobCardXls = jobCardXls;
        this.quoteData = quoteData;
        this.workbookManager = new ExcelWorkbookManager(this.jobCardXls);
    }

    public void prepareJobCard()
    {
        new HardwareSheetCreator(this.workbookManager.getSheetByName("Hardwares"), this.quoteData, this.workbookManager.getStyles()).prepare();
        this.closeWorkbook();
    }

    private void closeWorkbook()
    {
        this.workbookManager.closeWorkbook();
    }


}
