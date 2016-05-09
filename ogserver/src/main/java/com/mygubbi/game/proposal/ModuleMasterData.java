package com.mygubbi.game.proposal;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by test on 06-05-2016.
 */
public class ModuleMasterData
{
    private static final int QUOTATION_SHEET = 1;
    private static final String MASTER_SHEET = "Master";

    private static final CellReference MATERIAL_CELL = new CellReference("H5");
    private static final CellReference FINISH_CELL = new CellReference("H7");
    private static final CellReference TU001_PRICE_CELL = new CellReference("H13");

    private static final CellReference INPUT_CELL = new CellReference("J5");
    private static final CellReference MIDDLE_CELL = new CellReference("J6");
    private static final CellReference RESULT_CELL = new CellReference("J7");

    private static final CellReference MASTER_CARCASS = new CellReference("G5");
    private static final CellReference MASTER_CARCASS_CODE = new CellReference("G6");
    private static final CellReference MASTER_TU001 = new CellReference("B533");

    private String filename;

    public ModuleMasterData(String filename)
    {
        this.filename = filename;
    }

    public void price()
    {
        Workbook wb = null;
        try
        {
            wb = new XSSFWorkbook(new BufferedInputStream(getClass().getResourceAsStream(this.filename)));
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return;
        }

        List<ProductModule> productModules = new ArrayList<>();

        Sheet quoteSheet = wb.getSheetAt(QUOTATION_SHEET);
        Sheet masterSheet = wb.getSheet(MASTER_SHEET);
        System.out.println("Quote Sheet - " + quoteSheet.getSheetName());
        System.out.println("Master Sheet - " + masterSheet.getSheetName());

        FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

        this.printPrice(quoteSheet);

        this.changeMaterialAndPrint(quoteSheet, masterSheet, evaluator, "MDF");
        this.changeMaterialAndPrint(quoteSheet, masterSheet, evaluator, "MDF with PU");
        this.changeMaterialAndPrint(quoteSheet, masterSheet, evaluator, "PLY");

//        this.changeMaterialAndPrint(quoteSheet, evaluator, "MDF with PU");
//        this.changeMaterialAndPrint(quoteSheet, evaluator, "PLY");

//        this.printNumbers(quoteSheet);
//        this.changeInputAndPrint(quoteSheet, evaluator, 15);

        try
        {
            wb.close();
        }
        catch (IOException e)
        {
            //Ignore
        }
    }

    private void changeInputAndPrint(Sheet sheet, FormulaEvaluator evaluator, int value)
    {
        sheet.getRow(INPUT_CELL.getRow()).getCell(INPUT_CELL.getCol()).setCellValue(value);
        System.out.println("----------------------------------------------" );
        System.out.println("Evaluating for " + value);
        evaluateCell(evaluator, sheet, MIDDLE_CELL);
        evaluateCell(evaluator, sheet, RESULT_CELL);
        this.printNumbers(sheet);
    }

    private void changeMaterialAndPrint(Sheet quoteSheet, Sheet masterSheet, FormulaEvaluator evaluator, String material)
    {
        long startTime = System.currentTimeMillis();
        evaluator.clearAllCachedResultValues();
        quoteSheet.getRow(MATERIAL_CELL.getRow()).getCell(MATERIAL_CELL.getCol()).setCellValue(material);
        System.out.println("----------------------------------------------" );
        System.out.println("Evaluating for material " + material);

        this.evaluateCell(evaluator, masterSheet, MASTER_CARCASS);
        this.evaluateCell(evaluator, masterSheet, MASTER_CARCASS_CODE);
        this.evaluateRow(evaluator, masterSheet, MASTER_TU001);
        this.evaluateCell(evaluator, quoteSheet, TU001_PRICE_CELL);
        this.printPrice(quoteSheet);
        System.out.println("Calculated price in (ms)" + (System.currentTimeMillis() - startTime));
    }

    private void evaluateCell(FormulaEvaluator evaluator, Sheet sheet, CellReference cellRef)
    {
        Cell cell = sheet.getRow(cellRef.getRow()).getCell(cellRef.getCol());
        evaluator.evaluateFormulaCell(cell);
/*        String value = null;
        try
        {
            value = cell.getStringCellValue();
        }
        catch (Exception e)
        {
            try
            {
                value = String.valueOf(cell.getNumericCellValue());
            }
            catch (Exception e1)
            {
                value = "NA";
            }
        }
        System.out.println("Evaluated cell " + cellRef.formatAsString() + " in sheet " + sheet.getSheetName() + ". Value:" + value);
*/
    }

    private void evaluateRow(FormulaEvaluator evaluator, Sheet sheet, CellReference cellRef)
    {
        Row row = sheet.getRow(cellRef.getRow());
        short lastCellNum = row.getLastCellNum();
//        System.out.println("Evaluating " + lastCellNum + " columns in row : " + row.getCell(cellRef.getCol()).getStringCellValue());
        for (int colNum = row.getFirstCellNum(); colNum < lastCellNum; colNum++)
        {
            evaluator.evaluateFormulaCell(row.getCell(cellRef.getCol()));
        }
    }

    private void printPrice(Sheet sheet)
    {
        System.out.println("Material: " + sheet.getRow(MATERIAL_CELL.getRow()).getCell(MATERIAL_CELL.getCol()).getStringCellValue());
        System.out.println("Finish: " + sheet.getRow(FINISH_CELL.getRow()).getCell(FINISH_CELL.getCol()).getStringCellValue());
        System.out.println("TU001 Price: " + sheet.getRow(TU001_PRICE_CELL.getRow()).getCell(TU001_PRICE_CELL.getCol()).getNumericCellValue());
    }

    private void printNumbers(Sheet sheet)
    {
        System.out.println("Input: " + sheet.getRow(INPUT_CELL.getRow()).getCell(INPUT_CELL.getCol()).getNumericCellValue());
        System.out.println("Middle: " + sheet.getRow(MIDDLE_CELL.getRow()).getCell(MIDDLE_CELL.getCol()).getNumericCellValue());
        System.out.println("Result: " + sheet.getRow(RESULT_CELL.getRow()).getCell(RESULT_CELL.getCol()).getNumericCellValue());
    }

    public static void main(String[] args)
    {
        new ModuleMasterData("/testdata/modulemaster.xlsx").price();
    }
}
