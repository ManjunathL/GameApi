package com.mygubbi.game.proposal.erp;

import com.mygubbi.common.VertxInstance;
import com.mygubbi.config.ConfigHolder;
import com.mygubbi.game.proposal.model.ERPMaster;
import com.mygubbi.game.proposal.model.ProposalHeader;
import com.mygubbi.game.proposal.model.ProposalBOQ;
import com.mygubbi.si.excel.ExcelWorkbookManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.List;

/**
 * Created by Sunil on 22-05-2016.
 */
public class BOQTemplateCreator
{
    private final static Logger LOG = LogManager.getLogger(BOQTemplateCreator.class);

    private List<ProposalBOQ> proposalBoqsForProduct;
    private List<ProposalBOQ> proposalBoqsForAddon;
    private ProposalHeader proposalHeader;
    protected ExcelWorkbookManager workbookManager;
    private String outputFile;
    private List<ERPMaster> erpMasters;


    public BOQTemplateCreator(ProposalHeader proposalHeader, List<ProposalBOQ> proposalBoqsForProduct,List<ProposalBOQ> proposalBoqsForAddon, List<ERPMaster> erpMasters)
    {
        this.proposalHeader = proposalHeader;
        this.proposalBoqsForProduct = proposalBoqsForProduct;
        this.proposalBoqsForAddon = proposalBoqsForAddon;
        this.erpMasters = erpMasters;
        LOG.debug("ERP Master size in BOQ template creator" + erpMasters.size());
    }

    public String getTemplateName()
    {
        return  "boq_template";
    }

    public String getOutputFilename()
    {
        return "/boq.xlsx";
    }


    public String create()
    {
        this.openWorkbook();
        new BOQSheetCreator(this.workbookManager,(XSSFSheet) this.workbookManager.getSheetByName("BOQ"),this.workbookManager.getStyles(), proposalBoqsForProduct,proposalBoqsForAddon, proposalHeader).prepare();
        new ERPMasterSheetCreator(this.workbookManager,(XSSFSheet) this.workbookManager.getSheetByName("erp"),this.workbookManager.getStyles(), erpMasters, proposalHeader).prepare();
        this.closeWorkbook();
        return outputFile;
    }



    public String getOutputKey()
    {
        return "boqFile";
    }

    public static void main(String[] args)
    {

    }

    private String copyTemplateFile()
    {
        String templateName = this.getTemplateName();
        LOG.info("&&&&" +templateName);
        String templateFile = ConfigHolder.getInstance().getStringValue(templateName, "/tmp/" + this.getTemplateName() + ".xlsx");
        String targetFile = ConfigHolder.getInstance().getStringValue("proposal_docs_folder","/mnt/game/proposal/") + "/"+ proposalHeader.getId() + "/" + this.getOutputFilename();
        try
        {
            VertxInstance.get().fileSystem().deleteBlocking(targetFile);
        }
        catch (Exception e)
        {
            //Nothing to do
        }
        try
        {
            VertxInstance.get().fileSystem().copyBlocking(templateFile, targetFile);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error in copying template file " + templateFile + "to " + targetFile
                    + " for proposal " + this.proposalHeader.getId() + ".", e);
        }
        return targetFile;
    }

    protected void openWorkbook()
    {
        this.outputFile = this.copyTemplateFile();
        this.workbookManager = new ExcelWorkbookManager(this.outputFile);
    }

    protected void closeWorkbook()
    {
        this.workbookManager.closeWorkbook();
    }


}
