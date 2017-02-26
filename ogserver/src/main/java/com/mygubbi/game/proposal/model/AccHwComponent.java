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
    private String uom;
    private String catalogCode;


    private String category;

    public static AccHwComponent fromJson(JsonObject json)
    {
        return new AccHwComponent().setCode(json.getString("code")).setTitle(json.getString("title"))
                .setMakeType(json.getString("makeType")).setMake(json.getString("make"))
                .setImageUrl(json.getString("imagePath")).setPrice(json.getDouble("price")).setUom(json.getString("uom"))
                .setCatalogCode(json.getString("catalogCode")).setCategory((json.getString("category")));
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

    public String getUom()
    {
        return uom;
    }

    public AccHwComponent setUom(String uom)
    {
        this.uom = uom;
        return this;
    }
    public String getCatalogCode()
    {
        return catalogCode;
    }

    public AccHwComponent setCatalogCode(String catalogCode)
    {
        this.catalogCode = catalogCode;
        return this;
    }
    public String getCategory() {
        return category;
    }

    public  AccHwComponent  setCategory(String category) {
        this.category = category;
        return this;
    }

    @Override
    public String toString() {
        return "AccHwComponent{" +
                "code='" + code + '\'' +
                ", title='" + title + '\'' +
                ", makeType='" + makeType + '\'' +
                ", make='" + make + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", price=" + price +
                ", uom='" + uom + '\'' +
                ", catalogCode='" + catalogCode + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
