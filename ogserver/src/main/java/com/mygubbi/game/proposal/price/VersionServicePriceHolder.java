package com.mygubbi.game.proposal.price;

import com.mygubbi.game.proposal.model.ProposalHeader;
import com.mygubbi.game.proposal.model.ProposalVersion;
import com.mygubbi.game.proposal.model.RateCard;
import io.vertx.core.json.JsonArray;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by User on 14-12-2017.
 */
public class VersionServicePriceHolder {

    private ProposalVersion proposalVersion;
    private ProposalHeader proposalHeader;

    private RateCard projectHandlingRateCard;
    private RateCard deepClearingRateCard;
    private RateCard floorProtectionRateCard;

    private RateCard projectHandlingTaxRateCard;
    private RateCard deepClearingTaxRateCard;
    private RateCard floorProtectionTaxRateCard;

    private double projectHandlingQty = 0;
    private double deepClearingQuantity = 0;
    private double floorProtectionSqft = 0;

    private double projectHandlingPrice = 0;
    private double deepClearingPrice = 0;
    private double floorProtectionPrice = 0;

    private double projectHandlingPriceAfterTax = 0;
    private double deepClearingPriceAfterTax = 0;
    private double floorProtectionPriceAfterTax = 0;

    private double projectHandlingCost = 0;
    private double deepClearingCost = 0;
    private double floorProtectionCost = 0;

    private double totalProductPriceAfterDiscount = 0;

    private JsonArray errors = null;
    private final static Logger LOG = LogManager.getLogger(VersionServicePriceHolder.class);
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

        this.projectHandlingTaxRateCard = RateCardService.getInstance().getRateCard(RateCard.PROJECT_HANDLING_TAX_FACTOR, RateCard.FACTOR_TYPE,this.proposalHeader.getPriceDate(), this.proposalHeader.getProjectCity());
        this.deepClearingTaxRateCard = RateCardService.getInstance().getRateCard(RateCard.DEEP_CLEARING_TAX_FACTOR, RateCard.FACTOR_TYPE,this.proposalHeader.getPriceDate(), this.proposalHeader.getProjectCity());
        this.floorProtectionTaxRateCard = RateCardService.getInstance().getRateCard(RateCard.FLOOR_PROTECTION_TAX_FACTOR, RateCard.FACTOR_TYPE,this.proposalHeader.getPriceDate(), this.proposalHeader.getProjectCity());


        if (this.projectHandlingRateCard == null || this.deepClearingRateCard == null || this.floorProtectionRateCard == null)
        {
            this.addError("Project Handling, Deep Cleaning, Floor Protection rate cards not setup.");
        }
    }


    public void calculateTotalServiceCost() {
        this.projectHandlingQty = this.proposalVersion.getProjectHandlingQty();
        this.deepClearingQuantity = this.proposalVersion.getDeepClearingQty();
        this.floorProtectionSqft = this.proposalVersion.getFloorProtectionSqft();
        LOG.info("Before floorProtectionSqft = "+floorProtectionSqft);
        LOG.info("this.proposalHeader.getFloorProtectionChargesApplied() = "+this.proposalHeader.getFloorProtectionChargesApplied());
        if (this.projectHandlingRateCard.getRate() == 0 || this.totalProductPriceAfterDiscount == 0 ) {
            this.projectHandlingPrice = 0;
        } else {
            this.projectHandlingPrice = this.proposalVersion.getProjectHandlingQty() * (this.projectHandlingRateCard.getRate() / 100);
            this.projectHandlingCost = this.proposalVersion.getProjectHandlingQty() * (this.projectHandlingRateCard.getSourcePrice() / 100);
            this.projectHandlingPriceAfterTax = this.projectHandlingPrice * this.projectHandlingTaxRateCard.getSourcePrice();
        }
        if (this.floorProtectionSqft == 0) {
            this.floorProtectionPrice = 0;
        }
        else
        {
            this.floorProtectionPrice = this.floorProtectionRateCard.getRate() * this.floorProtectionSqft;
            this.floorProtectionCost = this.floorProtectionRateCard.getSourcePrice() * this.floorProtectionSqft;
            this.floorProtectionPriceAfterTax = this.floorProtectionPrice * this.floorProtectionTaxRateCard.getSourcePrice();
        }

        LOG.info("After floorProtectionSqft = "+floorProtectionSqft);
        if (this.deepClearingQuantity == 0 ) {
            this.deepClearingPrice = 0;
        }
        else
        {
            this.deepClearingPrice = this.deepClearingRateCard.getRate() * this.deepClearingQuantity;
            this.deepClearingCost = this.deepClearingRateCard.getSourcePrice() * this.deepClearingQuantity;
            this.deepClearingPriceAfterTax = this.deepClearingPrice * this.deepClearingTaxRateCard.getSourcePrice();
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

    public double getProjectHandlingCost() {
        return projectHandlingCost;
    }

    public double getDeepClearingCost() {
        return deepClearingCost;
    }

    public double getFloorProtectionCost() {
        return floorProtectionCost;
    }

    public double getDeepClearingQuantity() {
        return deepClearingQuantity;
    }

    public double getProjectHandlingQty() {
        return projectHandlingQty;
    }

    public double getFloorProtectionSqft() {
        return floorProtectionSqft;
    }

    public double getProjectHandlingPriceAfterTax() {
        return projectHandlingPriceAfterTax;
    }

    public double getDeepClearingPriceAfterTax() {
        return deepClearingPriceAfterTax;
    }

    public double getFloorProtectionPriceAfterTax() {
        return floorProtectionPriceAfterTax;
    }
}
