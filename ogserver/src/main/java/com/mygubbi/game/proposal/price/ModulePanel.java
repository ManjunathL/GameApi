package com.mygubbi.game.proposal.price;

import com.mygubbi.game.proposal.model.RateCard;
import com.mygubbi.game.proposal.model.ShutterFinish;

/**
 * Created by Chirag on 08-09-2016.
 */
public class ModulePanel implements ICostComponent
{
    @Override
    public double getCost()
    {
        return 0;
    }

    private double quantity;
    private boolean isExposed;
    private RateCard materialRateCard;
    private RateCard finishRateCard;
    private ShutterFinish finish;

    private int length;
    private int breadth;
    private int thickness;

    private String code;

    public ModulePanel setQuantity(double quantity) {
        this.quantity = quantity;
        return this;
    }

    public ModulePanel setExposed(boolean exposed) {
        isExposed = exposed;
        return this;
    }

    public ModulePanel setMaterialRateCard(RateCard materialRateCard) {
        this.materialRateCard = materialRateCard;
        return this;
    }

    public ModulePanel setFinishRateCard(RateCard finishRateCard) {
        this.finishRateCard = finishRateCard;
        return this;
    }

    public ModulePanel setFinish(ShutterFinish finish) {
        this.finish = finish;
        return this;
    }

    public ModulePanel setLength(int length) {
        this.length = length;
        return this;
    }

    public ModulePanel setBreadth(int breadth) {
        this.breadth = breadth;
        return this;
    }

    public ModulePanel setThickness(int thickness) {
        this.thickness = thickness;
        return this;
    }

    public ModulePanel setCode(String code) {
        this.code = code;
        return this;
    }
}
