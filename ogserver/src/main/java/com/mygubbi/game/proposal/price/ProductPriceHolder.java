package com.mygubbi.game.proposal.price;


import com.mygubbi.common.StringUtils;
import com.mygubbi.game.proposal.ModuleDataService;
import com.mygubbi.game.proposal.ProductLineItem;
import com.mygubbi.game.proposal.ProductModule;
import com.mygubbi.game.proposal.model.ProposalHeader;
import com.mygubbi.game.proposal.model.ProposalVersion;
import com.mygubbi.game.proposal.model.RateCard;
import io.vertx.core.json.JsonArray;

import java.util.List;

/**
 * Created by User on 08-08-2017.
 */
public class ProductPriceHolder {

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
    private double NStdModuleCount;

    public ProductPriceHolder(ProductLineItem productLineItem, List<ModulePriceHolder> modulePriceHolders, ProposalHeader proposalHeader, ProposalVersion proposalVersion) {
        this.modulePriceHolders = modulePriceHolders;
        this.productLineItem = productLineItem;
        this.priceDate = proposalHeader.getPriceDate();
        this.city = proposalHeader.getProjectCity();
        this.discountPercentage = proposalVersion.getDiscountPercentage() / 100;
        this.proposalVersion = proposalVersion;
    }

    public void prepare()
    {
        this.calculateProductLevelPricing();
    }


    private void calculateProductLevelPricing() {

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

          /*  addToLConnectorPrice(modulePriceHolder.());
            addToLConnectorPriceWoTax(modulePriceHolder.getLabourCostWoTax());
            addToLConnectorSourceCost(modulePriceHolder.getLabourSourceCost());*/


            addToHardwarePrice(modulePriceHolder.getHardwareCost());
            addToHardwarePriceWoTax(modulePriceHolder.getHardwareCostWoTax());
            addToHardwareSourceCost(modulePriceHolder.getHardwareSourceCost());

            addToAccessoryPrice(modulePriceHolder.getAccessoryCost());
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
        productSourceCost += totalSourceCost;
    }

    private void addToTotalProductPriceWoTax(double totalCostWoTax) {
        if (totalCostWoTax == 0) return;
        productPriceWoTax += totalCostWoTax;
    }

    private void addToTotalProductPrice(double totalCost) {
        if (totalCost == 0) return;
        productPrice += totalCost;
    }

    private void addToHingeSourceCost(double hingeSourceCost) {
        if (hingeSourceCost == 0) return;
        productHingeSourceCost += hingeSourceCost;
    }

    private void addToHingePriceWoTax(double hingeCostWoTax) {
        if (hingeCostWoTax == 0) return;
        productHingePriceWoTax += hingeCostWoTax;
    }

    private void addToHingePrice(double hingeCost) {
        if (hingeCost == 0) return;
        productHingePrice += hingeCost;
    }

    private void addToHandleAndKnobSourceCost(double handleandKnobSourceCost) {
        if (handleandKnobSourceCost == 0) return;
        productHandleAndKnobSourceCost += handleandKnobSourceCost;
    }

    private void addToHandleAndKnobPriceWoTax(double handleandKnobCostWoTax) {
        if (handleandKnobCostWoTax == 0) return;
        productHandleAndKnobPriceWoTax += handleandKnobCostWoTax;
    }

    private void addToHandleAndKnobPrice(double handleandKnobCost) {
        if (handleandKnobCost == 0) return;
        productHandleAndKnobPrice += handleandKnobCost;
    }

    private void addToAccessorySourceCost(double accessorySourceCost) {
        if (accessorySourceCost == 0) return;
        productAccessorySourceCost += accessorySourceCost;
    }

    private void addToAccessoryPriceWoTax(double accessoryCostWoTax) {
        if (accessoryCostWoTax == 0) return;
        productAccessoryPriceWoTax += accessoryCostWoTax;
    }

    private void addToAccessoryPrice(double accessoryCost) {
        if (accessoryCost == 0) return;
        productAccessoryPrice += accessoryCost;
    }

