package com.mygubbi.si.catalog;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Mehbub on 18-08-2016.
 */
public class BlogRecord {

    private static final int ID = 0;
    private static final int BID = 0;

    private final static Logger LOG = LogManager.getLogger(BlogRecord.class);
    private String[] row;
    public String getId()
    {
        return this.row[ID];
    }


    public String getBlogId()
    {
        return this.row[BID];
    }


}
