package com.mygubbi.game.proposal.model;

/**
 * Created by User on 27-08-2017.
 */
public class SOPartForAddon {

    private String description;
    private String details;
    private String uom ;
    private double qty;
    private String remarks;

    public SOPartForAddon(String description, String details, String uom, double qty, String remarks) {
        this.description = description;
        this.details = details;
        this.uom = uom;
        this.qty = qty;
        this.remarks = remarks;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "SOPartForAddon{" +
                "description='" + description + '\'' +
                ", details='" + details + '\'' +
                ", uom='" + uom + '\'' +
                ", qty=" + qty +
                ", remarks=" + remarks +
                '}';
    }
}
