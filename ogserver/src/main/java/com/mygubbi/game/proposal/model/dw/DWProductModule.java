package com.mygubbi.game.proposal.model.dw;

import com.mygubbi.game.proposal.ModuleDataService;
import com.mygubbi.game.proposal.ProductLineItem;
import com.mygubbi.game.proposal.ProductModule;
import com.mygubbi.game.proposal.model.ProposalHeader;
import com.mygubbi.game.proposal.model.ProposalVersion;
import com.mygubbi.game.proposal.model.ShutterFinish;
import com.mygubbi.game.proposal.price.ModulePriceHolder;
import io.vertx.core.json.JsonObject;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Created by User on 07-08-2017.
 */
public class DWProductModule extends JsonObject {

    private static final String ID="id";
    private static final String PROPOSALID="proposalId";
    private static final String VERSION="version";
    private static final String PROPOSAL_TITLE="proposalTitle";
    private static final String PRICEDATE="priceDate";
    private static final String BUSINESSDATE="businessDate";
    private static final String REGION="region";
    private static final String CRMID="crmId";
    private static final String QUOTENO="quoteNo";
    private static final String DESIGNER_NAME = "designerName";
    private static final String SALES_NAME = "salesName";
    private static final String SOURCE ="source";
    private static final String FROM_PRODUCT="fromProduct";
    private static final String BEF_PROD_SPEC = "beforeProductionSpecification";
    private static final String FROM_PROPOSAL = "fromProposal";
    private static final String OFFER_TYPE = "offerType";
    private static final String PACKAGE_FLAG = "packageFlag";
    private static final String SPACETYPE="spaceType";
    private static final String ROOM="room";
    private static final String PRID="prId";
    private static final String PRTITLE="prTitle";
    private static final String PR_CATEGORY_CODE="prCategoryCode";
    private static final String MODULESEQ="moduleSeq";
    private static final String MODULECODE="moduleCode";
    private static final String DESCRIPTION="description";
    private static final String STATUS="status";
    private static final String DISCOUNT_AMOUNT="discountAmount";
    private static final String DISCOUNT_AMOUNT_PERC="discountPercentage";
    private static final String WIDTH="width";
    private static final String DEPTH="depth";
    private static final String HEIGHT="height";
    private static final String MODULECATEGORY="moduleCategory";
    private static final String HANDLESIZE="handleSize";
    private static final String HANDLEQTY="handleQty";
    private static final String KNOBTYPE="knobType";
    private static final String KNOBFINISH="knobFinish";
    private static final String CARCASS="carcass";
    private static final String FINISH="finish";
    private static final String FINISHMATERIAL="finishMaterial";
    private static final String COLOR="color";
    private static final String EXPOSEDLEFT="exposedLeft";
    private static final String EXPOSEDRIGHT="exposedRight";
    private static final String EXPOSEDBOTTOM="exposedBottom";
    private static final String EXPOSEDTOP="exposedTop";
    private static final String EXPOSEDBACK="exposedBack";
    private static final String EXPOSEDOPEN="exposedOpen";
    private static final String NOOFACCPACKS="noOfAccPacks";
    private static final String MODULEAREA="moduleArea";
    private static final String CCPRICE="ccPrice";
    private static final String CCWOTAX="ccWoTax";
    private static final String CCCOST="ccCost";
    private static final String CCPROFIT="ccProfit";
    private static final String CCMARGIN="ccMargin";
    private static final String SHPRICE="shPrice";
    private static final String SHWOTAX="shWoTax";
    private static final String SHCOST="shCost";
    private static final String SHPROFIT="shProfit";
    private static final String SHMARGIN="shMargin";
    private static final String HWPRICE="hwPrice";
    private static final String HWWOTAX="hwWoTax";
    private static final String HWCOST="hwCost";
    private static final String HWPROFIT="hwProfit";
    private static final String HWMARGIN="hwMargin";
    private static final String ACCPRICE="accPrice";
    private static final String ACCWOTAX="accWoTax";
    private static final String ACCCOST="accCost";
    private static final String ACCPROFIT="accProfit";
    private static final String ACCMARGIN="accMargin";
    private static final String HANDLEPRICE="handlePrice";
    private static final String HANDLEWOTAX="handleWoTax";
    private static final String HANDLECOST="handleCost";
    private static final String HANDLEPROFIT="handleProfit";
    private static final String HANDLEMARGIN="handleMargin";
    private static final String HINGEPRICE="hingePrice";
    private static final String HINGEWOTAX="hingeWoTax";
    private static final String HINGECOST="hingeCost";
    private static final String HINGEPROFIT="hingeProfit";
    private static final String HINGEMARGIN="hingeMargin";
    private static final String LRPRICE="lrPrice";
    private static final String LRWOTAX="lrWoTax";
    private static final String LRCOST="lrCost";
    private static final String LRPROFIT="lrProfit";
    private static final String LRMARGIN="lrMargin";
    private static final String MODULEPRICE="modulePrice";
    private static final String MODULEWOTAX="moduleWoTax";
    private static final String MODULECOST="moduleCost";
    private static final String MODULEPROFIT="moduleProfit";
    private static final String MODULEMARGIN="moduleMargin";

