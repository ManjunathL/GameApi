package com.mygubbi.game.proposal;

import io.vertx.core.AbstractVerticle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * Created by Chirag on 19-08-2016.
 */
public class PriceRecorderService extends AbstractVerticle
{
    private static final String PRICE_FILES_FOLDER = "C:\\Users\\test\\Desktop\\Mygubbi\\CSV Files";
    private static final String csn = "UTF-8";

    private String moduleCode;
    private int productId;
    private String productTitle;

    private PrintWriter pw = null;

    public PriceRecorderService(String moduleCode, int productId, String productTitle)
    {
        this.moduleCode = moduleCode;
        this.productId = productId;
        this.productTitle = productTitle;
        try
        {
            this.pw = new PrintWriter(PRICE_FILES_FOLDER + "\\" + this.productTitle + "-" + this.productId + ".csv", csn);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            this.pw = null;
        }
    }

    public void print(String text)
    {
        if (this.pw == null) return;
        pw.println(text);
    }

    public void close()
    {
        if (this.pw == null) return;
        this.pw.flush();
        this.pw.close();
        this.pw = null;
    }
}
