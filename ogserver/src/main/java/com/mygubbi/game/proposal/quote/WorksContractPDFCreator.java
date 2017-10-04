package com.mygubbi.game.proposal.quote;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.mygubbi.config.ConfigHolder;
import com.mygubbi.game.proposal.model.ProposalHeader;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Shruthi on 9/25/2017.
 */

public class WorksContractPDFCreator
{
    private final static Logger LOG = LogManager.getLogger(WorksContractPDFCreator.class);
    Font fsize=new Font(Font.FontFamily.TIMES_ROMAN,8,Font.NORMAL);
    Font fsizebold=new Font(Font.FontFamily.TIMES_ROMAN,8,Font.BOLD);
    Font fsize3=new Font(Font.FontFamily.TIMES_ROMAN,9,Font.BOLD);
    QuoteData quoteData;
    private ProposalHeader proposalHeader;

    public WorksContractPDFCreator(QuoteData quoteData, ProposalHeader proposalHeader)
    {
        this.quoteData=quoteData;
        this.proposalHeader=proposalHeader;
    }
    public void createPdf(String destination)
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
            Phrase phrase;
            Date date=new Date();
            SimpleDateFormat formatter1 = new SimpleDateFormat("d");
            String date_name = formatter1.format(new Date());

            SimpleDateFormat simpleDateformat1 = new SimpleDateFormat("MMMM"); // the day of the week spelled out completely
            String month=simpleDateformat1.format(date);

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
            String year = formatter.format(new Date());

            String final_date=date_name + " " +month + " " +year;

            p=new Paragraph("WORKS CONTRACT\n",fsize3);
            p.setAlignment(Element.ALIGN_CENTER);
            fsize3.setColor(BaseColor.GRAY);
            document.add(p);

            phrase=new Phrase();
            phrase.add(new Chunk("This ",fsize));
            phrase.add(new Chunk("Works Contract ",fsizebold));
            phrase.add(new Chunk("(hereinafter referred to as the \"Contract\") is made on this ",fsize));
            phrase.add(new Chunk(final_date+ "\n",fsizebold));
            p=new Paragraph(phrase);
            document.add(p);

            p=new Paragraph("\nBY AND BETWEEN\n",fsize);
            p.setAlignment(Element.ALIGN_CENTER);
            document.add(p);

            if(proposalHeader.getProjectCity().equals("Chennai"))
            {
                phrase=new Phrase();
                phrase.add(new Chunk("Gubbi Technologies Pvt. Ltd., ",fsizebold));
                phrase.add(new Chunk("a company incorporated under the Companies Act 2013, having its registered office situated at ",fsize));
                phrase.add(new Chunk("Ramaniyam Ocean - Isha, No.11, Second Floor Rajiv Gandhi Salai, Old Mahabalipuram road, Okkiyam Thoraipakkam,Chennai 600 096 ",fsizebold));
                phrase.add(new Chunk("(hereinafter referred to as the\"MyGubbi\", which expression, where the context admits, shall include its successors and permitted assignees) OF THE ONE PART;\n",fsize));
                p=new Paragraph(phrase);
                document.add(p);

            }else if(proposalHeader.getProjectCity().equals("Pune"))
            {
                phrase=new Phrase();
                phrase.add(new Chunk("Gubbi Technologies Pvt. Ltd., ",fsizebold));
                phrase.add(new Chunk("a company incorporated under the Companies Act 2013, having its registered office situated at ",fsize));
                phrase.add(new Chunk("\"The Mint \" Building, Office No.101 ,2nd Floor Nr.Kapil Malhar Society, Baner Pune:411045 ",fsizebold));
                phrase.add(new Chunk("(hereinafter referred to as the \"MyGubbi\", which expression, where the context admits, shall include its successors and permitted assignees) OF THE ONE PART;\n",fsize));
                p=new Paragraph(phrase);
                document.add(p);

            }else if(proposalHeader.getProjectCity().equals("Mangalore"))
            {
                phrase=new Phrase();
                phrase.add(new Chunk("Gubbi Technologies Pvt. Ltd., ",fsizebold));
                phrase.add(new Chunk("a company incorporated under the Companies Act 2013, having its registered office situated at ",fsize));
                phrase.add(new Chunk("CRYSTAL ARC ( Building Name) Commercial shop premises No F11 & F 12 First Floor Door No 14-4-511/34 & 14-4-511/35 Balmatta Road,Hampankatta Mangalore Pincode :575001 ",fsizebold));
                phrase.add(new Chunk("(hereinafter referred to as the \"MyGubbi\", which expression, where the context admits, shall include its successors and permitted assignees) OF THE ONE PART;\n",fsize));
                p=new Paragraph(phrase);
                document.add(p);
            }
            else
            {
                phrase=new Phrase();
                phrase.add(new Chunk("Gubbi Technologies Pvt. Ltd., ",fsizebold));
                phrase.add(new Chunk("a company incorporated under the Companies Act 2013, having its registered office situated at ",fsize));
                phrase.add(new Chunk("1502, 1st Floor, 19th Main Road, Sector 1, HSR Layout, Bangalore 560 102 ",fsizebold));
                phrase.add(new Chunk("(hereinafter referred to as the \"MyGubbi\", which expression, where the context admits, shall include its successors and permitted assignees) OF THE ONE PART;\n",fsize));
                p=new Paragraph(phrase);
                document.add(p);
            }

