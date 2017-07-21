package com.mygubbi.game.proposal.model;

import io.vertx.core.json.JsonObject;

/**
 * Created by User on 06-07-2017.
 */
public class ProposalSOW extends JsonObject {


    public static final String ID = "id";
    public static final String PROPOSAL_ID = "proposalId";
    public static final String VERSION  = "version";
    public static final String SPACE_TYPE = "spaceType";
    public static final String ROOM = "roomcode";
    public static final String L1S01 = "L1S01";
    public static final String L2S01 = "L2S01";
    public static final String L2S02 = "L2S02";
    public static final String L2S03 = "L2S03";
    public static final String L2S04 = "L2S04";
    public static final String L2S05 ="L2S05";
    public static final String L2S06 ="L2S06";
    public static final String L1S01Code ="L1S01Code";

    public ProposalSOW() {}

    public ProposalSOW(JsonObject jsonObject){
        super(jsonObject.getMap());
    }

    public int getID() {
        return this.getInteger(ID);
    }

    public int getProposalId() {
        return this.getInteger(PROPOSAL_ID);
    }

    public String getVERSION() {
        return this.getString(VERSION);
    }

    public String getSpaceType() {
        return this.getString(SPACE_TYPE);
    }

    public String getROOM() {
        return this.getString(ROOM);
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
    public String getL1S01Code() {
        return this.getString(L1S01Code);
    }


    public ProposalSOW setId(int id)
    {
        put(ID,id);
        return this;
    }

    public ProposalSOW setProposalId(int proposalId)
    {
        put(PROPOSAL_ID,proposalId);
        return this;
    }

    public ProposalSOW setVersion(String version)
    {
        put(VERSION,version);
        return this;
    }

    public ProposalSOW setSpaceType(String spaceType)
    {
        put(SPACE_TYPE,spaceType);
        return this;
    }

    public ProposalSOW setRoom(String room)
    {
        put(ROOM,room);
        return this;
    }

    public ProposalSOW setL1S01(String l1s01)
    {
        put(L1S01,l1s01);
        return this;
    }

    public ProposalSOW setL2S01(String l2s01)
    {
        put(L2S01,l2s01);
        return this;
    }

    public ProposalSOW setL2S02(String l2s02)
    {
        put(L2S02,l2s02);
        return this;
    }

    public ProposalSOW setL2S03(String l2s03)
    {
        put(L2S03,l2s03);
        return this;
    }

    public ProposalSOW setL2S04(String l2s04)
    {
        put(L2S04,l2s04);
        return this;
    }

    public ProposalSOW setL2S05(String l2s05)
    {
        put(L2S05,l2s05);
        return this;
    }

    public ProposalSOW setL2S06(String l2s06)
    {
        put(L2S06,l2s06);
        return this;
    }
    public ProposalSOW setL1S01Code(String l2s06)
    {
        put(L1S01Code,l2s06);
        return this;
    }


}
