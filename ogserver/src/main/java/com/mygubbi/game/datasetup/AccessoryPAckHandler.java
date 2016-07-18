package com.mygubbi.game.datasetup;

import com.mygubbi.common.StringUtils;
import com.mygubbi.si.excel.ExcelRowHandler;

/**
 * Created by Sunil on 05-05-2016.
 */
public class AccessoryPackHandler implements ExcelRowHandler
{
    @Override
    public void handle(Object[] data)
    {
        String accode = (String) data[1];
        if (StringUtils.isEmpty( accode) || "Accessory Pack".equals( accode)) return;

        this.loopForComponents(data,  accode, 3, 6, "C");
        this.loopForComponents(data,  accode, 7, 12, "H");
        this.loopForComponents(data,  accode ,13, 18, "A");
    }

    private void loopForComponents(Object[] data, String accode, int startColumn, int endColumn, String compType)
    {
        for (int i = startColumn; i <= endColumn; i = i+2)
        {
            String compCode = (String) data[i];
            if (StringUtils.isEmpty(compCode)) continue;
            String quantity = (String) data[i+1];
            this.printRow(accode, compCode, quantity, compType);
        }
    }

    private void printRow(String moduleCode, String compCode, String quantity, String compType)
    {
        System.out.println(moduleCode + "|" + compType + "|" + compCode + "|" + quantity);
    }

    @Override
    public void done()
    {
        System.out.println("Done");
    }

}
