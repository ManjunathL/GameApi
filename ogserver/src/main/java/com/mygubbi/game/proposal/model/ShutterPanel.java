package com.mygubbi.game.proposal.model;

import io.vertx.core.json.JsonObject;

/**
 * Created by test on 17-05-2016.
 */
public class ShutterPanel
{
    private String code;
    private String title;
    private int length;
    private int breadth;
    private int height;
    private String edgebinding;
    private int quantity;

    public static ShutterPanel fromJson(JsonObject json)
    {
        return new ShutterPanel().setCode(json.getString("code")).setTitle(json.getString("title"))
                .setLength(json.getInteger("plength")).setBreadth(json.getInteger("breadth"))
                .setHeight(json.getInteger("height")).setEdgebinding(json.getString("edgebinding"))
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

    public int getHeight()
    {
        return height;
    }

    public ShutterPanel setHeight(int height)
    {
        this.height = height;
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
        return this.getCuttingArea() * rateCard.getRateByThickness(this.getCuttingThickness());
    }

    private int getCuttingThickness()
    {
        return this.getBreadth() - 2;
    }

    private double getCuttingArea()
    {
        return this.getLength() * this.getHeight();
    }
}
