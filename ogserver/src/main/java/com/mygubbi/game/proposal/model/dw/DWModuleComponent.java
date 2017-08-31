package com.mygubbi.game.proposal.model.dw;

import com.mygubbi.game.proposal.ModuleDataService;
import com.mygubbi.game.proposal.ProductLineItem;
import com.mygubbi.game.proposal.ProductModule;
import com.mygubbi.game.proposal.model.ProposalHeader;
import com.mygubbi.game.proposal.model.ProposalVersion;
import com.mygubbi.game.proposal.model.RateCard;
import com.mygubbi.game.proposal.model.ShutterFinish;
import com.mygubbi.game.proposal.price.PanelComponent;
import com.mygubbi.game.proposal.price.RateCardService;
import io.vertx.core.json.JsonObject;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Objects;

/**
 * Created by User on 30-08-2017.
 */
public class DWModuleComponent extends JsonObject {

    private static final String WARDROBE = "Wardrobe";


    public static final String ID="id";
    public static final String PROPOSALID="proposalId";
    public static final String QUOTENO="quoteNo";
    public static final String CRMID="crmId";
    public static final String PROPOSAL_TITLE="proposalTitle";
    public static final String VERSION="version";
    public static final String PRICEDATE="priceDate";
    public static final String BUSINESSDATE="businessDate";
    public static final String REGION="region";
    public static final String STATUS="status";
    public static final String SPACETYPE="spaceType";
    public static final String ROOM="room";
    public static final String PRID="prId";
    public static final String PRTITLE="prTitle";
    public static final String PRCATEGORY="prCategory";
    public static final String MODULE_TYPE="moduleType";
    public static final String MODULESEQ="moduleSeq";
    public static final String MODULECODE="moduleCode";
    public static final String MODULECATEGORY="moduleCategory";
    public static final String ACC_PACK_CODE="accPackCode";
    public static final String CARCASS="carcass";
    public static final String FINISH="finish";
    public static final String FINISH_MATERIAL="finishMaterial";
    public static final String DISCOUNT_AMOUNT="discountAmount";
    public static final String DISCOUNT_AMOUNT_PERC="discountPercentage";
    public static final String WIDTH="width";
    public static final String DEPTH="depth";
    public static final String HEIGHT="height";
    public static final String PANEL_AREA="panelArea";
    public static final String COMPONENT_TYPE="compType";
    public static final String COMPONENT_CODE="compCode";
    public static final String COMPONENT_QTY="compQty";
    public static final String COMPONENT_TITLE="compTitle";
    public static final String COMPONENT_PRICE="compPrice";
    public static final String COMPONENT_PRICE_AFTER_DISCOUNT="compPriceAfterDiscount";
    public static final String COMPONENT_PRICE_WO_TAX="compWoTax";
    public static final String COMPONENT_COST="compCost";
    public static final String COMPONENT_PROFIT="compProfit";
    public static final String COMPONENT_MARGIN="compMargin";


    public DWModuleComponent(JsonObject json) {
        super(json.getMap());
    }

    public DWModuleComponent() {}

    public int getID() {
        return this.getInteger(ID);
    }

    public DWModuleComponent setId(int id)
    {
        put(ID,id);
        return this;
    }

    public int getProposalID() {
        return this.getInteger(PROPOSALID);
    }

    public DWModuleComponent setProposalId(int proposalId)
    {
        put(PROPOSALID,proposalId);
        return this;
    }

    public String getQuoteno() {
        return this.getString(QUOTENO);
    }

    public DWModuleComponent setQuoteNo(String quoteNo)
    {
        put(QUOTENO,quoteNo);
        return this;
    }

    public String getCrmId() {
        return this.getString(CRMID);
    }

    public DWModuleComponent setCrmId(String crmId)
    {
        put(CRMID,crmId);
        return this;
    }

    public String getProposalTitle() {
        return this.getString(PROPOSAL_TITLE);
    }

    public DWModuleComponent setProposalTitle(String proposalTitle)
    {
        put(PROPOSAL_TITLE,proposalTitle);
        return this;
    }

    public String getVersion() {
        return this.getString(VERSION);
    }

    public DWModuleComponent setVersion(String version)
    {
        put(VERSION,version);
        return this;
    }

