package com.mygubbi.game.proposal.price;

import com.mygubbi.game.proposal.ModuleDataService;
import com.mygubbi.game.proposal.ProductAddon;
import com.mygubbi.game.proposal.ProductLineItem;
import com.mygubbi.game.proposal.ProductModule;
import com.mygubbi.game.proposal.model.PriceMaster;
import com.mygubbi.game.proposal.model.ProductCategoryMap;
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

    double addonFactor;
    PriceMaster customAddonSourcePrice;

    public AddonPriceHolder(ProductAddon productAddon, ProposalHeader proposalHeader) {
        this.productAddon = productAddon;
        this.priceDate = proposalHeader.getPriceDate();
        this.city = proposalHeader.getProjectCity();
        this.addonFactor = getAfterTaxFactor(proposalHeader,this.productAddon);
        this.customAddonSourcePrice = RateCardService.getInstance().getFactorRate(RateCard.CUSTOM_ADDON_SALES_PRICE_FACTOR, this.priceDate, this.city);
    }

    private double getAfterTaxFactor(ProposalHeader proposalHeader, ProductAddon productAddon) {

        RateCard prodWoTaxFactor = RateCardService.getInstance().getRateCard(RateCard.PRODUCT_WO_TAX,
                RateCard.FACTOR_TYPE, proposalHeader.getPriceDate(), proposalHeader.getProjectCity());
        double woTaxFactor = 0;

        ProductCategoryMap productCategoryMap = ModuleDataService.getInstance().getProductCategoryMap(productAddon.getCategoryCode(),proposalHeader.getPriceDate());
        String productType = productCategoryMap.getType();

        RateCard movableFurnitureRateCard = RateCardService.getInstance().getRateCard(RateCard.MOVABLE_FURNITURE,
                RateCard.FACTOR_TYPE,proposalHeader.getPriceDate(), proposalHeader.getProjectCity());
        RateCard nonMovableFurnitureRateCard = RateCardService.getInstance().getRateCard(RateCard.NON_MOVABLE_FURNITURE,
                RateCard.FACTOR_TYPE,proposalHeader.getPriceDate(), proposalHeader.getProjectCity());
        RateCard servicesCivilWork = RateCardService.getInstance().getRateCard(RateCard.SERVICES_CIVIL_WORK,
                RateCard.FACTOR_TYPE,proposalHeader.getPriceDate(), proposalHeader.getProjectCity());


        switch (productType) {
            case RateCard.MOVABLE_FURNITURE:
                woTaxFactor = movableFurnitureRateCard.getSourcePrice();
                break;
            case RateCard.NON_MOVABLE_FURNITURE:
                woTaxFactor = nonMovableFurnitureRateCard.getSourcePrice();
                break;
            case RateCard.SERVICES_CIVIL_WORK:
                woTaxFactor = servicesCivilWork.getSourcePrice();
                break;
            default:
                woTaxFactor = servicesCivilWork.getSourcePrice();
                break;
        }
        return woTaxFactor;
    }


    public void prepare()
    {
        this.calculateAddonLevelPricing();
    }

    private void calculateAddonLevelPricing() {

        PriceMaster addonRate = RateCardService.getInstance().getAddonRate(productAddon.getCode(), this.priceDate, this.city);

        if (productAddon.isCustomAddon()) {
            calculatePricingForCustomAddons();
        } else {
            calculatePricingForStdAddons(addonRate);
        }

    }

    private void calculatePricingForStdAddons(PriceMaster addonRate) {
        this.unitPrice = addonRate.getPrice();
        this.unitSourceCost = addonRate.getSourcePrice();
        this.price = this.productAddon.getAmount();
        this.priceWoTax = this.price * this.addonFactor;
        this.sourceCost = this.productAddon.getQuantity() * this.unitSourceCost;
        this.addonProfit = this.priceWoTax - this.sourceCost;
        if(this.addonProfit == 0 || this.priceWoTax == 0){
            this.addonMargin = 0;
        }else {
            this.addonMargin = (this.addonProfit / this.priceWoTax) * 100;
        }
    }

    private void calculatePricingForCustomAddons() {
        this.unitPrice = productAddon.getAmount();
        this.unitSourceCost = this.unitPrice * customAddonSourcePrice.getSourcePrice();
        this.price = this.productAddon.getAmount();
        this.priceWoTax = this.price * this.addonFactor;
        this.sourceCost = this.unitSourceCost;
        this.addonProfit = this.priceWoTax - this.sourceCost;
        if(this.addonProfit == 0 || this.priceWoTax == 0){
            this.addonMargin = 0;
        }else {
            this.addonMargin = (this.addonProfit / this.priceWoTax) * 100;
        }
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
