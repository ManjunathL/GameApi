package com.mygubbi.game.datasetup;

import com.mygubbi.common.StringUtils;
import com.mygubbi.si.excel.ExcelRowHandler;

/**
 * Created by Sunil on 05-05-2016.
 */
public class MasterSheetHandler implements ExcelRowHandler
{
    @Override
    public void handle(Object[] data)
    {
        String moduleCode = (String) data[1];
        if (StringUtils.isEmpty(moduleCode) || "Module Code".equals(moduleCode)) return;

        String kdmaxCode = (String) data[2];

        this.loopForComponents(data, moduleCode, kdmaxCode, 7, 32, "C");
        this.loopForComponents(data, moduleCode, kdmaxCode, 33, 60, "H");
        this.loopForComponents(data, moduleCode, kdmaxCode, 62, 65, "S");
    }

    private void loopForComponents(Object[] data, String moduleCode, String kdmaxCode, int startColumn, int endColumn, String compType)
    {
        for (int i = startColumn; i <= endColumn; i = i+2)
        {
            String compCode = (String) data[i];
            if (StringUtils.isEmpty(compCode)) continue;
            String quantity = (String) data[i+1];
            this.printRow(moduleCode, kdmaxCode, compCode, quantity, compType);
        }
    }

    private void printRow(String moduleCode, String kdmaxCode, String compCode, String quantity, String compType)
    {
        System.out.println(moduleCode + "|" + kdmaxCode + "|" + compType + "|" + compCode + "|" + quantity);
    }

    @Override
    public void done()
    {
        System.out.println("Done");
    }

}
