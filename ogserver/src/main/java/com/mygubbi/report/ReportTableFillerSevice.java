package com.mygubbi.report;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import com.mygubbi.game.proposal.model.ProposalVersion;
import com.mygubbi.pipeline.MessageDataHolder;
import com.mygubbi.pipeline.MultiMessageRequestExecutor;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by shilpa on 10/8/17.
 */
public class ReportTableFillerSevice extends AbstractVerticle
{
    private final static Logger LOG = LogManager.getLogger(ReportTableFillerSevice.class);
    public static final String RUN_FOR_ALL_PROPOSALS = "reporting.table.filler.forAll";
    public static final String RUN_FOR_SINGLE_PROPOSAL = "reporting.table.filler.forOneProposal";
    public static final String RUN_FOR_UPDATED_PROPOSALS = "reporting.table.filler.forUpdatedProposal";

    @Override
    public void start(Future<Void> startFuture) throws Exception
    {
        this.runReportingTableFillingLogic();
        startFuture.complete();
    }

    @Override
    public void stop() throws Exception
    {
        super.stop();
    }

    private void runReportingTableFillingLogic()
    {
        EventBus eb = VertxInstance.get().eventBus();
        eb.localConsumer(RUN_FOR_ALL_PROPOSALS, (Message<Integer> message) -> {
            this.getAllProposals(message);
        }).completionHandler(res -> {
            LOG.info("Proposal output service started." + res.succeeded());
        });
        eb.localConsumer(RUN_FOR_SINGLE_PROPOSAL, (Message<Integer> message) -> {
            JsonObject obj = (JsonObject) LocalCache.getInstance().remove(message.body());
            LOG.info("Json Obj = " + obj);
            this.getVersionObjForProposal(obj.getInteger("proposalId"), message);
        }).completionHandler(res -> {
            LOG.info("Proposal output service started." + res.succeeded());
        });
        eb.localConsumer(RUN_FOR_UPDATED_PROPOSALS, (Message<Integer> message) -> {
            this.getAllUpdatedProposals(message);
        }).completionHandler(res -> {
            LOG.info("Proposal output service started." + res.succeeded());
        });
    }

    private void getAllUpdatedProposalsOld(Message message)
    {

//        List<Integer> proposalIds = new ArrayList<>();
        Integer id = LocalCache.getInstance().store(new QueryData("proposal.updatedProposals.select", new JsonObject()));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag || resultData.rows == null || resultData.rows.isEmpty())
                    {
                        message.reply(LocalCache.getInstance().store(new JsonObject().put("error", "Proposal table is empty")));
                        LOG.error("Proposal table is empty");
                    }
                    else
                    {

//                        ProposalHeader proposalHeader = new ProposalHeader(resultData.rows.get(0));
                        resultData.rows.forEach(item -> {
//                            proposalIds.add(item.getInteger("id"));
                            getVersionObjForProposal(item.getInteger("id"), message);
                        });

                    }
                });

    }

    private void getAllUpdatedProposals(Message message)
    {
        Integer id = LocalCache.getInstance().store(new QueryData("proposal.updatedProposals.select", new JsonObject()));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag || resultData.rows == null || resultData.rows.isEmpty())
                    {
                        message.reply(LocalCache.getInstance().store(new JsonObject().put("error", "Proposal table is empty")));
                        LOG.error("Proposal table is empty");
                    }
                    else
                    {
                        resultData.rows.forEach(item -> {
                            getVersionObjForProposal(item.getInteger("id"), message);
                        });

                    }
                });

    }

    private void getAllProposals(Message message)
    {

//        List<Integer> proposalIds = new ArrayList<>();
        Integer id = LocalCache.getInstance().store(new QueryData("proposal.all.select", new JsonObject()));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag || resultData.rows == null || resultData.rows.isEmpty())
                    {
                        message.reply(LocalCache.getInstance().store(new JsonObject().put("error", "Proposal table is empty")));
                        LOG.error("Proposal table is empty");
                    }
                    else
                    {
                        resultData.rows.forEach(item -> {
                            getVersionObjForProposal(item.getInteger("id"), message);
                        });

                    }
                });

    }

    private void getVersionObjForProposalOld(Integer proposalId, Message message)
    {
        Integer id = LocalCache.getInstance().store(new QueryData("proposal.versions.list", new JsonObject().put("proposalId", proposalId)));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag || resultData.rows == null || resultData.rows.isEmpty())
                    {
                        message.reply(LocalCache.getInstance().store(new JsonObject().put("error", "Proposal " + proposalId + " doesn't have any versions")));
                        LOG.error("Proposal " + proposalId + " doesn't have any versions");
                    }
                    else
                    {
                        resultData.rows.forEach(item -> {
                            Integer id1 = LocalCache.getInstance().store(new ProposalVersion(item));
                            LOG.debug("Item :" + item.encodePrettily());
                            VertxInstance.get().eventBus().send(DwReportingService.RECORD_VERSION_PRICE, id1,
                                    (AsyncResult<Message<Integer>> result) -> {
                                        JsonObject response = (JsonObject) LocalCache.getInstance().remove(result.result().body());
                                        LOG.info("Quote Res :: " + response);
                                        message.reply(response);
                                    });

                        });

                    }
                });
    }

    private void getVersionObjForProposal(Integer proposalId, Message message)
    {
        Integer id = LocalCache.getInstance().store(new QueryData("proposal.versions.list", new JsonObject().put("proposalId", proposalId)));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag || resultData.rows == null || resultData.rows.isEmpty())
                    {
                        message.reply(LocalCache.getInstance().store(new JsonObject().put("error", "Proposal " + proposalId + " doesn't have any versions")));
                        LOG.error("Proposal " + proposalId + " doesn't have any versions");
                    }
                    else
                    {
                        List<MessageDataHolder> messageDataHolders = resultData.rows.stream()
                                .map(row -> new MessageDataHolder(DwReportingService.RECORD_VERSION_PRICE, new ProposalVersion(row)))
                                .collect(Collectors.toList());
                        List<MessageDataHolder> responseDataHolders = new MultiMessageRequestExecutor().execute(messageDataHolders);
                        message.reply("Done");
                    }
                });
    }
}
