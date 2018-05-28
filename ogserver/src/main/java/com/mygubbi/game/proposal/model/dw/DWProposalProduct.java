package com.mygubbi.game.proposal.model.dw;

import com.mygubbi.game.proposal.ModuleDataService;
import com.mygubbi.game.proposal.ProductLineItem;
import com.mygubbi.game.proposal.model.ProposalHeader;
import com.mygubbi.game.proposal.model.ProposalVersion;
import com.mygubbi.game.proposal.model.ShutterFinish;
import com.mygubbi.game.proposal.price.ProductPriceHolder;
import io.vertx.core.json.JsonObject;
import org.elasticsearch.common.recycler.Recycler;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Created by User on 08-08-2017.
 */
public class DWProposalProduct extends JsonObject {


     private  static final String ID="id";
     private  static final String PROPOSALID="proposalId";
     private static final String VERSION="version";
     private static final String PROPOSALTITLE="proposalTitle";
     private static final String PRICEDATE="priceDate";
     private static final String BUSINESSDATE="businessDate";
     private static final String REGION="region";
     private static final String STATUS="status";
     private static final String DISCOUNT_AMOUNT="discountAmount";
     private static final String DISCOUNT_AMOUNT_PERC="discountPercentage";
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
    private static final String CATEGORY="category";
    private static final String SUBCATEGORY="subCategory";
    private static final String PRID="prId";
    private static final String PRTITLE="prTitle";
    private static final String SPACETYPE="spaceType";
    private static final String ROOM="room";
    private static final String BASECARCASS="baseCarcass";
    private static final String WALLCARCASS="wallCarcass";
     private static final String FINISHMATERIAL="finishMaterial";
     private static final String FINISH="finish";
     private static final String SHUTTERDESIGN="shutterDesign";
     private static final String HINGE="hinge";
     private static final String GLASS="glass";
     private static final String HANDLESELECTION="handleSelection";
     private static final String COLOR="color";
     private static final String NOOFLENGTHS="noOfLengths";
     private static final String HANDLETYPE="handleType";
     private static final String HANDLEFINISH="handleFinish";
     private static final String HANDLESIZE="handleSize";
     private static final String KNOBTYPE="knobType";
     private static final String KNOBFINISH="knobFinish";
     private static final String PRAREA="prArea";
     private static final String PR_NET_AREA="prNetArea";
     private static final String PRCREATEDBY="prCreatedBy";
     private static final String PRCREATEDON="prCreatedOn";
     private static final String PRUPDATEDBY="prUpdatedBy";
    private static final String PRUPDATEDON="prUpdatedOn";
     private static final String PRPRICE="prPrice";
     private static final String PRPRICEAFTERDISCOUNT="prPriceAfterDiscount";
     private static final String PRPRICEAFTERTAX="prPriceAfterTax";
     private static final String PRCOST="prCost";
     private static final String PRPROFIT="prProfit";
     private static final String PRMARGIN="prMargin";
     private static final String WWPRICE="wwPrice";
     private static final String WWPRICEAFTERTAX="wwPriceAfterTax";
     private static final String WWCOST="wwCost";
     private static final String WWPROFIT="wwProfit";
     private static final String WWMARGIN="wwMargin";
     private static final String WWPRICEAFTERDISCOUNT="wwPriceAfterDiscount";
     private static final String HW_PRICE="hwPrice";
     private static final String HW_PRICEAFTERTAX="hwPriceAfterTax";
     private static final String HW_COST="hwCost";
     private static final String HW_PROFIT="hwProfit";
     private static final String HW_MARGIN="hwMargin";
     private static final String HWPRICEAFTERDISCOUNT="hwPriceAfterDiscount";
     private static final String ACC_PRICE="accPrice";
     private static final String ACC_PRICEAFTERTAX="accPriceAfterTax";
     private static final String ACC_COST="accCost";
     private static final String ACC_PROFIT="accProfit";
     private static final String ACC_MARGIN="accMargin";
     private static final String ACC_PRICEAFTERDISCOUNT="accPriceAfterDiscount";
     private static final String HK_PRICE="hkPrice";
     private static final String HK_PRICEAFTERTAX="hkPriceAfterTax";
     private static final String HK_COST="hkCost";
     private static final String HK_PROFIT="hkProfit";
     private static final String HK_MARGIN="hkMargin";
     private static final String HK_PRICE_AFTER_DISCOUNT="hkPriceAfterDiscount";
     private static final String HINGE_PRICE="hingePrice";
     private static final String HINGE_PRICEAFTERTAX="hingePriceAfterTax";
     private static final String HINGE_COST="hingeCost";
     private static final String HINGE_PROFIT="hingeProfit";
     private static final String HINGE_MARGIN="hingeMargin";
     private static final String HINGE_PRICE_AFTER_DISCOUNT="hingePriceAfterDiscount";
     private static final String LC_PRICE="lcPrice";
     private static final String LC_PRICEAFTERTAX="lcPriceAfterTax";
     private static final String LC_COST="lcCost";
     private static final String LC_PROFIT="lcProfit";
     private static final String LC_MARGIN="lcMargin";
     private static final String LC_PRICE_AFTER_DISCOUNT="lcPriceAfterDiscount";
     private static final String LA_PRICE="laPrice";
     private static final String LA_PRICEAFTERTAX="laPriceAfterTax";
     private static final String LA_COST="laCost";
     private static final String LA_PROFIT="laProfit";
     private static final String LA_MARGIN="laMargin";
     private static final String LA_PRICEAFTERDISCOUNT="laPriceAfterDiscount";
     private static final String INSTALLATIONCOST="installationCost";
     private static final String TRANSPORTATIONCOST="transportationCost";
     private static final String STDMC="stdModuleCount";
     private static final String STDMPRICE="stdMPrice";
     private static final String NSTDMC="nStdModuleCount";
     private static final String NSTDMPRICE="nStdMPrice";
     private static final String HIKEMC="hikeModuleCount";
     private static final String HIKEMPRICE="hikeMPrice";
     private static final String HIKEMCOST="hikeMCost";

