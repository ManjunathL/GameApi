package com.mygubbi.si.amazonS3;

import com.mygubbi.config.ConfigHolder;
import com.mygubbi.route.RouteUtil;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Created by User on 29-03-2018.
 */
public class S3ImageLoaderService extends AbstractVerticle {

    private Set<String> imageUrlsFromS3 = null;

    private static S3ImageLoaderService INSTANCE;


    @Override
    public void start(Future<Void> startFuture) throws Exception
    {
        this.getAllImagesFromS3();
        startFuture.complete();
    }

    public static S3ImageLoaderService getInstance()
    {
        return INSTANCE;
    }

    private void getAllImagesFromS3() {

        String bucketName = ConfigHolder.getInstance().getStringValue("bucketName","");
        String accessKey = ConfigHolder.getInstance().getStringValue("accessKey","");
        String secretKey = ConfigHolder.getInstance().getStringValue("secretKey","");

        AmazonS3ClientInitializer amazonS3ClientInitializer = new AmazonS3ClientInitializer(bucketName,accessKey,secretKey);

        imageUrlsFromS3 = amazonS3ClientInitializer.getAllImageUrlsFromBucket();

    }


    public String returnImageIfPresent(String image) {

        String imageUrlResponse = null;
        for (int i = 0; i < imageUrlsFromS3.size(); i++) {

            if (imageUrlsFromS3.contains(image)) ;
            {
                imageUrlResponse = image;

            }

        }
        return imageUrlResponse;
    }
}
