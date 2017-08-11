package com.mygubbi.game.proposal.model.dw;

import io.vertx.core.json.JsonObject;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Created by shilpa on 9/8/17.
 */
public class DwProposal extends JsonObject {
    private static final String ID = "id";
    private static String PROPOSAL_ID = "proposalId";
    private static String VERSION = "version";
    private static String PROPOSAL_TITLE = "proposalTitle";
    private static String REGION = "region";
    private static String PROJECT_NAME = "projectName";
    private static String DESIGNER_NAME = "designerName";
    private static String SALES_NAME = "salesName";
    private static String DESIGN_PARTNER_NAME = "designPartnerName";
    private static String PROPOSAL_UPDATED_BY = "proposalUpdatedBy";
    private static String PROPOSAL_CREATE_DATE = "proposalCreateDate";
    private static String PROPOSAL_PRICE_DATE = "proposalPriceDate";
    private static String BUSINESS_DATE = "businessDate";
    private static String VERSION_CREATED_BY = "versionCreatedBy";
    private static String VERSION_CREATED_ON = "versionCreatedOn";
    private static String VERSION_UPDATED_BY = "versionUpdatedBy";
    private static String VERSION_UPDATED_ON = "versionUpdatedOn";
    private static String STD_MODULE_COUNT = "stdModuleCount";
    private static String N_STD_MODULE_COUNT = "nStdModuleCount";
    private static String HIKE_MODULE_COUNT = "hikeModuleCount";
    private static String STD_MODULE_PRICE = "stdModulePrice";
    private static String N_STD_MODULE_PRICE = "nStdModulePrice";
    private static String HIKE_MODULE_PRICE = "hikeModulePrice";
    private static String VR_PRICE = "vrPrice";
    private static String VR_PRICE_AFTER_DISCOUNT = "vrPriceAfterDiscount";
    private static String VR_PRICE_AFTER_TAX = "vrPriceAfterTax";
    private static String VR_COST = "vrCost";
    private static String VR_PROFIT = "vrProfit";
    private static String VR_MARGIN = "vrMargin";
    private static String PR_PRICE = "prPrice";
    private static String PR_PRICE_AFTER_DISCOUNT = "prPriceAfterDiscount";
    private static String PR_PRICE_AFTER_TAX = "prPriceAfterTax";
    private static String PR_COST = "prCost";
    private static String PR_PROFIT = "prProfit";
    private static String PR_MARGIN = "prMargin";
    private static String WW_PRICE = "wwPrice";
    private static String WW_PRICE_AFTER_DISCOUNT = "wwPriceAfterDiscount";
    private static String WW_PRICE_AFTER_TAX = "wwPriceAfterTax";
    private static String WW_COST = "wwCost";
    private static String WW_PROFIT = "wwProfit";
    private static String WW_MARGIN = "wwMargin";
    public static final String HW_PRICE = "hwPrice";
    public static final String HW_PRICEAFTERTAX = "hwPriceAfterTax";
    private static String HW_PRICE_AFTER_DISCOUNT = "hwPriceAfterDiscount";
    public static final String HW_COST = "hwCost";
    public static final String HW_PROFIT = "hwProfit";
    public static final String HW_MARGIN = "hwMargin";
    public static final String ACC_PRICE = "accPrice";
    public static final String ACC_PRICEAFTERTAX = "accPriceAfterTax";
    private static String ACC_PRICE_AFTER_DISCOUNT = "accPriceAfterDiscount";
    public static final String ACC_COST = "accCost";
    public static final String ACC_PROFIT = "accProfit";
    public static final String ACC_MARGIN = "accMargin";
    public static final String HK_PRICE = "hkPrice";
    public static final String HK_PRICEAFTERTAX = "hkPriceAfterTax";
    private static String HK_PRICE_AFTER_DISCOUNT = "hkPriceAfterDiscount";
    public static final String HK_COST = "hkCost";
    public static final String HK_PROFIT = "hkProfit";
    public static final String HK_MARGIN = "hkMargin";
    public static final String HINGE_PRICE = "hingePrice";
    public static final String HINGE_PRICEAFTERTAX = "hingePriceAfterTax";
    private static String HINGE_PRICE_AFTER_DISCOUNT = "hingePriceAfterDiscount";
    public static final String HINGE_COST = "hingeCost";
    public static final String HINGE_PROFIT = "hingeProfit";
    public static final String HINGE_MARGIN = "hingeMargin";
    public static final String LC_PRICE = "lcPrice";
    public static final String LC_PRICEAFTERTAX = "lcPriceAfterTax";
    private static String LC_PRICE_AFTER_DISCOUNT = "lcPriceAfterDiscount";
    public static final String LC_COST = "lcCost";
    public static final String LC_PROFIT = "lcProfit";
    public static final String LC_MARGIN = "lcMargin";
    public static final String LA_PRICE = "laPrice";
    public static final String LA_PRICEAFTERTAX = "laPriceAfterTax";
    private static String LA_PRICE_AFTER_DISCOUNT = "laPriceAfterDiscount";
    public static final String LA_COST = "laCost";
    public static final String LA_PROFIT = "laProfit";
    public static final String LA_MARGIN = "laMargin";
    private static String BP_PRICE = "bpPrice";
    private static String BP_PRICE_AFTER_DISCOUNT = "bpPriceAfterDiscount";
    private static String BP_PRICE_AFTER_TAX = "bpPriceAfterTax";
    private static String BP_COST = "bpCost";
    private static String BP_PROFIT = "bpProfit";
    private static String BP_MARGIN = "bpMargin";
    private static String SV_PRICE = "svPrice";
    private static String SV_PRICE_AFTER_DISCOUNT = "svPriceAfterDiscount";
    private static String SV_PRICE_AFTER_TAX = "svPriceAfterTax";
    private static String SV_COST = "svCost";
    private static String SV_PROFIT = "svProfit";
    private static String SV_MARGIN = "svMargin";
    private static String KITCHEN_COUNT = "kitchenCount";
    private static String WARDROBE_COUNT = "wardrobeCount";
    private static String NS_PRODUCT_COUNT = "NSproductCount";
    private static String BP_COUNT = "bpCount";
    private static String SV_COUNT = "svCount ";


