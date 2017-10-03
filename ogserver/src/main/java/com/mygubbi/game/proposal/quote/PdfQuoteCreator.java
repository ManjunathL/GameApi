package com.mygubbi.game.proposal.quote;

import com.mygubbi.config.ConfigHolder;
import com.mygubbi.game.proposal.model.ProposalHeader;
import com.mygubbi.game.proposal.output.ProposalOutputCreator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

/**
 * Created by Chirag on 23-11-2016.
 */
public class PdfQuoteCreator implements ProposalOutputCreator {

    private final static Logger LOG = LogManager.getLogger(PdfQuoteCreator.class);


    private QuoteData quoteData;
    private ProposalHeader proposalHeader;
    private String targetFile;
    private String targetFileForWorksContract;
    private boolean isValid_Sow;

    public PdfQuoteCreator(QuoteData quoteData, ProposalHeader proposalHeader,boolean isValid_Sow){
        this.quoteData = quoteData;
        this.proposalHeader = proposalHeader;
        String targetFolder = ConfigHolder.getInstance().getStringValue("proposal_docs_folder","/mnt/game/proposal");
        this.targetFile = targetFolder+"/"+quoteData.getProposalHeader().getId() + "/quotation.pdf";
        this.targetFileForWorksContract=targetFolder+"/"+quoteData.getProposalHeader().getId() + "/workscontract.pdf";
        this.isValid_Sow = isValid_Sow;

    }

    @Override
    public void create() {

        LOG.debug("PATH :" + this.getOutputFile());
        if(Objects.equals(proposalHeader.getPackageFlag(), "Yes"))
        {
            LOG.info("if");
            new QuotationPdfCreatorForPackage(quoteData,proposalHeader).createpdf(targetFile);
            //here take target file as input
        }else {
            LOG.info("else");
            new QuotationPDFCreator(quoteData, proposalHeader).createpdf(targetFile,isValid_Sow);

        }
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
