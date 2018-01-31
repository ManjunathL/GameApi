package com.mygubbi.game.proposal.model;

import com.mygubbi.common.DateUtil;
import io.vertx.core.json.JsonObject;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;

/**
 * Created by Chirag on 08-03-2017.
 */
public class ProposalVersion extends JsonObject{

    public static final String ID = "id";
    public static final String VERSION = "version";
    public static final String FROM_VERSION = "fromVersion";
    public static final String TO_VERSION = "toVersion";
    public static final String PROPOSAL_ID = "proposalId";
    public static final String DATE = "date";
    public static final String FINAL_AMOUNT = "finalAmount";
    public static final String DISCOUNT_AMOUNT = "discountAmount";
    public static final String DISCOUNT_PERCENTAGE = "discountPercentage";
    public static final String STATUS = "status";
    public static final String INTERNAL_STATUS = "internalStatus";
    public static final String CREATED_BY= "createdBy";
    public static final String UPDATED_ON= "updatedOn";
    public static final String BUSINESS_DATE= "businessDate";
    public static final String UPDATED_BY= "updatedBy";
    public static final String AMOUNT= "amount";
    public static final String DEEP_CLEARING_QTY = "deepClearingQty";
    public static final String FLOOR_PROTECTION_SQFT = "floorProtectionSqft";
    public static final String DEEP_CLEARING_AMT = "deepClearingAmount";
    public static final String FLOOR_PROTECTION_AMT = "floorProtectionAmount";
    public static final String PROJECT_HANDLING_AMT = "projectHandlingAmount";
    public static final String PROJECT_HANDLING_QTY = "projectHandlingQty";
    public static final String TITLE = "title";


    public ProposalVersion() {}

    public ProposalVersion(JsonObject jsonObject){
        super(jsonObject.getMap());
    }

    public int getId() {
        return this.getInteger(ID);
    }

    public ProposalVersion setId(int id) {
        this.put(ID, id);
        return this;
    }

    public int getProposalId() {
        return this.getInteger(PROPOSAL_ID);
    }

    public ProposalVersion setProposalId(int proposalId) {
        this.put(PROPOSAL_ID, proposalId);
        return this;
    }

    public String getProposalStatus() {
        return this.getString(STATUS);
    }

    public ProposalVersion setProposalStatus(String status) {
        this.put(STATUS, status);
        return this;
    }
    public String getInternalStatus() {
        return this.getString(INTERNAL_STATUS);
    }

    public ProposalVersion setInternalStatus(String internalStatus) {
        this.put(INTERNAL_STATUS, internalStatus);
        return this;
    }

    public String getTitle()
    {
        return this.getString(TITLE);
    }

    public ProposalVersion setTitle(String title)
    {
        this.put(TITLE,title);
        return this;
    }
    public String getVersion() {
        return this.getString(VERSION);
    }

    public ProposalVersion setVersion(String version) {
        this.put(VERSION, version);
        return this;
    }

    public String getFromVersion() {
        return this.getString(FROM_VERSION);
    }

    public ProposalVersion setFromVersion(String fromVersion) {
        this.put(FROM_VERSION, fromVersion);
        return this;
    }
    public String getToVersion() {
        return this.getString(TO_VERSION);
    }

    public ProposalVersion setToVersion(String toVersion) {
        this.put(TO_VERSION, toVersion);
        return this;
    }

    public Date getDate() {
        return DateUtil.convertDate(this.getString(DATE));
    }


    public ProposalVersion setDate(Date date)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String format = formatter.format(date);
        this.put(DATE, format);
        return this;
    }

    public double getAmount() {
        return this.getDouble(AMOUNT);
    }

    public ProposalVersion setAmount(double amount) {
        this.put(AMOUNT, amount);
        return this;
    }

    public double getFinalAmount() {
        return this.getDouble(FINAL_AMOUNT);
    }

    public ProposalVersion setFinalAmount(double finalAmount) {
        this.put(FINAL_AMOUNT, finalAmount);
        return this;
    }

    public double getProjectHandlingQty() { return  this.getDouble(PROJECT_HANDLING_QTY);}

    public  ProposalVersion setProjectHAndlingQty(double projectHandlingQty)
    {
        this.put(PROJECT_HANDLING_QTY,projectHandlingQty);
        return this;
    }
    public double getDiscountAmount() {
        return this.getDouble(DISCOUNT_AMOUNT);
    }

    public ProposalVersion setDiscountAmount(double discountAmount) {
        this.put(DISCOUNT_AMOUNT, discountAmount);
        return this;
    }

    public double getDiscountPercentage() {
        return this.getDouble(DISCOUNT_PERCENTAGE);
    }

    public ProposalVersion setDiscountPercenatge(double discountPercentage) {
        this.put(DISCOUNT_PERCENTAGE, discountPercentage);
        return this;
    }


    public double getDeepClearingQty() {
        return this.getDouble(DEEP_CLEARING_QTY);
    }

    public ProposalVersion setDeepClearingQty(double deepClearingQty) {
        this.put(DEEP_CLEARING_QTY, deepClearingQty);
        return this;
    }

    public double getDeepClearingAmount() {
        return this.getDouble(DEEP_CLEARING_AMT);
    }

    public ProposalVersion setDeepClearingAmount(double deepClearingAmount) {
        this.put(DEEP_CLEARING_AMT, deepClearingAmount);
        return this;
    }

    public double getFloorProtectionSqft() {
        return this.getDouble(FLOOR_PROTECTION_SQFT);
    }

    public ProposalVersion setFloorProtectionSqft(double floorProtectionSqft) {
        this.put(FLOOR_PROTECTION_SQFT, floorProtectionSqft);
        return this;
    }

    public double getFloorProtectionAmount() {
        return this.getDouble(FLOOR_PROTECTION_AMT);
    }

    public ProposalVersion setFloorProtectionAmount(double floorProtectionAmount) {
        this.put(FLOOR_PROTECTION_AMT, floorProtectionAmount);
        return this;
    }

    public double getProjectHandlingAmount() {
        return this.getDouble(PROJECT_HANDLING_AMT);
    }

    public ProposalVersion setProjectHandlingAmount(double projectHandlingAmount) {
        this.put(PROJECT_HANDLING_AMT, projectHandlingAmount);
        return this;
    }



    public String getCreatedBy() {
        return this.getString(CREATED_BY);
    }


    public String getUpdatedBy()
    {
        return this.getString(UPDATED_BY);
    }

    public Date getUpdatedOn()
    {
        return DateUtil.convertDate(this.getString(UPDATED_ON));
    }
    public Date getBusinessDate()
    {
        return DateUtil.convertDate(this.getString(BUSINESS_DATE));
    }


}
