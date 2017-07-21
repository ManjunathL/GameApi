package com.mygubbi.game.proposal.model;

import com.mygubbi.common.DateUtil;
import io.vertx.core.json.JsonObject;

import java.sql.Date;

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
    public static final String DESIGN_PARTNER_NAME = "designPartnerName";
    public static final String AMOUNT = "amount";
    public static final String FOLDER_PATH = "folderPath";
    public static final String QNAME="title";
    public static final String NAME="cname";
    public static final String CONTACT="cphone1";
    public static final String QUOTENO="quoteNo";
    public static final String CID="customerId";
    public static final String SALESPHONE="salesPhone";
    public static final String SALESEMAIL="salesEmail";
    public static final String QUOTE_NO_NEW="quoteNoNew";
    public static final String PRICE_DATE="priceDate";
    public static final String PACKAGE_FLAG="packageFlag";
    public static final String SOW_REMARKS_V1="sowremarksv1";
    public static final String SOW_REMARKS_V2="sowremarksv2";

    private Date priceDate;


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

    public String getSowRemarksV1() {
        return this.getString(SOW_REMARKS_V1);
    }

    public String getSowRemarksV2() {
        return this.getString(SOW_REMARKS_V2);
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
}


