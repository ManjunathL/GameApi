package com.mygubbi.game.proposal.quote;

import com.hazelcast.core.LifecycleService;
import com.mygubbi.config.ConfigHolder;
import com.mygubbi.game.proposal.model.ProposalHeader;
import com.mygubbi.game.proposal.model.ProposalVersion;
import com.mygubbi.game.proposal.output.ProposalOutputCreator;
import com.mygubbi.game.proposal.sow.SpaceRoom;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Objects;

/**
 * Created by Chirag on 23-11-2016.
 */
public class PdfQuoteCreator implements ProposalOutputCreator {

    private final static Logger LOG = LogManager.getLogger(PdfQuoteCreator.class);


    private QuoteData quoteData;
    private ProposalHeader proposalHeader;
    private ProposalVersion proposalVersion;
    private List<SpaceRoom> spaceRoomList;
    private String targetFile;
    private String targetFileForWorksContract;
    private boolean isValid_Sow;
    java.util.Date date;
    java.util.Date quoteChangeDate = new java.util.Date(118 ,1,7,0,0,00);

    public PdfQuoteCreator(QuoteData quoteData, ProposalHeader proposalHeader, boolean isValid_Sow, ProposalVersion proposalVersion,List<SpaceRoom> spaceRooms){
        this.quoteData = quoteData;
        this.proposalHeader = proposalHeader;
        date=proposalHeader.getPriceDate();
        this.spaceRoomList=spaceRooms;
        String targetFolder = ConfigHolder.getInstance().getStringValue("proposal_docs_folder","/mnt/game/proposal");
        this.targetFile = targetFolder+"/"+quoteData.getProposalHeader().getId() + "/quotation.pdf";
        this.targetFileForWorksContract=targetFolder+"/"+quoteData.getProposalHeader().getId() + "/workscontract.pdf";
        this.isValid_Sow = isValid_Sow;
        this.proposalVersion=proposalVersion;
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
            if(date.after(quoteChangeDate))
            {
                new QuotationPdfRoomWise(quoteData, proposalHeader,proposalVersion,spaceRoomList).createpdf(targetFile,isValid_Sow);
            }else
            {
                new QuotationPDFCreator(quoteData, proposalHeader,proposalVersion,spaceRoomList).createpdf(targetFile,isValid_Sow);
            }
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
