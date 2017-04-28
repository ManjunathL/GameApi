package com.mygubbi.game.proposal.jobcard;

import com.mygubbi.game.proposal.ModuleDataService;
import com.mygubbi.game.proposal.ProductModule;
import com.mygubbi.game.proposal.model.Module;
import com.mygubbi.game.proposal.model.ShutterFinish;
import com.mygubbi.game.proposal.quote.AssembledProductInQuote;
import com.mygubbi.game.proposal.quote.QuoteData;
import com.mygubbi.si.excel.ExcelCellProcessor;
import com.mygubbi.si.excel.ExcelSheetProcessor;
import com.mygubbi.si.excel.ExcelStyles;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

/**
 * Created by Sunil on 22-05-2016.
 */
public class ModuleSheetCreator implements ExcelCellProcessor
{
    private final static Logger LOG = LogManager.getLogger(ModuleSheetCreator.class);

    private QuoteData quoteData;
    private Sheet sheet;
    private ExcelStyles styles;
    private AssembledProductInQuote product;
    private ExcelSheetProcessor sheetProcessor;

    public ModuleSheetCreator(Sheet sheet, QuoteData quoteData, AssembledProductInQuote product, ExcelStyles styles)
    {
        this.sheet = sheet;
        this.quoteData = quoteData;
        this.styles = styles;
        this.product = product;
    }

    public void prepare()
    {
        this.sheetProcessor = new ExcelSheetProcessor(this.sheet, this.styles, this);
        this.sheetProcessor.process();
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
            case "Modules":
                this.fillModules(this.product.getModules(), cell.getRow().getRowNum() + 1, "No Modules.");
                break;

            default:
                break;
        }
    }

    private int fillModules(List<ProductModule> modules, int currentRow, String defaultMessage)
    {
        if (modules == null || modules.isEmpty())
        {
            currentRow++;
            this.sheetProcessor.createDataRowInDataSheet(currentRow, new String[]{defaultMessage});
            return currentRow;
        }

        int seq = 1;
        for (ProductModule module : modules)
        {
            currentRow++;
            Module mgModule = ModuleDataService.getInstance().getModule(module.getMGCode());
            ShutterFinish finish = ModuleDataService.getInstance().getFinish(module.getFinishCode());
            this.sheetProcessor.createDataRowInDataSheet(currentRow, new Object[]{seq, module.getUnit(), mgModule.getDescription(),
                    mgModule.getWidth(), mgModule.getDepth(), mgModule.getHeight(), 1, module.getCarcassCode(), finish.getTitle(), mgModule.getDimension()});
            seq++;
        }
        return currentRow;
    }
}
