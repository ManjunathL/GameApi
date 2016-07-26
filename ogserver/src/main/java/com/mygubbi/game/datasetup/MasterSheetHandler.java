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
        String moduleCode = (String) data[0];
        if (StringUtils.isEmpty(moduleCode) || "Module Code".equals(moduleCode)) return;

        String kdmaxCode = (String) data[1];
        String description = (String) data[2];
        int width = Double.valueOf((String) data[3]).intValue();
        int depth = Double.valueOf((String) data[4]).intValue();
        int height = Double.valueOf((String) data[5]).intValue();

        this.printModuleRow(kdmaxCode, moduleCode, description, width, depth, height);
        this.loopForComponents(data, moduleCode, 6, 31, "C");
        this.loopForComponents(data, moduleCode, 32, 61, "H");
        this.loopForComponents(data, moduleCode, 62, 89, "S");
    }

    private void loopForComponents(Object[] data, String moduleCode, int startColumn, int endColumn, String compType)
    {
        for (int i = startColumn; i <= endColumn; i = i+2)
        {
            String compCode = (String) data[i];
            if (StringUtils.isEmpty(compCode)) continue;
            String quantity = (String) data[i+1];
            this.printComponentRow(moduleCode, compCode, quantity, compType);
        }
    }

    private void printModuleRow(String extCode, String code, String description, int width, int depth, int height)
    {
        String dimension = width + "x" + depth + "x" + height;
        System.out.println("insert module_master(extCode, code, description, imagePath, width, depth, height, dimension) " +
                "values ('" + extCode + "','" + code + "','" + description + "','" + code + ".jpg" + "'," + width + "," + depth + "," + height + ",'" + dimension + "');");
    }

    private void printComponentRow(String moduleCode, String compCode, String quantity, String compType)
    {
        System.out.println("insert module_components(modulecode, comptype, compcode, quantity) " +
                "values ('" + moduleCode + "','" + compType + "','" + compCode + "'," + quantity + ");");
    }

    @Override
    public void done()
    {
        System.out.println("Done");
    }

}
