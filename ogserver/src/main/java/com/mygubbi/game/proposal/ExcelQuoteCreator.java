package com.mygubbi.game.proposal;

import com.mygubbi.common.StringUtils;
import com.mygubbi.game.proposal.model.AssembledProductInQuote;
import com.mygubbi.game.proposal.model.QuoteData;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by Sunil on 22-05-2016.
 */
public class ExcelQuoteCreator
{
    private static final int TITLE_CELL = 1;
    private static final int INDEX_CELL = 0;
    private static final int QUANTITY_CELL = 2;
    private static final int RATE_CELL = 3;
    private static final int AMOUNT_CELL = 4;

    private static final String[] ALPHABET_SEQUENCE = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j"};
    private static final String[] ROMAN_SEQUENCE = new String[]{"i", "ii", "iii", "iv", "v", "vi", "vii", "viii", "ix", "x", "xi", "xii", "xiii", "xiv", "xv"};

    private String quoteXls;
    private QuoteData quoteData;

    private Sheet quoteSheet;
    private Sheet dataSheet;
    private Workbook wb;

    public ExcelQuoteCreator(String quoteXls, QuoteData quoteData)
    {
        this.quoteXls = quoteXls;
        this.quoteData = quoteData;
    }

    public void prepareQuote()
    {
        this.openWorkbook();

        this.processQuoteSheet();

        this.closeWorkbook(wb);
    }

