package com.mygubbi.game.datasetup;

import com.mygubbi.common.StringUtils;

/**
 * Created by Sunil on 05-05-2016.
 */
public class KitchenMasterSheetHandler extends AbstractMasterSheetHandler
{
    public static final int[] kitchen_indices = new int[]{1,2,3,4,5,6,7,32,33,60,62,65};

    public KitchenMasterSheetHandler()
    {
        super(kitchen_indices, "L / R");
    }


    @Override
    protected String getModuleCode(String moduleCode, String kdmaxCode)
    {
        if (StringUtils.isNonEmpty(kdmaxCode)) return kdmaxCode.trim();
        else return moduleCode.trim();
    }

    @Override
    protected String getModuleCodeWithSuffix(String moduleCode)
    {
        return moduleCode;
    }
}
