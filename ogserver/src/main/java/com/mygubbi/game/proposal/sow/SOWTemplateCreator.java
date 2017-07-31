package com.mygubbi.game.proposal.sow;

import com.mygubbi.common.VertxInstance;
import com.mygubbi.config.ConfigHolder;
import com.mygubbi.game.proposal.model.ProposalHeader;
import com.mygubbi.game.proposal.model.ProposalSOW;
import com.mygubbi.si.excel.ExcelWorkbookManager;
import com.mygubbi.si.gdrive.DriveFile;
import com.mygubbi.si.gdrive.DriveServiceProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Sunil on 22-05-2016.
 */
public class SOWTemplateCreator
{
    private final static Logger LOG = LogManager.getLogger(SOWTemplateCreator.class);

    protected ExcelWorkbookManager workbookManager;
    protected ProposalHeader proposalHeader;
    private String outputFile;
    private DriveFile driveFile;
    public DriveServiceProvider driveServiceProvider = new DriveServiceProvider();
    public List<ProposalSOW> proposal_sows;
    private String sowVersion;


    public SOWTemplateCreator(ProposalHeader proposalHeader, List<ProposalSOW> proposal_sow, String sowVersion)
    {
        this.proposalHeader = proposalHeader;
        this.proposal_sows = proposal_sow;
        this.sowVersion = sowVersion;
    }

    public String getTemplateName()
    {
        return  "sow_template";
    }

    public String getOutputFilename()
    {


        int randomNum = ThreadLocalRandom.current().nextInt(1, 1000 + 1);
        return  randomNum + "/sow.xlsx" ;
    }


    public String create()
    {
        this.openWorkbook();
        new SowSheetCreator((XSSFSheet) this.workbookManager.getSheetByName("Scope Of Services"),this.workbookManager.getStyles(),this.proposalHeader,proposal_sows, sowVersion).prepare();
        this.closeWorkbook();
        return outputFile;
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

    public String getOutputFile()
    {
        return outputFile;
    }




}
