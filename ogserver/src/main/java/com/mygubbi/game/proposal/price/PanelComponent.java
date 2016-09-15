package com.mygubbi.game.proposal.price;

import com.mygubbi.game.proposal.ProductModule;
import com.mygubbi.game.proposal.model.*;

/**
 * Created by Chirag on 08-09-2016.
 */
public class PanelComponent
{
    private static final String CARCASS_TYPE = "C";
    private static final String SHUTTER_TYPE = "S";

    private static final double SQMM2SQFT = 0.0000107639;
    private static final String TYPE_LEFT = "L";
    private static final String TYPE_RIGHT = "R";
    private static final String TYPE_TOP = "T";
    private static final String TYPE_BOTTOM = "B";
    private static final String TYPE_BACK = "K";
    private static final String TYPE_OTHER = "O";
    private static final int ACCESSORY_PANEL_THICKNESS = 18;

    private double quantity;
    private RateCard materialRateCard;
    private RateCard finishRateCard;
    private ShutterFinish finish;

    private String code;
    private String type;
    private String side;
    private int length;
    private int breadth;
    private int thickness;
    private PanelExposed exposed;

    private enum PanelExposed{NONE, SINGLE, DOUBLE};

    public PanelComponent(ModulePriceHolder priceHolder, ModulePanel modulePanel, IModuleComponent component)
    {
        this.setBaseAttributes(modulePanel, component);
        this.setDimensions(priceHolder.getProductModule(), priceHolder.getMgModule(), modulePanel);
        this.setExposed(priceHolder.getProductModule());
        this.setRateCards(priceHolder);
    }

    private void setBaseAttributes(ModulePanel modulePanel, IModuleComponent component)
    {
        this.setCode(modulePanel.getCode())
                .setType(modulePanel.getType()).setSide(modulePanel.getSide())
                .setQuantity(component.getQuantity());
    }

    private void setRateCards(ModulePriceHolder priceHolder)
    {
        if (this.isShutter() || priceHolder.getProductModule().isAccessoryUnit())
        {
            this.setFinish(priceHolder.getShutterFinish());
            this.setFinishRateCard(this.exposed == PanelExposed.DOUBLE ? priceHolder.getShutterDoubleExposedRateCard() : priceHolder.getShutterFinishRateCard());
        }
        else
        {
            this.setMaterialRateCard(priceHolder.getCarcassMaterialRateCard()).setFinish(priceHolder.getCarcassFinish());
            this.setFinishRateCard(this.exposed == PanelExposed.DOUBLE ? priceHolder.getCarcassDoubleExposedRateCard() : priceHolder.getCarcassFinishRateCard());
        }
    }

    public PanelComponent setQuantity(double quantity)
    {
        this.quantity = quantity;
        return this;
    }

    public PanelComponent setMaterialRateCard(RateCard materialRateCard)
    {
        this.materialRateCard = materialRateCard;
        return this;
    }

    public PanelComponent setFinishRateCard(RateCard finishRateCard)
    {
        this.finishRateCard = finishRateCard;
        return this;
    }

    public PanelComponent setFinish(ShutterFinish finish)
    {
        this.finish = finish;
        return this;
    }

    public PanelComponent setLength(int length)
    {
        this.length = length;
        return this;
    }

    public PanelComponent setBreadth(int breadth)
    {
        this.breadth = breadth;
        return this;
    }

    public PanelComponent setThickness(int thickness)
    {
        this.thickness = thickness;
        return this;
    }

    public PanelComponent setCode(String code)
    {
        this.code = code;
        return this;
    }

    public double getQuantity()
    {
        return quantity;
    }

    public RateCard getMaterialRateCard()
    {
        return materialRateCard;
    }

    public RateCard getFinishRateCard()
    {
        return finishRateCard;
    }

    public ShutterFinish getFinish()
    {
        return finish;
    }

    public int getLength()
    {
        return length;
    }

    public int getBreadth()
    {
        return breadth;
    }

    public int getThickness()
    {
        return thickness;
    }

    public String getCode()
    {
        return code;
    }

    public double getCost()
    {
        double unitCost = 0;
        if (this.exposed == PanelExposed.NONE)
        {
            unitCost = this.getMaterialCost();
        }
        else
        {
            unitCost = this.getFinishCost();
        }
        return this.quantity * unitCost; //quantity in panel_master is not to be used and can be dropped.
    }

