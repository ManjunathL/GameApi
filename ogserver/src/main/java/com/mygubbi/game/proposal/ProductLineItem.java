package com.mygubbi.game.proposal;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * Created by Sunil on 26-04-2016.
 */
public class ProductLineItem extends JsonObject
{

    private static String ID = "id";
    private static String ACTIVE = "active";
    private static String PROPOSALID = "proposalid";
    private static String TITLE = "title";
    private static String TYPE = "type"; //Assembled, Catalogue
    private static String KDMAX_FILE = "kdmax_file";
    private static String ROOM = "room"; //Kitchen, Bedroom 1
    private static String PRODUCT_TYPE = "product_type"; //Kitchen, Living Room
    private static String PRODUCT_ID = "product_id";
    private static String PRODUCT_NAME = "product_name";
    private static String CARCASS_CODE = "carcass_code";
    private static String FINISH_CODE = "finish_code";
    private static String FINISH_TYPE = "finish_type";
    private static String MAKE_TYPE = "make_type";
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

    public String getCarcassCode()
    {
        return this.getString(CARCASS_CODE);
    }

    public String getFinishCode()
    {
        return this.getString(FINISH_CODE);
    }

    public String getFinishType()
    {
        return this.getString(FINISH_TYPE);
    }

    public String getMakeType()
    {
        return this.getString(MAKE_TYPE);
    }

    public ProductLineItem addModule(ProductModule module)
    {
        if (!this.containsKey(MODULES))
        {
            this.put(MODULES, new JsonArray());
        }
        JsonArray mgModules = this.getJsonArray(MODULES);
        module.setCarcassCode(this.getCarcassCode()).setFinishCode(this.getFinishCode())
                .setFinishType(this.getFinishType()).setMakeType(this.getMakeType());
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
        return this.containsKey(ID) ? this.getInteger(ID) : 0;
    }
}

