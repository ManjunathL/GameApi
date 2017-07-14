package com.mygubbi.game.proposal.model;

import io.vertx.core.json.JsonObject;

/**
 * Created by User on 04-07-2017.
 */
public class SOWMaster extends JsonObject {

    public static final String SPACE_TYPE = "spaceType";

    public static final String L1S01_CODE = "L1S01Code";
    public static final String L1S01 = "L1S01";
    public static final String L2S01 = "L2S01";
    public static final String L2S02 = "L2S02";
    public static final String L2S03 = "L2S03";
    public static final String L2S04 = "L2S04";
    public static final String L2S05 ="L2S05";
    public static final String L2S06 ="L2S06";


    public SOWMaster() {}

    public SOWMaster(JsonObject jsonObject) {
        super(jsonObject.getMap());
    }


    private static SOWMaster fromJson(JsonObject json)
    {
        SOWMaster sow_master = new SOWMaster();
        sow_master.setSpaceType(json.getString("spaceType"));
        sow_master.setL1S01(json.getString("L1S01"));
        sow_master.setL2S01(json.getString("L2S01"));
        sow_master.setL2S02(json.getString("L2S02"));
        sow_master.setL2S03(json.getString("L2S03"));
        sow_master.setL2S04(json.getString("L2S04"));
        sow_master.setL2S05(json.getString("L2S05"));
        sow_master.setL2S06(json.getString("L2S06"));

        return sow_master;
    }

    public String getSpaceType() {
        return this.getString(SPACE_TYPE);
    }

    public SOWMaster setSpaceType(String spaceType)
    {
        put(SPACE_TYPE,spaceType);
        return this;
    }


    public String getL1S01Code() {
        return this.getString(L1S01_CODE);
    }

    public String getL1S01() {
        return this.getString(L1S01);
    }

    public SOWMaster setL1S01(String code)
    {
        put(L1S01,code);
        return this;
    }

    public String getL2S01() {
        return this.getString(L2S01);
    }

    public SOWMaster setL2S01(String code)
    {
        put(L2S01,code);
        return this;
    }

    public String getL2S02() {
        return this.getString(L2S02);
    }

    public SOWMaster setL2S02(String code)
    {
        put(L2S02,code);
        return this;
    }

    public String getL2S03() {
        return this.getString(L2S03);
    }

    public SOWMaster setL2S03(String code)
    {
        put(L2S03,code);
        return this;
    }

    public String getL2S04() {
        return this.getString(L2S04);
    }

    public SOWMaster setL2S04(String code)
    {
        put(L2S04,code);
        return this;
    }

    public String getL2S05() {
        return this.getString(L2S05);
    }

    public SOWMaster setL2S05(String code)
    {
        put(L2S05,code);
        return this;
    }

    public String getL2S06() {
        return this.getString(L2S06);
    }

    public SOWMaster setL2S06(String code)
    {
        put(L2S06,code);
        return this;
    }
}
