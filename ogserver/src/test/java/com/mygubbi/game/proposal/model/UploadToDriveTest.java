package com.mygubbi.game.proposal.model;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.model.File;

import java.util.Collections;


/**
 * Created by User on 28-06-2017.
 */
public class UploadToDriveTest {


    public void testValidRecord()
    {
        /*File fileMetadata = new File();
        fileMetadata.setName("My Report");
        fileMetadata.setMimeType("application/vnd.google-apps.spreadsheet");

        java.io.File filePath = new java.io.File("files/report.csv");
        FileContent mediaContent = new FileContent("text/csv", filePath);
        File file = driveService.files().create(fileMetadata, mediaContent)
                .setFields("id")
                .execute();
        System.out.println("File ID: " + file.getId());*/



       /* String folderId = "0BwwA4oUTeiV1TGRPeTVjaWRDY1E";
        File fileMetadata = new File();
        fileMetadata.setName("photo.jpg");
        fileMetadata.setParents(Collections.singletonList(folderId));
        java.io.File filePath = new java.io.File("files/photo.jpg");
        FileContent mediaContent = new FileContent("image/jpeg", filePath);
        File file = driveService.files().create(fileMetadata, mediaContent)
                .setFields("id, parents")
                .execute();
        System.out.println("File ID: " + file.getId());*/
    }



}
