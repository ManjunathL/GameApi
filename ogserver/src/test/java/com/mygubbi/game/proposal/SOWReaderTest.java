package com.mygubbi.game.proposal;

import com.mygubbi.common.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
 * Created by User on 04-07-2017.
 */
public class SOWReaderTest {

    private final static Logger LOG = LogManager.getLogger(ModuleExcelFileReader.class);
    public static final int S1L1 = 3;
    public static final int S2L1 = 5;
    public static final int S2L2 = 7;
    public static final int S2L3 = 9;
    public static final int S2L4 = 11;
    public static final int S2L5 = 13;
    public static final int S2L6 = 15;

    private String filename;

    public SOWReaderTest(String filename)
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
            if (!sheetName.equals("sow"))
            {
                continue;
            }
            System.out.println("Successfully read the sheet");
            boolean startReading = false;
            String unit = null;
            for (Row row : sheet)
            {
                if ("Kitchen".equals(row.getCell(0).getStringCellValue()))
                {
                    String rowString = row.toString();
                    System.out.println("Row string :" + row.toString());

                }

                /*if (!startReading)
                {
                    if ("Kitchen".equals(row.getCell(0).getStringCellValue()))
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
*/

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

    public static void main(String[] args)
    {
       new SOWReaderTest("D:\\Mygubbi GAME\\sow.xlsx").loadModules();
    }

}
