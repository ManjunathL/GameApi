package com.mygubbi.game.proposal.model;

import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Chirag on 15-02-2017.
 */
public class RateCardMaster {

    public static final String CARCASS_TYPE = "C";
    public static final String SHUTTER_TYPE = "S";
    public static final String FACTOR_TYPE = "F";
    public static final String LOADING_FACTOR = "L";
    public static final String LABOUR_FACTOR = "B";
    public static final String LOADING_FACTOR_NONSTANDARD = "N";

    private final static Logger LOG = LogManager.getLogger(RateCardMaster.class);
    public static final String KEYDELIMITER = ":";

    private String rateCardId;
    private String code;
    private String type;



    public static RateCardMaster fromJson(JsonObject json)
    {
        return new RateCardMaster().setRateCardId(json.getString("rateCardId"))
                .setCode(json.getString("code"))
                .setType(json.getString("type"));
    }

    public String getRateCardId() {
        return rateCardId;
    }

    public RateCardMaster setRateCardId(String rateCardId)
    {
        this.rateCardId = rateCardId;
        return this;
    }

    public String getCode() {
        return code;
    }

    public RateCardMaster setCode(String code)
    {
        this.code = code;
        return this;
    }

    public String getType() {
        return type;
    }

    public RateCardMaster setType(String type)
    {
        this.type = type;
        return this;
    }

    public String getCodeTypeKey() {
        return type + ":" + code;
    }

    public static String makeKey(String type, String code)
    {
        return type + KEYDELIMITER + code;
    }

    public static String makeKey(String code, String type, int thickness)
    {
        return type + KEYDELIMITER + code + KEYDELIMITER + thickness;
    }
}
