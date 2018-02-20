package com.mygubbi.game.proposal.erp;

import com.mygubbi.game.proposal.model.ProposalHeader;
import com.mygubbi.game.proposal.model.SOWPdf;
import com.mygubbi.game.proposal.output.AbstractProposalOutputCreator;
import com.mygubbi.game.proposal.quote.AssembledProductInQuote;
import com.mygubbi.game.proposal.quote.QuoteData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Created by Sunil on 22-05-2016.
 */
public class ExcelSalesOrderCreator extends AbstractProposalOutputCreator
{
    private final static Logger LOG = LogManager.getLogger(ExcelSalesOrderCreator.class);

    public ExcelSalesOrderCreator(QuoteData quoteData, ProposalHeader proposalHeader, List<SOWPdf> sowPdfList)
    {
        super(quoteData,proposalHeader,sowPdfList);
    }

    public String getTemplateName()
    {
        return "salesorder_template";
    }

    public String getOutputFilename()
    {
        return "salesorder-" + this.quoteData.getProposalHeader().getCrmId() + "-" + this.getAssembledProductInQuote().getTitle() + ".xlsx";
    }

    public void create()
    {
        AssembledProductInQuote product = this.getAssembledProductInQuote();
        this.openWorkbook();
        new SalesOrderSheetCreator(this.workbookManager.getSheetByName("IndentingModules"), quoteData, workbookManager.getStyles()).prepare();
        new SalesOrderSheetCreatorForModule(this.workbookManager.getSheetByName("ModulesListing"), quoteData, workbookManager.getStyles()).prepare();

        this.closeWorkbook();
    }

    private AssembledProductInQuote getAssembledProductInQuote()
    {
        if (this.quoteData.getAssembledProducts().isEmpty())
        {
            throw new RuntimeException("There is no assembled product in the proposal");
        }

        return this.quoteData.getAssembledProducts().get(0);
    }

    @Override
    public String getOutputKey()
    {
        return "salesorderFile";
    }

}
