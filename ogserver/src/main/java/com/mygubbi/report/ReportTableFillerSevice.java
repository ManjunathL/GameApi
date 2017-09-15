package com.mygubbi.report;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import com.mygubbi.game.proposal.model.ProposalVersion;
import com.mygubbi.pipeline.MessageDataHolder;
import com.mygubbi.pipeline.PipelineExecutor;
import com.mygubbi.pipeline.PipelineResponseHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by shilpa on 10/8/17.
 */
public class ReportTableFillerSevice extends AbstractVerticle
{
    private final static Logger LOG = LogManager.getLogger(ReportTableFillerSevice.class);
    public static final String RUN_FOR_SINGLE_PROPOSAL = "reporting.table.filler.forOneProposal";
    public static final String RUN_FOR_SINGLE_PROPOSAL_VERSION = "reporting.table.filler.forOneProposal.forOneVersion";
    public static final String RUN_FOR_UPDATED_PROPOSALS = "reporting.table.filler.forUpdatedProposal";
    public static final String FORCE_UPDATE_PROPOSALS = "reporting.table.filler.forForceUpdate";
    public static final String RUN_FROM_STARTDATE_TO_ENDDATE_PROPOSALS = "reporting.table.filler.ForRangeOfDates";
    public static final String RUN_FOR_LIST_OF_PROPOSALS = "reporting.table.filler.forListOfProposals";
    public static final String RUN_FOR_PROPOSALS_LIST = "reporting.table.filler";


