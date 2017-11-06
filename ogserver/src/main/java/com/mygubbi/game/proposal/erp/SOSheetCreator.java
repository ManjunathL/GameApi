package com.mygubbi.game.proposal.erp;

import com.mygubbi.game.proposal.model.ProposalBOQ;
import com.mygubbi.game.proposal.model.SOPart;
import com.mygubbi.si.excel.ExcelCellProcessor;
import com.mygubbi.si.excel.ExcelSheetProcessor;
import com.mygubbi.si.excel.ExcelStyles;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.Comparator;
import java.util.List;

/**
 * Created by User on 17-08-2017.
 */
public class SOSheetCreator implements ExcelCellProcessor {

    private final static Logger LOG = LogManager.getLogger(SOSheetCreator.class);


    private static final int SR_NO_CELL = 0;
    private static final int ITEM_CELL = 1;
    private static final int UOM_CELL = 2;
    private static final int QTY_CELL = 3;
    private static final int REFERENCE_PART_NO_CELL = 4;
    private static final int TITLE_CELL = 5;
    private static final int DISPLAY_ORDER = 6;


    private XSSFSheet quoteSheet;
    private ExcelStyles styles;
    private ExcelSheetProcessor sheetProcessor;
    private List<SOPart> proposalBoqs;



    public SOSheetCreator( XSSFSheet quoteSheet, ExcelStyles styles, List<SOPart> proposalBoqs)
    {
        this.quoteSheet = quoteSheet;
        this.styles = styles;
        this.proposalBoqs = proposalBoqs;
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
            case "SrNo":
                //int currentRow = this.fillAssembledProducts(cell.getRow().getRowNum());
                this.fillBoqAsPerProduct(cell.getRow().getRowNum() + 1);
                break;

            default:
                break;
        }

    }


    private int fillBoqAsPerProduct(int currentRow) {

        if (this.proposalBoqs.isEmpty())
        {
            return currentRow;
        }

        fillHardwareAndAccPerModule(currentRow,proposalBoqs);

        return currentRow;

    }

    private int fillHardwareAndAccPerModule(int startRow, List<SOPart> proposalBoqs)
    {
        int currentRow = startRow;

        proposalBoqs.sort(Comparator.comparing(SOPart::getDisplayOrder));


        for (SOPart proposal_boq : proposalBoqs) {
            createDataRow(currentRow, proposal_boq);
            currentRow++;
        }

        return currentRow;
    }



    private void createDataRow(int rowNum, SOPart proposal_boq)
    {

//        LOG.debug("Creatring row in erp sheet :" + rowNum);

        Row dataRow = this.createRow(rowNum, this.quoteSheet);

//        LOG.debug("Creating rows for ERP Master :" + proposal_boq);

        this.createCellWithData(dataRow, SR_NO_CELL, Cell.CELL_TYPE_STRING, rowNum).setCellStyle(this.styles.getIndexStyle());
        this.createCellWithData(dataRow, ITEM_CELL, Cell.CELL_TYPE_STRING, proposal_boq.getErpCode()).setCellStyle(this.styles.getIndexStyle());
        this.createCellWithData(dataRow, UOM_CELL, Cell.CELL_TYPE_STRING, proposal_boq.getUom()).setCellStyle(this.styles.getIndexStyle());
        this.createCellWithData(dataRow, QTY_CELL, Cell.CELL_TYPE_STRING, proposal_boq.getQty()).setCellStyle(this.styles.getIndexStyle());
        this.createCellWithData(dataRow, REFERENCE_PART_NO_CELL, Cell.CELL_TYPE_STRING, proposal_boq.getReferencePartNo()).setCellStyle(this.styles.getIndexStyle());
        this.createCellWithData(dataRow, TITLE_CELL, Cell.CELL_TYPE_STRING, proposal_boq.getTitle()).setCellStyle(this.styles.getIndexStyle());
        this.createCellWithData(dataRow, DISPLAY_ORDER, Cell.CELL_TYPE_STRING, proposal_boq.getDisplayOrder()).setCellStyle(this.styles.getIndexStyle());

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
