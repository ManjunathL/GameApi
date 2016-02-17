package com.mygubbi.si.catalog;

import com.opencsv.CSVReader;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by test on 20-01-2016.
 */
public class ShopifyStylePriceParser
{
    private String stylePriceFile;
    private Map<String, String[]> productMap;

    private static final Map<String, JsonObject> styleMap;
    private static final Map<String, JsonObject> priceMap;

    static
    {
        priceMap = new HashMap<>();
        priceMap.put("base", new JsonObject().put("priceRange", "Base").put("priceId", "base"));
        priceMap.put("luxury", new JsonObject().put("priceRange", "Luxury").put("priceId", "luxury"));
        priceMap.put("premium", new JsonObject().put("priceRange", "Premium").put("priceId", "premium"));

        styleMap = new HashMap<>();
        styleMap.put("contemporary monochrome", new JsonObject().put("styleName", "Monochrome").put("styleId", "cmono"));
        styleMap.put("contemporary bold", new JsonObject().put("styleName", "Bold").put("styleId", "cbold"));
        styleMap.put("contemporary fresh", new JsonObject().put("styleName", "Fresh").put("styleId", "cfresh"));
        styleMap.put("contemporary black & white", new JsonObject().put("styleName", "Black & White").put("styleId", "cbnw"));
    }

    public ShopifyStylePriceParser(String stylePriceFile)
    {
        this.stylePriceFile = stylePriceFile;
        this.productMap = new HashMap<>();
    }

    public JsonObject getStylePrice(String productId, String productName)
    {
        String[] row = this.getProductData(productId, productName);
        if (row == null) return null;

        JsonObject stylePrice = new JsonObject();
        String styleName = row[5].toLowerCase().trim();
        if (styleMap.containsKey(styleName))
        {
            stylePrice = stylePrice.mergeIn(styleMap.get(styleName));
        }
        else
        {
            System.out.println("Could not find style for " + styleName);
        }

        String priceName = row[9].toLowerCase().trim();
        if (priceMap.containsKey(priceName))
        {
            stylePrice = stylePrice.mergeIn(priceMap.get(priceName));
        }
        else
        {
            System.out.println("Could not find price for " + priceName);
            stylePrice = stylePrice.mergeIn(priceMap.get("base"));
        }

        return stylePrice;
    }

    private String[] getProductData(String productId, String productName)
    {
        productId = productId.toLowerCase();
        if (this.productMap.containsKey(productId)) return this.productMap.get(productId);

        productName = productName.toLowerCase();
        if (this.productMap.containsKey(productName)) return this.productMap.get(productName);

        return null;
    }

    public void parse()
    {
        if (this.stylePriceFile == null) return;

        CSVReader reader = this.getCsvReader();
        if (reader == null) return;

        this.getRecord(reader); //Skip the first one
        String[] rowData = null;

        while((rowData = this.getRecord(reader)) != null)
        {
            String id = rowData[2];
            if (id == null || id == "") continue;
            id = id.toLowerCase();
            if (!this.productMap.containsKey(id))
            {
                this.productMap.put(id, rowData);
            }
        }
        this.closeReader(reader);
    }

    private String[] getRecord(CSVReader reader)
    {
        try
        {
            String[] row = reader.readNext();
            if (row != null)
            {
                return row;
            }
        }
        catch (IOException e)
        {
            System.out.println("Error in reading next record. " + e.getMessage());
        }
        return null;
    }

    private void closeReader(CSVReader reader)
    {
        try
        {
            reader.close();
        }
        catch (IOException e)
        {
            System.out.println("Not closing file." + e.getMessage());
        }
    }

    private CSVReader getCsvReader()
    {
        CSVReader reader = null;
        try
        {
            reader = new CSVReader(new FileReader(this.stylePriceFile), '\t');
        }
        catch (FileNotFoundException e)
        {
            System.out.println("File not found at:" + this.stylePriceFile);
        }
        return reader;
    }

    public static void main(String[] args)
    {
        if (args.length != 1)
        {
            System.out.println("Needs input file - components.");
            return;
        }
        String componentsFile = args[0];
        new ShopifyStylePriceParser(componentsFile).parse();
    }
}
