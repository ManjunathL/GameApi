package com.mygubbi.si.catalog;

import com.mygubbi.ServerVerticle;
import com.mygubbi.catalog.ProductJson;
import com.mygubbi.catalog.ProductManagementService;
import com.mygubbi.common.LocalCache;
import com.mygubbi.common.StringUtils;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import io.vertx.core.AsyncResult;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FileCatalogManager
{
    private final static Logger LOG = LogManager.getLogger(FileCatalogManager.class);

    private String downloadFile;
    private String uploadFile;
    private String configFiles;

    private volatile int recordsToLoad = 0;
    private volatile int recordsLoaded = 0;

    public static void main(String[] args)
    {
        if (args.length == 3)
        {
            new FileCatalogManager(args[0], args[1], args[2]).start(); //Config files can be passed in as comma delimited set
        }
        else if (args.length == 2)
        {
            new FileCatalogManager(args[0], args[1], null).start(); //Config files can be passed in as comma delimited set
        }
    }

    public FileCatalogManager(String configFiles, String downloadFile, String uploadFile)
    {
        this.configFiles = configFiles;
        this.downloadFile = downloadFile;
        this.uploadFile = uploadFile;
    }

    private void start()
    {
        VertxInstance.get().deployVerticle(new ServerVerticle(this.configFiles), new DeploymentOptions().setWorker(true), result ->
        {
            if (result.succeeded())
            {
                LOG.info("Server started.");
                this.downloadProducts();
            }
            else
            {
                LOG.error("Server did not start. Message:" + result.result(), result.cause());
                System.exit(1);
            }
        });
    }

    private void downloadProducts()
    {
        Integer id = LocalCache.getInstance().store(new QueryData("product.select.all", new JsonObject()));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> result) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(result.result().body());
                    if (selectData == null || selectData.rows == null)
                    {
                        LOG.error("Could not fetch products from DB. Message:" + result.result(), result.cause());
                        System.exit(1);
                    }
                    else
                    {
                        VertxInstance.get().fileSystem().writeFile(downloadFile, Buffer.buffer(selectData.getJsonDataRows("productJson").toString()), fileResult ->
                        {
                            if (fileResult.succeeded())
                            {
                                LOG.info("{Product file downloaded:" + downloadFile);
                                this.uploadProducts();
                            }
                            else
                            {
                                LOG.error("Product file not downloaded.", result.cause());
                                System.exit(1);
                            }

                        });
                    }
                });
    }

    private void uploadProducts()
    {
        if (StringUtils.isEmpty(this.uploadFile))
        {
            System.exit(1);
        }
        LOG.info("Uploading products from :" + this.uploadFile);

        VertxInstance.get().fileSystem().readFile(uploadFile, fileResult ->
        {
            if (fileResult.failed())
            {
                LOG.error("Not able to read file: " + uploadFile, fileResult.cause());
                System.exit(1);
            }
            JsonArray productsJson = new JsonArray(fileResult.result().toString());
            if (productsJson.isEmpty())
            {
                LOG.info("No records to load, bringing down server.");
                System.exit(-1);
            }
            recordsToLoad = productsJson.size();

            for (Object productText : productsJson)
            {
                ProductJson productJson = new ProductJson((JsonObject) productText);
                Integer id = LocalCache.getInstance().store(productJson);
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

                            recordsLoaded++;
                            if (recordsToLoad == recordsLoaded)
                            {
                                LOG.info("All records processed, bringing down server.");
                                System.exit(-1);
                            }

                        });

            }
        });

    }

}
