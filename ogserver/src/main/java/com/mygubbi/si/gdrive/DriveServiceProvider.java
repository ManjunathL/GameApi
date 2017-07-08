package com.mygubbi.si.gdrive;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;
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

    private Map<String, String > mimeTypes = null;
    private DriveServiceManager serviceManager;

    public DriveServiceProvider()
    {
        this.serviceManager = new DriveServiceManager();
        this.serviceManager.init();
        this.initMimeTypes();
    }

    private void initMimeTypes()
    {
        this.mimeTypes = new HashMap<>();
        this.mimeTypes.put(TYPE_XLS, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        this.mimeTypes.put(TYPE_DOC, "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        this.mimeTypes.put(TYPE_CSV, "text/csv");
    }

    public DriveFile uploadFile(String filePath)
    {
        this.serviceManager.getDrive();
        File fileMetadata = new File();
        fileMetadata.setName("My Report");
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
            throw new RuntimeException("Couldn't create drive file for " + filePath, e);
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
        try
        {
            FileOutputStream outputStream = new FileOutputStream(path);
            if (this.mimeTypes.containsKey(mimeType))
            {
                this.serviceManager.getDrive().files().export(id, this.mimeTypes.get(mimeType))
                        .executeMediaAndDownloadTo(outputStream);
            }
            else
            {
                this.serviceManager.getDrive().files().get(id).executeMediaAndDownloadTo(outputStream);
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException("Could not download file " + id + " to " + path, e);
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

    public void allowUserToEditFile(String id, String email)
    {
        Permission userPermission = new Permission()
                .setType("user")
                .setRole("writer")
                .setEmailAddress(email);
        try
        {
            this.serviceManager.getDrive().permissions().create(id, userPermission)
                    .setFields("id").execute();
        }
        catch (IOException e)
        {
            throw new RuntimeException("Unable to make user " + email + " to edit file " + id);
        }
    }
}