    private static final String OLD_MATT_SOLID_FINISH = "Matt -solid";
    private static final String OLD_MATT_WOOD_GRAIN_FINISH = "Matt- Wood grain";
    private static final String NEW_MATT_SOLID_FINISH = "MATT-SOLID";
    private static final String NEW_MATT_WOOD_GRAIN_FINISH = "MATT-WG";

    public DWProductModule() {}

    public DWProductModule(JsonObject jsonObject){
        super(jsonObject.getMap());
    }

    public String getDesignerName() {return this.getString(DESIGNER_NAME);}
    public String getSalesName() {return this.getString(SALES_NAME);}
    public String getSource() {return this.getString(SOURCE);}
    public String getFromProduct() {return this.getString(FROM_PRODUCT);}
    public String getBefProdSpec() {return this.getString(BEF_PROD_SPEC);}
    public Integer getFromProposal() {return this.getInteger(FROM_PROPOSAL);}
    public String getOfferType() {return this.getString(OFFER_TYPE);}
    public String getPackageFlag() {return this.getString(PACKAGE_FLAG);}
    public DWProductModule setDesignerName(String dname) {this.put(DESIGNER_NAME, dname);return this;}
    public DWProductModule setSalesName(String sname) {this.put(SALES_NAME, sname);return this;}
    public DWProductModule setSource(String sname) {this.put(SOURCE, sname);return this;}
    public DWProductModule setFromProduct(Integer val) {this.put(FROM_PRODUCT, val);return this;}
    public DWProductModule setBefProdSpec(String val){put(BEF_PROD_SPEC,val);return this;}
    public DWProductModule setFromProposal(Integer val){put(FROM_PROPOSAL,val);return this;}
    public DWProductModule setOfferType(String val){put(OFFER_TYPE,val);return this;}
    public DWProductModule setPackageFlag(String val){put(PACKAGE_FLAG,val);return this;}


