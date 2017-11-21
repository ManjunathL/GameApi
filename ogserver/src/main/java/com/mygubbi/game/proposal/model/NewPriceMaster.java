package com.mygubbi.game.proposal.model;


import com.sun.org.apache.bcel.internal.generic.NEW;
import io.vertx.core.json.JsonObject;

/**
 * Created by Shruthi on 9/21/2017.
 */
public class NewPriceMaster extends JsonObject
{
    private static final String ID="id";
    private static final String PROPOSAL_ID="proposalId";
    private static final String VERSION="version";
    private static final String NEW_TXN_AMT="newTxnAmount";
    private static final String DISCOUNT_AMOUNT="discountAmount";
    private static final String DISCOUNT_PERCENTAGE="discountPercentage";
    private static final String OLD_TXN_AMOUNT="oldTxnAmount";
    private static final String DIFFERENCE_AMOUNT="differenceAmount";

    public NewPriceMaster(JsonObject jsonObject)
    {
        super(jsonObject.getMap());
    }

    public int getID() {
        return this.getInteger(ID);
    }

    public NewPriceMaster setId(int id) {
        this.put(ID, id);
        return this;
    }

    public int getProposalId() {
        return this.getInteger(PROPOSAL_ID);
    }

    public NewPriceMaster setProposalId(int proposalId) {
        this.put(PROPOSAL_ID, proposalId);
        return this;
    }

    public String getVersion() {
        return this.getString(VERSION);
    }

    public NewPriceMaster setVersion(String version) {
        this.put(VERSION, version);
        return this;
    }

    public double getNewTxnAmount() {
        return this.getDouble(NEW_TXN_AMT);
    }

    public NewPriceMaster setNewTxnAmount(double newTxnAmount) {
        this.put(NEW_TXN_AMT, newTxnAmount);
        return this;
    }

     public double getDiscountAmount() {
        return this.getDouble(DISCOUNT_AMOUNT);
    }

    public NewPriceMaster setDiscountAmount(double discountAmount) {
        this.put(DISCOUNT_AMOUNT,discountAmount);
        return this;
    }

    public double getDiscountPercentage() {
        return this.getDouble(DISCOUNT_PERCENTAGE);
    }

    public NewPriceMaster setDiscountPercentage(double discountPercentage) {
        this.put(DISCOUNT_PERCENTAGE,discountPercentage);
        return this;
    }

    public double getOldTxnAmount() {
        return this.getDouble(OLD_TXN_AMOUNT);
    }

    public NewPriceMaster setOldTxnAmount(double oldTxnAmount) {
        this.put(OLD_TXN_AMOUNT,oldTxnAmount);
        return this;
    }

    public double getDifferenceAmount() {
        return this.getDouble(DIFFERENCE_AMOUNT);
    }

    public NewPriceMaster setDifferenceAmount(double differenceAmount) {
        this.put(DIFFERENCE_AMOUNT,differenceAmount);
        return this;
    }




}
