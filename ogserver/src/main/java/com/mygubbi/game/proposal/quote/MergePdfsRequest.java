package com.mygubbi.game.proposal.quote;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.*;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.itextpdf.text.*;
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
    private String VersionStatus;
    private String crmId;
    private String quoteNo;
    private String versionNo;
    private final static Logger LOG = LogManager.getLogger(MergePdfsRequest.class);
    public MergePdfsRequest(List<String> pdfs,String mergedFileName,String mergedAndPageNumberedFileName,String VersionStatus,String crmId,String quoteNo,String versionNo){
        this.inputPdfs = pdfs;
        this.mergedFileName = mergedFileName;
        this.mergedAndPageNumberedFileName = mergedAndPageNumberedFileName;
        this.VersionStatus=VersionStatus;
        this.crmId=crmId;
        this.quoteNo=quoteNo;
        this.versionNo=versionNo;
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
            String FooterText=crmId+ "/" +quoteNo+ "/" +versionNo;
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
            addPageNumberToPdf(FooterText);
            if (VersionStatus == null || VersionStatus.length() == 0) {
                LOG.info("Version status empty");
            }else if(VersionStatus.equals("Draft"))
            {
                addPageNumberwithwatermarkToPdf(FooterText);
            }
        }

        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    private void addPageNumberToPdf (String FooterText) {
        LOG.info("Footer text inside add page numbers" +FooterText);
        String FONTS = "Montserrat-Regular.ttf";
        try {
            com.itextpdf.kernel.pdf.PdfDocument pdfDoc = new com.itextpdf.kernel.pdf.PdfDocument(new com.itextpdf.kernel.pdf.PdfReader(this.mergedFileName), new com.itextpdf.kernel.pdf.PdfWriter(mergedAndPageNumberedFileName));
            com.itextpdf.layout.Document doc = new com.itextpdf.layout.Document(pdfDoc);
            FontProgram fontProgram = FontProgramFactory.createFont(FONTS);
            PdfFont font = PdfFontFactory.createFont(fontProgram, PdfEncodings.WINANSI, true);

            int n = pdfDoc.getNumberOfPages();
            com.itextpdf.kernel.pdf.PdfPage page;
            com.itextpdf.kernel.pdf.PdfNumber rotate;
            for (int i = 1; i <= n; i++) {
                page = pdfDoc.getPage(i);
                rotate = page.getPdfObject().getAsNumber(com.itextpdf.kernel.pdf.PdfName.Rotate);
                int x = 536;
                int x1=180;
                int y = 10;
                int y1=10;
                float angle = 0;
                if (rotate != null && rotate.toString().equalsIgnoreCase("90")) {
                    x = 585;
                    x1= 585;
                    y = 785;
                    y1= 180;
                    angle = (float) 76.97;
                }
                doc.showTextAligned(new Paragraph(String.format("Page %s of %s", i, n)).setFont(font), x, y, i, TextAlignment.CENTER, VerticalAlignment.BOTTOM, angle);
                doc.showTextAligned(new Paragraph(String.format("Client ref : "+FooterText, i, n)).setFont(font), x1, y1, i, TextAlignment.CENTER, VerticalAlignment.BOTTOM, angle);
            }
            doc.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void addPageNumberwithwatermarkToPdf (String FooterText) {
        String FONTS = "Montserrat-Regular.ttf";
        try {
            Rectangle pagesize;
            com.itextpdf.kernel.pdf.PdfDocument pdfDoc = new com.itextpdf.kernel.pdf.PdfDocument(new com.itextpdf.kernel.pdf.PdfReader(this.mergedFileName), new com.itextpdf.kernel.pdf.PdfWriter(mergedAndPageNumberedFileName));
            com.itextpdf.layout.Document doc = new com.itextpdf.layout.Document(pdfDoc);
            FontProgram fontProgram = FontProgramFactory.createFont(FONTS);
            PdfFont font = PdfFontFactory.createFont(fontProgram, PdfEncodings.WINANSI, true);
            Font f2 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12);

            int n = pdfDoc.getNumberOfPages();
            com.itextpdf.kernel.pdf.PdfPage page;
            com.itextpdf.kernel.pdf.PdfNumber rotate;
            Paragraph p=new Paragraph("Draft copy not for circulation");
            p.setFont(font);
            p.setFontSize(30);
            p.setFontColor(Color.GRAY);
            for (int i = 1; i <= n; i++) {
                page = pdfDoc.getPage(i);
                rotate = page.getPdfObject().getAsNumber(com.itextpdf.kernel.pdf.PdfName.Rotate);
                int x = 536;
                int x1=180;
                int y = 10;
                int y1=10;
                float angle = 0;
                if (rotate != null && rotate.toString().equalsIgnoreCase("90")) {
                    x = 585;
                    x1= 585;
                    y = 785;
                    y1= 180;
                    angle = (float) 76.97;
                }
                doc.showTextAligned(p, 297, 420, i, TextAlignment.CENTER, VerticalAlignment.BOTTOM, 45);
                doc.showTextAligned(new Paragraph(String.format("Page %s of %s", i, n)).setFont(font), x, y, i, TextAlignment.CENTER, VerticalAlignment.BOTTOM, angle);
                doc.showTextAligned(new Paragraph(String.format("Client ref : "+FooterText, i, n)).setFont(font), x1, y1, i, TextAlignment.CENTER, VerticalAlignment.BOTTOM, angle);
            }
            doc.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void addWaterMark()
    {
        try
        {
            PdfFont font = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
            PdfReader reader = new PdfReader(this.mergedFileName);
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(mergedAndPageNumberedFileName));
            int n = reader.getNumberOfPages();
            // text watermark
            Font f = new Font(Font.FontFamily.HELVETICA, 30);
            Font f1=new Font(Font.FontFamily.TIMES_ROMAN,12,Font.BOLD,BaseColor.BLACK);
            Phrase p = new Phrase("Draft copy not for circulation", f);
            // transparency
            PdfGState gs1 = new PdfGState();
            gs1.setFillOpacity(0.5f);
            // properties
            PdfContentByte over;
            Rectangle pagesize;
            float x, y;
            // loop over every page
            for (int i = 1; i <= n; i++) {
                Phrase pageNumber=new Phrase("Page " +i + " of " +n,f1);
                pagesize = reader.getPageSizeWithRotation(i);
                x = (pagesize.getLeft() + pagesize.getRight()) / 2;
                LOG.info("X in draft " +x);
                y = (pagesize.getTop() + pagesize.getBottom()) / 2;
                LOG.info("Y in draft " +y);
                over = stamper.getOverContent(i);
                over.saveState();
                over.setGState(gs1);
                ColumnText.showTextAligned(over, Element.ALIGN_CENTER, p, x, y, 45);
                ColumnText.showTextAligned(over,Element.ALIGN_BOTTOM,pageNumber,500,10,0);
                over.restoreState();
            }
            stamper.close();
            reader.close();
        }
        catch(Exception e)
        {
            LOG.info("exception while adding the water mark");
        }
    }


}
