package com.mygubbi.game.datasetup;

import com.mygubbi.common.StringUtils;

/**
 * Created by Sunil on 05-05-2016.
 */
public class WardrobeMasterSheetHandler extends AbstractMasterSheetHandler
{
    public static final int[] wardrobe_indices = new int[]{0,1,2,3,4,5,6,45,46,81,83,88};

    public WardrobeMasterSheetHandler()
    {
        super(wardrobe_indices, "L/R");
    }

    public WardrobeMasterSheetHandler(String filename) throws Exception
    {
        super(wardrobe_indices, "L/R", filename);
    }


    @Override
    protected String getModuleCode(String moduleCode, String kdmaxCode)
    {
        if (StringUtils.isNonEmpty(kdmaxCode)) return kdmaxCode.trim();
        else return null;
    }

    @Override
    protected String getModuleCodeWithSuffix(String moduleCode)
    {
       /* if (moduleCode.endsWith("L"))
            return moduleCode.substring(0, moduleCode.length() - 1) + "-EXTL";
        else if (moduleCode.endsWith("R"))
            return moduleCode.substring(0, moduleCode.length() - 1) + "-EXTR";
        else
            return moduleCode + "-EXT"; */
        return moduleCode;
    }
}
