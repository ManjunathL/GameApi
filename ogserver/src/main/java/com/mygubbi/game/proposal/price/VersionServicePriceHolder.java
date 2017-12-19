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


    private double deepClearingQuantity = 0;//shilpa made it zero,previously it was 1
    private double floorProtectionSqft = 0;//shilpa made it zero,previously it was 1

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
        this.projectHandlingRateCard = RateCardService.getInstance().getRateCard(RateCard.PROJECT_HANDLING_FACTOR, RateCard.FACTOR_TYPE,this.proposalHeader.getPriceDate(), this.proposalHeader.getProjectCity());
        this.deepClearingRateCard = RateCardService.getInstance().getRateCard(RateCard.DEEP_CLEARING_FACTOR, RateCard.FACTOR_TYPE,this.proposalHeader.getPriceDate(), this.proposalHeader.getProjectCity());
        this.floorProtectionRateCard = RateCardService.getInstance().getRateCard(RateCard.FLOOR_PROTECTION_FACTOR, RateCard.FACTOR_TYPE,this.proposalHeader.getPriceDate(), this.proposalHeader.getProjectCity());


        if (this.projectHandlingRateCard == null || this.deepClearingRateCard == null || this.floorProtectionRateCard == null)
        {
            this.addError("Project Handling, Deep Cleaning, Floor Protection rate cards not setup.");
        }
    }


    public void calculateTotalServiceCost()
    {
        this.deepClearingQuantity = this.proposalVersion.getDeepClearingQty();
        this.floorProtectionSqft = this.proposalVersion.getFloorProtectionSqft();
//        if(this.deepClearingPrice == 0 && this.deepClearingQuantity == 0) this.deepClearingQuantity =1;
//        if(this.floorProtectionSqft == 0 && this.floorProtectionPrice == 0) this.floorProtectionSqft =1;
        //shilpa commented top lines
        if (this.projectHandlingRateCard.getRate() == 0 || this.totalProductPriceAfterDiscount == 0)
        {
            this.projectHandlingPrice = 0;
        }
        else {
            System.out.println("Shilpa:: this.totalProductPriceAfterDiscount = "+this.totalProductPriceAfterDiscount);
            System.out.println("Shilpa:: this.projectHandlingRateCard = "+this.projectHandlingRateCard.getRate());

           this.projectHandlingPrice = this.projectHandlingPrice;
            this.deepClearingPrice = this.deepClearingPrice;
            this.floorProtectionPrice = this.floorProtectionPrice;
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
