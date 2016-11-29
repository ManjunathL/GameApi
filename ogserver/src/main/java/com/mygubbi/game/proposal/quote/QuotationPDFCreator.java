package com.mygubbi.game.proposal.quote;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.mygubbi.game.proposal.ModuleDataService;
import com.mygubbi.game.proposal.ProductAddon;
import com.mygubbi.game.proposal.ProductLineItem;
import com.mygubbi.game.proposal.model.ProposalHeader;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
//    public static final String DEST = "d:/wede.pdf";

    private final static Logger LOG = LogManager.getLogger(QuotationPDFCreator.class);

    String series;
    double amt;
    NumberToWord word=new NumberToWord();
    List<QuotationPDFCreator.customeclass> li,li2;

    private static final String[] ALPHABET_SEQUENCE = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j"};
    private static final String[] ROMAN_SEQUENCE = new String[]{"i", "ii", "iii", "iv", "v", "vi", "vii", "viii", "ix", "x", "xi", "xii", "xiii", "xiv", "xv"};

    PdfPTable itemsTable,B1Table;
    QuoteData quoteData;
    public String pdfFile;

    Font fsize=new Font(Font.FontFamily.TIMES_ROMAN,8,Font.NORMAL);
    Font fsize1=new Font(Font.FontFamily.TIMES_ROMAN,8,Font.BOLD);
    Font fsize3=new Font(Font.FontFamily.TIMES_ROMAN,9,Font.BOLD);

    private ProposalHeader proposalHeader;
    QuotationPDFCreator(QuoteData quoteData, ProposalHeader proposalHeader)
    {
        this.quoteData=quoteData;
        this.proposalHeader=proposalHeader;

    }

    public void  createpdf(String destination)
    {   try {

        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(destination));
        writer.setPdfVersion(PdfWriter.VERSION_1_7);
        writer.createXmpMetadata();

        document.open();
            writer.setPageEvent(new CustomBorder());
            Image img = Image.getInstance("C:\\Users\\user\\IdeaProjects\\apidevelopment\\ogserver\\file-uploads\\myGubbi_Logo.png");
            img.setWidthPercentage(50);
        document.add(img);
        Paragraph p;
            p = new Paragraph("No 1502, 1st Floor, 19th Main, Sector 1, HSR Layout",fsize);
        p.setAlignment(Element.ALIGN_LEFT);
        document.add(p);

            p = new Paragraph("Bangalore 560 102, India",fsize);
        p.setAlignment(Element.ALIGN_LEFT);
        document.add(p);

            p = new Paragraph("Phone (080) 49 4999 00",fsize);
        p.setAlignment(Element.ALIGN_LEFT);
        document.add(p);

            p = new Paragraph("QUOTATION",fsize3);
        p.setAlignment(Element.ALIGN_CENTER);
            fsize3.setColor(BaseColor.GRAY);
        document.add(p);

        p = new Paragraph("      ");
        p.setAlignment(Element.ALIGN_LEFT);
        document.add(p);

            float[] columnWidths2 = {4,2};
            PdfPTable table = new PdfPTable(columnWidths2);
        table.setWidthPercentage(100);
        PdfPCell cell1;

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

            Phrase phrase4 = new Phrase();
            phrase4.add(new Chunk("Contact: ",fsize1));
            phrase4.add(new Chunk(proposalHeader.getContact(),fsize));

            Phrase phrase5 = new Phrase();
            phrase5.add(new Chunk("Quotation #: ",fsize1));
            phrase5.add(new Chunk(proposalHeader.getQuoteNum(),fsize));

            Phrase phrase6 = new Phrase();
            phrase6.add(new Chunk("Project Address: ",fsize1));
            phrase6.add(new Chunk(quoteData.concatValuesFromKeys(new String[]{ProposalHeader.PROJECT_NAME, ProposalHeader.PROJECT_ADDRESS1, ProposalHeader.PROJECT_ADDRESS2, ProposalHeader.PROJECT_CITY}, ","),fsize));

            Phrase phrase7 = new Phrase();
            phrase7.add(new Chunk("Customer Id: ",fsize1));
            phrase7.add(new Chunk(proposalHeader.getCustomerId(),fsize));

            table.addCell(phrase);
            table.addCell(phrase1);
            table.addCell(phrase2);
            table.addCell(phrase3);
            table.addCell(phrase4);
            table.addCell(phrase5);
            table.addCell(phrase6);
            table.addCell(phrase7);
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

            float[] columnWidths1 = {1,8,1,1,1};
            itemsTable = new PdfPTable(columnWidths1);
        itemsTable.setWidthPercentage(100);

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

            p = new Paragraph("Estimated Cost(A):" +this.getRoundOffValue(quoteData.productsCost) ,fsize1);
        p.setAlignment(Element.ALIGN_RIGHT);
        document.add(p);

        B1Table = new PdfPTable(5);
        B1Table.setWidthPercentage(100);

            PdfPCell cel1=new PdfPCell();
            p = new Paragraph("APPLIANCES,SERVICES,OTHERS \n ADD ON ACCESSORIES  ",fsize1);
            p.setAlignment(Element.ALIGN_LEFT);
            cel1.addElement(p);
            cel1.setColspan(5);
            cel1.setBorder(Rectangle.NO_BORDER);
            B1Table.addCell(cel1);

            PdfPCell B1Cell1 = new PdfPCell(new Paragraph("",fsize));
            PdfPCell B1Cell2 = new PdfPCell(new Paragraph("",fsize));
            PdfPCell B1Cell3 = new PdfPCell(new Paragraph("",fsize));
            PdfPCell B1Cell4 = new PdfPCell(new Paragraph("",fsize));
            PdfPCell B1Cell5 = new PdfPCell(new Paragraph("",fsize));
        B1Table.addCell(B1Cell1);
        B1Table.addCell(B1Cell2);
        B1Table.addCell(B1Cell3);
        B1Table.addCell(B1Cell4);
        B1Table.addCell(B1Cell5);

        this.fillAddons(B1Table, this.quoteData.getAccessories(), "No additional accessories.");
        document.add(B1Table);

            PdfPTable B2Table = new PdfPTable(columnWidths1);
        B2Table.setWidthPercentage(100);

            PdfPCell cel2=new PdfPCell();
            p = new Paragraph("APPLIANCES",fsize1);
            p.setAlignment(Element.ALIGN_LEFT);
            cel2.addElement(p);
            cel2.setColspan(5);
            cel2.setBorder(Rectangle.NO_BORDER);
            B2Table.addCell(cel2);

            PdfPCell B2Cell1 = new PdfPCell(new Paragraph("",fsize));
            PdfPCell B2Cell2 = new PdfPCell(new Paragraph("",fsize));
            PdfPCell B2Cell3 = new PdfPCell(new Paragraph("",fsize));
            PdfPCell B2Cell4 = new PdfPCell(new Paragraph("",fsize));
            PdfPCell B2Cell5 = new PdfPCell(new Paragraph("",fsize));
        B2Table.addCell(B2Cell1);
        B2Table.addCell(B2Cell2);
        B2Table.addCell(B2Cell3);
        B2Table.addCell(B2Cell4);
        B2Table.addCell(B2Cell5);

        this.fillAddons(B2Table, this.quoteData.getAppliances(), "No additional appliances.");
        document.add(B2Table);

            PdfPTable B3Table = new PdfPTable(columnWidths1);
        B3Table.setWidthPercentage(100);

            PdfPCell cel3=new PdfPCell();
            p = new Paragraph("COUNTER TOP",fsize1);
            p.setAlignment(Element.ALIGN_LEFT);
            cel3.addElement(p);
            cel3.setColspan(5);
            cel3.setBorder(Rectangle.NO_BORDER);
            B3Table.addCell(cel3);

            PdfPCell B3Cell1 = new PdfPCell(new Paragraph("",fsize));
            PdfPCell B3Cell2 = new PdfPCell(new Paragraph("",fsize));
            PdfPCell B3Cell3 = new PdfPCell(new Paragraph("",fsize));
            PdfPCell B3Cell4 = new PdfPCell(new Paragraph("",fsize));
            PdfPCell B3Cell5 = new PdfPCell(new Paragraph("",fsize));
        B3Table.addCell(B3Cell1);
        B3Table.addCell(B3Cell2);
        B3Table.addCell(B3Cell3);
        B3Table.addCell(B3Cell4);
        B3Table.addCell(B3Cell5);

        this.fillAddons(B3Table, this.quoteData.getCounterTops(), "No countertops.");
        document.add(B3Table);

            PdfPTable B4Table = new PdfPTable(columnWidths1);
        B4Table.setWidthPercentage(100);

            PdfPCell cel4=new PdfPCell();
            p = new Paragraph("SERVICES",fsize1);
            p.setAlignment(Element.ALIGN_LEFT);
            cel4.addElement(p);
            cel4.setColspan(5);
            cel4.setBorder(Rectangle.NO_BORDER);
            B4Table.addCell(cel4);

            PdfPCell B4Cell1 = new PdfPCell(new Paragraph("",fsize));
            PdfPCell B4Cell2 = new PdfPCell(new Paragraph("",fsize));
            PdfPCell B4Cell3 = new PdfPCell(new Paragraph("",fsize));
            PdfPCell B4Cell4 = new PdfPCell(new Paragraph("",fsize));
            PdfPCell B4Cell5 = new PdfPCell(new Paragraph("",fsize));
        B4Table.addCell(B4Cell1);
        B4Table.addCell(B4Cell2);
        B4Table.addCell(B4Cell3);
        B4Table.addCell(B4Cell4);
        B4Table.addCell(B4Cell5);

        this.fillAddons(B4Table, this.quoteData.getServices(), "No additional services.");
        document.add(B4Table);

            p = new Paragraph("Estimated Cost(B):" +this.getRoundOffValue(quoteData.addonsCost),fsize1);
            p.setAlignment(Element.ALIGN_RIGHT);
        document.add(p);

            double val2=quoteData.getTotalCost();
            double val3=val2-val2%10;
            double rem1=val2-val3;
            double fin_value1=val2+(10-rem1);


            p = new Paragraph("Estimated Cost(A+B):" +this.getRoundOffValue(quoteData.getTotalCost()) ,fsize1);
        p.setAlignment(Element.ALIGN_RIGHT);
        document.add(p);

            p = new Paragraph("Discount(C):" +this.getRoundOffValue(quoteData.discountAmount) ,fsize1);
            p.setAlignment(Element.ALIGN_RIGHT);
            document.add(p);


            Double fin_value;
            double val = quoteData.getTotalCost() - quoteData.getDiscountAmount();
            //Double grandTotal = totalAfterDiscount + costOfAccessories + addonsTotal;
            Double rem=val%10;

            if(rem<5)
            {
                fin_value=val-rem;
             }
            else
            {
                fin_value=val+(10-rem);
            }
            p = new Paragraph("Estimated Cost After Discount (A+B-C): " +fin_value.intValue() + "\n" ,fsize1);
            p.setAlignment(Element.ALIGN_RIGHT);
            document.add(p);

        p = new Paragraph("      ");
        p.setAlignment(Element.ALIGN_LEFT);
        document.add(p);

        PdfPTable table2=new PdfPTable(1);
        table2.setWidthPercentage(100);

            p=new Paragraph("In words: " +word.convertNumberToWords(fin_value.intValue()) + " Rupees Only" ,fsize1);

            //p=new Paragraph("In words: " +new CurrencyUtil().convert(String.valueOf(fin_value)) ,fsize1);
            table2.addCell(new Paragraph(p));
        document.add(table2);

        p = new Paragraph("      ");
        p.setAlignment(Element.ALIGN_LEFT);
        document.add(p);

            PdfPTable tab1=new PdfPTable(1);
        tab1.setWidthPercentage(100);

            PdfPCell cel5=new PdfPCell();
            p = new Paragraph("Material Specification",fsize1);
        p.setAlignment(Element.ALIGN_LEFT);
            cel5.addElement(p);
            cel5.setBorder(Rectangle.NO_BORDER);
            tab1.addCell(cel5);


            tab1.addCell(new Paragraph
                    ("1. \tPly: \tIS 303- BWR grade for kitchen, MR Grade for wardrobe and other units\n" +
                            "2. \tMdf: \tExterior Grade mdf\n" +
                            "3. \tEdge Banding: \tRehau\n" +
                            "4. \tLaminates: \tGlossy /Matt/Textured/Metalic Laminates by Merino/Greenlam\n" +
                            "5. \tHardwares: \tHettich/Ebco/Rehau\n" +
                            "6. \tAccessories: \tHettich/Haffle/Evershine/Ebco/Grass\n" +
                            "7. \tGlass/Mirror: \tAsahi/ Saint Gobain\n"+
                            "8. \tLacquered Glass: \tSaint Gobain\n" +
                            "9. \tAppliances: \tFaber /Elica/Kaff/Nagold/ Bosch\n" +
                            "10.\tSink: \tCarisyl/Franke/Nirali\n",fsize));

            PdfPCell cel6=new PdfPCell();
            p = new Paragraph(new Paragraph("Other Finishes offered are Acrylic, Foil, PU paint, UV laminated panels,Hardwood of mygubbi make.\t\t\t\t\n",fsize));
        p.setAlignment(Element.ALIGN_LEFT);
            cel6.addElement(p);
            cel6.setBorder(Rectangle.NO_BORDER);
            tab1.addCell(cel6);
            document.add(tab1);

            PdfPTable tab2=new PdfPTable(1);
        tab2.setWidthPercentage(100);

            PdfPCell cel7=new PdfPCell();
            p = new Paragraph("Note:\n",fsize);
        p.setAlignment(Element.ALIGN_LEFT);
            cel7.addElement(p);
            cel7.setBorder(Rectangle.NO_BORDER);
            tab2.addCell(cel7);

            tab2.addCell(new Paragraph("1. \t All 25 mm shelves will be in MDF with both side finish\n"
                    +"2. \t Plumbing, counter top , gas piping ,appliances, hob ,chimney ,sink, taps, electrical shifting, tile laying,Core cutting and civil changes are not considered kitchen quote. These items will be quoted seperately if needed.\n"
                    +"3. \t Final paint quote to be completed after furniture installation by Customer It will be quoted separately if it is in mygubbi scope.\n"
                    ,fsize));

        PdfPTable tab=new PdfPTable(1);
        tab.setWidthPercentage(100);

            Font size2=new Font(Font.FontFamily.TIMES_ROMAN,7,Font.NORMAL);
            p = new Paragraph("Terms and Conditions:\n",size2);
            p.setAlignment(Element.ALIGN_MIDDLE);
            PdfPCell cel9=new PdfPCell();
            cel9.addElement(p);
            cel9.setBorder(Rectangle.NO_BORDER);
            tab.addCell(p);

            /*Font size3=new Font(Font.FontFamily.TIMES_ROMAN,6,Font.BOLD);
            p=new Paragraph("VAT @ 14.5% & S T @14.5%",size3);*/

            tab.addCell(new Paragraph("1. The quote rate is inclusive of Applicable taxes." + ", Taxes are subject to changes, due to Govt. policies & changes in the rate if any, - the same will be chared at the time of submission of bill.\n"
                    +"2. The images depicted in the drawings and presentations are representative only. The final design - drawings provided and approved by the client will be conceived and executed at the site.\n"
                    +"3. The above quoted rate is as per a Typical Apartment and there will be variation in rates as per the final site conditions / Measurements / Specifications / Design.\n"
                    +"4. Quote is valid for 15 Days only",fsize));
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
            table4.addCell(new Paragraph("6. Design Sign up fees amounting to Rs.15000/- or 10% of the Budgetary quote is non-refundable. This amount will be adjusted against the final order value. Booking confirmation shall be acknowledged in the copy of this budgetary proposal.\n"
                    +"7. The 50% advance paid post approval of the design and quote cannot be refunded as the production would have be commenced.\n"
                    +"8. Warranty : 5 years of warranty against any manufacturing defect. The material specifications and brands specified are as per the approved standards of Orangegubbi Technologies Private Limited and covered under warranty.\n"
                    +"9. Any modifications/alterations to the proposed design will have an impact on the techno commercials of this quote and hence new drawings as well as associated commercials will be provided for by MyGubbi if the same occurs.\n"
                    +"10. Delivery shall be within 45 days from order Final Confirmation.\n"
                    +"11. Cheque / Demant Draft should be in favour of \"ORANGEGUBBI TECHNOLOGIES PRIVATE LIMITED.\n",fsize));
        document.add(table4);

        p = new Paragraph("      ");
        p.setAlignment(Element.ALIGN_LEFT);
        document.add(p);

            p = new Paragraph(new Paragraph("THANKS for considering OrangeGubbi!                                                                                                                                                     " + "\t"  + "\t" + "\t" + "\t" + "\t" +"\tAccepted (Sign) ",fsize));
        document.add(p);

        document.close();

        }
        catch(Exception e)
        {
            LOG.info(e.getMessage());
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

            this.fillAssembledProductInfo(tabname,sequenceNumber, product);
            sequenceNumber++;
        }
    }

    private void fillAssembledProductInfo(PdfPTable tabname,int sequenceNumber, AssembledProductInQuote product)
    {
        int num;
        this.createProductTitleRow(tabname,"A." + String.valueOf(sequenceNumber), product.getTitle());

        series="A." +String.valueOf(sequenceNumber) + ".";
        num=this.fillAssembledProductUnits(tabname,product,series);

        amt=product.getAmountWithoutAddons();
        String unitSequenceLetter = ALPHABET_SEQUENCE[num];

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

    private int customFunction(List<QuotationPDFCreator.customeclass> li)
    {

        int unitSequence = 0;
        int num=0;
        for(int index=0;index<li.size();index++)
        {

            if (li.get(index).getAmount()==0)
            {
                return num;
            }
            else if (li.get(index).getTitle().equals("Kitchen Base Unit") || li.get(index).getTitle().equals("Kitchen Tall Unit") || li.get(index).getTitle().equals("Kitchen Wall Unit") || li.get(index).getTitle().equals("Kitchen Lofts"))
            {

                num += 1;
                this.createSubHeadingRow(li.get(index).getTabName(), series + ALPHABET_SEQUENCE[unitSequence], li.get(index).getTitle());
                String fmaterial = li.get(index).getFinishmaterial().replaceAll("\n", "");


                this.createRowAndFillData(li.get(index).getTabName(), null, "unit consists of " + li.get(index).getModulecount() + " modules as per design provided.\n" + "Base Carcass: " + li.get(index).getBasecarcass() + ",Wall Carcass: " + li.get(index).getWallcarcass() + "\n" + "Finish Material: " + fmaterial + " , Finish Type : " + li.get(index).getFinishtype(), 1.0, li.get(index).getAmount(), 0.0);
                unitSequence++;
                if (unitSequence == ALPHABET_SEQUENCE.length) unitSequence = 0;

            }
            else
            {

                num += 1;
                this.createSubHeadingRow(li.get(index).getTabName(), series + ALPHABET_SEQUENCE[unitSequence], li.get(index).getTitle());
                String fmaterial = li.get(index).getFinishmaterial().replaceAll("\n", "");

                this.createRowAndFillData(li.get(index).getTabName(), null, "Base Carcass: " + li.get(index).getBasecarcass() + ",Wall Carcass: " + li.get(index).getWallcarcass() + "\n" + "Finish Material: " + fmaterial + " , Finish Type : " + li.get(index).getFinishtype(), 1.0, li.get(index).getAmount(), 0.0);
                unitSequence++;
                if (unitSequence == ALPHABET_SEQUENCE.length) unitSequence = 0;


            }
        }
        return num;
    }

    private int fillAssembledProductUnits(PdfPTable tabname,AssembledProductInQuote product,String series)
    {
        String caption="",caption1="",caption2="",caption3="",caption4="";
        String cname=product.getCatagoryName();

        int KBmodulecount=0,KWmoduleCount=0,KTmoduleCount=0,KLmoduleCount=0,SW1modulecount=0;
        String KBbasecarcass="",KWbasecarcass="",KTbasecarcass="",KLbasecarcass="",SW1basecarcass="";
        String KBWallcarcass="",KWwallcarcass="",KTwallcarcass="",KLwallcarcass="",SW1wallcarcass="";
        String KBfinishmaterial="",KWfinishmaterial="",KTfinishmaterial="",KLfinishmaterial="",SW1finishmaterial="";
        String KBfinishtype="",KWfinishtype="",KTfinishtype="",KLfinishtype="",SW1finishtype="";
        double KBamount=0,KWamount=0,KTamount=0,KLamount=0,SW1amount=0;

        int unitSequence = 0;

        for (AssembledProductInQuote.Unit unit : product.getUnits())
        {

            if(cname.equals("K") )
            {

                if(unit.title.contains("N - Base Units") ||
                        unit.title.contains("N - Drawer Units") ||
                        unit.title.contains("N - Drawer") ||
                        unit.title.contains("N - Open Units") ||
                        unit.title.contains("N - Panelling") ||
                        unit.title.contains ("N - WoodWork Add On") ||
                        unit.title.contains("S - Kitchen Base Corner Units")||
                        unit.title.contains("S - Kitchen Base Drawer Units") ||
                        unit.title.contains("S - Kitchen Base Shutter Units") ||
                        unit.title.contains("S - Kitchen Panels") ||
                        unit.title.contains("S - Sliding Mechanism") ||
                        unit.title.contains("S - Sliding Wardrobe 2100") ||
                        unit.title.contains("S - Sliding Wardrobe 2400") ||
                        unit.title.contains("S - Storage Module Base Unit") ||
                        unit.title.contains("S - Wardrobe Panels") ||
                        unit.title.contains("S - Bathroom Vanity") ||
                        unit.title.contains("S - Hinged Wardrobe 2100") ||
                        unit.title.contains("S - Hinged Wardrobe 2400"))
                {
                    if(unit.title.contains("N - Base Units") || unit.title.contains("S - Kitchen Base Corner Units")||
                            unit.title.contains("S - Kitchen Base Drawer Units") ||
                            unit.title.contains("S - Kitchen Base Shutter Units") ) {

                        KBmodulecount += unit.moduleCount;
                    }
                    KBbasecarcass = product.getProduct().getBaseCarcassCode();
                    KBWallcarcass = product.getProduct().getWallCarcassCode();
                    KBfinishmaterial = ModuleDataService.getInstance().getFinish(product.getProduct().getFinishCode()).getTitle();
                    KBfinishtype = product.getProduct().getFinishType();
                    KBamount += unit.amount;

                    if(cname.equals("K"))
                    {
                        caption="Kitchen Base Unit";
                    }
                }
                else if (unit.title.contains("S - Kitchen Wall Corner Units")||
                        unit.title.contains("S - Kitchen Wall Flap Up Units") ||
                        unit.title.contains("S - Kitchen Wall Open Units") ||
                        unit.title.contains("S - Kitchen Wall Shutter Units")||
                        unit.title.contains("S - Storage Module Wall Unit")||
                        unit.title.contains("S - Wall Open Units") ||
                        unit.title.contains ("N - Wall Units") )
                {
                    KWmoduleCount += unit.moduleCount;
                    KWbasecarcass = product.getProduct().getBaseCarcassCode();
                    KWwallcarcass = product.getProduct().getWallCarcassCode();
                    KWfinishmaterial = ModuleDataService.getInstance().getFinish(product.getProduct().getFinishCode()).getTitle();
                    KWfinishtype = product.getProduct().getFinishType();
                    KWamount += unit.amount;

                    if(cname.equals("K"))
                    {
                        caption1="Kitchen Wall Unit";
                    }

                }
                else if (unit.title.contains("S - Kitchen Tall Units") ||  unit.title.contains ("N - Tall/Semi Tall Units"))
                {

                    KTmoduleCount += unit.moduleCount;
                    KTbasecarcass = product.getProduct().getBaseCarcassCode();
                    KTwallcarcass = product.getProduct().getWallCarcassCode();
                    KTfinishmaterial = ModuleDataService.getInstance().getFinish(product.getProduct().getFinishCode()).getTitle();
                    KTfinishtype = product.getProduct().getFinishType();
                    KTamount += unit.amount;

                    if(cname.equals("K"))
                    {
                        caption2="Kitchen Tall Unit";
                    }
                }

                else if(unit.title.contains("S - Kitchen Loft Units") || unit.title.contains("S - Sliding Wardrobe with Loft") || unit.title.contains("S - Wardrobe Lofts"))
                {
                    KLmoduleCount += unit.moduleCount;
                    KLbasecarcass = product.getProduct().getBaseCarcassCode();
                    KLwallcarcass = product.getProduct().getWallCarcassCode();
                    KLfinishmaterial = ModuleDataService.getInstance().getFinish(product.getProduct().getFinishCode()).getTitle();
                    KLfinishtype = product.getProduct().getFinishType();
                    KLamount += unit.amount;

                    if(cname.equals("K"))
                    {
                        caption3="Kitchen Lofts";
                    }

                }

            }
            else if(cname.equals("W") || cname.equals("Storage Modules") || cname.equals("wallpanelling")  || cname.equals("oswalls")  || cname.equals("sidetables") ||  cname.equals("shoerack") ||  cname.equals("Bathroom Vanity") ||  cname.equals("tvunit") ||  cname.equals("barunit") || cname.equals("bookshelf") ||  cname.equals("crunit") ||  cname.equals("wallunits"))
            {
                SW1modulecount += unit.moduleCount;
                SW1basecarcass = product.getProduct().getBaseCarcassCode();
                SW1wallcarcass = product.getProduct().getWallCarcassCode();
                SW1finishmaterial = ModuleDataService.getInstance().getFinish(product.getProduct().getFinishCode()).getTitle();
                SW1finishtype = product.getProduct().getFinishType();
                SW1amount += unit.amount;
                if(cname.equals("W"))
                {
                    caption4="Wardrobe";
                }
                else if(cname.equals("K"))
                {
                    caption4="Kitchen Lofts";
                }
                else if(cname.equals("Storage Modules"))
                {
                    caption4="Stroage Modules";
                }
                else if(cname.equals("wallpanelling"))
                {
                    caption4="Wall Panelling";
                }
                else if(cname.equals("oswalls"))
                {
                    caption4="Open Shelves";
                }
                else if(cname.equals("W"))
                {
                    caption4="Wardrobe Lofts";
                }
                else if(cname.equals("sidetables"))
                {
                    caption4="Side Tables";
                }
                else if(cname.equals("shoerack"))
                {
                    caption4=" Shoe Rack";
                }
                else if(cname.equals("Bathroom Vanity"))
                {
                    caption4="Bathroom Vanity";
                }
                else if(cname.equals("tvunit"))
                {
                    caption4="TV unit";
                }
                else if(cname.equals("barunit") )
                {
                    caption4="Bar Unit";
                }
                else if(cname.equals("bookshelf"))
                {
                    caption4="Book Shelf";
                }
                else if(cname.equals("crunit") )
                {
                    caption4="Crockery Unit";
                }
                else if(cname.equals("wallunits"))
                {
                    caption4="Wall Unit";
                }
            }
            else
            {
                this.createSubHeadingRow(tabname, series + ALPHABET_SEQUENCE[unitSequence], unit.title + " - " + unit.getDimensions());
                String S = "Unit consists of " + unit.moduleCount + " modules as per design provided.\n" + "Base Carcass : " + product.getProduct().getBaseCarcassCode() + " , Wall Carcass : " + product.getProduct().getWallCarcassCode() + "\n" + "Finish Material : " + ModuleDataService.getInstance().getFinish(product.getProduct().getFinishCode()).getTitle() + " , Finish Type : " + product.getProduct().getFinishType();
                this.createRowAndFillData(tabname, null, S, 1.0, unit.amount, 0.0);
            unitSequence++;
            if (unitSequence == ALPHABET_SEQUENCE.length) unitSequence = 0;
        }
        }



        li=new ArrayList<QuotationPDFCreator.customeclass>();
        QuotationPDFCreator.customeclass obj;

        obj=new QuotationPDFCreator.customeclass(tabname,caption,KBmodulecount,KBbasecarcass,KBWallcarcass,KBfinishmaterial,KBfinishtype,KBamount);
        li.add(obj);

        obj=new QuotationPDFCreator.customeclass(tabname,caption1,KWmoduleCount,KWbasecarcass,KWwallcarcass,KWfinishmaterial,KWfinishtype,KWamount);
        li.add(obj);

        obj=new QuotationPDFCreator.customeclass(tabname,caption2,KTmoduleCount,KTbasecarcass,KTwallcarcass,KTfinishmaterial,KTfinishtype,KTamount);
        li.add(obj);

        obj=new QuotationPDFCreator.customeclass(tabname,caption3,KLmoduleCount,KLbasecarcass,KLwallcarcass,KLfinishmaterial,KLfinishtype,KLamount);
        li.add(obj);
        int num=customFunction(li);

        li2=new ArrayList<QuotationPDFCreator.customeclass>();
        QuotationPDFCreator.customeclass ob2;

        ob2=new QuotationPDFCreator.customeclass(tabname,caption4,SW1modulecount,SW1basecarcass,SW1wallcarcass,SW1finishmaterial,SW1finishtype,SW1amount);
        li2.add(ob2);

        int num1=customFunction(li2);

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
        Pindex=new Paragraph(this.getRoundOffValue(quantity),fsize);
        Pindex.setAlignment(Element.ALIGN_RIGHT);
        cell2.addElement(Pindex);
        tabname.addCell(cell2);

        PdfPCell cell4=new PdfPCell();
        Pindex=new Paragraph(this.getRoundOffValue(amount),fsize);
        Pindex.setAlignment(Element.ALIGN_RIGHT);
        cell4.addElement(Pindex);
        tabname.addCell(cell4);

        PdfPCell cell3=new PdfPCell();
        double amt=quantity*amount;
        Paragraph Pamt=new Paragraph(this.getRoundOffValue(amt),fsize);
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
            this.createRowAndFillData(tabname,ROMAN_SEQUENCE[acSequence], accessory.title);
            amount=amount+(accessory.quantity*accessory.msp);
            acSequence++;
            if (acSequence == ROMAN_SEQUENCE.length) acSequence = 0;
        }
        this.createCellWithData(tabname,"Accessory Cost",amount);
        this.createCellWithData(tabname,"WoodWork Cost",amt-amount);
    }

    private void createCellWithData(PdfPTable tabname,String str,Object data)
    {
        PdfPCell cell1 = new PdfPCell();
        Paragraph p1=new Paragraph(str,fsize);
        p1.setAlignment(Element.ALIGN_RIGHT);
        cell1.addElement(p1);
        cell1.setColspan(4);
        tabname.addCell(cell1);

        PdfPCell cell = new PdfPCell();
        Paragraph p=new Paragraph(this.getRoundOffValue(Double.parseDouble(data.toString())),fsize);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        tabname.addCell(cell);
    }


    private void createCellWithData(PdfPTable tabname,Object data)
    {
        PdfPCell cell = new PdfPCell();
        Paragraph p=new Paragraph(this.getRoundOffValue(Double.parseDouble(data.toString())),fsize);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        cell.setColspan(5);
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

    }

    private void createSubHeadingRowForCatalog(PdfPTable tabname, String index, String title,Double quantity, Double amount, Double total)
    {
        PdfPCell cell;
        Paragraph p;

        cell=new PdfPCell();
        p=new Paragraph(index,fsize);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        tabname.addCell(cell);

        cell=new PdfPCell();
        p=new Paragraph(title,fsize);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        tabname.addCell(cell);

        cell=new PdfPCell();
        p=new Paragraph(this.getRoundOffValue(quantity),fsize);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        tabname.addCell(cell);

        PdfPCell cell1=new PdfPCell();
        p=new Paragraph(this.getRoundOffValue(amount),fsize);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell1.addElement(p);
        tabname.addCell(cell1);

        PdfPCell cell2=new PdfPCell();
        double amt=quantity*amount;
        LOG.info("amount:" +amt);
        Paragraph Pamt=new Paragraph(this.getRoundOffValue(total),fsize);
        Pamt.setAlignment(Element.ALIGN_RIGHT);
        cell2.addElement(Pamt);
        tabname.addCell(cell2);
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
            this.createRowAndFillData(tabname,String.valueOf(index), addon.getExtendedTitle(), addon.getQuantity(), addon.getRate(), addon.getAmount());
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

    public static String getRoundOffValue(double value)
    {

        DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,###");
        return df.format(value);
    }

    class customeclass
    {
        PdfPTable tabName;
        String title,basecarcass,wallcarcass,finishmaterial,finishtype;
        int modulecount;
        double amount;

        public customeclass(PdfPTable tabName, String title,int modulecount, String basecarcass, String wallcarcass, String finishmaterial, String finishtype, double amount ) {
            this.tabName = tabName;
            this.title = title;
            this.basecarcass = basecarcass;
            this.wallcarcass = wallcarcass;
            this.finishmaterial = finishmaterial;
            this.finishtype = finishtype;
            this.amount = amount;
            this.modulecount = modulecount;
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

        public void setWallcarcass(String wallcarcass) {
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
    }

}
