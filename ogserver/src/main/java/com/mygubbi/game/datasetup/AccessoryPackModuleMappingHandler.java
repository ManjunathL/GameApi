package com.mygubbi.game.datasetup;

import com.mygubbi.common.StringUtils;
import com.mygubbi.si.excel.ExcelRowHandler;

/**
 * Created by Sunil on 05-05-2016.
 */
public class AccessoryPackModuleMappingHandler implements ExcelRowHandler
{
    @Override
    public void handle(Object[] data)
    {
        String accode = (String) data[1];
        if (StringUtils.isEmpty( accode) || "Accessory Pack Code".equals( accode)) return;

        this.loopForComponents(data,  accode, 2, 21);

    }

    private void loopForComponents(Object[] data, String accode, int startColumn, int endColumn)
    {
        for (int i = startColumn; i <= endColumn; i = i+2)
        {
            String mgcode = (String) data[i];
            if (StringUtils.isEmpty(mgcode)) continue;
            String kdmaxcode = (String) data[i+1];
            this.printRow(accode, mgcode, kdmaxcode);
        }
    }

    private void printRow(String accode, String mgcode, String kdmaxcode)
    {
        System.out.println("insert module_acc_pack(apcode, extcode, mgcode) values ('" + accode + "','" + kdmaxcode + "','" + mgcode + "');");
    }

    @Override
    public void done()
    {
        System.out.println("Done");
    }

}
