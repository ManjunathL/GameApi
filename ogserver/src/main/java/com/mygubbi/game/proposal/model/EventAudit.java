package com.mygubbi.game.proposal.model;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.EventDateTime;
import io.vertx.core.json.JsonObject;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Created by Chirag on 10-03-2017.
 */
public class EventAudit extends JsonObject {

    public static final String ID = "id";
    public static final String LEAD_ID = "leadId";
    public static final String CUSTOMER_NAME = "customerName";
    public static final String EVENT_CREATED_TIME = "eventCreatedTime";
    public static final String MEETING_DATE_TIME = "meetingDateTime";
    public static final String MEETING_LOCATION = "meetingLocation";
    public static final String CUSTOMER_PHONE = "customerPhone";
    public static final String EVENT_STATUS = "eventStatus";
    public static final String CUSTOMER_EMAIL = "customerEmail";

    public EventAudit() {}

    public EventAudit(JsonObject jsonObject){
        super(jsonObject.getMap());
    }

    public int getID() {
        return this.getInteger(ID);
    }

    public String getLeadId() {
        return this.getString(LEAD_ID);
    }

    public String getCustomerName() {
        return this.getString(CUSTOMER_NAME);
    }

    public Date getEventCreatedTime() {
        return (Date) this.getValue(EVENT_CREATED_TIME);
    }
    public Date getMeetingDateTime() {
        return (Date) this.getValue(MEETING_DATE_TIME);
    }

    public String getMeetingLocation() {
        return this.getString(MEETING_LOCATION);
    }

    public String getCustomerPhone() {
        return this.getString(CUSTOMER_PHONE);
    }

    public String getEventStatus() {
        return this.getString(EVENT_STATUS);
    }
    public String getCustomerEmail() {
        return this.getString(CUSTOMER_EMAIL);
    }


    public EventAudit setId(int id) {
        this.put(ID, id);
        return this;
    }

    public EventAudit setLeadId(String leadId) {
        this.put(LEAD_ID, leadId);
        return this;
    }

    public EventAudit setCustomerName(String customerName) {
        this.put(CUSTOMER_NAME, customerName);
        return this;
    }

    public EventAudit setEventCreatedTime(DateTime eventCreatedTime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String format = formatter.format(eventCreatedTime);
        this.put(EVENT_CREATED_TIME, format);
        return this;
    }

    public EventAudit setMeetingDateTime(EventDateTime meetingDateTime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String format = formatter.format(meetingDateTime);
        this.put(MEETING_DATE_TIME, format);
        return this;
    }

    public EventAudit setMeetingLocation(String meetingLocation) {
        this.put(MEETING_LOCATION, meetingLocation);
        return this;
    }

    public EventAudit setCustomerPhone(String customerPhone) {
        this.put(CUSTOMER_PHONE, customerPhone);
        return this;
    }

    public EventAudit setEventStatus(String eventStatus) {
        this.put(EVENT_STATUS, eventStatus );
        return this;
    }
    public EventAudit setCustomerEmail(String customerEmail) {
        this.put(CUSTOMER_EMAIL, customerEmail );
        return this;
    }

}
