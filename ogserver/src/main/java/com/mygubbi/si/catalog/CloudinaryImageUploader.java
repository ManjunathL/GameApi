package com.mygubbi.si.catalog;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.cloudinary.utils.StringUtils;
import com.opencsv.CSVReader;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by test on 20-01-2016.
 */
public class CloudinaryImageUploader
{
    private String filename;
    private String CLOUD_NAME = "mygubbi";
    private String API_KEY = "492523411154281";
    private String API_SECRET = "Qpr5Vg3klupb-ETRIsj-caLJ5zg";
    private Cloudinary cloudinary;
    private Map uploadProperties;

    public CloudinaryImageUploader(String filename)
    {
        this.filename = filename;
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", CLOUD_NAME,
                "api_key", API_KEY,
                "api_secret", API_SECRET));
        this.uploadProperties = ObjectUtils.asMap("use_filename", true, "unique_filename", false);
    }

    public void upload()
    {
        CSVReader reader = this.getCsvReader();
        if (reader == null) return;
        this.getRecord(reader); //Skip header
        ShopifyRecord record = null;

        while((record = this.getRecord(reader)) != null)
        {
            this.uploadImage(record.getImageUrl());
        }
        this.closeReader(reader);
    }

    private void uploadImage(String imageUrl)
    {
        if (StringUtils.isEmpty(imageUrl)) return;
        try
        {
            System.out.println("Uploading image:" + imageUrl);
            Map uploadResult = this.cloudinary.uploader().upload(imageUrl, this.uploadProperties);
        }
        catch (Exception e)
        {
            System.out.println("Error in uploading image:" + imageUrl);
            e.printStackTrace();
        }
    }

    private ShopifyRecord getRecord(CSVReader reader)
    {
        try
        {
            String[] row = reader.readNext();
            if (row != null)
            {
                return new ShopifyRecord(row);
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
            reader = new CSVReader(new FileReader(this.filename), '\t');
        }
        catch (FileNotFoundException e)
        {
            System.out.println("File not found at:" + this.filename);
        }
        return reader;
    }

    public static void main(String[] args)
    {
        if (args.length != 1)
        {
            System.out.println("Needs input file.");
            return;
        }
        String filename = args[0];
        new CloudinaryImageUploader(filename).upload();
    }

}