    public DwProposal() {
    }

    public DwProposal(JsonObject data) {
        super(data.getMap());
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

    public DwProposal setId(int id) {
        this.put(ID, id);
        return this;
    }

    public DwProposal setProposalId(int id) {
        this.put(ID, id);
        return this;
    }

    public DwProposal setVersion(double version) {
        this.put(VERSION, version);
        return this;
    }

    public DwProposal setProposalTitle(String title) {
        this.put(PROPOSAL_TITLE, title);
        return this;
    }

    public DwProposal setRegion(String region) {
        this.put(REGION, region);
        return this;
    }

    public DwProposal setProjectName(String pname) {
        this.put(PROJECT_NAME, pname);
        return this;
    }

    public DwProposal setDesignerName(String dname) {
        this.put(DESIGNER_NAME, dname);
        return this;
    }

    public DwProposal setSalesName(String sname) {
        this.put(SALES_NAME, sname);
        return this;
    }

    public DwProposal setDesignPartnerName(String dname) {
        this.put(DESIGNER_NAME, dname);
        return this;
    }

    public DwProposal setProposalUpdatedBy(String updatedBy) {
        this.put(PROPOSAL_UPDATED_BY, updatedBy);
        return this;
    }

    public DwProposal setProposalCreateDate(Date dt) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String format = formatter.format(dt);
        this.put(PROPOSAL_CREATE_DATE, format);
        return  this;
    }

