package com.mygubbi.game.proposal.model.dw;

import com.mygubbi.game.proposal.model.ProposalHeader;
import com.mygubbi.game.proposal.model.ProposalVersion;
import com.mygubbi.game.proposal.price.ModulePriceHolder;
import com.mygubbi.game.proposal.price.VersionPriceHolder;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Created by shilpa on 9/8/17.
 */
public class DwProposalVersion extends JsonObject {
    private final static Logger LOG = LogManager.getLogger(DwProposalVersion.class);


    private static final String ID = "id";
    private static final String PROPOSAL_ID = "proposalId";
    private static final String VERSION = "version";
    private static final String PROPOSAL_TITLE = "proposalTitle";
    private static final String REGION = "region";
    public static final String CRMID="crmId";
    public static final String QUOTENO="quoteNo";
    public static final String STATUS="status";
    public static final String DISCOUNT_AMOUNT="discountAmount";
    public static final String DISCOUNT_AMOUNT_PERC="discountPercentage";
    private static final String PROJECT_NAME = "projectName";
    private static final String DESIGNER_NAME = "designerName";
    private static final String SALES_NAME = "salesName";
    private static final String BEF_PROD_SPEC = "beforeProductionSpecification";
    private static final String FROM_PROPOSAL = "fromProposal";
    private static final String OFFER_TYPE = "offerType";
    private static final String PACKAGE_FLAG = "packageFlag";

    private static final String DESIGN_PARTNER_NAME = "designPartnerName";
    private static final String PROPOSAL_UPDATED_BY = "proposalUpdatedBy";
    private static final String PROPOSAL_CREATE_DATE = "proposalCreateDate";
    private static final String PROPOSAL_PRICE_DATE = "proposalPriceDate";
    private static final String BUSINESS_DATE = "businessDate";
    private static final String VERSION_CREATED_BY = "versionCreatedBy";
    private static final String VERSION_CREATED_ON = "versionCreatedOn";
    private static final String VERSION_UPDATED_BY = "versionUpdatedBy";
    private static final String VERSION_UPDATED_ON = "versionUpdatedOn";
    private static final String STD_MODULE_COUNT = "stdModuleCount";
    private static final String N_STD_MODULE_COUNT = "nStdModuleCount";
    private static final String HIKE_MODULE_COUNT = "hikeModuleCount";
    private static final String STD_MODULE_PRICE = "stdModulePrice";
    private static final String N_STD_MODULE_PRICE = "nStdModulePrice";
    private static final String HIKE_MODULE_PRICE = "hikeModulePrice";
    private static final String HIKE_MODULE_COST = "hikeModuleCost";
    private static final String VR_PRICE = "vrPrice";
    private static final String VR_PRICE_AFTER_DISCOUNT = "vrPriceAfterDiscount";
    private static final String VR_PRICE_AFTER_TAX = "vrPriceAfterTax";
    private static final String VR_COST = "vrCost";
    private static final String VR_PROFIT = "vrProfit";
    private static final String VR_MARGIN = "vrMargin";
    private static final String PR_PRICE = "prPrice";
    private static final String PR_PRICE_AFTER_DISCOUNT = "prPriceAfterDiscount";
    private static final String PR_PRICE_AFTER_TAX = "prPriceAfterTax";
    private static final String PR_COST = "prCost";
    private static final String PR_PROFIT = "prProfit";
    private static final String PR_MARGIN = "prMargin";
    private static final String WW_PRICE = "wwPrice";
    private static final String WW_PRICE_AFTER_DISCOUNT = "wwPriceAfterDiscount";
    private static final String WW_PRICE_AFTER_TAX = "wwPriceAfterTax";
    private static final String WW_COST = "wwCost";
    private static final String WW_PROFIT = "wwProfit";
    private static final String WW_MARGIN = "wwMargin";
    public static final String HW_PRICE = "hwPrice";
    public static final String HW_PRICEAFTERTAX = "hwPriceAfterTax";
    private static final String HW_PRICE_AFTER_DISCOUNT = "hwPriceAfterDiscount";
    public static final String HW_COST = "hwCost";
    public static final String HW_PROFIT = "hwProfit";
    public static final String HW_MARGIN = "hwMargin";
    public static final String ACC_PRICE = "accPrice";
    public static final String ACC_PRICEAFTERTAX = "accPriceAfterTax";
    private static final String ACC_PRICE_AFTER_DISCOUNT = "accPriceAfterDiscount";
    public static final String ACC_COST = "accCost";
    public static final String ACC_PROFIT = "accProfit";
    public static final String ACC_MARGIN = "accMargin";
    public static final String HK_PRICE = "hkPrice";
    public static final String HK_PRICEAFTERTAX = "hkPriceAfterTax";
    private static final String HK_PRICE_AFTER_DISCOUNT = "hkPriceAfterDiscount";
    public static final String HK_COST = "hkCost";
    public static final String HK_PROFIT = "hkProfit";
    public static final String HK_MARGIN = "hkMargin";
    public static final String HINGE_PRICE = "hingePrice";
    public static final String HINGE_PRICEAFTERTAX = "hingePriceAfterTax";
    private static final String HINGE_PRICE_AFTER_DISCOUNT = "hingePriceAfterDiscount";
    public static final String HINGE_COST = "hingeCost";
    public static final String HINGE_PROFIT = "hingeProfit";
    public static final String HINGE_MARGIN = "hingeMargin";
    public static final String LC_PRICE = "lcPrice";
    public static final String LC_PRICEAFTERTAX = "lcPriceAfterTax";
    private static final String LC_PRICE_AFTER_DISCOUNT = "lcPriceAfterDiscount";
    public static final String LC_COST = "lcCost";
    public static final String LC_PROFIT = "lcProfit";
    public static final String LC_MARGIN = "lcMargin";
    public static final String LA_PRICE = "laPrice";
    public static final String LA_PRICEAFTERTAX = "laPriceAfterTax";
    private static final String LA_PRICE_AFTER_DISCOUNT = "laPriceAfterDiscount";
    public static final String LA_COST = "laCost";
    public static final String LA_PROFIT = "laProfit";
    public static final String LA_MARGIN = "laMargin";
    private static final String BP_PRICE = "bpPrice";
    private static final String BP_PRICE_AFTER_DISCOUNT = "bpPriceAfterDiscount";
    private static final String BP_PRICE_AFTER_TAX = "bpPriceAfterTax";
    private static final String BP_COST = "bpCost";
    private static final String BP_PROFIT = "bpProfit";
    private static final String BP_MARGIN = "bpMargin";
    private static final String SV_PRICE = "svPrice";
    private static final String SV_PRICE_AFTER_DISCOUNT = "svPriceAfterDiscount";
    private static final String SV_PRICE_AFTER_TAX = "svPriceAfterTax";
    private static final String SV_COST = "svCost";
    private static final String SV_PROFIT = "svProfit";
    private static final String SV_MARGIN = "svMargin";
    private static final String KITCHEN_COUNT = "kitchenCount";
    private static final String WARDROBE_COUNT = "wardrobeCount";
    private static final String NS_PRODUCT_COUNT = "NSproductCount";
    private static final String BP_COUNT = "bpCount";
    private static final String SV_COUNT = "svCount";


