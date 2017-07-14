package com.mygubbi.game.proposal.Upload;

import com.mygubbi.config.ConfigHolder;
import com.mygubbi.route.AbstractRouteHandler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Chirag on 15-03-2017.
 */
public class FileUploadHandler extends AbstractRouteHandler {

    private final static Logger LOG = LogManager.getLogger(FileUploadHandler.class);

    private String defaultUrl = "http://" + "designwhimages.mygubbi.com/";


    AmazonS3FileUploadClient amazonS3FileUploadClient;

    public FileUploadHandler(Vertx vertx) {

        super(vertx);
        String amazon_uploads_directory = ConfigHolder.getInstance().getStringValue("amazon_uploads_directory", "c:/Users/Public/uploads");
        LOG.debug("path : " + amazon_uploads_directory);
        this.route().handler(BodyHandler.create().setUploadsDirectory("c:/Users/Public/uploads"));

        this.post("/file").handler(this::fileToAmazon);
        String bucketName = ConfigHolder.getInstance().getStringValue("amazon_s3_bucketname", "designwh");
        amazonS3FileUploadClient = new AmazonS3FileUploadClient(bucketName);

    }

    private void fileToAmazon(RoutingContext routingContext) {
        LOG.info("Inside file upload ");

        routingContext.response().putHeader("Content-Type", "text/plain");

        routingContext.response().setChunked(true);
        JsonArray urlJsonArray = new JsonArray();


        for (FileUpload f : routingContext.fileUploads()) {
            LOG.info("uploading  " + f.fileName() + ":" + f.uploadedFileName());

            amazonS3FileUploadClient.uploadFile(f.fileName(),f.uploadedFileName());
            urlJsonArray.add(defaultUrl+f.fileName());

        }
        sendJsonResponse(routingContext,urlJsonArray.toString());
    }
}
