package com.mygubbi.game.proposal.price;

import com.mygubbi.game.proposal.model.AccHwComponent;
import com.mygubbi.game.proposal.model.IModuleComponent;

/**
 * Created by Sunil on 09-09-2016.
 */
public class AccessoryComponent
{
    private AccHwComponent component;
    private double quantity;

    public AccessoryComponent(AccHwComponent component, IModuleComponent moduleComponent)
    {
        this.component = component;
        this.quantity = moduleComponent.getQuantity();
    }

    public double getCost()
    {
        return this.component.getPrice() * this.quantity;
    }
}
