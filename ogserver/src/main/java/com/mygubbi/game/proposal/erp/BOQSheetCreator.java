package com.mygubbi.game.proposal.erp;

import com.mygubbi.game.proposal.ModuleAccessoryPack;
import com.mygubbi.game.proposal.ModuleDataService;
import com.mygubbi.game.proposal.ProductAddon;
import com.mygubbi.game.proposal.ProductModule;
import com.mygubbi.game.proposal.jobcard.AddonsSheetCreator;
import com.mygubbi.game.proposal.model.AccHwComponent;
import com.mygubbi.game.proposal.model.AccessoryPackComponent;
import com.mygubbi.game.proposal.model.ModuleComponent;
import com.mygubbi.game.proposal.model.PriceMaster;
import com.mygubbi.game.proposal.price.AccessoryComponent;
import com.mygubbi.game.proposal.price.RateCardService;
import com.mygubbi.game.proposal.quote.AssembledProductInQuote;
import com.mygubbi.game.proposal.quote.QuoteData;
import com.mygubbi.si.excel.ExcelCellProcessor;
import com.mygubbi.si.excel.ExcelSheetProcessor;
import com.mygubbi.si.excel.ExcelStyles;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.sql.Date;
import java.util.Collection;
import java.util.List;

/**
 * Created by user on 27-May-17.
 */
public class BOQSheetCreator implements ExcelCellProcessor
{
    private final static Logger LOG = LogManager.getLogger(BOQSheetCreator.class);

    private static final int SPACE_TYPE_CELL = 0;
    private static final int ROOM_CELL = 1;
    private static final int CATEGORY_CELL = 2;
    private static final int PRODUCT_OR_SERVICE = 3;
    private static final int MODULE_CODE = 4;
    private static final int CUSTOM_CHECK = 5;
    private static final int CUSTOM_REMARKS = 6;
    private static final int COMPONENT_CATEGORY = 7;
    private static final int DSO_ERP_ITEM_CODE = 8;
    private static final int DSO_REFERENCE_PART_NO = 9;
    private static final int DSO_UOM = 10;
    private static final int DSO_RATE = 11;
    private static final int DSO_QTY = 12;
    private static final int DSO_PRICE = 13;
    private static final int PLANNER_ERP_ITEM_CODE = 14;
    private static final int PLANNER_UOM = 15;
    private static final int PLANNER_RATE = 16;
    private static final int PLANNER_QTY = 17;
    private static final int PLANNER_PRICE = 18;
    private static final int SPACE_TYPE_HIDDEN = 19;
    private static final int ROOM_HIDDEN = 20;
    private static final int CATEGORY_HIDDEN = 21;
    private static final int PRODUCT_HIDDEN = 22;
    private static final int MODULE_HIDDEN = 23;


    private QuoteData quoteData;
    private XSSFSheet quoteSheet;
    private ExcelStyles styles;
    private ExcelSheetProcessor sheetProcessor;
    private Date priceDate;
    private String city;
    private XSSFWorkbook quoteWorkBook;
    public BOQSheetCreator(XSSFWorkbook xssfWorkbook,XSSFSheet quoteSheet, QuoteData quoteData, ExcelStyles styles)
    {
        this.quoteWorkBook = xssfWorkbook;
        this.quoteSheet = quoteSheet;
        this.quoteData = quoteData;
        this.styles = styles;
        this.priceDate = quoteData.getProposalHeader().getPriceDate();
        this.city = quoteData.getProposalHeader().getProjectCity();
    }

    public void prepare()
    {
        this.sheetProcessor = new ExcelSheetProcessor(this.quoteSheet, this.styles, this);
        this.sheetProcessor.process();
        this.quoteSheet.protectSheet("mygubbi");
    }


    @Override
    public Object getValueForKey(String key)
    {
        return this.quoteData.getValue(key);
    }

    @Override
    public void processCell(Cell cell, String cellValue)
    {
        switch (cellValue)
        {
            case "Space Type":
                //int currentRow = this.fillAssembledProducts(cell.getRow().getRowNum());
                this.fillModules(cell.getRow().getRowNum() + 1);
                break;

            default:
                break;
        }
    }

