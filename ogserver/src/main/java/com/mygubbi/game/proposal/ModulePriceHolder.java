package com.mygubbi.game.proposal;

import com.mygubbi.game.proposal.model.Module;
import com.mygubbi.game.proposal.model.RateCard;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by test on 18-07-2016.
 */
public class ModulePriceHolder
{
    private final static Logger LOG = LogManager.getLogger(ModulePriceHolder.class);

    private double shutterCost = 0;
    private double carcassCost = 0;
    private double accessoryCost = 0;
    private double hardwareCost = 0;
    private double labourCost = 0;
    private double totalCost = 0;
    private double woodworkCost = 0;
    private double moduleArea;

    private JsonArray errors = null;

    public void addError(String error)
    {
        if (errors == null) this.errors = new JsonArray();
        this.errors.add(error);
    }

    public boolean hasErrors()
    {
        return this.errors != null && !this.errors.isEmpty();
    }

    public JsonArray getErrors()
    {
        return this.errors;
    }

    public JsonObject getPriceJson()
    {
        return new JsonObject().put("woodworkCost", this.round(this.woodworkCost, 2))
                .put("moduleArea", this.moduleArea)
                .put("totalCost", this.round(this.totalCost, 2));
    }

    public void addToCarcassCost(double cost)
    {
        this.carcassCost += cost;
    }

    public void addToShutterCost(double cost)
    {
        this.shutterCost += cost;
    }

    public void addToAccessoryCost(double cost)
    {
        this.accessoryCost += cost;
        LOG.info("Accessory cost incremented by :" + cost);
    }

    public void addToHardwareCost(double cost)
    {
        this.hardwareCost += cost;
    }

    public void calculateTotalCost(Module mgModule, RateCard labourRateCard, RateCard loadingFactorCard)
    {
        this.moduleArea = mgModule.getLargestAreaOfModuleInSft();
        LOG.debug("Module area : " + moduleArea );
        this.labourCost = this.moduleArea * labourRateCard.getRate();
        LOG.info("Labour cost : " + labourCost);
        this.woodworkCost = (this.carcassCost + this.shutterCost + this.labourCost) * loadingFactorCard.getRate() + this.hardwareCost;
        LOG.debug("Carcass cost :" + this.carcassCost);
        LOG.debug("Shutter cost :" + this.shutterCost);
        LOG.debug("Hardware cost :" + this.hardwareCost);
        LOG.debug("Ratecard cost :" + loadingFactorCard.getRate());

        LOG.debug("Woodwork cost :" + woodworkCost);
        this.totalCost = this.woodworkCost + this.accessoryCost;

        LOG.info("Accessory cost:" + this.accessoryCost);
    }

    private double round(double value, int places)
    {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
