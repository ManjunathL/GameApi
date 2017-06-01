package com.mygubbi.game.proposal;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;

/**
 * Created by Sunil on 26-04-2016.
 */
public class ProductLineItem extends JsonObject
{
    public static final String CUSTOMIZED_PRODUCT = "CUSTOMIZED";
    public static final String CATALOGUE_PRODUCT = "CATALOGUE";

    private static final String ID = "id";
    private static final String ACTIVE = "active";
    private static final String PROPOSALID = "proposalId";
    private static final String TITLE = "title";
    private static final String SEQ = "seq";
    private static final String TYPE = "type"; //Customized, Catalogue
    private static final String ROOM_CODE = "roomCode"; //Kitchen, Bedroom 1
    private static final String PRODUCT_CATEGORY = "productCategoryCode"; //Kitchen, Living Room
    private static final String CATALOGUE_ID = "catalogueId";
    private static final String CATALOGUE_NAME = "catalogueName";
    private static final String MAKE_TYPE = "makeTypeCode";
    private static final String BASE_CARCASS_CODE = "baseCarcassCode";
    private static final String WALL_CARCASS_CODE = "wallCarcassCode";
    private static final String FINISH_TYPE = "finishTypeCode";
    private static final String FINISH_CODE = "finishCode";
    private static final String DESIGN_CODE = "designCode";
    private static final String DIMENSION = "dimension";
    private static final String QUANTITY = "quantity";
    private static final String AMOUNT = "amount";
    private static final String QUOTE_FILE_PATH = "quoteFilePath";
    private static final String MODULES = "modules";
    private static final String ADDONS = "addons";
    private static final String GLASS = "glass";
    private static final String CREATED_ON = "createdOn";
    private static final String CREATED_BY = "createdBy";
    private static final String UPDATED_ON = "updatedOn";
    private static final String UPDATED_BY = "updatedBy";
    private static final String HANDLE_TYPE = "handleType";
    private static final String HANDLE_FINISH = "handleFinish";
    private static final String KNOB_TYPE = "konbType";
    private static final String KNOB_FINISH = "knobFinish";

    public static final String BASE_UNIT_TYPE = "Base unit";

    public ProductLineItem()
    {

    }

    public  String getProductCategory() {
        return this.getString(PRODUCT_CATEGORY);
    }

    public ProductLineItem(JsonObject data)
    {
        super(data.getMap());
        this.setModules();
        this.setAddons();
    }

    public String getTitle()
    {
        return this.getString(TITLE);
    }

    public String getType()
    {
        return this.getString(TYPE);
    }

    public  String getRoomCode() {
        return this.getString(ROOM_CODE);
    }

    public String getKdMaxFile()
    {
        return this.getString(QUOTE_FILE_PATH);
    }

    public String getBaseCarcassCode()
    {
        return this.getString(BASE_CARCASS_CODE);
    }

    public String getWallCarcassCode()
    {
        return this.getString(WALL_CARCASS_CODE);
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

    public String getDesignCode()
    {
        return this.getString(DESIGN_CODE);
    }

    public int getQuantity()
    {
        return this.getInteger(QUANTITY);
    }

    public double getAmount()
    {
        return this.getDouble(AMOUNT);
    }

    public String getDimension()
    {
        return this.getString(DIMENSION);
    }

    public String getName()
    {
        return this.getString(CATALOGUE_NAME);
    }

    public String getHandleType() {
        return this.getString(HANDLE_TYPE);
    }

    public String getHandleFinish() {
        return this.getString(HANDLE_FINISH);
    }

    public String getKnobType() {
        return this.getString(KNOB_TYPE);
    }

    public String getKnobFinish() {
        return this.getString(KNOB_FINISH);
    }

    public String getGlass()
    {
        return this.getString(GLASS);
    }

    public ProductLineItem setAmount(double amount) {
        this.put(AMOUNT, amount);
        return this;
    }

    public ProductLineItem addModule(ProductModule module)
    {
        if (!this.containsKey(MODULES))
        {
            this.put(MODULES, new JsonArray());
        }
        JsonArray mgModules = this.getJsonArray(MODULES);
        String carcassCode = BASE_UNIT_TYPE.equals(module.getUnit()) ? this.getBaseCarcassCode() : this.getWallCarcassCode();
        module.setCarcassCode(carcassCode)
                .setFinishCode(this.getFinishCode())
                .setFinishType(this.getFinishType());
        mgModules.add(module);
        this.put(MODULES, mgModules);
        return this;
    }

    private ProductLineItem addAddOn(ProductAddon addon)
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
        JsonArray productAddons = new JsonArray();
        if (this.containsKey(ADDONS) && (this.getValue(ADDONS) instanceof JsonArray))
        {
            JsonArray addonJsons = this.getJsonArray(ADDONS);
            for (int i=0; i < addonJsons.size(); i++)
            {
                productAddons.add(new ProductAddon(addonJsons.getJsonObject(i)));
            }
        }
        this.put(ADDONS, productAddons);
    }

    public List<ProductAddon> getAddons()
    {
        return this.getJsonArray(ADDONS).getList();
    }

    public int getId()
    {
        return this.containsKey(ID) ? this.getInteger(ID) : 0;
    }

    public List<ProductModule> getModules()
    {
        return this.getJsonArray(MODULES).getList();
    }

    public Double getRate()
    {
        if (this.getQuantity() == 0) return 0.0;
        return this.getAmount() / this.getQuantity();
    }
}

