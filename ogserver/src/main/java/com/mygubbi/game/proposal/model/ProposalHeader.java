package com.mygubbi.game.proposal.model;

import com.mygubbi.common.StringUtils;
import io.vertx.core.json.JsonObject;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

import java.util.Date;

/**
 * Created by test on 21-05-2016.
 */
public class ProposalHeader extends JsonObject
{
    public ProposalHeader(JsonObject json)
    {
        super(json.getMap());
    }

    public ProposalHeader()
    {

    }

    public String docsFolder()
    {
        return this.getString("docsfolder");
    }

    public int getId()
    {
        if (!this.containsKey("id")) return 0;
        return this.getInteger("id");
    }

    public Double getTotalAmount()
    {
        return this.getDouble("amount");
    }
}


