package com.mygubbi.game.proposal.model;

import io.vertx.core.json.JsonObject;

/**
 * Created by user on 24-Apr-17.
 */
public class CloudinaryImageUrl extends JsonObject
{
    public static final String IMAGE_URL="imageurl";

    private String imageurl;

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public JsonObject getcloudinaryJson()
    {
        return new JsonObject().put("imageurl", this.imageurl);
    }

    @Override
    public String toString() {
        return "CloudinaryImageUrl{" +
                "imageurl='" + imageurl + '\'' +
                '}';
    }
}
