package com.mygubbi.game.proposal.output;

import com.mygubbi.game.proposal.erp.ExcelSalesOrderCreator;
import com.mygubbi.game.proposal.jobcard.ExcelJobCardCreator;
import com.mygubbi.game.proposal.margin.MarginExcelCreator;
import com.mygubbi.game.proposal.model.ProposalHeader;
import com.mygubbi.game.proposal.quote.ExcelQuoteCreator;
import com.mygubbi.game.proposal.quote.PdfQuoteCreator;
import com.mygubbi.game.proposal.quote.QuoteData;

/**
 * Created by Sunil on 22-07-2016.
 */
public interface ProposalOutputCreator
{
    public enum OutputType {QUOTATION, JOBCARD, SALESORDER, QUOTEPDF, MARGIN};

    public void create();

    public String getOutputFile();

    public String getOutputKey();

    public static ProposalOutputCreator getCreator(OutputType outputType, QuoteData quoteData, ProposalHeader proposalHeader)
    {
        switch (outputType)
        {
            case QUOTATION:
                return new ExcelQuoteCreator(quoteData,proposalHeader);

            case JOBCARD:
                return new ExcelJobCardCreator(quoteData,proposalHeader);

            case SALESORDER:
                return new ExcelSalesOrderCreator(quoteData,proposalHeader);

            case QUOTEPDF:
                return new PdfQuoteCreator(quoteData, proposalHeader);

            case MARGIN:
                return new MarginExcelCreator(quoteData, proposalHeader);



            default:
                throw new RuntimeException("Output creator not defined for type:" + outputType);
        }
    }
}
