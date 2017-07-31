package com.mygubbi.game.proposal.model;

import io.vertx.core.json.JsonObject;

/**
 * Created by Shruthi on 7/29/2017.
 */
public class SOWPdf extends JsonObject
{
    public static final String SPACE_TYPE = "spaceType";
    public static final String ROOM = "roomcode";
    public static final String SERVICE = "service";
    public static final String SERVICE_VALUE = "serviceValue";
    public static final String RELATED_SERVICE1 = "relatedService1";
    public static final String RELATED_SERVICEVALUE1 = "relatedServiceValue1";
    public static final String RELATED_SERVICE2 = "relatedService2";
    public static final String RELATED_SERVICEVALUE2 = "relatedServiceValue2";
    public static final String RELATED_SERVICE3 = "relatedService3";
    public static final String RELATED_SERVICEVALUE3 = "relatedServiceValue3";
    public static final String RELATED_SERVICE4 = "relatedService4";
    public static final String RELATED_SERVICEVALUE4 = "relatedServiceValue4";
    public static final String RELATED_SERVICE5 = "relatedService5";
    public static final String RELATED_SERVICEVALUE5 = "relatedServiceValue5";
    public static final String RELATED_SERVICE6 = "relatedService6";
    public static final String RELATED_SERVICEVALUE6 = "relatedServiceValue6";

    public SOWPdf(){

    }

    public SOWPdf(JsonObject jsonObject)
    {
        super(jsonObject.getMap());
    }

    public  String getSpaceType() {
        return this.getString(SPACE_TYPE);
    }

    public String getROOM() {
        return this.getString(ROOM);
    }

    public String getSERVICE() {
        return this.getString(SERVICE);
    }

    public String getServiceValue() {
        return this.getString(SERVICE_VALUE);
    }

    public  String getRelatedService1() {
        return this.getString(RELATED_SERVICE1);
    }

    public  String getRelatedServicevalue1() {
        return this.getString(RELATED_SERVICEVALUE1);
    }

    public String getRelatedService2() {
        return this.getString(RELATED_SERVICE2);
    }

    public  String getRelatedServicevalue2() {
        return this.getString(RELATED_SERVICEVALUE2);
    }

    public  String getRelatedService3() {
        return this.getString(RELATED_SERVICE3);
    }

    public  String getRelatedServicevalue3() {
        return this.getString(RELATED_SERVICEVALUE3);
    }

    public String getRelatedService4() {
        return this.getString(RELATED_SERVICE4);
    }

    public String getRelatedServicevalue4() {
        return this.getString(RELATED_SERVICEVALUE4);
    }

    public  String getRelatedService5() {
        return this.getString(RELATED_SERVICE5);
    }

    public  String getRelatedServicevalue5() {
        return this.getString(RELATED_SERVICEVALUE5);
    }

    public String getRelatedService6() {
        return this.getString(RELATED_SERVICE6);
    }

    public String getRelatedServicevalue6() {
        return this.getString(RELATED_SERVICEVALUE6);
    }

    public SOWPdf setSpaceType(String spaceType)
    {
        put(SPACE_TYPE,spaceType);
        return this;
    }

    public SOWPdf setROOM(String room) {
        put(ROOM,room);
        return this;
    }

    public SOWPdf setSERVICE(String service) {
        put(SERVICE,service);
        return this;
    }

    public SOWPdf setServiceValue(String serviceValue) {
        put(SERVICE_VALUE,serviceValue);
        return this;
    }

    public  SOWPdf setRelatedService1(String relatedService1) {
        put(RELATED_SERVICE1,relatedService1);
        return this;
    }

    public SOWPdf setRelatedServicevalue1(String relatedServicevalue1) {
        put(RELATED_SERVICEVALUE1,relatedServicevalue1);
        return this;
    }

    public SOWPdf setRelatedService2(String relatedService2) {
        put(RELATED_SERVICE2,relatedService2);
        return this;
    }

    public  SOWPdf setRelatedServicevalue2(String relatedServiceValue2) {
        put(RELATED_SERVICEVALUE2,relatedServiceValue2);
        return this;
    }

    public  SOWPdf setRelatedService3(String relatedService3) {
        put(RELATED_SERVICE3,relatedService3);
        return this;
    }

    public  SOWPdf setRelatedServicevalue3(String relatedServicevalue3) {
        put(RELATED_SERVICEVALUE3,relatedServicevalue3);
        return this;
    }

    public SOWPdf setRelatedService4(String relatedService4) {
        put(RELATED_SERVICE4,relatedService4);
        return this;
    }

    public SOWPdf setRelatedServicevalue4(String relatedServiceValue4) {
        put(RELATED_SERVICEVALUE4,relatedServiceValue4);
        return this;
    }

    public  SOWPdf setRelatedService5(String relatedService5) {
        put(RELATED_SERVICE5,relatedService5);
        return this;
    }

    public  SOWPdf setRelatedServicevalue5(String relatedServicevalue5) {
        put(RELATED_SERVICEVALUE5,relatedServicevalue5);
        return this;
    }

    public SOWPdf setRelatedService6(String relatedService6) {
        put(RELATED_SERVICE6,relatedService6);
        return this;
    }

    public SOWPdf setRelatedServicevalue6(String relatedServiceValue6) {
        put(RELATED_SERVICEVALUE6,relatedServiceValue6);
        return this;
    }
}
