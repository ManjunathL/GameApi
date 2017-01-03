package com.mygubbi.game.proposal.quote;

import com.mygubbi.game.proposal.ModuleDataService;
import com.mygubbi.game.proposal.ProductAddon;
import com.mygubbi.game.proposal.ProductLineItem;
import com.mygubbi.si.excel.ExcelCellProcessor;
import com.mygubbi.si.excel.ExcelSheetProcessor;
import com.mygubbi.si.excel.ExcelStyles;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sunil on 22-05-2016.
 */
public class QuotationSheetCreator implements ExcelCellProcessor
{
    private final static Logger LOG = LogManager.getLogger(QuotationSheetCreator.class);

    private static final int TITLE_CELL = 1;
    private static final int INDEX_CELL = 0;
    private static final int QUANTITY_CELL = 2;
    private static final int RATE_CELL = 3;
    private static final int AMOUNT_CELL = 4;

    private static final String[] ALPHABET_SEQUENCE = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    private static final String[] ROMAN_SEQUENCE = new String[]{"i", "ii", "iii", "iv", "v", "vi", "vii", "viii", "ix", "x", "xi", "xii", "xiii", "xiv", "xv"};

    private QuoteData quoteData;
    private Sheet quoteSheet;
    private ExcelStyles styles;
    private ExcelSheetProcessor sheetProcessor;
    List<QuotationSheetCreator.customeclass> li,li2;

    public QuotationSheetCreator(Sheet quoteSheet, QuoteData quoteData, ExcelStyles styles)
    {
        this.quoteSheet = quoteSheet;
        this.quoteData = quoteData;
        this.styles = styles;
    }

    public void prepare()
    {
        this.sheetProcessor = new ExcelSheetProcessor(this.quoteSheet, this.styles, this);
        this.sheetProcessor.process();
    }

    @Override
    public Object getValueForKey(String key)
    {
        if ((key.equals("discountamount") && this.quoteData.getDiscountAmount() == 0) ||
                key.equals("amountafterdiscount") && this.quoteData.getAmountafterdiscount()==this.quoteData.getTotalCost())
        {
            this.sheetProcessor.removeCurrentRow();
            return ExcelSheetProcessor.CellAction.NONE;
        }
        return this.quoteData.getValue(key);
    }

    @Override
    public void processCell(Cell cell, String cellValue)
    {
        switch (cellValue)
        {
            case "Kitchen & Other Units":
                int currentRow = this.fillAssembledProducts(cell.getRow().getRowNum() + 2);
                this.fillCatalogProducts(currentRow);
                break;

            case "B.1":
                this.fillAddons(cell, this.quoteData.getAccessories(), "No additional accessories.");
                break;

            case "B.2":
                this.fillAddons(cell, this.quoteData.getAppliances(), "No additional appliances.");
                break;

            case "B.3":
                this.fillAddons(cell, this.quoteData.getCounterTops(), "No countertops.");
                break;

            case "B.4":
                this.fillAddons(cell, this.quoteData.getServices(), "No additional services.");
                break;

            default:
                break;
        }
    }

    public int fillAssembledProducts(int currentRow)
    {
        List<AssembledProductInQuote> assembledProducts = this.quoteData.getAssembledProducts();
        if (assembledProducts.isEmpty())
        {
            return currentRow;
        }

        int sequenceNumber = 1;
        for (AssembledProductInQuote product : assembledProducts)
        {
            LOG.info("Current row1 " +currentRow);
            currentRow = this.fillAssembledProductInfo(currentRow, sequenceNumber, product);
            currentRow++;
            sequenceNumber++;
        }
        return currentRow;
    }

    private int fillAssembledProductInfo(int startRow, int sequenceNumber, AssembledProductInQuote product)
    {
        int currentRow = startRow;

        LOG.info("Current row(parameter) " +currentRow);
        this.createProductTitleRow(currentRow, "A." + String.valueOf(sequenceNumber), product.getTitle());


        currentRow = this.fillAssembledProductUnits(product, currentRow);
        currentRow++;

        String unitSequenceLetter = ALPHABET_SEQUENCE[(product.getUnits().size())];
        currentRow = this.fillAssembledProductAccessories(product.getAccessories(), currentRow, unitSequenceLetter);

        this.createCellWithData(this.quoteSheet.getRow(startRow + 1), AMOUNT_CELL, Cell.CELL_TYPE_NUMERIC, product.getAmountWithoutAddons());
        this.quoteSheet.addMergedRegion(new CellRangeAddress(startRow + 1, currentRow, AMOUNT_CELL, AMOUNT_CELL));

        currentRow++;
        this.createRow(currentRow, this.quoteSheet);
        return currentRow;
    }