    public DwProposalVersion() {
    }

    public DwProposalVersion(JsonObject data) {
        super(data.getMap());
    }

    public String getBefProdSpec() {
         return this.getString(BEF_PROD_SPEC);
    }

    public Integer getFromProposal() {
        return this.getInteger(FROM_PROPOSAL);
    }

    public  String getOfferType() {
        return this.getString(OFFER_TYPE);
    }

    public String getPackageFlag() {
        return this.getString(PACKAGE_FLAG);
    }

    public DwProposalVersion setBefProdSpec(String val)
    {
        put(BEF_PROD_SPEC,val);
        return this;
    }
    public DwProposalVersion setFromProposal(Integer val)
    {
        put(FROM_PROPOSAL,val);
        return this;
    }
    public DwProposalVersion setOfferType(String val)
    {
        put(OFFER_TYPE,val);
        return this;
    }
    public DwProposalVersion setPackageFlag(String val)
    {
        put(PACKAGE_FLAG,val);
        return this;
    }



    public String getCrmid() {
        return this.getString(CRMID);
    }

    public DwProposalVersion setCrmId(String crm)
    {
        put(CRMID,crm);
        return this;
    }

    public String getQuotenoo() {
        return this.getString(QUOTENO);
    }

    public DwProposalVersion setQuoteNo(String quote)
    {
        put(QUOTENO,quote);
        return this;
    }
    public String getStatus() {
        return this.getString(STATUS);
    }

    public DwProposalVersion setStatus(String status)
    {
        put(STATUS,status);
        return this;
    }
    public double getDiscountAmount() {
        return this.getDouble(DISCOUNT_AMOUNT);
    }

    public DwProposalVersion setDiscountAmount(double price) {
        this.put(DISCOUNT_AMOUNT, price);
        return this;
    }
    public double getDiscountAmountPerc() {
        return this.getDouble(DISCOUNT_AMOUNT_PERC);
    }

    public DwProposalVersion setDiscountAmountPerc(double price) {
        this.put(DISCOUNT_AMOUNT_PERC, price);
        return this;
    }


    public double getId() {
        return this.getDouble(ID);
    }

    public Integer getProposalId() {
        return this.getInteger(PROPOSAL_ID);
    }

    public double getVersion() {
        return this.getDouble(VERSION);
    }

    public String getProposalTitle() {
        return this.getString(PROPOSAL_TITLE);
    }

    public String getRegion() {
        return this.getString(REGION);
    }

    public String getProjectName() {
        return this.getString(PROJECT_NAME);
    }

    public String getDesignerName() {
        return this.getString(DESIGNER_NAME);
    }

    public String getSalesName() {
        return this.getString(SALES_NAME);
    }

    public String getDesignPartnerName() {
        return this.getString(DESIGN_PARTNER_NAME);
    }

    public String getProposalUpdatedBy() {
        return this.getString(PROPOSAL_UPDATED_BY);
    }

    public Date getProposalCreateDate() {
        return (Date) this.getValue(PROPOSAL_CREATE_DATE);
    }

    public Date getProposalPriceDate() {
        return (Date) this.getValue(PROPOSAL_PRICE_DATE);
    }

    public Date getBusinessDate() {
        return (Date) this.getValue(BUSINESS_DATE);
    }

    public String getVersionCreatedBy() {
        return this.getString(VERSION_CREATED_BY);
    }

