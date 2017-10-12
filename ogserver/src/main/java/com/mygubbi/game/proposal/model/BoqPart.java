package com.mygubbi.game.proposal.model;

/**
 * Created by User on 27-08-2017.
 */
public class BoqPart {

    private String erpCode;
    private String referencePartNo;
    private String uom ;
    private String title;
    private double rate;
    private double qty;
    private double price;

    public BoqPart(String erpCode, String referencePartNo, String uom, String title, double rate, double qty, double price) {
        this.erpCode = erpCode;
        this.referencePartNo = referencePartNo;
        this.uom = uom;
        this.title = title;
        this.rate = rate;
        this.qty = qty;
        this.price = price;
    }

    public String getErpCode() {
        return erpCode;
    }

    public void setErpCode(String erpCode) {
        this.erpCode = erpCode;
    }

    public String getReferencePartNo() {
        return referencePartNo;
    }

    public void setReferencePartNo(String referencePartNo) {
        this.referencePartNo = referencePartNo;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BoqPart boqPart = (BoqPart) o;

        if (erpCode != null ? !erpCode.equals(boqPart.erpCode) : boqPart.erpCode != null) return false;
        if (referencePartNo != null ? !referencePartNo.equals(boqPart.referencePartNo) : boqPart.referencePartNo != null)
            return false;
        if (uom != null ? !uom.equals(boqPart.uom) : boqPart.uom != null) return false;
        return !(title != null ? !title.equals(boqPart.title) : boqPart.title != null);

    }

    @Override
    public int hashCode() {
        int result = erpCode != null ? erpCode.hashCode() : 0;
        result = 31 * result + (referencePartNo != null ? referencePartNo.hashCode() : 0);
        result = 31 * result + (uom != null ? uom.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        return result;
    }

    public BoqPart merge(BoqPart other) {
        assert(this.equals(other));
        return new BoqPart(this.erpCode, this.referencePartNo, this.uom, this.title, this.qty + other.qty, this.rate + this.rate, this.price + this.price);
    }
}
