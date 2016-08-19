package com.mygubbi.game.proposal.jobcard;

import com.mygubbi.game.proposal.ModuleDataService;
import com.mygubbi.game.proposal.ProductModule;
import com.mygubbi.game.proposal.model.*;
import com.mygubbi.game.proposal.quote.AssembledProductInQuote;
import com.mygubbi.game.proposal.quote.QuoteData;
import com.mygubbi.si.excel.ExcelCellProcessor;
import com.mygubbi.si.excel.ExcelSheetProcessor;
import com.mygubbi.si.excel.ExcelStyles;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

/**
 * Created by Sunil on 22-05-2016.
 */
public class JobCardSheetCreator implements ExcelCellProcessor
{
    private final static Logger LOG = LogManager.getLogger(JobCardSheetCreator.class);

    private static final String[] ALPHABET_SEQUENCE = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
            "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};

    private QuoteData quoteData;
    private Sheet jobcardSheet;
    private ExcelStyles styles;
    private AssembledProductInQuote product;
    private ExcelSheetProcessor sheetProcessor;

    public JobCardSheetCreator(Sheet jobcardSheet, QuoteData quoteData, AssembledProductInQuote product, ExcelStyles styles)
    {
        this.jobcardSheet = jobcardSheet;
        this.quoteData = quoteData;
        this.styles = styles;
        this.product = product;
    }

    public void prepare()
    {
        this.sheetProcessor = new ExcelSheetProcessor(this.jobcardSheet, this.styles, this);
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
                this.fillModules(this.product.getModules(), cell.getRow().getRowNum() + 1, "No modules.");
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
            Module mgModule = ModuleDataService.getInstance().getModule(module.getMGCode());

            currentRow++;
            this.sheetProcessor.createTitleRowInDataSheet(currentRow, new Object[]{"SL NO", "Description", "Width", "Depth", "Height/Thickness",
                    "Qty", "Remarks & Edge Binding", "Area",	"Finish", "Dimension",	"Box"});

            currentRow++;
            this.sheetProcessor.createDataRowInDataSheet(currentRow, new Object[]{seq, mgModule.getCode() + " - " + mgModule.getDescription(), mgModule.getWidth(),
                    mgModule.getDepth(), mgModule.getHeight(), 1});

            currentRow = this.fillPanels(currentRow, mgModule, module);

            currentRow++;
            this.sheetProcessor.createDataRowInDataSheet(currentRow, null);

            seq++;
        }
        return currentRow;
    }

    private int fillPanels(int currentRow, Module mgModule, ProductModule module)
    {
        int seq = 0;

        for (ModuleComponent component : ModuleDataService.getInstance().getModuleComponents(mgModule.getCode()))
        {
            if (ModuleComponent.CARCASS_TYPE.equals(component.getType()))
            {
                CarcassPanel panel = ModuleDataService.getInstance().getCarcassPanel(component.getComponentCode());
                if (panel == null) continue;

                if (seq == ALPHABET_SEQUENCE.length) seq = 0;
                currentRow++;
                this.sheetProcessor.createDataRowInDataSheet(currentRow, new Object[]{ALPHABET_SEQUENCE[seq], panel.getTitle(), panel.getLength(), panel.getBreadth(),
                        panel.getThickness(), component.getQuantity(), panel.getEdgebinding(), panel.getArea(), null, panel.getDimesions()});
                seq++;
            }
        }

        currentRow++;
        this.sheetProcessor.createTitleRowInDataSheet(currentRow, new Object[]{"Shutter", "Description", "Height", "Width", "Thickness",
                "Qty", "Remarks & Edge Binding", "Design", "Color", "Dimension",	"Box"});

        ShutterFinish shutterFinish = ModuleDataService.getInstance().getFinish(module.getFinishCode());


        for (ModuleComponent component : ModuleDataService.getInstance().getModuleComponents(mgModule.getCode()))
        {
            if (ModuleComponent.SHUTTER_TYPE.equals(component.getType()))
            {
                ShutterPanel panel = ModuleDataService.getInstance().getShutterPanel(component.getComponentCode());
                if (panel == null) continue;

                if (seq == ALPHABET_SEQUENCE.length) seq = 0;
                currentRow++;

                String edgeBinding = this.getEdgeBinding(shutterFinish, panel);
                this.sheetProcessor.createDataRowInDataSheet(currentRow, new Object[]{ALPHABET_SEQUENCE[seq], panel.getTitle(),  panel.getLength(), panel.getBreadth(),
                        panel.getThickness(), component.getQuantity(), edgeBinding, product.getProduct().getDesignCode(),
                        module.getColorCode(), panel.getDimesions()});
                seq++;
            }
        }

        currentRow++;
        return currentRow;
    }

    private String getEdgeBinding(ShutterFinish shutterFinish, ShutterPanel panel) {

        if ("Y".equals(shutterFinish.getEdgeBinding()))
        {
            return panel.getEdgebinding();
        }
        else
        {
            return shutterFinish.getEdgeBinding();
        }
    }


}
