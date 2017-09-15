package com.mygubbi.game.proposal.model;

import com.mygubbi.common.DateUtil;
import io.vertx.core.json.JsonObject;

import java.sql.Date;

/**
 * Created by test on 17-05-2016.
 */

public class ShutterFinish
{
    private static final String COST_CODE = "costCode";
    private static final String DOUBLE_EXPOSED_COST_CODE = "doubleExposedCostCode";
    private static final String FINISH_CODE = "finishCode";
    private static final String FINISH_TYPE = "finishType";
    private static final String FINISH_MATERIAL = "finishMaterial";
    private static final String DESIGN = "design";
    private static final String SHUTTER_MATERIAL = "shutterMaterial";
    private static final String COLOR_GROUP_CODE = "colorGroupCode";
    private static final String CUTTING_OFFSET = "cuttingOffset";
    private static final String TITLE = "title";
    private static final String EDGE_BINDING = "edgeBinding";
    private static final String FROM_DATE="fromDate";
    private static final String TO_DATE="toDate";


    private String costCode;
    private String doubleExposedCostCode;
    private String finishCode;
    private String finishType;
    private String finishMaterial;
    private String design;
    private String shutterMaterial;
    private String colorGroupCode;
    private String title;
    private int cuttingOffset;
    private String edgeBinding;
    private Date fromDate;
    private Date toDate;

    public ShutterFinish()
    {
    }

    public ShutterFinish(JsonObject json)
    {
        Date fromDateInSql = DateUtil.convertDate(json.getString(FROM_DATE));
        Date toDateInSql = DateUtil.convertDate(json.getString(TO_DATE));;

        this.setFinishCode(json.getString(FINISH_CODE)).setCostCode(json.getString(COST_CODE)).setFinishType(json.getString(FINISH_TYPE))
                .setFinishMaterial(json.getString(FINISH_MATERIAL)).setDesign(json.getString(DESIGN)).setShutterMaterial(json.getString(SHUTTER_MATERIAL))
                .setColorGroupCode(json.getString(COLOR_GROUP_CODE)).setTitle(json.getString(TITLE)).setCuttingOffset(json.getInteger(CUTTING_OFFSET))
                .setEdgeBinding(json.getString(EDGE_BINDING)).setDoubleExposedCostCode(json.getString(DOUBLE_EXPOSED_COST_CODE)).setFromDate(fromDateInSql).setToDate(toDateInSql);
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

    public String getEdgeBinding() {
        return edgeBinding;
    }

    public ShutterFinish setEdgeBinding(String edgeBinding) {
        this.edgeBinding = edgeBinding;
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

    public String getDoubleExposedCostCode()
    {
        return doubleExposedCostCode;
    }

    public ShutterFinish setDoubleExposedCostCode(String doubleExposedCostCode)
    {
        this.doubleExposedCostCode = doubleExposedCostCode;
        return this;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public ShutterFinish setFromDate(Date fromDate) {
        this.fromDate = fromDate;
        return this;
    }

    public Date getToDate() {
        return toDate;
    }

    public ShutterFinish setToDate(Date toDate) {
        this.toDate = toDate;
        return this;
    }

    @Override
    public String toString() {
        return "ShutterFinish{" +
                "costCode='" + costCode + '\'' +
                ", doubleExposedCostCode='" + doubleExposedCostCode + '\'' +
                ", finishCode='" + finishCode + '\'' +
                ", finishType='" + finishType + '\'' +
                ", finishMaterial='" + finishMaterial + '\'' +
                ", design='" + design + '\'' +
                ", shutterMaterial='" + shutterMaterial + '\'' +
                ", colorGroupCode='" + colorGroupCode + '\'' +
                ", title='" + title + '\'' +
                ", cuttingOffset=" + cuttingOffset +
                ", edgeBinding='" + edgeBinding + '\'' +
                ", fromDate=" + fromDate +
                ", toDate=" + toDate +
                '}';
    }
}