    private int fillAssembledProductAccessories(List<AssembledProductInQuote.Accessory> accessories, int currentRow, String unitSequenceLetter)
    {
        if (accessories == null || accessories.isEmpty())
        {
            return currentRow;
        }

        this.createSubHeadingRow(currentRow, unitSequenceLetter, "Accessories");
        int acSequence = 0;
        for (AssembledProductInQuote.Accessory accessory : accessories)
        {
            currentRow++;
            this.createRowAndFillData(currentRow, ROMAN_SEQUENCE[acSequence], accessory.title, accessory.quantity, null, null);
            acSequence++;
            if (acSequence == ROMAN_SEQUENCE.length) acSequence = 0;
        }
        return currentRow;
    }

    private int fillAssembledProductUnits(AssembledProductInQuote product, int currentRow)
    {
        int unitSequence = 0;
        String caption="",caption1="",caption2="",caption3="",caption4="";
        String cname=product.getCatagoryName();

        int KBmodulecount=0,KWmoduleCount=0,KTmoduleCount=0,KLmoduleCount=0,SW1modulecount=0;
        String KBbasecarcass="",KWbasecarcass="",KTbasecarcass="",KLbasecarcass="",SW1basecarcass="";
        String KBWallcarcass="",KWwallcarcass="",KTwallcarcass="",KLwallcarcass="",SW1wallcarcass="";
        String KBfinishmaterial="",KWfinishmaterial="",KTfinishmaterial="",KLfinishmaterial="",SW1finishmaterial="";
        String KBfinishtype="",KWfinishtype="",KTfinishtype="",KLfinishtype="",SW1finishtype="";
        double KBamount=0,KWamount=0,KTamount=0,KLamount=0,SW1amount=0;

        String basewidth="",wallwidth="",tallwidth="",loftwidth="";
        List<String> kwList= new ArrayList<String>();
        List<String> kbList=new ArrayList<String>();
        List<String> ktList=new ArrayList<String>();
        List<String> klList=new ArrayList<String>();

        for (AssembledProductInQuote.Unit unit : product.getUnits())
        {
            if(cname.equals("K") )
            {
                if(unit.title.contains("Base unit")||
                    unit.title.contains("N - Base Units") ||
                    unit.title.contains("N - Drawer Units") ||
                    unit.title.contains("N - Drawer") ||
                    unit.title.contains("N - Open Units") ||
                    unit.title.contains("N - Panelling") ||
                    unit.title.contains ("N - WoodWork Add On") ||
                    unit.title.contains("S - Kitchen Base Corner Units")||
                    unit.title.contains("S - Kitchen Base Drawer Units") ||
                    unit.title.contains("S - Kitchen Base Shutter Units") ||
                    unit.title.contains("S - Kitchen Panels") ||
                    unit.title.contains("S - Sliding Mechanism") ||
                    unit.title.contains("S - Sliding Wardrobe 2100") ||
                    unit.title.contains("S - Sliding Wardrobe 2400") ||
                    unit.title.contains("S - Storage Module Base Unit") ||
                    unit.title.contains("S - Wardrobe Panels") ||
                    unit.title.contains("S - Bathroom Vanity") ||
                    unit.title.contains("S - Hinged Wardrobe 2100") ||
                    unit.title.contains("S - Hinged Wardrobe 2400") )
            {
                if(unit.title.contains("N - Base Units") || unit.title.contains("S - Kitchen Base Corner Units")||
                        unit.title.contains("S - Kitchen Base Drawer Units") ||
                        unit.title.contains("S - Kitchen Base Shutter Units") ||
                        unit.title.contains("Base unit")) {
                    KBmodulecount += unit.moduleCount;
                }
                KBbasecarcass = product.getProduct().getBaseCarcassCode();
                KBWallcarcass = product.getProduct().getWallCarcassCode();
                KBfinishmaterial = ModuleDataService.getInstance().getFinish(product.getProduct().getFinishCode()).getTitle();
                KBfinishtype = product.getProduct().getFinishType();
                KBamount += unit.amount;

                if(cname.equals("K"))
                {
                    caption="Kitchen Base Unit"; // + " - " +unit.getDimensions();
                }
                LOG.info("base unit");
                String width = unit.getDimensions();
                basewidth=  width + " , " + basewidth;
                LOG.info("Length= " +basewidth.length());
                    /*LOG.info("base width" +basewidth);*/
                kbList.add(new String(width));
            }
            else if (unit.title.contains("Wall unit")||
                    unit.title.contains("S - Kitchen Wall Corner Units")||
                    unit.title.contains("S - Kitchen Wall Flap Up Units") ||
                    unit.title.contains("S - Kitchen Wall Open Units") ||
                    unit.title.contains("S - Kitchen Wall Shutter Units")||
                    unit.title.contains("S - Storage Module Wall Unit")||
                    unit.title.contains("S - Wall Open Units") ||
                    unit.title.contains ("N - Wall Units") )
            {
                KWmoduleCount += unit.moduleCount;
                KWbasecarcass = product.getProduct().getBaseCarcassCode();
                KWwallcarcass = product.getProduct().getWallCarcassCode();
                KWfinishmaterial = ModuleDataService.getInstance().getFinish(product.getProduct().getFinishCode()).getTitle();
                KWfinishtype = product.getProduct().getFinishType();
                KWamount += unit.amount;

                if(cname.equals("K"))
                {
                    caption1="Kitchen Wall Unit";
                }

                String width = unit.getDimensions();
                wallwidth=width + " , " + wallwidth;
                kwList.add(new String(width));

                    /*String width = unit.getDimensions();
                    kwList.add(new String(width));*/
                LOG.info( "wall width");
            }
            else if (unit.title.contains("Tall unit") || unit.title.contains("S - Kitchen Tall Units") ||  unit.title.contains ("N - Tall/Semi Tall Units"))
            {
                KTmoduleCount += unit.moduleCount;
                KTbasecarcass = product.getProduct().getBaseCarcassCode();
                KTwallcarcass = product.getProduct().getWallCarcassCode();
                KTfinishmaterial = ModuleDataService.getInstance().getFinish(product.getProduct().getFinishCode()).getTitle();
                KTfinishtype = product.getProduct().getFinishType();
                KTamount += unit.amount;

                if(cname.equals("K"))
                {
                    caption2="Kitchen Tall Unit" + " - " +unit.getDimensions();
                }

                String width = unit.getDimensions();
                tallwidth=width + " , " + tallwidth;
                ktList.add(new String(width));
            }

            else if(unit.title.contains("S - Kitchen Loft Units") || unit.title.contains("S - Sliding Wardrobe with Loft") || unit.title.contains("S - Wardrobe Lofts"))
            {
                KLmoduleCount += unit.moduleCount;
                KLbasecarcass = product.getProduct().getBaseCarcassCode();
                KLwallcarcass = product.getProduct().getWallCarcassCode();
                KLfinishmaterial = ModuleDataService.getInstance().getFinish(product.getProduct().getFinishCode()).getTitle();
                KLfinishtype = product.getProduct().getFinishType();
                KLamount += unit.amount;

                if(cname.equals("K"))
                {
                    caption3="Kitchen Lofts" + " - " +unit.getDimensions();
                }

                String width = unit.getDimensions();
                loftwidth=width + " , "  +loftwidth;
                klList.add(new String(width));
            }
        }
        else if(cname.equals("W") || cname.equals("Storage Modules") || cname.equals("wallpanelling")  || cname.equals("oswalls")  || cname.equals("sidetables") ||  cname.equals("shoerack") ||  cname.equals("Bathroom Vanity") ||  cname.equals("tvunit") ||  cname.equals("barunit") || cname.equals("bookshelf") ||  cname.equals("crunit") ||  cname.equals("wallunits"))
        {
            SW1modulecount += unit.moduleCount;
            SW1basecarcass = product.getProduct().getBaseCarcassCode();
            SW1wallcarcass = product.getProduct().getWallCarcassCode();
            SW1finishmaterial = ModuleDataService.getInstance().getFinish(product.getProduct().getFinishCode()).getTitle();
            SW1finishtype = product.getProduct().getFinishType();
            SW1amount += unit.amount;
            if(cname.equals("W"))
            {
                caption4="Wardrobe" + " - " + unit.getDimensions();
            }
            else if(cname.equals("K"))
            {
                caption4="Kitchen Lofts";
            }
            else if(cname.equals("Storage Modules"))
            {
                caption4="Stroage Modules";
            }
            else if(cname.equals("wallpanelling"))
            {
                caption4="Wall Panelling";
            }
            else if(cname.equals("oswalls"))
            {
                caption4="Open Shelves";
            }
            else if(cname.equals("W"))
            {
                caption4="Wardrobe Lofts";
            }
            else if(cname.equals("sidetables"))
            {
                caption4="Side Tables";
            }
            else if(cname.equals("shoerack"))
            {
                caption4=" Shoe Rack";
            }
            else if(cname.equals("Bathroom Vanity"))
            {
                caption4="Bathroom Vanity";
            }
            else if(cname.equals("tvunit"))
            {
                caption4="TV unit";
            }
            else if(cname.equals("barunit") )
            {
                caption4="Bar Unit";
            }
            else if(cname.equals("bookshelf"))
            {
                caption4="Book Shelf";
            }
            else if(cname.equals("crunit") )
            {
                caption4="Crockery Unit";
            }
            else if(cname.equals("wallunits"))
            {
                caption4="Wall Unit";
            }
        }
        else
        {
            currentRow++;
            this.createSubHeadingRow(currentRow,"A."+ ALPHABET_SEQUENCE[unitSequence], unit.title + " - " + unit.getDimensions());

            currentRow++;
            this.createRowAndFillData(currentRow, null, "Unit consists of " + unit.moduleCount + " modules as per design provided.",1.0,unit.amount,0.0);

            currentRow++;
            this.createRowAndFillData(currentRow, null, "Base Carcass : " + product.getProduct().getBaseCarcassCode() + " , Wall Carcass : " + product.getProduct().getWallCarcassCode());

            currentRow++;
            this.createRowAndFillData(currentRow, null, "Finish Material : " + ModuleDataService.getInstance().getFinish(product.getProduct().getFinishCode()).getTitle() + " , Finish Type : " + product.getProduct().getFinishType());

            unitSequence++;
            if (unitSequence == ALPHABET_SEQUENCE.length) unitSequence = 0;
        }
    }

    li=new ArrayList<QuotationSheetCreator.customeclass>();
        QuotationSheetCreator.customeclass obj;
    if(basewidth!="") {
        basewidth = this.changeCharInPosition(basewidth.length() - 2, ' ', basewidth);
    }
    if(wallwidth!="") {
        wallwidth = this.changeCharInPosition(wallwidth.length() - 2, ' ', wallwidth);
    }
    if(tallwidth!="") {
        tallwidth = this.changeCharInPosition(tallwidth.length() - 2, ' ', tallwidth);
    }
    if(loftwidth!="") {
        loftwidth = this.changeCharInPosition(loftwidth.length() - 2, ' ', loftwidth);
    }


        obj=new QuotationSheetCreator.customeclass(currentRow,caption,KBmodulecount,KBbasecarcass,KBWallcarcass,KBfinishmaterial,KBfinishtype,KBamount,basewidth);
        LOG.info("Current row2 " +currentRow);
        li.add(obj);

        /*currentRow++;
        LOG.info("Current row3 " +currentRow);
        obj=new QuotationSheetCreator.customeclass(currentRow,caption1,KWmoduleCount,KWbasecarcass,KWwallcarcass,KWfinishmaterial,KWfinishtype,KWamount,wallwidth);
        li.add(obj);

        currentRow++;
        LOG.info("Current row4 " +currentRow);
        obj=new QuotationSheetCreator.customeclass(currentRow,caption2,KTmoduleCount,KTbasecarcass,KTwallcarcass,KTfinishmaterial,KTfinishtype,KTamount,tallwidth);
        li.add(obj);

        currentRow++;
        LOG.info("Current row5 " +currentRow);
        obj=new QuotationSheetCreator.customeclass(currentRow,caption3,KLmoduleCount,KLbasecarcass,KLwallcarcass,KLfinishmaterial,KLfinishtype,KLamount,loftwidth);
        li.add(obj);*/

        LOG.info("Call custom func 1");
        int rowValue=customFunction(li);
        //int num=customFunction(li);
        LOG.info("end custom func 1");

        li2=new ArrayList<QuotationSheetCreator.customeclass>();
        QuotationSheetCreator.customeclass ob2;

        currentRow++;
        LOG.info("Current row6 " +currentRow);
        ob2=new QuotationSheetCreator.customeclass(currentRow,caption4,SW1modulecount,SW1basecarcass,SW1wallcarcass,SW1finishmaterial,SW1finishtype,SW1amount,null);
        li2.add(ob2);

        int num1=customFunction(li2);

        //return num;
        LOG.info("Current row7 " +currentRow);
        //return currentRow;
        return rowValue;
    }

