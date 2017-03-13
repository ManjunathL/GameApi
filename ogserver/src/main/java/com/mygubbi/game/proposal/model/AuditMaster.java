package com.mygubbi.game.proposal.model;

import io.vertx.core.json.JsonObject;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Created by Chirag on 10-03-2017.
 */
public class AuditMaster extends JsonObject {

    public static final String ID = "id";
    public static final String PROPOSAL_ID = "proposalId";
    public static final String PRICE_DATE = "priceDate";
    public static final String OLD_AMOUNT = "oldAmount";
    public static final String NEW_AMOUNT = "newAmount";

    public AuditMaster() {}

    public AuditMaster(JsonObject jsonObject){
        super(jsonObject.getMap());
    }

    public int getID() {
        return this.getInteger(ID);
    }

    public int getProposalId() {
        return this.getInteger(PROPOSAL_ID);
    }

    public Date getPriceDate() {
        return (Date) this.getValue(PRICE_DATE);
    }

    public double getOldAmount() {
        return this.getDouble(OLD_AMOUNT);
    }

    public double getNewAmount() {
        return this.getDouble(NEW_AMOUNT);
    }

    public AuditMaster setId(int id) {
        this.put(ID, id);
        return this;
    }

    public AuditMaster setProposalId(int proposalId) {
        this.put(PROPOSAL_ID, proposalId);
        return this;
    }

    public AuditMaster setPriceDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String format = formatter.format(date);
        this.put(PRICE_DATE, format);
        return this;
    }

    public AuditMaster setOldAmount(double oldAmount) {
        this.put(OLD_AMOUNT, oldAmount);
        return this;
    }

    public AuditMaster setNewAmount(double newAmount) {
        this.put(NEW_AMOUNT, newAmount);
        return this;
    }



}
