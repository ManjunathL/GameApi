package com.mygubbi.game.proposal;

import com.mygubbi.game.proposal.model.Module;
import com.mygubbi.game.proposal.model.RateCard;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by test on 18-07-2016.
 */
public class ModulePriceHolder
{
    private double shutterCost = 0;
    private double carcassCost = 0;
    private double accessoryCost = 0;
    private double hardwareCost = 0;
    private double labourCost = 0;
    private double totalCost = 0;

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
        return new JsonObject().put("carcassCost", carcassCost).put("shutterCost", shutterCost)
                .put("accessoryCost", accessoryCost).put("hardwareCost", hardwareCost).put("labourCost", labourCost)
                .put("totalCost", this.round(totalCost, 2));
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
    }

    public void addToHardwareCost(double cost)
    {
        this.hardwareCost += cost;
    }

    public void calculateTotalCost(Module mgModule, RateCard labourRateCard, RateCard loadingFactorCard)
    {
        double largestAreaOfModule = mgModule.getLargestAreaOfModuleInSft();
        this.labourCost = largestAreaOfModule * labourRateCard.getRate();
        this.totalCost = (carcassCost + shutterCost + labourCost ) * loadingFactorCard.getRate() + accessoryCost + hardwareCost;
    }

    private double round(double value, int places)
    {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
