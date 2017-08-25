package com.mygubbi.game.proposal.price;


import com.mygubbi.common.StringUtils;
import com.mygubbi.game.proposal.ModuleDataService;
import com.mygubbi.game.proposal.ProductLineItem;
import com.mygubbi.game.proposal.ProductModule;
import com.mygubbi.game.proposal.model.PriceMaster;
import com.mygubbi.game.proposal.model.ProposalHeader;
import com.mygubbi.game.proposal.model.ProposalVersion;
import com.mygubbi.game.proposal.model.RateCard;
import io.vertx.core.json.JsonArray;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.List;

/**
 * Created by User on 08-08-2017.
 */
public class ProductPriceHolder {

    private final static Logger LOG = LogManager.getLogger(ProductPriceHolder.class);


    private List<ModulePriceHolder> modulePriceHolders ;
    private ProductLineItem productLineItem ;
    private ProposalVersion proposalVersion ;

    private JsonArray errors = null;

    private double productSourceCost = 0.0;
    private double productPriceWoTax = 0.0;
    private double productPrice = 0.0;
    private double productPriceAfterDiscount = 0.0;
    private double productProfit = 0.0;
    private double productMargin = 0.0;
    private double productHingeSourceCost = 0.0;
    private double productHingePriceWoTax = 0.0;
    private double productHingePriceAfterDiscount = 0.0;
    private double productHingePrice = 0.0;
    private double productHingeProfit = 0.0;
    private double productHingeMargin = 0.0;
    private double productHandleAndKnobSourceCost = 0.0;
    private double productHandleAndKnobPriceWoTax = 0.0;
    private double productHandleAndKnobPrice = 0.0;
    private double productHandleAndKnobPriceAfterDiscount = 0.0;
    private double productHandleAndKnobProfit = 0.0;
    private double productHandleAndKnobMargin = 0.0;
    private double productAccessorySourceCost = 0.0;
    private double productAccessoryPriceWoTax = 0.0;
    private double productAccessoryProfit = 0.0;
    private double productAccessoryMargin = 0.0;
    private double productAccessoryPrice = 0.0;
    private double productAccessoryPriceAfterDiscount = 0.0;
    private double productHardwareSourceCost = 0.0;
    private double productHardwarePriceWoTax = 0.0;
    private double hardwarePriceAfterDiscount = 0.0;
    private double productHardwarePrice = 0.0;
    private double productHardwareProfit = 0.0;
    private double productHardwareMargin = 0.0;
    private double productLabourSourceCost = 0.0;
    private double productLabourPriceWoTax = 0.0;
    private double productLabourPrice = 0.0;
    private double productLabourPriceAfterDiscount = 0.0;
    private double productLabourProfit = 0.0;
    private double productLabourMargin = 0.0;
    private double productShutterSourceCost = 0.0;
    private double productShutterCostWoTax = 0.0;
    private double productShutterPrice = 0.0;
    private double productCarcassSourceCost = 0.0;
    private double productCarcassPriceWoTax = 0.0;
    private double productCarcassPrice = 0.0;
    private double woodWorkPrice = 0.0;
    private double woodWorkPriceAfterDiscount = 0.0;
    private double woodWorkPriceWoTax = 0.0;
    private double woodWorkSourceCost = 0.0;
    private double woodWorkProfit = 0.0;
    private double woodWorkMargin = 0.0;
    private double productLConnectorSourceCost = 0.0;
    private double productLConnectorPriceWoTax = 0.0;
    private double productLConnectorPriceAfterDiscount = 0.0;
    private double productLConnectorPrice = 0.0;
    private double productLConnectorProfit = 0.0;
    private double productLConnectorMargin = 0.0;
    private double productAreainSqft = 0.0;
    private int stdModuleCount = 0;
    private int nStdModuleCount = 0;
    private int hikeModuleCount = 0;
    private double stdModulePrice = 0.0;
    private double nStdModulePrice = 0.0;
    private double hikeModulePrice = 0.0;

    private java.sql.Date priceDate;
    private String city;
    private double discountPercentage;

