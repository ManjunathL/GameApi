package com.mygubbi.catalog;

import com.mygubbi.si.catalog.SeoRecord;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * Created by Mehbub on 13-10-2016.
 */
public class SeoJson  extends JsonObject {
    private boolean kitchen;

    public SeoJson()
    {

    }
    public SeoJson(SeoRecord record)
    {
        this.put("id", record.getId());
        this.put("blogId", record.getSeoId());

    }
    public SeoJson(JsonObject json)
    {
        super(json.getMap());
    }

    /*
        public Integer getId() { return this.getInteger("id"); }
    */
    public JsonArray getFirstImage()
    {
/*
    return this.getJsonArray("blog_categories").getString();
*/

        return this.getJsonArray("blog_categories");

    }
    /*public void addImage(String image)
    {
        if (image != null) this.put("blog_categories", this.getJsonArray("blog_categories").add(image));
    }*/
    public JsonArray getBlog_categories() {
        return this.getFirstImage();
/*
        return new JsonObject().put("", this.getFirstImage());
*/
    }
    public String getMetaKeywords()
    {
        return this.getString("meta_keywords");
    }

    public String getSeoId()
    {
        return this.getString("seoId");
    }
    public String getDescription()
    {
        return this.getString("description");
    }
    public String getTitle()
    {
        return this.getString("title");
    }

    public String getCategory()
    {
        return this.getString("category");
    }
    public String getSubCategory()
    {
        return this.getString("sub-category");
    }
    public String getLocation()
    {
        return this.getString("location");
    }

    public JsonObject getSeoJson()
    {
        return new JsonObject().put("id", this.getSeoId());
    }

}
