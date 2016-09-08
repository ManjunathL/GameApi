package com.mygubbi.game.proposal.price;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.game.proposal.*;
import com.mygubbi.game.proposal.model.*;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Sunil on 08-01-2016.
 */

public class ModulePricingService extends AbstractVerticle
{
    private final static Logger LOG = LogManager.getLogger(ModulePricingService.class);
    public static final String CALCULATE_PRICE = "calculate.module.price";
    private static final double SQMM2SQFT = 0.0000107639;

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

    private void calculatePrice(ProductModule productModule, Message message) {
        Module mgModule = ModuleDataService.getInstance().getModule(productModule.getMGCode());
        Collection<ModuleComponent> components = ModuleDataService.getInstance().getModuleComponents(productModule.getMGCode());

        ModulePriceHolder modulePriceHolder = new ModulePriceHolder();

        if (components == null || components.isEmpty() || mgModule == null) {
            modulePriceHolder.addError("Module components or module not setup for MG code: -" + productModule.getMGCode() + "-");
            this.sendResponse(message, modulePriceHolder, productModule);
            return;
        }
        ShutterFinish shutterFinish = ModuleDataService.getInstance().getFinish(productModule.getFinishCode());
        ShutterFinish carcassFinish = ModuleDataService.getInstance().getFinish(productModule.getCarcassCode(), productModule.getFinishCode());

        RateCard carcassRateCard = RateCardService.getInstance().getRateCard(productModule.getCarcassCode(), RateCard.CARCASS_TYPE);
        RateCard carcassFinishRateCard = RateCardService.getInstance().getRateCard(carcassFinish.getCostCode(), RateCard.SHUTTER_TYPE);
        RateCard shutterRateCard = RateCardService.getInstance().getRateCard(shutterFinish.getCostCode(), RateCard.SHUTTER_TYPE);
        RateCard loadingFactorCard = RateCardService.getInstance().getRateCard(RateCard.LOADING_FACTOR, RateCard.FACTOR_TYPE);
        RateCard labourRateCard = RateCardService.getInstance().getRateCard(RateCard.LABOUR_FACTOR, RateCard.FACTOR_TYPE);

        if (carcassRateCard == null || carcassFinishRateCard == null || shutterRateCard == null
                || loadingFactorCard == null || labourRateCard == null) {
            modulePriceHolder.addError("Carcass, Carcass Finish, Shutter, Labour or Loading factor rate cards not setup." + productModule.getCarcassCode() + " : "
                    + productModule.getFinishCode() + " : " + shutterFinish.getCostCode());
            this.sendResponse(message, modulePriceHolder, productModule);
            return;
        }

        for (IModuleComponent component : components) {
            if (component.isCarcass()) {
                this.calculateExposedCarcassPanelCost(productModule, modulePriceHolder, carcassFinish, carcassRateCard,
                        carcassFinishRateCard, component);
            } else {
                this.calculateComponentCost(productModule, modulePriceHolder, shutterFinish, carcassRateCard, shutterRateCard, component);
            }
        }

        for (ModuleAccessoryPack moduleAccessoryPack : productModule.getAccessoryPacks()) {
            LOG.info("Calculating cost for accessory pack:" + moduleAccessoryPack.getAccessoryPackCode());
            Collection<AccessoryPackComponent> accessoryPackComponents =
                    ModuleDataService.getInstance().getAccessoryPackComponents(moduleAccessoryPack.getAccessoryPackCode());
            for (AccessoryPackComponent accessoryPackComponent : accessoryPackComponents) {
                LOG.info("Calculating cost for ap component :" + accessoryPackComponent.getComponentCode() + " : " + accessoryPackComponent.getType());
                this.calculateComponentCost(productModule, modulePriceHolder, shutterFinish, carcassRateCard, shutterRateCard, accessoryPackComponent);
            }
            for (String addonCode : moduleAccessoryPack.getAddons()) {
                LOG.info("Calculating cost for addon accessory :" + addonCode);
                this.calculateAccessoryCost(modulePriceHolder, addonCode, 1);
            }
        }

        if (productModule.isAccessoryUnit())
        {
            modulePriceHolder.calculateTotalCost(productModule.getMGCode(), productModule.getWidth(), productModule.getDepth(), labourRateCard, loadingFactorCard);
        }
        else
        {
            modulePriceHolder.calculateTotalCost(mgModule, labourRateCard, loadingFactorCard);
        }
        this.sendResponse(message, modulePriceHolder, productModule);
    }

