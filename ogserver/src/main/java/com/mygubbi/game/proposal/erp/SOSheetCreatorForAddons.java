package com.mygubbi.game.proposal.erp;

import com.mygubbi.game.proposal.model.SOPart;
import com.mygubbi.game.proposal.model.SOPartForAddon;
import com.mygubbi.si.excel.ExcelCellProcessor;
import com.mygubbi.si.excel.ExcelSheetProcessor;
import com.mygubbi.si.excel.ExcelStyles;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.List;

/**
 * Created by User on 17-08-2017.
 */
public class SOSheetCreatorForAddons implements ExcelCellProcessor {

    private final static Logger LOG = LogManager.getLogger(SOSheetCreatorForAddons.class);


    private static final int SR_NO_CELL = 0;
    private static final int ITEM_CELL = 1;
    private static final int UOM_CELL = 2;
    private static final int QTY_CELL = 3;
    private static final int REFERENCE_PART_NO_CELL = 4;
    private static final int TITLE_CELL = 5;


    private XSSFSheet quoteSheet;
    private ExcelStyles styles;
    private ExcelSheetProcessor sheetProcessor;
    private List<SOPartForAddon> proposalBoqs;



    public SOSheetCreatorForAddons(XSSFSheet quoteSheet, ExcelStyles styles, List<SOPartForAddon> proposalBoqs)
    {
        this.quoteSheet = quoteSheet;
        this.styles = styles;
        this.proposalBoqs = proposalBoqs;
        LOG.debug(" Addons size : " + proposalBoqs.size());
        for (SOPartForAddon soPartForAddon : proposalBoqs)
        {
            LOG.debug("SO pARt for addon : " + soPartForAddon);
        }
//        LOG.debug("Inside Bow sheet creator");
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
            case "Sr No":
                //int currentRow = this.fillAssembledProducts(cell.getRow().getRowNum());
                this.fillBoqAddons(cell.getRow().getRowNum() + 1);
                break;

            default:
                break;
        }

    }


    private int fillBoqAddons(int currentRow) {

        if (this.proposalBoqs.isEmpty())
        {
            return currentRow;
        }

        fillHardwareAndAccPerModule(currentRow,proposalBoqs);

        return currentRow;

    }

    private int fillHardwareAndAccPerModule(int startRow, List<SOPartForAddon> proposalBoqs)
    {
        int currentRow = startRow;


        for (SOPartForAddon proposal_boq : proposalBoqs) {
            createDataRow(currentRow, proposal_boq);
            currentRow++;
        }

        return currentRow;
    }



    private void createDataRow(int rowNum, SOPartForAddon proposal_boq)
    {

//        LOG.debug("Creatring row in erp sheet :" + rowNum);

        Row dataRow = this.createRow(rowNum, this.quoteSheet);

//        LOG.debug("Creating rows for ERP Master :" + proposal_boq);

        this.createCellWithData(dataRow, SR_NO_CELL, Cell.CELL_TYPE_STRING, rowNum).setCellStyle(this.styles.getIndexStyle());
        this.createCellWithData(dataRow, ITEM_CELL, Cell.CELL_TYPE_STRING, proposal_boq.getDescription()).setCellStyle(this.styles.getIndexStyle());
        this.createCellWithData(dataRow, UOM_CELL, Cell.CELL_TYPE_STRING, proposal_boq.getDetails()).setCellStyle(this.styles.getIndexStyle());
        this.createCellWithData(dataRow, QTY_CELL, Cell.CELL_TYPE_STRING, proposal_boq.getUom()).setCellStyle(this.styles.getIndexStyle());
        this.createCellWithData(dataRow, REFERENCE_PART_NO_CELL, Cell.CELL_TYPE_STRING, proposal_boq.getQty()).setCellStyle(this.styles.getIndexStyle());
        this.createCellWithData(dataRow, TITLE_CELL, Cell.CELL_TYPE_STRING, proposal_boq.getRemarks()).setCellStyle(this.styles.getIndexStyle());

    }



    private Row createRow(int currentRow, Sheet sheet)
    {
//        LOG.debug("Current row :" + currentRow + " | sheet. last row :" + sheet.getLastRowNum() );

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
