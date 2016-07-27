package com.mygubbi.game.datasetup;

import com.mygubbi.common.StringUtils;
import com.mygubbi.si.excel.ExcelRowHandler;

/**
 * Created by Sunil on 05-05-2016.
 */
public class MasterSheetHandler implements ExcelRowHandler
{
    public static final int[] kitchen_indices = new int[]{1,2,3,4,5,6,7,32,33,60,62,65};
    public static final int[] wardrobe_indices = new int[]{0,1,2,3,4,5,6,45,46,81,83,88};

    private static int MODULE_CODE = 0;
    private static int KDMAX_CODE = 1;
    private static int TITLE = 2;
    private static int WIDTH = 3;
    private static int DEPTH = 4;
    private static int HEIGHT = 5;
    private static int CARCASS_START = 6;
    private static int CARCASS_END= 7;
    private static int HW_START = 8;
    private static int HW_END= 9;
    private static int FINISH_START = 10;
    private static int FINISH_END= 11;

    private int[] indices;

    public MasterSheetHandler(int[] indices)
    {
        this.indices = indices;
    }

    @Override
    public void handle(Object[] data)
    {
        String moduleCode = (String) data[this.indices[MODULE_CODE]];
        if (StringUtils.isEmpty(moduleCode) || "Module Code".equals(moduleCode)) return;

        String kdmaxCode = (String) data[this.indices[KDMAX_CODE]];
        if (StringUtils.isEmpty(kdmaxCode)) kdmaxCode = moduleCode;
        String description = (String) data[this.indices[TITLE]];
        int width = getInteger(data[this.indices[WIDTH]]);
        int depth = getInteger(data[this.indices[DEPTH]]);
        int height = getInteger(data[this.indices[HEIGHT]]);

        System.out.println("begin;");
        this.printModuleRow(kdmaxCode, kdmaxCode, description, width, depth, height);
        this.loopForComponents(data, kdmaxCode, this.indices[CARCASS_START], this.indices[CARCASS_END], "C");
        this.loopForComponents(data, kdmaxCode, this.indices[HW_START], this.indices[HW_END], "H");
        this.loopForComponents(data, kdmaxCode, this.indices[FINISH_START], this.indices[FINISH_END], "S");
        System.out.println("commit;");
    }

    private int getInteger(Object value)
    {
        String textValue = (String) value;
        if (StringUtils.isEmpty(textValue)) return 0;
        return Double.valueOf(textValue).intValue();
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
        System.out.println("insert module_master(code, description, imagePath, width, depth, height, dimension) " +
                "values ('" + code + "','" + description + "','" + code + ".jpg" + "'," + width + "," + depth + "," + height + ",'" + dimension + "');");
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