    private int customFunction(List<QuotationSheetCreator.customeclass> li)
    {
        LOG.info("custom function ");
        int unitSequence = 0;
        int num=0;
        int cr=0;
        for(int index=0;index<li.size();index++)
        {
            if (li.get(index).getAmount()==0)
            {
                return num;
            }
            else if (li.get(index).getTitle().equals("Kitchen Base Unit") || li.get(index).getTitle().equals("Kitchen Tall Unit") || li.get(index).getTitle().equals("Kitchen Wall Unit") || li.get(index).getTitle().equals("Kitchen Lofts"))
            {
                LOG.info("line 1 " +li.get(index).getCurrentRow() + "TITLE" +li.get(index).getTitle());

                cr= li.get(index).getCurrentRow();
                cr++;
                LOG.info("lno " +cr);
                this.createSubHeadingRow(cr,"A."+ ALPHABET_SEQUENCE[unitSequence], li.get(index).getTitle() + " - " + li.get(index).getDimension());

                cr++;
                LOG.info("lno " +cr);
                this.createRowAndFillData(cr, null, "Unit consists of " + li.get(index).getModulecount() + " modules as per design provided.",1.0,li.get(index).getAmount(),0.0);


                LOG.info("lno " +cr);
                this.createRowAndFillData(cr, null, "Base Carcass : " + li.get(index).getBasecarcass() + " , Wall Carcass : " + li.get(index).getWallcarcass());

                String fmaterial = li.get(index).getFinishmaterial().replaceAll("\n", "");

                LOG.info("lno " +cr);
                this.createRowAndFillData(cr, null, "Finish Material : " + fmaterial + " , Finish Type : " + li.get(index).getFinishtype());

                unitSequence++;
                if (unitSequence == ALPHABET_SEQUENCE.length) unitSequence = 0;



                /*num += 1;
                int currentRow=li.get(index).getCurrentRow();
                currentRow++;
                this.createSubHeadingRow(currentRow, "A." + ALPHABET_SEQUENCE[unitSequence], li.get(index).getTitle() + " - " +li.get(index).getDimension());
                String fmaterial = li.get(index).getFinishmaterial().replaceAll("\n", "");

                currentRow++;
                this.createRowAndFillData(currentRow, null, "unit consists of " + li.get(index).getModulecount() + " modules as per design provided.\n" + "Base Carcass: " + li.get(index).getBasecarcass() + ",Wall Carcass: " + li.get(index).getWallcarcass() + "\n" + "Finish Material: " + fmaterial + " , Finish Type : " + li.get(index).getFinishtype(), 1.0, li.get(index).getAmount(), 0.0);
                unitSequence++;
                if (unitSequence == ALPHABET_SEQUENCE.length) unitSequence = 0;*/

            }
            else
            {
                cr=0;
                LOG.info("line 11 " +li.get(index).getCurrentRow());
                this.createSubHeadingRow(li.get(index).getCurrentRow(),"A."+ ALPHABET_SEQUENCE[unitSequence], li.get(index).getTitle() + " - " + li.get(index).getDimension());

                //currentRow++;
                LOG.info("line 21 " +li.get(index).getCurrentRow());
                this.createRowAndFillData(li.get(index).getCurrentRow(), null, "Unit consists of " + li.get(index).getModulecount() + " modules as per design provided.",1.0,li.get(index).getAmount(),0.0);

                //currentRow++;
                LOG.info("line 31 " +li.get(index).getCurrentRow());
                this.createRowAndFillData(li.get(index).getCurrentRow(), null, "Base Carcass : " + li.get(index).getBasecarcass() + " , Wall Carcass : " + li.get(index).getWallcarcass());

                String fmaterial = li.get(index).getFinishmaterial().replaceAll("\n", "");
                //currentRow++;
                LOG.info("line 41 " +li.get(index).getCurrentRow());
                this.createRowAndFillData(li.get(index).getCurrentRow(), null, "Finish Material : " + fmaterial + " , Finish Type : " + li.get(index).getFinishtype());

                unitSequence++;
                if (unitSequence == ALPHABET_SEQUENCE.length) unitSequence = 0;

                /*num += 1;
                int currentRow=li.get(index).getCurrentRow();
                currentRow++;

                this.createSubHeadingRow(currentRow,"A. "  + ALPHABET_SEQUENCE[unitSequence], li.get(index).getTitle());
                String fmaterial = li.get(index).getFinishmaterial().replaceAll("\n", "");

                currentRow++;
                this.createRowAndFillData(currentRow, null, "Base Carcass: " + li.get(index).getBasecarcass() + ",Wall Carcass: " + li.get(index).getWallcarcass() + "\n" + "Finish Material: " + fmaterial + " , Finish Type : " + li.get(index).getFinishtype(), 1.0, li.get(index).getAmount(), 0.0);
                unitSequence++;
                if (unitSequence == ALPHABET_SEQUENCE.length) unitSequence = 0;*/
            }
        }
        return cr;
    }

