package com.mygubbi.game.proposal.model.dw;

import io.vertx.core.json.JsonObject;
import org.elasticsearch.common.recycler.Recycler;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Created by User on 08-08-2017.
 */
public class DWProposalProduct extends JsonObject {


    public static final String ID="id";
    public static final String PROPOSALID="proposalId";
    public static final String VERSION="version";
    public static final String PROPOSALTITLE="proposalTitle";
    public static final String PRICEDATE="priceDate";
    public static final String BUSINESSDATE="businessDate";
    public static final String REGION="region";
    public static final String CATEGORY="category";
    public static final String SUBCATEGORY="subCategory";
    public static final String PRID="prId";
    public static final String PRTITLE="prTitle";
    public static final String SPACETYPE="spaceType";
    public static final String ROOM="room";
    public static final String BASECARCASS="baseCarcass";
    public static final String WALLCARCASS="wallCarcass";
    public static final String FINISHMATERIAL="finishMaterial";
    public static final String FINISH="finish";
    public static final String SHUTTERDESIGN="shutterDesign";
    public static final String HINGE="hinge";
    public static final String GLASS="glass";
    public static final String HANDLESELECTION="handleSelection";
    public static final String NOOFLENGTHS="noOfLengths";
    public static final String HANDLETYPE="handleType";
    public static final String HANDLEFINISH="handleFinish";
    public static final String HANDLESIZE="handleSize";
    public static final String KNOBTYPE="knobType";
    public static final String KNOBFINISH="knobFinish";
    public static final String PRAREA="prArea";
    public static final String PRCREATEDBY="prCreatedBy";
    public static final String PRCREATEDON="prCreatedOn";
    public static final String PRUPDATEDBY="prUpdatedBy";
    public static final String PRUPDATEDON="prUpdatedOn";
    public static final String PRPRICE="prPrice";
    public static final String PRPRICEAFTERDISCOUNT="prPriceAfterDiscount";
    public static final String PRPRICEAFTERTAX="prPriceAfterTax";
    public static final String PRCOST="prCost";
    public static final String PRPROFIT="prProfit";
    public static final String PRMARGIN="prMargin";
    public static final String WWPRICE="wwPrice";
    public static final String WWPRICEAFTERTAX="wwPriceAfterTax";
    public static final String WWCOST="wwCost";
    public static final String WWPROFIT="wwProfit";
    public static final String WWMARGIN="wwMargin";
    public static final String HW_PRICE="hwPrice";
    public static final String HW_PRICEAFTERTAX="hwPriceAfterTax";
    public static final String HW_COST="hwCost";
    public static final String HW_PROFIT="hwProfit";
    public static final String HW_MARGIN="hwMargin";
    public static final String ACC_PRICE="accPrice";
    public static final String ACC_PRICEAFTERTAX="accPriceAfterTax";
    public static final String ACC_COST="accCost";
    public static final String ACC_PROFIT="accProfit";
    public static final String ACC_MARGIN="accMargin";
    public static final String HK_PRICE="hkPrice";
    public static final String HK_PRICEAFTERTAX="hkPriceAfterTax";
    public static final String HK_COST="hkCost";
    public static final String HK_PROFIT="hkProfit";
    public static final String HK_MARGIN="hkMargin";
    public static final String HINGE_PRICE="hingePrice";
    public static final String HINGE_PRICEAFTERTAX="hingePriceAfterTax";
    public static final String HINGE_COST="hingeCost";
    public static final String HINGE_PROFIT="hingeProfit";
    public static final String HINGE_MARGIN="hingeMargin";
    public static final String LC_PRICE="lcPrice";
    public static final String LC_PRICEAFTERTAX="lcPriceAfterTax";
    public static final String LC_COST="lcCost";
    public static final String LC_PROFIT="lcProfit";
    public static final String LC_MARGIN="lcMargin";

