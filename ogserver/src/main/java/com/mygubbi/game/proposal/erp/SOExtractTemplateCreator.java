package com.mygubbi.game.proposal.erp;

import com.mygubbi.common.VertxInstance;
import com.mygubbi.config.ConfigHolder;
import com.mygubbi.game.proposal.model.ERPMaster;
import com.mygubbi.game.proposal.model.ProposalHeader;
import com.mygubbi.game.proposal.model.ProposalBOQ;
import com.mygubbi.game.proposal.model.SOPart;
import com.mygubbi.si.excel.ExcelWorkbookManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.List;

/**
 * Created by Sunil on 22-05-2016.
 */
public class SOExtractTemplateCreator
{
    private final static Logger LOG = LogManager.getLogger(SOExtractTemplateCreator.class);

    private List<SOPart> proposalBoqs;
    protected ExcelWorkbookManager workbookManager;
    private String outputFile;
    private int productId;
    private int proposalId;


    public SOExtractTemplateCreator(List<SOPart> proposalBoqs, int productId, int proposalId)
    {
        this.proposalBoqs = proposalBoqs;
        this.productId = productId;
        this.proposalId = proposalId;
    }

    public String getTemplateName()
    {
        return  "so_template";
    }

    public String getOutputFilename()
    {
        return  productId+"/so.xlsx" ;
    }

    public String create()
    {
        this.openWorkbook();
        new SOSheetCreator((XSSFSheet) this.workbookManager.getSheetByName("SO"),this.workbookManager.getStyles(),proposalBoqs).prepare();
        this.closeWorkbook();
        return outputFile;
    }



    public String getOutputKey()
    {
        return "soFile";
    }

    public static void main(String[] args)
    {

    }

    private String copyTemplateFile()
    {
        String templateName = this.getTemplateName();
        LOG.info("&&&&" +templateName);
        String templateFile = ConfigHolder.getInstance().getStringValue(templateName, "/tmp/" + this.getTemplateName() + ".xlsx");
        String targetFile = ConfigHolder.getInstance().getStringValue("proposal_docs_folder","/mnt/game/proposal/") + "/"+ proposalId + "/" + this.getOutputFilename();
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
                    + " for proposal " + proposalId + ".", e);
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