    public Date getPriceDate() {
        return (Date) this.getValue(PRICEDATE);
    }


    public DWModuleComponent setPriceDate(Date priceDate)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String format = formatter.format(priceDate);
        this.put(PRICEDATE, format);
        return this;
    }

    public Date getBusinessDate() {
        return (Date) this.getValue(PRICEDATE);
    }

    public DWModuleComponent setBusinessDate(Date businessDate)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String format = formatter.format(businessDate);
        this.put(BUSINESSDATE, format);
        return this;
    }
    public String getRegion() {
        return this.getString(REGION);
    }

    public DWModuleComponent setRegion(String region)
    {
        put(REGION,region);
        return this;
    }

    public String getStatus() {
        return this.getString(STATUS);
    }

    public DWModuleComponent setStatus(String status)
    {
        put(STATUS,status);
        return this;
    }


    public String getSpacetype() {
        return this.getString(SPACETYPE);
    }

    public DWModuleComponent setSpaceType(String spaceType)
    {
        put(SPACETYPE,spaceType);
        return this;
    }

    public String getRoom() {
        return this.getString(ROOM);
    }

    public DWModuleComponent setRoom(String room)
    {
        put(ROOM,room);
        return this;
    }

    public int getPrId() {
        return this.getInteger(PRID);
    }

    public DWModuleComponent setPrId(int prId)
    {
        put(PRID,prId);
        return this;
    }



    public String getPrTitle() {
        return this.getString(PRTITLE);
    }

    public DWModuleComponent setPrTitle(String prTitle)
    {
        put(PRTITLE,prTitle);
        return this;
    }



    public String getProductCategory() {
        return this.getString(PRCATEGORY);
    }

    public DWModuleComponent setProductCategory(String productCategory)
    {
        put(PRCATEGORY,productCategory);
        return this;
    }

    public static String getModuleType() {
        return MODULE_TYPE;
    }

    public DWModuleComponent setModuleType(String moduleType)
    {
        put(MODULE_TYPE,moduleType);
        return this;
    }

    public int getModuleSeq() {
        return this.getInteger(MODULESEQ);
    }

    public DWModuleComponent setModuleSeq(int moduleSeq)
    {
        put(MODULESEQ,moduleSeq);
        return this;
    }

    public String getModuleCode() {
        return this.getString(MODULECODE);
    }

    public DWModuleComponent setModuleCode(String moduleCode)
    {
        put(MODULECODE,moduleCode);
        return this;
    }

    public String getModuleCategory() {
        return this.getString(MODULECATEGORY);
    }

    public DWModuleComponent setModuleCategory(String moduleCategory)
    {
        put(MODULECATEGORY,moduleCategory);
        return this;
    }

    public String getAccPackCode() {
        return this.getString(ACC_PACK_CODE);
    }

    public DWModuleComponent setAccPackCode(String accPackCode)
    {
        put(ACC_PACK_CODE,accPackCode);
        return this;
    }

    public double getWidth() {
        return this.getDouble(WIDTH);
    }

    public DWModuleComponent setWidth(double width)
    {
        put(WIDTH,width);
        return this;
    }

    public double getDepth() {
        return this.getDouble(DEPTH);
    }

    public DWModuleComponent setDepth(double depth)
    {
        put(DEPTH,depth);
        return this;
    }

    public double getHeight() {
        return this.getDouble(HEIGHT);
    }

    public DWModuleComponent setHeight(double height)
    {
        put(HEIGHT,height);
        return this;
    }

    public String getCarcass() {
        return this.getString(CARCASS);
    }

    public DWModuleComponent setCarcass(String carcass)
    {
        put(CARCASS,carcass);
        return this;
    }

    public String getFinish() {
        return this.getString(FINISH);
    }

    public DWModuleComponent setFinish(String finish)
    {
        put(FINISH,finish);
        return this;
    }

    public String getFinishMaterial() {
        return this.getString(FINISH_MATERIAL);
    }

    public DWModuleComponent setFinishMaterial(String finishMaterial)
    {
        put(FINISH_MATERIAL,finishMaterial);
        return this;
    }

    public String getDiscountAmount() {
        return this.getString(DISCOUNT_AMOUNT);
    }

    public DWModuleComponent setDiscountAmount(double price) {
        this.put(DISCOUNT_AMOUNT, price);
        return this;
    }

    public double getDiscountPercentage() {
        return this.getDouble(DISCOUNT_AMOUNT_PERC);
    }

    public DWModuleComponent setDiscountAmountPerc(double price) {
        this.put(DISCOUNT_AMOUNT_PERC, price);
        return this;
    }


    public double getPanelArea() {
        return this.getDouble(PANEL_AREA);
    }

    public DWModuleComponent setPanelArea(double panelArea) {
        this.put(PANEL_AREA, panelArea);
        return this;
    }



    public String getComponentType() {
        return COMPONENT_TYPE;
    }

    public DWModuleComponent setComponentType(String componentType) {
        this.put(COMPONENT_TYPE, componentType);
        return this;
    }

    public String getComponentCode() {
        return this.getString(COMPONENT_CODE);
    }

    public DWModuleComponent setComponentCode(String componentCode) {
        this.put(COMPONENT_CODE,componentCode);
        return this;
    }

    public double getComponentQty() {
        return this.getDouble(COMPONENT_QTY);
    }

    public DWModuleComponent setComponentQty(double componentQty) {
        this.put(COMPONENT_QTY, componentQty);
        return this;
    }

    public String getComponentTitle() {
        return this.getString(COMPONENT_TITLE);
    }

    public DWModuleComponent setComponentTitle(String componentTitle) {
        this.put(COMPONENT_TITLE, componentTitle);
        return this;
    }

    public double getComponentPrice() {
        return this.getDouble(COMPONENT_PRICE);
    }

    public DWModuleComponent setComponentPrice(double componentPrice) {
        this.put(COMPONENT_PRICE, componentPrice);
        return this;
    }

    public double getComponentPriceAfterDiscount() {
        return this.getDouble(COMPONENT_PRICE_AFTER_DISCOUNT);
    }

    public DWModuleComponent setComponentPriceAfterDiscount(double componentPriceAfterDiscount) {
        this.put(COMPONENT_PRICE_AFTER_DISCOUNT, componentPriceAfterDiscount);
        return this;
    }

    public double getComponentPriceWoTax() {
        return this.getDouble(COMPONENT_PRICE_WO_TAX);
    }

    public DWModuleComponent setComponentPriceWoTax(double componentPriceWoTax) {
        this.put(COMPONENT_PRICE_WO_TAX, componentPriceWoTax);
        return this;
    }

    public double getComponentCost() {
        return this.getDouble(COMPONENT_COST);
    }

    public DWModuleComponent setComponentCost(double componentCost) {
        this.put(COMPONENT_COST, componentCost);
        return this;
    }

    public double getComponentProfit() {
        return this.getDouble(COMPONENT_PROFIT);
    }

    public DWModuleComponent setComponentProfit(double componentProfit) {
        this.put(COMPONENT_PROFIT, componentProfit);
        return this;
    }

    public double getComponentMargin() {
        return this.getDouble(COMPONENT_MARGIN);
    }

    public DWModuleComponent setComponentMargin(double componentMargin) {
        this.put(COMPONENT_MARGIN, componentMargin);
        return this;
    }

    public DWModuleComponent setDwComponentAttributes(ProposalHeader proposalHeader, ProposalVersion proposalVersion, ProductLineItem productLineItem, ProductModule productModule, PanelComponent panelComponent) {

        DWModuleComponent dwModuleComponent = new DWModuleComponent();

        double panelPrice = 0;
        double panelPriceAfterDiscount = 0;
        double panelCost = 0;
        double panelPriceWoTax = 0;
        double panelProfit = 0;
        double panelMargin = 0;

        ShutterFinish finish = ModuleDataService.getInstance().getFinish(productLineItem.getFinishCode());

        RateCard nonStandardloadingFactorCard = RateCardService.getInstance().getRateCard(RateCard.LOADING_FACTOR_NONSTANDARD,
                RateCard.FACTOR_TYPE, proposalHeader.getPriceDate(), proposalHeader.getProjectCity());
        RateCard loadingFactorBasedOnProduct = RateCardService.getInstance().getRateCardBasedOnProduct(RateCard.LOADING_FACTOR,
                RateCard.FACTOR_TYPE, proposalHeader.getPriceDate(), proposalHeader.getProjectCity(), productModule.getProductCategory());
        RateCard stdLoadingSourceFactorBasedOnProduct = RateCardService.getInstance().getRateCardBasedOnProduct(RateCard.STD_MANUFACTURING_COST_FACTOR,
                RateCard.FACTOR_TYPE, proposalHeader.getPriceDate(), proposalHeader.getProjectCity(), productModule.getProductCategory());
        RateCard nStdLoadingSourceFactorBasedOnProduct = RateCardService.getInstance().getRateCardBasedOnProduct(RateCard.NONSTD_MANUFACTURING_COST_FACTOR,
                RateCard.FACTOR_TYPE, proposalHeader.getPriceDate(), proposalHeader.getProjectCity(), productModule.getProductCategory());
        RateCard prodWoTaxFactor = RateCardService.getInstance().getRateCard(RateCard.PRODUCT_WO_TAX,
                RateCard.FACTOR_TYPE, proposalHeader.getPriceDate(), proposalHeader.getProjectCity());
        RateCard stdManufacturingCost = RateCardService.getInstance().getRateCard(RateCard.STD_MANUFACTURING_COST_FACTOR, RateCard.FACTOR_TYPE, proposalHeader.getPriceDate(), proposalHeader.getProjectCity());
        RateCard nStdManufacturingCost = RateCardService.getInstance().getRateCard(RateCard.NONSTD_MANUFACTURING_COST_FACTOR, RateCard.FACTOR_TYPE, proposalHeader.getPriceDate(), proposalHeader.getProjectCity());


        double rate = loadingFactorBasedOnProduct.getRateBasedOnProduct();
        double stdSourceRate = stdLoadingSourceFactorBasedOnProduct.getSourcePrice();
        double nStdSourceRate = nStdLoadingSourceFactorBasedOnProduct.getSourcePrice();

        String moduleType;

        if (productModule.getMGCode().startsWith("MG-NS")) {
            moduleType = "nonStandard";
        } else if (productModule.getMGCode().startsWith("MG-NS-H")) {
            moduleType = "hike";
        } else {
            moduleType = "Standard";
        }

        dwModuleComponent.setProposalId(proposalHeader.getId());
        dwModuleComponent.setQuoteNo(proposalHeader.getQuoteNumNew());
        dwModuleComponent.setCrmId(proposalHeader.getCrmId());
        dwModuleComponent.setProposalTitle(proposalHeader.getQuotationFor());
        dwModuleComponent.setVersion(proposalVersion.getVersion());
        dwModuleComponent.setPriceDate(proposalHeader.getPriceDate());
        dwModuleComponent.setBusinessDate(proposalVersion.getDate());
        dwModuleComponent.setRegion(proposalHeader.getProjectCity());
        dwModuleComponent.setStatus(proposalVersion.getProposalStatus());
        dwModuleComponent.setDiscountAmount(proposalVersion.getDiscountAmount());
        dwModuleComponent.setDiscountAmountPerc(proposalVersion.getDiscountPercentage());
        dwModuleComponent.setSpaceType(productLineItem.getSpaceType());
        dwModuleComponent.setRoom(productLineItem.getRoomCode());
        dwModuleComponent.setPrId(productLineItem.getId());
        dwModuleComponent.setPrTitle(productLineItem.getTitle());
        dwModuleComponent.setProductCategory(productLineItem.getProductCategory());
        dwModuleComponent.setModuleType(moduleType);
        dwModuleComponent.setModuleCode(productModule.getMGCode());
        dwModuleComponent.setModuleCategory(productModule.getModuleCategory());
        dwModuleComponent.setModuleSeq(productModule.getModuleSequence());
        dwModuleComponent.setAccPackCode(panelComponent.getAccPackCode());
        dwModuleComponent.setCarcass(productModule.getCarcassCode());
        dwModuleComponent.setFinish(finish.getFinishType());
        dwModuleComponent.setFinishMaterial(productModule.getFinishType());
        dwModuleComponent.setHeight(panelComponent.getLength());
        dwModuleComponent.setWidth(panelComponent.getBreadth());
        dwModuleComponent.setDepth(panelComponent.getThickness());
        dwModuleComponent.setPanelArea(panelComponent.getArea());
        dwModuleComponent.setComponentType(panelComponent.getType());
        dwModuleComponent.setComponentCode(panelComponent.getCode());
        dwModuleComponent.setComponentTitle(panelComponent.getTitle());
        dwModuleComponent.setComponentQty(panelComponent.getQuantity());

        //calculatePanelPriceAndCost(productModule, panelComponent, nonStandardloadingFactorCard, nStdLoadingSourceFactorBasedOnProduct, stdManufacturingCost, nStdManufacturingCost, rate, stdSourceRate, nStdSourceRate, moduleType);

        if (panelComponent.isExposed()) {
            if ("Standard".equals(moduleType)) {
                if (Objects.equals(WARDROBE, productModule.getProductCategory()) || Objects.equals("W", productModule.getProductCategory())) {
                    panelPrice = panelComponent.getCost() * rate;
                    panelCost = panelComponent.getCost() / stdSourceRate;

                } else {
                    panelPrice = panelComponent.getCost();
                    panelCost = panelComponent.getCost() / stdManufacturingCost.getSourcePrice();
                }
            } else {
                if (Objects.equals("shoerack", productModule.getProductCategory()) || Objects.equals("studytable", productModule.getProductCategory()) || Objects.equals("crunit", productModule.getProductCategory())) {
                    panelPrice = panelComponent.getCost() * rate;
                    panelCost = ((panelComponent.getCost() * rate) / nStdLoadingSourceFactorBasedOnProduct.getSourcePriceBasedOnProduct());
                } else {
                    panelPrice = (panelComponent.getCost() * nonStandardloadingFactorCard.getRate());
                    panelCost = ((panelComponent.getCost() * nonStandardloadingFactorCard.getRate()) / nStdSourceRate);
                }
            }
        } else {
            if ("Standard".equals(moduleType)) {
                if (Objects.equals(WARDROBE, productModule.getProductCategory()) || Objects.equals("W", productModule.getProductCategory())) {
                    panelPrice = (panelComponent.getCost() * rate);
                    panelCost = (panelComponent.getCost() / stdSourceRate);
                } else {
                    panelPrice = (panelComponent.getCost());
                    panelCost = (panelComponent.getCost() / stdManufacturingCost.getSourcePrice());
                }
            } else if ("hike".equals(moduleType)) {
                panelPrice = (panelComponent.getCost());
                panelCost = (panelComponent.getCost() / stdManufacturingCost.getSourcePrice());
            } else {
                if (Objects.equals("shoerack", productModule.getProductCategory()) || Objects.equals("studytable", productModule.getProductCategory()) || Objects.equals("crunit", productModule.getProductCategory())) {
                    panelPrice = (panelComponent.getCost() * rate);
                    panelCost = ((panelComponent.getCost() * rate) / nStdLoadingSourceFactorBasedOnProduct.getSourcePriceBasedOnProduct());
                } else {
                    panelPrice = (panelComponent.getCost() * nonStandardloadingFactorCard.getRate());
                    panelCost = ((panelComponent.getCost() * nonStandardloadingFactorCard.getRate()) / nStdManufacturingCost.getSourcePrice());
                }
            }
        }

        panelPriceAfterDiscount = panelPrice - (panelPrice * (proposalVersion.getDiscountPercentage()/100));
        panelPriceWoTax = panelPriceAfterDiscount / prodWoTaxFactor.getSourcePrice();
        panelProfit = panelPriceWoTax - panelCost;
        panelMargin = (panelProfit / panelPriceWoTax) * 100;

        dwModuleComponent.setComponentPrice(panelPrice);
        dwModuleComponent.setComponentPriceAfterDiscount(panelPriceAfterDiscount);
        dwModuleComponent.setComponentPriceWoTax(panelPriceWoTax);
        dwModuleComponent.setComponentCost(panelCost);
        dwModuleComponent.setComponentProfit(panelProfit);
        dwModuleComponent.setComponentMargin(panelMargin);

        return dwModuleComponent;
    }

    }
