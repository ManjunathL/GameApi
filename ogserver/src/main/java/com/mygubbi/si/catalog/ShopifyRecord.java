package com.mygubbi.si.catalog;

import com.mygubbi.common.StringUtils;
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
    private static Map<String, JsonObject> categoryMap;
    private final static Logger LOG = LogManager.getLogger(ShopifyRecord.class);

    static
    {
        categoryMap = new HashMap<>();
        categoryMap.put("L Shaped Kitchen", new JsonObject().put("category", "Kitchen").put("categoryId", "kitchen").put("subcategoryId", "lshapedk"));
        categoryMap.put("U Shaped Kitchen", new JsonObject().put("category", "Kitchen").put("categoryId", "kitchen").put("subcategoryId", "ushapedk"));
        categoryMap.put("Straight Kitchen", new JsonObject().put("category", "Kitchen").put("categoryId", "kitchen").put("subcategoryId", "straightk"));
        categoryMap.put("Parallel Kitchen", new JsonObject().put("category", "Kitchen").put("categoryId", "kitchen").put("subcategoryId", "parallelk"));

        categoryMap.put("Wardrobe", new JsonObject().put("category", "Bedroom").put("categoryId", "bedroom").put("subcategoryId", "wardrobe"));
        categoryMap.put("Study Table", new JsonObject().put("category", "Bedroom").put("categoryId", "bedroom").put("subcategoryId", "studytable"));
        categoryMap.put("Side Table", new JsonObject().put("category", "Bedroom").put("categoryId", "bedroom").put("subcategoryId", "sidetable"));
        categoryMap.put("Book Rack", new JsonObject().put("category", "Bedroom").put("categoryId", "bedroom").put("subcategoryId", "bookrack"));

        categoryMap.put("Entertainment Unit", new JsonObject().put("category", "Living & Dining").put("categoryId", "livingndining").put("subcategoryId", "entunit"));
        categoryMap.put("Shoe Rack", new JsonObject().put("category", "Living & Dining").put("categoryId", "livingndining").put("subcategoryId", "shoerack"));
        categoryMap.put("Crockery Unit", new JsonObject().put("category", "Living & Dining").put("categoryId", "livingndining").put("subcategoryId", "crockunit"));
        categoryMap.put("Foyer Unit", new JsonObject().put("category", "Living & Dining").put("categoryId", "livingndining").put("subcategoryId", "foyerunit"));
        categoryMap.put("Sideboard", new JsonObject().put("category", "Living & Dining").put("categoryId", "livingndining").put("subcategoryId", "sideboard"));
    }

    private String[] row;
    private JsonArray components;
    private JsonObject stylePrice;

    public ShopifyRecord(String[] row)
    {
        this(row, null, null);
    }

    public ShopifyRecord(String[] row, ShopifyComponentParser componentParser, ShopifyStylePriceParser stylePriceParser)
    {
        this.row = row;
        if (componentParser != null) this.setComponents(componentParser);
        if (stylePriceParser != null) this.setStylePrice(stylePriceParser);
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
    public String getSubcategory()
    {
        return this.row[SUBCATEGORY];
    }
    public String getSubcategoryId()
    {
        if (!categoryMap.containsKey(this.getSubcategory())) return null;
        return categoryMap.get(this.getSubcategory()).getString("subcategoryId");
    }
    public String getCategory()
    {
        if (!categoryMap.containsKey(this.getSubcategory())) return null;
        return categoryMap.get(this.getSubcategory()).getString("category");
    }
    public String getCategoryId()
    {
        if (!categoryMap.containsKey(this.getSubcategory())) return null;
        return categoryMap.get(this.getSubcategory()).getString("categoryId");
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

    private void setStylePrice(ShopifyStylePriceParser stylePriceParser)
    {
        this.stylePrice = stylePriceParser.getStylePrice(this.getId(), this.getTitle());
    }

    public JsonObject getStylePrice()
    {
        return this.stylePrice;
    }
}
