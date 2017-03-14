package com.mygubbi.game.proposal.model;

import io.vertx.core.json.JsonObject;

/**
 * Created by Chirag on 08-03-2017.
 */
public class ProposalAddon {

    public static final String ID = "id";
    public static final String PROPOSAL_ID = "proposalId";
    public static final String PRODUCT = "product";
    public static final String RATE = "rate";

    private int id;
    private int proposalId;
    private String product;
    private double rate;

    public ProposalAddon(JsonObject jsonObject)
    {
         this.setId(jsonObject.getInteger(ID)).setProposalId(jsonObject.getInteger(PROPOSAL_ID))
                .setProduct(jsonObject.getString(PRODUCT)).setRate(jsonObject.getDouble(RATE));
    }

    public int getId() {
        return id;
    }

    public ProposalAddon setId(int id) {
        this.id = id;
        return this;
    }

    public int getProposalId() {
        return proposalId;
    }

    public ProposalAddon setProposalId(int proposalId) {
        this.proposalId = proposalId;
        return this;
    }

    public String getProduct() {
        return product;
    }

    public ProposalAddon setProduct(String product) {
        this.product = product;
        return this;
    }

    public double getRate() {
        return rate;
    }

    public ProposalAddon setRate(double rate) {
        this.rate = rate;
        return this;
    }
}
