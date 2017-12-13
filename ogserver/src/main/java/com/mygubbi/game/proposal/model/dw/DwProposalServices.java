package com.mygubbi.game.proposal.model.dw;

import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Created by shilpa on 12/12/17.
 */
public class DwProposalServices extends JsonObject {
    private final static Logger LOG = LogManager.getLogger(DwProposalVersion.class);

    private static final String ID                 ="id";
    private static final String PROPOSALID         ="proposalId";
    private static final String VERSION            ="version";
    private static final String PROPOSALTITLE      ="proposalTitle";
    private static final String PRICEDATE          ="priceDate";
    private static final String BUSINESSDATE       ="businessDate";
    private static final String CRMID              ="crmId";
    private static final String CUSTOMERID         ="customerId";
    private static final String CNAME              ="cname";
    private static final String CADDRESS1          ="caddress1";
    private static final String CADDRESS2          ="caddress2";
    private static final String CADDRESS3          ="caddress3";
    private static final String CCITY              ="ccity";
    private static final String CEMAIL             ="cemail";
    private static final String CPHONE1            ="cphone1";
    private static final String CPHONE2            ="cphone2";
    private static final String PROJECTNAME        ="projectName";
    private static final String PADDRESS1          ="paddress1";
    private static final String PADDRESS2          ="paddress2";
    private static final String PCITY              ="pcity";
    private static final String QUOTENO            ="quoteNo";
    private static final String DESIGNERNAME       ="designerName";
    private static final String DESIGNEREMAIL      ="designerEmail";
    private static final String DESIGNERPHONE      ="designerPhone";
    private static final String SALESNAME          ="salesName";
    private static final String SALESEMAIL         ="salesEmail";
    private static final String SALESPHONE         ="salesPhone";
    private static final String SERVICETITLE       ="serviceTitle";
    private static final String QUANTITY           ="quantity";
    private static final String AMOUNT             ="amount";
    private static final String UPDATEDBY          ="updatedBy";

    public DwProposalServices(JsonObject data) {
        super(data.getMap());
    }

    public DwProposalServices(){}

    public static String getID() {
        return ID;
    }

    public static String getProposalId() {
        return PROPOSALID;
    }
    public DwProposalServices setProposalId(Integer proposalId)
    {
        put(PROPOSALID,proposalId);
        return this;
    }
    public static String getVersion() {
        return VERSION;
    }
    public DwProposalServices setVersion(String version)
    {
        put(VERSION,version);
        return this;
    }

    public static String getProposalTitle() {
        return PROPOSALTITLE;
    }
    public DwProposalServices setProposalTitle(String title)
    {
        put(PROPOSALTITLE,title);
        return this;
    }

