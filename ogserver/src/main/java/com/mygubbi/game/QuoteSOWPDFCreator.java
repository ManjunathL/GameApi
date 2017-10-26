package com.mygubbi.game;

import com.hazelcast.core.Message;
import com.itextpdf.text.*;
//import com.itextpdf.text.List;
import com.itextpdf.text.pdf.*;
import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.config.ConfigHolder;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import com.mygubbi.game.proposal.ProductLineItem;
import com.mygubbi.game.proposal.model.ProposalHeader;
import com.mygubbi.game.proposal.model.ProposalSOW;
import com.mygubbi.game.proposal.model.SOWPdf;
import com.mygubbi.game.proposal.quote.QuoteData;
import io.vertx.core.AsyncResult;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.util.*;
import java.util.List;

/**
 * Created by Shruthi on 7/28/2017.
 */

class CustomBorder extends PdfPageEventHelper {
    public void onEndPage(PdfWriter writer, Document document) {

        PdfContentByte canvas = writer.getDirectContent();
        Rectangle rect = new Rectangle(28, 28, 820, 580);
        rect.setBorder(Rectangle.BOX);
        rect.setBorderWidth(2);
        canvas.rectangle(rect);
    }
}
public class QuoteSOWPDFCreator
{
    private final static Logger LOG = LogManager.getLogger(QuoteSOWPDFCreator.class);
    private static final String[] ALPHABET_SEQUENCE = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s"};
    private static final String[] ROMAN_SEQUENCE = new String[]{"i", "ii", "iii", "iv", "v", "vi", "vii", "viii", "ix", "x", "xi", "xii", "xiii", "xiv", "xv"};

    PdfPTable sowitemsTable,B1Table;
    private String sowversion;
    private String remarks;

    Font fsize=new Font(Font.FontFamily.TIMES_ROMAN,8,Font.NORMAL);
    Font fsize1=new Font(Font.FontFamily.TIMES_ROMAN,8,Font.BOLD);
    Font fsize3=new Font(Font.FontFamily.TIMES_ROMAN,9,Font.BOLD);

    private ProposalHeader proposalHeader;
    private QuoteData quoteData;
    List<SOWPdf> proposalSOWs = new ArrayList<SOWPdf>();
    Document document = new Document(PageSize.A4.rotate());
    FileOutputStream fout ;
    public QuoteSOWPDFCreator(ProposalHeader proposalHeader, QuoteData quoteData,List<SOWPdf> proposalSOWs)
    {
        LOG.info("proposal Sow " +proposalSOWs.size());
        this.proposalHeader=proposalHeader;
        this.quoteData=quoteData;
        this.proposalSOWs = proposalSOWs;
    }
    public void createSOWPDf(String destination)
    {
        sowversion = "1.0";
        String version = quoteData.fromVersion;

        if (version.contains("1.") || version.contains("2.")){
            sowversion = "2.0";
        }

        if (sowversion.equals("1.0")){
            remarks = proposalHeader.getSowRemarksV1();
        }
        else{
            remarks = proposalHeader.getSowRemarksV2();

        }

        if (remarks == null) remarks = "";

        try
            {
                readAndFillSowTable(proposalSOWs, destination);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                LOG.info("Exception " + e);
            }
            finally {
                LOG.info("In finally");
//                document.close();;
            }
    }


