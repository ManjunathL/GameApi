package com.mygubbi.game.proposal.quote;

import com.mygubbi.config.ConfigHolder;
import com.mygubbi.game.proposal.model.ProposalHeader;
import com.mygubbi.game.proposal.output.ProposalOutputCreator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Created by Chirag on 23-11-2016.
 */
public class PdfQuoteCreator implements ProposalOutputCreator {

    private final static Logger LOG = LogManager.getLogger(PdfQuoteCreator.class);


    private QuoteData quoteData;
    private ProposalHeader proposalHeader;
    private String targetFile;

    public PdfQuoteCreator(QuoteData quoteData, ProposalHeader proposalHeader){
        this.quoteData = quoteData;
        this.proposalHeader = proposalHeader;
        this.targetFile = ConfigHolder.getInstance().getStringValue("proposal_docs_folder","/mnt/game/proposal/") + "//quotation.pdf";

    }

    @Override
    public void create() {

        LOG.debug("PATH :" + this.getOutputFile());

        new QuotationPDFCreator(quoteData, proposalHeader).createpdf(targetFile);
    }

    @Override
    public String getOutputFile() {
        return this.targetFile;
    }

    @Override
    public String getOutputKey() {
        return "quoteFile";
    }
}
