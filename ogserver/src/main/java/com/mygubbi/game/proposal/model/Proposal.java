package com.mygubbi.game.proposal.model;

import io.vertx.core.json.JsonObject;

import java.sql.Date;

/**
 * Created by Chirag on 08-03-2017.
 */
public class Proposal extends JsonObject{

    public static final String ID = "id";
    public static final String STATUS = "status";
    public static final String VERSION = "version";
    public static final String CRMID = "crmId";
    public static final String PROJECT_CITY = "pcity";
    public static final String QUOTENO="quoteNo";
    public static final String TITLE="title";
    public static final String QUOTE_NO_NEW="quoteNoNew";
    public static final String PRICE_DATE="priceDate";




    public Proposal() {}

    public Proposal(JsonObject jsonObject){
        super(jsonObject.getMap());
    }


    public int getId() {
        return this.getInteger(ID);
    }

    public Proposal setId(int id) {
        put(ID,id);
        return this;
    }

    public String getStatus() {
        return getString(STATUS);
    }

    public Proposal setStatus(String status) {
        put(STATUS,status);
        return this;
    }

    public String getVersion() {
        return getString(VERSION);
    }

    public Proposal setVersion(String version) {
        put(VERSION,version);
        return this;
    }

    public String getCrmId() {
        return this.getString(CRMID);
    }

    public Proposal setCrmId(String crmId) {
        put(CRMID,crmId);
        return this;
    }

    public String getPcity() {
        return this.getString(PROJECT_CITY);
    }

    public Proposal setPcity(String pcity) {
        put(PROJECT_CITY,pcity);
        return this;
    }

    public String getTitle() {
        return this.getString(TITLE);
    }

    public Proposal setTitle(String title) {
        put(TITLE,title);
        return this;
    }

    public String getQuoteNo() {
        return this.getString(QUOTENO);
    }

    public Proposal setQuoteNo(String quoteNo) {
        put(QUOTENO,quoteNo);
        return this;
    }

    public String getQuoteNoNew() {
        return this.getString(QUOTE_NO_NEW);
    }

    public Proposal setQuoteNoNew(String quoteNoNew) {
        put(QUOTE_NO_NEW,quoteNoNew);
        return this;
    }

    public String getPriceDate() {
        return this.getString(PRICE_DATE);
    }

    public Proposal setPriceDate(Date priceDate) {
        put(PRICE_DATE,priceDate);
        return this;
    }
}
