package com.mygubbi.game.proposal.output;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
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
                        this.createQuote(quoteRequest, proposalHeader, products, addons, message, proposalVersion);
                    }
                });

    }

    private void createQuote(QuoteRequest quoteRequest, ProposalHeader proposalHeader, List<ProductLineItem> products,
                             List<ProductAddon> addons, Message message, ProposalVersion proposalVersion)
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
            ProposalOutputCreator outputCreator = ProposalOutputCreator.getCreator(quoteRequest.getOutputType(), quoteData,proposalHeader,isValidSow, new ArrayList<SOWPdf>(),proposalVersion,spaceRooms);
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
