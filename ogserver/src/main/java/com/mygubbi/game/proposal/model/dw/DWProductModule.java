package com.mygubbi.game.proposal.model.dw;

import io.vertx.core.json.JsonObject;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Created by User on 07-08-2017.
 */
public class DWProductModule extends JsonObject {

    public static final String ID="id";
    public static final String PROPOSALID="proposalId";
    public static final String VERSION="version";
    public static final String PROPOSAL_TITLE="proposalTitle";
    public static final String PRICEDATE="priceDate";
    public static final String BUSINESSDATE="businessDate";
    public static final String REGION="region";
    public static final String SPACETYPE="spaceType";
    public static final String ROOM="room";
    public static final String PRID="prId";
    public static final String PRTITLE="prTitle";
    public static final String MODULESEQ="moduleSeq";
    public static final String MODULECODE="moduleCode";
    public static final String DESCRIPTION="description";
    public static final String WIDTH="width";
    public static final String DEPTH="depth";
    public static final String HEIGHT="height";
    public static final String MODULECATEGORY="moduleCategory";
    public static final String HANDLESIZE="handleSize";
    public static final String HANDLEQTY="handleQty";
    public static final String CARCASS="carcass";
    public static final String FINISH="finish";
    public static final String FINISHMATERIAL="finishMaterial";
    public static final String COLOR="color";
    public static final String EXPOSEDLEFT="exposedLeft";
    public static final String EXPOSEDRIGHT="exposedRight";
    public static final String EXPOSEDBOTTOM="exposedBottom";
    public static final String EXPOSEDTOP="exposedTop";
    public static final String EXPOSEDBACK="exposedBack";
    public static final String EXPOSEDOPEN="exposedOpen";
    public static final String NOOFACCPACKS="noOfAccPacks";
    public static final String MODULEAREA="moduleArea";
    public static final String CCPRICE="ccPrice";
    public static final String CCWOTAX="ccWoTax";
    public static final String CCCOST="ccCost";
    public static final String CCPROFIT="ccProfit";
    public static final String CCMARGIN="ccMargin";
    public static final String SHPRICE="shPrice";
    public static final String SHWOTAX="shWoTax";
    public static final String SHCOST="shCost";
    public static final String SHPROFIT="shProfit";
    public static final String SHMARGIN="shMargin";
    public static final String HWPRICE="hwPrice";
    public static final String HWWOTAX="hwWoTax";
    public static final String HWCOST="hwCost";
    public static final String HWPROFIT="hwProfit";
    public static final String HWMARGIN="hwMargin";
    public static final String ACCPRICE="accPrice";
    public static final String ACCWOTAX="accWoTax";
    public static final String ACCCOST="accCost";
    public static final String ACCPROFIT="accProfit";
    public static final String ACCMARGIN="accMargin";
    public static final String HANDLEPRICE="handlePrice";
    public static final String HANDLEWOTAX="handleWoTax";
    public static final String HANDLECOST="handleCost";
    public static final String HANDLEPROFIT="handleProfit";
    public static final String HANDLEMARGIN="handleMargin";
    public static final String HINGEPRICE="hingePrice";
    public static final String HINGEWOTAX="hingeWoTax";
    public static final String HINGECOST="hingeCost";
    public static final String HINGEPROFIT="hingeProfit";
    public static final String HINGEMARGIN="hingeMargin";
    public static final String LRPRICE="lrPrice";
    public static final String LRWOTAX="lrWoTax";
    public static final String LRCOST="lrCost";
    public static final String LRPROFIT="lrProfit";
    public static final String LRMARGIN="lrMargin";
    public static final String MODULEPRICE="modulePrice";
    public static final String MODULEWOTAX="moduleWoTax";
    public static final String MODULECOST="moduleCost";
    public static final String MODULEPROFIT="moduleProfit";
    public static final String MODULEMARGIN="moduleMargin";

    public DWProductModule() {}

    public DWProductModule(JsonObject jsonObject){
        super(jsonObject.getMap());
    }


    public int getID() {
        return this.getInteger(ID);
    }

    public DWProductModule setID(int id)
    {
        put(ID,id);
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
