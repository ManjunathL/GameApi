package com.mygubbi.game.proposal.model;

import io.vertx.core.json.JsonObject;

/**
 * Created by test on 17-05-2016.
 */
public class CarcassPanel
{
    //code, title, plength, breadth, thickness, edgebinding, area
    private String code;
    private String title;
    private int length;
    private int breadth;
    private int thickness;
    private String edgebinding;
    private double area;

    public static CarcassPanel fromJson(JsonObject json)
    {
        return new CarcassPanel().setCode(json.getString("code")).setTitle(json.getString("title"))
                .setLength(json.getInteger("plength")).setBreadth(json.getInteger("breadth"))
                .setThickness(json.getInteger("thickness")).setEdgebinding(json.getString("edgebinding"))
                .setArea(json.getDouble("area"));
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

    public String getDimesions()
    {
        return this.getLength() + " X " + this.getBreadth() + " X " + this.getThickness();
    }
}