            p=new Paragraph("AND\n",fsize);
            p.setAlignment(Element.ALIGN_CENTER);
            document.add(p);

            phrase=new Phrase();
            phrase.add(new Chunk(proposalHeader.getName() + ", "+proposalHeader.getProjectAddress1()+ ", " +proposalHeader.getProjectCity(),fsizebold));
            phrase.add(new Chunk(" (hereinafter referred to as the \"Client \", which expression, where the context admits, shall include [his/her] heirs, executors and administrators), of the OTHER PART.",fsize));
            p=new Paragraph(phrase);
            document.add(p);

            phrase=new Phrase();
            phrase.add(new Chunk("1. ENGAGEMENT\n",fsizebold));
            phrase.add(new Chunk("          (a. ) The Client hereby engages the MyGubbi to provide services described herein under \"Scope and Manner of Services \" enumerated in Clause 3.\n"
                    +"                The MyGubbi hereby agrees to provide the Client with such services in exchange of consideration described in Clause 2 of this Contract. \n"
                    +"          (b. ) Scope of work for the product and services being provided by the MyGubbi are mentioned in Final Quote and Final Design signed off by the client.\n"
                    +"          (c. ) Delivery and installation of the Services shall be made within the time period, at the location and in the quantity and quality as specified in Clause 3.\n"
                    +"          (d. ) The Client shall ensure that the site of installing the fittings/fixtures is prepared and ready as per specification informed by the MyGubbi.\n"
                    +"          (e. ) The MyGubbi shall not be liable for taking any necessary approvals from the authorities for any civil works extensions as may be required in the work\n"
                    +"                specification.\n",fsize));
            p=new Paragraph(phrase);
            document.add(p);

            phrase=new Phrase();
            phrase.add(new Chunk("2. PAYMENT FOR SERVICES RENDERED\n",fsizebold));
            phrase.add(new Chunk("          (a. ) The total price INR ",fsize));
            Double val = quoteData.getTotalCost() - quoteData.getDiscountAmount();
            Double res = val - val % 10;
            phrase.add(new Chunk(String.valueOf(res.intValue()),fsizebold));
            phrase.add(new Chunk("/- (As per Enclosed final design and quote) is inclusive of Applicable taxes, taxes are subject to change, due to Govt.\n"
                    +"                policies & changes in the rate if any, -taxes prevailing at the time of delivery will be charged if any at the time of submission of bills.\n"
                    +"          (b. ) The Client shall pay 50% in advance to procure and start the manufacturing process. The remaining 50% will be expected once items are completely\n"
                    +"                manufactured and ready for shipment to client\'s site.\n"
                    +"          (c. ) Payments can be made through cheque/DD/Bank Transfer (NEFT/RTGS) are the preferred modes, Credit Card payments will attract an additional\n"
                    +"                transaction fee of 2%.\n",fsize));
            p=new Paragraph(phrase);
            document.add(p);

