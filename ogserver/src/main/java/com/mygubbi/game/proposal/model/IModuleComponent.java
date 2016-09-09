package com.mygubbi.game.proposal.model;

/**
 * Created by test on 18-07-2016.
 */
public interface IModuleComponent
{
    public static final String CARCASS_TYPE = "C";
    public static final String SHUTTER_TYPE = "S";
    public static final String ACCESSORY_TYPE = "A";
    public static final String HARDWARE_TYPE = "H";

    public static final String RESOLVED_TYPE = "X";

    public static final String QUANTITY_FIXED = "X";
    public static final String QUANTITY_CALCULATED = "C";

    public boolean isCarcass();

    public boolean isShutter();

    public boolean isAccessory();

    public boolean isHardware();

    public boolean isToBeResolved();

    public String getType();

    public String getComponentCode();

    public double getQuantity();

    public String getQuantityFormula();

    public String getQuantityFlag();

    public boolean isFixedQuantity();

    public boolean isCalculatedQuantity();
}