    private ProposalHeader proposalHeader;

    public ProductPriceHolder(ProductLineItem productLineItem, List<ModulePriceHolder> modulePriceHolders, ProposalHeader proposalHeader, ProposalVersion proposalVersion) {
        this.modulePriceHolders = modulePriceHolders;
        this.productLineItem = productLineItem;
        this.priceDate = proposalHeader.getPriceDate();
        this.city = proposalHeader.getProjectCity();
        this.discountPercentage = proposalVersion.getDiscountPercentage() / 100;
        this.proposalVersion = proposalVersion;
        this.proposalHeader = proposalHeader;
    }

    public void prepare()
    {

        this.calculateProductLevelPricing();
    }

private void initializeValues(){
    this.productSourceCost = 0.0;
    this.productPriceWoTax = 0.0;
    this.productPrice = 0.0;
    this.productPriceAfterDiscount = 0.0;
    this.productProfit = 0.0;
    this.productMargin = 0.0;
    this.productHingeSourceCost = 0.0;
    this.productHingePriceWoTax = 0.0;
    this.productHingePriceAfterDiscount = 0.0;
    this.productHingePrice = 0.0;
    this.productHingeProfit = 0.0;
    this.productHingeMargin = 0.0;
    this.productHandleAndKnobSourceCost = 0.0;
    this.productHandleAndKnobPriceWoTax = 0.0;
    this.productHandleAndKnobPrice = 0.0;
    this.productHandleAndKnobPriceAfterDiscount = 0.0;
    this.productHandleAndKnobProfit = 0.0;
    this.productHandleAndKnobMargin = 0.0;
    this.productAccessorySourceCost = 0.0;
    this.productAccessoryPriceWoTax = 0.0;
    this.productAccessoryProfit = 0.0;
    this.productAccessoryMargin = 0.0;
    this.productAccessoryPrice = 0.0;
    this.productAccessoryPriceAfterDiscount = 0.0;
    this.productHardwareSourceCost = 0.0;
    this.productHardwarePriceWoTax = 0.0;
    this.hardwarePriceAfterDiscount = 0.0;
    this.productHardwarePrice = 0.0;
    this.productHardwareProfit = 0.0;
    this.productHardwareMargin = 0.0;
    this.productLabourSourceCost = 0.0;
    this.productLabourPriceWoTax = 0.0;
    this.productLabourPrice = 0.0;
    this.productLabourPriceAfterDiscount = 0.0;
    this.productLabourProfit = 0.0;
    this.productLabourMargin = 0.0;
    this.productShutterSourceCost = 0.0;
    this.productShutterCostWoTax = 0.0;
    this.productShutterPrice = 0.0;
    this.productCarcassSourceCost = 0.0;
    this.productCarcassPriceWoTax = 0.0;
    this.productCarcassPrice = 0.0;
    this.woodWorkPrice = 0.0;
    this.woodWorkPriceAfterDiscount = 0.0;
    this.woodWorkPriceWoTax = 0.0;
    this.woodWorkSourceCost = 0.0;
    this.woodWorkProfit = 0.0;
    this.woodWorkMargin = 0.0;
    this.productLConnectorSourceCost = 0.0;
    this.productLConnectorPriceWoTax = 0.0;
    this.productLConnectorPriceAfterDiscount = 0.0;
    this.productLConnectorPrice = 0.0;
    this.productLConnectorProfit = 0.0;
    this.productLConnectorMargin = 0.0;
    this.productAreainSqft = 0.0;
    this.stdModuleCount = 0;
    this.nStdModuleCount = 0;
    this.hikeModuleCount = 0;
    this.stdModulePrice = 0.0;
    this.nStdModulePrice = 0.0;
    this.hikeModulePrice = 0.0;

}
    private void calculateProductLevelPricing() {

//        this.initializeValues();
        double lConnectorPrice = 0;
        PriceMaster lConnectorRate=RateCardService.getInstance().getHardwareRate("H074",this.priceDate,this.city);
        PriceMaster lConnectorFactor=RateCardService.getInstance().getFactorRate(RateCard.ADDON_WO_TAX_FACTOR,this.priceDate,this.city);

        for (ModulePriceHolder modulePriceHolder : modulePriceHolders)
        {

            ProductModule productModule = modulePriceHolder.getProductModule();

            if (productModule.getModuleCategory().startsWith("S")) {
                this.stdModuleCount = this.stdModuleCount + 1;
                this.stdModulePrice = this.stdModulePrice + modulePriceHolder.getTotalCost();
            } else if (productModule.getModuleCategory().startsWith("N")) {
                this.nStdModuleCount = this.nStdModuleCount + 1;
                this.nStdModulePrice = this.nStdModulePrice + modulePriceHolder.getTotalCost();
            } else if (productModule.getModuleCategory().startsWith("H")) {
                this.hikeModuleCount = this.hikeModuleCount + 1;
                this.hikeModulePrice = this.hikeModulePrice + modulePriceHolder.getTotalCost();
            }

            addToProductCarcassPrice(modulePriceHolder.getCarcassCost());
            addToProductCarcassPriceWoTax(modulePriceHolder.getCarcassCostWoTax());
            addToProductCarcassCost(modulePriceHolder.getCarcassSourceCost());

            addToProductShutterPrice(modulePriceHolder.getShutterCost());
            addToProductShutterPriceWoTax(modulePriceHolder.getShutterCostWoTax());
            addToProductShutterCost(modulePriceHolder.getShutterSourceCost());

            addToLabourPrice(modulePriceHolder.getLabourCost());
            addToLabourPriceWoTax(modulePriceHolder.getLabourCostWoTax());
            addToLabourSourceCost(modulePriceHolder.getLabourSourceCost());

            addToHardwarePrice(modulePriceHolder.getHardwareCost());
            addToHardwarePriceWoTax(modulePriceHolder.getHardwareCostWoTax());
            addToHardwareSourceCost(modulePriceHolder.getHardwareSourceCost());

            addToAccessoryPrice(modulePriceHolder.getAccessoryCost());
            addToAccessoryPriceWoTax(modulePriceHolder.getAccessoryCostWoTax());
            addToAccessorySourceCost(modulePriceHolder.getAccessorySourceCost());

            addToHandleAndKnobPrice(modulePriceHolder.getHandleandKnobCost());
            addToHandleAndKnobPriceWoTax(modulePriceHolder.getHandleandKnobCostWoTax());
            addToHandleAndKnobSourceCost(modulePriceHolder.getHandleandKnobSourceCost());

            addToHingePrice(modulePriceHolder.getHingeCost());
            addToHingePriceWoTax(modulePriceHolder.getHingeCostWoTax());
            addToHingeSourceCost(modulePriceHolder.getHingeSourceCost());

            addToTotalProductPrice(modulePriceHolder.getTotalCost());
            addToTotalProductPriceWoTax(modulePriceHolder.getTotalCostWoTax());
            addToTalProductSourceCost(modulePriceHolder.getTotalSourceCost());

            addToAreaInSqft(modulePriceHolder.getMgModule().getAreaOfModuleInSft());

        }

        double rateForLconnectorPrice=lConnectorRate.getPrice();
        double sourcePriceForLconnectorPrice = lConnectorRate.getSourcePrice();
        addToLConnectorPrice(this.productLineItem.getNoOfLengths() * rateForLconnectorPrice);
        addToLConnectorPriceWoTax(this.productLConnectorPrice * lConnectorFactor.getSourcePrice());
        addToLConnectorSourceCost(this.productLineItem.getNoOfLengths() * sourcePriceForLconnectorPrice );


    }

