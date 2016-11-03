package com.mygubbi.game.proposal.jobcard;

import com.mygubbi.game.proposal.model.ProposalHeader;
import com.mygubbi.game.proposal.output.AbstractProposalOutputCreator;
import com.mygubbi.game.proposal.quote.AssembledProductInQuote;
import com.mygubbi.game.proposal.quote.QuoteData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Sunil on 22-05-2016.
 */
public class ExcelJobCardCreator extends AbstractProposalOutputCreator
{
    private final static Logger LOG = LogManager.getLogger(ExcelJobCardCreator.class);

    public ExcelJobCardCreator(QuoteData quoteData, ProposalHeader proposalHeader)
    {
        super(quoteData,proposalHeader);
    }

    public String getTemplateName()
    {
        return "jobcard_template";
    }

    public String getOutputFilename()
    {
        return "jobcard.xlsx";
    }

    public void create()
    {
        if (this.quoteData.getAssembledProducts().isEmpty())
        {
            throw new RuntimeException("There is no assembled product in the proposal");
        }

        this.openWorkbook();

        AssembledProductInQuote product = this.quoteData.getAssembledProducts().get(0);
        new JobCardSheetCreator(workbookManager.getSheetByName("JOB Card"), this.quoteData, product, workbookManager.getStyles()).prepare();
        new HardwareSheetCreator(workbookManager.getSheetByName("Hardwares"), this.quoteData, product, workbookManager.getStyles()).prepare();
        new AddonsSheetCreator(workbookManager.getSheetByName("Appliances and Services"), this.quoteData, product, workbookManager.getStyles()).prepare();
        new PanelSheetCreator(workbookManager.getSheetByName("Panel Summary"), this.quoteData, product, workbookManager.getStyles()).prepare();
        new ModuleSheetCreator(workbookManager.getSheetByName("Module Summary"), this.quoteData, product, workbookManager.getStyles()).prepare();

        this.closeWorkbook();
    }

    @Override
    public String getOutputKey()
    {
        return "jobcardFile";
    }

}
