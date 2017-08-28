package com.mygubbi.game.proposal.price;

import com.mygubbi.game.proposal.ProductAddon;
import com.mygubbi.game.proposal.ProductLineItem;
import com.mygubbi.game.proposal.model.PriceMaster;
import com.mygubbi.game.proposal.model.ProposalHeader;
import com.mygubbi.game.proposal.model.RateCard;
import io.vertx.core.json.JsonArray;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by User on 09-08-2017.
 */
public class AddonPriceHolder {
    private final static Logger LOG = LogManager.getLogger(AddonPriceHolder.class);
    private ProductAddon productAddon;
    private java.sql.Date priceDate;
    private String city;

    private double unitPrice = 0.0;
    private double unitSourceCost = 0.0;
    private double price = 0.0;
    private double priceWoTax = 0.0;
    private double sourceCost = 0.0;
    private double addonProfit = 0.0;
    private double addonMargin = 0.0;

    private JsonArray errors = null;

    PriceMaster addonFactor;
    private double profit;
    private double margin;

    public AddonPriceHolder(ProductAddon productAddon, ProposalHeader proposalHeader) {
        LOG.info("In AddonPriceHolder "+productAddon+", :"+proposalHeader);
        this.productAddon = productAddon;
        LOG.info("AddonPriceHolder productAddon = "+productAddon);
        LOG.info("AddonPriceHolder priceDate = "+proposalHeader.getPriceDate());
        this.priceDate = proposalHeader.getPriceDate();
        this.city = proposalHeader.getProjectCity();
        this.addonFactor = RateCardService.getInstance().getFactorRate(RateCard.ADDON_WO_TAX_FACTOR, this.priceDate, this.city);
    }

    public void prepare()
    {
        this.calculateAddonLevelPricing();
    }

    private void calculateAddonLevelPricing() {

        PriceMaster addonRate = RateCardService.getInstance().getAddonRate(productAddon.getCode(), this.priceDate, this.city);

        this.unitPrice = addonRate.getPrice();
        this.unitSourceCost = addonRate.getSourcePrice();
        this.price = this.productAddon.getAmount();
        this.priceWoTax = this.price * addonFactor.getSourcePrice();
        this.sourceCost = this.productAddon.getQuantity() * this.unitSourceCost;
        this.addonProfit = this.priceWoTax - this.sourceCost;
        this.addonMargin = this.addonProfit / this.priceWoTax;

    }

    public boolean hasErrors()
    {
        return this.errors != null && !this.errors.isEmpty();
    }


    public double getUnitPrice() {
        return unitPrice;
    }

    public double getUnitSourceCost() {
        return unitSourceCost;
    }

    public double getPrice() {
        return price;
    }

    public double getPriceWoTax() {
        return priceWoTax;
    }

    public double getSourceCost() {
        return sourceCost;
    }

    public double getProfit() {
        return profit;
    }

    public double getMargin() {
        return margin;
    }

    public ProductAddon getProductAddon()
    {
        return this.productAddon;
    }

    @Override
    public String toString() {
        return "AddonPriceHolder{" +
                "productAddon=" + productAddon +
                ", priceDate=" + priceDate +
                ", city='" + city + '\'' +
                ", unitPrice=" + unitPrice +
                ", unitSourceCost=" + unitSourceCost +
                ", price=" + price +
                ", priceWoTax=" + priceWoTax +
                ", sourceCost=" + sourceCost +
                ", addonProfit=" + addonProfit +
                ", addonMargin=" + addonMargin +
                ", errors=" + errors +
                ", addonFactor=" + addonFactor +
                ", profit=" + profit +
                ", margin=" + margin +
                '}';
    }
}
