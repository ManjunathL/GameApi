package com.mygubbi.game.proposal.price;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import com.mygubbi.game.proposal.model.ProposalVersion;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Created by Sunil on 08-01-2016.
 */

public class ProposalPricingUpdateService extends AbstractVerticle
{
    private final static Logger LOG = LogManager.getLogger(ProposalPricingUpdateService.class);
    public static final String RETRIEVE_DRAFT_PROPOSALS = "update.proposal.price";

    @Override
    public void start(Future<Void> startFuture) throws Exception
    {
        this.setupPriceUpdater();
        startFuture.complete();
    }

    @Override
    public void stop() throws Exception
    {
        super.stop();
    }

    private void setupPriceUpdater()
    {
        EventBus eb = VertxInstance.get().eventBus();
        eb.localConsumer(RETRIEVE_DRAFT_PROPOSALS, (Message<Integer> message) ->
        {
            JsonObject updatePriceJsonData = (JsonObject) LocalCache.getInstance().remove(message.body());
            LOG.info(updatePriceJsonData.encodePrettily());
            this.updatePriceForProposals(message, updatePriceJsonData);
        }).completionHandler(res -> {
            LOG.info("Update Proposal price service started." + res.succeeded());
        });
    }

    private void updatePriceForProposals(Message<Integer> message, JsonObject updatePriceJsonData)
    {
        int id = LocalCache.getInstance().store(new QueryData("proposal.version.select.updateprice", new JsonObject()));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> dataResult) ->
                {
                    QueryData selectData = (QueryData) LocalCache.getInstance().remove(dataResult.result().body());
                    if (selectData == null || selectData.rows == null || selectData.rows.isEmpty())
                    {
                        LOG.info("No proposals to be updated.");
                        updatePriceJsonData.put("status", false);
                        Integer rId = LocalCache.getInstance().store(updatePriceJsonData);
                        message.reply(LocalCache.getInstance().remove(rId));
                    }
                    else
                    {
                        LOG.debug("to go ");
                        for (JsonObject record : selectData.rows)
                        {
                            ProposalVersion proposalVersion = new ProposalVersion(record);
                            Integer pvId = LocalCache.getInstance().store(proposalVersion);
                            VertxInstance.get().eventBus().send(ProposalVersionPriceUpdateService.UPDATE_VERSION_PRICE, pvId,
                                    (AsyncResult<Message<Integer>> selectResult) ->
                                    {
                                        ProposalVersion result = (ProposalVersion) LocalCache.getInstance().remove(selectResult.result().body());
                                    });
                        }
                        updatePriceJsonData.put("status", true);
                        message.reply(LocalCache.getInstance().store(updatePriceJsonData));
                    }

                });
    }

}
