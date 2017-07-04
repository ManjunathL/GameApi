package com.mygubbi.game.proposal.price;

import com.mygubbi.game.proposal.model.AccHwComponent;
import com.mygubbi.game.proposal.model.IModuleComponent;
import com.mygubbi.game.proposal.model.PriceMaster;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Date;

/**
 * Created by Sunil on 09-09-2016.
 */
public class AccessoryComponent
{
    private AccHwComponent component;
    private double quantity;
    private Date priceDate;
    private String city;


    private final static Logger LOG = LogManager.getLogger(AccessoryComponent.class);

    public AccessoryComponent(AccHwComponent component, IModuleComponent moduleComponent, Date priceDate, String city)
    {
        this.component = component;
        this.quantity = moduleComponent.getQuantity();
        this.priceDate = priceDate;
        this.city = city;
    }

    public AccHwComponent getComponent()
    {
        return component;
    }

    public double getQuantity()
    {
        return quantity;
    }

    public double getCost()
    {
        if (component.getCode().contains("ADDON"))
        {
            LOG.debug("inside addon");
            PriceMaster addonRate = RateCardService.getInstance().getAddonRate(component.getCode(), this.priceDate, this.city);
            return addonRate.getPrice() * this.quantity;
        }
        else {
            LOG.debug("inside accessory");
            PriceMaster accessoryRate = RateCardService.getInstance().getAccessoryRate(component.getCode(), this.priceDate, this.city);
            return accessoryRate.getPrice() * this.quantity;
        }
    }

    public double getTotalSourcePrice()
    {
        if (component.getCode().contains("ADDON"))
        {
            LOG.debug("inside addon");
            PriceMaster addonRate = RateCardService.getInstance().getAddonRate(component.getCode(), this.priceDate, this.city);
            return addonRate.getSourcePrice() * this.quantity;
        }
        else {
            LOG.debug("inside accessory");
            PriceMaster accessoryRate = RateCardService.getInstance().getAccessoryRate(component.getCode(), this.priceDate, this.city);
            return accessoryRate.getSourcePrice() * this.quantity;
        }
    }
    public double getPrice()
    {
        if (component.getCode().contains("ADDON"))
        {
            LOG.debug("inside addon");
            PriceMaster addonRate = RateCardService.getInstance().getAddonRate(component.getCode(), this.priceDate, this.city);
            return addonRate.getPrice();
        }
        else {
            LOG.debug("inside accessory");
            PriceMaster accessoryRate = RateCardService.getInstance().getAccessoryRate(component.getCode(), this.priceDate, this.city);
            return accessoryRate.getPrice();
        }
    }
    public double getSourcePrice()
    {
        if (component.getCode().contains("ADDON"))
        {
            LOG.debug("inside addon");
            PriceMaster addonRate = RateCardService.getInstance().getAddonRate(component.getCode(), this.priceDate, this.city);
            return addonRate.getSourcePrice();
        }
        else {
            LOG.debug("inside accessory");
            PriceMaster accessoryRate = RateCardService.getInstance().getAccessoryRate(component.getCode(), this.priceDate, this.city);
            return accessoryRate.getSourcePrice();
        }
    }
    @Override
    public String toString() {
        return "AccessoryComponent{" +
                "component=" + component +
                ", quantity=" + quantity +
                ", priceDate=" + priceDate +
                ", city='" + city + '\'' +
                '}';
    }
}
