package com.mygubbi.si.excel;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Created by test on 08-06-2016.
 */
public class ExcelStyles
{
    private CellStyle boldStyle;
    private CellStyle titleStyle;
    private CellStyle indexStyle;
    private CellStyle textStyle;
    private CellStyle specificationStyle;

    public ExcelStyles(Workbook wb)
    {
        this.boldStyle = this.createBoldStyle(wb);
        this.titleStyle = this.createTitleStyle(wb);
        this.indexStyle = this.createIndexStyle(wb);
        this.textStyle=this.createTextStyle(wb);
        this.specificationStyle = this.createSpecificationStyle(wb);
    }

    public CellStyle getBoldStyle()
    {
        return boldStyle;
    }

    public CellStyle getTitleStyle()
    {
        return titleStyle;
    }


    public CellStyle getIndexStyle()
    {
        return indexStyle;
    }

    public CellStyle getTextStyle(){ return textStyle; }

    public CellStyle getSpecificationStyle()
    {
        return specificationStyle;
    }

    private CellStyle createBoldStyle(Workbook wb)
    {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style.setFont(font);
        return style;
    }

    private CellStyle createTitleStyle(Workbook wb)
    {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
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

    private CellStyle createIndexStyle(Workbook wb)
    {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short) 11);
        //font.setFontHeight(new Double(font.getFontHeight() * 1.3).shortValue());
        style.setFont(font);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        return style;
    }

    private CellStyle createTextStyle(Workbook wb)
    {
        CellStyle style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_RIGHT);
        return style;
    }

    private CellStyle createSpecificationStyle(Workbook wb)
    {
        CellStyle style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_LEFT);
        return style;
    }


}