    public String changeCharInPosition(int position, char ch, String str){
        char[] charArray = str.toCharArray();
        charArray[position] = ch;
        return new String(charArray);
    }
    private int fillCatalogProducts(int currentRow)
    {
        List<ProductLineItem> catalogProducts = this.quoteData.getCatalogueProducts();
        if (catalogProducts.isEmpty())
        {
            return currentRow;
        }

        int sequenceNumber = this.quoteData.getCatalogStartSequence();
        for (ProductLineItem product : catalogProducts)
        {
            currentRow = this.fillCatalogProductInfo(currentRow, sequenceNumber, product);
            currentRow++;
            sequenceNumber++;
        }
        return currentRow;
    }

    private int fillCatalogProductInfo(int startRow, int sequenceNumber, ProductLineItem product)
    {
        int currentRow = startRow;

        this.createSubHeadingRowForCatalog(currentRow, "A." +String.valueOf(sequenceNumber), product.getTitle(), Double.valueOf(product.getQuantity()),
                product.getRate(), (double) Math.round(product.getAmount()));

        currentRow++;
        this.createRowAndFillData(currentRow, null, product.getName());

        this.quoteSheet.addMergedRegion(new CellRangeAddress(startRow, currentRow, AMOUNT_CELL, AMOUNT_CELL));

        currentRow++;
        this.createRow(currentRow, this.quoteSheet);

        return currentRow;
    }

