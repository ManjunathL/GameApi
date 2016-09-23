package com.mygubbi.game.proposal.price;

import com.mygubbi.common.StringUtils;
import com.mygubbi.game.proposal.ModuleAccessoryPack;
import com.mygubbi.game.proposal.ModuleDataService;
import com.mygubbi.game.proposal.ProductModule;
import com.mygubbi.game.proposal.model.*;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Created by Sunil on 18-07-2016.
 */
public class ModulePriceHolder
{
    private final static Logger LOG = LogManager.getLogger(ModulePriceHolder.class);

    private static final double SQMM2SQFT = 0.0000107639;

    private ProductModule productModule;
    private Module mgModule;
    private Collection<ModuleComponent> moduleComponents;
    private List<PanelComponent> panelComponents = Collections.EMPTY_LIST;
    private List<AccessoryComponent> accessoryComponents = Collections.EMPTY_LIST;
    private List<HardwareComponent> hardwareComponents = Collections.EMPTY_LIST;

    private ShutterFinish shutterFinish;
    private ShutterFinish carcassFinish;
    private RateCard carcassMaterialRateCard;
    private RateCard carcassFinishRateCard;
    private RateCard shutterFinishRateCard;
    private RateCard carcassDoubleExposedRateCard;
    private RateCard shutterDoubleExposedRateCard;
    private RateCard loadingFactorCard;
    private RateCard labourRateCard;

    private double shutterCost = 0;
    private double carcassCost = 0;
    private double accessoryCost = 0;
    private double hardwareCost = 0;
    private double labourCost = 0;
    private double totalCost = 0;
    private double woodworkCost = 0;
    private double moduleArea;

    private JsonArray errors = null;

    public ModulePriceHolder()
    {

    }

    public ModulePriceHolder(ProductModule productModule)
    {
        this.productModule = productModule;
    }

    public Collection<ModuleComponent> getModuleComponents()
    {
        return this.moduleComponents;
    }

    public void prepare()
    {
        this.getModuleAndComponents();
        this.prepareRateCards();
        this.resolveComponents();
    }

    private void resolveComponents()
    {
        this.panelComponents = new ArrayList<>();
        this.accessoryComponents = new ArrayList<>();
        this.hardwareComponents = new ArrayList<>();

        for (IModuleComponent component : this.getModuleComponents())
        {
            this.addComponent(component);
        }

        for (ModuleAccessoryPack moduleAccessoryPack : this.getProductModule().getAccessoryPacks())
        {
            Collection<AccessoryPackComponent> accessoryPackComponents =
                    ModuleDataService.getInstance().getAccessoryPackComponents(moduleAccessoryPack.getAccessoryPackCode());
            for (IModuleComponent accessoryPackComponent : accessoryPackComponents)
            {
                this.addComponent(accessoryPackComponent);
            }
            for (String addonCode : moduleAccessoryPack.getAddons())
            {
                this.addAccessoryComponent(new ModuleComponent().setComponentCode(addonCode).setQuantity(1));
            }
        }
    }

    private void addComponent(IModuleComponent component)
    {
        if (component.isCarcass() || component.isShutter())
        {
            this.addModulePanel(component);
        }
        else if (component.isHardware())
        {
            this.addHardwareComponent(component);
        }
        else if (component.isAccessory())
        {
            this.addAccessoryComponent(component);
        }
        else if (component.isToBeResolved())
        {
            IModuleComponent resolvedComponent = new ComponentResolver().resolveComponent(component, this.productModule);
            if (resolvedComponent == null)
            {
                this.addError("Component could not be resolved for : " + component.getType() + ":" + component.getComponentCode());
            }
            else
            {
                this.addComponent(resolvedComponent);
            }
        }
        else
        {
            this.addError("Component type not setup : " + component.getType() + ":" + component.getComponentCode());
        }
    }

    private void addAccessoryComponent(IModuleComponent component)
    {
        AccHwComponent accessoryComponent = this.getAccessoryComponent(component.getComponentCode());
        if (accessoryComponent != null)
        {
            this.accessoryComponents.add(new AccessoryComponent(accessoryComponent, component));
        }
    }

