package com.mygubbi.game.proposal.erp;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import com.mygubbi.game.proposal.*;
import com.mygubbi.game.proposal.model.*;
import com.mygubbi.game.proposal.price.HardwareComponent;
import com.mygubbi.game.proposal.price.RateCardService;
import com.mygubbi.game.proposal.quote.AssembledProductInQuote;
import com.mygubbi.game.proposal.quote.QuoteData;
import com.mygubbi.si.gdrive.DriveFile;
import com.mygubbi.si.gdrive.DriveServiceProvider;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Created by User on 21-07-2017.
 */
public class BoqCreatorService extends AbstractVerticle {

    public static final String CREATE_BOQ_OUTPUT = "create.proposal.boq.output";

    public DriveServiceProvider driveServiceProvider = new DriveServiceProvider();
    HardwareComponent hardwareComponent = new HardwareComponent();
    private ProposalBOQ proposal_boq ;
    private List<QueryData> queryDataListProduct = new ArrayList<>();
    private List<QueryData> queryDataListAddon =  new ArrayList<>();

    private final static Logger LOG = LogManager.getLogger(BoqCreatorService.class);

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        this.setupSowCreator();
        startFuture.complete();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    private void setupSowCreator()
    {
        EventBus eb = VertxInstance.get().eventBus();
        eb.localConsumer(CREATE_BOQ_OUTPUT, (Message<Integer> message) -> {
            LOG.info("INSIDE boq creator");
            JsonObject quoteRequest = (JsonObject) LocalCache.getInstance().remove(message.body());
            this.getProposalHeader(quoteRequest, message);
        }).completionHandler(res -> {
            LOG.info("BOQ service started." + res.succeeded());
        });
    }

