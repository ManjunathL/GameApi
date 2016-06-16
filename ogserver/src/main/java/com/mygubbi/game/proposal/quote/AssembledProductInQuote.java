package com.mygubbi.game.proposal.quote;

import com.mygubbi.common.StringUtils;
import com.mygubbi.game.proposal.ModuleDataService;
import com.mygubbi.game.proposal.ProductAddon;
import com.mygubbi.game.proposal.ProductLineItem;
import com.mygubbi.game.proposal.ProductModule;
import com.mygubbi.game.proposal.model.*;
import org.jooq.lambda.Seq;
import org.jooq.lambda.tuple.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.round;
import static org.jooq.lambda.tuple.Tuple.tuple;

/**
 * Created by Sunil on 22-05-2016.
 */
public class AssembledProductInQuote
{
    private ProductLineItem product;
    private List<Accessory> accessories;
    private List<ModuleAccessory> moduleAccessories;
    private List<Unit> units;
    private List<ModuleAccessory> moduleHardware;
    private double addonsAmount;
    private String shutterMaterial;

    public AssembledProductInQuote(ProductLineItem product)
    {
        this.product = product;
        this.prepare();
    }

    public String getTitle()
    {
        return this.product.getTitle();
    }

    public double getTotalAmount()
    {
        return this.product.getAmount();
    }

    public double getAmountWithoutAddons()
    {
        return round(this.product.getAmount() - this.getAddonsAmount());
    }

    public double getAddonsAmount()
    {
        return this.addonsAmount;
    }

    public List<Accessory> getAccessories()
    {
        return this.accessories;
    }

    public List<ModuleAccessory> getModuleAccessories()
    {
        return this.moduleAccessories;
    }

    public ProductLineItem getProduct()
    {
        return this.product;
    }

    public List<Unit> getUnits()
    {
        return this.units;
    }

    public Object getValue(String key)
    {
        if (this.product.containsKey(key)) return this.product.getValue(key);
        switch (key)
        {
            case "carcass.material":
                return "Base:" + this.product.getBaseCarcassCode() + " | Wall:" + this.product.getWallCarcassCode();
            case "shutter.material":
                return this.shutterMaterial;
            case "design":
                return this.product.getDesignCode();
            default:
                return null;
        }
    }

    private void prepare()
    {
        this.units = new ArrayList<>();
        this.accessories = new ArrayList<>();
        this.moduleAccessories = new ArrayList<>();
        this.moduleHardware = new ArrayList<>();
        for (ProductModule module : this.product.getModules())
        {
            this.addModuleToUnit(module);
            this.collectAccessories(module);
        }
        for (ProductAddon addon : this.product.getAddons())
        {
            this.addonsAmount += addon.getAmount();
        }
        ShutterFinish finish = ModuleDataService.getInstance().getFinish(this.product.getFinishCode());
        this.shutterMaterial = finish.getTitle() + " | " + finish.getFinishMaterial();
    }

    private void collectAccessories(ProductModule module)
    {
        for (ModuleComponent component : ModuleDataService.getInstance().getModuleComponents(module.getMGCode()))
        {
            if (ModuleComponent.ACCESSORY_TYPE.equals(component.getType()))
            {
                AccHwComponent accessory = ModuleDataService.getInstance().getAccessory(component.getComponentCode(), module.getMakeType());
                if (accessory == null) continue;
                this.addToProductAccessories(accessory, component.getQuantity());
                this.addToModuleAccessories(accessory, component.getQuantity(), module.getUnit(), module.getSequence());
            }
            else if (ModuleComponent.HARDWARE_TYPE.equals(component.getType()))
            {
                AccHwComponent hardware = ModuleDataService.getInstance().getHardware(component.getComponentCode(), module.getMakeType());
                if (hardware == null) continue;
                this.addToModuleHardware(hardware, component.getQuantity(), module.getUnit(), module.getSequence());
            }
        }
    }

    private void addToModuleAccessories(AccHwComponent component, int quantity, String unit, int seq)
    {
        ModuleAccessory accessory = new ModuleAccessory(unit, seq, component.getCode(), component.getTitle(), quantity, component.getMake(), component.getUom());
        this.moduleAccessories.add(accessory);
    }

