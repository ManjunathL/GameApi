package com.mygubbi.game.proposal.model.dw;

import com.mygubbi.game.proposal.ProductAddon;
import com.mygubbi.game.proposal.ProductLineItem;
import com.mygubbi.game.proposal.model.ProposalHeader;
import com.mygubbi.game.proposal.model.ProposalVersion;
import com.mygubbi.game.proposal.price.AddonPriceHolder;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Created by User on 09-08-2017.
 */
public class DWProposalAddon extends JsonObject {

    private final static Logger LOG = LogManager.getLogger(DWProposalAddon.class);
    public static final String ID="id";
    public static final String PROPOSALID="proposalId";
    public static final String VERSION="version";
    public static final String PROPOSALTITLE="proposalTitle";
    public static final String PRICEDATE="priceDate";
    public static final String BUSINESSDATE="businessDate";
    public static final String REGION="region";
    public static final String CRMID="crmId";
    public static final String QUOTENO="quoteNo";
    public static final String CATEGORY="category";
    public static final String SUBCATEGORY="subCategory";
    public static final String SPACETYPE="spaceType";
    public static final String ROOM="room";
    public static final String ADDON_ID="addonId";
    private static final String CODE = "code";
    private static final String ADDON_CATEGORY_CODE = "categoryCode";
    private static final String PRODUCT_TYPE_CODE = "productTypeCode";
    private static final String PRODUCT_SUBTYPE_CODE = "productSubtypeCode";
    private static final String PRODUCT = "product";
    private static final String BRAND_CODE = "brandCode";
    private static final String CATALOGUE_CODE = "catalogueCode";
    private static String QUANTITY = "quantity";
    private static String UOM = "uom";
    private static String UPDATED_BY = "updatedBy";
    private static String UNIT_PRICE = "unitPrice";
    private static String UNIT_SOURCECOST = "unitSourceCost";
    private static String PRICE = "price";
    private static String PRICEWOTAX = "priceWoTax";
    private static String SOURCE_COST = "sourceCost";
    private static String PROFIT = "addonProfit";
    private static String MARGIN = "addonMargin";

    public DWProposalAddon()
    {}

    public DWProposalAddon setDwAddonObjects(ProposalHeader proposalHeader,ProposalVersion proposalVersion,  ProductAddon productAddon,  AddonPriceHolder addonPriceHolder)
    {
        DWProposalAddon dwProposalAddon = new DWProposalAddon();

        dwProposalAddon.setProposalID(proposalHeader.getId());
        dwProposalAddon.setVersion(Double.parseDouble(proposalVersion.getVersion()));
        dwProposalAddon.setProposalTitle(proposalHeader.getQuotationFor());
        dwProposalAddon.setPriceDate(proposalHeader.getPriceDate());
        dwProposalAddon.setBusinessDate(proposalVersion.getDate());
        dwProposalAddon.setRegion(proposalHeader.getProjectCity());
        dwProposalAddon.setCrmId(proposalHeader.getCrmId());
        dwProposalAddon.setQuoteNo(proposalHeader.getQuoteNum());
        dwProposalAddon.setSpaceType(productAddon.getSpaceType());
        dwProposalAddon.setRoom(productAddon.getRoomCode());
        dwProposalAddon.setAddonId(productAddon.getId());
        dwProposalAddon.setCode(productAddon.getCode());
        dwProposalAddon.setCategory("BP");
        dwProposalAddon.setSubCategory(productAddon.getCategoryCode());
        dwProposalAddon.setProductTypeCode(productAddon.getProductTypeCode());
        dwProposalAddon.setProductSubTypeCode(productAddon.getProductSubtypeCode());
        dwProposalAddon.setProduct(productAddon.getProduct());
        dwProposalAddon.setBrandCode(productAddon.getBrandCode());
        dwProposalAddon.setCatalogueCode(productAddon.getCatalogueCode());
        dwProposalAddon.setUom(productAddon.getUom());
        dwProposalAddon.setQuantity(productAddon.getQuantity());
        dwProposalAddon.setUpdatedBy(productAddon.getUpdatedBy());
        dwProposalAddon.setUnitPrice(addonPriceHolder.getUnitPrice());
        dwProposalAddon.setUnitSourceCost(addonPriceHolder.getUnitSourceCost());
        dwProposalAddon.setPrice(addonPriceHolder.getPrice());
        dwProposalAddon.setPriceWoTax(addonPriceHolder.getPriceWoTax());
        dwProposalAddon.setSourceCost(addonPriceHolder.getSourceCost());
        dwProposalAddon.setProfit(addonPriceHolder.getProfit());
        dwProposalAddon.setMargin(addonPriceHolder.getMargin());

        return dwProposalAddon;
    }

    public String getCrmid() {
        return this.getString(CRMID);
    }

    public DWProposalAddon setCrmId(String crm)
    {
        put(CRMID,crm);
        return this;
    }

    public String getQuotenoo() {
        return this.getString(QUOTENO);
    }

    public DWProposalAddon setQuoteNo(String quote)
    {
        put(QUOTENO,quote);
        return this;
    }
    public int getID() {
        return this.getInteger(ID);
    }

    public DWProposalAddon setId(int id)
    {
        put(ID,id);
        return this;
    }

    public int getProposalId() {
        return this.getInteger(PROPOSALID);
    }

    public DWProposalAddon setProposalID(int proposalID)
    {
        put(PROPOSALID,proposalID);
        return this;
    }


    public double getVersion() {
        return this.getDouble(VERSION);
    }

    public DWProposalAddon setVersion(double version)
    {
        put(VERSION,version);
        return this;
    }

