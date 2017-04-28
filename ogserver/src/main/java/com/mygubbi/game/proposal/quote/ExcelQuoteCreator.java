package com.mygubbi.game.proposal.quote;

import com.mygubbi.game.proposal.model.ProposalHeader;
import com.mygubbi.game.proposal.output.AbstractProposalOutputCreator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Created by Sunil on 22-05-2016.
 */
public class ExcelQuoteCreator extends AbstractProposalOutputCreator
{
    private final static Logger LOG = LogManager.getLogger(ExcelQuoteCreator.class);

    public ExcelQuoteCreator(QuoteData quoteData,ProposalHeader proposalHeader)
    {
        super(quoteData,proposalHeader);
    }

    public String getTemplateName()
    {
        if(proposalHeader.getProjectCity().equals("Chennai")) {
            return "quote_template_CHN";
        }else if(proposalHeader.getProjectCity().equals("Mangalore"))
        {
            return "quote_template_MLR";
        }if(proposalHeader.getProjectCity().equals("Pune"))
        {
            return "quote_template_PUN";
        }
        else {
            return "quote_template";
        }
}

    public String getOutputFilename()
    {
        return "/quotation.xlsx";
    }


    public void create()
    {
        this.openWorkbook();
        new ExcelQuoteSheetCreator(this.workbookManager.getSheetByName("Quote"), this.quoteData, this.workbookManager.getStyles()).prepare();
       new DataSheetCreator(this.workbookManager.getSheetByName("Data"), this.quoteData, this.workbookManager.getStyles()).prepare();
        this.closeWorkbook();
    }

    @Override
    public String getOutputKey()
    {
        return "quoteFile";
    }

}
