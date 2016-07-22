package com.mygubbi.game.proposal.model;

import io.vertx.core.json.JsonObject;

/**
 * Created by test on 17-05-2016.
 */
public class ModuleComponent extends AbstractModuleComponent
{

    private String moduleCode;

    public static ModuleComponent fromJson(JsonObject json)
    {
        ModuleComponent component = new ModuleComponent();
        component.setModuleCode(json.getString("modulecode"));
        component.setType(json.getString("comptype"));
        component.setComponentCode(json.getString("compcode"));
        component.setQuantity(json.getDouble("quantity"));
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


}
