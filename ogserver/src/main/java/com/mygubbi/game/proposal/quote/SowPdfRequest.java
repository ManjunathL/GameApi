package com.mygubbi.game.proposal.quote;

import io.vertx.core.json.JsonObject;
/**
 * Created by shilpa on 13/7/17.
 */
public class SowPdfRequest {
    private String xlsFileName;
    private String xlsLocation;



    public SowPdfRequest(JsonObject req) {
        this.xlsFileName = req.getString("xlsFileName");
        this.xlsLocation = req.getString("xlsLocation");

    }

    public String getXlsFileName() {
        return xlsFileName;
    }

    public void setXlsFileName(String xlsFileName) {
        this.xlsFileName = xlsFileName;
    }

    public String getXlsLocation() {
        return xlsLocation;
    }

    public void setXlsLocation(String xlsLocation) {
        this.xlsLocation = xlsLocation;
    }
}
