package com.mygubbi.si.catalog;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Mehbub on 13-10-2016.
 */
public class SeoRecord {
    private static final int ID = 0;
    private static final int SID = 0;

    private final static Logger LOG = LogManager.getLogger(SeoRecord.class);
    private String[] row;
    public String getId()
    {
        return this.row[ID];
    }


    public String getSeoId()
    {
        return this.row[SID];
    }
}
