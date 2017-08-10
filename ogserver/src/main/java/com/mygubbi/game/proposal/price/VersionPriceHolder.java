package com.mygubbi.game.proposal.price;

import com.mygubbi.game.proposal.ProductAddon;
import com.mygubbi.game.proposal.ProductLineItem;
import com.mygubbi.game.proposal.model.Proposal;
import com.mygubbi.game.proposal.model.ProposalHeader;
import com.mygubbi.game.proposal.model.ProposalVersion;

import java.util.List;

/**
 * Created by User on 09-08-2017.
 */
public class VersionPriceHolder {

    private List<ProductPriceHolder> productPriceHolders ;
    private List<AddonPriceHolder> addonPriceHolders ;
    private ProposalHeader proposalHeader;
    private ProposalVersion proposalVersion;

    private double vrPrice = 0.0;
    private double vrPriceAfterDiscount = 0.0;
    private double vrPriceAfterTax = 0.0;
    private double vrCost = 0.0;
    private double vrProfit = 0.0;
    private double vrMargin = 0.0;
    private double prPrice = 0.0;
    private double prPriceAfterDiscount = 0.0;
    private double prPriceAfterTax = 0.0;
    private double prCost = 0.0;
    private double prProfit = 0.0;
    private double prMargin = 0.0;
    private double wwPrice = 0.0;
    private double wwPriceAfterDiscount = 0.0;
    private double wwPriceAfterTax = 0.0;
    private double wwCost = 0.0;
    private double wwProfit = 0.0;
    private double wwMargin = 0.0;
    private double hwPrice = 0.0;
    private double hwPriceAfterDiscount = 0.0;
    private double hwPriceAfterTax = 0.0;
    private double hwCost = 0.0;
    private double hwProfit = 0.0;
    private double hwMargin = 0.0;
    private double accPrice = 0.0;
    private double accPriceAfterDiscount = 0.0;
    private double accPriceAfterTax = 0.0;
    private double accCost = 0.0;
    private double accProfit = 0.0;
    private double accMargin = 0.0;
    private double hkPrice = 0.0;
    private double hkPriceAfterDiscount = 0.0;
    private double hkPriceAfterTax = 0.0;
    private double hkCost = 0.0;
    private double hkProfit = 0.0;
    private double hkMargin = 0.0;
    private double hingePrice = 0.0;
    private double hingePriceAfterDiscount = 0.0;
    private double hingePriceAfterTax = 0.0;
    private double hingeCost = 0.0;
    private double hingeProfit = 0.0;
    private double hingeMargin = 0.0;
    private double lcPrice = 0.0;
    private double lcPriceAfterDiscount = 0.0;
    private double lcPriceAfterTax = 0.0;
    private double lcCost = 0.0;
    private double lcProfit = 0.0;
    private double lcMargin = 0.0;
    private double laPrice = 0.0;
    private double laPriceAfterDiscount = 0.0;
    private double laPriceAfterTax = 0.0;
    private double laCost = 0.0;
    private double laProfit = 0.0;
    private double laMargin = 0.0;
    private double bpPrice = 0.0;
    private double bpPriceAfterDiscount = 0.0;
    private double bpPriceAfterTax = 0.0;
    private double bpCost = 0.0;
    private double bpProfit = 0.0;
    private double bpMargin = 0.0;
    private double svPrice = 0.0;
    private double svPriceAfterDiscount = 0.0;
    private double svPriceAfterTax = 0.0;
    private double svCost = 0.0;
    private double svProfit = 0.0;
    private double svMargin = 0.0;
    private int kitchenCount = 0;
    private int wardrobeCount = 0;
    private int nsProductCount = 0;
    private int bpCount = 0;
    private int servicesCount = 0;
    private int stdModuleCount = 0;
    private int nStdModuleCount = 0;
    private int hikeModuleCount = 0;
    private double stdModulePrice = 0;
    private double nStdModulePrice = 0;
    private double hikeModulePrice = 0;





    public VersionPriceHolder(ProposalHeader proposalHeader, List<ProductPriceHolder> productPriceHolders, List<AddonPriceHolder> addonPriceHolders, ProposalVersion proposalVersion) {
        this.proposalHeader = proposalHeader;
        this.productPriceHolders = productPriceHolders;
        this.addonPriceHolders = addonPriceHolders;
        this.proposalVersion = proposalVersion;
    }

    public void prepare()
    {
        this.setupPriceForProduct();
        this.setupPriceForAddon();
    }

