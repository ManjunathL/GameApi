package com.mygubbi.game.proposal.quote;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.mygubbi.game.proposal.model.ProposalHeader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileOutputStream;

/**
 * Created by Shruthi on 8/10/2017.
 */
class  CustomBorderForPdf extends PdfPageEventHelper {
    public void onEndPage(PdfWriter writer, Document document) {

        PdfContentByte canvas = writer.getDirectContent();
        Rectangle rect = new Rectangle(28, 28, 580, 830);
        rect.setBorder(Rectangle.BOX);
        rect.setBorderWidth(2);
        canvas.rectangle(rect);
    }
}
public class OfficeUseOnlyPdf
{
    private final static Logger LOG = LogManager.getLogger(OfficeUseOnlyPdf.class);
    Font bookingformfsize=new Font(Font.FontFamily.TIMES_ROMAN,8,Font.NORMAL);
    Font bookingformfsize1=new Font(Font.FontFamily.TIMES_ROMAN,10,Font.NORMAL,BaseColor.ORANGE);
    Font headingSize=new com.itextpdf.text.Font(Font.FontFamily.TIMES_ROMAN,10, com.itextpdf.text.Font.BOLD);
    Font zapfdingbats = new Font(Font.FontFamily.ZAPFDINGBATS, 16);
    ProposalHeader proposalHeader;
    public OfficeUseOnlyPdf(ProposalHeader proposalHeader)
    {
        this.proposalHeader=proposalHeader;

    }
    public void cretePdf(String destination)
    {
        try
        {
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(destination));
            writer.setPdfVersion(PdfWriter.VERSION_1_7);
            writer.createXmpMetadata();
            document.open();
            writer.setPageEvent(new CustomBorder());
            Paragraph p;

            PdfPTable officedetails = new PdfPTable(1);
            officedetails.setWidthPercentage(100);
            p=new Paragraph("FOR OFFICE USE ONLY",headingSize);
            p.setAlignment(Element.ALIGN_CENTER);
            PdfPCell scell1 = new PdfPCell();
            scell1.addElement(p);
            scell1.setBackgroundColor(BaseColor.ORANGE);
            officedetails.addCell(scell1);
            document.add(officedetails);

            p=new Paragraph();
            document.add(p);

            p = new Paragraph("Project Name : " + proposalHeader.getProjectName() + "                                                                                                              " +" Apartment No : __________         Floor No : ___________ ", bookingformfsize);
            document.add(p);

            p = new Paragraph(" ");
            document.add(p);

            p = new Paragraph("Sale able Area in sft :  ___________________________________________________________________________________________________________", bookingformfsize);
            document.add(p);

            p = new Paragraph(" ");
            document.add(p);

            p = new Paragraph("Advance Money Paid : Rs :  ___________________________________" +" /- " +"                      "+ " VideCheque/DD/Pay Order Number :  ______________________", bookingformfsize);
            document.add(p);

            p = new Paragraph(" ");
            document.add(p);

            p = new Paragraph("Dated : ____________________" + "                                 "+  " Drawn On :___________________ " + "                             "+"Drawn On : _______________________ Bank", bookingformfsize);
            document.add(p);

            p = new Paragraph(" ");
            document.add(p);

            p = new Paragraph("Receipt Number :  ____________________________________" +"                                                                       " + " Dated : ________________________________", bookingformfsize);
            document.add(p);

            p = new Paragraph(" ");
            document.add(p);

            Phrase ph4=new Phrase();
            ph4.add(new Chunk("Application  ",bookingformfsize));
            ph4.add(new Chunk(" Accepted : ",bookingformfsize));
            ph4.add(new Chunk(" o  ", zapfdingbats));
            ph4.add(new Chunk(" Rejected : ",bookingformfsize));
            ph4.add(new Chunk(" o  ", zapfdingbats));
            p=new Paragraph();
            p.add(ph4);
            //p = new Paragraph("Banking & Finance_____ " + " " + "Manufacturing/Distribution______ " + " " + "Others,please specify:________", bookingformfsize);
            document.add(p);

            p = new Paragraph(" ");
            document.add(p);

            p = new Paragraph("Name, Seal and Signature of the Sales person : " + proposalHeader.getDesignerName() , bookingformfsize);
            document.add(p);

            p = new Paragraph(" ");
            document.add(p);

            p = new Paragraph("Alloted Designer : " +proposalHeader.getDesignerName() , bookingformfsize);
            document.add(p);

            p = new Paragraph(" ");
            document.add(p);

            p = new Paragraph("Date: ____________________________" , bookingformfsize);
            document.add(p);

            p = new Paragraph(" ");
            document.add(p);

            p = new Paragraph("Place: ___________________________                                                                                                                                        Signature of Head - Sales " , bookingformfsize);
            document.add(p);

            p = new Paragraph(" ");
            document.add(p);

            PdfPTable remarksdetails = new PdfPTable(1);
            remarksdetails.setWidthPercentage(100);
            p=new Paragraph("REMARKS / APPROVALS ",headingSize);
            p.setAlignment(Element.ALIGN_CENTER);
            scell1 = new PdfPCell();
            scell1.setBackgroundColor(BaseColor.ORANGE);
            scell1.addElement(p);
            remarksdetails.addCell(scell1);
            document.add(remarksdetails);
            document.close();

            BookingFormPdf bookingFormPdf=new BookingFormPdf();
            bookingFormPdf.createBookingForm();
        }
        catch (Exception e)
        {
            LOG.debug("Exception in booking form " +e);
            e.printStackTrace();
        }

    }
}
