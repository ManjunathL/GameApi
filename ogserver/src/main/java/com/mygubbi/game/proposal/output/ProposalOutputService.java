package com.mygubbi.game.proposal.output;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.config.ConfigHolder;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import com.mygubbi.game.QuoteSOWPDFCreator;
import com.mygubbi.game.proposal.ProductAddon;
import com.mygubbi.game.proposal.ProductLineItem;
import com.mygubbi.game.proposal.model.ProposalHeader;
import com.mygubbi.game.proposal.model.ProposalVersion;
import com.mygubbi.game.proposal.model.SOWPdf;
import com.mygubbi.game.proposal.quote.QuoteData;
import com.mygubbi.game.proposal.quote.QuoteRequest;
import com.mygubbi.game.proposal.sow.SpaceRoom;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sunil on 08-01-2016.
 */

public class ProposalOutputService extends AbstractVerticle
{
    private final static Logger LOG = LogManager.getLogger(ProposalOutputService.class);

    public static final String CREATE_PROPOSAL_OUTPUT = "create.proposal.output";
    List<SpaceRoom> spaceRooms = new ArrayList<>();
    @Override
    public void start(Future<Void> startFuture) throws Exception
    {
        this.setupProposalOutput();
        startFuture.complete();
    }

    @Override
    public void stop() throws Exception
    {
        super.stop();
    }

    private void setupProposalOutput()
    {
        EventBus eb = VertxInstance.get().eventBus();
        eb.localConsumer(CREATE_PROPOSAL_OUTPUT, (Message<Integer> message) -> {
            QuoteRequest quoteRequest = (QuoteRequest) LocalCache.getInstance().remove(message.body());
            this.getProposalHeader(quoteRequest, message);
        }).completionHandler(res -> {
            LOG.info("Proposal output service started." + res.succeeded());
        });

    }