    private void readAndFillSowTable(List<SOWPdf> proposalSOWs,String destination ) throws Exception{

        this.fout = new FileOutputStream(destination);
        PdfWriter writer = PdfWriter.getInstance(document, fout);
        writer.setPdfVersion(PdfWriter.VERSION_1_7);
        writer.createXmpMetadata();
        document.open();

        writer.setPageEvent(new CustomBorder());
        float[] columnWidths2 = {4, 4};
        PdfPTable table = new PdfPTable(columnWidths2);
        table.setWidthPercentage(100);

        Paragraph p = new Paragraph("Appendix - B Scope Of Services",fsize3);
        p.setAlignment(Element.ALIGN_CENTER);
        fsize3.setColor(BaseColor.GRAY);
        document.add(p);

        Paragraph pp=new Paragraph(" ");
        document.add(pp);

        Phrase phrase = new Phrase();
        phrase.add(new Chunk("CRM ID: ", fsize1));
        phrase.add(new Chunk(proposalHeader.getCrmId(), fsize));

        Phrase phrase2 = new Phrase();
        phrase2.add(new Chunk("Client Name: ", fsize1));
        phrase2.add(new Chunk(proposalHeader.getName(), fsize));

        Phrase phrase1 = new Phrase();
        phrase1.add(new Chunk("Quotation #", fsize1));
        phrase1.add(new Chunk(proposalHeader.getQuoteNumNew(), fsize));

        Phrase phrase3 = new Phrase();
        phrase3.add(new Chunk("Version: ", fsize1));
        phrase3.add(new Chunk(sowversion, fsize));

        Phrase phrase4 = new Phrase();
        phrase4.add(new Chunk("Date: ", fsize1));
        phrase4.add(new Chunk(DateFormatUtils.format(new Date(), "dd-MMM-yyyy"),fsize));

        Phrase phrase5 = new Phrase();
        phrase5.add(new Chunk("Remarks ", fsize1));
        phrase5.add(new Chunk(remarks, fsize));

        table.addCell(phrase);
        table.addCell(phrase1);
        table.addCell(phrase2);
        table.addCell(phrase3);
        table.addCell(phrase4);
        table.addCell(phrase5);

                /*PdfPTable pdfPTable=new PdfPTable(1);
                pdfPTable.setWidthPercentage(100);
                Phrase phrase6 = new Phrase();
                phrase6.add(new Chunk("Project Address: ",fsize1));
                phrase6.add(new Chunk(quoteData.concatValuesFromKeys(new String[]{ProposalHeader.PROJECT_NAME, ProposalHeader.PROJECT_ADDRESS1, ProposalHeader.PROJECT_ADDRESS2, ProposalHeader.PROJECT_CITY}, ","),fsize));
                pdfPTable.addCell(phrase6);*/

        document.add(table);

        Paragraph p1=new Paragraph(" ");
        document.add(p1);

        float[] columnWidths1 = {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
        sowitemsTable = new PdfPTable(columnWidths1);
        sowitemsTable.setWidthPercentage(100);
        PdfPCell itemsCell1 = new PdfPCell(new Paragraph("SPACE TYPE",fsize1));
        itemsCell1.setBackgroundColor(BaseColor.ORANGE);
        PdfPCell itemsCell2 = new PdfPCell(new Paragraph("ROOM",fsize1));
        itemsCell2.setBackgroundColor(BaseColor.ORANGE);
        PdfPCell itemsCell3 = new PdfPCell(new Paragraph("SERVICES",fsize1));
        Paragraph Pindex=new Paragraph("SERVICES",fsize1);
        Pindex.setAlignment(Element.ALIGN_CENTER);
        itemsCell3.addElement(Pindex);
        itemsCell3.setBackgroundColor(BaseColor.ORANGE);
        itemsCell3.setColspan(2);

        PdfPCell itemsCell4 = new PdfPCell();
        Paragraph Pindex1=new Paragraph("RELATED SERVICES",fsize1);
        Pindex1.setAlignment(Element.ALIGN_CENTER);
        itemsCell4.addElement(Pindex1);
        itemsCell4.setBackgroundColor(BaseColor.ORANGE);
        itemsCell4.setColspan(12);


        sowitemsTable.addCell(itemsCell1);
        sowitemsTable.addCell(itemsCell2);
        sowitemsTable.addCell(itemsCell3);
        sowitemsTable.addCell(itemsCell4);

        sowitemsTable.setHeaderRows(1);
        String currentroom=null;
        String currentspaceType=null;
        String prevroom=null;
        String prevspaceType=null;
        for(SOWPdf proposalSOW:proposalSOWs)
        {
            currentroom=proposalSOW.getROOM();
            currentspaceType=proposalSOW.getSpaceType();
            if (currentroom==null)
            {
                currentroom = "NA";
                LOG.info("Room code is null");
            }
            if (currentspaceType==null)
            {
                currentspaceType = "NA";
                LOG.info("Space Type is null");
            }


           if(currentroom.equals(prevroom) && currentspaceType.equals(prevspaceType))
            {
                createRowAndFillData(sowitemsTable, "", "", proposalSOW.getSERVICE(), proposalSOW.getServiceValue(), proposalSOW.getRelatedService1(), proposalSOW.getRelatedServicevalue1(), proposalSOW.getRelatedService2(), proposalSOW.getRelatedServicevalue2(), proposalSOW.getRelatedService3(), proposalSOW.getRelatedServicevalue3(), proposalSOW.getRelatedService4(), proposalSOW.getRelatedServicevalue4(), proposalSOW.getRelatedService5(), proposalSOW.getRelatedServicevalue5(), proposalSOW.getRelatedService6(), proposalSOW.getRelatedServicevalue6());
            }
            else {
                createRowAndFillData(sowitemsTable, proposalSOW.getSpaceType(), proposalSOW.getROOM(), proposalSOW.getSERVICE(), proposalSOW.getServiceValue(), proposalSOW.getRelatedService1(), proposalSOW.getRelatedServicevalue1(), proposalSOW.getRelatedService2(), proposalSOW.getRelatedServicevalue2(), proposalSOW.getRelatedService3(), proposalSOW.getRelatedServicevalue3(), proposalSOW.getRelatedService4(), proposalSOW.getRelatedServicevalue4(), proposalSOW.getRelatedService5(), proposalSOW.getRelatedServicevalue5(), proposalSOW.getRelatedService6(), proposalSOW.getRelatedServicevalue6());
            }
            prevroom=currentroom;
            prevspaceType=currentspaceType;
        }
        try
        {
//            document.open();
            document.add(sowitemsTable);

            Paragraph para = new Paragraph("      ");
            para.setAlignment(Element.ALIGN_LEFT);
            document.add(para);

            para = new Paragraph("      ");
            para.setAlignment(Element.ALIGN_LEFT);
            document.add(para);

            para = new Paragraph(new Paragraph("THANKS for considering Gubbi!                                                                                                                                                                                                                                                                                                   " + "\t"  + "\t" + "\t" + "\t" + "\t" +"\tAccepted (Sign) ",fsize));
            document.add(para);


            this.fout.flush();
            document.close();
            this.fout.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOG.info("exception while inserting data ");
        }

    }
    private void createRowAndFillData(PdfPTable tabname,String spaceType, String room, String service, String serviceValue, String  relatedService1,String  relatedService1value,String  relatedService2,String  relatedService2value,String  relatedService3,String  relatedService3value,String  relatedService4,String  relatedService4value,String  relatedService5,String  relatedService5value,String  relatedService6,String  relatedService6value)
    {
        //LOG.info("inside create row n fill data");
        PdfPCell cell;
        Paragraph Pindex;
        Font size1=new Font(Font.FontFamily.TIMES_ROMAN,8,Font.BOLD);

        PdfPCell cell1=new PdfPCell();
        Pindex=new Paragraph(spaceType,size1);
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell1.addElement(Pindex);
        tabname.addCell(cell1);
        cell1.setRowspan(9);

        cell=new PdfPCell();
        Pindex=new Paragraph(room,size1);
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(Pindex);
        tabname.addCell(cell);

        PdfPCell cell2=new PdfPCell();
        Pindex=new Paragraph(service,fsize);
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell2.addElement(Pindex);
        tabname.addCell(cell2);

        PdfPCell cell4=new PdfPCell();
        Pindex=new Paragraph(serviceValue,fsize);
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell4.addElement(Pindex);
        tabname.addCell(cell4);

        PdfPCell cell5=new PdfPCell();
        Pindex=new Paragraph(relatedService1,fsize);
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell5.addElement(Pindex);
        tabname.addCell(cell5);

        PdfPCell cell6=new PdfPCell();
        Pindex=new Paragraph(relatedService1value,fsize);
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell6.addElement(Pindex);
        tabname.addCell(cell6);

        PdfPCell cell7=new PdfPCell();
        Pindex=new Paragraph(relatedService2,fsize);
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell7.addElement(Pindex);
        tabname.addCell(cell7);

        PdfPCell cell8=new PdfPCell();
        Pindex=new Paragraph(relatedService2value,fsize);
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell8.addElement(Pindex);
        tabname.addCell(cell8);

        PdfPCell cell9=new PdfPCell();
        Pindex=new Paragraph(relatedService3,fsize);
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell9.addElement(Pindex);
        tabname.addCell(cell9);

        PdfPCell cell10=new PdfPCell();
        Pindex=new Paragraph(relatedService3value,fsize);
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell10.addElement(Pindex);
        tabname.addCell(cell10);

        PdfPCell cell11=new PdfPCell();
        Pindex=new Paragraph(relatedService4,fsize);
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell11.addElement(Pindex);
        tabname.addCell(cell11);

        PdfPCell cell12=new PdfPCell();
        Pindex=new Paragraph(relatedService4value,fsize);
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell12.addElement(Pindex);
        tabname.addCell(cell12);

        PdfPCell cell13=new PdfPCell();
        Pindex=new Paragraph(relatedService5,fsize);
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell13.addElement(Pindex);
        tabname.addCell(cell13);

        PdfPCell cell14=new PdfPCell();
        Pindex=new Paragraph(relatedService5value,fsize);
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell14.addElement(Pindex);
        tabname.addCell(cell14);

        PdfPCell cell15=new PdfPCell();
        Pindex=new Paragraph(relatedService6,fsize);
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell15.addElement(Pindex);
        tabname.addCell(cell15);

        PdfPCell cell16=new PdfPCell();
        Pindex=new Paragraph(relatedService6value,fsize);
        Pindex.setAlignment(Element.ALIGN_LEFT);
        cell16.addElement(Pindex);
        tabname.addCell(cell16);

    }
}