            phrase=new Phrase(new Chunk("3. SCOPE AND MANNER OF SERVICES\n",fsizebold));
            document.add(phrase);
            p=new Paragraph("     Service Provider has been engaged to provide the following deliverables under this contract \n"
                    + "         (a. ) Advice and designing services for Home interior.\n"
                    + "         (b. ) Manufacture and installation of the product as per agreed Final design.\n",fsize);
            document.add(p);

            p=new Paragraph("4. DELIVERY AND INSTALLATION\n",fsizebold);
            document.add(p);

            p=new Paragraph("     The MyGubbi shall deliver the Services within Sixty days (60) Working days subject to the following:\n"
                    + "         (a. ) The Final design, specifications and Quote of the Services has been duly signed by both parties\n"
                    + "         (b. ) The requisite advance for the services has been paid by the Client, and \n"
                    + "         (c. ) There have been no changes to the scope of work pursuant to the design and specifications of the Services having been duly signed off by both parties.\n"
                    + "         (d. ) Readiness of Client’s site in all aspects to do installation of product as per final design.\n",fsize);
            document.add(p);

                    phrase=new Phrase(new Chunk("5. WARRANTY (MOST IMPORTANT TERMS)\n",fsizebold));
                    p=new Paragraph(phrase);
                    document.add(p);

                    p=new Paragraph("         (a. ) Exactness with respect to the appearance of the final installed product compared with any previous diagrams, drawings, laminate colour samples,\n " +
                    "                 computer or 3-D rendition, et cetera is not guaranteed.\n"
                    + "         (b. ) The products delivered under the Service carry a limited warranty of 05 years for its \'joinery\', effective from the date of invoice.\n"
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
                    + "                            for non-domestic purposes.\n",fsize);
                    document.add(p);

                    p=new Paragraph("EXCEPT TO THE EXTENT OF THE WARRANTY PROVIDED IN SECTION 3(b) and (d), MYGUBBI SPECIFICALLY DISCLAIMS ALL WARRANTIES AND INDEMNITIES, EXPRESS, IMPLIED OR STATUORY, INCLUDING WITHOUT LIMITATION ANY WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR ANY WARRANTY ARISING IN THE COURSE OF SERVICE OR DEALING.\n ",fsizebold);
                    document.add(p);

                    p=new Paragraph("6. CANCELLATION\n",fsizebold);
                    document.add(p);

                    p=new Paragraph("         (a. ) Orders/works-contracts once signed are expected to be non-cancellable.\n"
                    + "         (b. ) However, if client cancels his order for any reason whatsoever once the work has commenced, then the monies already paid by the client shall stand\n "
                    + "               forfeited and will not be refunded, and client shall be additionally liable to pay for MyGubbi\'s service charges (as determined by it), and\n"
                    + "                for all costs borne for materials ordered under the relevant work-contract. \n"
                    + "         (c. ) If MyGubbi cancels this contract unilaterally with or without any reason, then MyGubbi\'s liability shall be limited up to the refund of client\'s\n"
                    +"                entire amount actually paid to MyGubbi.\n",fsize);
                    document.add(p);

                    p=new Paragraph("7. RISK\n",fsizebold);
                    document.add(p);
                    p=new Paragraph("         (a. ) In the event that the goods delivered to the installation site are handled by people outside MyGubbi\'s authorized personnel or in the event that the\n"
                    + "               goods delivered to the installation site are mishandled by site personnel outside authorized MyGubbi\'s personnel or the site is not secure or the duration\n"
                    + "               between delivery and installation surpasses 05 working days due to reasons not attributable to MyGubbi, risk of loss of the products in such cases\n"
                    + "               shall pass to customer.\n"
                    + "         (b. ) If MyGubbi is unable to complete installation of the Services with the time period mentioned under Clause 3, a grace period of 20 (twenty) Working\n"
                    + "               days shall be available with MyGubbi beyond the committed time period to complete the installation. Intimation for any delay in installation of\n"
                    + "               Services within the time period shall be communicated to the Clients during the tenure of their engagement with MyGubbi.\n",fsize);
                    document.add(p);

