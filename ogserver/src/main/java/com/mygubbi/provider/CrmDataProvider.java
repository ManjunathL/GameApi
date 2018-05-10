package com.mygubbi.provider;

import us.monoid.json.JSONArray;
import us.monoid.json.JSONObject;

import java.io.File;
import java.util.HashMap;

/**
 * Created by User on 29-03-2018.
 */
public class CrmDataProvider {

private DataProviderMode dataProviderMode;

    public CrmDataProvider() {
        dataProviderMode = new RestDataProvider();
    }

    public JSONArray getDocuments(String opportunityId) {

        try {
            return dataProviderMode.postResourceWithUrl("get_customer_documents.php", new HashMap<String, String>(){
                {
                    put("opportunity_name",opportunityId);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    public JSONObject getOpportunityDetails(String opportunityId) {

        try {
            return dataProviderMode.postResourceWithFormData("get_opportunity_details.php", new HashMap<String, String>(){
                {
                    put("opportunity_name",opportunityId);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public JSONArray getCustomerIssues(String opportunityId) {

        try {
            return dataProviderMode.postResourceWithUrl("get_customer_issues.php", new HashMap<String, String>(){
                {
                    put("opportunity_name",opportunityId);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    public JSONObject createCustomerIssue(String opportunityId, String issue, String documents) {

        try {
            return dataProviderMode.postResourceWithFormData("create_customer_issue.php", new HashMap<String, String>(){
                {
                    put("opportunity_name",opportunityId);
                    put("name",issue);
                    put("document", documents);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public JSONArray getUpdates(String opportunityId) {

        try {
            return dataProviderMode.postResourceWithUrl("get_room_status.php", new HashMap<String, String>(){
                {
                    put("opportunity_name",opportunityId);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
