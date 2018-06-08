package com.mygubbi.game.proposal.Upload;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
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

import java.io.*;

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
    private Cloudinary cloudinary;

    private String CLOUD_NAME = "mygubbi";
    private String API_KEY = "492523411154281";
    private String API_SECRET = "Qpr5Vg3klupb-ETRIsj-caLJ5zg";
    private static String CLOUDINARY_DIRECTORY = ConfigHolder.getInstance().getStringValue("CLOUDINARY_DIRECTORY", "C:\\Users\\Public\\productlibraryuploads");




    public CloudinaryFileUploadHandler(Vertx vertx)
    {
        super(vertx);
        this.route().handler(BodyHandler.create().setUploadsDirectory(CLOUDINARY_DIRECTORY));
        this.post("/").handler(this::fileToCloudinary);
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", CLOUD_NAME,
                "api_key", API_KEY,
                "api_secret", API_SECRET));
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


    private File downloadImage(String fileName)
    {
        String fetch = cloudinary.url().type("fetch").imageTag(fileName);
        File file = new File(CLOUDINARY_DIRECTORY + "/" + fetch);
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;

    }
}
