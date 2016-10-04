package com.mygubbi.si.firebase;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.config.ConfigHolder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Sunil on 04-10-2016.
 */
public class FirebaseLifecycleManager
{
    private static boolean initalized = false;
    private String servicesJsonFile;
    private String fbUrl;

    public FirebaseLifecycleManager()
    {
        this.servicesJsonFile = ConfigHolder.getInstance().getStringValue("fbServicesJson", null);
        this.fbUrl = ConfigHolder.getInstance().getStringValue("fbUrl", null);
    }

    public FirebaseLifecycleManager(String servicesJsonFile, String fbUrl)
    {
        this.servicesJsonFile = servicesJsonFile;
        this.fbUrl = fbUrl;
    }

    public synchronized void init()
    {
        if (initalized) return;

        if (servicesJsonFile == null || fbUrl == null)
        {
            throw new RuntimeException("Config not setup for Firebase : " + servicesJsonFile + " | " + fbUrl);
        }
        InputStream in = null;
        try
        {
            in = new ByteArrayInputStream(VertxInstance.get().fileSystem().readFileBlocking(servicesJsonFile).getBytes());
            //in = new FileInputStream(servicesJsonFile);
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setServiceAccount(in)
                    .setDatabaseUrl(fbUrl)
                    .build();
            FirebaseApp.initializeApp(options);
            System.out.println("Firebase connected.");
        }
        catch (Exception e)
        {
            throw new RuntimeException("File could not be opened at:" + servicesJsonFile, e);
        }
        finally
        {
            if (in != null)
            {
                try
                {
                    in.close();
                }
                catch (IOException e)
                {
                    //Nothing to do
                }
            }
        }
        initalized = true;
    }

}
