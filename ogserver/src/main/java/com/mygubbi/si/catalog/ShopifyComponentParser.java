package com.mygubbi.si.catalog;

import com.opencsv.CSVReader;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by test on 20-01-2016.
 */
public class ShopifyComponentParser
{
    private String componentsFile;
    private Map<String, JsonArray> componentMap;
    private List<String> productIds;
    private static final JsonArray EMPTY_ARRAY = new JsonArray();

    public ShopifyComponentParser(String componentsFile)
    {
        this.componentsFile = componentsFile;
        this.componentMap = new HashMap<>();
        this.productIds = new ArrayList<>();
    }

    public JsonArray getComponents(String productId)
    {
        if (this.componentMap.containsKey(productId)) return this.componentMap.get(productId);
        return EMPTY_ARRAY;
    }

    public void parse()
    {
        if (this.componentsFile == null) return;

        CSVReader reader = this.getCsvReader();
        if (reader == null) return;

        this.getRecord(reader); //Skip the first one
        String[] rowData = null;

        while((rowData = this.getRecord(reader)) != null)
        {
            String id = rowData[1];
            if (id == null || id == "") continue;
            id = id.toLowerCase();
            if (!this.componentMap.containsKey(id))
            {
                this.componentMap.put(id, new JsonArray());
                this.productIds.add(id);
            }
            this.componentMap.get(id).add(this.getComponentJson(rowData));
        }
        this.closeReader(reader);
    }

    private void printAllComponents()
    {
        int i = 1;
        for (String key : this.productIds)
        {
            System.out.println(i + ". Id:" + key + " | " + this.componentMap.get(key).toString());
            i++;
        }
    }

    private JsonObject getComponentJson(String[] rowData)
    {
        String name = rowData[2];
        name = name.substring(name.indexOf(')') + 1).trim();
        return new JsonObject().put("name", name).put("size", rowData[3]).put("qty", this.convertToInt(rowData[4]));
    }

    private int convertToInt(String text)
    {
        try
        {
            return Integer.parseInt(text);
        }
        catch (NumberFormatException e)
        {
            return 1;
        }
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
            reader = new CSVReader(new FileReader(this.componentsFile), '\t');
        }
        catch (FileNotFoundException e)
        {
            System.out.println("File not found at:" + this.componentsFile);
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
        new ShopifyComponentParser(componentsFile).parse();
    }
}