    public String getProposalTitle() {
        return this.getString(PROPOSALTITLE);
    }

    public DWProposalAddon setProposalTitle(String proposalTitle)
    {
        put(PROPOSALTITLE,proposalTitle);
        return this;
    }


    public Date getPriceDate() {
        return (Date) this.getValue(PRICEDATE);
    }

    public DWProposalAddon setPriceDate(Date priceDate)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String format = formatter.format(priceDate);
        this.put(PRICEDATE, format);
        return this;
    }

    public Date getBusinessDate() {
        return (Date) this.getValue(BUSINESSDATE);
    }

    public DWProposalAddon setBusinessDate(Date businessDate)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String format = formatter.format(businessDate);
        this.put(BUSINESSDATE, format);
        return this;
    }

    public String getRegion() {
        return this.getString(REGION);
    }

    public DWProposalAddon setRegion(String region)
    {
        put(REGION,region);
        return this;
    }

    public String getCategory() {
        return this.getString(CATEGORY);
    }

    public DWProposalAddon setCategory(String category)
    {
        put(CATEGORY,category);
        return this;
    }

    public String getSubCategory() {
        return this.getString(SUBCATEGORY);
    }

    public DWProposalAddon setSubCategory(String subCategory)
    {
        put(SUBCATEGORY,subCategory);
        return this;
    }

    public String getSpaceType() {
        return this.getString(SPACETYPE);
    }

    public DWProposalAddon setSpaceType(String spaceType)
    {
        put(SPACETYPE,spaceType);
        return this;
    }

    public String getRoom() {
        return this.getString(ROOM);
    }

    public DWProposalAddon setRoom(String room)
    {
        put(ROOM,room);
        return this;
    }

    public int getAddonId() {
        return this.getInteger(ADDON_ID);
    }

    public DWProposalAddon setAddonId(int addonId)
    {
        put(ADDON_ID,addonId);
        return this;
    }

    public String getCode() {
        return this.getString(CODE);
    }

    public DWProposalAddon setCode(String code)
    {
        put(CODE,code);
        return this;
    }

    public String getAddonCategoryCode() {
        return this.getString(ADDON_CATEGORY_CODE);
    }

    public DWProposalAddon setAddonCategoryCode(String addonCategoryCode)
    {
        put(CATEGORY,addonCategoryCode);
        return this;
    }


    public String getProductTypeCode() {
        return this.getString(PRODUCT_TYPE_CODE);
    }

    public DWProposalAddon setProductTypeCode(String productTypeCode)
    {
        put(PRODUCT_TYPE_CODE,productTypeCode);
        return this;
    }

    public String getProductSubtypeCode() {
        return this.getString(PRODUCT_SUBTYPE_CODE);
    }

    public DWProposalAddon setProductSubTypeCode(String productSubTypeCode)
    {
        put(PRODUCT_SUBTYPE_CODE,productSubTypeCode);
        return this;
    }

    public String getProduct() {
        return this.getString(PRODUCT);
    }

    public DWProposalAddon setProduct(String product)
    {
        put(PRODUCT,product);
        return this;
    }

    public String getBrandCode() {
        return this.getString(BRAND_CODE);
    }

    public DWProposalAddon setBrandCode(String brandCode)
    {
        put(BRAND_CODE,brandCode);
        return this;
    }

    public String getCatalogueCode() {
        return this.getString(CATALOGUE_CODE);
    }

    public DWProposalAddon setCatalogueCode(String catalogueCode)
    {
        put(CATALOGUE_CODE,catalogueCode);
        return this;
    }

    public double getQuantity() {
        return this.getDouble(QUANTITY);
    }

    public DWProposalAddon setQuantity(double quantity)
    {
        put(QUANTITY,quantity);
        return this;
    }

    public String getUom() {
        return this.getString(UOM);
    }

    public DWProposalAddon setUom(String uom) {
        put(UOM,uom);
        return this;
    }

    public String getUpdatedBy() {
        return this.getString(UPDATED_BY);
    }

    public DWProposalAddon setUpdatedBy(String updatedBy)
    {
        put(UPDATED_BY,updatedBy);
        return this;
    }


    public double getUnitPrice() {
        return this.getDouble(UNIT_PRICE);
    }

    public DWProposalAddon setUnitPrice(double unitPrice)
    {
        put(UNIT_PRICE,unitPrice);
        return this;
    }

    public double getUnitSourceCost() {
        return this.getDouble(UNIT_SOURCECOST);
    }

    public DWProposalAddon setUnitSourceCost(double unitSourceCost)
    {
        put(UNIT_SOURCECOST,unitSourceCost);
        return this;
    }

    public double getPrice() {
        return this.getDouble(PRICE);
    }

    public DWProposalAddon setPrice(double price)
    {
        put(PRICE,price);
        return this;
    }

    public double getPriceWoTax() {
        return this.getDouble(PRICEWOTAX);
    }

    public DWProposalAddon setPriceWoTax(double priceWoTax)
    {
        put(PRICEWOTAX,priceWoTax);
        return this;
    }

    public double getSourceCost() {
        return this.getDouble(SOURCE_COST);
    }

    public DWProposalAddon setSourceCost(double sourceCost)
    {
        put(SOURCE_COST,sourceCost);
        return this;
    }

    public double getProfit() {
        return this.getDouble(PROFIT);
    }

    public DWProposalAddon setProfit(double profit)
    {
        put(PROFIT,profit);
        return this;
    }

    public double getMargin() {
        return this.getDouble(MARGIN);
    }

    public DWProposalAddon setMargin(double margin)
    {
        put(MARGIN,margin);
        return this;
    }

}