    public int fillModules(int currentRow)
    {
        List<AssembledProductInQuote> assembledProducts = this.quoteData.getAssembledProducts();
        if (assembledProducts.isEmpty())
        {
            return currentRow;
        }
        for (AssembledProductInQuote productInQuote : assembledProducts)
        {
            currentRow = this.fillHardwareAndAccPerModule(currentRow, productInQuote);
            currentRow++;
        }
        currentRow++;
        currentRow = fillProductAddons(currentRow);
        return currentRow;

    }

    private int fillProductAddons(int currentRow) {
        List<ProductAddon> addon_accessories = this.quoteData.getAccessories();
        List<ProductAddon> appliances = this.quoteData.getAppliances();
        List<ProductAddon> counter_tops = this.quoteData.getCounterTops();
        List<ProductAddon> loose_furniture = this.quoteData.getLooseFurniture();
        List<ProductAddon> services = this.quoteData.getServices();

        if (addon_accessories.size() != 0)
        {
            for (ProductAddon productAddon : addon_accessories)
            {
                this.createAddonData(currentRow,productAddon);
                currentRow++;
            }
        }

        if (appliances.size() != 0)
        {
            for (ProductAddon productAddon : appliances)
            {
                this.createAddonData(currentRow,productAddon);
                currentRow++;
            }
        }

        if (counter_tops.size() != 0)
        {
            for (ProductAddon productAddon : counter_tops)
            {
                this.createAddonData(currentRow,productAddon);
                currentRow++;
            }
        }

        if (loose_furniture.size() != 0)
        {
            for (ProductAddon productAddon : loose_furniture)
            {
                this.createAddonData(currentRow,productAddon);
                currentRow++;
            }
        }

        if (services.size() != 0)
        {
            for (ProductAddon productAddon : services)
            {
                this.createAddonData(currentRow,productAddon);
                currentRow++;
            }
        }
        return currentRow;
    }


    private int fillHardwareAndAccPerModule(int startRow, AssembledProductInQuote productInQuote)
    {
        int currentRow = startRow;
        String spaceType = productInQuote.getSpaceType();
        String room = productInQuote.getRoom();
        String product = productInQuote.getProduct().getProductCategory();

        List<ProductModule> modules = productInQuote.getModules();

        for (ProductModule module : modules) {
            Collection<ModuleComponent> moduleComponents = ModuleDataService.getInstance().getModuleComponents(module.getMGCode());

            this.createModuleHeadingRow(currentRow,module,spaceType,room,product);
            currentRow++;

            for (ModuleComponent moduleComponent: moduleComponents)
            {
                if (moduleComponent.getType().equals("H"))
                {

                    this.createModuleComponentDataRow(currentRow, moduleComponent, spaceType ,room, product, module);
                    currentRow++;
                }
            }

            currentRow = fillDataForAccessoryPacks(currentRow, spaceType , room, product, module);

            currentRow++;
        }

        return currentRow;
    }

    private int fillDataForAccessoryPacks(int currentRow,String spaceType, String room, String product ,ProductModule module) {
        for (ModuleAccessoryPack modAccessoryPack : module.getAccessoryPacks())
        {
            if (!(module.getAccessoryPacks().size() == 0))
            {
                Collection<AccessoryPackComponent> accessoryComponents = ModuleDataService.getInstance().getAccessoryPackComponents(modAccessoryPack.getAccessoryPackCode());
                for (AccessoryPackComponent accessoryPackComponent : accessoryComponents)
                {
                    AccHwComponent accHwComponent = null;
                    if (accessoryPackComponent.getType().equals("H") || accessoryPackComponent.getType().equals("A"))
                    {
                    if (accessoryPackComponent.getType().equals("H"))
                    {
                        accHwComponent = ModuleDataService.getInstance().getHardware(accessoryPackComponent.getComponentCode());
                    }
                        else if (accessoryPackComponent.getType().equals("A"))
                    {
                        accHwComponent = ModuleDataService.getInstance().getAccessory(accessoryPackComponent.getComponentCode());
                    }
                        this.createModuleAccessoryPackRow(currentRow,accHwComponent,accessoryPackComponent, spaceType, room, product, module);
                        currentRow++;
                    }
                }
                currentRow++;
            }
        }
        return currentRow;
    }