    public DWProductModule setDwModuleObjects(ModulePriceHolder modulePriceHolder, ProposalHeader proposalHeader, ProductLineItem productLineItem, ProposalVersion proposalVersion, ProductModule productModule)
    {
        DWProductModule dwProductModule = new DWProductModule();

        dwProductModule.setProposalId(proposalHeader.getId());
        dwProductModule.setVersion(Double.parseDouble(proposalVersion.getVersion()));
        dwProductModule.setProposalTitle(proposalHeader.getQuotationFor());
        dwProductModule.setPriceDate(proposalHeader.getPriceDate());
        dwProductModule.setBusinessDate(proposalVersion.getBusinessDate());
        dwProductModule.setRegion(proposalHeader.getProjectCity());
        dwProductModule.setCrmId(proposalHeader.getCrmId());
        dwProductModule.setQuoteNo(proposalHeader.getQuoteNumNew());
        dwProductModule.setSalesName(proposalHeader.getSalespersonName());
        dwProductModule.setSource(productLineItem.getSource());
        dwProductModule.setFromProduct(productLineItem.getFromProduct());
        dwProductModule.setBefProdSpec(proposalHeader.getBefProdSpec());
        dwProductModule.setFromProposal(proposalHeader.getFromProposal());
        dwProductModule.setOfferType(proposalHeader.getOfferType());
        dwProductModule.setPackageFlag(proposalHeader.getPackageFlag());
        dwProductModule.setDesignerName(proposalHeader.getDesignerName());
        dwProductModule.setSpaceType(productLineItem.getSpaceType());
        dwProductModule.setRoom(productLineItem.getRoomCode());
        dwProductModule.setPrId(productLineItem.getId());
        dwProductModule.setPrTitle(productLineItem.getTitle());
        dwProductModule.setPrCategoryCode(productLineItem.getProductCategory());
        dwProductModule.setModuleSeq(productModule.getModuleSequence());
        dwProductModule.setModuleCode(productModule.getMGCode());
        dwProductModule.setDescription(productModule.getDescription());
        dwProductModule.setDiscountAmount(proposalVersion.getDiscountAmount());
        dwProductModule.setDiscountAmountPerc(proposalVersion.getDiscountPercentage());
        dwProductModule.setStatus(proposalVersion.getProposalStatus());
        dwProductModule.setWidth(productModule.getWidth());
        dwProductModule.setHeight(productModule.getHeight());
        dwProductModule.setDepth(productModule.getDepth());
        dwProductModule.setModuleCategory(productModule.getModuleCategory());
        dwProductModule.setHandleSize(Double.parseDouble(productModule.getHandleThickness()));
        dwProductModule.setHandleQty(productModule.getHandleQuantity());
        dwProductModule.setKnobType(productLineItem.getKnobType());
        dwProductModule.setKnobFinish(productLineItem.getKnobFinish());
        dwProductModule.setCarcass(productModule.getCarcassCode());

        String finishCode = productModule.getFinishCode();
        /*if(finishCode.equalsIgnoreCase(OLD_MATT_SOLID_FINISH)){
            finishCode = NEW_MATT_SOLID_FINISH;
        }
        if(finishCode.equalsIgnoreCase(OLD_MATT_WOOD_GRAIN_FINISH)){
            finishCode = NEW_MATT_WOOD_GRAIN_FINISH;
        }*/

        ShutterFinish shutterFinish = ModuleDataService.getInstance().getFinish(finishCode);


        dwProductModule.setFinish(shutterFinish.getTitle());
        dwProductModule.setFinishMaterial(productModule.getFinishType());
        dwProductModule.setColor(productModule.getColorCode());
        dwProductModule.setExposedLeft(productModule.getLeftExposed() ? "Yes" : "No");
        dwProductModule.setExposedRight(productModule.getRightExposed() ? "Yes" : "No");
        dwProductModule.setExposedBottom(productModule.getBottomExposed() ? "Yes" : "No");
        dwProductModule.setExposedTop(productModule.getTopExposed() ? "Yes" : "No");
        dwProductModule.setExposedBack(productModule.getBackExposed() ? "Yes" : "No");
        dwProductModule.setExposedOpen(productModule.getOpenUnit() ? "Yes" : "No");
        dwProductModule.setNoOfAccPacks(modulePriceHolder.getNoOfAccPacks());
        dwProductModule.setModuleArea(productModule.getAreaOfModuleInSft());
        dwProductModule.setCarcassPrice(modulePriceHolder.getCarcassCost());
        dwProductModule.setCarcassWoTax(modulePriceHolder.getCarcassCostWoTax());
        dwProductModule.setCarcassCost(modulePriceHolder.getCarcassSourceCost());
        dwProductModule.setCarcassProfit(modulePriceHolder.getCarcassProfit());
        dwProductModule.setCarcassMargin(modulePriceHolder.getCarcassMargin());
        dwProductModule.setShutterPrice(modulePriceHolder.getShutterCost());
        dwProductModule.setShutterWOTax(modulePriceHolder.getShutterCostWoTax());
        dwProductModule.setShutterCost(modulePriceHolder.getShutterSourceCost());
        dwProductModule.setShutterProfit(modulePriceHolder.getShutterProfit());
        dwProductModule.setShutterMargin(modulePriceHolder.getShutterMargin());
        dwProductModule.setLabourPrice(modulePriceHolder.getLabourCost());
        dwProductModule.setLabourPriceWoTax(modulePriceHolder.getLabourCostWoTax());
        dwProductModule.setLabourCost(modulePriceHolder.getLabourSourceCost());
        dwProductModule.setLabourProfit(modulePriceHolder.getLabourProfit());
        dwProductModule.setLabourMargin(modulePriceHolder.getLabourMargin());
        dwProductModule.setHandlePrice(modulePriceHolder.getHandleandKnobCost());
        dwProductModule.setHandlePriceWoTax(modulePriceHolder.getHandleandKnobCostWoTax());
        dwProductModule.setHandleCost(modulePriceHolder.getHandleandKnobSourceCost());
        dwProductModule.setHandleProfit(modulePriceHolder.getHandleandKnobProfit());
        dwProductModule.setHandleMargin(modulePriceHolder.getHandleandKnobMargin());
        dwProductModule.setHingePrice(modulePriceHolder.getHingeCost());
        dwProductModule.setHingeWoTax(modulePriceHolder.getHingeCostWoTax());
        dwProductModule.setHingeCost(modulePriceHolder.getHingeSourceCost());
        dwProductModule.setHingeProfit(modulePriceHolder.getHingeProfit());
        dwProductModule.setHingeMargin(modulePriceHolder.getHingeMargin());
        dwProductModule.setHardwarePrice(modulePriceHolder.getHardwareCost());
        dwProductModule.setHardwareWoTax(modulePriceHolder.getHardwareCostWoTax());
        dwProductModule.setHardwareCost(modulePriceHolder.getHardwareSourceCost());
        dwProductModule.setHardwareProfit(modulePriceHolder.getHardwareProfit());
        dwProductModule.setHardwareMargin(modulePriceHolder.getHardwareMargin());
        dwProductModule.setAccessoryPrice(modulePriceHolder.getAccessoryCost());
        dwProductModule.setAccessoryPriceWoTax(modulePriceHolder.getAccessoryCostWoTax());
        dwProductModule.setAccessoryCost(modulePriceHolder.getAccessorySourceCost());
        dwProductModule.setAccessoryProfit(modulePriceHolder.getAccessoryProfit());
        dwProductModule.setAccessoryMargin(modulePriceHolder.getAccessoryMargin());
        dwProductModule.setModulePrice(modulePriceHolder.getTotalCost());
        dwProductModule.setModulePriceWoTax(modulePriceHolder.getTotalCostWoTax());
        dwProductModule.setModuleCost(modulePriceHolder.getTotalSourceCost());
        dwProductModule.setModuleProfit(modulePriceHolder.getTotalProfit());
        dwProductModule.setModuleMargin(modulePriceHolder.getTotalMargin());


        return dwProductModule;
    }


