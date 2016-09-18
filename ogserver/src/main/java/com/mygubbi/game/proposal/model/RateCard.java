package com.mygubbi.game.proposal.model;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by test on 17-05-2016.
 */

public class RateCard
{
    public static final String CARCASS_TYPE = "C";
    public static final String SHUTTER_TYPE = "S";
    public static final String FACTOR_TYPE = "F";
    public static final String LOADING_FACTOR = "L";
    public static final String LABOUR_FACTOR = "B";

    private String type;
    private String code;
    private double rate;
    private Map<Integer, Double> ratesByThickness = Collections.EMPTY_MAP;

/*
    public static RateCard fromJson(JsonObject json)
    {
        return new RateCard().setCode(json.getString("code")).setType(json.getString("type"))
                .setRate(json.getDouble("rate")).setRatesByThickness(createRateMap(json.getJsonArray("rates")));
    }
*/

    public RateCard()
    {

    }

    public RateCard(JsonObject json)
    {
        this.setCode(json.getString("code")).setType(json.getString("type"));
        if (json.containsKey("rate"))
        {
            this.setRate(json.getDouble("rate"));
        }
        if (json.containsKey("rates"))
        {
            this.setRatesByThickness(createRateMap(json.getJsonArray("rates")));
        }
    }

    public String getCode()
    {
        return code;
    }

    public RateCard setCode(String code)
    {
        this.code = code;
        return this;
    }

    public String getType()
    {
        return type;
    }

    public RateCard setType(String type)
    {
        this.type = type;
        return this;
    }

    public double getRate()
    {
        return rate;
    }

    public RateCard setRate(double rate)
    {
        this.rate = rate;
        return this;
    }

    public double getRateByThickness(int thickness)
    {
        if (!this.ratesByThickness.containsKey(thickness)) return 0;
        return this.ratesByThickness.get(thickness);
    }

    public RateCard setRatesByThickness(Map<Integer, Double> ratesByThickness)
    {
        this.ratesByThickness = ratesByThickness;
        return this;
    }

    public String getKey()
    {
        return makeKey(this.getCode(), this.getType());
    }

    public static String makeKey(String code, String type)
    {
        return code + ":" + type;

    }

    private Map<Integer, Double> createRateMap(JsonArray ratesArray)
    {
        Map<Integer, Double> rates = new HashMap<>();
        for (Object rateObject : ratesArray)
        {
            JsonObject rateJson = (JsonObject) rateObject;
            rates.put(rateJson.getInteger("thickness"), rateJson.getDouble("rate"));
        }
        return rates;
    }

    public Object getName()
    {
        return this.getType() + ":" + this.getCode();
    }

    public String getRates()
    {
        if (this.ratesByThickness.isEmpty()) return "";

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, Double> entry : this.ratesByThickness.entrySet())
        {
            sb.append(entry.getKey() + "=" + entry.getValue()).append(";");
        }
        return sb.toString();
    }
}
