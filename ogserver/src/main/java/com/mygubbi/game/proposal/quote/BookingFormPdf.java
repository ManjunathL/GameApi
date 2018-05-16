package com.mygubbi.game.proposal.quote;

import com.itextpdf.text.*;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.Font;
import java.io.FileOutputStream;

/**
 * Created by Shruthi on 8/7/2017.
 */
class CustomBorderforBookingForm extends PdfPageEventHelper{
    Image image;
    CustomBorderforBookingForm()
    {
        try
        {
            image = Image.getInstance("stripwith_pneumonic-13.png");
        }
        catch (Exception e)
        {

        }
    }
    public void onEndPage(PdfWriter writer, Document document) {
        try
        {
            PdfContentByte canvas = writer.getDirectContent();
            Rectangle rect = new Rectangle(28, 28, 580, 830);
            rect.setBorder(Rectangle.BOX);
            rect.setBorderWidth(2);
            canvas.rectangle(rect);

            PdfContentByte content = writer.getDirectContent();
            image.setAbsolutePosition(-0f, -1f);
            image.setWidthPercentage(90);
            content.addImage(image);
        }
        catch (Exception e)
        {

        }

    }
}
public class BookingFormPdf
{
    private final static Logger LOG = LogManager.getLogger(BookingFormPdf.class);
    BaseFont basefontforMontserrat;
    BaseColor baseColor= new BaseColor(234, 92, 54); //rgb(234, 92, 54)
    com.itextpdf.text.Font fsize,headingSize,size1,roomNameSizeBOLD,size3,bookingformfsize,headingSizeNewWhite,tableheadingWhite;
    public static String[][] FONTS = {
            {"Montserrat-Regular.ttf", BaseFont.WINANSI}
    };
    /*com.itextpdf.text.Font fsize=new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN,8, com.itextpdf.text.Font.NORMAL);
    com.itextpdf.text.Font headingSize=new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN,15, com.itextpdf.text.Font.BOLD);*/
    BookingFormPdf()
    {
        try
        {
            for (int i = 0; i < FONTS.length; i++) {
                basefontforMontserrat = BaseFont.createFont(FONTS[i][0], FONTS[i][1], BaseFont.NOT_EMBEDDED);
                fsize=new com.itextpdf.text.Font(basefontforMontserrat,8, com.itextpdf.text.Font.NORMAL);
                headingSize=new com.itextpdf.text.Font(basefontforMontserrat,10, com.itextpdf.text.Font.BOLD);
                headingSizeNewWhite=new com.itextpdf.text.Font(basefontforMontserrat,10, com.itextpdf.text.Font.BOLD,BaseColor.WHITE);
                tableheadingWhite=new com.itextpdf.text.Font(basefontforMontserrat,7, com.itextpdf.text.Font.BOLD,BaseColor.WHITE);
                size1=new com.itextpdf.text.Font(basefontforMontserrat,8, com.itextpdf.text.Font.BOLD,baseColor);
                roomNameSizeBOLD=new com.itextpdf.text.Font(basefontforMontserrat,15, com.itextpdf.text.Font.BOLD,BaseColor.BLACK);
                size3=new com.itextpdf.text.Font(basefontforMontserrat,10, com.itextpdf.text.Font.BOLD,BaseColor.BLACK);
                bookingformfsize=new com.itextpdf.text.Font(basefontforMontserrat,8, com.itextpdf.text.Font.BOLD);

            }
        }
        catch (Exception e)
        {
            LOG.info("Exception in font");
        }
    }
    public static void main(String args[])
    {
        BookingFormPdf bookingFormPdf=new BookingFormPdf();
        bookingFormPdf.createBookingForm();
    }
    public void createBookingForm()
    {
        String destination="E:/BookingFormChennai.pdf";
        Paragraph p;
        try
        {
            Document document=new Document();
            PdfWriter writer=PdfWriter.getInstance(document, new FileOutputStream(destination));
            writer.setPdfVersion(PdfWriter.VERSION_1_7);
            writer.createXmpMetadata();
            document.open();
            writer.setPageEvent(new CustomBorderforBookingForm());
            PdfPTable TCdetails = new PdfPTable(1);
            TCdetails.setWidthPercentage(100);
            p=new Paragraph("Terms and Conditions",headingSize);
            p.setAlignment(Element.ALIGN_CENTER);
            PdfPCell scell1 = new PdfPCell();
            scell1.addElement(p);
            scell1.setBackgroundColor(baseColor);
            TCdetails.addCell(scell1);
            document.add(TCdetails);

            p=new Paragraph("1.  Quotation Validity: 30 days \n"
                    + "2.  Order Finalization, Validity and Delivery\n "
                    +"     (a.) We request our clients to kindly check of all details provided  for in the drawing / quotation carefully as they form the basis\n"
                    +"           of the final solution being provided  by MyGubbi.\n"
                    +"     (b.) Any modifications/alterations to the proposed design will have an impact on the techno-commercials of this quote and\n"
                    +"          hence new drawings as well as associated commercials will be provided  by MyGubbi if the same occurs.\n"
                    +"     (c.) Once  the quote  and drawings are finalized,  no alterations  / modifications shall be accommodated. \n"
                    +"     (d.) All finalized drawings and associated commercials will be signed by the client, MyGubbi will retain the original signed\n"
                    +"          documents and a copy of the same will be provided  to the client. \n"
                    +"     (e.) Works Completion shall be within 60 working days of placement of order or as per quotation, whichever is latest, subject to\n "
                    +"          the following:\n"
                    +"          (i)   The design and specifications of the Services has been duly signed by both parties. \n"
                    +"          (ii)  The requisite advance for the services has been paid by the Client, and \n"
                    +"          (iii) There have been no changes  to the scope of work pursuant  to the design and specifications of the Services having been\n"
                    +"                 duly signed off by both parties, i.e. MyGubbi and client.\n"
                    +"     (f.) If MyGubbi is unable  to complete installation  of the Services with the time period conveyed to the Client, a grace period of\n"
                    +"          30 (thirty) working days shall be available  with MyGubbi beyond  the committed  time period to complete the installation.\n"
                    +"          Intimation for any delay in installation  of Services within the time period shall be communicated to the Clients during the\n"
                    +"          tenure of their engagement with MyGubbi\n"
                    +"3.  Prices: \n"
                    +"     (a.) The prices quoted  above are inclusive of Applicable  Taxes & Statutory Levy. \n"
                    +"     (b.) The following are extra chargeable, to be borne by the client (the below are not applicable to orders executed within\n"
                    +"          Chennai city limits): Packaging,Freight, Insurance,  Transportation  and handling  charges from Ware house to destination. \n"
                    +"     (c.) All prevailing taxes (Inter-city and Intrastate); any new taxes, duties and levies that may be applicable from time to time,\n"
                    +"          announced by the Government.\n"
                    +"     (d.) Travel, boarding  and lodging of the installation  team will be charged  on actual  to be borne by the client. \n"
                    +"4.  Payment: \n"
                    +"     (a.) All payments will be in Indian Rupees payable through any of the following instruments: Wire transfer,Demand Draft\n"
                    +"          or cheque payable to be drawn in favour of Gubbi Technologies Pvt. Ltd.; Credit Card payments will attract an additional\n"
                    +"          transaction fee of 2%.\n"
                    +"     (b.) Payments under INR 10,000/- may be made in cash, though  cheque / DD / Bank Transfer (NEFT/RTGS) are the\n"
                    +"          preferred modes.\n"
                    +"5.  Payment Terms: \n"
                    +"     For Customized Orders: Payment of 10% with the Order booking, 40% with finalization of the Design/Services/products,\n "
                    +"     50% will be expected once items are completely manufactured and ready for shipment to client’s site..\n "
                    +"6.  Guidelines, Inclusions and exceptions: \n"
                    +"     (a.) All dimensions are in millimeters  (unless otherwise  specified). \n"
                    +"     (b.) Installation  and transportation will not be charged  for our clients within Chennai city limits only.\n"
                    +"     (c.) In the case of kitchens,  this Quote  does not include  Electro domestic  appliances, Granite,  Dado Tiling and Plumbing\n"
                    +"          fixtures (unless specified).\n"
                    +"     (d.) While the team at MyGubbi will provide all the necessary  help to guide the client on modifications that will be required on\n"
                    +"          existing electrical, plumbing and civil works to accommodate the design finalized with the client, the team at MyGubbi will\n"
                    +"          not be responsible for Electrical, plumbing  and civil works of any kind at the site.\n"
                    +"     (e.) In the case of appliances and plumbing  fixtures, the same shall be procured by MyGubbi on your behalf and in your name\n"
                    +"          and you shall alone be the sole owner thereof. In case of increase  in prices prior to their purchase, ‘Gubbi  Technologies\n"
                    +"          Pvt. Ltd.’ reserves the right to collect the difference amount from the client failing which,  we will cancel  the same.\n"
                    +"          Delivery will also depend on availability  of stock.\n"
                    +"     (f.) MyGubbi shall not be liable for taking any necessary  approvals  from the authorities/RWAs for any civil works extensions as\n"
                    +"          may be required in the work specification.\n"
                    +"7.  Storage \n"
                    +"     (a.) In the event of the site not being ready by the installation  date; all products  will be stored in our transit ware- house\n"
                    +"          which is designed for short term storage only.\n"
                    +"     (b.) The storage facilities will be provided  free of charge for the first 2 weeks and thereafter  INR 6,500  will be charged  per\n"
                    +"          kitchen per month and INR 3,250/ will be charged  per bedroom set per month.  The maximum period of storage shall not\n "
                    +"          exceed three months.\n"
                    +"8.  Risk \n"
                    +"     In the event that the goods delivered  to the installation  site are handled by people  outside My Gubbi’s authorized personnel or\n"
                    +"     in the event that the goods delivered  to the installation  site are mishandled by site personnel outside authorized MyGubbi’s\n"
                    +"     personnel or the site is not secure or the duration  between delivery and installation surpasses 05 working days,title to and \n"
                    +"     risk of loss of the products in such cases shall pass to client upon delivery to the installation site\n \n"
                    +"9.  Damages: \n"
                    +"     (a.) Any breakage  or damages  before and during installation  are the responsibility  of MyGubbi and such parts will be replaced\n"
                    +"          free of charge.In case the damaged items are small, replacement would  be made within 45 days and if the items are large it\n"
                    +"          would  take approx.  99 days.\n "
                    +"     (b.) Damages  caused  by water seepages, normal wear and tear and color changes  due to climatic  conditions are not covered\n"
                    +"          under the MyGubbi company warranty.\n"
                    +"     (c.) Damages  caused  during storage at the client’s site / warehouse, where the material  has been shifted; (at the request of\n"
                    +"          the client) will not be covered by the warranty. \n"
                    +"     (d.) Replacement of damaged material  (post installation  and handover)  will be chargeable to the client. \n"
                    +"10.  Installation  schedule and technical assistance: \n"
                    +"     (a.) Schedule  for installation  will be informed to the client by the technical department. \n"
                    +"     (b.) Kindly contact  us at 080 – 88860860 all technical assistance  and trouble  shooting \n"
                    +"11.  Warranty \n"
                    +"     (a.) Exactness with respect  to the appearance of the final installed product  compared with any previous diagrams, drawings,\n"
                    +"          laminate  colour samples, computer or 3-D rendition, etcetera is not guaranteed. \n"
                    +"     (b.) The products  delivered  under the Service carry a limited warranty of 05 years for its ‘joinery’, effective from the date of\n"
                    +"           invoice.\n"
                    +"     (c.) The warranty for all third-party fittings like water taps, kitchen-sink, white goods and electrical/electronic appliances, etc.\n"
                    +"          procured for and on behalf of the Client shall be applicable as per the norms specified by the particular  brand/manufacturer.\n"
                    +"          Please note that all such appliances shall be procured on your behalf and in your name and you shall alone be the sole owner\n"
                    +"          thereof.\n"
                    +"     (d.) If during the 05 years’ warranty period the delivered  product  fail to operate  due to defect in manufacturing, MyGubbi at its\n"
                    +"          option,will either repair or replace the product  or part therein  in accordance with the terms and conditions below: \n"
                    +"          (i.) All the accessories which are procured by MyGubbi carry a lifetime warranty (on a back to back basis, as\n"
                    +"               assured by the respective  reputed accessory  manufacturer). \n"
                    +"          (ii.) MyGubbi reserves the right to charge a handling  fee and/or servicing fee if a returned  product  is found to be not under\n"
                    +"                warranty as per the conditions below: \n"
                    +"                  1. Upon a product being identified  by a customer  as failing to operate  under normal use and service, the customer\n"
                    +"                     will inform MyGubbi citing the failure of the condition. \n"
                    +"                  2. The warranty does not cover any failure of the product arising out of normal wear and tear or due to misuse, including\n "
                    +"                     but not limited to use in other than normal manner in accordance with the instructions  provided  during the\n"
                    +"                     handover\n"
                    +"                  3. This warranty does not cover any failure due to climate change, water-seepage, accident, modification, or adjustment,\n"
                    +"                     or acts of god or damage resulting from misuse. \n"
                    +"                  4. This warranty does not cover failure caused  by installation,  modification or repair or opening  of the product\n"
                    +"                     performed  by personnel other than that of MyGubbi. \n"
                    +"                  5. This warranty does not apply if products  have been placed  outdoors  or in a humid environment or if the products\n"
                    +"                     have been used for non-domestic purposes \n"
                    +"     EXCEPT TO THE EXTENT OF THE WARRANTY PROVIDED IN SECTION 11(b) and (d), MYGUBBI SPECIFICALLY DISCLAIMS ALL\n"
                    +"     WARRANTIES  AND INDEMNITIES,  EXPRESS, IMPLIED OR STATUTORY, INCLUDING WITHOUT LIMITATION ANY WARRANTY OF\n"
                    +"     MERCHANTABILITY,FITNESS FOR A PARTICULAR PURPOSE, OR ANY WARRANTY ARISING IN THE COURSE OF SERVICE OR\n"
                    +"     DEALING.\n"
                    +"12.  Cancellation: \n"
                    +"     (a.) Orders/works-contracts once signed are expected to be non-cancellable. \n"
                    +"     (b.) However,  if client cancels  his order for any reason whatsoever once the work has commenced, then the monies already\n"
                    +"          paid by the client shall stand forfeited and will not be refunded, and client shall be additionally liable to pay for\n"
                    +"          MyGubbi’s service charges (as determined by it),and for all costs borne for materials ordered under the relevant\n"
                    +"          work-contract.\n"
                    +"     (c.) If MyGubbi cancels this contract unilaterally with or without any reason,then MyGubbi’s liability shall be limited up to the\n"
                    +"           refund of client’s entire amount  actually  paid to MyGubbi.\n"
                    +"13.  Limitation of Liability: \n"
                    +"     (a.) IN NO EVENT WILL MYGUBBI OR ITS AFFILIATES, VENDORS OR SUBCONTRACTORS BE LIABLE TO THE CLIENT FOR ANY\n"
                    +"          INDIRECT,INCIDENTAL,SPECIAL OR CONSEQUENTIAL LOSSES HOWSOEVER ARISING WHETHER UNDER THIS CONTRACT,\n"
                    +"          TORT (INCLUDING NEGLIGENCE),BREACH OF STATUTORY DUTY OR OTHERWISE,  EVEN IF ADVISED OF THE POSSIBILITY\n"
                    +"          OF SUCH LOSSES.\n"
                    +"     (b.) AT ALL TIMES AND UNDER ALL CIRCUMSTANCES, MYGUBBI’S LIABILITY SHALL BE LIMITED UP TO THE REFUND OF\n"
                    +"          CLIENT’S ENTIRE AMOUNT ACTUALLY PAID TO MYGUBBI.\n"
                    +" \n"
                    +"We thank you for considering MyGubbi!\n"
                    +" \n"
                    +"Regards, Accepted by\n"
                    ,fsize);
            //);
            //document.add(table1);
            document.add(p);

            p = new Paragraph("      ");
            p.setAlignment(Element.ALIGN_LEFT);
            document.add(p);

            /*p = new Paragraph(new Paragraph("THANKS for considering Gubbi!                                                                                                                                                     " + "\t"  + "\t" + "\t" + "\t" + "\t" +"\tAccepted (Sign) ",fsize));
            document.add(p);*/

            document.close();
        }
        catch (Exception e)
        {

        }
    }
}
