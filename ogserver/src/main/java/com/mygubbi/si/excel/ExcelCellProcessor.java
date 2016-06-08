package com.mygubbi.si.excel;

import org.apache.poi.ss.usermodel.Cell;

/**
 * Created by test on 08-06-2016.
 */
public interface ExcelCellProcessor
{
    public Object getValueForKey(String key);

    public void processCell(Cell cell, String cellValue);
}
