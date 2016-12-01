package com.mygubbi.si.catalog;

import com.mygubbi.ServerVerticle;
import com.mygubbi.catalog.DIYJson;
import com.mygubbi.catalog.SeoJson;
import com.mygubbi.catalog.SeoManagementService;
import com.mygubbi.common.LocalCache;
import com.mygubbi.common.StringUtils;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import io.vertx.core.AsyncResult;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Mehbub on 01-12-2016.
 */
public class DIYRecord {

    private static final int ID = 0;
    private static final int SID = 0;

    private final static Logger LOG = LogManager.getLogger(SeoRecord.class);
    private String[] row;
    public String getId()
    {
        return this.row[ID];
    }


    public String getDiyId()
    {
        return this.row[SID];
    }
}