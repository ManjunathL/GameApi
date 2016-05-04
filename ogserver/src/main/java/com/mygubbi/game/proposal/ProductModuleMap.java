package com.mygubbi.game.proposal;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * Created by Sunil on 26-04-2016.
 */
public class ProductModuleMap extends JsonObject
{
    public static String MAPPED_AT_MODULE = "m";
    public static String MAPPED_AT_DEFAULT = "d";
    public static String NOT_MAPPED = "n";

    public ProductModuleMap setKdmModule(KDMaxModule kdmModule)
    {
        this.put("kdmaxmodule", kdmModule);
        return this;
    }

    public KDMaxModule getKdmModule()
    {
        return (KDMaxModule) this.getJsonObject("kdmaxmodule");
    }

    public ProductModuleMap setSequence(int seq)
    {
        this.put("seq", seq);
        return this;
    }

    public ProductModuleMap setMappedFlag(String flag)
    {
        this.put("mapped", flag);
        return this;
    }

    public ProductModuleMap setDefaultModule(String module)
    {
        this.put("defmodule", module);
        return this;
    }

    public ProductModuleMap addMGModule(String module, String title, String dimension, String image)
    {
        return this.addMGModule(new JsonObject().put("code", module).put("title", title).put("dim", dimension).put("image", image));
    }

    public ProductModuleMap addMGModule(JsonObject module)
    {
        if (!this.containsKey("mgmodules"))
        {
            this.put("mgmodules", new JsonArray());
        }
        JsonArray mgModules = this.getJsonArray("mgmodules");
        mgModules.add(module);
        this.put("mgmodules", mgModules);
        return this;
    }
}

