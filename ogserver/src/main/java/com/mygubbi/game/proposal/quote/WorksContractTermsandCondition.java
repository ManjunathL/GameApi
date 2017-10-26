package com.mygubbi.game.proposal.quote;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.mygubbi.game.proposal.model.ProposalHeader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileOutputStream;

/**
 * Created by Shruthi on 10/25/2017.
 */
public class WorksContractTermsandCondition
{

    private final static Logger LOG = LogManager.getLogger(WorksContractTermsandCondition.class);
    Font fsize=new Font(Font.FontFamily.TIMES_ROMAN,10,Font.NORMAL);
    Font fsizebold=new Font(Font.FontFamily.TIMES_ROMAN,9,Font.BOLD);
    Font fsize3=new Font(Font.FontFamily.TIMES_ROMAN,9,Font.BOLD);
    QuoteData quoteData;
    private ProposalHeader proposalHeader;

    public WorksContractTermsandCondition()
    {

    }

    public static void main(String args[])
    {
        WorksContractTermsandCondition worksContractTermsandCondition=new WorksContractTermsandCondition();
        worksContractTermsandCondition.createPdf("E:/WorkContract.pdf");
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
            //writer.setPageEvent(new CustomBorder());
            Paragraph p;
            Phrase phrase;

            p=new Paragraph("TERMS OF ORDER CONFIRMATION",fsize3);
            p.setAlignment(Element.ALIGN_CENTER);
            document.add(p);

            p=new Paragraph(" ");
            document.add(p);

            p=new Paragraph("This TERMS OF ORDER CONFIRMATION (hereinafter referred to as “Confirmation”) is made between M/s Gubbi Technologies Pvt. Ltd. (hereinafter referred to as “MyGubbi”, which expression, shall wherever the context permits, be deemed to include its successors and permitted assigns) and You (hereinafter referred to as the “Client”)  MyGubbi and Client are collectively referred to as the “Parties”. For the purpose of this Confirmation, “Effective Date” shall mean the date on which this Confirmation is signed by the Client.",fsize);
            document.add(p);

            p=new Paragraph("By signing this Confirmation, the Client expressly agrees to and consents to be bound by all of the terms of this Confirmation. This Confirmation governs the terms of service between MyGubbi and the Client.",fsize);
            document.add(p);

            p=new Paragraph(" ");
            document.add(p);

            p=new Paragraph("WHEREAS:",fsize3);
            document.add(p);

            p=new Paragraph("a) MyGubbi is a consumer tech company offering interior design solutions and home merchandise across various categories for\n"
                            +"    decorating homes and ensuring premium quality finished interiors;  ",fsize);
            document.add(p);

            p=new Paragraph("b) The Client is desirous of engaging the services of MyGubbi for his/her home (hereinafter referred to as “Site”)which are\n"
                            +"    morefully described in Annexure – A hereto (hereinafter referred to as “Works”) ",fsize);
            document.add(p);

            p=new Paragraph("c) MyGubbi hereby agrees to carry out such Works in consideration for the payment mutually agreed to by the Parties and set out\n"
                            +"    in Clause 2.1 of this Confirmation; and",fsize);
            document.add(p);

            p=new Paragraph("d) The Parties are entering into this Confirmation to set out the mutual rights and obligations governing the scope of Works under\n"
                            +"    this Agreement.",fsize);
            document.add(p);

            p=new Paragraph(" ");
            document.add(p);

            p=new Paragraph("NOW THEREFORE THIS CONFIRMATION WITNESSETH AS FOLLOWS:",fsizebold);
            document.add(p);

            p=new Paragraph(" ");
            document.add(p);

            p=new Paragraph("1. APPOINTMENT:",fsizebold);
            document.add(p);

            p=new Paragraph("1.1. The Client hereby appoints and MyGubbi hereby accepts such appointment and undertakes to provide and execute the Works.",fsize);
            document.add(p);

            p=new Paragraph("1.2. The designs, price quotations and specifications for the Works mutually discussed and finalized between the Parties earlier\n"
                    +"    are contained in Annexure – A to this Confirmation. ",fsize);
            document.add(p);

            p=new Paragraph(" ");
            document.add(p);

            p=new Paragraph("2. PAYMENT FOR EXECUTION OF WORKS:",fsizebold);
            document.add(p);

            p=new Paragraph("2.1. In consideration for the Works to be executed by MyGubbi, the Client hereby agrees to pay MyGubbi a total sum\n"
                    +"    as specified in Annexure ___ (“Fees”).",fsize);
            document.add(p);

            p=new Paragraph("2.2. The Fees quoted herein shall exclude all applicable taxes. Taxes on each independent service shall be levied as specified\n"
                    +"    in Annexure ___. However, any increase in the prevailing GST rates in connection with the execution oof Works under this\n"
                    +"    Confirmation shall be borne and paid for by the Client.",fsize);
            document.add(p);

            p=new Paragraph("2.3. The Client shall pay 50% (fifty percent) of the Fees in advance, on the Effective Date (“Advance Fee Date”).",fsize);
            document.add(p);

            p=new Paragraph("2.4. With respect to civil works carried out by MyGubbi to complement the Works under the Confirmation, the cost estimate for\n"
                    +"    such civil works included in the Fees is only a rough estimate. Actual cost may vary depending on the nature and magnitude\n"
                    +"    of civil works completed.  ",fsize);
            document.add(p);

            p=new Paragraph("2.5. Upon the Client and MyGubbi agreeing to the designs, specifications and other details in respect of the Works to be executed\n"
                    +"    on the Site and signed off by both parties (“Design Finalization Date”), MyGubbi shall procure all necessary materials required,\n"
                    +"    including but not limited to wood, laminates, hardware, glass/mirrors, appliances, sanitary fittings, civil materials and such\n"
                    +"    other items to be installed at the Site within the period specified in Annexure __. ",fsize);
            document.add(p);

            p=new Paragraph("2.6. Upon procurement of the items, MyGubbi shall intimate to the Client confirming that the items are procured and ready\n"
                    +"    for shipment to the Site (“Intimation”). The remaining 50% of the Fees shall be paid within Three (03) days from the date\n"
                    +"    of the Intimation (“Final Payment Date”). MyGubbi shall not be liable or responsible to deliver or install the items for\n"
                    +"    execution of the Works unless and until it has received the full and final payment of the Fees. ",fsize);
            document.add(p);

            p=new Paragraph("2.7. Payment of Fees to MyGubbi shall be made either through cheque, Demand Draft, Bank Transfer (NEFT/RTGS) or credit\n"
                    +"    card. Credit Card payments shall however attract an additional transaction fee of (2%).  ",fsize);
            document.add(p);

            p=new Paragraph(" ");
            document.add(p);

            p=new Paragraph(" ");
            document.add(p);

            p=new Paragraph("3. DELIVERY AND INSTALLATION",fsizebold);
            document.add(p);

            p=new Paragraph("3.1. MyGubbi shall complete the Works within sixty (60) days (“Term”) from the Design Finalization Date subject\n"
                    +"    to the following :",fsize);
            document.add(p);

            p=new Paragraph("i) Receipt of 50% of the Fees by MyGubbi on the Advance Fee Date;",fsize);
            document.add(p);

            p=new Paragraph("ii) Receipt of the remaining fifty percent (50%) of the Fees by MyGubbi on or before the Final Payment Date;",fsize);
            document.add(p);

            p=new Paragraph("iii) There having been no deviations or modifications to the scope of Works including any designs, specifications\n"
                    +"    and handover of the Site within the time stipulated.",fsize);
            document.add(p);

            p=new Paragraph("iv) Readiness of the Site for the installation of the items, as required by MyGubbi and subsequent handover of the same as\n"
                    +"    provided in this Confirmation to enable  commencement delivery and of the Works; ",fsize);
            document.add(p);

            p=new Paragraph("v) There having been no hindrance, suspension or interruption caused by the Client in respect of the Site or in otherwise\n"
                    +"    preventing MyGubbi from completing the Works within the Term; and ",fsize);
            document.add(p);

            p=new Paragraph("vi) There having been no Force Majeure Event (described hereinafter) during the Term.",fsize);
            document.add(p);

            p=new Paragraph(" ");
            document.add(p);

            p=new Paragraph("4. STORAGE",fsizebold);
            document.add(p);

            p=new Paragraph("4.1 In the event MyGubbi is ready to install the items but the Site is not ready for installation,\n"
                    +"    all items will be stored in the transit warehouse of MyGubbi, designed for short term storage.",fsize);
            document.add(p);

            p=new Paragraph("4.2 The storage facility shall be free of charge for first two weeks. Thereafter,\n"
                    +"    INR 6500/- (Six Thousand Five Hundred Rupees Only) shall be levied per kitchen per month and\n"
                    +"    INR 3,250/- (Three Thousand Two Hundred And Fifty Rupees Only) per bedroom per month",fsize);
            document.add(p);

            p=new Paragraph("4.3 MyGubbi shall have no liability for damage or loss of items stored in the transit warehouse\n"
                    +"    caused by heat, cold, theft, vandalism, fire, water, winds, dust, rain, explosion, rodents, insects or\n"
                    +"    any other cause whatsoever. MyGubbi carries no insurance covering damage to or loss of such items.",fsize);

            document.add(p);

            p=new Paragraph("4.4 The maximum period of storage shall not exceed three months, after which the Client shall be liable\n"
                    +"    to pay to MyGubbi the entire sum of money due under this Confirmation, including but not limited to the remaining 50%\n"
                    +"    of the Fees and all charges levied for storage of such items at the transit warehouse including labor charges and expenses\n"
                    +"    incurred in the sale or other disposition of such items.",fsize);
            document.add(p);

            p=new Paragraph(" ");
            document.add(p);

            p=new Paragraph("5. SUSPENSION OF WORK ",fsizebold);
            document.add(p);

            p=new Paragraph("5.1. On the occurrence of any of the events under Clause 3.1. above:",fsize);
            document.add(p);

            p=new Paragraph("5.1.1. The Term of the Confirmation shall be proportionally increased by the number of days such delay is caused thereto",fsize);
            document.add(p);

            p=new Paragraph("5.1.2. The Client shall be liable to compensate MyGubbi for any increase in costs and expenses incurred by MyGubbi,\n"
                    +"    as determined by MyGubbi. ",fsize);
            document.add(p);

            p=new Paragraph("5.1.3. Any portion of the Works completed by MyGubbi as on the date of such suspension, delay or interruption shall be\n"
                    +"    deemed to have been complete as per the terms of this Confirmation. ",fsize);
            document.add(p);

            p=new Paragraph("5.1.4. MyGubbi shall not be responsible for any damage or loss to the Works during such suspension period.",fsize);
            document.add(p);

            p=new Paragraph(" ");
            document.add(p);

            p=new Paragraph("6. HANDOVER OF SITE:",fsizebold);
            document.add(p);

            p=new Paragraph("6.1. Upon Intimation from MyGubbi, the Client shall immediately and in no event later than three (3) days from the\n"+
                    "    date on Intimation, handover the Site or the portion of the Site requiring installation of items, as the case may be, to MyGubbi.",fsize);
            document.add(p);

            p=new Paragraph("6.2. At the time of handover, the Site or the portion of the Site, as the case may be, requiring installation of Works shall be kept\n"
                    +"    completely vacant and shall not contain any other materials as may be directed by MyGubbi. ",fsize);
            document.add(p);

            p=new Paragraph("6.3. MyGubbi shall not be liable/responsible for any moveable or other items that may be left behind or found at the Site at the\n"
                    +"    time of handover of Site to MyGubbi for installation of  Works.",fsize);
            document.add(p);

            p=new Paragraph("6.4. The Client shall ensure that there is no interference from anybody in any manner whatsoever during the time MyGubbi is\n"
                    +"    carrying out installation of the items. ",fsize);
            document.add(p);

            p=new Paragraph("6.5. If MyGubbi is unable to complete the Works within the time period specified in this Confirmation for any reason whatsoever,\n"
                    +"    MyGubbi shall, subject to fulfillment of the provisions of Clause 4, be given a grace period of thirty (30) days to complete the\n"
                    +"    Works.",fsize);
            document.add(p);

            p=new Paragraph("6.6. Any complaints in respect of the Works, services or the products can be sent to MyGubbi at escalations@mygubbi.com.",fsize);
            document.add(p);

            p=new Paragraph(" ");
            document.add(p);

            p=new Paragraph("7. WARRANTY",fsizebold);
            document.add(p);

            p=new Paragraph("7.1. MyGubbi hereby represents and warrants that: ",fsize);
            document.add(p);

            p=new Paragraph("7.1.1. The Works shall be executed in a manner consistent with the terms of the Confirmation, and in accordance with all\n"
                    +"    specifications, drawings and standards as specified in the Confirmation. ",fsize);
            document.add(p);

            p=new Paragraph("7.1.2. The Works carry a limited warranty of five (05) years (“Warranty Period”) for its modular furniture alone, excluding any\n"
                    +"    fittings, accessories and loose furniture, effective from the date of completion of Works by MyGubbi.",fsize);
            document.add(p);

            p=new Paragraph("7.1.3. The warranty for all third-party fittings, including but not limited to as water taps, kitchen-sink, electrical/electronic\n"
                    +"    appliances procured for and on behalf of the Client shall be as specified by that particular manufacturer or supplier. All such\n"
                    +"    appliances and fittings procured by MyGubbi on behalf of the Client shall be in the name of the Client and the Client shall\n"
                    +"    alone be the sole owner thereof.MyGubbi shall handover all warranty cards and other related documents of such appliances to\n"
                    +"    the Client at the time of Handover.",fsize);
            document.add(p);

            p=new Paragraph("7.1.4. If during the Warranty Period, any component of the Works, excluding third-party fittings, fails to function or operate due\n"
                    +"    to a defect in manufacturing, MyGubbi at its option, shall either repair or replace the entire component or any part thereof\n"
                    +"     or rectify the defect in installation, as the case may be. ",fsize);
            document.add(p);

            p=new Paragraph("7.1.5. MyGubbi does not guarantee exactness with respect to the appearance of the final installed Works compared with any\n"
                    +"    previous diagrams, drawings, laminate colour samples, computer or 3-D renditions. ",fsize);
            document.add(p);

            p=new Paragraph("7.1.6. The warranty does not cover any failure, damage or deterioration of a product or part thereof arising out of normal wear and\n"
                    +"    tear or due to misuse, including but not limited to placing the products outdoors, use for non-domestic purposes or. due to usage\n"
                    +"    of the product beyond its intended use.",fsize);
            document.add(p);

            p=new Paragraph("7.1.7. The warranty does not cover any failure due to climate change, water-seepage, accidents, modification or adjustment to the\n"
                    +"    installation, acts of God or damage resulting from misuse.",fsize);
            document.add(p);

            p=new Paragraph("7.1.8. The warranty will cease to apply upon any interference caused to the products or joineries at any time by personnel other\n"
                    +"    than those authorized by MyGubbi.",fsize);
            document.add(p);

            p=new Paragraph("7.1.9. Except to the extent of Warranty specifically provided herein, MyGubbi disclaims all warranties and indemnities, express\n"
                    +"    or implied. ",fsize);
            document.add(p);

            p=new Paragraph(" ");
            document.add(p);

            p=new Paragraph("8. PROCEDURE FOR NOTIFYING DEFECTS:",fsizebold);
            document.add(p);

            p=new Paragraph("8.1. Upon any item or workmanship being identified by the Client as defective under normal use, the Client shall inform\n"
                    +"    MyGubbi of the same in writing.",fsize);
            document.add(p);

            p=new Paragraph("8.2. MyGubbi shall, at its option, either repair or replace such item or part thereof or rectify the defect in installation as the case\n"
                    +"    may be, if MyGubbi, at its sole discretion, deems that the issue falls within the warranty provided under this Confirmation. ",fsize);
            document.add(p);

            p=new Paragraph("8.3. MyGubbi reserves the right to charge a handling fee and/or servicing fee at its discretion, which shall be payable by the Client\n"
                    +"    immediately if, upon examination, MyGubbi finds the issue as one not falling within the warranty. ",fsize);
            document.add(p);

            p=new Paragraph(" ");
            document.add(p);

            p=new Paragraph("9. TERMINATION",fsizebold);
            document.add(p);

            p=new Paragraph("9.1. MyGubbi shall be entitled to terminate this Confirmation without reason at any time and upon such termination, MyGubbi’s\n"
                    +"    liability shall be limited to refund the portion of the Fees paid by the Client. ",fsize);
            document.add(p);

            p=new Paragraph("9.2. The Client shall be entitled to terminate this Confirmation with thirty (30) days advance notice in writing to MyGubbi\n"
                    +"    and upon acceptance of MyGubbi to such termination notice, all or any portion of Fees paid by the Client shall stand forfeited\n"
                    +"    by MyGubbi along with all additional costs and expenses borne by MyGubbi in the execution of this Confirmation.",fsize);
            document.add(p);

            p=new Paragraph("9.3. If for any reason MyGubbi is denied access to the Site, MyGubbi shall at its sole discretion be entitled to terminate the\n"
                    +"    Confirmation under Clause 8.1 and the portion of the Fees paid by the Client shall stand forfeited by MyGubbi along with all\n"
                    +"    additional costs and expenses borne by MyGubbi towards completion of the Works including costs of materials. ",fsize);
            document.add(p);

            p=new Paragraph("9.4. At any time during the pendency of the Works, MyGubbi reserves the right to identify specific Works to be removed\n"
                    +"    (De-Scoped) from the Confirmation for any reasons beyond the reasonable control of MyGubbi. To the extent that MyGubbi\n"
                    +"    De-Scopes works from this Confirmation, only such De-Scoped work shall be treated as being outside the scope of this\n"
                    +"    Confirmation and Fee charged for such De-scoped work shall be reimbursed to the Client or shall be adjusted with the final\n"
                    +"    payment.",fsize);
            document.add(p);

            p=new Paragraph("9.5. The Client shall be responsible for determining the suitability of the goods for the intended purpose. De-scoping\n"
                    +"    from the Confirmation by the Client may be accepted if made in writing not more than five (05) days from the Fee Date.\n"
                    +"    Fee charged for such De-scoped work shall be reimbursed to the Client or shall be adjusted with the final payment.",fsize);
            document.add(p);

            p=new Paragraph(" ");
            document.add(p);

            p=new Paragraph("10. RISK",fsizebold);
            document.add(p);

            p=new Paragraph("In the event that the items delivered to the Site are thereafter handled by any person, not being an authorized personnel of MyGubbi, MyGubbi shall not be liable or responsible for any damage or loss of the items in any manner.",fsize);
            document.add(p);

            p=new Paragraph(" ");
            document.add(p);

            p=new Paragraph("11. LIMITATION OF LIABILITY",fsizebold);
            document.add(p);

            p=new Paragraph("11.1. To the fullest extent permitted by law, the entire and total liability of MyGubbi, its officers, directors, partners, employees,\n"
                    +"    agents, representatives and subcontractors for any claims, losses, costs, or damages whatsoever arising out of, resulting from or\n"
                    +"    in any way related to this Confirmation from any cause or causes, including but not limited to negligence, professional errors,\n"
                    +"    omissions, strict liability, tort, breach of Confirmation, or breach of warranty, shall, under no circumstance exceed the amount\n"
                    +"    paid by the Client to MyGubbi under this Confirmation.",fsize);
            document.add(p);

            p=new Paragraph("11.2. MyGubbi shall in no event be liable for any indirect or consequential losses or damages of any kind and in any manner.",fsize);
            document.add(p);

            p=new Paragraph(" ");
            document.add(p);


            p=new Paragraph("12. FORCE MAJEURE",fsizebold);
            document.add(p);

            p=new Paragraph("MyGubbi shall not be held in breach of this Confirmation or liable for any failure to perform or observe any or all of the terms of this Confirmation resulting directly or indirectly from causes beyond the reasonable control of MyGubbi, such as but not limited to break down or damage of MyGubbi’s machinery, shortages or non-availability of raw materials, components or consumables etc., strikes, lock-out, war, riot, civil commotion, commercial disturbances or acts of God or other causes beyond the control of the MyGubbi, each covered under a “Force Majeure Event”.",fsize);
            document.add(p);

            p=new Paragraph(" ");
            document.add(p);

            p=new Paragraph("13. INTELLECTUAL PROPERTY RIGHTS",fsizebold);
            document.add(p);

            p=new Paragraph("The Client acknowledges that MyGubbi is the sole and absolute owner of all designs, drawings, specifications, products, documentation,  manuals, selling price, correspondence and reports are purely for the personal use of the Client alone. Any reproduction or disclosure thereof by the Client or anyone claiming under the Client to any third party shall be treated as an infringement of the intellectual property rights of MyGubbi and MyGubbi shall be entitled to recover damages from the Client in addition to other legal remedies that may be available to it for such infringement.  ",fsize);
            document.add(p);

            p=new Paragraph(" ");
            document.add(p);

            p=new Paragraph("14. CONFIDENTIALITY AND NON-DISPARGEMENT:",fsizebold);
            document.add(p);

            p=new Paragraph("14.1. The Client hereby undertakes and confirms that the Client shall not disclose any confidential information (including all\n"
                    +"    proprietary, commercial and confidential information of MyGubbi) to any third Party unless agreed or authorized by MyGubbi\n"
                    +"    in writing.",fsize);
            document.add(p);

            p=new Paragraph("14.2. The Client shall not knowingly, either on his/her own or through another person, do or cause any act or matter or thing\n"
                    +"     which would or might reasonably be expected to harm, prejudice materially or bring disrepute to the business or reputation\n"
                    +"     of MyGubbi or which would reasonably be expected to lead to unwanted or unfavorable publicity to MyGubbi during or after\n"
                    +"      the termination of the Confirmation.",fsize);
            document.add(p);

            p=new Paragraph(" ");
            document.add(p);

            p=new Paragraph("15. GOVERNING LAW ",fsizebold);
            document.add(p);

            p=new Paragraph("This Confirmation and any matters arising out of or in connection with it shall be governed by and shall be construed in accordance with the laws of India. The Courts at Bangalore, India shall have exclusive jurisdiction.",fsize);
            document.add(p);

            document.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
