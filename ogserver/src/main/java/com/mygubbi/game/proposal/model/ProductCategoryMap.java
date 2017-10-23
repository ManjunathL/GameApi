package com.mygubbi.game.proposal.model;

import com.mygubbi.common.DateUtil;
import io.vertx.core.json.JsonObject;

import java.sql.Date;

/**
 * Created by User on 17-08-2017.
 */
public class ProductCategoryMap extends JsonObject {


    private static final String ID = "id";
    private static final String PRODUCT_CATEGORY = "prodCategory";
    private static final String TYPE = "type";
    private static final String FROM_DATE = "fromDate";
    private static final String TO_DATE = "toDate";

    public ProductCategoryMap()
    {

    }

    public ProductCategoryMap(JsonObject json)
    {
        super(json.getMap());
    }


    public int getId() {
        return this.getInteger(ID);
    }

    public ProductCategoryMap setId(int id){
        put(ID,id);
        return this;
    }

    public String getProductCategory() {
        return this.getString(PRODUCT_CATEGORY);
    }

    public ProductCategoryMap setProductCategory(String productCategory)
    {
       put(PRODUCT_CATEGORY,productCategory);
        return this;
    }

    public String getType() {
        return this.getString(TYPE);
    }

    public ProductCategoryMap setType(String type)
    {
        put(TYPE,type);
        return this;
    }


    public Date getFromDate() {
        return DateUtil.convertDate(this.getString(FROM_DATE));
    }

    public ProductCategoryMap setFromDate(Date fromDate)
    {
        put(FROM_DATE,fromDate);
        return this;
    }

    public Date getToDate() {
        return DateUtil.convertDate(this.getString(TO_DATE));
    }

    public ProductCategoryMap setToDate(Date toDate)
    {
        put(TO_DATE,toDate);
        return this;
    }


}
