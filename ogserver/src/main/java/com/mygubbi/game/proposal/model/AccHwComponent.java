package com.mygubbi.game.proposal.model;

import io.vertx.core.json.JsonObject;

/**
 * Created by test on 17-05-2016.
 */
public class AccHwComponent
{
    private String code;
    private String title;
    private String makeType;
    private String make;
    private String imageUrl;
    private double price;

    public static AccHwComponent fromJson(JsonObject json)
    {
        return new AccHwComponent().setCode(json.getString("code")).setTitle(json.getString("title"))
                .setMakeType(json.getString("makeType")).setMake(json.getString("make"))
                .setImageUrl(json.getString("imagePath")).setPrice(json.getDouble("price"));
    }

    public String getMake()
    {
        return make;
    }

    public AccHwComponent setMake(String make)
    {
        this.make = make;
        return this;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }

    public AccHwComponent setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
        return this;
    }


    public String getCode()
    {
        return code;
    }

    public AccHwComponent setCode(String code)
    {
        this.code = code;
        return this;
    }

    public String getTitle()
    {
        return title;
    }

    public AccHwComponent setTitle(String title)
    {
        this.title = title;
        return this;
    }

    public String getMakeType()
    {
        return makeType;
    }

    public AccHwComponent setMakeType(String makeType)
    {
        this.makeType = makeType;
        return this;
    }

    public double getPrice()
    {
        return price;
    }

    public AccHwComponent setPrice(double price)
    {
        this.price = price;
        return this;
    }

    public String getCodeAndMakeType()
    {
        return getCodeAndMakeType(this.getCode(), this.getMakeType());
    }

    //Not the best way to make keys, but the overhead of another new Key object is not worth at this time
    public static String getCodeAndMakeType(String code, String makeType)
    {
        return code + ":" + makeType;
    }
}
