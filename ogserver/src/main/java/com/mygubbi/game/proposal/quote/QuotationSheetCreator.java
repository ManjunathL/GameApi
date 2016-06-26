package com.mygubbi.game.proposal.quote;

import com.mygubbi.common.StringUtils;
import com.mygubbi.game.proposal.ProductAddon;
import com.mygubbi.game.proposal.ProductLineItem;
import com.mygubbi.si.excel.ExcelStyles;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.List;

/**
 * Created by Sunil on 22-05-2016.
 */
public class QuotationSheetCreator
{
    private final static Logger LOG = LogManager.getLogger(QuotationSheetCreator.class);

    private static final int TITLE_CELL = 1;
    private static final int INDEX_CELL = 0;
    private static final int QUANTITY_CELL = 2;
    private static final int RATE_CELL = 3;
    private static final int AMOUNT_CELL = 4;

    private static final String[] ALPHABET_SEQUENCE = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j"};
    private static final String[] ROMAN_SEQUENCE = new String[]{"i", "ii", "iii", "iv", "v", "vi", "vii", "viii", "ix", "x", "xi", "xii", "xiii", "xiv", "xv"};

    private QuoteData quoteData;
    private Sheet quoteSheet;
    private ExcelStyles styles;

    public QuotationSheetCreator(Sheet quoteSheet, QuoteData quoteData, ExcelStyles styles)
    {
        this.quoteSheet = quoteSheet;
        this.quoteData = quoteData;
        this.styles = styles;
    }

    public void prepare()
    {
        this.processQuoteSheet();
    }

    private void processQuoteSheet()
    {
        int firstRow = this.quoteSheet.getFirstRowNum();
        int lastRow = this.quoteSheet.getLastRowNum();

        int rowNum = 0;
        int cellNum = 0;
        String cellValue = null;
        try
        {
            for (rowNum = lastRow; rowNum >= firstRow; rowNum--)
            {
                Row row = this.quoteSheet.getRow(rowNum);
                int lastCell = row.getLastCellNum();
                for (cellNum = row.getFirstCellNum(); cellNum < lastCell; cellNum++)
                {
                    Cell cell = row.getCell(cellNum);
                    if (cell == null) continue;
                    if (cell.getCellType() == Cell.CELL_TYPE_STRING)
                    {
                        cellValue = cell.getStringCellValue();
                        this.handleCell(cell, cellValue);
                        cellValue = null;
                    }
                }
            }
        }
        catch (Exception e)
        {
            String message = "Error processing cell (" + rowNum + "," + cellNum + ") in sheet " + quoteSheet.getSheetName()
                    + ". Cell value: " + cellValue + " - Error:" + e.getMessage();
            LOG.error(message, e);
            throw new RuntimeException(message, e);
        }
    }

    private void handleCell(Cell cell, String cellValue)
    {
        if (StringUtils.isEmpty(cellValue)) return;

        if (cellValue.charAt(0) == '$')
        {
            this.replaceCellValue(cell, cellValue);
        }
        else
        {
            this.checkTagAndInsertData(cell, cellValue);
        }
    }

    private void checkTagAndInsertData(Cell cell, String cellValue)
    {
//        LOG.info("Processing " + cellValue);
        switch (cellValue)
        {
            case "Kitchen & Other Units":
                int currentRow = this.fillAssembledProducts(cell.getRow().getRowNum() + 2);
                this.fillCatalogProducts(currentRow);
                break;

            case "B.1":
                this.fillAddons(cell, this.quoteData.getAccessories(), "No additional accessories.");
                break;

            case "B.2":
                this.fillAddons(cell, this.quoteData.getAppliances(), "No additional appliances.");
                break;

            case "B.3":
                this.fillAddons(cell, this.quoteData.getCounterTops(), "No countertops.");
                break;

            case "B.4":
                this.fillAddons(cell, this.quoteData.getServices(), "No additional services.");
                break;

            default:
                break;
        }
    }

    private int fillAssembledProducts(int currentRow)
    {
        List<AssembledProductInQuote> assembledProducts = this.quoteData.getAssembledProducts();
        if (assembledProducts.isEmpty())
        {
            return currentRow;
        }

        int sequenceNumber = 1;
        for (AssembledProductInQuote product : assembledProducts)
        {
            currentRow = this.fillAssembledProductInfo(currentRow, sequenceNumber, product);
            currentRow++;
            sequenceNumber++;
        }
        return currentRow;
    }

