package com.mygubbi.game.proposal.erp;

import com.mygubbi.game.proposal.model.ERPMaster;
import com.mygubbi.game.proposal.model.ProposalHeader;
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
import java.util.List;

/**
 * Created by User on 17-08-2017.
 */
public class ERPMasterSheetCreator implements ExcelCellProcessor {


    private final static Logger LOG = LogManager.getLogger(BOQSheetCreator.class);

    private XSSFSheet quoteSheet;
    private ExcelStyles styles;
    private ExcelSheetProcessor sheetProcessor;
    private Date priceDate;
    private String city;
    private XSSFWorkbook quoteWorkBook;
    private List<ERPMaster> erpMasters;




    private static final int ITEM_NAME_CELL = 0;
    private static final int REFERENCE_PART_NO_CELL = 1;
    private static final int INVENTORY_CATEGORY_CELL = 2;
    private static final int UOM_CELL = 3;
    private static final int IS_ACTIVE_CELL = 4;
    private static final int ITEM_CODE = 5;
    private static final int ITEM_REFERENCE_CODE_CELL = 6;


    public ERPMasterSheetCreator(XSSFWorkbook xssfWorkbook, XSSFSheet quoteSheet, ExcelStyles styles, List<ERPMaster> erpMasters,ProposalHeader proposalHeader)
    {
        this.quoteWorkBook = xssfWorkbook;
        this.quoteSheet = quoteSheet;
        this.styles = styles;
        this.priceDate = proposalHeader.getPriceDate();
        this.city = proposalHeader.getProjectCity();
        this.erpMasters = erpMasters;
        LOG.debug("Inside ERP master sheet creator");
        LOG.debug("ERP Master size :" + erpMasters.size());
        LOG.debug("QUote sheet in erp sheet creator : " + this.quoteSheet.getSheetName());
        LOG.debug("QUote sheet in erp sheet creator : " + this.quoteSheet.getLastRowNum());


    }

    public void prepare()
    {
        this.sheetProcessor = new ExcelSheetProcessor(this.quoteSheet, this.styles, this);
        this.sheetProcessor.process();
    }



    @Override
    public Object getValueForKey(String key) {
        return null;
    }

    @Override
    public void processCell(Cell cell, String cellValue) {

        switch (cellValue)
        {
            case "Item Name":
                //int currentRow = this.fillAssembledProducts(cell.getRow().getRowNum());
                this.fillERPMasterSheet(cell.getRow().getRowNum() + 1);
                break;

            default:
                break;
        }

    }

    private int fillERPMasterSheet(int currentRow) {

        if (this.erpMasters.isEmpty())
        {
            return currentRow;
        }

        fillHardwareAndAccPerModule(currentRow,erpMasters);

        return currentRow;

    }


    private int fillHardwareAndAccPerModule(int startRow, List<ERPMaster> erpMasters)
    {
        int currentRow = startRow;

        LOG.debug("Start row :" + startRow);


        for (ERPMaster erpMaster : erpMasters) {
            createDataRow(currentRow, erpMaster);
            currentRow++;
        }

        return currentRow;
    }



    private void createDataRow(int rowNum, ERPMaster erpMaster)
    {

        LOG.debug("Creatring row in erp sheet :" + rowNum);

        Row dataRow = this.createRow(rowNum, this.quoteSheet);

        LOG.debug("Creating rows for ERP Master :" + erpMaster);


        this.createCellWithData(dataRow, ITEM_NAME_CELL, Cell.CELL_TYPE_STRING, erpMaster.getItemName()).setCellStyle(this.styles.getIndexStyle());
        this.createCellWithData(dataRow, REFERENCE_PART_NO_CELL, Cell.CELL_TYPE_STRING, erpMaster.getReferencePartNo()).setCellStyle(this.styles.getIndexStyle());
        this.createCellWithData(dataRow, INVENTORY_CATEGORY_CELL, Cell.CELL_TYPE_STRING, erpMaster.getInvCategory()).setCellStyle(this.styles.getIndexStyle());
        this.createCellWithData(dataRow, UOM_CELL, Cell.CELL_TYPE_STRING, erpMaster.getUOM()).setCellStyle(this.styles.getIndexStyle());
        this.createCellWithData(dataRow, IS_ACTIVE_CELL, Cell.CELL_TYPE_STRING, erpMaster.getIsActive()).setCellStyle(this.styles.getIndexStyle());
        this.createCellWithData(dataRow, ITEM_CODE, Cell.CELL_TYPE_STRING, erpMaster.getItemCode()).setCellStyle(this.styles.getIndexStyle());
        this.createCellWithData(dataRow, ITEM_REFERENCE_CODE_CELL, Cell.CELL_TYPE_STRING, erpMaster.getItemReferenceCode()).setCellStyle(this.styles.getIndexStyle());

    }



    private Row createRow(int currentRow, Sheet sheet)
    {
        LOG.debug("Current row :" + currentRow + " | sheet. last row :" + sheet.getLastRowNum() );

        sheet.shiftRows(currentRow, 100000, 1);
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
