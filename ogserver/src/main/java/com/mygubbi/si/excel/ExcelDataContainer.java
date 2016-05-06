package com.mygubbi.si.excel;

import com.mygubbi.game.proposal.ProductModule;

import java.util.List;

/**
 * Created by Sunil on 27-04-2016.
 */

public class ExcelDataContainer
{
    private String excelFile;
    private List<ProductModule> modules;

    public ExcelDataContainer(String excelFile)
    {
        this.excelFile = excelFile;
    }

    public String getExcelFile()
    {
        return excelFile;
    }

    public List<ProductModule> getModules()
    {
        return modules;
    }

    public void setModules(List<ProductModule> modules)
    {
        this.modules = modules;
    }
}
