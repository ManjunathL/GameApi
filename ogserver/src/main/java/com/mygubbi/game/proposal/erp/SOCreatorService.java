package com.mygubbi.game.proposal.erp;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import com.mygubbi.game.proposal.model.*;
import com.mygubbi.game.proposal.quote.QuoteRequest;
import com.mygubbi.si.gdrive.DriveFile;
import com.mygubbi.si.gdrive.DriveServiceProvider;
import com.mygubbi.si.gdrive.UploadToDrive;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by User on 22-09-2017.
 */
public class SOCreatorService extends AbstractVerticle {

    public static final String CREATE_SO_OUTPUT = "create.proposal.so.output";

    private final static Logger LOG = LogManager.getLogger(BoqCreatorService.class);

    private String userId = null;

    public DriveServiceProvider driveServiceProvider = new DriveServiceProvider();

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        this.setUpSoCreator();
        startFuture.complete();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    private void setUpSoCreator()
    {
        EventBus eb = VertxInstance.get().eventBus();
        eb.localConsumer(CREATE_SO_OUTPUT, (Message<Integer> message) -> {
            int count = 0;
            LOG.info("INSIDE so creator :" + ++count);
            JsonObject quoteRequest = (JsonObject) LocalCache.getInstance().remove(message.body());
            this.createsoextract(quoteRequest, message);
        }).completionHandler(res -> {
            LOG.info("SO service started." + res.succeeded());
        });
    }

