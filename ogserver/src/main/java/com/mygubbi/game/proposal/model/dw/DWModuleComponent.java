package com.mygubbi.game.proposal.model.dw;

import com.mygubbi.game.proposal.ModuleDataService;
import com.mygubbi.game.proposal.ProductLineItem;
import com.mygubbi.game.proposal.ProductModule;
import com.mygubbi.game.proposal.model.*;
import com.mygubbi.game.proposal.price.AccessoryComponent;
import com.mygubbi.game.proposal.price.HardwareComponent;
import com.mygubbi.game.proposal.price.PanelComponent;
import com.mygubbi.game.proposal.price.RateCardService;
import io.vertx.core.json.JsonObject;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by User on 30-08-2017.
 */
public class DWModuleComponent extends JsonObject {

    private static final String WARDROBE = "Wardrobe";


    private static final String ID="id";
    private static final String PROPOSALID="proposalId";
    private static final String QUOTENO="quoteNo";
    private static final String DESIGNER_NAME = "designerName";
    private static final String SALES_NAME = "salesName";
    private static final String SOURCE ="source";
    private static final String FROM_PRODUCT="fromProduct";
    private static final String BEF_PROD_SPEC = "beforeProductionSpecification";
    private static final String FROM_PROPOSAL = "fromProposal";
    private static final String OFFER_TYPE = "offerType";
    private static final String PACKAGE_FLAG = "packageFlag";
    private static final String CRMID="crmId";
    private static final String PROPOSAL_TITLE="proposalTitle";
    private static final String VERSION="version";
    private static final String PRICEDATE="priceDate";
    private static final String BUSINESSDATE="businessDate";
    private static final String REGION="region";
    private static final String STATUS="status";
    private static final String SPACETYPE="spaceType";
    private static final String ROOM="room";
    private static final String PRID="prId";
    private static final String PRTITLE="prTitle";
    private static final String PRPRICE="prPrice";
    private static final String PRPRICEAFTERDISCOUNT="prPriceAfterDiscount";
    private static final String PRAREA="prArea";
    private static final String PRCATEGORY="prCategory";
    private static final String MODULE_TYPE="moduleType";
    private static final String MODULESEQ="moduleSeq";
    private static final String MODULECODE="moduleCode";
    private static final String MODULECATEGORY="moduleCategory";
    private static final String ACC_PACK_CODE="accPackCode";
    private static final String CARCASS="carcass";
    private static final String FINISH="finish";
    private static final String FINISH_MATERIAL="finishMaterial";
    private static final String DISCOUNT_AMOUNT="discountAmount";
    private static final String DISCOUNT_AMOUNT_PERC="discountPercentage";
    private static final String WIDTH="width";
    private static final String DEPTH="depth";
    private static final String HEIGHT="height";
    private static final String PANEL_AREA="panelArea";
    private static final String COMPONENT_TYPE="compType";
    private static final String COMPONENT_CODE="compCode";
    private static final String COMPONENT_UOM="compUom";
    private static final String COMPONENT_QTY="compQty";
    private static final String COMPONENT_TITLE="compTitle";
    private static final String COMPONENT_PRICE="compPrice";
    private static final String COMPONENT_PRICE_AFTER_DISCOUNT="compPriceAfterDiscount";
    private static final String COMPONENT_PRICE_WO_TAX="compWoTax";
    private static final String COMPONENT_COST="compCost";
    private static final String COMPONENT_PROFIT="compProfit";
    private static final String COMPONENT_MARGIN="compMargin";
    private static final String COLOR="color";
    private static final String COMPONENT_ERPCODE="erpCode";
    private static final String COMPONENT_ARTICLE_ID="articleId";

    private static final String OLD_MATT_SOLID_FINISH = "Matt -solid";
    private static final String OLD_MATT_WOOD_GRAIN_FINISH = "Matt- Wood grain";
    private static final String NEW_MATT_SOLID_FINISH = "MATT-SOLID";
    private static final String NEW_MATT_WOOD_GRAIN_FINISH = "MATT-WG";





    public DWModuleComponent(JsonObject json) {
        super(json.getMap());
    }

    public DWModuleComponent() {}

    public String getColor() {
        return this.getString(COLOR);
    }

    public DWModuleComponent setColor(String color)
    {
        put(COLOR,color);
        return this;
    }

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

    public static String getComponentErpcode() {
        return COMPONENT_ERPCODE;
    }

    public DWModuleComponent setComponentErpcode(String erpcode)
    {
        put(COMPONENT_ERPCODE,erpcode);
        return this;
    }
    public static String getComponentArticleId() {
        return COMPONENT_ARTICLE_ID;
    }

