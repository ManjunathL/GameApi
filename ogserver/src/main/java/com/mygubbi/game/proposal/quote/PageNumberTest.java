package com.mygubbi.game.proposal.quote;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;


import java.io.File;

import java.io.File;

/**
 * Created by Shruthi on 10/28/2017.
 */
public class PageNumberTest
{
    public static final String SRC = "E:/WorkContract.pdf";
    public static final String DEST = "E:/WorkContractMergedPdf.pdf";

    public static void main(String[] args)
            throws Exception {
        File file = new File(DEST);
        new PageNumberTest().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(DEST));
        Document doc = new Document(pdfDoc);
        int n = pdfDoc.getNumberOfPages();
        for (int i = 1; i <= n; i++) {
            //doc.showTextAligned(new Paragraph(String.format("page %s of %s", i, n)), 559, 810, i, TextAlignment.CENTER, VerticalAlignment.MIDDLE, 0);
            doc.showTextAligned(new Paragraph(String.format("page %s of %s", i, n)), ((doc.getRightMargin()-doc.getLeftMargin())/2), doc.getBottomMargin()-20, i, TextAlignment.CENTER, VerticalAlignment.BOTTOM, 0);
        }
        doc.close();
    }

}
