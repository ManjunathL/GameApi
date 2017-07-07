package com.mygubbi.game.proposal.quote;

import com.mygubbi.game.proposal.model.ProposalHeader;
import com.mygubbi.game.proposal.output.AbstractProposalOutputCreator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Sunil on 22-05-2016.
 */
public class SOWTemplateCreator extends AbstractProposalOutputCreator
{
    private final static Logger LOG = LogManager.getLogger(SOWTemplateCreator.class);

    public SOWTemplateCreator(QuoteData quoteData, ProposalHeader proposalHeader)
    {
        super(quoteData,proposalHeader);
    }

    public String getTemplateName()
    {
        return  "sow_template";
    }

    public String getOutputFilename()
    {
        return "/sow.xlsx";
    }


    public void create()
    {
        this.openWorkbook();
        new SowSheetCreator(this.workbookManager.getSheetByName("SOW"),this.quoteData,this.workbookManager.getStyles()).prepare();
        //new ExcelQuoteSheetCreatorForPackage(this.workbookManager.getSheetByName("sow"), this.quoteData, this.workbookManager.getStyles()).prepare();
        //new DataSheetCreator(this.workbookManager.getSheetByName("Data"), this.quoteData, this.workbookManager.getStyles()).prepare();
        this.closeWorkbook();
    }

    @Override
    public String getOutputKey()
    {
        return "sowFile";
    }

    public static void main(String[] args)
    {

    }

}