                    p=new Paragraph("8. LIMITATION OF LIABILITY\n",fsizebold);
                    document.add(p);

                    p=new Paragraph("         (a. ) IN NO EVENT WILL MYGUBBI OR ITS AFFILIATES, VENDORS OR SUBCONTRACTORS BE LIABLE TO THE CLIENT FOR ANY\n"
                    + "               INDIRECT, INCIDENTAL, SPECIAL OR CONSEQUENTIAL LOSSES HOWSOEVER ARISING WHETHER UNDER THIS CONTRACT,\n"
                    + "               TORT (INCLUDING NEGLIGENCE), BREACH OF STATUTORY DUTY OR OTHERWISE, EVEN IF ADVISED OF THE POSSIBILITY\n"
                    + "               OF SUCH LOSSES.\n"
                    + "         (b. ) AT ALL TIMES AND UNDER ALL CIRCUMSTANCES, MYGUBBI\'S LIABILITY SHALL BE LIMITED UP TO THE REFUND OF\n"
                    + "               CLIENT\'S ENTIRE AMOUNT ACTUALLY PAID TO MYGUBBI\n",fsize);
                    document.add(p);

                    p=new Paragraph( "9. FORCE MAJEURE\n",fsizebold);
                    document.add(p);
                    p=new Paragraph("         (a. ) MyGubbi shall not be held in breach or liable due to any delay caused by break down or damage of MyGubbi\'s machinery, shortages or non-\n"
                    + "               availability of raw materials, components or consumables etc., strikes, lock-out, war, riot, civil commotion, commercial disturbances or acts of God or other\n"
                    + "               causes beyond the control of the MyGubbi.\n"
                    +"\n",fsize);
                    document.add(p);

                    p=new Paragraph("10. INTELLECTUAL PROPERTY RIGHTS\n",fsizebold);
                    document.add(p);

                    p=new Paragraph("         (a. ) The designs, products manufactured and the services provided are purely for the personal use of Client and any reproduction of said designs/services\n"
                    + "               for commercial purposes shall be deemed to be infringement of the intellectual property rights of MyGubbi.\n",fsize);
                    document.add(p);

                    phrase=new Phrase();
                    phrase.add(new Chunk("11. GOVERNING LAW AND JURISDICTION\n",fsizebold));
                    phrase.add(new Chunk("         (a. ) This Contract and any matters arising out of or in connection with it shall be governed by, and shall be construed in accordance with, the laws of India.\n",fsize));
                    phrase.add(new Chunk("               The Courts at Bangalore, India shall have exclusive jurisdiction\n ",fsize));
                    phrase.add(new Chunk("In witness of their agreement to the terms above, the parties or their authorized agents hereby affix their signatures:\n",fsizebold));
                    p=new Paragraph(phrase);
                    document.add(p);

            p=new Paragraph("\n");
            document.add(p);

            float[] columnWidths2 = {1,1};
            PdfPTable table = new PdfPTable(columnWidths2);
            table.setWidthPercentage(100);

            phrase = new Phrase();
            phrase.add(new Chunk("For and on behalf of ",fsize));
            phrase.add(new Chunk("Gubbi Technologies Pvt. Ltd\n",fsizebold));
            phrase.add(new Chunk("\n"));
            phrase.add(new Chunk("\n"));
            phrase.add(new Chunk("\n"));

            Phrase phrase1 = new Phrase();
            phrase1.add(new Chunk("By Client \n ",fsizebold));
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
            e.printStackTrace();
        }
    }
}
