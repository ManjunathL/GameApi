package com.mygubbi.game.proposal;

import io.vertx.core.json.JsonObject;

/**
 * Created by Sunil on 26-04-2016. May not be needed as we don't process the on the server or maybe for the report purpose.
 */
public class ProductAddon extends JsonObject
{
    public static final String ACCESSORY_TYPE = "Accessories";
    public static final String APPLIANCE_TYPE = "Appliances";
    public static final String COUNTERTOP_TYPE = "Counter Top";
    public static final String SERVICE_TYPE = "Services";
    public static final String LOOSE_FURNITURE_TYPE = "Loose Furniture";
    public static final String CUSTOM_ADDON_TYPE = "Custom Addon";

    private static final String ID = "id";

    private static final String SEQ = "seq";
    private static final String CODE = "code";
    private static final String SPACE_TYPE = "spaceType";
    private static final String ROOMCODE = "roomcode";
    private static final String FROM_VERSION = "fromVersion";
    private static final String PROPOSAL_ID = "proposalId";
    private static final String ADDON_CATEGORY_CODE = "categoryCode";
    private static final String PRODUCT_TYPE_CODE = "productTypeCode";
    private static final String PRODUCT_SUBTYPE_CODE = "productSubtypeCode";
    private static final String PRODUCT = "product";
    private static final String BRAND_CODE = "brandCode";
    private static final String CATALOGUE_CODE = "catalogueCode";
    private static final String TITLE = "title";
    private static String QUANTITY = "quantity";
    private static String AMOUNT = "amount";
    private static String RATE = "rate";
    private static String REMARKS = "remarks";
    private static String UPDATED_BY = "updatedBy";


    public ProductAddon()
    {

    }

    public ProductAddon(JsonObject data)
    {
        super(data.getMap());
    }

    public double getQuantity()
    {
        return (isValueNull(QUANTITY)?0.0:this.getDouble(QUANTITY));
    }
    private boolean isValueNull(String key){
        if(this.getValue(key) == null ) return true;
        return  false;
    }

    public double getRate()
    {
        return this.getDouble(RATE);
    }

    public double getAmount()
    {
        return this.getDouble(AMOUNT);
    }

    public int getProposalId()
    {
        return this.getInteger(PROPOSAL_ID);
    }

    public int getId()
    {
        return this.getInteger(ID);
    }

    public String getCategoryCode()
    {
        return this.getString(ADDON_CATEGORY_CODE);
    }

    public String getBrandCode()
    {
        return this.getString(BRAND_CODE);
    }

    public String getCatalogueCode()
    {
        return this.getString(CATALOGUE_CODE);
    }

    public String getCode()
    {
        return this.getString(CODE);
    }

    public String getFromVersion()
    {
        return this.getString(FROM_VERSION);
    }

    public String getSpaceType()
    {
        return this.getString(SPACE_TYPE);
    }


    public String getRoomCode()
    {
        return this.getString(ROOMCODE);
    }


    public String getUom()
    {
        return "uom";
    }

    public boolean isAccessory()
    {
        return ACCESSORY_TYPE.equals(this.getCategoryCode());
    }

    public boolean isAppliance()
    {
        return APPLIANCE_TYPE.equals(this.getCategoryCode());
    }
    public boolean isLooseFurniture()
    {
        return LOOSE_FURNITURE_TYPE.equals(this.getCategoryCode());
    }

    public boolean isCounterTop()
    {
        return COUNTERTOP_TYPE.equals(this.getCategoryCode());
    }

    public boolean isService()
    {
        return SERVICE_TYPE.equals(this.getCategoryCode());
    }

    public boolean isCustomAddon()
    {
        return CUSTOM_ADDON_TYPE.equals(this.getCategoryCode());
    }

    public String getTitle()
    {
        return this.getString(TITLE);
    }

    public String getProductTypeCode() { return this.getString(PRODUCT_TYPE_CODE);}

    public String getProductSubtypeCode() { return this.getString(PRODUCT_SUBTYPE_CODE);}

    public String getProduct() {
        return this.getString(PRODUCT);
    }

    public String getExtendedTitle() { return this.getProductTypeCode() + "-" + this.getProductSubtypeCode() + "-" +this.getProduct();}
    public String getCustomTitle() { return this.getProduct();}

    public ProductAddon setRate(double rate)
    {
        this.put(RATE, rate);
        return this;
    }

    public ProductAddon setAmount(double amount)
    {
        this.put(AMOUNT, amount);
        return this;
    }

    public ProductAddon setFromVersion(String version)
    {
        this.put(FROM_VERSION, version);
        return this;
    }

    public  String getREMARKS() {
        return this.getString(REMARKS);
    }

    public static void setREMARKS(String REMARKS) {
        ProductAddon.REMARKS = REMARKS;
    }

    public String getUpdatedBy() {
        return this.getString(UPDATED_BY);
    }

}

