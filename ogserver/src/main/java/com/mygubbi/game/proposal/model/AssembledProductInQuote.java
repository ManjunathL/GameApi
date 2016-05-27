package com.mygubbi.game.proposal.model;

import com.mygubbi.common.StringUtils;
import com.mygubbi.game.proposal.ModuleDataService;
import com.mygubbi.game.proposal.ProductLineItem;
import com.mygubbi.game.proposal.ProductModule;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sunil on 22-05-2016.
 */
public class AssembledProductInQuote
{
    private ProductLineItem product;
    private List<Accessory> accessories;
    private List<Unit> units;

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

    public List<Unit> getUnits()
    {
        return this.units;
    }

    private void prepare()
    {
        this.units = new ArrayList<>();
        this.accessories = new ArrayList<>();
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
                this.addToAccessories(accessory, component.getQuantity());
            }
        }
    }

    private void addToAccessories(AccHwComponent accessoryComponent, int quantity)
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

        Accessory accessory = new Accessory(this.units.size() + 1, code, title);
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
            for (ModuleDimension dimension : this.moduleDimensions)
            {
                if (dimension.depth == mgModule.getDepth() && dimension.height == mgModule.getHeight())
                {
                    dimension.incrementLength(mgModule.getWidth());
                }
            }
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
        public short length;
        public short depth;
        public short height;

        public ModuleDimension(short length, short depth, short height)
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
        public int sequence;
        public String code;
        public String title;
        public double quantity;

        public Accessory(int sequence, String code, String title)
        {
            this.sequence = sequence;
            this.code = code;
            this.title = title;
        }

        public void incrementQuantity(int qty)
        {
            this.quantity += qty;
        }
    }
}