    private void setupPriceForAddon() {
        for (AddonPriceHolder addonPriceHolder : addonPriceHolders)
        {
            ProductAddon productAddon = addonPriceHolder.getProductAddon();

            if (productAddon.getCategoryCode().equals("Services"))
            {
                addToServicesPrice(addonPriceHolder.getPrice());
                addToServicesPriceWithDiscount(addonPriceHolder.getPrice());
                addToServicesPriceWoTax(addonPriceHolder.getPriceWoTax());
                addToServicesSourceCost(addonPriceHolder.getSourceCost());

                servicesCount = servicesCount + 1;
            }
            else {
                addToBPPrice(addonPriceHolder.getPrice());
                addToBPPriceWithDiscount(addonPriceHolder.getPrice());
                addToBPPriceWoTax(addonPriceHolder.getPriceWoTax());
                addToBPSourceCost(addonPriceHolder.getSourceCost());

                bpCount = bpCount + 1;
            }

                addToVersionPrice(addonPriceHolder.getPrice());
                addToVersionPriceAfterDiscount(addonPriceHolder.getPrice());
                addToVersionPriceWoTax(addonPriceHolder.getPriceWoTax());
                addToVersionSourceCost(addonPriceHolder.getSourceCost());

        }
    }

    private void setupPriceForProduct() {

        for (ProductPriceHolder productPriceHolder : productPriceHolders)
        {
            ProductLineItem productLineItem = productPriceHolder.getProductLineItem();

            if (productLineItem.getProductCategory().equals("K") || productLineItem.getProductCategory().equals("Kitchen"))
            {
                kitchenCount = kitchenCount + 1;
            }
            else if (productLineItem.getProductCategory().equals("Wardrobe") || productLineItem.getProductCategory().equals("Wardrobe"))
            {
                wardrobeCount = wardrobeCount + 1;
            }
            else
            {
                nsProductCount = nsProductCount + 1;
            }

            addToStdModuleCount(productPriceHolder.getStdModuleCount());
            addToNStdModuleCount(productPriceHolder.getNStdModuleCount());
            addToHikeModuleCount(productPriceHolder.getHikeModuleCount());

            addToStdModulePrice(productPriceHolder.getStdModulePrice());
            addToNStdModulePrice(productPriceHolder.getNStdModulePrice());
            addToHikeModulePrice(productPriceHolder.getHikeModulePrice());

            addToVersionPrice(productPriceHolder.getProductPrice());
            addToVersionPriceAfterDiscount(productPriceHolder.getProductPriceAfterDiscount());
            addToVersionPriceWoTax(productPriceHolder.getProductPriceWoTax());
            addToVersionSourceCost(productPriceHolder.getProductSourceCost());

            addToProductPrice(productPriceHolder.getProductPrice());
            addToProductPriceAfterDiscount(productPriceHolder.getProductPriceAfterDiscount());
            addToProductPriceWoTax(productPriceHolder.getProductPriceWoTax());
            addToProductSourceCost(productPriceHolder.getProductSourceCost());

            addToWoodWorkPrice(productPriceHolder.getWoodWorkPrice());
            addToWoodWorkPriceAfterDiscount(productPriceHolder.getWoodWorkPriceAfterDiscount());
            addToWoodWorkPriceWoTax(productPriceHolder.getWoodWorkPriceWoTax());
            addToWoodWorkSourceCost(productPriceHolder.getWoodWorkSourceCost());

            addToHardwarePrice(productPriceHolder.getHardwarePrice());
            addToHardwarePriceAfterDiscount(productPriceHolder.getHardwarePriceAfterDiscount());
            addToHardwarePriceWoTax(productPriceHolder.getProductHardwarePriceWoTax());
            addToHardwareSourceCost(productPriceHolder.getProductHardwareSourceCost());

            addToAccessoryPrice(productPriceHolder.getProductAccessoryPrice());
            addToAccessoryPriceAfterDiscount(productPriceHolder.getProductAccessoryPriceAfterDiscount());
            addToAccessoryPriceWoTax(productPriceHolder.getProductAccessoryPriceWoTax());
            addToAccessorySourceCost(productPriceHolder.getProductAccessorySourceCost());

            addToHandleAndKnobPrice(productPriceHolder.getProductHandleAndKnobPrice());
            addToHandleAndKnobPriceAfterDiscount(productPriceHolder.getProductHandleAndKnobPriceAfterDiscount());
            addToHandleAndKnobPriceWoTax(productPriceHolder.getProductHandleAndKnobPriceWoTax());
            addToHandleAndKnobSourceCost(productPriceHolder.getProductHandleAndKnobSourceCost());

            addToHingePrice(productPriceHolder.getProductHingePrice());
            addToHingePriceAfterDiscount(productPriceHolder.getProductHingePriceAfterDiscount());
            addToHingePriceWoTax(productPriceHolder.getProductHingePriceWoTax());
            addToHingeSourceCost(productPriceHolder.getProductHingeSourceCost());

            addToLabourPrice(productPriceHolder.getProductLabourPrice());
            addToLabourPriceAfterDiscount(productPriceHolder.getProductLabourPriceAfterDiscount());
            addToLabourPriceWoTax(productPriceHolder.getProductLabourPriceWoTax());
            addToLabourSourceCost(productPriceHolder.getProductLabourSourceCost());

        }
    }

