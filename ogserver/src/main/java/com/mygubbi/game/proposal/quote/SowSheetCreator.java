package com.mygubbi.game.proposal.quote;

import com.mygubbi.game.proposal.ModuleDataService;
import com.mygubbi.game.proposal.ProductAddon;
import com.mygubbi.game.proposal.ProductLineItem;
import com.mygubbi.game.proposal.model.SOWMaster;
import com.mygubbi.si.excel.ExcelCellProcessor;
import com.mygubbi.si.excel.ExcelSheetProcessor;
import com.mygubbi.si.excel.ExcelStyles;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Created by user on 27-May-17.
 */
public class SowSheetCreator implements ExcelCellProcessor
{
    private final static Logger LOG = LogManager.getLogger(SowSheetCreator.class);

    private static final int SPACE_TYPE_CELL = 0;
    private static final int ROOM_CELL = 1;
    private static final int PRODUCT_CELL = 2;
    private static final int L1S01_TITLE_CELL = 3;
    private static final int L1S01_OPTION_CELL = 4;
    private static final int L2S01_TITLE_CELL = 5;
    private static final int L2S01_OPTION_CELL = 6;
    private static final int L2S02_TITLE_CELL = 7;
    private static final int L2S02_OPTION_CELL = 8;
    private static final int L2S03_TITLE_CELL = 9;
    private static final int L2S03_OPTION_CELL = 10;
    private static final int L2S04_TITLE_CELL = 11;
    private static final int L2S04_OPTION_CELL = 12;
    private static final int L2S05_TITLE_CELL = 13;
    private static final int L2S05_OPTION_CELL = 14;
    private static final int L2S06_TITLE_CELL = 15;
    private static final int L2S06_OPTION_CELL = 16;

    private static final String[] ALPHABET_SEQUENCE = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j"};
    private static final String[] ROMAN_SEQUENCE = new String[]{"i", "ii", "iii", "iv", "v", "vi", "vii", "viii", "ix", "x", "xi", "xii", "xiii", "xiv", "xv"};

    private QuoteData quoteData;
    private XSSFSheet quoteSheet;
    private ExcelStyles styles;
    private ExcelSheetProcessor sheetProcessor;
    public SowSheetCreator(XSSFSheet quoteSheet, QuoteData quoteData, ExcelStyles styles)
    {
        this.quoteSheet = quoteSheet;
        this.quoteData = quoteData;
        this.styles = styles;
    }

    public void prepare()
    {
        this.sheetProcessor = new ExcelSheetProcessor(this.quoteSheet, this.styles, this);
        this.sheetProcessor.process();
    }


    @Override
    public Object getValueForKey(String key)
    {
       return null;
    }

    @Override
    public void processCell(Cell cell, String cellValue)
    {
        switch (cellValue)
        {
            case "Space Type":
                //int currentRow = this.fillAssembledProducts(cell.getRow().getRowNum());
                this.fillServicesBasedOnSpaceType(cell.getRow().getRowNum() + 1);
                break;


            default:
                break;
        }
    }

    public int fillServicesBasedOnSpaceType(int currentRow)
    {
        List<AssembledProductInQuote> assembledProducts = this.quoteData.getAssembledProducts();
        if (assembledProducts.isEmpty())
        {
            return currentRow;
        }
        int sequenceNumber = 1;
        for (AssembledProductInQuote productInQuote : assembledProducts)
        {
            currentRow = this.fillServicesInfo(currentRow, productInQuote);
            sequenceNumber++;

        }
        return currentRow;

    }




    private int fillServicesInfo(int startRow, AssembledProductInQuote productInQuote)
    {
        int currentRow = startRow;
        int noOfROws = 0;

        Collection<SOWMaster> sowMasterList = ModuleDataService.getInstance().getSOWMaster(productInQuote.getSpaceType());
        noOfROws = sowMasterList.size();

        this.createSpaceTypeHeadingRow(currentRow, productInQuote.getSpaceType(),productInQuote.getRoom(), productInQuote.getTitle(),sowMasterList.toArray()[0]);


        for (int i = 1; i <noOfROws ; i++)
        {
            this.createSpaceTypeDataRow(currentRow, productInQuote.getSpaceType(),productInQuote.getRoom(), productInQuote.getTitle(),sowMasterList.toArray()[i]);
        }

        currentRow++;

        return currentRow;
    }


