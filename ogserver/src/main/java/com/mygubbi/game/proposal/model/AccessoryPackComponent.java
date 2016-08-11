package com.mygubbi.game.proposal.model;

import io.vertx.core.json.JsonObject;

/**
 * Created by Sunil on 26-04-2016.
 */
public class AccessoryPackComponent extends JsonObject implements IModuleComponent
{
    private static final String ACCESSORY_TYPE = "A";

    private static final String APCODE = "apcode";
    private static final String CODE = "code";
    private static final String TYPE  = "type";
    private static final String QUANTITY = "qty";

    public AccessoryPackComponent()
    {

    }

    public AccessoryPackComponent(JsonObject data)
    {
        super(data.getMap());
    }

    public String getComponentCode()
    {
        return this.getString(CODE);
    }

    public String getAccessoryPackCode()
    {
        return this.getString(APCODE);
    }

    public String getType()
    {
        return this.getString(TYPE);
    }

    public double getQuantity()
    {
        return this.getDouble(QUANTITY);
    }

    public boolean isCarcass()
    {
        return CARCASS_TYPE.equals(this.getType());
    }

    public boolean isShutter()
    {
        return SHUTTER_TYPE.equals(this.getType());
    }

    public boolean isAccessory()
    {
        return ACCESSORY_TYPE.equals(this.getType());
    }

    public boolean isHardware()
    {
        return HARDWARE_TYPE.equals(this.getType());
    }

}

