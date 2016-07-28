package com.mygubbi.game.proposal.model;

import io.vertx.core.json.JsonObject;

/**
 * Created by test on 26-05-2016.
 */
public class Module
{
    private static final double SQMM2SQFT = 0.0000107639;

    private static final String CODE = "code";
    private static final String WIDTH = "width";
    private static final String DEPTH = "depth";
    private static final String HEIGHT = "height";
    private static final String DESCRIPTION = "description";
    private static final String IMAGE_PATH = "imagePath";

    private String code;
    private String description;
    private String imagePath;
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
        this.setDescription(json.getString(DESCRIPTION));
        this.setImagePath(json.getString(IMAGE_PATH));
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

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDimension()
    {
        return this.getWidth() + " X " + this.getDepth() + " X " + this.getHeight();
    }

    public String getImagePath()
    {
        return imagePath;
    }

    public void setImagePath(String imagePath)
    {
        this.imagePath = imagePath;
    }

    public double getLargestAreaOfModuleInSft()
    {
        double h = this.getHeight();
        double w = this.getWidth();
        double d = this.getDepth();

        double t1 = 0;
        double t2 = 0;

        if (h > w)
        {
            t1 = h;
            t2 = w;
        }
        else
        {
            t1 = w;
            t2 = h;
        }

        if (d > t2)
        {
            t2 = d;
        }

        return t1 * t2 * SQMM2SQFT;
    }


}


