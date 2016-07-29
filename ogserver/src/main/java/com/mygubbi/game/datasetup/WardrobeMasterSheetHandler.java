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


    @Override
    protected String getModuleCode(String moduleCode, String kdmaxCode)
    {
        if (StringUtils.isNonEmpty(kdmaxCode)) return kdmaxCode.trim();
        else return null;
    }

    @Override
    protected String getModuleCodeWithSuffix(String moduleCode)
    {
        return moduleCode + "-EXT";
    }
}
