package com.mygubbi.game.proposal.model;


import io.vertx.core.json.JsonObject;
import org.json.JSONObject;

/**
 * Created by Shruthi on 9/21/2017.
 */
public class ColorMaster extends JSONObject
{
    private static final String COLORGROUP_CODE="colorGroupCode";
    private static final String CODE="code";
    private static final String IMAGE_PATH="imagePath";
    private static final String PRICE_CODE="priceCode";

    public ColorMaster(JsonObject jsonObject)
    {
        super(jsonObject.getMap());
    }

    public String getColorgroupCode()
    {
        return this.getString(COLORGROUP_CODE);
    }

    public String getCode()
    {
        return this.getString(CODE);
    }

    public String getImagePath()
    {
        return  this.getString(IMAGE_PATH);
    }
    public String getPriceCode()
    {
        return  this.getString(PRICE_CODE);
    }

    public ColorMaster setColorGroupCode(String colorGroupCode)
    {
        put(COLORGROUP_CODE,colorGroupCode);
        return this;
    }

    public ColorMaster setCode(String code)
    {
        put(CODE,code);
        return this;
    }
    public ColorMaster setImagePath(String imagePath)
    {
        put(IMAGE_PATH,imagePath);
        return this;
    }

    public ColorMaster setPriceCode(String priceCode)
    {
        put(PRICE_CODE,priceCode);
        return this;
    }

}
