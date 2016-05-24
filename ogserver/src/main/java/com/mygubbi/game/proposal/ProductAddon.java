package com.mygubbi.game.proposal;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * Created by Sunil on 26-04-2016. May not be needed as we don't process the on the server or maybe for the report purpose.
 */
public class ProductAddon extends JsonObject
{
    public static final String ACCESSORY_TYPE = "A";
    public static final String APPLIANCE_TYPE = "P";
    public static final String COUNTERTOP_TYPE = "T";
    public static final String SERVICE_TYPE = "S";

    private static String SEQ = "seq";
    private static String TYPE = "type";
    private static String TITLE = "title";
    private static String DESCRIPTION = "desc";
    private static String IMAGE = "image";
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

    public String getType()
    {
        return this.getString(TYPE);
    }

    public boolean isAccessory()
    {
        return ACCESSORY_TYPE.equals(this.getString(TYPE));
    }

    public boolean isAppliance()
    {
        return APPLIANCE_TYPE.equals(this.getString(TYPE));
    }

    public boolean isCounterTop()
    {
        return COUNTERTOP_TYPE.equals(this.getString(TYPE));
    }

    public boolean isService()
    {
        return SERVICE_TYPE.equals(this.getString(TYPE));
    }

    public String getTitle()
    {
        return this.getString(TITLE);
    }
}

