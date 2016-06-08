package com.mygubbi.game.proposal.quote;

import com.mygubbi.common.StringUtils;
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
public class DataSheetCreator
{
    private final static Logger LOG = LogManager.getLogger(DataSheetCreator.class);

    private QuoteData quoteData;
    private Sheet dataSheet;
    private CellStyle boldStyle;

    public DataSheetCreator(Sheet dataSheet, QuoteData quoteData, CellStyle boldStyle)
    {
        this.dataSheet = dataSheet;
        this.quoteData = quoteData;
        this.boldStyle = boldStyle;
    }

    public void prepare()
    {
        this.processDataSheet();
    }

    private void processDataSheet()
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

        this.createDataRowInDataSheet(currentRow, new String[]{String.valueOf(sequenceNumber), product.getTitle()});

        currentRow++;
        this.createTitleRowInDataSheet(currentRow, new String[]{null, "Unit", "Carcass", "Finish", "Finish Type"});

        currentRow++;
        this.createDataRowInDataSheet(currentRow, new String[]{null, "Base unit", product.getProduct().getBaseCarcassCode(),
                product.getProduct().getFinishCode(), product.getProduct().getFinishType()});

        currentRow++;
        this.createDataRowInDataSheet(currentRow, new String[]{null, "Wall unit", product.getProduct().getWallCarcassCode(),
                product.getProduct().getFinishCode(), product.getProduct().getFinishType()});

        currentRow += 2;
        currentRow = this.fillAccessoriesInDataSheet(product.getModuleAccessories(), currentRow);

/*
        currentRow += 2;
        currentRow = this.fillHardwareInDataSheet(product.getModuleHardware(), currentRow);
*/

        currentRow++;

        return currentRow;
    }

    private int fillAccessoriesInDataSheet(List<AssembledProductInQuote.ModuleAccessory> accessories, int currentRow)
    {
        return this.fillAccHwInDataSheet(accessories, currentRow, "Accessories", "No accessories.");
    }

    private int fillHardwareInDataSheet(List<AssembledProductInQuote.ModuleAccessory> hardwares, int currentRow)
    {
        return this.fillAccHwInDataSheet(hardwares, currentRow, "Hardware", "No hardware.");
    }

    private int fillAccHwInDataSheet(List<AssembledProductInQuote.ModuleAccessory> components, int currentRow, String type, String defaultMessage)
    {
        this.createTitleRowInDataSheet(currentRow, new String[]{type, "Unit", "Module#", "Title", "Code", "Quantity", "Make"});

        if (components == null || components.isEmpty())
        {
            currentRow++;
            this.createDataRowInDataSheet(currentRow, new String[]{defaultMessage});
            return currentRow;
        }

        for (AssembledProductInQuote.ModuleAccessory component : components)
        {
            currentRow++;
            this.createDataRowInDataSheet(currentRow, new String[]{null, component.unit, String.valueOf(component.seq),
                    component.title, component.code, String.valueOf(component.quantity), component.make});
        }
        return currentRow;
    }

    private void createDataRowInDataSheet(int rowNum, String [] data)
    {
        this.createRowInDataSheet(rowNum, data, false);
    }

    private void createTitleRowInDataSheet(int rowNum, String [] data)
    {
        this.createRowInDataSheet(rowNum, data, true);
    }

    private void createRowInDataSheet(int rowNum, String [] data, boolean isTitle)
    {
        Row dataRow = this.dataSheet.createRow(rowNum);
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
                    cell.setCellStyle(this.boldStyle);
                }
            }
        }
    }

}
