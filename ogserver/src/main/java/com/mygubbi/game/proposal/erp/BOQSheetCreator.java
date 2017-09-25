package com.mygubbi.game.proposal.erp;

import com.mygubbi.game.proposal.ModuleDataService;
import com.mygubbi.game.proposal.model.*;
import com.mygubbi.si.excel.ExcelCellProcessor;
import com.mygubbi.si.excel.ExcelSheetProcessor;
import com.mygubbi.si.excel.ExcelStyles;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.sql.Date;
import java.util.*;

/**
 * Created by user on 27-May-17.
 */
public class BOQSheetCreator implements ExcelCellProcessor
{
    private final static Logger LOG = LogManager.getLogger(BOQSheetCreator.class);

    private static final int SPACE_TYPE_CELL = 0;
    private static final int ROOM_CELL = 1;
    private static final int CATEGORY_CELL = 2;
    private static final int PRODUCT_ID = 3;
    private static final int PRODUCT_OR_SERVICE = 4;
    private static final int MODULE_SEQ = 5;
    private static final int MODULE_CODE = 6;
    private static final int CUSTOM_CHECK = 7;
    private static final int CUSTOM_REMARKS = 8;
    private static final int COMPONENT_CATEGORY = 9;
    private static final int DSO_ERP_ITEM_CODE = 10;
    private static final int DSO_REFERENCE_PART_NO = 11;
    private static final int DSO_DESCRIPTION = 12;
    private static final int DSO_UOM = 13;
    private static final int DSO_RATE = 14;
    private static final int DSO_QTY = 15;
    private static final int DSO_PRICE = 16;
    private static final int PLANNER_ERP_ITEM_CODE = 17;
    private static final int PLANNER_REFERENCE_PART_NO = 18;
    private static final int PLANNER_DESCRIPTION = 19;
    private static final int PLANNER_UOM = 20;
    private static final int PLANNER_RATE = 21;
    private static final int PLANNER_QTY = 22;
    private static final int PLANNER_PRICE = 23;

    //Hidden fields for saving to the database
    private static final int SPACE_TYPE_HIDDEN = 24;
    private static final int ROOM_HIDDEN = 25;
    private static final int CATEGORY_HIDDEN = 26;
    private static final int PRODUCT_HIDDEN = 27;
    private static final int PRODUCT_ID_HIDDEN = 28;
    private static final int MODULE_SEQ_HIDDEN = 29;
    private static final int MODULE_HIDDEN = 30;
    private static final int MYGUBBI_ERPCODE_HIDDEN = 31;
    private static final int DISPLAY_ORDER = 32;

    private ProposalHeader proposalHeader;
    private XSSFSheet quoteSheet;
    private ExcelStyles styles;
    private ExcelSheetProcessor sheetProcessor;
    private Date priceDate;
    private String city;
    private XSSFWorkbook quoteWorkBook;
    private List<ProposalBOQ> proposalBoqsForProduct;
    private List<ProposalBOQ> proposalBoqsForAddon;

    public BOQSheetCreator(XSSFWorkbook xssfWorkbook, XSSFSheet quoteSheet, ExcelStyles styles, List<ProposalBOQ> proposalBoqsForProduct,List<ProposalBOQ> proposalBoqsForAddon, ProposalHeader proposalHeader)
    {
        this.quoteWorkBook = xssfWorkbook;
        this.quoteSheet = quoteSheet;
        this.styles = styles;
        this.priceDate = proposalHeader.getPriceDate();
        this.city = proposalHeader.getProjectCity();
        this.proposalBoqsForProduct = proposalBoqsForProduct;
        this.proposalBoqsForAddon = proposalBoqsForAddon;
//        LOG.debug("Proposal boq for product : " + proposalBoqsForProduct.size());
//        LOG.debug("Proposal boq for addon : " + proposalBoqsForAddon.size());
//        LOG.debug("Inside Bow sheet creator");
    }