    public static final String LA_PRICE="laPrice";
    public static final String LA_PRICEAFTERTAX="laPriceAfterTax";
    public static final String LA_COST="laCost";
    public static final String LA_PROFIT="laProfit";
    public static final String LA_MARGIN="laMargin";

    public static final String INSTALLATIONCOST="installationCost";
    public static final String TRANSPORTATIONCOST="transportationCost";
    public static final String STDMC="stdMC";
    public static final String STDMPRICE="stdMPrice";
    public static final String NSTDMC="nStdMC";
    public static final String NSTDMPRICE="nStdMPrice";
    public static final String HIKEMC="hikeMC";
    public static final String HIKEMPRICE="hikeMPrice";

    public DWProposalProduct() {}

    public DWProposalProduct(JsonObject jsonObject){
        super(jsonObject.getMap());
    }

    public int getID() {
        return this.getInteger(ID);
    }

    public DWProposalProduct setId(int id)
    {
        put(ID,id);
        return this;
    }

    public int getProposalId() {
        return this.getInteger(PROPOSALID);
    }

    public DWProposalProduct setProposalId(int proposalId)
    {
        put(PROPOSALID,proposalId);
        return this;
    }

    public double getVersion() {
        return this.getDouble(VERSION);
    }

    public DWProposalProduct setVersion(double version)
    {
        put(VERSION,version);
        return this;
    }

    public String getProposalTitle() {
        return this.getString(PROPOSALTITLE);
    }

    public DWProposalProduct setProposalTitle(String proposalTitle)
    {
        put(PROPOSALTITLE,proposalTitle);
        return this;
    }


    public Date getPriceDate() {
        return (Date) this.getValue(PRICEDATE);
    }

