package com.mygubbi.si.gdrive;

import com.google.api.services.drive.model.File;

/**
 * Created by test on 07-07-2017.
 */
public class DriveFile
{
    private String name;
    private String webContentLink;
    private String webViewLink;
    private String id;

    public DriveFile(File driveFile)
    {
        this.setName(driveFile.getName()).setId(driveFile.getId()).setWebContentLink(driveFile.getWebContentLink())
                .setWebViewLink(driveFile.getWebViewLink());
    }

    public String getName()
    {
        return name;
    }

    public DriveFile setName(String name)
    {
        this.name = name;
        return this;
    }

    public String getWebContentLink()
    {
        return webContentLink;
    }

    public DriveFile setWebContentLink(String webContentLink)
    {
        this.webContentLink = webContentLink;
        return this;
    }

    public String getWebViewLink()
    {
        return webViewLink;
    }

    public DriveFile setWebViewLink(String webViewLink)
    {
        this.webViewLink = webViewLink;
        return this;
    }

    public String getId()
    {
        return id;
    }

    public DriveFile setId(String id)
    {
        this.id = id;
        return this;
    }

    @Override
    public String toString()
    {
        return "DriveFile{" +
                "name='" + name + '\'' +
                ", webContentLink='" + webContentLink + '\'' +
                ", webViewLink='" + webViewLink + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