    private void fillAddons(Cell cell, List<ProductAddon> addOns, String emptyMessage)
    {
        int currentRow  = cell.getRow().getRowNum() + 1;
        if (addOns.isEmpty())
        {
            this.createRowWithMessage(currentRow, emptyMessage);
            return;
        }


        int index = 1;
        for (ProductAddon addon : addOns)
        {
            this.createRowAndFillData(currentRow, String.valueOf(index), addon.getExtendedTitle(), addon.getQuantity(), addon.getRate(), addon.getAmount());
            currentRow++;
            index++;
        }
    }

    private void createRowWithMessage(int row, String message)
    {
        this.createRowAndFillData(row, null, message, null, null, null);
    }

    private void createRowAndFillData(int row, String index, String title)
    {
        this.createRowAndFillData(row, index, title, null, null, null);
    }

    private void createRowAndFillData(int rowNum, String index, String title, Double quantity, Double amount, Double total)
    {
        Row dataRow = this.createRow(rowNum, this.quoteSheet);
        this.createCellWithData(dataRow, INDEX_CELL, Cell.CELL_TYPE_STRING, index);
        this.createCellWithData(dataRow, TITLE_CELL, Cell.CELL_TYPE_STRING, title);
        this.createCellWithData(dataRow, QUANTITY_CELL, Cell.CELL_TYPE_NUMERIC, quantity);
        this.createCellWithData(dataRow, RATE_CELL, Cell.CELL_TYPE_NUMERIC, amount);
        this.createCellWithData(dataRow, AMOUNT_CELL, Cell.CELL_TYPE_NUMERIC, total);
    }

