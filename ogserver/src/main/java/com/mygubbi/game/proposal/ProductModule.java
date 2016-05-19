package com.mygubbi.game.proposal;

import com.mygubbi.common.StringUtils;
import io.vertx.core.json.JsonObject;

/**
 * Created by Sunil on 27-04-2016.
 */

public class ProductModule extends JsonObject
{
    public static String MAPPED_AT_MODULE = "m";
    public static String MAPPED_AT_DEFAULT = "d";
    public static String NOT_MAPPED = "n";

    private static final String UNIT = "unit";
    private static final String KDMCODE = "kdmcode";
    private static final String KDMDEFCODE = "kdmdefcode";
    private static final String SEQ = "seq";
    private static final String CARCASS_CODE = "carcass_code";
    private static final String FINISH_CODE = "finish_code";
    private static final String FINISH_TYPE = "finish_type";
    private static final String COLOR_CODE = "color_code";
    private static final String MAKE_TYPE = "make_type";
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

    public String getMakeType()
    {
        return this.getString(MAKE_TYPE);
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

    public ProductModule setMakeType(String type)
    {
        this.put(MAKE_TYPE, type);
        return this;
    }

    public ProductModule setColorCode(String code)
    {
        this.put(COLOR_CODE, code);
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

    public boolean hasMGMapping()
    {
        return MAPPED_AT_MODULE.equals(this.getMapped());
    }

    public boolean hasDefaultMapping()
    {
        return MAPPED_AT_DEFAULT.equals(this.getMapped());
    }

    public boolean hasNoMapping()
    {
        if (StringUtils.isEmpty(this.getMapped())) return true;
        return NOT_MAPPED.equals(this.getMapped());
    }

    @Override
    public String toString()
    {
        return "ProductModule{" +
                "unit='" + this.getUnit() + '\'' +
                ", name='" + this.getName() + '\'' +
                ", code='" + this.getKDMCode() + '\'' +
                ", finish='" + this.getFinishCode() + '\'' +
                ", color='" + this.getColorCode() + '\'' +
                ", quantity=" + this.getQuantity() +
                ", uom='" + this.getUom() + '\'' +
                ", remarks='" + this.getRemarks() + '\'' +
                ", width=" + this.getWidth() +
                ", depth=" + this.getDepth()+
                ", height=" + this.getHeight() +
                '}';

    }

}