    private void addToAreaInSqft(double areaOfModuleInSft) {
        if (areaOfModuleInSft ==0 ) return;
        productAreainSqft += areaOfModuleInSft;
    }

    public void addError(String error)
    {
        if (errors == null)
        {
            this.errors = new JsonArray();
        }
        this.errors.add(error);
    }

    private void addToTalProductSourceCost(double totalSourceCost) {
        if (totalSourceCost == 0) return;
        this.productSourceCost += totalSourceCost;
    }

    private void addToTotalProductPriceWoTax(double totalCostWoTax) {
        if (totalCostWoTax == 0) return;
        this.productPriceWoTax += totalCostWoTax;
    }

    private void addToTotalProductPrice(double totalCost) {
        if (totalCost == 0) return;
        this.productPrice += totalCost;
       }

    private void addToHingeSourceCost(double hingeSourceCost) {
        if (hingeSourceCost == 0) return;
        this.productHingeSourceCost += hingeSourceCost;
    }

    private void addToHingePriceWoTax(double hingeCostWoTax) {
        if (hingeCostWoTax == 0) return;
        this.productHingePriceWoTax += hingeCostWoTax;
    }

    private void addToHingePrice(double hingeCost) {
        if (hingeCost == 0) return;
        this.productHingePrice += hingeCost;
    }

