package com.mygubbi.game.proposal.model;

import io.vertx.core.json.JsonObject;
import org.apache.poi.hssf.util.HSSFColor;

/**
 * Created by User on 06-07-2017.
 */
public class Proposal_BOQ extends JsonObject {

    public static final String ID = "id";
    public static final String PROPOSAL_ID = "proposalId";
    public static final String SPACE_TYPE = "spaceType";
    public static final String ROOM = "room";
    public static final String CATEGORY = "category";
    public static final String PRODUCT_SERVICE = "productService";
    public static final String MG_CODE = "mgCode";
    public static final String CUSTOM_CHECK = "customCheck";
    public static final String CUSTOM_REMARKS = "customRemarks";
    public static final String ITEM_CATEGORY = "itemCategory";
    public static final String DSO_ERP_ITEM_CODE = "DSOErpItemCode";
    public static final String DSO_REFERENCE_PART_NO ="DSOReferencePartNo";
    public static final String DSO_UOM ="DSOUom";
    public static final String DSO_RATE ="DSORate";
    public static final String DSO_QTY ="DSOQty";
    public static final String DSO_PRICE ="DSOPrice";
    public static final String PLANNER_ERP_ITEM_CODE = "PlannerErpItemCode";
    public static final String PLANNER_UOM ="PlannerUom";
    public static final String PLANNER_RATE ="PlannerRate";
    public static final String PLANNER_QTY ="PlannerQty";
    public static final String PLANNER_PRICE ="PlannerPrice";

    public Proposal_BOQ() {}

    public Proposal_BOQ(JsonObject jsonObject){
        super(jsonObject.getMap());
    }

    public int getID() {
        return this.getInteger(ID);
    }

    public int getProposalId() {
        return this.getInteger(PROPOSAL_ID);
    }

    public String getSpaceType() {
        return this.getString(SPACE_TYPE);
    }

    public String getROOM() {
        return this.getString(ROOM);
    }

    public static String getCATEGORY() {
        return CATEGORY;
    }

    public static String getProductService() {
        return PRODUCT_SERVICE;
    }

    public static String getMgCode() {
        return MG_CODE;
    }

    public static String getCustomCheck() {
        return CUSTOM_CHECK;
    }

    public static String getCustomRemarks() {
        return CUSTOM_REMARKS;
    }

    public static String getItemCategory() {
        return ITEM_CATEGORY;
    }

    public static String getDsoErpItemCode() {
        return DSO_ERP_ITEM_CODE;
    }

    public static String getDsoReferencePartNo() {
        return DSO_REFERENCE_PART_NO;
    }

    public static String getDsoUom() {
        return DSO_UOM;
    }

    public static String getDsoRate() {
        return DSO_RATE;
    }

    public static String getDsoQty() {
        return DSO_QTY;
    }

    public static String getDsoPrice() {
        return DSO_PRICE;
    }

    public static String getPlannerErpItemCode() {
        return PLANNER_ERP_ITEM_CODE;
    }

    public static String getPlannerUom() {
        return PLANNER_UOM;
    }

    public static String getPlannerRate() {
        return PLANNER_RATE;
    }

    public static String getPlannerQty() {
        return PLANNER_QTY;
    }

    public static String getPlannerPrice() {
        return PLANNER_PRICE;
    }

    public Proposal_BOQ setId(int id)
    {
        put(ID,id);
        return this;
    }

    public Proposal_BOQ setProposalId(int proposalId)
    {
        put(PROPOSAL_ID,proposalId);
        return this;
    }

    public Proposal_BOQ setSpaceType(String spaceType)
    {
        put(SPACE_TYPE,spaceType);
        return this;
    }
    public Proposal_BOQ setRoom(String room)
    {
        put(ROOM,room);
        return this;
    }

    public Proposal_BOQ setCategory(String category)
    {
        put(CATEGORY,category);
        return this;
    }

    public Proposal_BOQ setProductService(String productService)
    {
        put(PRODUCT_SERVICE,productService);
        return this;
    }

    public Proposal_BOQ setMgCode(String mgCode)
    {
        put(MG_CODE,mgCode);
        return this;
    }

    public Proposal_BOQ setCustomCheck(String customCheck)
    {
        put(CUSTOM_CHECK,customCheck);
        return this;
    }

    public Proposal_BOQ setCustomRemarks(String customRemarks)
    {
        put(CUSTOM_REMARKS,customRemarks);
        return this;
    }

    public Proposal_BOQ setItemCategory(String itemCategory)
    {
        put(ITEM_CATEGORY,itemCategory);
        return this;
    }

    public Proposal_BOQ setDSOErpItemCode(String dsoErpItemCode)
    {
        put(DSO_ERP_ITEM_CODE,dsoErpItemCode);
        return this;
    }

    public Proposal_BOQ setReferencePartNo(String referencePartNo)
    {
        put(DSO_REFERENCE_PART_NO,referencePartNo);
        return this;
    }

    public Proposal_BOQ setDSOUom(String dsoUom)
    {
        put(DSO_UOM,dsoUom);
        return this;
    }

    public Proposal_BOQ setDSORate(double dsoRate)
    {
        put(DSO_RATE,dsoRate);
        return this;
    }

    public Proposal_BOQ setDSOQty(double dsoQty)
    {
        put(DSO_QTY,dsoQty);
        return this;
    }

    public Proposal_BOQ setDSOPrice(double dsoPrice)
    {
        put(DSO_PRICE,dsoPrice);
        return this;
    }

    public Proposal_BOQ setPlannerErpCode(String plannerErpCode)
    {
        put(PLANNER_ERP_ITEM_CODE,plannerErpCode);
        return this;
    }

    public Proposal_BOQ setPlannerUom(String plannerUom)
    {
        put(PLANNER_UOM,plannerUom);
        return this;
    }

    public Proposal_BOQ setPlannerRate(double plannerRate)
    {
        put(PLANNER_RATE,plannerRate);
        return this;
    }

    public Proposal_BOQ setPlannerQty(double plannerQty)
    {
        put(PLANNER_QTY,plannerQty);
        return this;
    }

    public Proposal_BOQ setPlannerPrice(double plannerPrice)
    {
        put(PLANNER_PRICE,plannerPrice);
        return this;
    }

}