    public Date getVersionCreatedOn() {
        return (Date) this.getValue(VERSION_CREATED_ON);
    }

    public String getVersionUpdatedBy() {
        return this.getString(VERSION_UPDATED_BY);
    }

    public Date getVersionUpdatedOn() {
        return (Date) this.getValue(VERSION_UPDATED_ON);
    }

    public Integer getStdModuleCount() {
        return this.getInteger(STD_MODULE_COUNT);
    }

    public Integer getnStdModuleCount() {
        return this.getInteger(N_STD_MODULE_COUNT);
    }

    public Integer getHikeModuleCount() {
        return this.getInteger(HIKE_MODULE_COUNT);
    }

    public double getStdModulePrice() {
        return this.getDouble(STD_MODULE_PRICE);
    }

    public double getnStdModulePrice() {
        return this.getDouble(N_STD_MODULE_PRICE);
    }

    public double getHikeModulePrice() {
        return this.getDouble(HIKE_MODULE_PRICE);
    }

    public double getHikeModuleCost(){return this.getDouble(HIKE_MODULE_COST);}

    public double getVersionPrice() {
        return this.getDouble(VR_PRICE);
    }

    public double getVersionPriceAfterDiscount() {
        return this.getDouble(VR_PRICE_AFTER_DISCOUNT);
    }

    public double getVersionPriceAfterTax() {
        return this.getDouble(VR_PRICE_AFTER_TAX);
    }

    public double getVersionCost() {
        return this.getDouble(VR_COST);
    }

    public double getVersionProfit() {
        return this.getDouble(VR_PROFIT);
    }

    public double getVersionMargin() {
        return this.getDouble(VR_MARGIN);
    }

    public double getProductPrice() {
        return this.getDouble(PR_PRICE);
    }

    public double getProductPriceAfterDiscount() {
        return this.getDouble(PR_PRICE_AFTER_DISCOUNT);
    }

    public double getProductPriceAfterTax() {
        return this.getDouble(PR_PRICE_AFTER_TAX);
    }

    public double getProductCost() {
        return this.getDouble(PR_COST);
    }

    public double getProductProfit() {
        return this.getDouble(PR_PROFIT);
    }

    public double getProductMargin() {
        return this.getDouble(PR_MARGIN);
    }

    public double getWoodWorkPrice() {
        return this.getDouble(WW_PRICE);
    }

    public double getWoodWorkPriceAfterDiscount() {
        return this.getDouble(WW_PRICE_AFTER_DISCOUNT);
    }

    public double getWoodWorkPriceAfterTax() {
        return this.getDouble(WW_PRICE_AFTER_TAX);
    }

    public double getWoodWorkCost() {
        return this.getDouble(WW_COST);
    }

    public double getWoodWorkProfit() {
        return this.getDouble(WW_PROFIT);
    }

    public double getWoodWorkMargin() {
        return this.getDouble(WW_MARGIN);
    }

    public double getHardwarePrice() {
        return this.getDouble(HW_PRICE);
    }

    public double getHardwarePriceAfterTax() {
        return this.getDouble(HW_PRICEAFTERTAX);
    }

    public double getHardwareCost() {
        return this.getDouble(HW_COST);
    }

    public double getHardwareProfit() {
        return this.getDouble(HW_PROFIT);
    }

    public double getHardwareMargin() {
        return this.getDouble(HW_MARGIN);
    }

    public double getAccessoryPrice() {
        return this.getDouble(ACC_PRICE);
    }

    public double getAccessoryPriceAfterTax() {
        return this.getDouble(ACC_PRICEAFTERTAX);
    }

    public double getAccessoryCost() {
        return this.getDouble(ACC_COST);
    }

    public double getAccessoryProfit() {
        return this.getDouble(ACC_PROFIT);
    }

    public double getAccessoryMargin() {
        return this.getDouble(ACC_MARGIN);
    }

    public double getHandleKnobPrice() {
        return this.getDouble(HK_PRICE);
    }

    public double getHandleKnobPriceAfterTax() {
        return this.getDouble(HK_PRICEAFTERTAX);
    }

    public double getHandleKnobCost() {
        return this.getDouble(HK_COST);
    }

    public double getHandleKnobProfit() {
        return this.getDouble(HK_PROFIT);
    }

    public double getHandleKnobMargin() {
        return this.getDouble(HK_MARGIN);
    }

    public double getHingePrice() {
        return this.getDouble(HINGE_PRICE);
    }

    public double getHingePriceAfterTax() {
        return this.getDouble(HINGE_PRICEAFTERTAX);
    }

    public double getHingeCost() {
        return this.getDouble(HINGE_COST);
    }

    public double getHingeProfit() {
        return this.getDouble(HINGE_PROFIT);
    }

    public double getHingeMargin() {
        return this.getDouble(HINGE_MARGIN);
    }

    public double getLConnectorPrice() {
        return this.getDouble(LC_PRICE);
    }

    public double getLConnectorPriceAfterTax() {
        return this.getDouble(LC_PRICEAFTERTAX);
    }

    public double getLConnectorCost() {
        return this.getDouble(LC_COST);
    }

    public double getLConnectorProfit() {
        return this.getDouble(LC_PROFIT);
    }

