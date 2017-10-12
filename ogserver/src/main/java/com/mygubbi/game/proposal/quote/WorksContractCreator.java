package com.mygubbi.game.proposal.quote;

import com.mygubbi.config.ConfigHolder;
import com.mygubbi.game.proposal.model.ProposalHeader;
import com.mygubbi.game.proposal.output.ProposalOutputCreator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Shruthi on 9/26/2017.
 */
public class WorksContractCreator implements ProposalOutputCreator
{
    private final static Logger LOG = LogManager.getLogger(WorksContractCreator.class);
    private QuoteData quoteData;
    private ProposalHeader proposalHeader;
    private String targetFile;

    public WorksContractCreator(QuoteData quoteData, ProposalHeader proposalHeader) {
        this.quoteData = quoteData;
        this.proposalHeader = proposalHeader;
        String targetFolder = ConfigHolder.getInstance().getStringValue("proposal_docs_folder","/mnt/game/proposal");
        this.targetFile = targetFolder+"/"+quoteData.getProposalHeader().getId() + "/Workscontract.pdf";
    }

    @Override
    public void create() {
        new WorksContractPDFCreator(quoteData,proposalHeader).createPdf(targetFile);
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
