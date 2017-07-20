package com.mygubbi.game.proposal.erp;

import com.mygubbi.game.proposal.model.ProposalHeader;
import com.mygubbi.game.proposal.output.AbstractProposalOutputCreator;
import com.mygubbi.game.proposal.quote.QuoteData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFSheet;

/**
 * Created by Sunil on 22-05-2016.
 */
public class BOQTemplateCreator extends AbstractProposalOutputCreator
{
    private final static Logger LOG = LogManager.getLogger(BOQTemplateCreator.class);

    public BOQTemplateCreator(QuoteData quoteData, ProposalHeader proposalHeader)
    {
        super(quoteData,proposalHeader);
    }

    public String getTemplateName()
    {
        return  "boq_template";
    }

    public String getOutputFilename()
    {
        return "/boq.xlsx";
    }


    public void create()
    {
        this.openWorkbook();
        new BOQSheetCreator(this.workbookManager,(XSSFSheet) this.workbookManager.getSheetByName("BOQ"),this.quoteData,this.workbookManager.getStyles()).prepare();
        this.closeWorkbook();
    }


    @Override
    public String getOutputKey()
    {
        return "boqFile";
    }

    public static void main(String[] args)
    {

    }

}
