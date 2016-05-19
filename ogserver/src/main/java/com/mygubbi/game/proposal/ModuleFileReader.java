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

                String name = row.getCell(3).getStringCellValue();
                int width = this.getInteger(row, 5);
                int depth = this.getInteger(row, 6);
                int height = this.getInteger(row, 7);

                String finish = row.getCell(8).getStringCellValue();
                String color = this.getColor(row);
                int quantity = this.getInteger(row, 16);
                String uom = row.getCell(17).getStringCellValue();
                String remarks = row.getCell(20).getStringCellValue();

                ProductModule module = new ProductModule().setUnit(unit).setName(name).setKDMCode(moduleCode)
                        .setSequence(sequence)
                        .setColorCode(color)
                        .setQuantity(quantity).setUom(uom).setRemarks(remarks)
                        .setWidth(width).setDepth(depth).setHeight(height);
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
        Cell colorCell = row.getCell(9);
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