    public void prepare()
    {
        this.sheetProcessor = new ExcelSheetProcessor(this.quoteSheet, this.styles, this);
        this.sheetProcessor.process();
    }


    @Override
    public Object getValueForKey(String key)
    {
        return null;
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

            case "ADDONS":
                this.fillAddons(cell.getRow().getRowNum() + 2);
                break;

            default:
                break;
        }
    }

    public int fillModules(int currentRow) {
        if (this.proposalBoqsForProduct.isEmpty()) {
            return currentRow;
        }

        Map<DistinctModule,List<ProposalBOQ>> spaceRoomProducts = getDistinctModules(proposalBoqsForProduct);


        for (DistinctModule distinctModule : spaceRoomProducts.keySet()) {

            List<ProposalBOQ> proposalBoqAsPerProduct = spaceRoomProducts.get(distinctModule);

            fillHardwareAndAccPerModule(currentRow, proposalBoqAsPerProduct);

        }

        return currentRow;

    }



    private Map<DistinctModule,List<ProposalBOQ>> getDistinctModules(List<ProposalBOQ> proposalBoqs)
    {
        Map<DistinctModule, List<ProposalBOQ>> distinctModuleMap = new HashMap<>();
        for (ProposalBOQ boq : proposalBoqs)
        {
            DistinctModule distinctModule = new DistinctModule(boq);
            if (!distinctModuleMap.containsKey(distinctModule))
            {
                distinctModuleMap.put(distinctModule,new ArrayList<>());
            }
            distinctModuleMap.get(distinctModule).add(boq);
        }
        return distinctModuleMap;
    }

    public int fillAddons(int currentRow) {

//        LOG.debug("Inisde fill addons size :" + this.proposalBoqsForAddon.size());
        if (this.proposalBoqsForAddon.isEmpty()) {
            return currentRow;
        }

//        LOG.debug("Inisde fill addons size  2:" + this.proposalBoqsForAddon.size());


        fillAddonsForBoq(currentRow, proposalBoqsForAddon);

        return currentRow;
    }



    private int fillHardwareAndAccPerModule(int startRow, List<ProposalBOQ> proposalBoqs)
    {
        int currentRow = startRow;
        proposalBoqs.sort(Comparator.comparing(ProposalBOQ::getDsoItemSeq));

//        proposalBoqs.forEach(System.out::println);
//        LOG.debug("SIZE INSIDE :" + proposalBoqs.size() + " : " +proposalBoqs.get(0));

        createBoqLineItemHeadingRow(currentRow,proposalBoqs.get(0));
        currentRow++;

        for (int i =1; i<proposalBoqs.size() ; i++)
        {
//            LOG.debug("Creating data row :" +  i + " : " + proposalBoqs.get(i) );
            createBoqLineItemDataRow(currentRow,proposalBoqs.get(i));
            currentRow++;
        }

        return currentRow;
    }

    private int fillAddonsForBoq(int startRow, List<ProposalBOQ> proposalBoqs)
    {
        int currentRow = startRow;
        LOG.debug("Proposal boqs for addon: " + proposalBoqs.size());

        for (int i =0; i<proposalBoqs.size() ; i++)
        {
            LOG.debug("Creating row for addon :" + proposalBoqs.get(i));
            createBoqLineItemHeadingRowForAddon(currentRow,proposalBoqs.get(i));
            currentRow++;
        }

        return currentRow;
    }



    private void createBoqLineItemDataRow(int rowNum, ProposalBOQ proposalBoq)
    {
        /*LOG.debug("Create Module Component data row" + rowNum);
        LOG.debug("Proposal BOQ :" + proposalBoq);*/
        Row dataRow = this.createRow(rowNum, this.quoteSheet);


        this.createCellWithData(dataRow, SPACE_TYPE_CELL, Cell.CELL_TYPE_BLANK, "").setCellStyle(this.styles.getIndexStyle());
        this.createCellWithData(dataRow, ROOM_CELL, Cell.CELL_TYPE_BLANK, "").setCellStyle(this.styles.getIndexStyle());
        this.createCellWithData(dataRow, CATEGORY_CELL, Cell.CELL_TYPE_BLANK, "").setCellStyle(this.styles.getIndexStyle());
        this.createCellWithData(dataRow, PRODUCT_ID, Cell.CELL_TYPE_BLANK, "").setCellStyle(this.styles.getIndexStyle());
        this.createCellWithData(dataRow, PRODUCT_OR_SERVICE, Cell.CELL_TYPE_BLANK, "").setCellStyle(this.styles.getIndexStyle());
        this.createCellWithData(dataRow, MODULE_SEQ, Cell.CELL_TYPE_BLANK, "").setCellStyle(this.styles.getIndexStyle());
        this.createCellWithData(dataRow, MODULE_CODE, Cell.CELL_TYPE_BLANK,"").setCellStyle(this.styles.getIndexStyle());

        this.createCellWithData(dataRow, CUSTOM_CHECK, Cell.CELL_TYPE_BLANK, "").setCellStyle(this.styles.getIndexStyle());
        this.createCellWithData(dataRow, CUSTOM_REMARKS, Cell.CELL_TYPE_BLANK, "").setCellStyle(this.styles.getIndexStyle());
        this.createCellWithData(dataRow, COMPONENT_CATEGORY, Cell.CELL_TYPE_STRING, proposalBoq.getItemCategory()).setCellStyle(this.styles.getColoredCellStyle());

        this.createCellWithData(dataRow, DSO_ERP_ITEM_CODE, Cell.CELL_TYPE_STRING, proposalBoq.getDsoErpItemCode()).setCellStyle(this.styles.getColoredCellStyle());
        this.createCellWithData(dataRow, DSO_REFERENCE_PART_NO, Cell.CELL_TYPE_STRING, proposalBoq.getDsoReferencePartNo()).setCellStyle(this.styles.getColoredCellStyle());
        this.createCellWithData(dataRow, DSO_DESCRIPTION, Cell.CELL_TYPE_STRING, proposalBoq.getDsoDescription()).setCellStyle(this.styles.getColoredCellStyle());
        this.createCellWithData(dataRow, DSO_UOM, Cell.CELL_TYPE_STRING, proposalBoq.getDsoUom()).setCellStyle(this.styles.getColoredCellStyle());
        this.createCellWithData(dataRow, DSO_RATE, Cell.CELL_TYPE_STRING, proposalBoq.getDsoRate()).setCellStyle(this.styles.getColoredCellStyle());
        this.createCellWithData(dataRow, DSO_QTY, Cell.CELL_TYPE_STRING, proposalBoq.getDsoQty()).setCellStyle(this.styles.getColoredCellStyle());
        this.createCellWithData(dataRow, DSO_PRICE, Cell.CELL_TYPE_STRING, proposalBoq.getDsoPrice()).setCellStyle(this.styles.getColoredCellStyle());

        //Inserting Cell data for Planner version
        this.createCellWithData(dataRow, PLANNER_ERP_ITEM_CODE, Cell.CELL_TYPE_STRING, proposalBoq.getPlannerErpItemCode()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, PLANNER_REFERENCE_PART_NO, Cell.CELL_TYPE_STRING, proposalBoq.getPlannerReferencePartNo()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, PLANNER_DESCRIPTION, Cell.CELL_TYPE_STRING, proposalBoq.getPlannerDescription()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, PLANNER_UOM, Cell.CELL_TYPE_STRING, proposalBoq.getPlannerUom()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, PLANNER_RATE, Cell.CELL_TYPE_STRING, proposalBoq.getPlannerRate()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, PLANNER_QTY, Cell.CELL_TYPE_STRING, proposalBoq.getPlannerQty()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, PLANNER_PRICE, 1, proposalBoq.getPlannerPrice()).setCellStyle(this.styles.getTextStyle());

        //hidden rows used for querying while updating the data
        this.createCellWithData(dataRow, SPACE_TYPE_HIDDEN, Cell.CELL_TYPE_STRING, proposalBoq.getSpaceType()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, ROOM_HIDDEN, Cell.CELL_TYPE_STRING, proposalBoq.getROOM()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, CATEGORY_HIDDEN, Cell.CELL_TYPE_STRING, proposalBoq.getcategory()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, PRODUCT_HIDDEN, Cell.CELL_TYPE_STRING, proposalBoq.getProductService()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, PRODUCT_ID_HIDDEN, Cell.CELL_TYPE_STRING, proposalBoq.getProductId()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, MODULE_SEQ_HIDDEN, Cell.CELL_TYPE_STRING, proposalBoq.getModuleSeq()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, MODULE_HIDDEN, Cell.CELL_TYPE_STRING, proposalBoq.getMgCode()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, MYGUBBI_ERPCODE_HIDDEN, Cell.CELL_TYPE_STRING, proposalBoq.getDsoErpItemCode()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, DISPLAY_ORDER, Cell.CELL_TYPE_STRING, proposalBoq.getDsoItemSeq()).setCellStyle(this.styles.getTextStyle());

    }

    private void createBoqLineItemHeadingRow(int rowNum, ProposalBOQ proposalBoq)
    {
    /*    LOG.debug("Create Module Component Heading row" + rowNum);
        LOG.debug("Proposal BOQ :" + proposalBoq);*/
        Row dataRow = this.createRow(rowNum, this.quoteSheet);

        Module module = ModuleDataService.getInstance().getModule(proposalBoq.getMgCode());


        this.createCellWithData(dataRow, SPACE_TYPE_CELL, Cell.CELL_TYPE_STRING, proposalBoq.getSpaceType()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, ROOM_CELL, Cell.CELL_TYPE_STRING, proposalBoq.getROOM()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, CATEGORY_CELL, Cell.CELL_TYPE_STRING, proposalBoq.getcategory()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, PRODUCT_ID, Cell.CELL_TYPE_STRING, proposalBoq.getProductId()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, PRODUCT_OR_SERVICE, Cell.CELL_TYPE_STRING, proposalBoq.getProductService()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, MODULE_SEQ, Cell.CELL_TYPE_STRING, proposalBoq.getModuleSeq()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, MODULE_CODE, Cell.CELL_TYPE_STRING, module.getDescription()+ ":" + proposalBoq.getMgCode()).setCellStyle(this.styles.getTextStyle());

        this.createCellWithData(dataRow, CUSTOM_CHECK, Cell.CELL_TYPE_STRING, proposalBoq.getCustomCheck()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, CUSTOM_REMARKS, Cell.CELL_TYPE_STRING, proposalBoq.getCustomRemarks()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, COMPONENT_CATEGORY, Cell.CELL_TYPE_STRING, proposalBoq.getItemCategory()).setCellStyle(this.styles.getColoredCellStyle());


//       dataRow.setHeight(new Double(dataRow.getHeight() * 1.5).shortValue());
        //Inserting Cell data for DSO version
        this.createCellWithData(dataRow, DSO_ERP_ITEM_CODE, Cell.CELL_TYPE_STRING, proposalBoq.getDsoErpItemCode()).setCellStyle(this.styles.getColoredCellStyle());
        this.createCellWithData(dataRow, DSO_REFERENCE_PART_NO, Cell.CELL_TYPE_STRING, proposalBoq.getDsoReferencePartNo()).setCellStyle(this.styles.getColoredCellStyle());
        this.createCellWithData(dataRow, DSO_DESCRIPTION, Cell.CELL_TYPE_STRING, proposalBoq.getDsoDescription()).setCellStyle(this.styles.getColoredCellStyle());
        this.createCellWithData(dataRow, DSO_UOM, Cell.CELL_TYPE_STRING, proposalBoq.getDsoUom()).setCellStyle(this.styles.getColoredCellStyle());
        this.createCellWithData(dataRow, DSO_RATE, Cell.CELL_TYPE_STRING, proposalBoq.getDsoRate()).setCellStyle(this.styles.getColoredCellStyle());
        this.createCellWithData(dataRow, DSO_QTY, Cell.CELL_TYPE_STRING, proposalBoq.getDsoQty()).setCellStyle(this.styles.getColoredCellStyle());
        this.createCellWithData(dataRow, DSO_PRICE, Cell.CELL_TYPE_STRING, proposalBoq.getDsoPrice()).setCellStyle(this.styles.getColoredCellStyle());

        //Inserting Cell data for Planner version
        this.createCellWithData(dataRow, PLANNER_ERP_ITEM_CODE, Cell.CELL_TYPE_STRING, proposalBoq.getPlannerErpItemCode()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, PLANNER_REFERENCE_PART_NO, Cell.CELL_TYPE_STRING, proposalBoq.getPlannerReferencePartNo()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, PLANNER_DESCRIPTION, Cell.CELL_TYPE_STRING, proposalBoq.getPlannerDescription()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, PLANNER_UOM, Cell.CELL_TYPE_STRING, proposalBoq.getPlannerUom()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, PLANNER_RATE, Cell.CELL_TYPE_STRING, proposalBoq.getPlannerRate()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, PLANNER_QTY, Cell.CELL_TYPE_STRING, proposalBoq.getPlannerQty()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, PLANNER_PRICE, 1, proposalBoq.getPlannerPrice()).setCellStyle(this.styles.getTextStyle());

        this.createCellWithData(dataRow, SPACE_TYPE_HIDDEN, Cell.CELL_TYPE_STRING, proposalBoq.getSpaceType()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, ROOM_HIDDEN, Cell.CELL_TYPE_STRING, proposalBoq.getROOM()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, CATEGORY_HIDDEN, Cell.CELL_TYPE_STRING, proposalBoq.getcategory()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, PRODUCT_HIDDEN, Cell.CELL_TYPE_STRING, proposalBoq.getProductService()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, PRODUCT_ID_HIDDEN, Cell.CELL_TYPE_STRING, proposalBoq.getProductId()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, MODULE_SEQ_HIDDEN, Cell.CELL_TYPE_STRING, proposalBoq.getModuleSeq()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, MODULE_HIDDEN, Cell.CELL_TYPE_STRING, proposalBoq.getMgCode()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, MYGUBBI_ERPCODE_HIDDEN, Cell.CELL_TYPE_STRING, proposalBoq.getDsoErpItemCode()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, DISPLAY_ORDER, Cell.CELL_TYPE_STRING, proposalBoq.getDsoItemSeq()).setCellStyle(this.styles.getTextStyle());


    }

    private void createBoqLineItemHeadingRowForAddon(int rowNum, ProposalBOQ proposalBoq)
    {
    /*    LOG.debug("Create Module Component Heading row" + rowNum);
        LOG.debug("Proposal BOQ :" + proposalBoq);*/
        Row dataRow = this.createRow(rowNum, this.quoteSheet);

        Module module = ModuleDataService.getInstance().getModule(proposalBoq.getMgCode());


        this.createCellWithData(dataRow, SPACE_TYPE_CELL, Cell.CELL_TYPE_STRING, proposalBoq.getSpaceType()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, ROOM_CELL, Cell.CELL_TYPE_STRING, proposalBoq.getROOM()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, CATEGORY_CELL, Cell.CELL_TYPE_STRING, proposalBoq.getcategory()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, PRODUCT_ID, Cell.CELL_TYPE_STRING, proposalBoq.getProductId()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, PRODUCT_OR_SERVICE, Cell.CELL_TYPE_STRING, proposalBoq.getProductService()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, MODULE_SEQ, Cell.CELL_TYPE_STRING, proposalBoq.getModuleSeq()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, MODULE_CODE, Cell.CELL_TYPE_STRING, proposalBoq.getMgCode()).setCellStyle(this.styles.getTextStyle());

        this.createCellWithData(dataRow, CUSTOM_CHECK, Cell.CELL_TYPE_STRING, proposalBoq.getCustomCheck()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, CUSTOM_REMARKS, Cell.CELL_TYPE_STRING, proposalBoq.getCustomRemarks()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, COMPONENT_CATEGORY, Cell.CELL_TYPE_STRING, proposalBoq.getItemCategory()).setCellStyle(this.styles.getColoredCellStyle());


//       dataRow.setHeight(new Double(dataRow.getHeight() * 1.5).shortValue());
        //Inserting Cell data for DSO version
        this.createCellWithData(dataRow, DSO_ERP_ITEM_CODE, Cell.CELL_TYPE_STRING, proposalBoq.getDsoErpItemCode()).setCellStyle(this.styles.getColoredCellStyle());
        this.createCellWithData(dataRow, DSO_REFERENCE_PART_NO, Cell.CELL_TYPE_STRING, proposalBoq.getDsoReferencePartNo()).setCellStyle(this.styles.getColoredCellStyle());
        this.createCellWithData(dataRow, DSO_DESCRIPTION, Cell.CELL_TYPE_STRING, proposalBoq.getDsoDescription()).setCellStyle(this.styles.getColoredCellStyle());
        this.createCellWithData(dataRow, DSO_UOM, Cell.CELL_TYPE_STRING, proposalBoq.getDsoUom()).setCellStyle(this.styles.getColoredCellStyle());
        this.createCellWithData(dataRow, DSO_RATE, Cell.CELL_TYPE_STRING, proposalBoq.getDsoRate()).setCellStyle(this.styles.getColoredCellStyle());
        this.createCellWithData(dataRow, DSO_QTY, Cell.CELL_TYPE_STRING, proposalBoq.getDsoQty()).setCellStyle(this.styles.getColoredCellStyle());
        this.createCellWithData(dataRow, DSO_PRICE, Cell.CELL_TYPE_STRING, proposalBoq.getDsoPrice()).setCellStyle(this.styles.getColoredCellStyle());

        //Inserting Cell data for Planner version
        this.createCellWithData(dataRow, PLANNER_ERP_ITEM_CODE, Cell.CELL_TYPE_STRING, proposalBoq.getPlannerErpItemCode()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, PLANNER_REFERENCE_PART_NO, Cell.CELL_TYPE_STRING, proposalBoq.getPlannerReferencePartNo()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, PLANNER_DESCRIPTION, Cell.CELL_TYPE_STRING, proposalBoq.getPlannerDescription()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, PLANNER_UOM, Cell.CELL_TYPE_STRING, proposalBoq.getPlannerUom()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, PLANNER_RATE, Cell.CELL_TYPE_STRING, proposalBoq.getPlannerRate()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, PLANNER_QTY, Cell.CELL_TYPE_STRING, proposalBoq.getPlannerQty()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, PLANNER_PRICE, 1, proposalBoq.getPlannerPrice()).setCellStyle(this.styles.getTextStyle());

        this.createCellWithData(dataRow, SPACE_TYPE_HIDDEN, Cell.CELL_TYPE_STRING, proposalBoq.getSpaceType()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, ROOM_HIDDEN, Cell.CELL_TYPE_STRING, proposalBoq.getROOM()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, CATEGORY_HIDDEN, Cell.CELL_TYPE_STRING, proposalBoq.getcategory()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, PRODUCT_HIDDEN, Cell.CELL_TYPE_STRING, proposalBoq.getProductService()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, PRODUCT_ID_HIDDEN, Cell.CELL_TYPE_STRING, proposalBoq.getProductId()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, MODULE_SEQ_HIDDEN, Cell.CELL_TYPE_STRING, proposalBoq.getModuleSeq()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, MODULE_HIDDEN, Cell.CELL_TYPE_STRING, proposalBoq.getMgCode()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, MYGUBBI_ERPCODE_HIDDEN, Cell.CELL_TYPE_STRING, proposalBoq.getDsoErpItemCode()).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, DISPLAY_ORDER, Cell.CELL_TYPE_STRING, proposalBoq.getDsoItemSeq()).setCellStyle(this.styles.getTextStyle());


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
