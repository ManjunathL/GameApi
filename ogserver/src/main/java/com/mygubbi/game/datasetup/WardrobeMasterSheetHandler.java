package com.mygubbi.game.datasetup;

import com.mygubbi.common.StringUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    private Set<String> moduleCodes = Collections.emptySet();

    public WardrobeMasterSheetHandler(String filename) throws Exception
    {
        super(wardrobe_indices, "L/R", filename);
    }

    public WardrobeMasterSheetHandler(String filename, String moduleFile) throws Exception
    {
        super(wardrobe_indices, "L/R", filename);
        this.loadModuleCodes(moduleFile);
    }

    private void loadModuleCodes(String moduleFile) throws IOException {
        this.moduleCodes = new HashSet<>(FileUtils.readLines(new File(moduleFile)));

    }


    @Override
    protected String getModuleCode(String moduleCode, String kdmaxCode)
    {
        if (StringUtils.isNonEmpty(kdmaxCode))
        {
            kdmaxCode = kdmaxCode.trim();
            if (!this.moduleCodes.contains(kdmaxCode)) return kdmaxCode;
        }
        return null;
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