    public Collection<ICostComponent> getModuleComponents(ProductModule productModule)
    {
        Module mgModule = ModuleDataService.getInstance().getModule(productModule.getMGCode());
        Collection<ModuleComponent> components = ModuleDataService.getInstance().getModuleComponents(productModule.getMGCode());

        ShutterFinish carcassFinish = ModuleDataService.getInstance().getFinish(productModule.getCarcassCode(), productModule.getFinishCode());
        RateCard carcassRateCard = RateCardService.getInstance().getRateCard(productModule.getCarcassCode(), RateCard.CARCASS_TYPE);
        RateCard carcassFinishRateCard = RateCardService.getInstance().getRateCard(carcassFinish.getCostCode(), RateCard.SHUTTER_TYPE);

        ShutterFinish shutterFinish = ModuleDataService.getInstance().getFinish(productModule.getFinishCode());
        RateCard shutterRateCard = RateCardService.getInstance().getRateCard(shutterFinish.getCostCode(), RateCard.SHUTTER_TYPE);

        List<ICostComponent> costComponents = new ArrayList<>();

        for (ModuleComponent moduleComponent : components)
        {
            if (moduleComponent.isCarcass())
            {
                costComponents.add(new ModulePanel());
            }
        }

        return costComponents;
    }


    private void calculateComponentCost(ProductModule productModule, ModulePriceHolder modulePriceHolder, ShutterFinish shutterFinish, RateCard carcassRateCard,
                                        RateCard shutterRateCard, IModuleComponent component)
    {
        switch (component.getType())
        {
            case IModuleComponent.CARCASS_TYPE:
                this.calculateCarcassPanelCost(modulePriceHolder, carcassRateCard, component);
                break;

            case IModuleComponent.SHUTTER_TYPE:
                if (productModule.isAccessoryUnit())
                {
                    this.calculateShutterCostFromModuleDimensions(modulePriceHolder, productModule.getWidth(), productModule.getDepth(), shutterRateCard, component);
                }
                else
                {
                    this.calculateShutterCost(modulePriceHolder, shutterFinish, shutterRateCard, component);
                }
                break;

            case IModuleComponent.ACCESSORY_TYPE:
                this.calculateAccessoryCost(modulePriceHolder, component);
                break;

            case IModuleComponent.HARDWARE_TYPE:
                this.calculateHardwareCost(modulePriceHolder, component);
                break;

            default:
                modulePriceHolder.addError("Component is not of known type " + component.toString());
                break;
        }
    }

    private void calculateHardwareCost(ModulePriceHolder modulePriceHolder, IModuleComponent component)
    {
        AccHwComponent hardware = ModuleDataService.getInstance().getHardware(component.getComponentCode());
        if (hardware == null || hardware.getPrice() == 0)
        {
            modulePriceHolder.addError("Hardware cost is not available for " + component.getComponentCode());
        }
        else
        {
            modulePriceHolder.addToHardwareCost(hardware.getPrice() * component.getQuantity());
            System.out.println("null" + "," + "null" + "," + "null" + "," + "null" + "," + "null" + "," + "null" + "," + "null" + "," + "null" + "," + "null" + "," + component.getType() + "," + component.getComponentCode() + "," + component.getQuantity() + "," + "null" + "," + "null" + "," + "null" + "," + hardware.getPrice() + "," + (hardware.getPrice() * component.getQuantity()));
        }
    }

