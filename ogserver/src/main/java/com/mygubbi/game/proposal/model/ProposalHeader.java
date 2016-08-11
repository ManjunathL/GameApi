package com.mygubbi.game.proposal.model;

import io.vertx.core.json.JsonObject;

/**
 * Created by test on 21-05-2016.
 */
public class ProposalHeader extends JsonObject
{

    public static final String CRMID = "crmId";
    public static final String PROJECT_NAME = "projectName";
    public static final String PROJECT_ADDRESS1 = "paddress1";
    public static final String PROJECT_ADDRESS2 = "paddress2";
    public static final String PROJECT_CITY = "pcity";
    public static final String SALESPERSON_NAME = "salesName";
    public static final String DESIGNER_NAME = "designerName";
    public static final String AMOUNT = "amount";
    public static final String FOLDER_PATH = "folderPath";

    public ProposalHeader(JsonObject json)
    {
        super(json.getMap());
    }

    public ProposalHeader()
    {

    }

    public String folderPath()
    {
        return this.getString(FOLDER_PATH);
    }

    public String getCrmId()
    {
        return this.getString(CRMID);
    }

    public int getId()
    {
        if (!this.containsKey("id")) return 0;
        return this.getInteger("id");
    }

    public Double getAmount()
    {
        if (this.containsKey(AMOUNT))
            return this.getDouble(AMOUNT);
        else
            return 0.0;
    }
}