    private void addToModuleHardware(AccHwComponent component, int quantity, String unit, int seq)
    {
        ModuleAccessory accessory = new ModuleAccessory(unit, seq, component.getCode(), component.getTitle(), quantity, component.getMake(), component.getUom());
        this.moduleHardware.add(accessory);
    }

    private void addToProductAccessories(AccHwComponent accessoryComponent, int quantity)
    {
        Accessory accessory = this.getAccessory(accessoryComponent.getCode(), accessoryComponent.getTitle());
        accessory.incrementQuantity(quantity);
    }

    private Accessory getAccessory(String code, String title)
    {
        if (StringUtils.isEmpty(code)) code = "Default";
        for (Accessory accessory : this.accessories)
        {
            if (accessory.code.equals(code))
            {
                return accessory;
            }
        }

        Accessory accessory = new Accessory(code, title);
        this.accessories.add(accessory);
        return accessory;
    }

    private void addModuleToUnit(ProductModule module)
    {
        Unit unit = this.getUnit(module.getUnit());
        unit.addModule(module);
    }

    private Unit getUnit(String unitTitle)
    {
        if (StringUtils.isEmpty(unitTitle)) unitTitle = "Default";
        for (Unit unit : this.units)
        {
            if (unit.title.equals(unitTitle))
            {
                return unit;
            }
        }

        Unit unit = new Unit(this.units.size() + 1, unitTitle);
        this.units.add(unit);
        return unit;
    }

    public List<ModuleAccessory> getModuleHardware()
    {
        return moduleHardware;
    }

    public List<ProductModule> getModules()
    {
        return this.product.getModules();
    }

    public List<ModuleCarcass> getAggregatedCarcassPanels()
    {
        List<ModuleCarcass> aggregated =

                Seq.ofType(this.getCarcassPanels().stream(), ModuleCarcass.class)
                        .groupBy(x -> tuple(x.code, x.title, x.width, x.depth, x.thickness, x.edgebinding, x.dimension),
                                Tuple.collectors(
                                        Collectors.summingInt(x -> x.quantity), Collectors.summingDouble(x -> x.area)
                                )
                        )
                        .entrySet()
                        .stream()
                        .map(e -> new ModuleCarcass().setCode(e.getKey().v1).setTitle(e.getKey().v2).setWidth(e.getKey().v3).setDepth(e.getKey().v4)
                                .setThickness(e.getKey().v5).setEdgebinding(e.getKey().v6).setDimension(e.getKey().v7)
                                .setQuantity(e.getValue().v1).setArea(e.getValue().v2))
                        .collect(Collectors.toList());

        return aggregated;
    }

    public List<ModuleShutter> getAggregatedShutterPanels()
    {
        List<ModuleShutter> aggregated =

                Seq.ofType(this.getShutterPanels().stream(), ModuleShutter.class)
                        .groupBy(x -> tuple(x.code, x.title, x.height, x.width, x.thickness, x.edgebinding, x.design, x.color, x.dimension),
                                Tuple.collectors(
                                        Collectors.summingInt(x -> x.quantity), Collectors.summingDouble(x -> x.area)
                                )
                        )
                        .entrySet()
                        .stream()
                        .map(e -> new ModuleShutter().setCode(e.getKey().v1).setTitle(e.getKey().v2).setHeight(e.getKey().v3)
                                .setWidth(e.getKey().v4).setThickness(e.getKey().v5).setEdgebinding(e.getKey().v6)
                                .setDesign(e.getKey().v7).setColor(e.getKey().v8).setDimension(e.getKey().v9)
                                .setQuantity(e.getValue().v1).setArea(e.getValue().v2))
                        .collect(Collectors.toList());

        return aggregated;
    }

    public List<ModuleCarcass> getCarcassPanels()
    {
        List<ModuleCarcass> carcassPanels = new ArrayList<>();
        for (ProductModule module : this.getModules())
        {
            for (ModuleComponent component : ModuleDataService.getInstance().getModuleComponents(module.getMGCode()))
            {
                if (ModuleComponent.CARCASS_TYPE.equals(component.getType()))
                {
                    CarcassPanel panel = ModuleDataService.getInstance().getCarcassPanel(component.getComponentCode());
                    if (panel == null) continue;
                    carcassPanels.add(new ModuleCarcass(panel, component));
                }
            }
        }
        return carcassPanels;
    }