    public static String getPriceDate() {
        return PRICEDATE;
    }
    public DwProposalServices setPriceDate(Date dt)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String format = formatter.format(dt);
        this.put(PRICEDATE, format);
        return  this;
    }

    public static String getBusinessDate() {
        return BUSINESSDATE;
    }
    public DwProposalServices setBusinessDate(Date dt)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String format = formatter.format(dt);
        this.put(BUSINESSDATE, format);
        return  this;
    }


    public static String getCrmId() {
        return CRMID;
    }
    public DwProposalServices setCrmId(String crmId)
    {
        put(CRMID,crmId);
        return this;
    }

    public static String getCustomerId() {
        return CUSTOMERID;
    }
    public DwProposalServices setCustomerId(String customerId)
    {
        put(CUSTOMERID,customerId);
        return this;
    }

    public static String getCustomerName() {
        return CNAME;
    }
    public DwProposalServices setCustomerName(String customerName)
    {
        put(CNAME,customerName);
        return this;
    }

    public static String getCustomerAddress1() {
        return CADDRESS1;
    }

    public DwProposalServices setCustomerAddress1(String address1)
    {
        put(CADDRESS1,address1);
        return this;
    }
    public static String getCustomerAddress2() {
        return CADDRESS2;
    }

    public DwProposalServices setCustomerAddress2(String address2)
    {
        put(CADDRESS2,address2);
        return this;
    }

    public static String getCustomerAddress3() {
        return CADDRESS3;
    }
    public DwProposalServices setCustomerAddress3(String address3)
    {
        put(CADDRESS3,address3);
        return this;
    }
    public static String getCustomeCity() {
        return CCITY;
    }

    public DwProposalServices setCustomeCity(String city)
    {
        put(CCITY,city);
        return this;
    }
    public static String getCustomerEmail() {
        return CEMAIL;
    }
    public DwProposalServices setCustomerEmail(String email)
    {
        put(CEMAIL,email);
        return this;
    }

    public static String getCustomerPhone1() {
        return CPHONE1;
    }

    public DwProposalServices setCustomerPhone1(String cphone)
    {
        put(CEMAIL,cphone);
        return this;
    }

    public static String getCustomerPhone2() {
        return CPHONE2;
    }

    public DwProposalServices setCustomerPhone2(String cphone)
    {
        put(CPHONE2,cphone);
        return this;
    }


    public static String getProjectName() {
        return PROJECTNAME;
    }

    public DwProposalServices setProjectName(String pname)
    {
        put(PROJECTNAME,pname);
        return this;
    }
    public static String getProjectAddress1() {
        return PADDRESS1;
    }
    public DwProposalServices setProjectAddress1(String address1)
    {
        put(PADDRESS1,address1);
        return this;
    }

    public static String getProjectAddress2() {
        return PADDRESS2;
    }
    public DwProposalServices setProjectAddress2(String address2)
    {
        put(PADDRESS2,address2);
        return this;
    }

    public static String getProjectCity() {
        return PCITY;
    }
    public DwProposalServices setProjectCity(String city)
    {
        put(PCITY,city);
        return this;
    }

    public static String getQuoteNo() {
        return QUOTENO;
    }
    public DwProposalServices setQuoteNo(String quoteNo)
    {
        put(QUOTENO,quoteNo);
        return this;
    }

    public static String getDesignerName() {
        return DESIGNERNAME;
    }
    public DwProposalServices setDesignerName(String dname)
    {
        put(DESIGNERNAME,dname);
        return this;
    }

    public static String getDesignerEmail() {
        return DESIGNEREMAIL;
    }
    public DwProposalServices setDesigneremail(String email)
    {
        put(DESIGNEREMAIL,email);
        return this;
    }

    public static String getDesignerPhone() {
        return DESIGNERPHONE;
    }
    public DwProposalServices setDesignerPhone(String phone)
    {
        put(DESIGNERPHONE,phone);
        return this;
    }

    public static String getSalesName() {
        return SALESNAME;
    }
    public DwProposalServices setSalesName(String name)
    {
        put(SALESNAME,name);
        return this;
    }

    public static String getSalesEmail() {
        return SALESEMAIL;
    }
    public DwProposalServices setSalesEmail(String email)
    {
        put(SALESEMAIL,email);
        return this;
    }

    public static String getSalesPhone() {
        return SALESPHONE;
    }
    public DwProposalServices setSalesPhone(String phone)
    {
        put(SALESPHONE,phone);
        return this;
    }

    public static String getServiceTitle() {
        return SERVICETITLE;
    }
    public DwProposalServices setServiceTitle(String serviceTitle)
    {
        put(SERVICETITLE,serviceTitle);
        return this;
    }

    public static String getQuantity() {
        return QUANTITY;
    }
    public DwProposalServices setQuantity(Double quantity)
    {
        put(QUANTITY,quantity);
        return this;
    }

    public static String getAmount() {
        return AMOUNT;
    }
    public DwProposalServices setAmount(Double amount)
    {
        put(AMOUNT,amount);
        return this;
    }

    public static String getUpdatedBy() {
        return UPDATEDBY;
    }
    public DwProposalServices setUpdatedBy(String updatedBy)
    {
        put(UPDATEDBY,updatedBy);
        return this;
    }
}
