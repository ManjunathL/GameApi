package com.mygubbi.si.amazonS3;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.mygubbi.config.ConfigHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Chirag on 15-03-2017.
 */


    public class AmazonS3ClientInitializer {

    private final static Logger LOG = LogManager.getLogger(AmazonS3ClientInitializer.class);

    private String bucketName;

    AmazonS3 s3client;


    public AmazonS3ClientInitializer(String bucketName, String accessKey, String secretKey) {

        this.bucketName = bucketName;

            BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKey,secretKey);

            s3client = AmazonS3ClientBuilder.standard().
                    withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials)).withRegion(Regions.AP_SOUTH_1).build();

            if (s3client == null)
            {
                LOG.debug("Could not log in to Amazon s3");
            }

        }

    public Set<String> getAllImageUrlsFromBucket() {


        ListObjectsRequest listObjectsRequest =
                new ListObjectsRequest()
                        .withBucketName(bucketName);

        Set<String> keys = new HashSet<>();

        ObjectListing objects = s3client.listObjects(listObjectsRequest);
        for (;;) {
            List<S3ObjectSummary> summaries = objects.getObjectSummaries();
            if (summaries.size() < 1) {
                break;
            }
            summaries.forEach(s -> keys.add(s.getKey()));
            objects = s3client.listNextBatchOfObjects(objects);
        }

        return keys;
    }

    public void uploadFile(String keyName, String uploadFileName) {
        try {
            System.out.println("Uploading a new object to S3 from a file\n");
            File file = new File(uploadFileName);
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    bucketName, keyName, file);
            putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
            PutObjectResult result=s3client.putObject(putObjectRequest);
            LOG.info("result file " +result.getETag() + " ---> " +result);
        } catch (AmazonServiceException ase) {
            LOG.error("Caught an AmazonServiceException, which " +
                    "means your request made it " +
                    "to Amazon S3, but was rejected with an error response" +
                    " for some reason.");
            LOG.error("Error Message:    " + ase.getMessage());
            LOG.error("HTTP Status Code: " + ase.getStatusCode());
            LOG.error("AWS Error Code:   " + ase.getErrorCode());
            LOG.error("Error Type:       " + ase.getErrorType());
            LOG.error("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            LOG.debug("Caught an AmazonClientException, which " +
                    "means the client encountered " +
                    "an internal error while trying to " +
                    "communicate with S3, " +
                    "such as not being able to access the network.");
            LOG.error("Error Message: " + ace.getMessage());
        }
    }

}

