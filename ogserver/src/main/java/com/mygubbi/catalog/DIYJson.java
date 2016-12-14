package com.mygubbi.catalog;

import com.mygubbi.si.catalog.DIYRecord;
import com.mygubbi.si.catalog.SeoRecord;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * Created by Mehbub on 01-12-2016.
 */
public class DIYJson  extends JsonObject {
    private boolean kitchen;

    public DIYJson()
    {

    }
    public DIYJson(DIYRecord record)
    {
        this.put("id", record.getId());
        this.put("diyId", record.getDiyId());

    }
    public DIYJson(JsonObject json)
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

        return this.getJsonArray("diy_categories");

    }
    /*public void addImage(String image)
    {
        if (image != null) this.put("blog_categories", this.getJsonArray("blog_categories").add(image));
    }*/
    public JsonArray getDiy_categories() {
        return this.getFirstImage();
/*
        return new JsonObject().put("", this.getFirstImage());
*/
    }
    public String getPublishDate() {return this.getString("publish_date");}

    public String getDiyId()
    {
        return this.getString("diyId");
    }
    public String getTags()
    {
        return this.getString("tags");
    }
    public JsonObject getDiyJson()
    {
        return new JsonObject().put("id", this.getDiyId());
    }

}
