package com.mygubbi.game.proposal.sow;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import com.mygubbi.game.proposal.model.ProposalHeader;
import com.mygubbi.game.proposal.model.ProposalSOW;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 13-07-2017.
 */
public class SOWCreatorService extends AbstractVerticle {

    public static final String CREATE_SOW_OUTPUT = "create.proposal.sow.output";

    public DriveServiceProvider driveServiceProvider = new DriveServiceProvider();

    private final static Logger LOG = LogManager.getLogger(SOWCreatorService.class);

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
        eb.localConsumer(CREATE_SOW_OUTPUT, (Message<Integer> message) -> {
            JsonObject quoteRequest = (JsonObject) LocalCache.getInstance().remove(message.body());
            this.getDistinctSpaceAndRooms(quoteRequest, message);
        }).completionHandler(res -> {
            LOG.info("Proposal output service started." + res.succeeded());
        });
    }


    private void getDistinctSpaceAndRooms(JsonObject quoteRequest, Message message)
    {
        LOG.debug("distinct space ");

        List<QueryData> queryDatas =new ArrayList<>();

        double version = quoteRequest.getDouble("version");
        String db_query = null;
        JsonObject jsonObject = new JsonObject().put("proposalId", quoteRequest.getInteger("proposalId"))
                .put("userId",quoteRequest.getString("userId"));

        if (version==1.0 || version==2.0) {
            String versiontobeconsidered = String.valueOf(version);
            jsonObject.put("versiontobeconsidered" , versiontobeconsidered);
            jsonObject.put("sowversion" , version == 1.0 ? "1.0" : "2.0");
            db_query = "proposal.product.specificversion";
        }
        else if (version<1.0)
        {
            db_query = "proposal.product.sow.till1";
            jsonObject.put("sowversion" , "1.0");
        }
        else
        {
            db_query = "proposal.product.sow.till2";
            jsonObject.put("sowversion" , "2.0");
        }

        queryDatas.add(new QueryData(db_query,jsonObject));
        queryDatas.add(new QueryData("proposal.sow.select.space.rooms",jsonObject));


        Integer id = LocalCache.getInstance().store(queryDatas);
        VertxInstance.get().eventBus().send(DatabaseService.MULTI_DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    List<QueryData> resultData = (List<QueryData>) LocalCache.getInstance().remove(selectResult.result().body());

                    if (resultData.get(0).errorFlag || resultData.get(1).errorFlag)
                    {
                        sendError(message);
                    return;
                    }
                    else
                    {
                        List<JsonObject> proposal_spaces = resultData.get(0).rows;
                        List<JsonObject> proposal_sows = resultData.get(1).rows;
                        syncSowWIthProposalData(jsonObject,message,proposal_spaces,proposal_sows);
                    }

                });
    }

    private void syncSowWIthProposalData(JsonObject quoteRequest, Message message, List<JsonObject> proposalSpaces, List<JsonObject> proposalSows)
    {
        LOG.debug("SOW with proposal data");
        LOG.debug("Proposal Spaces :" + proposalSpaces.size());
        LOG.debug("SOW Spaces :" + proposalSows.size());
        List<QueryData> queryDataList =new ArrayList<>();


        addSpacesToSow(quoteRequest, proposalSpaces, proposalSows, queryDataList);
        deleteSpacesFromSow(quoteRequest, proposalSpaces, proposalSows, queryDataList);
        updateProposalSow(quoteRequest,message,queryDataList);
    }

    private void addSpacesToSow(JsonObject quoteRequest, List<JsonObject> proposalSpaces, List<JsonObject> proposalSows, List<QueryData> queryDataList) {
        LOG.debug("aDD SPACES");
        for (JsonObject proposalSpace : proposalSpaces)
        {
            LOG.debug("Json object in add spaces :" + proposalSpace.toString());
            String space = proposalSpace.getString("spaceType");
            String room = proposalSpace.getString("roomcode");

            if (spaceNotExistinProposalSow(space,room,proposalSows))
            {
                LOG.debug("Creating SOW for Space and Room :" + space + ":"+ room);

                proposalSpace.put("proposalId",quoteRequest.getInteger("proposalId"));
                proposalSpace.put("version",quoteRequest.getString("sowversion"));
                QueryData queryData = new QueryData("proposal.sow.create",proposalSpace);
                queryDataList.add(queryData);
            }

        }
    }

    private void deleteSpacesFromSow(JsonObject quoteRequest, List<JsonObject> proposalSpaces, List<JsonObject> sowSpaces, List<QueryData> queryDataList) {
        LOG.debug("delete spaces");
        for (JsonObject sowSpace : sowSpaces)
        {
            LOG.debug("Json object in add spaces :" + sowSpaces.toString());

            String space = sowSpace.getString("spaceType");
            String room = sowSpace.getString("roomcode");


            if (spaceNotExistinProposalSow(space,room,proposalSpaces))
            {
                LOG.debug("Deleting SOW for Space and Room :" + space + ":"+ room);

                sowSpace.put("proposalId",quoteRequest.getInteger("proposalId"));
                sowSpace.put("version",quoteRequest.getString("sowversion"));
                QueryData queryData = new QueryData("proposal.sow.delete",sowSpace);
                queryDataList.add(queryData);
            }

        }
    }

    private boolean spaceNotExistinProposalSow(String space, String room, List<JsonObject> proposalSows) {
        for (JsonObject proposalSpace : proposalSows)
        {
            String sowSpace = proposalSpace.getString("spaceType");
            String sowroom = proposalSpace.getString("roomcode");

          if (space.equals(sowSpace) && room.equals(sowroom))
              return false;
        }
        return true;
    }

    private void updateProposalSow(JsonObject sowrequest, Message message, List<QueryData> queryDataList)
    {
        LOG.debug("update proposal sow");
        Integer id = LocalCache.getInstance().store(queryDataList);
        VertxInstance.get().eventBus().send(DatabaseService.MULTI_DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    List<QueryData> resultData = (List<QueryData>) LocalCache.getInstance().remove(selectResult.result().body());
                    for (int i = 0; i < resultData.size(); i++) {
                        if (resultData.get(i).errorFlag) {
                            LOG.debug("Error :" + resultData.get(i).errorMessage);
                            sendError(message);
                            return;
                        }
                    }
                createSowinDrive(sowrequest,message);
                });
    }

    private void sendError(Message message) {
        message.reply(new JsonObject().put("error","Error in creating sow"));
    }

    private void createSowinDrive(JsonObject sowrequest, Message message) {
        LOG.debug("Create sow in drive");

        List<QueryData> queryDatas = new ArrayList<>();

        LOG.debug("Create SOw in drive :" + sowrequest.encodePrettily());

        queryDatas.add(new QueryData("proposal.header", new JsonObject().put("id", sowrequest.getInteger("proposalId"))));
        queryDatas.add(new QueryData("proposal.sow.select.proposalversion", sowrequest));


        Integer id = LocalCache.getInstance().store(queryDatas);
        VertxInstance.get().eventBus().send(DatabaseService.MULTI_DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    List<QueryData> resultData = (List<QueryData>) LocalCache.getInstance().remove(selectResult.result().body());

                    if (resultData.get(0).errorFlag || resultData.get(1).errorFlag)
                    {
                        LOG.debug("error inside");
                        sendError(message);
                        return;
                    }
                    else
                    {
                        LOG.debug("GOt SOW and header ");
                        ProposalHeader proposalHeader = new ProposalHeader(resultData.get(0).rows.get(0));
                        List<JsonObject> sow_jsons = resultData.get(1).rows;
                        List<ProposalSOW> proposalSOWs = new ArrayList<ProposalSOW>();
                        for (JsonObject proposalSOW : sow_jsons)
                        {
                            proposalSOWs.add(new ProposalSOW(proposalSOW));
                        }
                        createSowAndUploadToDrive(sowrequest,message,proposalHeader, proposalSOWs);
                    }

                });

    }

    private void createSowAndUploadToDrive(JsonObject sowrequest,Message message, ProposalHeader proposalHeader, List<ProposalSOW> proposalSOWs) {
        LOG.debug("Upload to drive");
        String outputFile = new SOWTemplateCreator(proposalHeader,proposalSOWs).create();

        DriveFile driveFile = this.driveServiceProvider.uploadFileForUser(outputFile,sowrequest.getString("userId"), proposalHeader.getQuoteNumNew() + "_SOW");

        sendResponse(message, new JsonObject().put("driveWebViewLink",driveFile.getWebViewLink()).put("id",driveFile.getId()).put("outputFile",outputFile).put("version",sowrequest.getString("sowversion")));

    }

    private void sendResponse(Message message, JsonObject response)
    {
        LOG.debug("Response :" + response.encodePrettily());
        message.reply(LocalCache.getInstance().store(response));
    }


}
