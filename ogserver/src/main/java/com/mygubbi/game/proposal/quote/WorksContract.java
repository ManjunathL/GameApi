package com.mygubbi.game.proposal.quote;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileOutputStream;

/**
 * Created by Shruthi on 9/22/2017.
 */
class WorksContractBorder extends PdfPageEventHelper {
    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte canvas = writer.getDirectContent();
        Rectangle rect = new Rectangle(28, 28, 580, 830);
        rect.setBorder(Rectangle.BOX);
        rect.setBorderWidth(2);
        canvas.rectangle(rect);
    }
}

public class WorksContract
{
    private final static Logger LOG = LogManager.getLogger(WorksContract.class);
    com.itextpdf.text.Font fsize=new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN,8, com.itextpdf.text.Font.NORMAL);
    com.itextpdf.text.Font fsizebold=new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN,8, Font.BOLD);
    com.itextpdf.text.Font headingSize=new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN,15, com.itextpdf.text.Font.BOLD);
    public static void main(String args[])
    {
        WorksContract worksContract=new WorksContract();
        worksContract.createBookingForm();
    }

    public void createBookingForm() {
        String destination = "E:/Worscontract.pdf";
        Paragraph p;
        try {
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(destination));
            writer.setPdfVersion(PdfWriter.VERSION_1_7);
            writer.createXmpMetadata();
            document.open();
            writer.setPageEvent(new WorksContractBorder());

            p=new Paragraph("3. SCOPE AND MANNER OF SERVICES\n"
                    + "     Service Provider has been engaged to provide the following deliverables under this contract \n"
                    + "         (a. ) Advice and designing services for Home interior.\n"
                    + "         (b. ) Manufacture and installation of the product as per agreed Final design.\n"
                    + "4. DELIVERY AND INSTALLATION\n"
                    + "     The MyGubbi shall deliver the Services within Sixty days (60) Working days subject to the following:\n"
                    + "         (a. ) The Final design, specifications and Quote of the Services has been duly signed by both parties\n"
                    + "         (b. ) The requisite advance for the services has been paid by the Client, and \n"
                    + "         (c. ) There have been no changes to the scope of work pursuant to the design and specifications of the Services having been duly signed off by both parties.\n"
                    + "         (d. ) Readiness of Client’s site in all aspects to do installation of product as per final design.\n"
                    + "5. WARRANTY (MOST IMPORTANT TERMS)\n"
                    + "         (a. ) Exactness with respect to the appearance of the final installed product compared with any previous diagrams, drawings, laminate colour samples,\n " +
                    "                 computer or 3-D rendition, et cetera is not guaranteed.\n"
                    + "         (b. ) The products delivered under the Service carry a limited warranty of 05 years for its 'joinery’, effective from the date of invoice.\n"
                    + "         (c. ) The warranty for all third-party fittings like water taps, kitchen-sink, white goods and electrical/electronic appliances, etc. procured for and on behalf of\n " +
                    "                 the Client shall be applicable as per the norms specified by the particular brand/manufacturer. Please note that all such appliances shall be procured on\n" +
                    "                 your behalf and in your name and you shall alone be the sole owner thereof.\n"
                    + "         (d. ) If during the 05 years’ warranty period the delivered product fail to operate due to defect in manufacturing, MyGubbi at its option, will either repair\n " +
                    "                 or replace the product or part therein in accordance with the terms and conditions below.\n"
                    + "                 (i. ) All the accessories which are procured by MyGubbi carry a lifetime warranty (on a back to back basis, as assured by the\n " +
                    "                         respective reputed accessory manufacturer).\n"
                    + "                 (ii.) MyGubbi reserves the right to charge a handling fee and/or servicing fee if a returned product is found to be not under warranty\n " +
                    "                         as per the conditions below:\n "
                    + "                       (1. )Upon a product being identified by a customer as failing to operate under normal use and service, the customer will inform MyGubbi citing\n " +
                    "                              the failure of the condition.\n"
                    + "                       (2. )The warranty does not cover any failure of the product arising out of normal wear and tear or due to misuse, including but not limited to use in\n " +
                    "                              other than normal manner in accordance with the instructions provided during the handover.\n "
                    + "                       (3. )This warranty does not cover any failure due to climate change, water-seepage, accident, modification, or adjustment, or acts of god or damage\n" +
                    "                              resulting from misuse\n"
                    + "                       (4. )This warranty does not cover failure caused by installation, modification or repair or opening of the product performed by personnel other\n" +
                    "                              than that of MyGubbi.\n"
                    + "                       (5. )This warranty does not apply if products have been placed outdoors or in a humid environment or if the products have been used\n "
                    + "                            for non-domestic purposes.\n"
                    + "EXCEPT TO THE EXTENT OF THE WARRANTY PROVIDED IN SECTION 3(b) and (d), MYGUBBI SPECIFICALLY DISCLAIMS ALL WARRANTIES AND INDEMNITIES, EXPRESS, IMPLIED OR STATUORY, INCLUDING WITHOUT LIMITATION ANY WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR ANY WARRANTY ARISING IN THE COURSE OF SERVICE OR DEALING.\n "
                    + "6. CANCELLATION\n"
                    + "         (a. ) Orders/works-contracts once signed are expected to be non-cancellable.\n"
                    + "         (b. ) However, if client cancels his order for any reason whatsoever once the work has commenced, then the monies already paid by the client shall stand\n "
                    + "               forfeited and will not be refunded, and client shall be additionally liable to pay for MyGubbi’s service charges (as determined by it), and\n"
                    + "                for all costs borne for materials ordered under the relevant work-contract. \n"
                    + "         (c. ) If MyGubbi cancels this contract unilaterally with or without any reason, then MyGubbi’s liability shall be limited up to the refund of client’s\n"
                    +"                entire amount actually paid to MyGubbi.\n"
                    + "7. RISK\n"
                    + "         (a. ) In the event that the goods delivered to the installation site are handled by people outside MyGubbi’s authorized personnel or in the event that the goods\n"
                    + "               delivered to the installation site are mishandled by site personnel outside authorized MyGubbi’s personnel or the site is not secure or the duration\n"
                    + "               between delivery and installation surpasses 05 working days due to reasons not attributable to MyGubbi, risk of loss of the products in such cases\n"
                    + "               shall pass to customer.\n"
                    + "         (b. ) If MyGubbi is unable to complete installation of the Services with the time period mentioned under Clause 3, a grace period of 20 (twenty) Working\n"
                    + "               days shall be available with MyGubbi beyond the committed time period to complete the installation. Intimation for any delay in installation of\n"
                    + "               Services within the time period shall be communicated to the Clients during the tenure of their engagement with MyGubbi.\n"
                    + "8. LIMITATION OF LIABILITY\n"
                    + "         (a. ) IN NO EVENT WILL MYGUBBI OR ITS AFFILIATES, VENDORS OR SUBCONTRACTORS BE LIABLE TO THE CLIENT FOR ANY\n"
                    + "               INDIRECT, INCIDENTAL, SPECIAL OR CONSEQUENTIAL LOSSES HOWSOEVER ARISING WHETHER UNDER THIS CONTRACT,\n"
                    + "               TORT (INCLUDING NEGLIGENCE), BREACH OF STATUTORY DUTY OR OTHERWISE, EVEN IF ADVISED OF THE POSSIBILITY\n"
                    + "               OF SUCH LOSSES.\n"
                    + "         (b. ) AT ALL TIMES AND UNDER ALL CIRCUMSTANCES, MYGUBBI’S LIABILITY SHALL BE LIMITED UP TO THE REFUND OF\n"
                    + "               CLIENT’S ENTIRE AMOUNT ACTUALLY PAID TO MYGUBBI\n"
                    + "9. FORCE MAJEURE\n"
                    + "         (a. ) MyGubbi shall not be held in breach or liable due to any delay caused by break down or damage of MyGubbi’s machinery, shortages or non-availability\n"
                    + "               of raw materials, components or consumables etc., strikes, lock-out, war, riot, civil commotion, commercial disturbances or acts of God or other\n"
                    + "               causes beyond the control of the MyGubbi.\n"
                    +"\n"
                    + "10. INTELLECTUAL PROPERTY RIGHTS\n"
                    + "         (a. ) The designs, products manufactured and the services provided are purely for the personal use of Client and any reproduction of said designs/services\n"
                    + "               for commercial purposes shall be deemed to be infringement of the intellectual property rights of MyGubbi.\n"
                    + "11. GOVERNING LAW AND JURISDICTION\n"
                    + "         (a. ) This Contract and any matters arising out of or in connection with it shall be governed by, and shall be construed in accordance with, the laws of India.\n"
                    + "               The Courts at Bangalore, India shall have exclusive jurisdiction\n"
                    + "In witness of their agreement to the terms above, the parties or their authorized agents hereby affix their signatures:\n"

            ,fsize);
            document.add(p);

            p=new Paragraph("\n");
            document.add(p);

            float[] columnWidths2 = {1,1};
            PdfPTable table = new PdfPTable(columnWidths2);
            table.setWidthPercentage(100);

            Phrase phrase = new Phrase();
            phrase.add(new Chunk("For and on behalf of ",fsize));
            phrase.add(new Chunk("Gubbi Technologies Pvt. Ltd\n",fsizebold));
            phrase.add(new Chunk("\n"));
            phrase.add(new Chunk("\n"));
            phrase.add(new Chunk("\n"));

            Phrase phrase1 = new Phrase();
            phrase1.add(new Chunk("By Client \n ",fsize));
            phrase1.add(new Chunk("\n"));
            phrase1.add(new Chunk("\n"));
            phrase1.add(new Chunk("\n"));
            phrase1.add(new Chunk("\n"));

            table.addCell(phrase);
            table.addCell(phrase1);
            document.add(table);
            document.close();

        }
        catch (Exception e)
        {


        }

    }
}
