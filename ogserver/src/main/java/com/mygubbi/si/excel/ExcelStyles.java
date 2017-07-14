package com.mygubbi.si.excel;

import javafx.scene.layout.BackgroundFill;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.w3c.dom.css.RGBColor;

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
    private CellStyle coloredCellStyle;
    private CellStyle lockedTextStyle;

    public ExcelStyles(Workbook wb)
    {
        this.boldStyle = this.createBoldStyle(wb);
        this.titleStyle = this.createTitleStyle(wb);
        this.indexStyle = this.createIndexStyle(wb);
        this.textStyle=this.createTextStyle(wb);
        this.specificationStyle = this.createSpecificationStyle(wb);
        this.coloredCellStyle = this.createBackGroundStyleForCell(wb);
        this.lockedTextStyle = this.createCellLockedStyle(wb);
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
    public CellStyle getLockedTextStyle(){ return lockedTextStyle; }

    public CellStyle getSpecificationStyle()
    {
        return specificationStyle;
    }
    public CellStyle getColoredCellStyle()
    {
        return coloredCellStyle;
    }

    private CellStyle createBoldStyle(Workbook wb)
    {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style.setFont(font);
        return style;
    }

    private CellStyle createBackGroundStyleForCell(Workbook wb)
    {
        CellStyle style = wb.createCellStyle();
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setWrapText(true);
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
        style.setAlignment(CellStyle.ALIGN_LEFT);

        style.setWrapText(true);
        return style;
    }

    private CellStyle createCellLockedStyle(Workbook wb)
    {
        CellStyle unlockedCellStyle = wb.createCellStyle();
        unlockedCellStyle.setLocked(false);
        return unlockedCellStyle;
    }

    private CellStyle createSpecificationStyle(Workbook wb)
    {
        CellStyle style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_LEFT);
        return style;
    }


}
