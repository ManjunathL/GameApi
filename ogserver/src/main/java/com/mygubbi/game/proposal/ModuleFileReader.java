package com.mygubbi.game.proposal;

import com.mygubbi.common.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Sunil on 27-04-2016.
 */

public class ModuleFileReader
{
    private final static Logger LOG = LogManager.getLogger(ModuleFileReader.class);
    public static final int NAME_CELL = 3;
    public static final int WIDTH_CELL = 5;
    public static final int DEPTH_CELL = 6;
    public static final int HEIGHT_CELL = 7;
    public static final int COLOR_CELL = 8;
    public static final int REMARKS_CELL = 20;

    private String filename;

    public ModuleFileReader(String filename)
    {
        this.filename = filename;
    }

    public List<ProductModule> loadModules()
    {
        LOG.info("Loading file " + this.filename);
        Workbook wb = null;
        try
        {
            wb = new XSSFWorkbook(new BufferedInputStream(new FileInputStream(this.filename)));
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        }

        List<ProductModule> productModules = new ArrayList<>();

        for (int i = 0; i < wb.getNumberOfSheets(); i++)
        {
            Sheet sheet = wb.getSheetAt(i);
            String sheetName = wb.getSheetName(i);
            System.out.println(sheetName);
            if (!sheetName.equals("Order"))
            {
                continue;
            }
            boolean startReading = false;
            String unit = null;
            for (Row row : sheet)
            {
                if (!startReading)
                {
                    if ("Class".equals(row.getCell(0).getStringCellValue()))
                    {
                        startReading = true;
                    }
                    continue;
                }
                String firstCell = row.getCell(0).getStringCellValue();
                if ("Total".equals(firstCell))
                {
                    break;
                }
                if (StringUtils.isNonEmpty(firstCell)) unit = firstCell;
                String moduleCode = row.getCell(4).getStringCellValue();
                if (StringUtils.isEmpty(moduleCode)) continue;

                int sequence = this.getInteger(row, 1);

                String name = row.getCell(NAME_CELL).getStringCellValue();
                int width = this.getInteger(row, WIDTH_CELL);
                int depth = this.getInteger(row, DEPTH_CELL);
                int height = this.getInteger(row, HEIGHT_CELL);

                String color = this.getColor(row);
                String remarks = row.getCell(REMARKS_CELL).getStringCellValue();

                ProductModule module = new ProductModule()
                        .setUnit(unit)
                        .setKDMCode(moduleCode)
                        .setSequence(sequence)
                        .setColorCode(color)
                        .setRemarks(remarks);
                productModules.add(module);
            }
        }
        try
        {
            wb.close();
        }
        catch (IOException e)
        {
            //Ignore
        }

        for (ProductModule module : productModules)
        {
            LOG.info(module);
        }
        return productModules;
    }

    private int getInteger(Row row, int index)
    {
        return new Double(row.getCell(index).getNumericCellValue()).intValue();
    }

    private String getColor(Row row)
    {
        Cell colorCell = row.getCell(COLOR_CELL);
        String color = null;
        if (colorCell.getCellType() == Cell.CELL_TYPE_STRING)
        {
            color = colorCell.getStringCellValue();
        }
        else if (colorCell.getCellType() == Cell.CELL_TYPE_NUMERIC)
        {
            color = String.valueOf(new Double(colorCell.getNumericCellValue()).intValue());
        }
        return color;
    }

    public static void main(String[] args)
    {
        new ModuleFileReader("/testdata/Kitchen001-Quote.xlsx").loadModules();
        new ModuleFileReader("/testdata/Kitchen002-Quote.xlsx").loadModules();
        new ModuleFileReader("/testdata/Kitchen003-Quote.xlsx").loadModules();
        new ModuleFileReader("/testdata/Kitchen004-Quote.xlsx").loadModules();
    }
}
