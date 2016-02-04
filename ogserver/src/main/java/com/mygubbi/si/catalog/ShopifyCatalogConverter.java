package com.mygubbi.si.catalog;

import com.mygubbi.ServerVerticle;
import com.mygubbi.catalog.ProductJson;
import com.mygubbi.catalog.ProductManagementService;
import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.si.firebase.FirebaseObjectWriter;
import com.opencsv.CSVReader;
import io.vertx.core.AsyncResult;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.eventbus.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by test on 20-01-2016.
 */
public class ShopifyCatalogConverter
{
    private final static Logger LOG = LogManager.getLogger(ShopifyCatalogConverter.class);

    private String productsFile;
    private String componentsFile;
    private String stylePriceFile;
    private ShopifyComponentParser componentParser;
    private ShopifyStylePriceParser stylePriceParser;

    public ShopifyCatalogConverter(String productsFile, String componentsFile, String stylePriceFile)
    {
        this.productsFile = productsFile;
        this.componentsFile = componentsFile;
        this.stylePriceFile = stylePriceFile;
        this.componentParser = new ShopifyComponentParser(this.componentsFile);
        this.stylePriceParser = new ShopifyStylePriceParser(this.stylePriceFile);
    }

    public void parse()
    {
        this.componentParser.parse();
        this.stylePriceParser.parse();

        CSVReader reader = this.getCsvReader();
        if (reader == null) return;

        ProductJson product = null;

        this.skipFirstRecord(reader);
        ShopifyRecord record = null;

        while((record = this.getRecord(reader)) != null)
        {
            if (product != null && record.getId().equals(product.getProductId()))
            {
                this.mergeToProduct(product, record);
            }
            else
            {
                this.storeProduct(product);
                product = this.createNewProduct(record);
            }
        }
        this.storeProduct(product);
        this.closeReader(reader);
        LOG.info("Products loaded.");
    }

    private void skipFirstRecord(CSVReader reader)
    {
        try
        {
            reader.readNext(); //Skip the first one
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void mergeToProduct(ProductJson product, ShopifyRecord record)
    {
        product.addMFP(record.getMFP());
        product.addImage(record.getImage());
    }

    private ProductJson createNewProduct(ShopifyRecord record)
    {
        LOG.info("Record:" + record);
        return new ProductJson(record);
    }

    private void storeProduct(ProductJson product)
    {
        if (product == null) return;
        LOG.info("Storing product:" + product.getString("name"));
//        LOG.info(product.getShortJson().toString());
//        if (true) return;

        Integer id = LocalCache.getInstance().store(product);
        VertxInstance.get().eventBus().send(ProductManagementService.CREATE_PRODUCT, id,
                (AsyncResult<Message<String>> result) -> {
                    if (result.failed())
                    {
                        LOG.error(result.result());
                    }
                    else
                    {
                        LOG.info(result.result());
                    }
                });
    }

    private ShopifyRecord getRecord(CSVReader reader)
    {
        try
        {
            String[] row = reader.readNext();
            if (row != null)
            {
                return new ShopifyRecord(row, this.componentParser, this.stylePriceParser);
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
            reader = new CSVReader(new FileReader(this.productsFile), '\t');
        }
        catch (FileNotFoundException e)
        {
            System.out.println("File not found at:" + this.productsFile);
        }
        return reader;
    }

    public static void main(String[] args)
    {
        if (args.length != 3)
        {
            System.out.println("Needs 3 input files - products, components and styleprice.");
            return;
        }
        String productsFile = args[0];
        String componentsFile = args[1];
        String stylePriceFile = args[2];

        //new ShopifyCatalogConverter(productsFile, componentsFile, stylePriceFile).parse();
        //if (true) return;

        VertxInstance.get().deployVerticle(new ServerVerticle("config/conf.local.json"), new DeploymentOptions().setWorker(true), result ->
        {
            if (result.succeeded())
            {
                LOG.info("Server started.");
                new ShopifyCatalogConverter(productsFile, componentsFile, stylePriceFile).parse();
            }
            else
            {
                LOG.error("Server did not start. Message:" + result.result(), result.cause());
                System.exit(1);
            }
        });

    }
}