    private void calculateAccessoryCost(ModulePriceHolder modulePriceHolder, IModuleComponent component)
    {
        this.calculateAccessoryCost(modulePriceHolder, component.getComponentCode(), component.getQuantity());
        System.out.print("null" +  "," + "null" +  "," + "null" + "," + "null" + "," + "null" + "," + "null" + "," + "null" + "," + "null" + "," + "null" + "," + component.getType() +","+ component.getComponentCode() + "," + component.getQuantity()  + "," + "null" + "," + "null" + "," + "null" + ","  );
    }

    private void calculateAccessoryCost(ModulePriceHolder modulePriceHolder, String addonCode, double quantity)
    {
        AccHwComponent accessory = ModuleDataService.getInstance().getAccessory(addonCode);
        if (accessory == null || accessory.getPrice() == 0)
        {
            modulePriceHolder.addError("Accessory cost is not available for " + addonCode);
        }
        else
        {
            modulePriceHolder.addToAccessoryCost(accessory.getPrice() * quantity);
        }
        System.out.println((accessory.getPrice() * quantity));
    }

    private void calculateShutterCost(ModulePriceHolder modulePriceHolder, ShutterFinish shutterFinish, RateCard shutterRateCard, IModuleComponent component)
    {
        ShutterPanel shutterPanel = ModuleDataService.getInstance().getShutterPanel(component.getComponentCode());
        LOG.debug(" Shutter : " + shutterPanel.getDimesions() );
        if (shutterPanel == null)
        {
            modulePriceHolder.addError("Shutter panel is not setup for " + component.getComponentCode());
            return;
        }
        double shutterPanelCost = shutterPanel.getCost(shutterRateCard, shutterFinish);

        LOG.debug("Shutter Panel Area : " + shutterPanel.getCuttingArea(shutterFinish));
        LOG.debug("Shutter Panel Cost : " + shutterPanel.getCost(shutterRateCard,shutterFinish));

        if (shutterPanelCost == 0)
        {
            modulePriceHolder.addError("Shutter panel cost is not available for " + shutterPanel.getCode() + shutterRateCard.getKey());
        }
        modulePriceHolder.addToShutterCost(shutterPanelCost * component.getQuantity());

        System.out.println("null" + "," + "null" +  "," + "null" + "," + "null" + "," + "null" + "," + "null" + "," + "null" + "," + "null" + "," + "null" + "," + component.getType() + "," + component.getComponentCode() + "," + component.getQuantity()  + "," + shutterPanel.getDimesions() + "," + "null" + "," + "null" + "," + shutterPanelCost +  "," + (shutterPanelCost * component.getQuantity()));

    }

    private void calculateShutterCostFromModuleDimensions(ModulePriceHolder modulePriceHolder, int width, int depth,
                                                          RateCard shutterRateCard, IModuleComponent component)
    {
        double shutterPanelCost = 0;
        if (shutterRateCard != null) shutterPanelCost = width * depth * SQMM2SQFT * shutterRateCard.getRateByThickness(18);

        if (shutterPanelCost == 0)
        {
            modulePriceHolder.addError("Shutter panel cost is not available for " + component.getComponentCode() + ":" + shutterRateCard.getKey());
        }
        modulePriceHolder.addToShutterCost(shutterPanelCost * component.getQuantity());

        System.out.println("null" + "," + "null" +  "," + "null" + "," + "null" + "," + "null" + "," + "null" + "," + "null" + "," + "null" + "," + "null" + "," + component.getType() + "," + component.getComponentCode() + "," + component.getQuantity() + "," + "null" + "," + width + "," + depth +","+ shutterPanelCost +  "," + (shutterPanelCost * component.getQuantity()));

    }