     private static final String OLD_MATT_SOLID_FINISH = "Matt -solid";
     private static final String OLD_MATT_WOOD_GRAIN_FINISH = "Matt- Wood grain";
     private static final String NEW_MATT_SOLID_FINISH = "MATT-SOLID";
     private static final String NEW_MATT_WOOD_GRAIN_FINISH = "MATT-WG";

    private static final String CUSTOMER_ID =   "customerId" ;
    private static final String CNAME =         "cname" ;
    private static final String CADDRESS1 =     "caddress1" ;
    private static final String CADDRESS2 =     "caddress2" ;
    private static final String CADDRESS3 =     "caddress3" ;
    private static final String CCITY =         "ccity" ;
    private static final String CEMAIL =        "cemail" ;
    private static final String CPHONE1 =       "cphone1" ;
    private static final String CPHONE2 =       "cphone2" ;
    private static final String PADDRESS1 =     "paddress1" ;
    private static final String PADDRESS2 =     "paddress2" ;
    private static final String PCITY =         "pcity" ;
    private static final String SALES_EMAIL =   "salesEmail" ;
    private static final String SALES_PHONE =   "salesPhone" ;
    private static final String DESIGNER_EMAIL ="designerEmail" ;
    private static final String DESIGNER_PHONE ="designerPhone" ;
    private static final String PROJECT_NAME = "projectName";
    private static final String EXPECTED_DELIVERY_DATE ="expectedDeliveryDate" ;
    private static final String BOOKING_ORDER_MONTH ="bookingOrderMonth" ;


