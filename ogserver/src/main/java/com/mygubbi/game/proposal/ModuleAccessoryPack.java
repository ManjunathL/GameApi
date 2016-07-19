package com.mygubbi.game.proposal;

import io.vertx.core.json.JsonObject;

import java.util.Collections;
import java.util.List;

/**
 * Created by Sunil on 27-04-2016.
 */

public class ModuleAccessoryPack extends JsonObject
{
    private static final String APCODE = "apcode";
    private static final String ADDONS = "addons";

    public ModuleAccessoryPack()
    {

    }

    public ModuleAccessoryPack(JsonObject json)
    {
        super(json.getMap());
    }

    public String getAccessoryPackCode()
    {
        return this.getString(APCODE);
    }

    public List<String> getAddons()
    {
        if (!this.containsKey(ADDONS)) return Collections.emptyList();
        return this.getJsonArray(ADDONS).getList();
    }

}