    private void createSubHeadingRowForCatalog(int rowNum, String index, String title,Double quantity, Double amount, Double total)
    {
        Row dataRow = this.createRow(rowNum, this.quoteSheet);
        this.createCellWithData(dataRow, INDEX_CELL, Cell.CELL_TYPE_STRING, index).setCellStyle(this.styles.getTitleStyle());
        this.createCellWithData(dataRow, TITLE_CELL, Cell.CELL_TYPE_STRING, title).setCellStyle(this.styles.getTitleStyle());
        this.createCellWithData(dataRow, QUANTITY_CELL, Cell.CELL_TYPE_NUMERIC, quantity);
        this.createCellWithData(dataRow, RATE_CELL, Cell.CELL_TYPE_NUMERIC, amount);
        this.createCellWithData(dataRow, AMOUNT_CELL, Cell.CELL_TYPE_NUMERIC, total);
    }


    private void createSubHeadingRow(int rowNum, String index, String title)
    {
        Row dataRow = this.createRow(rowNum, this.quoteSheet);
        this.createCellWithData(dataRow, INDEX_CELL, Cell.CELL_TYPE_STRING, index).setCellStyle(this.styles.getBoldStyle());
        this.createCellWithData(dataRow, TITLE_CELL, Cell.CELL_TYPE_STRING, title).setCellStyle(this.styles.getBoldStyle());
    }

