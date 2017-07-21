package com.mygubbi.game.proposal.Upload;

import com.mygubbi.config.ConfigHolder;
import com.mygubbi.game.proposal.model.CloudinaryImageUrl;
import com.mygubbi.route.AbstractRouteHandler;
import com.mygubbi.si.catalog.CloudinaryImageUploader;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by shruthi on 20-Apr-17.
 */
public class CloudinaryFileUploadHandler extends AbstractRouteHandler
{
    private final static Logger LOG = LogManager.getLogger(CloudinaryFileUploadHandler.class);
    private JsonObject urlJsonobject = null;
    private String defaultUrl = "";
    CloudinaryImageUploader cloudinaryImageUploader;
    CloudinaryImageUpload cloudinaryImageUpload;


    public CloudinaryFileUploadHandler(Vertx vertx)
    {
        super(vertx);
        this.route().handler(BodyHandler.create().setUploadsDirectory(ConfigHolder.getInstance().getStringValue("cloudinary_uploads_directory", "C:\\Users\\Public\\productlibraryuploads")));
        this.post("/").handler(this::fileToCloudinary);
    }
    private void fileToCloudinary(RoutingContext routingContext)
    {
        routingContext.response().putHeader("Content-Type", "text/plain");
        routingContext.response().setChunked(true);
        JsonObject imageurl=routingContext.getBodyAsJson();
        String filename = imageurl.getString("imageurl");
        cloudinaryImageUpload=new CloudinaryImageUpload(filename);
        CloudinaryImageUrl cloudinaryImageUrl=new CloudinaryImageUrl();
        cloudinaryImageUrl.setImageurl(cloudinaryImageUpload.getUploadedfilepath());
        JsonObject jsonObject=new JsonObject();
        jsonObject.put("imageurl" ,cloudinaryImageUpload.getUploadedfilepath());
        LOG.info("json object " +jsonObject.toString());
        sendJsonResponse(routingContext,jsonObject.toString());
    }
}