    private void getProposalHeader(QuoteRequest quoteRequest, Message message)
    {
        List<QueryData> queryDataList = new ArrayList<>();
        queryDataList.add(new QueryData("proposal.header", new JsonObject().put("id", quoteRequest.getProposalId())));
        queryDataList.add(new QueryData("proposal.version.selectversion", new JsonObject().put("proposalId", quoteRequest.getProposalId()).put("version",quoteRequest.getToVersion())));
        Integer id = LocalCache.getInstance().store(queryDataList);
        VertxInstance.get().eventBus().send(DatabaseService.MULTI_DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    List<QueryData> resultData = (List<QueryData>) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.get(0).errorFlag || resultData.get(1).errorFlag)
                    {
                        message.reply(LocalCache.getInstance().store(new JsonObject().put("error", "Proposal not found for id:" + quoteRequest.getProposalId())));
                        LOG.error("Proposal not found for id:" + quoteRequest.getProposalId());
                    }
                    else
                    {
                        ProposalHeader proposalHeader = new ProposalHeader(resultData.get(0).rows.get(0));
                        ProposalVersion proposalVersion = new ProposalVersion(resultData.get(1).rows.get(0));
                        this.getProposalProducts(proposalHeader, quoteRequest, message,proposalVersion);
                    }
                });

    }

    private void getProposalProducts(ProposalHeader proposalHeader, QuoteRequest quoteRequest, Message message, ProposalVersion proposalVersion)
    {
        QueryData queryData = null;
        JsonObject paramsJson= new JsonObject().put("proposalId", proposalHeader.getId()).put("fromVersion",quoteRequest.getToVersion());

//        if (quoteRequest.hasProductIds())
//        {
//            queryData = new QueryData("proposal.product.selected.detail", paramsJson.put("productIds", quoteRequest.getProductIdsAsText()));
//            LOG.debug("paramsJson :" + paramsJson.encodePrettily());
//        }
//        else
//        {
//            queryData = new QueryData("proposal.product.all.detail", paramsJson);
//        }
        queryData = new QueryData("proposal.product.all.detail", paramsJson);
        List<ProductLineItem> products = new ArrayList<ProductLineItem>();
        Integer id = LocalCache.getInstance().store(queryData);
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    LOG.info("Parameter Values" +resultData.paramsObject);
                    if (resultData.errorFlag || resultData.rows == null || resultData.rows.isEmpty())
                    {
                        //message.reply(LocalCache.getInstance().store(new JsonObject().put("error", "Proposal products not found for id:" + proposalHeader.getId())));
                        LOG.error("Proposal products not found for id:" + proposalHeader.getId());
                        //return;
                        this.getProposalAddons(quoteRequest, proposalHeader,products, message, proposalVersion);
                    }
                    else
                    {
                        for (JsonObject json : resultData.rows)
                        {
                            if (quoteRequest.hasProductIds())
                            {
                                if (quoteRequest.getProductIds().contains(json.getInteger("id")))
                                {
                                    products.add(new ProductLineItem(json));
                                }
                            }
                            else
                            {
                                products.add(new ProductLineItem(json));
                            }
                        }
                        this.getProposalAddons(quoteRequest, proposalHeader, products, message,proposalVersion);
                    }
                });
    }

    private void getProposalAddons(QuoteRequest quoteRequest, ProposalHeader proposalHeader, List<ProductLineItem> products, Message message, ProposalVersion proposalVersion)
    {

        QueryData queryData = null;
        JsonObject paramsJson = new JsonObject().put("proposalId", proposalHeader.getId()).put("fromVersion",quoteRequest.getToVersion());
        LOG.debug("Proposal product addon :" + paramsJson.toString());
        /*JsonObject paramsJson= new JsonObject().put("proposalId", proposalHeader.getId()).put("fromVersion",quoteRequest.getFromVersion());
        LOG.info("get proposal products from Version" +quoteRequest.getFromVersion());*/
//        if (quoteRequest.hasAddonIds())
//        {
//            queryData = new QueryData("proposal.addon.selected.detail", paramsJson.put("addonIds", quoteRequest.getAddonIdsAsText()));
//        }
//        else
//        {
//            queryData = new QueryData("proposal.addon.list", paramsJson);
//        }
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
                            if (quoteRequest.hasAddonIds())
                            {
                                if (quoteRequest.getAddonIds().contains(json.getInteger("id")))
                                {
                                    addons.add(new ProductAddon(json));
                                }
                            }
                            else
                            {
                                addons.add(new ProductAddon(json));
                            }
                        }
                        this.getSowRows(quoteRequest, proposalHeader, products, addons, message,proposalVersion);
                        //this.createQuote(quoteRequest, proposalHeader, products, addons, message, proposalVersion);
                    }
                });

    }


    private void getSowRows(QuoteRequest quoteRequest, ProposalHeader proposalHeader, List<ProductLineItem> products,
                            List<ProductAddon> addons, Message  message,ProposalVersion proposalVersion)
    {
        JsonObject jsonObject=new JsonObject();
        List<SOWPdf> proposalSOWs = new ArrayList<SOWPdf>();

        QuoteData quoteData = new QuoteData(proposalHeader, products, addons, quoteRequest.getDiscountAmount(),quoteRequest.getToVersion(),quoteRequest.getBookingFormFlag(),quoteRequest.getDiscountPercentage(),quoteRequest.getWorkscontractFlag());
        String sowversion = "1.0";
        String version = quoteData.fromVersion;

        if (version.contains("1.") || version.contains("2.0")){
            sowversion = "2.0";
        }else if (version.contains("2.") || version.contains("3.")){
            sowversion = "3.0";
        }

        String remarks;
        if (sowversion.equals("1.0")){
            remarks = proposalHeader.getSowRemarksV1();
        }
        else{
            remarks = proposalHeader.getSowRemarksV2();

        }

        if (remarks == null) remarks = "";

        LOG.info ("Version here :: "+sowversion);
        jsonObject.put("version",sowversion);
        jsonObject.put("proposalId",proposalHeader.getId());

        Integer id = LocalCache.getInstance().store(new QueryData("proposal.sow.select.forpdfDownload", jsonObject));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<io.vertx.core.eventbus.Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag || resultData.rows.size() == 0)
                    {
                        LOG.error("Error in fetching data from proposal_sow table " + resultData.errorMessage, resultData.error);
                    }
                    else
                    {
                        resultData.rows.forEach(item->{proposalSOWs.add(new SOWPdf(item));});
                        //this.createSow(quoteRequest, proposalHeader, products, addons, proposalSOWs,quoteData,message);
                        this.createQuote(quoteRequest, proposalHeader, products, addons, message, proposalVersion,proposalSOWs);

                    }

                });

    }

    private void createSow(QuoteRequest quoteRequest, ProposalHeader proposalHeader, List<ProductLineItem> products,
                           List<ProductAddon> addons, List<SOWPdf> proposalSOWs,QuoteData quoteData,Message  message)
    {
        for (ProductLineItem productLineItem : products)
        {
            SpaceRoom spaceRoom = new SpaceRoom(productLineItem.getSpaceType(),productLineItem.getRoomCode());
            if (!spaceRooms.contains(spaceRoom))
            {
                spaceRooms.add(spaceRoom);
            }
        }
        for (ProductAddon productAddon : addons)
        {
            SpaceRoom spaceRoom = new SpaceRoom(productAddon.getSpaceType(),productAddon.getRoomCode());
            if (!spaceRooms.contains(spaceRoom))
            {
                spaceRooms.add(spaceRoom);
            }
        }

        LOG.info("SpaceRoom List size " +spaceRooms.size());
        for(SpaceRoom spaceRoom:spaceRooms)
        {
            LOG.info("space room data " +spaceRoom);
        }

        try
        {
            ProposalVersion proposalVersion=new ProposalVersion();
            ProposalOutputCreator outputCreator = ProposalOutputCreator.getCreator(quoteRequest.getOutputType(), quoteData,proposalHeader,false,proposalSOWs,proposalVersion,spaceRooms);
            outputCreator.create();

            QuoteSOWPDFCreator quoteSOWPDFCreator=new QuoteSOWPDFCreator(proposalHeader,quoteData,proposalSOWs);
            String proposalFolder = ConfigHolder.getInstance().getStringValue("proposal_docs_folder","/mnt/game/proposal/");
            String sowDestinationFile = proposalFolder+"/"+proposalHeader.getId()+"/"+
                    ConfigHolder.getInstance().getStringValue("sow_downloaded_pdf_fomat","sow.pdf");
            quoteSOWPDFCreator.createSOWPDf(sowDestinationFile);
            LOG.debug("created SOW.pdf");
            sendResponse(message, new JsonObject().put("sowPdfFile", outputCreator.getOutputFile()));
            LOG.debug("Response:" + outputCreator.getOutputKey() + " |file: " + outputCreator.getOutputFile());

        }
        catch (Exception e)
        {
            String errorMessage = "Error in preparing file for :" + proposalHeader.getId() + ". " + e.getMessage();
            sendResponse(message, new JsonObject().put("error", errorMessage));
            LOG.error(errorMessage, e);
        }
    }

    private void createQuote(QuoteRequest quoteRequest, ProposalHeader proposalHeader, List<ProductLineItem> products,
                             List<ProductAddon> addons, Message message, ProposalVersion proposalVersion,List<SOWPdf> sowPdfs)
    {
        List<SpaceRoom> spaceRooms = new ArrayList<>();
        for (ProductLineItem productLineItem : products)
        {
            SpaceRoom spaceRoom = new SpaceRoom(productLineItem.getSpaceType(),productLineItem.getRoomCode());
            if (!spaceRooms.contains(spaceRoom))
            {
                spaceRooms.add(spaceRoom);
            }
        }
        for (ProductAddon productAddon : addons)
        {
            SpaceRoom spaceRoom = new SpaceRoom(productAddon.getSpaceType(),productAddon.getRoomCode());
            if (!spaceRooms.contains(spaceRoom))
            {
                spaceRooms.add(spaceRoom);
            }
        }

        LOG.info("SpaceRoom List size " +spaceRooms.size());
        for(SpaceRoom spaceRoom:spaceRooms)
        {
            LOG.info("space room data " +spaceRoom);
        }

        try
        {
            QuoteData quoteData = new QuoteData(proposalHeader, products, addons, quoteRequest.getDiscountAmount(),proposalVersion,quoteRequest.getBookingFormFlag(),quoteRequest.getDiscountPercentage(),quoteRequest.getWorkscontractFlag(),spaceRooms);
            boolean isValidSow = quoteRequest.isValidSowRows();
            ProposalOutputCreator outputCreator = ProposalOutputCreator.getCreator(quoteRequest.getOutputType(), quoteData,proposalHeader,isValidSow,sowPdfs ,proposalVersion,spaceRooms);
            outputCreator.create();

            LOG.debug("created Quotation.pdf");
            sendResponse(message, new JsonObject().put(outputCreator.getOutputKey(), outputCreator.getOutputFile()));
            LOG.debug("Response:" + outputCreator.getOutputKey() + " |file: " + outputCreator.getOutputFile());
        }
        catch (Exception e)
        {
            String errorMessage = "Error in preparing file for :" + proposalHeader.getId() + ". " + e.getMessage();
            sendResponse(message, new JsonObject().put("error", errorMessage));
            LOG.error(errorMessage, e);
        }
    }

    private void sendResponse(Message message, JsonObject response)
    {
        message.reply(LocalCache.getInstance().store(response));
    }

}
