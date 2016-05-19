package com.mygubbi.game.proposal;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.game.proposal.model.*;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;

/**
 * Created by Sunil on 08-01-2016.
 */

public class ModulePricingService extends AbstractVerticle
{
    private final static Logger LOG = LogManager.getLogger(ModulePricingService.class);

    public static final String CALCULATE_PRICE = "calculate.module.price";

    @Override
    public void start(Future<Void> startFuture) throws Exception
    {
        this.setupPriceCalculator();
        startFuture.complete();
    }

    @Override
    public void stop() throws Exception
    {
        super.stop();
    }

    private void setupPriceCalculator()
    {
        EventBus eb = VertxInstance.get().eventBus();
        eb.localConsumer(CALCULATE_PRICE, (Message<Integer> message) -> {
            ProductModule productModule = (ProductModule) LocalCache.getInstance().remove(message.body());
            this.calculatePrice(productModule, message);
        }).completionHandler(res -> {
            LOG.info("Module price calculator service started." + res.succeeded());
        });
    }

    private void calculatePrice(ProductModule productModule, Message message)
    {
        Collection<ModuleComponent> components = ModuleDataService.getInstance().getModuleComponents(productModule.getMGCode());
        JsonArray errors = new JsonArray();

        double shutterCost = 0;
        double carcassCost = 0;
        double accessoryCost = 0;
        double hardwareCost = 0;

        //TODO: labourcost to be included

        RateCard carcassRateCard = RateCardService.getInstance().getRateCard(productModule.getCarcassCode(), RateCard.CARCASS_TYPE);
        RateCard shutterRateCard = RateCardService.getInstance().getRateCard(productModule.getFinishCode(), RateCard.SHUTTER_TYPE);
        RateCard loadingFactorCard = RateCardService.getInstance().getRateCard(RateCard.LOADING_FACTOR, RateCard.LOADING_FACTOR_TYPE);
        RateCard labourRateCard = RateCardService.getInstance().getRateCard(RateCard.LABOUR_FACTOR, RateCard.LABOUR_FACTOR_TYPE);

        if (carcassRateCard == null || shutterRateCard == null || loadingFactorCard == null || labourRateCard == null)
        {
            errors.add("Carcass, Shutter, Labour or Loading factor rate cards not setup." + productModule.getCarcassCode() + ":" + productModule.getFinishCode());
            this.sendResponse(message, errors, shutterCost, carcassCost, accessoryCost, hardwareCost, 0);
            return;
        }

        for (ModuleComponent component : components)
        {
            switch (component.getType())
            {
                case ModuleComponent.CARCASS_TYPE:
                    CarcassPanel carcassPanel = ModuleDataService.getInstance().getCarcassPanel(component.getComponentCode());
                    double carcassPanelCost = carcassPanel.getCost(carcassRateCard);
                    if (carcassPanelCost == 0)
                    {
                        errors.add("Carcass panel cost is not available for " + carcassPanel.toString() + carcassRateCard.toString());
                    }
                    carcassCost += carcassPanelCost * component.getQuantity();
                    break;

                case ModuleComponent.SHUTTER_TYPE:
                    ShutterPanel shutterPanel = ModuleDataService.getInstance().getShutterPanel(component.getComponentCode());
                    double shutterPanelCost = shutterPanel.getCost(shutterRateCard);
                    if (shutterPanelCost == 0)
                    {
                        errors.add("Shutter panel cost is not available for " + shutterPanel.toString() + shutterRateCard.toString());
                    }
                    shutterCost += shutterPanelCost * component.getQuantity();
                    break;

                case ModuleComponent.ACCESSORY_TYPE:
                    AccHwComponent accessory = ModuleDataService.getInstance().getAccessory(component.getComponentCode(), productModule.getMakeType());
                    if (accessory == null || accessory.getPrice() == 0)
                    {
                        errors.add("Accessory cost is not available for " + accessory);
                    }
                    accessoryCost += accessory.getPrice() * component.getQuantity();
                    break;

                case ModuleComponent.HARDWARE_TYPE:
                    AccHwComponent hardware = ModuleDataService.getInstance().getHardware(component.getComponentCode(), productModule.getMakeType());
                    if (hardware == null || hardware.getPrice() == 0)
                    {
                        errors.add("Hardware cost is not available for " + hardware);
                    }
                    hardwareCost += hardware.getPrice() * component.getQuantity();
                    break;

                default:
                    errors.add("Component is not of known type " + component.toString());
                    break;
            }
        }
        double totalCost = (carcassCost + shutterCost + accessoryCost + hardwareCost) * loadingFactorCard.getRate();

        this.sendResponse(message, errors, shutterCost, carcassCost, accessoryCost, hardwareCost, totalCost);
    }

    private void sendResponse(Message message, JsonArray errors, double shutterCost, double carcassCost,
                              double accessoryCost, double hardwareCost, double totalCost)
    {
        JsonObject moduleCost = new JsonObject().put("carcasscost", carcassCost).put("shuttercost", shutterCost)
                .put("accessorycost", accessoryCost).put("hardwarecost", hardwareCost)
                .put("totalcost", totalCost).put("errors", errors);
        LOG.info("Sending price calculation result :" + moduleCost.encodePrettily());
        message.reply(LocalCache.getInstance().store(moduleCost));
    }

}
