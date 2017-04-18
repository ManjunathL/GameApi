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
    public static final String VERSION = "version";
    public static final String PRICE_DATE = "priceDate";
    public static final String OLD_AMOUNT_PRODUCT = "oldAmountProduct";
    public static final String NEW_AMOUNT_PRODUCT = "newAmountProduct";
    public static final String OLD_AMOUNT_ADDON = "oldAmountAddon";
    public static final String NEW_AMOUNT_ADDON = "newAmountAddon";

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

    public String getVERSION() {
        return this.getString(VERSION);
    }

    public Date getPriceDate() {
        return (Date) this.getValue(PRICE_DATE);
    }

    public double getOldAmountProduct() {
        return this.getDouble(OLD_AMOUNT_PRODUCT);
    }

    public double getNewAmountProduct() {
        return this.getDouble(NEW_AMOUNT_PRODUCT);
    }

    public double getOldAmountAddon() {
        return this.getDouble(OLD_AMOUNT_ADDON);
    }

    public double getNewAmountAddon() {
        return this.getDouble(NEW_AMOUNT_ADDON);
    }

    public AuditMaster setId(int id) {
        this.put(ID, id);
        return this;
    }

    public AuditMaster setProposalId(int proposalId) {
        this.put(PROPOSAL_ID, proposalId);
        return this;
    }

    public AuditMaster setVersion(String version) {
        this.put(VERSION, version);
        return this;
    }

    public AuditMaster setPriceDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String format = formatter.format(date);
        this.put(PRICE_DATE, format);
        return this;
    }

    public AuditMaster setOldAmountProduct(double oldAmountProduct) {
        this.put(OLD_AMOUNT_PRODUCT, oldAmountProduct);
        return this;
    }

    public AuditMaster setNewAmountProduct(double newAmountProduct) {
        this.put(NEW_AMOUNT_PRODUCT, newAmountProduct);
        return this;
    }

    public AuditMaster setOldAmountAddon(double oldAmountAddon) {
        this.put(OLD_AMOUNT_ADDON, oldAmountAddon );
        return this;
    }

    public AuditMaster setNewAmountAddon(double newAmountAddon) {
        this.put(NEW_AMOUNT_ADDON, newAmountAddon);
        return this;
    }


}
