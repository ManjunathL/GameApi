package com.mygubbi.game.proposal;

import io.vertx.core.json.JsonObject;

/**
 * Created by Sunil on 27-04-2016.
 */

public class KDMaxModule extends JsonObject
{
    public String getUnit()
    {
        return this.getString("unit");
    }

    public String getCode()
    {
        return this.getString("code");
    }

    public String getFinish()
    {
        return this.getString("finish");
    }

    public String getColor()
    {
        return this.getString("color");
    }

    public int getQuantity()
    {
        return this.getInteger("qty");
    }

    public String getUom()
    {
        return this.getString("uom");
    }

    public String getRemarks()
    {
        return this.getString("remarks");
    }

    public KDMaxModule setUnit(String unit)
    {
        this.put("unit", unit);
        return this;
    }

    public KDMaxModule setCode(String code)
    {
        this.put("code", code);
        return this;
    }

    public KDMaxModule setFinish(String finish)
    {
        this.put("finish", finish);
        return this;
    }

    public KDMaxModule setColor(String color)
    {
        this.put("color", color);
        return this;
    }

    public KDMaxModule setQuantity(int quantity)
    {
        this.put("qty", quantity);
        return this;
    }

    public KDMaxModule setUom(String uom)
    {
        this.put("uom", uom);
        return this;
    }

    public KDMaxModule setRemarks(String remarks)
    {
        this.put("remarks", remarks);
        return this;
    }

    public String getName()
    {
        return this.getString("name");
    }

    public KDMaxModule setName(String name)
    {
        this.put("name", name);
        return this;
    }

    public int getWidth()
    {
        return this.getInteger("width");
    }

    public KDMaxModule setWidth(int width)
    {
        this.put("width", width);
        return this;
    }

    public int getDepth()
    {
        return this.getInteger("depth");
    }

    public KDMaxModule setDepth(int depth)
    {
        this.put("depth", depth);
        return this;
    }

    public int getHeight()
    {
        return this.getInteger("height");
    }

    public KDMaxModule setHeight(int height)
    {
        this.put("height", height);
        return this;
    }

    @Override
    public String toString()
    {
        return "KDMaxModule{" +
                "unit='" + this.getUnit() + '\'' +
                ", name='" + this.getName() + '\'' +
                ", code='" + this.getCode() + '\'' +
                ", finish='" + this.getFinish() + '\'' +
                ", color='" + this.getColor() + '\'' +
                ", quantity=" + this.getQuantity() +
                ", uom='" + this.getUom() + '\'' +
                ", remarks='" + this.getRemarks() + '\'' +
                ", width=" + this.getWidth() +
                ", depth=" + this.getDepth()+
                ", height=" + this.getHeight() +
                '}';

    }


}
