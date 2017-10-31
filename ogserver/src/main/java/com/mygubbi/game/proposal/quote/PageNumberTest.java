package com.mygubbi.game.proposal.quote;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;


import java.io.File;

import java.io.File;

/**
 * Created by Shruthi on 10/28/2017.
 */
public class PageNumberTest
{
    public static final String SRC = "E:/WorksContractQuoteFinal.pdf";
    public static final String DEST = "E:/WorkContractMergedPdf.pdf";
    Font fsize=new Font(Font.FontFamily.TIMES_ROMAN,8,Font.NORMAL);
    PdfFont font;

    public static void main(String[] args)
            throws Exception {
        File file = new File(DEST);
        new PageNumberTest().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        try
        {
            font = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(DEST));
        Document doc = new Document(pdfDoc);
        int n = pdfDoc.getNumberOfPages();
        for (int i = 1; i <= n; i++) {
            //doc.showTextAligned(new Paragraph(String.format("page %s of %s", i, n)), 559, 810, i, TextAlignment.CENTER, VerticalAlignment.MIDDLE, 0);
//            doc.showTextAligned(new Paragraph(String.format("page %s of %s", i, n)), ((doc.getRightMargin()-doc.getLeftMargin())/2), doc.getBottomMargin()-20, i, TextAlignment.CENTER, VerticalAlignment.BOTTOM, 0);
            PageSize pageSize = new PageSize();
            System.out.println("x: " +(doc.getBottomMargin() + 500) +"y: " +((doc.getLeftMargin()+doc.getRightMargin())/2));
            //doc.showTextAligned(new Paragraph(String.format("page %s of %s", i, n)), doc.getBottomMargin() + 500, ((doc.getLeftMargin()+doc.getRightMargin())/2)-5, i, TextAlignment.CENTER, VerticalAlignment.BOTTOM, 0);
            doc.showTextAligned(new Paragraph(String.format("Page %s of %s", i, n)).setFont(font), 536, 10, i, TextAlignment.CENTER, VerticalAlignment.BOTTOM, 0);

        }
        doc.close();
    }

}
