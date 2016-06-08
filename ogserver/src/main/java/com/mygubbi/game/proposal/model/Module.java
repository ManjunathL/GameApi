package com.mygubbi.game.proposal.model;

import io.vertx.core.json.JsonObject;

/**
 * Created by test on 26-05-2016.
 */
public class Module
{
    private static final String CODE = "code";
    private static final String WIDTH = "width";
    private static final String DEPTH = "depth";
    private static final String HEIGHT = "height";

    private String code;
    private int height;
    private int depth;
    private int width;

    public Module()
    {

    }

    public Module(JsonObject json)
    {
        this.setCode(json.getString(CODE));
        this.setHeight(json.getInteger(HEIGHT));
        this.setDepth(json.getInteger(DEPTH));
        this.setWidth(json.getInteger(WIDTH));
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public int getDepth()
    {
        return depth;
    }

    public void setDepth(int depth)
    {
        this.depth = depth;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }
}

