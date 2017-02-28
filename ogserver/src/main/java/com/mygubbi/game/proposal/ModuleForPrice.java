package com.mygubbi.game.proposal;

import io.vertx.core.json.JsonObject;

import java.sql.Date;

/**
 * Created by Sunil on 27-04-2016.
 */

public class ModuleForPrice extends JsonObject
{
    private static final double SQMM2SQFT = 0.0000107639;

    private static final String PRICE_DATE = "priceDate";
    private static final String CITY = "city";
    private static final String MODULE = "module";

    public ModuleForPrice(JsonObject json)
    {
        super(json.getMap());
        this.setModule();
    }

    public Date getPriceDate()
    {
        return Date.valueOf(this.getString(PRICE_DATE));
    }

    public String getCity()
    {
        return this.getString(CITY);
    }


    public ModuleForPrice setCreateDate(Date createDate)
    {
        this.put(PRICE_DATE,createDate);
        return this;
    }

    public ModuleForPrice setCity(String city)
    {
        this.put(CITY, city);
        return this;
    }


    private void setModule()
    {
        if (this.containsKey(MODULE))
        {
            this.put(MODULE, new ProductModule(this.getJsonObject(MODULE)));
        }
    }

    public ProductModule getModule()
    {
        if (this.containsKey(MODULE)) return (ProductModule) this.getJsonObject(MODULE);
        return null;
    }



}
