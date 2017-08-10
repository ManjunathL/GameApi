package com.mygubbi.game.proposal.model;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import junit.framework.TestCase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * Created by Chirag on 16-03-2017.
 */
public class AmazonS3FileUploadTest extends TestCase {


    AmazonS3 s3client;

    String bucketName = "designwh";

    private final static Logger LOG = LogManager.getLogger(AmazonS3FileUploadTest.class);

    public void testValidRecord(){

        AmazonS3FileUploadClient();
        uploadFile();

    }



    public void AmazonS3FileUploadClient() {


        String accessKey = "AKIAJS7FUU6IRYAPHDLQ";
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