    private void getProposalHeader(JsonObject quoteRequest, Message message)
    {
        Integer id = LocalCache.getInstance().store(new QueryData("proposal.header", new JsonObject().put("id", quoteRequest.getInteger("proposalId"))));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag || resultData.rows == null || resultData.rows.isEmpty())
                    {
                        message.reply(LocalCache.getInstance().store(new JsonObject().put("error", "Proposal not found for id:" + quoteRequest.getInteger("proposalId"))));
                        LOG.error("Proposal not found for id:" + quoteRequest.getInteger("proposalId"));
                    }
                    else
                    {
                        ProposalHeader proposalHeader = new ProposalHeader(resultData.rows.get(0));
                        checkIfBoqCreated(quoteRequest,proposalHeader,message);

                    }
                });

    }

    private void checkIfBoqCreated(JsonObject quoteRequest, ProposalHeader proposalHeader, Message message) {
        QueryData queryData = null;
        queryData = new QueryData("proposal.boq.proposalversion", new JsonObject().put("proposalId", quoteRequest.getInteger("proposalId")));
        Integer id = LocalCache.getInstance().store(queryData);
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    LOG.info("Parameter Values" +resultData.paramsObject);
                    if (resultData.errorFlag)
                    {
                        message.reply(LocalCache.getInstance().store(new JsonObject().put("error", "Proposal not found for id:" + quoteRequest.getInteger("proposalId"))));
                        LOG.error("Proposal not found for id:" + quoteRequest.getInteger("proposalId"));
                    }
                    else if ( resultData.rows == null || resultData.rows.isEmpty())
                    {
                        this.getProposalProductsBySpaces(proposalHeader, quoteRequest, message);
                    }
                    else
                    {
                        createBoqInDrive(quoteRequest,message);
                    }
                });
    }


    private void getProposalProductsBySpaces(ProposalHeader proposalHeader, JsonObject quoteRequest, Message message)
    {
        QueryData queryData = null;
        JsonObject paramsJson= new JsonObject().put("proposalId", proposalHeader.getId()).put("fromVersion",quoteRequest.getDouble("version"));
        queryData = new QueryData("proposal.product.all.detail", paramsJson);
        Integer id = LocalCache.getInstance().store(queryData);
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    LOG.info("Parameter Values" +resultData.paramsObject);
                    if (resultData.errorFlag || resultData.rows == null || resultData.rows.isEmpty())
                    {
                        message.reply(LocalCache.getInstance().store(new JsonObject().put("error", "Proposal products not found for id:" + proposalHeader.getId())));
                        LOG.error("Proposal products not found for id:" + proposalHeader.getId());
                        //return;
                    }
                    else
                    {
                        List<ProductLineItem> products = new ArrayList<ProductLineItem>();
                        for (JsonObject json : resultData.rows)
                        {
                                    products.add(new ProductLineItem(json));

                        }
                        this.getProposalAddonsBySpaces(quoteRequest, proposalHeader, products, message);
                    }
                });
    }

    private void getProposalAddonsBySpaces(JsonObject quoteRequest, ProposalHeader proposalHeader, List<ProductLineItem> products, Message message)
    {

        QueryData queryData = null;
        JsonObject paramsJson = new JsonObject().put("proposalId", proposalHeader.getId()).put("fromVersion",quoteRequest.getInteger("version"));
        LOG.debug("Proposal product addon :" + paramsJson.toString());
        queryData = new QueryData("proposal.version.addon.list", paramsJson);

        Integer id = LocalCache.getInstance().store(queryData);
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag)
                    {
                        message.reply(LocalCache.getInstance().store(new JsonObject().put("error", "Error in fetching Proposal level addons for :" + proposalHeader.getId())));
                        LOG.error("Proposal level addons not found for id:" + proposalHeader.getId() + resultData.errorMessage);
                        return;
                    }
                    else {
                        List<ProductAddon> addons = new ArrayList<ProductAddon>();
                        for (JsonObject json : resultData.rows)
                        {
                                    addons.add(new ProductAddon(json));
                        }
                        this.getProposalProducts(quoteRequest, proposalHeader, products, addons, message);
                    }
                });

    }

    private void getProposalProducts(JsonObject quoteRequest, ProposalHeader proposalHeader, List<ProductLineItem> products,
                                     List<ProductAddon> addons, Message  message)
    {
        QuoteData quoteData = new QuoteData(proposalHeader, products, addons, 0,"2.0","No");
        List<AssembledProductInQuote> assembledProducts = quoteData.getAssembledProducts();

        for (AssembledProductInQuote productInQuote : assembledProducts) {
            fillHardwareAndAccPerModule(quoteRequest,productInQuote,proposalHeader,message);
        }
        fillProposalAddons(quoteRequest,message,quoteData,proposalHeader);

    }


    private void fillProposalAddons(JsonObject quoteRequest, Message  message, QuoteData quoteData, ProposalHeader proposalHeader) {
        List<ProductAddon> addon_accessories = quoteData.getAccessories();
        List<ProductAddon> appliances = quoteData.getAppliances();
        List<ProductAddon> counter_tops = quoteData.getCounterTops();
        List<ProductAddon> loose_furniture = quoteData.getLooseFurniture();
        List<ProductAddon> services = quoteData.getServices();
        List<ProductAddon> master_addons = new ArrayList<>();

        if (addon_accessories.size() != 0)
        {
            for (ProductAddon productAddon : addon_accessories)
            {
                master_addons.add(productAddon);
            }
        }

        if (appliances.size() != 0)
        {
            for (ProductAddon productAddon : appliances)
            {
                master_addons.add(productAddon);
            }
        }

        if (counter_tops.size() != 0)
        {
            for (ProductAddon productAddon : counter_tops)
            {
                master_addons.add(productAddon);
            }
        }

        if (loose_furniture.size() != 0)
        {
            for (ProductAddon productAddon : loose_furniture)
            {
                master_addons.add(productAddon);
            }
        }

        if (services.size() != 0)
        {
            for (ProductAddon productAddon : services)
            {
                master_addons.add(productAddon);
            }
        }

        fillAllAddonsintoDb(master_addons,proposalHeader);
    }


    private void fillHardwareAndAccPerModule(JsonObject quoteRequest,AssembledProductInQuote productInQuote, ProposalHeader proposalHeader, Message message)
    {



        String product = productInQuote.getProduct().getProductCategory();

        List<ProductModule> modules = productInQuote.getModules();

        LOG.debug("Modules size :" + modules.size());

        for (ProductModule module : modules) {
            LOG.debug("Module :" + module);
            Collection<ModuleComponent> moduleComponents = ModuleDataService.getInstance().getModuleComponents(module.getMGCode());

            List<BoqItem> boqItemListMaster = new ArrayList<>();
            boqItemListMaster.addAll(collectModuleHandles(module,proposalHeader.getPriceDate(),proposalHeader.getProjectCity(),productInQuote));
            boqItemListMaster.add(collectModuleKnob(module,proposalHeader.getPriceDate(),proposalHeader.getProjectCity()));
            List<BoqItem> c = collectModuleHinge(module, proposalHeader.getPriceDate(), proposalHeader.getProjectCity());
            LOG.debug("Hinge size :" + c.size());
            boqItemListMaster.addAll(c);

            for (ModuleComponent moduleComponent: moduleComponents)
            {
                LOG.debug("Module Component :" + moduleComponent);
                if (moduleComponent.getType().equals("H"))
                {
                    double quantity = moduleComponent.getQuantity();
                    if (!(moduleComponent.getQuantityFlag() == null || moduleComponent.getQuantityFlag().isEmpty() ))
                    {
                        if (moduleComponent.getQuantityFlag().equals("C"))
                        {
                            quantity = hardwareComponent.calculateQuantityUsingFormula(module,moduleComponent);
                        }
                    }

                    AccHwComponent accHwComponent = ModuleDataService.getInstance().getHardware(moduleComponent.getComponentCode());
                    PriceMaster addonRate = RateCardService.getInstance().getHardwareRate(moduleComponent.getComponentCode(), productInQuote.getPriceDate(), productInQuote.getCity());
                    ProposalBOQ proposalBOQ = new ProposalBOQ(productInQuote, proposalHeader, product, module, quantity, accHwComponent, addonRate);

                    queryDataListProduct.add(new QueryData("proposal.boq.create",proposalBOQ));


                }
            }

            fillDataForHandleKnobAndHinge(productInQuote,module,proposalHeader, boqItemListMaster);

            fillDataForAccessoryPacks(productInQuote,module, proposalHeader,queryDataListProduct);

        }
        insertBoqLineItemsForProductAndAddons(quoteRequest,queryDataListProduct,queryDataListAddon,message);

    }

    private void fillDataForHandleKnobAndHinge(AssembledProductInQuote productInQuote, ProductModule module, ProposalHeader proposalHeader, List<BoqItem> boqItems) {

        for (BoqItem boqItem : boqItems)
        {
            proposal_boq = new ProposalBOQ(proposalHeader,productInQuote,module,boqItem);
            this.queryDataListProduct.add(new QueryData("proposal.boq.create",proposal_boq));
        }
    }


    private void fillDataForAccessoryPacks(AssembledProductInQuote productInQuote, ProductModule module, ProposalHeader proposalHeader, List<QueryData> queryDatas) {

        for (ModuleAccessoryPack modAccessoryPack : module.getAccessoryPacks()) {
            Collection<AccessoryPackComponent> accessoryPackComponents =
                    ModuleDataService.getInstance().getAccessoryPackComponents(modAccessoryPack.getAccessoryPackCode());
            if (accessoryPackComponents == null) continue;
            for (AccessoryPackComponent accessoryPackComponent : accessoryPackComponents) {
                double quantity = accessoryPackComponent.getQuantity();
                if (accessoryPackComponent.getQuantityFlag().equals("C")) {
                    quantity = hardwareComponent.calculateQuantityUsingFormula(module, accessoryPackComponent);
                }
                PriceMaster addonRate = null;

                if (accessoryPackComponent.isHardware())
                {
                    addonRate = RateCardService.getInstance().getHardwareRate(accessoryPackComponent.getComponentCode(), productInQuote.getPriceDate(), productInQuote.getCity());
                }
                else if (accessoryPackComponent.isAccessory())
                {
                    addonRate = RateCardService.getInstance().getAccessoryRate(accessoryPackComponent.getComponentCode(), productInQuote.getPriceDate(), productInQuote.getCity());
                }

                if (accessoryPackComponent.isAccessory() || accessoryPackComponent.isHardware()) {
                    AccHwComponent accHwComponent = ModuleDataService.getInstance().getAccessory(accessoryPackComponent.getComponentCode());
                    if (accHwComponent == null) continue;
                    proposal_boq = new ProposalBOQ(productInQuote, module, proposalHeader, quantity, addonRate, accHwComponent);
                    queryDatas.add(new QueryData("proposal.boq.create",proposal_boq));
                }
            }
        }
    }

    private void fillAllAddonsintoDb(List<ProductAddon> productAddons, ProposalHeader proposalHeader)
    {
        List<QueryData> queryDatasAddons = new ArrayList<>();


        for (ProductAddon productAddon : productAddons)
        {
            PriceMaster addonPrice = RateCardService.getInstance().getAddonRate(productAddon.getCode(),proposalHeader.getPriceDate(),proposalHeader.getProjectCity());
            proposal_boq = new ProposalBOQ(productAddon, addonPrice);

            queryDatasAddons.add(new QueryData("proposal.boq.create",proposal_boq));

        }

    }


    private void insertBoqLineItemsForProductAndAddons(JsonObject quoteRequest, List<QueryData> queryDataListProduct,List<QueryData> queryDataListAddon, Message message)
    {
        LOG.debug("QueryData List product size :" + queryDataListProduct.size());
        LOG.debug("QueryData List addon size :" + queryDataListAddon.size());
        List<QueryData> queryDataMasterList = new ArrayList<>();
        queryDataMasterList.addAll(queryDataListProduct);
        queryDataMasterList.addAll(queryDataListAddon);
        Integer id = LocalCache.getInstance().store(queryDataMasterList);
        VertxInstance.get().eventBus().send(DatabaseService.MULTI_DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    List<QueryData> resultData = (List<QueryData>) LocalCache.getInstance().remove(selectResult.result().body());
                    for (int i = 0; i < resultData.size() ; i++)
                    {
                        if (resultData.get(i).errorFlag || resultData.get(i).updateResult == null)
                        {
                            message.reply(LocalCache.getInstance().store(new JsonObject().put("error", "ProposalBoq could not be inserted for product id:" + quoteRequest.getInteger("proposalId"))));
                            LOG.error("ProposalBoq could not be inserted for product id:" + quoteRequest.getInteger("proposalId"));
                        }
                        else
                        {

                        }

                    }
                    createBoqInDrive(quoteRequest,message);

                });

    }

    private void createBoqInDrive(JsonObject quoteRequest, Message message) {
        LOG.debug("Create boq in drive");

        List<QueryData> queryDatas = new ArrayList<>();

        LOG.debug("Create BOQ in drive :" + quoteRequest);

        queryDatas.add(new QueryData("proposal.header", new JsonObject().put("id", quoteRequest.getInteger("proposalId"))));
        queryDatas.add(new QueryData("proposal.boq.proposalversion", new JsonObject().put("proposalId", quoteRequest.getInteger("proposalId")).put("version",quoteRequest.getInteger("version"))));
        queryDatas.add(new QueryData("erp.master.select",quoteRequest));

        Integer id = LocalCache.getInstance().store(queryDatas);
        VertxInstance.get().eventBus().send(DatabaseService.MULTI_DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    List<QueryData> resultData = (List<QueryData>) LocalCache.getInstance().remove(selectResult.result().body());

                    if (resultData.get(0).errorFlag || resultData.get(1).errorFlag)
                    {
                        LOG.debug("error inside");
                        message.reply(LocalCache.getInstance().store(new JsonObject().put("error", "Proposal boq not found for id:" + quoteRequest.getInteger("proposalId"))));
                        return;
                    }
                    else
                    {
                        ProposalHeader proposalHeader = new ProposalHeader(resultData.get(0).rows.get(0));
                        List<JsonObject> sow_jsons = resultData.get(1).rows;
                        List<ProposalBOQ> proposal_boqs = new ArrayList<ProposalBOQ>();
                        for (JsonObject proposalBoq : sow_jsons)
                        {
                            proposal_boqs.add(new ProposalBOQ(proposalBoq));
                        }

                        List<JsonObject> erp_jsons = resultData.get(2).rows;
                        List<ERPMaster> erpMasters = new ArrayList<>();
                        for (JsonObject erpMaster : erp_jsons)
                        {
                            erpMasters.add(new ERPMaster(erpMaster));
                        }
                        createBoqAndUploadTDrive(quoteRequest,message,proposalHeader, proposal_boqs, erpMasters);
                    }

                });


    }


    private void createBoqAndUploadTDrive(JsonObject quoteRequest, Message message, ProposalHeader proposalHeader, List<ProposalBOQ> proposalBoqs, List<ERPMaster> erpMasters) {

        LOG.debug("QUote request inside createAndUploadDrive : " + quoteRequest);
        List<ProposalBOQ> proposalBOQsForProduct = new ArrayList<>();
        List<ProposalBOQ> proposalBOQsForAddon = new ArrayList<>();
        for (ProposalBOQ proposalBOQ : proposalBoqs)
        {
            if (proposalBOQ.getcategory().equals("ADDON"))
            {
                proposalBOQsForAddon.add(proposalBOQ);
            }
            else
            {
                proposalBOQsForProduct.add(proposalBOQ);
            }
        }

        LOG.info("proposalBoqs size = "+proposalBoqs);
        if(proposalBoqs.size()  == 0){
            JsonObject res = new JsonObject();
            res.put("status", "Failure");
            res.put("comments", "Please add the products/addons before creating BOQ");
            LOG.info(res.toString());
            sendResponse(message, res);

        }else {
            DriveFile driveFile;
            try {
                LOG.debug("Proposal Boq size :" + proposalBoqs.size() );
                LOG.debug("ERP Master size in boq creator service :" + erpMasters.size() );
                String outputFile = new BOQTemplateCreator(proposalHeader,proposalBOQsForProduct,proposalBOQsForAddon, erpMasters).create();
                driveFile = this.driveServiceProvider.uploadFileForUser(outputFile, quoteRequest.getString("userId"), proposalHeader.getQuoteNumNew() + "_BOQ", proposalHeader.getSalesEmail(), quoteRequest.getString("readOnlyFlag"));
                JsonObject res = new JsonObject();
                res.put("status", "Success");
                res.put("driveWebViewLink", driveFile.getWebViewLink());
                res.put("id", driveFile.getId());
                res.put("outputFile", outputFile);
                res.put("version", quoteRequest.getString("sowversion"));
                LOG.info(res.toString());

                sendResponse(message, res);
            } catch (Exception e) {
                LOG.debug("Exception :" + e.getMessage() + e.getCause() + e.getStackTrace());
                sendResponse(message, new JsonObject().put("status","failure").put("driveWebViewLink","").put("id","").put("outputFile",""));
                e.printStackTrace();
            }
        }
    }


    private List<BoqItem> collectModuleHandles(ProductModule module, Date priceDate, String city, AssembledProductInQuote assembledProductInQuote)
    {
        List<BoqItem> boqItems = new ArrayList<>();

        String NORMAL = "Normal";
        String GOLA_PROFILE = "Gola Profile";
        String CORNER_UNIT = "Corner";
        String LOFTS = "Loft";

        PriceMaster lWidthRate = RateCardService.getInstance().getHardwareRate("H073",priceDate , city);
        PriceMaster cWidthRate = RateCardService.getInstance().getHardwareRate("H071",priceDate, city);
        PriceMaster wWidthRate = RateCardService.getInstance().getHardwareRate("H076", priceDate, city);
        PriceMaster bracketRate = RateCardService.getInstance().getHardwareRate("H075", priceDate, city);
        PriceMaster lConnectorRate = RateCardService.getInstance().getHardwareRate("H074", priceDate, city);
        PriceMaster cConnectorRate = RateCardService.getInstance().getHardwareRate("H072", priceDate, city);
        PriceMaster gProfileRate = RateCardService.getInstance().getHardwareRate("H018", priceDate, city);
        PriceMaster jProfileRate = RateCardService.getInstance().getHardwareRate("H077", priceDate, city);


        if (Objects.equals(assembledProductInQuote.getProduct().getHandletypeSelection(),GOLA_PROFILE ))
        {

            LOG.debug("Inside gola profile");

            AccHwComponent hardware = ModuleDataService.getInstance().getHardware(wWidthRate.getRateId());
            boqItems.add(new BoqItem(hardware.getCode(),hardware.getCatalogCode(),hardware.getERPCode(),hardware.getUom(),1,priceDate,city));

            AccHwComponent hardware1 = ModuleDataService.getInstance().getHardware(lWidthRate.getRateId());
            boqItems.add(new BoqItem(hardware1.getCode(),hardware1.getCatalogCode(),hardware1.getERPCode(),hardware1.getUom(),1,priceDate,city));

            AccHwComponent hardware2 = ModuleDataService.getInstance().getHardware(wWidthRate.getRateId());
            boqItems.add(new BoqItem(hardware2.getCode(),hardware2.getCatalogCode(),hardware2.getERPCode(),hardware2.getUom(),1,priceDate,city));

            AccHwComponent hardware3 = ModuleDataService.getInstance().getHardware(bracketRate.getRateId());
            boqItems.add(new BoqItem(hardware3.getCode(),hardware3.getCatalogCode(),hardware3.getERPCode(),hardware3.getUom(),1,priceDate,city));

//                lConnectorPrice = golaProfileLength * this.lConnectorRate.getPrice();
            AccHwComponent hardware4 = ModuleDataService.getInstance().getHardware(cConnectorRate.getRateId());
            boqItems.add(new BoqItem(hardware4.getCode(),hardware4.getCatalogCode(),hardware4.getERPCode(),hardware4.getUom(),1,priceDate,city));


        }
        if (Objects.equals(assembledProductInQuote.getProduct().getHandletypeSelection(), "G Profile")){

            LOG.debug("G Profile : ");

            double quantity = 0;

            Collection<AccessoryPackComponent> handles = ModuleDataService.getInstance().getAccessoryPackComponents(module.getMGCode());
            for (AccessoryPackComponent accessoryPackComponent : handles)
            {
                quantity = accessoryPackComponent.getQuantity();
            }
            AccHwComponent hardware5 = ModuleDataService.getInstance().getHardware(gProfileRate.getRateId());
            boqItems.add(new BoqItem(hardware5.getCode(),hardware5.getCatalogCode(),hardware5.getERPCode(),hardware5.getUom(),quantity,priceDate,city));

        }
        if (Objects.equals(assembledProductInQuote.getProduct().getHandletypeSelection(), "J Profile"))
        {
            LOG.debug("J profile : ");

            double quantity = 0;

            Collection<AccessoryPackComponent> handles = ModuleDataService.getInstance().getAccessoryPackComponents(module.getMGCode());
            for (AccessoryPackComponent accessoryPackComponent : handles)
            {
                quantity = accessoryPackComponent.getQuantity();
            }

            AccHwComponent hardware6 = ModuleDataService.getInstance().getHardware(jProfileRate.getRateId());
            boqItems.add(new BoqItem(hardware6.getCode(),hardware6.getCatalogCode(),hardware6.getERPCode(),hardware6.getUom(),quantity,priceDate,city));
            LOG.debug("J profile rate : " +  jProfileRate.getPrice());


        }
        if(module.getHandleCode()==null)
        {
            return null;
        }
        Handle handle = ModuleDataService.getInstance().getHandleKnobHingeDetails(module.getHandleCode());
        boqItems.add(new BoqItem(handle.getCode(),handle.getArticleNo(),handle.getErpCode(),"Nos",module.getHandleQuantity(),priceDate,city));

        return boqItems;

    }


    private BoqItem collectModuleKnob(ProductModule module,Date priceDate, String city)
    {
        if(module.getKnobCode()==null)
        {
            return null;
        }
        Handle knob = ModuleDataService.getInstance().getHandleKnobHingeDetails(module.getKnobCode());
        LOG.debug("KNOB to be printed : " + knob);

        return new BoqItem(knob.getCode(),knob.getArticleNo(),knob.getErpCode(),"Nos",module.getHandleQuantity(),priceDate,city);

    }

    private List<BoqItem> collectModuleHinge(ProductModule module,Date priceDate, String city)
    {
        LOG.debug("Inside collect module hinge BOQ creator service");
        List<BoqItem> boqItems = new ArrayList<>();
        if (module.getHingePacks().size() == 0)
        {
            return null;
        }
        else{
            for (HingePack hingePack : module.getHingePacks()) {
                Handle hinge = ModuleDataService.getInstance().getHandleKnobHingeDetails(hingePack.getHingeCode());
                LOG.debug("Inside collect module hinge :" + hingePack);
                LOG.info("hingePack " +hingePack.toString());
                boqItems.add(new BoqItem(hingePack.getHingeCode(),hinge.getArticleNo(),hinge.getErpCode(),"Nos",module.getHandleQuantity(),priceDate,city));
            }}

        return boqItems;
    }



    private void sendResponse(Message message, JsonObject response)
    {
        LOG.debug("Response :" + response.encodePrettily());
        message.reply(LocalCache.getInstance().store(response));
    }


}