    public DWProposalProduct setExpectedDeliveryDate(Date dt) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String format = formatter.format(dt);
        this.put(EXPECTED_DELIVERY_DATE, format);
        return  this;
    }
    public Date getExpectedDeliveryDate() {
        return (Date) this.getValue(EXPECTED_DELIVERY_DATE);
    }

    public static String getCustomerId() {
        return CUSTOMER_ID;
    }
    public DWProposalProduct setCustomerId(String customerId)
    {
        put(CUSTOMER_ID,customerId);
        return this;
    }

    public static String getCName() {
        return CNAME;
    }
    public DWProposalProduct setCName(String cName)
    {
        put(CNAME,cName);
        return this;
    }

    public static String getcAddress1() {
        return CADDRESS1;
    }
    public DWProposalProduct setcAddress1(String add1)
    {
        put(CADDRESS1,add1);
        return this;
    }
    public static String getcAddress2() {
        return CADDRESS2;
    }
    public DWProposalProduct setcAddress2(String add1)
    {
        put(CADDRESS2,add1);
        return this;
    }
    public static String getcAddress3() {
        return CADDRESS3;
    }
    public DWProposalProduct setcAddress3(String add1)
    {
        put(CADDRESS3,add1);
        return this;
    }


    public static String getcCity() {
        return CCITY;
    }
    public DWProposalProduct setcCity(String city)
    {
        put(CCITY,city);
        return this;
    }

    public static String getCEmail() {
        return CEMAIL;
    }
    public DWProposalProduct setCEmail(String email)
    {
        put(CEMAIL,email);
        return this;
    }

    public static String getCPhone1() {
        return CPHONE1;
    }
    public DWProposalProduct setCPhone1(String cPhone)
    {
        put(CPHONE1,cPhone);
        return this;
    }

    public static String getCPhone2() {
        return CPHONE2;
    }
    public DWProposalProduct setCPhone2(String cPhone)
    {
        put(CPHONE2,cPhone);
        return this;
    }

    public static String getPAddress1() {
        return PADDRESS1;
    }
    public DWProposalProduct setPAddress1(String address)
    {
        put(PADDRESS1,address);
        return this;
    }

    public static String getPAddress2() {
        return PADDRESS2;
    }
    public DWProposalProduct setPAddress2(String address)
    {
        put(PADDRESS2,address);
        return this;
    }

    public static String getCity() {
        return PCITY;
    }
    public DWProposalProduct setPCity(String city)
    {
        put(PCITY,city);
        return this;
    }


    public static String getSalesEmail() {
        return SALES_EMAIL;
    }
    public DWProposalProduct setSalesEmail(String salesEmail)
    {
        put(SALES_EMAIL,salesEmail);
        return this;
    }

    public static String getSalesPhone() {
        return SALES_PHONE;
    }
    public DWProposalProduct setSalesPhone(String phone)
    {
        put(SALES_PHONE,phone);
        return this;
    }

    public static String getDesignerEmail() {
        return DESIGNER_EMAIL;
    }
    public DWProposalProduct setDesignerEmail(String email)
    {
        put(DESIGNER_EMAIL,email);
        return this;
    }

    public static String getDesignerPhone() {
        return DESIGNER_PHONE;
    }
    public DWProposalProduct setDesignerPhone(String phone)
    {
        put(DESIGNER_PHONE,phone);
        return this;
    }

    public String getProjectName() {
        return this.getString(PROJECT_NAME);
    }
    public DWProposalProduct setProjectName(String pname) {
        this.put(PROJECT_NAME, pname);
        return this;
    }
    public DWProposalProduct() {}

    public DWProposalProduct(JsonObject jsonObject){
        super(jsonObject.getMap());
    }
    public String getCrmid() {
        return this.getString(CRMID);
    }
    public String getDesignerName() {return this.getString(DESIGNER_NAME);}
    public String getSalesName() {return this.getString(SALES_NAME);}
    public String getSource() {return this.getString(SOURCE);}
    public String getFromProduct() {return this.getString(FROM_PRODUCT);}
    public String getBefProdSpec() {return this.getString(BEF_PROD_SPEC);}
    public Integer getFromProposal() {return this.getInteger(FROM_PROPOSAL);}
    public String getOfferType() {return this.getString(OFFER_TYPE);}
    public String getPackageFlag() {return this.getString(PACKAGE_FLAG);}
    public DWProposalProduct setDesignerName(String dname) {this.put(DESIGNER_NAME, dname);return this;}
    public DWProposalProduct setSalesName(String sname) {this.put(SALES_NAME, sname);return this;}
    public DWProposalProduct setSource(String sname) {this.put(SOURCE, sname);return this;}
    public DWProposalProduct setFromProduct(Integer val) {this.put(FROM_PRODUCT, val);return this;}
    public DWProposalProduct setBefProdSpec(String val){put(BEF_PROD_SPEC,val);return this;}
    public DWProposalProduct setFromProposal(Integer val){put(FROM_PROPOSAL,val);return this;}
    public DWProposalProduct setOfferType(String val){put(OFFER_TYPE,val);return this;}
    public DWProposalProduct setPackageFlag(String val){put(PACKAGE_FLAG,val);return this;}


    public DWProposalProduct setCrmId(String crm)
    {
        put(CRMID,crm);
        return this;
    }

    public static String getWWPRICEAFTERDISCOUNT() {
        return WWPRICEAFTERDISCOUNT;
    }
    public DWProposalProduct setWoodWorkPriceAfterDiscount(double woodWorkPriceAfterDiscount)
    {
        put(WWPRICEAFTERDISCOUNT,woodWorkPriceAfterDiscount);
        return this;
    }


    public static String getHWPRICEAFTERDISCOUNT() {
        return HWPRICEAFTERDISCOUNT;
    }
    public DWProposalProduct setHwPriceAfterDiscount(double hwPriceAfterDiscount)
    {
        put(HWPRICEAFTERDISCOUNT,hwPriceAfterDiscount);
        return this;
    }

    public static String getHKPriceafterDiscount() {
        return HK_PRICE_AFTER_DISCOUNT;
    }

    public DWProposalProduct setHKPriceAfterDiscount(double hkPriceAfterDiscount)
    {
        put(HK_PRICE_AFTER_DISCOUNT,hkPriceAfterDiscount);
        return this;
    }
    public static String getHingePriceafterdiscount() {
        return HINGE_PRICE_AFTER_DISCOUNT;
    }

    public DWProposalProduct setHingePriceAfterDiscount(double hingePriceAfterDiscount)
    {
        put(HINGE_PRICE_AFTER_DISCOUNT,hingePriceAfterDiscount);
        return this;
    }
    public static String getLCPriceafterdiscount() {
        return LC_PRICE_AFTER_DISCOUNT;
    }

    public DWProposalProduct setLCPriceAfterDiscount(double lcPriceAfterDiscount)
    {
        put(LC_PRICE_AFTER_DISCOUNT,lcPriceAfterDiscount);
        return this;
    }
    public static String getAccPriceafterdiscount() {
        return ACC_PRICEAFTERDISCOUNT;
    }

    public DWProposalProduct setAccPriceAfterDiscount(double accPriceAfterDiscount)
    {
        put(ACC_PRICEAFTERDISCOUNT,accPriceAfterDiscount);
        return this;
    }

    public static String getLaPriceafterdiscount() {
        return LA_PRICEAFTERDISCOUNT;
    }

    public DWProposalProduct setLAPriceAfterDiscount(double laPriceAfterDiscount)
    {
        put(LA_PRICEAFTERDISCOUNT,laPriceAfterDiscount);
        return this;
    }

    public String getQuotenoo() {
        return this.getString(QUOTENO);
    }

    public DWProposalProduct setQuoteNo(String quote)
    {
        put(QUOTENO,quote);
        return this;
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

    public String getVersion() {
        return this.getString(VERSION);
    }

    public DWProposalProduct setVersion(String version)
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
    public String getStatus() {
        return this.getString(STATUS);
    }

    public DWProposalProduct setStatus(String description)
    {
        put(STATUS,description);
        return this;
    }

    public String getDiscountAmount() {
        return this.getString(DISCOUNT_AMOUNT);
    }

    public DWProposalProduct setDiscountAmount(double price) {
        this.put(DISCOUNT_AMOUNT, price);
        return this;
    }

    public double getDiscountAmountPerc() {
        return this.getDouble(DISCOUNT_AMOUNT_PERC);
    }

    public DWProposalProduct setDiscountAmountPerc(double price) {
        this.put(DISCOUNT_AMOUNT_PERC, price);
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

    public String getColorCode() {
        return this.getString(COLOR);
    }

    public DWProposalProduct setColorCode(String colorCode)
    {
        put(COLOR,colorCode);
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
        put(HANDLESIZE,handleFinish);
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


    public double getProductNetArea() {
        return this.getDouble(PR_NET_AREA);
    }

    public DWProposalProduct setProductNetArea(double productNetArea)
    {
        put(PR_NET_AREA,productNetArea);
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
        put(HK_COST,hkSourceCost);
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
        put(HIKEMPRICE,hikeModulePrice);
        return this;
    }

    public double getHikeModuleCost() {
        return this.getDouble(HIKEMCOST);
    }

    public DWProposalProduct setHikeModuleCost(double hikeModuleCost)
    {
        put(HIKEMCOST,hikeModuleCost);
        return this;
    }


    public String getBookingOrderMonth() {return this.getString(BOOKING_ORDER_MONTH);}

    public DWProposalProduct setBookingOrderMonth(String bookingOrderMonth)
    {
        this.put(BOOKING_ORDER_MONTH, bookingOrderMonth);
        return this;
    }


    public DWProposalProduct setDwProductObjects(ProductPriceHolder productPriceHolder, ProposalHeader proposalHeader, ProposalVersion proposalVersion, ProductLineItem productLineItem) {
        DWProposalProduct dwProposalProduct = new DWProposalProduct();


        dwProposalProduct.setProposalId(proposalHeader.getId());
        dwProposalProduct.setVersion(proposalVersion.getVersion());
        dwProposalProduct.setProposalTitle(proposalHeader.getQuotationFor());
        dwProposalProduct.setPriceDate(proposalHeader.getPriceDate());
        dwProposalProduct.setBusinessDate(proposalVersion.getBusinessDate());
        dwProposalProduct.setRegion(proposalHeader.getProjectCity());
        dwProposalProduct.setDiscountAmount(proposalVersion.getDiscountAmount());
        dwProposalProduct.setDiscountAmountPerc(proposalVersion.getDiscountPercentage());
        dwProposalProduct.setStatus(proposalVersion.getProposalStatus());
        dwProposalProduct.setCrmId(proposalHeader.getCrmId());
        dwProposalProduct.setCustomerId(proposalHeader.getCustomerId());

        dwProposalProduct.setProjectName(proposalHeader.getProjectName());
        dwProposalProduct.setCName(proposalHeader.getName());
        dwProposalProduct.setcAddress1(proposalHeader.getcAddress1());
        dwProposalProduct.setcAddress2(proposalHeader.getcAddress2());
        dwProposalProduct.setcAddress3(proposalHeader.getcAddress3());
        dwProposalProduct.setcCity(proposalHeader.getcCity());
        dwProposalProduct.setCEmail(proposalHeader.getCEmail());
        dwProposalProduct.setCPhone1(proposalHeader.getContact());
        dwProposalProduct.setCPhone2(proposalHeader.getCPhone2());
        dwProposalProduct.setPAddress1(proposalHeader.getProjectAddress1());
        dwProposalProduct.setPAddress2(proposalHeader.getProjectAddress2());
        dwProposalProduct.setPCity(proposalHeader.getProjectCity());
        dwProposalProduct.setSalesEmail(proposalHeader.getSalesEmail());
        dwProposalProduct.setSalesPhone(proposalHeader.getSalesPhone());
        dwProposalProduct.setDesignerEmail(proposalHeader.getDesignerEmail());
        dwProposalProduct.setDesignerPhone(proposalHeader.getDesignerPhone());
        if (proposalHeader.getBookingOrderMonth().equals("") || proposalHeader.getBookingOrderMonth() == null)
        {
            dwProposalProduct.setBookingOrderMonth("NA");
        }
        else
        {
            dwProposalProduct.setBookingOrderMonth(proposalHeader.getBookingOrderMonth());
        }

        dwProposalProduct.setQuoteNo(proposalHeader.getQuoteNumNew());
        dwProposalProduct.setSalesName(proposalHeader.getSalespersonName());
        dwProposalProduct.setSource(productLineItem.getSource());
        dwProposalProduct.setFromProduct(productLineItem.getFromProduct());
        dwProposalProduct.setBefProdSpec(proposalHeader.getBefProdSpec());
        dwProposalProduct.setFromProposal(proposalHeader.getFromProposal());
        dwProposalProduct.setOfferType(proposalHeader.getOfferType());
        dwProposalProduct.setPackageFlag(proposalHeader.getPackageFlag());
        dwProposalProduct.setDesignerName(proposalHeader.getDesignerName());
        dwProposalProduct.setCategory("Modular Products");
        dwProposalProduct.setSubCategory(productLineItem.getProductCategory());
        dwProposalProduct.setProductId(productLineItem.getId());
        dwProposalProduct.setProductTitle(productLineItem.getTitle());
        dwProposalProduct.setSpaceType(productLineItem.getSpaceType());
        dwProposalProduct.setRoom(productLineItem.getRoomCode());
        dwProposalProduct.setBaseCarcass(productLineItem.getBaseCarcassCode());
        dwProposalProduct.setWallCarcass(productLineItem.getWallCarcassCode());

        String finishCode = productLineItem.getFinishCode();
       /* if(finishCode.equalsIgnoreCase(OLD_MATT_SOLID_FINISH)){
            finishCode = NEW_MATT_SOLID_FINISH;
        }
        if(finishCode.equalsIgnoreCase(OLD_MATT_WOOD_GRAIN_FINISH)){
            finishCode = NEW_MATT_WOOD_GRAIN_FINISH;
        }*/

        ShutterFinish shutterFinish = ModuleDataService.getInstance().getFinish(finishCode);

        dwProposalProduct.setFinish(shutterFinish.getTitle());
        dwProposalProduct.setFinishMaterial(productLineItem.getFinishType());
        dwProposalProduct.setShutterDesign(productLineItem.getShutterDesignCode());
        dwProposalProduct.setHinge(productLineItem.getHingeType());
        dwProposalProduct.setGlass(productLineItem.getGlass());
        dwProposalProduct.setHandleSelection(productLineItem.getHandletypeSelection());
        dwProposalProduct.setColorCode(productLineItem.getColorgroupCode());
        dwProposalProduct.setNoOfLengths(productLineItem.getNoOfLengths());
        dwProposalProduct.setHandleType(productLineItem.getHandleType());
        dwProposalProduct.setHandleFinish(productLineItem.getHandleFinish());
        dwProposalProduct.setHandleSize(productLineItem.getHandleThickness());
        dwProposalProduct.setKnobType(productLineItem.getKnobType());
        dwProposalProduct.setKnobFinish(productLineItem.getKnobFinish());
        dwProposalProduct.setProductNetArea(productPriceHolder.getProductNetAreaInSqft());
        dwProposalProduct.setProductArea(productPriceHolder.getProductAreainSqft());
        dwProposalProduct.setProductCreatedBy(productLineItem.getCreatedBy());
        dwProposalProduct.setProductCreatedOn(productLineItem.getCreatedOn());
        dwProposalProduct.setProductUpdatedBy(productLineItem.getUpdatedBy());
        dwProposalProduct.setProductUpdatedOn(productLineItem.getUpdatedOn());
        dwProposalProduct.setProductPrice(productPriceHolder.getProductPrice());
        dwProposalProduct.setProductPriceAfterDiscount(productPriceHolder.getProductPriceAfterDiscount());
        dwProposalProduct.setProductPriceAfterTax(productPriceHolder.getProductPriceWoTax());
        dwProposalProduct.setProductSourceCost(productPriceHolder.getProductSourceCost());
        dwProposalProduct.setProductProfit(productPriceHolder.getProductProfit());
        dwProposalProduct.setProductMargin(productPriceHolder.getProductMargin());
        dwProposalProduct.setWoodWorkPrice(productPriceHolder.getWoodWorkPrice());
        dwProposalProduct.setWoodWorkPriceWoTax(productPriceHolder.getWoodWorkPriceWoTax());
        dwProposalProduct.setWoodWorkCost(productPriceHolder.getWoodWorkSourceCost());
        dwProposalProduct.setWoodWorkProfit(productPriceHolder.getWoodWorkProfit());
        dwProposalProduct.setWoodWorkMargin(productPriceHolder.getWoodWorkMargin());
        dwProposalProduct.setWoodWorkPriceAfterDiscount(productPriceHolder.getWoodWorkPriceAfterDiscount());
        dwProposalProduct.setHwPrice(productPriceHolder.getHardwarePrice());
        dwProposalProduct.setHWPriceWoTax(productPriceHolder.getProductHardwarePriceWoTax());
        dwProposalProduct.setHwSourceCost(productPriceHolder.getProductHardwareSourceCost());
        dwProposalProduct.setHwProfit(productPriceHolder.getProductHardwareProfit());
        dwProposalProduct.setHwMargin(productPriceHolder.getProductHardwareMargin());
        dwProposalProduct.setHwPriceAfterDiscount(productPriceHolder.getHardwarePriceAfterDiscount());
        dwProposalProduct.setAccPrice(productPriceHolder.getProductAccessoryPrice());
        dwProposalProduct.setAccPriceWoTax(productPriceHolder.getProductAccessoryPriceWoTax());
        dwProposalProduct.setAccSourceCost(productPriceHolder.getProductAccessorySourceCost());
        dwProposalProduct.setAccProfit(productPriceHolder.getProductAccessoryProfit());
        dwProposalProduct.setAccMargin(productPriceHolder.getProductAccessoryMargin());
        dwProposalProduct.setAccPriceAfterDiscount(productPriceHolder.getProductAccessoryPriceAfterDiscount());
        dwProposalProduct.setHkPrice(productPriceHolder.getProductHandleAndKnobPrice());
        dwProposalProduct.setHkPriceWoTax(productPriceHolder.getProductHandleAndKnobPriceWoTax());
        dwProposalProduct.setHkSourceCost(productPriceHolder.getProductHandleAndKnobSourceCost());
        dwProposalProduct.setHkProfit(productPriceHolder.getProductHandleAndKnobProfit());
        dwProposalProduct.setHkMargin(productPriceHolder.getProductHandleAndKnobMargin());
        dwProposalProduct.setHKPriceAfterDiscount(productPriceHolder.getProductHandleAndKnobPriceAfterDiscount());
        dwProposalProduct.setHingePrice(productPriceHolder.getProductHingePrice());
        dwProposalProduct.setHingePriceWoTax(productPriceHolder.getProductHingePriceWoTax());
        dwProposalProduct.setHingeSourceCost(productPriceHolder.getProductHingeSourceCost());
        dwProposalProduct.setHingeProfit(productPriceHolder.getProductHingeProfit());
        dwProposalProduct.setHingeMargin(productPriceHolder.getProductHingeMargin());
        dwProposalProduct.setHingePriceAfterDiscount(productPriceHolder.getProductHingePriceAfterDiscount());
        dwProposalProduct.setLaPrice(productPriceHolder.getProductLabourPrice());
        dwProposalProduct.setLaPriceWoTax(productPriceHolder.getProductLabourPriceWoTax());
        dwProposalProduct.setLaSourceCost(productPriceHolder.getProductLabourSourceCost());
        dwProposalProduct.setLaProfit(productPriceHolder.getProductLabourProfit());
        dwProposalProduct.setLaMargin(productPriceHolder.getProductLabourMargin());
        dwProposalProduct.setLAPriceAfterDiscount(productPriceHolder.getProductLabourPriceAfterDiscount());
        dwProposalProduct.setLCPrice(productPriceHolder.getLConnectorPrice());
        dwProposalProduct.setLcPriceWoTax(productPriceHolder.getLConnectorWoTax());
        dwProposalProduct.setLcSourceCost(productPriceHolder.getLConnectorSourceCost());
        dwProposalProduct.setLcProfit(productPriceHolder.getLConnectorProfit());
        dwProposalProduct.setLcMargin(productPriceHolder.getLConnectorMargin());
        dwProposalProduct.setLCPriceAfterDiscount(productPriceHolder.getLConnectorPriceAfterDiscount());
        dwProposalProduct.setStdModuleCount(productPriceHolder.getStdModuleCount());
        dwProposalProduct.setStdModulePrice(productPriceHolder.getStdModulePrice());
        dwProposalProduct.setNStdModuleCount(productPriceHolder.getNStdModuleCount());
        dwProposalProduct.setNStdModulePrice(productPriceHolder.getNStdModulePrice());
        dwProposalProduct.setHikeModuleCount(productPriceHolder.getHikeModuleCount());
        dwProposalProduct.setHikeModulePrice(productPriceHolder.getHikeModulePrice());
        dwProposalProduct.setHikeModulePrice(productPriceHolder.getHikeModulePrice());
        dwProposalProduct.setHikeModuleCost(productPriceHolder.getHikeModuleCost());
        dwProposalProduct.setInstallationCost(0);
        dwProposalProduct.setTransportationCost(0);
        dwProposalProduct.setExpectedDeliveryDate(proposalHeader.getExpectedDeliveryDate());
        return dwProposalProduct;
    }

}