    public double getLConnectorMargin() {
        return this.getDouble(LC_MARGIN);
    }

    public double getLabourPrice() {
        return this.getDouble(LA_PRICE);
    }

    public double getLabourPriceAfterTax() {
        return this.getDouble(LA_PRICEAFTERTAX);
    }

    public double getLabourCost() {
        return this.getDouble(LA_COST);
    }

    public double getLabourProfit() {
        return this.getDouble(LA_PROFIT);
    }

    public double getLabourMargin() {
        return this.getDouble(LA_MARGIN);
    }

    public double getBroughtoutProductPrice() {
        return this.getDouble(BP_PRICE);
    }

    public double getBroughtoutProductPriceAfterDiscount() {
        return this.getDouble(BP_PRICE_AFTER_DISCOUNT);
    }

    public double getBroughtoutProductPriceAfterTax() {
        return this.getDouble(BP_PRICE_AFTER_TAX);
    }

    public double getBroughtoutProductCost() {
        return this.getDouble(BP_COST);
    }

    public double getBroughtoutProductProfit() {
        return this.getDouble(BP_PROFIT);
    }

    public double getBroughtoutProductMargin() {
        return this.getDouble(BP_MARGIN);
    }

    public double getServicesPrice() {
        return this.getDouble(SV_PRICE);
    }

    public double getServicesPriceAfterDiscount() {
        return this.getDouble(SV_PRICE_AFTER_DISCOUNT);
    }

    public double getServicesPriceAfterTax() {
        return this.getDouble(SV_PRICE_AFTER_TAX);
    }

    public double getServicesCost() {
        return this.getDouble(SV_COST);
    }

    public double getServicesProfit() {
        return this.getDouble(SV_PROFIT);
    }

    public double getServicesMargin() {
        return this.getDouble(SV_MARGIN);
    }

    public Integer getKitchenCount() {
        return this.getInteger(KITCHEN_COUNT);
    }

    public Integer getWardrobeCount() {
        return this.getInteger(WARDROBE_COUNT);
    }

    public Integer getNSproductCount() {
        return this.getInteger(N_STD_MODULE_COUNT);
    }

    public Integer getBroughtoutProductCount() {
        return this.getInteger(BP_COUNT);
    }

    public Integer getServicesCount() {
        return this.getInteger(SV_COUNT);
    }

    public double getHwPriceAfterDiscount() {
        return this.getDouble(HW_PRICE_AFTER_DISCOUNT);
    }

    public double getAccPriceAfterDiscount() {
        return this.getDouble(ACC_PRICE_AFTER_DISCOUNT);
    }

    public double getHkPriceAfterDiscount() {
        return this.getDouble(HK_PRICE_AFTER_DISCOUNT);
    }

    public double getHingePriceAfterDiscount() {
        return this.getDouble(HINGE_PRICE_AFTER_DISCOUNT);
    }

    public double getLcPriceAfterDiscount() {
        return this.getDouble(LC_PRICE_AFTER_DISCOUNT);
    }

    public double getLaPriceAfterDiscount() {
        return this.getDouble(LA_PRICE_AFTER_DISCOUNT);
    }

    public DwProposalVersion setId(int id) {
        this.put(ID, id);
        return this;
    }

    public DwProposalVersion setProposalId(int id) {
        this.put(PROPOSAL_ID, id);
        return this;
    }

    public DwProposalVersion setVersion(double version) {
        this.put(VERSION, version);
        return this;
    }

    public DwProposalVersion setProposalTitle(String title) {
        this.put(PROPOSAL_TITLE, title);
        return this;
    }

    public DwProposalVersion setRegion(String region) {
        this.put(REGION, region);
        return this;
    }

    public DwProposalVersion setProjectName(String pname) {
        this.put(PROJECT_NAME, pname);
        return this;
    }

    public DwProposalVersion setDesignerName(String dname) {
        this.put(DESIGNER_NAME, dname);
        return this;
    }

    public DwProposalVersion setSalesName(String sname) {
        this.put(SALES_NAME, sname);
        return this;
    }

    public DwProposalVersion setDesignPartnerName(String dname) {
        this.put(DESIGNER_NAME, dname);
        return this;
    }

    public DwProposalVersion setProposalUpdatedBy(String updatedBy) {
        this.put(PROPOSAL_UPDATED_BY, updatedBy);
        return this;
    }

