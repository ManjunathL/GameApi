package com.mygubbi.game.proposal.quote;

import com.mygubbi.config.ConfigHolder;
import com.mygubbi.game.proposal.model.ProposalHeader;
import com.mygubbi.game.proposal.output.ProposalOutputCreator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Shruthi on 8/10/2017.
 */
public class PdfOfficeUseCreator implements ProposalOutputCreator
{
    private final static Logger LOG = LogManager.getLogger(PdfOfficeUseCreator.class);
    private QuoteData quoteData;
    private ProposalHeader proposalHeader;
    private String targetFile;

    public PdfOfficeUseCreator(QuoteData quoteData,ProposalHeader proposalHeader)
    {
        this.quoteData = quoteData;
        this.proposalHeader = proposalHeader;
        String targetFolder = ConfigHolder.getInstance().getStringValue("proposal_docs_folder","/mnt/game/proposal");
        this.targetFile = targetFolder+"/"+quoteData.getProposalHeader().getId() + "/BookingFormOfficeUse.pdf";
    }

    @Override
    public void create() {
        new OfficeUseOnlyPdf(proposalHeader).cretePdf(targetFile);
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