    private int fillAssembledProductInfo(int startRow, int sequenceNumber, AssembledProductInQuote product)
    {
        int currentRow = startRow;

        this.createProductTitleRow(currentRow, "A." + String.valueOf(sequenceNumber), product.getTitle());
        currentRow = this.fillAssembledProductUnits(product, currentRow);

        currentRow++;
        String unitSequenceLetter = ALPHABET_SEQUENCE[(product.getUnits().size())];
        currentRow = this.fillAssembledProductAccessories(product.getAccessories(), currentRow, unitSequenceLetter);

        this.createCellWithData(this.quoteSheet.getRow(startRow + 1), AMOUNT_CELL, Cell.CELL_TYPE_NUMERIC, product.getAmountWithoutAddons());

        this.quoteSheet.addMergedRegion(new CellRangeAddress(startRow + 1, currentRow, RATE_CELL, RATE_CELL));
        this.quoteSheet.addMergedRegion(new CellRangeAddress(startRow + 1, currentRow, AMOUNT_CELL, AMOUNT_CELL));

        currentRow++;
        this.createRow(currentRow, this.quoteSheet);
        return currentRow;
    }

    private int fillAssembledProductAccessories(List<AssembledProductInQuote.Accessory> accessories, int currentRow, String unitSequenceLetter)
    {
        if (accessories == null || accessories.isEmpty())
        {
            return currentRow;
        }

        this.createSubHeadingRow(currentRow, unitSequenceLetter, "Accessories");

        int acSequence = 0;
        for (AssembledProductInQuote.Accessory accessory : accessories)
        {
            currentRow++;
            this.createRowAndFillData(currentRow, ROMAN_SEQUENCE[acSequence], accessory.title, accessory.quantity, null, null);
            acSequence++;
            if (acSequence == ROMAN_SEQUENCE.length) acSequence = 0;
        }
        return currentRow;
    }

    private int fillAssembledProductUnits(AssembledProductInQuote product, int currentRow)
    {
        int unitSequence = 0;
        for (AssembledProductInQuote.Unit unit : product.getUnits())
        {
            currentRow++;
            this.createSubHeadingRow(currentRow,"A."+ ALPHABET_SEQUENCE[unitSequence], unit.title + " - " + unit.getDimensions());

            currentRow++;
            this.createRowAndFillData(currentRow, null, "Unit consists of " + unit.moduleCount + " modules as per design provided.");

            unitSequence++;
            if (unitSequence == ALPHABET_SEQUENCE.length) unitSequence = 0;
        }
        return currentRow;
    }

    private int fillCatalogProducts(int currentRow)
    {
        List<ProductLineItem> catalogProducts = this.quoteData.getCatalogueProducts();
        if (catalogProducts.isEmpty())
        {
            return currentRow;
        }

        int sequenceNumber = this.quoteData.getCatalogStartSequence();
        for (ProductLineItem product : catalogProducts)
        {
            currentRow = this.fillCatalogProductInfo(currentRow, sequenceNumber, product);
            currentRow++;
            sequenceNumber++;
        }
        return currentRow;
    }

    private int fillCatalogProductInfo(int startRow, int sequenceNumber, ProductLineItem product)
    {
        int currentRow = startRow;

        this.createSubHeadingRowForCatalog(currentRow, "A." +String.valueOf(sequenceNumber), product.getTitle(), Double.valueOf(product.getQuantity()),
                product.getRate(), (double) Math.round(product.getAmount()));


    /*    currentRow++;
        this.createRowAndFillData(currentRow, "",product.getName() );*/

        currentRow++;
        this.createRowAndFillData(currentRow, null, product.getName());

        this.quoteSheet.addMergedRegion(new CellRangeAddress(startRow, currentRow, QUANTITY_CELL, QUANTITY_CELL));
        this.quoteSheet.addMergedRegion(new CellRangeAddress(startRow, currentRow, RATE_CELL, RATE_CELL));
        this.quoteSheet.addMergedRegion(new CellRangeAddress(startRow, currentRow, AMOUNT_CELL, AMOUNT_CELL));

        currentRow++;
        this.createRow(currentRow, this.quoteSheet);

        return currentRow;
    }

    private void fillAddons(Cell cell, List<ProductAddon> addOns, String emptyMessage)
    {
        int currentRow  = cell.getRow().getRowNum() + 1;
        if (addOns.isEmpty())
        {
            this.createRowWithMessage(currentRow, emptyMessage);
            return;
        }


        int index = 1;
        for (ProductAddon addon : addOns)
        {
            this.createRowAndFillData(currentRow, String.valueOf(index), addon.getTitle(), addon.getQuantity(), addon.getRate(), addon.getAmount());
            currentRow++;
            index++;
        }
        LOG.info("Addons :" + TITLE_CELL);
    }

    private void createRowWithMessage(int row, String message)
    {
        this.createRowAndFillData(row, null, message, null, null, null);
    }