    private void addHardwareComponent(IModuleComponent component)
    {
        AccHwComponent hardwareComponent = this.getHardwareComponent(component.getComponentCode());
        if (hardwareComponent != null)
        {
            this.hardwareComponents.add(new HardwareComponent(hardwareComponent, this.productModule, component));
        }
    }

    private void addModulePanel(IModuleComponent component)
    {
        ModulePanel modulePanel = this.getModulePanel(component);
        if (modulePanel != null)
        {
            this.panelComponents.add(new PanelComponent(this, modulePanel, component));
        }
    }

    private ModulePanel getModulePanel(IModuleComponent component)
    {
        ModulePanel modulePanel = ModuleDataService.getInstance().getPanel(component.getComponentCode());
        if (modulePanel == null)
        {
            this.addError("Panel not setup for " + component.getComponentCode());
        }
        return modulePanel;
    }

    private AccHwComponent getAccessoryComponent(String componentCode)
    {
        AccHwComponent accHwComponent = ModuleDataService.getInstance().getAccessory(componentCode);
        if (accHwComponent == null)
        {
            this.addError("Accessory not setup for " + componentCode);
        }
        return accHwComponent;
    }

    private AccHwComponent getHardwareComponent(String componentCode)
    {
        AccHwComponent accHwComponent = ModuleDataService.getInstance().getHardware(componentCode);
        if (accHwComponent == null)
        {
            this.addError("Hardware not setup for " + componentCode);
        }
        return accHwComponent;
    }

    public List<PanelComponent> getPanelComponents()
    {
        return this.panelComponents;
    }

    private void prepareRateCards()
    {
        this.shutterFinish = ModuleDataService.getInstance().getFinish(productModule.getFinishCode());
        this.carcassFinish = ModuleDataService.getInstance().getFinish(productModule.getCarcassCode(), productModule.getFinishCode());

        String carcassCode = StringUtils.isEmpty(this.mgModule.getMaterial()) ? productModule.getCarcassCode() : this.mgModule.getMaterial();
        this.carcassMaterialRateCard = RateCardService.getInstance().getRateCard(carcassCode, RateCard.CARCASS_TYPE);

        this.carcassFinishRateCard = RateCardService.getInstance().getRateCard(carcassFinish.getCostCode(), RateCard.SHUTTER_TYPE);
        this.shutterFinishRateCard = RateCardService.getInstance().getRateCard(shutterFinish.getCostCode(), RateCard.SHUTTER_TYPE);

        this.carcassDoubleExposedRateCard = RateCardService.getInstance().getRateCard(carcassFinish.getDoubleExposedCostCode(), RateCard.SHUTTER_TYPE);
        this.shutterDoubleExposedRateCard = RateCardService.getInstance().getRateCard(shutterFinish.getDoubleExposedCostCode(), RateCard.SHUTTER_TYPE);

        this.loadingFactorCard = RateCardService.getInstance().getRateCard(RateCard.LOADING_FACTOR, RateCard.FACTOR_TYPE);
        this.labourRateCard = RateCardService.getInstance().getRateCard(RateCard.LABOUR_FACTOR, RateCard.FACTOR_TYPE);

        if (carcassMaterialRateCard == null || carcassFinishRateCard == null || shutterFinishRateCard == null
                || loadingFactorCard == null || labourRateCard == null)
        {
            this.addError("Carcass, Carcass Finish, Shutter, Labour or Loading factor rate cards not setup." + carcassCode + " : "
                    + productModule.getFinishCode() + " : " + shutterFinish.getCostCode());
        }
    }

    private void getModuleAndComponents()
    {
        this.mgModule = ModuleDataService.getInstance().getModule(productModule.getMGCode());
        this.moduleComponents = ModuleDataService.getInstance().getModuleComponents(productModule.getMGCode());
        if (moduleComponents == null || moduleComponents.isEmpty() || mgModule == null)
        {
            this.addError("Module components or module not setup for MG code: -" + productModule.getMGCode() + "-");
        }
    }