    private void createsoextract(JsonObject quoteRequest,Message message)
    {
        int proposalId = quoteRequest.getInteger("proposalId");
        userId = quoteRequest.getString("userId");
        Integer id = LocalCache.getInstance().store(new QueryData("proposal.boq.select.products.all",new JsonObject().put("proposalId",proposalId)));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,  new DeliveryOptions().setSendTimeout(120000),
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag || resultData.rows.size() == 0) {
                        LOG.error("Error in retrieving. " + resultData.errorMessage, resultData.error);
                        JsonObject res = new JsonObject();
                        res.put("status", "Failure");
                        res.put("comments", "Could not retrieve ");
                        message.reply(LocalCache.getInstance().store(res));
                        return;
                    } else {
                        List<ProposalBOQ> proposal_boqs_products = new ArrayList<>();
                        List<ProposalBOQ> proposal_boqs_addons = new ArrayList<>();
                        List<ProposalBOQ> proposal_boqs_services = new ArrayList<>();

                        List<JsonObject> jsonObjects = resultData.rows;
                        for (JsonObject jsonObject1 : jsonObjects)
                        {
                            ProposalBOQ proposal_boq = new ProposalBOQ(jsonObject1);
                            if (proposal_boq.getcategory().equals("Modular Products"))
                            {
                                proposal_boqs_products.add(proposal_boq);
                            }
                            else
                            {
                                proposal_boqs_addons.add(proposal_boq);
                            }

                        }
                        LOG.debug("Proposal boq addons size : " + proposal_boqs_addons.size());

                       /* LOG.debug("Modular products size :" + proposal_boqs_products.size());
                        LOG.debug("Services size :" + proposal_boqs_services.size());
                        LOG.debug("Addons size :" + proposal_boqs_addons.size());*/

                        generateSo(quoteRequest, proposal_boqs_products,proposal_boqs_addons,proposal_boqs_services, message);

                    }
                });
    }



    private void generateSo(JsonObject routingContext, List<ProposalBOQ> boqProductObjects, List<ProposalBOQ> boqAddonObjects, List<ProposalBOQ> boqServiceObjects, Message message) {

        DriveFile driveFile = null;
        List<UploadToDrive> outputFiles = new ArrayList<>();

        if (!(boqProductObjects.size() == 0))
        {
//            LOG.debug("generateSo:" + boqProductObjects.size());
            outputFiles = generateSoForProduct(boqProductObjects);
        }
//        LOG.debug("Output files size :" + outputFiles.size());
        if (!(boqAddonObjects.size() == 0))
        {
            UploadToDrive outputFileForAddon = generateSoForAddon(boqAddonObjects);
            if (outputFileForAddon != null) outputFiles.add(outputFileForAddon);
        }

        try {
            LOG.debug("Before calling method :" + userId);
            driveFile = this.driveServiceProvider.createFolder(outputFiles,"BOQ_"+ boqProductObjects.get(0).getProposalId()  ,userId);
        } catch (Exception e) {

            LOG.debug("Exception newly found" + e.getMessage());
        }

        JsonObject res = new JsonObject();
        res.put("status", "Success");
        res.put("driveWebViewLink", driveFile.getWebViewLink());
        res.put("id", driveFile.getId());
        res.put("boqStatus","Yes");
        res.put("proposalId", boqProductObjects.get(0).getProposalId());

        updateProposalHeader(routingContext,message, res);

    }

    private List<UploadToDrive> generateSoForProduct(List<ProposalBOQ> boqProductObjects) {

//        LOG.debug("generateSoForProduct:" + boqProductObjects.size());
        Map<SpaceRoomProduct,List<ProposalBOQ>> spaceRoomProducts = getDistinctSpaceRoomProducts(boqProductObjects);

//        LOG.debug("Distinct space room product :" + spaceRoomProducts.size());
        List<UploadToDrive> outputFiles = new ArrayList<>();

        for (SpaceRoomProduct spaceRoomProduct : spaceRoomProducts.keySet()) {

            UploadToDrive uploadToDrive = new UploadToDrive();

            List<ProposalBOQ> proposalBoqAsPerProduct = spaceRoomProducts.get(spaceRoomProduct);
//            LOG.debug("Proposal BOQ as per product size : " + proposalBoqAsPerProduct.size() + " :" + proposalBoqAsPerProduct.get(0).getProductId());
            List<SOPart> soPartsList = new ArrayList<>();

            for (ProposalBOQ proposalBOQ : proposalBoqAsPerProduct) {
                SOPart soPart = new SOPart(proposalBOQ.getPlannerErpItemCode(), proposalBOQ.getPlannerReferencePartNo(), proposalBOQ.getPlannerUom(), proposalBOQ.getPlannerDescription(), proposalBOQ.getPlannerQty());
                soPartsList.add(soPart);
            }


            Map<String,SOPart> soPartMap = new HashMap<>();

            for (SOPart soPart : soPartsList) {

                if (soPartMap.containsKey(soPart.getErpCode()))
                {
                    SOPart soPart1 = soPartMap.get(soPart.getErpCode());
                    double qty = soPart1.getQty() + soPart.getQty();
                    soPart1.setQty(qty);
                    soPartMap.put(soPart.getErpCode(),soPart1);
                }
                else {
                    soPartMap.put(soPart.getErpCode(),soPart);
                }
            }

            soPartsList.clear();
            soPartsList = soPartMap.values().stream().collect(Collectors.toList());

            String outputFile = new SOExtractTemplateCreator(soPartsList, spaceRoomProduct.getProductId(), proposalBoqAsPerProduct.get(0).getProposalId()).create();
            uploadToDrive.setFileName(proposalBoqAsPerProduct.get(0).getProductService());
            uploadToDrive.setFilePath(outputFile);

            outputFiles.add(uploadToDrive);
        }
        return outputFiles;
    }

    private Map<SpaceRoomProduct,List<ProposalBOQ>> getDistinctSpaceRoomProducts(List<ProposalBOQ> proposalBoqs)
    {
        Map<SpaceRoomProduct, List<ProposalBOQ>> spaceRoomProductMap = new HashMap<>();
        for (ProposalBOQ boq : proposalBoqs)
        {
            SpaceRoomProduct spaceRoom = new SpaceRoomProduct(boq);
            if (!spaceRoomProductMap.containsKey(spaceRoom))
            {
                spaceRoomProductMap.put(spaceRoom,new ArrayList<>());
            }
            spaceRoomProductMap.get(spaceRoom).add(boq);
        }
        return spaceRoomProductMap;
    }

    private UploadToDrive generateSoForAddon(List<ProposalBOQ> boqAddonObjects) {

        UploadToDrive uploadToDrive = null;

        if (boqAddonObjects.size() == 0)
        {
            return null;
        }
        else
        {
            List<SOPartForAddon> soPartForAddonList = new ArrayList<>();
            for (ProposalBOQ proposalBOQs : boqAddonObjects)
            {
                    SOPartForAddon soPart = new SOPartForAddon(proposalBOQs.getProductService(),proposalBOQs.getDsoDescription(),proposalBOQs.getDsoUom(),proposalBOQs.getDsoQty(),proposalBOQs.getROOM());
                    soPartForAddonList.add(soPart);
            }

            String outputFile = new SOExtractTemplateCreatorForAddons(soPartForAddonList, boqAddonObjects.get(0).getProposalId()).create();
            uploadToDrive = new UploadToDrive();
            uploadToDrive.setFileName("Addons");
            uploadToDrive.setFilePath(outputFile);

        }



        return uploadToDrive;
    }

    private void updateProposalHeader(JsonObject quoteRequest, Message message,JsonObject result) {
        int proposalId = quoteRequest.getInteger("proposalId");
        Integer id = LocalCache.getInstance().store(new QueryData("proposal.header.boqupdate", result));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag || resultData.updateResult.getUpdated() == 0)
                    {
                        message.reply(LocalCache.getInstance().store(new JsonObject().put("error", "Error in updating header for :" + proposalId)));
                        LOG.error("Proposal not found for id:" + proposalId);
                    }
                    else
                    {
                        LOG.debug("Result :" + result.encodePrettily());
                        message.reply(LocalCache.getInstance().store(result));
                    }
                });
    }

}