    public double getMaterialCost()
    {
        if (this.materialRateCard == null) return 0;
        return this.getArea() * this.materialRateCard.getRateByThickness(this.getThickness());
    }

    public double getFinishCost()
    {
        if (this.finishRateCard == null) return 0;
        return this.getCuttingArea() * this.finishRateCard.getRateByThickness(this.getThickness());

    }

    public double getArea()
    {
        return this.getLength() * this.getBreadth() * SQMM2SQFT;
    }

    public double getCuttingArea()
    {
        return (this.getLength() - this.finish.getCuttingOffset()) * (this.getBreadth() - this.finish.getCuttingOffset()) * SQMM2SQFT;
    }

    public void setDimensions(ProductModule productModule, Module mgModule, ModulePanel modulePanel)
    {
        if (productModule.isAccessoryUnit())
        {
            this.setDimensionsForAccessoryUnit(productModule);
        }
        else if (mgModule.isCustomized())
        {
            this.setDimensionsForCustomizedModule(productModule, modulePanel);
        }
        else
        {
            this.setDimensionsForStandardModule(modulePanel);
        }
    }

    private void setDimensionsForStandardModule(ModulePanel modulePanel)
    {
        this.setLength(modulePanel.getLength()).setBreadth(modulePanel.getBreadth());
        this.setThickness(modulePanel.getThickness());
    }

    private void setDimensionsForCustomizedModule(ProductModule productModule, ModulePanel modulePanel)
    {
        if (this.isLeftPanel() || this.isRightPanel())
        {
            this.setLength(productModule.getDepth()).setBreadth(productModule.getHeight());
        }
        else if (this.isTopPanel() || this.isBottomPanel())
        {
            this.setLength(productModule.getDepth()).setBreadth(productModule.getWidth());
        }
        else
        {
            this.setLength(productModule.getHeight()).setBreadth(productModule.getWidth());
        }
        this.setThickness(modulePanel.getThickness());
    }

    private void setDimensionsForAccessoryUnit(ProductModule productModule)
    {
        this.setLength(productModule.getDepth()).setBreadth(productModule.getWidth());
        this.setThickness(ACCESSORY_PANEL_THICKNESS);
    }

    public void setExposed(ProductModule productModule)
    {
        this.exposed = PanelExposed.NONE;

        if (this.isShutter())
        {
            this.exposed = PanelExposed.SINGLE;
            return;
        }

        boolean exposedSide = ((productModule.isLeftExposed() && this.isLeftPanel()) ||
                (productModule.isRightExposed() && this.isRightPanel()) ||
                (productModule.isBottomExposed() && this.isBottomPanel()) ||
                (productModule.isTopExposed() && this.isTopPanel()) ||
                (productModule.isBackExposed() && this.isBackPanel()));

        if (exposedSide) this.exposed = PanelExposed.SINGLE;

        if (exposedSide && productModule.isOpenUnit()) this.exposed = PanelExposed.DOUBLE;

        if (!exposedSide && productModule.isOpenUnit()) this.exposed = PanelExposed.SINGLE;

    }

    public boolean isShutter()
    {
        return SHUTTER_TYPE.equals(this.getType());
    }

    public boolean isCarcass()
    {
        return CARCASS_TYPE.equals(this.getType());
    }

    public String getType()
    {
        return type;
    }

    public PanelComponent setType(String type)
    {
        this.type = type;
        return this;
    }

    public String getSide()
    {
        return side;
    }

    public PanelComponent setSide(String side)
    {
        this.side = side;
        return this;
    }


    public boolean isLeftPanel()
    {
        return TYPE_LEFT.equals(this.side);
    }

    public boolean isRightPanel()
    {
        return TYPE_RIGHT.equals(this.side);
    }

    public boolean isBottomPanel()
    {
        return TYPE_BOTTOM.equals(this.side);
    }

    public boolean isTopPanel()
    {
        return TYPE_TOP.equals(this.side);
    }

    public boolean isBackPanel()
    {
        return TYPE_BACK.equals(this.side);
    }

    public boolean isExposed()
    {
        return this.exposed != null && this.exposed != PanelExposed.NONE;
    }

}
