package com.mygubbi.game.proposal;

import com.mygubbi.game.proposal.model.HingePack;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Sunil on 27-04-2016.
 */

public class ProductModule extends JsonObject
{
    private static final double SQMM2SQFT = 0.0000107639;

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
    private static final String MODULE_CATEGORY = "moduleCategory";
    private static final String IMAGE_PATH = "imagePath";

    private static final String CARCASS_CODE = "carcassCode";
    private static final String FINISH_CODE = "finishCode";
    private static final String FINISH_TYPE = "finishTypeCode";
    private static final String COLOR_CODE = "colorCode";
    private static final String AMOUNT = "amount";
    private static final String EXPOSED_SIDES = "expSides"; //None, Left, Right, Both
    private static final String EXPOSED_BOTTOM = "expBottom"; //Boolean Yes or No


    private static final String BOTH_EXPOSED = "Both";
    private static final String NONE_EXPOSED = "None";

    private static final String LEFT_EXPOSED = "exposedLeft";
    private static final String RIGHT_EXPOSED = "exposedRight";
    private static final String TOP_EXPOSED = "exposedTop";
    private static final String BOTTOM_EXPOSED = "exposedBottom";
    private static final String BACK_EXPOSED = "exposedBack";
    private static final String OPEN_UNIT = "exposedOpen";

    private static final String REMARKS = "remarks";
    private static final String DESCRIPTION = "description";
    private static final String ACCPACKS = "accessoryPacks";
    private static final String HINGE_PACK = "hingePack";
    private static final String ACC_PACK_DEFAULT = "accessoryPackDefault";
    private static final String COST_WO_ACCESSORIES = "costWoAccessories";
    private static final String ACCESSORY_FLAG = "accessoryflag";
    private static final String HANDLE_CODE = "handleCode";
    private static final String KNOB_CODE = "knobCode";
    private static final String HANDLE_QUANTITY = "handleQuantity";
    private static final String KNOB_QUANTITY = "knobQuantity";
    private static final String HINGE_CODE = "hingeCode";
    private static final String HINGE_QUANTITY = "hingeQuantity";
    private static final String HANDLE_MANDATORY = "handlePresent";
    private static final String KNOB_MANDATORY = "knobPresent";
    private static final String HINGE_MANDATORY = "hingePresent";
    private static final String PRODUCT_CATEGORY = "productCategory";
    private static final String SQFT_CALCULATION = "sqftCalculation";
    private static final String HANDLE_TYPE = "handleType";
    private static final String HANDLE_FINISH = "handleFinish";
    private static final String KNOB_TYPE = "konbType";
    private static final String KNOB_FINISH = "knobFinish";
    private static final String SHUTTER_DESIGN_CODE = "shutterDesignCode";
    private static final String GOLA_PROFILE_FLAG = "golaProfileFlag";
    private static final String CUSTOM_CHECK = "customCheck";
    private static final String HANDLE_OVERRIDE_FLAG = "handleOverrideFlag";
    private static final String HANDLE_THICKNESS = "handleThickness";


    public ProductModule()
    {

    }

    public ProductModule(JsonObject json)
    {
        super(json.getMap());
        this.setAccessoryPacks();
        this.setHingePacks();
        this.setExposed();
    }

