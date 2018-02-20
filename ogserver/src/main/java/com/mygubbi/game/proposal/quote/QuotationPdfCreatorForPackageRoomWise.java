package com.mygubbi.game.proposal.quote;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mygubbi.game.proposal.ModuleDataService;
import com.mygubbi.game.proposal.ProductAddon;
import com.mygubbi.game.proposal.ProductModule;
import com.mygubbi.game.proposal.model.*;
import com.mygubbi.game.proposal.price.RateCardService;
import com.mygubbi.game.proposal.sow.SpaceRoom;
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
 * Created by Shruthi on 2/19/2018.
 */
public class QuotationPdfCreatorForPackageRoomWise
{
    private final static Logger LOG = LogManager.getLogger(QuotationPdfCreatorForPackageRoomWise.class);
    int count=0;
    int finalcount=1;
    QuoteData quoteData;
    ProposalVersion proposalVersion;
    ProposalHeader proposalHeader;
    List<SpaceRoom> spaceRoomsList;
    Document document = new Document();
    Font fsize1=new Font(Font.FontFamily.TIMES_ROMAN,7,Font.BOLD);
    String series;
    double amt;
    int unitSequence;
    int wunitSequence;
    Double miscCharges=0.0;
    NumberToWord word=new NumberToWord();
    List<QuotationPdfCreatorForPackageRoomWise.customeclass> li,li2;
    List<GSTForProducts> finalmovableList=new ArrayList<>();
    Paragraph p;
    Font fsize3=new Font(Font.FontFamily.TIMES_ROMAN,9,Font.BOLD);
    Font fsize=new Font(Font.FontFamily.TIMES_ROMAN,8,Font.NORMAL);

    Font size1=new Font(Font.FontFamily.TIMES_ROMAN,8, Font.BOLD, BaseColor.RED);
    Font roomNameSizeBOLD=new Font(Font.FontFamily.TIMES_ROMAN,15,Font.BOLD,BaseColor.BLACK);
    Font size3=new Font(Font.FontFamily.TIMES_ROMAN,10,Font.BOLD,BaseColor.BLACK);

    Font roomSummarySize=new Font(Font.FontFamily.TIMES_ROMAN,10,Font.NORMAL,BaseColor.RED);
    Font headingSize=new Font(Font.FontFamily.TIMES_ROMAN,8,Font.BOLD,BaseColor.RED);
    Font headingSizeNew=new com.itextpdf.text.Font(Font.FontFamily.TIMES_ROMAN,10, com.itextpdf.text.Font.BOLD);
    Font roomNameSize=new Font(Font.FontFamily.TIMES_ROMAN,10,Font.BOLD,BaseColor.BLACK);

    Font Size=new Font(Font.FontFamily.TIMES_ROMAN,10,Font.BOLD,BaseColor.BLACK);
    private static final String[] ALPHABET_SEQUENCE = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s"};
    private static final String[] BOLD_ALPHABET_SEQUENCE = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S"};
    private static final String[] ROMAN_SEQUENCE = new String[]{"i", "ii", "iii", "iv", "v", "vi", "vii", "viii", "ix", "x", "xi", "xii", "xiii", "xiv", "xv"};
    List<roomSummary> roomSummaryList=new ArrayList<>();
    java.util.Date date;
    int noOfDaysWorkCompletion;
    java.util.Date currentDate = new java.util.Date(117 ,9,28,0,0,00);
    java.util.Date gstTextChangeDate = new java.util.Date(117 ,10,15,0,0,00);
    java.util.Date miscTextChangeDate = new java.util.Date(118 ,00,4,0,0,00);
    Boolean gstTextChangeDateValue;
    Boolean mscTextChangeDateValue;
    PriceMaster designServicePrice, nonMovablePrice, movablePrice, nonMovablePriceTax, movablePriceTax,scwTax,designServiceTax,projectHandlingTax,floorProtectionTax,deepClearingTax,projectHandlingAmount,floorProtectionAmount,deepClearingAmount;
    List<GSTForProducts> nonMovableList=new ArrayList<>();
    List<GSTForProducts> proposalservicesList=new ArrayList<>();
    List<GSTForProducts> movableList=new ArrayList<>();
    List<GSTForProducts> scwList=new ArrayList<>();
    List<GSTForProducts> designServiceList=new ArrayList<>();
    public static final String DESIGN_SERVICE_PRICE = "DSP";
    public static final String DESIGN_SERVICE_TAX = "DS";
    public static final String NON_MOVABLE_PRICE = "NMP";
    public static final String MOVABLE_PRICE = "MP";
    public static final String NON_MOVABLE_PRICE_TAX = "NMF";
    public static final String MOVABLE_PRICE_TAX = "MF";
    public static final String SCW_PRICE_TAX = "SCW";
    public static final String PROJECT_HANDLING_TAX = "PHT";
    public static final String DEEP_CLEARING_TAX = "DCT";
    public static final String FLOOR_PROTECTION_TAX = "FPT";
    public static final String PROJECT_HANDLING_AMOUNT = "PHC";
    public static final String DEEP_CLEARING_AMOUNT = "DCC";
    public static final String FLOOR_PROTECTION_AMOUNT = "FPC";

    double totalproductPrice=0,totalDAP=0,totalTaxAmt=0,totalPriceAfterTax=0;
    double set1totalproductPrice=0,set1totalDAP=0,set1totalTaxAmt=0,set1totalPriceAfterTax=0;
    double movabletotalproductPrice=0,movabletotalDAP=0,movabletotalTaxAmt=0,movabletotalPriceAfterTax=0;
    double nonmovabletotalproductPrice=0,nonmovabletotalDAP=0,nonmovabletotalTaxAmt=0,nonmovabletotalPriceAfterTax=0;
    double scwtotalproductPrice=0,scwtotalDAP=0,scwtotalTaxAmt=0,scwtotalPriceAfterTax=0;
    double designtotalproductPrice=0,designtotalDAP=0,designtotalTaxAmt=0,designtotalPriceAfterTax=0;
    double producttotalPrice=0,producttotalDAP=0,producttotalTaxAmt=0,producttotalPriceAfterTax=0;
    double misctotalPrice=0,misctotalDAP=0,misctotalTaxAmt=0, misctotalPriceAfterTax=0;
    double addontotalproductPrice=0,addontotalDAP=0,addontotalTaxAmt=0,addontotalPriceAfterTax=0;
    double set2totalproductPrice=0,set2totalDAP=0,set2totalTaxAmt=0,set2totalPriceAfterTax=0;
    Font bookingformfsize=new Font(Font.FontFamily.TIMES_ROMAN,8,Font.NORMAL);
    Font bookingformfsize1=new Font(Font.FontFamily.TIMES_ROMAN,10,Font.BOLD,BaseColor.ORANGE);
    Font zapfdingbats = new Font(Font.FontFamily.ZAPFDINGBATS, 16);
    List<String> customAddonAccessoryList=new ArrayList<>();
    List<String> customAddonAppliancesList=new ArrayList<>();
    List<String> customAddonCounterTopList=new ArrayList<>();
    List<String> customAddonServicesList=new ArrayList<>();
    List<String> customAddonLooseFurnitureList=new ArrayList<>();
    List<AddonsList> customaddonsList=new ArrayList<>();

    QuotationPdfCreatorForPackageRoomWise(QuoteData quoteData, ProposalHeader proposalHeader, ProposalVersion proposalVersion, List<SpaceRoom> spaceRooms)
    {
        LOG.info("quotation package room wise");
        this.date=proposalHeader.getPriceDate();
        this.quoteData=quoteData;
        this.proposalHeader=proposalHeader;
        this.noOfDaysWorkCompletion=proposalHeader.getNoOfDaysforworkcompletion();
        this.proposalVersion=proposalVersion;
        this.spaceRoomsList=spaceRooms;
        miscCharges=proposalVersion.getProjectHandlingAmount()+proposalVersion.getDeepClearingAmount()+proposalVersion.getFloorProtectionAmount();
        if(date.after(currentDate))
        {
            getProducts();
        }
        if(date.after(gstTextChangeDate))
        {
            gstTextChangeDateValue=new Boolean(true);
        }
        else {
            gstTextChangeDateValue=new Boolean(false);
        }
        if(date.after(miscTextChangeDate))
        {
            mscTextChangeDateValue=new Boolean(true);
        }else
        {
            mscTextChangeDateValue=new Boolean(false);
        }
        customAddonAccessoryList.add("Accessories");
        customAddonAppliancesList.add("Appliances");
        customAddonAppliancesList.add("Chimney");
        customAddonAppliancesList.add("Hob");
        customAddonAppliancesList.add("Sink");
        customAddonCounterTopList.add("Counter Top");
        customAddonCounterTopList.add("Granite Counter top");
        customAddonCounterTopList.add("Granite/ Marble");
        customAddonServicesList.add("Acrylic/Metal Ceiling");
        customAddonServicesList.add("Dado");
        customAddonServicesList.add("Door");
        customAddonServicesList.add("Electrical");
        customAddonServicesList.add("False Ceiling");
        customAddonServicesList.add("Flooring");
        customAddonServicesList.add("Glass Partitions");
        customAddonServicesList.add("Lighting");
        customAddonServicesList.add("Others");
        customAddonServicesList.add("Partitions");
        customAddonServicesList.add("Soft Board");
        customAddonServicesList.add("Wall Cladding");
        customAddonServicesList.add("Wall Panelling");
        customAddonServicesList.add("Wall paper");
        customAddonLooseFurnitureList.add("Loose Furniture");
    }

