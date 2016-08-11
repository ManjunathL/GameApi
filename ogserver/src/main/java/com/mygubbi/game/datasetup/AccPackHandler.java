package com.mygubbi.game.datasetup;

import com.mygubbi.common.StringUtils;
import com.mygubbi.si.excel.ExcelRowHandler;

/**
 * Created by Sunil on 19-07-2016.
 */
public class AccPackHandler implements ExcelRowHandler
{
    @Override
    public void handle(Object[] data)
    {
        String accode = (String) data[1];
        if (StringUtils.isEmpty( accode) || "Accessory Pack".equals( accode)) return;

        String title = (String) data[2];

        System.out.println("begin;");
        this.printPackRow(accode, title);
        this.loopForComponents(data,  accode, 3, 6, "C");
        this.loopForComponents(data,  accode, 7, 12, "H");
        this.loopForComponents(data,  accode ,13, 18, "A");
        System.out.println("commit;");
    }

    private void loopForComponents(Object[] data, String accode, int startColumn, int endColumn, String compType)
    {
        for (int i = startColumn; i <= endColumn; i = i+2)
        {
            String compCode = (String) data[i];
            if (StringUtils.isEmpty(compCode)) continue;
            double quantity = Double.valueOf((String) data[i+1]).doubleValue();
            this.printComponentRow(accode, compCode, quantity, compType);
        }
    }

    private void printPackRow(String accode, String title)
    {
        System.out.println("insert acc_pack_master(code, title) values ('" + accode + "','" + title + "');");
    }

    private void printComponentRow(String apCode, String compCode, double quantity, String compType)
    {
        System.out.println("insert acc_pack_components(apcode, type, code, qty) " +
                "values ('" + apCode + "','" + compType + "','" + compCode + "'," + quantity + ");");
    }

    @Override
    public void done()
    {
        System.out.println("Done");
    }

}

