package com.mygubbi.game.proposal.price;

import com.mygubbi.game.proposal.model.RateCard;
import com.mygubbi.si.excel.ExcelCellProcessor;
import com.mygubbi.si.excel.ExcelSheetProcessor;
import com.mygubbi.si.excel.ExcelStyles;
import io.vertx.core.json.JsonObject;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;
import java.util.Set;

/**
 * Created by Sunil on 22-05-2016.
 */
public class PriceSheetCreator implements ExcelCellProcessor
{
    private final static Logger LOG = LogManager.getLogger(PriceSheetCreator.class);

    private ModulePriceHolder priceHolder;
    private Sheet sheet;
    private ExcelStyles styles;
    private ExcelSheetProcessor sheetProcessor;

    public PriceSheetCreator(Sheet sheet, ModulePriceHolder priceHolder, ExcelStyles styles)
    {
        this.sheet = sheet;
        this.priceHolder = priceHolder;
        this.styles = styles;
    }

    public void prepare()
    {
        this.sheetProcessor = new ExcelSheetProcessor(this.sheet, this.styles, this);
        this.sheetProcessor.process();
    }

    @Override
    public Object getValueForKey(String key)
    {
        return "NA";
    }

    @Override
    public void processCell(Cell cell, String cellValue)
    {
        switch (cellValue)
        {
            case "Panels":
                this.fillPanels(cell.getRow().getRowNum() + 1, "No Panels.");
                break;

            case "Hardware":
                this.fillHardware(cell.getRow().getRowNum() + 1, "No Hardware.");
                break;

            case "Accessories":
                this.fillAccessories(cell.getRow().getRowNum() + 1, "No Accessories.");
                break;

            case "Rate Cards":
                this.fillRateCards(cell.getRow().getRowNum() + 1, "No Rate Cards.");
                break;

            case "Price Breakup":
                this.fillDetails(cell.getRow().getRowNum() + 1, "No Details.", this.priceHolder.getPriceJson());
                break;

            case "Module Details":
            this.fillDetails(cell.getRow().getRowNum() + 1, "No Details.", this.priceHolder.getProductModule());
            break;

            default:
                break;
        }

    }

    private int fillPanels(int currentRow, String defaultMessage)
    {
        List<PanelComponent> panels = this.priceHolder.getPanelComponents();
        if (panels == null || panels.isEmpty())
        {
            currentRow++;
            this.sheetProcessor.createDataRowInDataSheet(currentRow, new String[]{defaultMessage});
            return currentRow;
        }

        int seq = 1;
        for (PanelComponent panel : panels)
        {
            currentRow++;
            this.sheetProcessor.createDataRowInDataSheet(currentRow, new Object[]{seq, panel.getCode(), panel.getType(), panel.getSide(),
                    panel.getLength(), panel.getBreadth(), panel.getThickness(), panel.getExposed(), panel.getQuantity(),
                    panel.getUnitCost(), panel.getCost()});
            seq++;
        }
        return currentRow;
    }

    private int fillHardware(int currentRow, String defaultMessage)
    {
        List<HardwareComponent> hws = this.priceHolder.getHardwareComponents();
        if (hws == null || hws.isEmpty())
        {
            currentRow++;
            this.sheetProcessor.createDataRowInDataSheet(currentRow, new String[]{defaultMessage});
            return currentRow;
        }

        int seq = 1;
        for (HardwareComponent hw : hws)
        {
            currentRow++;
            this.sheetProcessor.createDataRowInDataSheet(currentRow, new Object[]{seq, hw.getComponent().getCode(),
                    hw.getComponent().getTitle(), hw.getComponent().getMake(), hw.getComponent().getUom(),
                    hw.getComponent().getPrice(), hw.getQuantityFormula(), hw.getQuantity(), hw.getCost()});
            seq++;
        }
        return currentRow;
    }

    private int fillAccessories(int currentRow, String defaultMessage)
    {
        List<AccessoryComponent> accessoryComponents = this.priceHolder.getAccessoryComponents();
        if (accessoryComponents == null || accessoryComponents.isEmpty())
        {
            currentRow++;
            this.sheetProcessor.createDataRowInDataSheet(currentRow, new String[]{defaultMessage});
            return currentRow;
        }

        int seq = 1;
        for (AccessoryComponent acc : accessoryComponents)
        {
            currentRow++;
            this.sheetProcessor.createDataRowInDataSheet(currentRow, new Object[]{seq, acc.getComponent().getCode(),
                    acc.getComponent().getTitle(), acc.getComponent().getMake(), acc.getComponent().getUom(),
                    acc.getComponent().getPrice(), acc.getQuantity(), acc.getCost()});
            seq++;
        }
        return currentRow;
    }

    private int fillRateCards(int currentRow, String defaultMessage)
    {
        List<RateCard> rateCards = this.priceHolder.getRateCards();
        if (rateCards == null || rateCards.isEmpty())
        {
            currentRow++;
            this.sheetProcessor.createDataRowInDataSheet(currentRow, new String[]{defaultMessage});
            return currentRow;
        }

        int seq = 1;
        for (RateCard rc : rateCards)
        {
            if (rc == null) continue;
            currentRow++;
            this.sheetProcessor.createDataRowInDataSheet(currentRow, new Object[]{seq, rc.getName(),
                    rc.getType(), rc.getCode(), rc.getRate(), rc.getRates()});
            seq++;
        }
        return currentRow;
    }

    private int fillDetails(int currentRow, String defaultMessage, JsonObject jsonObject)
    {
        if (jsonObject == null)
        {
            currentRow++;
            this.sheetProcessor.createDataRowInDataSheet(currentRow, new String[]{defaultMessage});
            return currentRow;
        }

        Set<String> fieldNames = jsonObject.fieldNames();
        for (String field : fieldNames)
        {
            currentRow++;
            this.sheetProcessor.createDataRowInDataSheet(currentRow, new Object[]{field, jsonObject.getValue(field)});
        }
        return currentRow;
    }

}
