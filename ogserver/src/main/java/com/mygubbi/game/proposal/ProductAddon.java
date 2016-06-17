package com.mygubbi.game.proposal;

import io.vertx.core.json.JsonObject;

/**
 * Created by Sunil on 26-04-2016. May not be needed as we don't process the on the server or maybe for the report purpose.
 */
public class ProductAddon extends JsonObject
{
    public static final String ACCESSORY_TYPE = "Accessories";
    public static final String APPLIANCE_TYPE = "Appliances";
    public static final String COUNTERTOP_TYPE = "Countertop";
    public static final String SERVICE_TYPE = "Services";
    public static final String LOOSE_FURNITURE_TYPE = "Loose Furniture";

    private static final String SEQ = "seq";
    private static final String CODE = "code";
    private static final String ADDON_CATEGORY_CODE = "addonCategoryCode";
    private static final String PRODUCT_TYPE_CODE = "productTypeCode";
    private static final String BRAND_CODE = "brandCode";
    private static final String CATALOGUE_CODE = "catalogueCode";
    private static final String TITLE = "title";
    private static String QUANTITY = "quantity";
    private static String AMOUNT = "amount";
    private static String RATE = "rate";

    public ProductAddon()
    {

    }

    public ProductAddon(JsonObject data)
    {
        super(data.getMap());
    }

    public double getQuantity()
    {
        return this.getDouble(QUANTITY);
    }

    public double getRate()
    {
        return this.getDouble(RATE);
    }

    public double getAmount()
    {
        return this.getDouble(AMOUNT);
    }

    public String getCategoryCode()
    {
        return this.getString(ADDON_CATEGORY_CODE);
    }

    public String getBrandCode()
    {
        return this.getString(BRAND_CODE);
    }

    public String getCatalogueCode()
    {
        return this.getString(CATALOGUE_CODE);
    }

    public String getUom()
    {
        return "uom";
    }

    public boolean isAccessory()
    {
        return ACCESSORY_TYPE.equals(this.getCategoryCode());
    }

    public boolean isAppliance()
    {
        return APPLIANCE_TYPE.equals(this.getCategoryCode());
    }

    public boolean isCounterTop()
    {
        return COUNTERTOP_TYPE.equals(this.getCategoryCode());
    }

    public boolean isService()
    {
        return SERVICE_TYPE.equals(this.getCategoryCode());
    }

    public String getTitle()
    {
        return this.getString(TITLE);
    }
}

