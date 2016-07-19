package com.mygubbi.game.proposal.model;

import io.vertx.core.json.JsonObject;

/**
 * Created by Sunil on 26-04-2016.
 */
public class AccessoryPack extends JsonObject
{
    public static final String CODE = "code";
    public static final String TITLE = "title";

    public AccessoryPack()
    {

    }

    public AccessoryPack(JsonObject data)
    {
        super(data.getMap());
    }

    public String getCode()
    {
        return this.getString(CODE);
    }

    public String getTitle()
    {
        return this.getString(TITLE);
    }
}

