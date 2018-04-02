package com.mygubbi.game.proposal.model;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import junit.framework.TestCase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chirag on 16-03-2017.
 */
public class AmazonS3FileUploadTest extends TestCase {


    AmazonS3 s3client;

    String bucketName = "designwh";

    private final static Logger LOG = LogManager.getLogger(AmazonS3FileUploadTest.class);

    public void testValidRecord(){

        AmazonS3FileUploadClient();
//        uploadFile();
        List<String> objectslistFromFolder = getObjectslistFromFolder("design-wh", "");
        System.out.print(objectslistFromFolder.size());
        objectslistFromFolder.forEach(v->{
            String baseUrl = "http://designwhimages.mygubbi.com/";
            try {
                System.out.println(baseUrl+ URLEncoder.encode(v, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });
    }


        public List<String> getObjectslistFromFolder(String bucketName, String folderKey) {

        ListObjectsRequest listObjectsRequest =
                new ListObjectsRequest()
                        .withBucketName(bucketName);

        List<String> keys = new ArrayList<>();

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




    public void AmazonS3FileUploadClient() {


        String accessKey = "AKIAIYWIAMY6EVGV4DQA";
              String secretKey = "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";

        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKey, secretKey);

        s3client = AmazonS3ClientBuilder.standard().
                withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials)).withRegion(Regions.AP_SOUTH_1).build();

        if (s3client == null) {
            LOG.debug("Could not log in to Amazon s3");
        }


    }

    public void uploadFile() {
        try {
            System.out.println("Uploading a new object to S3 from a file\n");
            File file = new File("D:\\WareHouse\\the plan.txt");
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    bucketName, "theplan", file);
            putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
            s3client.putObject(putObjectRequest);

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
