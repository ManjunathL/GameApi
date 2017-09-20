package com.mygubbi.game.proposal.model;

import com.mygubbi.game.proposal.erp.BOQTemplateCreator;
import io.vertx.core.json.JsonObject;

/**
 * Created by user on 26-05-2017.
 */
public class Handle {
    public static final String CODE = "code";
    public static final String TYPE = "type";
    public static final String TITLE = "title";
    public static final String FINISH = "finish";
    public static final String MG_CODE = "mgCode";
    public static final String THICKNESS = "thickness";
    public static final String SOURCE_PRICE = "sourcePrice";
    public static final String MSP = "msp";
    public static final String IMAGE_PATH = "imagePath";
    public static final String ERP_CODE = "erpCode";
    public static final String ARTICLE_NO = "articleNo";
    public static final String BOQ_DISPLAY_ORDER = "boqDisplayOrder";
    //public static final String QUANTITY="quantity";

    private String code;
    private String type;
    private String title;
    private String finish;
    private String mgCode;
    private String thickness;
    private int sourcePrice;
    private int msp;
    private String imagePath;
    private String erpCode;
    private String articleNo;
    private int boqDisplayOrder;

    public Handle() {
    }

    public Handle(JsonObject json) {
        this.setCode(json.getString(CODE)).setType(json.getString(TYPE)).setTitle(json.getString(TITLE)).setFinish(json.getString(FINISH))
                .setMgCode(json.getString(MG_CODE)).setThickness(json.getString(THICKNESS)).setSourcePrice(json.getInteger(SOURCE_PRICE))
                .setMsp(json.getInteger(MSP)).setImagePath(json.getString(IMAGE_PATH)).setErpCode(json.getString(ERP_CODE))
                .setArticleNo(json.getString(ARTICLE_NO)).setBoqDisplayOrder(json.getInteger(BOQ_DISPLAY_ORDER));
    }

    public String getCode() {
        return code;
    }

    public Handle setCode(String code) {
        this.code = code;
        return this;
    }

    public String getType() {
        return type;
    }

    public Handle setType(String type) {
        this.type = type;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Handle setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getFinish() {
        return finish;
    }

    public Handle setFinish(String finish) {
        this.finish = finish;
        return this;
    }

    public String getMgCode() {
        return mgCode;
    }

    public Handle setMgCode(String mgCode) {
        this.mgCode = mgCode;
        return this;
    }

    public String getThickness() {
        return thickness;
    }

    public Handle setThickness(String thickness) {
        this.thickness = thickness;
        return this;
    }

    public int getSourcePrice() {
        return sourcePrice;
    }

    public Handle setSourcePrice(int sourcePrice) {
        this.sourcePrice = sourcePrice;
        return this;
    }

    public int getMsp() {
        return msp;
    }

    public Handle setMsp(int msp) {
        this.msp = msp;
        return this;
    }


    public String getImagePath() {
        return imagePath;
    }

    public Handle setImagePath(String imagePath) {
        this.imagePath = imagePath;
        return this;
    }

    public String getErpCode() {
        return erpCode;
    }

    public Handle setErpCode(String erpCode) {
        this.erpCode = erpCode;
        return this;
    }

    public String getArticleNo(){
        return articleNo;
    }

    public Handle setArticleNo(String articleNo){
        this.articleNo = articleNo;
        return this;
    }

    public int getBoqDisplayOrder(){
        return boqDisplayOrder;
    }

    public Handle setBoqDisplayOrder(int boqDisplayOrder){
        this.boqDisplayOrder = boqDisplayOrder;
        return this;
    }



    @Override
    public String toString() {
        return "Handle{" +
                "code='" + code + '\'' +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", finish='" + finish + '\'' +
                ", mgCode='" + mgCode + '\'' +
                ", thickness='" + thickness + '\'' +
                ", sourcePrice=" + sourcePrice +
                ", msp=" + msp +
                ", imagePath='" + imagePath + '\'' +
                ", erpCode='" + erpCode + '\'' +
                ", articleNo='" + articleNo + '\'' +
                ", boqDisplayOrder='" + boqDisplayOrder + '\'' +
                '}';
    }

}
