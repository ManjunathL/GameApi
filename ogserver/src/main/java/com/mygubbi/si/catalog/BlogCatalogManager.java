package com.mygubbi.si.catalog;

import com.mygubbi.ServerVerticle;
import com.mygubbi.catalog.BlogJson;
import com.mygubbi.catalog.BlogManagementService;
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
import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Mehbub on 18-08-2016.
 */
public class BlogCatalogManager {

    private final static Logger LOG = LogManager.getLogger(BlogCatalogManager.class);

    private String downloadFile;
    private String uploadFile;
    private String configFiles;

    private volatile int recordsToLoad = 0;
    private volatile int recordsLoaded = 0;

    public static void main(String[] args)
    {
        if (args.length == 3)
        {
            new BlogCatalogManager(args[0], args[1], args[2]).start(); //Config files can be passed in as comma delimited set
        }
        else if (args.length == 2)
        {
            new BlogCatalogManager(args[0], args[1], null).start(); //Config files can be passed in as comma delimited set
        }
    }

    public BlogCatalogManager(String configFiles, String downloadFile, String uploadFile)
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
                this.downloadBlogs();
            }
            else
            {
                LOG.error("Server did not start. Message:" + result.result(), result.cause());
                System.exit(1);
            }
        });
    }

    private void downloadBlogs()
    {
        Integer id = LocalCache.getInstance().store(new QueryData("blog.select.all", new JsonObject()));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> result) -> {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(result.result().body());
                    if (selectData == null || selectData.rows == null)
                    {
                        LOG.error("Could not fetch blogs from DB. Message:" + result.result(), result.cause());
                        System.exit(1);
                    }
                    else
                    {
                        VertxInstance.get().fileSystem().writeFile(downloadFile, Buffer.buffer(selectData.getJsonDataRows("blogJson").toString()), fileResult ->
                        {
                            if (fileResult.succeeded())
                            {
                                LOG.info("{Blog file downloaded:" + downloadFile);
                                this.uploadBlogs();
                            }
                            else
                            {
                                LOG.error("Blog file not downloaded.", result.cause());
                                System.exit(1);
                            }

                        });
                    }
                });
    }

    private void uploadBlogs()
    {
        if (StringUtils.isEmpty(this.uploadFile))
        {
            System.exit(1);
        }
        LOG.info("Uploading blogs from :" + this.uploadFile);

        VertxInstance.get().fileSystem().readFile(uploadFile, fileResult ->
        {
            if (fileResult.failed())
            {
                LOG.error("Not able to read file: " + uploadFile, fileResult.cause());
                System.exit(1);
            }
            String jsonText = this.sanitizeBuffer(fileResult.result());
            LOG.info("Json Data: " + jsonText);
            if (jsonText == null)
            {
                LOG.info("Could not read data from file, bringing down server.");
                System.exit(-1);
            }
            JsonArray blogsJson = new JsonArray(jsonText);
            if (blogsJson.isEmpty())
            {
                LOG.info("No records to load, bringing down server.");
                System.exit(-1);
            }
            else
            {
                LOG.info("Records to load: " + blogsJson.size());
            }

            recordsToLoad = blogsJson.size();

            for (Object productText : blogsJson)
            {
                BlogJson blogJson = new BlogJson((JsonObject) productText);
                Integer id = LocalCache.getInstance().store(blogJson);
                VertxInstance.get().eventBus().send(BlogManagementService.CREATE_BLOG, id,
                        (AsyncResult<Message<String>> result) -> {
                            if (result.failed())
                            {
                                LOG.error("error is here", result.result());
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

    private String sanitizeBuffer(Buffer buffer)
    {
        String defaultEncoding = "UTF-8";
        InputStream inputStream = new ByteArrayInputStream(buffer.getBytes());
        try {
            BOMInputStream bOMInputStream = new BOMInputStream(inputStream);
            ByteOrderMark bom = bOMInputStream.getBOM();
            String charsetName = bom == null ? defaultEncoding : bom.getCharsetName();
            return IOUtils.toString(new BufferedInputStream(bOMInputStream), charsetName);
        }
        catch (Exception ex)
        {
            LOG.error("Error in the input stream. " + buffer.toString(), ex);
            return null;
        }
        finally
        {
            try
            {
                inputStream.close();
            }
            catch (IOException e)
            {
                //Ignore
            }
        }
    }

}
