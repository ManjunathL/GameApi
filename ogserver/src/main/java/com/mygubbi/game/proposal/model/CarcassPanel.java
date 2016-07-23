package com.mygubbi.game.proposal.model;

import io.vertx.core.json.JsonObject;

/**
 * Created by Sunil on 17-05-2016.
 */

//TODO: Make an abstract panel with Carcass and Shutter as child classes

public class CarcassPanel
{
    private static final double SQMM2SQFT = 0.0000107639;

    private static final String TYPE_LEFT = "L";
    private static final String TYPE_RIGHT = "R";
    private static final String TYPE_TOP = "T";
    private static final String TYPE_BOTTOM = "B";
    private static final String TYPE_BACK = "K";
    private static final String TYPE_OTHER = "O";

    private String code;
    private String title;
    private int length;
    private int breadth;
    private int thickness;
    private String edgebinding;
    private double area;
    private String type;

    public static CarcassPanel fromJson(JsonObject json)
    {
        return new CarcassPanel().setCode(json.getString("code")).setTitle(json.getString("title"))
                .setLength(json.getInteger("plength")).setBreadth(json.getInteger("breadth"))
                .setThickness(json.getInteger("thickness")).setEdgebinding(json.getString("edgebinding"))
                .setArea(json.getDouble("area")).setType(json.getString("type"));
    }

    public String getCode()
    {
        return code;
    }

    public CarcassPanel setCode(String code)
    {
        this.code = code;
        return this;
    }

    public CarcassPanel setType(String type)
    {
        this.type = type;
        return this;
    }

    public String getTitle()
    {
        return title;
    }

    public CarcassPanel setTitle(String title)
    {
        this.title = title;
        return this;
    }

    public int getLength()
    {
        return length;
    }

    public CarcassPanel setLength(int length)
    {
        this.length = length;
        return this;
    }

    public int getBreadth()
    {
        return breadth;
    }

    public CarcassPanel setBreadth(int breadth)
    {
        this.breadth = breadth;
        return this;
    }

    public int getThickness()
    {
        return thickness;
    }

    public CarcassPanel setThickness(int thickness)
    {
        this.thickness = thickness;
        return this;
    }

    public String getEdgebinding()
    {
        return edgebinding;
    }

    public CarcassPanel setEdgebinding(String edgebinding)
    {
        this.edgebinding = edgebinding;
        return this;
    }

    public double getArea()
    {
        return area;
    }

    public CarcassPanel setArea(double area)
    {
        this.area = area;
        return this;
    }

    public double getCost(RateCard rateCard)
    {
        if (rateCard == null) return 0;
        return this.getArea() * rateCard.getRateByThickness(this.getThickness());
    }

    public double getCost(RateCard rateCard, ShutterFinish finish)
    {
        if (rateCard == null) return 0;
        return this.getCuttingArea(finish) * rateCard.getRateByThickness(this.getThickness());
    }

    public double getCuttingArea(ShutterFinish finish)
    {
        return (this.getLength() - finish.getCuttingOffset()) * (this.getBreadth() - finish.getCuttingOffset()) * SQMM2SQFT;
    }

    public String getDimesions()
    {
        return this.getLength() + " X " + this.getBreadth() + " X " + this.getThickness();
    }

    public boolean isLeftPanel()
    {
        return TYPE_LEFT.equals(this.type);
    }

    public boolean isRightPanel()
    {
        return TYPE_RIGHT.equals(this.type);
    }

    public boolean isBottomPanel()
    {
        return TYPE_BOTTOM.equals(this.type);
    }
}
