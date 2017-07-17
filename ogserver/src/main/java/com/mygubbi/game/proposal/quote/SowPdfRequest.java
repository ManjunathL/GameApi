package com.mygubbi.game.proposal.quote;

import io.vertx.core.json.JsonObject;
/**
 * Created by shilpa on 13/7/17.
 */
public class SowPdfRequest {
        private String xlsFileLocation;
        private String fileNameToUpload;
        private String xlsFileNameInDrive;
        private String userId;

   public SowPdfRequest(JsonObject req) {
        this.xlsFileLocation = req.getString("xlsFileLocation");
        this.fileNameToUpload = req.getString("fileNameToUpload");
        this.xlsFileNameInDrive = req.getString("xlsFileNameInDrive");
        this.userId = req.getString("userId");

    }

    public String getXlsFileName() {
        return xlsFileLocation;
    }

    public void setXlsFileName(String xlsFileName) {
        this.xlsFileLocation = xlsFileName;
    }

    public String getXlsFileNameInDrive() { return xlsFileNameInDrive; }

    public void setXlsFileNameInDrive(String xlsFileNameInDrive) {this.xlsFileNameInDrive = xlsFileNameInDrive; }

    public String getFileNameToUpload() {return fileNameToUpload;}

    public void setFileNameToUpload(String fileNameToUpload) { this.fileNameToUpload = fileNameToUpload; }

    public void setUserId(String userId) {this.userId = userId;}

    public String getUserId() {return userId;}
}
