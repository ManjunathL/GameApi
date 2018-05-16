package com.mygubbi;

import com.itextpdf.text.Document;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;

import java.io.FileOutputStream;

/**
 * Created by Shruthi on 5/15/2018.
 */
class CustomBorderforBookingForm extends PdfPageEventHelper {
    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte canvas = writer.getDirectContent();
        Rectangle rect = new Rectangle(28, 28, 580, 830);
        rect.setBorder(Rectangle.BOX);
        rect.setBorderWidth(2);
        canvas.rectangle(rect);
    }
}
public class Border
{
    public static void main(String[] args)
    {
        try
        {
            PdfReader pdfReader = new PdfReader("C:/Users/Shruthi/Downloads/WorksContract.pdf");
            Document document=new Document();
            //PdfWriter writer=PdfWriter.getInstance(document, new FileOutputStream("E:/WorksContract.pdf"));
            PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream("E:/pdfwithimage.pdf"));
            //pdfStamper.setPageAction();
            //writer.setPageEvent(new CustomBorderforBookingForm());


            for(int i=1; i<= pdfReader.getNumberOfPages(); i++){
                PdfContentByte canvas = pdfStamper.getOverContent(i);
                Rectangle rect = new Rectangle(28, 28, 580, 530);
                rect.setBorder(Rectangle.BOX);
                rect.setBorderWidth(2);
                canvas.rectangle(rect);
                /*canvas.fill();
                canvas.restoreState();*/
            }
            pdfStamper.close();
        }
        catch(Exception e)
        {
            System.out.println("exception " +e);
        }

    }


}