    private void addToHikeModulePrice(double hikeModulePrice) {
        if (hikeModulePrice == 0) return;
        this.hikeModulePrice += hikeModulePrice;
    }

    private void addToNStdModulePrice(double nStdModulePrice) {
        if (nStdModulePrice == 0) return;
        this.nStdModulePrice += nStdModulePrice;
    }

    private void addToStdModulePrice(double stdModulePrice) {
        if (stdModulePrice == 0) return;
        this.stdModulePrice += stdModulePrice;
    }



    private void addToHikeModuleCount(int hikeModuleCount) {
        if (hikeModuleCount == 0) return;
        this.hikeModuleCount += hikeModuleCount;
    }

    private void addToNStdModuleCount(double nStdModuleCount) {
        if (nStdModuleCount == 0) return;
        this.nStdModuleCount += nStdModuleCount;
    }

    private void addToStdModuleCount(int stdModuleCount) {
        if (stdModuleCount == 0) return;
        this.stdModuleCount += stdModuleCount;
    }

    private void addToVersionPrice(double price)
    {
        if (price == 0) return;
        this.vrPrice += price;
    }

    private void addToVersionPriceAfterDiscount(double priceAfterDiscount)
    {
        if (priceAfterDiscount == 0) return;
        this.vrPriceAfterDiscount += priceAfterDiscount;
    }

    private void addToVersionPriceWoTax(double priceWoTax)
    {
        if (priceWoTax ==0) return;
        this.vrPriceAfterTax += priceWoTax;
    }

    private void addToVersionSourceCost(double sourceCost)
    {
        if (sourceCost == 0) return;
        this.vrCost += sourceCost;
    }

    private void addToProductPrice(double price)
    {
        if (price == 0) return;
        this.prPrice += price;
    }

    private void addToProductPriceAfterDiscount(double priceAfterDiscount)
    {
        if (priceAfterDiscount == 0) return;
        this.prPriceAfterDiscount += priceAfterDiscount;
    }

    private void addToProductPriceWoTax(double priceWoTax)
    {
        if (priceWoTax ==0) return;
        this.prPriceAfterTax += priceWoTax;
    }

    private void addToProductSourceCost(double sourceCost)
    {
        if (sourceCost == 0) return;
        this.prCost += sourceCost;
    }

    private void addToWoodWorkPrice(double price)
    {
        if (price == 0) return;
        this.wwPrice += price;
    }

    private void addToWoodWorkPriceAfterDiscount(double priceAfterDiscount)
    {
        if (priceAfterDiscount == 0) return;
        this.wwPriceAfterDiscount += priceAfterDiscount;
    }

    private void addToWoodWorkPriceWoTax(double priceWoTax)
    {
        if (priceWoTax ==0) return;
        this.wwPriceAfterTax += priceWoTax;
    }

    private void addToWoodWorkSourceCost(double sourceCost)
    {
        if (sourceCost == 0) return;
        this.wwCost += sourceCost;
    }

    private void addToHardwarePrice(double price)
    {
        if (price == 0) return;
        this.hwPrice += price;
    }

    private void addToHardwarePriceAfterDiscount(double priceAfterDiscount)
    {
        if (priceAfterDiscount == 0) return;
        this.hwPriceAfterDiscount += priceAfterDiscount;
    }

    private void addToHardwarePriceWoTax(double priceWoTax)
    {
        if (priceWoTax ==0) return;
        this.hwPriceAfterTax += priceWoTax;
    }

    private void addToHardwareSourceCost(double sourceCost)
    {
        if (sourceCost == 0) return;
        this.hwCost += sourceCost;
    }

    private void addToAccessoryPrice(double price)
    {
        if (price == 0) return;
        this.accPrice += price;
    }

    private void addToAccessoryPriceAfterDiscount(double priceAfterDiscount)
    {
        if (priceAfterDiscount == 0) return;
        this.accPriceAfterDiscount += priceAfterDiscount;
    }

