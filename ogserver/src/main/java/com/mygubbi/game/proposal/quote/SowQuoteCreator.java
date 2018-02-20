package com.mygubbi.game.proposal.quote;

import com.mygubbi.common.StringUtils;
import com.mygubbi.game.proposal.model.SOWPdf;
import com.mygubbi.si.excel.ExcelStyles;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shruthi on 2/20/2018.
 */
public class SowQuoteCreator
{
    private final static Logger LOG = LogManager.getLogger(SowQuoteCreator.class);
    private QuoteData quoteData;
    private Sheet dataSheet;
    private ExcelStyles styles;
    private List<SOWPdf> sowList=new ArrayList<>();
    public SowQuoteCreator(Sheet sheet,QuoteData quoteData, ExcelStyles styles,List<SOWPdf> sowList)
    {
        this.dataSheet = sheet;
        this.quoteData = quoteData;
        this.styles = styles;
        this.sowList=sowList;
    }
    public void prepare()
    {
        this.processDataSheet();
    }

    private void processDataSheet()
    {
        int startRow = 1;
        int sequenceNumber = 1;
        String currentroom=null;
        String currentspaceType=null;
        String prevroom=null;
        String prevspaceType=null;
        for (SOWPdf sowPdf:sowList)
        {
            currentroom=sowPdf.getROOM();
            currentspaceType=sowPdf.getSpaceType();
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
                this.createDataRowInDataSheet(startRow,new String []{" "," ",sowPdf.getSERVICE(),sowPdf.getServiceValue(),sowPdf.getRelatedService1(),sowPdf.getRelatedServicevalue1(),sowPdf.getRelatedService2(),sowPdf.getRelatedServicevalue2(),sowPdf.getRelatedService3(),sowPdf.getRelatedServicevalue3(),sowPdf.getRelatedService4(),sowPdf.getRelatedServicevalue4(),sowPdf.getRelatedService5(),sowPdf.getRelatedServicevalue5(),sowPdf.getRelatedService6(),sowPdf.getRelatedServicevalue6()});
                //createRowAndFillData(sowitemsTable, "", "", proposalSOW.getSERVICE(), proposalSOW.getServiceValue(), proposalSOW.getRelatedService1(), proposalSOW.getRelatedServicevalue1(), proposalSOW.getRelatedService2(), proposalSOW.getRelatedServicevalue2(), proposalSOW.getRelatedService3(), proposalSOW.getRelatedServicevalue3(), proposalSOW.getRelatedService4(), proposalSOW.getRelatedServicevalue4(), proposalSOW.getRelatedService5(), proposalSOW.getRelatedServicevalue5(), proposalSOW.getRelatedService6(), proposalSOW.getRelatedServicevalue6());
            }
            else {
                this.createDataRowInDataSheet(startRow,new String []{sowPdf.getSpaceType(),sowPdf.getROOM(),sowPdf.getSERVICE(),sowPdf.getServiceValue(),sowPdf.getRelatedService1(),sowPdf.getRelatedServicevalue1(),sowPdf.getRelatedService2(),sowPdf.getRelatedServicevalue2(),sowPdf.getRelatedService3(),sowPdf.getRelatedServicevalue3(),sowPdf.getRelatedService4(),sowPdf.getRelatedServicevalue4(),sowPdf.getRelatedService5(),sowPdf.getRelatedServicevalue5(),sowPdf.getRelatedService6(),sowPdf.getRelatedServicevalue6()});
                //createRowAndFillData(sowitemsTable, proposalSOW.getSpaceType(), proposalSOW.getROOM(), proposalSOW.getSERVICE(), proposalSOW.getServiceValue(), proposalSOW.getRelatedService1(), proposalSOW.getRelatedServicevalue1(), proposalSOW.getRelatedService2(), proposalSOW.getRelatedServicevalue2(), proposalSOW.getRelatedService3(), proposalSOW.getRelatedServicevalue3(), proposalSOW.getRelatedService4(), proposalSOW.getRelatedServicevalue4(), proposalSOW.getRelatedService5(), proposalSOW.getRelatedServicevalue5(), proposalSOW.getRelatedService6(), proposalSOW.getRelatedServicevalue6());
            }
            prevroom=currentroom;
            prevspaceType=currentspaceType;
            //this.createDataRowInDataSheet(startRow,new String []{sowPdf.getSpaceType(),sowPdf.getROOM(),sowPdf.getSERVICE(),sowPdf.getServiceValue(),sowPdf.getRelatedService1(),sowPdf.getRelatedServicevalue1(),sowPdf.getRelatedService2(),sowPdf.getRelatedServicevalue2(),sowPdf.getRelatedService3(),sowPdf.getRelatedServicevalue3(),sowPdf.getRelatedService4(),sowPdf.getRelatedServicevalue4(),sowPdf.getRelatedService5(),sowPdf.getRelatedServicevalue5(),sowPdf.getRelatedService6(),sowPdf.getRelatedServicevalue6()});
            startRow++;
        }
    }
    private Row createRow(int currentRow, Sheet sheet)
    {
        sheet.shiftRows(currentRow, sheet.getLastRowNum(), 1);
        return sheet.createRow(currentRow);
    }
    private void createDataRowInDataSheet(int rowNum, String [] data)
    {
        this.createRowInDataSheet(rowNum, data, false);
    }

    private void createRowInDataSheet(int rowNum, String [] data, boolean isTitle) {
        Row dataRow = this.dataSheet.createRow(rowNum);
        int lastCell = data.length;
        for (int cellNum = 0; cellNum < lastCell; cellNum++) {
            String value = data[cellNum];
            if (StringUtils.isNonEmpty(value)) {
                Cell cell = dataRow.createCell(cellNum, Cell.CELL_TYPE_STRING);
                cell.setCellValue(value);
                if (isTitle) {
                    cell.setCellStyle(this.styles.getBoldStyle());
                }
            }
        }
    }

}
