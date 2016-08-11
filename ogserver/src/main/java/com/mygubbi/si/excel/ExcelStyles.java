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

    public ExcelStyles(Workbook wb)
    {
        this.boldStyle = this.createBoldStyle(wb);
        this.titleStyle = this.createTitleStyle(wb);
    }

    public CellStyle getBoldStyle()
    {
        return boldStyle;
    }

    public CellStyle getTitleStyle()
    {
        return titleStyle;
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

}
