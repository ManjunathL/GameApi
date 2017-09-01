package com.mygubbi.game.proposal.jobcard;

import com.mygubbi.common.StringUtils;
import com.mygubbi.game.proposal.ModuleAccessoryPack;
import com.mygubbi.game.proposal.ModuleDataService;
import com.mygubbi.game.proposal.ProductModule;
import com.mygubbi.game.proposal.model.AccHwComponent;
import com.mygubbi.game.proposal.model.AccessoryPackComponent;
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

import java.util.Collection;
import java.util.List;

/**
 * Created by Shruthi on 8/30/2017.
 */
public class AccessorySheetCreator implements ExcelCellProcessor
{
    private final static Logger LOG = LogManager.getLogger(AccessorySheetCreator.class);
    private final AssembledProductInQuote product;
    private QuoteData quoteData;
    private Sheet hardwareSheet;
    private ExcelStyles styles;

    public AccessorySheetCreator(Sheet hardwareSheet, QuoteData quoteData, AssembledProductInQuote product, ExcelStyles styles)
    {
        this.hardwareSheet = hardwareSheet;
        this.quoteData = quoteData;
        this.styles = styles;
        this.product = product;
    }
    public void prepare()
    {
        new ExcelSheetProcessor(this.hardwareSheet, this.styles, this).process();
    }

    @Override
    public Object getValueForKey(String key)
    {
        Object value = this.quoteData.getValue(key);
        if (value == null)
        {
            return this.product.getValue(key);
        }
        return value;
    }

    @Override
    public void processCell(Cell cell, String cellValue)
    {
        switch (cellValue)
        {
            case "Accessories":
                //this.fillComponents(this.quoteData.getAllModuleAcessories(), cell.getRow().getRowNum() + 1, "No accessories.");
                this.fillComponents(this.product.getModules(), cell.getRow().getRowNum() + 1, "No accessories.");
                break;

            default:
                break;
        }

    }

    private int fillComponents(List<ProductModule> components, int currentRow, String defaultMessage)
    {
        CellStyle style = this.hardwareSheet.getRow(currentRow + 1).getRowStyle();

        if (components == null || components.isEmpty())
        {
            currentRow++;
            this.createDataRowInDataSheet(currentRow, new String[]{defaultMessage}, style);
            return currentRow;
        }

        int seq = 1;
        for (ProductModule component : components)
        {
            for(ModuleAccessoryPack moduleAccessoryPack:component.getAccessoryPacks())
            {
                Collection<AccessoryPackComponent> accessoryPackComponents= ModuleDataService.getInstance().getAccessoryPackComponents(moduleAccessoryPack.getAccessoryPackCode());
                if (accessoryPackComponents == null) continue;
                for (AccessoryPackComponent accessoryPackComponent : accessoryPackComponents)
                {
                    if (accessoryPackComponent.isAccessory()) {
                        AccHwComponent accHwComponent = ModuleDataService.getInstance().getAccessory(accessoryPackComponent.getComponentCode());
                        if (accHwComponent == null) continue;
                        if(accHwComponent.getCategory().equals("Primary")) {
                            currentRow++;
                            this.createDataRowInDataSheet(currentRow, new String[]{String.valueOf(seq), component.getMGCode(), accHwComponent.getTitle(), accHwComponent.getERPCode(), accHwComponent.getMake()}, style);
                            seq++;
                        }
                    }
                }
            }


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
