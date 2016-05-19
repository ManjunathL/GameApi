package com.mygubbi.game.proposal.model;

import io.vertx.core.json.JsonObject;

/**
 * Created by test on 17-05-2016.
 */
public class ModuleComponent
{
    public static final String CARCASS_TYPE = "C";
    public static final String SHUTTER_TYPE = "S";
    public static final String ACCESSORY_TYPE = "A";
    public static final String HARDWARE_TYPE = "H";

    private String moduleCode;
    private String type;
    private String componentCode;
    private int quantity;

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

    public static ModuleComponent fromJson(JsonObject json)
    {
        return new ModuleComponent().setModuleCode(json.getString("modulecode")).setType(json.getString("comptype"))
                .setComponentCode(json.getString("compcode")).setQuantity(json.getInteger("quantity"));
    }

    public String getModuleCode()
    {
        return moduleCode;
    }

    public ModuleComponent setModuleCode(String moduleCode)
    {
        this.moduleCode = moduleCode;
        return this;
    }

    public String getType()
    {
        return type;
    }

    public ModuleComponent setType(String type)
    {
        this.type = type;
        return this;
    }

    public String getComponentCode()
    {
        return componentCode;
    }

    public ModuleComponent setComponentCode(String componentCode)
    {
        this.componentCode = componentCode;
        return this;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public ModuleComponent setQuantity(int quantity)
    {
        this.quantity = quantity;
        return this;
    }


}
