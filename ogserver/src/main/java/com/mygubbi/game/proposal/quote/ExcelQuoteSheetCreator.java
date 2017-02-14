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
 * Created by shruthi on 27-Dec-16.
 */
public class ExcelQuoteSheetCreator implements ExcelCellProcessor
{
    private final static Logger LOG = LogManager.getLogger(ExcelQuoteSheetCreator.class);

    private static final int TITLE_CELL = 1;
    private static final int INDEX_CELL = 0;
    private static final int QUANTITY_CELL = 2;
    private static final int RATE_CELL = 3;
    private static final int AMOUNT_CELL = 4;

    String series;
    double amt;
    int unitSequence;
    int wunitSequence;
    private static final String[] ALPHABET_SEQUENCE = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j"};
    private static final String[] ROMAN_SEQUENCE = new String[]{"i", "ii", "iii", "iv", "v", "vi", "vii", "viii", "ix", "x", "xi", "xii", "xiii", "xiv", "xv"};

    private QuoteData quoteData;
    private Sheet quoteSheet;
    private ExcelStyles styles;
    private ExcelSheetProcessor sheetProcessor;
    List<customeclass> li,li2;

    public ExcelQuoteSheetCreator(Sheet quoteSheet, QuoteData quoteData, ExcelStyles styles)
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
            currentRow = this.fillAssembledProductInfo(currentRow, sequenceNumber, product);
            //currentRow++;
            sequenceNumber++;
        }
        return currentRow;
    }
    private int fillAssembledProductInfo(int startRow, int sequenceNumber, AssembledProductInQuote product)
    {
        int currentRow = startRow;

        this.createProductTitleRow(currentRow, "A." + String.valueOf(sequenceNumber), product.getTitle());

        series="A." +String.valueOf(sequenceNumber) + ".";
        currentRow = this.fillAssembledProductUnits(product, currentRow,series);
        //currentRow++;

        amt=product.getAmountWithoutAddons();
        String unitSequenceLetter="";
        if(!(product.getCatagoryName().equals("K") || product.getCatagoryName().equals("W"))) {
            unitSequenceLetter = ALPHABET_SEQUENCE[1];
        }
        else
        {
            if(product.getCatagoryName().equals("K")) {
            unitSequenceLetter = ALPHABET_SEQUENCE[unitSequence];
        }
            else if(product.getCatagoryName().equals("W"))
            {
                unitSequenceLetter = ALPHABET_SEQUENCE[wunitSequence];
            }
        }
        LOG.info("unit sequence for acc " +unitSequenceLetter);
        currentRow = this.fillAssembledProductAccessories(product.getAccessories(), currentRow, unitSequenceLetter);

        this.createRowAndFillData(currentRow,"Total Cost",product.getAmountWithoutAddons());

        /*this.createCellWithData(this.quoteSheet.getRow(startRow + 1), AMOUNT_CELL, Cell.CELL_TYPE_NUMERIC, product.getAmountWithoutAddons());
        this.quoteSheet.addMergedRegion(new CellRangeAddress(startRow + 1, currentRow, AMOUNT_CELL, AMOUNT_CELL));*/

        /*currentRow++;
        LOG.info("After Merge2 " +currentRow);
        //LOG.info("merge 3 " +this.quoteSheet.getRow(currentRow + 1));
        this.createRowAndFillData(currentRow,"Accsory Cost",product.getAmountWithoutAddons());

        currentRow++;
        this.createRowAndFillData(currentRow,"WoodWork Cost",product.getAmountWithoutAddons());*/

        //this.createCellWithData(this.quoteSheet.getRow(currentRow + 1), AMOUNT_CELL, Cell.CELL_TYPE_NUMERIC, product.getAmountWithoutAddons());
        //this.quoteSheet.addMergedRegion(new CellRangeAddress(startRow + 1, currentRow, AMOUNT_CELL, AMOUNT_CELL));
        //currentRow++;

        //this.createCellWithData(this.quoteSheet.getRow(startRow + 1), AMOUNT_CELL, Cell.CELL_TYPE_NUMERIC, product.getAmountWithoutAddons());
        //this.quoteSheet.addMergedRegion(new CellRangeAddress(startRow + 1, currentRow, AMOUNT_CELL, AMOUNT_CELL));

        currentRow++;
        /*this.createRow(currentRow, this.quoteSheet);*/
        return currentRow;
    }

    private int fillAssembledProductUnits(AssembledProductInQuote product, int currentRow,String series)
    {
        unitSequence = 0;
        wunitSequence = 0;
        String caption="",caption1="",caption2="",caption3="",caption4="",caption5="",captionLoft="",captionWardrobe="";
        String cname=product.getCatagoryName();


        int rowValue=currentRow;
        String baseDimesion="",WallDimesion="",TallDimesion="",loftDimesion="",wardrobeDimesion="";
        int KBmodulecount=0,KWmoduleCount=0,KTmoduleCount=0,KLmoduleCount=0,SW1modulecount=0,WWmodulecount=0,WW1modulecount=0;
        String KBbasecarcass="",KWbasecarcass="",KTbasecarcass="",KLbasecarcass="",SW1basecarcass="",WWbasecarcass="",WW1basecarcass="";
        String KBWallcarcass="",KWwallcarcass="",KTwallcarcass="",KLwallcarcass="",SW1wallcarcass="",WWwallcarcass="",WW1wallcarcass="";
        String KBfinishmaterial="",KWfinishmaterial="",KTfinishmaterial="",KLfinishmaterial="",SW1finishmaterial="",WWfinishmaterial="",WW1finishmaterial="";
        String KBfinishtype="",KWfinishtype="",KTfinishtype="",KLfinishtype="",SW1finishtype="",WWfinishtype="",WW1finishtype="";
        double KBamount=0,KWamount=0,KTamount=0,KLamount=0,SW1amount=0,WWamount=0,WW1amount=0;

        String basewidth="",wallwidth="",tallwidth="",loftwidth="",wardrobewidth="",wardrobeLoftwidth="";
        int kbwidthSum=0,kbdepthSum=0,kbheightSum=0;
        int kwwidthSum=0,kwdepthSum=0,kwheightSum=0;
        int ktwidthSum=0,ktdepthSum=0,ktheightSum=0;
        int klwidthSum=0,kldepthSum=0,klheightSum=0;
        int wrwidthSum=0,wrdepthSum=0, wrheightSum=0;

        List<String> kwList= new ArrayList<String>();
        List<String> kbList=new ArrayList<String>();
        List<String> ktList=new ArrayList<String>();
        List<String> klList=new ArrayList<String>();
        List<String> kwaList=new ArrayList<String>();

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
                            unit.title.contains("S - Kitchen Base Shutter Units") ||unit.title.contains("S - Storage Module Base Unit")||
                            unit.title.contains("Base unit")) {
                        KBmodulecount += unit.moduleCount;
                        String width = unit.getDimensions();
                        basewidth=  basewidth + " , " +width;
                        kbList.add(new String(width));

                        if(unit.title.contains("S - Kitchen Base Corner Units")||
                                unit.title.contains("S - Kitchen Base Drawer Units") ||
                                unit.title.contains("S - Kitchen Base Shutter Units") || unit.title.contains("S - Storage Module Base Unit")  || unit.title.contains("Base unit") )
                                {
                            kbwidthSum = kbwidthSum + unit.getWidth();
                            kbheightSum = unit.getHeight();
                            kbdepthSum = unit.getDepth();
                            baseDimesion = kbwidthSum + " x " + kbdepthSum + " x " + kbheightSum;
                    }
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
                    String width = unit.getDimensions();
                    wallwidth=wallwidth + " , " +width ;
                    kwList.add(new String(width));

                    if(!unit.title.contains ("N - Wall Units"))
                    {
                        kwwidthSum = kwwidthSum + unit.getWidth();
                        kwheightSum = unit.getHeight();
                        kwdepthSum = unit.getDepth();
                        WallDimesion = kwwidthSum + " x " + kwdepthSum + " x " + kwheightSum;
                    }
                    if(cname.equals("K"))
                    {
                        caption1="Kitchen Wall Unit";
                    }
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
                        caption2="Kitchen Tall Unit" ;
                    }
                    if(!unit.title.contains ("N - Tall/Semi Tall Units")) {
                    String width = unit.getDimensions();
                    tallwidth=tallwidth + " , " +width;
                    ktList.add(new String(width));

                        ktwidthSum = ktwidthSum + unit.getWidth();
                        ktheightSum = unit.getHeight();
                        ktdepthSum = unit.getDepth();

                        TallDimesion = ktwidthSum + " x " + ktdepthSum + " x " + ktheightSum;
                    }
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
                        caption3="Kitchen Lofts" ;
                    }

                    String width = unit.getDimensions();
                    loftwidth=loftwidth + " , " +width;
                    klList.add(new String(width));

                    klwidthSum=klwidthSum+unit.getWidth();
                    klheightSum=unit.getHeight();
                    kldepthSum=unit.getDepth();

                    loftDimesion=klwidthSum + " x " + kldepthSum + " x " +klheightSum;
                }
            }
            else if(cname.equals("W"))
            {
                    if(unit.title.contains("N - Base Units") ||
                            unit.title.contains("N - Drawer Units") ||
                            unit.title.contains("N - Drawer") ||
                            unit.title.contains("N - Open Units") ||
                            unit.title.contains("N - Panelling") ||
                            unit.title.contains ("N - WoodWork Add On") ||
                            unit.title.contains("S - Sliding Mechanism") ||
                            unit.title.contains("N - Tall/Semi Tall Units") ||
                            unit.title.contains("N - Wall Units") ||
                            unit.title.contains("S - Hinged Wardrobe 2100") ||
                            unit.title.contains("S - Hinged Wardrobe 2400") ||
                            unit.title.contains("S - Sliding Wardrobe 2100") ||
                            unit.title.contains("S - Sliding Wardrobe 2400") ||
                            unit.title.contains("S - Storage Module Wall Unit") ||
                            unit.title.contains("S - Storage Module Base Unit") ||
                            unit.title.contains("S - Wardrobe Panels"))
                    {
                        WWmodulecount += unit.moduleCount;
                        WWbasecarcass = product.getProduct().getBaseCarcassCode();
                        WWwallcarcass = product.getProduct().getWallCarcassCode();
                        WWfinishmaterial = ModuleDataService.getInstance().getFinish(product.getProduct().getFinishCode()).getTitle();
                        WWfinishtype = product.getProduct().getFinishType();
                        WWamount += unit.amount;
                        captionWardrobe="Wardrobe";
                        if(!(unit.title.contains ("N")|| unit.title.contains("S - Wardrobe Panels"))) {
                            String width = unit.getDimensions();
                            wardrobewidth = wardrobewidth + " , " + width;
                            kwaList.add(new String(width));
                        }
                    }
                    else
                    {
                        WW1modulecount += unit.moduleCount;
                        WW1basecarcass = product.getProduct().getBaseCarcassCode();
                        WW1wallcarcass = product.getProduct().getWallCarcassCode();
                        WW1finishmaterial = ModuleDataService.getInstance().getFinish(product.getProduct().getFinishCode()).getTitle();
                        WW1finishtype = product.getProduct().getFinishType();
                        WW1amount += unit.amount;
                        captionLoft="Wardrobe Loft";

                        String width = unit.getDimensions();
                        wardrobeLoftwidth = wardrobeLoftwidth + " , " + width;
                        kwaList.add(new String(width));
                    }
            }
            else if(cname.equals("Storage Modules") || cname.equals("wallpanelling")  || cname.equals("oswalls")  || cname.equals("sidetables") ||  cname.equals("shoerack") ||  cname.equals("Bathroom Vanity") ||  cname.equals("tvunit") ||  cname.equals("barunit") || cname.equals("bookshelf") ||  cname.equals("crunit") ||  cname.equals("wallunits") || cname.equals("codrawers") || cname.equals("usstorage"))
            {
                SW1modulecount += unit.moduleCount;
                SW1basecarcass = product.getProduct().getBaseCarcassCode();
                SW1wallcarcass = product.getProduct().getWallCarcassCode();
                SW1finishmaterial = ModuleDataService.getInstance().getFinish(product.getProduct().getFinishCode()).getTitle();
                SW1finishtype = product.getProduct().getFinishType();
                SW1amount += unit.amount;

                if(cname.equals("W"))
                {
                    caption4="Wardrobe";
                    if(unit.title.contains("N - Base Units") ||
                            unit.title.contains("N - Drawer Units") ||
                            unit.title.contains("N - Drawer") ||
                            unit.title.contains("N - Open Units") ||
                            unit.title.contains("N - Panelling") ||
                            unit.title.contains ("N - WoodWork Add On") ||
                            unit.title.contains("N - Wall Units") ||
                            unit.title.contains("N - Tall/Semi Tall Units") ||
                            unit.title.contains("N - Wall Units")) {

                    }else {
                      if(!(unit.title.contains ("N")|| unit.title.contains("S - Wardrobe Panels"))) {
                        String width = unit.getDimensions();
                        wardrobewidth = wardrobewidth + " , " + width;
                          kwaList.add(new String(width));
                      }
                    }
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
                else if(cname.equals("codrawers"))
                {
                    caption4="Cest Of Drawers";
                }
                else if(cname.equals("usstorage"))
                {
                    caption4="Under Staircase Storage";
                }
            }
            /*else
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
            }*/
        }

        li=new ArrayList<customeclass>();
        customeclass obj;
        if(basewidth!="") {
            basewidth =basewidth.substring(2) ;
            //this.changeCharInPosition(basewidth.length() - 2, ' ', basewidth);
        }
        if(wallwidth!="") {
            wallwidth =wallwidth.substring(2);
        }
        if(tallwidth!="") {
            tallwidth = tallwidth.substring(2);
        }

        if(loftwidth!="") {
            loftwidth = loftwidth.substring(2);
        }
        if(wardrobewidth!="")
        {
            wardrobewidth=wardrobewidth.substring(2);
        }
        if(wardrobeLoftwidth!="")
        {
            wardrobeLoftwidth=wardrobeLoftwidth.substring(2);
        }

        if(KBamount!=0) {
            obj = new customeclass(rowValue, caption, KBmodulecount, KBbasecarcass, KBWallcarcass, KBfinishmaterial, KBfinishtype, KBamount, baseDimesion);
            li.add(obj);
            rowValue = customFunction(li,unitSequence);
            unitSequence++;
            li.clear();
        }

        if(KWamount!=0) {
            obj = new customeclass(rowValue, caption1, KWmoduleCount, KWbasecarcass, KWwallcarcass, KWfinishmaterial, KWfinishtype, KWamount,WallDimesion);
            li.add(obj);
            rowValue = customFunction(li,unitSequence);
            //rowValue++;
            unitSequence++;
            li.clear();
        }

        if(KTamount!=0) {
            obj = new customeclass(rowValue, caption2, KTmoduleCount, KTbasecarcass, KTwallcarcass, KTfinishmaterial, KTfinishtype, KTamount, TallDimesion);
            li.add(obj);
            rowValue = customFunction(li,unitSequence);
            //rowValue++;
            unitSequence++;
            li.clear();
        }

        if(KLamount!=0) {
            obj = new customeclass(rowValue, caption3, KLmoduleCount, KLbasecarcass, KLwallcarcass, KLfinishmaterial, KLfinishtype, KLamount, loftDimesion);
            li.add(obj);
            rowValue = customFunction(li,unitSequence);
            //rowValue++;
            unitSequence++;
            li.clear();
        }

        if(WWamount!=0)
        {

            obj = new customeclass(rowValue,captionWardrobe,WWmodulecount,WWbasecarcass,WWwallcarcass,WWfinishmaterial,WWfinishtype,WWamount,wardrobewidth);
            li.add(obj);
            rowValue = customFunction(li,wunitSequence);
            //rowValue++;
            wunitSequence++;
            li.clear();
        }

        if(WW1amount!=0)
        {
            obj = new customeclass(rowValue,captionLoft,WW1modulecount,WW1basecarcass,WW1wallcarcass,WW1finishmaterial,WW1finishtype,WW1amount,wardrobeLoftwidth);
            li.add(obj);
            rowValue = customFunction(li,wunitSequence);
            //rowValue++;
            wunitSequence++;
            li.clear();
        }


        /* LOG.info("Accessories Line" +rowValue);
         String unitSequenceLetter = ALPHABET_SEQUENCE[unitSequence];
         rowValue= this.fillAssembledProductAccessories(product.getAccessories(), rowValue, unitSequenceLetter);
         LOG.info("Accessories Line after executing " +rowValue);*/
         //rowValue++;

        /*this.createCellWithData(this.quoteSheet.getRow(currentRow + 1), AMOUNT_CELL, Cell.CELL_TYPE_NUMERIC, product.getAmountWithoutAddons());
        this.quoteSheet.addMergedRegion(new CellRangeAddress(currentRow + 1, rowValue, AMOUNT_CELL, AMOUNT_CELL));
        //return currentRow;*/


        li2=new ArrayList<customeclass>();
        customeclass ob2;
        ob2=new customeclass(rowValue,caption4,SW1modulecount,SW1basecarcass,SW1wallcarcass,SW1finishmaterial,SW1finishtype,SW1amount,wardrobewidth);
        li2.add(ob2);

        rowValue++;
        rowValue=customFunction(li2,unitSequence);
        rowValue++;

        /*this.createCellWithData(this.quoteSheet.getRow(currentRow + 1), AMOUNT_CELL, Cell.CELL_TYPE_NUMERIC, product.getAmountWithoutAddons());
        this.quoteSheet.addMergedRegion(new CellRangeAddress(currentRow + 1, rowValue, AMOUNT_CELL, AMOUNT_CELL));
        LOG.info("after merge value " +rowValue);*/
        //rowValue++;
        /*this.createRow(rowValue, this.quoteSheet);*/
        return rowValue;
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

    private int fillAssembledProductAccessories(List<AssembledProductInQuote.Accessory> accessories, int currentRow, String unitSequenceLetter)
    {
        if (accessories == null || accessories.isEmpty())
        {
            return currentRow;
        }
        //currentRow++;
        this.createSubHeadingRow(currentRow, series+ "" + unitSequenceLetter, "Accessories");
        int acSequence = 0;
        double amount=0.0;
        for (AssembledProductInQuote.Accessory accessory : accessories)
        {
            currentRow++;
            this.createRowAndFillData(currentRow, ROMAN_SEQUENCE[acSequence], accessory.title, null, null, null);
            amount=amount+(accessory.quantity*accessory.msp);
            acSequence++;
            if (acSequence == ROMAN_SEQUENCE.length) acSequence = 0;
        }
        /*currentRow++;
        LOG.info("After Merge2 " +currentRow);
        //LOG.info("merge 3 " +this.quoteSheet.getRow(currentRow + 1));
        this.createRowAndFillData(currentRow,"Accsory Cost",product.getAmountWithoutAddons());

        currentRow++;
        this.createRowAndFillData(currentRow,"WoodWork Cost",product.getAmountWithoutAddons());*/

        currentRow++;
        this.createRowAndFillData(currentRow,"Accessory Cost",amount);
        currentRow++;
        this.createRowAndFillData(currentRow,"WoodWork Cost",amt-amount);
        currentRow++;
        return currentRow;
    }

    private int customFunction(List<customeclass> li,int unitSequence)
    {
        int num=0;
        int cr=0;
        for(int index=0;index<li.size();index++)
        {
            if (li.get(index).getAmount()==0)
            {
                return li.get(index).getCurrentRow();
            }
            else if (li.get(index).getTitle().contains("Kitchen Base Unit") || li.get(index).getTitle().contains("Kitchen Tall Unit") || li.get(index).getTitle().contains("Kitchen Wall Unit") || li.get(index).getTitle().contains("Kitchen Lofts"))
            {
                cr= li.get(index).getCurrentRow();

                if(li.get(index).getDimension().equals("")) {
                    cr++;
                    this.createSubHeadingRow(cr,series + ALPHABET_SEQUENCE[unitSequence], li.get(index).getTitle());
                    cr++;
                }else {
                cr++;
                this.createSubHeadingRow(cr,series + ALPHABET_SEQUENCE[unitSequence], li.get(index).getTitle() + " - " + li.get(index).getDimension());
                cr++;
                }

                this.createRowAndFillDataNew(cr, null, "Unit consists of " + li.get(index).getModulecount() + " modules as per design provided.",1.0,li.get(index).getAmount(),li.get(index).getAmount());
                cr++;
                if(li.get(index).getTitle().contains("Kitchen Base Unit") || li.get(index).getTitle().contains("Kitchen Tall Unit")) {
                    this.createRowAndFillData(cr, null, "Carcass : " + li.get(index).getBasecarcass());
                cr++;
                }else {
                    this.createRowAndFillData(cr, null, "Carcass : " + li.get(index).getWallcarcass());
                    cr++;
                }

                String fmaterial = li.get(index).getFinishmaterial().replaceAll("\n", "");

                this.createRowAndFillData(cr, null, "Finish Material : " + fmaterial + " , Finish Type : " + li.get(index).getFinishtype());
                unitSequence++;
                if (unitSequence == ALPHABET_SEQUENCE.length) unitSequence = 0;
            }
            else
            {
                cr= li.get(index).getCurrentRow();
                if(li.get(index).getTitle().contains("Wardrobe")) {
                    cr++;
                    this.createSubHeadingRow(cr, series + ALPHABET_SEQUENCE[unitSequence], li.get(index).getTitle() + " - " + li.get(index).getDimension());
                    cr++;
                }else
                {
                    cr++;
                this.createSubHeadingRow(cr,series + ALPHABET_SEQUENCE[unitSequence], li.get(index).getTitle());
                cr++;
                }

                this.createRowAndFillDataNew(cr, null, "Carcass : " + li.get(index).getBasecarcass() ,1.0,li.get(index).getAmount(),li.get(index).getAmount());
                cr++;

                String fmaterial = li.get(index).getFinishmaterial().replaceAll("\n", "");
                this.createRowAndFillData(cr, null, "Finish Material : " + fmaterial + " , Finish Type : " + li.get(index).getFinishtype());
                //cr++;

                unitSequence++;
                if (unitSequence == ALPHABET_SEQUENCE.length) unitSequence = 0;

               /* this.createSubHeadingRow(li.get(index).getCurrentRow(),"A."+ ALPHABET_SEQUENCE[unitSequence], li.get(index).getTitle() + " - " + li.get(index).getDimension());
                this.createRowAndFillData(li.get(index).getCurrentRow(), null, "Unit consists of " + li.get(index).getModulecount() + " modules as per design provided.",1.0,li.get(index).getAmount(),0.0);
                this.createRowAndFillData(li.get(index).getCurrentRow(), null, "Base Carcass : " + li.get(index).getBasecarcass() + " , Wall Carcass : " + li.get(index).getWallcarcass());

                String fmaterial = li.get(index).getFinishmaterial().replaceAll("\n", "");
                this.createRowAndFillData(li.get(index).getCurrentRow(), null, "Finish Material : " + fmaterial + " , Finish Type : " + li.get(index).getFinishtype());

                unitSequence++;
                if (unitSequence == ALPHABET_SEQUENCE.length) unitSequence = 0;

                *//*num += 1;
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
        this.createCellWithData(dataRow, INDEX_CELL, Cell.CELL_TYPE_STRING, index).setCellStyle(this.styles.getIndexStyle());
        this.createCellWithData(dataRow, TITLE_CELL, Cell.CELL_TYPE_STRING, title);
        this.createCellWithData(dataRow, QUANTITY_CELL, Cell.CELL_TYPE_NUMERIC, quantity);
        this.createCellWithData(dataRow, RATE_CELL, Cell.CELL_TYPE_NUMERIC, amount);
        this.createCellWithData(dataRow, AMOUNT_CELL, Cell.CELL_TYPE_NUMERIC, total);
    }

    private void createRowAndFillData(int rowNum,String title,Double amount)
    {
        String amt=this.getRoundOffValue(String.valueOf(amount.intValue()));
        Row dataRow = this.createRow(rowNum, this.quoteSheet);
        this.createCellWithData(dataRow, AMOUNT_CELL, Cell.CELL_TYPE_STRING, amt).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, RATE_CELL, Cell.CELL_TYPE_STRING, title).setCellStyle(this.styles.getTextStyle());
    }

    private void createSubHeadingRowForCatalog(int rowNum, String index, String title,Double quantity, Double amount, Double total)
    {
        Row dataRow = this.createRow(rowNum, this.quoteSheet);
        this.createCellWithData(dataRow, INDEX_CELL, Cell.CELL_TYPE_STRING, index);
        this.createCellWithData(dataRow, TITLE_CELL, Cell.CELL_TYPE_STRING, title);
        this.createCellWithData(dataRow, QUANTITY_CELL, Cell.CELL_TYPE_NUMERIC, quantity);
        this.createCellWithData(dataRow, RATE_CELL, Cell.CELL_TYPE_NUMERIC, amount).setCellStyle(this.styles.getTitleStyle());
        this.createCellWithData(dataRow, AMOUNT_CELL, Cell.CELL_TYPE_NUMERIC,total).setCellStyle(this.styles.getTitleStyle());
    }
    private void createRowAndFillDataNew(int rowNum, String index, String title, Double quantity, Double amount, Double total)
    {
        String amt=this.getRoundOffValue(String.valueOf(amount.intValue()));
        String Total=this.getRoundOffValue(String.valueOf(total.intValue()));
        Row dataRow = this.createRow(rowNum, this.quoteSheet);
        this.createCellWithData(dataRow, INDEX_CELL, Cell.CELL_TYPE_STRING, index).setCellStyle(this.styles.getIndexStyle());
        this.createCellWithData(dataRow, TITLE_CELL, Cell.CELL_TYPE_STRING, title);
        this.createCellWithData(dataRow, QUANTITY_CELL, Cell.CELL_TYPE_NUMERIC, quantity);
        this.createCellWithData(dataRow, RATE_CELL, Cell.CELL_TYPE_STRING, amt).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, AMOUNT_CELL, Cell.CELL_TYPE_STRING, Total).setCellStyle(this.styles.getTextStyle());
    }

    /*private void createSubHeadingRowNew(int rowNum, String index, String title,Double quantity, Double amount, Double total)
    {
        String amt=this.getRoundOffValue(String.valueOf(amount.intValue()));
        String Total=this.getRoundOffValue(String.valueOf(total.intValue()));
        //this.getRoundOffValue(String.valueOf(amount.intValue()))
        Row dataRow = this.createRow(rowNum, this.quoteSheet);
        this.createCellWithData(dataRow, INDEX_CELL, Cell.CELL_TYPE_STRING, index).setCellStyle(this.styles.getTitleStyle());
        this.createCellWithData(dataRow, TITLE_CELL, Cell.CELL_TYPE_STRING, title).setCellStyle(this.styles.getTitleStyle());
        this.createCellWithData(dataRow, QUANTITY_CELL, Cell.CELL_TYPE_NUMERIC, quantity);
        this.createCellWithData(dataRow, RATE_CELL, Cell.CELL_TYPE_STRING, amt).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, AMOUNT_CELL, Cell.CELL_TYPE_STRING,Total).setCellStyle(this.styles.getTextStyle());
    }*/

    private void createSubHeadingRow(int rowNum, String index, String title)
    {
        Row dataRow = this.createRow(rowNum, this.quoteSheet);
        this.createCellWithData(dataRow, INDEX_CELL, Cell.CELL_TYPE_STRING, index).setCellStyle(this.styles.getIndexStyle());
        this.createCellWithData(dataRow, TITLE_CELL, Cell.CELL_TYPE_STRING, title).setCellStyle(this.styles.getBoldStyle());
    }

    private void createProductTitleRow(int rowNum, String index, String title)
    {
        Row dataRow = this.createRow(rowNum, this.quoteSheet);
        dataRow.setHeight(new Double(dataRow.getHeight() * 1.5).shortValue());
        this.createCellWithData(dataRow, INDEX_CELL, Cell.CELL_TYPE_STRING, index).setCellStyle(this.styles.getIndexStyle());
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

    public static String getRoundOffValue(String value)
    {
        value=value.replace(",","");
        char lastDigit=value.charAt(value.length()-1);
        String result = "";
        int len = value.length()-1;
        int nDigits = 0;

        for (int i = len - 1; i >= 0; i--)
        {
            result = value.charAt(i) + result;
            nDigits++;
            if (((nDigits % 2) == 0) && (i > 0))
            {
                result = "," + result;
            }
        }
        return (result+lastDigit);
    }
}