    private void createModuleHeadingRow(int rowNum, ProductModule module, String spaceType, String room, String product )
    {
        LOG.debug("Create Module heading row" + rowNum);
        Row dataRow = this.createRow(rowNum, this.quoteSheet);
        String remarks = "";
        String customCheck = "No";

        //Set header details for module
        this.createCellWithData(dataRow, SPACE_TYPE_CELL, Cell.CELL_TYPE_STRING, spaceType).setCellStyle(this.styles.getColoredCellStyle());
        this.createCellWithData(dataRow, ROOM_CELL, Cell.CELL_TYPE_STRING, room).setCellStyle(this.styles.getColoredCellStyle());
        this.createCellWithData(dataRow, CATEGORY_CELL, Cell.CELL_TYPE_STRING,"Modular products").setCellStyle(this.styles.getColoredCellStyle());
        this.createCellWithData(dataRow, PRODUCT_OR_SERVICE, Cell.CELL_TYPE_STRING, product).setCellStyle(this.styles.getColoredCellStyle());
        this.createCellWithData(dataRow, MODULE_CODE, Cell.CELL_TYPE_STRING, module.getMGCode()).setCellStyle(this.styles.getColoredCellStyle());
        this.createCellWithData(dataRow, CUSTOM_CHECK, Cell.CELL_TYPE_STRING, customCheck).setCellStyle(this.styles.getColoredCellStyle());
        this.createCellWithData(dataRow, CUSTOM_REMARKS, Cell.CELL_TYPE_STRING, remarks).setCellStyle(this.styles.getColoredCellStyle());

        this.createCellWithData(dataRow, SPACE_TYPE_HIDDEN, Cell.CELL_TYPE_STRING, spaceType).setCellStyle(this.styles.getColoredCellStyle());
        this.createCellWithData(dataRow, ROOM_HIDDEN, Cell.CELL_TYPE_STRING, room).setCellStyle(this.styles.getColoredCellStyle());
        this.createCellWithData(dataRow, CATEGORY_HIDDEN, Cell.CELL_TYPE_STRING, "Modular products").setCellStyle(this.styles.getColoredCellStyle());
        this.createCellWithData(dataRow, PRODUCT_HIDDEN, Cell.CELL_TYPE_STRING, product).setCellStyle(this.styles.getColoredCellStyle());
        this.createCellWithData(dataRow, MODULE_HIDDEN, Cell.CELL_TYPE_STRING, module.getMGCode()).setCellStyle(this.styles.getColoredCellStyle());

    }

    private void createAddonData(int rowNum, ProductAddon productAddon)
    {
        LOG.debug("Create Addon row" + rowNum);
        Row dataRow = this.createRow(rowNum, this.quoteSheet);

        PriceMaster componentRate = RateCardService.getInstance().getAddonRate(productAddon.getCode(), priceDate, city);

        //Set header details for module
        this.createCellWithData(dataRow, PRODUCT_OR_SERVICE, Cell.CELL_TYPE_STRING, productAddon.getCategoryCode()).setCellStyle(this.styles.getColoredCellStyle());
        this.createCellWithData(dataRow, DSO_ERP_ITEM_CODE, Cell.CELL_TYPE_STRING, "ERP CODE").setCellStyle(this.styles.getColoredCellStyle());
        this.createCellWithData(dataRow, DSO_REFERENCE_PART_NO, Cell.CELL_TYPE_STRING, productAddon.getCatalogueCode()).setCellStyle(this.styles.getColoredCellStyle());
        this.createCellWithData(dataRow, DSO_UOM, Cell.CELL_TYPE_STRING, productAddon.getUom()).setCellStyle(this.styles.getColoredCellStyle());
        this.createCellWithData(dataRow, DSO_RATE, Cell.CELL_TYPE_STRING, componentRate.getSourcePrice()).setCellStyle(this.styles.getColoredCellStyle());
        this.createCellWithData(dataRow, DSO_QTY, Cell.CELL_TYPE_STRING, productAddon.getQuantity()).setCellStyle(this.styles.getColoredCellStyle());
        this.createCellWithData(dataRow, DSO_PRICE, Cell.CELL_TYPE_STRING, componentRate.getSourcePrice() * productAddon.getQuantity()).setCellStyle(this.styles.getColoredCellStyle());

    }