    public void  createpdf(String destination,boolean isValid_Sow)
    {
        try
        {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(destination));
            writer.setPdfVersion(PdfWriter.VERSION_1_7);
            writer.createXmpMetadata();
            document.open();
            writer.setPageEvent(new CustomBorder3());

            if(quoteData.getBookingFormFlag().equals("Yes"))
            {
                Image img = Image.getInstance("logo.png");
                img.setAlignment(Image.MIDDLE);
                img.scaleAbsolute(100,50);
                document.add(img);

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

                p = new Paragraph(" ");
                document.add(p);

                p = new Paragraph(" BOOKING FORM ", headingSizeNew);
                p.setAlignment(Element.ALIGN_CENTER);
                document.add(p);

                p = new Paragraph(" ");
                document.add(p);

                p = new Paragraph("Date:_________________", bookingformfsize);
                p.setAlignment(Element.ALIGN_RIGHT);
                document.add(p);

                p = new Paragraph("Name : Mr/Mrs/Ms/Dr : " + " " + proposalHeader.getName(), bookingformfsize);
                document.add(p);

                p = new Paragraph("Quotation ID: " + proposalHeader.getQuoteNumNew(), bookingformfsize);
                document.add(p);

                p = new Paragraph(" ");
                document.add(p);

                PdfPTable Appdetails = new PdfPTable(1);
                Appdetails.setWidthPercentage(100);
                p=new Paragraph("APPLICANT'S DETAILS",headingSizeNew);
                p.setAlignment(Element.ALIGN_CENTER);
                PdfPCell scell1 = new PdfPCell();
                scell1.addElement(p);
                scell1.setBackgroundColor(BaseColor.ORANGE);
                Appdetails.addCell(scell1);
                document.add(Appdetails);


                p = new Paragraph(" ");
                document.add(p);

                p = new Paragraph("Project Name : " + proposalHeader.getProjectName() + "                                                                                    " +" Apartment No : __________         Floor No : __________ ", bookingformfsize);
                document.add(p);

                p = new Paragraph(" ");
                document.add(p);

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

                PdfPTable orderdetails = new PdfPTable(1);
                orderdetails.setWidthPercentage(100);
                p=new Paragraph("ORDER DETAILS",headingSizeNew);
                p.setAlignment(Element.ALIGN_CENTER);
                scell1 = new PdfPCell();
                scell1.addElement(p);
                scell1.setBackgroundColor(BaseColor.ORANGE);
                orderdetails.addCell(scell1);
                document.add(orderdetails);

                p = new Paragraph(" ");
                document.add(p);

                p = new Paragraph("Sales Order ID : " + proposalHeader.getCrmId(), bookingformfsize);
                document.add(p);

                p = new Paragraph("Order Date : ____________________________________________________________________________________________________________________", bookingformfsize);
                document.add(p);

                p = new Paragraph("Remarks : ______________________________________________________________________________________________________________________", bookingformfsize);
                document.add(p);

                Double val =( quoteData.getTotalCost() + miscCharges.intValue() )- quoteData.getDiscountAmount();

                Double res = val - val % 10;
                p = new Paragraph("Total Quotation Value Rs. " + this.getRoundOffValue(String.valueOf(res.intValue())), bookingformfsize);
                document.add(p);
            }

            document.newPage();

            Image img1 = Image.getInstance("myGubbi_Logo.png");
            img1.setWidthPercentage(50);
            document.add(img1);

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
            float[] columnWidths2 = {4,2};
            PdfPTable table = new PdfPTable(columnWidths2);
            table.setWidthPercentage(100);

            Phrase phrase = new Phrase();
            phrase.add(new Chunk("Quotation For: ",fsize1));
            //phrase.add(new Chunk(proposalHeader.getQuotationFor(),fsize1));
            phrase.add(new Chunk(proposalVersion.getTitle(),fsize1));

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
            document.add(table);

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

            p = new Paragraph("      ");
            p.setAlignment(Element.ALIGN_LEFT);
            document.add(p);

            this.getRoomSummary();

            document.newPage();

            p=new Paragraph(" ");
            document.add(p);

            this.functionForRoomWiseListingNew();

            float[] addonsWidths1 = {1,7,1,1,1,1};
            PdfPTable miscellaneousTable = new PdfPTable(addonsWidths1);
            miscellaneousTable.setWidthPercentage(100);

            PdfPCell Cell1 = new PdfPCell(new Paragraph("SL.NO",fsize1));
            Cell1.setBackgroundColor(BaseColor.ORANGE);
            PdfPCell Cell2 = new PdfPCell(new Paragraph("DESCRIPTION",fsize1));
            Cell2.setBackgroundColor(BaseColor.ORANGE);
            PdfPCell Cell6=new PdfPCell(new Paragraph("UOM",fsize1));
            Cell6.setBackgroundColor(BaseColor.ORANGE);
            PdfPCell Cell3 = new PdfPCell(new Paragraph("QTY",fsize1));
            Cell3.setBackgroundColor(BaseColor.ORANGE);
            PdfPCell Cell4 = new PdfPCell(new Paragraph("PRICE",fsize1));
            Cell4.setBackgroundColor(BaseColor.ORANGE);
            PdfPCell Cell5 = new PdfPCell(new Paragraph("AMOUNT",fsize1));
            Cell5.setBackgroundColor(BaseColor.ORANGE);

            miscellaneousTable.addCell(Cell1);
            miscellaneousTable.addCell(Cell2);
            miscellaneousTable.addCell(Cell6);
            miscellaneousTable.addCell(Cell3);
            miscellaneousTable.addCell(Cell4);
            miscellaneousTable.addCell(Cell5);

            double val2=quoteData.getTotalCost();
            double val3=val2-val2%10;

            p=new Paragraph(" ");
            document.add(p);

            p = new Paragraph("TOTAL PRICE :" +this.getRoundOffValue(String.valueOf((int)quoteData.getTotalCost() + miscCharges.intValue())) ,fsize1);
            p.setAlignment(Element.ALIGN_RIGHT);
            document.add(p);

            p=new Paragraph(" ");
            document.add(p);

            p = new Paragraph("DISCOUNT :" + this.getRoundOffValue(String.valueOf((int) quoteData.discountAmount)), fsize1);
            p.setAlignment(Element.ALIGN_RIGHT);
            document.add(p);

            p=new Paragraph(" ");
            document.add(p);

            Double val = (quoteData.getTotalCost() + miscCharges.intValue()) - quoteData.getDiscountAmount();

            Double res = val - val % 10;
            p = new Paragraph("TOTAL PRICE AFTER DISCOUNT : " +this.getRoundOffValue(String.valueOf(res.intValue())) + "\n" ,fsize1);
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

            p = new Paragraph("* The interiors and services will be delivered within " +noOfDaysWorkCompletion +" days of the design sign off, 50% payment or site readiness whichever is later. ", fsize);
            p.setAlignment(Element.ALIGN_LEFT);
            document.add(p);

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
            String noteParagraphString= new String("1. \t Plumbing, counter top , gas piping ,appliances, hob ,chimney ,sink, taps, electrical shifting, tile laying,Core cutting and civil changes are not considered in kitchen quote. These items are quoted seperately if needed.\n"
                    +"2. \t Final paint quote to be completed after furniture installation by Customer It will be quoted separately if it is in mygubbi scope.\n"
                    +"3. \t Please refer \"Scope of Services\" section at the end for more details of the services scope\n"
                    +"4. \t Installation Charges for Appliances are not part of the Appliance prices. If they are to be accounted for in scope, a separate line item has to be explicitly stated with appropriate estimates.\n"
            );

            if(quoteData.fromVersion.equals("1.0") || quoteData.fromVersion.startsWith("0."))
            {
                noteParagraphString+= "5. \t Rates for Appliances are approximate. The exact amount will be clarified during the DSO Sign Off stage.";
            }
            tab2.addCell(new Paragraph(noteParagraphString,fsize));
            document.add(tab2);

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
                        +"10. Cheque / Demant Draft should be in favour of \"GUBBI TECHNOLOGIES PRIVATE LIMITED.\n"
                        +"11. Installation Charges for Appliances are not part of the Appliance prices. If they are to be accounted for in scope, a separate line item has to be explicitly stated with appropriate estimates.\n",fsize));
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

                PdfPTable gsttotalTableFormovable = new PdfPTable(gstcolumnWidths1);
                gsttotalTableFormovable.setWidthPercentage(100);
                this.createRowAndFillDataForGSTtotalProductAndAddon(gsttotalTableFormovable, "TOTAL"," ",movabletotalproductPrice,movabletotalDAP,movabletotalTaxAmt, String.valueOf(round(movabletotalPriceAfterTax, 2)));

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

                PdfPTable gsttotalTableFornonmovable = new PdfPTable(gstcolumnWidths1);
                gsttotalTableFornonmovable.setWidthPercentage(100);
                this.createRowAndFillDataForGSTtotalProductAndAddon(gsttotalTableFornonmovable, "TOTAL"," ",this.round(nonmovabletotalproductPrice,2),this.round(nonmovabletotalDAP,2),nonmovabletotalTaxAmt, String.valueOf(round(nonmovabletotalPriceAfterTax, 2)));

                count=0;
                PdfPTable scwTable= new PdfPTable(gstcolumnWidths1);
                scwTable.setWidthPercentage(100);
                PdfPCell scwCell1 = new PdfPCell(new Paragraph("SL.NO", fsize1));
                PdfPCell scwCell2 = new PdfPCell(new Paragraph("ITEM", fsize1));
                PdfPCell scwCell5 = new PdfPCell(new Paragraph("GST RATE", fsize1));
                PdfPCell scwCell6 = new PdfPCell(new Paragraph("GST", fsize1));
                PdfPCell scwCell7 = new PdfPCell(new Paragraph("ITEM PRICE", fsize1));
                PdfPCell scwCell4 = new PdfPCell(new Paragraph("TOTAL PRICE", fsize1));
                scwTable.addCell(scwCell1);
                scwTable.addCell(scwCell2);
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

                PdfPTable gsttotalTableForscw = new PdfPTable(gstcolumnWidths1);
                gsttotalTableForscw.setWidthPercentage(100);
                this.createRowAndFillDataForGSTtotalProductAndAddon(gsttotalTableForscw, "TOTAL"," ",scwtotalproductPrice,scwtotalDAP,scwtotalTaxAmt, String.valueOf(round(scwtotalPriceAfterTax, 2)));

                PdfPTable mscTable= new PdfPTable(gstcolumnWidths1);
                mscTable.setWidthPercentage(100);
                PdfPCell mscCell1 = new PdfPCell(new Paragraph("SL.NO", fsize1));
                PdfPCell mscCell2 = new PdfPCell(new Paragraph("ITEM", fsize1));
                PdfPCell mscCell5 = new PdfPCell(new Paragraph("GST RATE", fsize1));
                PdfPCell mscCell6 = new PdfPCell(new Paragraph("GST", fsize1));
                PdfPCell mscCell7 = new PdfPCell(new Paragraph("ITEM PRICE", fsize1));
                PdfPCell mscCell4 = new PdfPCell(new Paragraph("TOTAL PRICE", fsize1));
                mscTable.addCell(mscCell1);
                mscTable.addCell(mscCell2);
                mscTable.addCell(mscCell5);
                mscTable.addCell(mscCell6);
                mscTable.addCell(mscCell7);
                mscTable.addCell(mscCell4);

                count=1;
                for(GSTForProducts proposalServiceList:proposalservicesList)
                {
                    this.createRowForDataForProposalServices(mscTable,count, proposalServiceList.getProducttitle(),proposalServiceList.getCategoryType(),this.round(proposalServiceList.getPriceAfterDiscount(),2), proposalServiceList.getPrice(), proposalServiceList.getPriceAfterTax(), proposalServiceList.getTax());
                }
                PdfPTable gsttotalTableFormsc = new PdfPTable(gstcolumnWidths1);
                gsttotalTableFormsc.setWidthPercentage(100);
                this.createRowAndFillDataForGSTtotalProductAndAddon(gsttotalTableFormsc, "TOTAL"," ", misctotalPrice,misctotalDAP,misctotalTaxAmt, String.valueOf(round(misctotalPriceAfterTax, 2)));

                PdfPTable gstTable = new PdfPTable(gstcolumnWidths1);
                gstTable.setWidthPercentage(100);
                PdfPCell gstCell1 = new PdfPCell(new Paragraph("SL.NO", fsize1));
                PdfPCell gstCell2 = new PdfPCell(new Paragraph("Annexure", fsize1));
                PdfPCell gstCell4 = new PdfPCell(new Paragraph("TOTAL PRICE", fsize1));
                PdfPCell gstCell5 = new PdfPCell(new Paragraph("GST rate", fsize1));
                PdfPCell gstCell7 = new PdfPCell(new Paragraph("GST", fsize1));
                PdfPCell gstCell6 = new PdfPCell(new Paragraph("ITEM PRICE", fsize1));
                gstTable.addCell(gstCell1);
                gstTable.addCell(gstCell2);
                gstTable.addCell(gstCell4);
                gstTable.addCell(gstCell5);
                gstTable.addCell(gstCell7);
                gstTable.addCell(gstCell6);

                this.getfinalGSTList(designServiceList, "Design Services");
                this.getfinalGSTList(movableList, "MF");
                this.getfinalGSTList(nonMovableList, "NMF");
                this.getfinalGSTList(scwList, "SCW");
                this.getfinalGSTList(proposalservicesList,"PS");
                for (GSTForProducts finalList : finalmovableList) {
                    this.createRowAndFillDataForGST(gstTable, finalList.getCategory(), finalList.getPriceAfterDiscount(), finalList.getPrice(), finalList.getPriceAfterTax(), finalList.getTax());
                }
                float[] gsttotalcolumnWidths1 = {1, 4, 1, 1, 1, 1};
                PdfPTable gsttotalTable = new PdfPTable(gsttotalcolumnWidths1);
                gsttotalTable.setWidthPercentage(100);
                this.createRowAndFillDataForGSTtotal(designTable, "TOTAL", totalproductPrice, totalDAP, totalTaxAmt, String.valueOf(round(totalPriceAfterTax, 2)));
                this.createRowAndFillForDesign(designTable, "Design/Consultation Services"," ",this.round(designtotalproductPrice,2),this.round(designtotalDAP,2),designtotalTaxAmt, String.valueOf(round(designtotalPriceAfterTax, 2)));
                this.createRowAndFillDataForGSTtotalProductAndAddon(designTable, "TOTAL"," ",this.round(totalproductPrice+designtotalproductPrice,2),this.round(totalDAP+designtotalDAP,2),this.round(totalTaxAmt+designtotalTaxAmt,2), String.valueOf(this.round(totalPriceAfterTax+designtotalPriceAfterTax,2)));

                float[] gstfinalcolumnWidths1 = {1, 4, 1, 1, 1, 1};
                PdfPTable finalTable = new PdfPTable(gstfinalcolumnWidths1);
                finalTable.setWidthPercentage(100);
                PdfPCell gstfinalCell1 = new PdfPCell(new Paragraph("SL.NO", fsize1));
                PdfPCell gstfinalCell2 = new PdfPCell(new Paragraph("Title", fsize1));
                PdfPCell gstfinalCell4 = new PdfPCell(new Paragraph("TOTAL PRICE", fsize1));
                PdfPCell gstfinalCell5 = new PdfPCell(new Paragraph("GST rate", fsize1));
                PdfPCell gstfinalCell7 = new PdfPCell(new Paragraph("GST", fsize1));
                PdfPCell gstfinalCell6 = new PdfPCell(new Paragraph("ITEM PRICE", fsize1));
                finalTable.addCell(gstfinalCell1);
                finalTable.addCell(gstfinalCell2);
                finalTable.addCell(gstfinalCell4);
                finalTable.addCell(gstfinalCell5);
                finalTable.addCell(gstfinalCell7);
                finalTable.addCell(gstfinalCell6);
                this.createRowAndFillDataForGSTforFinal(finalTable, "Sum of Products and Service Billed @", set1totalproductPrice, set1totalDAP, "18%", set1totalTaxAmt, set1totalPriceAfterTax);
                this.createRowAndFillDataForGSTforFinal(finalTable, "Sum of Products and Service Billed @", set2totalproductPrice, set2totalDAP, "28%", set2totalTaxAmt, set2totalPriceAfterTax);

                float[] gtcolumnWidths1 = {1,4 , 1, 1, 1, 1};
                PdfPTable gstTable1 = new PdfPTable(gtcolumnWidths1);
                gstTable1.setWidthPercentage(100);
                this.createRowAndFillDataForGSTtotal(gstTable1, "TOTAL", totalproductPrice, totalDAP, totalTaxAmt, String.valueOf(round(totalPriceAfterTax, 2)));
                //document.add(gstTable1);

                /*if(quoteData.fromVersion.equals("1.0") || quoteData.fromVersion.startsWith("0."))
                {

                }
                else {
                    if(!gstTextChangeDateValue)
                    {
                        p = new Paragraph("\n");
                        document.add(p);

                        p = new Paragraph("\n");
                        document.add(p);

                        document.newPage();

                        p = new Paragraph("PRICE BREAKUP",fsize3);
                        p.setAlignment(Element.ALIGN_CENTER);
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

                        p=new Paragraph(" ");
                        document.add(p);

                        p=new Paragraph("\nD. Summary\n",fsize1);
                        document.add(p);

                        p=new Paragraph(" ");
                        document.add(p);
                        document.add(gsttotalTable);
                        document.add(designTable);
                    }else
                    {
                        p = new Paragraph("\n");
                        document.add(p);

                        p = new Paragraph("\n");
                        document.add(p);

                        document.newPage();

                        p = new Paragraph("PRICE BREAKUP",fsize3);
                        p.setAlignment(Element.ALIGN_CENTER);
                        document.add(p);

                        p=new Paragraph(" ");
                        document.add(p);

                        p=new Paragraph("The detailed price break-up(post Discount) with the split-up of design, consultancy and GST charges are as follows: ",fsize);
                        document.add(p);


                        p=new Paragraph( "\n A. Appliances\n",fsize1);
                        document.add(p);

                        p=new Paragraph("  ");
                        document.add(p);

                        document.add(individualTable);
                        document.add(gsttotalTableFormovable);

                        p=new Paragraph( "\nB. Interior Works And Furniture\n",fsize1);
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

                        if(mscTextChangeDateValue)
                        {
                            p=new Paragraph( "\n D. Miscellaneous Charges:\n",fsize1);
                            document.add(p);

                            p=new Paragraph(" ");
                            document.add(p);
                            document.add(mscTable);
                            document.add(gsttotalTableFormsc);

                            p=new Paragraph(" ");
                            document.add(p);

                            p=new Paragraph("\nE. Summary\n",fsize1);
                            document.add(p);

                            p=new Paragraph(" ");
                            document.add(p);
                            document.add(gsttotalTable);
                            document.add(designTable);

                        }else
                        {
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

                }*/
            }
            document.close();
        }
        catch (Exception e)
        {
            LOG.info("Exception in quote test" +e);
            e.printStackTrace();
        }
    }

    private void getRoomSummary() throws Exception
    {
        p=new Paragraph("PROJECT SUMMARY",size1);
        document.add(p);

        p=new Paragraph(" ");
        document.add(p);
        float[] addonsWidths1 = {1,7,1,1,1,1};
        PdfPTable miscellaneousTable = new PdfPTable(addonsWidths1);
        miscellaneousTable.setWidthPercentage(100);

        PdfPCell Cell1 = new PdfPCell(new Paragraph("SL.NO",fsize1));
        Cell1.setBackgroundColor(BaseColor.ORANGE);
        PdfPCell Cell2 = new PdfPCell(new Paragraph("DESCRIPTION",fsize1));
        Cell2.setBackgroundColor(BaseColor.ORANGE);
        PdfPCell Cell6=new PdfPCell(new Paragraph("UOM",fsize1));
        Cell6.setBackgroundColor(BaseColor.ORANGE);
        PdfPCell Cell3 = new PdfPCell(new Paragraph("QTY",fsize1));
        Cell3.setBackgroundColor(BaseColor.ORANGE);
        PdfPCell Cell4 = new PdfPCell(new Paragraph("PRICE",fsize1));
        Cell4.setBackgroundColor(BaseColor.ORANGE);
        PdfPCell Cell5 = new PdfPCell(new Paragraph("AMOUNT",fsize1));
        Cell5.setBackgroundColor(BaseColor.ORANGE);

        miscellaneousTable.addCell(Cell1);
        miscellaneousTable.addCell(Cell2);
        miscellaneousTable.addCell(Cell6);
        miscellaneousTable.addCell(Cell3);
        miscellaneousTable.addCell(Cell4);
        miscellaneousTable.addCell(Cell5);

        float[] columnWidths1 = {1,1,1,1,1,1,1,1,1};
        PdfPTable itemsTable=new PdfPTable(columnWidths1);
        itemsTable.setWidthPercentage(100);

        PdfPCell itemsCell1 = new PdfPCell(new Paragraph("SL.NO",fsize1));
        itemsCell1.setBackgroundColor(BaseColor.ORANGE);
        PdfPCell itemsCell2 = new PdfPCell(new Paragraph("ROOM NAME",fsize1));
        itemsCell2.setBackgroundColor(BaseColor.ORANGE);
        PdfPCell itemsCell3 = new PdfPCell(new Paragraph("MODULAR PRODUCT",fsize1));
        itemsCell3.setBackgroundColor(BaseColor.ORANGE);
        PdfPCell itemsCell4 = new PdfPCell(new Paragraph("ADD ON ACCESSORIES",fsize1));
        itemsCell4.setBackgroundColor(BaseColor.ORANGE);
        PdfPCell itemsCell5 = new PdfPCell(new Paragraph("APPLIANCES",fsize1));
        itemsCell5.setBackgroundColor(BaseColor.ORANGE);
        PdfPCell itemsCell6 = new PdfPCell(new Paragraph("COUNTER TOP",fsize1));
        itemsCell6.setBackgroundColor(BaseColor.ORANGE);
        PdfPCell itemsCell7 = new PdfPCell(new Paragraph("SERVICES",fsize1));
        itemsCell7.setBackgroundColor(BaseColor.ORANGE);
        PdfPCell itemsCell8 = new PdfPCell(new Paragraph("LOOSE FURNITURE",fsize1));
        itemsCell8.setBackgroundColor(BaseColor.ORANGE);
        PdfPCell itemsCell10 = new PdfPCell(new Paragraph("PRICE",fsize1));
        itemsCell10.setBackgroundColor(BaseColor.ORANGE);

        itemsTable.addCell(itemsCell1);
        itemsTable.addCell(itemsCell2);
        itemsTable.addCell(itemsCell3);
        itemsTable.addCell(itemsCell5);
        itemsTable.addCell(itemsCell6);
        itemsTable.addCell(itemsCell7);
        itemsTable.addCell(itemsCell8);
        itemsTable.addCell(itemsCell4);
        itemsTable.addCell(itemsCell10);

        List<AssembledProductInQuote> assembledProductInQuotes=this.quoteData.getAssembledProducts();
        Double totalproductCost=0.0;
        Double totalaccessoryCost=0.0;
        Double totalappliancesCost=0.0;
        Double totalcountertopCost=0.0;
        Double totalservicesCost=0.0;
        Double totallooseFurnitureCost=0.0;
        Double totalcustomAddonCost=0.0;
        Double totalPrice=0.0;
        for(SpaceRoom sp:spaceRoomsList)
        {
            Double totalRoomCost=0.0;
            Double productCost=0.0;
            Double accessoryCost=0.0;
            Double appliancesCost=0.0;
            Double countertopCost=0.0;
            Double servicesCost=0.0;
            Double looseFurnitureCost=0.0;
            Double customAddonCost=0.0;
            String roomName=sp.getRoom();
            if(assembledProductInQuotes.size()!=0)
            {
                for(AssembledProductInQuote product:assembledProductInQuotes)
                {
                    if(sp.getRoom().equals(product.getRoom()) && sp.getSpace().equals(product.getSpaceType()))
                    {
                        totalRoomCost+=product.getAmountWithoutAddons();
                        totalPrice+=product.getAmountWithoutAddons();
                        productCost+=product.getAmountWithoutAddons();
                        totalproductCost+=product.getAmountWithoutAddons();

                    }
                }
            }

            if(this.quoteData.getAccessories().size()!=0) {
                for (ProductAddon productAddonAccessories : this.quoteData.getAccessories()) {
                    if (sp.getSpace().equals(productAddonAccessories.getSpaceType()) && sp.getRoom().equals(productAddonAccessories.getRoomCode())) {
                        totalRoomCost += productAddonAccessories.getAmount();
                        totalPrice += productAddonAccessories.getAmount();
                        accessoryCost += productAddonAccessories.getAmount();
                        totalaccessoryCost += productAddonAccessories.getAmount();
                    }
                }
            }
            if(this.quoteData.getAppliances().size()!=0)
            {
                for(ProductAddon productAddonAppliances:this.quoteData.getAppliances())
                {
                    if(sp.getSpace().equals(productAddonAppliances.getSpaceType()) && sp.getRoom().equals(productAddonAppliances.getRoomCode()))
                    {
                        totalRoomCost+=productAddonAppliances.getAmount();
                        totalPrice+=productAddonAppliances.getAmount();
                        appliancesCost+=productAddonAppliances.getAmount();
                        totalappliancesCost+=productAddonAppliances.getAmount();
                    }
                }
            }
            if(this.quoteData.getCounterTops().size()!=0)
            {
                for(ProductAddon productAddonCounterTop:this.quoteData.getCounterTops())
                {
                    if(sp.getSpace().equals(productAddonCounterTop.getSpaceType()) && sp.getRoom().equals(productAddonCounterTop.getRoomCode()))
                    {
                        totalRoomCost+=productAddonCounterTop.getAmount();
                        totalPrice+=productAddonCounterTop.getAmount();
                        countertopCost+=productAddonCounterTop.getAmount();
                        totalcountertopCost+=productAddonCounterTop.getAmount();
                    }
                }
            }
            if(quoteData.getLooseFurniture().size()!=0)
            {
                for (ProductAddon productAddonLooseFurniture: this.quoteData.getLooseFurniture())
                {
                    if(sp.getSpace().equals(productAddonLooseFurniture.getSpaceType()) && sp.getRoom().equals(productAddonLooseFurniture.getRoomCode()))
                    {
                        totalRoomCost+=productAddonLooseFurniture.getAmount();
                        totalPrice+=productAddonLooseFurniture.getAmount();
                        looseFurnitureCost+=productAddonLooseFurniture.getAmount();
                        totallooseFurnitureCost+=productAddonLooseFurniture.getAmount();
                    }
                }
            }
            if(quoteData.getServices().size()!=0) {
                for (ProductAddon productAddonServices : this.quoteData.getServices()) {
                    if (sp.getSpace().equals(productAddonServices.getSpaceType()) && sp.getRoom().equals(productAddonServices.getRoomCode())) {
                        totalRoomCost += productAddonServices.getAmount();
                        totalPrice += productAddonServices.getAmount();
                        servicesCost += productAddonServices.getAmount();
                        totalservicesCost += productAddonServices.getAmount();
                    }
                }
            }
            if(this.quoteData.getCustomAddons().size()!=0)
            {
                for(ProductAddon ProductAddonCustomAddon : this.quoteData.getCustomAddons())
                {
                    if(sp.getSpace().equals(ProductAddonCustomAddon.getSpaceType()) && sp.getRoom().equals(ProductAddonCustomAddon.getRoomCode()))
                    {
                        if(customAddonAccessoryList.contains(ProductAddonCustomAddon.getCustomAddonCategory()))
                        {
                            totalRoomCost+=ProductAddonCustomAddon.getAmount();
                            totalPrice+=ProductAddonCustomAddon.getAmount();
                            accessoryCost+=ProductAddonCustomAddon.getAmount();
                            totalaccessoryCost+=ProductAddonCustomAddon.getAmount();
                            AddonsList addonsList=new AddonsList(ProductAddonCustomAddon.getCustomAddonCategory(),ProductAddonCustomAddon.getTitle(),ProductAddonCustomAddon.getProduct(),ProductAddonCustomAddon.getUom(), ProductAddonCustomAddon.getQuantity(), ProductAddonCustomAddon.getRate(), ProductAddonCustomAddon.getAmount());
                            customaddonsList.add(addonsList);
                        }else if(customAddonAppliancesList.contains(ProductAddonCustomAddon.getCustomAddonCategory()))
                        {
                            totalRoomCost+=ProductAddonCustomAddon.getAmount();
                            totalPrice+=ProductAddonCustomAddon.getAmount();
                            appliancesCost+=ProductAddonCustomAddon.getAmount();
                            totalappliancesCost+=ProductAddonCustomAddon.getAmount();
                            AddonsList addonsList=new AddonsList(ProductAddonCustomAddon.getCustomAddonCategory(),ProductAddonCustomAddon.getTitle(),ProductAddonCustomAddon.getProduct(),ProductAddonCustomAddon.getUom(), ProductAddonCustomAddon.getQuantity(), ProductAddonCustomAddon.getRate(), ProductAddonCustomAddon.getAmount());
                            customaddonsList.add(addonsList);
                        }else if(customAddonCounterTopList.contains(ProductAddonCustomAddon.getCustomAddonCategory()))
                        {
                            totalRoomCost+=ProductAddonCustomAddon.getAmount();
                            totalPrice+=ProductAddonCustomAddon.getAmount();
                            countertopCost+=ProductAddonCustomAddon.getAmount();
                            totalcountertopCost+=ProductAddonCustomAddon.getAmount();
                            AddonsList addonsList=new AddonsList(ProductAddonCustomAddon.getCustomAddonCategory(),ProductAddonCustomAddon.getTitle(),ProductAddonCustomAddon.getProduct(),ProductAddonCustomAddon.getUom(), ProductAddonCustomAddon.getQuantity(), ProductAddonCustomAddon.getRate(), ProductAddonCustomAddon.getAmount());
                            customaddonsList.add(addonsList);
                        }else if(customAddonServicesList.contains(ProductAddonCustomAddon.getCustomAddonCategory()))
                        {
                            totalRoomCost+=ProductAddonCustomAddon.getAmount();
                            totalPrice+=ProductAddonCustomAddon.getAmount();
                            servicesCost+=ProductAddonCustomAddon.getAmount();
                            totalservicesCost+=ProductAddonCustomAddon.getAmount();
                            AddonsList addonsList=new AddonsList(ProductAddonCustomAddon.getCustomAddonCategory(),ProductAddonCustomAddon.getTitle(),ProductAddonCustomAddon.getProduct(),ProductAddonCustomAddon.getUom(), ProductAddonCustomAddon.getQuantity(), ProductAddonCustomAddon.getRate(), ProductAddonCustomAddon.getAmount());
                            customaddonsList.add(addonsList);
                        }else if(customAddonLooseFurnitureList.contains(ProductAddonCustomAddon.getCustomAddonCategory()))
                        {
                            totalRoomCost+=ProductAddonCustomAddon.getAmount();
                            totalPrice+=ProductAddonCustomAddon.getAmount();
                            looseFurnitureCost+=ProductAddonCustomAddon.getAmount();
                            totallooseFurnitureCost+=ProductAddonCustomAddon.getAmount();
                            AddonsList addonsList=new AddonsList(ProductAddonCustomAddon.getCustomAddonCategory(),ProductAddonCustomAddon.getTitle(),ProductAddonCustomAddon.getProduct(),ProductAddonCustomAddon.getUom(), ProductAddonCustomAddon.getQuantity(), ProductAddonCustomAddon.getRate(), ProductAddonCustomAddon.getAmount());
                            customaddonsList.add(addonsList);
                        }
                    }
                }
            }
            roomSummary roomSummary=new roomSummary(roomName,productCost,appliancesCost,countertopCost,servicesCost,looseFurnitureCost,accessoryCost,totalRoomCost);
            roomSummaryList.add(roomSummary);
        }
        /*roomSummary subtotalroomSummary=new roomSummary("Sub Total",totalproductCost,totalappliancesCost,totalcountertopCost,totalservicesCost,totallooseFurnitureCost,totalaccessoryCost,totalPrice);
        roomSummaryList.add(subtotalroomSummary);*/

        int countForRoomSummary=1;
        for(roomSummary roomSummary:roomSummaryList)
        {
            createRowAndFillDataForRoomSummary(itemsTable,String.valueOf(countForRoomSummary),roomSummary.getRoomName(),roomSummary.getProductCost(),roomSummary.getAccessoryCost(),roomSummary.getAppliancesCost(),roomSummary.getCounterTopCost(),roomSummary.getServicesCost(),roomSummary.getLooseFurniturecost(),roomSummary.getTotalRoomCost());
            countForRoomSummary++;
        }
        document.add(itemsTable);

        p = new Paragraph("TOTAL PROJECT PRICE (A) :" +this.getRoundOffValue(String.valueOf(totalPrice.intValue())) ,fsize1);
        p.setAlignment(Element.ALIGN_RIGHT);
        document.add(p);

        if(mscTextChangeDateValue) {

            p = new Paragraph(" ");
            document.add(p);

            p = new Paragraph("MISCELLANEOUS CHARGES \n", size1);
            p.setAlignment(Element.ALIGN_LEFT);
            document.add(p);

            p = new Paragraph(" ");
            document.add(p);

            String projectHandling = "";
            projectHandling = ((Double) projectHandlingAmount.getPrice()).intValue() + "%";

            this.createRowAndFillDataForMiscellaneousForPer(miscellaneousTable, "1", " Project Handling Charges", "%", proposalVersion.getProjectHandlingQty(), projectHandling, proposalVersion.getProjectHandlingAmount());
            this.createRowAndFillDataForMiscellaneous(miscellaneousTable, "2", " House Keeping Charges", "Rs", proposalVersion.getDeepClearingQty(), String.valueOf(round(deepClearingAmount.getPrice(), 0)), round(proposalVersion.getDeepClearingAmount(), 0));
            this.createRowAndFillDataForMiscellaneous(miscellaneousTable, "3", " Floor Protection Charges", "Rs per Sqft", proposalVersion.getFloorProtectionSqft(), String.valueOf(round(floorProtectionAmount.getPrice(), 0)), round(proposalVersion.getFloorProtectionAmount(), 0));
            this.createCellWithDataForMiscTotal(miscellaneousTable,"Sub Total",miscCharges);
            document.add(miscellaneousTable);

            p = new Paragraph("TOTAL MISCELLANEOUS PRICE (B) :" +this.getRoundOffValue(String.valueOf(miscCharges.intValue())) ,fsize1);
            p.setAlignment(Element.ALIGN_RIGHT);
            document.add(p);
        }

        p=new Paragraph(" ");
        document.add(p);

        p = new Paragraph("TOTAL PRICE (A + B) :" +this.getRoundOffValue(String.valueOf((int)quoteData.getTotalCost() + miscCharges.intValue())) ,fsize1);
        p.setAlignment(Element.ALIGN_RIGHT);
        document.add(p);

        p = new Paragraph("DISCOUNT (C) :" + this.getRoundOffValue(String.valueOf((int) quoteData.discountAmount)), fsize1);
        p.setAlignment(Element.ALIGN_RIGHT);
        document.add(p);

        Double val = (quoteData.getTotalCost() + miscCharges.intValue()) - quoteData.getDiscountAmount();

        Double res = val - val % 10;
        p = new Paragraph("TOTAL PRICE AFTER DISCOUNT (A + B - C) : " +this.getRoundOffValue(String.valueOf(res.intValue())) + "\n" ,fsize1);
        p.setAlignment(Element.ALIGN_RIGHT);
        document.add(p);

    }
    private void functionForRoomWiseListingNew() throws Exception
    {
        Paragraph RoomName=new Paragraph("ROOM SUMMARY",size1);
        document.add(RoomName);

        List<AssembledProductInQuote> assembledProductInQuotes=this.quoteData.getAssembledProducts();
        int index = 1;
        int spcount=0;
        int spaceRoomListSize=spaceRoomsList.size();
        for(SpaceRoom sp:spaceRoomsList) {
            customaddonsList.clear();
            spcount++;
            Double totalRoomCost = 0.0;
            Double totalAddonCost=0.0;
            Double productCost = 0.0;
            Double accessoryCost = 0.0;
            Double appliancesCost = 0.0;
            Double countertopCost = 0.0;
            Double servicesCost = 0.0;
            Double looseFurnitureCost = 0.0;
            Double customAddonCost = 0.0;
            String roomName = "";

            p = new Paragraph(spcount + ". " + sp.getRoom(), roomNameSizeBOLD);
            document.add(p);

            p = new Paragraph(" ");
            document.add(p);

            p = new Paragraph("A. MODULAR PRODUCT", size3);
            document.add(p);

            p = new Paragraph(" ");
            document.add(p);

            float[] columnWidths1 = {1, 8, 1, 1, 1};
            PdfPTable itemsTable = new PdfPTable(columnWidths1);
            itemsTable.setWidthPercentage(100);

            PdfPCell itemsCell1 = new PdfPCell(new Paragraph("SL.NO", fsize1));
            itemsCell1.setBackgroundColor(BaseColor.ORANGE);
            PdfPCell itemsCell2 = new PdfPCell(new Paragraph("DESCRIPTION", fsize1));
            itemsCell2.setBackgroundColor(BaseColor.ORANGE);
            PdfPCell itemsCell3 = new PdfPCell(new Paragraph("QTY", fsize1));
            itemsCell3.setBackgroundColor(BaseColor.ORANGE);
            PdfPCell itemsCell4 = new PdfPCell(new Paragraph("PRICE", fsize1));
            itemsCell4.setBackgroundColor(BaseColor.ORANGE);
            PdfPCell itemsCell5 = new PdfPCell(new Paragraph("AMOUNT", fsize1));
            itemsCell5.setBackgroundColor(BaseColor.ORANGE);

            itemsTable.addCell(itemsCell1);
            itemsTable.addCell(itemsCell2);
            itemsTable.addCell(itemsCell3);
            itemsTable.addCell(itemsCell4);
            itemsTable.addCell(itemsCell5);

            int sequenceNumber = 0;
            if (assembledProductInQuotes.size() != 0) {
                for (AssembledProductInQuote product : assembledProductInQuotes) {
                    if (sp.getRoom().equals(product.getRoom()) && sp.getSpace().equals(product.getSpaceType())) {
                        sequenceNumber++;
                        this.fillAssembledProductInfo(itemsTable, sequenceNumber, product);
                        roomName = product.getRoom();
                        totalRoomCost += product.getAmountWithoutAddons();
                        productCost += product.getAmountWithoutAddons();
                    }
                }
                if (sequenceNumber == 0) {
                    this.createRowWithMessage(itemsTable, "No Products");
                }
            } else {
                this.createRowWithMessage(itemsTable, "No Products");
            }
            document.add(itemsTable);

            float[] columnWidthsForAccessories = {1, 2,7, 1, 1, 1, 1};
            PdfPTable AccessoryTable = new PdfPTable(columnWidthsForAccessories);
            AccessoryTable.setWidthPercentage(100);

            PdfPCell itemsCell1ForAccessory = new PdfPCell(new Paragraph("SL.NO", fsize1));
            itemsCell1ForAccessory.setBackgroundColor(BaseColor.ORANGE);
            PdfPCell itemsCell7ForAccessory = new PdfPCell(new Paragraph("CATEGORY", fsize1));
            itemsCell7ForAccessory.setBackgroundColor(BaseColor.ORANGE);
            PdfPCell itemsCell2ForAccessory = new PdfPCell(new Paragraph("DESCRIPTION", fsize1));
            itemsCell2ForAccessory.setBackgroundColor(BaseColor.ORANGE);
            PdfPCell itemsCell6ForAccessory = new PdfPCell(new Paragraph("UOM", fsize1));
            itemsCell6ForAccessory.setBackgroundColor(BaseColor.ORANGE);
            PdfPCell itemsCell3ForAccessory = new PdfPCell(new Paragraph("QTY", fsize1));
            itemsCell3ForAccessory.setBackgroundColor(BaseColor.ORANGE);
            PdfPCell itemsCell4ForAccessory = new PdfPCell(new Paragraph("PRICE", fsize1));
            itemsCell4ForAccessory.setBackgroundColor(BaseColor.ORANGE);
            PdfPCell itemsCell5ForAccessory = new PdfPCell(new Paragraph("AMOUNT", fsize1));
            itemsCell5ForAccessory.setBackgroundColor(BaseColor.ORANGE);

            AccessoryTable.addCell(itemsCell1ForAccessory);
            AccessoryTable.addCell(itemsCell7ForAccessory);
            AccessoryTable.addCell(itemsCell2ForAccessory);
            AccessoryTable.addCell(itemsCell6ForAccessory);
            AccessoryTable.addCell(itemsCell3ForAccessory);
            AccessoryTable.addCell(itemsCell4ForAccessory);
            AccessoryTable.addCell(itemsCell5ForAccessory);
            boolean no_accessories = true;


            p = new Paragraph("B. ADD ON", size3);
            document.add(p);

            p = new Paragraph(" ");
            document.add(p);

            if (this.quoteData.getAccessories().size() != 0) {
                for (ProductAddon productAddonAccessories : this.quoteData.getAccessories()) {
                    if (sp.getSpace().equals(productAddonAccessories.getSpaceType()) && sp.getRoom().equals(productAddonAccessories.getRoomCode())) {
                        totalRoomCost += productAddonAccessories.getAmount();
                        accessoryCost += productAddonAccessories.getAmount();
                        totalAddonCost +=productAddonAccessories.getAmount();
                        AddonsList addonsList = new AddonsList(productAddonAccessories.getCategoryCode(), productAddonAccessories.getTitle(),productAddonAccessories.getExtendedTitle(), productAddonAccessories.getUom(), productAddonAccessories.getQuantity(), productAddonAccessories.getRate(), productAddonAccessories.getAmount());
                        customaddonsList.add(addonsList);
                    }
                }
            }

            if (this.quoteData.getAppliances().size() != 0) {
                for (ProductAddon productAddonAppliances : this.quoteData.getAppliances()) {
                    if (sp.getSpace().equals(productAddonAppliances.getSpaceType()) && sp.getRoom().equals(productAddonAppliances.getRoomCode())) {
                        totalRoomCost += productAddonAppliances.getAmount();
                        appliancesCost += productAddonAppliances.getAmount();
                        totalAddonCost+=productAddonAppliances.getAmount();
                        AddonsList addonsList = new AddonsList(productAddonAppliances.getCategoryCode(), productAddonAppliances.getTitle(),productAddonAppliances.getExtendedTitle(), productAddonAppliances.getUom(), productAddonAppliances.getQuantity(), productAddonAppliances.getRate(), productAddonAppliances.getAmount());
                        customaddonsList.add(addonsList);
                    }
                }
            }
            if (this.quoteData.getCounterTops().size() != 0) {
                for (ProductAddon productAddonCounterTop : this.quoteData.getCounterTops()) {
                    if (sp.getSpace().equals(productAddonCounterTop.getSpaceType()) && sp.getRoom().equals(productAddonCounterTop.getRoomCode())) {
                        totalRoomCost += productAddonCounterTop.getAmount();
                        countertopCost += productAddonCounterTop.getAmount();
                        totalAddonCost+=productAddonCounterTop.getAmount();
                        AddonsList addonsList = new AddonsList(productAddonCounterTop.getCategoryCode(), productAddonCounterTop.getTitle(),productAddonCounterTop.getExtendedTitle(), productAddonCounterTop.getUom(), productAddonCounterTop.getQuantity(), productAddonCounterTop.getRate(), productAddonCounterTop.getAmount());
                        customaddonsList.add(addonsList);
                    }
                }
            }
            if (quoteData.getLooseFurniture().size() != 0) {
                for (ProductAddon productAddonLooseFurniture : this.quoteData.getLooseFurniture()) {
                    if (sp.getSpace().equals(productAddonLooseFurniture.getSpaceType()) && sp.getRoom().equals(productAddonLooseFurniture.getRoomCode())) {
                        totalRoomCost += productAddonLooseFurniture.getAmount();
                        totalAddonCost+=productAddonLooseFurniture.getAmount();
                        looseFurnitureCost += productAddonLooseFurniture.getAmount();
                        AddonsList addonsList = new AddonsList(productAddonLooseFurniture.getCategoryCode(), productAddonLooseFurniture.getTitle(), productAddonLooseFurniture.getExtendedTitle(),productAddonLooseFurniture.getUom(), productAddonLooseFurniture.getQuantity(), productAddonLooseFurniture.getRate(), productAddonLooseFurniture.getAmount());
                        customaddonsList.add(addonsList);
                    }
                }
            }
            if (quoteData.getServices().size() != 0) {
                for (ProductAddon productAddonServices : this.quoteData.getServices()) {
                    if (sp.getSpace().equals(productAddonServices.getSpaceType()) && sp.getRoom().equals(productAddonServices.getRoomCode())) {
                        totalRoomCost += productAddonServices.getAmount();
                        servicesCost += productAddonServices.getAmount();
                        totalAddonCost+=productAddonServices.getAmount();
                        AddonsList addonsList = new AddonsList(productAddonServices.getCategoryCode(), productAddonServices.getTitle(),productAddonServices.getExtendedTitle(), productAddonServices.getUom(), productAddonServices.getQuantity(), productAddonServices.getRate(), productAddonServices.getAmount());
                        customaddonsList.add(addonsList);
                    }
                }
            }
            if (this.quoteData.getCustomAddons().size() != 0)
            {
                for (ProductAddon ProductAddonCustomAddon : this.quoteData.getCustomAddons()) {
                    if (sp.getSpace().equals(ProductAddonCustomAddon.getSpaceType()) && sp.getRoom().equals(ProductAddonCustomAddon.getRoomCode())) {
                        totalRoomCost += ProductAddonCustomAddon.getAmount();
                        customAddonCost += ProductAddonCustomAddon.getAmount();
                        totalAddonCost+=ProductAddonCustomAddon.getAmount();
                        AddonsList addonsList = new AddonsList(ProductAddonCustomAddon.getCustomAddonCategory(), ProductAddonCustomAddon.getTitle(),ProductAddonCustomAddon.getProduct(), ProductAddonCustomAddon.getUom(), ProductAddonCustomAddon.getQuantity(), ProductAddonCustomAddon.getRate(), ProductAddonCustomAddon.getAmount());
                        customaddonsList.add(addonsList);
                    }
                }
            }

            int slNoForAccessories = 0;
            for(AddonsList addonsList:customaddonsList)
            {
                slNoForAccessories++;
                this.createProductTitleRowForAddon(AccessoryTable,"B. " +String.valueOf(slNoForAccessories),addonsList.getCategory(), addonsList.getExtendedTitle());
                this.createRowAndFillDataForAddon(AccessoryTable, "", "","Specification: "+addonsList.getDescription(),addonsList.getUom(),addonsList.getQty(),addonsList.getPrice(),addonsList.getAmount());
            }


            if(customaddonsList.size()==0)
            {
                this.createRowWithMessage(AccessoryTable,"No Additional Add On");
            }
            document.add(AccessoryTable);
            p = new Paragraph(" ");
            document.add(p);

            float[] columnWidthsForRoomTotal = {1, 7, 1, 1, 1};
            PdfPTable roomTotalTable = new PdfPTable(columnWidthsForRoomTotal);
            roomTotalTable.setWidthPercentage(100);

            //this.createCellWithData(roomTotalTable, "Total Room Cost", totalRoomCost);
            document.add(roomTotalTable);

            p=new Paragraph(" ");
            document.add(p);
        }
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
        //this.createCellWithData(tabname,"Total Product Cost",product.getAmountWithoutAddons());

    }

    private void fillAssembledProductUnits(PdfPTable tabname,AssembledProductInQuote product,String series)
    {
        wunitSequence = 0;
        String cname;
        String caption="",caption1="",caption2="",caption3="",caption4="",captionLoft="",captionWardrobe="",captionForQuickBase="",captionForQuickWall="",captionForQuickTall="",captionForQuickLoft="",captionForWardrobe="";
        if(product.getCatagoryName().equals("atvunit"))
        {
            cname="tvunit";
        }else
        {
            cname = product.getCatagoryName();
        }
        String Wcaption="";
        String Kcaption="";
        String baseDimesion="",WallDimesion="",TallDimesion="",loftDimesion="",wardrobeDimesion="",QKitchneBaseDimension="",QKitchneWallDimension="",QKitchneTallDimension="",QKitchenLoftDimension="",QWardrobeDimension="";
        int KBmodulecount=0,KWmoduleCount=0,KTmoduleCount=0,KLmoduleCount=0,SW1modulecount=0,WWmodulecount=0,WW1modulecount=0,QWWmodulecount=0;
        String KBbasecarcass="",KWbasecarcass="",KTbasecarcass="",KLbasecarcass="",SW1basecarcass="",WWbasecarcass="",WW1basecarcass="";
        String KBWallcarcass="",KWwallcarcass="",KTwallcarcass="",KLwallcarcass="",SW1wallcarcass="",WWwallcarcass="",WW1wallcarcass="";
        String KBfinishmaterial="",KWfinishmaterial="",KTfinishmaterial="",KLfinishmaterial="",SW1finishmaterial="",WWfinishmaterial="",WW1finishmaterial="";
        String KBcolorgroupCode="",KWcolorGroupCode="",KTcolorGroupCode="",KLcolorGroupCode="",SW1colorGroupcode="",WWcolorGroupCode="",WW1colorGroupcode="";
        String KBhinge="",KWhinge="",KThinge="",KLhinge="",SW1hinge="",WWhinge="",WW1hinge="";
        String KBfinishtype="",KWfinishtype="",KTfinishtype="",KLfinishtype="",SW1finishtype="",WWfinishtype="",WW1finishtype="";
        double KBamount=0,KWamount=0,KTamount=0,KLamount=0,SW1amount=0,WWamount=0,WW1amount=0;
        int QKBmodulecount=0,QKWmoduleCount=0,QKTmoduleCount=0,QKLmoduleCount=0;
        String QKBbasecarcass="",QKWbasecarcass="",QKTbasecarcass="",QKLbasecarcass="",QWWbasecarcass="";
        String QKBWallcarcass="",QKWwallcarcass="",QKTwallcarcass="",QKLwallcarcass="",QWWwallcarcass="";
        String QKBfinishmaterial="",QKWfinishmaterial="",QKTfinishmaterial="",QKLfinishmaterial="",QWWfinishmaterial="";
        String QKBcolorgroupCode="",QKWcolorGroupCode="",QKTcolorGroupCode="",QKLcolorGroupCode="",QWWcolorGroupCode="";
        String QKBhinge="",QKWhinge="",QKThinge="",QKLhinge="",QWWfinishtype="",QWWhinge="";
        String QKBfinishtype="",QKWfinishtype="",QKTfinishtype="",QKLfinishtype="";
        double QKBamount=0,QKWamount=0,QKTamount=0,QKLamount=0,QWWamount=0;

        int kbwidthSum=0,kbdepthSum=0,kbheightSum=0;
        int qkbwidthSum=0,qkbdepthSum=0,qkbheightSum=0;
        int kwwidthSum=0,kwdepthSum=0,kwheightSum=0;
        int qkwwidthSum=0,qkwdepthSum=0,qkwheightSum=0;
        int ktwidthSum=0,ktdepthSum=0,ktheightSum=0;
        int qktwidthSum=0,qktdepthSum=0,qktheightSum=0;
        int klwidthSum=0,kldepthSum=0,klheightSum=0;
        int qklwidthSum=0,qkldepthSum=0,qklheightSum=0;
        int wrwidthSum=0,wrdepthSum=0, wrheightSum=0;


        unitSequence = 0;
        String basewidth="",wallwidth="",tallwidth="",loftwidth="",wardrobewidth="",wardrobeLoftwidth="",qwardrobewidth="";
        List<String> kwList= new ArrayList<String>();
        List<String> kbList=new ArrayList<String>();
        List<String> ktList=new ArrayList<String>();
        List<String> klList=new ArrayList<String>();
        List<String> kwaList=new ArrayList<String>();
        List<String> quickKitchenbaseList=new ArrayList<>();
        List<String> quickKitchenwallList=new ArrayList<>();
        List<String> quickKitchentallList=new ArrayList<>();
        List<String> quickKitchenLoftList=new ArrayList<>();
        List<String> quickWardrobeList=new ArrayList<>();

        String finish=product.getProduct().getFinishType();
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

        for (AssembledProductInQuote.Unit unit : product.getUnits())
        {
            if(product.getCatagoryName().equalsIgnoreCase("aloft"))
            {
                for(ProductModule module: product.getModules())
                {
                    QKLmoduleCount +=1;
                    QKLbasecarcass = product.getProduct().getBaseCarcassCode();
                    QKLwallcarcass = product.getProduct().getWallCarcassCode();
                    QKLfinishmaterial = ModuleDataService.getInstance().getFinish(product.getProduct().getFinishCode()).getTitle();
                    QKLfinishtype = product.getProduct().getFinishType();
                    QKLcolorGroupCode =product.getProduct().getColorgroupCode();
                    QKLhinge=product.getHingeTitle();
                    QKLamount += module.getAmount();
                    String width = String.valueOf(module.getWidth());
                    quickKitchenLoftList.add(new String(width));
                    qklwidthSum = qklwidthSum + module.getWidth();
                    qklheightSum = module.getHeight();
                    qkldepthSum = module.getDepth();
                    QKitchenLoftDimension= qklwidthSum + " x " + qkldepthSum + " x " + qklheightSum;
                    captionForQuickLoft="Kitchen Loft Unit";
                }
            }else
            if(product.getCatagoryName().equalsIgnoreCase("akitchen"))
            {
                for(ProductModule module:product.getModules())
                {
                    if(module.getMGCode().equals("MG-NS-EX01"))
                    {
                        //base
                        QKBmodulecount +=1;
                        QKBbasecarcass = product.getProduct().getBaseCarcassCode();
                        QKBWallcarcass = product.getProduct().getWallCarcassCode();
                        QKBfinishmaterial = ModuleDataService.getInstance().getFinish(product.getProduct().getFinishCode()).getTitle();
                        QKBfinishtype = product.getProduct().getFinishType();
                        QKBcolorgroupCode =product.getProduct().getColorgroupCode();
                        QKBhinge=product.getHingeTitle();
                        QKBamount += module.getAmount();
                        String width = String.valueOf(module.getWidth());
                        quickKitchenbaseList.add(new String(width));
                        qkbwidthSum = qkbwidthSum + module.getWidth();
                        qkbheightSum = module.getHeight();
                        qkbdepthSum = module.getDepth();
                        QKitchneBaseDimension= qkbwidthSum + " x " + qkbdepthSum + " x " + qkbheightSum;
                        captionForQuickBase="Kitchen Base Unit";

                    }
                    else if(module.getMGCode().equals("MG-NS-EX02"))
                    {
                        //wall
                        QKWmoduleCount +=1;
                        QKWbasecarcass = product.getProduct().getBaseCarcassCode();
                        QKWwallcarcass =product.getProduct().getWallCarcassCode();
                        QKWfinishmaterial = ModuleDataService.getInstance().getFinish(product.getProduct().getFinishCode()).getTitle();
                        QKWfinishtype = product.getProduct().getFinishType();
                        QKWcolorGroupCode =product.getProduct().getColorgroupCode();
                        QKWhinge=product.getHingeTitle();
                        QKWamount += module.getAmount();
                        String width = String.valueOf(module.getWidth());
                        quickKitchenwallList.add(new String(width));
                        qkwwidthSum = qkwwidthSum + module.getWidth();
                        qkwheightSum = module.getHeight();
                        qkwdepthSum = module.getDepth();
                        QKitchneWallDimension= qkwwidthSum + " x " + qkwdepthSum + " x " + qkwheightSum;
                        captionForQuickWall="Kitchen Wall Unit";
                    }
                    else
                    {
                        //tall
                        QKTmoduleCount +=1;
                        QKTbasecarcass = product.getProduct().getBaseCarcassCode();
                        QKTwallcarcass = product.getProduct().getWallCarcassCode();
                        QKTfinishmaterial = ModuleDataService.getInstance().getFinish(product.getProduct().getFinishCode()).getTitle();
                        QKTfinishtype = product.getProduct().getFinishType();
                        QKTcolorGroupCode =product.getProduct().getColorgroupCode();
                        QKThinge=product.getHingeTitle();
                        QKTamount += module.getAmount();
                        String width = String.valueOf(module.getWidth());
                        quickKitchentallList.add(new String(width));
                        qktwidthSum = qktwidthSum + module.getWidth();
                        qktheightSum = module.getHeight();
                        qktdepthSum = module.getDepth();
                        QKitchneTallDimension= qktwidthSum + " x " + qktdepthSum + " x " + qktheightSum;
                        captionForQuickTall="Kitchen Tall Unit";
                    }
                }
            }

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
                        unit.moduleCategory.contains("S - Hinged Wardrobe 2400")  ||
                        unit.moduleCategory.contains("N - Quick Units"))
                {
                    if(     unit.moduleCategory.contains("N - Base Units") ||
                            unit.moduleCategory.contains("S - Kitchen Base Corner Units")||
                            unit.moduleCategory.contains("S - Kitchen Base Drawer Units") ||
                            unit.moduleCategory.contains("S - Kitchen Base Shutter Units") ||
                            unit.moduleCategory.contains("S - Kitchen Wall Sliding Units") ||
                            unit.moduleCategory.contains("S - Storage Module Base Unit") ||
                            unit.moduleCategory.contains("N - Quick Units")){

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
                                unit.moduleCategory.contains("Base unit") ||
                                unit.moduleCategory.contains("N - Quick Units"))
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
            }else if(product.getCatagoryName().equalsIgnoreCase("ahingedwardrobe") || product.getCatagoryName().equalsIgnoreCase("aslidingwardrobe") || product.getCatagoryName().equalsIgnoreCase("awardrobeloft"))
            {
                for(ProductModule module:product.getModules())
                {
                    QWWmodulecount += unit.moduleCount;
                    QWWbasecarcass = product.getProduct().getBaseCarcassCode();
                    QWWwallcarcass = product.getProduct().getWallCarcassCode();
                    QWWfinishmaterial = ModuleDataService.getInstance().getFinish(product.getProduct().getFinishCode()).getTitle();
                    QWWfinishtype = product.getProduct().getFinishType();
                    QWWcolorGroupCode=product.getProduct().getColorgroupCode();
                    QWWhinge=product.getProduct().getHingeType();
                    QWWamount +=module.getAmount() ;

                    String width = String.valueOf(module.getWidth());
                    quickWardrobeList.add(new String(width));
                    wrwidthSum = wrwidthSum + module.getWidth();
                    wrheightSum = module.getHeight();
                    wrdepthSum = module.getDepth();
                    QWardrobeDimension= wrwidthSum + " x " + wrdepthSum + " x " + wrheightSum;
                    if(product.getCatagoryName().equalsIgnoreCase("ahingedwardrobe"))
                    {
                        captionForWardrobe="Hinged Wardrobe";
                    }else if(product.getCatagoryName().equalsIgnoreCase("aslidingwardrobe"))
                    {
                        captionForWardrobe="Sliding Wardrobe";
                    }else
                    {
                        captionForWardrobe="Wardrobe Loft";
                    }
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
                        unit.moduleCategory.contains("S - Sliding Wardrobe with Loft") ||
                        unit.moduleCategory.contains("N - Quick Units"))
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
                    }else if(product.getCatagoryName().equals("ahingedwardrobe"))
                    {
                        captionWardrobe="Hinged Wardrobe";
                    }else if(product.getCatagoryName().equals("aslidingwardrobe"))
                    {
                        captionWardrobe="Sliding Wardrobe";
                    }
                    else
                    {
                        captionWardrobe="Hinged Wardrobe";
                    }


                    if(!(unit.moduleCategory.contains ("N")|| unit.moduleCategory.contains("S - Wardrobe Panels") || unit.moduleCategory.contains ("H - Panel")) ) {

                        String width = unit.getDimensions();
                        wardrobewidth = wardrobewidth + " , " + width;
                        kwaList.add(new String(width));
                    }else if(unit.moduleCategory.contains("N - Quick Units"))
                    {
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
                }
                else if(cname.equals("Storage Modules"))
                {
                    caption4=product.getRoom();
                }
                else if(cname.equals("wallpanelling"))
                {
                    caption4=product.getRoom();
                }
                else if(cname.equals("oswalls"))
                {
                    caption4=product.getRoom();
                }
                else if(cname.equals("Wardrobe"))
                {
                    caption4="Wardrobe Lofts";
                }
                else if(cname.equals("sidetables"))
                {
                    caption4=product.getRoom();
                }
                else if(cname.equals("shoerack"))
                {
                    caption4=product.getRoom();
                }
                else if(cname.equals("Bathroom Vanity"))
                {
                    caption4=product.getRoom();
                }
                else if(cname.equals("tvunit"))
                {
                    caption4=product.getRoom();
                }
                else if(cname.equals("barunit") )
                {
                    caption4=product.getRoom();
                }
                else if(cname.equals("bookshelf"))
                {
                    caption4=product.getRoom();
                }
                else if(cname.equals("crunit") )
                {
                    caption4=product.getRoom();
                }
                else if(cname.equals("wallunits"))
                {
                    caption4=product.getRoom();
                }
                else if(cname.equals("codrawers"))
                {
                    caption4=product.getRoom();
                }
                else if(cname.equals("usstorage"))
                {
                    caption4=product.getRoom();
                }
                else if(cname.equals("poojaunit"))
                {
                    caption4=product.getRoom();
                }
                else if(cname.equals("studytable"))
                {
                    caption4=product.getRoom();
                }
                else if(cname.equals("others"))
                {
                    caption4=product.getRoom();
                }
            }
        }
        li=new ArrayList<QuotationPdfCreatorForPackageRoomWise.customeclass>();
        QuotationPdfCreatorForPackageRoomWise.customeclass obj;
        if(basewidth!="") {
            basewidth =basewidth.substring(2) ;
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

        if(QKBamount!=0)
        {
            obj = new QuotationPdfCreatorForPackageRoomWise.customeclass(tabname, captionForQuickBase, QKBmodulecount, QKBbasecarcass, QKBWallcarcass, QKBfinishmaterial, QKBfinishtype, QKBamount, QKitchneBaseDimension,QKBcolorgroupCode,QKBhinge);
            li.add(obj);
            customFunction(li,unitSequence);
            unitSequence++;
            li.clear();
        }
        if(QKWamount!=0)
        {
            obj = new QuotationPdfCreatorForPackageRoomWise.customeclass(tabname, captionForQuickWall, QKWmoduleCount, QKWbasecarcass, QKWwallcarcass, QKWfinishmaterial, QKWfinishtype, QKWamount, QKitchneWallDimension,QKWcolorGroupCode,QKWhinge);
            li.add(obj);
            customFunction(li,unitSequence);
            unitSequence++;
            li.clear();
        }
        if(QKTamount!=0)
        {
            obj = new QuotationPdfCreatorForPackageRoomWise.customeclass(tabname, captionForQuickTall, QKTmoduleCount, QKTbasecarcass, QKTwallcarcass, QKTfinishmaterial, QKTfinishtype, QKTamount, QKitchneTallDimension,QKTcolorGroupCode,QKThinge);
            li.add(obj);
            customFunction(li,unitSequence);
            unitSequence++;
            li.clear();
        }
        if(KBamount!=0) {
            obj = new QuotationPdfCreatorForPackageRoomWise.customeclass(tabname, caption, KBmodulecount, KBbasecarcass, KBWallcarcass, KBfinishmaterial, KBfinishtype, KBamount, baseDimesion,KBcolorgroupCode,KBhinge);
            li.add(obj);
            customFunction(li,unitSequence);
            unitSequence++;
            li.clear();
        }

        if(KWamount!=0) {
            obj = new QuotationPdfCreatorForPackageRoomWise.customeclass(tabname, caption1, KWmoduleCount, KWbasecarcass, KWwallcarcass, KWfinishmaterial, KWfinishtype, KWamount, WallDimesion,KWcolorGroupCode,KWhinge);
            li.add(obj);
            customFunction(li,unitSequence);
            unitSequence++;
            li.clear();
        }

        if(KTamount!=0) {
            obj = new QuotationPdfCreatorForPackageRoomWise.customeclass(tabname, caption2, KTmoduleCount, KTbasecarcass, KTwallcarcass, KTfinishmaterial, KTfinishtype, KTamount, TallDimesion,KTcolorGroupCode,KThinge);
            li.add(obj);
            customFunction(li,unitSequence);
            unitSequence++;
            li.clear();
        }

        if(KLamount!=0) {
            obj = new QuotationPdfCreatorForPackageRoomWise.customeclass(tabname, caption3, KLmoduleCount, KLbasecarcass, KLwallcarcass, KLfinishmaterial, KLfinishtype, KLamount, loftDimesion,KLcolorGroupCode,KLhinge);
            li.add(obj);
            customFunction(li,unitSequence);
            unitSequence++;
            li.clear();
        }
        if(QKLamount!=0)
        {
            obj = new QuotationPdfCreatorForPackageRoomWise.customeclass(tabname, captionForQuickLoft, QKLmoduleCount, QKLbasecarcass, QKLwallcarcass, QKLfinishmaterial, QKLfinishtype, QKLamount, QKitchenLoftDimension,QKLcolorGroupCode,QKLhinge);
            li.add(obj);
            customFunction(li,unitSequence);
            unitSequence++;
            li.clear();
        }
        if(QWWamount!=0)
        {
            obj = new QuotationPdfCreatorForPackageRoomWise.customeclass(tabname, captionForWardrobe, QWWmodulecount, QWWbasecarcass, QWWwallcarcass, QWWfinishmaterial, QWWfinishtype, QWWamount, QWardrobeDimension,QWWcolorGroupCode,QWWhinge);
            li.add(obj);
            customFunction(li,unitSequence);
            unitSequence++;
            li.clear();
        }
        if(WWamount!=0)
        {

            obj = new QuotationPdfCreatorForPackageRoomWise.customeclass(tabname,captionWardrobe,WWmodulecount,WWbasecarcass,WWwallcarcass,WWfinishmaterial,WWfinishtype,WWamount,wardrobewidth,WWcolorGroupCode,WWhinge);
            li.add(obj);
            customFunction(li,wunitSequence);
            wunitSequence++;
            li.clear();
        }

        if(WW1amount!=0)
        {
            obj = new QuotationPdfCreatorForPackageRoomWise.customeclass(tabname,captionLoft,WW1modulecount,WW1basecarcass,WW1wallcarcass,WW1finishmaterial,WW1finishtype,WW1amount,wardrobeLoftwidth,WW1colorGroupcode,WW1hinge);
            li.add(obj);
            customFunction(li,wunitSequence);
            wunitSequence++;
            li.clear();
        }

        li2=new ArrayList<QuotationPdfCreatorForPackageRoomWise.customeclass>();
        QuotationPdfCreatorForPackageRoomWise.customeclass ob2;

        ob2=new QuotationPdfCreatorForPackageRoomWise.customeclass(tabname,caption4,SW1modulecount,SW1basecarcass,SW1wallcarcass,SW1finishmaterial,SW1finishtype,SW1amount,wardrobewidth,SW1colorGroupcode,SW1hinge);
        li2.add(ob2);

        customFunction(li2,unitSequence);
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
            if(accessory.category.equals("Primary") || accessory.category.equals("Add on")|| accessory.category.equals("Standalone add on"))
            {
                this.createProductTitleRow(tabname,ROMAN_SEQUENCE[acSequence], accessory.title);
                acSequence++;
                if (acSequence == ROMAN_SEQUENCE.length) acSequence = 0;
            }
            PriceMaster accessoryPrice= RateCardService.getInstance().getAccessoryRate(accessory.code,proposalHeader.getPriceDate(),proposalHeader.getProjectCity());
            amount=amount+(accessory.quantity*accessoryPrice.getPrice());
        }
        //this.createCellWithData(tabname,"WoodWork Cost",amt-amount);
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
    private void createCellWithDataForMiscTotal(PdfPTable tabname,String str,double data)
    {
        PdfPCell cell1 = new PdfPCell();
        Paragraph p1=new Paragraph(str,fsize1);
        p1.setAlignment(Element.ALIGN_RIGHT);
        cell1.addElement(p1);
        cell1.setColspan(5);
        tabname.addCell(cell1);

        PdfPCell cell = new PdfPCell();
        Paragraph p=new Paragraph(this.getRoundOffValue(String.valueOf((int)data)),fsize1);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        tabname.addCell(cell);
    }

    private void createCellWithData(PdfPTable tabname,String str,double data)
    {
        PdfPCell cell1 = new PdfPCell();
        Paragraph p1=new Paragraph(str,fsize1);
        p1.setAlignment(Element.ALIGN_RIGHT);
        cell1.addElement(p1);
        cell1.setColspan(4);
        tabname.addCell(cell1);

        PdfPCell cell = new PdfPCell();
        Paragraph p=new Paragraph(this.getRoundOffValue(String.valueOf((int)data)),fsize1);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        tabname.addCell(cell);
    }

    private int customFunction(List<QuotationPdfCreatorForPackageRoomWise.customeclass> li,int unitSequence)
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
                if(li.get(index).getTitle().contains("Kitchen Base Unit") || li.get(index).getTitle().contains("Kitchen Tall Unit")) {

                    String text ="unit consists of " + li.get(index).getModulecount() +
                            " modules as per design provided.\n" + "Carcass: " + li.get(index).getBasecarcass() + "\n" +
                            "Finish Material: " +li.get(index).getFinishtype() + " , Finish Type : " + fmaterial+
                            ((isNotNullorEmpty(li.get(index).getColorGroupCode())) ?  "\n" +"Color : " +li.get(index).getColorGroupCode():"" )+
                            ((isNotNullorEmpty(li.get(index).getHingeType())) ? "\n" + "Hinge : " +li.get(index).getHingeType():"");

                    this.createSubHeadingRow(li.get(index).getTabName(), null, text);

                }else {
                    String text = "unit consists of " + li.get(index).getModulecount() +
                            " modules as per design provided.\n" +  "Carcass: " + li.get(index).getWallcarcass() + "\n" +
                            "Finish Material: " + li.get(index).getFinishtype() + " , Finish Type : " + fmaterial +
                            ((isNotNullorEmpty(li.get(index).getColorGroupCode())) ?  "\n" +"Color : " +li.get(index).getColorGroupCode():"" )+
                            ((isNotNullorEmpty(li.get(index).getHingeType())) ? "\n" + "Hinge : " +li.get(index).getHingeType():"");

                    this.createSubHeadingRow(li.get(index).getTabName(), null, text);
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

                this.createSubHeadingRow(li.get(index).getTabName(), null, text);
                unitSequence++;
                if (unitSequence == ALPHABET_SEQUENCE.length) unitSequence = 0;
            }
        }
        return num;
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

    private boolean isNotNullorEmpty(String s){
        if ((s == null )||( s.length() == 0)){
            return false;
        }else
            return true;
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
        cell1.setColspan(4);
        tabname.addCell(cell);

        /*PdfPCell cell2=new PdfPCell();
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

        Paragraph Pamt = new Paragraph(this.getRoundOffValue(String.valueOf(total.intValue())), fsize);
        Pamt.setAlignment(Element.ALIGN_RIGHT);
        cell3.addElement(Pamt);
        tabname.addCell(cell3);*/

    }

    private void createRowWithMessage(PdfPTable tabname,String message)
    {
        PdfPCell cell = new PdfPCell();
        cell.addElement(new Paragraph(message,fsize));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setColspan(7);
        tabname.addCell(cell);
    }
    private void createProductTitleRowForAddon(PdfPTable tabname,String index, String category,String title)
    {
        Font size1=new Font(Font.FontFamily.TIMES_ROMAN,8,Font.BOLD);
        Font size2=new Font(Font.FontFamily.TIMES_ROMAN,8,Font.BOLD|Font.UNDERLINE);

        PdfPCell cell1=new PdfPCell();
        Paragraph Pindex=new Paragraph(index,size1);
        Pindex.setAlignment(Element.ALIGN_CENTER);
        cell1.addElement(Pindex);
        tabname.addCell(cell1);

        PdfPCell cell2=new PdfPCell();
        Paragraph paragraph=new Paragraph(category,size1);
        cell2.addElement(paragraph);
        tabname.addCell(cell2);

        PdfPCell cell3=new PdfPCell();
        Paragraph paragraph1=new Paragraph(title,size1);
        cell3.addElement(paragraph1);
        tabname.addCell(cell3);

        PdfPCell cell = new PdfPCell();
        Paragraph p=new Paragraph("");
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        cell.setColspan(4);
        tabname.addCell(cell);
    }
    private void createRowAndFillDataForAddon(PdfPTable tabname,String index,String category, String title,String UOM, Double quantity, Double amount, Double total)
    {
        PdfPCell cell;
        Paragraph Pindex;
        Font size1=new Font(Font.FontFamily.TIMES_ROMAN,8,Font.BOLD);

        PdfPCell cell1=new PdfPCell();
        Pindex=new Paragraph(index,size1);
        Pindex.setAlignment(Element.ALIGN_CENTER);
        cell1.addElement(Pindex);
        tabname.addCell(cell1);

        cell=new PdfPCell(new Paragraph(category,fsize));
        tabname.addCell(cell);

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
    private void createRowAndFillDataForRoomSummary(PdfPTable tabname,String index, String roomName,Double product , Double accessory, Double appliances, Double counterTop,Double services,Double looseFurniture,Double totalRoomCost)
    {
        if(roomName.equalsIgnoreCase("Sub Total"))
        {
            index=" ";
        }
        PdfPCell cell;
        Paragraph Pindex;
        Font size1=new Font(Font.FontFamily.TIMES_ROMAN,8,Font.BOLD);

        PdfPCell cell1=new PdfPCell();
        Pindex=new Paragraph(index,size1);
        Pindex.setAlignment(Element.ALIGN_CENTER);
        cell1.addElement(Pindex);
        tabname.addCell(cell1);

        cell=new PdfPCell(new Paragraph(roomName,size1));
        tabname.addCell(cell);

        PdfPCell cell5=new PdfPCell();
        Pindex=new Paragraph("0",fsize);
        Pindex.setAlignment(Element.ALIGN_RIGHT);
        cell5.addElement(Pindex);
        tabname.addCell(cell5);

        PdfPCell cell2=new PdfPCell();
        Pindex=new Paragraph("0",fsize);
        Pindex.setAlignment(Element.ALIGN_RIGHT);
        cell2.addElement(Pindex);
        tabname.addCell(cell2);


        PdfPCell cell4 = new PdfPCell();
        Pindex = new Paragraph("0", fsize);
        Pindex.setAlignment(Element.ALIGN_RIGHT);
        cell4.addElement(Pindex);
        tabname.addCell(cell4);

        PdfPCell cell3 = new PdfPCell();
        Paragraph Pamt = new Paragraph("0", fsize);
        Pamt.setAlignment(Element.ALIGN_RIGHT);
        cell3.addElement(Pamt);
        tabname.addCell(cell3);

        PdfPCell cell6=new PdfPCell();
        Pindex=new Paragraph("0",fsize);
        Pindex.setAlignment(Element.ALIGN_RIGHT);
        cell6.addElement(Pindex);
        tabname.addCell(cell6);

        PdfPCell cell7=new PdfPCell();
        Pindex=new Paragraph("0",fsize);
        Pindex.setAlignment(Element.ALIGN_RIGHT);
        cell7.addElement(Pindex);
        tabname.addCell(cell7);

        PdfPCell cell9=new PdfPCell();
        Pindex=new Paragraph("0",fsize);
        Pindex.setAlignment(Element.ALIGN_RIGHT);
        cell9.addElement(Pindex);
        tabname.addCell(cell9);
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
        projectHandlingTax=RateCardService.getInstance().getFactorRate(PROJECT_HANDLING_TAX,proposalHeader.getPriceDate(),proposalHeader.getProjectCity());
        floorProtectionTax=RateCardService.getInstance().getFactorRate(FLOOR_PROTECTION_TAX,proposalHeader.getPriceDate(),proposalHeader.getProjectCity());
        deepClearingTax=RateCardService.getInstance().getFactorRate(DEEP_CLEARING_TAX,proposalHeader.getPriceDate(),proposalHeader.getProjectCity());
        projectHandlingAmount=RateCardService.getInstance().getFactorRate(PROJECT_HANDLING_AMOUNT,proposalHeader.getPriceDate(),proposalHeader.getProjectCity());
        floorProtectionAmount=RateCardService.getInstance().getFactorRate(FLOOR_PROTECTION_AMOUNT,proposalHeader.getPriceDate(),proposalHeader.getProjectCity());
        deepClearingAmount=RateCardService.getInstance().getFactorRate(DEEP_CLEARING_AMOUNT,proposalHeader.getPriceDate(),proposalHeader.getProjectCity());

        List<AssembledProductInQuote> assembledProducts = this.quoteData.getAssembledProducts();
        getProposalServices();
        for (AssembledProductInQuote product : assembledProducts)
        {
            ProductCategoryMap productCategoryMap = ModuleDataService.getInstance().getProductCategoryMap(product.getCatagoryName(),proposalHeader.getPriceDate());
            String productType = productCategoryMap.getType();
            if(productType.equals("NMF"))
            {
                double discountAmount = product.getTotalAmount() * (quoteData.getDiscountPercentage() / 100.0);
                double pricewithTax=0,price=0,dsWithTax=0,dsPrice=0;
                price=(product.getTotalAmount()-discountAmount)*(nonMovablePrice.getPrice()/100);
                pricewithTax=price*nonMovablePriceTax.getSourcePrice();
                dsPrice=(product.getTotalAmount()-discountAmount)*(designServicePrice.getPrice()/100);
                dsWithTax=dsPrice*designServiceTax.getSourcePrice();
                String tax="18%";
                GSTForProducts nonmovable=new GSTForProducts(product.getCatagoryName(),product.getTitle(),(product.getTotalAmount()-discountAmount),price,pricewithTax,tax,"P",productType);
                nonMovableList.add(nonmovable);
                GSTForProducts designservice=new GSTForProducts(product.getCatagoryName(),product.getTitle(),(product.getTotalAmount()-discountAmount),dsPrice,dsWithTax,tax,"P",productType);
                designServiceList.add(designservice);
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
            }
        }
        List<ProductAddon> productAddonsforAccessories=this.quoteData.getAccessories();
        for(ProductAddon productAddon:productAddonsforAccessories)
        {
            ProductCategoryMap productCategoryMap = ModuleDataService.getInstance().getProductCategoryMap(productAddon.getCategoryCode(),proposalHeader.getPriceDate());
            String productType = productCategoryMap.getType();
            String tax="28%";
            if(productType.equals("MF"))
            {
                double pricewithTax=0;
                pricewithTax=productAddon.getAmount()*movablePriceTax.getSourcePrice();
                GSTForProducts movable=new GSTForProducts(productAddon.getCategoryCode(),productAddon.getExtendedTitle(),productAddon.getAmount(),productAddon.getAmount(),pricewithTax,tax,"A",productType);
                movableList.add(movable);
            }else if(productType.equalsIgnoreCase("NMF"))
            {
                double pricewithTax=0;
                pricewithTax=productAddon.getAmount()*nonMovablePriceTax.getSourcePrice();
                GSTForProducts movable=new GSTForProducts(productAddon.getCategoryCode(),productAddon.getExtendedTitle(),productAddon.getAmount(),productAddon.getAmount(),pricewithTax,"18%","A",productType);
                nonMovableList.add(movable);
            }

        }
        List<ProductAddon> productAddonsforAppliances=this.quoteData.getAppliances();
        for(ProductAddon productAddon:productAddonsforAppliances)
        {
            ProductCategoryMap productCategoryMap = ModuleDataService.getInstance().getProductCategoryMap(productAddon.getCategoryCode(),proposalHeader.getPriceDate());
            String productType = productCategoryMap.getType();
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
        for(ProductAddon productAddon:productAddonsforcounterTop)
        {
            ProductCategoryMap productCategoryMap = ModuleDataService.getInstance().getProductCategoryMap(productAddon.getCategoryCode(),proposalHeader.getPriceDate());
            String productType = productCategoryMap.getType();
            String tax="18%";
            if(productType.equals("SCW"))
            {
                double pricewithTax=0;
                pricewithTax=productAddon.getAmount()*scwTax.getSourcePrice();
                GSTForProducts scw=new GSTForProducts(productAddon.getCategoryCode(),productAddon.getExtendedTitle(),productAddon.getAmount(),productAddon.getAmount(),pricewithTax,tax,"A",productType);
                scwList.add(scw);
            }
        }

        List<ProductAddon> productAddonsforservices=this.quoteData.getServices();
        for(ProductAddon productAddon:productAddonsforservices)
        {
            ProductCategoryMap productCategoryMap = ModuleDataService.getInstance().getProductCategoryMap(productAddon.getCategoryCode(),proposalHeader.getPriceDate());
            String productType = productCategoryMap.getType();
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
        for(ProductAddon productAddon:productAddonsforLooseFurniture)
        {
            ProductCategoryMap productCategoryMap = ModuleDataService.getInstance().getProductCategoryMap(productAddon.getCategoryCode(),proposalHeader.getPriceDate());
            String productType = productCategoryMap.getType();
            String tax="28%";
            if(productType.equals("MF"))
            {
                double pricewithTax=0;
                pricewithTax=productAddon.getAmount()*movablePriceTax.getSourcePrice();
                GSTForProducts movable=new GSTForProducts(productAddon.getCategoryCode(),productAddon.getExtendedTitle(),productAddon.getAmount(),productAddon.getAmount(),pricewithTax,tax,"A",productType);
                movableList.add(movable);
            }
            else if(productType.equalsIgnoreCase("NMF"))
            {
                double pricewithTax=0;
                pricewithTax=productAddon.getAmount()*nonMovablePriceTax.getSourcePrice();
                GSTForProducts movable=new GSTForProducts(productAddon.getCategoryCode(),productAddon.getExtendedTitle(),productAddon.getAmount(),productAddon.getAmount(),pricewithTax,"18%","A",productType);
                nonMovableList.add(movable);
            }

        }

        List<ProductAddon> productAddonsforcustomAddon=this.quoteData.getCustomAddons();
        for(ProductAddon productAddon:productAddonsforcustomAddon)
        {
            ProductCategoryMap productCategoryMap = ModuleDataService.getInstance().getProductCategoryMap(productAddon.getCategoryCode(),proposalHeader.getPriceDate());
            String productType = productCategoryMap.getType();
            String tax="18%";
            if(productType.equals("SCW"))
            {
                double pricewithTax=0;
                pricewithTax=productAddon.getAmount()*scwTax.getSourcePrice();
                GSTForProducts scw=new GSTForProducts(productAddon.getCategoryCode(),productAddon.getCustomTitle(),productAddon.getAmount(),productAddon.getAmount(),pricewithTax,tax,"A",productType);
                scwList.add(scw);
            }
        }
        GSTForProducts movableProducts;
        for(GSTForProducts movable:movableList)
        {
            double tax_amount=movable.getPrice()-movable.getPriceAfterTax();
            movabletotalproductPrice+=movable.getPriceAfterDiscount();
            movabletotalDAP+=movable.getPrice();
            movabletotalTaxAmt+=movable.getPriceAfterTax();
            movabletotalPriceAfterTax+=this.round(tax_amount,2);
        }
        for(GSTForProducts nonmovable : nonMovableList)
        {
            double tax_amount=nonmovable.getPrice()-nonmovable.getPriceAfterTax();
            nonmovabletotalproductPrice+=nonmovable.getPriceAfterDiscount();
            nonmovabletotalDAP+=nonmovable.getPrice();
            nonmovabletotalTaxAmt+=nonmovable.getPriceAfterTax();
            nonmovabletotalPriceAfterTax+=this.round(tax_amount,2);
        }
        for(GSTForProducts scw : scwList)
        {
            double tax_amount=scw.getPrice()-scw.getPriceAfterTax();
            scwtotalproductPrice+=scw.getPriceAfterDiscount();
            scwtotalDAP+=scw.getPrice();
            scwtotalTaxAmt+=scw.getPriceAfterTax();
            scwtotalPriceAfterTax+=this.round(tax_amount,2);
        }
        for(GSTForProducts design: designServiceList)
        {
            double tax_amount=design.getPrice()-design.getPriceAfterTax();
            designtotalproductPrice+=design.getPriceAfterDiscount();
            designtotalDAP+=design.getPrice();
            designtotalTaxAmt+=design.getPriceAfterTax();
            designtotalPriceAfterTax+=this.round(tax_amount,2);
        }
        for(GSTForProducts miscellaneous: proposalservicesList)
        {
            double tax_amount=miscellaneous.getPrice()-miscellaneous.getPriceAfterTax();
            misctotalPrice+=miscellaneous.getPriceAfterDiscount();
            misctotalDAP+=miscellaneous.getPrice();
            misctotalTaxAmt+=miscellaneous.getPriceAfterTax();
            misctotalPriceAfterTax+=this.round(tax_amount,2);
        }
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

    public void getProposalServices()
    {
        double projectHandlingwithTax=proposalVersion.getProjectHandlingAmount()*projectHandlingTax.getSourcePrice();
        double deepClearingwithTax=proposalVersion.getDeepClearingAmount()*deepClearingTax.getSourcePrice();
        double floorProtectionwithTax=proposalVersion.getFloorProtectionAmount()*floorProtectionTax.getSourcePrice();
        GSTForProducts gstPHC=new GSTForProducts("Proposal Services","Project Handling Charges",proposalVersion.getProjectHandlingAmount(),proposalVersion.getProjectHandlingAmount(),projectHandlingwithTax,"18%","A","NA");
        GSTForProducts gstDCC=new GSTForProducts("Proposal Services","House Keeping Charges",proposalVersion.getDeepClearingAmount(),proposalVersion.getDeepClearingAmount(),deepClearingwithTax,"18%","A","NA");
        GSTForProducts gstFPC=new GSTForProducts("Proposal Services","Floor Protection Charges",proposalVersion.getFloorProtectionAmount(),proposalVersion.getFloorProtectionAmount(),floorProtectionwithTax,"18%","A","NA");
        proposalservicesList.add(gstPHC);
        proposalservicesList.add(gstDCC);
        proposalservicesList.add(gstFPC);
    }

    private void createRowAndFillDataForMiscellaneousForPer(PdfPTable tabname,String index, String title,String uom, Double quantity, String amount, Double total)
    {
        PdfPCell cell;
        Paragraph Pindex;
        Font size1=new Font(Font.FontFamily.TIMES_ROMAN,8,Font.BOLD);

        PdfPCell cell1=new PdfPCell();
        Pindex=new Paragraph(index,size1);
        Pindex.setAlignment(Element.ALIGN_CENTER);
        cell1.addElement(Pindex);
        tabname.addCell(cell1);

        cell=new PdfPCell(new Paragraph(title,size1));
        tabname.addCell(cell);

        PdfPCell cl2=new PdfPCell();
        Pindex=new Paragraph(uom,fsize);
        Pindex.setAlignment(Element.ALIGN_RIGHT);
        cl2.addElement(Pindex);
        tabname.addCell(cl2);

        PdfPCell cell2=new PdfPCell();
        Pindex=new Paragraph(this.getRoundOffValue(String.valueOf(quantity.intValue())),fsize);
        Pindex.setAlignment(Element.ALIGN_RIGHT);
        cell2.addElement(Pindex);
        tabname.addCell(cell2);


        PdfPCell cell4 = new PdfPCell();
        Pindex = new Paragraph((amount), fsize);
        Pindex.setAlignment(Element.ALIGN_RIGHT);
        cell4.addElement(Pindex);
        tabname.addCell(cell4);

        PdfPCell cell3 = new PdfPCell();

        Paragraph Pamt = new Paragraph(this.getRoundOffValue(String.valueOf(Double.valueOf(total).intValue())), fsize);
        Pamt.setAlignment(Element.ALIGN_RIGHT);
        cell3.addElement(Pamt);
        tabname.addCell(cell3);

    }
    private void createRowForDataForProposalServices(PdfPTable tabname,int count,String GSTCategory,String categoryType, double PriceAfterDiscount, double DesignpriceAfterDsicount,double currentpriceAfterTax,String tax)
    {
        double tax_amount = round(DesignpriceAfterDsicount - currentpriceAfterTax, 2);
        /*producttotalPrice+=PriceAfterDiscount;
        producttotalDAP+=DesignpriceAfterDsicount;
        producttotalTaxAmt+=currentpriceAfterTax;
        producttotalPriceAfterTax+=tax_amount;*/

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

        PdfPCell cell4 = new PdfPCell();
        Pindex = new Paragraph(Double.toString(DesignpriceAfterDsicount), fsize);
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell4.addElement(Pindex);
        tabname.addCell(cell4);

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

        PdfPCell cell2=new PdfPCell();
        Pindex=new Paragraph();
        cell2.setBackgroundColor(BaseColor.ORANGE);
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell2.addElement(Pindex);
        tabname.addCell(cell2);

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
    private void createRowAndFillDataForGSTtotal(PdfPTable tabname,String GSTCategory, double PriceAfterDiscount, double DesignpriceAfterDsicount,double currentpriceAfterTax,String tax)
    {
        PdfPCell cell;
        Paragraph Pindex;
        Font size1=new Font(Font.FontFamily.TIMES_ROMAN,8,Font.BOLD);

        PdfPCell cell1=new PdfPCell();
        Pindex=new Paragraph("1",fsize);
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell1.addElement(Pindex);
        tabname.addCell(cell1);
        cell1.setRowspan(2);

        if(!mscTextChangeDateValue)
        {
            cell=new PdfPCell();
            Pindex=new Paragraph("Product And Services (A+B+C) ",fsize);
            Pindex.setAlignment(Element.ALIGN_LEFT);
            cell.addElement(Pindex);
            tabname.addCell(cell);
        }else
        {
            cell=new PdfPCell();
            Pindex=new Paragraph("Product And Services (A+B+C+D) ",fsize);
            Pindex.setAlignment(Element.ALIGN_LEFT);
            cell.addElement(Pindex);
            tabname.addCell(cell);
        }

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
    }
    private void createRowAndFillDataForGST(PdfPTable tabname,String GSTCategory, double PriceAfterDiscount, double DesignpriceAfterDsicount,double currentpriceAfterTax,String tax)
    {
        String sequence=BOLD_ALPHABET_SEQUENCE[count];
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


        PdfPCell cell2=new PdfPCell();
        Pindex=new Paragraph("18%",fsize);
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell2.addElement(Pindex);
        tabname.addCell(cell2);


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
        Pindex=new Paragraph(Double.toString(DesignpriceAfterDsicount),fsize);
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell4.addElement(Pindex);
        tabname.addCell(cell4);

    }

    private void createRowAndFillDataForIndividualForProducts(PdfPTable tabname,int count,String GSTCategory,String categoryType, double PriceAfterDiscount, double DesignpriceAfterDsicount,double currentpriceAfterTax,String tax)
    {
        String title="";
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

        PdfPCell cell4 = new PdfPCell();
        Pindex = new Paragraph(Double.toString(DesignpriceAfterDsicount), fsize);
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell4.addElement(Pindex);
        tabname.addCell(cell4);
    }

    private void createRowAndFillDataForMiscellaneous(PdfPTable tabname,String index, String title,String uom, Double quantity, String amount, Double total)
    {
        PdfPCell cell;
        Paragraph Pindex;
        Font size1=new Font(Font.FontFamily.TIMES_ROMAN,8,Font.BOLD);

        PdfPCell cell1=new PdfPCell();
        Pindex=new Paragraph(index,size1);
        Pindex.setAlignment(Element.ALIGN_CENTER);
        cell1.addElement(Pindex);
        tabname.addCell(cell1);

        cell=new PdfPCell(new Paragraph(title,size1));
        tabname.addCell(cell);

        PdfPCell cl2=new PdfPCell();
        Pindex=new Paragraph(uom,fsize);
        Pindex.setAlignment(Element.ALIGN_RIGHT);
        cl2.addElement(Pindex);
        tabname.addCell(cl2);

        PdfPCell cell2=new PdfPCell();
        Pindex=new Paragraph((String.valueOf(quantity)),fsize);
        Pindex.setAlignment(Element.ALIGN_RIGHT);
        cell2.addElement(Pindex);
        tabname.addCell(cell2);


        PdfPCell cell4 = new PdfPCell();
        Pindex = new Paragraph(this.getRoundOffValue(String.valueOf(Double.valueOf(amount).intValue())), fsize);
        Pindex.setAlignment(Element.ALIGN_RIGHT);
        cell4.addElement(Pindex);
        tabname.addCell(cell4);

        PdfPCell cell3 = new PdfPCell();

        Paragraph Pamt = new Paragraph(this.getRoundOffValue(String.valueOf(Double.valueOf(total).intValue())), fsize);
        Pamt.setAlignment(Element.ALIGN_RIGHT);
        cell3.addElement(Pamt);
        tabname.addCell(cell3);
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
        }
        else if(productType.equals("PS"))
        {
            title="Proposal Services";
            tax="18%";
        }
        else {
            title="Design Services";
            tax="18%";
        }
        productslist=new GSTForProducts(title," ",productPriceAfterDiscount,actualProductPrice,productTaxPrice,tax);
        finalmovableList.add(productslist);
    }
}
/*
class roomSummary
{
    PdfPTable tableName;
    String roomName;
    double productCost,accessoryCost,appliancesCost,CounterTopCost,servicesCost,looseFurniturecost,customAddonCost,totalRoomCost;

    public roomSummary( String roomName, double productCost, double accessoryCost, double appliancesCost, double counterTopCost, double servicesCost, double looseFurniturecost,double totalRoomCost) {
        this.roomName = roomName;
        this.productCost = productCost;
        this.accessoryCost = accessoryCost;
        this.appliancesCost = appliancesCost;
        CounterTopCost = counterTopCost;
        this.servicesCost = servicesCost;
        this.looseFurniturecost = looseFurniturecost;
        this.totalRoomCost=totalRoomCost;
    }

    public double getTotalRoomCost() {
        return totalRoomCost;
    }

    public void setTotalRoomCost(double totalRoomCost) {
        this.totalRoomCost = totalRoomCost;
    }

    public PdfPTable getTableName() {
        return tableName;
    }

    public void setTableName(PdfPTable tableName) {
        this.tableName = tableName;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public double getProductCost() {
        return productCost;
    }

    public void setProductCost(double productCost) {
        this.productCost = productCost;
    }

    public double getAccessoryCost() {
        return accessoryCost;
    }

    public void setAccessoryCost(double accessoryCost) {
        this.accessoryCost = accessoryCost;
    }

    public double getAppliancesCost() {
        return appliancesCost;
    }

    public void setAppliancesCost(double appliancesCost) {
        this.appliancesCost = appliancesCost;
    }

    public double getCounterTopCost() {
        return CounterTopCost;
    }

    public void setCounterTopCost(double counterTopCost) {
        CounterTopCost = counterTopCost;
    }

    public double getServicesCost() {
        return servicesCost;
    }

    public void setServicesCost(double servicesCost) {
        this.servicesCost = servicesCost;
    }

    public double getLooseFurniturecost() {
        return looseFurniturecost;
    }

    public void setLooseFurniturecost(double looseFurniturecost) {
        this.looseFurniturecost = looseFurniturecost;
    }

    public double getCustomAddonCost() {
        return customAddonCost;
    }

    public void setCustomAddonCost(double customAddonCost) {
        this.customAddonCost = customAddonCost;
    }
}
class AddonsList
{
    String category,description,uom,extendedTitle;
    Double qty,price,amount;

    public AddonsList(String category,String description,String extendedTitle, String uom, Double qty, Double price, Double amount) {
        this.category=category;
        this.description = description;
        this.extendedTitle=extendedTitle;
        this.uom = uom;
        this.qty = qty;
        this.price = price;
        this.amount = amount;
    }

    public String getExtendedTitle() {
        return extendedTitle;
    }

    public void setExtendedTitle(String extendedTitle) {
        this.extendedTitle = extendedTitle;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}

*/
