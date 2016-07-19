package com.mygubbi.game.proposal.model;

/**
 * Created by test on 18-07-2016.
 */
public class AbstractModuleComponent implements IModuleComponent
{
    private String type;
    private String componentCode;
    private double quantity;

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

    public String getType()
    {
        return type;
    }

    public AbstractModuleComponent setType(String type)
    {
        this.type = type;
        return this;
    }

    public String getComponentCode()
    {
        return componentCode;
    }

    public AbstractModuleComponent setComponentCode(String componentCode)
    {
        this.componentCode = componentCode;
        return this;
    }

    public double getQuantity()
    {
        return quantity;
    }

    public AbstractModuleComponent setQuantity(double quantity)
    {
        this.quantity = quantity;
        return this;
    }
}