    private void addToHandleAndKnobSourceCost(double handleandKnobSourceCost) {
        if (handleandKnobSourceCost == 0) return;
        this.productHandleAndKnobSourceCost += handleandKnobSourceCost;
    }

    private void addToHandleAndKnobPriceWoTax(double handleandKnobCostWoTax) {
        if (handleandKnobCostWoTax == 0) return;
        this.productHandleAndKnobPriceWoTax += handleandKnobCostWoTax;
    }

    private void addToHandleAndKnobPrice(double handleandKnobCost) {
        if (handleandKnobCost == 0) return;
        this.productHandleAndKnobPrice += handleandKnobCost;
    }

    private void addToAccessorySourceCost(double accessorySourceCost) {
        if (accessorySourceCost == 0) return;
        this.productAccessorySourceCost += accessorySourceCost;
    }

    private void addToAccessoryPriceWoTax(double accessoryCostWoTax) {
        if (accessoryCostWoTax == 0) return;
        this.productAccessoryPriceWoTax += accessoryCostWoTax;
    }

    private void addToAccessoryPrice(double accessoryCost) {
        if (accessoryCost == 0) return;
        this.productAccessoryPrice += accessoryCost;
    }

    private void addToHardwareSourceCost(double hardwareSourceCost) {
        if (hardwareSourceCost == 0) return;
        this.productHardwareSourceCost += hardwareSourceCost;
    }

    private void addToHardwarePriceWoTax(double hardwareCostWoTax) {
        if (hardwareCostWoTax == 0) return;
        this.productHardwarePriceWoTax += hardwareCostWoTax;
    }

    private void addToHardwarePrice(double hardwareCost) {
        if (hardwareCost == 0) return;
        this.productHardwarePrice += hardwareCost;
    }

    private void addToLabourSourceCost(double labourSourceCost) {
        if (labourSourceCost == 0) return;
        this.productLabourSourceCost += labourSourceCost;
    }

    private void addToLabourPriceWoTax(double labourCostWoTax) {
        if (labourCostWoTax == 0) return;
        this.productLabourPriceWoTax += labourCostWoTax;
    }

    private void addToLabourPrice(double labourCost) {
        if (labourCost == 0) return;
        this.productLabourPrice += labourCost;
    }

    private void addToLConnectorSourceCost(double lConnectorSourceCost) {
        if (lConnectorSourceCost == 0) return;
        this.productLConnectorSourceCost += lConnectorSourceCost;
    }

    private void addToLConnectorPriceWoTax(double lConnectorCostWoTax) {
        if (lConnectorCostWoTax == 0) return;
        this.productLConnectorPriceWoTax += lConnectorCostWoTax;
    }

    private void addToLConnectorPrice(double lConnectorPrice) {
        if (lConnectorPrice == 0) return;
        this.productLConnectorPrice += lConnectorPrice;
    }

    private void addToProductShutterCost(double shutterSourceCost) {
        if (shutterSourceCost == 0 ) return;
        this.productShutterSourceCost += shutterSourceCost;
    }

    private void addToProductShutterPriceWoTax(double shutterCostWoTax) {
        if (shutterCostWoTax == 0) return;
        this.productShutterCostWoTax += shutterCostWoTax;
    }

