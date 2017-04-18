package com.mygubbi.game.proposal.model;

import io.vertx.core.json.JsonObject;

/**
 * Created by Chirag on 08-03-2017.
 */
public class ProposalVersion extends JsonObject{

    public static final String ID = "id";
    public static final String VERSION = "version";
    public static final String PROPOSAL_ID = "proposalId";
    public static final String FINAL_AMOUNT = "finalAmount";
    public static final String DISCOUNT_AMOUNT = "discountAmount";
    public static final String DISCOUNT_PERCENTAGE = "discountPercentage";
    public static final String AMOUNT= "amount";

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

    public String getVersion() {
        return this.getString(VERSION);
    }

    public ProposalVersion setVersion(String version) {
        this.put(VERSION, version);
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
}
