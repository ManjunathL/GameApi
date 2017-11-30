package com.mygubbi.game.proposal.quote;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.mygubbi.game.proposal.ModuleDataService;
import com.mygubbi.game.proposal.ProductAddon;
import com.mygubbi.game.proposal.ProductLineItem;
import com.mygubbi.game.proposal.ProposalHandler;
import com.mygubbi.game.proposal.model.*;
import com.mygubbi.game.proposal.price.RateCardService;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by user on 02-Nov-16.
 */

class CustomBorder extends PdfPageEventHelper {
    public void onEndPage(PdfWriter writer, Document document) {

        PdfContentByte canvas = writer.getDirectContent();
        Rectangle rect = new Rectangle(28, 28, 580, 830);
        rect.setBorder(Rectangle.BOX);
        rect.setBorderWidth(2);
        canvas.rectangle(rect);
    }
}
public class QuotationPDFCreator
{
    private final static Logger LOG = LogManager.getLogger(QuotationPDFCreator.class);
    int count=0;
    int individualcountforProduct=1;
    int individualcountforAddon=1;
    int finalcount=1;
    String series;
    double amt;
    int unitSequence;
    int wunitSequence;
    NumberToWord word=new NumberToWord();
    List<QuotationPDFCreator.customeclass> li,li2;
    public static final String DESIGN_SERVICE_PRICE = "DSP";
    public static final String DESIGN_SERVICE_TAX = "DS";
    public static final String NON_MOVABLE_PRICE = "NMP";
    public static final String MOVABLE_PRICE = "MP";
    public static final String NON_MOVABLE_PRICE_TAX = "NMF";
    public static final String MOVABLE_PRICE_TAX = "MF";
    public static final String SCW_PRICE_TAX = "SCW";

    private static final String[] ALPHABET_SEQUENCE = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s"};
    private static final String[] BOLD_ALPHABET_SEQUENCE = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S"};
    private static final String[] ROMAN_SEQUENCE = new String[]{"i", "ii", "iii", "iv", "v", "vi", "vii", "viii", "ix", "x", "xi", "xii", "xiii", "xiv", "xv"};

    public static final String DEST = "results/fonts/math_symbols.pdf";
    public static final String FONT = "src/main/resources/webroot/fonts/FreeSans.ttf";
    public static final String TEXT = "this string contains special characters like this  \u2208, \u2229, \u2211, \u222b, \u2206";


    PdfPTable itemsTable,B1Table;
    QuoteData quoteData;

    Font fsize=new Font(Font.FontFamily.TIMES_ROMAN,8,Font.NORMAL);
    Font bookingformfsize=new Font(Font.FontFamily.TIMES_ROMAN,8,Font.NORMAL);
    Font bookingformfsize1=new Font(Font.FontFamily.TIMES_ROMAN,10,Font.BOLD,BaseColor.ORANGE);
    Font fsize1=new Font(Font.FontFamily.TIMES_ROMAN,8,Font.BOLD);
    Font fsize3=new Font(Font.FontFamily.TIMES_ROMAN,9,Font.BOLD);
    Font headingSize=new com.itextpdf.text.Font(Font.FontFamily.TIMES_ROMAN,10, com.itextpdf.text.Font.BOLD);
    Font zapfdingbats = new Font(Font.FontFamily.ZAPFDINGBATS, 16);
    private ProposalHeader proposalHeader;
    private ProposalHandler proposalHandler;
    double totalproductPrice=0,totalDAP=0,totalTaxAmt=0,totalPriceAfterTax=0;
    double set1totalproductPrice=0,set1totalDAP=0,set1totalTaxAmt=0,set1totalPriceAfterTax=0;
    double movabletotalproductPrice=0,movabletotalDAP=0,movabletotalTaxAmt=0,movabletotalPriceAfterTax=0;
    double nonmovabletotalproductPrice=0,nonmovabletotalDAP=0,nonmovabletotalTaxAmt=0,nonmovabletotalPriceAfterTax=0;
    double scwtotalproductPrice=0,scwtotalDAP=0,scwtotalTaxAmt=0,scwtotalPriceAfterTax=0;
    double designtotalproductPrice=0,designtotalDAP=0,designtotalTaxAmt=0,designtotalPriceAfterTax=0;
    double producttotalPrice=0,producttotalDAP=0,producttotalTaxAmt=0,producttotalPriceAfterTax=0;
    double addontotalproductPrice=0,addontotalDAP=0,addontotalTaxAmt=0,addontotalPriceAfterTax=0;
    double set2totalproductPrice=0,set2totalDAP=0,set2totalTaxAmt=0,set2totalPriceAfterTax=0;

    List<GSTForProducts> nonMovableList=new ArrayList<>();
    List<GSTForProducts> movableList=new ArrayList<>();
    List<GSTForProducts> scwList=new ArrayList<>();
    List<GSTForProducts> designServiceList=new ArrayList<>();

    List<GSTForProducts> finalMovableList=new ArrayList<>();
    List<GSTForProducts> finalmovableList=new ArrayList<>();
    List<GSTForProducts> finalscwList=new ArrayList<>();
    java.util.Date date;
    java.util.Date currentDate = new java.util.Date(117 ,9,28,0,0,00);

    PriceMaster designServicePrice, nonMovablePrice, movablePrice, nonMovablePriceTax, movablePriceTax,scwTax,designServiceTax;

    QuotationPDFCreator(QuoteData quoteData, ProposalHeader proposalHeader)
    {
        this.date=proposalHeader.getPriceDate();
        this.quoteData=quoteData;
        this.proposalHeader=proposalHeader;
        LOG.info("curremt date " +currentDate +" priceDate " +date);
        LOG.info("date.agfter(currentDate)" +date.after(currentDate));
        if(date.after(currentDate))
        {
            LOG.info("inside if of current date");
            getProducts();
        }

    }

