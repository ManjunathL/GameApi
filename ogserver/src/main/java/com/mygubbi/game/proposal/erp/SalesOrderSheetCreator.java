package com.mygubbi.game.proposal.erp;

import com.mygubbi.game.proposal.ProductModule;
import com.mygubbi.game.proposal.quote.AssembledProductInQuote;
import com.mygubbi.game.proposal.quote.QuoteData;
import com.mygubbi.si.excel.ExcelCellProcessor;
import com.mygubbi.si.excel.ExcelSheetProcessor;
import com.mygubbi.si.excel.ExcelStyles;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

/**
 * Created by Sunil on 22-05-2016.
 */
public class SalesOrderSheetCreator implements ExcelCellProcessor
{
    private final static Logger LOG = LogManager.getLogger(SalesOrderSheetCreator.class);

    private final AssembledProductInQuote product;
    private Sheet soSheet;
    private ExcelStyles styles;
    private ExcelSheetProcessor sheetProcessor;
    private QuoteData quoteData;

    public SalesOrderSheetCreator(Sheet soSheet, QuoteData quoteData, ExcelStyles styles)
    {
        this.soSheet = soSheet;
        this.styles = styles;
        this.product = quoteData.getAssembledProducts().get(0);
        this.quoteData = quoteData;
    }

    public void prepare()
    {
        this.sheetProcessor = new ExcelSheetProcessor(this.soSheet, this.styles, this);
        this.sheetProcessor.process();
    }

    @Override
    public Object getValueForKey(String key)
    {
        return "";
    }

    @Override
    public void processCell(Cell cell, String cellValue)
    {
        switch (cellValue)
        {
            case "SrNo":
                this.fillSheet(cell.getRow().getRowNum() + 1);
                break;

            default:
                break;
        }

    }

    private int fillSheet(int currentRow)
    {
        List<ProductModule> modules = this.product.getModules();
     /*   {
            for(ProductModule module:modules)
            {
                LOG.info("product " +module.getMGCode());
                currentRow=this.writeTitle(currentRow,module.getMGCode());
                Collection<ModuleComponent> accPacks1 = ModuleDataService.getInstance().getModuleComponents(module.getMGCode());
                {
                    currentRow=this.writeTitle(currentRow,"Module Hardware");
                    for (ModuleComponent moduleComponent : accPacks1)
                    {

                        if (moduleComponent.isHardware()) {
                            AccHwComponent hardware = ModuleDataService.getInstance().getHardware(moduleComponent.getComponentCode());
                            currentRow=this.WriteData(currentRow,"1",hardware.getCode(),hardware.getUom(),0.0,hardware.getTitle(),hardware.getCatalogCode());
                            LOG.info("hardware" +hardware.toString());
                        }
                    }
                }

                for (ModuleAccessoryPack modAccessoryPack : module.getAccessoryPacks())
                {
                    LOG.info("Acc pack" +modAccessoryPack);
                    currentRow=this.writeTitle(currentRow,"Accessory pack");
                    Collection<AccessoryPackComponent> accessoryPackComponents =
                            ModuleDataService.getInstance().getAccessoryPackComponents(modAccessoryPack.getAccessoryPackCode());
                    for (AccessoryPackComponent accessoryPackComponent : accessoryPackComponents) {
                        if (accessoryPackComponent.isAccessory()) {
                            AccHwComponent accessory = ModuleDataService.getInstance().getAccessory(accessoryPackComponent.getComponentCode());
                            currentRow=this.WriteData(currentRow,"1",accessory.getCode(),accessory.getUom(),0.0,accessory.getTitle(),accessory.getCatalogCode());

                            LOG.info("selected accc " +accessory);

                        } else if (accessoryPackComponent.isHardware())
                        {
                            currentRow=this.writeTitle(currentRow,"Accessory hardware");
                            AccHwComponent hardware = ModuleDataService.getInstance().getHardware(accessoryPackComponent.getComponentCode());
                            currentRow=this.WriteData(currentRow,"1",hardware.getCode(),hardware.getUom(),0.0,hardware.getTitle(),hardware.getCatalogCode());

                            LOG.info("Selected Hardware " +hardware);
                        }
                        for (String code : modAccessoryPack.getAddons())
                        {
                            currentRow=this.writeTitle(currentRow,"Addons");
                            AccHwComponent accessory = ModuleDataService.getInstance().getAccessory(code);
                            currentRow=this.WriteData(currentRow,"1",accessory.getCode(),accessory.getUom(),0.0,accessory.getTitle(),accessory.getCatalogCode());

                            LOG.info("Selected addons" +accessory);

                        }
                    }
                }
            }
        }
        if (modules == null || modules.isEmpty())
        {
            currentRow++;
            this.sheetProcessor.createDataRowInDataSheet(currentRow, new String[]{"No components"});
            return currentRow;
        }

        List<AssembledProductInQuote.ModulePart> aggregatedModules=this.product.getAggregatedModules();
        for(AssembledProductInQuote.ModulePart modulePart:aggregatedModules)
        {
            LOG.info("Acc pack hw" +this.product.getAggregatedAccessoryPackHardware());
        }*/

        //currentRow = this.writeRecords(currentRow, this.product.getAggregatedAccessoryPackPanels());
        currentRow = this.writeRecords(currentRow, this.product.getAggregatedAccessoryPackHardware());
        currentRow = this.writeRecords(currentRow, this.product.getAggregatedAccessoryPackAccessories());
        currentRow = this.writeRecords(currentRow, this.product.getAggregatedAccessoryAddons());
        currentRow = this.writeRecords(currentRow, this.product.getAggregatedAddons());
        currentRow = this.writeRecords(currentRow, this.product.getAggregatedKnobAndHandle());
        currentRow = this.writeRecords(currentRow, this.product.getAggregatedHingePack());

        return currentRow;
    }


    private int writeRecords(int currentRow, List<AssembledProductInQuote.ModulePart> parts)
    {
        for (AssembledProductInQuote.ModulePart modulePart : parts)
        {
            LOG.info("module part" +modulePart);
            this.sheetProcessor.createDataRowInDataSheet(currentRow, new Object[]{currentRow,modulePart.ERPCode, modulePart.uom, modulePart.quantity , modulePart.catalogCode,modulePart.title });
            currentRow++;
        }
        return currentRow;
    }

    /*private int writeTitle(int currentRow,String title)
    {
        this.sheetProcessor.createDataRowInDataSheet(currentRow, new Object[]{currentRow, title});
        currentRow++;

        return currentRow;
    }

    private int WriteData(int currentRow,String serialnum, String item, String UOM, double quantity, String title,String catalogcode)
    {
        this.sheetProcessor.createDataRowInDataSheet(currentRow, new Object[]{currentRow, serialnum,item,UOM,quantity,title,catalogcode});
            currentRow++;

        return currentRow;
    }*/



}