    public List<ModuleShutter> getShutterPanels()
    {
        List<ModuleShutter> shutterPanels = new ArrayList<>();
        for (ProductModule module : this.getModules())
        {
            for (ModuleComponent component : ModuleDataService.getInstance().getModuleComponents(module.getMGCode()))
            {
                if (ModuleComponent.SHUTTER_TYPE.equals(component.getType()))
                {
                    ShutterPanel panel = ModuleDataService.getInstance().getShutterPanel(component.getComponentCode());
                    if (panel == null) continue;
                    shutterPanels.add(new ModuleShutter(panel, component, module, this.product.getDesignCode()));
                }
            }
        }
        return shutterPanels;
    }

    public List<ProductAddon> getAddonAccessories()
    {
        return Seq.seq(this.product.getAddons()).filter(addon -> addon.isAccessory()).toList();
    }

    public List<ProductAddon> getServices()
    {
        return Seq.seq(this.product.getAddons()).filter(addon -> addon.isService()).toList();
    }

    public List<ProductAddon> getCounterTops()
    {
        return Seq.seq(this.product.getAddons()).filter(addon -> addon.isCounterTop()).toList();
    }

    public List<ProductAddon> getAppliances()
    {
        return Seq.seq(this.product.getAddons()).filter(addon -> addon.isAppliance()).toList();
    }

    public static class Unit
    {
        public int sequence;
        public short moduleCount;
        public String title;
        public List<ModuleDimension> moduleDimensions;

        public Unit(int sequence, String title)
        {
            this.sequence = sequence;
            this.title = title;
            this.moduleDimensions = new ArrayList<>();
        }

        public void addModule(ProductModule module)
        {
            Module mgModule = ModuleDataService.getInstance().getModule(module.getMGCode());
            if (mgModule == null)
            {
                throw new RuntimeException("Module not found in master : " + module.getMGCode());
            }

            this.moduleCount++;
            ModuleDimension matchingDimension = this.getModuleDimension(mgModule);
            if (matchingDimension != null)
            {
                matchingDimension.incrementLength(mgModule.getWidth());
            }
            else
            {
                this.moduleDimensions.add(new ModuleDimension(mgModule.getWidth(), mgModule.getDepth(), mgModule.getHeight()));
            }
        }

        private ModuleDimension getModuleDimension(Module mgModule)
        {
            for (ModuleDimension dimension : this.moduleDimensions)
            {
                if (dimension.depth == mgModule.getDepth() && dimension.height == mgModule.getHeight())
                {
                    return dimension;
                }
            }
            return null;
        }

        public String getDimensions()
        {
            StringBuilder sb = new StringBuilder();
            for (ModuleDimension dimension : this.moduleDimensions)
            {
                if (sb.length() > 0) sb.append(", ");
                sb.append(dimension.length).append(" x ").append(dimension.depth).append(" x ").append(dimension.height);
            }
            return sb.toString();
        }
    }

    public static class ModuleDimension
    {
        public int length;
        public int depth;
        public int height;

        public ModuleDimension(int length, int depth, int height)
        {
            this.length = length;
            this.depth = depth;
            this.height = height;
        }

        public void incrementLength(int length)
        {
            this.length += length;
        }
    }

    public static class Accessory
    {
        public String code;
        public String title;
        public double quantity;

        public Accessory(String code, String title)
        {
            this.code = code;
            this.title = title;
        }

        public void incrementQuantity(int qty)
        {
            this.quantity += qty;
        }
    }

    public static class ModuleAccessory
    {
        public String unit;
        public int seq;
        public String code;
        public String title;
        public double quantity;
        public String make;
        public String uom;

        public ModuleAccessory(String unit, int seq, String code, String title, double quantity, String make, String uom)
        {
            this.unit = unit;
            this.seq = seq;
            this.code = code;
            this.title = title;
            this.quantity = quantity;
            this.make = make;
            this.uom = uom;
        }