    private void createRowAndFillData(int row, String index, String title)
    {
        this.createRowAndFillData(row, index, title, null, null, null);
    }

    private void createRowAndFillData(int rowNum, String index, String title, Double quantity, Double amount, Double total)
    {
        Row dataRow = this.createRow(rowNum, this.quoteSheet);
        this.createCellWithData(dataRow, INDEX_CELL, Cell.CELL_TYPE_STRING, index);
        this.createCellWithData(dataRow, TITLE_CELL, Cell.CELL_TYPE_STRING, title);
        this.createCellWithData(dataRow, QUANTITY_CELL, Cell.CELL_TYPE_NUMERIC, quantity);
        this.createCellWithData(dataRow, RATE_CELL, Cell.CELL_TYPE_NUMERIC, amount);
        this.createCellWithData(dataRow, AMOUNT_CELL, Cell.CELL_TYPE_NUMERIC, total);
    }

    private void createSubHeadingRowForCatalog(int rowNum, String index, String title,Double quantity, Double amount, Double total)
    {
        Row dataRow = this.createRow(rowNum, this.quoteSheet);
        this.createCellWithData(dataRow, INDEX_CELL, Cell.CELL_TYPE_STRING, index).setCellStyle(this.styles.getTitleStyle());
        this.createCellWithData(dataRow, TITLE_CELL, Cell.CELL_TYPE_STRING, title).setCellStyle(this.styles.getTitleStyle());
        this.createCellWithData(dataRow, QUANTITY_CELL, Cell.CELL_TYPE_NUMERIC, quantity);
        this.createCellWithData(dataRow, RATE_CELL, Cell.CELL_TYPE_NUMERIC, amount);
        this.createCellWithData(dataRow, AMOUNT_CELL, Cell.CELL_TYPE_NUMERIC, total);
    }


    private void createSubHeadingRow(int rowNum, String index, String title)
    {
        Row dataRow = this.createRow(rowNum, this.quoteSheet);
        this.createCellWithData(dataRow, INDEX_CELL, Cell.CELL_TYPE_STRING, index).setCellStyle(this.styles.getBoldStyle());
        this.createCellWithData(dataRow, TITLE_CELL, Cell.CELL_TYPE_STRING, title).setCellStyle(this.styles.getBoldStyle());
    }

    private void createProductTitleRow(int rowNum, String index, String title)
    {
        Row dataRow = this.createRow(rowNum, this.quoteSheet);
        dataRow.setHeight(new Double(dataRow.getHeight() * 1.5).shortValue());
        this.createCellWithData(dataRow, INDEX_CELL, Cell.CELL_TYPE_STRING, index).setCellStyle(this.styles.getTitleStyle());
        this.createCellWithData(dataRow, TITLE_CELL, Cell.CELL_TYPE_STRING, title).setCellStyle(this.styles.getTitleStyle());
        this.createCellWithData(dataRow, QUANTITY_CELL, Cell.CELL_TYPE_NUMERIC, null).setCellStyle(this.styles.getTitleStyle());
        this.createCellWithData(dataRow, RATE_CELL, Cell.CELL_TYPE_NUMERIC, null).setCellStyle(this.styles.getTitleStyle());
        this.createCellWithData(dataRow, AMOUNT_CELL, Cell.CELL_TYPE_NUMERIC, null).setCellStyle(this.styles.getTitleStyle());
    }

    private Row createRow(int currentRow, Sheet sheet)
    {
        sheet.shiftRows(currentRow, sheet.getLastRowNum(), 1);
        return sheet.createRow(currentRow);
    }

    private Row deleteRow(int rownnum, Sheet sheet)
    {
        Row dataRow=this.deleteRow(rownnum,this.quoteSheet);
        sheet.removeRow(dataRow);
        return dataRow;
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


    private void replaceCellValue(Cell cell, String cellValue)
    {
        String fieldName = cellValue.substring(1);
        Object value = this.quoteData.getValue(fieldName);

        if (value == null) value = "";
        if (value instanceof String)
        {
            cell.setCellValue((String) value);
        }
        else if (value instanceof Double)
        {
            cell.setCellValue((Double) value);
        }
        else
        {
            cell.setCellValue(value.toString());
        }

        if (fieldName.equals("discountamount") && this.quoteData.getDiscountAmount() == 0 )
        {
            quoteSheet.removeRow(cell.getRow());

        }
        if (fieldName.equals("amountafterdiscount") && this.quoteData.getAmountafterdiscount()==this.quoteData.getTotalCost())
        {
            Row newRow=cell.getRow();
            quoteSheet.removeRow(newRow);
        }


    }

}
