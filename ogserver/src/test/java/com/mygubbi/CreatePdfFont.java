package com.mygubbi;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import javax.swing.border.Border;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Shruthi on 5/9/2018.
 */
public class CreatePdfFont
{
    public static String RESULT = "E:/font_types.pdf";
    public static String TEXT = "Project Name";
    public static String[][] FONTS = {
            {BaseFont.HELVETICA, BaseFont.WINANSI},
            {"C:/Users/Shruthi/Downloads/Montserrat-Regular.ttf", BaseFont.WINANSI}
    };
    public void createPdf(String filename) throws IOException, DocumentException
    {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        document.open();
        BaseFont bf;
        Font font;
        for (int i = 0; i < FONTS.length; i++) {
            bf = BaseFont.createFont(FONTS[i][0], FONTS[i][1], BaseFont.EMBEDDED);
            document.add(new Paragraph(
                    String.format("Font file: %s with encoding %s", FONTS[i][0], FONTS[i][1])));
            document.add(new Paragraph(
                    String.format("iText class: %s", bf.getClass().getName())));
            font = new Font(bf, 12,Font.BOLD);
            document.add(new Paragraph(TEXT, font));
            document.add(new LineSeparator(0.5f, 100, null, 0, -5));
        }
        PdfPTable orderdetails = new PdfPTable(1);
        orderdetails.setWidthPercentage(100);
        //orderdetails.setBorder(Border.NO_BORDER);
        Paragraph p=new Paragraph(" ");
        p.setAlignment(Element.ALIGN_CENTER);
        PdfPCell scell1 = new PdfPCell();
        scell1.addElement(p);
        BaseColor baseColor= new BaseColor(234, 92, 54); //rgb(234, 92, 54)
        scell1.setBackgroundColor(baseColor);
        //scell1.setMinimumHeight();
        scell1.setFixedHeight(10f);
        scell1.setBorder(Rectangle.NO_BORDER);
        orderdetails.addCell(scell1);
        document.add(new Paragraph(" "));
        document.add(orderdetails);
        document.close();
    }

    public static void main(String[] args) throws IOException, DocumentException {
        new CreatePdfFont().createPdf(RESULT);
    }
}
