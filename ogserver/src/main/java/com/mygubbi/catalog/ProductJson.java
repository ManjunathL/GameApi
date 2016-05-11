package com.mygubbi.catalog;

import com.mygubbi.si.catalog.ShopifyRecord;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Date;

/**
 * Created by test on 22-01-2016.
 */
public class ProductJson extends JsonObject
{
    private boolean kitchen;

    public ProductJson()
    {

    }

    public ProductJson(JsonObject json)
    {
        super(json.getMap());
    }

    public ProductJson(ShopifyRecord record)
    {
        this.put("id", record.getId());
        this.put("productId", record.getId());
        this.put("name", record.getTitle());
        this.put("desc", record.getDesc());
        this.put("dimension", record.getDimension());
        this.put("category", record.getCategory());
        this.put("subcategory", record.getSubcategory());
        this.put("categoryId", record.getCategoryId());
        this.put("subcategoryId", record.getSubcategoryId());
        this.put("tags", record.getTags());
        this.put("designer", "mygubbi");
        this.put("curr", "INR");
        this.put("popularity", 1);
        this.put("relevance", 1);
        this.put("shortlisted", 1);
        this.put("likes", 1);
        this.put("createDt", "01-02-2015");
        this.put("pageId", record.getId());
        JsonObject stylePrice = record.getStylePrice();
        if (stylePrice != null)
        {
            this.mergeIn(stylePrice);
        }
        this.put("images", new JsonArray());
        this.addImage(record.getImage());
        this.put("defaultPrice", record.getPrice());
        this.put("defaultMaterial", record.getMaterial());
        this.put("defaultFinish", record.getFinish());
        this.put("accessories", new JsonArray());
        this.put("mf", new JsonArray().add(record.getMFP()));
        if (record.getComponents() != null)
        {
            this.put("components", record.getComponents());
        }
    }

    public void addImage(String image)
    {
        if (image != null) this.put("images", this.getJsonArray("images").add(image));
    }

    public void addMFP(JsonObject mfp)
    {
        this.put("mf", this.getJsonArray("mf").add(mfp));
    }

    public String getCategory()
    {
        return this.getString("category");
    }

    public String getSubCategory()
    {
        return this.getString("subcategory");
    }

    public String getProductId()
    {
        return this.getString("productId");
    }
    public String getCreateDt()
    {
        return this.getString("createDt");
    }

    public String getName()
    {
        return this.getString("name");
    }

    public String getDescription()
    {
        return this.getString("desc");
    }

    public String getStyleId()
    {
        return this.getString("styleId");
    }

    public Integer getPopularity()
    {
        return this.getInteger("popularity");
    }

    public Integer getRelevance() {return this.getInteger("relevance");}

/*
    public Date getCreateDt() {return this.getCreateDt();}
*/

    public String getFirstImage()
    {
        return this.getJsonArray("images").getString(0);
    }

    public JsonObject getShortJson()
    {
        return new JsonObject().put("id", this.getProductId()).put("productId", this.getProductId())
                .put("name", this.getName()).put("image", this.getFirstImage());
    }

    public boolean isKitchen()
    {
        return "Kitchen".equals(this.getCategory());
    }
}
