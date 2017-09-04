package com.mygubbi.game.proposal.model.dw;

import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 04-09-2017.
 */
public class ReportingObjects {

    public int proposalId;
    public String version;


    public List<JsonObject> queryDatasForModule = new ArrayList<>();
    public List<JsonObject> queryDatasForComponent = new ArrayList<>();
    public List<JsonObject> queryDatasForProduct = new ArrayList<>();
    public List<JsonObject> queryDatasForAddon = new ArrayList<>();


}
