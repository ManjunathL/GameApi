package com.mygubbi.game.datasetup;

import com.mygubbi.common.StringUtils;
import com.mygubbi.si.excel.ExcelRowHandler;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * Created by Sunil on 28-07-2016.
 */
public abstract class AbstractMasterSheetHandler implements ExcelRowHandler
{
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
    private String leftRightSuffix;

    private PrintStream out;

    protected abstract String getModuleCode(String moduleCode, String kdmaxCode);
    protected abstract String getModuleCodeWithSuffix(String moduleCode);

    public AbstractMasterSheetHandler(int[] indices, String leftRightSuffix)
    {
        this(indices, leftRightSuffix, System.out);
    }

    public AbstractMasterSheetHandler(int[] indices, String leftRightSuffix, String filename) throws Exception
    {
        this(indices, leftRightSuffix, new PrintStream(new FileOutputStream(filename)));
    }

    public AbstractMasterSheetHandler(int[] indices, String leftRightSuffix, PrintStream out)
    {
        this.indices = indices;
        this.leftRightSuffix = leftRightSuffix;
        this.out = out;
    }

    @Override
    public void handle(Object[] data)
    {
        String moduleCode = (String) data[this.indices[MODULE_CODE]];
        if (StringUtils.isEmpty(moduleCode) || "Module Code".equals(moduleCode)) return;
        String kdmaxCode = (String) data[this.indices[KDMAX_CODE]];

        String moduleCodeToUse = this.getModuleCode(moduleCode, kdmaxCode);
        if (StringUtils.isEmpty(moduleCodeToUse)) return;

        String description = (String) data[this.indices[TITLE]];
        int width = getInteger(data[this.indices[WIDTH]]);
        int depth = getInteger(data[this.indices[DEPTH]]);
        int height = getInteger(data[this.indices[HEIGHT]]);

        this.out.println("begin;");
        if (moduleCodeToUse.endsWith(this.leftRightSuffix))
        {
            String shortModuleCode = moduleCodeToUse.substring(0, (moduleCodeToUse.length() - this.leftRightSuffix.length()));
            this.printRecords(data, shortModuleCode + "L", description, width, depth, height);
            this.printRecords(data, shortModuleCode + "R", description, width, depth, height);
        }
        else
        {
            this.printRecords(data, moduleCodeToUse, description, width, depth, height);
        }
        this.out.println("commit;");
    }

    private void printRecords(Object[] data, String moduleCode, String description, int width, int depth, int height)
    {
        String moduleCodeWithSuffix = this.getModuleCodeWithSuffix(moduleCode);
        this.printModuleRow(moduleCodeWithSuffix, description, width, depth, height);
        this.loopForComponents(data, moduleCodeWithSuffix, this.indices[CARCASS_START], this.indices[CARCASS_END], "C");
        this.loopForComponents(data, moduleCodeWithSuffix, this.indices[HW_START], this.indices[HW_END], "H");
        this.loopForComponents(data, moduleCodeWithSuffix, this.indices[FINISH_START], this.indices[FINISH_END], "S");
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

    private void printModuleRow(String code, String description, int width, int depth, int height)
    {
        String dimension = width + "x" + depth + "x" + height;
        this.out.println("insert module_master(code, description, imagePath, width, depth, height, dimension) " +
                "values ('" + code + "','" + description + "','" + code + ".jpg" + "'," + width + "," + depth + "," + height + ",'" + dimension + "');");
    }

    private void printComponentRow(String moduleCode, String compCode, String quantity, String compType)
    {
        this.out.println("insert module_components(modulecode, comptype, compcode, quantity) " +
                "values ('" + moduleCode + "','" + compType + "','" + compCode + "'," + quantity + ");");
    }

    @Override
    public void done()
    {
        System.out.println("Done");
        this.out.close();
    }
}