    private void addToAccessoryPriceWoTax(double priceWoTax)
    {
        if (priceWoTax ==0) return;
        this.accPriceAfterTax += priceWoTax;
    }

    private void addToAccessorySourceCost(double sourceCost)
    {
        if (sourceCost == 0) return;
        this.accCost += sourceCost;
    }

    private void addToHandleAndKnobPrice(double price)
    {
        if (price == 0) return;
        this.hkPrice += price;
    }

    private void addToHandleAndKnobPriceAfterDiscount(double priceAfterDiscount)
    {
        if (priceAfterDiscount == 0) return;
        this.hkPriceAfterDiscount += priceAfterDiscount;
    }

    private void addToHandleAndKnobPriceWoTax(double priceWoTax)
    {
        if (priceWoTax ==0) return;
        this.hkPriceAfterTax += priceWoTax;
    }

    private void addToHandleAndKnobSourceCost(double sourceCost)
    {
        if (sourceCost == 0) return;
        this.hkCost += sourceCost;
    }

    private void addToHingePrice(double price)
    {
        if (price == 0) return;
        this.hingePrice += price;
    }

    private void addToHingePriceAfterDiscount(double price)
    {
        if (price == 0) return;
        this.hingePriceAfterDiscount += price;
    }

    private void addToHingePriceWoTax(double priceWoTax)
    {
        if (priceWoTax ==0) return;
        this.hingePriceAfterTax += priceWoTax;
    }

    private void addToHingeSourceCost(double sourceCost)
    {
        if (sourceCost == 0) return;
        this.hingeCost += sourceCost;
    }

    private void addToLConnectorPrice(double price)
    {
        if (price == 0) return;
        this.lcPrice += price;
    }

    private void addToLConnectorPriceWoTax(double priceWoTax)
    {
        if (priceWoTax ==0) return;
        this.lcPriceAfterTax += priceWoTax;
    }

    private void addToLConnectorSourceCost(double sourceCost)
    {
        if (sourceCost == 0) return;
        this.lcCost += sourceCost;
    }

    private void addToLabourPrice(double price)
    {
        if (price == 0) return;
        this.laPrice += price;
    }

    private void addToLabourPriceAfterDiscount(double price)
    {
        if (price == 0) return;
        this.laPriceAfterDiscount += price;
    }

    private void addToLabourPriceWoTax(double priceWoTax)
    {
        if (priceWoTax ==0) return;
        this.laPriceAfterTax += priceWoTax;
    }

    private void addToLabourSourceCost(double sourceCost)
    {
        if (sourceCost == 0) return;
        this.laCost += sourceCost;
    }

    private void addToBPPrice(double price)
    {
        if (price == 0) return;
        this.bpPrice += price;
    }

    private void addToBPPriceWithDiscount(double price)
    {
        if (price == 0) return;
        this.bpPriceAfterDiscount += price;
    }

    private void addToBPPriceWoTax(double priceWoTax)
    {
        if (priceWoTax ==0) return;
        this.bpPriceAfterTax += priceWoTax;
    }

    private void addToBPSourceCost(double sourceCost)
    {
        if (sourceCost == 0) return;
        this.bpCost += sourceCost;
    }

    private void addToServicesPrice(double price)
    {
        if (price == 0) return;
        this.svPrice += price;
    }

    private void addToServicesPriceWithDiscount(double price)
    {
        if (price == 0) return;
        this.svPriceAfterDiscount += price;
    }

    private void addToServicesPriceWoTax(double priceWoTax)
    {
        if (priceWoTax ==0) return;
        this.svPriceAfterTax += priceWoTax;
    }

    private void addToServicesSourceCost(double sourceCost)
    {
        if (sourceCost == 0) return;
        this.svCost += sourceCost;
    }


    public double getPrice() {
        return this.vrPrice;
    }

    public double getPriceAfterDiscount() {
        return this.vrPriceAfterDiscount;
    }

    public double getPriceWotax() {
        return this.vrPriceAfterTax;
    }

    public double getSourceCost() {
        return this.vrCost;
    }

    public double getProfit() {
        return this.vrProfit;
    }

    public double getMargin() {
        return this.vrMargin;
    }

    public double getProductPrice() {
        return this.prPrice;
    }

    public double getProductPriceAfterDiscount() {
        return this.prPriceAfterDiscount;
    }

    public double getProductPriceWotax() {
        return this.prPriceAfterTax;
    }

    public double getProductSourceCost() {
        return this.prCost;
    }

    public double getProductProfit() {
        return this.prProfit;
    }

    public double getProductMargin() {
        return this.prMargin;
    }

