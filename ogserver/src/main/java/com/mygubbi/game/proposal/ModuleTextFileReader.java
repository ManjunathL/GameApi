package com.mygubbi.game.proposal;

import com.mygubbi.common.StringUtils;
import com.opencsv.CSVReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Sunil on 27-04-2016.
 */

public class ModuleTextFileReader
{
    private final static Logger LOG = LogManager.getLogger(ModuleTextFileReader.class);
    public static final int SEQ_CELL = 0;
    public static final int UNIT_CELL = 1;
    public static final int WIDTH_CELL = 3;
    public static final int DEPTH_CELL = 4;
    public static final int HEIGHT_CELL = 5;
    public static final int NAME_CELL = 7;
    public static final int KDMAXCODE_CELL = 8;
    public static final int COLOR_CELL = 16;
    public static final int REMARKS_CELL = 23;

    private String filename;

    public ModuleTextFileReader(String filename)
    {
        this.filename = filename;
    }

    public List<ProductModule> loadModules()
    {
        LOG.info("Loading kdmax file " + this.filename);
        Workbook wb = null;

        List<String[]> records = this.getRecords();
        if (records.isEmpty()) return Collections.EMPTY_LIST;

        List<ProductModule> productModules = new ArrayList<>();

        boolean startReading = false;
        String unit = null;
        for (String[] record : records)
        {
            if (record.length == 0) continue;
            try
            {
                if (!startReading)
                {
                    if ("SerialNo".equals(record[0]))
                    {
                        startReading = true;
                    }
                    continue;
                }

                if (record.length <= 1) continue;
                String firstCell = record[UNIT_CELL];
                if ("Worktop".equals(firstCell) || "Accessory".equals(firstCell))
                {
                    break;
                }

                if ("Base unit".equals(firstCell) || "Wall unit".equals(firstCell))
                {
                    unit = firstCell;
                    continue;
                }

                if (record.length <= 2) continue;

                String name = record[NAME_CELL];
                String moduleCode = record[KDMAXCODE_CELL];
                if (StringUtils.isEmpty(moduleCode) || "Handle".equals(name)) continue;

                int sequence = this.getInteger(record[SEQ_CELL]);
                if (sequence == 0) continue;

                String color = record[COLOR_CELL];
                String remarks = record[REMARKS_CELL];
                int width = this.getInteger(record[WIDTH_CELL]);
                int depth = this.getInteger(record[DEPTH_CELL]);
                int height = this.getInteger(record[HEIGHT_CELL]);

                ProductModule module = new ProductModule()
                        .setUnit(unit)
                        .setExternalCode(moduleCode)
                        .setWidth(width)
                        .setHeight(height)
                        .setDepth(depth)
                        .setDimension(width + "x" + depth + "x" + height)
                        .setSequence(sequence)
                        .setDescription(name)
                        .setColorCode(color)
                        .setRemarks(remarks);
                productModules.add(module);
            }
            catch (Exception e)
            {
                LOG.error("Error in parsing record in file:" + this.filename, e);
                for (String field : record)
                {
                    System.out.print(field + " | ");
                }
                return Collections.EMPTY_LIST;
            }
        }

        for (ProductModule module : productModules)
        {
            LOG.debug(module);
        }
        return productModules;
    }

    private List<String[]> getRecords()
    {
        CSVReader reader = null;
        try
        {
            reader = new CSVReader(new FileReader(this.filename), '^');
            return reader.readAll();
        }
        catch (IOException e)
        {
            LOG.error("Error in reading file : " + this.filename, e);
        }
        finally
        {
            try
            {
                reader.close();
            }
            catch (IOException e)
            {
                //Ignore
            }
        }
        return Collections.EMPTY_LIST;
    }

    private int getInteger(String number)
    {
        try
        {
            return Integer.parseInt(number);
        }
        catch (NumberFormatException e)
        {
            LOG.error("Error in parsing " + number + " to integer.");
            return 0;
        }
    }

    public static void main(String[] args)
    {
        new ModuleTextFileReader("D:/work/mygubbi/gamedata/kdmaxquotes/forktail.txt").loadModules();
    }
}
