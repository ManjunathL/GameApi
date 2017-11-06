package com.mygubbi.game.proposal.quote;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Shruthi on 10/25/2017.
 */
public class WorksContractBookletView
{
    public static final String SRC = "E:/WorkContract.pdf";
    public static final String DEST = "E:/WorkContractMergedPdf.pdf";

    public static void main(String[] args)
            throws Exception {
        File file = new File(DEST);
        new WorksContractBookletView().manipulatePdf(SRC, DEST);
    }
    public void manipulatePdf(String src, String dest)
            throws Exception{
        // Creating a reader
        PdfReader reader = new PdfReader(src);
        // step 1
//        Document document = new Document(PageSize.A3.rotate());
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(DEST));
        // step 2

        // step 3
        document.open();
        // step 4
        PdfContentByte canvas = writer.getDirectContent();
        float a4_width = PageSize.A4.getWidth();
        System.out.println("page width = " +a4_width);
        int n = reader.getNumberOfPages();
        int p = 0;
        PdfImportedPage page;
        while (p++ < n) {
            page = writer.getImportedPage(reader, p);
            if (p % 2 == 1) {
                System.out.println("inside if = " +p);
                canvas.addTemplate(page, 0, 0);
            }
            else {
                System.out.println("inside else" +p);
                canvas.addTemplate(page, a4_width, 0);
                document.newPage();
            }
        }
        // step 5
        document.close();
        reader.close();
    }

}