    private void addToProductShutterPrice(double shutterCost) {
        if (shutterCost == 0) return;
        this.productShutterPrice += shutterCost;
    }

    private void addToProductCarcassCost(double carcassSourceCost) {
        if (carcassSourceCost == 0) return;
        productCarcassSourceCost += carcassSourceCost;
    }

    private void addToProductCarcassPriceWoTax(double carcassCostWoTax) {
        if (carcassCostWoTax == 0) return;
        productCarcassPriceWoTax += carcassCostWoTax;
    }


    private void addToProductCarcassPrice(double carcassCost) {
        if (carcassCost == 0) return;
        productCarcassPrice += carcassCost;
    }

    public double getProductProfit()
    {
        productProfit = this.productPriceWoTax - this.productSourceCost;
        return productProfit;
    }

    public double getProductMargin()
    {
        if (this.productProfit == 0 || this.productPriceWoTax == 0)
        {
            return this.productMargin;
        }
        else {
            this.productMargin = this.productProfit / this.productPriceWoTax;
            return this.productMargin * 100;
        }
    }

    public double getProductPrice()
    {
        return this.productPrice;
    }

    public double getProductPriceAfterDiscount()
    {
        this.productPriceAfterDiscount = this.productPrice - (this.productPrice * this.discountPercentage);
        return this.productPriceAfterDiscount;
    }

    public double getProductSourceCost()
    {
        this.productSourceCost = this.productSourceCost - (this.productSourceCost * this.discountPercentage);
        return this.productSourceCost;
    }

    public double getProductPriceWoTax()
    {
        this.productPriceWoTax = this.productPriceWoTax - (this.productPriceWoTax * this.discountPercentage);
        return productPriceWoTax;
    }

    public double getWoodWorkPrice()
    {
        this.woodWorkPrice = productCarcassPrice + productShutterPrice;
        return this.woodWorkPrice;
    }

    public double getWoodWorkPriceAfterDiscount()
    {
        this.woodWorkPriceAfterDiscount = this.woodWorkPrice - (woodWorkPrice * this.discountPercentage);
        return this.woodWorkPriceAfterDiscount;
    }

    public double getWoodWorkPriceWoTax()
    {
        this.woodWorkPriceWoTax = productCarcassPriceWoTax + productShutterCostWoTax;
        this.woodWorkPriceWoTax = this.woodWorkPriceWoTax - (woodWorkPriceWoTax * this.discountPercentage);
        return this.woodWorkPriceWoTax;
    }

    public double getWoodWorkSourceCost()
    {
        this.woodWorkSourceCost = productCarcassSourceCost + productShutterSourceCost;
        return this.woodWorkSourceCost;
    }

    public double getWoodWorkProfit()
    {
        this.woodWorkProfit = this.woodWorkPriceWoTax - this.woodWorkSourceCost;
        return this.woodWorkProfit;
    }

    public double getWoodWorkMargin()
    {
        if (this.woodWorkProfit == 0 || this.woodWorkPriceWoTax == 0)
        {
            return this.woodWorkMargin;
        }
        else {

            this.woodWorkMargin = this.woodWorkProfit / this.woodWorkPriceWoTax;
            return this.woodWorkMargin * 100;
        }
    }

    public double getHardwarePrice()
    {
        return this.productHardwarePrice;
    }

    public double getHardwarePriceAfterDiscount()
    {
        this.hardwarePriceAfterDiscount = this.productHardwarePrice - (this.productHardwarePrice * this.discountPercentage);
        return this.hardwarePriceAfterDiscount;
    }

    public double getProductHardwarePriceWoTax()
    {
        this.productHardwarePriceWoTax = this.productHardwarePriceWoTax - (productHardwarePriceWoTax * this.discountPercentage);
        return this.productHardwarePriceWoTax;
    }

    public double getProductHardwareSourceCost()
    {
        return this.productHardwareSourceCost;
    }

