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
    PriceMaster customAddonSourcePrice;

    public AddonPriceHolder(ProductAddon productAddon, ProposalHeader proposalHeader) {
        this.productAddon = productAddon;
        this.priceDate = proposalHeader.getPriceDate();
        this.city = proposalHeader.getProjectCity();
        this.addonFactor = RateCardService.getInstance().getFactorRate(RateCard.ADDON_WO_TAX_FACTOR, this.priceDate, this.city);
        this.customAddonSourcePrice = RateCardService.getInstance().getFactorRate(RateCard.CUSTOM_ADDON_SALES_PRICE_FACTOR, this.priceDate, this.city);
    }

    public void prepare()
    {
        this.calculateAddonLevelPricing();
    }

    private void calculateAddonLevelPricing() {

        PriceMaster addonRate = RateCardService.getInstance().getAddonRate(productAddon.getCode(), this.priceDate, this.city);

        if (productAddon.isCustomAddon()) {
            calculatePricingForCustomAddons(addonRate);
        } else {
            calculatePricingForStdAddons(addonRate);
        }

    }

    private void calculatePricingForStdAddons(PriceMaster addonRate) {
        this.unitPrice = addonRate.getPrice();
        this.unitSourceCost = addonRate.getSourcePrice();
        this.price = this.productAddon.getAmount();
        this.priceWoTax = this.price * addonFactor.getSourcePrice();
        this.sourceCost = this.productAddon.getQuantity() * this.unitSourceCost;
        this.addonProfit = this.priceWoTax - this.sourceCost;
        this.addonMargin = (this.addonProfit / this.priceWoTax) * 100;
    }

    private void calculatePricingForCustomAddons(PriceMaster addonRate) {
        this.unitPrice = productAddon.getAmount();
        this.unitSourceCost = this.unitPrice * customAddonSourcePrice.getSourcePrice();
        this.price = this.productAddon.getAmount();
        this.priceWoTax = this.price * addonFactor.getSourcePrice();
        this.sourceCost = this.unitSourceCost;
        this.addonProfit = this.priceWoTax - this.sourceCost;
        this.addonMargin = (this.addonProfit / this.priceWoTax) * 100;

        LOG.debug("Calulate pricing for custom addons :");
        LOG.debug("unitPrice :" + this.unitPrice);
        LOG.debug("unitPrice :" + this.unitSourceCost);
        LOG.debug("unitPrice :" + this.price);
        LOG.debug("unitPrice :" + this.priceWoTax);
        LOG.debug("unitPrice :" + this.sourceCost);
        LOG.debug("unitPrice :" + this.addonProfit);
        LOG.debug("unitPrice :" + this.addonMargin);

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
        return addonProfit;
    }

    public double getMargin() {
        return addonMargin;
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
                ", profit=" + addonProfit +
                ", margin=" + addonMargin +
                '}';
    }
}
