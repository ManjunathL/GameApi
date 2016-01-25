package com.mygubbi.si.catalog;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by test on 21-01-2016.
 */
public class ShopifyRecord
{
    private static final int ID = 0;
    private static final int TITLE = 1;
    private static final int DESC = 2;
    private static final int DIMENSION = 3;
    private static final int SUBCATEGORY = 4;
    private static final int TAGS = 5;
    private static final int MATERIAL = 8;
    private static final int FINISH = 10;
    private static final int PRICE = 19;
    private static final int IMAGE = 24;
    private static Map<String, String> categoryMap;
    private final static Logger LOG = LogManager.getLogger(ShopifyRecord.class);

    static
    {
        categoryMap = new HashMap<>();
        categoryMap.put("L Shaped Kitchen", "Kitchen");
        categoryMap.put("U Shaped Kitchen", "Kitchen");
        categoryMap.put("Straight Kitchen", "Kitchen");
        categoryMap.put("Parallel Kitchen", "Kitchen");
        categoryMap.put("Wardrobe", "Bedroom");
        categoryMap.put("Study Table", "Bedroom");
        categoryMap.put("Side Table", "Bedroom");
        categoryMap.put("Book Rack", "Bedroom");
        categoryMap.put("Entertainment Unit", "Living & Dining");
        categoryMap.put("Shoe Rack", "Living & Dining");
        categoryMap.put("Crockery Unit", "Living & Dining");
    }

    private String[] row;
    private JsonArray components;

    public ShopifyRecord(String[] row)
    {
        this(row, null);
    }

    public ShopifyRecord(String[] row, ShopifyComponentParser componentParser)
    {
        this.row = row;
        if (componentParser != null) this.setComponents(componentParser);
    }

    public String getId()
    {
        return this.row[ID];
    }

    public String getTitle()
    {
        return this.row[TITLE];
    }
    public String getDesc()
    {
        int length = this.row[DESC].length();
        if (length <= 7) return "";
        return this.row[DESC].substring(3, length - 4);
    }
    public String getDimension()
    {
        return "WxDxH " + this.row[DIMENSION];
    }
    public String getSubCategory()
    {
        return this.row[SUBCATEGORY];
    }
    public String getCategory()
    {
        return categoryMap.get(this.getSubCategory());
    }
    public String getTags()
    {
        return this.row[TAGS];
    }
    public String getMaterial()
    {
        return this.row[MATERIAL];
    }
    public String getFinish()
    {
        return this.row[FINISH];
    }
    public int getPrice()
    {
        try
        {
            return Integer.parseInt(this.row[PRICE]);
        }
        catch (NumberFormatException e)
        {
            return 0;
        }
    }
    public String getImage()
    {
        if (this.row[IMAGE] == "" || this.row[IMAGE] == null) return null;
        int lastSlash = this.row[IMAGE].lastIndexOf('/');
        int lastDot = this.row[IMAGE].lastIndexOf('.');
        if (lastSlash == -1 || lastDot == -1) return null;
        return this.row[IMAGE].substring(lastSlash + 1, lastDot);
    }

    public String getImageUrl()
    {
        if (this.row[IMAGE] == "" || this.row[IMAGE] == null) return null;
        return this.row[IMAGE];
    }

    public boolean isKitchen()
    {
        return "Kitchen".equals(this.getCategory());
    }

    @Override
    public String toString()
    {
        return "ShopifyRecord{" +
                this.getId() + " | " + this.getTitle() + " | " + this.getPrice() + " | " + this.getImage() +
                '}';
    }

    public JsonObject getMFP()
    {
        return new JsonObject().put("basePrice", this.getPrice()).put("material", this.getMaterial()).put("finish", this.getFinish());
    }

    public String getBaseProductId()
    {
        int firstHyphen = this.getTitle().indexOf(' ');
        if (firstHyphen == -1) return "";
        return this.getTitle().substring(0, firstHyphen).trim().toLowerCase();
    }

    public JsonArray getComponents()
    {
        return this.components;
    }

    private void setComponents(ShopifyComponentParser componentParser)
    {
        if (this.isKitchen())
        {
            String productId = this.getBaseProductId();
            JsonArray components = componentParser.getComponents(productId);
            if (components.isEmpty()) LOG.info("components not setup for productid:" + productId);
            this.components = components;
        }
    }
}