    public double getProductHardwareProfit()
    {
        this.productHardwareProfit = this.productHardwarePriceWoTax - this.productHardwareSourceCost;
        return this.productHardwareProfit;
    }

    public double getProductHardwareMargin()
    {
        if (this.productHardwareProfit == 0 || this.productHardwarePriceWoTax == 0)
        {
            return this.productHardwareMargin;
        }
        else {
            this.productHardwareMargin = this.productHardwareProfit / this.productHardwarePriceWoTax;
            return this.productHardwareMargin * 100;
        }
    }

    public double getProductAccessoryPrice()
    {
        return this.productAccessoryPrice;
    }

    public double getProductAccessoryPriceAfterDiscount()
    {
        this.productAccessoryPriceAfterDiscount = this.productAccessoryPrice - (this.productAccessoryPrice * this.discountPercentage);
        return this.productAccessoryPriceAfterDiscount;
    }

    public double getProductAccessoryPriceWoTax()
    {
        java.util.Date date = proposalHeader.getCreatedOn();
        java.util.Date currentDate = new Date(117, 3, 20, 0, 0, 00);
        if (date.after(currentDate)) {

           return this.productAccessoryPriceWoTax = this.productAccessoryPriceWoTax - (productAccessoryPriceWoTax * this.discountPercentage);
        }
        else
        {
            return this.productAccessoryPriceWoTax;
        }
    }

    public double getProductAccessorySourceCost()
    {
        return this.productAccessorySourceCost;
    }

    public double getProductAccessoryProfit()
    {
        this.productAccessoryProfit = this.productAccessoryPriceWoTax - this.productAccessorySourceCost;
        return this.productAccessoryProfit;
    }

    public double getProductAccessoryMargin()
    {
        if (this.productAccessoryProfit == 0 || this.productAccessoryPriceWoTax == 0)
        {
            return this.productAccessoryMargin;
        }
        else {
            this.productAccessoryMargin = this.productAccessoryProfit / this.productAccessoryPriceWoTax;
            return this.productHardwareMargin * 100;
        }
    }


    public double getProductHandleAndKnobPrice()
    {
        return this.productHandleAndKnobPrice;
    }

    public double getProductHandleAndKnobPriceAfterDiscount()
    {
        this.productHandleAndKnobPriceAfterDiscount = this.productHandleAndKnobPrice - (this.productHandleAndKnobPrice * this.discountPercentage);
        return this.productHandleAndKnobPriceAfterDiscount;
    }

    public double getProductHandleAndKnobPriceWoTax()
    {
        this.productHandleAndKnobPriceWoTax = this.productHandleAndKnobPriceWoTax - (productHandleAndKnobPriceWoTax * this.discountPercentage);
        return this.productHandleAndKnobPriceWoTax;
    }

    public double getProductHandleAndKnobSourceCost()
    {
        return this.productHandleAndKnobSourceCost;
    }

    public double getProductHandleAndKnobProfit()
    {
        this.productHandleAndKnobProfit = this.productHandleAndKnobPriceWoTax - this.productHandleAndKnobSourceCost;
        return this.productHandleAndKnobProfit;
    }

    public double getProductHandleAndKnobMargin()
    {
        if (this.productHandleAndKnobProfit == 0 || this.productHandleAndKnobPriceWoTax == 0)
        {
            return this.productHandleAndKnobMargin;
        }
        else {
            this.productHandleAndKnobMargin = this.productHandleAndKnobProfit / this.productHandleAndKnobPriceWoTax;
            return this.productHandleAndKnobMargin * 100;
        }
    }

    public double getProductHingePrice()
    {
        return this.productHingePrice;
    }

    public double getProductHingePriceAfterDiscount()
    {
        this.productHingePriceAfterDiscount = this.productHingePrice - (this.productHingePrice * this.discountPercentage);
        return this.productHingePriceAfterDiscount;
    }

    public double getProductHingePriceWoTax()
    {
        this.productHingePriceWoTax = this.productHingePriceWoTax - (productHingePriceWoTax * this.discountPercentage);
        return this.productHingePriceWoTax;
    }

