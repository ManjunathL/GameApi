package com.mygubbi.game.proposal;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.mygubbi.config.ConfigHolder;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;

/**
 * Created by Chirag on 15-03-2017.
 */


    public class AmazonS3FileUploadClient {

    private final static Logger LOG = LogManager.getLogger(FileUploadHandler.class);

    private String bucketName;

    AmazonS3 s3client;


    public AmazonS3FileUploadClient(String bucketName) {

        this.bucketName = bucketName;

            String accessKey = ConfigHolder.getInstance().getStringValue("amazon_s3_access_key", "AKIAJS7FUU6IRYAPHDLQ") ;
            String secretKey = ConfigHolder.getInstance().getStringValue("amazon_s3_secret_key", "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") ;


            BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKey,secretKey);

            s3client = AmazonS3ClientBuilder.standard().
                    withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials)).withRegion(Regions.AP_SOUTH_1).build();

            if (s3client == null)
            {
                LOG.debug("Could not log in to Amazon s3");
            }

        }

    public void uploadFile(String keyName, String uploadFileName) {
        try {
            System.out.println("Uploading a new object to S3 from a file\n");
            File file = new File(uploadFileName);
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    bucketName, keyName, file);
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

