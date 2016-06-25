package com.mygubbi.game.proposal.model;

import io.vertx.core.json.JsonObject;

/**
 * Created by test on 17-05-2016.
 */

public class ShutterFinish
{
    private static final String COST_CODE = "costCode";
    private static final String FINISH_CODE = "finishCode";
    private static final String FINISH_TYPE = "finishType";
    private static final String FINISH_MATERIAL = "finishMaterial";
    private static final String DESIGN = "design";
    private static final String SHUTTER_MATERIAL = "shutterMaterial";
    private static final String COLOR_GROUP_CODE = "colorGroupCode";
    private static final String CUTTING_OFFSET = "cuttingOffset";
    private static final String TITLE = "title";

    private String costCode;
    private String finishCode;
    private String finishType;
    private String finishMaterial;
    private String design;
    private String shutterMaterial;
    private String colorGroupCode;
    private String title;
    private int cuttingOffset;

    public ShutterFinish()
    {

    }

    public ShutterFinish(JsonObject json)
    {
        this.setFinishCode(json.getString(FINISH_CODE)).setCostCode(json.getString(COST_CODE)).setFinishType(json.getString(FINISH_TYPE))
                .setFinishMaterial(json.getString(FINISH_MATERIAL)).setDesign(json.getString(DESIGN)).setShutterMaterial(json.getString(SHUTTER_MATERIAL))
                .setColorGroupCode(json.getString(COLOR_GROUP_CODE)).setTitle(json.getString(TITLE)).setCuttingOffset(json.getInteger(CUTTING_OFFSET));
    }

    public String getCostCode()
    {
        return costCode;
    }

    public ShutterFinish setCostCode(String costCode)
    {
        this.costCode = costCode;
        return this;
    }

    public String getFinishCode()
    {
        return finishCode;
    }

    public ShutterFinish setFinishCode(String finishCode)
    {
        this.finishCode = finishCode;
        return this;
    }

    public String getFinishType()
    {
        return finishType;
    }

    public ShutterFinish setFinishType(String finishType)
    {
        this.finishType = finishType;
        return this;
    }

    public String getFinishMaterial()
    {
        return finishMaterial;
    }

    public ShutterFinish setFinishMaterial(String finishMaterial)
    {
        this.finishMaterial = finishMaterial;
        return this;
    }

    public String getDesign()
    {
        return design;
    }

    public ShutterFinish setDesign(String design)
    {
        this.design = design;
        return this;
    }

    public String getShutterMaterial()
    {
        return shutterMaterial;
    }

    public ShutterFinish setShutterMaterial(String shutterMaterial)
    {
        this.shutterMaterial = shutterMaterial;
        return this;
    }

    public String getColorGroupCode()
    {
        return colorGroupCode;
    }

    public ShutterFinish setColorGroupCode(String colorGroupCode)
    {
        this.colorGroupCode = colorGroupCode;
        return this;
    }

    public String getTitle()
    {
        return title;
    }

    public ShutterFinish setTitle(String title)
    {
        this.title = title;
        return this;
    }

    public int getCuttingOffset()
    {
        return cuttingOffset;
    }

    public ShutterFinish setCuttingOffset(int cuttingOffset)
    {
        this.cuttingOffset = cuttingOffset;
        return this;
    }
}