package com.mygubbi.game.proposal.model;

import io.vertx.core.json.JsonObject;
import org.json.JSONObject;

/**
 * Created by Shruthi on 9/13/2017.
 */
public class OldToNewFinishMapping extends JSONObject
{
    private static final String OLD_CODE = "oldCode";
    private static final String NEW_CODE = "newCode";
    private static final String FLAG = "flag";

    public OldToNewFinishMapping(JsonObject jsonObject)
    {
        super(jsonObject.getMap());
    }

    public  String getOldCode() {
        return this.getString(OLD_CODE);
    }

    public  String getNewCode() {
        return this.getString(NEW_CODE);
    }
    public String getFlag()
    {
        return this.getString(FLAG);
    }
    public OldToNewFinishMapping setOldCode(String oldcode)
    {
        put(OLD_CODE,oldcode);
        return this;
    }
    public OldToNewFinishMapping setnewCode(String newcode)
    {
        put(NEW_CODE,newcode);
        return this;
    }
    public OldToNewFinishMapping setFlag(String flag)
    {
        put(FLAG,flag);
        return this;
    }

}
