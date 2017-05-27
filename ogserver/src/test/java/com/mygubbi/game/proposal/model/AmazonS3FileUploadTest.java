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

import java.io.File;

/**
 * Created by Chirag on 16-03-2017.
 */
public class AmazonS3FileUploadTest extends TestCase {


    AmazonS3 s3client;

    String bucketName = "designwh";

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

        } catch (AmazonClientException ace) {

        }
    }

}