    public DwProposalVersion setProposalCreateDate(Date dt) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String format = formatter.format(dt);
        this.put(PROPOSAL_CREATE_DATE, format);
        return  this;
    }

    public DwProposalVersion setProposalPriceDate(Date dt) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String format = formatter.format(dt);
        this.put(PROPOSAL_PRICE_DATE, format);
        return  this;
    }

    public DwProposalVersion setBusinessDate(Date dt) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String format = formatter.format(dt);
        this.put(BUSINESS_DATE, format);
        return  this;
    }

    public DwProposalVersion setVersionCreatedBy(String createdBy) {
        this.put(VERSION_CREATED_BY, createdBy);
        return this;
    }

    public DwProposalVersion setVersionCreatedOn(Date dt) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String format = formatter.format(dt);
        this.put(VERSION_CREATED_ON, format);
        return  this;
    }

    public DwProposalVersion setVersionUpdatedBy(String updatedBy) {
        this.put(VERSION_UPDATED_BY, updatedBy);
        return this;
    }

    public DwProposalVersion setVersionUpdatedOn(Date dt) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String format = formatter.format(dt);
        this.put(VERSION_UPDATED_ON, format);
        return  this;
    }

    public DwProposalVersion setStdModuleCount(int nCount) {
        this.put(STD_MODULE_COUNT, nCount);
        return this;
    }

    public DwProposalVersion setnStdModuleCount(int nCount) {
        this.put(N_STD_MODULE_COUNT, nCount);
        return this;
    }

    public DwProposalVersion setHikeModuleCount(int nCount) {
        this.put(HIKE_MODULE_COUNT, nCount);
        return this;
    }

    public DwProposalVersion setStdModulePrice(double price) {
        this.put(STD_MODULE_PRICE, price);
        return this;
    }

    public DwProposalVersion setnStdModulePrice(double price) {
        this.put(N_STD_MODULE_PRICE, price);
        return this;
    }

    public DwProposalVersion setHikeModulePrice(double price) {
        this.put(HIKE_MODULE_PRICE, price);
        return this;
    }

    public DwProposalVersion setVersionPrice(double price) {
        this.put(VR_PRICE, price);
        return this;
    }

    public DwProposalVersion setVersionPriceAfterDiscount(double price) {
        this.put(VR_PRICE_AFTER_DISCOUNT, price);
        return this;
    }

    public DwProposalVersion setVersionPriceAfterTax(double price) {
        this.put(VR_PRICE_AFTER_TAX, price);
        return this;
    }

    public DwProposalVersion setVersionCost(double price) {
        this.put(VR_COST, price);
        return this;
    }

    public DwProposalVersion setVersionProfit(double price) {
        this.put(VR_PROFIT, price);
        return this;
    }

    public DwProposalVersion setVersionMargin(double price) {
        this.put(VR_MARGIN, price);
        return this;
    }

    public DwProposalVersion setProductPrice(double price) {
        this.put(PR_PRICE, price);
        return this;
    }

    public DwProposalVersion setProductPriceAfterDiscount(double price) {
        this.put(PR_PRICE_AFTER_DISCOUNT, price);
        return this;
    }

    public DwProposalVersion setProductPriceAfterTax(double price) {
        this.put(PR_PRICE_AFTER_TAX, price);
        return this;
    }

    public DwProposalVersion setProductCost(double price) {
        this.put(PR_COST, price);
        return this;
    }

    public DwProposalVersion setProductProfit(double price) {
        this.put(PR_PROFIT, price);
        return this;
    }

    public DwProposalVersion setProductMargin(double price) {
        this.put(PR_MARGIN, price);
        return this;
    }

    public DwProposalVersion setWoodWorkPrice(double price) {
        this.put(WW_PRICE, price);
        return this;
    }

    public DwProposalVersion setWoodWorkPriceAfterDiscount(double price) {
        this.put(WW_PRICE_AFTER_DISCOUNT, price);
        return this;
    }

    public DwProposalVersion setWoodWorkPriceAfterTax(double price) {
        this.put(WW_PRICE_AFTER_TAX, price);
        return this;
    }

    public DwProposalVersion setWoodWorkCost(double price) {
        this.put(WW_COST, price);
        return this;
    }

    public DwProposalVersion setWoodWorkProfit(double price) {
        this.put(WW_PROFIT, price);
        return this;
    }

    public DwProposalVersion setWoodWorkMargin(double price) {
        this.put(WW_MARGIN, price);
        return this;
    }

    public DwProposalVersion setHardwarePrice(double price) {
        this.put(HW_PRICE, price);
        return this;
    }

    public DwProposalVersion setHardwarePriceAfterTax(double price) {
        this.put(HW_PRICEAFTERTAX, price);
        return this;
    }

    public DwProposalVersion setHardwareCost(double price) {
        this.put(HW_COST, price);
        return this;
    }

    public DwProposalVersion setHardwareProfit(double price) {
        this.put(HW_PROFIT, price);
        return this;
    }

    public DwProposalVersion setHardwareMargin(double price) {
        this.put(HW_MARGIN, price);
        return this;
    }

    public DwProposalVersion setAccessoryPrice(double price) {
        this.put(ACC_PRICE, price);
        return this;
    }

    public DwProposalVersion setAccessoryPriceAfterTax(double price) {
        this.put(ACC_PRICEAFTERTAX, price);
        return this;
    }

    public DwProposalVersion setAccessoryCost(double price) {
        this.put(ACC_COST, price);
        return this;
    }

    public DwProposalVersion setAccessoryProfit(double price) {
        this.put(ACC_PROFIT, price);
        return this;
    }

    public DwProposalVersion setAccessoryMargin(double price) {
        this.put(ACC_MARGIN, price);
        return this;
    }

    public DwProposalVersion setHandleKnobPrice(double price) {
        this.put(HK_PRICE, price);
        return this;
    }

    public DwProposalVersion setHandleKnobPriceAfterTax(double price) {
        this.put(HK_PRICEAFTERTAX, price);
        return this;
    }

    public DwProposalVersion setHandleKnobCost(double price) {
        this.put(HK_COST, price);
        return this;
    }

    public DwProposalVersion setHandleKnobProfit(double price) {
        this.put(HK_PROFIT, price);
        return this;
    }

    public DwProposalVersion setHandleKnobMargin(double price) {
        this.put(HK_MARGIN, price);
        return this;
    }

    public DwProposalVersion setHingePrice(double price) {
        this.put(HINGE_PRICE, price);
        return this;
    }

    public DwProposalVersion setHingePriceAfterTax(double price) {
        this.put(HINGE_PRICEAFTERTAX, price);
        return this;
    }

    public DwProposalVersion setHingeCost(double price) {
        this.put(HINGE_COST, price);
        return this;
    }

    public DwProposalVersion setHingeProfit(double price) {
        this.put(HINGE_PROFIT, price);
        return this;
    }

    public DwProposalVersion setHingeMargin(double price) {
        this.put(HINGE_MARGIN, price);
        return this;
    }

    public DwProposalVersion setLConnectorPrice(double price) {
        this.put(LC_PRICE, price);
        return this;
    }

    public DwProposalVersion setLConnectorPriceAfterTax(double price) {
        this.put(LC_PRICEAFTERTAX, price);
        return this;
    }

    public DwProposalVersion setLConnectorCost(double price) {
        this.put(LC_COST, price);
        return this;
    }

    public DwProposalVersion setLConnectorProfit(double price) {
        this.put(LC_PROFIT, price);
        return this;
    }

    public DwProposalVersion setLConnectorMargin(double price) {
        this.put(LC_MARGIN, price);
        return this;
    }

    public DwProposalVersion setLabourPrice(double price) {
        this.put(LA_PRICE, price);
        return this;
    }

    public DwProposalVersion setLabourPriceAfterTax(double price) {
        this.put(LA_PRICEAFTERTAX, price);
        return this;
    }

    public DwProposalVersion setLabourCost(double price) {
        this.put(LA_COST, price);
        return this;
    }

    public DwProposalVersion setLabourProfit(double price) {
        this.put(LA_PROFIT, price);
        return this;
    }

    public DwProposalVersion setLabourMargin(double price) {
        this.put(LA_MARGIN, price);
        return this;
    }

    public DwProposalVersion setBroughtoutProductPrice(double price) {
        this.put(BP_PRICE, price);
        return this;
    }

    public DwProposalVersion setBroughtoutProductPriceAfterDiscount(double price) {
        this.put(BP_PRICE_AFTER_DISCOUNT, price);
        return this;
    }

    public DwProposalVersion setBroughtoutProductPriceAfterTax(double price) {
        this.put(BP_PRICE_AFTER_TAX, price);
        return this;
    }

    public DwProposalVersion setBroughtoutProductCost(double price) {
        this.put(BP_COST, price);
        return this;
    }

    public DwProposalVersion setBroughtoutProductProfit(double price) {
        this.put(BP_PROFIT, price);
        return this;
    }

    public DwProposalVersion setBroughtoutProductMargin(double price) {
        this.put(BP_MARGIN, price);
        return this;
    }

    public DwProposalVersion setServicesPrice(double price) {
        this.put(SV_PRICE, price);
        return this;
    }

    public DwProposalVersion setServicesPriceAfterDiscount(double price) {
        this.put(SV_PRICE_AFTER_DISCOUNT, price);
        return this;
    }

    public DwProposalVersion setServicesPriceAfterTax(double price) {
        this.put(SV_PRICE_AFTER_TAX, price);
        return this;
    }

    public DwProposalVersion setServicesCost(double price) {
        this.put(SV_COST, price);
        return this;
    }

    public DwProposalVersion setServicesProfit(double price) {
        this.put(SV_PROFIT, price);
        return this;
    }

    public DwProposalVersion setServicesMargin(double price) {
        this.put(SV_MARGIN, price);
        return this;
    }

    public DwProposalVersion setKitchenCount(int nCount) {
        this.put(KITCHEN_COUNT, nCount);
        return this;
    }

    public DwProposalVersion setWardrobeCount(int nCount) {
        this.put(WARDROBE_COUNT, nCount);
        return this;
    }

    public DwProposalVersion setNSproductCount(int nCount) {
        this.put(NS_PRODUCT_COUNT, nCount);
        return this;
    }

    public DwProposalVersion setBroughtoutProductCount(int nCount) {
        this.put(BP_COUNT, nCount);
        return this;
    }

    public DwProposalVersion setServicesCount(int nCount) {
        this.put(SV_COUNT, nCount);
        return this;
    }

    public DwProposalVersion setHardwarePriceAfterDiscount(double price) {
        this.put(HW_PRICE_AFTER_DISCOUNT, price);
        return this;
    }
    public DwProposalVersion setAccessoryPriceAfterDiscount(double price) {
        this.put(ACC_PRICE_AFTER_DISCOUNT, price);
        return this;
    }
    public DwProposalVersion setHandleKnobPriceAfterDiscount(double price) {
        this.put(HK_PRICE_AFTER_DISCOUNT, price);
        return this;
    }
    public DwProposalVersion setHingePriceAfterDiscount(double price) {
        this.put(HINGE_PRICE_AFTER_DISCOUNT, price);
        return this;
    }
    public DwProposalVersion setLConnectorPriceAfterDiscount(double price) {
        this.put(LC_PRICE_AFTER_DISCOUNT, price);
        return this;
    }
    public DwProposalVersion setLabourPriceAfterDiscount(double price) {
        this.put(LA_PRICE_AFTER_DISCOUNT, price);
        return this;
    }

    public DwProposalVersion setHikeModuleCost(double cost){
        this.put(HIKE_MODULE_COST,cost);
        return this;
    }
    public DwProposalVersion setDwVersionObjects(ProposalHeader proposalHeader, ProposalVersion proposalVersion, VersionPriceHolder versionPriceHolder) {
        DwProposalVersion dwProposalVersion = new DwProposalVersion();

        dwProposalVersion.setProposalId(proposalHeader.getId());
        dwProposalVersion.setVersion(Double.parseDouble(proposalVersion.getVersion()));
        dwProposalVersion.setProposalTitle(proposalHeader.getQuotationFor());
        dwProposalVersion.setRegion(proposalHeader.getProjectCity());
        dwProposalVersion.setCrmId(proposalHeader.getCrmId());
        dwProposalVersion.setQuoteNo(proposalHeader.getQuoteNumNew());
        dwProposalVersion.setProjectName(proposalHeader.getProjectName());
        dwProposalVersion.setSalesName(proposalHeader.getSalespersonName());
        dwProposalVersion.setBefProdSpec(proposalHeader.getBefProdSpec());
        dwProposalVersion.setFromProposal(proposalHeader.getFromProposal());
        dwProposalVersion.setOfferType(proposalHeader.getOfferType());
        dwProposalVersion.setPackageFlag(proposalHeader.getPackageFlag());
        dwProposalVersion.setDesignPartnerName(proposalHeader.getDesignPartnerName());
        dwProposalVersion.setProposalCreateDate(proposalHeader.getCreatedOn());
        dwProposalVersion.setProposalUpdatedBy(proposalHeader.getUpdatedBy());
        dwProposalVersion.setProposalPriceDate(proposalHeader.getPriceDate());
        dwProposalVersion.setBusinessDate(proposalVersion.getBusinessDate());
        dwProposalVersion.setVersionCreatedBy(proposalVersion.getCreatedBy());
        dwProposalVersion.setVersionCreatedOn(proposalVersion.getDate());
        dwProposalVersion.setVersionUpdatedBy(proposalVersion.getUpdatedBy());
        dwProposalVersion.setVersionUpdatedOn(proposalVersion.getUpdatedOn());

        dwProposalVersion.setDiscountAmount(proposalVersion.getDiscountAmount());
        dwProposalVersion.setDiscountAmountPerc(proposalVersion.getDiscountPercentage());
        dwProposalVersion.setStatus(proposalVersion.getProposalStatus());

        dwProposalVersion.setStdModuleCount(versionPriceHolder.getStdModuleCount());
        dwProposalVersion.setStdModulePrice(versionPriceHolder.getStdModulePrice());
        dwProposalVersion.setnStdModuleCount(versionPriceHolder.getNStdModuleCount());
        dwProposalVersion.setnStdModulePrice(versionPriceHolder.getNStdModulePrice());
        dwProposalVersion.setHikeModuleCount(versionPriceHolder.getHikeModuleCount());
        dwProposalVersion.setHikeModulePrice(versionPriceHolder.getHikeModulePrice());
        dwProposalVersion.setHikeModuleCost(versionPriceHolder.getHikeModuleCost());
        dwProposalVersion.setVersionPrice(versionPriceHolder.getPrice());
        dwProposalVersion.setVersionPriceAfterDiscount(versionPriceHolder.getPriceAfterDiscount());
        dwProposalVersion.setVersionPriceAfterTax(versionPriceHolder.getPriceWotax());
        dwProposalVersion.setVersionCost(versionPriceHolder.getSourceCost());
        dwProposalVersion.setVersionProfit(versionPriceHolder.getProfit());
        dwProposalVersion.setVersionMargin(versionPriceHolder.getMargin());
        dwProposalVersion.setProductPrice(versionPriceHolder.getProductPrice());
        dwProposalVersion.setProductPriceAfterDiscount(versionPriceHolder.getProductPriceAfterDiscount());
        dwProposalVersion.setProductPriceAfterTax(versionPriceHolder.getProductPriceWotax());
        dwProposalVersion.setProductCost(versionPriceHolder.getProductSourceCost());
        dwProposalVersion.setProductProfit(versionPriceHolder.getProductProfit());
        dwProposalVersion.setProductMargin(versionPriceHolder.getProductMargin());
        dwProposalVersion.setWoodWorkPrice(versionPriceHolder.getWoodworkPrice());
        dwProposalVersion.setWoodWorkPriceAfterDiscount(versionPriceHolder.getWoodworkPriceAfterDiscount());
        dwProposalVersion.setWoodWorkPriceAfterTax(versionPriceHolder.getWoodworkPriceWotax());
        dwProposalVersion.setWoodWorkCost(versionPriceHolder.getWoodworkSourceCost());
        dwProposalVersion.setWoodWorkProfit(versionPriceHolder.getWoodworkProfit());
        dwProposalVersion.setWoodWorkMargin(versionPriceHolder.getWoodworkMargin());
        dwProposalVersion.setHardwarePrice(versionPriceHolder.getHardwarePrice());
        dwProposalVersion.setHardwarePriceAfterDiscount(versionPriceHolder.getHardwarePriceAfterDiscount());
        dwProposalVersion.setHardwarePriceAfterTax(versionPriceHolder.getHardwarePriceWotax());
        dwProposalVersion.setHardwareCost(versionPriceHolder.getHardwareSourceCost());
        dwProposalVersion.setHardwareProfit(versionPriceHolder.getHardwareProfit());
        dwProposalVersion.setHardwareMargin(versionPriceHolder.getHardwareMargin());
        dwProposalVersion.setAccessoryPrice(versionPriceHolder.getAccessoryPrice());
        dwProposalVersion.setAccessoryPriceAfterDiscount(versionPriceHolder.getAccessoryPriceAfterDiscount());
        dwProposalVersion.setAccessoryPriceAfterTax(versionPriceHolder.getAccessoryPriceWotax());
        dwProposalVersion.setAccessoryCost(versionPriceHolder.getAccessorySourceCost());
        dwProposalVersion.setAccessoryProfit(versionPriceHolder.getAccessoryProfit());
        dwProposalVersion.setAccessoryMargin(versionPriceHolder.getAccessoryMargin());
        dwProposalVersion.setHandleKnobPrice(versionPriceHolder.getHKPrice());
        dwProposalVersion.setHandleKnobPriceAfterDiscount(versionPriceHolder.getHKPriceAfterDiscount());
        dwProposalVersion.setHandleKnobPriceAfterTax(versionPriceHolder.getHKPriceWotax());
        dwProposalVersion.setHandleKnobCost(versionPriceHolder.getHKSourceCost());
        dwProposalVersion.setHandleKnobProfit(versionPriceHolder.getHKProfit());
        dwProposalVersion.setHandleKnobMargin(versionPriceHolder.getHKMargin());
        dwProposalVersion.setHingePrice(versionPriceHolder.getHingePrice());
        dwProposalVersion.setHingePriceAfterDiscount(versionPriceHolder.getHingePriceAfterDiscount());
        dwProposalVersion.setHingePriceAfterTax(versionPriceHolder.getHingePriceWotax());
        dwProposalVersion.setHingeCost(versionPriceHolder.getHingeSourceCost());
        dwProposalVersion.setHingeProfit(versionPriceHolder.getHingeProfit());
        dwProposalVersion.setHingeMargin(versionPriceHolder.getHingeMargin());
        dwProposalVersion.setLabourPrice(versionPriceHolder.getLabourPrice());
        dwProposalVersion.setLabourPriceAfterDiscount(versionPriceHolder.getLabourPriceAfterDiscount());
        dwProposalVersion.setLabourPriceAfterTax(versionPriceHolder.getLabourPriceWotax());
        dwProposalVersion.setLabourCost(versionPriceHolder.getLabourSourceCost());
        dwProposalVersion.setLabourProfit(versionPriceHolder.getLabourProfit());
        dwProposalVersion.setLabourMargin(versionPriceHolder.getLabourMargin());
        dwProposalVersion.setLConnectorPrice(versionPriceHolder.getLCPrice());
        dwProposalVersion.setLConnectorPriceAfterDiscount(versionPriceHolder.getLCPriceAfterDiscount());
        dwProposalVersion.setLConnectorPriceAfterTax(versionPriceHolder.getLCPriceWotax());
        dwProposalVersion.setLConnectorCost(versionPriceHolder.getLCSourceCost());
        dwProposalVersion.setLConnectorProfit(versionPriceHolder.getLCProfit());
        dwProposalVersion.setLConnectorMargin(versionPriceHolder.getLCMargin());
        dwProposalVersion.setBroughtoutProductPrice(versionPriceHolder.getBPPrice());
        dwProposalVersion.setBroughtoutProductPriceAfterDiscount(versionPriceHolder.getBPPriceAfterDiscount());
        dwProposalVersion.setBroughtoutProductPriceAfterTax(versionPriceHolder.getBPPriceWotax());
        dwProposalVersion.setBroughtoutProductCost(versionPriceHolder.getBPSourceCost());
        dwProposalVersion.setBroughtoutProductProfit(versionPriceHolder.getBPProfit());
        dwProposalVersion.setBroughtoutProductMargin(versionPriceHolder.getBPMargin());
        dwProposalVersion.setServicesPrice(versionPriceHolder.getSVPrice());
        dwProposalVersion.setServicesPriceAfterDiscount(versionPriceHolder.getSVPriceAfterDiscount());
        dwProposalVersion.setServicesPriceAfterTax(versionPriceHolder.getSVPriceWotax());
        dwProposalVersion.setServicesCost(versionPriceHolder.getSVSourceCost());
        dwProposalVersion.setServicesProfit(versionPriceHolder.getSVProfit());
        dwProposalVersion.setServicesMargin(versionPriceHolder.getSVMargin());
        dwProposalVersion.setKitchenCount(versionPriceHolder.getKitchenCount());
        dwProposalVersion.setWardrobeCount(versionPriceHolder.getWardrobeCount());
        dwProposalVersion.setNSproductCount(versionPriceHolder.getNSProductCount());
        dwProposalVersion.setBroughtoutProductCount(versionPriceHolder.getBPCount());
        dwProposalVersion.setServicesCount(versionPriceHolder.getServicesCount());
        dwProposalVersion.setDesignerName(proposalHeader.getDesignerName());


        return dwProposalVersion;
    }
}