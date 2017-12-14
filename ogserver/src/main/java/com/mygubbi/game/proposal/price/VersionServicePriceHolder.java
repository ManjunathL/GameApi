package com.mygubbi.game.proposal.price;

import com.mygubbi.game.proposal.model.ProposalHeader;
import com.mygubbi.game.proposal.model.ProposalVersion;
import com.mygubbi.game.proposal.model.RateCard;
import io.vertx.core.json.JsonArray;

/**
 * Created by User on 14-12-2017.
 */
public class VersionServicePriceHolder {

    private ProposalVersion proposalVersion;
    private ProposalHeader proposalHeader;

    private RateCard projectHandlingRateCard;
    private RateCard deepClearingRateCard;
    private RateCard floorProtectionRateCard;


    private double deepClearingQuantity = 1;
    private double floorProtectionSqft = 1;

    private double projectHandlingPrice = 0;
    private double deepClearingPrice = 0;
    private double floorProtectionPrice = 0;

    private double totalProductPriceAfterDiscount = 0;

    private JsonArray errors = null;

    public VersionServicePriceHolder(ProposalVersion proposalVersion, double totalProductPriceAfterDiscount, ProposalHeader proposalHeader) {
        this.proposalVersion = proposalVersion;
        this.totalProductPriceAfterDiscount = totalProductPriceAfterDiscount;
        this.proposalHeader = proposalHeader;
    }

    public void prepare()
    {
        collectFactors();
    }

    private void collectFactors() {
        this.projectHandlingRateCard = RateCardService.getInstance().getRateCard(RateCard.LOADING_FACTOR, RateCard.FACTOR_TYPE,this.proposalHeader.getPriceDate(), this.proposalHeader.getProjectCity());
        this.deepClearingRateCard = RateCardService.getInstance().getRateCard(RateCard.LABOUR_FACTOR, RateCard.FACTOR_TYPE,this.proposalHeader.getPriceDate(), this.proposalHeader.getProjectCity());
        this.floorProtectionRateCard = RateCardService.getInstance().getRateCard(RateCard.LABOUR_COST_FACTOR, RateCard.FACTOR_TYPE,this.proposalHeader.getPriceDate(), this.proposalHeader.getProjectCity());


        if (this.projectHandlingRateCard == null || this.deepClearingRateCard == null || this.floorProtectionRateCard == null)
        {
            this.addError("Project Handling, Deep Cleaning, Floor Protection rate cards not setup.");
        }
    }


    public void calculateTotalServiceCost()
    {
        this.deepClearingQuantity = this.proposalVersion.getDeepClearingQty();
        this.floorProtectionSqft = this.proposalVersion.getFloorProtectionSqft();
        if (this.projectHandlingRateCard.getRate() == 0 || this.deepClearingQuantity == 0 || this.floorProtectionSqft == 0)
        {
            this.projectHandlingPrice = 0;
            this.deepClearingPrice = 0;
            this.floorProtectionPrice = 0;
        }
        else {
            this.projectHandlingPrice = this.totalProductPriceAfterDiscount * (this.projectHandlingRateCard.getRate() / 100);
            this.deepClearingPrice = this.deepClearingRateCard.getRate() * this.deepClearingQuantity;
            this.floorProtectionPrice = this.floorProtectionRateCard.getRate() * this.floorProtectionSqft;
        }

    }


    public void addError(String error)
    {
        if (errors == null)
        {
            this.errors = new JsonArray();
        }
        this.errors.add(error);
    }

    public boolean hasErrors()
    {
        return this.errors != null && !this.errors.isEmpty();
    }

    public JsonArray getErrors()
    {
        return this.errors;
    }

    public double getProjectHandlingPrice() {
        return this.projectHandlingPrice;
    }

    public double getDeepClearingPrice() {
        return this.deepClearingPrice;
    }

    public double getFloorProtectionPrice() {
        return this.floorProtectionPrice;
    }

    public double getDeepClearingQuantity() {
        return deepClearingQuantity;
    }

    public double getFloorProtectionSqft() {
        return floorProtectionSqft;
    }
}