    private void calculateExposedCarcassPanelCost(ProductModule productModule, ModulePriceHolder modulePriceHolder,
                                                  ShutterFinish carcassFinish, RateCard carcassRateCard,
                                                  RateCard carcassFinishRateCard, IModuleComponent component)
    {
        CarcassPanel carcassPanel = ModuleDataService.getInstance().getCarcassPanel(component.getComponentCode());
        if (carcassPanel == null)
        {
            modulePriceHolder.addError("Carcass panel is not setup for " + component.getComponentCode());
            return;
        }

        double carcassPanelCost = 0;
        if ((productModule.isLeftExposed() && carcassPanel.isLeftPanel()) ||
                (productModule.isRightExposed() && carcassPanel.isRightPanel()) ||
                (productModule.hasExposedBottom() && carcassPanel.isBottomPanel()))
        {
            carcassPanelCost = carcassPanel.getCost(carcassFinishRateCard, carcassFinish);
        }
        else
        {
            carcassPanelCost = carcassPanel.getCost(carcassRateCard);
        }
        if (carcassPanelCost == 0)
        {
            LOG.info("Carcass panel area:" + carcassPanel.getArea() + ". Rate:" + carcassRateCard.getRateByThickness(carcassPanel.getThickness()));
            modulePriceHolder.addError("Carcass panel cost is not available for " + carcassPanel.getCode() + " in rate card " + carcassRateCard.getKey());
        }
        modulePriceHolder.addToCarcassCost(carcassPanelCost * component.getQuantity());

        System.out.println("null" + "," + "null" +  "," + "null" + "," + "null" + "," + "null" + "," + "null" + "," + "null" + "," + "null" + "," + "null" + "," + component.getType() +","+ component.getComponentCode() + "," + component.getQuantity() + ","+ carcassPanel.getDimesions() + "," + "null" + "," + "null" + "," + carcassPanelCost + "," + (carcassPanelCost * component.getQuantity()));

    }

    private void calculateCarcassPanelCost(ModulePriceHolder modulePriceHolder, RateCard carcassRateCard, IModuleComponent component)
    {
        CarcassPanel carcassPanel = ModuleDataService.getInstance().getCarcassPanel(component.getComponentCode());
        if (carcassPanel == null)
        {
            modulePriceHolder.addError("Carcass panel is not setup for " + component.getComponentCode());
            return;
        }

        double carcassPanelCost = carcassPanel.getCost(carcassRateCard);
        if (carcassPanelCost == 0)
        {
            LOG.info("Carcass panel area:" + carcassPanel.getArea() + ". Rate:" + carcassRateCard.getRateByThickness(carcassPanel.getThickness()));
            modulePriceHolder.addError("Carcass panel cost is not available for " + carcassPanel.getCode() + " in rate card " + carcassRateCard.getKey());
        }
        modulePriceHolder.addToCarcassCost(carcassPanelCost * component.getQuantity());

        System.out.println("null" + "," + "null" +  "," + "null" + "," + "null" + "," + "null" + "," + "null" + "," + "null" + "," + "null" + "," + "null" + "," + component.getType() +","+ component.getComponentCode() + "," + component.getQuantity() + ","+ carcassPanel.getDimesions() + "," + "null" + "," + "null" + "," + carcassPanelCost + "," + (carcassPanelCost * component.getQuantity()));

    }

    private void sendResponse(Message message, ModulePriceHolder modulePriceHolder, ProductModule productModule)
    {
        JsonObject resultJson = null;
        if (modulePriceHolder.hasErrors())
        {
            resultJson = new JsonObject().put("errors", modulePriceHolder.getErrors()).put("mgCode", productModule.getMGCode());
            LOG.info("Pricing for product module has errors: " + productModule.encodePrettily() + " ::: " + resultJson.encodePrettily());
        }
        else
        {
            resultJson = modulePriceHolder.getPriceJson();
            JsonObject pm = new JsonObject().put("mg", productModule.getMGCode()).put("carcass", productModule.getCarcassCode())
                    .put("finish", productModule.getFinishCode());
            LOG.info("Sending price calculation result :" + pm.encodePrettily() + " :: " + resultJson.encodePrettily());
        }
        message.reply(LocalCache.getInstance().store(resultJson));

    }

}