    private void createProductTitleRow(int rowNum, String index, String title)
    {
        Row dataRow = this.createRow(rowNum, this.quoteSheet);
        dataRow.setHeight(new Double(dataRow.getHeight() * 1.5).shortValue());
        this.createCellWithData(dataRow, INDEX_CELL, Cell.CELL_TYPE_STRING, index).setCellStyle(this.styles.getTitleStyle());
        this.createCellWithData(dataRow, TITLE_CELL, Cell.CELL_TYPE_STRING, title).setCellStyle(this.styles.getTitleStyle());
        this.createCellWithData(dataRow, QUANTITY_CELL, Cell.CELL_TYPE_NUMERIC, null).setCellStyle(this.styles.getTitleStyle());
        this.createCellWithData(dataRow, RATE_CELL, Cell.CELL_TYPE_NUMERIC, null).setCellStyle(this.styles.getTitleStyle());
        this.createCellWithData(dataRow, AMOUNT_CELL, Cell.CELL_TYPE_NUMERIC, null).setCellStyle(this.styles.getTitleStyle());
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

    class customeclass
    {
        int currentRow;
        String title,basecarcass,wallcarcass,finishmaterial,finishtype,dimension;
        int modulecount;
        double amount;

        public customeclass(int currentRow, String title,int modulecount, String basecarcass, String wallcarcass, String finishmaterial, String finishtype, double amount,String dimension ) {
           this.currentRow=currentRow;
            this.title = title;
            this.basecarcass = basecarcass;
            this.wallcarcass = wallcarcass;
            this.finishmaterial = finishmaterial;
            this.finishtype = finishtype;
            this.amount = amount;
            this.modulecount = modulecount;
            this.dimension=dimension;
        }

        public String getDimension() {
            return dimension;
        }

        public void setDimension(String dimension) {
            this.dimension = dimension;
        }

        public int getCurrentRow() {
            return currentRow;
        }

        public void setCurrentRow(int currentRow) {
            this.currentRow = currentRow;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getBasecarcass() {
            return basecarcass;
        }

        public void setBasecarcass(String basecarcass) {
            this.basecarcass = basecarcass;
        }

        public String getWallcarcass() {
            return wallcarcass;
        }

        public void setWallcarcass(String wallcarcass)
        {
            this.wallcarcass = wallcarcass;
        }

        public String getFinishmaterial() {
            return finishmaterial;
        }

        public void setFinishmaterial(String finishmaterial) {
            this.finishmaterial = finishmaterial;
        }

        public String getFinishtype() {
            return finishtype;
        }

        public void setFinishtype(String finishtype) {
            this.finishtype = finishtype;
        }

        public int getModulecount() {
            return modulecount;
        }

        public void setModulecount(int modulecount) {
            this.modulecount = modulecount;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }
    }
}
