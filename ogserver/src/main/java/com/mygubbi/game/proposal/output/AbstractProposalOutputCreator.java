package com.mygubbi.game.proposal.output;

import com.mygubbi.common.VertxInstance;
import com.mygubbi.config.ConfigHolder;
import com.mygubbi.game.proposal.model.ProposalHeader;
import com.mygubbi.game.proposal.quote.QuoteData;
import com.mygubbi.si.excel.ExcelWorkbookManager;

/**
 * Created by test on 22-07-2016.
 */
public abstract class AbstractProposalOutputCreator implements ProposalOutputCreator
{
    protected QuoteData quoteData;
    protected ProposalHeader proposalHeader;
    protected ExcelWorkbookManager workbookManager;
    private String outputFile;

    public abstract String getTemplateName();
    public abstract String getOutputFilename();

    public AbstractProposalOutputCreator(QuoteData quoteData,ProposalHeader proposalHeader)
    {
        this.quoteData = quoteData;
        this.proposalHeader=proposalHeader;
    }

    protected void openWorkbook()
    {
        //this.outputFile = this.copyTemplateFile();
        //this.workbookManager = new ExcelWorkbookManager(this.outputFile);
    }

    @Override
    public String getOutputFile()
    {
        return outputFile;
    }

    private String copyTemplateFile()
    {
        String templateName = this.getTemplateName();
        String templateFile = ConfigHolder.getInstance().getStringValue(templateName, "/tmp/" + this.getTemplateName() + ".xlsx");
        String targetFile = this.quoteData.getProposalHeader().folderPath() + "/" + this.getOutputFilename();
        try
        {
            VertxInstance.get().fileSystem().deleteBlocking(targetFile);
        }
        catch (Exception e)
        {
            //Nothing to do
        }
        try
        {
            VertxInstance.get().fileSystem().copyBlocking(templateFile, targetFile);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error in copying template file " + templateFile + "to " + targetFile
                    + " for proposal " + this.quoteData.getProposalHeader().getId() + ".", e);
        }
        return targetFile;
    }

    protected void closeWorkbook()
    {
        this.workbookManager.closeWorkbook();
    }
}
