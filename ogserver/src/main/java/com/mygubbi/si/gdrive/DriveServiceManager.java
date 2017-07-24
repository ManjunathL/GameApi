package com.mygubbi.si.gdrive;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.mygubbi.config.ConfigHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Sunil.
 *
 * Initializes and holds a Drive instance
 */
public class DriveServiceManager
{
    private final static Logger LOG = LogManager.getLogger(DriveServiceManager.class);

    private static volatile boolean initalized = false;

    private String credentialsJson;
    private String APPLICATION_NAME;
    private String localDriveFolder;
    private File DATA_STORE_DIR;
    private FileDataStoreFactory DATA_STORE_FACTORY;
    private final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private HttpTransport HTTP_TRANSPORT;
    private final List<String> SCOPES = Arrays.asList(DriveScopes.DRIVE,
            SheetsScopes.SPREADSHEETS,SheetsScopes.DRIVE);

    private Drive drive;

    public DriveServiceManager()
    {
        this.APPLICATION_NAME = ConfigHolder.getInstance().getStringValue("driveAppName", "GAME Drive App");
        this.credentialsJson = ConfigHolder.getInstance().getStringValue("driveCredentials", "/game/gamedrive.json");
        this.localDriveFolder = ConfigHolder.getInstance().getStringValue("localDriveFolder", "D:/data/gdrive/");


       /* this.APPLICATION_NAME = "GAME Drive App";
        this.credentialsJson = "/game/gamedrive.json";
        this.localDriveFolder = "D:/data/gdrive/";*/
        this.DATA_STORE_DIR = new File(localDriveFolder);

    }

    public Drive getDrive()
    {
        return drive;
    }

    public void init()
    {
        LOG.info("Google drive initialized at start:" + initalized);
        if (initalized)
        {
            return;
        }

        try
        {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
            this.drive = initDriveService();
        }
        catch (Throwable t)
        {
            throw new IllegalStateException("Could not initialize http transport and data store factory", t);
        }
    }

    public void init(boolean flag)
    {
        LOG.info("Google drive initialized at start:" + initalized);

        try
        {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
            this.drive = initDriveService();
        }
        catch (Throwable t)
        {
            throw new IllegalStateException("Could not initialize http transport and data store factory", t);
        }
    }

    /**
     * Creates an authorized Credential object.
     */
    private Credential authorize() throws IOException
    {
        return GoogleCredential
                .fromStream(this.getClass().getResourceAsStream(this.credentialsJson))
                .createScoped(SCOPES);
    }

    private Drive initDriveService() throws IOException
    {
        Credential credential = authorize();
        return new Drive.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}