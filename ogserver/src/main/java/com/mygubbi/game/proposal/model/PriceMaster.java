package com.mygubbi.game.proposal.model;

import com.mygubbi.common.DateUtil;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Date;


/**
 * Created by Chirag on 20-02-2017.
 */
public class PriceMaster {

    public static final String ALL_CITIES = "all";

    public static final String RATE_TYPE = "rateType";
    public static final String RATE_ID = "rateId";
    public static final String PRICE = "price";
    public static final String SOURCE_PRICE = "sourcePrice";
    public static final String CITY = "city";
    public static final String FROM_DATE = "fromDate";
    public static final String TO_DATE = "toDate";

    private String rateType;
    private String rateId;
    private String city;
    private double price;
    private double sourcePrice;
    private Date fromDate;
    private Date toDate;

    private final static Logger LOG = LogManager.getLogger(PriceMaster.class);

    public PriceMaster() {

    }

    public PriceMaster(JsonObject json)
    {
        Date fromDateInSql = DateUtil.convertDate(json.getString(FROM_DATE));
        Date toDateInSql = DateUtil.convertDate(json.getString(TO_DATE));;

        this.setRateType(json.getString(RATE_TYPE))
                .setRateId(json.getString(RATE_ID))
                .setCity(json.getString(CITY))
                .setPrice(json.getDouble(PRICE))
                .setSourcePrice(json.getDouble(SOURCE_PRICE))
                .setFromDate(fromDateInSql)
                .setToDate(toDateInSql);
    }

    public String getRateType() {
        return rateType;
    }

    public PriceMaster setRateType(String rateType) {
        this.rateType = rateType;
        return this;
    }

    public String getRateId() {
        return rateId;
    }

    public PriceMaster setRateId(String rateId) {
        this.rateId = rateId;
        return this;
    }

    public double getPrice() {
        return price;
    }

    public PriceMaster setPrice(double price) {
        this.price = price;
        return this;
    }

    public double getSourcePrice()
    {
        return sourcePrice;
    }

    public PriceMaster setSourcePrice(double price) {
        this.sourcePrice = price;
        return this;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public PriceMaster setFromDate(Date fromDate) {
        this.fromDate = fromDate;
        return this;
    }

    public Date getToDate() {
        return toDate;
    }

    public PriceMaster setToDate(Date toDate) {
        this.toDate = toDate;
        return this;
    }

    public String getCity() {
        return city;
    }

    public PriceMaster setCity(String city) {
        this.city = city;
        return this;
    }

    public boolean isValidForDate(Date inputDate) {
        int before = inputDate.compareTo(this.getFromDate());
        int after = inputDate.compareTo(this.getToDate());
        LOG.debug("Input date:" + inputDate + " from date: " + this.getFromDate() + " to date:" + this.getToDate()
                + " before:" + before + " after:" + after);
        return (before >= 0 && after <= 0);
    }

    public PriceMasterKey getKey() {
        return new PriceMasterKey(this.getRateType(), this.getRateId(), this.getCity());
    }

    @Override
    public String toString() {
        return "PriceMaster{" +
                "rateType='" + rateType + '\'' +
                ", rateId='" + rateId + '\'' +
                ", city='" + city + '\'' +
                ", price=" + price +
                ", fromDate=" + fromDate +
                ", toDate=" + toDate +
                '}';
    }

    public JsonObject toJson()
    {
        return new JsonObject().put("rateType", this.rateType).put("rateId", this.rateId).put("city", this.city)
                .put("price", this.price).put("sourcePrice",this.sourcePrice)
                .put("fromDate", this.fromDate.toString()).put("toDate", this.toDate.toString());
    }
}
