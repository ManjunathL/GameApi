package com.mygubbi.catalog;

import com.mygubbi.si.catalog.BlogRecord;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * Created by Mehbub on 18-08-2016.
 */
public class BlogJson extends JsonObject
{
    private boolean kitchen;

    public BlogJson()
    {

    }
    public BlogJson(BlogRecord record)
    {
        this.put("id", record.getId());
        this.put("blogId", record.getBlogId());

    }
    public BlogJson(JsonObject json)
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
    public String getPublishDate() {return this.getString("publish_date");}

    public String getBlogId()
    {
        return this.getString("blogId");
    }
    public String getTags()
    {
        return this.getString("tags");
    }
    public JsonObject getBlogJson()
    {
        return new JsonObject().put("id", this.getBlogId());
    }

}
