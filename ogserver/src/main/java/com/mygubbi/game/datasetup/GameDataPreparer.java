package com.mygubbi.game.datasetup;

import com.mygubbi.si.excel.ExcelReaderService;

/**
 * Created by Sunil on 05-05-2016.
 */
public class GameDataPreparer
{
    public static final String XL_FILE = "D:\\MyGubbi\\SQL\\New\\Master Working Sheet_Kitchen_KD Max - R1.xlsx";

    public static void main(String[] args)
    {
        GameDataPreparer dataPreparer = new GameDataPreparer();
        dataPreparer.processMasterSheet();
        //dataPreparer.processAccPackSheet();
        //dataPreparer.processAccPackmappingSheet();
    }

    private void processMasterSheet() {
        int[] columnsToRead = new int[66];
        for (int i = 0; i<columnsToRead.length; i++)
        {
            columnsToRead[i] = i;
        }
        new ExcelReaderService(XL_FILE, 0, true, columnsToRead, new MasterSheetHandler()).read();
    }

    private void processAccPackSheet() {
        int[] columnsToRead = new int[19];
        for (int i = 0; i<columnsToRead.length; i++)
        {
            columnsToRead[i] = i;
        }
        new ExcelReaderService(XL_FILE, 1, true, columnsToRead, new AccessoryPackHandler()).read();
    }

    private void processAccPackmappingSheet() {
        int[] columnsToRead = new int[22];
        for (int i = 0; i<columnsToRead.length; i++)
        {
            columnsToRead[i] = i;
        }
        new ExcelReaderService(XL_FILE, 2, true, columnsToRead, new AccessoryPackModuleMappingHandler()).read();
    }
}