    private void createModuleAccessoryPackRow(int rowNum, AccHwComponent accHwComponent, AccessoryPackComponent accessoryPackComponent, String spaceType, String room, String product, ProductModule module)
    {
        LOG.debug("Create Module Accessory pack row" + rowNum);
        Row dataRow = this.createRow(rowNum, this.quoteSheet);
        double quantity = accessoryPackComponent.getQuantity();
        String accPackComponentCategory = null;


        PriceMaster componentRate = null;
        if (accessoryPackComponent.getType().equals("H"))
        {
            componentRate = RateCardService.getInstance().getHardwareRate(accessoryPackComponent.getComponentCode(), priceDate, city);
        }
        else if (accessoryPackComponent.getType().equals("A"))
        {
            componentRate = RateCardService.getInstance().getAccessoryRate(accessoryPackComponent.getComponentCode(), priceDate, city);
        }
        this.createCellWithData(dataRow, COMPONENT_CATEGORY, Cell.CELL_TYPE_STRING, accessoryPackComponent.getAccessoryPackCode()).setCellStyle(this.styles.getColoredCellStyle());

        //Inserting Cell data for DSO version
        this.createCellWithData(dataRow, DSO_ERP_ITEM_CODE, Cell.CELL_TYPE_STRING, accHwComponent.getERPCode()).setCellStyle(this.styles.getColoredCellStyle());
        this.createCellWithData(dataRow, DSO_REFERENCE_PART_NO, Cell.CELL_TYPE_STRING, accHwComponent.getCatalogCode()).setCellStyle(this.styles.getColoredCellStyle());
        this.createCellWithData(dataRow, DSO_UOM, Cell.CELL_TYPE_STRING, accHwComponent.getUom()).setCellStyle(this.styles.getColoredCellStyle());
        this.createCellWithData(dataRow, DSO_RATE, Cell.CELL_TYPE_NUMERIC, componentRate.getSourcePrice()).setCellStyle(this.styles.getColoredCellStyle());
        this.createCellWithData(dataRow, DSO_QTY, Cell.CELL_TYPE_NUMERIC, quantity).setCellStyle(this.styles.getColoredCellStyle());
        this.createCellWithData(dataRow, DSO_PRICE, Cell.CELL_TYPE_NUMERIC, componentRate.getSourcePrice()*quantity).setCellStyle(this.styles.getColoredCellStyle());

        //Inserting Cell data for Planner version
        this.createCellWithData(dataRow, PLANNER_ERP_ITEM_CODE, Cell.CELL_TYPE_STRING, accHwComponent.getERPCode()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, PLANNER_UOM, Cell.CELL_TYPE_STRING, accHwComponent.getUom()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, PLANNER_RATE, Cell.CELL_TYPE_NUMERIC, componentRate.getSourcePrice()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, PLANNER_QTY, Cell.CELL_TYPE_NUMERIC, quantity).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, PLANNER_PRICE, Cell.CELL_TYPE_NUMERIC, componentRate.getSourcePrice()*quantity).setCellStyle(this.styles.getTextStyle());


