package com.mygubbi.si.gdrive;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by test on 07-07-2017.
 */
public class GoogleDriveTest
{
    public static void main(String[] args)
    {
        long timer = System.currentTimeMillis();
        new GoogleDriveTest().testUploadFileIntoDrive();
        System.out.println("timer" + (System.currentTimeMillis() - timer));
    }

    private void listFiles()
    {
        System.out.println("Files:" + this.serviceProvider.listFiles());
    }

    public DriveServiceProvider serviceProvider;

    public GoogleDriveTest()
    {
        this.serviceProvider = new DriveServiceProvider();
    }

    private void testUploadAndDownload()
    {
        DriveFile file = this.serviceProvider.uploadFile("D:/Mygubbi GAME/TestCopy12.xlsx","test");
        System.out.println(file);
        this.serviceProvider.downloadFile(file.getId(), "D:/Mygubbi GAME/TestCopy12Downloaded.xlsx", DriveServiceProvider.TYPE_XLS);
    }

    private void testUploadXLSAndDownloadPDF()
    {
        DriveFile file = this.serviceProvider.uploadFileForUser("C:\\Users\\Public\\game_files\\5010\\sow.xlsx","chiragsharath@gmail.com","","sow_checklist.xls","");
        System.out.println(file);
        this.serviceProvider.downloadFile(file.getId(), "C:\\Users\\Public\\game_files\\5010\\sow_checklist_falsenoquotes.pdf", DriveServiceProvider.TYPE_PDF);
    }
    private void testUpload()
    {
        DriveFile file = this.serviceProvider.uploadFile("D:/Mygubbi GAME/TestCopy12.xlsx","test");
        System.out.println(file);
        this.serviceProvider.allowUserToEditFile(file.getId(), "guttulasunil@gmail.com");
        System.out.println("Permission granted to edit file - " + file.getName());
    }

    private void testUploadForUser()
    {
        DriveFile file = this.serviceProvider.uploadFileForUser("D:/Mygubbi GAME/TestCopy12.xlsx","chiragsharath@gmail.com","test","shilpa.g@mygubbi.com","yes");
        System.out.println(file);
        System.out.println("Permission granted to edit file - " + file.getName());

    }

    private void testUploadFileIntoDrive()
    {
        List<String> filepaths = new ArrayList<>();
        filepaths.add("C:\\Users\\Public\\game_files\\5010\\sow.xlsx");
        filepaths.add("C:\\Users\\Public\\game_files\\5010\\sow2.xlsx");
        //filepaths.add("C:\\Users\\Public\\game_files\\5010\\sow_checklist_falsenoquotes.pdf");
        DriveFile file = this.serviceProvider.createFolder(filepaths,"chiragtest","chiragsharath@gmail.com");
        System.out.print("WEBviewLink :" + file.getWebContentLink() + " : " + file.getWebViewLink());

    }
}
