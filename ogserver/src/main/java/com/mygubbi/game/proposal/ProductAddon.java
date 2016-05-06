package com.mygubbi.game.proposal;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * Created by Sunil on 26-04-2016. May not be needed as we don't process the on the server or maybe for the report purpose.
 */
public class ProductAddon extends JsonObject
{
    private static String SEQ = "seq";
    private static String TYPE = "type";
    private static String NAME = "name";
    private static String DESCRIPTION = "desc";
    private static String IMAGE = "image";
    private static String QUANTITY = "quantity";
    private static String AMOUNT = "amount";
    private static String RATE = "rate";

    public ProductAddon()
    {

    }

    public ProductAddon(JsonObject data)
    {
        super(data.getMap());
    }


}

