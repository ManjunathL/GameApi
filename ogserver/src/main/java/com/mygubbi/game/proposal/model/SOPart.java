package com.mygubbi.game.proposal.model;

/**
 * Created by User on 27-08-2017.
 */
public class SOPart {

    private String erpCode;
    private String referencePartNo;
    private String uom ;
    private String title;
    private double qty;

    public SOPart(String erpCode, String referencePartNo, String uom, String title, double qty) {
        this.erpCode = erpCode;
        this.referencePartNo = referencePartNo;
        this.uom = uom;
        this.title = title;
        this.qty = qty;
    }

    public String getErpCode() {
        return erpCode;
    }

    public String getReferencePartNo() {
        return referencePartNo;
    }

    public String getUom() {
        return uom;
    }

    public String getTitle() {
        return title;
    }

    public double getQty() {
        return qty;
    }

    public void setErpCode(String erpCode) {
        this.erpCode = erpCode;
    }

    public void setReferencePartNo(String referencePartNo) {
        this.referencePartNo = referencePartNo;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SOPart soPart = (SOPart) o;

        if (erpCode != null ? !erpCode.equals(soPart.erpCode) : soPart.erpCode != null) return false;
        if (referencePartNo != null ? !referencePartNo.equals(soPart.referencePartNo) : soPart.referencePartNo != null)
            return false;
        if (uom != null ? !uom.equals(soPart.uom) : soPart.uom != null) return false;
        return !(title != null ? !title.equals(soPart.title) : soPart.title != null);

    }

    @Override
    public int hashCode() {
        int result = erpCode != null ? erpCode.hashCode() : 0;
        result = 31 * result + (referencePartNo != null ? referencePartNo.hashCode() : 0);
        result = 31 * result + (uom != null ? uom.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        return result;
    }


    public SOPart merge(SOPart other) {
        assert(this.equals(other));
        return new SOPart(this.erpCode, this.referencePartNo, this.uom, this.title, this.qty + other.qty);
    }

    @Override
    public String toString() {
        return "SOPart{" +
                "erpCode='" + erpCode + '\'' +
                ", referencePartNo='" + referencePartNo + '\'' +
                ", uom='" + uom + '\'' +
                ", title='" + title + '\'' +
                ", qty=" + qty +
                '}';
    }
}
