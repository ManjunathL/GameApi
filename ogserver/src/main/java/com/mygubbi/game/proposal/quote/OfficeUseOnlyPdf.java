package com.mygubbi.game.proposal.quote;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.mygubbi.game.proposal.model.ProposalHeader;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileOutputStream;
import java.util.Date;

/**
 * Created by Shruthi on 8/10/2017.
 */
public class OfficeUseOnlyPdf
{
    private final static Logger LOG = LogManager.getLogger(OfficeUseOnlyPdf.class);
    Font zapfdingbats = new Font(Font.FontFamily.ZAPFDINGBATS, 16);
    ProposalHeader proposalHeader;
    BaseFont basefontforMontserrat;
    Font bookingformfsize, headingSize,tableheadingWhite,fsize;
    BaseColor baseColor= new BaseColor(234, 92, 54); //rgb(234, 92, 54)
    public static String[][] FONTS = {
            {"Montserrat-Regular.ttf", BaseFont.WINANSI}
    };
    public OfficeUseOnlyPdf(ProposalHeader proposalHeader) {
        this.proposalHeader = proposalHeader;
        try {

            for (int i = 0; i < FONTS.length; i++) {
                basefontforMontserrat = BaseFont.createFont(FONTS[i][0], FONTS[i][1], BaseFont.NOT_EMBEDDED);
                bookingformfsize=new Font(basefontforMontserrat,8,Font.BOLD);
                fsize=new Font(basefontforMontserrat,8,Font.NORMAL);
                headingSize=new com.itextpdf.text.Font(basefontforMontserrat,8, com.itextpdf.text.Font.BOLD);
                tableheadingWhite=new com.itextpdf.text.Font(basefontforMontserrat,7, com.itextpdf.text.Font.BOLD,BaseColor.WHITE);
            }
        }
        catch (Exception e)
        {
            LOG.info("exception in font creation at officeuseonlypdf" +e);
        }
    }
    public void cretePdf(String destination)
    {
        try
        {
            //Document document = new Document();
            Document document = new Document(PageSize.A4, 36, 36, 36, 53);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(destination));
            writer.setPdfVersion(PdfWriter.VERSION_1_7);
            writer.createXmpMetadata();
            document.open();
            writer.setPageEvent(new CustomBorder());
            Paragraph p;

            PdfPTable officedetails = new PdfPTable(1);
            officedetails.setWidthPercentage(100);
            p=new Paragraph("FOR OFFICE USE ONLY",tableheadingWhite);
            p.setAlignment(Element.ALIGN_CENTER);
            PdfPCell scell1 = new PdfPCell();
            scell1.addElement(p);
            scell1.setBackgroundColor(baseColor);
            officedetails.addCell(scell1);
            document.add(officedetails);

            p=new Paragraph();
            document.add(p);

            Phrase phrase3 = new Phrase();
            phrase3.add(new Chunk("Project Name : ",bookingformfsize));
            phrase3.add(new Chunk(proposalHeader.getProjectName(),fsize));
            phrase3.add(new Chunk("                                                 " +" Apartment No : __________         Floor No : ___________ ",bookingformfsize));
            document.add(phrase3);

            p = new Paragraph(" ");
            document.add(p);

            p = new Paragraph("Sale able Area in sft :  ___________________________________________________________________________________________________________", bookingformfsize);
            document.add(p);

            p = new Paragraph(" ");
            document.add(p);

            p = new Paragraph("Advance Money Paid : Rs :  ___________________________________" +" /- " +"     "+ " Cheque/DD/Pay Order Number :  ______________________", bookingformfsize);
            document.add(p);

            p = new Paragraph(" ");
            document.add(p);

            p = new Paragraph("Dated : ____________________" + "       "+  " Drawn On :___________________ " + "        "+"Drawn On : _______________________ Bank", bookingformfsize);
            document.add(p);

            p = new Paragraph(" ");
            document.add(p);

            p = new Paragraph("Receipt Number :  ____________________________________" +"                                                  " + " Dated : ________________________________", bookingformfsize);
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
            document.add(p);

            p = new Paragraph(" ");
            document.add(p);

            Phrase phrase2 = new Phrase();
            phrase2.add(new Chunk("Name, Seal and Signature of the Sales person : ",bookingformfsize));
            phrase2.add(new Chunk(proposalHeader.getSalespersonName(),fsize));
            document.add(phrase2);

            p = new Paragraph(" ");
            document.add(p);

            Phrase phrase1 = new Phrase();
            phrase1.add(new Chunk("Alloted Designer : ",bookingformfsize));
            phrase1.add(new Chunk(proposalHeader.getDesignerName(),fsize));

            document.add(phrase1);

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
            p=new Paragraph("REMARKS / APPROVALS ",tableheadingWhite);
            p.setAlignment(Element.ALIGN_CENTER);
            scell1 = new PdfPCell();
            scell1.setBackgroundColor(baseColor);
            scell1.addElement(p);
            remarksdetails.addCell(scell1);
            document.add(remarksdetails);
            document.close();
        }
        catch (Exception e)
        {
            LOG.debug("Exception in Office use only pdf " +e);
            e.printStackTrace();
        }
    }
}
