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

   /* public static void main(String[] args)
    {
        String  opportunity= "SAL-1607-000039";
        String  opportunities = "Opportunities";
        String  category = "Floor_Plan";
        String  type = "all";
        String  status = "In Progress";
        String  taskType = "Intial Approval";
        String  parentType = "Oppertunity";
        String  taskId = null;
        String  parentId ="1";


        try {
            System.out.println(new CrmApiClient().getOpportunityDetails(opportunity));

            //System.out.println(new CrmApiClient().getDocuments(opportunities, opportunity, category, type));
            // System.out.println(new CrmApiClient().updateTask( opportunities, opportunity, status, taskId , taskType, parentId));
            // System.out.println(new CrmApiClient().updateDocument( parentType, opportunity, status, taskId , taskType, parentId));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

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
        JsonObject doc = new JsonObject(this.crmPort.get_opportunity_details(this.sessionId, opportunityId));

        String status = (String) doc.getValue("sales_stage");

        if (status.equals("Customer_Requirement_Update") || status.equals("Floor Plan Uploaded") || status.equals("Initial Proposal Uploaded")){
            status = "initiated";
        }
        else if (status.equals("Send Approved Proposal") ){
            status = "Proposal approved";
        }
        else if(status.equals("Booking Amount Collected") || status.equals("Site_Measurement_Uploaded") || status.equals("Detail Design") || status.equals("Final Proposal Uploaded") || status.equals("Proposal Approval")){
            status = "Order Placed";
        }
        else if (status.equals("Collect Milestone 2 Payment") ){
            status = "Production Started";
        }
        else {
            status = "Installation in Progress";
        }
        JsonObject newDoc = new JsonObject().put("paymentAmount", doc.getString("amount"))
                .put("paymentDate",doc.getValue("m1_payment_date_c"))
                .put("paymentMode",doc.getValue("m1_mode_of_payment_c"))
                .put("paymentPercentage","10")
                .put("paymentRef",doc.getValue("payment_reference1_c"))
                .put("paymentStatus",status)
                .put("paymentType","booking");
        JsonObject newDoc1 = new JsonObject().put("paymentAmount", doc.getString("amount"))
                .put("paymentDate",doc.getValue("m2_payment_date_c"))
                .put("paymentMode",doc.getValue("m2_mode_of_payment_c"))
                .put("paymentPercentage","70")
                .put("paymentRef",doc.getValue("payment_reference2_c"))
                .put("paymentStatus",status)
                .put("paymentType","advance");
        JsonObject newDoc2 = new JsonObject().put("paymentAmount", doc.getString("amount"))
                .put("paymentDate",doc.getValue("m3_payment_date_c"))
                .put("paymentMode",doc.getValue("m3_mode_of_payment_c"))
                .put("paymentPercentage","100")
                .put("paymentRef",doc.getValue("payment_reference3_c"))
                .put("paymentStatus",doc.getValue(""))
                .put("paymentType",doc.getValue(""));
        JsonObject status1 = new JsonObject().put("status", "NA")
                .put("statusId","NA")
                .put("statusName",status);

        JsonArray payment = new JsonArray();
        payment.add(newDoc);
        payment.add(newDoc1);
        payment.add(newDoc2);


        String  opportunities = "Opportunities";
        String  categoryFloorPlan = "Floor_Plan";
        String  type = "all";

        JsonArray documentFloorPlan = new JsonArray(new CrmApiClient().getDocuments(opportunities, opportunityId, categoryFloorPlan, type));

        String  categoryProposal = "Proposal";

        JsonArray documentProposal = new JsonArray(new CrmApiClient().getDocuments(opportunities, opportunityId, categoryProposal, type));

        JsonObject objectDocumentFloorPlan = documentFloorPlan.getJsonObject(0);
        JsonObject objectDocumentProposal = documentProposal.getJsonObject(0);

        JsonArray proposalObject = new JsonArray();
        proposalObject.add(objectDocumentFloorPlan);
        proposalObject.add(objectDocumentProposal);

        JsonObject finalJson = new JsonObject();

        finalJson.put("documentDetails",(Object)proposalObject);
        finalJson.put("paymentDetails",(Object)payment);
        finalJson.put("projectStatus",(Object)status1);
        return finalJson.encodePrettily();


    }

    public String getDocuments(String parentType, String parentId, String category, String type) throws Exception
    {
        String documentLinkBaseUrl = "https://s3-ap-southeast-1.amazonaws.com/mygubbicrm/";
        JsonArray doc = new JsonArray(this.crmPort.get_documents(this.sessionId, parentType, parentId, category, type));

        JsonObject newDoc = doc.getJsonObject(0);

        JsonObject docJson1 = new JsonObject().put("documentLink", documentLinkBaseUrl+newDoc.getString("docUrl"))
                .put("documentName", newDoc.getString("documentName"))
                .put("documentType", newDoc.getString("categoryId"))
                .put("uploadDate", "NA");
        //.put("uploadDate", newDoc.getString("date"))
        JsonArray act = new JsonArray();
        act.add(docJson1);
        return act.encodePrettily();    }

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

    public String updateDocument(String parentType, String parentId, String category, String user_id, String file_contents, String file_ext, String file_name) throws Exception {

        return new JsonArray(this.crmPort.update_document(this.sessionId, parentType, parentId, category, user_id, file_contents, file_ext, file_name)).encodePrettily();
    }

    public String updateTask(String parentType, String parentId, String status, String task_id, String task_type, String user_id) throws Exception {
        System.out.print(parentType + parentId + status + task_id + task_type + user_id);
        System.out.print( new JsonArray(this.crmPort.update_task_status (this.sessionId, parentType, parentId, status, task_id, task_type, user_id)));
        return new JsonArray(this.crmPort.update_task_status (this.sessionId, parentType, parentId, status, task_id, task_type, user_id)).encodePrettily();
    }

}