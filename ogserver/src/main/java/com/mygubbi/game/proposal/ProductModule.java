package com.mygubbi.game.proposal;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * Created by Sunil on 27-04-2016.
 */

public class ProductModule extends JsonObject
{
    private static final String UNIT = "unit";
    private static final String KDMCODE = "kdmcode";
    private static final String KDMDEFCODE = "kdmdefcode";
    private static final String SEQ = "seq";
    private static final String CARCASS_ID = "carcass_id";
    private static final String CARCASS_NAME = "carcass_name";
    private static final String SHUTTER_ID = "shutter_id";
    private static final String SHUTTER_NAME = "shutter_name";
    private static final String FINISH_ID = "finish_id";
    private static final String FINISH_NAME = "finish_name";
    private static final String COLOR_ID = "color_id";
    private static final String COLOR_NAME = "color_name";
    private static final String MAKE_ID = "make_id";
    private static final String MAKE_NAME = "make_name";
    private static final String MGCODE = "mgcode";
    private static final String MGNAME = "mgname";
    private static final String MGIMAGE = "mgimage";
    private static final String MGDIMENSION = "mgdim";
    private static final String UOM = "uom";
    private static final String QUANTITY = "quantity";
    private static final String AMOUNT = "amount";
    private static final String WIDTH = "width";
    private static final String DEPTH = "depth";
    private static final String HEIGHT = "height";
    private static final String REMARKS = "remarks";
    private static final String NAME = "name";
    private static final String MAPPED = "mapped";
    private static final String MGMODULES = "mgmodules";

    public ProductModule()
    {

    }

    public ProductModule(JsonObject json)
    {
        super(json.getMap());
    }

    public String getUnit()
    {
        return this.getString(UNIT);
    }

    public String getKDMCode()
    {
        return this.getString(KDMCODE);
    }

    public String getKDMDefaultCode()
    {
        return this.getString(KDMDEFCODE);
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

    public int getQuantity()
    {
        return this.getInteger(QUANTITY);
    }

    public int getAmount()
    {
        return this.getInteger(AMOUNT);
    }

    public String getUom()
    {
        return this.getString(UOM);
    }

    public int getWidth()
    {
        return this.getInteger(WIDTH);
    }

    public int getDepth()
    {
        return this.getInteger(DEPTH);
    }

    public int getHeight()
    {
        return this.getInteger(HEIGHT);
    }

    public String getRemarks()
    {
        return this.getString(REMARKS);
    }

    public String getName()
    {
        return this.getString(NAME);
    }

    public String getMGCode()
    {
        return this.getString(MGCODE);
    }

    public String getMGName()
    {
        return this.getString(MGNAME);
    }

    public String getMGImage()
    {
        return this.getString(MGIMAGE);
    }

    public String getMGDimension()
    {
        return this.getString(MGDIMENSION);
    }

    public String getMapped()
    {
        return this.getString(MAPPED);
    }

    public int getSequence()
    {
        return this.getInteger(SEQ);
    }

    public ProductModule setUnit(String unit)
    {
        this.put(UNIT, unit);
        return this;
    }

    public ProductModule setKDMCode(String code)
    {
        this.put(KDMCODE, code);
        return this;
    }

    public ProductModule setDefaultModule(String module)
    {
        this.put(KDMDEFCODE, module);
        return this;
    }

    public ProductModule setSequence(int seq)
    {
        this.put(SEQ, seq);
        return this;
    }

    public ProductModule setCarcassId(String id)
    {
        this.put(CARCASS_ID, id);
        return this;
    }

    public ProductModule setCarcassName(String name)
    {
        this.put(CARCASS_NAME, name);
        return this;
    }

    public ProductModule setShutterId(String id)
    {
        this.put(SHUTTER_ID, id);
        return this;
    }

    public ProductModule setShutterName(String name)
    {
        this.put(SHUTTER_NAME, name);
        return this;
    }

    public ProductModule setFinishId(String finishId)
    {
        this.put(FINISH_ID, finishId);
        return this;
    }

    public ProductModule setFinishName(String finishName)
    {
        this.put(FINISH_ID, finishName);
        return this;
    }

    public ProductModule setMakeId(String id)
    {
        this.put(MAKE_ID, id);
        return this;
    }

    public ProductModule setMakeName(String name)
    {
        this.put(MAKE_NAME, name);
        return this;
    }

    public ProductModule setColorId(String id)
    {
        this.put(COLOR_ID, id);
        return this;
    }

    public ProductModule setColorName(String name)
    {
        this.put(COLOR_NAME, name);
        return this;
    }

    public ProductModule setMGCode(String value)
    {
        this.put(MGCODE, value);
        return this;
    }

    public ProductModule setMGName(String value)
    {
        this.put(MGNAME, value);
        return this;
    }

    public ProductModule setMGImage(String value)
    {
        this.put(MGIMAGE, value);
        return this;
    }

    public ProductModule setMGDimension(String value)
    {
        this.put(MGDIMENSION, value);
        return this;
    }

    public ProductModule setUom(String uom)
    {
        this.put(UOM, uom);
        return this;
    }

    public ProductModule setQuantity(int quantity)
    {
        this.put(QUANTITY, quantity);
        return this;
    }

    public ProductModule setAmount(int amount)
    {
        this.put(AMOUNT, amount);
        return this;
    }

    public ProductModule setRemarks(String remarks)
    {
        this.put(REMARKS, remarks);
        return this;
    }

    public ProductModule setName(String name)
    {
        this.put(NAME, name);
        return this;
    }

    public ProductModule setWidth(int width)
    {
        this.put(WIDTH, width);
        return this;
    }

    public ProductModule setDepth(int depth)
    {
        this.put(DEPTH, depth);
        return this;
    }

    public ProductModule setHeight(int height)
    {
        this.put(HEIGHT, height);
        return this;
    }

    public ProductModule setMappedFlag(String flag)
    {
        this.put(MAPPED, flag);
        return this;
    }

    public ProductModule addMappedModule(String module, String title, String dimension, String image)
    {
        return this.addMappedModule(new JsonObject().put("code", module).put("title", title).put("dim", dimension).put("image", image));
    }

    public ProductModule addMappedModule(JsonObject module)
    {
        if (!this.containsKey(MGMODULES))
        {
            this.put(MGMODULES, new JsonArray());
        }
        JsonArray mgModules = this.getJsonArray(MGMODULES);
        mgModules.add(module);
        this.put(MGMODULES, mgModules);
        return this;
    }

    @Override
    public String toString()
    {
        return "ProductModule{" +
                "unit='" + this.getUnit() + '\'' +
                ", name='" + this.getName() + '\'' +
                ", code='" + this.getKDMCode() + '\'' +
                ", finish='" + this.getFinishId() + '\'' +
                ", color='" + this.getColorId() + '\'' +
                ", quantity=" + this.getQuantity() +
                ", uom='" + this.getUom() + '\'' +
                ", remarks='" + this.getRemarks() + '\'' +
                ", width=" + this.getWidth() +
                ", depth=" + this.getDepth()+
                ", height=" + this.getHeight() +
                '}';

    }


}
