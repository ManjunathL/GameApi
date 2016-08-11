package com.mygubbi.game.proposal.model;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * Created by Sunil on 26-04-2016.
 */
public class AccessoryPack extends JsonObject
{
    private static final String CODE = "code";
    private static final String TITLE = "title";
    private static final String ACCESSORIES = "accessories";

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

    public void addAccessory(AccHwComponent component)
    {
        if (component == null) return;
        if (!this.containsKey(ACCESSORIES))
        {
            this.put(ACCESSORIES, new JsonArray());
        }
        this.getJsonArray(ACCESSORIES).add(new AccessoryComponent(component.getCode(), component.getTitle(), component.getImageUrl()));
    }

    public static class AccessoryComponent extends JsonObject
    {
        public AccessoryComponent(String code, String title, String imagePath)
        {
            this.put("code", code);
            this.put("title", title);
            this.put("imagePath", imagePath);
        }
    }
}

