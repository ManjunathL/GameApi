package com.mygubbi.game.proposal.quote;

import com.mygubbi.game.proposal.model.ProposalHeader;
import com.mygubbi.game.proposal.output.ProposalOutputCreator;

/**
 * Created by Chirag on 23-11-2016.
 */
public class PdfQuoteCreator implements ProposalOutputCreator {

    private QuoteData quoteData;
    private ProposalHeader proposalHeader;


    String targetFile = this.quoteData.getProposalHeader().folderPath() + "/" + this.getOutputFile();

    public PdfQuoteCreator(QuoteData quoteData, ProposalHeader proposalHeader){
        this.quoteData = quoteData;
        this.proposalHeader = proposalHeader;
    }

    @Override
    public void create() {
        new QuotationPDFCreator(quoteData, proposalHeader).createpdf(targetFile);
    }

    @Override
    public String getOutputFile() {
        return "/quotation.pdf";
    }

    @Override
    public String getOutputKey() {
        return null;
    }
}
