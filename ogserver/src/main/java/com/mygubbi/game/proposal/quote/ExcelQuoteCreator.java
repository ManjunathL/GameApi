package com.mygubbi.game.proposal.quote;

import com.mygubbi.game.proposal.output.AbstractProposalOutputCreator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Sunil on 22-05-2016.
 */
public class ExcelQuoteCreator extends AbstractProposalOutputCreator
{
    private final static Logger LOG = LogManager.getLogger(ExcelQuoteCreator.class);

    public ExcelQuoteCreator(QuoteData quoteData)
    {
        super(quoteData);
    }

    public String getTemplateName()
    {
        return "quote_template";
    }

    public String getOutputFilename()
    {
        return "/quotation.xlsx";
    }


    public void create()
    {
        this.openWorkbook();
        new QuotationSheetCreator(this.workbookManager.getSheetByName("Quote"), this.quoteData, this.workbookManager.getStyles()).prepare();
        new DataSheetCreator(this.workbookManager.getSheetByName("Data"), this.quoteData, this.workbookManager.getStyles()).prepare();
        this.closeWorkbook();
    }

    @Override
    public String getOutputKey()
    {
        return "quoteFile";
    }

}
