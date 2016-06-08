package com.mygubbi.game.proposal.quote;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

/**
 * Created by Sunil on 22-05-2016.
 */
public class ExcelQuoteCreator
{
    private final static Logger LOG = LogManager.getLogger(ExcelQuoteCreator.class);

    private String quoteXls;
    private QuoteData quoteData;

    private Sheet quoteSheet;
    private Sheet dataSheet;
    private Workbook wb;
    private CellStyle boldStyle;
    private CellStyle titleStyle;

    public ExcelQuoteCreator(String quoteXls, QuoteData quoteData)
    {
        this.quoteXls = quoteXls;
        this.quoteData = quoteData;
    }

    public void prepareQuote()
    {
        this.openWorkbook();
        this.boldStyle = this.createBoldStyle();
        this.titleStyle = this.createTitleStyle();

        new QuotationSheetCreator(this.quoteSheet, this.quoteData, this.boldStyle, this.titleStyle).prepare();
        new DataSheetCreator(this.dataSheet, this.quoteData, this.boldStyle).prepare();

        this.closeWorkbook();
    }

    private CellStyle createBoldStyle()
    {
        CellStyle style = this.wb.createCellStyle();
        Font font = this.wb.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style.setFont(font);
        return style;
    }

    private CellStyle createTitleStyle()
    {
        CellStyle style = this.wb.createCellStyle();
        Font font = this.wb.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setFontHeight(new Double(font.getFontHeight() * 1.3).shortValue());
        style.setFont(font);
        /*XSSFColor orange = new XSSFColor(new java.awt.Color(255,200,0));
        LOG.info("Title color code prior : " + style.getFillBackgroundColor());
        XSSFColor orange = new XSSFColor(Color.ORANGE);
        style.setFillBackgroundColor(orange.getIndexed());
        style.setFillForegroundColor(orange.getIndexed());
        LOG.info("Title color: " + style.getFillBackgroundColorColor());
        LOG.info("Title color code: " + style.getFillBackgroundColor()); */
        return style;
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

    private void openWorkbook()
    {
        this.wb = this.getWorkbook(this.quoteXls);
        this.quoteSheet = wb.getSheet("Quote");
        if (quoteSheet == null) throw new RuntimeException("Quote sheet not found.");
        this.dataSheet = wb.getSheet("Data");
        if (dataSheet == null) throw new RuntimeException("Data sheet not found.");
    }

    private void closeWorkbook()
    {
        try
        {
            this.wb.write(new BufferedOutputStream(new FileOutputStream(this.quoteXls)));
            this.wb.close();
        }
        catch (IOException e)
        {
            LOG.error("Error in wiring out the quote xls file at " + this.quoteXls);
        }
    }


}
