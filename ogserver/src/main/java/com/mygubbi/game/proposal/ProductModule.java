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

    private static final String UNIT = "unitType";
    private static final String SEQ = "seq";
    private static final String EXT_CODE = "extCode";
    private static final String EXT_DEF_CODE = "extDefCode";

    private static final String MGCODE = "mgCode";
    private static final String MGTYPE = "mgType";
    private static final String MGIMAGE = "mgImage";
    private static final String MGDIMENSION = "mgDimension";
    private static final String MGDESCRIPTION = "mgDescription";

    private static final String CARCASS_CODE = "carcassCode";
    private static final String FINISH_CODE = "finishCode";
    private static final String FINISH_TYPE = "finishTypeCode";
    private static final String COLOR_CODE = "colorCode";
    private static final String MAKE_TYPE = "makeTypeCode";
    private static final String AMOUNT = "amount";
    private static final String REMARKS = "remarks";
    private static final String MAPPED = "importStatus";

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
        return this.getString(EXT_CODE);
    }

    public String getKDMDefaultCode()
    {
        return this.getString(EXT_DEF_CODE);
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

    public int getAmount()
    {
        return this.getInteger(AMOUNT);
    }

    public String getRemarks()
    {
        return this.getString(REMARKS);
    }

    public String getMGCode()
    {
        return this.getString(MGCODE);
    }

    public String getMGName()
    {
        return this.getString(MGTYPE);
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
        this.put(EXT_CODE, code);
        return this;
    }

    public ProductModule setDefaultModule(String module)
    {
        this.put(EXT_DEF_CODE, module);
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
        this.put(MGTYPE, value);
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
                ", code='" + this.getKDMCode() + '\'' +
                ", finish='" + this.getFinishCode() + '\'' +
                ", color='" + this.getColorCode() + '\'' +
                ", remarks='" + this.getRemarks() + '\'' +
                '}';

    }

}
