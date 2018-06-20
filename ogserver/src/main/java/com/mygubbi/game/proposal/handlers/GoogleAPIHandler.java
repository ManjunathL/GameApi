package com.mygubbi.game.proposal.handlers;

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
import com.mygubbi.common.DateUtil;
import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.config.ConfigHolder;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import com.mygubbi.game.proposal.model.EventAudit;
import com.mygubbi.provider.CrmDataProvider;
import com.mygubbi.route.AbstractRouteHandler;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import us.monoid.json.JSONArray;
import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by User on 14-06-2018.
 */
public class GoogleAPIHandler extends AbstractRouteHandler{

        private final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
        private final List<String> SCOPES = Arrays.asList(DriveScopes.DRIVE,
                CalendarScopes.CALENDAR);
        private String APPLICATION_NAME = "";
    private static HttpTransport HTTP_TRANSPORT;
    String credentialsJson = null;
    private final String TIME_ZONE = "Asia/Calcutta";
    private CrmDataProvider crmDataProvider;

    private final static Logger LOG = LogManager.getLogger(GoogleAPIHandler.class);

    public GoogleAPIHandler(Vertx vertx) {
        super(vertx);
        this.route().handler(BodyHandler.create());
        crmDataProvider =  new CrmDataProvider();
        this.post("/calendar/insert").handler(this::insertCalendarEvent);
    }

    private void insertCalendarEvent(RoutingContext routingContext) {

        insertEvent(routingContext);
    }

    private Credential authorize() throws IOException
    {
        this.APPLICATION_NAME = ConfigHolder.getInstance().getStringValue("driveAppName","GAME Drive App");
        this.credentialsJson = ConfigHolder.getInstance().getStringValue("calendarjson","/game/game_new_project.json");
        return GoogleCredential
                .fromStream(this.getClass().getResourceAsStream(this.credentialsJson))
                .createScoped(SCOPES);
    }

    private Calendar initDriveService() throws IOException
    {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }

        Credential credential = authorize();
        return new Calendar.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    private void insertEvent(RoutingContext routingContext)
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

        JsonObject jsonObject = routingContext.getBodyAsJson();
        LOG.debug("Jsonobject:" + jsonObject);


        String summary = jsonObject.getString("summary");
        String calendar_location = jsonObject.getString("calendarLocation");
        String location = jsonObject.getString("calendarLocation");
        String description = jsonObject.getString("description");
        String startTime = jsonObject.getString("startTime");
        String endTime = jsonObject.getString("endTime");
        String createdByEmail = jsonObject.getString("createdByEmail");
        String leadId = jsonObject.getString("LeadId");
        String customerEmail = jsonObject.getString("customerEmail");
        String customerPhone = jsonObject.getString("customerPhone");
        String ownerEmailId = jsonObject.getString("ownerEmailId");
        String customerName = jsonObject.getString("customerName");
        String testTime = "2018-06-14T07:25:00Z";


        LOG.debug("Start time before :" + startTime+ " : End time before : " + endTime);

        LOG.debug("Summary :" + summary + " : Calendar location : " + calendar_location + " : Location :" + location + " : " + description);
        String startTimeNew = DateUtil.convertDateString(startTime);
        String endTimeNew = DateUtil.convertDateString(endTime);
        LOG.debug("Start time after :" + startTime+ " : End time after : " + endTime);

        if (summary.contains("Meeting") && location!= null){
            Event event = new Event()
                    .setSummary(summary)
                    .setLocation(location)
                    .setDescription(description);

            DateTime startDateTime = new DateTime(startTimeNew);
            EventDateTime start = new EventDateTime()
                    .setDateTime(startDateTime)
                    .setTimeZone(TIME_ZONE);
            event.setStart(start);

            DateTime endDateTime = new DateTime(endTimeNew);
            EventDateTime end = new EventDateTime()
                    .setDateTime(endDateTime)
                    .setTimeZone(TIME_ZONE);
            event.setEnd(end);

            String[] recurrence = new String[] {"RRULE:FREQ=DAILY;COUNT=1"};
            event.setRecurrence(Arrays.asList(recurrence));
            String locationEmail = ConfigHolder.getInstance().getStringValue(calendar_location,"");

            LOG.debug("EMAIL attendees : " + locationEmail + " : " +  ownerEmailId + " : " + customerEmail + " : " + createdByEmail );

            List<String> emailAttendees = new ArrayList<>();
            if (locationEmail != null) emailAttendees.add(locationEmail);
            if (ownerEmailId != null) emailAttendees.add(ownerEmailId);;
            if (customerEmail != null) emailAttendees.add(customerEmail);;
            if (createdByEmail != null) emailAttendees.add(createdByEmail);;

            LOG.debug("Email attendees : " + emailAttendees.size());

            EventAttendee[] attendees = new EventAttendee[10];

            for (int i=0; i <emailAttendees.size() ; i++)
            {
                attendees[i]=new EventAttendee().setEmail(emailAttendees.get(i));
            }

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

            if (event != null)
            {
                insertEventStatus(routingContext,event,leadId,customerName,calendar_location,customerPhone,customerEmail);
            }
            else
            {
                sendJsonResponse(routingContext, String.valueOf(new JsonObject().put("status","Failure").put("message","Event not inserted")));
            }
        }
        else {
            sendJsonResponse(routingContext, String.valueOf(new JsonObject().put("status","Failure").put("message","Event not inserted")));

        }


    }

    private JSONObject getLeadDetailsFromLeadSquared(String leadId){

        String accessKey = ConfigHolder.getInstance().getStringValue("leadsquared_access_key","u$r1c221f1db494ffe457d25c16814685ce");
        String secretKey = ConfigHolder.getInstance().getStringValue("leadsquared_secret_key","894f1e8cbdc36c967504b9dc912905f1fc77c012");

        JSONArray jsonArray = crmDataProvider.getLeadDetails(accessKey,secretKey,leadId);

        JSONObject jsonObject = null;

        try {
            jsonObject =  jsonArray.getJSONObject(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject != null)
        {
            return jsonObject;
        }
        else
        {
            return null;
        }

    }

    private void insertEventStatus(RoutingContext routingContext,Event event,String leadId,String customerName,String meetingLocation,String customerPhone, String customerEmail)
    {
        EventAudit eventAudit = new EventAudit();
        eventAudit.setCustomerName(customerName);
        eventAudit.setLeadId(leadId);
        eventAudit.setMeetingLocation(meetingLocation);
        eventAudit.setCustomerPhone(customerPhone);
        eventAudit.setEventCreatedTime(event.getCreated());
        eventAudit.setMeetingDateTime(event.getOriginalStartTime());
        eventAudit.setEventStatus(event.getStatus());
        eventAudit.setCustomerEmail(customerEmail);

        LOG.debug("routing context :" + eventAudit.encodePrettily());
        Integer id = LocalCache.getInstance().store(new QueryData("event.audit.insert", eventAudit));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag || resultData.updateResult.getUpdated() == 0)
                    {
                        sendError(routingContext, "Error in creating version.");
                    }
                    else
                    {
                        sendJsonResponse(routingContext, eventAudit.toString());
                    }
                });
    }

}
