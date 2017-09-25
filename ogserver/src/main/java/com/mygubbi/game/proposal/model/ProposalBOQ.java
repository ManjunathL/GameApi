package com.mygubbi.game.proposal.model;

import com.mygubbi.game.proposal.ProductAddon;
import com.mygubbi.game.proposal.ProductModule;
import com.mygubbi.game.proposal.quote.AssembledProductInQuote;
import io.vertx.core.json.JsonObject;

/**
 * Created by User on 06-07-2017.
 */
public class ProposalBOQ extends JsonObject {

    private static final String ACC_CODE = "AP";
    private static final String ADDON_CODE = "ADDON";
    private static final String HINGE_CODE = "HINGE";
    private static final String HANDLE_CODE = "HANDLE";
    private static final String KNOB_CODE = "KNOB";
    private static final String HARDWARE_CODE = "HW";


    public static final String ID = "id";
    public static final String PROPOSAL_ID = "proposalId";
    public static final String SPACE_TYPE = "spaceType";
    public static final String ROOM = "roomcode";
    public static final String CATEGORY = "category";
    public static final String PRODUCT_SERVICE = "productService";
    public static final String PRODUCT_ID = "productId";
    public static final String MG_CODE = "mgCode";
    public static final String MODULE_SEQ = "moduleSeq";
    public static final String CUSTOM_CHECK = "customCheck";
    public static final String CUSTOM_REMARKS = "customRemarks";
    public static final String ITEM_CATEGORY = "itemCategory";
    public static final String DSO_ERP_ITEM_CODE = "DSOErpItemCode";
    public static final String DSO_ITEM_SEQ = "DSOItemSeq";
    public static final String DSO_REFERENCE_PART_NO ="DSOReferencePartNo";
    public static final String DSO_DESCRIPTION ="DSODescription";
    public static final String DSO_UOM ="DSOUom";
    public static final String DSO_RATE ="DSORate";
    public static final String DSO_QTY ="DSOQty";
    public static final String DSO_PRICE ="DSOPrice";
    public static final String PLANNER_ERP_ITEM_CODE = "plannerErpItemCode";
    public static final String PLANNER_REFERENCE_PART_NO ="plannerReferencePartNo";
    public static final String PLANNER_DESCRIPTION ="plannerDescription";
    public static final String PLANNER_UOM ="plannerUom";
    public static final String PLANNER_RATE ="plannerRate";
    public static final String PLANNER_QTY ="plannerQty";
    public static final String PLANNER_PRICE ="plannerPrice";

    public ProposalBOQ() {}