    private void createSpaceTypeHeadingRow(int rowNum, String spaceType, String room, String product, Object sowMaster )
    {
        Row dataRow = this.createRow(rowNum, this.quoteSheet);
        SOWMaster sowMaster1 = (SOWMaster) sowMaster;

//        dataRow.setHeight(new Double(dataRow.getHeight() * 1.5).shortValue());
        this.createCellWithData(dataRow, SPACE_TYPE_CELL, Cell.CELL_TYPE_STRING, spaceType).setCellStyle(this.styles.getBoldStyle());
        this.createCellWithData(dataRow, ROOM_CELL, Cell.CELL_TYPE_STRING, room).setCellStyle(this.styles.getBoldStyle());
        this.createCellWithData(dataRow, PRODUCT_CELL, Cell.CELL_TYPE_STRING, product).setCellStyle(this.styles.getBoldStyle());

        this.createCellWithData(dataRow, L1S01_TITLE_CELL, Cell.CELL_TYPE_STRING,sowMaster1.getL1S01() ).setCellStyle(this.styles.getTitleStyle());
        this.createCellWithDropDownForL1(dataRow, L1S01_OPTION_CELL, Cell.CELL_TYPE_BLANK,rowNum).setCellStyle(this.styles.getTitleStyle());
        this.createCellWithData(dataRow, L2S01_TITLE_CELL, Cell.CELL_TYPE_STRING, sowMaster1.getL2S01()).setCellStyle(this.styles.getTitleStyle());
        this.createCellWithDropDownForL2(dataRow, L2S01_OPTION_CELL, Cell.CELL_TYPE_BLANK, rowNum, L2S01_OPTION_CELL).setCellStyle(this.styles.getTitleStyle());
        this.createCellWithData(dataRow, L2S02_TITLE_CELL, Cell.CELL_TYPE_STRING, sowMaster1.getL2S02()).setCellStyle(this.styles.getTitleStyle());
        this.createCellWithDropDownForL2(dataRow, L2S02_OPTION_CELL, Cell.CELL_TYPE_BLANK, rowNum, L2S02_OPTION_CELL).setCellStyle(this.styles.getTitleStyle());
        this.createCellWithData(dataRow, L2S03_TITLE_CELL, Cell.CELL_TYPE_STRING, sowMaster1.getL2S03()).setCellStyle(this.styles.getTitleStyle());
        this.createCellWithDropDownForL2(dataRow, L2S03_OPTION_CELL, Cell.CELL_TYPE_BLANK, rowNum, L2S03_OPTION_CELL).setCellStyle(this.styles.getTitleStyle());
        this.createCellWithData(dataRow, L2S04_TITLE_CELL, Cell.CELL_TYPE_STRING, sowMaster1.getL2S04()).setCellStyle(this.styles.getTitleStyle());
        this.createCellWithDropDownForL2(dataRow, L2S04_OPTION_CELL, Cell.CELL_TYPE_BLANK, rowNum, L2S04_OPTION_CELL).setCellStyle(this.styles.getTitleStyle());
        this.createCellWithData(dataRow, L2S05_TITLE_CELL, Cell.CELL_TYPE_STRING, sowMaster1.getL2S05()).setCellStyle(this.styles.getTitleStyle());
        this.createCellWithDropDownForL2(dataRow, L2S05_OPTION_CELL, Cell.CELL_TYPE_BLANK, rowNum, L2S05_OPTION_CELL).setCellStyle(this.styles.getTitleStyle());
        this.createCellWithData(dataRow, L2S06_TITLE_CELL, Cell.CELL_TYPE_STRING, sowMaster1.getL2S06()).setCellStyle(this.styles.getTitleStyle());
        this.createCellWithDropDownForL2(dataRow, L2S06_OPTION_CELL, Cell.CELL_TYPE_BLANK, rowNum, L2S06_OPTION_CELL).setCellStyle(this.styles.getTitleStyle());
    }

