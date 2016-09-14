package com.mygubbi.game.proposal.price;

import com.mygubbi.game.proposal.ProductModule;
import com.mygubbi.game.proposal.model.AccHwComponent;
import com.mygubbi.game.proposal.model.IModuleComponent;

/**
 * Created by Sunil on 09-09-2016.
 */
public class HardwareComponent
{
    private AccHwComponent component;
    private double quantity;

    public HardwareComponent(AccHwComponent component, ProductModule productModule, IModuleComponent moduleComponent)
    {
        this.component = component;
        this.quantity = this.calculateQuantity(productModule, moduleComponent);
    }

    public double getCost()
    {
        return this.component.getPrice() * this.quantity;
    }

    private double calculateQuantity(ProductModule productModule, IModuleComponent moduleComponent)
    {
        if (moduleComponent.isCalculatedQuantity()) return this.calculateQuantityUsingFormula(productModule, moduleComponent);
        return moduleComponent.getQuantity();
    }

    private double calculateQuantityUsingFormula(ProductModule productModule, IModuleComponent moduleComponent)
    {
        switch (moduleComponent.getQuantityFormula())
        {
            case "F1":
                return (productModule.getWidth() > 1000) ? 4 : 2;
            case "F2":
                return (productModule.getWidth() * productModule.getDepth() * 1.05 / (1000 / 1000));
            case "F3":
                return productModule.getWidth() * 1.05 / 100;
            case "F4":
                return (productModule.getWidth() * productModule.getDepth() * 1.05 / (1000 / 1000)) * 4;
            case "F5":
                return (productModule.getWidth() * productModule.getDepth() * 1.05 / (1000 / 1000)) / 2;
            case "F6":
                int value1 = (productModule.getHeight() > 2100) ? 5 : 4;
                int value2 = (productModule.getWidth() > 600) ? 2 : 1;
                return value1 * value2;
            case "F7":
                return (productModule.getHeight() * 2 + productModule.getWidth()) / 1000;
            case "F8":
                return (productModule.getHeight() + productModule.getWidth() * 2) * 1.05 / 1000;
            case "F9":
                return (productModule.getHeight() * 2 + productModule.getWidth()) * 1.05 / 1000;
            case "F10":
                return (productModule.getWidth() > 600) ? 6 : 4;
            case "F11":
                return productModule.getWidth() * 1.05 / 1000;
            case "F12":
                return (productModule.getWidth() > 600) ? 4 : 2;
            case "F13":
                return (productModule.getWidth() > 1000) ? 2 : 1;
            case "F14":
                return productModule.getWidth() * productModule.getDepth() * 10.764 / (1000 / 1000);
            case "F15":
                return productModule.getHeight() * 1.05/1000;
            case "F16":
                return (productModule.getWidth() > 600) ? 8 : 4;
            case "F17":
                return (productModule.getWidth() > 600) ? 3 : 2;
            case "F18":
                        int valuenew1 = (productModule.getHeight() > 2100) ? 5 : 4;
                        int valuenew2 = (productModule.getWidth() > 600) ? 2 : 1;
                        return valuenew1 * valuenew2*4;
            case "F19":
                return (productModule.getWidth() > 600) ? 12 : 8;
            case "F20":
                return (productModule.getWidth() > 600) ? 16 : 8;
            default:
                return 0;
        }
    }

}