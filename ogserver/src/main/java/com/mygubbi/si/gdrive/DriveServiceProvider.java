package com.mygubbi.si.gdrive;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by test on 07-07-2017.
 */
public class DriveServiceProvider
{
    private final static Logger LOG = LogManager.getLogger(DriveServiceProvider.class);

    public static final String TYPE_XLS = "xls";
    public static final String TYPE_DOC = "doc";
    public static final String TYPE_CSV = "csv";
    public static final String TYPE_PDF = "pdf";

    private Map<String, String > mimeTypes = null;
    private DriveServiceManager serviceManager;

    public DriveServiceProvider()
    {
        this.serviceManager = new DriveServiceManager();
        this.serviceManager.init();
        this.initMimeTypes();
    }

    public DriveServiceProvider(boolean flag)
    {
        this.serviceManager = new DriveServiceManager();
        this.serviceManager.init(flag);
        this.initMimeTypes();
    }

    private void initMimeTypes()
    {
        this.mimeTypes = new HashMap<>();
        this.mimeTypes.put(TYPE_XLS, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        this.mimeTypes.put(TYPE_DOC, "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        this.mimeTypes.put(TYPE_CSV, "text/csv");
        this.mimeTypes.put(TYPE_PDF,"application/pdf");
    }

    public DriveFile createFolder(List<UploadToDrive> filePaths, String folderName, String userId)
    {
        this.serviceManager.getDrive();
        File folderMetadata = new File();
        folderMetadata.setName(folderName);
        folderMetadata.setMimeType("application/vnd.google-apps.folder");
        String folderId = null;

        LOG.debug("FOlder created");

        File folder = null;
        try {
            folder = this.serviceManager.getDrive().files().create(folderMetadata)
                    .setFields("id,name,webContentLink,webViewLink")
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!(folder == null))
        {
            folderId = folder.getId();
        }

        LOG.debug("After folder creation :" + folder.getId());

        allowUserToEditFile(folderId,userId);

        for (UploadToDrive fileNames : filePaths)
        {
            File fileExcel = new File();
            fileExcel.setName(fileNames.getFileName());
            fileExcel.setMimeType("application/vnd.google-apps.spreadsheet");
            fileExcel.setParents(Collections.singletonList(folderId));
            java.io.File filePath = new java.io.File(fileNames.getFilePath());
            FileContent mediaContent = new FileContent("application/vnd.ms-excel", filePath);
            try {

                File file = this.serviceManager.getDrive().files().create(fileExcel, mediaContent)
                        .setFields("id, parents,webViewLink")
                        .execute();
                allowUserToEditFile(file.getId(),userId);
                LOG.debug("File created:" + fileNames.getFilePath() + " with web link: " + file.getWebViewLink());

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return new DriveFile(folder);
    }

    public DriveFile uploadFile(String filePath, String filename)
    {
        LOG.debug("File Path :" + filePath + "fileName :" + filename);
        this.serviceManager.getDrive();
        File fileMetadata = new File();
        fileMetadata.setName(filename);
        fileMetadata.setMimeType("application/vnd.google-apps.spreadsheet");
        //convert' => true,
        //'uploadType' => 'multipart',

        FileContent mediaContent = new FileContent("application/vnd.ms-excel", new java.io.File(filePath));
        File file = null;
        try
        {
            file = this.serviceManager.getDrive().files().create(fileMetadata, mediaContent)
                    .setFields("id,name,webContentLink,webViewLink").execute();
        }
        catch (IOException e)
        {
            LOG.debug("Exception found : " + e.getMessage() + " : " + e.getStackTrace().toString());
            throw new RuntimeException("Couldn't create drive file for " + filePath, e.getCause());
        }
        return new DriveFile(file);
    }

    public void downloadAndDelete(String id, String path, String mimeType)
    {
        this.downloadFile(id, path, mimeType);
        this.deleteFile(id);
    }

    public void deleteFile(String id)
    {
        try
        {
            this.serviceManager.getDrive().files().delete(id);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Not able to delete file with id " + id, e);
        }
    }

    public void downloadFile(String id, String path, String mimeType)
    {
        LOG.debug("Inisde download file :" + id + " : " + path + " : " + mimeType) ;
        try
        {
            LOG.debug("inisde download file");
            FileOutputStream outputStream = new FileOutputStream(path);
            if (this.mimeTypes.containsKey(mimeType))
            {
                LOG.debug("inisde if file");
                this.serviceManager.getDrive().files().export(id, this.mimeTypes.get(mimeType))
                        .set("portrait", false).set("size","A4").set("fitw",true).set("gridlines",false).executeMediaAndDownloadTo(outputStream);
            }
            else
            {
                this.serviceManager.getDrive().files().get(id).executeMediaAndDownloadTo(outputStream);
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException("Could not download file " + id + " to " + path, e.getCause());
        }
    }

    public List<DriveFile> listFiles()
    {
        FileList result = null;
        try
        {
            result = this.serviceManager.getDrive().files().list()
                    .setPageSize(10)
                    .setFields("nextPageToken, files(id, name)")
                    .execute();
        }
        catch (IOException e)
        {
            LOG.error("Error in getting files", e);
            return Collections.emptyList();
        }

        List<com.google.api.services.drive.model.File> files = result.getFiles();
        if (files == null || files.size() == 0)
        {
            LOG.info("No files found");
            return Collections.emptyList();
        }
        else
        {
            return files.stream().map(file -> new DriveFile(file)).collect(Collectors.toList());
        }
    }

    public DriveFile uploadFileForUser(String filePath, String email, String fileName, String salesEmail, String readOnlyFlag)
    {
        LOG.debug("filePath :" + filePath + ":" + email + ":" +fileName + " : " + readOnlyFlag);
        DriveFile driveFile = this.uploadFile(filePath, fileName);
        try {
            Thread.sleep(10*1000);
            if (readOnlyFlag.equalsIgnoreCase("yes"))
            {
                this.allowUserToReadFile(driveFile.getId(), email);
                this.allowUserToReadFile(driveFile.getId(), salesEmail);
            }
            else
            {
                this.allowUserToEditFile(driveFile.getId(), email);
                this.allowUserToEditFile(driveFile.getId(), salesEmail);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return driveFile;
    }

    public void allowUserToEditFile(String id, String email)
    {
        LOG.debug("Allow user to edit file :" + email);
        Permission userPermission = new Permission()
                .setType("user")
                .setRole("writer")
                .setEmailAddress(email);
        try
        {
            this.serviceManager.getDrive().permissions().create(id, userPermission).setSendNotificationEmail(false)
                    .setFields("id").execute();
        }
        catch (IOException e)
        {
            throw new RuntimeException("Unable to make user " + email + " to edit file " + id,e);
        }
    }

    public void allowUserToReadFile(String id, String email)
    {
        LOG.debug("Allow user to read file :" + email + "| folderid:" + id);

        Permission userPermission = new Permission()
                .setType("user")
                .setRole("reader")
                .setEmailAddress(email);
        try
        {
            this.serviceManager.getDrive().permissions().create(id, userPermission).setSendNotificationEmail(false)
                    .setFields("id").execute();
        }
        catch (IOException e)
        {
            throw new RuntimeException("Unable to make user " + email + " to edit file " + id,e);
        }
    }
}
