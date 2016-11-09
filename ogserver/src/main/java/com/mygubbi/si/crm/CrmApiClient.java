package com.mygubbi.si.crm;

import com.sugarcrm.www.sugarcrm.Set_entry_result;
import com.sugarcrm.www.sugarcrm.SugarsoapLocator;
import com.sugarcrm.www.sugarcrm.SugarsoapPortType;
import com.sugarcrm.www.sugarcrm.User_auth;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.security.MessageDigest;

/**
 * Created by Chirag on 26-10-2016.
 */
public class CrmApiClient
{
    public static final String APPLICATION_NAME = "mygubbi_crm";
    public static final String VERSION = "0.1";
    public static final String USER_NAME = "admin";
    public static final String PASSWORD = "admin!@#";

    private SugarsoapPortType crmPort;
    private String sessionId;

    public CrmApiClient() throws Exception
    {
        this.initSoapClient();
    }

//    public static void main(String[] args)
//    {
//        String  opportunity= "SAL-1607-000039";
//        try {
//            System.out.println(new CrmApiClient().getOpportunityDetails(opportunity));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private void initSoapClient() throws Exception
    {
        try {
            this.crmPort = new SugarsoapLocator().getsugarsoapPort();
            this.sessionId = this.getSession(USER_NAME, PASSWORD);
            System.out.println("Session id:" + this.sessionId);
        } catch (Exception e) {
            throw new RuntimeException("Error in creating session ID");
        }
    }

    public String getSession(String Username, String Password) throws Exception
    {
        User_auth e = new User_auth();
        e.setUser_name(Username);
        MessageDigest md = MessageDigest.getInstance("MD5");
        Password = this.getHexString(md.digest(Password.getBytes()));
        e.setPassword(Password);
        e.setVersion(VERSION);
        Set_entry_result loginRes = this.crmPort.login(e, (String)null);
        return loginRes.getId();
    }

    private String getHexString(byte[] data) throws Exception {
        StringBuffer buf = new StringBuffer();

        for(int i = 0; i < data.length; ++i) {
            int halfbyte = data[i] >>> 4 & 15;
            int two_halfs = 0;

            do {
                if(0 <= halfbyte && halfbyte <= 9) {
                    buf.append((char)(48 + halfbyte));
                } else {
                    buf.append((char)(97 + (halfbyte - 10)));
                }

                halfbyte = data[i] & 15;

            } while(two_halfs++ < 1);
        }

        return buf.toString();
    }

    public String getOpportunityDetails(String opportunityId) throws Exception
    {
        return new JsonObject(this.crmPort.get_opportunity_details(this.sessionId, opportunityId)).encodePrettily();
    }

    public String getDocuments(String parentType, String parentId, String category, String type) throws Exception
    {
        return new JsonArray(this.crmPort.get_documents(this.sessionId, parentType, parentId, category, type)).encodePrettily();
    }

    public String getLatestDocuments(String parentType, String parentId, String category) throws Exception
    {
        String related_document = this.crmPort.get_related_document(this.sessionId, parentType, parentId, category);
        try {
            return new JsonArray(related_document).encodePrettily();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
