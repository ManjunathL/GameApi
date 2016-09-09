package com.mygubbi.game.proposal.model;

import io.vertx.core.json.JsonObject;

/**
 * Created by test on 17-05-2016.
 */
public class ModuleComponent implements IModuleComponent
{
    private String moduleCode;
    private String type;
    private String quantityFlag;
    private String quantityFormula;
    private String componentCode;
    private double quantity;

    public static ModuleComponent fromJson(JsonObject json)
    {
        ModuleComponent component = new ModuleComponent();
        component.setModuleCode(json.getString("modulecode"));
        component.setType(json.getString("comptype"));
        component.setComponentCode(json.getString("compcode"));
        component.setQuantity(json.getDouble("quantity"));
        component.setQuantityFlag(json.getString("quantityFlag"));
        component.setQuantityFormula(json.getString("quantityFormula"));
        return component;
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

    public boolean isToBeResolved()
    {
        return RESOLVED_TYPE.equals(this.getType());
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

    public double getQuantity()
    {
        return quantity;
    }

    @Override
    public String getQuantityFormula()
    {
        return this.quantityFormula;
    }

    @Override
    public boolean isFixedQuantity()
    {
        return this.getQuantityFlag() == null || QUANTITY_FIXED.equals(this.getQuantityFlag());
    }

    @Override
    public boolean isCalculatedQuantity()
    {
        return QUANTITY_CALCULATED.equals(this.getQuantityFlag());
    }

    public ModuleComponent setQuantity(double quantity)
    {
        this.quantity = quantity;
        return this;
    }

    public String getQuantityFlag()
    {
        return quantityFlag;
    }

    public ModuleComponent setQuantityFlag(String quantityFlag)
    {
        this.quantityFlag = quantityFlag;
        return this;
    }

    public ModuleComponent setQuantityFormula(String quantityFormula)
    {
        this.quantityFormula = quantityFormula;
        return this;
    }
}
