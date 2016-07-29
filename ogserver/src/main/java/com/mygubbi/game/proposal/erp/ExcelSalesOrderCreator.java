package com.mygubbi.game.proposal.erp;

import com.mygubbi.game.proposal.output.AbstractProposalOutputCreator;
import com.mygubbi.game.proposal.quote.AssembledProductInQuote;
import com.mygubbi.game.proposal.quote.QuoteData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Sunil on 22-05-2016.
 */
public class ExcelSalesOrderCreator extends AbstractProposalOutputCreator
{
    private final static Logger LOG = LogManager.getLogger(ExcelSalesOrderCreator.class);

    public ExcelSalesOrderCreator(QuoteData quoteData)
    {
        super(quoteData);
    }

    public String getTemplateName()
    {
        return "salesorder_template";
    }

    public String getOutputFilename()
    {
        return "salesorder.xlsx";
    }

    public void create()
    {
        if (this.quoteData.getAssembledProducts().isEmpty())
        {
            throw new RuntimeException("There is no assembled product in the proposal");
        }

        AssembledProductInQuote product = this.quoteData.getAssembledProducts().get(0);
        this.openWorkbook();
        new SalesOrderSheetCreator(this.workbookManager.getSheetByName("Sheet1"), product, workbookManager.getStyles()).prepare();
        this.closeWorkbook();
    }

    @Override
    public String getOutputKey()
    {
        return "salesorderFile";
    }

}
