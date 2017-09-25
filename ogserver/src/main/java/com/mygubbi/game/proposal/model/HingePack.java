package com.mygubbi.game.proposal.model;

import io.vertx.core.json.JsonObject;

/**
 * Created by User on 29-05-2017.
 */
public class HingePack extends JsonObject {

    private static final String MODULE_CODE = "modulecode";
    private static final String HINGE_CODE = "hingecode";
    private static final String QUANTITY = "qty";
    private static final String TYPE = "type";
    private static final String QTY_FORMULA = "qtyFormula";
    private static final String QTY_FLAG = "qtyFlag";
    private static final String ERP_CODE = "erpCode";

    public HingePack()
    {

    }

    public HingePack(JsonObject json)
    {
        super(json.getMap());
    }

    public String getModuleCode() {
        return this.getString(MODULE_CODE);
    }

    public String getHingeCode() {
        return this.getString(HINGE_CODE);
    }

    public double getQUANTITY() {
        return this.getDouble(QUANTITY);
    }

    public String getTYPE() {
        return this.getString(TYPE);
    }

    public String getQtyFormula() {
        return this.getString(QTY_FORMULA);
    }

    public String getQtyFlag() {
        return this.getString(QTY_FLAG);
    }
    public String getErpCode() {
        return this.getString(ERP_CODE);
    }


}
