package com.mygubbi.game.proposal.output;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import com.mygubbi.game.proposal.ProductAddon;
import com.mygubbi.game.proposal.ProductLineItem;
import com.mygubbi.game.proposal.model.ProposalHeader;
import com.mygubbi.game.proposal.quote.QuoteData;
import com.mygubbi.game.proposal.quote.QuoteRequest;
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
        Integer id = LocalCache.getInstance().store(new QueryData("proposal.header", new JsonObject().put("id", quoteRequest.getProposalId())));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag || resultData.rows == null || resultData.rows.isEmpty())
                    {
                        message.reply(LocalCache.getInstance().store(new JsonObject().put("error", "Proposal not found for id:" + quoteRequest.getProposalId())));
                        LOG.error("Proposal not found for id:" + quoteRequest.getProposalId());
                    }
                    else
                    {
                        ProposalHeader proposalHeader = new ProposalHeader(resultData.rows.get(0));
                        this.getProposalProducts(proposalHeader, quoteRequest, message);
                    }
                });

    }

    private void getProposalProducts(ProposalHeader proposalHeader, QuoteRequest quoteRequest, Message message)
    {
        QueryData queryData = null;
        JsonObject paramsJson = new JsonObject().put("proposalId", proposalHeader.getId());
        if (quoteRequest.hasProductIds())
        {
            queryData = new QueryData("proposal.product.selected.detail", paramsJson.put("productIds", quoteRequest.getProductIdsAsText()));
        }
        else
        {
            queryData = new QueryData("proposal.product.all.detail", paramsJson);
        }
        Integer id = LocalCache.getInstance().store(queryData);
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag || resultData.rows == null || resultData.rows.isEmpty())
                    {
                        message.reply(LocalCache.getInstance().store(new JsonObject().put("error", "Proposal products not found for id:" + proposalHeader.getId())));
                        LOG.error("Proposal products not found for id:" + proposalHeader.getId());
                        return;
                    }
                    else
                    {
                        List<ProductLineItem> products = new ArrayList<ProductLineItem>();
                        for (JsonObject json : resultData.rows)
                        {
                            products.add(new ProductLineItem(json));
                        }
                        this.getProposalAddons(quoteRequest, proposalHeader, products, message);
                    }
                });
    }

    private void getProposalAddons(QuoteRequest quoteRequest, ProposalHeader proposalHeader, List<ProductLineItem> products, Message message)
    {
        Integer id = LocalCache.getInstance().store(new QueryData("proposal.addon.list", new JsonObject().put("proposalId", proposalHeader.getId())));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag)
                    {
                        message.reply(LocalCache.getInstance().store(new JsonObject().put("error", "Error in fetching Proposal level addons for :" + proposalHeader.getId())));
                        LOG.error("Proposal level addons not found for id:" + proposalHeader.getId());
                        return;
                    }

                    List<ProductAddon> addons = null;
                    if (resultData.rows != null && !resultData.rows.isEmpty())
                    {
                        addons = new ArrayList<ProductAddon>();
                        for (JsonObject json : resultData.rows)
                        {
                            addons.add(new ProductAddon(json));
                        }
                    }
                    this.createQuote(quoteRequest, proposalHeader, products, addons, message);
                });
    }

    private void createQuote(QuoteRequest quoteRequest, ProposalHeader proposalHeader, List<ProductLineItem> products,
                             List<ProductAddon> addons, Message  message)
    {
        try
        {
            QuoteData quoteData = new QuoteData(proposalHeader, products, addons, quoteRequest.getDiscountAmount());
            ProposalOutputCreator outputCreator = ProposalOutputCreator.getCreator(quoteRequest.getOutputType(), quoteData);
            outputCreator.create();
            sendResponse(message, new JsonObject().put(outputCreator.getOutputKey(), outputCreator.getOutputFile()));
        }
        catch (Exception e)
        {
            String errorMessage = "Error in preparing excel file for :" + proposalHeader.getId() + ". " + e.getMessage();
            sendResponse(message, new JsonObject().put("error", errorMessage));
            LOG.error(errorMessage, e);
        }
    }

    private void sendResponse(Message message, JsonObject response)
    {
        message.reply(LocalCache.getInstance().store(response));
    }

}
