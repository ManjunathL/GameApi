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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;

/**
 * Created by Sunil on 08-01-2016.
 */

public class ModulePricingService extends AbstractVerticle
{
    private final static Logger LOG = LogManager.getLogger(ModulePricingService.class);

    public static final double SQMM2SQFT = 0.0000107639;
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
        Module mgModule = ModuleDataService.getInstance().getModule(productModule.getMGCode());
        Collection<ModuleComponent> components = ModuleDataService.getInstance().getModuleComponents(productModule.getMGCode());

        JsonArray errors = new JsonArray();

        if (components == null || components.isEmpty() || mgModule == null)
        {
            errors.add("Module components or module not setup for MG code: -" + productModule.getMGCode() + "-");
            this.sendResponse(message, errors, 0, 0, 0, 0, 0, 0, productModule);
            return;
        }

        RateCard carcassRateCard = RateCardService.getInstance().getRateCard(productModule.getCarcassCode(), RateCard.CARCASS_TYPE);
        RateCard shutterRateCard = RateCardService.getInstance().getRateCard(productModule.getFinishCode(), RateCard.SHUTTER_TYPE);
        RateCard loadingFactorCard = RateCardService.getInstance().getRateCard(RateCard.LOADING_FACTOR, RateCard.FACTOR_TYPE);
        RateCard labourRateCard = RateCardService.getInstance().getRateCard(RateCard.LABOUR_FACTOR, RateCard.FACTOR_TYPE);
        //TODO: labourcost to be included

        if (carcassRateCard == null || shutterRateCard == null || loadingFactorCard == null || labourRateCard == null)
        {
            errors.add("Carcass, Shutter, Labour or Loading factor rate cards not setup." + productModule.getCarcassCode() + ":" + productModule.getFinishCode());
            this.sendResponse(message, errors, 0, 0, 0, 0, 0, 0, productModule);
            return;
        }

        double shutterCost = 0;
        double carcassCost = 0;
        double accessoryCost = 0;
        double hardwareCost = 0;
        double labourCost = 0;

        for (ModuleComponent component : components)
        {
            switch (component.getType())
            {
                case ModuleComponent.CARCASS_TYPE:
                    CarcassPanel carcassPanel = ModuleDataService.getInstance().getCarcassPanel(component.getComponentCode());
                    double carcassPanelCost = carcassPanel.getCost(carcassRateCard);
                    if (carcassPanelCost == 0)
                    {
                        LOG.info("Carcass panel area:" + carcassPanel.getArea() + ". Rate:" + carcassRateCard.getRateByThickness(carcassPanel.getThickness()));
                        errors.add("Carcass panel cost is not available for " + carcassPanel.getCode() + " in rate card " + carcassRateCard.getKey());
                    }
                    carcassCost += carcassPanelCost * component.getQuantity();
                    break;

                case ModuleComponent.SHUTTER_TYPE:
                    ShutterPanel shutterPanel = ModuleDataService.getInstance().getShutterPanel(component.getComponentCode());
                    double shutterPanelCost = shutterPanel.getCost(shutterRateCard);
                    if (shutterPanelCost == 0)
                    {
                        errors.add("Shutter panel cost is not available for " + shutterPanel.getCode() + shutterRateCard.getKey());
                    }
                    shutterCost += shutterPanelCost * component.getQuantity();
                    break;

                case ModuleComponent.ACCESSORY_TYPE:
                    AccHwComponent accessory = ModuleDataService.getInstance().getAccessory(component.getComponentCode(), productModule.getMakeType());
                    if (accessory == null || accessory.getPrice() == 0)
                    {
                        errors.add("Accessory cost is not available for " + component.getComponentCode() + " of make " + productModule.getMakeType());
                    }
                    else
                    {
                        accessoryCost += accessory.getPrice() * component.getQuantity();
                    }
                    break;

                case ModuleComponent.HARDWARE_TYPE:
                    AccHwComponent hardware = ModuleDataService.getInstance().getHardware(component.getComponentCode(), productModule.getMakeType());
                    if (hardware == null || hardware.getPrice() == 0)
                    {
                        errors.add("Hardware cost is not available for " + component.getComponentCode() + " of make " + productModule.getMakeType());
                    }
                    else
                    {
                        hardwareCost += hardware.getPrice() * component.getQuantity();
                    }
                    break;

                default:
                    errors.add("Component is not of known type " + component.toString());
                    break;
            }
        }

        double largestAreaOfModule = this.getLargestAreaOfModule(mgModule);
        labourCost = largestAreaOfModule * labourRateCard.getRate();
        double totalCost = (carcassCost + shutterCost + labourCost ) * loadingFactorCard.getRate() + accessoryCost + hardwareCost;
        totalCost = round(totalCost, 2);
        this.sendResponse(message, errors, shutterCost, carcassCost, accessoryCost, hardwareCost, labourCost, totalCost, productModule);
    }

    private double getLargestAreaOfModule(Module mgModule)
    {
        double h = mgModule.getHeight();
        double w = mgModule.getWidth();
        double d = mgModule.getDepth();

        double t1 = 0;
        double t2 = 0;

        if (h > w)
        {
            t1 = h;
            t2 = w;
        }
        else
        {
            t1 = w;
            t2 = h;
        }

        if (d > t2)
        {
            t2 = d;
        }

        return t1 * t2 * SQMM2SQFT;
    }

    private void sendResponse(Message message, JsonArray errors, double shutterCost, double carcassCost,
                              double accessoryCost, double hardwareCost, double labourCost, double totalCost, ProductModule productModule)
    {
        JsonObject resultJson = null;
        if (errors != null && !errors.isEmpty())
        {
            resultJson = new JsonObject().put("errors", errors).put("mgCode", productModule.getMGCode());
            LOG.info("Pricing for product module has errors: " + productModule.encodePrettily() + " ::: " + resultJson.encodePrettily());
        }
        else
        {
            resultJson = new JsonObject().put("carcassCost", carcassCost).put("shutterCost", shutterCost)
                    .put("accessoryCost", accessoryCost).put("hardwareCost", hardwareCost).put("labourCost", labourCost)
                    .put("totalCost", totalCost);
            LOG.info("Sending price calculation result :" + resultJson.encodePrettily());
        }
        message.reply(LocalCache.getInstance().store(resultJson));

    }

    private double round(double value, int places)
    {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
