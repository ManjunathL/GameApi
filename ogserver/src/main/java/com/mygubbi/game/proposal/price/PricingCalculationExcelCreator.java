package com.mygubbi.game.proposal.price;

import com.mygubbi.common.VertxInstance;
import com.mygubbi.config.ConfigHolder;
import com.mygubbi.si.excel.ExcelWorkbookManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Sunil on 22-05-2016.
 */
public class PricingCalculationExcelCreator
{
    private final static Logger LOG = LogManager.getLogger(PricingCalculationExcelCreator.class);

    protected ExcelWorkbookManager workbookManager;
    private String outputFile;
    private ModulePriceHolder priceHolder;

    public PricingCalculationExcelCreator(ModulePriceHolder priceHolder)
    {
        this.priceHolder = priceHolder;
    }

    public String getOutputFilename()
    {
        return "module-pricing-" + this.priceHolder.getName() + "-" + System.currentTimeMillis() + ".xlsx";
    }

    public void create()
    {
        this.openWorkbook();

        new PriceSheetCreator(workbookManager.getSheetByName("Module Price Calculation"), this.priceHolder, workbookManager.getStyles()).prepare();

        this.closeWorkbook();
    }

    protected void openWorkbook()
    {
        this.outputFile = this.copyTemplateFile();
        this.workbookManager = new ExcelWorkbookManager(this.outputFile);
    }

    public String getOutputFile()
    {
        return outputFile;
    }

    private String copyTemplateFile()
    {
        String templateFile = ConfigHolder.getInstance().getStringValue("templates_folder", "/tmp") + "/module_price_calculation.xlsx";
        String targetFile = ConfigHolder.getInstance().getStringValue("price_files_folder", "/tmp") + "/" + this.getOutputFilename();
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
                    + " for module " + this.priceHolder.getName() + ".", e);
        }
        return targetFile;
    }

    protected void closeWorkbook()
    {
        this.workbookManager.closeWorkbook();
    }

}