        public ModuleAccessory(String code, String title, String make, String uom, double quantity)
        {
            this.code = code;
            this.quantity = quantity;
            this.title = title;
            this.make = make;
            this.uom = uom;
        }
    }

    public static class ModuleShutter
    {
        public String dimension;
        public String code;
        public String title;
        public int quantity;
        public int width;
        public int height;
        public int thickness;
        public String edgebinding;
        public double area;
        public String design;
        public String color;

        public ModuleShutter(ShutterPanel panel, ModuleComponent component, ProductModule module, String design)
        {
            this.code = panel.getCode();
            this.title = panel.getTitle();
            this.quantity = component.getQuantity();
            this.width = panel.getLength();
            this.height = panel.getBreadth();
            this.thickness = panel.getThickness();
            this.edgebinding = panel.getEdgebinding();
            this.area = this.quantity * panel.getCuttingArea(ModuleDataService.getInstance().getFinish(module.getFinishCode()));
            this.dimension = this.getDimesions();
            this.color = module.getColorCode();
            this.design = design;
        }

        public ModuleShutter()
        {

        }

        public ModuleShutter setCode(String code)
        {
            this.code = code;
            return this;
        }

        public ModuleShutter setTitle(String title)
        {
            this.title = title;
            return this;
        }

        public ModuleShutter setQuantity(int quantity)
        {
            this.quantity = quantity;
            return this;
        }

        public ModuleShutter setWidth(int width)
        {
            this.width = width;
            return this;
        }

        public ModuleShutter setHeight(int height)
        {
            this.height = height;
            return this;
        }

        public ModuleShutter setThickness(int thickness)
        {
            this.thickness = thickness;
            return this;
        }

        public ModuleShutter setEdgebinding(String edgebinding)
        {
            this.edgebinding = edgebinding;
            return this;
        }

        public ModuleShutter setArea(double area)
        {
            this.area = area;
            return this;
        }

        public ModuleShutter setDesign(String design)
        {
            this.design = design;
            return this;
        }

        public ModuleShutter setColor(String color)
        {
            this.color = color;
            return this;
        }

        public ModuleShutter setDimension(String dimension)
        {
            this.dimension = dimension;
            return this;
        }

        private String getDimesions()
        {
            return this.width + " X " + this.height + " X " + this.thickness;
        }
    }

    public static class ModuleCarcass
    {
        public String code;
        public String title;
        public int quantity;
        public int width;
        public int depth;
        public int thickness;
        public String edgebinding;
        public String dimension;
        public double area;

        public ModuleCarcass()
        {

        }
        public ModuleCarcass(CarcassPanel panel, ModuleComponent component)
        {
            this.code = panel.getCode();
            this.title = panel.getTitle();
            this.quantity = component.getQuantity();
            this.width = panel.getLength();
            this.depth = panel.getBreadth();
            this.thickness = panel.getThickness();
            this.edgebinding = panel.getEdgebinding();
            this.area = panel.getArea();
            this.dimension = this.getDimesions();
        }

        private String getDimesions()
        {
            return this.depth + " X " + this.width + " X " + this.thickness;
        }

        public ModuleCarcass setCode(String code)
        {
            this.code = code;
            return this;
        }

        public ModuleCarcass setTitle(String title)
        {
            this.title = title;
            return this;
        }

        public ModuleCarcass setQuantity(int quantity)
        {
            this.quantity = quantity;
            return this;
        }

        public ModuleCarcass setWidth(int width)
        {
            this.width = width;
            return this;
        }

        public ModuleCarcass setDepth(int depth)
        {
            this.depth = depth;
            return this;
        }

        public ModuleCarcass setThickness(int thickness)
        {
            this.thickness = thickness;
            return this;
        }

        public ModuleCarcass setEdgebinding(String edgebinding)
        {
            this.edgebinding = edgebinding;
            return this;
        }

        public ModuleCarcass setDimension(String dimension)
        {
            this.dimension = dimension;
            return this;
        }

        public ModuleCarcass setArea(double area)
        {
            this.area = area;
            return this;
        }
    }
}