    private void createSpaceTypeDataRow(int rowNum, String spaceType, String room, String product, Object sowMaster )
    {
        Row dataRow = this.createRow(rowNum, this.quoteSheet);
        SOWMaster sowMaster1 = (SOWMaster) sowMaster;

//        dataRow.setHeight(new Double(dataRow.getHeight() * 1.5).shortValue());
        this.createCellWithData(dataRow, SPACE_TYPE_CELL, Cell.CELL_TYPE_STRING, "").setCellStyle(this.styles.getBoldStyle());
        this.createCellWithData(dataRow, ROOM_CELL, Cell.CELL_TYPE_STRING, "").setCellStyle(this.styles.getBoldStyle());
        this.createCellWithData(dataRow, PRODUCT_CELL, Cell.CELL_TYPE_STRING, "").setCellStyle(this.styles.getBoldStyle());

        this.createCellWithData(dataRow, L1S01_TITLE_CELL, Cell.CELL_TYPE_STRING,sowMaster1.getL1S01() ).setCellStyle(this.styles.getTitleStyle());
        this.createCellWithDropDownForL1(dataRow, L1S01_OPTION_CELL, Cell.CELL_TYPE_BLANK,rowNum).setCellStyle(this.styles.getTitleStyle());
        this.createCellWithData(dataRow, L2S01_TITLE_CELL, Cell.CELL_TYPE_STRING, sowMaster1.getL2S01()).setCellStyle(this.styles.getTitleStyle());
        this.createCellWithDropDownForL2(dataRow, L2S01_OPTION_CELL, Cell.CELL_TYPE_BLANK, rowNum, L2S01_OPTION_CELL).setCellStyle(this.styles.getTitleStyle());
        this.createCellWithData(dataRow, L2S02_TITLE_CELL, Cell.CELL_TYPE_STRING, sowMaster1.getL2S02()).setCellStyle(this.styles.getTitleStyle());
        this.createCellWithDropDownForL2(dataRow, L2S02_OPTION_CELL, Cell.CELL_TYPE_BLANK, rowNum, L2S02_OPTION_CELL).setCellStyle(this.styles.getTitleStyle());
        this.createCellWithData(dataRow, L2S03_TITLE_CELL, Cell.CELL_TYPE_STRING, sowMaster1.getL2S03()).setCellStyle(this.styles.getTitleStyle());
        this.createCellWithDropDownForL2(dataRow, L2S03_OPTION_CELL, Cell.CELL_TYPE_BLANK, rowNum, L2S03_OPTION_CELL).setCellStyle(this.styles.getTitleStyle());
        this.createCellWithData(dataRow, L2S04_TITLE_CELL, Cell.CELL_TYPE_STRING, sowMaster1.getL2S04()).setCellStyle(this.styles.getTitleStyle());
        this.createCellWithDropDownForL2(dataRow, L2S04_OPTION_CELL, Cell.CELL_TYPE_BLANK, rowNum, L2S04_OPTION_CELL).setCellStyle(this.styles.getTitleStyle());
        this.createCellWithData(dataRow, L2S05_TITLE_CELL, Cell.CELL_TYPE_STRING, sowMaster1.getL2S05()).setCellStyle(this.styles.getTitleStyle());
        this.createCellWithDropDownForL2(dataRow, L2S05_OPTION_CELL, Cell.CELL_TYPE_BLANK, rowNum, L2S05_OPTION_CELL).setCellStyle(this.styles.getTitleStyle());
        this.createCellWithData(dataRow, L2S06_TITLE_CELL, Cell.CELL_TYPE_STRING, sowMaster1.getL2S06()).setCellStyle(this.styles.getTitleStyle());
        this.createCellWithDropDownForL2(dataRow, L2S06_OPTION_CELL, Cell.CELL_TYPE_BLANK, rowNum, L2S06_OPTION_CELL).setCellStyle(this.styles.getTitleStyle());
    }



    private Row createRow(int currentRow, Sheet sheet)
    {
        sheet.shiftRows(currentRow, sheet.getLastRowNum(), 1);
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


    private Cell createCellWithDropDownForL1(Row dataRow, int cellNum, int cellType, int rownum)
    {
        Cell cell = dataRow.createCell(cellNum);

        XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(quoteSheet);
        XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint)
                dvHelper.createExplicitListConstraint(new String[]{"Yes", "No"});
        CellRangeAddressList addressList = new CellRangeAddressList(rownum, rownum, 4, 4);
        XSSFDataValidation validation = (XSSFDataValidation) dvHelper.createValidation(
                dvConstraint, addressList);
        validation.setShowErrorBox(true);
        quoteSheet.addValidationData(validation);

        return cell;
    }

    private Cell createCellWithDropDownForL2(Row dataRow, int cellNum, int cellType, int rownum, int colnum)
    {
        Cell cell = dataRow.createCell(cellNum);

        XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper((XSSFSheet) quoteSheet);
        XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint)
                dvHelper.createExplicitListConstraint(new String[]{"Mygubbi", "Client", "NA"});
        CellRangeAddressList addressList = new CellRangeAddressList(rownum, rownum, colnum, colnum);
        XSSFDataValidation validation = (XSSFDataValidation)dvHelper.createValidation(
                dvConstraint, addressList);
        validation.setShowErrorBox(true);
        quoteSheet.addValidationData(validation);

        return cell;
    }

}
