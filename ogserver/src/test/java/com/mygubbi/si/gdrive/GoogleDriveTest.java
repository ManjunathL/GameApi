package com.mygubbi.si.gdrive;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.mygubbi.config.ConfigHolder;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by test on 07-07-2017.
 */
public class GoogleDriveTest
{

    private String credentialsJson = "/game/game_new_project.json";
   /* private final List<String> SCOPES = Arrays.asList(DriveScopes.DRIVE,
            SheetsScopes.SPREADSHEETS,SheetsScopes.DRIVE);*/
    private static HttpTransport HTTP_TRANSPORT;
    private File DATA_STORE_DIR = new File("E:/data/gdrive/");
    private String APPLICATION_NAME = "";
    private final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private final List<String> SCOPES = Arrays.asList(DriveScopes.DRIVE,
            CalendarScopes.CALENDAR);



    public static void main(String[] args)
    {

        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }

        long timer = System.currentTimeMillis();
        new GoogleDriveTest().insertCalendarEvent();
        System.out.println("timer" + (System.currentTimeMillis() - timer));
    }

    private void listFiles()
    {
        System.out.println("Files:" + this.serviceProvider.listFiles());
    }

    public DriveServiceProvider serviceProvider;

   /* public GoogleDriveTest()
    {
        this.serviceProvider = new DriveServiceProvider();
    }*/

      public GoogleDriveTest()
    {
        try {
            this.initDriveService();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

   /* private void testUploadFileIntoDrive()
    {
        List<String> filepaths = new ArrayList<>();
        filepaths.add("C:\\Users\\Public\\game_files\\5010\\sow.xlsx");
        filepaths.add("C:\\Users\\Public\\game_files\\5010\\sow2.xlsx");
        //filepaths.add("C:\\Users\\Public\\game_files\\5010\\sow_checklist_falsenoquotes.pdf");
        DriveFile file = this.serviceProvider.createFolder(filepaths,"chiragtest","chiragsharath@gmail.com");
        System.out.print("WEBviewLink :" + file.getWebContentLink() + " : " + file.getWebViewLink());

    }*/

    private void insertCalendarEvent()
    {
        // Refer to the Java quickstart on how to setup the environment:
        // https://developers.google.com/calendar/quickstart/java
        // Change the scope to CalendarScopes.CALENDAR and delete any stored
        // credentials.

        // Initialize Calendar service with valid OAuth credentials
        Calendar service = null;
        try {
            service = initDriveService();
        } catch (IOException e) {
            e.printStackTrace();
        }

    /*    com.google.api.services.calendar.model.Calendar calendar = new com.google.api.services.calendar.model.Calendar();
        calendar.setSummary("calendarSummary");
        calendar.setTimeZone("Asia/Calcutta");

        try {
            com.google.api.services.calendar.model.Calendar createdCalendar = service.calendars().insert(calendar).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        Event event = new Event()
                .setSummary("Mygubbi HSR EC")
                .setLocation("HSR Layout, Bangalore")
                .setDescription("CCA Launch");

        DateTime startDateTime = new DateTime("2018-06-14T07:25:00Z");
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("Asia/Calcutta");
        event.setStart(start);

        DateTime endDateTime = new DateTime("2018-06-14T07:25:00Z");
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("Asia/Calcutta");
        event.setEnd(end);

        String[] recurrence = new String[] {"RRULE:FREQ=DAILY;COUNT=2"};
        event.setRecurrence(Arrays.asList(recurrence));

        EventAttendee[] attendees = new EventAttendee[] {
                new EventAttendee().setEmail("chiragsharath@gmail.com"),
                new EventAttendee().setEmail("chirag@91social.com"),
                new EventAttendee().setEmail("sairamk1305@gmail.com"),
                new EventAttendee().setEmail("mygubbi.com_pckif5fs90g049gv5okj2al8n8@group.calendar.google.com"),
        };
        event.setAttendees(Arrays.asList(attendees));

        EventReminder[] reminderOverrides = new EventReminder[] {
                new EventReminder().setMethod("email").setMinutes(5),
                new EventReminder().setMethod("popup").setMinutes(2),
        };
        Event.Reminders reminders = new Event.Reminders()
                .setUseDefault(true);
        event.setReminders(reminders);

        String calendarId = "primary";
                ;
        try {
            if (service != null) {
                event = service.events().insert(calendarId, event).execute();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.printf("Event created: %s\n", event.getHtmlLink());
    }

    private Credential authorize() throws IOException
    {
        return GoogleCredential
                .fromStream(this.getClass().getResourceAsStream(this.credentialsJson))
                .createScoped(SCOPES);
    }

    private Calendar initDriveService() throws IOException
    {
        Credential credential = authorize();
        return new Calendar.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}
