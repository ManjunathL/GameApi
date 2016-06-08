package com.mygubbi.game.proposal.jobcard;

import com.mygubbi.common.StringUtils;
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

import java.util.List;

/**
 * Created by Sunil on 22-05-2016.
 */
public class HardwareSheetCreator implements ExcelCellProcessor
{
    private final static Logger LOG = LogManager.getLogger(HardwareSheetCreator.class);

    private QuoteData quoteData;
    private Sheet hardwareSheet;
    private ExcelStyles styles;

    public HardwareSheetCreator(Sheet hardwareSheet, QuoteData quoteData, ExcelStyles styles)
    {
        this.hardwareSheet = hardwareSheet;
        this.quoteData = quoteData;
        this.styles = styles;
    }

    public void prepare()
    {
        new ExcelSheetProcessor(this.hardwareSheet, this).process();
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
            case "Hardwares":
                this.fillComponents(this.quoteData.getAllModuleHardware(), cell.getRow().getRowNum() + 1, "No hardware.");
                break;

            case "Accessories":
                this.fillComponents(this.quoteData.getAllModuleAcessories(), cell.getRow().getRowNum() + 1, "No accessories.");
                break;

            default:
                break;
        }

    }

    private int fillComponents(List<AssembledProductInQuote.ModuleAccessory> components, int currentRow, String defaultMessage)
    {
        CellStyle style = this.hardwareSheet.getRow(currentRow + 1).getRowStyle();

        if (components == null || components.isEmpty())
        {
            currentRow++;
            this.createDataRowInDataSheet(currentRow, new String[]{defaultMessage}, style);
            return currentRow;
        }

        int seq = 1;
        for (AssembledProductInQuote.ModuleAccessory component : components)
        {
            currentRow++;
            this.createDataRowInDataSheet(currentRow, new String[]{String.valueOf(seq), component.title, component.make,
                    component.code, component.uom, String.valueOf(component.quantity) }, style);
            seq++;
        }
        return currentRow;
    }

    private void createDataRowInDataSheet(int rowNum, String [] data, CellStyle style)
    {
        this.createRowInDataSheet(rowNum, data, false, style);
    }

    private void createRowInDataSheet(int rowNum, String [] data, boolean isTitle, CellStyle style)
    {
        if (rowNum < this.hardwareSheet.getLastRowNum())
        {
            this.hardwareSheet.shiftRows(rowNum, this.hardwareSheet.getLastRowNum(), 1);
        }
        Row dataRow = this.hardwareSheet.createRow(rowNum);
        dataRow.setRowStyle(style);
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