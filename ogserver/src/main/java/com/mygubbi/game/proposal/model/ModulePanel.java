package com.mygubbi.game.proposal.model;

import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by test on 17-05-2016.
 */
public class ModulePanel
{
    private final static Logger LOG = LogManager.getLogger(ModulePanel.class);

    private String code;
    private String type;
    private String side;
    private String title;
    private String subtitle;
    private int length;
    private int breadth;
    private int thickness;
    private String edgebinding;
    private String dimensionFormula;

    public static ModulePanel fromJson(JsonObject json)
    {
        return new ModulePanel().setCode(json.getString("code")).setType(json.getString("type"))
                .setSide(json.getString("side")).setTitle(json.getString("title"))
                .setSubtitle(json.getString("subtitle"))
                .setLength(json.getInteger("plength")).setBreadth(json.getInteger("breadth"))
                .setThickness(json.getInteger("thickness")).setEdgebinding(json.getString("edgebinding"))
                .setDimensionFormula(json.getString("dimensionFormula"));
    }

    public String getCode()
    {
        return code;
    }

    public ModulePanel setCode(String code)
    {
        this.code = code;
        return this;
    }

    public String getTitle()
    {
        return title;
    }

    public ModulePanel setTitle(String title)
    {
        this.title = title;
        return this;
    }

    public int getLength()
    {
        return length;
    }

    public ModulePanel setLength(int length)
    {
        this.length = length;
        return this;
    }

    public int getBreadth()
    {
        return breadth;
    }

    public ModulePanel setBreadth(int breadth)
    {
        this.breadth = breadth;
        return this;
    }

    public int getThickness()
    {
        return thickness;
    }

    public ModulePanel setThickness(int thickness)
    {
        this.thickness = thickness;
        return this;
    }

    public String getEdgebinding()
    {
        return edgebinding;
    }

    public ModulePanel setEdgebinding(String edgebinding)
    {
        this.edgebinding = edgebinding;
        return this;
    }

    public String getDimesions()
    {
        return this.getLength() + " X "  + this.getBreadth() +  " X " + this.getThickness();
    }

    public String getType()
    {
        return type;
    }

    public ModulePanel setType(String type)
    {
        this.type = type;
        return this;
    }

    public String getSide()
    {
        return side;
    }

    public ModulePanel setSide(String side)
    {
        this.side = side;
        return this;
    }

    public String getSubtitle()
    {
        return subtitle;
    }

    public ModulePanel setSubtitle(String subtitle)
    {
        this.subtitle = subtitle;
        return this;
    }

    public String getDimensionFormula()
    {
        return dimensionFormula;
    }

    public ModulePanel setDimensionFormula(String dimensionFormula)
    {
        this.dimensionFormula = dimensionFormula;
        return this;
    }
}
