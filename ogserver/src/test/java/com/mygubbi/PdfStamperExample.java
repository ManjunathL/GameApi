package com.mygubbi;

import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import java.io.FileOutputStream;

/**
 * Created by Shruthi on 5/10/2018.
 */
public class PdfStamperExample
{
    public static final String SRC = "C:/Users/Shruthi/Downloads/test.pdf";
    public static final String DEST = "E:/pdfwithimage.pdf";
    public static final String IMG = "C:/Users/Shruthi/Downloads/stripwith_pneumonic-13.png";
    public static void main(String[] args) {
        try {
            PdfReader pdfReader = new PdfReader("C:/Users/Shruthi/Downloads/Quotation2.pdf");

            PdfStamper pdfStamper = new PdfStamper(pdfReader,
                    new FileOutputStream("E:/pdfwithimage.pdf"));

            Image image = Image.getInstance("C:/Users/Shruthi/Downloads/stripwith_pneumonic-13.png");

            for(int i=1; i<= pdfReader.getNumberOfPages(); i++){

                //put content under
                PdfContentByte content = pdfStamper.getUnderContent(i);
                /*image.setAbsolutePosition(100f, 150f);
                content.addImage(image);
*/
                //put content over
                content = pdfStamper.getOverContent(i);
                image.setAbsolutePosition(-0f, -1f);
                image.setWidthPercentage(90);
                content.addImage(image);
                //Text over the existing page
                /*BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA,
                        BaseFont.WINANSI, BaseFont.EMBEDDED);
                content.beginText();
                content.setFontAndSize(bf, 18);
                content.showTextAligned(PdfContentByte.ALIGN_LEFT,"Page No: " + i,430,15,0);
                content.endText();*/

            }

            pdfStamper.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
