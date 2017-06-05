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
    private static final String HANDLE_TYPE = "handleType";
    private static final String PRODUCT = "product";

    public ModuleForPrice(JsonObject json)
    {
        super(json.getMap());
        this.setModule();
        this.setProduct();
    }

    public Date getPriceDate()
    {
        return Date.valueOf(this.getString(PRICE_DATE));
    }

    public String getCity()
    {
        return this.getString(CITY);
    }

    public String getHandleType() {
        return this.getString(HANDLE_TYPE);
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

    private void setProduct()
    {
        if (this.containsKey(PRODUCT))
        {
            this.put(PRODUCT, new ProductLineItem(this.getJsonObject(PRODUCT)));
        }
    }

    public ProductLineItem getProduct()
    {
        if (this.containsKey(PRODUCT)) return (ProductLineItem) this.getJsonObject(PRODUCT);
        return null;
    }

    public ProductModule getModule()
    {
        if (this.containsKey(MODULE)) return (ProductModule) this.getJsonObject(MODULE);
        return null;
    }



}