    public DWModuleComponent setComponentArticleId(String articleId)
    {
        put(COMPONENT_ARTICLE_ID,articleId);
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

    public double getPrPrice() {
        return this.getDouble(PRPRICE);
    }

    public DWModuleComponent setPrPrice(double prPrice)
    {
        put(PRPRICE,prPrice);
        return this;
    }
    public double getPrPriceAfterDiscount() {
        return this.getDouble(PRPRICEAFTERDISCOUNT);
    }

    public DWModuleComponent setPrPriceAfterDiscount(double prPriceAfterDiscount)
    {
        put(PRPRICEAFTERDISCOUNT,prPriceAfterDiscount);
        return this;
    }
    public double getPrArea() {
        return this.getDouble(PRAREA);
    }

    public DWModuleComponent setPrArea(double prArea)
    {
        put(PRAREA,prArea);
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

    public String getComponentUom() {
        return this.getString(COMPONENT_UOM);
    }

    public DWModuleComponent setComponentUom(String componentUom) {
        this.put(COMPONENT_UOM,componentUom);
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
    public String getDesignerName() {return this.getString(DESIGNER_NAME);}
    public String getSalesName() {return this.getString(SALES_NAME);}
    public String getSource() {return this.getString(SOURCE);}
    public Integer getFromProduct() { if(this.containsKey(FROM_PRODUCT))return this.getInteger(FROM_PRODUCT); return 0;}
    public String getBefProdSpec() {return this.getString(BEF_PROD_SPEC);}
    public Integer getFromProposal() {return this.getInteger(FROM_PROPOSAL);}
    public String getOfferType() {return this.getString(OFFER_TYPE);}
    public String getPackageFlag() {return this.getString(PACKAGE_FLAG);}
    public DWModuleComponent setDesignerName(String dname) {this.put(DESIGNER_NAME, dname);return this;}
    public DWModuleComponent setSalesName(String sname) {this.put(SALES_NAME, sname);return this;}
    public DWModuleComponent setSource(String sname) {this.put(SOURCE, sname);return this;}
    public DWModuleComponent setFromProduct(Integer val) {this.put(FROM_PRODUCT, val);return this;}
    public DWModuleComponent setBefProdSpec(String val){this.put(BEF_PROD_SPEC,val);return this;}
    public DWModuleComponent setFromProposal(Integer val){this.put(FROM_PROPOSAL,val);return this;}
    public DWModuleComponent setOfferType(String val){this.put(OFFER_TYPE,val);return this;}
    public DWModuleComponent setPackageFlag(String val){this.put(PACKAGE_FLAG,val);return this;}

    public DWModuleComponent setDwComponentAttributesForPanel(ProposalHeader proposalHeader, ProposalVersion proposalVersion, ProductLineItem productLineItem, ProductModule productModule, PanelComponent panelComponent) {

        DWModuleComponent dwModuleComponent = new DWModuleComponent();

        double panelPrice = 0;
        double panelPriceAfterDiscount = 0;
        double panelCost = 0;
        double panelPriceWoTax = 0;
        double panelProfit = 0;
        double panelMargin = 0;

        ShutterFinish finish = ModuleDataService.getInstance().getFinish(productModule.getFinishCode());

        RateCard nonStandardloadingFactorCard = RateCardService.getInstance().getRateCard(RateCard.LOADING_FACTOR_NONSTANDARD,
                RateCard.FACTOR_TYPE, proposalHeader.getPriceDate(), proposalHeader.getProjectCity());
        RateCard loadingFactorBasedOnProduct = RateCardService.getInstance().getRateCardBasedOnProduct(RateCard.LOADING_FACTOR,
                RateCard.FACTOR_TYPE, proposalHeader.getPriceDate(), proposalHeader.getProjectCity(), productModule.getProductCategory());
        RateCard stdLoadingSourceFactorBasedOnProduct = RateCardService.getInstance().getRateCardBasedOnProduct(RateCard.STD_MANUFACTURING_COST_FACTOR,
                RateCard.FACTOR_TYPE, proposalHeader.getPriceDate(), proposalHeader.getProjectCity(), productModule.getProductCategory());
        RateCard nStdLoadingSourceFactorBasedOnProduct = RateCardService.getInstance().getRateCardBasedOnProduct(RateCard.NONSTD_MANUFACTURING_COST_FACTOR,
                RateCard.FACTOR_TYPE, proposalHeader.getPriceDate(), proposalHeader.getProjectCity(), productModule.getProductCategory());
        RateCard stdManufacturingCost = RateCardService.getInstance().getRateCard(RateCard.STD_MANUFACTURING_COST_FACTOR, RateCard.FACTOR_TYPE, proposalHeader.getPriceDate(), proposalHeader.getProjectCity());
        RateCard nStdManufacturingCost = RateCardService.getInstance().getRateCard(RateCard.NONSTD_MANUFACTURING_COST_FACTOR, RateCard.FACTOR_TYPE, proposalHeader.getPriceDate(), proposalHeader.getProjectCity());


        double rate = loadingFactorBasedOnProduct.getRateBasedOnProduct();
        double stdSourceRate = stdLoadingSourceFactorBasedOnProduct.getSourcePrice();
        double nStdSourceRate = nStdLoadingSourceFactorBasedOnProduct.getSourcePrice();

        double afterTaxFactor = getAfterTaxFactor(proposalHeader, productModule);


        String moduleType;

        if (productModule.getMGCode().startsWith("MG-NS")) {
            moduleType = "nonStandard";
        } else if (productModule.getMGCode().startsWith("MG-NS-H")) {
            moduleType = "hike";
        } else {
            moduleType = "Standard";
        }

        double productAreaInSqft = 0.0;

        List<ProductModule> modules = productLineItem.getModules();
        for (ProductModule module : modules)
        {
            if (module.getAreaOfModuleInSft() != 0)
            {
                productAreaInSqft += module.getAreaOfModuleInSft();
            }
        }

        dwModuleComponent.setProposalId(proposalHeader.getId());
        dwModuleComponent.setQuoteNo(proposalHeader.getQuoteNumNew());
        dwModuleComponent.setSalesName(proposalHeader.getSalespersonName());
        dwModuleComponent.setSource(productLineItem.getSource());
        dwModuleComponent.setFromProduct(productLineItem.getFromProduct());
        dwModuleComponent.setBefProdSpec(proposalHeader.getBefProdSpec());
        dwModuleComponent.setFromProposal(proposalHeader.getFromProposal());
        dwModuleComponent.setOfferType(proposalHeader.getOfferType());
        dwModuleComponent.setPackageFlag(proposalHeader.getPackageFlag());
        dwModuleComponent.setDesignerName(proposalHeader.getDesignerName());
        dwModuleComponent.setCrmId(proposalHeader.getCrmId());
        dwModuleComponent.setProposalTitle(proposalHeader.getQuotationFor());
        dwModuleComponent.setVersion(proposalVersion.getVersion());
        dwModuleComponent.setPriceDate(proposalHeader.getPriceDate());
        dwModuleComponent.setBusinessDate(proposalVersion.getBusinessDate());
        dwModuleComponent.setRegion(proposalHeader.getProjectCity());
        dwModuleComponent.setStatus(proposalVersion.getProposalStatus());
        dwModuleComponent.setDiscountAmount(proposalVersion.getDiscountAmount());
        dwModuleComponent.setDiscountAmountPerc(proposalVersion.getDiscountPercentage());
        dwModuleComponent.setSpaceType(productLineItem.getSpaceType());
        dwModuleComponent.setRoom(productLineItem.getRoomCode());
        dwModuleComponent.setPrId(productLineItem.getId());
        dwModuleComponent.setPrTitle(productLineItem.getTitle());
        dwModuleComponent.setPrPrice(productLineItem.getAmount());
        dwModuleComponent.setPrPriceAfterDiscount(productLineItem.getAmount() - (productLineItem.getAmount() * (proposalVersion.getDiscountPercentage()/100)));
        dwModuleComponent.setPrArea(productAreaInSqft);
        dwModuleComponent.setProductCategory(productLineItem.getProductCategory());
        dwModuleComponent.setModuleType(moduleType);
        dwModuleComponent.setModuleCode(productModule.getMGCode());
        dwModuleComponent.setModuleCategory(productModule.getModuleCategory());
        dwModuleComponent.setModuleSeq(productModule.getModuleSequence());
        dwModuleComponent.setAccPackCode(panelComponent.getAccPackCode());
        dwModuleComponent.setCarcass(productModule.getCarcassCode());

        String finishCode = finish.getFinishCode();
        /*if(finishCode.equalsIgnoreCase(OLD_MATT_SOLID_FINISH)){
            finishCode = NEW_MATT_SOLID_FINISH;
        }
        if(finishCode.equalsIgnoreCase(OLD_MATT_WOOD_GRAIN_FINISH)){
            finishCode = NEW_MATT_WOOD_GRAIN_FINISH;
        }*/

        ShutterFinish shutterFinish = ModuleDataService.getInstance().getFinish(finishCode);

        dwModuleComponent.setFinish(shutterFinish.getTitle());
        dwModuleComponent.setFinishMaterial(productModule.getFinishType());
        dwModuleComponent.setHeight(panelComponent.getLength());
        dwModuleComponent.setWidth(panelComponent.getBreadth());
        dwModuleComponent.setDepth(panelComponent.getThickness());
        dwModuleComponent.setColor(productModule.getColorCode());
        dwModuleComponent.setPanelArea(panelComponent.getArea() * panelComponent.getQuantity());
//        dwModuleComponent.setComponentType(panelComponent.getType());
        dwModuleComponent.setComponentCode(panelComponent.getCode());
        dwModuleComponent.setComponentUom("Qty");
        dwModuleComponent.setComponentTitle(panelComponent.getTitle());
        dwModuleComponent.setComponentQty(panelComponent.getQuantity());

        //calculatePanelPriceAndCost(productModule, panelComponent, nonStandardloadingFactorCard, nStdLoadingSourceFactorBasedOnProduct, stdManufacturingCost, nStdManufacturingCost, rate, stdSourceRate, nStdSourceRate, moduleType);

        if (panelComponent.isExposed()) {
            dwModuleComponent.setComponentType(PanelComponent.SHUTTER_TYPE);
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
                dwModuleComponent.setComponentType(PanelComponent.CARCASS_TYPE);
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
        panelPriceWoTax = panelPriceAfterDiscount * afterTaxFactor;
        panelProfit = panelPriceWoTax - panelCost;

        if(panelPriceWoTax ==0 ||panelProfit == 0 ){
            panelMargin = 0.0;
        }else {
            panelMargin = (panelProfit / panelPriceWoTax) * 100;
        }

        dwModuleComponent.setComponentPrice(panelPrice);
        dwModuleComponent.setComponentPriceAfterDiscount(panelPriceAfterDiscount);
        dwModuleComponent.setComponentPriceWoTax(panelPriceWoTax);
        dwModuleComponent.setComponentCost(panelCost);
        dwModuleComponent.setComponentProfit(panelProfit);
        dwModuleComponent.setComponentMargin(panelMargin);

        return dwModuleComponent;
    }


    public DWModuleComponent setDwComponentAttributesForHardware(ProposalHeader proposalHeader, ProposalVersion proposalVersion, ProductLineItem productLineItem, ProductModule productModule, HardwareComponent hardwareComponent) {

        DWModuleComponent dwModuleComponent = new DWModuleComponent();

        double afterTaxFactor = getAfterTaxFactor(proposalHeader, productModule);

        double quantity = 0;

        ShutterFinish finish = ModuleDataService.getInstance().getFinish(productLineItem.getFinishCode());

        String moduleType;

        if (productModule.getMGCode().startsWith("MG-NS")) {
            moduleType = "nonStandard";
        } else if (productModule.getMGCode().startsWith("MG-NS-H")) {
            moduleType = "hike";
        } else {
            moduleType = "Standard";
        }

        double productAreaInSqft = 0.0;

        List<ProductModule> modules = productLineItem.getModules();
        for (ProductModule module : modules)
        {
            if (module.getAreaOfModuleInSft() != 0)
            {
                productAreaInSqft += module.getAreaOfModuleInSft();
            }
        }

        dwModuleComponent.setProposalId(proposalHeader.getId());
        dwModuleComponent.setQuoteNo(proposalHeader.getQuoteNumNew());
        dwModuleComponent.setSalesName(proposalHeader.getSalespersonName());
        dwModuleComponent.setSource(productLineItem.getSource());
        dwModuleComponent.setFromProduct(productLineItem.getFromProduct());
        dwModuleComponent.setBefProdSpec(proposalHeader.getBefProdSpec());
        dwModuleComponent.setFromProposal(proposalHeader.getFromProposal());
        dwModuleComponent.setOfferType(proposalHeader.getOfferType());
        dwModuleComponent.setPackageFlag(proposalHeader.getPackageFlag());
        dwModuleComponent.setDesignerName(proposalHeader.getDesignerName());
        dwModuleComponent.setCrmId(proposalHeader.getCrmId());
        dwModuleComponent.setProposalTitle(proposalHeader.getQuotationFor());
        dwModuleComponent.setVersion(proposalVersion.getVersion());
        dwModuleComponent.setPriceDate(proposalHeader.getPriceDate());
        dwModuleComponent.setBusinessDate(proposalVersion.getUpdatedOn());
        dwModuleComponent.setRegion(proposalHeader.getProjectCity());
        dwModuleComponent.setStatus(proposalVersion.getProposalStatus());
        dwModuleComponent.setDiscountAmount(proposalVersion.getDiscountAmount());
        dwModuleComponent.setDiscountAmountPerc(proposalVersion.getDiscountPercentage());
        dwModuleComponent.setSpaceType(productLineItem.getSpaceType());
        dwModuleComponent.setRoom(productLineItem.getRoomCode());
        dwModuleComponent.setPrId(productLineItem.getId());
        dwModuleComponent.setPrTitle(productLineItem.getTitle());
        dwModuleComponent.setPrPrice(productLineItem.getAmount());
        dwModuleComponent.setPrPriceAfterDiscount(productLineItem.getAmount() - (productLineItem.getAmount() * proposalVersion.getDiscountPercentage()/100));
        dwModuleComponent.setPrArea(productAreaInSqft);
        dwModuleComponent.setProductCategory(productLineItem.getProductCategory());
        dwModuleComponent.setModuleType(moduleType);
        dwModuleComponent.setModuleCode(productModule.getMGCode());
        dwModuleComponent.setModuleCategory(productModule.getModuleCategory());
        dwModuleComponent.setModuleSeq(productModule.getModuleSequence());
        dwModuleComponent.setAccPackCode("NA");
        dwModuleComponent.setCarcass(productModule.getCarcassCode());

        String finishCode = finish.getFinishCode();
        /*if(finishCode.equalsIgnoreCase(OLD_MATT_SOLID_FINISH)){
            finishCode = NEW_MATT_SOLID_FINISH;
        }
        if(finishCode.equalsIgnoreCase(OLD_MATT_WOOD_GRAIN_FINISH)){
            finishCode = NEW_MATT_WOOD_GRAIN_FINISH;
        }*/

        ShutterFinish shutterFinish = ModuleDataService.getInstance().getFinish(finishCode);


        dwModuleComponent.setFinish(shutterFinish.getTitle());
        dwModuleComponent.setFinishMaterial(productModule.getFinishType());
        dwModuleComponent.setHeight(0);
        dwModuleComponent.setWidth(0);
        dwModuleComponent.setDepth(0);
        dwModuleComponent.setPanelArea(0);
        dwModuleComponent.setColor("");
        dwModuleComponent.setComponentType("H");
        dwModuleComponent.setComponentCode(hardwareComponent.getComponent().getCode());
        dwModuleComponent.setComponentUom(hardwareComponent.getComponent().getUom());
        dwModuleComponent.setComponentTitle(hardwareComponent.getComponent().getTitle());
        dwModuleComponent.setComponentErpcode(hardwareComponent.getComponent().getERPCode());
        dwModuleComponent.setComponentArticleId(hardwareComponent.getComponent().getCatalogCode());
        if (hardwareComponent.getQuantityFormula().equals("Fixed Quantity"))
        {
            quantity = hardwareComponent.getQuantity();
        }
        else
        {
            quantity = hardwareComponent.calculateQuantityUsingFormula(productModule,hardwareComponent.getQuantityFormula());
        }
        dwModuleComponent.setComponentQty(quantity);

        double componentPrice = 0;
        double componentPriceAfterDiscount = 0;
        double componentPriceWoTax = 0;
        double componentCost = 0;
        double componentProfit = 0;
        double componentMargin = 0;

        if (hardwareComponent.getPrice() != 0)
        {
            componentPrice = hardwareComponent.getPrice() * quantity;
            componentPriceAfterDiscount = componentPrice - (componentPrice * (proposalVersion.getDiscountPercentage()/100));
            componentPriceWoTax = componentPriceAfterDiscount * afterTaxFactor;
            componentCost = hardwareComponent.getSourcePrice() * quantity;
            componentProfit = componentPriceWoTax - componentCost;
            if(componentProfit == 0.0 || componentPriceWoTax == 0.0){
                componentMargin = 0.0;
            }else
                  componentMargin = componentProfit / componentPriceWoTax;
        }

        dwModuleComponent.setComponentPrice(componentPrice);
        dwModuleComponent.setComponentPriceAfterDiscount(componentPriceAfterDiscount);
        dwModuleComponent.setComponentPriceWoTax(componentPriceWoTax);
        dwModuleComponent.setComponentCost(componentCost);
        dwModuleComponent.setComponentProfit(componentProfit);
        dwModuleComponent.setComponentMargin(componentMargin);

        return dwModuleComponent;
    }



    public DWModuleComponent setDwComponentAttributesForAccessories(ProposalHeader proposalHeader, ProposalVersion proposalVersion, ProductLineItem productLineItem, ProductModule productModule, AccessoryComponent accessoryComponent) {

        DWModuleComponent dwModuleComponent = new DWModuleComponent();

        double afterTaxFactor = getAfterTaxFactor(proposalHeader, productModule);


        ShutterFinish finish = ModuleDataService.getInstance().getFinish(productLineItem.getFinishCode());

        String moduleType;

        if (productModule.getMGCode().startsWith("MG-NS")) {
            moduleType = "nonStandard";
        } else if (productModule.getMGCode().startsWith("MG-NS-H")) {
            moduleType = "hike";
        } else {
            moduleType = "Standard";
        }

        double productAreaInSqft = 0.0;

        List<ProductModule> modules = productLineItem.getModules();
        for (ProductModule module : modules)
        {
            if (module.getAreaOfModuleInSft() != 0)
            {
                productAreaInSqft += module.getAreaOfModuleInSft();
            }
        }

        dwModuleComponent.setProposalId(proposalHeader.getId());
        dwModuleComponent.setQuoteNo(proposalHeader.getQuoteNumNew());
        dwModuleComponent.setSalesName(proposalHeader.getSalespersonName());
        dwModuleComponent.setSource(productLineItem.getSource());
        dwModuleComponent.setFromProduct(productLineItem.getFromProduct());
        dwModuleComponent.setBefProdSpec(proposalHeader.getBefProdSpec());
        dwModuleComponent.setFromProposal(proposalHeader.getFromProposal());
        dwModuleComponent.setOfferType(proposalHeader.getOfferType());
        dwModuleComponent.setPackageFlag(proposalHeader.getPackageFlag());
        dwModuleComponent.setDesignerName(proposalHeader.getDesignerName());
        dwModuleComponent.setCrmId(proposalHeader.getCrmId());
        dwModuleComponent.setProposalTitle(proposalHeader.getQuotationFor());
        dwModuleComponent.setVersion(proposalVersion.getVersion());
        dwModuleComponent.setPriceDate(proposalHeader.getPriceDate());
        dwModuleComponent.setBusinessDate(proposalVersion.getUpdatedOn());
        dwModuleComponent.setRegion(proposalHeader.getProjectCity());
        dwModuleComponent.setStatus(proposalVersion.getProposalStatus());
        dwModuleComponent.setDiscountAmount(proposalVersion.getDiscountAmount());
        dwModuleComponent.setDiscountAmountPerc(proposalVersion.getDiscountPercentage());
        dwModuleComponent.setSpaceType(productLineItem.getSpaceType());
        dwModuleComponent.setRoom(productLineItem.getRoomCode());
        dwModuleComponent.setPrId(productLineItem.getId());
        dwModuleComponent.setPrTitle(productLineItem.getTitle());
        dwModuleComponent.setPrPrice(productLineItem.getAmount());
        dwModuleComponent.setPrPriceAfterDiscount(productLineItem.getAmount() - (productLineItem.getAmount() * proposalVersion.getDiscountPercentage()/100));
        dwModuleComponent.setPrArea(productAreaInSqft);
        dwModuleComponent.setProductCategory(productLineItem.getProductCategory());
        dwModuleComponent.setModuleType(moduleType);
        dwModuleComponent.setModuleCode(productModule.getMGCode());
        dwModuleComponent.setModuleCategory(productModule.getModuleCategory());
        dwModuleComponent.setModuleSeq(productModule.getModuleSequence());
        dwModuleComponent.setAccPackCode("NA");
        dwModuleComponent.setCarcass(productModule.getCarcassCode());

        String finishCode = finish.getFinishCode();
      /*  if(finishCode.equalsIgnoreCase(OLD_MATT_SOLID_FINISH)){
            finishCode = NEW_MATT_SOLID_FINISH;
        }
        if(finishCode.equalsIgnoreCase(OLD_MATT_WOOD_GRAIN_FINISH)){
            finishCode = NEW_MATT_WOOD_GRAIN_FINISH;
        }*/

        ShutterFinish shutterFinish = ModuleDataService.getInstance().getFinish(finishCode);


        dwModuleComponent.setFinish(shutterFinish.getTitle());
        dwModuleComponent.setFinishMaterial(productModule.getFinishType());
        dwModuleComponent.setHeight(0);
        dwModuleComponent.setWidth(0);
        dwModuleComponent.setDepth(0);
        dwModuleComponent.setPanelArea(0);
        dwModuleComponent.setColor("");
        dwModuleComponent.setComponentType("A");
        dwModuleComponent.setComponentErpcode(accessoryComponent.getComponent().getERPCode());
        dwModuleComponent.setComponentCode(accessoryComponent.getComponent().getCode());
        dwModuleComponent.setComponentUom(accessoryComponent.getComponent().getUom());
        dwModuleComponent.setComponentTitle(accessoryComponent.getComponent().getTitle());

        dwModuleComponent.setComponentQty(accessoryComponent.getQuantity());

        double componentPrice = 0;
        double componentPriceAfterDiscount = 0;
        double componentPriceWoTax = 0;
        double componentCost = 0;
        double componentProfit = 0;
        double componentMargin = 0;

        if (accessoryComponent.getPrice() != 0)
        {
            componentPrice = accessoryComponent.getPrice() * accessoryComponent.getQuantity();
            componentPriceAfterDiscount = componentPrice - (componentPrice * (proposalVersion.getDiscountPercentage()/100));
            componentPriceWoTax = componentPriceAfterDiscount * afterTaxFactor;
            componentCost = accessoryComponent.getSourcePrice() * accessoryComponent.getQuantity();
            componentProfit = componentPriceWoTax - componentCost;
            if(componentProfit == 0 || componentPriceWoTax == 0){
                componentMargin = 0.0;
            }else
              componentMargin = componentProfit / componentPriceWoTax;
        }

        dwModuleComponent.setComponentPrice(componentPrice);
        dwModuleComponent.setComponentPriceAfterDiscount(componentPriceAfterDiscount);
        dwModuleComponent.setComponentPriceWoTax(componentPriceWoTax);
        dwModuleComponent.setComponentCost(componentCost);
        dwModuleComponent.setComponentProfit(componentProfit);
        dwModuleComponent.setComponentMargin(componentMargin);

        return dwModuleComponent;
    }


    public DWModuleComponent setDwComponentAttributesForGolaProfileHardware(ProposalHeader proposalHeader, ProposalVersion proposalVersion, ProductLineItem productLineItem, ProductModule productModule, AccHwComponent accHwComponent, double quantity) {
        DWModuleComponent dwModuleComponent = new DWModuleComponent();

        double afterTaxFactor = getAfterTaxFactor(proposalHeader, productModule);

        PriceMaster hardwareRate = RateCardService.getInstance().getHardwareRate(accHwComponent.getCode(),proposalHeader.getPriceDate(),proposalHeader.getProjectCity());

        ShutterFinish finish = ModuleDataService.getInstance().getFinish(productLineItem.getFinishCode());

        String moduleType;

        if (productModule.getMGCode().startsWith("MG-NS")) {
            moduleType = "nonStandard";
        } else if (productModule.getMGCode().startsWith("MG-NS-H")) {
            moduleType = "hike";
        } else {
            moduleType = "Standard";
        }

        double productAreaInSqft = 0.0;

        List<ProductModule> modules = productLineItem.getModules();
        for (ProductModule module : modules)
        {
            if (module.getAreaOfModuleInSft() != 0)
            {
                productAreaInSqft += module.getAreaOfModuleInSft();
            }
        }

        dwModuleComponent.setProposalId(proposalHeader.getId());
        dwModuleComponent.setQuoteNo(proposalHeader.getQuoteNumNew());
        dwModuleComponent.setSalesName(proposalHeader.getSalespersonName());
        dwModuleComponent.setSource(productLineItem.getSource());
        dwModuleComponent.setFromProduct(productLineItem.getFromProduct());
        dwModuleComponent.setBefProdSpec(proposalHeader.getBefProdSpec());
        dwModuleComponent.setFromProposal(proposalHeader.getFromProposal());
        dwModuleComponent.setOfferType(proposalHeader.getOfferType());
        dwModuleComponent.setPackageFlag(proposalHeader.getPackageFlag());
        dwModuleComponent.setDesignerName(proposalHeader.getDesignerName());
        dwModuleComponent.setCrmId(proposalHeader.getCrmId());
        dwModuleComponent.setProposalTitle(proposalHeader.getQuotationFor());
        dwModuleComponent.setVersion(proposalVersion.getVersion());
        dwModuleComponent.setPriceDate(proposalHeader.getPriceDate());
        dwModuleComponent.setBusinessDate(proposalVersion.getUpdatedOn());
        dwModuleComponent.setRegion(proposalHeader.getProjectCity());
        dwModuleComponent.setStatus(proposalVersion.getProposalStatus());
        dwModuleComponent.setDiscountAmount(proposalVersion.getDiscountAmount());
        dwModuleComponent.setDiscountAmountPerc(proposalVersion.getDiscountPercentage());
        dwModuleComponent.setSpaceType(productLineItem.getSpaceType());
        dwModuleComponent.setRoom(productLineItem.getRoomCode());
        dwModuleComponent.setPrId(productLineItem.getId());
        dwModuleComponent.setPrTitle(productLineItem.getTitle());
        dwModuleComponent.setPrPrice(productLineItem.getAmount());
        dwModuleComponent.setPrPriceAfterDiscount(productLineItem.getAmount() - (productLineItem.getAmount() * proposalVersion.getDiscountPercentage()/100));
        dwModuleComponent.setPrArea(productAreaInSqft);
        dwModuleComponent.setProductCategory(productLineItem.getProductCategory());
        dwModuleComponent.setModuleType(moduleType);
        dwModuleComponent.setModuleCode(productModule.getMGCode());
        dwModuleComponent.setModuleCategory(productModule.getModuleCategory());
        dwModuleComponent.setModuleSeq(productModule.getModuleSequence());
        dwModuleComponent.setAccPackCode("NA");
        dwModuleComponent.setCarcass(productModule.getCarcassCode());

        String finishCode = finish.getFinishCode();
        /*if(finishCode.equalsIgnoreCase(OLD_MATT_SOLID_FINISH)){
            finishCode = NEW_MATT_SOLID_FINISH;
        }
        if(finishCode.equalsIgnoreCase(OLD_MATT_WOOD_GRAIN_FINISH)){
            finishCode = NEW_MATT_WOOD_GRAIN_FINISH;
        }*/

        ShutterFinish shutterFinish = ModuleDataService.getInstance().getFinish(finishCode);



        dwModuleComponent.setFinish(shutterFinish.getTitle());
        dwModuleComponent.setFinishMaterial(productModule.getFinishType());
        dwModuleComponent.setHeight(0);
        dwModuleComponent.setWidth(0);
        dwModuleComponent.setDepth(0);
        dwModuleComponent.setPanelArea(0);
        dwModuleComponent.setComponentType("H");
        dwModuleComponent.setComponentErpcode(accHwComponent.getERPCode());
        dwModuleComponent.setComponentCode(accHwComponent.getCode());
        dwModuleComponent.setComponentUom(accHwComponent.getUom());
        dwModuleComponent.setComponentTitle(accHwComponent.getTitle());
        dwModuleComponent.setComponentQty(quantity);

        double componentPrice = 0;
        double componentPriceAfterDiscount = 0;
        double componentPriceWoTax = 0;
        double componentCost = 0;
        double componentProfit = 0;
        double componentMargin = 0;

        if (hardwareRate.getPrice() != 0)
        {
            componentPrice = hardwareRate.getPrice() * quantity;
            componentPriceAfterDiscount = componentPrice - (componentPrice * (proposalVersion.getDiscountPercentage()/100));
            componentPriceWoTax = componentPriceAfterDiscount * afterTaxFactor;
            componentCost = hardwareRate.getSourcePrice() * quantity;
            componentProfit = componentPriceWoTax - componentCost;
            if(componentProfit == 0.0 || componentPriceWoTax == 0.0){
                componentMargin = 0.0;
            }else
                componentMargin = componentProfit / componentPriceWoTax;
        }

        dwModuleComponent.setComponentPrice(componentPrice);
        dwModuleComponent.setComponentPriceAfterDiscount(componentPriceAfterDiscount);
        dwModuleComponent.setComponentPriceWoTax(componentPriceWoTax);
        dwModuleComponent.setComponentCost(componentCost);
        dwModuleComponent.setComponentProfit(componentProfit);
        dwModuleComponent.setComponentMargin(componentMargin);

        return dwModuleComponent;
    }

    public DWModuleComponent setDwComponentAttributesForHandleKnobOrHinge(ProposalHeader proposalHeader, ProposalVersion proposalVersion, ProductLineItem productLineItem, ProductModule productModule, Handle handle, double quantity) {


        DWModuleComponent dwModuleComponent = new DWModuleComponent();

        String compType = null;
        PriceMaster handleOrKnobRate = null;


        if (handle.getCode().startsWith("HANDLE"))
        {
            compType = "HL";
            handleOrKnobRate = RateCardService.getInstance().getHandleOrKnobRate(handle.getCode(),proposalHeader.getPriceDate(),proposalHeader.getProjectCity());
        }
        else if(handle.getCode().startsWith("KNOB")){
            compType = "K";
            handleOrKnobRate = RateCardService.getInstance().getHandleOrKnobRate(handle.getCode(),proposalHeader.getPriceDate(),proposalHeader.getProjectCity());
        }
        else
        {
            compType = "HI";
            handleOrKnobRate = RateCardService.getInstance().getHingeRate(handle.getCode(),proposalHeader.getPriceDate(),proposalHeader.getProjectCity());
        }

        double afterTaxFactor = getAfterTaxFactor(proposalHeader, productModule);


        ShutterFinish finish = ModuleDataService.getInstance().getFinish(productLineItem.getFinishCode());

        String moduleType;

        if (productModule.getMGCode().startsWith("MG-NS")) {
            moduleType = "nonStandard";
        } else if (productModule.getMGCode().startsWith("MG-NS-H")) {
            moduleType = "hike";
        } else {
            moduleType = "Standard";
        }

        double productAreaInSqft = 0.0;

        List<ProductModule> modules = productLineItem.getModules();
        for (ProductModule module : modules)
        {
            if (module.getAreaOfModuleInSft() != 0)
            {
                productAreaInSqft += module.getAreaOfModuleInSft();
            }
        }

        dwModuleComponent.setProposalId(proposalHeader.getId());
        dwModuleComponent.setQuoteNo(proposalHeader.getQuoteNumNew());
        dwModuleComponent.setSalesName(proposalHeader.getSalespersonName());
        dwModuleComponent.setSource(productLineItem.getSource());
        dwModuleComponent.setFromProduct(productLineItem.getFromProduct());
        dwModuleComponent.setBefProdSpec(proposalHeader.getBefProdSpec());
        dwModuleComponent.setFromProposal(proposalHeader.getFromProposal());
        dwModuleComponent.setOfferType(proposalHeader.getOfferType());
        dwModuleComponent.setPackageFlag(proposalHeader.getPackageFlag());
        dwModuleComponent.setDesignerName(proposalHeader.getDesignerName());
        dwModuleComponent.setCrmId(proposalHeader.getCrmId());
        dwModuleComponent.setProposalTitle(proposalHeader.getQuotationFor());
        dwModuleComponent.setVersion(proposalVersion.getVersion());
        dwModuleComponent.setPriceDate(proposalHeader.getPriceDate());
        dwModuleComponent.setBusinessDate(proposalVersion.getUpdatedOn());
        dwModuleComponent.setRegion(proposalHeader.getProjectCity());
        dwModuleComponent.setStatus(proposalVersion.getProposalStatus());
        dwModuleComponent.setDiscountAmount(proposalVersion.getDiscountAmount());
        dwModuleComponent.setDiscountAmountPerc(proposalVersion.getDiscountPercentage());
        dwModuleComponent.setSpaceType(productLineItem.getSpaceType());
        dwModuleComponent.setRoom(productLineItem.getRoomCode());
        dwModuleComponent.setPrId(productLineItem.getId());
        dwModuleComponent.setPrTitle(productLineItem.getTitle());
        dwModuleComponent.setPrPrice(productLineItem.getAmount());
        dwModuleComponent.setPrPriceAfterDiscount(productLineItem.getAmount() - (productLineItem.getAmount() * proposalVersion.getDiscountPercentage()/100));
        dwModuleComponent.setPrArea(productAreaInSqft);
        dwModuleComponent.setProductCategory(productLineItem.getProductCategory());
        dwModuleComponent.setModuleType(moduleType);
        dwModuleComponent.setModuleCode(productModule.getMGCode());
        dwModuleComponent.setModuleCategory(productModule.getModuleCategory());

        dwModuleComponent.setModuleSeq(productModule.getModuleSequence());
        dwModuleComponent.setAccPackCode("NA");
        dwModuleComponent.setCarcass(productModule.getCarcassCode());

        String finishCode = finish.getFinishCode();
        /*if(finishCode.equalsIgnoreCase(OLD_MATT_SOLID_FINISH)){
            finishCode = NEW_MATT_SOLID_FINISH;
        }
        if(finishCode.equalsIgnoreCase(OLD_MATT_WOOD_GRAIN_FINISH)){
            finishCode = NEW_MATT_WOOD_GRAIN_FINISH;
        }*/

        ShutterFinish shutterFinish = ModuleDataService.getInstance().getFinish(finishCode);


        dwModuleComponent.setFinish(shutterFinish.getTitle());
        dwModuleComponent.setFinishMaterial(productModule.getFinishType());
        dwModuleComponent.setHeight(productModule.getHeight());
        dwModuleComponent.setWidth(productModule.getWidth());
        dwModuleComponent.setDepth(productModule.getDepth());
        dwModuleComponent.setPanelArea(0);
        dwModuleComponent.setComponentCode(handle.getCode());
        dwModuleComponent.setComponentTitle(handle.getTitle());
        dwModuleComponent.setComponentUom("Qty");
        dwModuleComponent.setColor("");
        dwModuleComponent.setComponentType(compType);
        dwModuleComponent.setComponentQty(quantity);
        dwModuleComponent.setComponentErpcode(handle.getErpCode());
        dwModuleComponent.setComponentArticleId(handle.getArticleNo());

        double componentPrice = 0;
        double componentPriceAfterDiscount = 0;
        double componentPriceWoTax = 0;
        double componentCost = 0;
        double componentProfit = 0;
        double componentMargin = 0;



        if (handleOrKnobRate.getPrice() != 0)
        {
            componentPrice = handleOrKnobRate.getPrice() * quantity;
            componentPriceAfterDiscount = componentPrice - (componentPrice * (proposalVersion.getDiscountPercentage()/100));
            componentPriceWoTax = componentPriceAfterDiscount * afterTaxFactor;
            componentCost = handleOrKnobRate.getSourcePrice() * quantity;
            componentProfit = componentPriceWoTax - componentCost;
            if(componentProfit == 0.0 || componentPriceWoTax == 0.0){
                componentMargin = 0.0;
            }else
                componentMargin = componentProfit / componentPriceWoTax;
        }

        dwModuleComponent.setComponentPrice(componentPrice);
        dwModuleComponent.setComponentPriceAfterDiscount(componentPriceAfterDiscount);
        dwModuleComponent.setComponentPriceWoTax(componentPriceWoTax);
        dwModuleComponent.setComponentCost(componentCost);
        dwModuleComponent.setComponentProfit(componentProfit);
        dwModuleComponent.setComponentMargin(componentMargin);

        return dwModuleComponent;
    }

    private double getAfterTaxFactor(ProposalHeader proposalHeader, ProductModule productModule) {

        RateCard prodWoTaxFactor = RateCardService.getInstance().getRateCard(RateCard.PRODUCT_WO_TAX,
                RateCard.FACTOR_TYPE, proposalHeader.getPriceDate(), proposalHeader.getProjectCity());
        double woTaxFactor = 0;

        ProductCategoryMap productCategoryMap = ModuleDataService.getInstance().getProductCategoryMap(productModule.getProductCategory(),proposalHeader.getPriceDate());
        String productType = productCategoryMap.getType();

        RateCard movableFurnitureRateCard = RateCardService.getInstance().getRateCard(RateCard.MOVABLE_FURNITURE,
                RateCard.FACTOR_TYPE,proposalHeader.getPriceDate(), proposalHeader.getProjectCity());
        RateCard nonMovableFurnitureRateCard = RateCardService.getInstance().getRateCard(RateCard.NON_MOVABLE_FURNITURE,
                RateCard.FACTOR_TYPE,proposalHeader.getPriceDate(), proposalHeader.getProjectCity());


        switch (productType) {
            case RateCard.MOVABLE_FURNITURE:
                woTaxFactor = movableFurnitureRateCard.getSourcePrice();
                break;
            case RateCard.NON_MOVABLE_FURNITURE:
                woTaxFactor = nonMovableFurnitureRateCard.getSourcePrice();
                break;
            default:
                woTaxFactor = prodWoTaxFactor.getSourcePrice();
                break;
        }
        return woTaxFactor;
    }


}
