package com.mygubbi.game.proposal.model;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import junit.framework.TestCase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 24-04-2017.
 */
public class CloudinaryImageUploadTest extends TestCase {

    private final static Logger LOG = LogManager.getLogger(CloudinaryImageUploadTest.class);


    public String imageUrl = "E://d drive//Mygubbi//Images//KitchenPlan3.jpg";

    public void testValidRecord() {

        uploadImage(imageUrl);
    }


    public void uploadImage(String filename) {
        Map config = new HashMap();
        config.put("cloud_name", "mygubbi");
        config.put("api_key", "492523411154281");
        config.put("api_secret", "Qpr5Vg3klupb-ETRIsj-caLJ5zg");
        Cloudinary cloudinary = new Cloudinary(config);
        try {
            Map uploadParams =  ObjectUtils.asMap("folder", "product_library");
            Map uploadResult = cloudinary.uploader().upload(new File(filename), uploadParams);
            String publicId = (String) uploadResult.get("public_id");
            String uploadedfilepath = cloudinary.url().imageTag(publicId);
            LOG.debug("Image URL : " + uploadedfilepath);
        } catch (IOException e) {
            e.printStackTrace();
            LOG.debug("Could not upload image :" + e.getMessage());
        }
    }
}
