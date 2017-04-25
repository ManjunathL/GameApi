package com.mygubbi.game.proposal;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 24-04-2017.
 */
public class CloudinaryImageUpload {

    private final static Logger LOG = LogManager.getLogger(CloudinaryImageUpload.class);
    String uploadedfilepath;

    public String getUploadedfilepath() {
        return uploadedfilepath;
    }

    public void setUploadedfilepath(String uploadedfilepath) {
        this.uploadedfilepath = uploadedfilepath;
    }

    public CloudinaryImageUpload(String filename)
    {
        Map config = new HashMap();
        config.put("cloud_name", "mygubbi");
        config.put("api_key", "492523411154281");
        config.put("api_secret", "Qpr5Vg3klupb-ETRIsj-caLJ5zg");
        Cloudinary cloudinary = new Cloudinary(config);
        try {
            Map uploadParams =  ObjectUtils.asMap("folder", "product_library");
            File file = new File(filename);
            LOG.info("file name" +file.toString());
            Map uploadResult = cloudinary.uploader().upload(file, uploadParams);
            String publicId = (String) uploadResult.get("public_id");
            uploadedfilepath = cloudinary.url().imageTag(publicId);
            uploadedfilepath=uploadedfilepath.substring(10,uploadedfilepath.length()-3);
            this.setUploadedfilepath(uploadedfilepath);
            LOG.debug("Image URL : " + uploadedfilepath);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            LOG.debug("Could not upload image :" + e.getMessage());
        }
    }
}

