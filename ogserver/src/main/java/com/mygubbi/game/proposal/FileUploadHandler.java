package com.mygubbi.game.proposal;

import com.amazonaws.regions.Regions;
import com.mygubbi.config.ConfigHolder;
import com.mygubbi.route.AbstractRouteHandler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Created by Chirag on 15-03-2017.
 */
public class FileUploadHandler extends AbstractRouteHandler {

    private final static Logger LOG = LogManager.getLogger(FileUploadHandler.class);

    private JsonArray urlJsonArray = null;
    private String defaultUrl = "https://" + ConfigHolder.getInstance().getStringValue("amazon_s3_bucketname", "designwh") + ".s3-" + Regions.AP_SOUTH_1 + ".amazonaws.com/";


    AmazonS3FileUploadClient amazonS3FileUploadClient;

    public FileUploadHandler(Vertx vertx) {
        super(vertx);
        this.route().handler(BodyHandler.create());
        this.route().handler(BodyHandler.create().setUploadsDirectory(ConfigHolder.getInstance().getStringValue("amazon_uploads_directory", "/mnt/uploads")));

        this.post("/").handler(this::fileToAmazon);
        String bucketName = ConfigHolder.getInstance().getStringValue("amazon_s3_bucketname", "designwh");
        amazonS3FileUploadClient = new AmazonS3FileUploadClient(bucketName);

    }

    private void fileToAmazon(RoutingContext routingContext) {
        routingContext.response().putHeader("Content-Type", "text/plain");

        routingContext.response().setChunked(true);

        for (FileUpload f : routingContext.fileUploads()) {

            amazonS3FileUploadClient.uploadFile(f.fileName(),f.fileName());
            urlJsonArray.add(defaultUrl+f.fileName());

        }
        sendJsonResponse(routingContext,urlJsonArray.toString());

        routingContext.response().end();
    }

}
