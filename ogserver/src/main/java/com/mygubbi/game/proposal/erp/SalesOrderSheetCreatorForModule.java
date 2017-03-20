package com.mygubbi.game.proposal.erp;

import com.mygubbi.common.StringUtils;
import com.mygubbi.game.proposal.ProductModule;
import com.mygubbi.game.proposal.quote.AssembledProductInQuote;
import com.mygubbi.game.proposal.quote.QuoteData;
import com.mygubbi.si.excel.ExcelCellProcessor;
import com.mygubbi.si.excel.ExcelSheetProcessor;
import com.mygubbi.si.excel.ExcelStyles;
import org.apache.logging.log4j.LogManager;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

/**
 * Created by user on 20-Mar-17.
 */
public class SalesOrderSheetCreatorForModule implements ExcelCellProcessor
{
    private final static org.apache.logging.log4j.Logger LOG = LogManager.getLogger(SalesOrderSheetCreatorForModule.class);

    private final AssembledProductInQuote product;
    private Sheet soSheet;
    private ExcelStyles styles;
    private ExcelSheetProcessor sheetProcessor;
    private QuoteData quoteData;

    public SalesOrderSheetCreatorForModule(Sheet soSheet, QuoteData quoteData, ExcelStyles styles)
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
    public Object getValueForKey(String key) {
        return null;
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
    private void fillSheet(int currentrow)
    {
        List<ProductModule> modules = this.product.getModules();
        int sequenceNumberforModule=1;
        for(ProductModule module:modules)
        {
            this.createDataRowInDataSheet(currentrow, new String[]{String.valueOf(sequenceNumberforModule),module.getUnit(), module.getMGCode()});
            sequenceNumberforModule++;
            currentrow++;
        }
    }

    /*private void processSalesSheet()
    {
        int startRow = 1;
        int sequenceNumber = 1;

        for (AssembledProductInQuote product : this.quoteData.getAssembledProducts())
        {
            startRow = this.fillAssembledProductInDataSheet(startRow, sequenceNumber, product);
            startRow += 2;
            sequenceNumber++;
        }

    }


    private int fillAssembledProductInDataSheet(int startRow, int sequenceNumber, AssembledProductInQuote product)
    {
        int currentRow = startRow;
        int sequenceNumberforModule = 1;
        int seqnumforacc=1;

        List<ProductModule> modules = this.product.getModules();
        {
            for (ProductModule module : modules) {
                this.createDataRowInDataSheet(currentRow, new String[]{String.valueOf(sequenceNumberforModule), module.getMGCode()});
                currentRow += 2;
                sequenceNumberforModule++;
                currentRow = this.fillAccessoriesInDataSheet(module, currentRow,seqnumforacc);
            }
        }

*//*
        currentRow += 2;
        currentRow = this.fillHardwareInDataSheet(product.getModuleHardware(), currentRow);
*//*

        currentRow++;

        return currentRow;
    }

    private int fillAccessoriesInDataSheet(ProductModule module, int currentRow,int sequenceNumber)
    {
        this.createTitleRowInDataSheet(currentRow, new String[]{null, "Serial No", "Item", "UOM", "QTY", "Title", "CODe"});
        currentRow++;
        Collection<ModuleComponent> accPacks1 = ModuleDataService.getInstance().getModuleComponents(module.getMGCode());
        {
            for (ModuleComponent moduleComponent : accPacks1)
            {
                if (moduleComponent.isHardware()) {
                    AccHwComponent hardware = ModuleDataService.getInstance().getHardware(moduleComponent.getComponentCode());

                        currentRow++;
                        this.createDataRowInDataSheet(currentRow, new String[]{null,String.valueOf(sequenceNumber),hardware.getCode(),
                                hardware.getUom(),"QTY", String.valueOf(hardware.getTitle()), hardware.getCatalogCode()});
                        sequenceNumber++;
                    LOG.info("hardware" +hardware.toString());
                }
            }
        }

        for (ModuleAccessoryPack modAccessoryPack : module.getAccessoryPacks())
        {
            LOG.info("Acc pack" +modAccessoryPack);
            //currentRow=this.writeTitle(currentRow,"Accessory pack");
            Collection<AccessoryPackComponent> accessoryPackComponents =
                    ModuleDataService.getInstance().getAccessoryPackComponents(modAccessoryPack.getAccessoryPackCode());
            for (AccessoryPackComponent accessoryPackComponent : accessoryPackComponents) {
                if (accessoryPackComponent.isAccessory()) {
                    AccHwComponent accessory = ModuleDataService.getInstance().getAccessory(accessoryPackComponent.getComponentCode());
                    //currentRow=this.WriteData(currentRow,"1",accessory.getCode(),accessory.getUom(),0.0,accessory.getTitle(),accessory.getCatalogCode());
                    currentRow++;
                    this.createDataRowInDataSheet(currentRow, new String[]{null,String.valueOf(sequenceNumber),accessory.getCode(),
                            accessory.getUom(),"QTY", String.valueOf(accessory.getTitle()), accessory.getCatalogCode()});
                    sequenceNumber++;
                    LOG.info("selected accc " +accessory);

                } else if (accessoryPackComponent.isHardware())
                {
                    //currentRow=this.writeTitle(currentRow,"Accessory hardware");
                    AccHwComponent hardware = ModuleDataService.getInstance().getHardware(accessoryPackComponent.getComponentCode());
                    //currentRow=this.WriteData(currentRow,"1",hardware.getCode(),hardware.getUom(),0.0,hardware.getTitle(),hardware.getCatalogCode());
                    currentRow++;
                    this.createDataRowInDataSheet(currentRow, new String[]{null,String.valueOf(sequenceNumber),hardware.getCode(),
                            hardware.getUom(),"QTY", String.valueOf(hardware.getTitle()), hardware.getCatalogCode()});
                    sequenceNumber++;

                    LOG.info("Selected Hardware " +hardware);
                }
                for (String code : modAccessoryPack.getAddons())
                {
                    //currentRow=this.writeTitle(currentRow,"Addons");
                    AccHwComponent accessory = ModuleDataService.getInstance().getAccessory(code);
                    //currentRow=this.WriteData(currentRow,"1",accessory.getCode(),accessory.getUom(),0.0,accessory.getTitle(),accessory.getCatalogCode());
                    currentRow++;
                    this.createDataRowInDataSheet(currentRow, new String[]{null,String.valueOf(sequenceNumber),accessory.getCode(),
                            accessory.getUom(),"QTY", String.valueOf(accessory.getTitle()), accessory.getCatalogCode()});
                    sequenceNumber++;
                    LOG.info("Selected addons" +accessory);

                }
            }
        }
        currentRow++;
        return currentRow;
        //return this.fillAccHwInDataSheet(accessories, currentRow, "Accessories", "No accessories.");
    }

    private int fillAccInDataSheet(Collection<AccHwComponent> hardwares, int currentRow)
    {
        return this.fillAccHwInDataSheet(hardwares, currentRow, "Accessories", "No hardware.");
    }

    *//*private int fillHardwareInDataSheet(List<AssembledProductInQuote.ModulePart> hardwares, int currentRow)
    {
        return this.fillAccHwInDataSheet(hardwares, currentRow, "Hardware", "No hardware.");
    }*//*

    private int fillAccHwInDataSheet(Collection<AccHwComponent> components, int currentRow, String type, String defaultMessage)
    {
        if (components == null || components.isEmpty())
        {
            currentRow++;
            this.createDataRowInDataSheet(currentRow, new String[]{defaultMessage});
            return currentRow;
        }

        for (AccHwComponent component : components)
        {
            currentRow++;
            this.createDataRowInDataSheet(currentRow, new String[]{null,"1", String.valueOf(component.getTitle()),
                    component.getUom(),"QTY", String.valueOf(component.getTitle()), component.getCatalogCode()});
        }
        return currentRow;
    }*/

    private void createDataRowInDataSheet(int rowNum, String [] data)
    {
        this.createRowInDataSheet(rowNum, data, false);
    }

   /* private void createTitleRowInDataSheet(int rowNum, String [] data)
    {
        this.createRowInDataSheet(rowNum, data, true);
    }*/

    private void createRowInDataSheet(int rowNum, String [] data, boolean isTitle)
    {
        Row dataRow = this.soSheet.createRow(rowNum);
        int lastCell = data.length;
        for (int cellNum = 0; cellNum < lastCell; cellNum++)
        {
            String value = data[cellNum];
            if (StringUtils.isNonEmpty(value))
            {
                Cell cell = dataRow.createCell(cellNum, Cell.CELL_TYPE_STRING);
                cell.setCellValue(value);
                if (isTitle)
                {
                    cell.setCellStyle(this.styles.getBoldStyle());
                }
            }
        }
    }

}