    public void addError(String error)
    {
        if (errors == null)
        {
            this.errors = new JsonArray();
        }
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
/*
        return new JsonObject().put("woodworkCost", this.round(this.woodworkCost, 2))
                .put("moduleArea", this.moduleArea)
                .put("totalCost", this.round(this.totalCost, 2));
*/
        return new JsonObject().put("woodworkCost", this.round(this.woodworkCost, 2))
                .put("moduleArea", this.moduleArea)
                .put("carcassCost", this.round(this.carcassCost, 2))
                .put("shutterCost", this.round(this.shutterCost, 2))
                .put("accessoryCost", this.round(this.accessoryCost, 2))
                .put("hardwareCost", this.round(this.hardwareCost, 2))
                .put("totalCost", this.round(this.totalCost, 2));
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

    public void calculateTotalCost()
    {
        this.shutterCost = 0;
        this.carcassCost = 0;
        this.accessoryCost = 0;
        this.hardwareCost = 0;
        this.labourCost = 0;
        this.totalCost = 0;
        this.woodworkCost = 0;
        this.moduleArea = 0;

        for (PanelComponent panel : this.getPanelComponents())
        {
            if (panel.isExposed())
            {
                this.addToShutterCost(panel.getCost());
            }
            else
            {
                this.addToCarcassCost(panel.getCost());
            }
        }

        for (AccessoryComponent accessory : this.accessoryComponents)
        {
            this.addToAccessoryCost(accessory.getCost());
        }

        for (HardwareComponent hardware : this.hardwareComponents)
        {
            this.addToHardwareCost(hardware.getCost());
        }

        if (this.productModule.isAccessoryUnit())
        {
            this.calculateTotalCost(productModule.getMGCode(), productModule.getWidth(), productModule.getDepth(), labourRateCard, loadingFactorCard);
        }
        else
        {
            this.calculateTotalCost(mgModule, labourRateCard, loadingFactorCard);
        }

    }

    public void calculateTotalCost(Module mgModule, RateCard labourRateCard, RateCard loadingFactorCard)
    {
        this.moduleArea = productModule.getAreaOfModuleInSft();
        this.calculateTotalCost(labourRateCard, loadingFactorCard, mgModule.getCode());
    }

    public void calculateTotalCost(String moduleCode, int width, int depth, RateCard labourRateCard, RateCard loadingFactorCard)
    {
        this.moduleArea = width * depth * SQMM2SQFT;
        this.calculateTotalCost(labourRateCard, loadingFactorCard, moduleCode);
    }

    private void calculateTotalCost(RateCard labourRateCard, RateCard loadingFactorCard, String code)
    {
        this.labourCost = this.moduleArea * labourRateCard.getRate();
        this.woodworkCost = (this.carcassCost + this.shutterCost + this.labourCost) * loadingFactorCard.getRate() + this.hardwareCost;
        this.totalCost = this.woodworkCost + this.accessoryCost;
    }

    private double round(double value, int places)
    {
        if (places < 0)
        {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public Module getMgModule()
    {
        return mgModule;
    }

    public ProductModule getProductModule()
    {
        return productModule;
    }

    public ShutterFinish getShutterFinish()
    {
        return shutterFinish;
    }

    public ShutterFinish getCarcassFinish()
    {
        return carcassFinish;
    }

    public RateCard getCarcassMaterialRateCard()
    {
        return carcassMaterialRateCard;
    }

    public RateCard getCarcassFinishRateCard()
    {
        return carcassFinishRateCard;
    }

    public RateCard getShutterFinishRateCard()
    {
        return shutterFinishRateCard;
    }

    public RateCard getCarcassDoubleExposedRateCard()
    {
        return carcassDoubleExposedRateCard;
    }

    public RateCard getShutterDoubleExposedRateCard()
    {
        return shutterDoubleExposedRateCard;
    }

    public String getName()
    {
        return this.getProductModule().getUnit() + "-" + this.productModule.getModuleSequence();
    }

    public List<HardwareComponent> getHardwareComponents()
    {
        return this.hardwareComponents;
    }

    public List<AccessoryComponent> getAccessoryComponents()
    {
        return this.accessoryComponents;
    }

    public List<RateCard> getRateCards()
    {
        return Arrays.asList(this.carcassMaterialRateCard, this.carcassFinishRateCard, this.shutterFinishRateCard,
                this.carcassDoubleExposedRateCard, this.shutterDoubleExposedRateCard, this.loadingFactorCard, this.labourRateCard);
    }
}
