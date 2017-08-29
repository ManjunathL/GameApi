package com.mygubbi.game.proposal.model;


import com.mygubbi.game.proposal.price.RateCardService;
import io.vertx.core.json.JsonObject;

import java.sql.Date;

/**
 * Created by User on 19-08-2017.
 */
public class BoqItem extends JsonObject {

    private String code;
    private String erpCode;
    private String catalogueCode;
    private String uom;
    private double rate;
    private double unitRate;
    private double quantity;
    private double price;
    private double unitPrice;

    public BoqItem(String code, String catalogueCode, String erpCode, String uom, double quantity, Date priceDate, String city)
    {
        PriceMaster priceMaster = null;
        if (code.startsWith("HANDLE") || code.startsWith("KNOB"))
        {
            priceMaster = RateCardService.getInstance().getHandleOrKnobRate(code,priceDate,city);
        }
        else if (code.startsWith("HINGE"))
        {
            priceMaster = RateCardService.getInstance().getHingeRate(code,priceDate,city);
        }
        else {
            priceMaster = RateCardService.getInstance().getHardwareRate(code,priceDate,city);
        }
        this.code = code;
        this.catalogueCode = catalogueCode;
        this.erpCode = erpCode;
        this.rate = priceMaster.getPrice();
        this.unitRate = priceMaster.getSourcePrice();
        this.quantity = quantity;
        this.uom = uom;
        this.price = this.rate * this.quantity;
        this.unitPrice = this.unitRate * this.quantity;

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getErpCode() {
        return erpCode;
    }

    public void setErpCode(String erpCode) {
        this.erpCode = erpCode;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCatalogueCode() {
        return catalogueCode;
    }

    public void setCatalogueCode(String catalogueCode) {
        this.catalogueCode = catalogueCode;
    }

    public double getUnitRate() {
        return unitRate;
    }

    public void setUnitRate(double unitRate) {
        this.unitRate = unitRate;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }
}