    public void  createpdf(String destination, boolean isValid_Sow)
    {

                try {
        //PdfDocument pdfDocument=new PdfDocument(new PdfWriter(destination));

        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(destination));
        writer.setPdfVersion(PdfWriter.VERSION_1_7);
        writer.createXmpMetadata();
            document.open();
            writer.setPageEvent(new CustomBorder());
            Paragraph p;

            char checked='\u00FE';
            char unchecked='\u00A8';

            if(quoteData.getBookingFormFlag().equals("Yes")) {
                Image img = Image.getInstance("logo.png");
                img.setAlignment(Image.MIDDLE);
                img.scaleAbsolute(100,50);
                //img.setWidthPercentage(5);
                document.add(img);

                /*Phrase pph1=new Phrase();
                pph1.add(new Chunk("Mygubbi.com ",bookingformfsize1));
                pph1.add(new Chunk("#38, Maini Sadan, 7th cross, Lavelle Road, Bangalore 560 001 Phone number: 080 22555789",bookingformfsize));
                p=new Paragraph();
                p.setAlignment(Element.ALIGN_CENTER);
                p.add(pph1);
                document.add(p);*/

                if(proposalHeader.getProjectCity().equals("Chennai"))
                {
                    p = new Paragraph("Ramaniyam Ocean - Isha, No.11, Second Floor Rajiv Gandhi Salai, Old Mahabalipuram road, Okkiyam Thoraipakkam", bookingformfsize);
                    p.setAlignment(Element.ALIGN_CENTER);
                    document.add(p);

                    p = new Paragraph("Chennai 600 096, India Phone +91 80888 60860", fsize);
                    p.setAlignment(Element.ALIGN_CENTER);
                    document.add(p);

                }else if(proposalHeader.getProjectCity().equals("Pune"))
                {
                    p = new Paragraph("\"The Mint \" Building, Office No.101 ,2nd Floor", fsize);
                    p.setAlignment(Element.ALIGN_CENTER);
                    document.add(p);

                    p = new Paragraph("Nr.Kapil Malhar Society, Baner", fsize);
                    p.setAlignment(Element.ALIGN_CENTER);
                    document.add(p);

                    p = new Paragraph("Pune:411045", fsize);
                    p.setAlignment(Element.ALIGN_CENTER);
                    document.add(p);

                }else if(proposalHeader.getProjectCity().equals("Mangalore"))
                {
                    p = new Paragraph("CRYSTAL ARC ( Building Name) Commercial shop premises No F11 & F 12 First Floor Door No 14-4-511/34 & 14-4-511/35 Balmatta Road", fsize);
                    p.setAlignment(Element.ALIGN_CENTER);
                    document.add(p);

                    p = new Paragraph("Hampankatta Mangalore Pincode :575001, India Phone +91 80888 60860", fsize);
                    p.setAlignment(Element.ALIGN_CENTER);
                    document.add(p);

                }
                else
                {
                    p = new Paragraph("No 1502, 1st Floor, 19th Main, Sector 1, HSR Layout", fsize);
                    p.setAlignment(Element.ALIGN_CENTER);
                    document.add(p);

                    p = new Paragraph("Bangalore 560 102, India Phone +91 80888 60860", fsize);
                    p.setAlignment(Element.ALIGN_CENTER);
                    document.add(p);
                }

               /* p = new Paragraph("Mygubbi.com #38, Maini Sadan, 7th cross, Lavelle Road, Bangalore 560 001 Phone number: 080 22555789", fsize);
                p.setAlignment(Element.ALIGN_CENTER);
                document.add(p);*/

                p = new Paragraph(" ");
                document.add(p);

                p = new Paragraph(" BOOKING FORM ", headingSize);
                p.setAlignment(Element.ALIGN_CENTER);
                document.add(p);

                p = new Paragraph(" ");
                document.add(p);

                p = new Paragraph("Date:_________________", bookingformfsize);
                p.setAlignment(Element.ALIGN_RIGHT);
                document.add(p);

                //p=new Paragraph("Name:Mr/Mrs/Ms/Dr_____________________________________________________________________________",bookingformfsize);
                p = new Paragraph("Name : Mr/Mrs/Ms/Dr : " + " " + proposalHeader.getName(), bookingformfsize);
                document.add(p);

                /*p = new Paragraph(" ");
                document.add(p);*/

                //p=new Paragraph("Quotation Id:__________________________________________________________________________________",bookingformfsize);
                p = new Paragraph("Quotation ID: " + proposalHeader.getQuoteNumNew(), bookingformfsize);
                document.add(p);

                p = new Paragraph(" ");
                document.add(p);

                PdfPTable Appdetails = new PdfPTable(1);
                Appdetails.setWidthPercentage(100);
                p=new Paragraph("APPLICANT'S DETAILS",headingSize);
                p.setAlignment(Element.ALIGN_CENTER);
                PdfPCell scell1 = new PdfPCell();
                scell1.addElement(p);
                scell1.setBackgroundColor(BaseColor.ORANGE);
                Appdetails.addCell(scell1);
                document.add(Appdetails);

               /* p = new Paragraph("Application Details", headingSize);
                p.setAlignment(Element.ALIGN_CENTER);
                document.add(p);*/

                p = new Paragraph(" ");
                document.add(p);

                //p=new Paragraph("Project Name:________________________________Apartment No:____________Floor No: __________",bookingformfsize);
                p = new Paragraph("Project Name : " + proposalHeader.getProjectName() + "                                                                                    " +" Apartment No : __________         Floor No : __________ ", bookingformfsize);
                document.add(p);

                p = new Paragraph(" ");
                document.add(p);

                LOG.info("proposalHeader.getProjectAddress1() "+proposalHeader.getProjectAddress1());
                if(proposalHeader.getProjectAddress1()== "null" || proposalHeader.getProjectAddress1().length() == 0 )
                {
                    p=new Paragraph("Project Address : __________________________________________________________________________________________________",bookingformfsize);
                }else {
                    p = new Paragraph("Project Address:" + proposalHeader.getProjectAddress1(), bookingformfsize);
                }
                document.add(p);

                p = new Paragraph(" ");
                document.add(p);

                Phrase ph1=new Phrase();
                ph1.add(new Chunk("Project/Apartment Possession :     ",bookingformfsize));
                ph1.add(new Chunk("  Less than 60 days*  ",bookingformfsize));
                ph1.add(new Chunk("  o  ", zapfdingbats));
                ph1.add(new Chunk("     ", zapfdingbats));
                ph1.add(new Chunk("  More than 60 days*  ",bookingformfsize));
                ph1.add(new Chunk("  o  ", zapfdingbats));
                ph1.add(new Chunk("     ", bookingformfsize));
                ph1.add(new Chunk("*(Mandatory to be filled)",bookingformfsize ));
                p=new Paragraph();
                p.add(ph1);
                document.add(p);

                p = new Paragraph(" ");
                document.add(p);

                Phrase ph2=new Phrase();
                ph2.add(new Chunk("Profession:    ",bookingformfsize));
                ph2.add(new Chunk("  Salaried  ",bookingformfsize));
                ph2.add(new Chunk("  o  ", zapfdingbats));
                ph2.add(new Chunk("  Bussiness  ",bookingformfsize));
                ph2.add(new Chunk("  o  ", zapfdingbats));
                ph2.add(new Chunk("  Others  ",bookingformfsize));
                ph2.add(new Chunk("  o  ", zapfdingbats));
                p=new Paragraph();
                p.add(ph2);
                //p = new Paragraph("Profession:  " + "Salarised ____ " + " " + "Business_____ " + " " + "Others____", bookingformfsize);
                document.add(p);

                p = new Paragraph(" ");
                document.add(p);

                p = new Paragraph("Post/Designation : ______________________________________________________" + "             " + "Company's Name : _________________________________", bookingformfsize);
                document.add(p);

                p = new Paragraph(" ");
                document.add(p);

                Phrase ph3=new Phrase();
                ph3.add(new Chunk("Professional Details:   ",bookingformfsize));
                ph3.add(new Chunk("  IT  ",bookingformfsize));
                ph3.add(new Chunk("  o  ", zapfdingbats));
                ph3.add(new Chunk("  ITES/BPO  ",bookingformfsize));
                ph3.add(new Chunk("  o  ", zapfdingbats));
                ph3.add(new Chunk("  Doctor  ",bookingformfsize));
                ph3.add(new Chunk("  o  ", zapfdingbats));
                ph3.add(new Chunk("  Govt. Services/PSU  ",bookingformfsize));
                ph3.add(new Chunk("  o  ", zapfdingbats));
                p=new Paragraph();
                p.add(ph3);
               // p = new Paragraph("Professional Details: IT____" + "  " + "ITES/BPO______ " + " " + "Doctor_____ " + " " + "Govt. Services/PSU___", bookingformfsize);
                document.add(p);

                p = new Paragraph(" ");
                document.add(p);

                Phrase ph4=new Phrase();
                ph4.add(new Chunk("Banking & Finance  ",bookingformfsize));
                ph4.add(new Chunk("  o  ", zapfdingbats));
                ph4.add(new Chunk("  Manufacturing/Distribution  ",bookingformfsize));
                ph4.add(new Chunk("  o  ", zapfdingbats));
                ph4.add(new Chunk("  Others,please specify  ",bookingformfsize));
                ph4.add(new Chunk("  ______________________  ", bookingformfsize));
                p=new Paragraph();
                p.add(ph4);
                //p = new Paragraph("Banking & Finance_____ " + " " + "Manufacturing/Distribution______ " + " " + "Others,please specify:________", bookingformfsize);
                document.add(p);

                p = new Paragraph(" ");
                document.add(p);

                Phrase ph5=new Phrase();
                ph5.add(new Chunk("Annual Income (in Rs. Lacs) ",bookingformfsize));
                ph5.add(new Chunk("  Less than 15  ",bookingformfsize));
                ph5.add(new Chunk("  o  ", zapfdingbats));
                ph5.add(new Chunk("  16-25  ",bookingformfsize));
                ph5.add(new Chunk("  o  ", zapfdingbats));
                ph5.add(new Chunk("  26-35  ",bookingformfsize));
                ph5.add(new Chunk("  o  ", zapfdingbats));
                ph5.add(new Chunk("  36-50  ",bookingformfsize));
                ph5.add(new Chunk("  o  ", zapfdingbats));
                ph5.add(new Chunk("  51 & above  ",bookingformfsize));
                ph5.add(new Chunk("  o  ", zapfdingbats));
                p=new Paragraph();
                p.add(ph5);
                //p = new Paragraph("Annual Income (in RS. Lacs) Less than 15 __" + "16-25__" + " " + "26-35___" + "36-50___" + "51 & above__", bookingformfsize);
                document.add(p);

                p = new Paragraph(" ");
                document.add(p);

                p = new Paragraph("Landline : ___________________________________" + "                     " +  " Mobile(1) : " + proposalHeader.getContact() + "                       " +"   Mobile(2) : _________________________ ", bookingformfsize);
                document.add(p);

                p = new Paragraph(" ");
                document.add(p);

                p = new Paragraph("Email ID(1) : ____________________________________________________________________________________________________________________", bookingformfsize);
                document.add(p);

                p = new Paragraph(" ");
                document.add(p);

                p = new Paragraph("Email ID(2) : ____________________________________________________________________________________________________________________", bookingformfsize);
                document.add(p);

                p = new Paragraph(" ");
                document.add(p);

                p = new Paragraph("Address for Communication : ________________________________________________________________________________________________________", bookingformfsize);
                document.add(p);

                p = new Paragraph(" ");
                document.add(p);

                p = new Paragraph("Office Address:(if applicable) _______________________________________________________________________________________________________", bookingformfsize);
                document.add(p);

                p = new Paragraph(" ");
                document.add(p);

                p = new Paragraph("_________________________________________________________________________________________________" + "  PAN Number:__________________", bookingformfsize);
                document.add(p);

                p = new Paragraph(" ");
                document.add(p);

                /*p = new Paragraph("Order Details", headingSize);
                p.setAlignment(Element.ALIGN_CENTER);
                document.add(p);*/

                PdfPTable orderdetails = new PdfPTable(1);
                orderdetails.setWidthPercentage(100);
                p=new Paragraph("ORDER DETAILS",headingSize);
                p.setAlignment(Element.ALIGN_CENTER);
                scell1 = new PdfPCell();
                scell1.addElement(p);
                scell1.setBackgroundColor(BaseColor.ORANGE);
                orderdetails.addCell(scell1);
                document.add(orderdetails);

                p = new Paragraph(" ");
                document.add(p);

               /* p = new Paragraph("Order Value : ____________________________________________________________________________________________________________________", bookingformfsize);
                document.add(p);*/

                /*p = new Paragraph(" ");
                document.add(p);*/

                //p=new Paragraph("Sales Order ID:_________________________________________________________________________________",bookingformfsize);
                p = new Paragraph("Sales Order ID : " + proposalHeader.getCrmId(), bookingformfsize);
                document.add(p);

                /*p = new Paragraph(" ");
                document.add(p);*/

                p = new Paragraph("Order Date : ____________________________________________________________________________________________________________________", bookingformfsize);
                document.add(p);

               /* p = new Paragraph(" ");
                document.add(p);*/

                p = new Paragraph("Remarks : ______________________________________________________________________________________________________________________", bookingformfsize);
                document.add(p);

               /* p = new Paragraph(" ");
                document.add(p);*/
                //p=new Paragraph("Total Quotation Value:___________________________________________________________________________",bookingformfsize);
                Double val = quoteData.getTotalCost() - quoteData.getDiscountAmount();

                Double res = val - val % 10;
                p = new Paragraph("Total Quotation Value Rs. " + this.getRoundOffValue(String.valueOf(res.intValue())), bookingformfsize);
                document.add(p);

               /* p = new Paragraph(" ");
                document.add(p);*/
            }

            document.newPage(); //new page

            Image img1 = Image.getInstance("myGubbi_Logo.png");
            img1.setWidthPercentage(50);
            document.add(img1);
            //Paragraph p;
            LOG.info("quoteData.getBookingFormFlag()" +quoteData.getBookingFormFlag()+ " || quotedata.getworksContractFlag" +quoteData.getWorksContractFlag());

            if(proposalHeader.getProjectCity().equals("Chennai"))
            {
                p = new Paragraph("Ramaniyam Ocean - Isha, No.11, Second Floor", fsize);
                p.setAlignment(Element.ALIGN_LEFT);
                document.add(p);

                p = new Paragraph("Rajiv Gandhi Salai, Old Mahabalipuram road, Okkiyam Thoraipakkam", fsize);
                p.setAlignment(Element.ALIGN_LEFT);
                document.add(p);

                p = new Paragraph("Chennai 600 096, India", fsize);
                p.setAlignment(Element.ALIGN_LEFT);
                document.add(p);

                p = new Paragraph("Phone +91 80888 60860", fsize);
                p.setAlignment(Element.ALIGN_LEFT);
                document.add(p);
                p = new Paragraph("Annexure - A : QUOTATION",fsize3);
                p.setAlignment(Element.ALIGN_CENTER);
                fsize3.setColor(BaseColor.GRAY);
                document.add(p);

                p = new Paragraph("      ");
                p.setAlignment(Element.ALIGN_LEFT);
                document.add(p);

            }else if(proposalHeader.getProjectCity().equals("Pune"))
            {
                p = new Paragraph("\"The Mint \" Building, Office No.101 ,2nd Floor", fsize);
                p.setAlignment(Element.ALIGN_LEFT);
                document.add(p);

                p = new Paragraph("Nr.Kapil Malhar Society, Baner", fsize);
                p.setAlignment(Element.ALIGN_LEFT);
                document.add(p);

                p = new Paragraph("Pune:411045", fsize);
                p.setAlignment(Element.ALIGN_LEFT);
                document.add(p);

                p = new Paragraph("Phone +91 80888 60860", fsize);
                p.setAlignment(Element.ALIGN_LEFT);
                document.add(p);

                p = new Paragraph("Annexure - A : QUOTATION",fsize3);
                p.setAlignment(Element.ALIGN_CENTER);
                fsize3.setColor(BaseColor.GRAY);
                document.add(p);

                p = new Paragraph("      ");
                p.setAlignment(Element.ALIGN_LEFT);
                document.add(p);

            }else if(proposalHeader.getProjectCity().equals("Mangalore"))
            {
                p = new Paragraph("CRYSTAL ARC ( Building Name) Commercial shop premises No F11 & F 12 First Floor Door No 14-4-511/34 & 14-4-511/35 Balmatta Road", fsize);
                p.setAlignment(Element.ALIGN_LEFT);
                document.add(p);

                p = new Paragraph("Hampankatta Mangalore Pincode :575001, India", fsize);
                p.setAlignment(Element.ALIGN_LEFT);
                document.add(p);

                p = new Paragraph("Phone +91 80888 60860", fsize);
                p.setAlignment(Element.ALIGN_LEFT);
                document.add(p);

                p = new Paragraph("Annexure - A : QUOTATION",fsize3);
                p.setAlignment(Element.ALIGN_CENTER);
                fsize3.setColor(BaseColor.GRAY);
                document.add(p);

                p = new Paragraph("      ");
                p.setAlignment(Element.ALIGN_LEFT);
                document.add(p);
            }
            else
            {

                p = new Paragraph("No 1502, 1st Floor, 19th Main, Sector 1, HSR Layout", fsize);
                p.setAlignment(Element.ALIGN_LEFT);
                document.add(p);

                p = new Paragraph("Bangalore 560 102, India", fsize);
                p.setAlignment(Element.ALIGN_LEFT);
                document.add(p);

                p = new Paragraph("Phone +91 80888 60860", fsize);
                p.setAlignment(Element.ALIGN_LEFT);
                document.add(p);

                p = new Paragraph("Annexure - A : QUOTATION",fsize3);
                p.setAlignment(Element.ALIGN_CENTER);
                fsize3.setColor(BaseColor.GRAY);
                document.add(p);

                p = new Paragraph("      ");
                p.setAlignment(Element.ALIGN_LEFT);
                document.add(p);

            }
            if(Objects.equals(proposalHeader.getPackageFlag(), "Yes"))
            {
               // LOG.info("packge value " +proposalHeader.getPackageFlag());
                return;
            }
            float[] columnWidths2 = {4,2};
            PdfPTable table = new PdfPTable(columnWidths2);
            table.setWidthPercentage(100);

            Phrase phrase = new Phrase();
            phrase.add(new Chunk("Quotation For: ",fsize1));
            phrase.add(new Chunk(proposalHeader.getQuotationFor(),fsize1));

            Phrase phrase1 = new Phrase();
            phrase1.add(new Chunk("Date: ",fsize1));
            phrase1.add(new Chunk(DateFormatUtils.format(new Date(), "dd-MMM-yyyy"),fsize));

            Phrase phrase2 = new Phrase();
            phrase2.add(new Chunk("Name: ",fsize1));
            phrase2.add(new Chunk(proposalHeader.getName(),fsize));

            Phrase phrase3 = new Phrase();
            phrase3.add(new Chunk("CRM ID: ",fsize1));
            phrase3.add(new Chunk(proposalHeader.getCrmId(),fsize));

            String strqnum= quoteData.fromVersion;
            String qnum="";
            strqnum=strqnum.replace(".","");
            if(proposalHeader.getQuoteNum()==null || proposalHeader.getQuoteNum().equals(""))
            {
                qnum=proposalHeader.getQuoteNumNew()+ "." +strqnum;
            }
            else {
                        qnum=proposalHeader.getQuoteNum()+ "." +strqnum;
            }

            Phrase phrase4 = new Phrase();
            phrase4.add(new Chunk("Quotation #: ",fsize1));
            phrase4.add(new Chunk(qnum,fsize));

            Phrase phrase5 = new Phrase();
            phrase5.add(new Chunk("Project Address: ",fsize1));
            phrase5.add(new Chunk(quoteData.concatValuesFromKeys(new String[]{ProposalHeader.PROJECT_NAME, ProposalHeader.PROJECT_ADDRESS1, ProposalHeader.PROJECT_ADDRESS2, ProposalHeader.PROJECT_CITY}, ","),fsize));

            table.addCell(phrase);
            table.addCell(phrase1);
            table.addCell(phrase2);
            table.addCell(phrase3);
            table.addCell(phrase5);
            table.addCell(phrase4);

            /*PdfPTable pdfPTable=new PdfPTable(1);
            pdfPTable.setWidthPercentage(100);
            Phrase phrase6 = new Phrase();
            phrase6.add(new Chunk("Project Address: ",fsize1));
            phrase6.add(new Chunk(quoteData.concatValuesFromKeys(new String[]{ProposalHeader.PROJECT_NAME, ProposalHeader.PROJECT_ADDRESS1, ProposalHeader.PROJECT_ADDRESS2, ProposalHeader.PROJECT_CITY}, ","),fsize));
            pdfPTable.addCell(phrase6);*/

            document.add(table);
            //document.add(pdfPTable);

            p = new Paragraph("      ");
            p.setAlignment(Element.ALIGN_LEFT);
            document.add(p);

            PdfPTable stable = new PdfPTable(3);
            stable.setWidthPercentage(100);
            PdfPCell scell1 = new PdfPCell(new Paragraph("Sales/Design",fsize1));
            scell1.setBackgroundColor(BaseColor.ORANGE);
            PdfPCell scell2 = new PdfPCell(new Paragraph("Sales Person Number",fsize1));
            scell2.setBackgroundColor(BaseColor.ORANGE);
            PdfPCell scell3 = new PdfPCell(new Paragraph("Email Id",fsize1));
            scell3.setBackgroundColor(BaseColor.ORANGE);
            stable.addCell(scell1);
            stable.addCell(scell2);
            stable.addCell(scell3);

            Font ffsize=new Font(Font.FontFamily.TIMES_ROMAN,7,Font.NORMAL);
            LOG.info("ProposalHeader.SALESPERSON_NAME, ProposalHeader.DESIGNER_NAME " +ProposalHeader.SALESPERSON_NAME, ProposalHeader.DESIGNER_NAME);
            PdfPCell scell4 = new PdfPCell(new Paragraph(quoteData.concatValuesFromKeys(new String[]{ProposalHeader.SALESPERSON_NAME, ProposalHeader.DESIGNER_NAME}, "/"),ffsize));
            ffsize.setColor(BaseColor.BLUE);
            PdfPCell scell5 = new PdfPCell(new Paragraph(proposalHeader.getSalesPhone(),ffsize));
            ffsize.setColor(BaseColor.BLUE);
            PdfPCell scell6 = new PdfPCell(new Paragraph(proposalHeader.getSalesEmail(),ffsize));
            ffsize.setColor(BaseColor.BLUE);
            stable.addCell(scell4);
            stable.addCell(scell5);
            stable.addCell(scell6);
            document.add(stable);

            float[] columnWidths1 = {1,8,1,1,1};
            itemsTable = new PdfPTable(columnWidths1);
            itemsTable.setWidthPercentage(100);

            if(Objects.equals(proposalHeader.getPackageFlag(), "Yes"))
            {
               // LOG.info("packge value " +proposalHeader.getPackageFlag());
                return;
            }

            PdfPCell cel=new PdfPCell();
            p=new Paragraph("KITCHEN & OTHER BASE UNITS",fsize1);
            p.setAlignment(Element.ALIGN_LEFT);
            cel.addElement(p);
            cel.setColspan(5);

            cel.setBorder(Rectangle.NO_BORDER);
            itemsTable.addCell(cel);

            PdfPCell itemsCell1 = new PdfPCell(new Paragraph("SL.NO",fsize1));
            itemsCell1.setBackgroundColor(BaseColor.ORANGE);
            PdfPCell itemsCell2 = new PdfPCell(new Paragraph("DESCRIPTION",fsize1));
            itemsCell2.setBackgroundColor(BaseColor.ORANGE);
            PdfPCell itemsCell3 = new PdfPCell(new Paragraph("QTY",fsize1));
            itemsCell3.setBackgroundColor(BaseColor.ORANGE);
            PdfPCell itemsCell4 = new PdfPCell(new Paragraph("PRICE",fsize1));
            itemsCell4.setBackgroundColor(BaseColor.ORANGE);
            PdfPCell itemsCell5 = new PdfPCell(new Paragraph("AMOUNT",fsize1));
            itemsCell5.setBackgroundColor(BaseColor.ORANGE);

            itemsTable.addCell(itemsCell1);
            itemsTable.addCell(itemsCell2);
            itemsTable.addCell(itemsCell3);
            itemsTable.addCell(itemsCell4);
            itemsTable.addCell(itemsCell5);

            this.fillAssembledProducts(itemsTable);

            this.fillCatalogProducts(itemsTable);
            document.add(itemsTable);

            p = new Paragraph("Estimated Cost(A):" +this.getRoundOffValue(String.valueOf((int)quoteData.productsCost)) ,fsize1);
            p.setAlignment(Element.ALIGN_RIGHT);
            document.add(p);

            p=new Paragraph(" ");
            document.add(p);

            float[] addonsWidths1 = {1,7,1,1,1,1};
            B1Table = new PdfPTable(addonsWidths1);
            B1Table.setWidthPercentage(100);

            PdfPCell cel1=new PdfPCell();
            p = new Paragraph("APPLIANCES,SERVICES,OTHERS \n ADD ON ACCESSORIES  ",fsize1);
            p.setAlignment(Element.ALIGN_LEFT);
            cel1.addElement(p);
            cel1.setColspan(6);
            cel1.setBorder(Rectangle.NO_BORDER);
            B1Table.addCell(cel1);

            PdfPCell B1Cell1 = new PdfPCell(new Paragraph("SL.NO",fsize1));
            B1Cell1.setBackgroundColor(BaseColor.ORANGE);
            PdfPCell B1Cell2 = new PdfPCell(new Paragraph("Description",fsize1));
                    B1Cell2.setBackgroundColor(BaseColor.ORANGE);
            PdfPCell B1Cell6 = new PdfPCell(new Paragraph("UOM",fsize1));
                    B1Cell6.setBackgroundColor(BaseColor.ORANGE);
            PdfPCell B1Cell3 = new PdfPCell(new Paragraph("QTY",fsize1));
                    B1Cell3.setBackgroundColor(BaseColor.ORANGE);
            PdfPCell B1Cell4 = new PdfPCell(new Paragraph("PRICE",fsize1));
                    B1Cell4.setBackgroundColor(BaseColor.ORANGE);
            PdfPCell B1Cell5 = new PdfPCell(new Paragraph("AMOUNT",fsize1));
                    B1Cell5.setBackgroundColor(BaseColor.ORANGE);
            B1Table.addCell(B1Cell1);
            B1Table.addCell(B1Cell2);
            B1Table.addCell(B1Cell6);
            B1Table.addCell(B1Cell3);
            B1Table.addCell(B1Cell4);
            B1Table.addCell(B1Cell5);

            this.fillAddons(B1Table, this.quoteData.getAccessories(), "No additional accessories.");
            document.add(B1Table);

            PdfPTable B2Table = new PdfPTable(addonsWidths1);
            B2Table.setWidthPercentage(100);

            PdfPCell cel2=new PdfPCell();
            p = new Paragraph("APPLIANCES",fsize1);
            p.setAlignment(Element.ALIGN_LEFT);
            cel2.addElement(p);
            cel2.setColspan(6);
            cel2.setBorder(Rectangle.NO_BORDER);
            B2Table.addCell(cel2);

            PdfPCell B2Cell1 = new PdfPCell(new Paragraph("SL.NO",fsize1));
                    B2Cell1.setBackgroundColor(BaseColor.ORANGE);
            PdfPCell B2Cell2 = new PdfPCell(new Paragraph("DESCRIPTION",fsize1));
            PdfPCell B2Cell3 = new PdfPCell(new Paragraph("UOM",fsize1));
            PdfPCell B2Cell4 = new PdfPCell(new Paragraph("QTY",fsize1));
            PdfPCell B2Cell5 = new PdfPCell(new Paragraph("PRICE",fsize1));
            PdfPCell B2Cell6 = new PdfPCell(new Paragraph("AMOUNT",fsize1));
                    B2Cell2.setBackgroundColor(BaseColor.ORANGE);
                    B2Cell3.setBackgroundColor(BaseColor.ORANGE);
                    B2Cell4.setBackgroundColor(BaseColor.ORANGE);
                    B2Cell5.setBackgroundColor(BaseColor.ORANGE);
                    B2Cell6.setBackgroundColor(BaseColor.ORANGE);
            B2Table.addCell(B2Cell1);
            B2Table.addCell(B2Cell2);
            B2Table.addCell(B2Cell3);
            B2Table.addCell(B2Cell4);
            B2Table.addCell(B2Cell5);
            B2Table.addCell(B2Cell6);

            this.fillAddons(B2Table, this.quoteData.getAppliances(), "No additional appliances.");
            document.add(B2Table);

            PdfPTable B3Table = new PdfPTable(addonsWidths1);
            B3Table.setWidthPercentage(100);

            PdfPCell cel3=new PdfPCell();
            p = new Paragraph("COUNTER TOP",fsize1);
            p.setAlignment(Element.ALIGN_LEFT);
            cel3.addElement(p);
            cel3.setColspan(6);
            cel3.setBorder(Rectangle.NO_BORDER);
            B3Table.addCell(cel3);

            PdfPCell B3Cell1 = new PdfPCell(new Paragraph("SL.NO",fsize1));
            PdfPCell B3Cell2 = new PdfPCell(new Paragraph("DESCRIPTION",fsize1));
            PdfPCell B3Cell3 = new PdfPCell(new Paragraph("UOM",fsize1));
            PdfPCell B3Cell4 = new PdfPCell(new Paragraph("QTY",fsize1));
            PdfPCell B3Cell5 = new PdfPCell(new Paragraph("PRICE",fsize1));
            PdfPCell B3Cell6 = new PdfPCell(new Paragraph("AMOUNT",fsize1));
                    B3Cell1.setBackgroundColor(BaseColor.ORANGE);
                    B3Cell2.setBackgroundColor(BaseColor.ORANGE);
                    B3Cell3.setBackgroundColor(BaseColor.ORANGE);
                    B3Cell4.setBackgroundColor(BaseColor.ORANGE);
                    B3Cell5.setBackgroundColor(BaseColor.ORANGE);
                    B3Cell6.setBackgroundColor(BaseColor.ORANGE);
            B3Table.addCell(B3Cell1);
        B3Table.addCell(B3Cell2);
        B3Table.addCell(B3Cell3);
        B3Table.addCell(B3Cell4);
        B3Table.addCell(B3Cell5);
        B3Table.addCell(B3Cell6);

        this.fillAddons(B3Table, this.quoteData.getCounterTops(), "No countertops.");
        document.add(B3Table);

            PdfPTable B4Table = new PdfPTable(addonsWidths1);
        B4Table.setWidthPercentage(100);

            PdfPCell cel4=new PdfPCell();
            p = new Paragraph("SERVICES",fsize1);
            p.setAlignment(Element.ALIGN_LEFT);
            cel4.addElement(p);
            cel4.setColspan(6);
            cel4.setBorder(Rectangle.NO_BORDER);
            B4Table.addCell(cel4);

            PdfPCell B4Cell1 = new PdfPCell(new Paragraph("SL.NO",fsize1));
            PdfPCell B4Cell2 = new PdfPCell(new Paragraph("DESCRIPTION",fsize1));
            PdfPCell B4Cell3 = new PdfPCell(new Paragraph("UOM",fsize1));
            PdfPCell B4Cell4 = new PdfPCell(new Paragraph("QTY",fsize1));
            PdfPCell B4Cell5 = new PdfPCell(new Paragraph("PRICE",fsize1));
            PdfPCell B4Cell6 = new PdfPCell(new Paragraph("AMOUNT",fsize1));
                    B4Cell1.setBackgroundColor(BaseColor.ORANGE);
                    B4Cell2.setBackgroundColor(BaseColor.ORANGE);
                    B4Cell3.setBackgroundColor(BaseColor.ORANGE);
                    B4Cell4.setBackgroundColor(BaseColor.ORANGE);
                    B4Cell5.setBackgroundColor(BaseColor.ORANGE);
                    B4Cell6.setBackgroundColor(BaseColor.ORANGE);
        B4Table.addCell(B4Cell1);
        B4Table.addCell(B4Cell2);
        B4Table.addCell(B4Cell3);
        B4Table.addCell(B4Cell4);
        B4Table.addCell(B4Cell5);
        B4Table.addCell(B4Cell6);

        this.fillAddons(B4Table, this.quoteData.getServices(), "No additional services.");
        document.add(B4Table);

            PdfPTable B5Table = new PdfPTable(addonsWidths1);
            B5Table.setWidthPercentage(100);

            PdfPCell cel5=new PdfPCell();
            p = new Paragraph("LOOSE FURNITURE",fsize1);
            p.setAlignment(Element.ALIGN_LEFT);
            cel5.addElement(p);
            cel5.setColspan(6);
            cel5.setBorder(Rectangle.NO_BORDER);
            B5Table.addCell(cel5);

            PdfPCell B5Cell1 = new PdfPCell(new Paragraph("SL.NO",fsize1));
            PdfPCell B5Cell2 = new PdfPCell(new Paragraph("DESCRIPTION",fsize1));
            PdfPCell B5Cell3 = new PdfPCell(new Paragraph("UOM",fsize1));
            PdfPCell B5Cell4 = new PdfPCell(new Paragraph("QTY",fsize1));
            PdfPCell B5Cell5 = new PdfPCell(new Paragraph("PRICE",fsize1));
            PdfPCell B5Cell6 = new PdfPCell(new Paragraph("AMOUNT",fsize1));
                    B5Cell1.setBackgroundColor(BaseColor.ORANGE);
                    B5Cell2.setBackgroundColor(BaseColor.ORANGE);
                    B5Cell3.setBackgroundColor(BaseColor.ORANGE);
                    B5Cell4.setBackgroundColor(BaseColor.ORANGE);
                    B5Cell5.setBackgroundColor(BaseColor.ORANGE);
                    B5Cell6.setBackgroundColor(BaseColor.ORANGE);
            B5Table.addCell(B5Cell1);
            B5Table.addCell(B5Cell2);
            B5Table.addCell(B5Cell3);
            B5Table.addCell(B5Cell4);
            B5Table.addCell(B5Cell5);
            B5Table.addCell(B5Cell6);

            this.fillAddons(B5Table, this.quoteData.getLooseFurniture(), "No additional Loose Furniture.");
            document.add(B5Table);

            PdfPTable B6Table = new PdfPTable(addonsWidths1);
            B6Table.setWidthPercentage(100);

            PdfPCell cell6=new PdfPCell();
            p = new Paragraph("CUSTOM ADDON ACCESSORIES",fsize1);
                    p.setAlignment(Element.ALIGN_LEFT);
            p.setAlignment(Element.ALIGN_LEFT);
            cell6.addElement(p);
            cell6.setColspan(6);
            cell6.setColspan(6);
            cell6.setBorder(Rectangle.NO_BORDER);
            B6Table.addCell(cell6);

            PdfPCell B6Cell1 = new PdfPCell(new Paragraph("SL.NO",fsize1));
            PdfPCell B6Cell2 = new PdfPCell(new Paragraph("DESCRIPTION",fsize1));
            PdfPCell B6Cell3 = new PdfPCell(new Paragraph("UOM",fsize1));
            PdfPCell B6Cell4 = new PdfPCell(new Paragraph("QTY",fsize1));
            PdfPCell B6Cell5 = new PdfPCell(new Paragraph("PRICE",fsize1));
            PdfPCell B6Cell6 = new PdfPCell(new Paragraph("AMOUNT",fsize1));
                    B6Cell1.setBackgroundColor(BaseColor.ORANGE);
                    B6Cell2.setBackgroundColor(BaseColor.ORANGE);
                    B6Cell3.setBackgroundColor(BaseColor.ORANGE);
                    B6Cell4.setBackgroundColor(BaseColor.ORANGE);
                    B6Cell5.setBackgroundColor(BaseColor.ORANGE);
                    B6Cell6.setBackgroundColor(BaseColor.ORANGE);
            B6Table.addCell(B6Cell1);
            B6Table.addCell(B6Cell2);
            B6Table.addCell(B6Cell3);
            B6Table.addCell(B6Cell4);
            B6Table.addCell(B6Cell5);
            B6Table.addCell(B6Cell6);

            this.fillAddons(B6Table, this.quoteData.getCustomAddons(), "No additional Custom Addon.");
            document.add(B6Table);

            p = new Paragraph("Estimated Cost(B):" +this.getRoundOffValue(String.valueOf((int)quoteData.addonsCost)),fsize1);
            p.setAlignment(Element.ALIGN_RIGHT);
        document.add(p);

            double val2=quoteData.getTotalCost();
            double val3=val2-val2%10;

        p = new Paragraph("Estimated Cost(A+B):" +this.getRoundOffValue(String.valueOf((int)quoteData.getTotalCost())) ,fsize1);
        p.setAlignment(Element.ALIGN_RIGHT);
        document.add(p);


           // LOG.info("Quote data" +quoteData);
            //LOG.info("discount amount" +quoteData.discountAmount);
            //LOG.info("is value" +(!(quoteData.discountAmount==0.0)));
            if(!(quoteData.discountAmount==0.0))
            {
                p = new Paragraph("Discount(C):" + this.getRoundOffValue(String.valueOf((int) quoteData.discountAmount)), fsize1);
                p.setAlignment(Element.ALIGN_RIGHT);
                document.add(p);
            }
            Double val = quoteData.getTotalCost() - quoteData.getDiscountAmount();

            Double res = val - val % 10;
            p = new Paragraph("Estimated Cost After Discount (A+B-C): " +this.getRoundOffValue(String.valueOf(res.intValue())) + "\n" ,fsize1);
            p.setAlignment(Element.ALIGN_RIGHT);
            document.add(p);

        p = new Paragraph("      ");
        p.setAlignment(Element.ALIGN_LEFT);
        document.add(p);

        PdfPTable table2=new PdfPTable(1);
        table2.setWidthPercentage(100);

            p=new Paragraph("In words: " +word.convertNumberToWords(res.intValue()) + " Rupees Only" ,fsize1);
            table2.addCell(new Paragraph(p));
        document.add(table2);

        p = new Paragraph("      ");
        p.setAlignment(Element.ALIGN_LEFT);
        document.add(p);

        p=new Paragraph("* The quoted price is an all inclusive price including design, consultancy and GST charges.",fsize);
        p.setAlignment(Element.ALIGN_LEFT);
        document.add(p);

        p=new Paragraph(" ");
        document.add(p);

        if(isValid_Sow) {
            p = new Paragraph("* The interiors and services will be delivered within 60 days of the design sign off, 50% payment or site readiness whichever is later. ", fsize);
            p.setAlignment(Element.ALIGN_LEFT);
            document.add(p);

        }

            p = new Paragraph("      ");
            p.setAlignment(Element.ALIGN_LEFT);
            document.add(p);

            PdfPTable tab1=new PdfPTable(1);
            tab1.setWidthPercentage(100);

            PdfPCell cel6=new PdfPCell();
            p = new Paragraph("Material Specification: ",fsize1);
            p.setAlignment(Element.ALIGN_LEFT);
            cel6.addElement(p);
            cel6.setBorder(Rectangle.NO_BORDER);
            tab1.addCell(cel6);


            tab1.addCell(new Paragraph
                    ("1. \tPly: \tIS 303 grade\n" +
                            "2. \tMdf: \tInterior Grade Mdf\n" +
                            "3. \tEdge Banding: \tRehau\n" +
                            "4. \tLaminates: \tGlossy /Matt/Textured/Metalic Laminates by Merino/Greenlam\n" +
                            "5. \tHardwares: \tHettich/Ebco/Rehau\n" +
                            "6. \tAccessories: \tHe - Hettich/Ha - Hafele/Ev - Evershine/Eb - Ebco\n" +
                            "7. \tGlass/Mirror: \tAsahi/ Saint Gobain\n"+
                            "8. \tLacquered Glass: \tSaint Gobain\n" +
                            "9. \tAppliances: \tFaber /Elica/Kaff/Nagold/ Bosch\n" +
                            "10.\tSink: \tCarisyl/Franke/Nirali/Futura\n",fsize));

            PdfPCell cel7=new PdfPCell();
            p = new Paragraph(new Paragraph("Other Finishes offered are Acrylic, Foil, PU paint, UV laminated panels,Hardwood of mygubbi make.\t\t\t\t\n",fsize));
            p.setAlignment(Element.ALIGN_LEFT);
            cel7.addElement(p);
            cel7.setBorder(Rectangle.NO_BORDER);
            tab1.addCell(cel7);
            document.add(tab1);

            PdfPTable tab2=new PdfPTable(1);
            tab2.setWidthPercentage(100);

            PdfPCell cel8=new PdfPCell();
            p = new Paragraph("Note:\n",fsize1);
            p.setAlignment(Element.ALIGN_LEFT);
            cel8.addElement(p);
            cel8.setBorder(Rectangle.NO_BORDER);
            tab2.addCell(cel8);

            tab2.addCell(new Paragraph("1. \t Plumbing, counter top , gas piping ,appliances, hob ,chimney ,sink, taps, electrical shifting, tile laying,Core cutting and civil changes are not considered in kitchen quote. These items are quoted seperately if needed.\n"
                    +"2. \t Final paint quote to be completed after furniture installation by Customer It will be quoted separately if it is in mygubbi scope.\n"
                    +"3. \t Please refer \"Scope of Services\" section at the end for more details of the services scope"
                    ,fsize));

            document.add(tab2);

            LOG.info("quoteData .get bookingform flag " +quoteData.getBookingFormFlag()+ " quoteDate get worksContract " +quoteData.getWorksContractFlag());
            if(quoteData.getBookingFormFlag().equals("No") && quoteData.getWorksContractFlag().equals("No")) {
            PdfPTable tab=new PdfPTable(1);
            tab.setWidthPercentage(100);

            Font size2=new Font(Font.FontFamily.TIMES_ROMAN,7,Font.NORMAL);
            p = new Paragraph("Terms and Conditions:\n",size2);
            p.setAlignment(Element.ALIGN_MIDDLE);
            PdfPCell cel9=new PdfPCell();
            cel9.addElement(p);
            cel9.setBorder(Rectangle.NO_BORDER);
            tab.addCell(p);

            tab.addCell(new Paragraph("1. The quote rate is inclusive of Applicable taxes." + ", Taxes are subject to changes, due to Govt. policies & changes in the rate if any, - the same will be chared at the time of submission of bill.\n"
                    +"2. The images depicted in the drawings and presentations are representative only. The final design - drawings provided and approved by the client will be conceived and executed at the site.\n"
                    +"3. The above quoted rate is as per a Typical Apartment and there will be variation in rates as per the final site conditions / Measurements / Specifications / Design.\n"
                    +"4. Quote is valid for 30 Days only",fsize));
            document.add(tab);

            PdfPTable table3=new PdfPTable(2);
            table3.setWidthPercentage(100);
            table3.addCell(new Paragraph("5. Payment terms:\n" +
                    "\n" +
                    "\n",fsize));
            table3.addCell(new Paragraph("10% for finalization of the Design\n"+ "40% advance for order confirmation\n" + "50% before the Delivery of materials \n",fsize));
            document.add(table3);

            PdfPTable table4=new PdfPTable(1);
            table4.setWidthPercentage(100);
            table4.addCell(new Paragraph("6. Design Sign up fees amounting to 10% of the Budgetary quote is non-refundable. This amount will be adjusted against the final order value. Booking confirmation shall be acknowledged in the copy of this budgetary proposal.\n"
                    +"7. The 50% advance paid post approval of the design and quote cannot be refunded as the production would have be commenced.\n"
                    +"8. Warranty : 5 years of warranty against any manufacturing defect. The material specifications and brands specified are as per the approved standards of Gubbi Technologies Private Limited and covered under warranty.\n"
                    +"9. Any modifications/alterations to the proposed design will have an impact on the techno commercials of this quote and hence new drawings as well as associated commercials will be provided for by MyGubbi if the same occurs.\n"
                    +"10. Delivery shall be within 45 days from order Final Confirmation.\n"
                    +"11. Cheque / Demant Draft should be in favour of \"GUBBI TECHNOLOGIES PRIVATE LIMITED.\n",fsize));
            document.add(table4);

            p = new Paragraph("      ");
            p.setAlignment(Element.ALIGN_LEFT);
            document.add(p);

            p = new Paragraph(new Paragraph("THANKS for considering Gubbi!                                                                                                                                                     " + "\t"  + "\t" + "\t" + "\t" + "\t" +"\tAccepted (Sign) ",fsize));
            document.add(p);

        }else
        {
            p = new Paragraph("      ");
            p.setAlignment(Element.ALIGN_LEFT);
            document.add(p);

            p = new Paragraph(new Paragraph("THANKS for considering Gubbi!                                                                                                                                                     " + "\t"  + "\t" + "\t" + "\t" + "\t" +"\tAccepted (Sign) ",fsize));
            document.add(p);
        }

                    p=new Paragraph(" ");
                    document.add(p);

                    p=new Paragraph(" ");
                    document.add(p);

                    p=new Paragraph(" ");
                    document.add(p);
                    p=new Paragraph(" ");
                    document.add(p);
                    p=new Paragraph(" ");
                    document.add(p);
                    p=new Paragraph(" ");
                    document.add(p);

                    Image img = Image.getInstance("MailFooter-04.jpg");
                    img.setAlignment(Image.MIDDLE);
                    //img.scaleAbsolute(100,50);
                    //img.setWidthPercentage(5);
                    img.scaleToFit(500f, 500f);
                    document.add(img);

        if(date.after(currentDate) && quoteData.getWorksContractFlag().equals("Yes")) {
            float[] gstcolumnWidths1 = {1, 4, 1, 1, 1, 1};
            float[] gstProductCategory = {1, 1, 1, 1, 1, 1,1};



            PdfPTable designTable= new PdfPTable(gstcolumnWidths1);
            designTable.setWidthPercentage(100);
            PdfPCell designCell1 = new PdfPCell(new Paragraph("SL.NO", fsize1));
            PdfPCell designCell2 = new PdfPCell(new Paragraph("ITEM", fsize1));
            PdfPCell designCell5 = new PdfPCell(new Paragraph("GST RATE", fsize1));
            PdfPCell designCell6 = new PdfPCell(new Paragraph("GST", fsize1));
            PdfPCell designCell7 = new PdfPCell(new Paragraph("ITEM PRICE", fsize1));
            PdfPCell designCell4 = new PdfPCell(new Paragraph("TOTAL PRICE", fsize1));
            designTable.addCell(designCell1);
            designTable.addCell(designCell2);
            designTable.addCell(designCell5);
            designTable.addCell(designCell6);
            designTable.addCell(designCell7);
            designTable.addCell(designCell4);

            //document.add(designTable);



            PdfPTable individualTable= new PdfPTable(gstcolumnWidths1);
            individualTable.setWidthPercentage(100);
            PdfPCell tablecell1 = new PdfPCell(new Paragraph("SL.NO", fsize1));
            PdfPCell tablecell7 = new PdfPCell(new Paragraph("ITEM", fsize1));
            PdfPCell tablecell4 = new PdfPCell(new Paragraph("GST RATE", fsize1));
            PdfPCell tablecell5 = new PdfPCell(new Paragraph("GST", fsize1));
            PdfPCell tablecell6 = new PdfPCell(new Paragraph("ITEM PRICE", fsize1));
            PdfPCell tablecell3 = new PdfPCell(new Paragraph("TOTAL PRICE", fsize1));
            individualTable.addCell(tablecell1);
            individualTable.addCell(tablecell7);
            individualTable.addCell(tablecell4);
            individualTable.addCell(tablecell5);
            individualTable.addCell(tablecell6);
            individualTable.addCell(tablecell3);

            int count=1;
            for (GSTForProducts movableindivisualList : movableList) {

                this.createRowAndFillDataForIndividualForProducts(individualTable,count, movableindivisualList.getProducttitle(),movableindivisualList.getCategoryType(),movableindivisualList.getPriceAfterDiscount(), movableindivisualList.getPrice(), movableindivisualList.getPriceAfterTax(), movableindivisualList.getTax());
                count++;
            }
            //document.add(individualTable);

            PdfPTable gsttotalTableFormovable = new PdfPTable(gstcolumnWidths1);
            gsttotalTableFormovable.setWidthPercentage(100);
            this.createRowAndFillDataForGSTtotalProductAndAddon(gsttotalTableFormovable, "TOTAL"," ",movabletotalproductPrice,movabletotalDAP,movabletotalTaxAmt, String.valueOf(round(movabletotalPriceAfterTax, 2)));
//            document.add(gsttotalTableFormovable);

           /* p=new Paragraph( "\nNon Movable Furniture\n",fsize1);
            document.add(p);

            p=new Paragraph(" \n");
            document.add(p);*/

            PdfPTable nonMovableTable= new PdfPTable(gstcolumnWidths1);
            nonMovableTable.setWidthPercentage(100);
            PdfPCell nonMovableCell1 = new PdfPCell(new Paragraph("SL.NO", fsize1));
            PdfPCell nonMovableCell2 = new PdfPCell(new Paragraph("ITEM", fsize1));
            PdfPCell nonMovableCell5 = new PdfPCell(new Paragraph("GST RATE", fsize1));
            PdfPCell nonMovableCell6 = new PdfPCell(new Paragraph("GST", fsize1));
            PdfPCell nonMovableCell7 = new PdfPCell(new Paragraph("ITEM PRICE", fsize1));
            PdfPCell nonMovableCell4 = new PdfPCell(new Paragraph("TOTAL PRICE", fsize1));
            nonMovableTable.addCell(nonMovableCell1);
            nonMovableTable.addCell(nonMovableCell2);
            nonMovableTable.addCell(nonMovableCell5);
            nonMovableTable.addCell(nonMovableCell6);
            nonMovableTable.addCell(nonMovableCell7);
            nonMovableTable.addCell(nonMovableCell4);
            count=1;
            for (GSTForProducts nonmovableindivisualList: nonMovableList)
            {
                this.createRowAndFillDataForIndividualForProducts(nonMovableTable,count, nonmovableindivisualList.getProducttitle() + " Interiors",nonmovableindivisualList.getCategoryType(),this.round(nonmovableindivisualList.getPriceAfterDiscount(),2), nonmovableindivisualList.getPrice(), nonmovableindivisualList.getPriceAfterTax(), nonmovableindivisualList.getTax());
                count++;
            }
            //document.add(nonMovableTable);

            PdfPTable gsttotalTableFornonmovable = new PdfPTable(gstcolumnWidths1);
            gsttotalTableFornonmovable.setWidthPercentage(100);
            this.createRowAndFillDataForGSTtotalProductAndAddon(gsttotalTableFornonmovable, "TOTAL"," ",this.round(nonmovabletotalproductPrice,2),this.round(nonmovabletotalDAP,2),nonmovabletotalTaxAmt, String.valueOf(round(nonmovabletotalPriceAfterTax, 2)));
            //document.add(gsttotalTableFornonmovable);



            count=0;
            PdfPTable scwTable= new PdfPTable(gstcolumnWidths1);
            scwTable.setWidthPercentage(100);
            PdfPCell scwCell1 = new PdfPCell(new Paragraph("SL.NO", fsize1));
            PdfPCell scwCell2 = new PdfPCell(new Paragraph("ITEM", fsize1));
            //PdfPCell scwCell3 = new PdfPCell(new Paragraph("Annexure", fsize1));
            PdfPCell scwCell5 = new PdfPCell(new Paragraph("GST RATE", fsize1));
            PdfPCell scwCell6 = new PdfPCell(new Paragraph("GST", fsize1));
            PdfPCell scwCell7 = new PdfPCell(new Paragraph("ITEM PRICE", fsize1));
            PdfPCell scwCell4 = new PdfPCell(new Paragraph("TOTAL PRICE", fsize1));
            //PdfPCell gstCell7 = new PdfPCell(new Paragraph("CURRENT PRICE AFTER TAX",fsize1));
            scwTable.addCell(scwCell1);
            scwTable.addCell(scwCell2);
            //scwTable.addCell(scwCell3);
            //gstTable.addCell(gstCell3)
            scwTable.addCell(scwCell5);
            scwTable.addCell(scwCell6);
            scwTable.addCell(scwCell7);
            scwTable.addCell(scwCell4);
            count=1;
            for(GSTForProducts scwList: this.scwList)
            {
                this.createRowAndFillDataForIndividualForProducts(scwTable,count, scwList.getProducttitle(),scwList.getCategoryType(), scwList.getPriceAfterDiscount(), scwList.getPrice(), scwList.getPriceAfterTax(), scwList.getTax());
                count++;
            }
            //document.add(scwTable);

            PdfPTable gsttotalTableForscw = new PdfPTable(gstcolumnWidths1);
            gsttotalTableForscw.setWidthPercentage(100);
            this.createRowAndFillDataForGSTtotalProductAndAddon(gsttotalTableForscw, "TOTAL"," ",scwtotalproductPrice,scwtotalDAP,scwtotalTaxAmt, String.valueOf(round(scwtotalPriceAfterTax, 2)));
            //document.add(gsttotalTableForscw);

            /*float[] gsttotalcolumnWidths1 = {1, 1, 1, 1, 1, 1};
            PdfPTable gsttotalTable = new PdfPTable(gsttotalcolumnWidths1);
            gsttotalTable.setWidthPercentage(100);
            this.createRowAndFillDataForGSTtotal(gsttotalTable, "Total", totalproductPrice, totalDAP, totalTaxAmt, String.valueOf(round(totalPriceAfterTax, 2)));
            document.add(gsttotalTable);*/

            /*PdfPTable gsttotalTableForAddon = new PdfPTable(gstProductCategory);
            gsttotalTableForAddon.setWidthPercentage(100);
            this.createRowAndFillDataForGSTtotalProductAndAddon(gsttotalTableForAddon, "Total"," ", producttotalPrice, producttotalDAP, producttotalTaxAmt, String.valueOf(round(producttotalPriceAfterTax, 2)));
            document.add(gsttotalTableForAddon);

            p=new Paragraph(" ");
            document.add(p);

            p=new Paragraph( "ADDONS\n",fsize1);
            document.add(p);

            p=new Paragraph(" ");
            document.add(p);

            PdfPTable individualTableforAddon= new PdfPTable(gstProductCategory);
            individualTableforAddon.setWidthPercentage(100);
            PdfPCell tableAddoncell1 = new PdfPCell(new Paragraph("SI", fsize1));
            PdfPCell tableAddoncell2 = new PdfPCell(new Paragraph("Product Category", fsize1));
            PdfPCell tableAddoncell3 = new PdfPCell(new Paragraph("Annexure", fsize1));
            PdfPCell tableAddoncell4 = new PdfPCell(new Paragraph("Price After discount", fsize1));
            PdfPCell tableAddoncell5 = new PdfPCell(new Paragraph("TAX %", fsize1));
            PdfPCell tableAddoncell6 = new PdfPCell(new Paragraph("TAX Amount", fsize1));
            PdfPCell tableAddoncell7 = new PdfPCell(new Paragraph("Price After Tax", fsize1));
            //PdfPCell gstCell7 = new PdfPCell(new Paragraph("CURRENT PRICE AFTER TAX",fsize1));
            individualTableforAddon.addCell(tableAddoncell1);
            individualTableforAddon.addCell(tableAddoncell2);
            individualTableforAddon.addCell(tableAddoncell3);
            //gstTable.addCell(gstCell3);
            individualTableforAddon.addCell(tableAddoncell4);
            individualTableforAddon.addCell(tableAddoncell5);
            individualTableforAddon.addCell(tableAddoncell6);
            individualTableforAddon.addCell(tableAddoncell7);

            for (GSTForProducts movableindivisualList : movableList) {
                this.createRowAndFillDataForIndividualForAddon(individualTableforAddon,movableindivisualList.getProductFlag(), movableindivisualList.getCategory(),movableindivisualList.getCategoryType(), movableindivisualList.getPriceAfterDiscount(), movableindivisualList.getPrice(), movableindivisualList.getPriceAfterTax(), movableindivisualList.getTax());
            }
            for (GSTForProducts nonmovableindivisualList: nonMovableList)
            {
                this.createRowAndFillDataForIndividualForAddon(individualTableforAddon,nonmovableindivisualList.getProductFlag(), nonmovableindivisualList.getCategory(),nonmovableindivisualList.getCategoryType(), nonmovableindivisualList.getPriceAfterDiscount(), nonmovableindivisualList.getPrice(), nonmovableindivisualList.getPriceAfterTax(), nonmovableindivisualList.getTax());
            }
            for(GSTForProducts scwList: scwList)
            {
                this.createRowAndFillDataForIndividualForAddon(individualTableforAddon,scwList.getProductFlag(), scwList.getCategory(),scwList.getCategoryType(), scwList.getPriceAfterDiscount(), scwList.getPrice(), scwList.getPriceAfterTax(), scwList.getTax());
            }

            document.add(individualTableforAddon);


            PdfPTable gsttotalTableForProduct = new PdfPTable(gstProductCategory);
            gsttotalTableForProduct.setWidthPercentage(100);
            this.createRowAndFillDataForGSTtotalProductAndAddon(gsttotalTableForProduct, "Total","" ,addontotalproductPrice, addontotalDAP, addontotalTaxAmt, String.valueOf(round(addontotalPriceAfterTax, 2)));
            document.add(gsttotalTableForProduct);*/


            PdfPTable gstTable = new PdfPTable(gstcolumnWidths1);
            gstTable.setWidthPercentage(100);
            PdfPCell gstCell1 = new PdfPCell(new Paragraph("SL.NO", fsize1));
            PdfPCell gstCell2 = new PdfPCell(new Paragraph("Annexure", fsize1));
            //PdfPCell gstCell3 = new PdfPCell(new Paragraph("Products Price", fsize1));
            PdfPCell gstCell4 = new PdfPCell(new Paragraph("TOTAL PRICE", fsize1));
            PdfPCell gstCell5 = new PdfPCell(new Paragraph("GST rate", fsize1));
            PdfPCell gstCell7 = new PdfPCell(new Paragraph("GST", fsize1));
            PdfPCell gstCell6 = new PdfPCell(new Paragraph("ITEM PRICE", fsize1));
            //PdfPCell gstCell7 = new PdfPCell(new Paragraph("CURRENT PRICE AFTER TAX",fsize1));
            gstTable.addCell(gstCell1);
            gstTable.addCell(gstCell2);
            //gstTable.addCell(gstCell3);
            gstTable.addCell(gstCell4);
            gstTable.addCell(gstCell5);
            gstTable.addCell(gstCell7);
            gstTable.addCell(gstCell6);

            this.getfinalGSTList(designServiceList, "Design Services");
            /*for(GSTForProducts movableobjects :movableList)
            {
                this.createRowAndFillDataForGST(gstTable, movableobjects.getCategory(), movableobjects.getPriceAfterDiscount(), movableobjects.getPrice(), movableobjects.getPriceAfterTax(), movableobjects.getTax());
            }*/
            this.getfinalGSTList(movableList, "MF");
            /*for(GSTForProducts movableobjects :nonMovableList)
            {
                this.createRowAndFillDataForGST(gstTable, movableobjects.getCategory(), movableobjects.getPriceAfterDiscount(), movableobjects.getPrice(), movableobjects.getPriceAfterTax(), movableobjects.getTax());
            }*/
            this.getfinalGSTList(nonMovableList, "NMF");
            this.getfinalGSTList(scwList, "SCW");

            for (GSTForProducts finalList : finalmovableList) {
                this.createRowAndFillDataForGST(gstTable, finalList.getCategory(), finalList.getPriceAfterDiscount(), finalList.getPrice(), finalList.getPriceAfterTax(), finalList.getTax());
            }
            //document.add(gstTable);

            float[] gsttotalcolumnWidths1 = {1, 4, 1, 1, 1, 1};
            PdfPTable gsttotalTable = new PdfPTable(gsttotalcolumnWidths1);
            gsttotalTable.setWidthPercentage(100);
            this.createRowAndFillDataForGSTtotal(designTable, "TOTAL", totalproductPrice, totalDAP, totalTaxAmt, String.valueOf(round(totalPriceAfterTax, 2)));
            this.createRowAndFillForDesign(designTable, "Design/Consultation Services"," ",this.round(designtotalproductPrice,2),this.round(designtotalDAP,2),designtotalTaxAmt, String.valueOf(round(designtotalPriceAfterTax, 2)));
            this.createRowAndFillDataForGSTtotalProductAndAddon(designTable, "TOTAL"," ",this.round(totalproductPrice+designtotalproductPrice,2),this.round(totalDAP+designtotalDAP,2),this.round(totalTaxAmt+designtotalTaxAmt,2), String.valueOf(this.round(totalPriceAfterTax+designtotalPriceAfterTax,2)));


           // document.add(gsttotalTable);



            float[] gstfinalcolumnWidths1 = {1, 4, 1, 1, 1, 1};
            PdfPTable finalTable = new PdfPTable(gstfinalcolumnWidths1);
            finalTable.setWidthPercentage(100);
            PdfPCell gstfinalCell1 = new PdfPCell(new Paragraph("SL.NO", fsize1));
            PdfPCell gstfinalCell2 = new PdfPCell(new Paragraph("Title", fsize1));
            /*PdfPCell gstfinalCell3 = new PdfPCell(new Paragraph("Products Price", fsize1));*/
            PdfPCell gstfinalCell4 = new PdfPCell(new Paragraph("TOTAL PRICE", fsize1));
            PdfPCell gstfinalCell5 = new PdfPCell(new Paragraph("GST rate", fsize1));
            PdfPCell gstfinalCell7 = new PdfPCell(new Paragraph("GST", fsize1));
            PdfPCell gstfinalCell6 = new PdfPCell(new Paragraph("ITEM PRICE", fsize1));
            //PdfPCell gstCell7 = new PdfPCell(new Paragraph("CURRENT PRICE AFTER TAX",fsize1));
            finalTable.addCell(gstfinalCell1);
            finalTable.addCell(gstfinalCell2);
            //finalTable.addCell(gstfinalCell3);
            finalTable.addCell(gstfinalCell4);
            finalTable.addCell(gstfinalCell5);
            finalTable.addCell(gstfinalCell7);
            finalTable.addCell(gstfinalCell6);
            this.createRowAndFillDataForGSTforFinal(finalTable, "Sum of Products and Service Billed @", set1totalproductPrice, set1totalDAP, "18%", set1totalTaxAmt, set1totalPriceAfterTax);
            this.createRowAndFillDataForGSTforFinal(finalTable, "Sum of Products and Service Billed @", set2totalproductPrice, set2totalDAP, "28%", set2totalTaxAmt, set2totalPriceAfterTax);
            //document.add(finalTable);

            float[] gtcolumnWidths1 = {1,4 , 1, 1, 1, 1};
            PdfPTable gstTable1 = new PdfPTable(gtcolumnWidths1);
            gstTable1.setWidthPercentage(100);
            this.createRowAndFillDataForGSTtotal(gstTable1, "TOTAL", totalproductPrice, totalDAP, totalTaxAmt, String.valueOf(round(totalPriceAfterTax, 2)));
            //document.add(gstTable1);

            if(quoteData.fromVersion.equals("1.0") || quoteData.fromVersion.startsWith("0."))
            {
                /*p = new Paragraph("\nQuotation Summary", fsize1);
                document.add(p);

                p = new Paragraph(" ");
                document.add(p);

                document.add(finalTable);
                document.add(gstTable1);*/

            }
            else {
               // Make scope of service/s response as 'yes' for the following - Electrical in SpaceType_Room Living Room:electrical laboure

                p = new Paragraph("\n");
                document.add(p);

                p = new Paragraph("\n");
                document.add(p);

                document.newPage();

                p = new Paragraph("PRICE BREAKUP",fsize3);
                p.setAlignment(Element.ALIGN_CENTER);
                //fsize3.setColor(BaseColor.GRAY);
                document.add(p);

                p=new Paragraph(" ");
                document.add(p);

                p=new Paragraph("The detailed price break-up(post Discount) with the split-up of design, consultancy and GST charges are as follows: ",fsize);
                document.add(p);


                p=new Paragraph( "\n A. Furniture And Appliances\n",fsize1);
                document.add(p);

                p=new Paragraph("  ");
                document.add(p);

                document.add(individualTable);
                document.add(gsttotalTableFormovable);

                p=new Paragraph( "\nB. Interior Works\n",fsize1);
                document.add(p);

                p=new Paragraph(" ");
                document.add(p);
                document.add(nonMovableTable);
                document.add(gsttotalTableFornonmovable);


                p=new Paragraph( "\n C. Services And Civil Works\n",fsize1);
                document.add(p);

                p=new Paragraph(" ");
                document.add(p);
                document.add(scwTable);
                document.add(gsttotalTableForscw);

                p=new Paragraph(" ");
                document.add(p);

                p=new Paragraph("\nD. Summary\n",fsize1);
                document.add(p);

                p=new Paragraph(" ");
                document.add(p);
                document.add(gsttotalTable);
                document.add(designTable);


            }

        }
        document.close();
        }
        catch(Exception e)
        {
            LOG.info("error" +e.getMessage());
            e.printStackTrace();
        }
    }


    public void fillAssembledProducts(PdfPTable tabname)
    {

        List<AssembledProductInQuote> assembledProducts = this.quoteData.getAssembledProducts();
        if (assembledProducts.isEmpty())
        {
            return ;
        }

        int sequenceNumber = 1;
        for (AssembledProductInQuote product : assembledProducts)
        {
            LOG.info("product color code " +product.getColorGroupCode() + "hinges in quote " +product.getHingeTitle());
            this.fillAssembledProductInfo(tabname,sequenceNumber, product);
            sequenceNumber++;
        }
    }

    private void fillAssembledProductInfo(PdfPTable tabname,int sequenceNumber, AssembledProductInQuote product)
    {
        int num;
        this.createProductTitleRow(tabname,"A." + String.valueOf(sequenceNumber), product.getTitle());

        series="A." +String.valueOf(sequenceNumber) + ".";
        this.fillAssembledProductUnits(tabname,product,series);

        amt=product.getAmountWithoutAddons();
        String unitSequenceLetter="";
        if(!(product.getCatagoryName().equals("Kitchen") || product.getCatagoryName().equals("Wardrobe"))) {
            unitSequenceLetter =
                    ALPHABET_SEQUENCE[1];
        }
        else
        {
            if(product.getCatagoryName().equals("Kitchen")) {
                unitSequenceLetter = ALPHABET_SEQUENCE[unitSequence];
            }
            else if(product.getCatagoryName().equals("Wardrobe"))
            {
                unitSequenceLetter = ALPHABET_SEQUENCE[wunitSequence];
            }
        }

        this.fillAssembledProductAccessories(tabname,product.getAccessories(), unitSequenceLetter);
        this.createCellWithData(tabname,"Total Cost",product.getAmountWithoutAddons());

    }

    private void createProductTitleRow(PdfPTable tabname,String index, String title)
    {
        Font size1=new Font(Font.FontFamily.TIMES_ROMAN,8,Font.BOLD);
        Font size2=new Font(Font.FontFamily.TIMES_ROMAN,8,Font.BOLD|Font.UNDERLINE);

        PdfPCell cell1=new PdfPCell();
        Paragraph Pindex=new Paragraph(index,size1);
        Pindex.setAlignment(Element.ALIGN_CENTER);
        cell1.addElement(Pindex);
        tabname.addCell(cell1);

        PdfPCell cell2=new PdfPCell();
        Paragraph paragraph=new Paragraph(title,size1);
        cell2.addElement(paragraph);
        tabname.addCell(cell2);


        PdfPCell cell = new PdfPCell();
        Paragraph p=new Paragraph("");
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        cell.setColspan(3);
        tabname.addCell(cell);
    }

    private void createProductTitleRowForAddon(PdfPTable tabname,String index, String title)
    {
        Font size1=new Font(Font.FontFamily.TIMES_ROMAN,8,Font.BOLD);
        Font size2=new Font(Font.FontFamily.TIMES_ROMAN,8,Font.BOLD|Font.UNDERLINE);

        PdfPCell cell1=new PdfPCell();
        Paragraph Pindex=new Paragraph(index,size1);
        Pindex.setAlignment(Element.ALIGN_CENTER);
        cell1.addElement(Pindex);
        tabname.addCell(cell1);

        PdfPCell cell2=new PdfPCell();
        Paragraph paragraph=new Paragraph(title,size1);
        cell2.addElement(paragraph);
        tabname.addCell(cell2);


        PdfPCell cell = new PdfPCell();
        Paragraph p=new Paragraph("");
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        cell.setColspan(4);
        tabname.addCell(cell);
    }

    private void specificationRow(PdfPTable tabname,String index,String title)
    {
        Font size1=new Font(Font.FontFamily.TIMES_ROMAN,8,Font.NORMAL);
        Font size2=new Font(Font.FontFamily.TIMES_ROMAN,8,Font.BOLD|Font.UNDERLINE);

        PdfPCell cell1=new PdfPCell();
        Paragraph Pindex=new Paragraph(index,size1);
        Pindex.setAlignment(Element.ALIGN_CENTER);
        cell1.addElement(Pindex);
        tabname.addCell(cell1);

        PdfPCell cell2=new PdfPCell();
        Paragraph paragraph=new Paragraph(title,size1);
        cell2.addElement(paragraph);
        tabname.addCell(cell2);


        PdfPCell cell = new PdfPCell();
        Paragraph p=new Paragraph("");
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        cell.setColspan(3);
        tabname.addCell(cell);

    }
    private boolean isNotNullorEmpty(String s){
        if ((s == null )||( s.length() == 0)){
            return false;
        }else
            return true;
    }
    private int customFunction(List<QuotationPDFCreator.customeclass> li,int unitSequence)
    {
        //int unitSequence = 0;
        int num=0;
        for(int index=0;index<li.size();index++)
        {
            if (li.get(index).getAmount()==0)
            {
                return num;
            }
            else if (li.get(index).getTitle().contains("Kitchen Base Unit") || li.get(index).getTitle().contains("Kitchen Tall Unit") || li.get(index).getTitle().contains("Kitchen Wall Unit") || li.get(index).getTitle().contains("Kitchen Lofts"))
            {
                num += 1;
                if(li.get(index).getDimension().equals("")) {
                    this.createSubHeadingRow(li.get(index).getTabName(), series + ALPHABET_SEQUENCE[unitSequence], li.get(index).getTitle()); // + " - " + li.get(index).getDimension());
                }
                else
                {
                this.createSubHeadingRow(li.get(index).getTabName(), series + ALPHABET_SEQUENCE[unitSequence], li.get(index).getTitle() + " - " +li.get(index).getDimension());
                }
                String fmaterial = li.get(index).getFinishmaterial().replaceAll("\n", "");
                //LOG.info("finish material " +li.get(index).getFinishtype()  +"finish type" +fmaterial);
                if(li.get(index).getTitle().contains("Kitchen Base Unit") || li.get(index).getTitle().contains("Kitchen Tall Unit")) {

                    String text ="unit consists of " + li.get(index).getModulecount() +
                           " modules as per design provided.\n" + "Carcass: " + li.get(index).getBasecarcass() + "\n" +
                           "Finish Material: " +li.get(index).getFinishtype() + " , Finish Type : " + fmaterial+
                           ((isNotNullorEmpty(li.get(index).getColorGroupCode())) ?  "\n" +"Color : " +li.get(index).getColorGroupCode():"" )+
                           ((isNotNullorEmpty(li.get(index).getHingeType())) ? "\n" + "Hinge : " +li.get(index).getHingeType():"");

                    this.createRowAndFillData(li.get(index).getTabName(), null, text, 1.0, li.get(index).getAmount(), 0.0);

                }else {
                    String text = "unit consists of " + li.get(index).getModulecount() +
                            " modules as per design provided.\n" +  "Carcass: " + li.get(index).getWallcarcass() + "\n" +
                            "Finish Material: " + li.get(index).getFinishtype() + " , Finish Type : " + fmaterial +
                            ((isNotNullorEmpty(li.get(index).getColorGroupCode())) ?  "\n" +"Color : " +li.get(index).getColorGroupCode():"" )+
                            ((isNotNullorEmpty(li.get(index).getHingeType())) ? "\n" + "Hinge : " +li.get(index).getHingeType():"");

                    this.createRowAndFillData(li.get(index).getTabName(), null, text, 1.0, li.get(index).getAmount(), 0.0);
                }

                unitSequence++;
                if (unitSequence == ALPHABET_SEQUENCE.length) unitSequence = 0;
            }
            else
            {
                num += 1;
                if(li.get(index).getTitle().contains("Wardrobe"))
                {
                    this.createSubHeadingRow(li.get(index).getTabName(), series + ALPHABET_SEQUENCE[unitSequence], li.get(index).getTitle()+ " - " +li.get(index).getDimension());
                }
                else {
                this.createSubHeadingRow(li.get(index).getTabName(), series + ALPHABET_SEQUENCE[unitSequence], li.get(index).getTitle());
                }
                String fmaterial = li.get(index).getFinishmaterial().replaceAll("\n", "");

                String text = "Carcass: " + li.get(index).getBasecarcass() + "\n" +
                        "Finish Material: " + li.get(index).getFinishtype() + " , Finish Type : " +fmaterial +
                        ((isNotNullorEmpty(li.get(index).getColorGroupCode())) ?  "\n" +"Color : " +li.get(index).getColorGroupCode():"" )+
                        ((isNotNullorEmpty(li.get(index).getHingeType())) ? "\n" + "Hinge : " +li.get(index).getHingeType():"");

                this.createRowAndFillData(li.get(index).getTabName(), null, text, 1.0, li.get(index).getAmount(), 0.0);
                unitSequence++;
                if (unitSequence == ALPHABET_SEQUENCE.length) unitSequence = 0;
            }
        }
        return num;
    }

    private void fillAssembledProductUnits(PdfPTable tabname,AssembledProductInQuote product,String series)
    {
        wunitSequence = 0;
        String caption="",caption1="",caption2="",caption3="",caption4="",captionLoft="",captionWardrobe="";
        String cname=product.getCatagoryName();
        String Wcaption="";
        String baseDimesion="",WallDimesion="",TallDimesion="",loftDimesion="",wardrobeDimesion="";
        int KBmodulecount=0,KWmoduleCount=0,KTmoduleCount=0,KLmoduleCount=0,SW1modulecount=0,WWmodulecount=0,WW1modulecount=0;
        String KBbasecarcass="",KWbasecarcass="",KTbasecarcass="",KLbasecarcass="",SW1basecarcass="",WWbasecarcass="",WW1basecarcass="";
        String KBWallcarcass="",KWwallcarcass="",KTwallcarcass="",KLwallcarcass="",SW1wallcarcass="",WWwallcarcass="",WW1wallcarcass="";
        String KBfinishmaterial="",KWfinishmaterial="",KTfinishmaterial="",KLfinishmaterial="",SW1finishmaterial="",WWfinishmaterial="",WW1finishmaterial="";
        String KBcolorgroupCode="",KWcolorGroupCode="",KTcolorGroupCode="",KLcolorGroupCode="",SW1colorGroupcode="",WWcolorGroupCode="",WW1colorGroupcode="";
        String KBhinge="",KWhinge="",KThinge="",KLhinge="",SW1hinge="",WWhinge="",WW1hinge="";
        String KBfinishtype="",KWfinishtype="",KTfinishtype="",KLfinishtype="",SW1finishtype="",WWfinishtype="",WW1finishtype="";
        double KBamount=0,KWamount=0,KTamount=0,KLamount=0,SW1amount=0,WWamount=0,WW1amount=0;

        int kbwidthSum=0,kbdepthSum=0,kbheightSum=0;
        int kwwidthSum=0,kwdepthSum=0,kwheightSum=0;
        int ktwidthSum=0,ktdepthSum=0,ktheightSum=0;
        int klwidthSum=0,kldepthSum=0,klheightSum=0;
        int wrwidthSum=0,wrdepthSum=0, wrheightSum=0;

        unitSequence = 0;
        String basewidth="",wallwidth="",tallwidth="",loftwidth="",wardrobewidth="",wardrobeLoftwidth="";
        List<String> kwList= new ArrayList<String>();
        List<String> kbList=new ArrayList<String>();
        List<String> ktList=new ArrayList<String>();
        List<String> klList=new ArrayList<String>();
        List<String> kwaList=new ArrayList<String>();

        String finish=product.getProduct().getFinishType();
        LOG.info("Finish " +finish);

        for(AssembledProductInQuote.Unit unit:product.getUnits())
        {
            if(cname.equals("Wardrobe"))
            {
                if(unit.moduleCategory.contains("S - Sliding Mechanism"))
                {
                    if(unit.mgCode.equals("MG-SF2D-22"))
                    {
                        Wcaption="Sliding Wardrobe - TopLine 22 Sliding fitting 2 Door";
                    }else if(unit.mgCode.equals("MG-SF3D-22"))
                    {
                        Wcaption="Sliding Wardrobe - TopLine 22 Sliding fitting 3 Door";
                    }else if(unit.mgCode.equals("MG-SF2D-XL"))
                    {
                        Wcaption="Sliding Wardrobe - TopLine XL 2 Door";
                    }else
                    {
                        Wcaption="Sliding Wardrobe - TopLine XL 3 Door";
                    }
                }
            }
            if(finish.contains("Aristo"))
            {
                Wcaption="Aristo Wardrobe";
            }
        }
        LOG.info("Wcaption " +Wcaption);
        for (AssembledProductInQuote.Unit unit : product.getUnits())
        {
            if(cname.equals("Kitchen") )
            {
                if(     unit.moduleCategory.contains("H - Panel") ||
                        unit.moduleCategory.contains("Base Units") ||
                        unit.moduleCategory.contains("N - Base Units") ||
                        unit.moduleCategory.contains("N - Drawer Units") ||
                        unit.moduleCategory.contains("N - Drawer") ||
                        unit.moduleCategory.contains("N - Open Units") ||
                        unit.moduleCategory.contains("N - Panelling") ||
                        unit.moduleCategory.contains("N - WoodWork Add On") ||
                        unit.moduleCategory.contains("S - Kitchen Base Corner Units")||
                        unit.moduleCategory.contains("S - Kitchen Base Drawer Units") ||
                        unit.moduleCategory.contains("S - Kitchen Base Shutter Units") ||
                        unit.moduleCategory.contains("S - Kitchen Panels") ||
                        unit.moduleCategory.contains("S - Sliding Mechanism") ||
                        unit.moduleCategory.contains("S - Sliding Wardrobe 2100") ||
                        unit.moduleCategory.contains("S - Sliding Wardrobe 2400") ||
                        unit.moduleCategory.contains("S - Storage Module Base Unit") ||
                        unit.moduleCategory.contains("S - Wardrobe Panels") ||
                        unit.moduleCategory.contains("S - Bathroom Vanity") ||
                        unit.moduleCategory.contains("S - Hinged Wardrobe 2100") ||
                        unit.moduleCategory.contains("S - Kitchen Wall Sliding Units") ||
                        unit.moduleCategory.contains("S - Hinged Wardrobe 2400") )
                {
                    if(     unit.moduleCategory.contains("N - Base Units") ||
                            unit.moduleCategory.contains("S - Kitchen Base Corner Units")||
                            unit.moduleCategory.contains("S - Kitchen Base Drawer Units") ||
                            unit.moduleCategory.contains("S - Kitchen Base Shutter Units") ||
                            unit.moduleCategory.contains("S - Kitchen Wall Sliding Units") ||
                            unit.moduleCategory.contains("S - Storage Module Base Unit")) {

                        //LOG.info("Module count " +unit.moduleCount);
                        KBmodulecount += unit.moduleCount;
                        String width = unit.getDimensions();
                        basewidth=  basewidth + " , " +width;
                        kbList.add(new String(width));

                        if(unit.moduleCategory.contains("S - Kitchen Base Corner Units")||
                                unit.moduleCategory.contains("S - Kitchen Base Drawer Units") ||
                                unit.moduleCategory.contains("S - Kitchen Base Shutter Units") ||
                                unit.moduleCategory.contains("S - Storage Module Base Unit") ||
                                unit.moduleCategory.contains("S - Kitchen Wall Sliding Units") ||
                                unit.moduleCategory.contains("Base unit") )
                        {
                            kbwidthSum = kbwidthSum + unit.getWidth();
                            kbheightSum = unit.getHeight();
                            kbdepthSum = unit.getDepth();
                            baseDimesion = kbwidthSum + " x " + kbdepthSum + " x " + kbheightSum;
                        }
                    }
                    KBbasecarcass = product.getProduct().getBaseCarcassCode();
                    KBWallcarcass = product.getProduct().getWallCarcassCode();
                    KBfinishmaterial = ModuleDataService.getInstance().getFinish(product.getProduct().getFinishCode()).getTitle();
                    KBfinishtype = product.getProduct().getFinishType();
                    KBcolorgroupCode=product.getProduct().getColorgroupCode();
                    KBhinge=product.getProduct().getHingeType();
                   // LOG.info("title" +unit.moduleCategory + "amount" +unit.amount);
                    KBamount += unit.amount;

                    if(cname.equals("Kitchen"))
                    {
                        caption="Kitchen Base Unit"; // + " - " +unit.getDimensions();
                    }
                }
                else if (unit.moduleCategory.contains("Wall unit")||
                        unit.moduleCategory.contains("S - Kitchen Wall Corner Units")||
                        unit.moduleCategory.contains("S - Kitchen Wall Flap Up Units") ||
                        unit.moduleCategory.contains("S - Kitchen Wall Open Units") ||
                        unit.moduleCategory.contains("S - Kitchen Wall Shutter Units")||
                        unit.moduleCategory.contains("S - Storage Module Wall Unit")||
                        unit.moduleCategory.contains("S - Wall Open Units") ||
                        unit.moduleCategory.contains ("N - Wall Units") )
                {

                    KWmoduleCount += unit.moduleCount;
                    KWbasecarcass = product.getProduct().getBaseCarcassCode();
                    KWwallcarcass = product.getProduct().getWallCarcassCode();
                    KWfinishmaterial = ModuleDataService.getInstance().getFinish(product.getProduct().getFinishCode()).getTitle();
                    KWfinishtype = product.getProduct().getFinishType();
                    KWcolorGroupCode=product.getProduct().getColorgroupCode();
                    KWhinge=product.getProduct().getHingeType();
                    KWamount += unit.amount;

                    if(!unit.moduleCategory.contains ("N - Wall Units")) {
                        kwwidthSum = kwwidthSum + unit.getWidth();
                        kwheightSum = unit.getHeight();
                        kwdepthSum = unit.getDepth();

                        WallDimesion = kwwidthSum + " x " + kwdepthSum + " x " + kwheightSum;
                    }

                    if(cname.equals("Kitchen"))
                    {
                        caption1="Kitchen Wall Unit";
                    }

                    String width = unit.getDimensions();
                    wallwidth=wallwidth + " , " +width ;
                    kwList.add(new String(width));
                }
                else if (unit.moduleCategory.contains("Tall unit") || unit.moduleCategory.contains("S - Kitchen Tall Units") ||  unit.moduleCategory.contains ("N - Tall/Semi Tall Units"))
                {
                    KTmoduleCount += unit.moduleCount;
                    KTbasecarcass = product.getProduct().getBaseCarcassCode();
                    KTwallcarcass = product.getProduct().getWallCarcassCode();
                    KTfinishmaterial = ModuleDataService.getInstance().getFinish(product.getProduct().getFinishCode()).getTitle();
                    KTfinishtype = product.getProduct().getFinishType();
                    KTcolorGroupCode=product.getProduct().getColorgroupCode();
                    KThinge=product.getProduct().getHingeType();
                    KTamount += unit.amount;

                    if(cname.equals("Kitchen"))
                    {
                        caption2="Kitchen Tall Unit";
                    }
                    if(!unit.moduleCategory.contains ("N - Tall/Semi Tall Units")) {
                    String width = unit.getDimensions();
                    tallwidth=tallwidth + " , " +width;
                    ktList.add(new String(width));

                        ktwidthSum = ktwidthSum + unit.getWidth();
                        ktheightSum = unit.getHeight();
                        ktdepthSum = unit.getDepth();

                        TallDimesion = ktwidthSum + " x " + ktdepthSum + " x " + ktheightSum;
                    }

                    String width = unit.getDimensions();
                    tallwidth=tallwidth + " , " +width;
                    ktList.add(new String(width));

                }

                else if(unit.moduleCategory.contains("S - Kitchen Loft Units") || unit.moduleCategory.contains("S - Sliding Wardrobe with Loft") || unit.moduleCategory.contains("S - Wardrobe Lofts"))
                {
                    KLmoduleCount += unit.moduleCount;
                    KLbasecarcass = product.getProduct().getBaseCarcassCode();
                    KLwallcarcass = product.getProduct().getWallCarcassCode();
                    KLfinishmaterial = ModuleDataService.getInstance().getFinish(product.getProduct().getFinishCode()).getTitle();
                    KLfinishtype = product.getProduct().getFinishType();
                    KLcolorGroupCode=product.getProduct().getColorgroupCode();
                    KLhinge=product.getHingeTitle();
                    KLamount += unit.amount;
                    if(cname.equals("Kitchen"))
                    {
                        caption3="Kitchen Lofts" ;
                    }
                    String width = unit.getDimensions();
                    loftwidth=loftwidth + " , " +width;
                    klList.add(new String(width));

                    klwidthSum=klwidthSum+unit.getWidth();
                    klheightSum=unit.getHeight();
                    kldepthSum=unit.getDepth();

                    loftDimesion=klwidthSum + " x " + kldepthSum + " x " +klheightSum;
                }
            }
            else if(cname.equals("Wardrobe"))
            {
               // LOG.info("wardrobe category" + unit.moduleCategory);

                    if(     unit.moduleCategory.contains("H - Panel") ||
                            unit.moduleCategory.contains("N - Base Units") ||
                            unit.moduleCategory.contains("N - Drawer Units") ||
                            unit.moduleCategory.contains("N - Drawer") ||
                            unit.moduleCategory.contains("N - Open Units") ||
                            unit.moduleCategory.contains("N - Panelling") ||
                            unit.moduleCategory.contains("N - WoodWork Add On") ||
                            unit.moduleCategory.contains("N - Wall Units") ||
                            unit.moduleCategory.contains("S - Sliding Mechanism") ||
                            unit.moduleCategory.contains("N - Tall/Semi Tall Units") ||
                            unit.moduleCategory.contains("N - Wall Units") ||
                            unit.moduleCategory.contains("S - Hinged Wardrobe 2100") ||
                            unit.moduleCategory.contains("S - Hinged Wardrobe 2400") ||
                            unit.moduleCategory.contains("S - Sliding Wardrobe 2100") ||
                            unit.moduleCategory.contains("S - Sliding Wardrobe 2400") ||
                            unit.moduleCategory.contains("S - Storage Module Wall Unit") ||
                            unit.moduleCategory.contains("S - Storage Module Base Unit") ||
                            unit.moduleCategory.contains("S - Wardrobe Panels") ||
                            unit.moduleCategory.contains("S - Sliding Wardrobe with Loft"))
                {
                    WWmodulecount += unit.moduleCount;
                    WWbasecarcass = product.getProduct().getBaseCarcassCode();
                    WWwallcarcass = product.getProduct().getWallCarcassCode();
                    WWfinishmaterial = ModuleDataService.getInstance().getFinish(product.getProduct().getFinishCode()).getTitle();
                    WWfinishtype = product.getProduct().getFinishType();
                    WWcolorGroupCode=product.getProduct().getColorgroupCode();
                    WWhinge=product.getProduct().getHingeType();
                    WWamount += unit.amount;
                    captionWardrobe="Wardrobe";

                    if(Wcaption.contains("Sliding Wardrobe"))
                    {
                        captionWardrobe = Wcaption;
                    }else if (Wcaption.equals("Aristo Wardrobe"))
                    {
                        captionWardrobe="Aristo Wardrobe";
                    }else
                    {
                        captionWardrobe="Hinged Wardrobe";
                    }


                    if(!(unit.moduleCategory.contains ("N")|| unit.moduleCategory.contains("S - Wardrobe Panels") || unit.moduleCategory.contains ("H - Panel"))) {

                        String width = unit.getDimensions();
                        wardrobewidth = wardrobewidth + " , " + width;
                        kwaList.add(new String(width));
                    }
                }
                else
                {
                    WW1modulecount += unit.moduleCount;
                    WW1basecarcass = product.getProduct().getBaseCarcassCode();
                    WW1wallcarcass = product.getProduct().getWallCarcassCode();
                    WW1finishmaterial = ModuleDataService.getInstance().getFinish(product.getProduct().getFinishCode()).getTitle();
                    WW1finishtype = product.getProduct().getFinishType();
                    WW1colorGroupcode=product.getProduct().getColorgroupCode();
                    WW1hinge=product.getProduct().getHingeType();
                    WW1amount += unit.amount;
                    captionLoft="Wardrobe Loft";
                    String width = unit.getDimensions();
                    wardrobeLoftwidth = wardrobeLoftwidth + " , " + width;
                    kwaList.add(new String(width));
                }
            }
            else if(cname.equals("Storage Modules") || cname.equals("wallpanelling")  || cname.equals("oswalls")  || cname.equals("sidetables") ||  cname.equals("shoerack") ||  cname.equals("Bathroom Vanity") ||  cname.equals("tvunit") ||  cname.equals("barunit") || cname.equals("bookshelf") ||  cname.equals("crunit") ||  cname.equals("wallunits") || cname.equals("codrawers") || cname.equals("usstorage") || cname.equals("poojaunit") || cname.equals("studytable") || cname.equals("others"))
            {
                SW1modulecount += unit.moduleCount;
                SW1basecarcass = product.getProduct().getBaseCarcassCode();
                SW1wallcarcass = product.getProduct().getWallCarcassCode();
                SW1finishmaterial = ModuleDataService.getInstance().getFinish(product.getProduct().getFinishCode()).getTitle();
                SW1finishtype = product.getProduct().getFinishType();
                SW1colorGroupcode=product.getProduct().getColorgroupCode();
                SW1hinge=product.getProduct().getHingeType();
                SW1amount += unit.amount;

                if(cname.equals("Wardrobe"))
                {
                    caption4="Wardrobe";
                    if(!(unit.moduleCategory.contains ("N")|| unit.moduleCategory.contains("S - Wardrobe Panels"))) {
                        String width = unit.getDimensions();
                        wardrobewidth = wardrobewidth + " , " + width;
                        kwaList.add(new String(width));
                    }
                }
                else if(cname.equals("Kitchen"))
                {
                    caption4="Kitchen Lofts";
                    //caption4=product.getRoom();
                }
                else if(cname.equals("Storage Modules"))
                {
                    //caption4="Storage Modules";
                    caption4=product.getRoom();
                }
                else if(cname.equals("wallpanelling"))
                {
                    //caption4="Wall Panelling";
                    caption4=product.getRoom();
                }
                else if(cname.equals("oswalls"))
                {
                    //caption4="Open Shelves";
                    caption4=product.getRoom();
                }
                else if(cname.equals("Wardrobe"))
                {
                    caption4="Wardrobe Lofts";

                }
                else if(cname.equals("sidetables"))
                {
                    //caption4="Side Tables";
                    caption4=product.getRoom();
                }
                else if(cname.equals("shoerack"))
                {
                    //caption4=" Shoe Rack";
                    caption4=product.getRoom();
                }
                else if(cname.equals("Bathroom Vanity"))
                {
                    //caption4="Bathroom Vanity";
                    caption4=product.getRoom();
                }
                else if(cname.equals("tvunit"))
                {
                    //caption4="TV unit";
                    caption4=product.getRoom();
                }
                else if(cname.equals("barunit") )
                {
                    //caption4="Bar Unit";
                    caption4=product.getRoom();
                }
                else if(cname.equals("bookshelf"))
                {
                    //caption4="Book Shelf";
                    caption4=product.getRoom();
                }
                else if(cname.equals("crunit") )
                {
                    //caption4="Crockery Unit";
                    caption4=product.getRoom();
                }
                else if(cname.equals("wallunits"))
                {
                    //caption4="Wall Unit";
                    caption4=product.getRoom();
                }
                else if(cname.equals("codrawers"))
                {
                    //caption4="Chest of Drawers";
                    caption4=product.getRoom();
                }
                else if(cname.equals("usstorage"))
                {
                    //caption4="Under Staircase Storage";
                    caption4=product.getRoom();
                }
                else if(cname.equals("poojaunit"))
                {
                 //   caption4="Pooja Unit";
                    caption4=product.getRoom();
                }
                else if(cname.equals("studytable"))
                {
                    //caption4="Study Table";
                    caption4=product.getRoom();
                }
                else if(cname.equals("others"))
                {
                    //caption4="Others";
                    caption4=product.getRoom();
                }
            }
            /*else
            {
                this.createSubHeadingRow(tabname, series + ALPHABET_SEQUENCE[unitSequence], unit.title + " - " + unit.getDimensions());
                String S = "Unit consists of " + unit.moduleCount + " modules as per design provided.\n" + "Base Carcass : " + product.getProduct().getBaseCarcassCode() + " , Wall Carcass : " + product.getProduct().getWallCarcassCode() + "\n" + "Finish Material : " + ModuleDataService.getInstance().getFinish(product.getProduct().getFinishCode()).getTitle() + " , Finish Type : " + product.getProduct().getFinishType();
                this.createRowAndFillData(tabname, null, S, 1.0, unit.amount, 0.0);
            unitSequence++;
            if (unitSequence == ALPHABET_SEQUENCE.length) unitSequence = 0;
        }*/
        }
        li=new ArrayList<QuotationPDFCreator.customeclass>();
        QuotationPDFCreator.customeclass obj;
        if(basewidth!="") {
            basewidth =basewidth.substring(2) ;
                    //this.changeCharInPosition(basewidth.length() - 2, ' ', basewidth);
        }
        if(wallwidth!="") {
            wallwidth =wallwidth.substring(2);
        }
        if(tallwidth!="") {
            tallwidth = tallwidth.substring(2);
        }

        if(loftwidth!="") {
            loftwidth = loftwidth.substring(2);
        }

        if(wardrobewidth!="") {
            wardrobewidth = wardrobewidth.substring(2);
        }

        if(wardrobeLoftwidth!="")
        {
            wardrobeLoftwidth=wardrobeLoftwidth.substring(2);
        }

        /*obj=new QuotationPDFCreator.customeclass(tabname,caption,KBmodulecount,KBbasecarcass,KBWallcarcass,KBfinishmaterial,KBfinishtype,KBamount,basewidth);
        li.add(obj);
        customFunction(li);

        obj=new QuotationPDFCreator.customeclass(tabname,caption1,KWmoduleCount,KWbasecarcass,KWwallcarcass,KWfinishmaterial,KWfinishtype,KWamount,wallwidth);
        li.add(obj);
        customFunction(li);

        obj=new QuotationPDFCreator.customeclass(tabname,caption2,KTmoduleCount,KTbasecarcass,KTwallcarcass,KTfinishmaterial,KTfinishtype,KTamount,tallwidth);
        li.add(obj);
        customFunction(li);

        obj=new QuotationPDFCreator.customeclass(tabname,caption3,KLmoduleCount,KLbasecarcass,KLwallcarcass,KLfinishmaterial,KLfinishtype,KLamount,loftwidth);
        li.add(obj);
        LOG.info("caption3" +obj.getTitle());
        LOG.info("Module count" +KLmoduleCount);
        int num=customFunction(li);
        //int num=customFunction(li);*/

        if(KBamount!=0) {
            obj = new customeclass(tabname, caption, KBmodulecount, KBbasecarcass, KBWallcarcass, KBfinishmaterial, KBfinishtype, KBamount, baseDimesion,KBcolorgroupCode,KBhinge);
            li.add(obj);
            customFunction(li,unitSequence);
            unitSequence++;
            li.clear();
        }

        if(KWamount!=0) {
            obj = new customeclass(tabname, caption1, KWmoduleCount, KWbasecarcass, KWwallcarcass, KWfinishmaterial, KWfinishtype, KWamount, WallDimesion,KWcolorGroupCode,KWhinge);
            li.add(obj);
            customFunction(li,unitSequence);
            //rowValue++;
            unitSequence++;
            li.clear();
        }

        if(KTamount!=0) {
            obj = new customeclass(tabname, caption2, KTmoduleCount, KTbasecarcass, KTwallcarcass, KTfinishmaterial, KTfinishtype, KTamount, TallDimesion,KTcolorGroupCode,KThinge);
            li.add(obj);
            customFunction(li,unitSequence);
            //rowValue++;
            unitSequence++;
            li.clear();
        }

        if(KLamount!=0) {
            obj = new customeclass(tabname, caption3, KLmoduleCount, KLbasecarcass, KLwallcarcass, KLfinishmaterial, KLfinishtype, KLamount, loftDimesion,KLcolorGroupCode,KLhinge);
            li.add(obj);
            customFunction(li,unitSequence);
            //rowValue++;
            unitSequence++;
            li.clear();
        }

        if(WWamount!=0)
        {

            obj = new customeclass(tabname,captionWardrobe,WWmodulecount,WWbasecarcass,WWwallcarcass,WWfinishmaterial,WWfinishtype,WWamount,wardrobewidth,WWcolorGroupCode,WWhinge);
            li.add(obj);
            customFunction(li,wunitSequence);
            //rowValue++;
            wunitSequence++;
            li.clear();
        }

        if(WW1amount!=0)
        {
            obj = new customeclass(tabname,captionLoft,WW1modulecount,WW1basecarcass,WW1wallcarcass,WW1finishmaterial,WW1finishtype,WW1amount,wardrobeLoftwidth,WW1colorGroupcode,WW1hinge);
            li.add(obj);
            customFunction(li,wunitSequence);
            //rowValue++;
            wunitSequence++;
            li.clear();
        }

        li2=new ArrayList<QuotationPDFCreator.customeclass>();
        QuotationPDFCreator.customeclass ob2;

        ob2=new QuotationPDFCreator.customeclass(tabname,caption4,SW1modulecount,SW1basecarcass,SW1wallcarcass,SW1finishmaterial,SW1finishtype,SW1amount,wardrobewidth,SW1colorGroupcode,SW1hinge);
        li2.add(ob2);

        customFunction(li2,unitSequence);
    }


    private void createSubHeadingRow(PdfPTable tabname,String index, String title)
    {
        Font size1=new Font(Font.FontFamily.TIMES_ROMAN,8,Font.BOLD);

        PdfPCell cell1=new PdfPCell();
        Paragraph Pindex=new Paragraph(index,size1);
        Pindex.setAlignment(Element.ALIGN_CENTER);
        cell1.addElement(Pindex);
        tabname.addCell(cell1);

        PdfPCell cell2=new PdfPCell();
        Paragraph Ptitle=new Paragraph(title,size1);
        cell2.setColspan(4);
        cell2.addElement(Ptitle);
        tabname.addCell(cell2);
    }

    private void createRowforAcc(PdfPTable tabname,String index,String title)
    {
        PdfPCell cell;
        Paragraph Pindex;
        Font size1=new Font(Font.FontFamily.TIMES_ROMAN,8,Font.BOLD);

        PdfPCell cell1=new PdfPCell();
        Pindex=new Paragraph(index,size1);
        Pindex.setAlignment(Element.ALIGN_CENTER);
        cell1.addElement(Pindex);
        tabname.addCell(cell1);

        cell=new PdfPCell(new Paragraph(title,fsize));
        tabname.addCell(cell);
    }
    private void createRowAndFillDataForAddon(PdfPTable tabname,String index, String title,String UOM, Double quantity, Double amount, Double total)
    {
        PdfPCell cell;
        Paragraph Pindex;
        Font size1=new Font(Font.FontFamily.TIMES_ROMAN,8,Font.BOLD);

        PdfPCell cell1=new PdfPCell();
        Pindex=new Paragraph(index,size1);
        Pindex.setAlignment(Element.ALIGN_CENTER);
        cell1.addElement(Pindex);
        tabname.addCell(cell1);

        cell=new PdfPCell(new Paragraph(title,fsize));
        tabname.addCell(cell);

        PdfPCell cell5=new PdfPCell();
        Pindex=new Paragraph(UOM,fsize);
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell5.addElement(Pindex);
        tabname.addCell(cell5);

        PdfPCell cell2=new PdfPCell();
        Pindex=new Paragraph(this.getRoundOffValue(String.valueOf(quantity.intValue())),fsize);
        Pindex.setAlignment(Element.ALIGN_RIGHT);
        cell2.addElement(Pindex);
        tabname.addCell(cell2);


        PdfPCell cell4 = new PdfPCell();
        Pindex = new Paragraph(this.getRoundOffValue(String.valueOf(amount.intValue())), fsize);
        Pindex.setAlignment(Element.ALIGN_RIGHT);
        cell4.addElement(Pindex);
        tabname.addCell(cell4);

        PdfPCell cell3 = new PdfPCell();
        double amt = quantity * amount;
        Paragraph Pamt = new Paragraph(this.getRoundOffValue(String.valueOf((int) amt)), fsize);
        Pamt.setAlignment(Element.ALIGN_RIGHT);
        cell3.addElement(Pamt);
        tabname.addCell(cell3);
    }
    private void createRowAndFillData(PdfPTable tabname,String index, String title, Double quantity, Double amount, Double total)
    {
        PdfPCell cell;
        Paragraph Pindex;
        Font size1=new Font(Font.FontFamily.TIMES_ROMAN,8,Font.BOLD);

        PdfPCell cell1=new PdfPCell();
        Pindex=new Paragraph(index,size1);
        Pindex.setAlignment(Element.ALIGN_CENTER);
        cell1.addElement(Pindex);
        tabname.addCell(cell1);

        cell=new PdfPCell(new Paragraph(title,fsize));
        tabname.addCell(cell);

        PdfPCell cell2=new PdfPCell();
        Pindex=new Paragraph(this.getRoundOffValue(String.valueOf(quantity.intValue())),fsize);
        Pindex.setAlignment(Element.ALIGN_RIGHT);
        cell2.addElement(Pindex);
        tabname.addCell(cell2);


            PdfPCell cell4 = new PdfPCell();
            Pindex = new Paragraph(this.getRoundOffValue(String.valueOf(amount.intValue())), fsize);
            Pindex.setAlignment(Element.ALIGN_RIGHT);
            cell4.addElement(Pindex);
            tabname.addCell(cell4);

            PdfPCell cell3 = new PdfPCell();
            double amt = quantity * amount;
            Paragraph Pamt = new Paragraph(this.getRoundOffValue(String.valueOf((int) amt)), fsize);
            Pamt.setAlignment(Element.ALIGN_RIGHT);
            cell3.addElement(Pamt);
            tabname.addCell(cell3);

    }

    private void createRowAndFillDataForAccessories(PdfPTable tabname,String index, String title, Double quantity)
    {
        PdfPCell cell;
        Paragraph Pindex;
        Font size1=new Font(Font.FontFamily.TIMES_ROMAN,8,Font.BOLD);

        PdfPCell cell1=new PdfPCell();
        Pindex=new Paragraph(index,size1);
        Pindex.setAlignment(Element.ALIGN_CENTER);
        cell1.addElement(Pindex);
        tabname.addCell(cell1);

        cell=new PdfPCell(new Paragraph(title,fsize));
        tabname.addCell(cell);

        PdfPCell cell2=new PdfPCell();
        Pindex=new Paragraph(this.getRoundOffValue(String.valueOf(quantity.intValue())),fsize);
        Pindex.setAlignment(Element.ALIGN_RIGHT);
        cell2.addElement(Pindex);
        tabname.addCell(cell2);

        PdfPCell cell4 = new PdfPCell();
        Pindex = new Paragraph();
        Pindex.setAlignment(Element.ALIGN_RIGHT);
        cell4.addElement(Pindex);
        tabname.addCell(cell4);

        PdfPCell cell3 = new PdfPCell();
        Paragraph Pamt = new Paragraph();
        Pamt.setAlignment(Element.ALIGN_RIGHT);
        cell3.addElement(Pamt);
        tabname.addCell(cell3);

    }

    private void createRowAndFillData(PdfPTable tabname,String index, String title)
    {
        PdfPCell cell;
        Font size1=new Font(Font.FontFamily.TIMES_ROMAN,8,Font.BOLD);

        PdfPCell cell1=new PdfPCell();
        Paragraph Pindex=new Paragraph(index,size1);
        Pindex.setAlignment(Element.ALIGN_CENTER);
        cell1.addElement(Pindex);
        tabname.addCell(cell1);

        cell=new PdfPCell(new Paragraph(title,fsize));
        cell.setColspan(4);
        tabname.addCell(cell);
    }
    private void fillAssembledProductAccessories(PdfPTable tabname,List<AssembledProductInQuote.Accessory> accessories, String unitSequenceLetter)
    {
        if (accessories == null || accessories.isEmpty())
        {
            return;
        }
        this.createSubHeadingRow(tabname, series+ "" +unitSequenceLetter, "Accessories");
        int acSequence = 0;
        double amount=0.0;
        for (AssembledProductInQuote.Accessory accessory : accessories)
        {
            LOG.info("Accesoory " +accessory);
            if(accessory.category.equals("Primary") || accessory.category.equals("Add on")|| accessory.category.equals("Standalone add on"))
            {
                LOG.info("Inserting new accessory row");
                this.createRowAndFillDataForAccessories(tabname,ROMAN_SEQUENCE[acSequence], accessory.title, accessory.quantity);

//                this.createProductTitleRow(tabname, ROMAN_SEQUENCE[acSequence], accessory.title + " - " +Double.valueOf(accessory.quantity).intValue());
                acSequence++;
                if (acSequence == ROMAN_SEQUENCE.length) acSequence = 0;
            }
            LOG.info("accessory code " +accessory.code+ " priceDate " +proposalHeader.getPriceDate()+ " city " +proposalHeader.getProjectCity());
            PriceMaster accessoryPrice=RateCardService.getInstance().getAccessoryRate(accessory.code,proposalHeader.getPriceDate(),proposalHeader.getProjectCity());
            LOG.info("Accessory Price " +accessoryPrice);
            amount=amount+(accessory.quantity*accessoryPrice.getPrice());
            //amount=amount+(accessory.quantity*accessory.msp);
        }

        /*this.createCellWithData(tabname,"Accessory Cost",amount);
        this.createCellWithData(tabname,"WoodWork Cost",amt-amount);*/
    }

    private void createCellWithData(PdfPTable tabname,String str,double data)
    {
        PdfPCell cell1 = new PdfPCell();
        Paragraph p1=new Paragraph(str,fsize);
        p1.setAlignment(Element.ALIGN_RIGHT);
        cell1.addElement(p1);
        cell1.setColspan(4);
        tabname.addCell(cell1);

        PdfPCell cell = new PdfPCell();
        Paragraph p=new Paragraph(this.getRoundOffValue(String.valueOf((int)data)),fsize);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        tabname.addCell(cell);
    }
    private void fillCatalogProducts(PdfPTable tabname)
    {
        List<ProductLineItem> catalogProducts = this.quoteData.getCatalogueProducts();
        if (catalogProducts.isEmpty())
        {
            return ;
        }
        int sequenceNumber = this.quoteData.getCatalogStartSequence();
        for (ProductLineItem product : catalogProducts)
        {
            this.fillCatalogProductInfo(tabname,sequenceNumber, product);
            sequenceNumber++;
        }
    }

    private void fillCatalogProductInfo(PdfPTable tabname, int sequenceNumber, ProductLineItem product)
    {
        this.createSubHeadingRowForCatalog(tabname,"A." +String.valueOf(sequenceNumber), product.getTitle(), Double.valueOf(product.getQuantity()),
                product.getRate(), (double) Math.round(product.getAmount()));
        this.createRowAndFillData(tabname,null, product.getName());
        series="A." +String.valueOf(sequenceNumber) + ".";
        this.createSubHeadingRow(tabname,series+ "a",product.getRoomCode());
        this.createRowAndFillData(tabname, null, "Material : " + product.getBaseCarcassCode() + "\n" +"Finish : " + product.getFinishCode(),1.0,product.getAmount(),product.getAmount());
        //this.createRowAndFillData(tabname, null, "Finish : " + product.getFinishCode());
        this.createCellWithData(tabname,"Total Cost",product.getAmount());
        //this.quoteSheet.addMergedRegion(new CellRangeAddress(startRow, currentRow, AMOUNT_CELL, AMOUNT_CELL));

        //currentRow++;
        //this.createRow(currentRow, this.quoteSheet);
    }

    private void createSubHeadingRowForCatalog(PdfPTable tabname, String index, String title,Double quantity, Double amount, Double total)
    {
        Font size1=new Font(Font.FontFamily.TIMES_ROMAN,8,Font.BOLD);
        Font size2=new Font(Font.FontFamily.TIMES_ROMAN,8,Font.BOLD|Font.UNDERLINE);

        PdfPCell cell;
        Paragraph p;

        PdfPCell cell1=new PdfPCell();
        Paragraph Pindex=new Paragraph(index,size1);
        Pindex.setAlignment(Element.ALIGN_CENTER);
        cell1.addElement(Pindex);
        tabname.addCell(cell1);


        cell=new PdfPCell();
        p=new Paragraph(title,size1);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        tabname.addCell(cell);

        /*cell=new PdfPCell();
        p=new Paragraph(this.getRoundOffValue(String.valueOf(quantity.intValue())),fsize);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        tabname.addCell(cell);

        PdfPCell cell1=new PdfPCell();
        p=new Paragraph(this.getRoundOffValue(String.valueOf(amount.intValue())),fsize);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell1.addElement(p);
        tabname.addCell(cell1);

        PdfPCell cell2=new PdfPCell();
        double amt=quantity*amount;
        Paragraph Pamt=new Paragraph(this.getRoundOffValue(String.valueOf(total.intValue())),fsize);
        Pamt.setAlignment(Element.ALIGN_RIGHT);
        cell2.addElement(Pamt);
        tabname.addCell(cell2);*/
    }

    private void fillAddons(PdfPTable tabname,List<ProductAddon> addOns, String emptyMessage)
    {
        if (addOns.isEmpty())
        {
            this.createRowWithMessage(tabname,emptyMessage);
            return;
        }
        int index = 1;

        for (ProductAddon addon : addOns)
        {
            //LOG.info("category " +addon.getCategoryCode());
            if(("Custom Addon").equals (addon.getCategoryCode()))
                this.createRowAndFillDataForAddon(tabname,String.valueOf(index), addon.getCustomTitle(), addon.getUom(),addon.getQuantity(), addon.getRate(), addon.getAmount());
            else if(("Appliances").equals(addon.getCategoryCode()))
            {
                this.createProductTitleRowForAddon(tabname,String.valueOf(index),addon.getExtendedTitle());
                this.createRowAndFillDataForAddon(tabname, null,"Specification: " +addon.getTitle(),addon.getUom(), addon.getQuantity(), addon.getRate(), addon.getAmount());
            }
            else
            {
                this.createProductTitleRowForAddon(tabname,String.valueOf(index),addon.getExtendedTitle());
                this.createRowAndFillDataForAddon(tabname, null,"Specification: " +addon.getTitle()+ "\nLocation: " +addon.getREMARKS() ,addon.getUom(), addon.getQuantity(), addon.getRate(), addon.getAmount());
            }
            index++;
        }
    }

    private void createRowWithMessage(PdfPTable tabname,String message)
    {
        PdfPCell cell = new PdfPCell();
        cell.addElement(new Paragraph(message,fsize));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setColspan(5);
        tabname.addCell(cell);
    }

    public static String getRoundOffValue(String value)
    {
        value=value.replace(",","");
        char lastDigit=value.charAt(value.length()-1);
        String result = "";
        int len = value.length()-1;
        int nDigits = 0;

        for (int i = len - 1; i >= 0; i--)
        {
            result = value.charAt(i) + result;
            nDigits++;
            if (((nDigits % 2) == 0) && (i > 0))
            {
                result = "," + result;
            }
        }
        return (result+lastDigit);
    }

    class customeclass
    {
        PdfPTable tabName;
        String title,basecarcass,wallcarcass,finishmaterial,finishtype,dimension,colorGroupCode,hingeType;
        int modulecount;
        double amount;

        public customeclass(PdfPTable tabName, String title,int modulecount, String basecarcass, String wallcarcass, String finishmaterial, String finishtype, double amount,String dimension,String colorGroupCode,String hingeType ) {
            this.tabName = tabName;
            this.title = title;
            this.basecarcass = basecarcass;
            this.wallcarcass = wallcarcass;
            this.finishmaterial = finishmaterial;
            this.finishtype = finishtype;
            this.amount = amount;
            this.modulecount = modulecount;
            this.dimension=dimension;
            this.colorGroupCode=colorGroupCode;
            this.hingeType=hingeType;
        }

        public String getHingeType() {
            return hingeType;
        }

        public void setHingeType(String hingeType) {
            this.hingeType = hingeType;
        }

        public String getDimension() {
            return dimension;
        }

        public void setDimension(String dimension) {
            this.dimension = dimension;
        }

        public PdfPTable getTabName() {
            return tabName;
        }

        public void setTabName(PdfPTable tabName) {
            this.tabName = tabName;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getBasecarcass() {
            return basecarcass;
        }

        public void setBasecarcass(String basecarcass) {
            this.basecarcass = basecarcass;
        }

        public String getWallcarcass() {
            return wallcarcass;
        }

        public void setWallcarcass(String wallcarcass)
        {
            this.wallcarcass = wallcarcass;
        }

        public String getFinishmaterial() {
            return finishmaterial;
        }

        public void setFinishmaterial(String finishmaterial) {
            this.finishmaterial = finishmaterial;
        }

        public String getFinishtype() {
            return finishtype;
        }

        public void setFinishtype(String finishtype) {
            this.finishtype = finishtype;
        }

        public int getModulecount() {
            return modulecount;
        }

        public void setModulecount(int modulecount) {
            this.modulecount = modulecount;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getColorGroupCode() {
            return colorGroupCode;
        }

        public void setColorGroupCode(String colorGroupCode) {
            this.colorGroupCode = colorGroupCode;
        }
    }

    public void getProducts()
    {
        designServicePrice=RateCardService.getInstance().getFactorRate(DESIGN_SERVICE_PRICE,proposalHeader.getPriceDate(),proposalHeader.getProjectCity());
        designServiceTax=RateCardService.getInstance().getFactorRate(DESIGN_SERVICE_TAX,proposalHeader.getPriceDate(),proposalHeader.getProjectCity());
        nonMovablePrice=RateCardService.getInstance().getFactorRate(NON_MOVABLE_PRICE,proposalHeader.getPriceDate(),proposalHeader.getProjectCity());
        movablePrice=RateCardService.getInstance().getFactorRate(MOVABLE_PRICE,proposalHeader.getPriceDate(),proposalHeader.getProjectCity());
        nonMovablePriceTax=RateCardService.getInstance().getFactorRate(NON_MOVABLE_PRICE_TAX,proposalHeader.getPriceDate(),proposalHeader.getProjectCity());
        movablePriceTax=RateCardService.getInstance().getFactorRate(MOVABLE_PRICE_TAX,proposalHeader.getPriceDate(),proposalHeader.getProjectCity());
        scwTax=RateCardService.getInstance().getFactorRate(SCW_PRICE_TAX,proposalHeader.getPriceDate(),proposalHeader.getProjectCity());
        List<AssembledProductInQuote> assembledProducts = this.quoteData.getAssembledProducts();
        for (AssembledProductInQuote product : assembledProducts)
        {
            String ProductType="P";
            LOG.info("design service price " +DESIGN_SERVICE_PRICE+ "price Date " + proposalHeader.getPriceDate()+ " city " +proposalHeader.getProjectCity());
            LOG.info("Design service " +designServicePrice);
            LOG.info("Design Service tax " +designServiceTax);
            LOG.info("non movable price" +nonMovablePrice);
            LOG.info("movable" +movablePrice);

            ProductCategoryMap productCategoryMap = ModuleDataService.getInstance().getProductCategoryMap(product.getCatagoryName(),proposalHeader.getPriceDate());
            String productType = productCategoryMap.getType();
            LOG.info("Product type " +productType);

            if(productType.equals("NMF"))
            {
                double discountAmount = product.getTotalAmount() * (quoteData.getDiscountPercentage() / 100.0);
                double pricewithTax=0,price=0,dsWithTax=0,dsPrice=0;
                price=(product.getTotalAmount()-discountAmount)*(nonMovablePrice.getPrice()/100);
                pricewithTax=price*nonMovablePriceTax.getSourcePrice();
                dsPrice=(product.getTotalAmount()-discountAmount)*(designServicePrice.getPrice()/100);
                dsWithTax=dsPrice*designServiceTax.getSourcePrice();
                String tax="18%";
                LOG.info("discount percentage " +quoteData.getDiscountPercentage());
                LOG.info("log.info" +product.getTitle());
                GSTForProducts nonmovable=new GSTForProducts(product.getCatagoryName(),product.getTitle(),(product.getTotalAmount()-discountAmount),price,pricewithTax,tax,"P",productType);
                nonMovableList.add(nonmovable);
                GSTForProducts designservice=new GSTForProducts(product.getCatagoryName(),product.getTitle(),(product.getTotalAmount()-discountAmount),dsPrice,dsWithTax,tax,"P",productType);
                designServiceList.add(designservice);

                LOG.info("non movable furniture " +productType+ " " +product.getTitle());

            }else if(productType.equals("MF"))
            {
                double discountAmount = product.getTotalAmount() * (quoteData.getDiscountPercentage() / 100.0);
                double pricewithTax=0,price=0,dsWithTax=0,dsPrice=0;
                price=(product.getTotalAmount()-discountAmount)*(movablePrice.getPrice()/100);
                pricewithTax=price*movablePriceTax.getSourcePrice();
                dsPrice=(product.getTotalAmount()-discountAmount)*(designServicePrice.getPrice()/100);
                dsWithTax=dsPrice*designServiceTax.getSourcePrice();
                String tax="28%";
                GSTForProducts movable=new GSTForProducts(product.getCatagoryName(),product.getTitle(),(product.getTotalAmount()-discountAmount),price,pricewithTax,tax,"P",productType);
                movableList.add(movable);
                GSTForProducts designservice=new GSTForProducts(product.getCatagoryName(),product.getTitle(),(product.getTotalAmount()-discountAmount),dsPrice,dsWithTax,tax,"P",productType);
                designServiceList.add(designservice);
                LOG.info("movable furniture " +productType+ " " +product.getTitle());
            }
            else if(productType.equals("SCW"))
            {
                double discountAmount = product.getTotalAmount() * (quoteData.getDiscountPercentage() / 100.0);
                double pricewithTax=0,price,dsWithTax=0,dsPrice=0;
                price=product.getTotalAmount()-discountAmount;
                pricewithTax=dsPrice*scwTax.getSourcePrice();
                dsPrice=(product.getTotalAmount()-discountAmount)*(designServicePrice.getPrice()/100);
                dsWithTax=dsPrice*designServiceTax.getSourcePrice();
                String tax="18%";
                GSTForProducts scw=new GSTForProducts(product.getCatagoryName(),product.getTitle(),(product.getTotalAmount()-discountAmount),price,pricewithTax,tax,"P",productType);
                scwList.add(scw);
                GSTForProducts designservice=new GSTForProducts(product.getCatagoryName(),product.getTitle(),(product.getTotalAmount()-discountAmount),dsPrice,dsWithTax,tax,"P",productType);
                designServiceList.add(designservice);
                LOG.info("SCW " +productType+ " " +product.getTitle());
            }
        }
        List<ProductAddon> productAddonsforAccessories=this.quoteData.getAccessories();
        LOG.info("Addon size " +productAddonsforAccessories.size());
        String AddonType="A";
        for(ProductAddon productAddon:productAddonsforAccessories)
        {
            ProductCategoryMap productCategoryMap = ModuleDataService.getInstance().getProductCategoryMap(productAddon.getCategoryCode(),proposalHeader.getPriceDate());
            String productType = productCategoryMap.getType();
            LOG.info("Addon type " +productType);
            String tax="28%";
            if(productType.equals("MF"))
            {
                double pricewithTax=0;
                pricewithTax=productAddon.getAmount()*movablePriceTax.getSourcePrice();
                GSTForProducts movable=new GSTForProducts(productAddon.getCategoryCode(),productAddon.getExtendedTitle(),productAddon.getAmount(),productAddon.getAmount(),pricewithTax,tax,"A",productType);
                movableList.add(movable);
            }

        }
        List<ProductAddon> productAddonsforAppliances=this.quoteData.getAppliances();
        LOG.info("Addon size " +productAddonsforAppliances.size());
        for(ProductAddon productAddon:productAddonsforAppliances)
        {
            ProductCategoryMap productCategoryMap = ModuleDataService.getInstance().getProductCategoryMap(productAddon.getCategoryCode(),proposalHeader.getPriceDate());
            String productType = productCategoryMap.getType();
            LOG.info("Addon type " +productType);
            String tax="28%";
            if(productType.equals("MF"))
            {
                double pricewithTax=0;
                pricewithTax=productAddon.getAmount()*movablePriceTax.getSourcePrice();
                GSTForProducts movable=new GSTForProducts(productAddon.getCategoryCode(),productAddon.getExtendedTitle(),productAddon.getAmount(),productAddon.getAmount(),pricewithTax,tax,"A",productType);
                movableList.add(movable);
            }

        }

        List<ProductAddon> productAddonsforcounterTop=this.quoteData.getCounterTops();
        LOG.info("Addon size " +productAddonsforcounterTop.size());
        for(ProductAddon productAddon:productAddonsforcounterTop)
        {
            ProductCategoryMap productCategoryMap = ModuleDataService.getInstance().getProductCategoryMap(productAddon.getCategoryCode(),proposalHeader.getPriceDate());
            String productType = productCategoryMap.getType();
            String tax="18%";
            LOG.info("Addon type " +productType);

            if(productType.equals("SCW"))
            {
                double pricewithTax=0;
                pricewithTax=productAddon.getAmount()*scwTax.getSourcePrice();
                GSTForProducts scw=new GSTForProducts(productAddon.getCategoryCode(),productAddon.getExtendedTitle(),productAddon.getAmount(),productAddon.getAmount(),pricewithTax,tax,"A",productType);
                scwList.add(scw);
            }
        }

        List<ProductAddon> productAddonsforservices=this.quoteData.getServices();
        LOG.info("Addon size " +productAddonsforservices.size());
        for(ProductAddon productAddon:productAddonsforservices)
        {
            ProductCategoryMap productCategoryMap = ModuleDataService.getInstance().getProductCategoryMap(productAddon.getCategoryCode(),proposalHeader.getPriceDate());
            String productType = productCategoryMap.getType();
            LOG.info("Addon type " +productType);
            String tax="18%";
            if(productType.equals("SCW"))
            {
                double pricewithTax=0;
                pricewithTax=productAddon.getAmount()*scwTax.getSourcePrice();
                GSTForProducts scw=new GSTForProducts(productAddon.getCategoryCode(),productAddon.getExtendedTitle(),productAddon.getAmount(),productAddon.getAmount(),pricewithTax,tax,"A",productType);
                scwList.add(scw);
            }
        }

        List<ProductAddon> productAddonsforLooseFurniture=this.quoteData.getLooseFurniture();
        LOG.info("Addon size " +productAddonsforLooseFurniture.size());
        for(ProductAddon productAddon:productAddonsforLooseFurniture)
        {
            ProductCategoryMap productCategoryMap = ModuleDataService.getInstance().getProductCategoryMap(productAddon.getCategoryCode(),proposalHeader.getPriceDate());
            String productType = productCategoryMap.getType();
            LOG.info("Addon type " +productType);
            String tax="28%";
            if(productType.equals("MF"))
            {
                double pricewithTax=0;
                pricewithTax=productAddon.getAmount()*movablePriceTax.getSourcePrice();
                GSTForProducts movable=new GSTForProducts(productAddon.getCategoryCode(),productAddon.getExtendedTitle(),productAddon.getAmount(),productAddon.getAmount(),pricewithTax,tax,"A",productType);
                movableList.add(movable);
            }

        }

        List<ProductAddon> productAddonsforcustomAddon=this.quoteData.getCustomAddons();
        LOG.info("Addon size " +productAddonsforcustomAddon.size());
        for(ProductAddon productAddon:productAddonsforcustomAddon)
        {
            ProductCategoryMap productCategoryMap = ModuleDataService.getInstance().getProductCategoryMap(productAddon.getCategoryCode(),proposalHeader.getPriceDate());
            String productType = productCategoryMap.getType();
            LOG.info("Addon type " +productType);
            String tax="18%";
            if(productType.equals("SCW"))
            {
                double pricewithTax=0;
                pricewithTax=productAddon.getAmount()*scwTax.getSourcePrice();
                GSTForProducts scw=new GSTForProducts(productAddon.getCategoryCode(),productAddon.getCustomTitle(),productAddon.getAmount(),productAddon.getAmount(),pricewithTax,tax,"A",productType);
                scwList.add(scw);
            }
        }

        LOG.info("movable " +movableList.size() + " non movable " +nonMovableList.size() + "scw size " +scwList.size());
        GSTForProducts movableProducts;
        for(GSTForProducts movable:movableList)
        {
            double tax_amount=movable.getPrice()-movable.getPriceAfterTax();
            LOG.info("movable list " +movable.toString());
            movabletotalproductPrice+=movable.getPriceAfterDiscount();
            movabletotalDAP+=movable.getPrice();
            movabletotalTaxAmt+=movable.getPriceAfterTax();
            movabletotalPriceAfterTax+=this.round(tax_amount,2);
        }
        //movableProducts=new GSTForProducts("Movable Furniture","",priceAfterDiscount,)
        for(GSTForProducts nonmovable : nonMovableList)
        {
            LOG.info("non movable " +nonmovable.toString());
            double tax_amount=nonmovable.getPrice()-nonmovable.getPriceAfterTax();
            nonmovabletotalproductPrice+=nonmovable.getPriceAfterDiscount();
            nonmovabletotalDAP+=nonmovable.getPrice();
            nonmovabletotalTaxAmt+=nonmovable.getPriceAfterTax();
            nonmovabletotalPriceAfterTax+=this.round(tax_amount,2);
        }
        for(GSTForProducts scw : scwList)
        {
            LOG.info("scw " +scw.toString());

            LOG.info("non movable " +scw.toString());
            double tax_amount=scw.getPrice()-scw.getPriceAfterTax();
            scwtotalproductPrice+=scw.getPriceAfterDiscount();
            scwtotalDAP+=scw.getPrice();
            scwtotalTaxAmt+=scw.getPriceAfterTax();
            scwtotalPriceAfterTax+=this.round(tax_amount,2);
        }
        for(GSTForProducts design: designServiceList)
        {
            LOG.info("Design services " +design.toString());
            double tax_amount=design.getPrice()-design.getPriceAfterTax();
            designtotalproductPrice+=design.getPriceAfterDiscount();
            designtotalDAP+=design.getPrice();
            designtotalTaxAmt+=design.getPriceAfterTax();
            designtotalPriceAfterTax+=this.round(tax_amount,2);
        }
    }

    private void getfinalGSTList(List<GSTForProducts> gstForProducts,String productType)
    {
        GSTForProducts productslist;
        double productPriceAfterDiscount=0,actualProductPrice=0,productTaxPrice=0;
        String title="";
        String tax="";
        for(GSTForProducts gstForProducts1:gstForProducts)
        {
            productPriceAfterDiscount+=gstForProducts1.getPriceAfterDiscount();
            actualProductPrice+=gstForProducts1.getPrice();
            productTaxPrice+=gstForProducts1.getPriceAfterTax();
        }
        if(productType.equals("MF"))
        {
            title="Movable Furniture";
            tax="28%";

        }
        else if(productType.equals("NMF"))
        {
            title="Non Movable Furniture";
            tax="18%";
        }else if(productType.equals("SCW"))
        {
            title="Services and Civil Work";
            tax="18%";
        }else
        {
            title="Design Services";
            tax="18%";
        }
        productslist=new GSTForProducts(title," ",productPriceAfterDiscount,actualProductPrice,productTaxPrice,tax);
        finalmovableList.add(productslist);
    }
    private void createRowAndFillDataForGST(PdfPTable tabname,String GSTCategory, double PriceAfterDiscount, double DesignpriceAfterDsicount,double currentpriceAfterTax,String tax)
    {
        LOG.info("GST Category " +GSTCategory);
        String sequence=BOLD_ALPHABET_SEQUENCE[count];
        //LOG.info("inside create row n fill data");
        double tax_amount=round(DesignpriceAfterDsicount-currentpriceAfterTax,2);

        if(!GSTCategory.equals("Design Services"))
        {
            totalproductPrice+=PriceAfterDiscount;
            totalDAP+=DesignpriceAfterDsicount;
            totalTaxAmt+=currentpriceAfterTax;
            totalPriceAfterTax+=tax_amount;
        }
            if(tax.equals("18%"))
            {
                set1totalproductPrice+=PriceAfterDiscount;
                set1totalDAP+=DesignpriceAfterDsicount;
                set1totalTaxAmt+=currentpriceAfterTax;
                set1totalPriceAfterTax+=tax_amount;

            }else
            {
                set2totalproductPrice+=PriceAfterDiscount;
                set2totalDAP+=DesignpriceAfterDsicount;
                set2totalTaxAmt+=currentpriceAfterTax;
                set2totalPriceAfterTax+=tax_amount;
            }



        PdfPCell cell;
        Paragraph Pindex;
        Font size1=new Font(Font.FontFamily.TIMES_ROMAN,8,Font.BOLD);

        PdfPCell cell1=new PdfPCell();
        Pindex=new Paragraph(sequence,size1);
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell1.addElement(Pindex);
        tabname.addCell(cell1);


        cell=new PdfPCell();
        Pindex=new Paragraph(GSTCategory,size1);
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(Pindex);
        tabname.addCell(cell);

        /*PdfPCell cell2=new PdfPCell();
        Pindex=new Paragraph(Double.toString(PriceAfterDiscount),fsize);
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell2.addElement(Pindex);
        tabname.addCell(cell2);*/

        PdfPCell cell5=new PdfPCell();
        Pindex=new Paragraph(tax,fsize);
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell5.addElement(Pindex);
        tabname.addCell(cell5);

        PdfPCell cell6=new PdfPCell();
        Pindex=new Paragraph(Double.toString(tax_amount),fsize);
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell6.addElement(Pindex);
        tabname.addCell(cell6);

        PdfPCell cell7=new PdfPCell();
        Pindex=new Paragraph(Double.toString(currentpriceAfterTax),fsize);
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell7.addElement(Pindex);
        tabname.addCell(cell7);

        PdfPCell cell4=new PdfPCell();
        Pindex=new Paragraph(Double.toString(DesignpriceAfterDsicount),fsize);
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell4.addElement(Pindex);
        tabname.addCell(cell4);

        count++;
    }

    private void createRowAndFillDataForGSTtotal(PdfPTable tabname,String GSTCategory, double PriceAfterDiscount, double DesignpriceAfterDsicount,double currentpriceAfterTax,String tax)
    {

       /* //LOG.info("inside create row n fill data");
        double tax_amount=round(DesignpriceAfterDsicount-currentpriceAfterTax,2);

        totalproductPrice+=PriceAfterDiscount;
        totalDAP+=DesignpriceAfterDsicount;
        totalTaxAmt+=currentpriceAfterTax;
        totalPriceAfterTax+=tax_amount;
*/
        PdfPCell cell;
        Paragraph Pindex;
        Font size1=new Font(Font.FontFamily.TIMES_ROMAN,8,Font.BOLD);

        PdfPCell cell1=new PdfPCell();
        Pindex=new Paragraph("1",fsize);
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell1.addElement(Pindex);
        tabname.addCell(cell1);
        cell1.setRowspan(2);


        cell=new PdfPCell();
        Pindex=new Paragraph("A+B+C",fsize);
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(Pindex);
        tabname.addCell(cell);

        /*PdfPCell cell2=new PdfPCell();
        Pindex=new Paragraph(Double.toString(PriceAfterDiscount),fsize);
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell2.addElement(Pindex);
        tabname.addCell(cell2);*/



        PdfPCell cell6=new PdfPCell();
        Pindex=new Paragraph();
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell6.addElement(Pindex);
        tabname.addCell(cell6);

        PdfPCell cell5=new PdfPCell();
        Pindex=new Paragraph(tax,fsize);
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell5.addElement(Pindex);
        tabname.addCell(cell5);

        PdfPCell cell7=new PdfPCell();
        Pindex=new Paragraph(Double.toString(round(currentpriceAfterTax,2)),fsize);
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell7.addElement(Pindex);
        tabname.addCell(cell7);

        PdfPCell cell4=new PdfPCell();
        Pindex=new Paragraph(Double.toString(round(DesignpriceAfterDsicount,2)),fsize);
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell4.addElement(Pindex);
        tabname.addCell(cell4);
        //count++;

    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            return value = 0;
        } else {
            BigDecimal bd = new BigDecimal(value);
            bd = bd.setScale(places, RoundingMode.HALF_UP);
            return bd.doubleValue();
        }
    }

    private void createRowAndFillDataForGSTforFinal(PdfPTable tabname,String Category, double productPrice, double DAP,String taxPercentage,double taxamount,double priceAfterDiscount)
    {
        PdfPCell cell;
        Paragraph Pindex;
        Font size1=new Font(Font.FontFamily.TIMES_ROMAN,8,Font.BOLD);

        PdfPCell cell1=new PdfPCell();
        Pindex=new Paragraph(Integer.toString(finalcount),size1);
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell1.addElement(Pindex);
        tabname.addCell(cell1);


        cell=new PdfPCell();
        Pindex=new Paragraph(Category,size1);
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(Pindex);
        tabname.addCell(cell);

        /*PdfPCell cell2=new PdfPCell();
        Pindex=new Paragraph(Double.toString(round(productPrice,2)),fsize);
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell2.addElement(Pindex);
        tabname.addCell(cell2);*/

        PdfPCell cell5=new PdfPCell();
        Pindex=new Paragraph(taxPercentage,fsize);
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell5.addElement(Pindex);
        tabname.addCell(cell5);

        PdfPCell cell6=new PdfPCell();
        Pindex=new Paragraph(Double.toString(round(priceAfterDiscount,2)),fsize);
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell6.addElement(Pindex);
        tabname.addCell(cell6);

        PdfPCell cell7=new PdfPCell();
        Pindex=new Paragraph(Double.toString(round(taxamount,2)),fsize);
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell7.addElement(Pindex);
        tabname.addCell(cell7);

        PdfPCell cell4=new PdfPCell();
        Pindex=new Paragraph(Double.toString(round(DAP,2)),fsize);
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell4.addElement(Pindex);
        tabname.addCell(cell4);

        finalcount++;
    }


    private void createRowAndFillDataForIndividualForProducts(PdfPTable tabname,int count,String GSTCategory,String categoryType, double PriceAfterDiscount, double DesignpriceAfterDsicount,double currentpriceAfterTax,String tax)
    {
        LOG.info("$$$1111 " +GSTCategory+ " categoryType " +categoryType);
            String title="";
            LOG.info("GST Category in products " + GSTCategory);
            if(categoryType.equals("MF"))
            {
                title="Movable Furniture";
            }
            else if(categoryType.equals("NMF"))
            {
                title="Non Movable Furniture";

            }else if(categoryType.equals("SCW"))
            {
                title="Services and Civil Work";

            }

            double tax_amount = round(DesignpriceAfterDsicount - currentpriceAfterTax, 2);

            producttotalPrice+=PriceAfterDiscount;
            producttotalDAP+=DesignpriceAfterDsicount;
            producttotalTaxAmt+=currentpriceAfterTax;
            producttotalPriceAfterTax+=tax_amount;

        /*totalproductPrice+=PriceAfterDiscount;
        totalDAP+=DesignpriceAfterDsicount;
        totalTaxAmt+=currentpriceAfterTax;
        totalPriceAfterTax+=tax_amount;
        //}
        if(tax.equals("18%"))
        {
            set1totalproductPrice+=PriceAfterDiscount;
            set1totalDAP+=DesignpriceAfterDsicount;
            set1totalTaxAmt+=currentpriceAfterTax;
            set1totalPriceAfterTax+=tax_amount;

        }else
        {
            set2totalproductPrice+=PriceAfterDiscount;
            set2totalDAP+=DesignpriceAfterDsicount;
            set2totalTaxAmt+=currentpriceAfterTax;
            set2totalPriceAfterTax+=tax_amount;
        }*/


            PdfPCell cell;
            Paragraph Pindex;
            Font size1 = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD);

            PdfPCell cell1 = new PdfPCell();
            Pindex = new Paragraph(Integer.valueOf(count).toString(), size1);
            Pindex.setAlignment(Element.ALIGN_LEFT);
            cell1.addElement(Pindex);
            tabname.addCell(cell1);

            PdfPCell cell2 = new PdfPCell();
            Pindex = new Paragraph(GSTCategory, fsize);
            Pindex.setAlignment(Element.ALIGN_LEFT);
            cell2.addElement(Pindex);
            tabname.addCell(cell2);

           /* cell = new PdfPCell();
            Pindex = new Paragraph(title, fsize);
            Pindex.setAlignment(Element.ALIGN_LEFT);
            cell.addElement(Pindex);
            tabname.addCell(cell);*/


            PdfPCell cell5 = new PdfPCell();
            Pindex = new Paragraph(tax, fsize);
            Pindex.setAlignment(Element.ALIGN_LEFT);
            cell5.addElement(Pindex);
            tabname.addCell(cell5);

            PdfPCell cell6 = new PdfPCell();
            Pindex = new Paragraph(Double.toString(tax_amount), fsize);
            Pindex.setAlignment(Element.ALIGN_LEFT);
            cell6.addElement(Pindex);
            tabname.addCell(cell6);

            PdfPCell cell7 = new PdfPCell();
            Pindex = new Paragraph(Double.toString(currentpriceAfterTax), fsize);
            Pindex.setAlignment(Element.ALIGN_LEFT);
            cell7.addElement(Pindex);
            tabname.addCell(cell7);
          //  individualcountforProduct++;

        PdfPCell cell4 = new PdfPCell();
        Pindex = new Paragraph(Double.toString(DesignpriceAfterDsicount), fsize);
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell4.addElement(Pindex);
        tabname.addCell(cell4);
        //count++;
    }
    private void createRowAndFillDataForIndividualForAddon(PdfPTable tabname,String Productcategory,String GSTCategory,String category, double PriceAfterDiscount, double DesignpriceAfterDsicount,double currentpriceAfterTax,String tax)
    {
        String title="";
        if(Productcategory.equals("A")) {

            if(category.equals("MF"))
            {
                title="Movable Furniture";


            }
            else if(category.equals("NMF"))
            {
                title="Non Movable Furniture";

            }else if(category.equals("SCW"))
            {
                title="Services and Civil Work";

            }

            LOG.info("GST Category " + GSTCategory);
            //String sequence=BOLD_ALPHABET_SEQUENCE[count];

            double tax_amount = round(DesignpriceAfterDsicount - currentpriceAfterTax, 2);

            addontotalproductPrice+=PriceAfterDiscount;
            addontotalDAP+=DesignpriceAfterDsicount;
            addontotalTaxAmt+=currentpriceAfterTax;
            addontotalPriceAfterTax+=tax_amount;

            if(GSTCategory.equals("MF"))
            {
                title="Movable Furniture";


            }
            else if(GSTCategory.equals("NMF"))
            {
                title="Non Movable Furniture";

            }else if(GSTCategory.equals("SCW"))
            {
                title="Services and Civil Work";

            }
            PdfPCell cell;
            Paragraph Pindex;
            Font size1 = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD);

            PdfPCell cell1 = new PdfPCell();
            Pindex = new Paragraph(Integer.valueOf(individualcountforAddon).toString(), size1);
            Pindex.setAlignment(Element.ALIGN_LEFT);
            cell1.addElement(Pindex);
            tabname.addCell(cell1);

            PdfPCell cell2 = new PdfPCell();
            Pindex = new Paragraph(GSTCategory, fsize);
            Pindex.setAlignment(Element.ALIGN_LEFT);
            cell2.addElement(Pindex);
            tabname.addCell(cell2);

            cell = new PdfPCell();
            Pindex = new Paragraph(title, fsize);
            Pindex.setAlignment(Element.ALIGN_LEFT);
            cell.addElement(Pindex);
            tabname.addCell(cell);


            PdfPCell cell4 = new PdfPCell();
            Pindex = new Paragraph(Double.toString(DesignpriceAfterDsicount), fsize);
            Pindex.setAlignment(Element.ALIGN_LEFT);
            cell4.addElement(Pindex);
            tabname.addCell(cell4);

            PdfPCell cell5 = new PdfPCell();
            Pindex = new Paragraph(tax, fsize);
            Pindex.setAlignment(Element.ALIGN_LEFT);
            cell5.addElement(Pindex);
            tabname.addCell(cell5);

            PdfPCell cell6 = new PdfPCell();
            Pindex = new Paragraph(Double.toString(tax_amount), fsize);
            Pindex.setAlignment(Element.ALIGN_LEFT);
            cell6.addElement(Pindex);
            tabname.addCell(cell6);

            PdfPCell cell7 = new PdfPCell();
            Pindex = new Paragraph(Double.toString(currentpriceAfterTax), fsize);
            Pindex.setAlignment(Element.ALIGN_LEFT);
            cell7.addElement(Pindex);
            tabname.addCell(cell7);
            //count++;
            individualcountforAddon++;
        }
    }

    private void createRowAndFillDataForGSTtotalProductAndAddon(PdfPTable tabname,String GSTCategory,String category, double PriceAfterDiscount, double DesignpriceAfterDsicount,double currentpriceAfterTax,String tax)
    {
        PdfPCell cell;
        Paragraph Pindex;
        Font size1=new Font(Font.FontFamily.TIMES_ROMAN,8,Font.BOLD);

        PdfPCell cell1=new PdfPCell();
        Pindex=new Paragraph();
        cell1.setBackgroundColor(BaseColor.ORANGE);
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell1.addElement(Pindex);
        tabname.addCell(cell1);
        cell1.setRowspan(2);


        cell=new PdfPCell();
        Pindex=new Paragraph(GSTCategory,fsize);
        cell.setBackgroundColor(BaseColor.ORANGE);
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(Pindex);
        tabname.addCell(cell);

        /*PdfPCell cell2=new PdfPCell();
        Pindex=new Paragraph();
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell2.addElement(Pindex);
        tabname.addCell(cell2);*/

        PdfPCell cell2=new PdfPCell();
        Pindex=new Paragraph();
        cell2.setBackgroundColor(BaseColor.ORANGE);
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell2.addElement(Pindex);
        tabname.addCell(cell2);

       /* PdfPCell cell6=new PdfPCell();
        Pindex=new Paragraph();
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell6.addElement(Pindex);
        tabname.addCell(cell6);*/

        PdfPCell cell5=new PdfPCell();
        Pindex=new Paragraph(tax,fsize);
        cell5.setBackgroundColor(BaseColor.ORANGE);
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell5.addElement(Pindex);
        tabname.addCell(cell5);

        PdfPCell cell7=new PdfPCell();
        cell7.setBackgroundColor(BaseColor.ORANGE);
        Pindex=new Paragraph(Double.toString(round(currentpriceAfterTax,2)),fsize);
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell7.addElement(Pindex);
        tabname.addCell(cell7);
        //count++;

        PdfPCell cell4=new PdfPCell();
        cell4.setBackgroundColor(BaseColor.ORANGE);
        Pindex=new Paragraph(Double.toString(DesignpriceAfterDsicount),fsize);
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell4.addElement(Pindex);
        tabname.addCell(cell4);

    }

    private void createRowAndFillForDesign(PdfPTable tabname,String GSTCategory,String category, double PriceAfterDiscount, double DesignpriceAfterDsicount,double currentpriceAfterTax,String tax)
    {
        PdfPCell cell;
        Paragraph Pindex;
        Font size1=new Font(Font.FontFamily.TIMES_ROMAN,8,Font.BOLD);

        PdfPCell cell1=new PdfPCell();
        Pindex=new Paragraph("2",fsize);
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell1.addElement(Pindex);
        tabname.addCell(cell1);
        cell1.setRowspan(2);


        cell=new PdfPCell();
        Pindex=new Paragraph(GSTCategory,fsize);
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(Pindex);
        tabname.addCell(cell);

        /*PdfPCell cell2=new PdfPCell();
        Pindex=new Paragraph();
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell2.addElement(Pindex);
        tabname.addCell(cell2);*/

        PdfPCell cell2=new PdfPCell();
        Pindex=new Paragraph("18%",fsize);
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell2.addElement(Pindex);
        tabname.addCell(cell2);

       /* PdfPCell cell6=new PdfPCell();
        Pindex=new Paragraph();
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell6.addElement(Pindex);
        tabname.addCell(cell6);*/

        PdfPCell cell5=new PdfPCell();
        Pindex=new Paragraph(tax,fsize);
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell5.addElement(Pindex);
        tabname.addCell(cell5);

        PdfPCell cell7=new PdfPCell();
        Pindex=new Paragraph(Double.toString(round(currentpriceAfterTax,2)),fsize);
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell7.addElement(Pindex);
        tabname.addCell(cell7);
        //count++;

        PdfPCell cell4=new PdfPCell();
        Pindex=new Paragraph(Double.toString(DesignpriceAfterDsicount),fsize);
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell4.addElement(Pindex);
        tabname.addCell(cell4);

    }
}
