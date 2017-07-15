package com.mygubbi.game.proposal.sow;

import com.mygubbi.game.proposal.ModuleDataService;
import com.mygubbi.game.proposal.model.ProposalHeader;
import com.mygubbi.game.proposal.model.ProposalSOW;
import com.mygubbi.game.proposal.model.SOWMaster;
import com.mygubbi.si.excel.ExcelCellProcessor;
import com.mygubbi.si.excel.ExcelSheetProcessor;
import com.mygubbi.si.excel.ExcelStyles;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.*;

/**
 * Created by user on 27-May-17.
 */
public class SowSheetCreator implements ExcelCellProcessor
{
    private final static Logger LOG = LogManager.getLogger(SowSheetCreator.class);

    private static final int SPACE_TYPE_CELL = 0;
    private static final int ROOM_CELL = 1;
    private static final int L1S01_TITLE_CELL = 2;
    private static final int L1S01_OPTION_CELL = 3;
    private static final int L2S01_TITLE_CELL = 4;
    private static final int L2S01_OPTION_CELL = 5;
    private static final int L2S02_TITLE_CELL = 6;
    private static final int L2S02_OPTION_CELL = 7;
    private static final int L2S03_TITLE_CELL = 8;
    private static final int L2S03_OPTION_CELL = 9;
    private static final int L2S04_TITLE_CELL = 10;
    private static final int L2S04_OPTION_CELL = 11;
    private static final int L2S05_TITLE_CELL = 12;
    private static final int L2S05_OPTION_CELL = 13;
    private static final int L2S06_TITLE_CELL = 14;
    private static final int L2S06_OPTION_CELL = 15;
    private static final int SPACETYPE_HIDDEN = 16;
    private static final int ROOM_HIDDEN = 17;
    private static final int L1S01CODE_HIDDEN = 18;

    private XSSFSheet quoteSheet;
    private ExcelStyles styles;
    private ExcelSheetProcessor sheetProcessor;
    List<ProposalSOW> proposal_sows ;
    private ProposalHeader proposalHeader;


    public SowSheetCreator(XSSFSheet quoteSheet, ExcelStyles styles, ProposalHeader proposalHeader, List<ProposalSOW> proposal_sows)
    {
        this.quoteSheet = quoteSheet;
        this.styles = styles;
        this.proposal_sows = proposal_sows;
        this.proposalHeader = proposalHeader;
    }

    public void prepare()
    {
        this.sheetProcessor = new ExcelSheetProcessor(this.quoteSheet, this.styles, this);
        this.sheetProcessor.process();
    }


    @Override
    public Object getValueForKey(String key)
    {
        return null;
    }

    @Override
    public void processCell(Cell cell, String cellValue)
    {
        switch (cellValue)
        {
            case "Space Type":
                //int currentRow = this.fillAssembledProducts(cell.getRow().getRowNum());
                this.fillServicesBasedOnSpaceType(cell.getRow().getRowNum() + 1);
                break;


            default:
                break;
        }
    }

    public int fillServicesBasedOnSpaceType(int currentRow)
    {
        if (proposal_sows.isEmpty())
        {
            LOG.debug("Proposal sow is empty");
            return currentRow;
        }
        List<SpaceRoom> distinctSpaceRooms = getDistinctSpaceRooms();
        for (SpaceRoom spaceRoom : distinctSpaceRooms)
        {
            LOG.debug("Space room:" + spaceRoom);
            currentRow = this.fillServicesInfo(currentRow, spaceRoom);
        }
        return currentRow;

    }

    private List<SpaceRoom> getDistinctSpaceRooms()
    {
        List<SpaceRoom> spaceRooms = new ArrayList<>();
        for (ProposalSOW sow : this.proposal_sows)
        {
            SpaceRoom spaceRoom = new SpaceRoom(sow);
            if (!spaceRooms.contains(spaceRoom))
            {
                spaceRooms.add(spaceRoom);
            }
        }
        return spaceRooms;
    }


