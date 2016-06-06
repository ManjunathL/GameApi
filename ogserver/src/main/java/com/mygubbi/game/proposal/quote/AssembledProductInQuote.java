package com.mygubbi.game.proposal.quote;

import com.mygubbi.common.StringUtils;
import com.mygubbi.game.proposal.ModuleDataService;
import com.mygubbi.game.proposal.ProductLineItem;
import com.mygubbi.game.proposal.ProductModule;
import com.mygubbi.game.proposal.model.AccHwComponent;
import com.mygubbi.game.proposal.model.Module;
import com.mygubbi.game.proposal.model.ModuleComponent;

import java.util.ArrayList;
import java.util.List;

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

    public AssembledProductInQuote(ProductLineItem product)
    {
        this.product = product;
        this.prepare();
    }

    public String getTitle()
    {
        return this.product.getTitle();
    }

    public double getAmount()
    {
        return this.product.getAmount();
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
        ModuleAccessory accessory = new ModuleAccessory(unit, seq, component.getCode(), component.getTitle(), quantity, component.getMake());
        this.moduleAccessories.add(accessory);
    }

    private void addToModuleHardware(AccHwComponent component, int quantity, String unit, int seq)
    {
        ModuleAccessory accessory = new ModuleAccessory(unit, seq, component.getCode(), component.getTitle(), quantity, component.getMake());
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

        public ModuleAccessory(String unit, int seq, String code, String title, double quantity, String make)
        {
            this.unit = unit;
            this.seq = seq;
            this.code = code;
            this.title = title;
            this.quantity = quantity;
            this.make = make;
        }
    }
}

