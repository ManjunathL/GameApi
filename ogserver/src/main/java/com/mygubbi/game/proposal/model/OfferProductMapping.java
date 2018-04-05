package com.mygubbi.game.proposal.model;

import io.vertx.core.json.JsonObject;

/**
 * Created by User on 05-04-2018.
 */
public class OfferProductMapping extends JsonObject {

    private static final String ID="id";
    private static final String OFFER_CODE="offerCode";
    private static final String TYPE="type";
    private static final String CATEGORY="category";
    private static final String SUB_CATEGORY="subCategory";
    private static final String MIN_ORDER_VALUE="minOrderValue";
    private static final String MIN_PRODUCT_QTY="minProductQty";

    public OfferProductMapping(JsonObject jsonObject)
    {
        super(jsonObject.getMap());
    }

    public int getID() {
        return this.getInteger(ID);
    }

    public OfferProductMapping setId(int id) {
        this.put(ID, id);
        return this;
    }

    public String getOfferCode() {
        return this.getString(OFFER_CODE);
    }

    public OfferProductMapping setOfferCode(String offerCode) {
        this.put(OFFER_CODE, offerCode);
        return this;
    }

    public String getType() {
        return this.getString(TYPE);
    }

    public OfferProductMapping setType(String type) {
        this.put(TYPE, type);
        return this;
    }

    public String getCategory() {
        return this.getString(CATEGORY);
    }

    public OfferProductMapping setCategory(String category) {
        this.put(CATEGORY, category);
        return this;
    }

    public String getSubCategory() {
        return this.getString(SUB_CATEGORY);
    }

    public OfferProductMapping setSubCategory(String subCategory) {
        this.put(SUB_CATEGORY, subCategory);
        return this;
    }

    public double getMinOrderValue() {
        return this.getDouble(MIN_ORDER_VALUE);
    }

    public OfferProductMapping setMinOrderValue(double minOrderValue) {
        this.put(MIN_ORDER_VALUE, minOrderValue);
        return this;
    }

    public int getMinOrderQty() {
        return this.getInteger(MIN_PRODUCT_QTY);
    }

    public OfferProductMapping setMinOrderQty(int minOrderQty) {
        this.put(MIN_PRODUCT_QTY, minOrderQty);
        return this;
    }



}