    private int fillServicesInfo(int startRow, SpaceRoom spaceRoom)
    {
        int currentRow = startRow;
        int noOfROws = 0;

        Collection<SOWMaster> sowMasterList = ModuleDataService.getInstance().getSOWMaster(spaceRoom.getSpace());
        noOfROws = sowMasterList.size();


        Object[] masterSOWs = sowMasterList.toArray();
        SOWMaster masterSOW = (SOWMaster) masterSOWs[0];
        this.createSpaceTypeHeadingRow(currentRow, spaceRoom.getSpace(),spaceRoom.getRoom(),
                masterSOW.getL1S01Code(), masterSOWs[0]);

        currentRow++;
        for (int i = 1; i <noOfROws ; i++)
        {
            masterSOW = (SOWMaster) masterSOWs[i];
            LOG.debug("SOW master :" + masterSOW);

            this.createSpaceTypeDataRow(currentRow, masterSOW,spaceRoom.getSpace(),
                    masterSOW.getL1S01Code(), spaceRoom.getRoom() );
            currentRow++;
        }

        currentRow++;

        return currentRow;
    }


    private void createSpaceTypeHeadingRow(int rowNum, String spaceType, String room, String L1S01COde, Object sowMaster)
    {
        ProposalSOW proposalSow = null;
        for (ProposalSOW proposal_sow : proposal_sows)
        {
            if (proposal_sow.getSpaceType().equals(spaceType) && proposal_sow.getROOM().equals(room))
            {
                proposalSow = proposal_sow;
            }
        }

        String L1S01DropDownValue = "";
        String L2S01DropDownValue = "";
        String L2S02DropDownValue = "";
        String L2S03DropDownValue = "";
        String L2S04DropDownValue = "";
        String L2S05DropDownValue = "";
        String L2S06DropDownValue = "";

        if (!Objects.equals(proposalSow.getL1S01(), "") || !(proposalSow.getL1S01() == null)) L1S01DropDownValue = proposalSow.getL1S01();
        if (!Objects.equals(proposalSow.getL2S01(), "") || !(proposalSow.getL2S01() == null)) L2S01DropDownValue = proposalSow.getL2S01();
        if (!Objects.equals(proposalSow.getL2S02(), "") || !(proposalSow.getL2S02() == null)) L2S02DropDownValue = proposalSow.getL2S02();
        if (!Objects.equals(proposalSow.getL2S03(), "") || !(proposalSow.getL2S03() == null)) L2S03DropDownValue = proposalSow.getL2S03();
        if (!Objects.equals(proposalSow.getL2S04(), "") || !(proposalSow.getL2S04() == null)) L2S04DropDownValue = proposalSow.getL2S04();
        if (!Objects.equals(proposalSow.getL2S05(), "") || !(proposalSow.getL2S05() == null)) L2S05DropDownValue = proposalSow.getL2S05();
        if (!Objects.equals(proposalSow.getL2S06(), "") || !(proposalSow.getL2S06() == null)) L2S06DropDownValue = proposalSow.getL2S06();

        LOG.debug("Create space type heading row" + rowNum);

        Row dataRow = this.createRow(rowNum, this.quoteSheet);
        SOWMaster sowMaster1 = (SOWMaster) sowMaster;

//        dataRow.setHeight(new Double(dataRow.getHeight() * 1.5).shortValue());
        this.createCellWithData(dataRow, SPACE_TYPE_CELL, Cell.CELL_TYPE_STRING, spaceType).setCellStyle(this.styles.getBoldStyle());
        this.createCellWithData(dataRow, ROOM_CELL, Cell.CELL_TYPE_STRING, room).setCellStyle(this.styles.getBoldStyle());

        this.createCellWithData(dataRow, L1S01_TITLE_CELL, Cell.CELL_TYPE_STRING,sowMaster1.getL1S01()).setCellStyle(this.styles.getColoredCellStyle());
        if (sowMaster1.getL1S01().isEmpty() || Objects.equals(sowMaster1.getL1S01(), "")){
            this.createCellWithData(dataRow, L1S01_TITLE_CELL, Cell.CELL_TYPE_STRING,sowMaster1.getL1S01()).setCellStyle(this.styles.getColoredCellStyle());
        }
        else {
            this.createCellWithDropDownForL1(dataRow, L1S01_OPTION_CELL, Cell.CELL_TYPE_BLANK,rowNum,L1S01DropDownValue).setCellStyle(this.styles.getTextStyle());
        }
        this.createCellWithData(dataRow, L2S01_TITLE_CELL, Cell.CELL_TYPE_STRING, sowMaster1.getL2S01()).setCellStyle(this.styles.getColoredCellStyle());
        if (sowMaster1.getL2S01().isEmpty() || Objects.equals(sowMaster1.getL2S01(), "")){
            this.createCellWithData(dataRow, L2S01_TITLE_CELL, Cell.CELL_TYPE_STRING, sowMaster1.getL2S01()).setCellStyle(this.styles.getColoredCellStyle());
        }
        else {
            this.createCellWithDropDownForL2(dataRow, L2S01_OPTION_CELL, Cell.CELL_TYPE_BLANK, rowNum, L2S01_OPTION_CELL,L2S01DropDownValue).setCellStyle(this.styles.getTextStyle());
        }
        this.createCellWithData(dataRow, L2S02_TITLE_CELL, Cell.CELL_TYPE_STRING, sowMaster1.getL2S02()).setCellStyle(this.styles.getColoredCellStyle());
        if (sowMaster1.getL2S02().isEmpty() || Objects.equals(sowMaster1.getL2S02(), "")){
            this.createCellWithData(dataRow, L2S02_TITLE_CELL, Cell.CELL_TYPE_STRING, sowMaster1.getL2S02()).setCellStyle(this.styles.getColoredCellStyle());
        }
        else {
            this.createCellWithDropDownForL2(dataRow, L2S02_OPTION_CELL, Cell.CELL_TYPE_BLANK, rowNum, L2S02_OPTION_CELL, L2S02DropDownValue).setCellStyle(this.styles.getTextStyle());
        }
        this.createCellWithData(dataRow, L2S03_TITLE_CELL, Cell.CELL_TYPE_STRING, sowMaster1.getL2S03()).setCellStyle(this.styles.getColoredCellStyle());
        if (sowMaster1.getL2S03().isEmpty() || Objects.equals(sowMaster1.getL2S03(), "")){
            this.createCellWithData(dataRow, L2S03_TITLE_CELL, Cell.CELL_TYPE_STRING, sowMaster1.getL2S03()).setCellStyle(this.styles.getColoredCellStyle());
        }
        else {
            this.createCellWithDropDownForL2(dataRow, L2S03_OPTION_CELL, Cell.CELL_TYPE_BLANK, rowNum, L2S03_OPTION_CELL, L2S03DropDownValue).setCellStyle(this.styles.getTextStyle());
        }
        this.createCellWithData(dataRow, L2S04_TITLE_CELL, Cell.CELL_TYPE_STRING, sowMaster1.getL2S04()).setCellStyle(this.styles.getColoredCellStyle());
        if (sowMaster1.getL2S04().isEmpty() || Objects.equals(sowMaster1.getL2S04(), "")){
            this.createCellWithData(dataRow, L2S04_TITLE_CELL, Cell.CELL_TYPE_STRING, sowMaster1.getL2S04()).setCellStyle(this.styles.getColoredCellStyle());
        }
        else {
            this.createCellWithDropDownForL2(dataRow, L2S04_OPTION_CELL, Cell.CELL_TYPE_BLANK, rowNum, L2S04_OPTION_CELL, L2S04DropDownValue).setCellStyle(this.styles.getTextStyle());
        }
        this.createCellWithData(dataRow, L2S05_TITLE_CELL, Cell.CELL_TYPE_STRING, sowMaster1.getL2S05()).setCellStyle(this.styles.getColoredCellStyle());
        if (sowMaster1.getL2S05().isEmpty() || Objects.equals(sowMaster1.getL2S05(), "")){
            this.createCellWithData(dataRow, L2S05_TITLE_CELL, Cell.CELL_TYPE_STRING, sowMaster1.getL2S05()).setCellStyle(this.styles.getColoredCellStyle());
        }
        else {
            this.createCellWithDropDownForL2(dataRow, L2S05_OPTION_CELL, Cell.CELL_TYPE_BLANK, rowNum, L2S05_OPTION_CELL, L2S05DropDownValue).setCellStyle(this.styles.getTextStyle());
        }
        this.createCellWithData(dataRow, L2S06_TITLE_CELL, Cell.CELL_TYPE_STRING, sowMaster1.getL2S06()).setCellStyle(this.styles.getColoredCellStyle());
        if (sowMaster1.getL2S06().isEmpty() || Objects.equals(sowMaster1.getL2S06(), "")){
            this.createCellWithData(dataRow, L2S06_TITLE_CELL, Cell.CELL_TYPE_STRING, sowMaster1.getL2S06()).setCellStyle(this.styles.getColoredCellStyle());
        }
        else {
            this.createCellWithDropDownForL2(dataRow, L2S06_OPTION_CELL, Cell.CELL_TYPE_BLANK, rowNum, L2S06_OPTION_CELL, L2S06DropDownValue).setCellStyle(this.styles.getTextStyle());
        }

        this.createCellWithData(dataRow, SPACETYPE_HIDDEN, Cell.CELL_TYPE_STRING, spaceType).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, ROOM_HIDDEN, Cell.CELL_TYPE_STRING, room).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, L1S01CODE_HIDDEN, Cell.CELL_TYPE_STRING, L1S01COde).setCellStyle(this.styles.getTextStyle());
    }

    private void createSpaceTypeDataRow(int rowNum, Object sowMaster, String spaceType,String L1S01Code, String room)
    {
        LOG.debug("Create space type data row" + rowNum);
        Row dataRow = this.createRow(rowNum, this.quoteSheet);
        SOWMaster sowMaster1 = (SOWMaster) sowMaster;

        ProposalSOW proposal_sow = null;
        for (ProposalSOW proposal_sow_1 : proposal_sows)
        {
            if (proposal_sow_1.getSpaceType().equals(spaceType) && proposal_sow_1.getROOM().equals(room))
            {
                proposal_sow = proposal_sow_1;
            }
        }

        String L1S01DropDownValue = "";
        String L2S01DropDownValue = "";
        String L2S02DropDownValue = "";
        String L2S03DropDownValue = "";
        String L2S04DropDownValue = "";
        String L2S05DropDownValue = "";
        String L2S06DropDownValue = "";

        if (!Objects.equals(proposal_sow.getL1S01(), "") || !(proposal_sow.getL1S01() == null)) L1S01DropDownValue = proposal_sow.getL1S01();
        if (!Objects.equals(proposal_sow.getL2S01(), "") || !(proposal_sow.getL2S01() == null)) L2S01DropDownValue = proposal_sow.getL2S01();
        if (!Objects.equals(proposal_sow.getL2S02(), "") || !(proposal_sow.getL2S02() == null)) L2S02DropDownValue = proposal_sow.getL2S02();
        if (!Objects.equals(proposal_sow.getL2S03(), "") || !(proposal_sow.getL2S03() == null)) L2S03DropDownValue = proposal_sow.getL2S03();
        if (!Objects.equals(proposal_sow.getL2S04(), "") || !(proposal_sow.getL2S04() == null)) L2S04DropDownValue = proposal_sow.getL2S04();
        if (!Objects.equals(proposal_sow.getL2S05(), "") || !(proposal_sow.getL2S05() == null)) L2S05DropDownValue = proposal_sow.getL2S05();
        if (!Objects.equals(proposal_sow.getL2S06(), "") || !(proposal_sow.getL2S06() == null)) L2S06DropDownValue = proposal_sow.getL2S06();

//        dataRow.setHeight(new Double(dataRow.getHeight() * 1.5).shortValue());
        this.createCellWithData(dataRow, SPACE_TYPE_CELL, Cell.CELL_TYPE_STRING, "").setCellStyle(this.styles.getBoldStyle());
        this.createCellWithData(dataRow, ROOM_CELL, Cell.CELL_TYPE_STRING, "").setCellStyle(this.styles.getBoldStyle());

        this.createCellWithData(dataRow, L1S01_TITLE_CELL, Cell.CELL_TYPE_STRING,sowMaster1.getL1S01()).setCellStyle(this.styles.getColoredCellStyle());
        if (sowMaster1.getL1S01().isEmpty() || Objects.equals(sowMaster1.getL1S01(), "")){
            this.createCellWithData(dataRow, L1S01_TITLE_CELL, Cell.CELL_TYPE_STRING,sowMaster1.getL1S01()).setCellStyle(this.styles.getColoredCellStyle());
        }
        else {
            this.createCellWithDropDownForL1(dataRow, L1S01_OPTION_CELL, Cell.CELL_TYPE_BLANK,rowNum, L1S01DropDownValue).setCellStyle(this.styles.getTextStyle());
        }
        this.createCellWithData(dataRow, L2S01_TITLE_CELL, Cell.CELL_TYPE_STRING, sowMaster1.getL2S01()).setCellStyle(this.styles.getColoredCellStyle());
        if (sowMaster1.getL2S01().isEmpty() || Objects.equals(sowMaster1.getL2S01(), "")){
            this.createCellWithData(dataRow, L2S01_TITLE_CELL, Cell.CELL_TYPE_STRING, sowMaster1.getL2S01()).setCellStyle(this.styles.getColoredCellStyle());
        }
        else {
            this.createCellWithDropDownForL2(dataRow, L2S01_OPTION_CELL, Cell.CELL_TYPE_BLANK, rowNum, L2S01_OPTION_CELL, L2S01DropDownValue).setCellStyle(this.styles.getTextStyle());
        }
        this.createCellWithData(dataRow, L2S02_TITLE_CELL, Cell.CELL_TYPE_STRING, sowMaster1.getL2S02()).setCellStyle(this.styles.getColoredCellStyle());
        if (sowMaster1.getL2S02().isEmpty() || Objects.equals(sowMaster1.getL2S02(), "")){
            this.createCellWithData(dataRow, L2S02_TITLE_CELL, Cell.CELL_TYPE_STRING, sowMaster1.getL2S02()).setCellStyle(this.styles.getColoredCellStyle());
        }
        else {
            this.createCellWithDropDownForL2(dataRow, L2S02_OPTION_CELL, Cell.CELL_TYPE_BLANK, rowNum, L2S02_OPTION_CELL, L2S02DropDownValue).setCellStyle(this.styles.getTextStyle());
        }
        this.createCellWithData(dataRow, L2S03_TITLE_CELL, Cell.CELL_TYPE_STRING, sowMaster1.getL2S03()).setCellStyle(this.styles.getColoredCellStyle());
        if (sowMaster1.getL2S03().isEmpty() || Objects.equals(sowMaster1.getL2S03(), "")){
            this.createCellWithData(dataRow, L2S03_TITLE_CELL, Cell.CELL_TYPE_STRING, sowMaster1.getL2S03()).setCellStyle(this.styles.getColoredCellStyle());
        }
        else {
            this.createCellWithDropDownForL2(dataRow, L2S03_OPTION_CELL, Cell.CELL_TYPE_BLANK, rowNum, L2S03_OPTION_CELL, L2S03DropDownValue).setCellStyle(this.styles.getTextStyle());
        }
        this.createCellWithData(dataRow, L2S04_TITLE_CELL, Cell.CELL_TYPE_STRING, sowMaster1.getL2S04()).setCellStyle(this.styles.getColoredCellStyle());
        if (sowMaster1.getL2S04().isEmpty() || Objects.equals(sowMaster1.getL2S04(), "")){
            this.createCellWithData(dataRow, L2S04_TITLE_CELL, Cell.CELL_TYPE_STRING, sowMaster1.getL2S04()).setCellStyle(this.styles.getColoredCellStyle());
        }
        else {
            this.createCellWithDropDownForL2(dataRow, L2S04_OPTION_CELL, Cell.CELL_TYPE_BLANK, rowNum, L2S04_OPTION_CELL, L2S04DropDownValue).setCellStyle(this.styles.getTextStyle());
        }
        this.createCellWithData(dataRow, L2S05_TITLE_CELL, Cell.CELL_TYPE_STRING, sowMaster1.getL2S05()).setCellStyle(this.styles.getColoredCellStyle());
        if (sowMaster1.getL2S05().isEmpty() || Objects.equals(sowMaster1.getL2S05(), "")){
            this.createCellWithData(dataRow, L2S05_TITLE_CELL, Cell.CELL_TYPE_STRING, sowMaster1.getL2S05()).setCellStyle(this.styles.getColoredCellStyle());
        }
        else {
            this.createCellWithDropDownForL2(dataRow, L2S05_OPTION_CELL, Cell.CELL_TYPE_BLANK, rowNum, L2S05_OPTION_CELL, L2S05DropDownValue).setCellStyle(this.styles.getTextStyle());
        }
        this.createCellWithData(dataRow, L2S06_TITLE_CELL, Cell.CELL_TYPE_STRING, sowMaster1.getL2S06()).setCellStyle(this.styles.getColoredCellStyle());
        if (sowMaster1.getL2S06().isEmpty() || Objects.equals(sowMaster1.getL2S06(), "")){
            this.createCellWithData(dataRow, L2S06_TITLE_CELL, Cell.CELL_TYPE_STRING, sowMaster1.getL2S06()).setCellStyle(this.styles.getColoredCellStyle());
        }
        else {
            this.createCellWithDropDownForL2(dataRow, L2S06_OPTION_CELL, Cell.CELL_TYPE_BLANK, rowNum, L2S06_OPTION_CELL, L2S06DropDownValue).setCellStyle(this.styles.getTextStyle());
        }

        this.createCellWithData(dataRow, SPACETYPE_HIDDEN, Cell.CELL_TYPE_STRING, spaceType).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, ROOM_HIDDEN, Cell.CELL_TYPE_STRING, room).setCellStyle(this.styles.getTextStyle());
        this.createCellWithData(dataRow, L1S01CODE_HIDDEN, Cell.CELL_TYPE_STRING,L1S01Code ).setCellStyle(this.styles.getTextStyle());

    }



    private Row createRow(int currentRow, Sheet sheet)
    {
        sheet.shiftRows(currentRow, sheet.getLastRowNum(), 1);
        return sheet.createRow(currentRow);
    }

    private Cell createCellWithData(Row dataRow, int cellNum, int cellType, Object data)
    {
        Cell cell = dataRow.createCell(cellNum, cellType);
        if (data == null) return cell;

        if (cellType == Cell.CELL_TYPE_NUMERIC)
        {
            cell.setCellValue((Double) data);
        }
        else
        {
            cell.setCellValue(data.toString());
        }
        return cell;
    }


    private Cell createCellWithDropDownForL1(Row dataRow, int cellNum, int cellType, int rownum, String defaultValue)
    {
        Cell cell = dataRow.createCell(cellNum);

        cell.setCellValue(defaultValue);

        XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(quoteSheet);
        XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint)
                dvHelper.createExplicitListConstraint(new String[]{"Yes", "No"});
        CellRangeAddressList addressList = new CellRangeAddressList(rownum, rownum, cellNum, cellNum);
        XSSFDataValidation validation = (XSSFDataValidation) dvHelper.createValidation(
                dvConstraint, addressList);
        validation.setShowErrorBox(true);
        quoteSheet.addValidationData(validation);

        return cell;
    }

    private Cell createCellWithDropDownForL2(Row dataRow, int cellNum, int cellType, int rownum, int colnum, String defaultValue)
    {
        Cell cell = dataRow.createCell(cellNum);

        cell.setCellValue(defaultValue);

        XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper((XSSFSheet) quoteSheet);
        XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint)
                dvHelper.createExplicitListConstraint(new String[]{"Mygubbi", "Client", "NA"});
        CellRangeAddressList addressList = new CellRangeAddressList(rownum, rownum, colnum, colnum);
        XSSFDataValidation validation = (XSSFDataValidation)dvHelper.createValidation(
                dvConstraint, addressList);
        validation.setShowErrorBox(true);
        quoteSheet.addValidationData(validation);

        return cell;
    }


}
