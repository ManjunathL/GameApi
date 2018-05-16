package com.mygubbi;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Created by Shruthi on 5/10/2018.
 */
public class AddImageToPdf
{
    public static final String SRC = "C:/Users/Shruthi/Downloads/test.pdf";
    public static final String DEST = "E:/pdfwithimage.pdf";
    public static final String IMG = "C:/Users/Shruthi/Downloads/stripwith_pneumonic-13.png";

    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new AddImageToPdf().manipulatePdf(SRC, DEST);
    }

    public void manipulatePdf(String src, String dest) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        Image image = Image.getInstance(IMG);
        PdfImage stream = new PdfImage(image, "", null);
        //stream.put(new PdfName("ITXT_SpecialId"), new PdfName("123456789"));
        PdfIndirectObject ref = stamper.getWriter().addToBody(stream);
        image.setDirectReference(ref.getIndirectReference());
        image.setAbsolutePosition(36, 400);
        PdfContentByte over = stamper.getOverContent(1);
        over.addImage(image);
        stamper.close();
        reader.close();
    }

    /*public static void main(String[] args) throws DocumentException, MalformedURLException, IOException {

        try
        {
            //PdfReader reader = new PdfReader("C:/Users/Shruthi/Downloads/test.pdf");
            PdfReader reader = new PdfReader("E:/pdfwithimage.pdf");
            //PdfStamper stamper = new PdfStamper(reader, new FileOutputStream("E:/pdfwithimage.pdf"));
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream("C:/Users/Shruthi/Downloads/test.pdf"));
            Rectangle pagesize;
            int n = reader.getNumberOfPages();
            for (int i = 1; i <= n; i++) {
                PdfContentByte over = stamper.getOverContent(i);
                pagesize = reader.getPageSize(i);
                float x = pagesize.getLeft() + 10;
                float y = pagesize.getTop() - 50;
                Image img = Image.getInstance("C:/Users/Shruthi/Downloads/stripwith_pneumonic-13.png");
                img.setAbsolutePosition(x, y);
                over.addImage(img);
            }
            stamper.close();
            reader.close();
        }
        catch (Exception e)
        {
            System.out.println("exception " +e);
        }

    }*/


}
