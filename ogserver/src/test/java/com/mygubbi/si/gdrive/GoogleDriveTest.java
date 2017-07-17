package com.mygubbi.si.gdrive;

/**
 * Created by test on 07-07-2017.
 */
public class GoogleDriveTest
{
    public static void main(String[] args)
    {
        new GoogleDriveTest().testUploadXLSAndDownloadPDF();
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
        DriveFile file = this.serviceProvider.uploadFileForUser("/home/shilpa/Downloads","chiragsharath@gmail.com","sow_checklist.xls");
        System.out.println(file);
        this.serviceProvider.downloadFile(file.getId(), "/home/shilpa/Downloads/sow_checklist.pdf", DriveServiceProvider.TYPE_PDF);
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
        DriveFile file = this.serviceProvider.uploadFileForUser("D:/Mygubbi GAME/TestCopy12.xlsx","chiragsharath@gmail.com","test");
        System.out.println(file);
        System.out.println("Permission granted to edit file - " + file.getName());

    }
}
