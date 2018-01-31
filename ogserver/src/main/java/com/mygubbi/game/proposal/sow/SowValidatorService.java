package com.mygubbi.game.proposal.sow;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import com.mygubbi.game.proposal.ModuleDataService;
import com.mygubbi.game.proposal.model.SOWMaster;
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
 * Created by shilpa on 20/9/17.
 */
public class SowValidatorService extends AbstractVerticle {
    public static final String VALIDATE_PROPOSAL_SOW = "validate.proposal.sow";
    private final static Logger LOG = LogManager.getLogger(SowValidatorService.class);
    private  final static String COLON_DELIMITER = ":";

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
        eb.localConsumer(VALIDATE_PROPOSAL_SOW, (Message<Integer> message) -> {

            JsonObject queryParams = (JsonObject) LocalCache.getInstance().remove(message.body());
            validateSowSheet(queryParams,message);

        }).completionHandler(res -> {
            LOG.info("SOW Validator Service started" + res.succeeded());
        });
    }

    private void sendError(Message message,String comments) {
        JsonObject res = new JsonObject();
        res.put("status", "Failure");
        res.put("comments", comments);
//        message.reply(res);
        message.reply(LocalCache.getInstance().store(res));
    }
    private void sendSuccess(Message message,String comments) {
        JsonObject res = new JsonObject();
        res.put("status", "Success");
        res.put("comments", comments);
//        message.reply(res);
        message.reply(LocalCache.getInstance().store(res));
    }

    public void validateSowSheet(JsonObject queryParams,Message message){
        JsonObject response  = new JsonObject();
        List sowList = new ArrayList();
        Set spaceRoomListFromSow = new HashSet();
        Set yesSpaceRoomListFromSow = new HashSet();
        Set noSpaceRoomListFromSow = new HashSet();
        Integer id = LocalCache.getInstance().store(new QueryData("proposal.sow.select.proposalversion", queryParams));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,
                (AsyncResult<Message<Integer>> selectResult) -> {
                    QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
                    if (resultData.errorFlag) {
                        LOG.error("Error in getting proposal. " + resultData.errorMessage, resultData.error);
                        sendError(message, "Error in getting proposal. ");

                    } else {
                        if (resultData.rows.size() == 0) {
                            LOG.error("There is no scope of service/s.");
                            sendError(message, "There is no scope of service/s.");
                        }else{
                            resultData.rows.forEach(row -> {
                                sowList.add(row);
                                if (row.getString("L1S01").isEmpty() || row.getString("L1S01").equals(""))
                                {
                                    LOG.error("Response is :: It is mandatory to fill all the first level of services.");
                                    sendError(message, "It is mandatory to fill all the first level of services.");
                                }

                                if(row.getString("L1S01").equalsIgnoreCase("Yes")) {
                                    if(isRowContainsMygubbiScope(row)) {
                                        yesSpaceRoomListFromSow.add(row.getString("spaceType") + COLON_DELIMITER + row.getString("roomcode").toLowerCase());
                                    }else{
                                        LOG.info("Yes, but has client or NA as Scope");
                                    }
                                }
                                if(row.getString("L1S01").equalsIgnoreCase("No"))
                                    noSpaceRoomListFromSow.add(row.getString("spaceType")+COLON_DELIMITER+row.getString("roomcode").toLowerCase());
                                    spaceRoomListFromSow.add(row.getString("spaceType")+COLON_DELIMITER+row.getString("roomcode").toLowerCase());
                            });

                            List spaceRoomList = new ArrayList();
                            spaceRoomList.addAll(spaceRoomListFromSow);
                            List spaceRoomListWithYes = new ArrayList();
                            spaceRoomListWithYes.addAll(yesSpaceRoomListFromSow);
                            List spaceRoomListWithNo = new ArrayList();
                            spaceRoomListWithNo.addAll(noSpaceRoomListFromSow);
                            getListOfAddonAndProduct(message,queryParams,sowList,spaceRoomList,spaceRoomListWithYes,spaceRoomListWithNo, queryParams);
                        }

                    }
                });
    }
    public  static List compareLists(List<String> sourceList, List<String> destinationList){

        sourceList.removeAll( destinationList );
        return sourceList;
    }
    private void getListOfAddonAndProduct(Message message,JsonObject contextJson, List sowList, List spaceRoomListFromSow, List yesSpaceRoomListFromSow, List spaceRoomListWithNo, JsonObject params) {
        JsonObject queryParams =  new JsonObject();
        Integer proposalId = contextJson.getInteger("proposalId");
        Double version = Double.parseDouble(contextJson.getString("version"));
        queryParams.put("proposalIdForPro", proposalId);
        queryParams.put("fromVerForProd", version);
        queryParams.put("proposalIdForAddOn", proposalId);
        queryParams.put("fromVerForAddOn", version);

        JsonObject response = new JsonObject();
        List spaceRoomListFromProduct= new ArrayList();
        List spaceRoomListFromAddon = new ArrayList();

        Integer id = LocalCache.getInstance().store(new QueryData("spaces.select.fromProductAndAddon", queryParams));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,(AsyncResult<Message<Integer>> selectResult) -> {
            QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
            if ((resultData.errorFlag )||(resultData.rows.size() == 0))
            {
                LOG.error("Error in getting Products and Addons. " + resultData.errorMessage, resultData.error);
                sendError(message, "Error in getting Products and Addons.");
            }
            else
            {
                resultData.rows.forEach(row ->{
                    if(row.getString("type").equalsIgnoreCase("Product"))
                        spaceRoomListFromProduct.add(row.getString("spaceType")+COLON_DELIMITER+row.getString("roomCode").toLowerCase());
                    else
                        spaceRoomListFromAddon.add(row.getString("spaceType")+COLON_DELIMITER+row.getString("roomCode").toLowerCase());
                });

                spaceRoomListFromProduct.forEach(item -> LOG.info(item + ","));

                if((yesSpaceRoomListFromSow.size() == 0 && spaceRoomListFromAddon.size() == 0 && spaceRoomListFromProduct.size()    == 0)
                        && (spaceRoomListWithNo.size() != 0))
                {
                    sendSuccess(message,"Successfully Validated SOW");
                }else {

                    if(yesSpaceRoomListFromSow.size() == 0 && spaceRoomListWithNo.size() == 0){

                        sendError(message, "There is no scope of service/s.");
                    }

                    List ls0 = compareLists(new ArrayList<>(spaceRoomListFromProduct), new ArrayList<>(spaceRoomListFromSow));

                    List ls00 = compareLists(new ArrayList<>(spaceRoomListFromAddon), new ArrayList<>(spaceRoomListFromSow));

                    List ls1 = compareLists(new ArrayList<>(yesSpaceRoomListFromSow), new ArrayList<>(spaceRoomListFromAddon));
                    List ls2 = compareLists(new ArrayList<>(spaceRoomListFromAddon), new ArrayList<>(spaceRoomListFromSow));

                    List list1 = compareLists(new ArrayList<>(spaceRoomListFromProduct),new ArrayList<>(yesSpaceRoomListFromSow));
                    List list2 = compareLists(list1,new ArrayList<>(spaceRoomListWithNo));

                    if(ls0.size() > 0 ){
                        StringBuilder val = new StringBuilder();
                        ls0.forEach(item -> val.append(item + ","));
                        sendError(message, "Add the scope of service/s for the following space type : room - " + val.deleteCharAt(val.lastIndexOf(",")));
                    }else if(list2.size() > 0){
                        StringBuilder val = new StringBuilder();
                        list2.forEach(item -> val.append(item + ","));
                        sendError(message, "Add the scope of service/s for the following space type : room - " + val.deleteCharAt(val.lastIndexOf(",")));
                    }
                    else if(ls00.size() > 0 ){
                        StringBuilder val = new StringBuilder();
                        ls00.forEach(item -> val.append(item + ","));
                        sendError(message, "Add the scope of service/s for the following space type : room - " + val.deleteCharAt(val.lastIndexOf(",")));
                    }
                    else if (ls1.size() == 0) {
                        LOG.info("Saving From here !!!");
                        getListOfCustomAddons(message, sowList, params,contextJson);
                    } else if (ls1.size() > 0) {
                        StringBuilder val = new StringBuilder();
                        ls1.forEach(item -> val.append(item + ","));
                        sendError(message,"Add the addon/s for the following space type : room - " + val.deleteCharAt(val.lastIndexOf(",")));
                    } else if (ls2.size() > 0) {
                        StringBuilder val = new StringBuilder();
                        ls2.forEach(item -> val.append(item + ","));
                        sendError(message,"Add the scope of service/s for the following space type : room - " + val.deleteCharAt(val.lastIndexOf(",")));
                    } else {
                        getListOfCustomAddons(message,sowList,params,contextJson);

                    }
                }
            }
        });

    }

    private void getListOfProposalAddons(Message message,JsonObject contextJson,List
            <JsonObject>sowList,JsonObject params,List<JsonObject> addOnsFromCustomAddons) {
        JsonObject queryParams =  new JsonObject();
        queryParams.put("proposalId", contextJson.getInteger("proposalId"));
        queryParams.put("fromVersion", contextJson.getString("version"));

        JsonObject response = new JsonObject();
        List addOnsFromProductAddonsWithcode = new ArrayList();
        List addOnFromProduct = new ArrayList();
        List addOnCodeFromSow = new ArrayList();
        List addOnCodeFromSowwithNo = new ArrayList();
        List addOnCodeFromSowwithYes = new ArrayList();
        Integer id = LocalCache.getInstance().store(new QueryData("select.proposalAddOns", queryParams));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id,(AsyncResult<Message<Integer>> selectResult) -> {
            QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
            if ((resultData.errorFlag) ) {
                LOG.error("Error in getting addon codes. " + resultData.errorMessage, resultData.error);
                sendError(message, "Error in getting addon codes.");

            } else
            {
                resultData.rows.forEach(row->addOnsFromProductAddonsWithcode.add(row));

                resultData.rows.forEach(row->{
//                        LOG.info("row.getString(\"roomCode\") = "+row.getString("roomCode").toLowerCase());
                    addOnFromProduct.add(row.getString("spaceType")+COLON_DELIMITER+row.getString("roomcode").toLowerCase()
                            +COLON_DELIMITER+row.getString("L1S01Code"));});

                addOnsFromCustomAddons.forEach(customAddon ->{
                    addOnFromProduct.add(customAddon.getString("spaceType")+COLON_DELIMITER+customAddon.getString("roomcode").toLowerCase()
                            +COLON_DELIMITER+customAddon.getString("L1S01Code"));});

                sowList.forEach(sow-> {
                    if((sow.getString("L1S01").equalsIgnoreCase("Yes") && isRowContainsMygubbiScope(sow) )|| sow.getString("L1S01").equalsIgnoreCase("No")) {
                        addOnCodeFromSow.add(sow.getString("spaceType") + COLON_DELIMITER + sow.getString("roomcode").toLowerCase() + COLON_DELIMITER + sow.getString("L1S01Code"));
                    }
                    if(sow.getString("L1S01").equalsIgnoreCase("No"))
                        addOnCodeFromSowwithNo.add(sow.getString("spaceType")+COLON_DELIMITER+sow.getString("roomcode").toLowerCase() + COLON_DELIMITER + sow.getString("L1S01Code"));

                    if(sow.getString("L1S01").equalsIgnoreCase("Yes") && isRowContainsMygubbiScope(sow))
                        addOnCodeFromSowwithYes.add(sow.getString("spaceType")+COLON_DELIMITER+sow.getString("roomcode").toLowerCase() + COLON_DELIMITER + sow.getString("L1S01Code"));
                });

                List<String> l1 = compareLists(new ArrayList<>(addOnCodeFromSow),new ArrayList<>(addOnFromProduct));
                List<String> l3 =compareLists(new ArrayList<>(l1),new ArrayList<>(addOnCodeFromSowwithNo));
                List<String> l2 = compareLists(new ArrayList<>(addOnFromProduct),new ArrayList<>(addOnCodeFromSow));
                List<String> l4 = compareLists(new ArrayList<>(addOnFromProduct),new ArrayList<>(addOnCodeFromSowwithYes));

                //if l2,l4 contains any row that has null as L1S01Code then jusrt remove that line from list...
                List l2Dup = new ArrayList();
                l2Dup.addAll(l2);
                l2.forEach(item ->{
                    if(getL1S01ValFromString(item).length() == 0){
                        l2Dup.remove(item);
                    }
                });

                List l4Dup = new ArrayList();
                l4Dup.addAll(l4);
                l4.forEach(item ->{
                    if(getL1S01ValFromString(item).length() == 0){
                        l4Dup.remove(item);
                    }
                });

                //check l3 and l2 values and ssend error message.....
                if(l3.size() > 0){
                    //there is sow, no adon for that
                    sendError(message, "Add the addon/s for the following - "+getParamValues(l3));

                }else if (l2Dup.size() > 0){
                    sendError(message,"Add the scope of service/s  for the following - "+getParamValues(l2Dup));

                }else if(l4Dup.size() > 0){StringBuilder val = new StringBuilder();
                    sendError(message, "Make scope of service/s response as 'yes' for the following - "+getParamValues(l4Dup));

                }else{
                    sendSuccess(message,"Successfully Validated SOW");
                }
            }

        });
    }

    private String getL1SO1Value(List<String> paramObj){
        StringBuilder sbServiceName = new StringBuilder();
        paramObj.forEach(item->{
            String space_L1Code = item;
            String[] spaceAndCode = space_L1Code.split(COLON_DELIMITER);

            String spaceType = spaceAndCode[0];
            Collection<SOWMaster> sowMasterList = ModuleDataService.getInstance().getSOWMaster(spaceType);
            Object[] masterSOWs = (Object[])sowMasterList.toArray();

            String strL1S01Code = spaceAndCode[2];
            int noOfRows = masterSOWs.length;
            for(int i =0 ;i< noOfRows;i++){
                SOWMaster sow = (SOWMaster) masterSOWs[i];

                if(sow.getL1S01Code().equalsIgnoreCase(strL1S01Code)){
                    sbServiceName.append(sow.getL1S01());
                    sbServiceName.append(", ");

                }
            }
        });
        return  sbServiceName.deleteCharAt(sbServiceName.lastIndexOf(",")).toString();
    }
    private String getParamValues(List<String> paramObj){

        String l1s01Code = getL1SO1Value (paramObj);
        StringBuilder sbSpaceAndRoom = new StringBuilder();
        paramObj.forEach(item-> {
            String space_L1Code = item;
            LOG.info("space_L1Code = "+space_L1Code);
            String[] spaceAndCode = space_L1Code.split(COLON_DELIMITER);

            LOG.info("space, Room = "+spaceAndCode[0]+", "+spaceAndCode[1]);
            sbSpaceAndRoom.append(spaceAndCode[0]+COLON_DELIMITER);
            sbSpaceAndRoom.append(spaceAndCode[1]+",");
        });
        return l1s01Code +"in SpaceType_Room "+sbSpaceAndRoom.deleteCharAt(sbSpaceAndRoom.lastIndexOf(","));
    }


    private String getL1S01ValFromString(String item){
        String space_L1Code = item;
        String[] spaceAndCode = space_L1Code.split(COLON_DELIMITER);

        String spaceType = spaceAndCode[0];
        Collection<SOWMaster> sowMasterList = ModuleDataService.getInstance().getSOWMaster(spaceType);
        Object[] masterSOWs = (Object[])sowMasterList.toArray();
//indesx 1 is room code
        String strL1S01Code = spaceAndCode[2];
        int noOfRows = masterSOWs.length;
        for(int i =0 ;i< noOfRows;i++){
            SOWMaster sow = (SOWMaster) masterSOWs[i];

            if(sow.getL1S01Code().equalsIgnoreCase(strL1S01Code)){
                return(sow.getL1S01());
            }
        }
        return "";
    }
    private void getListOfCustomAddons(Message message,List
            <JsonObject>sowList,JsonObject params,JsonObject contextJson) {
        JsonObject queryParams = new JsonObject();
        queryParams.put("proposalId", contextJson.getInteger("proposalId"));
        queryParams.put("fromVersion", contextJson.getString("version"));
        List addOnsFromCustomAddons = new ArrayList();

        Integer id = LocalCache.getInstance().store(new QueryData("select.customAddons", queryParams));
        VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, id, (AsyncResult<Message<Integer>> selectResult) -> {
            QueryData resultData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
            if ((resultData.errorFlag)) {
                sendError(message, "Error in getting custom addon.");
                LOG.error("Error in getting addon codes. " + resultData.errorMessage, resultData.error);
            } else {
                resultData.rows.forEach(row -> addOnsFromCustomAddons.add(row));
                getListOfProposalAddons(message, contextJson,sowList, params,addOnsFromCustomAddons);

            }
        });
    }

    private boolean isRowContainsMygubbiScope(JsonObject rowObj){
        List<String> level2ServiceList = new ArrayList<>();
        level2ServiceList.add(rowObj.getString("L2S01"));
        level2ServiceList.add(rowObj.getString("L2S02"));
        level2ServiceList.add(rowObj.getString("L2S03"));
        level2ServiceList.add(rowObj.getString("L2S04"));
        level2ServiceList.add(rowObj.getString("L2S05"));
        level2ServiceList.add(rowObj.getString("L2S06"));

        if(level2ServiceList.contains(new String("Mygubbi"))){
            LOG.info("Mygubbi Scope");
            return true;
        }
        LOG.info("Client/NA SCope");
        return  false;

    }

}