        this.createCellWithData(dataRow, SPACE_TYPE_HIDDEN, Cell.CELL_TYPE_STRING, spaceType).setCellStyle(this.styles.getColoredCellStyle());
        this.createCellWithData(dataRow, ROOM_HIDDEN, Cell.CELL_TYPE_STRING, room).setCellStyle(this.styles.getColoredCellStyle());
        this.createCellWithData(dataRow, CATEGORY_HIDDEN, Cell.CELL_TYPE_STRING, "Modular products").setCellStyle(this.styles.getColoredCellStyle());
        this.createCellWithData(dataRow, PRODUCT_HIDDEN, Cell.CELL_TYPE_STRING, product).setCellStyle(this.styles.getColoredCellStyle());
        this.createCellWithData(dataRow, MODULE_HIDDEN, Cell.CELL_TYPE_STRING, module.getMGCode()).setCellStyle(this.styles.getColoredCellStyle());

    }

    private void createModuleComponentDataRow(int rowNum, ModuleComponent moduleComponent , String spaceType, String room, String product, ProductModule module)
    {
        LOG.debug("Create Module Component data row" + rowNum);
        Row dataRow = this.createRow(rowNum, this.quoteSheet);
        double quantity = moduleComponent.getQuantity();
        AccHwComponent accHwComponent = ModuleDataService.getInstance().getHardware(moduleComponent.getComponentCode());
        PriceMaster addonRate = RateCardService.getInstance().getHardwareRate(moduleComponent.getComponentCode(), priceDate, city);

        this.createCellWithData(dataRow, COMPONENT_CATEGORY, Cell.CELL_TYPE_STRING, "HW").setCellStyle(this.styles.getColoredCellStyle());

//       dataRow.setHeight(new Double(dataRow.getHeight() * 1.5).shortValue());
        //Inserting Cell data for DSO version
        this.createCellWithData(dataRow, DSO_ERP_ITEM_CODE, Cell.CELL_TYPE_STRING, accHwComponent.getERPCode()).setCellStyle(this.styles.getColoredCellStyle());
        this.createCellWithData(dataRow, DSO_REFERENCE_PART_NO, Cell.CELL_TYPE_STRING, accHwComponent.getCatalogCode()).setCellStyle(this.styles.getColoredCellStyle());
        this.createCellWithData(dataRow, DSO_UOM, Cell.CELL_TYPE_STRING, accHwComponent.getUom()).setCellStyle(this.styles.getColoredCellStyle());
        this.createCellWithData(dataRow, DSO_RATE, Cell.CELL_TYPE_NUMERIC, addonRate.getSourcePrice()).setCellStyle(this.styles.getColoredCellStyle());
        this.createCellWithData(dataRow, DSO_QTY, Cell.CELL_TYPE_NUMERIC, quantity).setCellStyle(this.styles.getColoredCellStyle());
        this.createCellWithData(dataRow, DSO_PRICE, Cell.CELL_TYPE_NUMERIC, addonRate.getSourcePrice()*quantity).setCellStyle(this.styles.getColoredCellStyle());

        //Inserting Cell data for Planner version
        this.createCellWithData(dataRow, PLANNER_ERP_ITEM_CODE, Cell.CELL_TYPE_STRING, accHwComponent.getERPCode()).setCellStyle(this.styles.getLockedTextStyle());
        this.createCellWithData(dataRow, PLANNER_UOM, Cell.CELL_TYPE_STRING, accHwComponent.getUom()).setCellStyle(this.styles.getLockedTextStyle());
        this.createCellWithData(dataRow, PLANNER_RATE, Cell.CELL_TYPE_NUMERIC, addonRate.getSourcePrice()).setCellStyle(this.styles.getLockedTextStyle());
        this.createCellWithData(dataRow, PLANNER_QTY, Cell.CELL_TYPE_NUMERIC, quantity).setCellStyle(this.styles.getLockedTextStyle());
        this.createCellWithData(dataRow, PLANNER_PRICE, Cell.CELL_TYPE_NUMERIC, addonRate.getSourcePrice()*quantity).setCellStyle(this.styles.getLockedTextStyle());

        this.createCellWithData(dataRow, SPACE_TYPE_HIDDEN, Cell.CELL_TYPE_STRING, spaceType).setCellStyle(this.styles.getColoredCellStyle());
        this.createCellWithData(dataRow, ROOM_HIDDEN, Cell.CELL_TYPE_STRING, room).setCellStyle(this.styles.getColoredCellStyle());
        this.createCellWithData(dataRow, CATEGORY_HIDDEN, Cell.CELL_TYPE_STRING, "Modular products").setCellStyle(this.styles.getColoredCellStyle());
        this.createCellWithData(dataRow, PRODUCT_HIDDEN, Cell.CELL_TYPE_STRING, product).setCellStyle(this.styles.getColoredCellStyle());
        this.createCellWithData(dataRow, MODULE_HIDDEN, Cell.CELL_TYPE_STRING, module.getMGCode()).setCellStyle(this.styles.getColoredCellStyle());
    }

    private Row createRow(int currentRow, Sheet sheet)
    {
        sheet.shiftRows(currentRow, sheet.getLastRowNum(), 1);
        return sheet.createRow(currentRow);
    }

    private Cell createCellWithData(Row dataRow, int cellNum, int cellType, Object data)
    {
        Cell cell = dataRow.createCell(cellNum, cellType);
        if (data == null) return cell;

        if (cellType == Cell.CELL_TYPE_NUMERIC)
        {
            cell.setCellValue((Double) data);
        }
        else
        {
            cell.setCellValue(data.toString());
        }
        return cell;
    }


}
