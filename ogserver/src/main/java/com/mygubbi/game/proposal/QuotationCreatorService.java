package com.mygubbi.game.proposal;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.config.ConfigHolder;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import com.mygubbi.game.proposal.model.*;
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

public class QuotationCreatorService extends AbstractVerticle
{
    private final static Logger LOG = LogManager.getLogger(QuotationCreatorService.class);

    public static final String CREATE_QUOTE = "create.quote";
    private String quoteTemplate;

    @Override
    public void start(Future<Void> startFuture) throws Exception
    {
        this.setupQuoteCreator();
        this.quoteTemplate = ConfigHolder.getInstance().getStringValue("quote_template", "/tmp/quote-template.xlsx");
        startFuture.complete();
    }

    @Override
    public void stop() throws Exception
    {
        super.stop();
    }

    private void setupQuoteCreator()
    {
        EventBus eb = VertxInstance.get().eventBus();
        eb.localConsumer(CREATE_QUOTE, (Message<Integer> message) -> {
            Integer proposalId = (Integer) LocalCache.getInstance().remove(message.body());
            this.getProposalHeader(proposalId, message);
        }).completionHandler(res -> {
            LOG.info("QuotationCreatorService service started." + res.succeeded());
        });
    }

    private void getProposalHeader(Integer proposalId, Message message)
    {
        Integer id = LocalCache.getInstance().store(new QueryData("proposal.header", new JsonObject().put("id", proposalId)));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag || resultData.rows == null || resultData.rows.isEmpty())
                    {
                        message.reply(LocalCache.getInstance().store(new JsonObject().put("error", "Proposal not found for id:" + proposalId)));
                        LOG.error("Proposal not found for id:" + proposalId);
                    }
                    else
                    {
                        ProposalHeader proposalHeader = new ProposalHeader(resultData.rows.get(0));
                        this.getProposalProducts(proposalHeader, message);
                    }
                });

    }

    private void getProposalProducts(ProposalHeader proposalHeader, Message message)
    {
        Integer id = LocalCache.getInstance().store(new QueryData("proposal.product.list.detail", new JsonObject().put("proposalId", proposalHeader.getId())));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag || resultData.rows == null || resultData.rows.isEmpty())
                    {
                        message.reply(LocalCache.getInstance().store(new JsonObject().put("error", "Proposal products not found for id:" + proposalHeader.getId())));
                        LOG.error("Proposal products not found for id:" + proposalHeader.getId());
                    }
                    else
                    {
                        List<ProductLineItem> products = new ArrayList<ProductLineItem>();
                        for (JsonObject json : resultData.rows)
                        {
                            products.add(new ProductLineItem(json));
                        }
                        this.createQuote(proposalHeader, products, message);
                    }
                });
    }

    private void createQuote(ProposalHeader proposalHeader, List<ProductLineItem> products, Message message)
    {
        String quoteXls = proposalHeader.folderPath() + "/quotation.xlsx";
        try
        {
            VertxInstance.get().fileSystem().deleteBlocking(quoteXls);
        }
        catch (Exception e)
        {
            //Nothing to do
        }
        try
        {
            VertxInstance.get().fileSystem().copyBlocking(this.quoteTemplate, quoteXls);
            QuoteData quoteData = new QuoteData(proposalHeader, products);
            new ExcelQuoteCreator(quoteXls, quoteData).prepareQuote();
            sendResponse(message, new JsonObject().put("quoteFile", quoteXls));
        }
        catch (Exception e)
        {
            String errorMessage = "Error in preparing quote file for :" + proposalHeader.getId() + ". " + e.getMessage();
            sendResponse(message, new JsonObject().put("error", errorMessage));
            LOG.error(errorMessage, e);
        }
    }

    private void sendResponse(Message message, JsonObject response)
    {
        message.reply(LocalCache.getInstance().store(response));
    }

}
