package com.mygubbi.game.datasetup;

import com.mygubbi.si.excel.ExcelReaderService;

/**
 * Created by Sunil on 05-05-2016.
 */
public class GameDataPreparer
{

    public static final String XL_FILE = "D:\\work\\mygubbi\\game-2\\Master Working Sheet_Kitchen_KD Max - R1.xlsx";

    public static void main(String[] args)
    {
        int[] columnsToRead = new int[66];
        for (int i = 0; i<columnsToRead.length; i++)
        {
            columnsToRead[i] = i;
        }
        new ExcelReaderService(XL_FILE, 0, true, columnsToRead, new MasterSheetHandler()).read();
    }
}