    public double getProductHingeSourceCost()
    {
        return this.productHingeSourceCost;
    }

    public double getProductHingeProfit()
    {
        this.productHingeProfit = this.productHingePriceWoTax - this.productHingeSourceCost;
        return this.productHingeProfit;
    }

    public double getProductHingeMargin()
    {
        if (this.productHingeProfit == 0 || this.productHingePriceWoTax == 0)
        {
            return this.productHingeMargin;
        }
        else {
            this.productHingeMargin = this.productHingeProfit / this.productHingePriceWoTax;
            return this.productHingeMargin * 100;
        }
    }

    public double getProductLabourPrice()
    {
        return this.productLabourPrice;
    }

    public double getProductLabourPriceAfterDiscount()
    {
        this.productLabourPriceAfterDiscount = this.productLabourPrice - (this.productHingePrice * this.discountPercentage);
        return this.productLabourPriceAfterDiscount;    }

    public double getProductLabourPriceWoTax()
    {
        this.productLabourPriceWoTax = this.productLabourPriceWoTax - (productLabourPriceWoTax * this.discountPercentage);
        return this.productLabourPriceWoTax;
    }

    public double getProductLabourSourceCost()
    {
        return this.productLabourSourceCost;
    }

    public double getProductLabourProfit()
    {
        this.productLabourProfit = this.productLabourPriceWoTax - this.productLabourSourceCost;
        return this.productLabourProfit;
    }

    public double getProductLabourMargin()
    {
        if (this.productLabourProfit == 0 || this.productLabourPriceWoTax == 0)
        {
            return this.productLabourMargin;
        }
        else {

            this.productLabourMargin = this.productLabourProfit / this.productLabourPriceWoTax;
            return this.productLabourMargin * 100;
        }
    }

    public double getProductAreainSqft()
    {
        return this.productAreainSqft;
    }

    public ProductLineItem getProductLineItem()
    {
        return this.productLineItem;
    }

    public int getStdModuleCount() {
        return this.stdModuleCount;
    }

    public double getStdModulePrice() {
        return this.stdModulePrice;
    }

    public int getNStdModuleCount() {
        return this.nStdModuleCount;
    }

    public double getNStdModulePrice() {
        return this.nStdModulePrice;
    }

    public int getHikeModuleCount() {
        return this.hikeModuleCount;
    }

    public double getHikeModulePrice()
    {
        return this.hikeModulePrice;
    }

    public double getLConnectorPrice() {
        return this.productLConnectorPrice;
    }

    public double getLConnectorWoTax() {
        return this.productLConnectorPriceWoTax;
    }

    public double getLConnectorSourceCost() {
        return this.productLConnectorSourceCost;
    }

    public double getLConnectorPriceAfterDiscount()
    {
        this.productLConnectorPriceAfterDiscount = this.productLConnectorPrice - (this.productLConnectorPrice * this.discountPercentage);
        return this.productLConnectorPriceAfterDiscount;    }

    public double getLConnectorProfit() {

        this.productLConnectorProfit = this.productLConnectorPriceWoTax - this.productLConnectorSourceCost;
        return this.productLConnectorProfit;
    }

    public double getLConnectorMargin() {

        if (this.productLConnectorProfit == 0 || this.productLConnectorPriceWoTax == 0)
        {
            return this.productLConnectorMargin;
        }
        else
        {
            this.productLConnectorMargin = this.productLConnectorProfit / this.productLConnectorPriceWoTax;
            return this.productLConnectorMargin*100;
        }
    }

