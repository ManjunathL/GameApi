package com.mygubbi.game.proposal.price;

import com.mygubbi.common.StringUtils;
import com.mygubbi.game.proposal.ProductModule;
import com.mygubbi.game.proposal.model.AccHwComponent;
import com.mygubbi.game.proposal.model.IModuleComponent;
import com.mygubbi.game.proposal.model.PriceMaster;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Date;

/**
 * Created by Sunil on 09-09-2016.
 */
public class HardwareComponent
{
    private AccHwComponent component;
    private double quantity;
    private String quantityFormula;
    private String accPackCode;
    private Date priceDate;
    private String city;

    private final static Logger LOG = LogManager.getLogger(HardwareComponent.class);


    public HardwareComponent(AccHwComponent component, ProductModule productModule, IModuleComponent moduleComponent, String accPackCode, String city, Date priceDate)
    {
        this.component = component;
        this.quantity = this.calculateQuantity(productModule, moduleComponent);
        this.quantityFormula = moduleComponent.isCalculatedQuantity() ? moduleComponent.getQuantityFormula() : "Fixed Quantity";
        this.accPackCode = accPackCode;
        this.priceDate = priceDate;
        this.city = city;
    }

    public HardwareComponent() {
    }

    public AccHwComponent getComponent()
    {
        return component;
    }

    public double getQuantity()
    {
        return quantity;
    }

    public String getQuantityFormula()
    {
        return quantityFormula;
    }

    public double getCost()
    {
//        LOG.debug("hardware component : " + component.toString());
        PriceMaster hardwareRate = RateCardService.getInstance().getHardwareRate(component.getCode(), this.priceDate, this.city);
        return hardwareRate.getPrice() * this.quantity;
    }

    private double calculateQuantity(ProductModule productModule, IModuleComponent moduleComponent)
    {
        if (moduleComponent.isCalculatedQuantity()) return this.calculateQuantityUsingFormula(productModule, moduleComponent.getQuantityFormula());
        return moduleComponent.getQuantity();
    }

    public double calculateQuantityUsingFormula(ProductModule productModule, String quantityFormula)
    {
        switch (quantityFormula)
        {
            case "F1":
                return (productModule.getWidth() > 1000) ? 4 : 2;
            case "F2":
                return new Double((productModule.getHeight() * productModule.getWidth() * 1.05) / (1000 * 1000));
            case "F3":
                return new Double(productModule.getWidth() * 1.05) / 100;
            case "F4":
                return new Double(productModule.getWidth() * productModule.getDepth() * 1.05) / (1000 * 1000) * 4;
            case "F5":
                return new Double(productModule.getWidth() * productModule.getDepth() * 1.05) / (1000 * 1000) / 2;
            case "F6":
                int value1 = (productModule.getHeight() > 2100) ? 5 : 4;
                int value2 = (productModule.getWidth() > 600) ? 2 : 1;
                return value1 * value2;
            case "F7":
                return new Double(productModule.getHeight() * 2 + productModule.getWidth()) / 1000;
            case "F8":
                return new Double((productModule.getHeight() + productModule.getWidth() * 2) * 1.05) / 1000;
            case "F9":
                return new Double((productModule.getHeight() * 2 + productModule.getWidth()) * 1.05) / 1000;
            case "F10":
                return (productModule.getWidth() > 600) ? 6 : 4;
            case "F11":
                return new Double(productModule.getWidth() * 1.05) / 1000;
            case "F12":
                return (productModule.getWidth() >= 601) ? 4 : 2;
            case "F13":
                return (productModule.getWidth() >= 1001) ? 2 : 1;
            case "F14":
                return new Double(productModule.getWidth() * productModule.getDepth() * 10.764) / (1000 * 1000);
            case "F15":
                return new Double(productModule.getHeight() * 1.05)/1000;
            case "F16":
                return (productModule.getWidth() > 600) ? 8 : 4;
            case "F17":
                return (productModule.getWidth() > 600) ? 3 : 2;
            case "F18":
                        int valuenew1 = (productModule.getHeight() > 2100) ? 5 : 4;
                        int valuenew2 = (productModule.getWidth() > 600) ? 2 : 1;
                        return valuenew1 * valuenew2 * 4;
            case "F19":
                return (productModule.getWidth() > 600) ? 12 : 8;
            case "F20":
                return (productModule.getWidth() > 600) ? 16 : 8;
            case "F21":
                return new Double((productModule.getWidth() * productModule.getDepth() * 1.05) / (1000 * 1000));
            case "F22":
                return new Double(productModule.getHeight() + productModule.getWidth() * 2) / 1000;
            case "F23":
                int value = (productModule.getWidth() > 600) ? 2 : 1;
                return value * 5;
            case "F24":
                return  (productModule.getWidth() >= 1000) ? 3 : 2;
            case "F25":
                return (productModule.getWidth() > 600)? 2 : 1;
            case "F26":
                return ((productModule.getWidth() > 600)?4 : 2)*5;
            case "F27":
                return new Double((productModule.getWidth() * productModule.getDepth() * 1.5) / (1000 * 1000));
            case "F28":
               return new Double((productModule.getHeight() * productModule.getWidth() * 1.5) / (1000 * 1000));
            case "F29":
                int f29value1 = (productModule.getHeight() > 2100) ? 5 : 4;
                int f29value2 = (productModule.getWidth() > 600) ? 2 : 1;
                return f29value1 * f29value2 * 5;
            default:
                return 0;
        }
    }

    public boolean isInAccessoryPack()
    {
        return StringUtils.isNonEmpty(this.accPackCode);
    }

    public boolean isInBaseModule()
    {
        return StringUtils.isEmpty(this.accPackCode);
    }

    public double getTotalSourcePrice()
    {
//        LOG.debug("hardware component : " + component.toString());
        PriceMaster hardwareRate = RateCardService.getInstance().getHardwareRate(component.getCode(), this.priceDate, this.city);
        return hardwareRate.getSourcePrice() * this.quantity;
    }
    public double getSourcePrice()
    {
        PriceMaster hardwareRate = RateCardService.getInstance().getHardwareRate(component.getCode(), this.priceDate, this.city);
        return hardwareRate.getSourcePrice();
    }
    public double getPrice()
    {
        PriceMaster hardwareRate = RateCardService.getInstance().getHardwareRate(component.getCode(), this.priceDate, this.city);
        return hardwareRate.getPrice();
    }

    @Override
    public String toString() {
        return "HardwareComponent{" +
                "component=" + component +
                ", quantity=" + quantity +
                ", quantityFormula='" + quantityFormula + '\'' +
                ", accPackCode='" + accPackCode + '\'' +
                ", priceDate=" + priceDate +
                ", city='" + city + '\'' +
                '}';
    }
}
