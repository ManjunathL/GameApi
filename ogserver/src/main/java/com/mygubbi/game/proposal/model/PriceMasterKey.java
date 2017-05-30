package com.mygubbi.game.proposal.model;

/**
 * Created by Chirag on 20-02-2017.
 */
public class PriceMasterKey {

    public static final String RATECARD_TYPE = "R";
    public static final String ADDON_TYPE = "D";
    public static final String ACESSORY_TYPE = "A";
    public static final String HARDWARE_TYPE = "H";
    public static final String KNOB_OR_HANDLE_TYPE = "K";
    public static final String HINGE_TYPE = "HI";

    private String rateType;
    private String rateId;
    private String city;

    public PriceMasterKey(String rateType, String rateId, String city) {
        this.rateType = rateType;
        this.rateId = rateId;
        this.city = city;
    }

    public String getRateType() {
        return rateType;
    }

    public String getRateId() {
        return rateId;
    }

    public String getCity() {
        return city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PriceMasterKey that = (PriceMasterKey) o;

        if (rateType != null ? !rateType.equals(that.rateType) : that.rateType != null) return false;
        if (rateId != null ? !rateId.equals(that.rateId) : that.rateId != null) return false;
        return !(city != null ? !city.equals(that.city) : that.city != null);

    }

    @Override
    public int hashCode() {
        int result = rateType != null ? rateType.hashCode() : 0;
        result = 31 * result + (rateId != null ? rateId.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PriceMasterKey{" +
                "rateType='" + rateType + '\'' +
                ", rateId='" + rateId + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}