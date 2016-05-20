package com.mygubbi.game.proposal.model;

import io.vertx.core.json.JsonObject;

/**
 * Created by test on 17-05-2016.
 */
public class ShutterPanel
{
    public static final double SQMM2SQFT = 0.0000107639;
    private String code;
    private String title;
    private int length;
    private int breadth;
    private int thickness;
    private String edgebinding;
    private int quantity;

    public static ShutterPanel fromJson(JsonObject json)
    {
        return new ShutterPanel().setCode(json.getString("code")).setTitle(json.getString("title"))
                .setLength(json.getInteger("plength")).setBreadth(json.getInteger("breadth"))
                .setThickness(json.getInteger("thickness")).setEdgebinding(json.getString("edgebinding"))
                .setQuantity(json.getInteger("quantity"));
    }

    public String getCode()
    {
        return code;
    }

    public ShutterPanel setCode(String code)
    {
        this.code = code;
        return this;
    }

    public String getTitle()
    {
        return title;
    }

    public ShutterPanel setTitle(String title)
    {
        this.title = title;
        return this;
    }

    public int getLength()
    {
        return length;
    }

    public ShutterPanel setLength(int length)
    {
        this.length = length;
        return this;
    }

    public int getBreadth()
    {
        return breadth;
    }

    public ShutterPanel setBreadth(int breadth)
    {
        this.breadth = breadth;
        return this;
    }

    public int getThickness()
    {
        return thickness;
    }

    public ShutterPanel setThickness(int thickness)
    {
        this.thickness = thickness;
        return this;
    }

    public String getEdgebinding()
    {
        return edgebinding;
    }

    public ShutterPanel setEdgebinding(String edgebinding)
    {
        this.edgebinding = edgebinding;
        return this;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public ShutterPanel setQuantity(int quantity)
    {
        this.quantity = quantity;
        return this;
    }

    public double getCost(RateCard rateCard)
    {
        if (rateCard == null) return 0;
        return this.getCuttingArea() * rateCard.getRateByThickness(this.getThickness());
    }

    private double getCuttingArea()
    {
        return this.getLength() * this.getBreadth() * SQMM2SQFT; //TODO: Reduce the lenght and breadth by the Finish type
    }
}