    public DwProposal setProposalPriceDate(Date dt) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String format = formatter.format(dt);
        this.put(PROPOSAL_PRICE_DATE, format);
        return  this;
    }

    public DwProposal setBusinessDate(Date dt) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String format = formatter.format(dt);
        this.put(BUSINESS_DATE, format);
        return  this;
    }

    public DwProposal setVersionCreatedBy(String createdBy) {
        this.put(VERSION_CREATED_BY, createdBy);
        return this;
    }

    public DwProposal setVersionCreatedOn(Date dt) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String format = formatter.format(dt);
        this.put(VERSION_CREATED_ON, format);
        return  this;
    }

    public DwProposal setVersionUpdatedBy(String updatedBy) {
        this.put(VERSION_UPDATED_BY, updatedBy);
        return this;
    }

    public DwProposal setVersionUpdatedOn(Date dt) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String format = formatter.format(dt);
        this.put(VERSION_UPDATED_ON, format);
        return  this;
    }

    public DwProposal setStdModuleCount(int nCount) {
        this.put(STD_MODULE_COUNT, nCount);
        return this;
    }

    public DwProposal setnStdModuleCount(int nCount) {
        this.put(N_STD_MODULE_COUNT, nCount);
        return this;
    }

    public DwProposal setHikeModuleCount(int nCount) {
        this.put(HIKE_MODULE_COUNT, nCount);
        return this;
    }

    public DwProposal setStdModulePrice(double price) {
        this.put(STD_MODULE_PRICE, price);
        return this;
    }

    public DwProposal setnStdModulePrice(double price) {
        this.put(N_STD_MODULE_PRICE, price);
        return this;
    }

    public DwProposal setHikeModulePrice(double price) {
        this.put(HIKE_MODULE_PRICE, price);
        return this;
    }

    public DwProposal setVersionPrice(double price) {
        this.put(VR_PRICE, price);
        return this;
    }

    public DwProposal setVersionPriceAfterDiscount(double price) {
        this.put(VR_PRICE_AFTER_DISCOUNT, price);
        return this;
    }

    public DwProposal setVersionPriceAfterTax(double price) {
        this.put(VR_PRICE_AFTER_TAX, price);
        return this;
    }

    public DwProposal setVersionCost(double price) {
        this.put(VR_COST, price);
        return this;
    }

    public DwProposal setVersionProfit(double price) {
        this.put(VR_PROFIT, price);
        return this;
    }

    public DwProposal setVersionMargin(double price) {
        this.put(VR_MARGIN, price);
        return this;
    }

    public DwProposal setProductPrice(double price) {
        this.put(PR_PRICE, price);
        return this;
    }

    public DwProposal setProductPriceAfterDiscount(double price) {
        this.put(PR_PRICE_AFTER_DISCOUNT, price);
        return this;
    }

    public DwProposal setProductPriceAfterTax(double price) {
        this.put(PR_PRICE_AFTER_TAX, price);
        return this;
    }

    public DwProposal setProductCost(double price) {
        this.put(PR_COST, price);
        return this;
    }

    public DwProposal setProductProfit(double price) {
        this.put(PR_PROFIT, price);
        return this;
    }

    public DwProposal setProductMargin(double price) {
        this.put(PR_MARGIN, price);
        return this;
    }

    public DwProposal setWoodWorkPrice(double price) {
        this.put(WW_PRICE, price);
        return this;
    }

    public DwProposal setWoodWorkPriceAfterDiscount(double price) {
        this.put(WW_PRICE_AFTER_DISCOUNT, price);
        return this;
    }

    public DwProposal setWoodWorkPriceAfterTax(double price) {
        this.put(WW_PRICE_AFTER_TAX, price);
        return this;
    }

    public DwProposal setWoodWorkCost(double price) {
        this.put(WW_COST, price);
        return this;
    }

    public DwProposal setWoodWorkProfit(double price) {
        this.put(WW_PROFIT, price);
        return this;
    }

    public DwProposal setWoodWorkMargin(double price) {
        this.put(WW_MARGIN, price);
        return this;
    }

    public DwProposal setHardwarePrice(double price) {
        this.put(WW_PRICE, price);
        return this;
    }

    public DwProposal setHardwarePriceAfterTax(double price) {
        this.put(HW_PRICEAFTERTAX, price);
        return this;
    }

    public DwProposal setHardwareCost(double price) {
        this.put(HW_COST, price);
        return this;
    }

    public DwProposal setHardwareProfit(double price) {
        this.put(HW_PROFIT, price);
        return this;
    }

    public DwProposal setHardwareMargin(double price) {
        this.put(HW_MARGIN, price);
        return this;
    }

    public DwProposal setAccessoryPrice(double price) {
        this.put(ACC_PRICE, price);
        return this;
    }

    public DwProposal setAccessoryPriceAfterTax(double price) {
        this.put(ACC_PRICEAFTERTAX, price);
        return this;
    }

    public DwProposal setAccessoryCost(double price) {
        this.put(ACC_COST, price);
        return this;
    }

    public DwProposal setAccessoryProfit(double price) {
        this.put(ACC_PROFIT, price);
        return this;
    }

    public DwProposal setAccessoryMargin(double price) {
        this.put(ACC_MARGIN, price);
        return this;
    }

    public DwProposal setHandleKnobPrice(double price) {
        this.put(HK_PRICE, price);
        return this;
    }

    public DwProposal setHandleKnobPriceAfterTax(double price) {
        this.put(HK_PRICEAFTERTAX, price);
        return this;
    }

    public DwProposal setHandleKnobCost(double price) {
        this.put(HK_COST, price);
        return this;
    }

    public DwProposal setHandleKnobProfit(double price) {
        this.put(HK_PROFIT, price);
        return this;
    }

    public DwProposal setHandleKnobMargin(double price) {
        this.put(HK_MARGIN, price);
        return this;
    }

    public DwProposal setHingePrice(double price) {
        this.put(HINGE_PRICE, price);
        return this;
    }

    public DwProposal setHingePriceAfterTax(double price) {
        this.put(HINGE_PRICEAFTERTAX, price);
        return this;
    }

    public DwProposal setHingeCost(double price) {
        this.put(HINGE_COST, price);
        return this;
    }

    public DwProposal setHingeProfit(double price) {
        this.put(HINGE_COST, price);
        return this;
    }

    public DwProposal setHingeMargin(double price) {
        this.put(HINGE_MARGIN, price);
        return this;
    }

    public DwProposal setLConnectorPrice(double price) {
        this.put(LC_PRICE, price);
        return this;
    }

    public DwProposal setLConnectorPriceAfterTax(double price) {
        this.put(LC_PRICEAFTERTAX, price);
        return this;
    }

    public DwProposal setLConnectorCost(double price) {
        this.put(LC_COST, price);
        return this;
    }

    public DwProposal setLConnectorProfit(double price) {
        this.put(LC_PROFIT, price);
        return this;
    }

    public DwProposal setLConnectorMargin(double price) {
        this.put(LC_MARGIN, price);
        return this;
    }

    public DwProposal setLabourPrice(double price) {
        this.put(LA_PRICE, price);
        return this;
    }

    public DwProposal setLabourPriceAfterTax(double price) {
        this.put(LA_PRICEAFTERTAX, price);
        return this;
    }

    public DwProposal setLabourCost(double price) {
        this.put(LA_COST, price);
        return this;
    }

    public DwProposal setLabourProfit(double price) {
        this.put(LA_PROFIT, price);
        return this;
    }

    public DwProposal setLabourMargin(double price) {
        this.put(LA_MARGIN, price);
        return this;
    }

    public DwProposal setBroughtoutProductPrice(double price) {
        this.put(BP_PRICE, price);
        return this;
    }

    public DwProposal setBroughtoutProductPriceAfterDiscount(double price) {
        this.put(BP_PRICE_AFTER_DISCOUNT, price);
        return this;
    }

    public DwProposal setBroughtoutProductPriceAfterTax(double price) {
        this.put(BP_PRICE_AFTER_TAX, price);
        return this;
    }

    public DwProposal setBroughtoutProductCost(double price) {
        this.put(BP_COST, price);
        return this;
    }

    public DwProposal setBroughtoutProductProfit(double price) {
        this.put(BP_PROFIT, price);
        return this;
    }

    public DwProposal setBroughtoutProductMargin(double price) {
        this.put(BP_MARGIN, price);
        return this;
    }

    public DwProposal setServicesPrice(double price) {
        this.put(SV_PRICE, price);
        return this;
    }

    public DwProposal setServicesPriceAfterDiscount(double price) {
        this.put(SV_PRICE_AFTER_DISCOUNT, price);
        return this;
    }

    public DwProposal setServicesPriceAfterTax(double price) {
        this.put(SV_PRICE_AFTER_TAX, price);
        return this;
    }

    public DwProposal setServicesCost(double price) {
        this.put(SV_COST, price);
        return this;
    }

    public DwProposal setServicesProfit(double price) {
        this.put(SV_PROFIT, price);
        return this;
    }

    public DwProposal setServicesMargin(double price) {
        this.put(SV_MARGIN, price);
        return this;
    }

    public DwProposal setKitchenCount(int nCount) {
        this.put(KITCHEN_COUNT, nCount);
        return this;
    }

    public DwProposal setWardrobeCount(int nCount) {
        this.put(WARDROBE_COUNT, nCount);
        return this;
    }

    public DwProposal setNSproductCount(int nCount) {
        this.put(NS_PRODUCT_COUNT, nCount);
        return this;
    }

    public DwProposal setBroughtoutProductCount(int nCount) {
        this.put(BP_COUNT, nCount);
        return this;
    }

    public DwProposal setServicesCount(int nCount) {
        this.put(SV_COUNT, nCount);
        return this;
    }

    public DwProposal setHardwarePriceAfterDiscount(double price) {
        this.put(HW_PRICE_AFTER_DISCOUNT, price);
        return this;
    }
    public DwProposal setAccessoryPriceAfterDiscount(double price) {
        this.put(ACC_PRICE_AFTER_DISCOUNT, price);
        return this;
    }
    public DwProposal setHandleKnobPriceAfterDiscount(double price) {
        this.put(HK_PRICE_AFTER_DISCOUNT, price);
        return this;
    }
    public DwProposal setHingePriceAfterDiscount(double price) {
        this.put(HINGE_PRICE_AFTER_DISCOUNT, price);
        return this;
    }
    public DwProposal setLConnectorPriceAfterDiscount(double price) {
        this.put(LC_PRICE_AFTER_DISCOUNT, price);
        return this;
    }
    public DwProposal setLabourPriceAfterDiscount(double price) {
        this.put(LA_PRICE_AFTER_DISCOUNT, price);
        return this;
    }

}