    LocalDateTime servicecallTime;
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
       eb.localConsumer(RUN_FOR_SINGLE_PROPOSAL, (Message<Integer> message) -> {
            JsonObject obj = (JsonObject) LocalCache.getInstance().remove(message.body());
            this.getVersionObjForProposal(obj.getInteger("proposalId"), message);
            long end = System.currentTimeMillis();
        }).completionHandler(res -> {
            LOG.info("Proposal output service started." + res.succeeded());
        });
        eb.localConsumer(RUN_FOR_UPDATED_PROPOSALS, (Message<Integer> message) -> {
            LOG.info(RUN_FOR_UPDATED_PROPOSALS);
            this.getAllUpdatedProposals(message);
        }).completionHandler(res -> {
            LOG.info("Proposal output service started." + res.succeeded());
        });
        eb.localConsumer(RUN_FOR_SINGLE_PROPOSAL_VERSION, (Message<Integer> message) -> {
            JsonObject obj = (JsonObject) LocalCache.getInstance().remove(message.body());
            this.getVersionObjForProposal(obj.getInteger("proposalId"),obj.getString("version"), message);
            long end = System.currentTimeMillis();
        }).completionHandler(res -> {
            LOG.info("Proposal output service started." + res.succeeded());
        });
        eb.localConsumer(RUN_FROM_STARTDATE_TO_ENDDATE_PROPOSALS, (Message<Integer> message) -> {
            JsonObject obj = (JsonObject) LocalCache.getInstance().remove(message.body());
            this.getAllUpdatedProposalsOnDate(message,obj.getString("startDate"),obj.getString("endDate"));

        }).completionHandler(res -> {
            LOG.info("Proposal output service started." + res.succeeded());
        });
        eb.localConsumer(FORCE_UPDATE_PROPOSALS, (Message<Integer> message) -> {
            updateDataLoadStatusToNo(message);

        }).completionHandler(res -> {
            LOG.info("Proposal output service started." + res.succeeded());
        });
        eb.localConsumer(RUN_FOR_LIST_OF_PROPOSALS, (Message<Integer> message) -> {
            JsonObject obj = (JsonObject) LocalCache.getInstance().remove(message.body());
            JsonArray proposalIds =  obj.getJsonArray("proposals");
            for(int i=0;i< proposalIds.size();i++) {
                JsonObject obj1 = proposalIds.getJsonObject(i);
                this.getVersionObjForProposal(obj1.getInteger("proposalId"), obj1.getString("version"), message);
            }

        }).completionHandler(res -> {
            LOG.info("Proposal output service started." + res.succeeded());
        });
        eb.localConsumer(RUN_FOR_PROPOSALS_LIST,(Message<Integer> message) -> {
            JsonObject obj = (JsonObject) LocalCache.getInstance().remove(message.body());
            String proposalIds =  obj.getString("props");
            LOG.info("JSON OBJECT :: "+proposalIds.toString());
            this.getVersionObjForProposalList(proposalIds.toString(), message);

        }).completionHandler(res -> {
            LOG.info("Proposal output service started." + res.succeeded());
        });

    }
    private void updateDataLoadStatusToNo(Message message)
    {
        Integer id = LocalCache.getInstance().store(new QueryData("version_master.dataLOadStatus.update", new JsonObject()));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag) {
                        message.reply(LocalCache.getInstance().store(new JsonObject().put("status", "failure")));
                        LOG.info("Updating version_master, isDataLoadedToReport column to No. " + resultData.errorMessage, resultData.error);
                    } else {
                        this.getAllUpdatedProposals(message);
                    }
                });
    }

    private void getAllUpdatedProposals(Message message)
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        servicecallTime = LocalDateTime.now();

        LOG.info("Running for updated proposals");
        QueryData requestData = new QueryData("proposal_version.updatedProposals.select", new JsonObject());
        MessageDataHolder dataHolder = new MessageDataHolder(DatabaseService.DB_QUERY, requestData);
        new PipelineExecutor().execute(dataHolder, new ProposalVersionsRetriever(message));
    }
    private void getAllUpdatedProposalsOnDate(Message message,String startDate,String endDate)
    {
        JsonObject params = new JsonObject();
        params.put("startDate",startDate);
        params.put("endDate",endDate);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        servicecallTime = LocalDateTime.now();

        LOG.info("Running for updated proposals");
        QueryData requestData = new QueryData("proposal_version.updatedProposals.select.onDate", params);
        MessageDataHolder dataHolder = new MessageDataHolder(DatabaseService.DB_QUERY, requestData);
        new PipelineExecutor().execute(dataHolder, new ProposalVersionsRetriever(message));
    }
    private void getVersionObjForProposal(Integer proposalId, Message message)
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        servicecallTime = LocalDateTime.now();

        long start = System.currentTimeMillis();

        QueryData requestData = new QueryData("proposal.versions.list", new JsonObject().put("proposalId", proposalId));
        MessageDataHolder dataHolder = new MessageDataHolder(DatabaseService.DB_QUERY, requestData);
        new PipelineExecutor().execute(dataHolder, new ProposalVersionsRetriever(message, proposalId));
        LOG.info("Time taken = "+(System.currentTimeMillis()-start));
    }
    private void getVersionObjForProposalList(String proposals, Message message)
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        servicecallTime = LocalDateTime.now();

        long start = System.currentTimeMillis();
        LOG.info("proposals = "+proposals);

        QueryData requestData = new QueryData("proposal.versions.list.test", new JsonObject().put("proposalId", proposals.toString()));
        MessageDataHolder dataHolder = new MessageDataHolder(DatabaseService.DB_QUERY, requestData);
        new PipelineExecutor().execute(dataHolder, new ProposalVersionsRetriever(message));
        LOG.info("Time taken = "+(System.currentTimeMillis()-start));
    }

    private void getVersionObjForProposal(Integer proposalId,String version, Message message)
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        servicecallTime = LocalDateTime.now();

        long start = System.currentTimeMillis();

        QueryData requestData = new QueryData("version.getversiondetails", new JsonObject().put("proposalId", proposalId).put("version",version));
        MessageDataHolder dataHolder = new MessageDataHolder(DatabaseService.DB_QUERY, requestData);
        new PipelineExecutor().execute(dataHolder, new ProposalVersionsRetriever(message, proposalId));
        LOG.info("Time taken = "+(System.currentTimeMillis()-start));
    }

    private class ProposalVersionsRetriever implements PipelineResponseHandler
    {
        private Message message;
        private Integer proposalId;

        public ProposalVersionsRetriever(Message message)
        {
            LOG.info("Message :: "+message);
            this.message = message;

        }

        public ProposalVersionsRetriever(Message message, Integer proposalId)
        {
            this.message = message;
            this.proposalId = proposalId;
        }

        @Override
        public void handleResponse(List<MessageDataHolder> messageDataHolders)
        {
            QueryData resultData = (QueryData) messageDataHolders.get(0).getResponseData();
            if (resultData.errorFlag || resultData.rows == null || resultData.rows.isEmpty())
            {
                message.reply(LocalCache.getInstance().store(new JsonObject().put("error", "Proposal " + proposalId + " doesn't have any versions")));
                LOG.error("Proposal " + proposalId + " doesn't have any versions");
            }
            else
            {
                List<MessageDataHolder> steps = resultData.rows.stream()
                        .map(row -> new MessageDataHolder(DwReportingService.RECORD_VERSION_PRICE, new ProposalVersion(row)))
                        .collect(Collectors.toList());
                new PipelineExecutor().execute(steps, new ReportingServiceResponseHandler(message));
            }
        }

    }

    private class ReportingServiceResponseHandler implements PipelineResponseHandler
    {
        private Message message;

        public ReportingServiceResponseHandler(Message message)
        {
            this.message = message;
        }

        @Override
        public void handleResponse(List<MessageDataHolder> messageDataHolders)
        {
            StringBuilder comments = new StringBuilder();
            for(int i=0;i<messageDataHolders.size();i++){

                JsonObject response = (JsonObject) messageDataHolders.get(i).getResponseData();
                if(response.containsKey("status")){
                    if(response.getString("status").equalsIgnoreCase("FAILURE")){
                        int proposalId = response.getInteger("proposalId");
                        String version = response.getString("version");
                        comments.append(proposalId+"-"+version+", ");
                    }
                }
            }
            if(comments.toString().length() == 0){
                comments.append("Successfully ran for all proposals");
            }
            LOG.debug("Reporting service returned :: "+messageDataHolders.get(0).getResponseData());
            //update the table
            updateReportSchedularTable("success",message,comments.toString());
            message.reply(LocalCache.getInstance().store(new JsonObject().put("status","success")));

            
        }
    }

    private  void  updateReportSchedularTable(String strMsg,Message message,String comments){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now));
        JsonObject obj = new JsonObject();
        obj.put("reportsRunDate",dtf.format(this.servicecallTime));
        obj.put("status",strMsg);
        obj.put("lastRun",dtf.format(now));
        obj.put("comments",comments);
        Integer id = LocalCache.getInstance().store(new QueryData("report.schedular.insert", obj));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag || resultData.updateResult.getUpdated() == 0) {
                        message.reply(LocalCache.getInstance().store(new JsonObject().put("status", "failure")));
                        LOG.info("Error in inserting into report_schedular. " + resultData.errorMessage, resultData.error);
                    } else {
                        message.reply(LocalCache.getInstance().store(new JsonObject().put("status", "failure")));
                        LOG.info("Successfully inserted into report_schedular. ");
                    }
                });
    }
}
