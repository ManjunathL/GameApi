package com.mygubbi.si.gdrive;

import io.vertx.core.json.JsonObject;

/**
 * Created by User on 22-09-2017.
 */
public class UploadToDrive extends JsonObject {

    public String fileName;
    public String filePath;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return "UploadToDrive{" +
                "fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                '}';
    }
}