    @Override
    public String toString() {
        return "ProductPriceHolder{" +
                "modulePriceHolders=" + modulePriceHolders +
                ", productLineItem=" + productLineItem +
                ", proposalVersion=" + proposalVersion +
                ", errors=" + errors +
                ", productSourceCost=" + productSourceCost +
                ", productPriceWoTax=" + productPriceWoTax +
                ", productPrice=" + productPrice +
                ", productPriceAfterDiscount=" + productPriceAfterDiscount +
                ", productProfit=" + productProfit +
                ", productMargin=" + productMargin +
                ", productHingeSourceCost=" + productHingeSourceCost +
                ", productHingePriceWoTax=" + productHingePriceWoTax +
                ", productHingePriceAfterDiscount=" + productHingePriceAfterDiscount +
                ", productHingePrice=" + productHingePrice +
                ", productHingeProfit=" + productHingeProfit +
                ", productHingeMargin=" + productHingeMargin +
                ", productHandleAndKnobSourceCost=" + productHandleAndKnobSourceCost +
                ", productHandleAndKnobPriceWoTax=" + productHandleAndKnobPriceWoTax +
                ", productHandleAndKnobPrice=" + productHandleAndKnobPrice +
                ", productHandleAndKnobPriceAfterDiscount=" + productHandleAndKnobPriceAfterDiscount +
                ", productHandleAndKnobProfit=" + productHandleAndKnobProfit +
                ", productHandleAndKnobMargin=" + productHandleAndKnobMargin +
                ", productAccessorySourceCost=" + productAccessorySourceCost +
                ", productAccessoryPriceWoTax=" + productAccessoryPriceWoTax +
                ", productAccessoryProfit=" + productAccessoryProfit +
                ", productAccessoryMargin=" + productAccessoryMargin +
                ", productAccessoryPrice=" + productAccessoryPrice +
                ", productAccessoryPriceAfterDiscount=" + productAccessoryPriceAfterDiscount +
                ", productHardwareSourceCost=" + productHardwareSourceCost +
                ", productHardwarePriceWoTax=" + productHardwarePriceWoTax +
                ", hardwarePriceAfterDiscount=" + hardwarePriceAfterDiscount +
                ", productHardwarePrice=" + productHardwarePrice +
                ", productHardwareProfit=" + productHardwareProfit +
                ", productHardwareMargin=" + productHardwareMargin +
                ", productLabourSourceCost=" + productLabourSourceCost +
                ", productLabourPriceWoTax=" + productLabourPriceWoTax +
                ", productLabourPrice=" + productLabourPrice +
                ", productLabourPriceAfterDiscount=" + productLabourPriceAfterDiscount +
                ", productLabourProfit=" + productLabourProfit +
                ", productLabourMargin=" + productLabourMargin +
                ", productShutterSourceCost=" + productShutterSourceCost +
                ", productShutterCostWoTax=" + productShutterCostWoTax +
                ", productShutterPrice=" + productShutterPrice +
                ", productCarcassSourceCost=" + productCarcassSourceCost +
                ", productCarcassPriceWoTax=" + productCarcassPriceWoTax +
                ", productCarcassPrice=" + productCarcassPrice +
                ", woodWorkPrice=" + woodWorkPrice +
                ", woodWorkPriceAfterDiscount=" + woodWorkPriceAfterDiscount +
                ", woodWorkPriceWoTax=" + woodWorkPriceWoTax +
                ", woodWorkSourceCost=" + woodWorkSourceCost +
                ", woodWorkProfit=" + woodWorkProfit +
                ", woodWorkMargin=" + woodWorkMargin +
                ", productLConnectorSourceCost=" + productLConnectorSourceCost +
                ", productLConnectorPriceWoTax=" + productLConnectorPriceWoTax +
                ", productLConnectorPriceAfterDiscount=" + productLConnectorPriceAfterDiscount +
                ", productLConnectorPrice=" + productLConnectorPrice +
                ", productLConnectorProfit=" + productLConnectorProfit +
                ", productLConnectorMargin=" + productLConnectorMargin +
                ", productAreainSqft=" + productAreainSqft +
                ", stdModuleCount=" + stdModuleCount +
                ", nStdModuleCount=" + nStdModuleCount +
                ", hikeModuleCount=" + hikeModuleCount +
                ", stdModulePrice=" + stdModulePrice +
                ", nStdModulePrice=" + nStdModulePrice +
                ", hikeModulePrice=" + hikeModulePrice +
                ", priceDate=" + priceDate +
                ", city='" + city + '\'' +
                ", discountPercentage=" + discountPercentage +
                '}';
    }
}