    public ProposalBOQ(JsonObject jsonObject){
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

    public String getcategory() {
        return this.getString(CATEGORY);
    }

    public String getProductService() {
        return this.getString(PRODUCT_SERVICE);
    }
    public int getProductId() {
        return this.getInteger(PRODUCT_ID);
    }

    public String getMgCode() {
        return this.getString(MG_CODE);
    }

    public int getModuleSeq() {
        return this.getInteger(MODULE_SEQ);
    }

    public String getCustomCheck() {
        return this.getString(CUSTOM_CHECK);
    }

    public String getCustomRemarks() {
        return this.getString(CUSTOM_REMARKS);
    }

    public String getItemCategory() {
        return this.getString(ITEM_CATEGORY);
    }

    public String getDsoErpItemCode() {
        return this.getString(DSO_ERP_ITEM_CODE);
    }

    public int getDsoItemSeq() {
        return this.getInteger(DSO_ITEM_SEQ);
    }

    public String getDsoReferencePartNo() {
        return this.getString(DSO_REFERENCE_PART_NO);
    }

    public String getDsoDescription() {
        return this.getString(DSO_DESCRIPTION);
    }

    public String getDsoUom() {
        return this.getString(DSO_UOM);
    }

    public double getDsoRate() {
        return this.getDouble(DSO_RATE);
    }

    public double getDsoQty() {
        return this.getDouble(DSO_QTY);
    }

    public double getDsoPrice() {
        return this.getDouble(DSO_PRICE);
    }

    public String getPlannerErpItemCode() {
        return this.getString(PLANNER_ERP_ITEM_CODE);
    }


    public String getPlannerReferencePartNo() {
        return this.getString(PLANNER_REFERENCE_PART_NO);
    }

    public String getPlannerDescription() {
        return this.getString(PLANNER_DESCRIPTION);
    }

    public String getPlannerUom() {
        return this.getString(PLANNER_UOM);
    }

    public double getPlannerRate() {
        return this.getDouble(PLANNER_RATE);
    }

    public double getPlannerQty() {
        return this.getDouble(PLANNER_QTY);
    }

    public double getPlannerPrice() {
        return this.getDouble(PLANNER_PRICE);
    }


    public ProposalBOQ setId(int id)
    {
        put(ID,id);
        return this;
    }

    public ProposalBOQ setProposalId(int proposalId)
    {
        put(PROPOSAL_ID,proposalId);
        return this;
    }

    public ProposalBOQ setSpaceType(String spaceType)
    {
        put(SPACE_TYPE,spaceType);
        return this;
    }
    public ProposalBOQ setRoom(String room)
    {
        put(ROOM,room);
        return this;
    }

    public ProposalBOQ setCategory(String category)
    {
        put(CATEGORY,category);
        return this;
    }

    public ProposalBOQ setProductService(String productService)
    {
        put(PRODUCT_SERVICE,productService);
        return this;
    }

    public ProposalBOQ setProductId(int productId)
    {
        put(PRODUCT_ID,productId);
        return this;
    }

    public ProposalBOQ setMgCode(String mgCode)
    {
        put(MG_CODE,mgCode);
        return this;
    }

    public ProposalBOQ setModuleSeq(int moduleSeq)
    {
        put(MODULE_SEQ,moduleSeq);
        return this;
    }

    public ProposalBOQ setCustomCheck(String customCheck)
    {
        put(CUSTOM_CHECK,customCheck);
        return this;
    }

    public ProposalBOQ setCustomRemarks(String customRemarks)
    {
        put(CUSTOM_REMARKS,customRemarks);
        return this;
    }

    public ProposalBOQ setItemCategory(String itemCategory)
    {
        put(ITEM_CATEGORY,itemCategory);
        return this;
    }

    public ProposalBOQ setDSOErpItemCode(String dsoErpItemCode)
    {
        put(DSO_ERP_ITEM_CODE,dsoErpItemCode);
        return this;
    }

    public ProposalBOQ setDSOItemSeq(int dsoItemSeq)
    {
        put(DSO_ITEM_SEQ,dsoItemSeq);
        return this;
    }

    public ProposalBOQ setDSOReferencePartNo(String referencePartNo)
    {
        put(DSO_REFERENCE_PART_NO,referencePartNo);
        return this;
    }

    public ProposalBOQ setDSODescription(String dsoDescription)
    {
        put(DSO_DESCRIPTION,dsoDescription);
        return this;
    }

    public ProposalBOQ setDSOUom(String dsoUom)
    {
        put(DSO_UOM,dsoUom);
        return this;
    }

    public ProposalBOQ setDSORate(double dsoRate)
    {
        put(DSO_RATE,dsoRate);
        return this;
    }

    public ProposalBOQ setDSOQty(double dsoQty)
    {
        put(DSO_QTY,dsoQty);
        return this;
    }

    public ProposalBOQ setDSOPrice(double dsoPrice)
    {
        put(DSO_PRICE,dsoPrice);
        return this;
    }

    public ProposalBOQ setPlannerErpCode(String plannerErpCode)
    {
        put(PLANNER_ERP_ITEM_CODE,plannerErpCode);
        return this;
    }

    public ProposalBOQ setPlannerReferencePartNo(String plannerReferencePartNo)
    {
        put(PLANNER_REFERENCE_PART_NO,plannerReferencePartNo);
        return this;
    }

    public ProposalBOQ setPlannerDescription(String plannerDescription)
    {
        put(PLANNER_DESCRIPTION,plannerDescription);
        return this;
    }

    public ProposalBOQ setPlannerUom(String plannerUom)
    {
        put(PLANNER_UOM,plannerUom);
        return this;
    }

    public ProposalBOQ setPlannerRate(double plannerRate)
    {
        put(PLANNER_RATE,plannerRate);
        return this;
    }

    public ProposalBOQ setPlannerQty(double plannerQty)
    {
        put(PLANNER_QTY,plannerQty);
        return this;
    }

    public ProposalBOQ setPlannerPrice(double plannerPrice)
    {
        put(PLANNER_PRICE,plannerPrice);
        return this;
    }

    public ProposalBOQ(AssembledProductInQuote productInQuote, ProposalHeader proposalHeader, String product, ProductModule module, double quantity, AccHwComponent accHwComponent, PriceMaster addonRate) {

        this.setProposalId(proposalHeader.getId());
        this.setSpaceType(productInQuote.getSpaceType());
        this.setRoom(productInQuote.getRoom());
        this.setCategory("Modular Products");
        this.setProductService(product);
        this.setProductId(productInQuote.getProduct().getId());
        this.setMgCode(module.getMGCode());
        this.setModuleSeq(module.getModuleSequence());
        this.setCustomCheck(module.getCustomCheck());
       /* if (module.getCustomCheck().equals("yes"))
        {
            this.setCustomRemarks(module.getRemarks());
        }
        else
        {*/

            this.setCustomRemarks("");
//        }
        this.setItemCategory(HARDWARE_CODE);
        this.setDSOErpItemCode(accHwComponent.getERPCode());
        this.setDSOReferencePartNo(accHwComponent.getCatalogCode());
        this.setDSODescription(accHwComponent.getTitle());
        this.setDSOUom(accHwComponent.getUom());
        this.setDSORate(addonRate.getSourcePrice());
        this.setDSOQty(quantity);
        this.setDSOPrice(addonRate.getSourcePrice() * quantity);
        this.setPlannerErpCode(accHwComponent.getERPCode());
        this.setDSOItemSeq(accHwComponent.getBoqDisplayOrder());
        this.setPlannerReferencePartNo(accHwComponent.getCatalogCode());
        this.setPlannerDescription(accHwComponent.getTitle());
        this.setPlannerUom(accHwComponent.getUom());
        this.setPlannerUom(accHwComponent.getUom());
        this.setPlannerRate(addonRate.getSourcePrice());
        this.setPlannerQty(quantity);
        this.setPlannerPrice(addonRate.getSourcePrice() * quantity);
    }

    public ProposalBOQ(AssembledProductInQuote productInQuote, ProductModule module, ProposalHeader proposalHeader, double quantity, PriceMaster addonRate, AccHwComponent accHwComponent) {
        this.setProposalId(proposalHeader.getId());
        this.setSpaceType(productInQuote.getSpaceType());
        this.setRoom(productInQuote.getRoom());
        this.setCategory("Modular Products");
        this.setProductService(productInQuote.getProduct().getProductCategory());
        this.setProductId(productInQuote.getProduct().getId());
        this.setMgCode(module.getMGCode());
        this.setModuleSeq(module.getModuleSequence());
        this.setCustomCheck(module.getCustomCheck());
        if (module.getCustomCheck().equals("yes")) {
            this.setCustomRemarks(module.getRemarks());
        } else {
            this.setCustomRemarks("");
        }
        if (accHwComponent.getCode().startsWith("A"))
        {
            this.setItemCategory(ACC_CODE);
        }
        else {
            this.setItemCategory(HARDWARE_CODE);
        }
        this.setDSOErpItemCode(accHwComponent.getERPCode());
        this.setDSOItemSeq(accHwComponent.getBoqDisplayOrder());
        this.setDSOReferencePartNo(accHwComponent.getCatalogCode());
        this.setDSODescription(accHwComponent.getTitle());
        this.setDSOUom(accHwComponent.getUom());
        this.setDSORate(addonRate.getSourcePrice());
        this.setDSOQty(quantity);
        this.setDSOPrice(addonRate.getSourcePrice() * quantity);
        this.setPlannerErpCode(accHwComponent.getERPCode());
        this.setPlannerReferencePartNo(accHwComponent.getCatalogCode());
        this.setPlannerDescription(accHwComponent.getTitle());
        this.setPlannerUom(accHwComponent.getUom());
        this.setPlannerRate(addonRate.getSourcePrice());
        this.setPlannerQty(quantity);
        this.setPlannerPrice(addonRate.getSourcePrice() * quantity);
    }

    public ProposalBOQ(ProductAddon productAddon, double dsoRate, double DSOPrice) {
        this.setProposalId(productAddon.getProposalId());
        this.setSpaceType(productAddon.getSpaceType());
        this.setRoom(productAddon.getRoomCode());
        this.setCategory("Addons");
        this.setProductService(productAddon.getCategoryCode());
        this.setProductId(0);
        this.setMgCode(productAddon.getCode());
        this.setModuleSeq(0);
        this.setCustomCheck("No");
        this.setCustomRemarks("");
        this.setCategory(ADDON_CODE);
        this.setDSOErpItemCode("NA");
        this.setDSOItemSeq(0);
        this.setDSOReferencePartNo(productAddon.getCatalogueCode());
        this.setDSODescription(productAddon.getProduct());
        this.setDSOUom(productAddon.getUom());
        this.setDSORate(dsoRate);
        this.setDSOQty(productAddon.getQuantity());
        this.setDSOPrice(DSOPrice);
        this.setPlannerErpCode("NA");
        this.setPlannerUom(productAddon.getCatalogueCode());
        this.setPlannerDescription(productAddon.getProduct());
        this.setPlannerUom(productAddon.getUom());
        this.setPlannerRate(dsoRate);
        this.setPlannerQty(productAddon.getQuantity());
        this.setPlannerPrice(DSOPrice);
    }

    public ProposalBOQ(ProposalHeader proposalHeader, AssembledProductInQuote productInQuote, ProductModule module, BoqItem boqItem)
    {
        this.setProposalId(proposalHeader.getId());
        this.setSpaceType(productInQuote.getSpaceType());
        this.setRoom(productInQuote.getRoom());
        this.setCategory("Modular Products");
        this.setProductService(productInQuote.getProduct().getProductCategory());
        this.setProductId(productInQuote.getProduct().getId());
        this.setMgCode(module.getMGCode());
        this.setModuleSeq(module.getModuleSequence());
        this.setCustomCheck(module.getCustomCheck());
//        if (module.getCustomCheck().equals("yes"))
//        {
//            this.setCustomRemarks(module.getRemarks());
//        }
//        else
//        {
            this.setCustomRemarks("");
 //       }
        if (boqItem.getCode().startsWith(HANDLE_CODE))
        {
            this.setItemCategory(HANDLE_CODE);
        }
        else if (boqItem.getCode().startsWith(KNOB_CODE))
        {
            this.setItemCategory(KNOB_CODE);
        }
        else if (boqItem.getCode().startsWith(HINGE_CODE))
        {
            this.setItemCategory(HINGE_CODE);
        }
        else
        {
            this.setItemCategory("HW");
        }
        this.setDSOErpItemCode(boqItem.getErpCode());
        this.setDSOItemSeq(boqItem.getBoqDisplayOrder());

        this.setDSOReferencePartNo(boqItem.getCatalogueCode());
        this.setDSODescription(boqItem.getTitle());
        this.setDSOUom(boqItem.getUom());
        this.setDSORate(boqItem.getUnitRate());
        this.setDSOQty(boqItem.getQuantity());
        this.setDSOPrice(boqItem.getUnitRate() * boqItem.getQuantity());
        this.setPlannerErpCode(boqItem.getErpCode());
        this.setPlannerReferencePartNo(boqItem.getCatalogueCode());
        this.setPlannerDescription(boqItem.getTitle());
        this.setPlannerUom(boqItem.getUom());
        this.setPlannerRate(boqItem.getUnitRate());
        this.setPlannerQty(boqItem.getQuantity());
        this.setPlannerPrice(boqItem.getUnitRate() * boqItem.getQuantity());
    }

}
