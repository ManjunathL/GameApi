package com.mygubbi.provider;

import us.monoid.json.JSONArray;

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
}
