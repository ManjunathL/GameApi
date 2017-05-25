package com.mygubbi.game.proposal.model;

/**
 * Created by Chirag on 20-02-2017.
 */
public class RateCardMasterKey {

    public static final String RATECARD_TYPE = "R";
    public static final String ADDON_TYPE = "D";
    public static final String ACESSORY_TYPE = "A";
    public static final String HARDWARE_TYPE = "H";
    public static final String KNOB_OR_HANDLE_TYPE = "K";

    private String type;
    private String code;
    private String productCategory;

    public RateCardMasterKey(String type, String code, String productCategory) {
        this.type = type;
        this.code = code;
        this.productCategory = productCategory;
    }

    public String getType() {
        return type;
    }

    public String getCode() {
        return code;
    }

    public String getProductCategory() {
        return productCategory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RateCardMasterKey that = (RateCardMasterKey) o;

        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        return !(productCategory != null ? !productCategory.equals(that.productCategory) : that.productCategory != null);

    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (productCategory != null ? productCategory.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RateCardMasterKey{" +
                "type='" + type + '\'' +
                ", code='" + code + '\'' +
                ", productCategory='" + productCategory + '\'' +
                '}';
    }
}