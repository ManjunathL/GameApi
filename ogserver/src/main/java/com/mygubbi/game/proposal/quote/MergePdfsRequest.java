package com.mygubbi.game.proposal.quote;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.*;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shilpa on 14/7/17.
 */
public class MergePdfsRequest {

    private List<String> inputPdfs;
    private String mergedFileName;
    private String mergedAndPageNumberedFileName;
    private final static Logger LOG = LogManager.getLogger(MergePdfsRequest.class);
    public MergePdfsRequest(List<String> pdfs,String mergedFileName,String mergedAndPageNumberedFileName){
        this.inputPdfs = pdfs;
        this.mergedFileName = mergedFileName;
        this.mergedAndPageNumberedFileName = mergedAndPageNumberedFileName;
    }


    public String getMergedFileName() {
        return mergedFileName;
    }

    public void setMergedFileName(String mergedFileName) {
        this.mergedFileName = mergedFileName;
    }

    public String getMergedAndPageNumberedFileName() {
        return mergedAndPageNumberedFileName;
    }

    public void setMergedAndPageNumberedFileName(String mergedAndPageNumberedFileName) {
        this.mergedAndPageNumberedFileName = mergedAndPageNumberedFileName;
    }
    public void mergePdfFiles(){
        try {
            Document PDFCombineUsingJava = new Document();
            PdfCopy copy = new PdfCopy(PDFCombineUsingJava, new FileOutputStream(mergedFileName));
            PDFCombineUsingJava.open();
            PdfReader ReadInputPDF;
            int number_of_pages;
            for (int i = 0; i < inputPdfs.size(); i++) {
                ReadInputPDF = new PdfReader(inputPdfs.get(i));
                number_of_pages = ReadInputPDF.getNumberOfPages();
                for (int page = 0; page < number_of_pages; ) {
                    copy.addPage(copy.getImportedPage(ReadInputPDF, ++page));

                }
            }
            PDFCombineUsingJava.close();
            addPageNumberToPdf();
        }

        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    private void addPageNumberToPdf () {
        try {
            com.itextpdf.kernel.pdf.PdfDocument pdfDoc = new com.itextpdf.kernel.pdf.PdfDocument(new com.itextpdf.kernel.pdf.PdfReader(this.mergedFileName), new com.itextpdf.kernel.pdf.PdfWriter(mergedAndPageNumberedFileName));
            com.itextpdf.layout.Document doc = new com.itextpdf.layout.Document(pdfDoc);
            PdfFont font = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
            int n = pdfDoc.getNumberOfPages();
            com.itextpdf.kernel.pdf.PdfPage page;
            com.itextpdf.kernel.pdf.PdfNumber rotate;
            for (int i = 1; i <= n; i++) {
                page = pdfDoc.getPage(i);
                rotate = page.getPdfObject().getAsNumber(com.itextpdf.kernel.pdf.PdfName.Rotate);
                int x = 536;
                int y = 10;
                float angle = 0;
                if (rotate != null && rotate.toString().equalsIgnoreCase("90")) {
                    x = 585;
                    y = 785;
                    angle = PageSize.A4.rotate().getRotation()-13;
                }
                doc.showTextAligned(new Paragraph(String.format("Page %s of %s", i, n)).setFont(font), x, y, i, TextAlignment.CENTER, VerticalAlignment.BOTTOM, angle);

            }
            doc.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
