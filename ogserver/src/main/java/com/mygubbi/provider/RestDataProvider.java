package com.mygubbi.provider;

import com.mygubbi.config.ConfigHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import us.monoid.json.JSONArray;
import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;
import us.monoid.web.FormData;
import us.monoid.web.JSONResource;
import us.monoid.web.Resty;
import us.monoid.web.TextResource;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static us.monoid.web.Resty.content;
import static us.monoid.web.Resty.data;
import static us.monoid.web.Resty.form;

/**
 * Created by User on 26-03-2018.
 */
public class RestDataProvider implements DataProviderMode {

    private static final Logger LOG = LogManager.getLogger(RestDataProvider.class);
    private final Resty resty = new Resty();

    @Override
    public JSONObject getResource(String urlFrag, Map<String, String> params) {
        try {
            return resty.json(getBaseURL() + "/" + urlFrag + "?" + queryParams(params)).object();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            throw new RuntimeException("Error querying - " + urlFrag, e);
        }
    }

    @Override
    public JSONArray getResourceArray(String urlFrag, Map<String, String> params) {
        try {
            String string = getBaseURLForCrm() + "/" + urlFrag + "?" + queryParams(params);
            LOG.debug("Hitting crm :" + string);
            return resty.json(string).array();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            throw new RuntimeException("Error querying - " + urlFrag, e);
        }
    }

    @Override
    public JSONObject postResource(String urlFrag, String json) {

        try {
            return resty.json(getBaseURL() + "/" + urlFrag,
                    content(json))
                    .object();

        } catch (IOException | JSONException e) {
            e.printStackTrace();
            throw new RuntimeException("Error querying - " + urlFrag, e);
        }
    }

    @Override
    public JSONObject postResourceWithFormData(String url, Map<String, String> keyValuePairs) {
        return null;
    }

    @Override
    public JSONArray postResourceWithFormData(String url, String parent_id) {

        try {
            return new Resty().json(getBaseURLForCrm() + "/" + url, form(data("opportunity_name", parent_id))).array();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return  null;
    }

    @Override
    public JSONObject postResourceWithUrl(String urlFrag, String json) {
        return null;
    }

    @Override
    public JSONArray postResourceWithUrl(String url, Map<String, String> keyValuePairs) {

        try {
            FormData[] values = new FormData[keyValuePairs.size()];
            int index = 0;
            for (String key : keyValuePairs.keySet()) {
                values[index] = data(key, keyValuePairs.get(key));
                index++;
            }
            return resty.json(getBaseURLForCrm() + "/" + url , form(values)).array();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public JSONArray postResourceGetMultiple(String urlFrag, String jsonParams) {

        try {
            return resty.json(getBaseURL() + "/" + urlFrag,
                    content(jsonParams)).array();

        } catch (IOException | JSONException e) {
            e.printStackTrace();
            throw new RuntimeException("Error querying - " + urlFrag, e);
        }
    }


    private String getBaseURL() {
        return ConfigHolder.getInstance().getStringValue("restUrl", "");
    }

    private String getBaseURLForCrm() {
        return ConfigHolder.getInstance().getStringValue("urlForCrm", "");
    }

    private String getBaseURLforLdSqr()
    {
        LOG.info("base url " +ConfigHolder.getInstance().getStringValue("baseLdSqrUrl", ""));
        String baseLdSqrUrl = ConfigHolder.getInstance().getStringValue("baseLdSqrUrl", "") + "?accessKey=" + ConfigHolder.getInstance().getStringValue("ldSqrAccessKey", "") + "&secretKey=" + ConfigHolder.getInstance().getStringValue("ldSqrSecretKey", "");
        return baseLdSqrUrl;
    }

    private String queryParams(Map<String, String> params) {
        return params.entrySet().stream().map(entry -> (entry.getKey() + "=" + entry.getValue())).collect(Collectors.joining("&"));
    }


}
