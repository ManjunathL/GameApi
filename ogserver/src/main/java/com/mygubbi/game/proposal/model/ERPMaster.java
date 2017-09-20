package com.mygubbi.game.proposal.model;

import io.vertx.core.json.JsonObject;

/**
 * Created by User on 17-08-2017.
 */
public class ERPMaster extends JsonObject {


    private static final String ITEM_NAME = "itemName";
    private static final String REFERENCE_PART_NO = "referencePartNo";
    private static final String INV_CATEGORY = "invCategory";
    private static final String UOM = "uom";
    private static final String IS_ACTIVE = "isActive";
    private static final String ITEM_CODE = "itemCode";
    private static final String ITEM_REFERENCE_CODE = "itemRefCode";
    private static final String PRICE = "price";
    private static final String SOURCE_PRICE = "sourcePrice";

    public ERPMaster()
    {

    }

    public ERPMaster(JsonObject json)
    {
        super(json.getMap());
    }


    public String getItemName() {
        return this.getString(ITEM_NAME);
    }

    public ERPMaster setItemName(String itemName){
        put(ITEM_NAME,itemName);
        return this;
    }

    public String getReferencePartNo() {
        return this.getString(REFERENCE_PART_NO);
    }

    public ERPMaster setReferencePartNo(String referencePartNo)
    {
       put(REFERENCE_PART_NO,referencePartNo);
        return this;
    }

    public String getInvCategory() {
        return this.getString(INV_CATEGORY);
    }

    public ERPMaster setInvCategory(String invCategory)
    {
        put(INV_CATEGORY,invCategory);
        return this;
    }


    public String getUOM() {
        return this.getString(UOM);
    }

    public ERPMaster setUom(String uom)
    {
        put(UOM,uom);
        return this;
    }

    public String getIsActive() {
        return this.getString(IS_ACTIVE);
    }

    public ERPMaster setIsActive(String isActive)
    {
        put(IS_ACTIVE,isActive);
        return this;
    }

    public String getItemCode() {
        return this.getString(ITEM_CODE);
    }


    public ERPMaster setItemCode(String itemCode)
    {
        put(ITEM_CODE,itemCode);
        return this;
    }

    public String getItemReferenceCode() {
        return this.getString(ITEM_REFERENCE_CODE);
    }

    public ERPMaster setItemReferencePartNo(String itemReferencePartNo)
    {
        put(ITEM_REFERENCE_CODE, itemReferencePartNo);
        return this;
    }

    public double getPrice() {
        return this.getDouble(PRICE);
    }

    public ERPMaster setPrice(double price)
    {
        put(PRICE, price);
        return this;
    }

    public double getSourcePrice() {
        return this.getDouble(SOURCE_PRICE);
    }

    public ERPMaster setSourcePrice(double sourcePrice)
    {
        put(SOURCE_PRICE, sourcePrice);
        return this;
    }
}
