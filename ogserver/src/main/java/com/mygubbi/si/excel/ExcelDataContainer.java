package com.mygubbi.si.excel;

import com.mygubbi.game.proposal.KDMaxModule;

import java.util.List;
import java.util.Map;

/**
 * Created by Sunil on 27-04-2016.
 */

public class ExcelDataContainer
{
    private String excelFile;
    private List<KDMaxModule> modules;

    public ExcelDataContainer(String excelFile)
    {
        this.excelFile = excelFile;
    }

    public String getExcelFile()
    {
        return excelFile;
    }

    public List<KDMaxModule> getModules()
    {
        return modules;
    }

    public void setModules(List<KDMaxModule> modules)
    {
        this.modules = modules;
    }
}
