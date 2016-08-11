package com.mygubbi.game.datasetup;

import com.mygubbi.si.excel.ExcelReaderService;

/**
 * Created by Sunil on 05-05-2016.
 */
public class KDMaxMappingLoader
{
    public static void main(String[] args)
    {
        new ExcelReaderService("D:\\work\\mygubbi\\gamedata\\jun9-2016\\kdmax-map.xlsx", 0, true, new int[]{0,1,2,3,4,5,6,7}, new KDMaxMappingHandler()).read();
    }
}
