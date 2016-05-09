package com.mygubbi.game.datasetup;

import com.mygubbi.si.excel.ExcelReaderService;

/**
 * Created by Sunil on 05-05-2016.
 */
public class KDMaxMappingLoader
{
    public static void main(String[] args)
    {
        new ExcelReaderService("/testdata/KDMax-ModuleMaster.xlsx", 0, true, new int[]{7,8,9,10,11,12,13,14}, new KDMaxMappingHandler()).read();
    }
}