    private void setExposed() {
        if (this.containsKey(EXPOSED_SIDES)){
            if (!this.containsKey(LEFT_EXPOSED))
                this.put(LEFT_EXPOSED,LEFT_EXPOSED.equals(this.getExposedSides()) || BOTH_EXPOSED.equals(this.getExposedSides()));
            if (!this.containsKey(RIGHT_EXPOSED))
                this.put(RIGHT_EXPOSED,RIGHT_EXPOSED.equals(this.getExposedSides()) || BOTH_EXPOSED.equals(this.getExposedSides()));
        }
        if (this.containsKey(EXPOSED_BOTTOM)){
            if (!this.containsKey(BOTTOM_EXPOSED))
                this.put(BOTTOM_EXPOSED,this.getBoolean(EXPOSED_BOTTOM,false));
        }
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

    public String getShutterDesignCode()
    {
        return this.getString(SHUTTER_DESIGN_CODE);
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

    public double getCostWoAccessories() { return this.getDouble(COST_WO_ACCESSORIES);}

    public String getRemarks()
    {
        return this.getString(REMARKS);
    }

    public String getMGCode()
    {
        return this.getString(MGCODE);
    }

    public String getModuleCategory()
    {
        return this.getString(MODULE_CATEGORY);
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

    public String getDescription()
    {
        return this.getString(DESCRIPTION);
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

    public String getAccessoryFlag()
    {
        return this.getString(ACCESSORY_FLAG);
    }

    public boolean isLeftExposed()
    {
        return this.containsKey(LEFT_EXPOSED) && this.getBoolean(LEFT_EXPOSED);
    }

    public boolean isRightExposed()
    {
        return this.containsKey(RIGHT_EXPOSED) && this.getBoolean(RIGHT_EXPOSED);
    }

    public boolean isTopExposed()
    {
        return this.containsKey(TOP_EXPOSED) && this.getBoolean(TOP_EXPOSED);
    }

    public boolean isBottomExposed()
    {
        return this.containsKey(BOTTOM_EXPOSED) && this.getBoolean(BOTTOM_EXPOSED);
    }

    public boolean isBackExposed()
    {
        return this.containsKey(BACK_EXPOSED) && this.getBoolean(BACK_EXPOSED);
    }

    public boolean isOpenUnit()
    {
        return this.containsKey(OPEN_UNIT) && this.getBoolean(OPEN_UNIT);
    }

    public String getAccPackDefault() {
        return this.getString(ACC_PACK_DEFAULT);
    }

    public String getHandleCode() {
        return this.getString(HANDLE_CODE);
    }

    public String getKnobCode() {
        return this.getString(KNOB_CODE);
    }

    public double getHandleQuantity() {
        return this.getDouble(HANDLE_QUANTITY);
    }

    public double getKnobQuantity() {
        return this.getDouble(KNOB_QUANTITY);
    }

    public String getProductCategory() {
        return this.getString(PRODUCT_CATEGORY);
    }

    public double getHingeQuantity() {
        return this.getDouble(HINGE_QUANTITY);
    }

    public String getHingeCode() {
        return this.getString(HINGE_CODE);
    }

    public String getHandleMandatory() { return this.getString(HANDLE_MANDATORY);}

    public String getKnobMandatory() { return this.getString(KNOB_MANDATORY); }

    public String getHingeMandatory() { return this.getString(HINGE_MANDATORY); }

    public String getSqftCalculation() {  return this.getString(SQFT_CALCULATION);  }

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

    public String getHandleOverrideFlag() {
        return this.getString(HANDLE_OVERRIDE_FLAG);
    }

    public  String getHandleThickness() {
        return this.getString(HANDLE_THICKNESS);
    }

    public ProductModule setHandleThickness(String handleThickness)
    {
        this.put(HANDLE_THICKNESS,handleThickness);
        return this;

    }
    public ProductModule setHandleOverrideFlag(String handleOverrideFlag)
    {
        this.put(HANDLE_OVERRIDE_FLAG,handleOverrideFlag);
        return this;
    }


    public ProductModule setHandleType(String handleType)
    {
        this.put(HANDLE_TYPE,handleType);
        return this;
    }

    public ProductModule setHandleFinish(String handleFinish)
    {
        this.put(HANDLE_FINISH,handleFinish);
        return this;
    }

    public ProductModule setKnobType(String knobType)
    {
        this.put(KNOB_TYPE,knobType);
        return this;
    }

    public ProductModule setKnobFinish(String knobFinish)
    {
        this.put(KNOB_FINISH,knobFinish);
        return this;
    }


    public ProductModule setAccPackDefault(String accPackDefault)
    {
        this.put(ACC_PACK_DEFAULT,accPackDefault);
        return this;
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

    public ProductModule setModuleCategory(String moduleCategory)
    {
        this.put(MODULE_CATEGORY, moduleCategory );
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

    public ProductModule setExposedSides(String exposedSides)
    {
        this.put(EXPOSED_SIDES, exposedSides);
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

    public ProductModule setShutterDesignCode(String code)
    {
        this.put(SHUTTER_DESIGN_CODE, code);
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

    public ProductModule setAccessoryFlag(String flag)
    {
        this.put(ACCESSORY_FLAG, flag);
        return this;
    }

    public ProductModule setCustomCheck(String customCheck)
    {
        this.put(CUSTOM_CHECK,customCheck);
        return this;
    }

    public ProductModule setProductCategory(String productCategory)
    {
        this.put(PRODUCT_CATEGORY,productCategory);
        return this;
    }

    public String getCustomCheck() {
        return this.getString(CUSTOM_CHECK);
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

    public void setHingePacks()
    {
        if (this.containsKey(HINGE_PACK))
        {
            JsonArray hingePackJson = this.getJsonArray(HINGE_PACK);
            JsonArray hingePacks = new JsonArray();
            for (int i=0; i < hingePackJson.size(); i++)
            {
                hingePacks.add(new HingePack(hingePackJson.getJsonObject(i)));
            }
            this.put(HINGE_PACK, hingePacks);
        }
    }


    public List<HingePack> getHingePacks()
    {
        if (this.containsKey(HINGE_PACK)) return this.getJsonArray(HINGE_PACK).getList();
        return Collections.EMPTY_LIST;
    }

    public ProductModule setHandleMandatory(String flag)
    {
        this.put(HANDLE_MANDATORY, flag);
        return this;
    }

    public ProductModule setKnobMandatory(String flag)
    {
        this.put(KNOB_MANDATORY, flag);
        return this;
    }

    public ProductModule setHingeMandatory(String flag)
    {
        this.put(HINGE_MANDATORY, flag);
        return this;
    }

    public ProductModule setSqftCalculation(String flag)
    {
        this.put(SQFT_CALCULATION, flag);
        return this;
    }

    public ProductModule setHandleQuantity(double handleQuantity)
    {
        this.put(HANDLE_QUANTITY, handleQuantity);
        return this;
    }

    public ProductModule setKnobQuantity(double knobQuantity)
    {
        this.put(KNOB_QUANTITY, knobQuantity);
        return this;
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

    public double getAreaOfModuleInSft()
    {
        double h = this.getHeight();
        double w = this.getWidth();
        double d = this.getDepth();

        double t1 = 0;
        double t2 = 0;

        t1=h;
        t2=w;

        return t1 * t2 * SQMM2SQFT;
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
                ", shutterDesignCode='" + this.getShutterDesignCode() + '\'' +
                ", color='" + this.getColorCode() + '\'' +
                ", exposedSides='" + this.getExposedSides() + '\'' +
                ", handleCode='" + this.getHandleCode() + '\'' +
                ", knobCode='" + this.getKnobCode()+ '\'' +
                ", hingeCode='" + this.getHingeCode()+ '\'' +
                ", handleQuantity='" + this.getHandleQuantity() + '\'' +
                ", knobQuantity='" + this.getKnobQuantity() + '\'' +
                ", remarks='" + this.getRemarks() + '\'' +
                ", handleMandatory='" + this.getHandleMandatory() + '\'' +
                ", knobMandatory='" + this.getKnobMandatory() + '\'' +
                ", hingeMandatory='" + this.getHingeMandatory() + '\'' +
                ", sqftCalculation='" + this.getSqftCalculation() + '\'' +
                ", customCheck='" + this.getCustomCheck() + '\'' +
                '}';

    }

    public boolean isAccessoryUnit()
    {
        return ACCESSORY_UNIT.equals(this.getUnit());
    }

    public ProductModule setAmount(double amount) {
        this.put(AMOUNT, amount);
        return this;
    }

    public ProductModule setCostWoAccessories(double costWoAccessories) {
        this.put(COST_WO_ACCESSORIES, costWoAccessories);
        return this;
    }

    public ProductModule setHandleCode(String handleCode)
    {
        this.put(HANDLE_CODE,handleCode);
        return this;

    }

    public ProductModule setKnobCode(String knobCode)
    {
        this.put(KNOB_CODE,knobCode);
        return this;

    }


}