    public int getID() {
        return this.getInteger(ID);
    }

    public DWProductModule setID(int id)
    {
        put(ID,id);
        return this;
    }

    public String getCrmid() {
        return this.getString(CRMID);
    }

    public DWProductModule setCrmId(String crm)
    {
        put(CRMID,crm);
        return this;
    }

    public String getQuotenoo() {
        return this.getString(QUOTENO);
    }

    public DWProductModule setQuoteNo(String quote)
    {
        put(QUOTENO,quote);
        return this;
    }

    public int getProposalId() {
        return this.getInteger(PROPOSALID);
    }

    public DWProductModule setProposalId(int proposalId)
    {
        put(PROPOSALID,proposalId);
        return this;
    }

    public String getVersion() {
        return this.getString(VERSION);
    }

    public DWProductModule setVersion(double version)
    {
        put(VERSION,version);
        return this;
    }

    public String getProposalTitle() {
        return this.getString(PROPOSAL_TITLE);
    }

    public DWProductModule setProposalTitle(String proposalTitle)
    {
        put(PROPOSAL_TITLE,proposalTitle);
        return this;
    }

    public Date getPriceDate() {
        return (Date) this.getValue(PRICEDATE);
    }


    public DWProductModule setPriceDate(Date priceDate)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String format = formatter.format(priceDate);
        this.put(PRICEDATE, format);
        return this;
    }

    public Date getBusinessDate() {
        return (Date) this.getValue(PRICEDATE);
    }

    public DWProductModule setBusinessDate(Date businessDate)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String format = formatter.format(businessDate);
        this.put(BUSINESSDATE, format);
        return this;
    }

    public String getRegion() {
        return this.getString(REGION);
    }

    public DWProductModule setRegion(String region)
    {
        put(REGION,region);
        return this;
    }

    public String getSpacetype() {
        return this.getString(SPACETYPE);
    }

    public DWProductModule setSpaceType(String spaceType)
    {
        put(SPACETYPE,spaceType);
        return this;
    }

    public String getRoom() {
        return this.getString(ROOM);
    }

    public DWProductModule setRoom(String room)
    {
        put(ROOM,room);
        return this;
    }

    public int getPrId() {
        return this.getInteger(PRID);
    }

    public DWProductModule setPrId(int prId)
    {
        put(PRID,prId);
        return this;
    }



    public String getPrTitle() {
        return this.getString(PRTITLE);
    }

    public DWProductModule setPrTitle(String prTitle)
    {
        put(PRTITLE,prTitle);
        return this;
    }

    public String getPrCatergoryCode() {
        return this.getString(PR_CATEGORY_CODE);
    }

    public DWProductModule setPrCategoryCode(String prCode)
    {
        put(PR_CATEGORY_CODE,prCode);
        return this;
    }


    public int getModuleSeq() {
        return this.getInteger(MODULESEQ);
    }

    public DWProductModule setModuleSeq(int moduleSeq)
    {
        put(MODULESEQ,moduleSeq);
        return this;
    }

    public String getModuleCode() {
        return this.getString(MODULECODE);
    }

    public DWProductModule setModuleCode(String moduleCode)
    {
        put(MODULECODE,moduleCode);
        return this;
    }

    public String getStatus() {
        return this.getString(STATUS);
    }

    public DWProductModule setStatus(String description)
    {
        put(STATUS,description);
        return this;
    }

    public String getDiscountAmount() {
        return this.getString(DISCOUNT_AMOUNT);
    }

    public DWProductModule setDiscountAmount(double price) {
        this.put(DISCOUNT_AMOUNT, price);
        return this;
    }

    public double getDiscountAmountPerc() {
        return this.getDouble(DISCOUNT_AMOUNT_PERC);
    }

    public DWProductModule setDiscountAmountPerc(double price) {
        this.put(DISCOUNT_AMOUNT_PERC, price);
        return this;
    }


    public String getDescription() {
        return this.getString(DESCRIPTION);
    }

    public DWProductModule setDescription(String description)
    {
        put(DESCRIPTION,description);
        return this;
    }

    public double getWidth() {
        return this.getDouble(WIDTH);
    }

    public DWProductModule setWidth(double width)
    {
        put(WIDTH,width);
        return this;
    }

    public double getDepth() {
        return this.getDouble(DEPTH);
    }

    public DWProductModule setDepth(double depth)
    {
        put(DEPTH,depth);
        return this;
    }

    public double getHeight() {
        return this.getDouble(HEIGHT);
    }

    public DWProductModule setHeight(double height)
    {
        put(HEIGHT,height);
        return this;
    }

    public String getModuleCategory() {
        return this.getString(MODULECATEGORY);
    }

    public DWProductModule setModuleCategory(String moduleCategory)
    {
        put(MODULECATEGORY,moduleCategory);
        return this;
    }

    public double getHandleSize() {
        return this.getDouble(HANDLESIZE);
    }

    public DWProductModule setHandleSize(double handleSize)
    {
        put(HANDLESIZE,handleSize);
        return this;
    }

    public double getHandleQty() {
        return this.getDouble(HANDLEQTY);
    }

    public DWProductModule setHandleQty(double handleQty)
    {
        put(HANDLEQTY,handleQty);
        return this;
    }

    public String getKnobType() {
        return this.getString(KNOBTYPE);
    }

    public DWProductModule setKnobType(String knobType)
    {
        put(KNOBTYPE,knobType);
        return this;
    }

    public String getKnobFinish() {
        return this.getString(KNOBFINISH);
    }

    public DWProductModule setKnobFinish(String knobFinish)
    {
        put(KNOBFINISH,knobFinish);
        return this;
    }

    public String getCarcass() {
        return this.getString(CARCASS);
    }

    public DWProductModule setCarcass(String carcass)
    {
        put(CARCASS,carcass);
        return this;
    }

    public String getFinish() {
        return this.getString(FINISH);
    }

    public DWProductModule setFinish(String finish)
    {
        put(FINISH,finish);
        return this;
    }

    public String getFinishMaterial() {
        return this.getString(FINISHMATERIAL);
    }

    public DWProductModule setFinishMaterial(String finishMaterial)
    {
        put(FINISHMATERIAL,finishMaterial);
        return this;
    }

    public String getColor() {
        return this.getString(COLOR);
    }

    public DWProductModule setColor(String color)
    {
        put(COLOR,color);
        return this;
    }

    public String getExposedLeft() {
        return this.getString(EXPOSEDLEFT);
    }

    public DWProductModule setExposedLeft(String exposedLeft)
    {
        put(EXPOSEDLEFT,exposedLeft);
        return this;
    }

    public String getExposedRight() {
        return this.getString(EXPOSEDRIGHT);
    }

    public DWProductModule setExposedRight(String exposedRight)
    {
        put(EXPOSEDRIGHT,exposedRight);
        return this;
    }

    public String getExposedBottom() {
        return this.getString(EXPOSEDBOTTOM);
    }

    public DWProductModule setExposedBottom(String exposedBottom)
    {
        put(EXPOSEDBOTTOM,exposedBottom);
        return this;
    }

    public String getExposedTop() {
        return this.getString(EXPOSEDTOP);
    }

    public DWProductModule setExposedTop(String exposedTop)
    {
        put(EXPOSEDTOP,exposedTop);
        return this;
    }

    public String getExposedBack() {
        return this.getString(EXPOSEDBACK);
    }

    public DWProductModule setExposedBack(String exposedBack)
    {
        put(EXPOSEDBACK,exposedBack);
        return this;
    }

    public String getExposedOpen() {
        return this.getString(EXPOSEDOPEN);
    }

    public DWProductModule setExposedOpen(String exposedOpen)
    {
        put(EXPOSEDOPEN,exposedOpen);
        return this;
    }

    public int getNoOfAccPacks() {
        return this.getInteger(NOOFACCPACKS);
    }

    public DWProductModule setNoOfAccPacks(int setNoOfAccPacks)
    {
        put(NOOFACCPACKS,setNoOfAccPacks);
        return this;
    }

    public double getModuleArea() {
        return this.getDouble(MODULEAREA);
    }

    public DWProductModule setModuleArea(double moduleArea)
    {
        put(MODULEAREA,moduleArea);
        return this;
    }

    public double getCarcassPrice() {
        return this.getDouble(CCPRICE);
    }

    public DWProductModule setCarcassPrice(double carcassPrice)
    {
        put(CCPRICE,carcassPrice);
        return this;
    }

    public double getCarcassWoTax() {
        return this.getDouble(CCWOTAX);
    }

    public DWProductModule setCarcassWoTax(double carcassWoTax)
    {
        put(CCWOTAX,carcassWoTax);
        return this;
    }

    public double getCarcassCost() {
        return this.getDouble(CCCOST);
    }

    public DWProductModule setCarcassCost(double carcassCost)
    {
        put(CCCOST,carcassCost);
        return this;
    }

    public double getCarcassProfit() {
        return this.getDouble(CCPROFIT);
    }

    public DWProductModule setCarcassProfit(double carcassProfit)
    {
        put(CCPROFIT,carcassProfit);
        return this;
    }

    public double getCarcassMargin() {
        return this.getDouble(CCMARGIN);
    }

    public DWProductModule setCarcassMargin(double carcassMargin)
    {
        put(CCMARGIN,carcassMargin);
        return this;
    }

    public double getShutterPrice() {
        return this.getDouble(SHPRICE);
    }

    public DWProductModule setShutterPrice(double shutterPrice)
    {
        put(SHPRICE,shutterPrice);
        return this;
    }

    public double getShutterWoTax() {
        return this.getDouble(SHWOTAX);
    }

    public DWProductModule setShutterWOTax(double shutterWOTax)
    {
        put(SHWOTAX,shutterWOTax);
        return this;
    }

    public double getShutterCost() {
        return this.getDouble(SHCOST);
    }

    public DWProductModule setShutterCost(double shutterCost)
    {
        put(SHCOST,shutterCost);
        return this;
    }

    public double getShutterProfit() {
        return this.getDouble(SHPROFIT);
    }

    public DWProductModule setShutterProfit(double shutterProfit)
    {
        put(SHPROFIT,shutterProfit);
        return this;
    }

    public double getShutterMargin() {
        return this.getDouble(SHMARGIN);
    }

    public DWProductModule setShutterMargin(double shutterMargin)
    {
        put(SHMARGIN,shutterMargin);
        return this;
    }

    public double getHardwarePrice() {
        return this.getDouble(HWPRICE);
    }

    public DWProductModule setHardwarePrice(double hardwarePrice)
    {
        put(HWPRICE,hardwarePrice);
        return this;
    }

    public double getHardwareWoTax() {
        return this.getDouble(HWWOTAX);
    }

    public DWProductModule setHardwareWoTax(double hardwareWoTax)
    {
        put(HWWOTAX,hardwareWoTax);
        return this;
    }

    public double getHardwareCost() {
        return this.getDouble(HWCOST);
    }

    public DWProductModule setHardwareCost(double hardwareCost)
    {
        put(HWCOST,hardwareCost);
        return this;
    }

    public double getHardwareProfit() {
        return this.getDouble(HWPROFIT);
    }

    public DWProductModule setHardwareProfit(double hardwareProfit)
    {
        put(HWPROFIT,hardwareProfit);
        return this;
    }

    public double getHardwareMargin() {
        return this.getDouble(HWMARGIN);
    }

    public DWProductModule setHardwareMargin(double hardwareMargin)
    {
        put(HWMARGIN,hardwareMargin);
        return this;
    }

    public double getAccessoryPrice() {
        return this.getDouble(ACCPRICE);
    }

    public DWProductModule setAccessoryPrice(double accessoryPrice)
    {
        put(ACCPRICE,accessoryPrice);
        return this;
    }

    public double getAccessoryPriceWoTax() {
        return this.getDouble(ACCWOTAX);
    }

    public DWProductModule setAccessoryPriceWoTax(double accessoryPriceWoTax)
    {
        put(ACCWOTAX,accessoryPriceWoTax);
        return this;
    }

    public double getAccessoryCost() {
        return this.getDouble(ACCCOST);
    }

    public DWProductModule setAccessoryCost(double accessoryCost)
    {
        put(ACCCOST,accessoryCost);
        return this;
    }

    public double getAccessoryProfit() {
        return this.getDouble(ACCPROFIT);
    }

    public DWProductModule setAccessoryProfit(double accessoryProfit)
    {
        put(ACCPROFIT,accessoryProfit);
        return this;
    }

    public double getAccessoryMargin() {
        return this.getDouble(ACCMARGIN);
    }

    public DWProductModule setAccessoryMargin(double accessoryMargin)
    {
        put(ACCMARGIN,accessoryMargin);
        return this;
    }

    public double getHandlePrice() {
        return this.getDouble(HANDLEPRICE);
    }

    public DWProductModule setHandlePrice(double handlePrice)
    {
        put(HANDLEPRICE,handlePrice);
        return this;
    }

    public double getHandlePriceWoTax() {
        return this.getDouble(HANDLEWOTAX);
    }

    public DWProductModule setHandlePriceWoTax(double handlePriceWoTax)
    {
        put(HANDLEWOTAX,handlePriceWoTax);
        return this;
    }

    public double getHandleCost() {
        return this.getDouble(HANDLECOST);
    }

    public DWProductModule setHandleCost(double handleCost)
    {
        put(HANDLECOST,handleCost);
        return this;
    }

    public double getHandleProfit() {
        return this.getDouble(HANDLEPROFIT);
    }

    public DWProductModule setHandleProfit(double handleProfit)
    {
        put(HANDLEPROFIT,handleProfit);
        return this;
    }

    public double getHandleMargin() {
        return this.getDouble(HANDLEMARGIN);
    }

    public DWProductModule setHandleMargin(double handleMargin)
    {
        put(HANDLEMARGIN,handleMargin);
        return this;
    }

    public double getHingePrice() {
        return this.getDouble(HINGEPRICE);
    }

    public DWProductModule setHingePrice(double hingePrice)
    {
        put(HINGEPRICE,hingePrice);
        return this;
    }

    public double getHingeWoTax() {
        return this.getDouble(HINGEWOTAX);
    }

    public DWProductModule setHingeWoTax(double hingeWoTax)
    {
        put(HINGEWOTAX,hingeWoTax);
        return this;
    }

    public double getHingeCost() {
        return this.getDouble(HINGECOST);
    }

    public DWProductModule setHingeCost(double hingeCost)
    {
        put(HINGECOST,hingeCost);
        return this;
    }

    public double getHingeProfit() {
        return this.getDouble(HINGEPROFIT);
    }

    public DWProductModule setHingeProfit(double hingeProfit)
    {
        put(HINGEPROFIT,hingeProfit);
        return this;
    }

    public double getHingeMargin() {
        return this.getDouble(HINGEMARGIN);
    }

    public DWProductModule setHingeMargin(double hingeMargin)
    {
        put(HINGEMARGIN,hingeMargin);
        return this;
    }

    public double getLabourPrice() {
        return this.getDouble(LRPRICE);
    }

    public DWProductModule setLabourPrice(double labourPrice)
    {
        put(LRPRICE,labourPrice);
        return this;
    }

    public double getLabourPriceWoTax() {
        return this.getDouble(LRWOTAX);
    }

    public DWProductModule setLabourPriceWoTax(double labourPriceWoTax)
    {
        put(LRWOTAX,labourPriceWoTax);
        return this;
    }

    public double getLabourCost() {
        return this.getDouble(LRCOST);
    }

    public DWProductModule setLabourCost(double labourCost)
    {
        put(LRCOST,labourCost);
        return this;
    }

    public double getLabourProfit() {
        return this.getDouble(LRPROFIT);
    }

    public DWProductModule setLabourProfit(double labourProfit)
    {
        put(LRPROFIT,labourProfit);
        return this;
    }

    public double getLabourMargin() {
        return this.getDouble(LRMARGIN);
    }

    public DWProductModule setLabourMargin(double labourMargin)
    {
        put(LRMARGIN,labourMargin);
        return this;
    }

    public double getModulePrice() {
        return this.getDouble(MODULEPRICE);
    }

    public DWProductModule setModulePrice(double modulePrice)
    {
        put(MODULEPRICE,modulePrice);
        return this;
    }

    public double getModulePriceWoTax() {
        return this.getDouble(MODULEWOTAX);
    }

    public DWProductModule setModulePriceWoTax(double modulePriceWoTax)
    {
        put(MODULEWOTAX,modulePriceWoTax);
        return this;
    }

    public double getModuleCost() {
        return this.getDouble(MODULECOST);
    }

    public DWProductModule setModuleCost(double moduleCost)
    {
        put(MODULECOST,moduleCost);
        return this;
    }

    public double getModuleProfit() {
        return this.getDouble(MODULEPROFIT);
    }

    public DWProductModule setModuleProfit(double moduleProfit)
    {
        put(MODULEPROFIT,moduleProfit);
        return this;
    }

    public double getModuleMargin() {
        return this.getDouble(MODULEMARGIN);
    }

    public DWProductModule setModuleMargin(double moduleMargin)
    {
        put(MODULEMARGIN,moduleMargin);
        return this;
    }
}
