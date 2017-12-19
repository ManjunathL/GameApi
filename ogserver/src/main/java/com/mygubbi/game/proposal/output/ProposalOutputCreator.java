package com.mygubbi.game.proposal.output;

import com.mygubbi.game.proposal.model.ProposalVersion;
import com.mygubbi.game.proposal.model.SOWPdf;
import com.mygubbi.game.proposal.sow.SOWTemplateCreator;
//import com.mygubbi.game.proposal.erp.BOQTemplateCreator;
import com.mygubbi.game.proposal.erp.ExcelSalesOrderCreator;
import com.mygubbi.game.proposal.jobcard.ExcelJobCardCreator;
import com.mygubbi.game.proposal.model.ProposalHeader;
import com.mygubbi.game.proposal.quote.*;

import java.util.List;

/**
 * Created by Sunil on 22-07-2016.
 */
public interface ProposalOutputCreator
{
    public enum OutputType {QUOTATION, JOBCARD, SALESORDER, QUOTEPDF, SOW, SOWPDF , BOOKING_FORM,WORKSCONTRACT };

    public void create();

    public String getOutputFile();

    public String getOutputKey();

    public static ProposalOutputCreator getCreator(OutputType outputType, QuoteData quoteData, ProposalHeader proposalHeader, Boolean isValidSow, List<SOWPdf> proposalSOWs, ProposalVersion proposalVersion) {

        switch (outputType) {
            case QUOTATION:
                return new ExcelQuoteCreator(quoteData, proposalHeader);

            case JOBCARD:
                return new ExcelJobCardCreator(quoteData, proposalHeader);

            case SALESORDER:
                return new ExcelSalesOrderCreator(quoteData, proposalHeader);

            case QUOTEPDF:
                return new PdfQuoteCreator(quoteData, proposalHeader,isValidSow,proposalVersion);
            case SOWPDF:
                return new PdfSowCreator(quoteData, proposalHeader,proposalSOWs);
            case BOOKING_FORM:
                return new PdfOfficeUseCreator(quoteData,proposalHeader);
            case WORKSCONTRACT:
                return new WorksContractCreator(quoteData,proposalHeader);
            default:
                throw new RuntimeException("Output creator not defined for type:" + outputType);
        }
    }

        public static ProposalOutputCreator getCreator(OutputType outputType, ProposalHeader proposalHeader)
        {

        switch (outputType)
        {




            default:
                throw new RuntimeException("Output creator not defined for type:" + outputType);
            }
        }



}
