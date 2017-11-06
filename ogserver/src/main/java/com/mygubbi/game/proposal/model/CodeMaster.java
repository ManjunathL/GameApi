package com.mygubbi.game.proposal.model;


import io.vertx.core.json.JsonObject;
import org.json.JSONObject;

/**
 * Created by Shruthi on 9/21/2017.
 */
public class CodeMaster extends JSONObject
{
    private static final String LOOKUP_TYPE="lookupType";
    private static final String LEVEL_TYPE="levelType";
    private static final String ADDITIONAL_TYPE="additionalType";
    private static final String CODE="code";
    private static final String TITLE="title";

    public CodeMaster(JsonObject jsonObject)
    {
        super(jsonObject.getMap());
    }

    public String getLookupType()
    {
        return this.getString(LOOKUP_TYPE);
    }

    public String getCode()
    {
        return this.getString(CODE);
    }

    public String getLevelType()
    {
        return  this.getString(LEVEL_TYPE);
    }
    public String getAdditionalType()
    {
        return  this.getString(ADDITIONAL_TYPE);
    }
    public String getTitle()
    {
        return  this.getString(TITLE);
    }

    public CodeMaster setCode(String code)
    {
        put(CODE,code);
        return this;
    }

}
