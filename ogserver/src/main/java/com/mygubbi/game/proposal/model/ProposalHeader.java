package com.mygubbi.game.proposal.model;

import com.mygubbi.common.DateUtil;
import com.mygubbi.game.proposal.model.dw.DWProposalProduct;
import com.mygubbi.game.proposal.model.dw.DwProposalVersion;
import io.vertx.core.json.JsonObject;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Created by test on 21-05-2016.
 */
public class ProposalHeader extends JsonObject
{
    public static final String ID = "id";

    public static final String CRMID = "crmId";
    public static final String PROJECT_NAME = "projectName";
    public static final String PROJECT_ADDRESS1 = "paddress1";
    public static final String PROJECT_ADDRESS2 = "paddress2";
    public static final String PROJECT_CITY = "pcity";
    public static final String SALESPERSON_NAME = "salesName";
    public static final String DESIGNER_NAME = "designerName";
    private static final String BEF_PROD_SPEC = "beforeProductionSpecification";
    private static final String FROM_PROPOSAL = "fromProposal";
    private static final String OFFER_TYPE = "offerType";
    public static final String DESIGN_PARTNER_NAME = "designPartnerName";
    public static final String AMOUNT = "amount";
    public static final String FOLDER_PATH = "folderPath";
    public static final String QNAME="title";
    public static final String NAME="cname";
    public static final String CONTACT="cphone1";
    public static final String QUOTENO="quoteNo";
    public static final String CID="customerId";
    public static final String SALESPHONE="salesPhone";
    public static final String DESIGNEREMAIL="designerEmail";
    public static final String SALESEMAIL="salesEmail";
    public static final String QUOTE_NO_NEW="quoteNoNew";
    public static final String PRICE_DATE="priceDate";
    public static final String PACKAGE_FLAG="packageFlag";
    public static final String SOW_REMARKS_V1="sowremarksv1";
    public static final String SOW_REMARKS_V2="sowremarksv2";
    public static final String CREATED_ON="createdOn";
    public static final String CREATED_BY="createdBy";
    public static final String UPDATED_ON="updatedBy";
    public static final String UPDATED_BY="updatedBy";
    public static final String BEFORE_PRODUCTION_SPECIFICATION="beforeProductionSpecification";

    private static final String CADDRESS1 =     "caddress1" ;
    private static final String CADDRESS2 =     "caddress2" ;
    private static final String CADDRESS3 =     "caddress3" ;
    private static final String CCITY =         "ccity" ;
    private static final String CEMAIL =        "cemail" ;
    private static final String CPHONE2 =       "cphone2" ;
    private static final String DESIGNER_PHONE ="designerPhone" ;



    private Date priceDate;
    private String designerName;
    private Date createDate;


    public ProposalHeader(JsonObject json)
    {
        super(json.getMap());
    }

    public ProposalHeader()
    {

    }
    public String getSalesPhone()
    {
        return this.getString(SALESPHONE);
    }
    public String getDesignPartnerName()
    {
        return this.getString(DESIGN_PARTNER_NAME);
    }
    public String getDesignerEmail() {   return this.getString(DESIGNEREMAIL); }
    public String getSalesEmail()
    {
        return this.getString(SALESEMAIL);
    }
    public String getContact()
    {
        return this.getString(CONTACT);
    }
    public String getQuoteNum()
    {
        return this.getString(QUOTENO);
    }
    public String getQuoteNumNew()
    {
        return this.getString(QUOTE_NO_NEW);
    }
    public String getCustomerId()
    {
        return this.getString(CID);
    }
    public String getQuotationFor()
    {
        return this.getString(QNAME);
    }
    public String getName()
    {
        return this.getString(NAME);
    }
    public String folderPath()
    {
        return this.getString(FOLDER_PATH);
    }
    public String getProjectCity()
    {
        return this.getString(PROJECT_CITY);
    }
    public String getPackageFlag() { return this.getString(PACKAGE_FLAG);}
    public String getProjectName() { return  this.getString(PROJECT_NAME);}
    public String getSowRemarksV1() {
        return this.getString(SOW_REMARKS_V1);
    }
    public String getProjectAddress1() {return this.getString(PROJECT_ADDRESS1);}
    public String getProjectAddress2() {return this.getString(PROJECT_ADDRESS2);}
    public String getSowRemarksV2() {
        return this.getString(SOW_REMARKS_V2);
    }
    public String getBeforeProductionSpecification() {
        return this.getString(BEFORE_PRODUCTION_SPECIFICATION);
    }
    public ProposalHeader setSowRemarksV1(String remarksV1)
    {
        put(remarksV1,SOW_REMARKS_V1);
        return this;
    }

    public ProposalHeader setSowRemarksV2(String remarksV2)
    {
        put(remarksV2,SOW_REMARKS_V2);
        return this;
    }

    public String getcAddress1() {
        return this.getString(CADDRESS1);
    }

    public  String getcAddress2() {
        return this.getString(CADDRESS2);
    }
    public  String getcAddress3() {
        return this.getString(CADDRESS3);
    }

    public  String getcCity() {
        return this.getString(CCITY);
    }

    public  String getCEmail() {
        return this.getString(CEMAIL);
    }

    public  String getCPhone2() {
        return this.getString(CPHONE2);
    }

    public  String getDesignerPhone() {
        return this.getString(DESIGNER_PHONE);
    }


    public Date getPriceDate() {
        return DateUtil.convertDate(this.getString(PRICE_DATE));
    }

    public void setPriceDate(Date priceDate) {
        this.priceDate = priceDate;
    }


    public String getCrmId()
    {
        return this.getString(CRMID);
    }

    public int getId()
    {
        if (!this.containsKey("id")) return 0;
        return this.getInteger("id");
    }

    public Double getAmount()
    {
        if (this.containsKey(AMOUNT))
            return this.getDouble(AMOUNT);
        else
            return 0.0;
    }

    public String getDesignerName() {
        return this.getString(DESIGNER_NAME);
    }

    public String getSalespersonName() {
        return this.getString(SALESPERSON_NAME);
    }


    public Date getCreatedOn() {
        return DateUtil.convertDate(this.getString(CREATED_ON));
    }


    public String getCreatedBy()
    {
        return this.getString(CREATED_BY);
    }

    public String getUpdatedBy()
    {
        return this.getString(UPDATED_BY);
    }

    public String getUpdatedOn() {
        return this.getString(UPDATED_ON);
    }

    public String getBefProdSpec() {if(this.containsKey(BEF_PROD_SPEC) )return this.getString(BEF_PROD_SPEC);return "";}

    public Integer getFromProposal() {
        if (this.getInteger(FROM_PROPOSAL) == null || this.getInteger(FROM_PROPOSAL).equals("")) return 0;
        else return this.getInteger(FROM_PROPOSAL); }

    public  String getOfferType() {if(this.containsKey(OFFER_TYPE) )return this.getString(OFFER_TYPE);return "";}



}


