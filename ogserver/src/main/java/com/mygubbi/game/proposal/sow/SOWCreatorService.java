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

import java.util.*;

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
            LOG.info("INSIDE setupSowCreator");
            JsonObject quoteRequest = (JsonObject) LocalCache.getInstance().remove(message.body());
            this.getDistinctSpaceAndRooms(quoteRequest, message);
        }).completionHandler(res -> {
            LOG.info("Proposal output service started." + res.succeeded());
        });
    }


    private void getDistinctSpaceAndRooms(JsonObject quoteRequest, Message message)
    {
        LOG.debug("distinct space ");
        LOG.info("INSIDE getDistinctSpaceAndRooms");
        List<QueryData> queryDatas =new ArrayList<>();

        double version = quoteRequest.getDouble("version");
        String db_query_product = null;
        String db_query_addon = null;
        JsonObject jsonObject = new JsonObject().put("proposalId", quoteRequest.getInteger("proposalId"))
                .put("userId",quoteRequest.getString("userId")).put("readOnlyFlag",quoteRequest.getString("readOnlyFlag"));

        String verFromProposal = String.valueOf(version);
        String sowversion = null;
        if(verFromProposal.contains("0.") || verFromProposal.equals("1.0")){
            sowversion = "1.0";
        }else if(verFromProposal.contains("1.") || verFromProposal.contains("2.")){
            sowversion = "2.0";
        }else{
            LOG.info("INVALID VERSION and VERSION IS::"+verFromProposal);
        }

        if (version==1.0 || version==2.0) {
            LOG.info("version==1.0 || version==2.0 ");
            String versiontobeconsidered = String.valueOf(version);
            jsonObject.put("versiontobeconsidered" , versiontobeconsidered);
            jsonObject.put("sowversion" , sowversion);
            db_query_product = "proposal.product.specificversion";
            db_query_addon = "proposal.addon.specificversion";
        }
        else if (version<1.0)
        {
            LOG.info("version<1.0 ");
            db_query_product = "proposal.product.sow.till1";
            db_query_addon = "proposal.addon.sow.till1";
            jsonObject.put("sowversion" , sowversion);

        }
        else
        {
            LOG.info("ELSE");
            db_query_product = "proposal.product.sow.till2";
            db_query_addon = "proposal.addon.sow.till2";
            jsonObject.put("sowversion" , "2.0");
        }
        jsonObject.put("version" , sowversion);

        queryDatas.add(new QueryData(db_query_product,jsonObject));
        queryDatas.add(new QueryData(db_query_addon,jsonObject));
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
                        List<JsonObject> proposal__product_spaces = resultData.get(0).rows;
                        List<JsonObject> proposal__addon_spaces = resultData.get(1).rows;
                        List<JsonObject> proposal_sows = resultData.get(2).rows;
                        LOG.debug("Products : " + proposal__product_spaces.toString());
                        LOG.debug("Addons : " + proposal__addon_spaces.toString());
                        List<JsonObject> distinctSpaceRooms = getDistinctSpaceRooms(proposal__product_spaces, proposal__addon_spaces);
                        syncSowWIthProposalData(jsonObject,message,distinctSpaceRooms,proposal_sows);
                    }

                });
    }

    private List<JsonObject> getDistinctSpaceRooms(List<JsonObject> proposal__product_spaces, List<JsonObject> proposal__addon_spaces)
    {
        List<JsonObject> allSpaces = new ArrayList<>();
        /*allSpaces.addAll(proposal__product_spaces);*/

        LOG.debug("All spaces size before : " + allSpaces.size());

        Set<SpaceRoom> spaceRooms = new HashSet<>();
        for (JsonObject spaceRoomJson : proposal__product_spaces)
        {
            SpaceRoom spaceRoom = new SpaceRoom(spaceRoomJson.getString("spaceType"),spaceRoomJson.getString("roomcode").toLowerCase());
            if (!spaceRooms.contains(spaceRoom))
            {
                spaceRooms.add(spaceRoom);
                allSpaces.add(spaceRoomJson);
            }
        }
        for (JsonObject spaceRoomJson : proposal__addon_spaces)
        {
            SpaceRoom spaceRoom = new SpaceRoom(spaceRoomJson.getString("spaceType"),spaceRoomJson.getString("roomcode").toLowerCase());
            if (!spaceRooms.contains(spaceRoom))
            {
                spaceRooms.add(spaceRoom);
                allSpaces.add(spaceRoomJson);
            }
        }
        return allSpaces;
    }

    private void syncSowWIthProposalData(JsonObject quoteRequest, Message message, List<JsonObject> proposalSpaces, List<JsonObject> proposalSows)
    {
        List<QueryData> queryDataList =new ArrayList<>();


        addSpacesToSow(quoteRequest, proposalSpaces, proposalSows, queryDataList);
        deleteSpacesFromSow(quoteRequest, proposalSpaces, proposalSows, queryDataList);
        updateProposalSow(quoteRequest,message,queryDataList);
    }

    private void addSpacesToSow(JsonObject quoteRequest, List<JsonObject> proposalSpaces, List<JsonObject> proposalSows, List<QueryData> queryDataList) {
        for (JsonObject proposalSpace : proposalSpaces)
        {
            String space = proposalSpace.getString("spaceType");
            String room = proposalSpace.getString("roomcode").toLowerCase();

            if (spaceNotExistinProposalSow(space,room,proposalSows))
            {
                LOG.info("Spaces not present in proposal sow");
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
            LOG.debug("Json object in delete spaces :" + sowSpaces.toString());

            String space = sowSpace.getString("spaceType");
            String room = sowSpace.getString("roomcode").toLowerCase();


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
        LOG.debug("Inside spaceNotExistinProposalSow");
        LOG.debug("Space :: "+space+", Room::"+room);
        LOG.debug("Space size : " + proposalSows.size());
        for (JsonObject proposalSpace : proposalSows)
        {
            LOG.debug("Proposal space object :" + proposalSpace);
            String sowSpace = proposalSpace.getString("spaceType");
            String sowroom = proposalSpace.getString("roomcode").toLowerCase();
            LOG.debug("Space Type : " + sowSpace + " | roomcode :" + sowroom + " | space : " + space + " | room :" + room);

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
        JsonObject res = new JsonObject();
        res.put("status", "Failure");
        res.put("comments", "Error in creating scope of services");
        message.reply(res);
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
                        ProposalHeader proposalHeader = new ProposalHeader(resultData.get(0).rows.get(0));
                        List<JsonObject> sow_jsons = resultData.get(1).rows;
                        List<ProposalSOW> proposalSOWs = new ArrayList<ProposalSOW>();
                        for (JsonObject proposalSOW : sow_jsons)
                        {
                            LOG.info("proposalSOW = "+proposalSOW);
                            proposalSOWs.add(new ProposalSOW(proposalSOW));
                        }
                        proposalSOWs.forEach(item -> {LOG.info(item);});
                        createSowAndUploadToDrive(sowrequest,message,proposalHeader, proposalSOWs);
                    }

                });

    }

    private void createSowAndUploadToDrive(JsonObject sowrequest,Message message, ProposalHeader proposalHeader, List<ProposalSOW> proposalSOWs) {

        LOG.info("proposalSOWs size = "+proposalSOWs.size());
        if(proposalSOWs.size()  == 0){
            JsonObject res = new JsonObject();
            res.put("status", "Failure");
            res.put("comments", "Please add the products/addons before creating Scope of services");
            LOG.info(res.toString());
            sendResponse(message, res);

        }else {
            DriveFile driveFile = null;
            try {
                String outputFile = new SOWTemplateCreator(proposalHeader,proposalSOWs,sowrequest.getString("sowversion")).create();
                driveFile = this.driveServiceProvider.uploadFileForUser(outputFile, sowrequest.getString("userId"), proposalHeader.getQuoteNumNew() + "_SOW", proposalHeader.getSalesEmail(), sowrequest.getString("readOnlyFlag"));
                JsonObject res = new JsonObject();
                res.put("status", "Success");
                res.put("driveWebViewLink", driveFile.getWebViewLink());
                res.put("id", driveFile.getId());
                res.put("outputFile", outputFile);
                res.put("version", sowrequest.getString("sowversion"));
                LOG.info(res.toString());

                sendResponse(message, res);
            } catch (Exception e) {
                sendResponse(message, new JsonObject().put("status","failure").put("driveWebViewLink","").put("id","").put("outputFile","").put("version",sowrequest.getString("sowversion")));
                e.printStackTrace();
            }
        }
    }

    private void sendResponse(Message message, JsonObject response)
    {
        LOG.debug("Response :" + response.encodePrettily());
        message.reply(LocalCache.getInstance().store(response));
    }

}