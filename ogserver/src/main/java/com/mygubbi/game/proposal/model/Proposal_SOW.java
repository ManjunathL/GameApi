package com.mygubbi.game.proposal.model;

import io.vertx.core.json.JsonObject;

/**
 * Created by User on 06-07-2017.
 */
public class Proposal_SOW extends JsonObject {


    public static final String ID = "id";
    public static final String PROPOSAL_ID = "proposalId";
    public static final String VERSION  = "version";
    public static final String SPACE_TYPE = "spaceType";
    public static final String ROOM = "room";
    public static final String PRODUCT_ID = "productId";
    public static final String PRODUCT = "product";
    public static final String L1S01 = "L1S01";
    public static final String L2S01 = "L2S01";
    public static final String L2S02 = "L2S02";
    public static final String L2S03 = "L2S03";
    public static final String L2S04 = "L2S04";
    public static final String L2S05 ="L2S05";
    public static final String L2S06 ="L2S06";
    public static final String L2S07 ="L2S07";

    public Proposal_SOW() {}

    public Proposal_SOW(JsonObject jsonObject){
        super(jsonObject.getMap());
    }

    public int getID() {
        return this.getInteger(ID);
    }

    public int getProposalId() {
        return this.getInteger(PROPOSAL_ID);
    }

    public double getVERSION() {
        return this.getDouble(VERSION);
    }

    public String getSpaceType() {
        return this.getString(SPACE_TYPE);
    }

    public String getROOM() {
        return this.getString(ROOM);
    }

    public int getProductId() {
        return this.getInteger(PRODUCT_ID);
    }

    public String getPRODUCT() {
        return this.getString(PRODUCT);
    }

    public String getL1S01() {
        return this.getString(L1S01);
    }

    public String getL2S01() {
        return this.getString(L2S01);
    }

    public String getL2S02() {
        return this.getString(L2S02);
    }

    public String getL2S03() {
        return this.getString(L2S03);
    }

    public String getL2S04() {
        return this.getString(L2S04);
    }

    public String getL2S05() {
        return this.getString(L2S05);
    }

    public String getL2S06() {
        return this.getString(L2S06);
    }

    public String getL2S07() {
        return this.getString(L2S07);
    }

    public Proposal_SOW setId(int id)
    {
        put(ID,id);
        return this;
    }

    public Proposal_SOW setProposalId(int proposalId)
    {
        put(PROPOSAL_ID,proposalId);
        return this;
    }

    public Proposal_SOW setVersion(double version)
    {
        put(VERSION,version);
        return this;
    }

    public Proposal_SOW setSpaceType(String spaceType)
    {
        put(SPACE_TYPE,spaceType);
        return this;
    }

    public Proposal_SOW setRoom(String room)
    {
        put(ROOM,room);
        return this;
    }

    public Proposal_SOW setProductId(int productId)
    {
        put(PRODUCT_ID,productId);
        return this;
    }

    public Proposal_SOW setProduct(String product)
    {
        put(PRODUCT,product);
        return this;
    }

    public Proposal_SOW setL1S01(String l1s01)
    {
        put(L1S01,l1s01);
        return this;
    }

    public Proposal_SOW setL2S01(String l2s01)
    {
        put(L2S01,l2s01);
        return this;
    }

    public Proposal_SOW setL2S02(String l2s02)
    {
        put(L2S02,l2s02);
        return this;
    }

    public Proposal_SOW setL2S03(String l2s03)
    {
        put(L2S03,l2s03);
        return this;
    }

    public Proposal_SOW setL2S04(String l2s04)
    {
        put(L2S04,l2s04);
        return this;
    }

    public Proposal_SOW setL2S05(String l2s05)
    {
        put(L2S05,l2s05);
        return this;
    }

    public Proposal_SOW setL2S06(String l2s06)
    {
        put(L2S06,l2s06);
        return this;
    }

    public Proposal_SOW setL2S07(String l2s07)
    {
        put(L2S07,l2s07);
        return this;
    }

}
