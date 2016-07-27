package com.mygubbi.game.datasetup;

import com.mygubbi.si.excel.ExcelReaderService;
import com.mygubbi.si.excel.ExcelRowHandler;

/**
 * Created by Sunil on 05-05-2016.
 */
public class GameDataPreparer
{
    //public static final String XL_FILE = "D:\\work\\mygubbi\\game-2\\27Jul2016\\kitchen.xlsx";
    public static final String XL_FILE = "D:\\work\\mygubbi\\game-2\\27Jul2016\\wardrobe.xlsx";

    public static void main(String[] args)
    {
        GameDataPreparer dataPreparer = new GameDataPreparer();
        //dataPreparer.processSheet(66, 0, new MasterSheetHandler(MasterSheetHandler.kitchen_indices));
        dataPreparer.processSheet(89, 0, new MasterSheetHandler(MasterSheetHandler.wardrobe_indices));
        //dataPreparer.processSheet(19, 1, new AccPackHandler());
        //dataPreparer.processSheet(22, 2, new AccessoryPackModuleMappingHandler());
    }

    private void processSheet(int maxColumns, int sheetNumber, ExcelRowHandler rowHandler) {
        int[] columnsToRead = new int[maxColumns];
        for (int i = 0; i<columnsToRead.length; i++)
        {
            columnsToRead[i] = i;
        }
        new ExcelReaderService(XL_FILE, sheetNumber, true, columnsToRead, rowHandler).read();
    }

}
