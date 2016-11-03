package com.mygubbi.game.proposal.quote;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mygubbi.game.proposal.ProductAddon;
import com.mygubbi.game.proposal.ProductLineItem;
import com.mygubbi.game.proposal.model.ProposalHeader;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

/**
 * Created by user on 02-Nov-16.
 */
public class NewQuotationSheetCreator
{
    public static final String DEST = "d:/wede.pdf";

    private static final String[] ALPHABET_SEQUENCE = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j"};
    private static final String[] ROMAN_SEQUENCE = new String[]{"i", "ii", "iii", "iv", "v", "vi", "vii", "viii", "ix", "x", "xi", "xii", "xiii", "xiv", "xv"};

    PdfPTable itemsTable,B1Table;
    QuoteData quoteData;
    private ProposalHeader proposalHeader;

    NewQuotationSheetCreator(QuoteData quoteData,ProposalHeader proposalHeader)
    {
        this.quoteData=quoteData;
        this.proposalHeader=proposalHeader;
        try
        {
            this.createpdf(DEST);
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }
    }

    private void  createpdf(String dest)
    {   try {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
        writer.setPdfVersion(PdfWriter.VERSION_1_7);
        writer.createXmpMetadata();

        document.open();
        Image img = Image.getInstance("mygubbi.png");
        document.add(img);
        Paragraph p;
        //p = new Paragraph(basic.getName() + " " + basic.getId(), font14);
        p = new Paragraph("No 1502, 1st Floor, 19th Main, Sector 1, HSR Layout");
        p.setAlignment(Element.ALIGN_LEFT);
        document.add(p);

        p = new Paragraph("Bangalore 560 102, India");
        p.setAlignment(Element.ALIGN_LEFT);
        document.add(p);

        p = new Paragraph("Phone (080) 49 4999 00");
        p.setAlignment(Element.ALIGN_LEFT);
        document.add(p);

        p = new Paragraph("      ");
        p.setAlignment(Element.ALIGN_LEFT);
        document.add(p);

        p = new Paragraph("QUOTATION");
        p.setAlignment(Element.ALIGN_CENTER);
        document.add(p);

        p = new Paragraph("      ");
        p.setAlignment(Element.ALIGN_LEFT);
        document.add(p);

        //System.out.println("Addrerss printed");

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        PdfPCell cell1;

        /*System.out.print("title" +proposalHeader.getTITLE());
        System.out.println("Date:" + DateFormatUtils.format(new Date(), "dd-MMM-yyyy"));
        System.out.print("title" +proposalHeader.getProjectName());
        System.out.print("title" +proposalHeader.getCrmId());*/

        cell1 = new PdfPCell(new Paragraph("Quotation for:" + "title"));
        PdfPCell cell2 = new PdfPCell(new Paragraph("Date:" + DateFormatUtils.format(new Date(), "dd-MMM-yyyy")));
        table.addCell(cell1);
        table.addCell(cell2);

        //System.out.println("quoatation printed");

        PdfPCell cell3 = new PdfPCell(new Paragraph("Name:" + "Project name"));
        PdfPCell cell4 = new PdfPCell(new Paragraph("CRM ID:" + proposalHeader.getCrmId()));
        table.addCell(cell3);
        table.addCell(cell4);

        PdfPCell cell5 = new PdfPCell(new Paragraph("Contact:"));
        PdfPCell cell6 = new PdfPCell(new Paragraph("Quotation num: quote num"));
        table.addCell(cell5);
        table.addCell(cell6);
        //System.out.print("project Address" +quoteData.concatValuesFromKeys(new String[]{ProposalHeader.PROJECT_NAME, ProposalHeader.PROJECT_ADDRESS1, ProposalHeader.PROJECT_ADDRESS2, ProposalHeader.PROJECT_CITY}, ","));
        PdfPCell cell7 = new PdfPCell(new Paragraph("Project Adress:" + quoteData.concatValuesFromKeys(new String[]{ProposalHeader.PROJECT_NAME, ProposalHeader.PROJECT_ADDRESS1, ProposalHeader.PROJECT_ADDRESS2, ProposalHeader.PROJECT_CITY}, ",")));
        PdfPCell cell8 = new PdfPCell(new Paragraph("Customer ID:"));
        table.addCell(cell7);
        table.addCell(cell8);
        document.add(table);

        p = new Paragraph("      ");
        p.setAlignment(Element.ALIGN_LEFT);
        document.add(p);

        //System.out.print("sales value printed");
        PdfPTable stable = new PdfPTable(3);
        stable.setWidthPercentage(100);
        PdfPCell scell1 = new PdfPCell(new Paragraph("Sales/Design"));
        PdfPCell scell2 = new PdfPCell(new Paragraph("Sales Person Number"));
        PdfPCell scell3 = new PdfPCell(new Paragraph("Email Id"));
        stable.addCell(scell1);
        stable.addCell(scell2);
        stable.addCell(scell3);

        /*System.out.println("Sales name" +quoteData.concatValuesFromKeys(new String[]{ProposalHeader.SALESPERSON_NAME, ProposalHeader.DESIGNER_NAME}, "/"));
        System.out.println("Sales phone" +proposalHeader.getSalesphone());
        System.out.println("email" +proposalHeader.getEmail());*/

        PdfPCell scell4 = new PdfPCell(new Paragraph(quoteData.concatValuesFromKeys(new String[]{ProposalHeader.SALESPERSON_NAME, ProposalHeader.DESIGNER_NAME}, "/")));
        PdfPCell scell5 = new PdfPCell(new Paragraph("SalesNumber"));
        PdfPCell scell6 = new PdfPCell(new Paragraph("SalesEmail"));
        stable.addCell(scell4);
        stable.addCell(scell5);
        stable.addCell(scell6);
        document.add(stable);

        p = new Paragraph("      ");
        p.setAlignment(Element.ALIGN_LEFT);
        document.add(p);

        //System.out.print("ssss value inserted");
        itemsTable = new PdfPTable(5);
        itemsTable.setWidthPercentage(100);
        PdfPCell itemsCell1 = new PdfPCell(new Paragraph("SL. No"));
        PdfPCell itemsCell2 = new PdfPCell(new Paragraph("Description"));
        PdfPCell itemsCell3 = new PdfPCell(new Paragraph("Quantity"));
        PdfPCell itemsCell4 = new PdfPCell(new Paragraph("Amount"));
        PdfPCell itemsCell5 = new PdfPCell(new Paragraph("Total"));
        itemsTable.addCell(itemsCell1);
        itemsTable.addCell(itemsCell2);
        itemsTable.addCell(itemsCell3);
        itemsTable.addCell(itemsCell4);
        itemsTable.addCell(itemsCell5);

        this.fillAssembledProducts(itemsTable);
        this.fillCatalogProducts(itemsTable);
        document.add(itemsTable);

        p = new Paragraph("Estimated Cost: 12345");
        p.setAlignment(Element.ALIGN_RIGHT);
        document.add(p);

        p = new Paragraph("      ");
        p.setAlignment(Element.ALIGN_LEFT);
        document.add(p);

        PdfPTable BTable = new PdfPTable(5);
        BTable.setWidthPercentage(100);
        PdfPCell BCell1 = new PdfPCell(new Paragraph("APPLIANCES,SERVICES,OTHERS"));
        PdfPCell BCell2 = new PdfPCell(new Paragraph(""));
        PdfPCell BCell3 = new PdfPCell(new Paragraph(""));
        PdfPCell BCell4 = new PdfPCell(new Paragraph(""));
        PdfPCell BCell5 = new PdfPCell(new Paragraph(""));
        BTable.addCell(BCell1);
        BTable.addCell(BCell2);
        BTable.addCell(BCell3);
        BTable.addCell(BCell4);
        BTable.addCell(BCell5);
        document.add(BTable);

        B1Table = new PdfPTable(5);
        B1Table.setWidthPercentage(100);
        PdfPCell B1Cell1 = new PdfPCell(new Paragraph("ADD ON ACCESSORIES"));
        PdfPCell B1Cell2 = new PdfPCell(new Paragraph(""));
        PdfPCell B1Cell3 = new PdfPCell(new Paragraph(""));
        PdfPCell B1Cell4 = new PdfPCell(new Paragraph(""));
        PdfPCell B1Cell5 = new PdfPCell(new Paragraph(""));
        B1Table.addCell(B1Cell1);
        B1Table.addCell(B1Cell2);
        B1Table.addCell(B1Cell3);
        B1Table.addCell(B1Cell4);
        B1Table.addCell(B1Cell5);

        //System.out.println("cal  acc func");
        this.fillAddons(B1Table, this.quoteData.getAccessories(), "No additional accessories.");
        //System.out.println("finish  acc func");
        document.add(B1Table);

        PdfPTable B2Table = new PdfPTable(5);
        B2Table.setWidthPercentage(100);
        PdfPCell B2Cell1 = new PdfPCell(new Paragraph("APPLIANCES"));
        PdfPCell B2Cell2 = new PdfPCell(new Paragraph(""));
        PdfPCell B2Cell3 = new PdfPCell(new Paragraph(""));
        PdfPCell B2Cell4 = new PdfPCell(new Paragraph(""));
        PdfPCell B2Cell5 = new PdfPCell(new Paragraph(""));
        B2Table.addCell(B2Cell1);
        B2Table.addCell(B2Cell2);
        B2Table.addCell(B2Cell3);
        B2Table.addCell(B2Cell4);
        B2Table.addCell(B2Cell5);

        //System.out.print("call addons");
        this.fillAddons(B2Table, this.quoteData.getAppliances(), "No additional appliances.");
        document.add(B2Table);

        PdfPTable B3Table = new PdfPTable(5);
        B3Table.setWidthPercentage(100);
        PdfPCell B3Cell1 = new PdfPCell(new Paragraph("COUNTER TOP"));
        PdfPCell B3Cell2 = new PdfPCell(new Paragraph(""));
        PdfPCell B3Cell3 = new PdfPCell(new Paragraph(""));
        PdfPCell B3Cell4 = new PdfPCell(new Paragraph(""));
        PdfPCell B3Cell5 = new PdfPCell(new Paragraph(""));
        B3Table.addCell(B3Cell1);
        B3Table.addCell(B3Cell2);
        B3Table.addCell(B3Cell3);
        B3Table.addCell(B3Cell4);
        B3Table.addCell(B3Cell5);

        this.fillAddons(B3Table, this.quoteData.getCounterTops(), "No countertops.");
        document.add(B3Table);

        PdfPTable B4Table = new PdfPTable(5);
        B4Table.setWidthPercentage(100);
        PdfPCell B4Cell1 = new PdfPCell(new Paragraph("SERVICES"));
        PdfPCell B4Cell2 = new PdfPCell(new Paragraph(""));
        PdfPCell B4Cell3 = new PdfPCell(new Paragraph(""));
        PdfPCell B4Cell4 = new PdfPCell(new Paragraph(""));
        PdfPCell B4Cell5 = new PdfPCell(new Paragraph(""));
        B4Table.addCell(B4Cell1);
        B4Table.addCell(B4Cell2);
        B4Table.addCell(B4Cell3);
        B4Table.addCell(B4Cell4);
        B4Table.addCell(B4Cell5);

        this.fillAddons(B4Table, this.quoteData.getServices(), "No additional services.");
        document.add(B4Table);

        p = new Paragraph("      ");
        p.setAlignment(Element.ALIGN_LEFT);
        document.add(p);

        p = new Paragraph("Estimated Cost(B): 12345");
        p.setAlignment(Element.ALIGN_RIGHT);
        document.add(p);

        p = new Paragraph("      ");
        p.setAlignment(Element.ALIGN_LEFT);
        document.add(p);

        p = new Paragraph("Estimated Cost(A+B): 12345");
        p.setAlignment(Element.ALIGN_RIGHT);
        document.add(p);

        p = new Paragraph("      ");
        p.setAlignment(Element.ALIGN_LEFT);
        document.add(p);

        PdfPTable table2=new PdfPTable(1);
        table2.setWidthPercentage(100);
        table2.addCell("IN WORDS: Four Lac Twenty Seven Thousand Five Hundred and Ninty Three Rupees  Only");
        document.add(table2);

        p = new Paragraph("      ");
        p.setAlignment(Element.ALIGN_LEFT);
        document.add(p);

        p = new Paragraph("Material Specification:");
        p.setAlignment(Element.ALIGN_LEFT);
        document.add(p);

        PdfPTable tab1=new PdfPTable(2);
        tab1.setWidthPercentage(100);
        tab1.addCell("Ply:");
        tab1.addCell("IS 303- BWR grade for kitchen, MR Grade for wardrobe and other units\t\t\n" + "IS710- BWP for kitchen sink unit\n");
        tab1.addCell("MDF:\n");
        tab1.addCell("Exterior Grade MDF\n");
        tab1.addCell("Edge Banding\n");
        tab1.addCell("Rehau\n");
        tab1.addCell("Laminates\n");
        tab1.addCell("Glossy /Matt/Textured/Metalic Laminates by Merino/Greenlam\t\t\n");
        tab1.addCell("Hardwares\n");
        tab1.addCell("Hettich/Ebco/Rehau\n");
        tab1.addCell("Accessories\n");
        tab1.addCell("Hettich/Haffle/Evershine/Ebco/Grass\n");
        tab1.addCell("Glass/Mirror\n");
        tab1.addCell("Asahi/ Saint Gobain\n");
        tab1.addCell("Lacquered Glass\n");
        tab1.addCell("Saint Gobain\n");
        tab1.addCell("Appliances\n");
        tab1.addCell("Faber /Elica/Kaff/Nagold/ Bosch\n");
        tab1.addCell("Sink\n");
        tab1.addCell("Carisyl/Franke/Nirali\n");
        document.add(tab1);

        p = new Paragraph("Other Finishes offered are Acrylic, Foil, PU paint, UV laminated panels,Hardwood of mygubbi make.\t\t\t\t\n");
        p.setAlignment(Element.ALIGN_LEFT);
        document.add(p);

        p = new Paragraph("Note:\n");
        p.setAlignment(Element.ALIGN_LEFT);
        document.add(p);

        PdfPTable tab2=new PdfPTable(2);
        tab2.setWidthPercentage(100);
        tab2.addCell("1");
        tab2.addCell("All 25 mm shelves will be in MDF with both side finish\t\t\n");
        tab2.addCell("2");
        tab2.addCell("Plumbing, counter top , gas piping ,appliances, hob ,chimney ,sink, taps, electrical shifting, tile laying,Core cutting and civil changes are not considered kitchen quote. These items will be quoted seperately if needed.");
        tab2.addCell("3");
        tab2.addCell("Final paint quote to be completed after furniture installation by Customer It will be quoted separately if it is in mygubbi scope.");
        document.add(tab2);

        p = new Paragraph("      ");
        p.setAlignment(Element.ALIGN_LEFT);
        document.add(p);

        PdfPTable tab=new PdfPTable(1);
        tab.setWidthPercentage(100);
        tab.addCell("Note :- The above quotation does not include the cost for Tap, Appliances & Plumbing.");
        tab.addCell("Terms and Conditions:");
        tab.addCell("1. The quote rate is inclusive of Applicable taxes. (VAT @ 14.5% & S T @14.5%), Taxes are subject to changes, due to Govt. policies & changes in the rate if any, - the same will be chared at the time of submission of bill.");
        tab.addCell("2. The images depicted in the drwaings and presentations are representative only. The final design - drawings provided and approved by the client will be conceived and executed at the site.");
        tab.addCell("3. The above quoted rate is as per a Typical Apartment and there will be variation in rates as per the final site conditions / Measurements / Specifications / Design.");
        tab.addCell("4. Quote is valid for 15 Days only");
        document.add(tab);

        PdfPTable table3=new PdfPTable(2);
        table3.setWidthPercentage(100);
        table3.addCell("5. Payment terms:\n" +
                "\n" +
                "\n");
        table3.addCell("10% for finalization of the Design\n"+ "40% advance for order confirmation\n" + "50% before the Delivery of materials \n");
        document.add(table3);

        PdfPTable table4=new PdfPTable(1);
        table4.setWidthPercentage(100);
        table4.addCell("6. Design Signup fees amounting to Rs.15000/- or 10% of the Budgetary quote is non-refundable. This amount will be adjusted against the final order value. Booking confirmation shall be acknowledged in the copy of this budgetary proposal.");
        table4.addCell("7. The 50% advance paid post approval of the design and quote cannot be refunded as the production would have be commenced.");
        table4.addCell("8. Warranty : 5 years of warranty against any manufacturing defect. The material specifications and brands specified are as per the approved standards of Orangegubbi Technologies Private Limited and covered under warranty.");
        table4.addCell("9. Any modifications/alterations to the proposed design will have an impact on the techno commercials of this quote and hence new drawings as well as associated commercials will be provided for by MyGubbi if the same occurs.");
        table4.addCell("10. Delivery shall be within 45 days from order Final Confirmation.");
        table4.addCell("11. Cheque / Demant Draft should be in favour of \"ORANGEGUBBI TECHNOLOGIES PRIVATE LIMITED\" ");
        document.add(table4);

        p = new Paragraph("      ");
        p.setAlignment(Element.ALIGN_LEFT);
        document.add(p);

        p = new Paragraph("THANKS for considering OrangeGubbi !\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t4" + " Accepted (Sign) ");
        p.setAlignment(Element.ALIGN_LEFT);
        document.add(p);

        document.close();
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
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
       // System.out.println(product.getTitle());
        this.createProductTitleRow(tabname,"A." + String.valueOf(sequenceNumber), product.getTitle());
        this.fillAssembledProductUnits(tabname,product);

        String unitSequenceLetter = ALPHABET_SEQUENCE[(product.getUnits().size())];
        this.fillAssembledProductAccessories(tabname,product.getAccessories(), unitSequenceLetter);

        this.createCellWithData(tabname,product.getAmountWithoutAddons());
    }

    private void createProductTitleRow(PdfPTable tabname,String index, String title)
    {
        tabname.addCell(index);
        tabname.addCell(title);
        tabname.addCell("");
        tabname.addCell("");
        tabname.addCell("");
    }

    private void fillAssembledProductUnits(PdfPTable tabname,AssembledProductInQuote product)
    {
        int unitSequence = 0;
        for (AssembledProductInQuote.Unit unit : product.getUnits())
        {
            /*System.out.println(unit.title);
            System.out.println(unit.getDimensions());
            System.out.println(unit.moduleCount);
            System.out.print(unit.amount);*/

            this.createSubHeadingRow(tabname,"A."+ ALPHABET_SEQUENCE[unitSequence], unit.title + " - " + unit.getDimensions());
            this.createRowAndFillData(tabname,null, "Unit consists of " + unit.moduleCount + " modules as per design provided.",1.0,unit.amount,0.0);
            unitSequence++;
            if (unitSequence == ALPHABET_SEQUENCE.length) unitSequence = 0;
        }

    }

    private void createSubHeadingRow(PdfPTable tabname,String index, String title)
    {
        tabname.addCell(index);
        tabname.addCell(title);
        tabname.addCell("");
        tabname.addCell("");
        tabname.addCell("");
    }

    private void createRowAndFillData(PdfPTable tabname,String index, String title, Double quantity, Double amount, Double total)
    {
        //System.out.println("index" +index+ "title" +title+ "Quantity" +quantity+ "amount" +amount+ "total" +total);
        tabname.addCell(index);
        tabname.addCell(title);
        tabname.addCell(Double.toString(quantity));
        tabname.addCell(Double.toString(amount));
        tabname.addCell(Double.toString(total));
    }

    private void createRowAndFillData(PdfPTable tabname,String index, String title)
    {
        this.createRowAndFillData(tabname,index, title, null, null, null);
    }
    private void fillAssembledProductAccessories(PdfPTable tabname,List<AssembledProductInQuote.Accessory> accessories, String unitSequenceLetter)
    {
        if (accessories == null || accessories.isEmpty())
        {
            //return currentRow;
        }

        this.createSubHeadingRow(tabname,unitSequenceLetter, "Accessories");

        int acSequence = 0;
        for (AssembledProductInQuote.Accessory accessory : accessories)
        {
            /*System.out.println(accessory.title);
            System.out.println(accessory.quantity);*/

            this.createRowAndFillData(tabname,ROMAN_SEQUENCE[acSequence], accessory.title, accessory.quantity, null, null);
            acSequence++;
            if (acSequence == ROMAN_SEQUENCE.length) acSequence = 0;
        }

    }

    private void createCellWithData(PdfPTable tabname,Object data)
    {
        tabname.addCell(data.toString());
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
        //int currentRow = startRow;
        /*System.out.println(product.getTitle());
        System.out.println(product.getQuantity());
        System.out.println(product.getRate());
        System.out.println(product.getAmount());
        System.out.println(product.getName());*/
        this.createSubHeadingRowForCatalog(tabname,"A." +String.valueOf(sequenceNumber), product.getTitle(), Double.valueOf(product.getQuantity()),
                product.getRate(), (double) Math.round(product.getAmount()));

        this.createRowAndFillData(tabname,null, product.getName());
    }

    private void createSubHeadingRowForCatalog(PdfPTable tabname, String index, String title,Double quantity, Double amount, Double total)
    {
        tabname.addCell(index);
        tabname.addCell(title);
        tabname.addCell(Double.toString(quantity));
        tabname.addCell(Double.toString(amount));
        tabname.addCell(Double.toString(total));
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
            //currentRow++;
            index++;
        }
    }
    private void createRowWithMessage(PdfPTable tabname,String message)
    {
        /*System.out.println(message);
        this.createRowAndFillData(tabname,null, message, null, null, null);*/
        tabname.addCell(message);
    }
}