    public double getWoodworkPrice() {
        return this.wwPrice;
    }

    public double getWoodworkPriceAfterDiscount() {
        return this.wwPriceAfterDiscount;
    }

    public double getWoodworkPriceWotax() {
        return this.wwPriceAfterTax;
    }

    public double getWoodworkSourceCost() {
        return this.wwCost;
    }

    public double getWoodworkProfit() {
        return this.wwProfit;
    }

    public double getWoodworkMargin() {
        return this.wwMargin;
    }

    public double getHardwarePrice() {
        return this.hwPrice;
    }

    public double getHardwarePriceAfterDiscount() {
        return this.hwPriceAfterDiscount;
    }

    public double getHardwarePriceWotax() {
        return this.hwPriceAfterTax;
    }

    public double getHardwareSourceCost() {
        return this.hwCost;
    }

    public double getHardwareProfit() {
        return this.hwProfit;
    }

    public double getHardwareMargin() {
        return this.hwMargin;
    }

    public double getAccessoryPrice() {
        return this.accPrice;
    }

    public double getAccessoryPriceAfterDiscount() {
        return this.accPriceAfterDiscount;
    }

    public double getAccessoryPriceWotax() {
        return this.accPriceAfterTax;
    }

    public double getAccessorySourceCost() {
        return this.accCost;
    }

    public double getAcccessoryProfit() {
        return this.accProfit;
    }

    public double getAccessoryMargin() {
        return this.accMargin;
    }

    public double getHKPrice() {
        return this.hkPrice;
    }

    public double getHKPriceAfterDiscount() {
        return this.hkPriceAfterDiscount;
    }

    public double getHKPriceWotax() {
        return this.hkPriceAfterTax;
    }

    public double getHKSourceCost() {
        return this.hkCost;
    }

    public double getHKProfit() {
        return this.hkProfit;
    }

    public double getHKMargin() {
        return this.hkMargin;
    }

    public double getHingePrice() {
        return this.hingePrice;
    }

    public double getHingePriceAfterDiscount() {
        return this.hingePriceAfterDiscount;
    }

    public double getHingePriceWotax() {
        return this.hingePriceAfterTax;
    }

    public double getHingeSourceCost() {
        return this.hingeCost;
    }

    public double getHingeProfit() {
        return this.hingeProfit;
    }

    public double getHingeMargin() {
        return this.hingeMargin;
    }

    public double getLabourPrice() {
        return this.laPrice;
    }

    public double getLabourPriceAfterDiscount() {
        return this.laPriceAfterDiscount;
    }

    public double getLabourPriceWotax() {
        return this.laPriceAfterTax;
    }

    public double getLabourSourceCost() {
        return this.laCost;
    }

    public double getLabourProfit() {
        return this.laProfit;
    }

    public double getLabourMargin() {
        return this.laMargin;
    }

    public double getLCPrice() {
        return this.lcPrice;
    }

    public double getLCPriceAfterDiscount() {
        return this.lcPriceAfterDiscount;
    }

    public double getLCPriceWotax() {
        return this.lcPriceAfterTax;
    }

    public double getLCSourceCost() {
        return this.lcCost;
    }

    public double getLCProfit() {
        return this.lcProfit;
    }

    public double getLCMargin() {
        return this.lcMargin;
    }

    public double getBPPrice() {
        return this.bpPrice;
    }

    public double getBPPriceAfterDiscount() {
        return this.bpPriceAfterDiscount;
    }

    public double getBPPriceWotax() {
        return this.bpPriceAfterTax;
    }

    public double getBPSourceCost() {
        return this.bpCost;
    }

    public double getBPProfit() {
        return this.bpProfit;
    }

    public double getBPMargin() {
        return this.bpMargin;
    }

    public double getSVPrice() {
        return this.svPrice;
    }

    public double getSVPriceAfterDiscount() {
        return this.svPriceAfterDiscount;
    }

    public double getSVPriceWotax() {
        return this.svPriceAfterTax;
    }

    public double getSVSourceCost() {
        return this.svCost;
    }

    public double getSVProfit() {
        return this.svProfit;
    }

    public double getSVMargin() {
        return this.svMargin;
    }

    public int getKitchenCount() {
        return this.kitchenCount;
    }

    public int getWardrobeCount() {
        return this.wardrobeCount;
    }

    public int getNSProductCount() {
        return this.nsProductCount;
    }

    public int getBPCount() {
        return this.bpCount;
    }

    public int getServicesCount() {
        return this.servicesCount;
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

    public double getHikeModulePrice() {
        return this.hikeModulePrice;
    }
}