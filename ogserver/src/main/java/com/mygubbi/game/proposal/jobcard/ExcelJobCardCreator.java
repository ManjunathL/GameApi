package com.mygubbi.game.proposal.jobcard;

import com.mygubbi.game.proposal.quote.AssembledProductInQuote;
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

    public ExcelJobCardCreator(String jobCardXls, QuoteData quoteData)
    {
        this.jobCardXls = jobCardXls;
        this.quoteData = quoteData;
    }

    public void prepareJobCard()
    {
        if (this.quoteData.getAssembledProducts().isEmpty())
        {
            throw new RuntimeException("There is no assembled product in the proposal");
        }
        AssembledProductInQuote product = this.quoteData.getAssembledProducts().get(0);

        ExcelWorkbookManager workbookManager = new ExcelWorkbookManager(this.jobCardXls);
        new JobCardSheetCreator(workbookManager.getSheetByName("JOB Card"), this.quoteData, product, workbookManager.getStyles()).prepare();
        new HardwareSheetCreator(workbookManager.getSheetByName("Hardwares"), this.quoteData, product, workbookManager.getStyles()).prepare();
        new AddonsSheetCreator(workbookManager.getSheetByName("Appliances and Services"), this.quoteData, product, workbookManager.getStyles()).prepare();
        new PanelSheetCreator(workbookManager.getSheetByName("Panel Summary"), this.quoteData, product, workbookManager.getStyles()).prepare();
        new ModuleSheetCreator(workbookManager.getSheetByName("Module Summary"), this.quoteData, product, workbookManager.getStyles()).prepare();
        workbookManager.closeWorkbook();
    }

}
