package com.mygubbi.si.excel;

/**
 * Created by Sunil on 05-05-2016.
 */
public interface ExcelRowHandler
{
    public void handle(Object[] data);

    public void done();
}