    private void addToHardwareSourceCost(double hardwareSourceCost) {
        if (hardwareSourceCost == 0) return;
        productHardwareSourceCost += hardwareSourceCost;
    }

    private void addToHardwarePriceWoTax(double hardwareCostWoTax) {
        if (hardwareCostWoTax == 0) return;
        productHardwarePriceWoTax += hardwareCostWoTax;
    }

    private void addToHardwarePrice(double hardwareCost) {
        if (hardwareCost == 0) return;
        productHardwarePrice += hardwareCost;
    }

    private void addToLabourSourceCost(double labourSourceCost) {
        if (labourSourceCost == 0) return;
        productLabourSourceCost += labourSourceCost;
    }

    private void addToLabourPriceWoTax(double labourCostWoTax) {
        if (labourCostWoTax == 0) return;
        productLabourPriceWoTax += labourCostWoTax;
    }

    private void addToLabourPrice(double labourCost) {
        if (labourCost == 0) return;
        productLabourPrice += labourCost;
    }

   /* private void addToLConnectorSourceCost(double lConnectorSourceCost) {
        if (lConnectorSourceCost == 0) return;
        productLConnectorSourceCost += lConnectorSourceCost;
    }

    private void addToLConnectorPriceWoTax(double lConnectorCostWoTax) {
        if (lConnectorCostWoTax == 0) return;
        productLConnectorPriceWoTax += lConnectorCostWoTax;
    }

    private void addToLConnectorPrice(double lConnectorPrice) {
        if (lConnectorPrice == 0) return;
        productLConnectorPrice += lConnectorPrice;
    }*/

    private void addToProductShutterCost(double shutterSourceCost) {
        if (shutterSourceCost == 0 ) return;
        productShutterSourceCost += shutterSourceCost;
    }

    private void addToProductShutterPriceWoTax(double shutterCostWoTax) {
        if (shutterCostWoTax == 0) return;
        productShutterCostWoTax += shutterCostWoTax;
    }

    private void addToProductShutterPrice(double shutterCost) {
        if (shutterCost == 0) return;
        productShutterPrice += shutterCost;
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
        productMargin = this.productProfit / this.productPriceWoTax;
        return this.productMargin;
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
        return this.productSourceCost;
    }

    public double getPriceAfterDiscount()
    {
        return productPrice;
    }

    public double getProductPriceWoTax()
    {
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
        this.woodWorkMargin = this.woodWorkProfit / this.woodWorkPriceWoTax;
        return this.woodWorkMargin;
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
        this.productHardwareMargin = this.productHardwareProfit / this.productHardwarePriceWoTax;
        return this.productHardwareMargin;
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
        return this.productAccessoryPriceWoTax;
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
        this.productAccessoryMargin = this.productAccessoryMargin / this.productAccessoryPriceWoTax;
        return this.productHardwareMargin;
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
        this.productHandleAndKnobMargin = this.productHandleAndKnobProfit / this.productHandleAndKnobPriceWoTax;
        return this.productHandleAndKnobMargin;
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
        return this.productHingePriceWoTax;
    }

    public double getProductHingeSourceCost()
    {
        return this.productHingeSourceCost;
    }

    public double getProductHingeProfit()
    {
        this.productHingeProfit = this.productHingePriceWoTax - this.productHandleAndKnobSourceCost;
        return this.productHingeProfit;
    }

    public double getProductHingeMargin()
    {
        this.productHingeMargin = this.productHingeProfit / this.productHingePriceWoTax;
        return this.productHingeMargin;
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
        this.productLabourMargin = this.productLabourProfit / this.productLabourPriceWoTax;
        return this.productLabourMargin;
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
        return stdModuleCount;
    }

    public double getStdModulePrice() {
        return stdModulePrice;
    }

    public double getNStdModuleCount() {
        return nStdModuleCount;
    }

    public double getNStdModulePrice() {
        return nStdModulePrice;
    }

    public int getHikeModuleCount() {
        return hikeModuleCount;
    }

    public double getHikeModulePrice() {
        return hikeModulePrice;
    }
}