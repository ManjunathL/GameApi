package com.mygubbi.game.proposal;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * Created by Sunil on 26-04-2016.
 */
public class ProductLineItem extends JsonObject
{
    public static String MAPPED_AT_MODULE = "m";
    public static String MAPPED_AT_DEFAULT = "d";
    public static String NOT_MAPPED = "n";

    private static String ID = "id";
    private static String ACTIVE = "active";
    private static String PROPOSALID = "proposalid";
    private static String TITLE = "title";
    private static String TYPE = "type";
    private static String KDMAX_FILE = "kdmax_file";
    private static String PRODUCT_TYPE = "product_type";
    private static String PRODUCT_ID = "product_id";
    private static String PRODUCT_NAME = "product_name";
    private static String CARCASS_ID = "carcass_id";
    private static String CARCASS_NAME = "carcass_name";
    private static String SHUTTER_ID = "shutter_id";
    private static String SHUTTER_NAME = "shutter_name";
    private static String FINISH_ID = "finish_id";
    private static String FINISH_NAME = "finish_name";
    private static String COLOR_ID = "color_id";
    private static String COLOR_NAME = "color_name";
    private static String MAKE_ID = "make_id";
    private static String MAKE_NAME = "make_name";
    private static String DIMENSION = "dimension";
    private static String QUANTITY = "quantity";
    private static String AMOUNT = "amount";
    private static String NOTES = "notes";
    private static String MODULES = "modules";
    private static String ADDONS = "addons";
    private static String CREATED_ON = "createdon";
    private static String CREATED_BY = "createdby";
    private static String UPDATED_ON = "updatedon";
    private static String UPDATED_BY = "updatedby";

    public ProductLineItem()
    {

    }

    public ProductLineItem(JsonObject data)
    {
        super(data.getMap());
        this.setModules();
        this.setAddons();
    }

    public String getKdMaxFile()
    {
        return this.getString(KDMAX_FILE);
    }

    public String getCarcassId()
    {
        return this.getString(CARCASS_ID);
    }

    public String getCarcassName()
    {
        return this.getString(CARCASS_NAME);
    }

    public String getShutterId()
    {
        return this.getString(SHUTTER_ID);
    }

    public String getShutterName()
    {
        return this.getString(SHUTTER_NAME);
    }

    public String getFinishId()
    {
        return this.getString(FINISH_ID);
    }

    public String getFinishName()
    {
        return this.getString(FINISH_NAME);
    }

    public String getColorId()
    {
        return this.getString(COLOR_ID);
    }

    public String getColorName()
    {
        return this.getString(COLOR_NAME);
    }

    public String getMakeId()
    {
        return this.getString(MAKE_ID);
    }

    public String getMakeName()
    {
        return this.getString(MAKE_NAME);
    }

    public ProductLineItem addModule(ProductModule module)
    {
        if (!this.containsKey(MODULES))
        {
            this.put(MODULES, new JsonArray());
        }
        JsonArray mgModules = this.getJsonArray(MODULES);
        module.setCarcassId(this.getCarcassId()).setCarcassName(this.getCarcassName())
                .setShutterId(this.getShutterId()).setShutterName(this.getShutterName())
                .setFinishId(this.getFinishId()).setFinishName(this.getFinishName())
                .setMakeId(this.getMakeId()).setMakeName(this.getMakeName())
                .setColorId(this.getColorId()).setColorName(this.getColorName());
        mgModules.add(module);
        this.put(MODULES, mgModules);
        return this;
    }

    public ProductLineItem addAddOn(ProductAddon addon)
    {
        if (!this.containsKey(ADDONS))
        {
            this.put(ADDONS, new JsonArray());
        }
        JsonArray addons = this.getJsonArray(ADDONS);
        addons.add(addon);
        this.put(ADDONS, addons);
        return this;
    }

    public void resetModules()
    {
        if (this.containsKey(MODULES))
        {
            JsonArray moduleJsons = this.getJsonArray(MODULES);
            moduleJsons.clear();
            this.put(MODULES, moduleJsons);
        }
        else
        {
            this.put(MODULES, new JsonArray());
        }
    }

    private void setModules()
    {
        if (this.containsKey(MODULES))
        {
            JsonArray moduleJsons = this.getJsonArray(MODULES);
            JsonArray modules = new JsonArray();
            for (int i=0; i < moduleJsons.size(); i++)
            {
                modules.add(new ProductModule(moduleJsons.getJsonObject(i)));
            }
            this.put(MODULES, modules);
        }
    }

    private void setAddons()
    {
        if (this.containsKey(ADDONS))
        {
            JsonArray addonJsons = this.getJsonArray(ADDONS);
            JsonArray productAddons = new JsonArray();
            for (int i=0; i < addonJsons.size(); i++)
            {
                productAddons.add(new ProductAddon(addonJsons.getJsonObject(i)));
            }
            this.put(ADDONS, productAddons);
        }
    }

    public int getId()
    {
        return this.getInteger(ID);
    }
}

