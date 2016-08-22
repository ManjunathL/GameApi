package com.mygubbi.game.proposal;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Collections;
import java.util.List;

/**
 * Created by Sunil on 27-04-2016.
 */

public class ProductModule extends JsonObject
{
    private static final String ACCESSORY_UNIT = "Accessory";

    public static String MODULE_MAPPED = "m";
    public static String MODULE_NOT_MAPPED = "n";

    private static final String MAPPED = "importStatus";
    private static final String UNIT = "unitType";
    private static final String SEQ = "seq";
    private static final String MODULE_SEQUENCE = "moduleSequence";

    private static final String WIDTH = "width";
    private static final String DEPTH = "depth";
    private static final String HEIGHT = "height";
    private static final String DIMENSION = "dimension";

    private static final String EXTCODE = "extCode";
    private static final String MGCODE = "mgCode";
    private static final String IMAGE_PATH = "imagePath";

    private static final String CARCASS_CODE = "carcassCode";
    private static final String FINISH_CODE = "finishCode";
    private static final String FINISH_TYPE = "finishTypeCode";
    private static final String COLOR_CODE = "colorCode";
    private static final String AMOUNT = "amount";
    private static final String EXPOSED_SIDES = "expSides"; //None, Left, Right, Both
    private static final String EXPOSED_BOTTOM = "expBottom"; //Boolean Yes or No

    private static final String LEFT_EXPOSED = "Left";
    private static final String NONE_EXPOSED = "None";
    private static final String RIGHT_EXPOSED = "Right";
    private static final String BOTH_EXPOSED = "Both";

    private static final String REMARKS = "remarks";
    private static final String DESCRIPTION = "description";
    private static final String ACCPACKS = "accessoryPacks";

    public ProductModule()
    {

    }

    public ProductModule(JsonObject json)
    {
        super(json.getMap());
        this.setAccessoryPacks();
    }

    public int getModuleSequence() {
        return this.getInteger(MODULE_SEQUENCE);
    }

    public String getUnit()
    {
        return this.getString(UNIT);
    }

    public String getExternalCode()
    {
        return this.getString(EXTCODE);
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

    public String getColorCode()
    {
        return this.getString(COLOR_CODE);
    }

    public double getAmount()
    {
        return this.getDouble(AMOUNT);
    }

    public String getRemarks()
    {
        return this.getString(REMARKS);
    }

    public String getMGCode()
    {
        return this.getString(MGCODE);
    }

    public String getImagePath()
    {
        return this.getString(IMAGE_PATH);
    }

    public String getDimension()
    {
        return this.getString(DIMENSION);
    }

    public String getMapped()
    {
        return this.getString(MAPPED);
    }

    public String getExposedSides()
    {
        return this.getString(EXPOSED_SIDES);
    }

    public boolean hasExposedBottom()
    {
        if (this.containsKey(EXPOSED_BOTTOM)) return "Yes".equals(this.getString(EXPOSED_BOTTOM));
        return false;
    }

    public int getSequence()
    {
        return this.getInteger(SEQ);
    }

    public boolean isLeftExposed()
    {
        return LEFT_EXPOSED.equals(this.getExposedSides()) || BOTH_EXPOSED.equals(this.getExposedSides());
    }

    public boolean isRightExposed()
    {
        return RIGHT_EXPOSED.equals(this.getExposedSides()) || BOTH_EXPOSED.equals(this.getExposedSides());
    }

    public ProductModule setUnit(String unit)
    {
        this.put(UNIT, unit);
        return this;
    }

    public ProductModule setExternalCode(String code)
    {
        this.put(EXTCODE, code);
        return this;
    }

    public ProductModule setMGCode(String code)
    {
        this.put(MGCODE, code);
        return this;
    }

    public ProductModule setSequence(int seq)
    {
        this.put(SEQ, seq);
        return this;
    }

    public ProductModule setModuleSequence(int moduleSequence)
    {
        this.put(MODULE_SEQUENCE, moduleSequence);
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

    public ProductModule setDimension(String dimension)
    {
        this.put(DIMENSION, dimension);
        return this;
    }

    public ProductModule setCarcassCode(String code)
    {
        this.put(CARCASS_CODE, code);
        return this;
    }

    public ProductModule setFinishCode(String code)
    {
        this.put(FINISH_CODE, code);
        return this;
    }

    public ProductModule setFinishType(String code)
    {
        this.put(FINISH_TYPE, code);
        return this;
    }

    public ProductModule setImagePath(String path)
    {
        this.put(IMAGE_PATH, path);
        return this;
    }

    public ProductModule setColorCode(String code)
    {
        this.put(COLOR_CODE, code);
        return this;
    }

    public ProductModule setRemarks(String remarks)
    {
        this.put(REMARKS, remarks);
        return this;
    }

    public ProductModule setDescription(String description)
    {
        this.put(DESCRIPTION, description);
        return this;
    }

    public ProductModule setMappedFlag(String flag)
    {
        this.put(MAPPED, flag);
        return this;
    }

    private void setAccessoryPacks()
    {
        if (this.containsKey(ACCPACKS))
        {
            JsonArray accPacksJson = this.getJsonArray(ACCPACKS);
            JsonArray accPacks = new JsonArray();
            for (int i=0; i < accPacksJson.size(); i++)
            {
                accPacks.add(new ModuleAccessoryPack(accPacksJson.getJsonObject(i)));
            }
            this.put(ACCPACKS, accPacks);
        }
    }

    public List<ModuleAccessoryPack> getAccessoryPacks()
    {
        if (this.containsKey(ACCPACKS)) return this.getJsonArray(ACCPACKS).getList();
        return Collections.EMPTY_LIST;
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

    @Override
    public String toString()
    {
        return "ProductModule{" +
                "seq='" + this.getSequence() + '\'' +
                "unit='" + this.getUnit() + '\'' +
                ", extCode='" + this.getExternalCode() + '\'' +
                ", code='" + this.getMGCode() + '\'' +
                ", finish='" + this.getFinishCode() + '\'' +
                ", color='" + this.getColorCode() + '\'' +
                ", remarks='" + this.getRemarks() + '\'' +
                '}';

    }

    public boolean isAccessoryUnit()
    {
        return ACCESSORY_UNIT.equals(this.getUnit());
    }
}