    private void processQuoteSheet()
    {
        int firstRow = quoteSheet.getFirstRowNum();
        int lastRow = quoteSheet.getLastRowNum();

        for (int rowNum = lastRow; rowNum >= firstRow; rowNum--)
        {
            Row row = quoteSheet.getRow(rowNum);
            int lastCell = row.getLastCellNum();
            for (short cellNum = row.getFirstCellNum(); cellNum < lastCell; cellNum++)
            {
                try
                {
                    Cell cell = row.getCell(cellNum);
                    if (cell.getCellType() == Cell.CELL_TYPE_STRING)
                    {
                        String cellValue = cell.getStringCellValue();
                        this.handleCell(cell, cellValue);
                    }
                }
                catch (Exception e)
                {
                    throw new RuntimeException("Error reading cell (" + rowNum + "," + cellNum + ") in sheet ." + quoteSheet.getSheetName() + ". Error:" + e.getMessage(), e);
                }
            }
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
        switch (cellValue)
        {
            case "A.1":
                this.fillAssembledProducts(cell);
                break;

            case "A.2":
                this.fillCatalogProducts(cell);
                break;

            case "B.1":
                this.fillAddons(cell, this.quoteData.getAccessories(), "No additional accessories.");
                break;

            case "B.2":
                this.fillAddons(cell, this.quoteData.getAppliances(), "No additional appliances.");
                break;

            case "B.3":
                this.fillAddons(cell, this.quoteData.getServices(), "No additional services.");
                break;

            default:
                break;
        }
    }

    private void fillAssembledProducts(Cell cell)
    {
        int startRow  = cell.getRow().getRowNum() + 1;
        List<AssembledProductInQuote> assembledProducts = this.quoteData.getAssembledProducts();
        if (assembledProducts.isEmpty())
        {
            Row dataRow = this.quoteSheet.createRow(startRow);
            dataRow.getCell(TITLE_CELL).setCellValue("No assembled products.");
            return;
        }

        int sequenceNumber = 1;
        for (AssembledProductInQuote product : assembledProducts)
        {
            startRow = this.fillAssembledProductInfo(startRow, sequenceNumber, product);
            startRow++;
            sequenceNumber++;
        }

    }

    private int fillAssembledProductInfo(int startRow, int sequenceNumber, AssembledProductInQuote product)
    {
        int currentRow = startRow;

        this.fillAssembledProductSummary(sequenceNumber, product, currentRow);
        currentRow = this.fillAssembledProductUnits(product, currentRow);

        currentRow++;
        String unitSequenceLetter = ALPHABET_SEQUENCE[(product.getUnits().size() + 1)];
        currentRow = this.fillAssembledProductAccessories(product.getAccessories(), currentRow, unitSequenceLetter);

        currentRow++;
        Row dataRow = this.quoteSheet.createRow(currentRow);

        this.quoteSheet.addMergedRegion(new CellRangeAddress(startRow, currentRow, RATE_CELL, RATE_CELL));
        this.quoteSheet.addMergedRegion(new CellRangeAddress(startRow, currentRow, AMOUNT_CELL, AMOUNT_CELL));

        return currentRow;
    }

    private int fillAssembledProductAccessories(List<AssembledProductInQuote.Accessory> accessories, int currentRow, String unitSequenceLetter)
    {
        if (accessories == null || accessories.isEmpty())
        {
            return currentRow;
        }

        Row dataRow = this.quoteSheet.createRow(currentRow);
        dataRow.getCell(INDEX_CELL).setCellValue(unitSequenceLetter);
        dataRow.getCell(TITLE_CELL).setCellValue("Accessories");

        int acSequence = 0;
        for (AssembledProductInQuote.Accessory accessory : accessories)
        {
            currentRow++;
            dataRow = this.quoteSheet.createRow(currentRow);
            dataRow.getCell(INDEX_CELL).setCellValue(ROMAN_SEQUENCE[acSequence]);
            dataRow.getCell(TITLE_CELL).setCellValue(accessory.title);
            dataRow.getCell(QUANTITY_CELL).setCellValue(accessory.quantity);
            acSequence++;
            if (acSequence == ROMAN_SEQUENCE.length) acSequence = 0;
        }
        return currentRow;
    }

    private int fillAssembledProductUnits(AssembledProductInQuote product, int currentRow)
    {
        Row dataRow;
        int unitSequence = 0;
        for (AssembledProductInQuote.Unit unit : product.getUnits())
        {
            currentRow++;
            dataRow = this.quoteSheet.createRow(currentRow);
            dataRow.getCell(INDEX_CELL).setCellValue(ALPHABET_SEQUENCE[unitSequence]);
            dataRow.getCell(TITLE_CELL).setCellValue(unit.title + " - " + unit.getDimensions());

            currentRow++;
            dataRow = this.quoteSheet.createRow(currentRow);
            dataRow.getCell(TITLE_CELL).setCellValue("Unit consists of " + unit.moduleCount + " modules as per design provided.");

            unitSequence++;
            if (unitSequence == ALPHABET_SEQUENCE.length) unitSequence = 0;
        }
        return currentRow;
    }

    private void fillAssembledProductSummary(int sequenceNumber, AssembledProductInQuote product, int currentRow)
    {
        Row dataRow = this.quoteSheet.createRow(currentRow);
        dataRow.getCell(INDEX_CELL).setCellValue(sequenceNumber);
        dataRow.getCell(TITLE_CELL).setCellValue(product.getTitle());
        dataRow.getCell(AMOUNT_CELL).setCellValue(product.getAmount());
    }

    private void fillCatalogProducts(Cell cell)
    {
        int startRow  = cell.getRow().getRowNum() + 1;
        List<ProductLineItem> catalogProducts = this.quoteData.getCatalogueProducts();
        if (catalogProducts.isEmpty())
        {
            Row dataRow = this.quoteSheet.createRow(startRow);
            dataRow.getCell(TITLE_CELL).setCellValue("No products from catalogue.");
            return;
        }

        int sequenceNumber = this.quoteData.getCatalogStartSequence();
        for (ProductLineItem product : catalogProducts)
        {
            startRow = this.fillCatalogProductInfo(startRow, sequenceNumber, product);
            startRow++;
            sequenceNumber++;
        }
    }

    private int fillCatalogProductInfo(int startRow, int sequenceNumber, ProductLineItem product)
    {
        int currentRow = startRow;

        Row dataRow = this.quoteSheet.createRow(currentRow);
        dataRow.getCell(INDEX_CELL).setCellValue(sequenceNumber);
        dataRow.getCell(TITLE_CELL).setCellValue(product.getTitle());
        dataRow.getCell(QUANTITY_CELL).setCellValue(product.getQuantity());
        dataRow.getCell(RATE_CELL).setCellValue(product.getRate());
        dataRow.getCell(AMOUNT_CELL).setCellValue(product.getAmount());

        currentRow++;
        dataRow = this.quoteSheet.createRow(currentRow);
        dataRow.getCell(INDEX_CELL).setCellValue("a");
        dataRow.getCell(TITLE_CELL).setCellValue("OVERALL DIMENSION - " + product.getDimension());

        currentRow++;
        dataRow = this.quoteSheet.createRow(currentRow);
        dataRow.getCell(TITLE_CELL).setCellValue(product.getName());

        currentRow++;
        dataRow = this.quoteSheet.createRow(currentRow);

        this.quoteSheet.addMergedRegion(new CellRangeAddress(startRow, currentRow, QUANTITY_CELL, QUANTITY_CELL));
        this.quoteSheet.addMergedRegion(new CellRangeAddress(startRow, currentRow, RATE_CELL, RATE_CELL));
        this.quoteSheet.addMergedRegion(new CellRangeAddress(startRow, currentRow, AMOUNT_CELL, AMOUNT_CELL));

        return currentRow;
    }

    private void fillAddons(Cell cell, List<ProductAddon> addOns, String emptyMessage)
    {
        int startRow  = cell.getRow().getRowNum() + 1;
        if (addOns.isEmpty())
        {
            Row dataRow = this.quoteSheet.createRow(startRow);
            dataRow.getCell(TITLE_CELL).setCellValue(emptyMessage);
            return;
        }

        int index = 1;
        for (ProductAddon addon : addOns)
        {
            Row dataRow = this.quoteSheet.createRow(startRow);
            dataRow.getCell(INDEX_CELL).setCellValue(index);
            dataRow.getCell(TITLE_CELL).setCellValue(addon.getTitle());
            dataRow.getCell(QUANTITY_CELL).setCellValue(addon.getQuantity());
            dataRow.getCell(RATE_CELL).setCellValue(addon.getRate());
            dataRow.getCell(AMOUNT_CELL).setCellValue(addon.getAmount());
            startRow++;
            index++;
        }
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
    }

    private Workbook getWorkbook(String quoteXls)
    {
        Workbook wb = null;
        try
        {
            wb = new XSSFWorkbook(new BufferedInputStream(getClass().getResourceAsStream(quoteXls)));
        }
        catch (IOException e)
        {
            throw new RuntimeException(quoteXls + " workbook is not available", e);
        }
        return wb;
    }

    private void openWorkbook()
    {
        this.wb = this.getWorkbook(quoteXls);
        this.quoteSheet = wb.getSheet("Quote");
        if (quoteSheet == null) throw new RuntimeException("Quote sheet not found.");
        this.dataSheet = wb.getSheet("Data");
        if (dataSheet == null) throw new RuntimeException("Data sheet not found.");
    }

    private void closeWorkbook(Workbook wb)
    {
        try
        {
            wb.close();
        }
        catch (IOException e)
        {
            //Ignore
        }
    }

}
