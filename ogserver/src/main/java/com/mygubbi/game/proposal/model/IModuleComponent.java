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

    public boolean isCarcass();

    public boolean isShutter();

    public boolean isAccessory();

    public boolean isHardware();

    public String getType();

    public String getComponentCode();

    public double getQuantity();

}
