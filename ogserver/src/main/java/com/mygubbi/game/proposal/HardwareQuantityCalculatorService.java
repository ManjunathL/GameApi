package com.mygubbi.game.proposal;

import com.mygubbi.game.proposal.price.ModulePriceHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Chirag on 08-09-2016.
 */
public class HardwareQuantityCalculatorService {

    private final static Logger LOG = LogManager.getLogger(ModulePriceHolder.class);

    public double calculateQuantity(ProductModule productModule){

        String formula = null;
        double quantity = 0;
        switch (formula) {
            case "F1": if (productModule.getWidth() > 1000) {
                            quantity = 2;
                        } else {
                            quantity = 4;
                        }
            case "F2" : quantity = (productModule.getWidth()*productModule.getDepth()*1.05/(1000/1000));
                        break;
            case "F3" : quantity = productModule.getWidth()*1.05/100;
                        break;
            case "F4" : quantity = (productModule.getWidth()*productModule.getDepth()*1.05/(1000/1000))/4;
                        break;
            case "F5" : quantity = (productModule.getWidth()*productModule.getDepth()*1.05/(1000/1000))/2;
                        break;
            case "F6" :
                int value1 = 0;
                int value2 = 0;

                if (productModule.getHeight()>2100){
                            value1 = 5;
                        } else {
                            value1 = 4;
                        }
                        if (productModule.getWidth()>600){
                            value2 = 2;
                        } else {
                            value2 = 1;
                        }
                        quantity = value1 * value2;
                        break;
            case "F7" : quantity = (productModule.getHeight() * 2 + productModule.getWidth())/1000;
                        break;
            case "F8" : quantity = (productModule.getHeight() + productModule.getWidth() * 2)* 1.05 / 1000;
                        break;
            case "F9" : quantity = (productModule.getHeight() * 2 + productModule.getWidth()) * 1.05 / 1000;
                        break;
            default: quantity = 0;
        }
        return quantity;
    }

}
