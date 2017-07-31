package com.mygubbi.game.proposal.quote;

import com.mygubbi.config.ConfigHolder;
import com.mygubbi.game.QuoteSOWPDFCreator;
import com.mygubbi.game.proposal.model.ProposalHeader;
import com.mygubbi.game.proposal.output.ProposalOutputCreator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

/**
 * Created by shilpa on 31/7/17.
 */
public class PdfSowCreator implements ProposalOutputCreator {
    private final static Logger LOG = LogManager.getLogger(PdfQuoteCreator.class);


    private QuoteData quoteData;
    private ProposalHeader proposalHeader;
    private String targetFile;

    public PdfSowCreator(QuoteData quoteData, ProposalHeader proposalHeader){
        this.quoteData = quoteData;
        this.proposalHeader = proposalHeader;
        String targetFolder = ConfigHolder.getInstance().getStringValue("proposal_docs_folder","/mnt/game/proposal");
        this.targetFile = targetFolder+"/"+quoteData.getProposalHeader().getId() + "/sow.pdf";

    }

    @Override
    public void create() {

        LOG.debug("PATH :" + this.getOutputFile());

            LOG.info("else");
            new QuoteSOWPDFCreator( proposalHeader,quoteData).createSOWPDf(targetFile);

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