    public DWProposalProduct setPriceDate(Date priceDate)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String format = formatter.format(priceDate);
        this.put(PRICEDATE, format);
        return this;
    }

    public Date getBusinessDate() {
        return (Date) this.getValue(BUSINESSDATE);
    }

    public DWProposalProduct setBusinessDate(Date businessDate)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String format = formatter.format(businessDate);
        this.put(BUSINESSDATE, format);
        return this;
    }

    public String getRegion() {
        return this.getString(REGION);
    }

    public DWProposalProduct setRegion(String region)
    {
        put(REGION,region);
        return this;
    }

    public String getCategory() {
        return this.getString(CATEGORY);
    }

    public DWProposalProduct setCategory(String category)
    {
        put(CATEGORY,category);
        return this;
    }

    public String getSubCategory() {
        return this.getString(SUBCATEGORY);
    }

    public DWProposalProduct setSubCategory(String subCategory)
    {
        put(SUBCATEGORY,subCategory);
        return this;
    }

    public Integer getProductId() {
        return this.getInteger(PRID);
    }

    public DWProposalProduct setProductId(int productId)
    {
        put(PRID,productId);
        return this;
    }

    public String getProductTitle() {
        return this.getString(PRTITLE);
    }

    public DWProposalProduct setProductTitle(String productTitle)
    {
        put(PRTITLE,productTitle);
        return this;
    }

    public String getSpaceType() {
        return this.getString(SPACETYPE);
    }

    public DWProposalProduct setSpaceType(String spaceType)
    {
        put(SPACETYPE,spaceType);
        return this;
    }

    public String getRoom() {
        return this.getString(ROOM);
    }

    public DWProposalProduct setRoom(String room)
    {
        put(ROOM,room);
        return this;
    }

    public String getBaseCarcass() {
        return this.getString(BASECARCASS);
    }

    public DWProposalProduct setBaseCarcass(String baseCarcass)
    {
        put(BASECARCASS,baseCarcass);
        return this;
    }

    public String getWallCarcass() {
        return this.getString(WALLCARCASS);
    }

    public DWProposalProduct setWallCarcass(String wallCarcass)
    {
        put(WALLCARCASS,wallCarcass);
        return this;
    }

    public String getFinishMaterial() {
        return this.getString(FINISHMATERIAL);
    }

    public DWProposalProduct setFinishMaterial(String finishMaterial)
    {
        put(FINISHMATERIAL,finishMaterial);
        return this;
    }

    public String getFinish() {
        return this.getString(FINISH);
    }

    public DWProposalProduct setFinish(String finish)
    {
        put(FINISH,finish);
        return this;
    }

    public String getShutterDesign() {
        return this.getString(SHUTTERDESIGN);
    }

    public DWProposalProduct setShutterDesign(String shutterDesign)
    {
        put(SHUTTERDESIGN,shutterDesign);
        return this;
    }

    public String getHinge() {
        return this.getString(HINGE);
    }

    public DWProposalProduct setHinge(String hinge)
    {
        put(HINGE,hinge);
        return this;
    }

    public String getGlass() {
        return this.getString(GLASS);
    }

    public DWProposalProduct setGlass(String glass)
    {
        put(GLASS,glass);
        return this;
    }

    public String getHandleSelection() {
        return this.getString(HANDLESELECTION);
    }

    public DWProposalProduct setHandleSelection(String handleSelection)
    {
        put(HANDLESELECTION,handleSelection);
        return this;
    }

    public Integer getNoOfLengths() {
        return this.getInteger(NOOFLENGTHS);
    }

    public DWProposalProduct setNoOfLengths(int noOfLengths)
    {
        put(NOOFLENGTHS,noOfLengths);
        return this;
    }

    public String getHandleType() {
        return this.getString(HANDLETYPE);
    }

    public DWProposalProduct setHandleType(String handleType)
    {
        put(HANDLETYPE,handleType);
        return this;
    }

    public String getHandleFinish() {
        return this.getString(HANDLEFINISH);
    }

    public DWProposalProduct setHandleFinish(String handleFinish)
    {
        put(HANDLEFINISH,handleFinish);
        return this;
    }

    public double getHandleSize() {
        return this.getDouble(HANDLESIZE);
    }

    public DWProposalProduct setHandleSize(String handleFinish)
    {
        put(HANDLEFINISH,handleFinish);
        return this;
    }

    public String getKnobType() {
        return this.getString(KNOBTYPE);
    }

    public DWProposalProduct setKnobType(String knobType)
    {
        put(KNOBTYPE,knobType);
        return this;
    }

    public String getKnobFinish() {
        return this.getString(KNOBFINISH);
    }

    public DWProposalProduct setKnobFinish(String knobFinish)
    {
        put(KNOBFINISH,knobFinish);
        return this;
    }

    public double getProductArea() {
        return this.getDouble(PRAREA);
    }

    public DWProposalProduct setProductArea(double productArea)
    {
        put(PRAREA,productArea);
        return this;
    }

    public String getProductCreatedBy() {
        return this.getString(PRCREATEDBY);
    }

    public DWProposalProduct setProductCreatedBy(String productCreatedBy)
    {
        put(PRCREATEDBY,productCreatedBy);
        return this;
    }

    public Date getProductCreatedOn() {
        return (Date) this.getValue(PRCREATEDON);
    }

    public DWProposalProduct setProductCreatedOn(Date productCreatedOn) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String format = formatter.format(productCreatedOn);
        this.put(PRCREATEDON, format);
        return this;
    }



    public String getProductUpdatedBy() {
        return this.getString(PRUPDATEDBY);
    }

    public DWProposalProduct setProductUpdatedBy(String productUpdatedBy)
    {
        put(PRUPDATEDBY,productUpdatedBy);
        return this;
    }

    public Date getProductUpdatedOn() {
        return (Date) this.getValue(PRUPDATEDON);
    }

    public DWProposalProduct setProductUpdatedOn(Date productUpdatedOn) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String format = formatter.format(productUpdatedOn);
        this.put(PRUPDATEDON, format);
        return this;
    }

    public double getProductPrice() {
        return this.getDouble(PRPRICE);
    }

    public DWProposalProduct setProductPrice(double productPrice)
    {
        put(PRPRICE,productPrice);
        return this;
    }

    public double getProductPriceAfterDiscount() {
        return this.getDouble(PRPRICEAFTERDISCOUNT);
    }

    public DWProposalProduct setProductPriceAfterDiscount(double productPriceAfterDiscount)
    {
        put(PRPRICEAFTERDISCOUNT,productPriceAfterDiscount);
        return this;
    }



    public double getProductPriceAfterTax() {
        return this.getDouble(PRPRICEAFTERTAX);
    }

    public DWProposalProduct setProductPriceAfterTax(double productPriceAfterTax)
    {
        put(PRPRICEAFTERTAX,productPriceAfterTax);
        return this;
    }



    public double getProductSourceCost() {
        return this.getDouble(PRCOST);
    }

    public DWProposalProduct setProductSourceCost(double productSourceCost)
    {
        put(PRCOST,productSourceCost);
        return this;
    }



    public double getProductProfit() {
        return this.getDouble(PRPROFIT);
    }

    public DWProposalProduct setProductProfit(double productProfit)
    {
        put(PRPROFIT,productProfit);
        return this;
    }

    public double getProductMargin() {
        return this.getDouble(PRMARGIN);
    }

    public DWProposalProduct setProductMargin(double productMargin)
    {
        put(PRMARGIN,productMargin);
        return this;
    }
    public double getWoodWorkPrice() {
        return this.getDouble(WWPRICE);
    }

    public DWProposalProduct setWoodWorkPrice(double woodWorkPrice)
    {
        put(WWPRICE,woodWorkPrice);
        return this;
    }

    public double getWoodWorkPriceWoTax() {
        return this.getDouble(WWPRICEAFTERTAX);
    }

    public DWProposalProduct setWoodWorkPriceWoTax(double woodWorkPriceWoTax)
    {
        put(WWPRICEAFTERTAX,woodWorkPriceWoTax);
        return this;
    }

    public double getWoodWorkCost() {
        return this.getDouble(WWCOST);
    }

    public DWProposalProduct setWoodWorkCost(double woodWorkCost)
    {
        put(WWCOST,woodWorkCost);
        return this;
    }

    public double getWoodWorkProfit() {
        return this.getDouble(WWPROFIT);
    }

    public DWProposalProduct setWoodWorkProfit(double woodWorkProfit)
    {
        put(WWPROFIT,woodWorkProfit);
        return this;
    }

    public double getWoodWorkMargin() {
        return this.getDouble(WWMARGIN);
    }

    public DWProposalProduct setWoodWorkMargin(double woodWorkMargin)
    {
        put(WWMARGIN,woodWorkMargin);
        return this;
    }

    public double getHwPrice() {
        return this.getDouble(HW_PRICE);
    }

    public DWProposalProduct setHwPrice(double hwPrice)
    {
        put(HW_PRICE,hwPrice);
        return this;
    }

    public double getHwPriceWoTax() {
        return this.getDouble(HW_PRICEAFTERTAX);
    }

    public DWProposalProduct setHWPriceWoTax(double haPriceAfterTax)
    {
        put(HW_PRICEAFTERTAX,haPriceAfterTax);
        return this;
    }

    public double getHwSourceCost() {
        return this.getDouble(HW_COST);
    }

    public DWProposalProduct setHwSourceCost(double hwSourceCost)
    {
        put(HW_COST,hwSourceCost);
        return this;
    }

    public double getHwProfit() {
        return this.getDouble(HW_PROFIT);
    }

    public DWProposalProduct setHwProfit(double hwProfit)
    {
        put(HW_PROFIT,hwProfit);
        return this;
    }

    public double getHwMargin() {
        return this.getDouble(HW_MARGIN);
    }

    public DWProposalProduct setHwMargin(double hwMargin)
    {
        put(HW_MARGIN,hwMargin);
        return this;
    }

    public double getAccPrice() {
        return this.getDouble(ACC_PRICE);
    }

    public DWProposalProduct setAccPrice(double accPrice)
    {
        put(ACC_PRICE,accPrice);
        return this;
    }

    public double getAccPriceWoTax() {
        return this.getDouble(ACC_PRICEAFTERTAX);
    }

    public DWProposalProduct setAccPriceWoTax(double accPriceAfterTax)
    {
        put(ACC_PRICEAFTERTAX,accPriceAfterTax);
        return this;
    }

    public double getAccSourceCost() {
        return this.getDouble(ACC_COST);
    }

    public DWProposalProduct setAccSourceCost(double accSourceCost)
    {
        put(ACC_COST,accSourceCost);
        return this;
    }

    public double getAccProfit() {
        return this.getDouble(ACC_PROFIT);
    }

    public DWProposalProduct setAccProfit(double accProfit)
    {
        put(ACC_PROFIT,accProfit);
        return this;
    }

    public double getAccMargin() {
        return this.getDouble(ACC_MARGIN);
    }

    public DWProposalProduct setAccMargin(double accMargin)
    {
        put(ACC_MARGIN,accMargin);
        return this;
    }

    public double getHkPrice() {
        return this.getDouble(HK_PRICE);
    }

    public DWProposalProduct setHkPrice(double hkPrice)
    {
        put(HK_PRICE,hkPrice);
        return this;
    }

    public double getHkPriceWoTax() {
        return this.getDouble(HK_PRICEAFTERTAX);
    }

    public DWProposalProduct setHkPriceWoTax(double hkPriceAfterTax)
    {
        put(HK_PRICEAFTERTAX,hkPriceAfterTax);
        return this;
    }

    public double getHkSourceCost() {
        return this.getDouble(HK_COST);
    }

    public DWProposalProduct setHkSourceCost(double hkSourceCost)
    {
        put(HW_COST,hkSourceCost);
        return this;
    }

    public double getHkProfit() {
        return this.getDouble(HK_PROFIT);
    }

    public DWProposalProduct setHkProfit(double hkProfit)
    {
        put(HK_PROFIT,hkProfit);
        return this;
    }

    public double getHkMargin() {
        return this.getDouble(HK_MARGIN);
    }

    public DWProposalProduct setHkMargin(double hkMargin)
    {
        put(HK_MARGIN,hkMargin);
        return this;
    }

    public double getHingePrice() {
        return this.getDouble(HINGE_PRICE);
    }

    public DWProposalProduct setHingePrice(double hingePrice)
    {
        put(HINGE_PRICE,hingePrice);
        return this;
    }

    public double getHingePriceWoTax() {
        return this.getDouble(HINGE_PRICEAFTERTAX);
    }

    public DWProposalProduct setHingePriceWoTax(double hingePriceAfterTax)
    {
        put(HINGE_PRICEAFTERTAX,hingePriceAfterTax);
        return this;
    }

    public double getHingeSourceCost() {
        return this.getDouble(HINGE_COST);
    }

    public DWProposalProduct setHingeSourceCost(double hingeSourceCost)
    {
        put(HINGE_COST,hingeSourceCost);
        return this;
    }

    public double getHingeProfit() {
        return this.getDouble(HINGE_PROFIT);
    }

    public DWProposalProduct setHingeProfit(double hingeProfit)
    {
        put(HINGE_PROFIT,hingeProfit);
        return this;
    }

    public double getHingeMargin() {
        return this.getDouble(HINGE_MARGIN);
    }

    public DWProposalProduct setHingeMargin(double hingeMargin)
    {
        put(HINGE_MARGIN,hingeMargin);
        return this;
    }

    public double getLcPrice() {
        return this.getDouble(LC_PRICE);
    }

    public DWProposalProduct setLCPrice(double lcPrice)
    {
        put(LC_PRICE,lcPrice);
        return this;
    }

    public double getLcPriceWoTax() {
        return this.getDouble(LC_PRICEAFTERTAX);
    }

    public DWProposalProduct setLcPriceWoTax(double lcPriceAfterTax)
    {
        put(LC_PRICEAFTERTAX,lcPriceAfterTax);
        return this;
    }

    public double getLcSourceCost() {
        return this.getDouble(LC_COST);
    }

    public DWProposalProduct setLcSourceCost(double lcSourceCost)
    {
        put(LC_COST,lcSourceCost);
        return this;
    }

    public double getLcProfit() {
        return this.getDouble(LC_PROFIT);
    }

    public DWProposalProduct setLcProfit(double lcProfit)
    {
        put(LC_PROFIT,lcProfit);
        return this;
    }

    public double getLcMargin() {
        return this.getDouble(LC_MARGIN);
    }

    public DWProposalProduct setLcMargin(double lcMargin)
    {
        put(LC_MARGIN,lcMargin);
        return this;
    }


    public double getLaPrice() {
        return this.getDouble(LA_PRICE);
    }

    public DWProposalProduct setLaPrice(double laPrice)
    {
        put(LA_PRICE,laPrice);
        return this;
    }

    public double getLaPriceWoTax() {
        return this.getDouble(LA_PRICEAFTERTAX);
    }

    public DWProposalProduct setLaPriceWoTax(double laPriceAfterTax)
    {
        put(LA_PRICEAFTERTAX,laPriceAfterTax);
        return this;
    }

    public double getLaSourceCost() {
        return this.getDouble(LA_COST);
    }

    public DWProposalProduct setLaSourceCost(double laSourceCost)
    {
        put(LA_COST,laSourceCost);
        return this;
    }

    public double getLaProfit() {
        return this.getDouble(LA_PROFIT);
    }

    public DWProposalProduct setLaProfit(double laProfit)
    {
        put(LA_PROFIT,laProfit);
        return this;
    }

    public double getLaMargin() {
        return this.getDouble(LA_MARGIN);
    }

    public DWProposalProduct setLaMargin(double laMargin)
    {
        put(LA_MARGIN,laMargin);
        return this;
    }





    public double getInstallationCost() {
        return this.getDouble(INSTALLATIONCOST);
    }

    public DWProposalProduct setInstallationCost(double installationCost)
    {
        put(INSTALLATIONCOST,installationCost);
        return this;
    }

    public double getTransportationCost() {
        return this.getDouble(TRANSPORTATIONCOST);
    }

    public DWProposalProduct setTransportationCost(double transportationCost)
    {
        put(TRANSPORTATIONCOST,transportationCost);
        return this;
    }


    public int getStdModuleCount() {
        return this.getInteger(STDMC);
    }

    public DWProposalProduct setStdModuleCount(int stdModuleCount)
    {
        put(STDMC,stdModuleCount);
        return this;
    }

    public double getStdModulePrice() {
        return this.getDouble(STDMPRICE);
    }

    public DWProposalProduct setStdModulePrice(double stdModulePrice)
    {
        put(STDMPRICE,stdModulePrice);
        return this;
    }

    public int getNStdModuleCount() {
        return this.getInteger(NSTDMC);
    }

    public DWProposalProduct setNStdModuleCount(double nStdModuleCount)
    {
        put(NSTDMC,nStdModuleCount);
        return this;
    }

    public double getNStdModulePrice() {
        return this.getDouble(NSTDMPRICE);
    }

    public DWProposalProduct setNStdModulePrice(double nStdModulePrice)
    {
        put(NSTDMPRICE,nStdModulePrice);
        return this;
    }

    public int getHikeModuleCount() {
        return this.getInteger(HIKEMC);
    }

    public DWProposalProduct setHikeModuleCount(int hikeModuleCount)
    {
        put(HIKEMC,hikeModuleCount);
        return this;
    }

    public double getHikeModulePrice() {
        return this.getDouble(HIKEMPRICE);
    }

    public DWProposalProduct setHikeModulePrice(double hikeModulePrice)
    {
        put(HIKEMC,hikeModulePrice);
        return this;
    }
}
