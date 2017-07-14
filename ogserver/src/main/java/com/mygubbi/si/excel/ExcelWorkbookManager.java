package com.mygubbi.si.excel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

/**
 * Created by test on 08-06-2016.
 */
public class ExcelWorkbookManager extends XSSFWorkbook {
    private final static Logger LOG = LogManager.getLogger(ExcelWorkbookManager.class);

    private String xlFilename;
    private Workbook wb;
    private ExcelStyles styles;

    public ExcelWorkbookManager(String xlFilename)
    {
        this.xlFilename = xlFilename;
        this.openWorkbook();
        this.createStyles();
    }

    public ExcelStyles getStyles()
    {
        return this.styles;
    }

    private void createStyles()
    {
        this.styles = new ExcelStyles(this.wb);
    }

    public void openWorkbook()
    {
        this.wb = this.getWorkbook(this.xlFilename);
    }

    public Sheet getSheetByName(String name)
    {
        return this.wb.getSheet(name);
    }

    public void closeWorkbook()
    {
        try
        {
            this.wb.write(new BufferedOutputStream(new FileOutputStream(this.xlFilename)));
            this.wb.close();
        }
        catch (IOException e)
        {
            LOG.error("Error in wiring out the quote xls file at " + this.xlFilename);
        }
    }

    private Workbook getWorkbook(String quoteXls)
    {
        Workbook wb = null;
        try
        {
//            wb = new XSSFWorkbook(new BufferedInputStream(getClass().getResourceAsStream(quoteXls)));
            wb = new XSSFWorkbook(new BufferedInputStream(new FileInputStream(quoteXls)));
        }
        catch (IOException e)
        {
            throw new RuntimeException(quoteXls + " workbook is not available", e);
        }
        return wb;
    }

}
