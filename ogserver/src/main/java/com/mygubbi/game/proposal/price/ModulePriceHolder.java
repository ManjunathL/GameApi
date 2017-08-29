package com.mygubbi.game.proposal.price;

import com.mygubbi.common.StringUtils;
import com.mygubbi.game.proposal.*;
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
    private static final String WARDROBE = "Wardrobe";
    private static final String NORMAL = "Normal";
    private static final String GOLA_PROFILE = "Gola Profile";
    private static final String CORNER_UNIT = "Corner";
    private static final String LOFTS = "Loft";


    private ProductModule productModule;
    private ProductLineItem productLineItem;
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
    private RateCard stdManufacturingCost;
    private RateCard nStdManufacturingCost;
    private RateCard carcassDoubleExposedRateCard;
    private RateCard shutterDoubleExposedRateCard;
    private RateCard loadingFactorCard;
    private RateCard labourRateCard;
    private RateCard labourManufacturingRateCard;
    private RateCard nonStandardloadingFactorCard;
    private RateCard loadingFactorBasedOnProduct;
    private RateCard stdLoadingSourceFactorBasedOnProduct;
    private RateCard nStdLoadingSourceFactorBasedOnProduct;
    private RateCard prodWoTaxFactor;

    private PriceMaster lWidthRate;
    private PriceMaster cWidthRate;
    private PriceMaster wWidthRate;
    private PriceMaster bracketRate;
    private PriceMaster cConnectorRate;
    private PriceMaster lConnectorRate;
    private PriceMaster gProfileRate;
    private PriceMaster jProfileRate;

    private Boolean finishValue =false;

    private double shutterCost = 0;
    private double shutterCostWoTax = 0;
    private double shutterSourceCost = 0;
    private double shutterProfit = 0;
    private double shutterMargin = 0;
    private double carcassCost = 0;
    private double carcassCostWoTax = 0;
    private double carcassSourceCost = 0;
    private double carcassProfit = 0;
    private double carcassMargin = 0;
    private double accessoryCost = 0;
    private double accessoryCostWoTax = 0;
    private double accessorySourceCost = 0;
    private double accessoryProfit = 0;
    private double accessoryMargin = 0;
    private double handleandKnobCost = 0;
    private double handleandKnobCostWoTax = 0;
    private double handleandKnobSourceCost = 0;
    private double handleandKnobProfit = 0;
    private double handleandKnobMargin = 0;
    private double hingeCost = 0.0;
    private double hingeCostWoTax = 0;
    private double hingeSourceCost = 0;
    private double hingeProfit = 0;
    private double hingeMargin = 0;
    private double hardwareCost = 0;
    private double hardwareCostWoTax = 0;
    private double hardwareSourceCost = 0;
    private double hardwareProfit = 0;
    private double hardwareMargin = 0;
    private double labourCost = 0;
    private double labourCostWoTax = 0;
    private double labourSourceCost = 0;
    private double labourProfit = 0;
    private double labourMargin = 0;
    private double totalCost = 0;
    private double totalCostWoTax = 0;
    private double totalSourceCost = 0;
    private double totalProfit = 0;
    private double totalMargin = 0;
    private double woodworkCost = 0;
    private double moduleArea;
    private int noOfAccPacks;

    private String moduleCode;
    private String moduleType;
   // private String handleType;

    private java.sql.Date priceDate;
    private String city;

    private JsonArray errors = null;


    public ModulePriceHolder()
    {

    }

    public ModulePriceHolder(ModuleForPrice moduleForPrice)
    {
        LOG.debug("Module For price :" + moduleForPrice);
        this.productModule = moduleForPrice.getModule();
        LOG.debug("This productModule : " + this.productModule);
        this.priceDate = moduleForPrice.getPriceDate();
        this.city = moduleForPrice.getCity();
        //this.handleType = moduleForPrice.getProduct().getHandletypeSelection();
        this.productLineItem = moduleForPrice.getProduct();
        LOG.debug("This City : " + this.city);
    }


    public ModulePriceHolder(ProductModule productModule, String city, java.sql.Date date,ProductLineItem productLineItem )
    {
        this.productModule = productModule;
        this.city = city;
        this.priceDate = date;
        this.productLineItem = productLineItem;
        LOG.debug("Inside 2nd Module price holder");


    }

    public Collection<ModuleComponent> getModuleComponents()
    {
        return this.moduleComponents;
    }


    public void prepare()
    {
        this.mgModule = ModuleDataService.getInstance().getModule(productModule.getMGCode());

        standardOrNonStandardModule();

        this.getModuleAndComponents();
        this.prepareRateCards();
        this.resolveComponents();
        this.resolveHandles();

    }

    private void standardOrNonStandardModule() {
        this.moduleCode =  ModuleDataService.getInstance().getModule(productModule.getMGCode()).getCode();

        if (moduleCode.startsWith("MG-NS"))
        {
            moduleType = "nonStandard";
        }
        else if (moduleCode.equals("MG-NS-H-001"))
        {
            moduleType ="hike";
        }
        else
        {
            moduleType = "Standard";
        }
    }

    private void resolveComponents()
    {
        this.panelComponents = new ArrayList<>();
        this.accessoryComponents = new ArrayList<>();
        this.hardwareComponents = new ArrayList<>();

        for (IModuleComponent component : this.getModuleComponents())
        {
            this.addComponent(component, null);
        }

        this.noOfAccPacks = this.getProductModule().getAccessoryPacks().size();

        for (ModuleAccessoryPack moduleAccessoryPack : this.getProductModule().getAccessoryPacks())
        {
            Collection<AccessoryPackComponent> accessoryPackComponents =
                    ModuleDataService.getInstance().getAccessoryPackComponents(moduleAccessoryPack.getAccessoryPackCode());
            for (IModuleComponent accessoryPackComponent : accessoryPackComponents)
            {
                this.addComponent(accessoryPackComponent, moduleAccessoryPack.getAccessoryPackCode());
            }
            for (String addonCode : moduleAccessoryPack.getAddons())
            {
                this.addAccessoryComponent(new ModuleComponent().setComponentCode(addonCode).setQuantity(1));
            }
        }
        for (HingePack hingePack : this.productModule.getHingePacks())
        {
            Handle hinge = ModuleDataService.getInstance().getHandleTitle(hingePack.getHingeCode());
            this.getHingeRateBasedOnQty(hingePack);
         //   this.productionSpecificationComponents.add(new Handle(hinge.getType(),hinge.getCode(),hinge.getMgCode(),hinge.getThickness(),hinge.getTitle()));
        }

//        if (!(this.productModule.getHingeCode() == null)) this.getHingeRate(this.productModule.getHingeCode(),this.productModule.getHingeQuantity());
       /* if (!(this.productModule.getHandleCode() == null)) this.getHandleOrKnobRate(this.productModule.getHandleCode(),this.productModule.getHandleQuantity());
        if (!(this.productModule.getKnobCode() == null)) this.getHandleOrKnobRate(this.productModule.getKnobCode(),this.productModule.getKnobQuantity());*/
    }

    private void resolveHandles()
    {
        String handletypeSelection = this.productLineItem.getHandletypeSelection();

        LOG.debug("Inisde resolve handles module :" + this.productModule.encodePrettily());

        if (handletypeSelection == null) return;

        if (Objects.equals(handletypeSelection,NORMAL ))
        {
            if (this.productModule.getHandleOverrideFlag()== null)
            {
                if (!(this.productLineItem.getHandleCode() == null))
                {
                    this.getHandleOrKnobRate(this.productLineItem.getHandleCode(),this.productModule.getHandleQuantity());
                    // Handle handle = ModuleDataService.getInstance().getHandleTitle(this.productModule.getHandleCode());
                    //  this.productionSpecificationComponents.add(new Handle(handle));
                }
            }
            else if (this.productModule.getHandleOverrideFlag().equals("Yes")) {
                {
                    this.getHandleOrKnobRate(this.productModule.getHandleCode(), this.productModule.getHandleQuantity());
                    // Handle handle = ModuleDataService.getInstance().getHandleTitle(this.productModule.getHandleCode());
                    //  this.productionSpecificationComponents.add(new Handle(handle));
                }
            }



            if (!(this.productLineItem.getKnobCode() == null)){
                this.getHandleOrKnobRate(this.productLineItem.getKnobCode(),this.productModule.getKnobQuantity());
                // Handle knob = ModuleDataService.getInstance().getHandleTitle(this.productModule.getKnobCode());
                //  this.productionSpecificationComponents.add(new Handle(knob));
            }
        }



        if (Objects.equals(handletypeSelection,GOLA_PROFILE ))
        {

            LOG.debug("Inside gola profile");


            if (!(this.productModule.getHandleCode() == null))
            {
                this.getHandleOrKnobRate(this.productLineItem.getHandleCode(),this.productModule.getHandleQuantity());
                // Handle handle = ModuleDataService.getInstance().getHandleTitle(this.productModule.getHandleCode());
                //  this.productionSpecificationComponents.add(new Handle(handle));
            }
            if (!(this.productModule.getKnobCode() == null)){
                this.getHandleOrKnobRate(this.productLineItem.getKnobCode(),this.productModule.getKnobQuantity());
                // Handle knob = ModuleDataService.getInstance().getHandleTitle(this.productModule.getKnobCode());
                //  this.productionSpecificationComponents.add(new Handle(knob));
            }
            int moduleCount = 0;
            int drawerModuleCount= 0;
            double wallProfileWidth = 0.0;
            double lProfileWidth = 0.0;
            double cProfileWidth = 0.0;
            //double golaProfileLength = Double.valueOf(productLineItem.getNoOfLengths().toString());
            double wProfilePrice ;
            double lProfilePrice ;
            double cProfilePrice ;
            double profilePrice ;
            double bracketPrice ;
            double lConnectorPrice ;
            double cConnectorPrice ;
            double golaProfilePrice ;

            double wProfileSourceCost;
            double lProfileSorceCost;
            double cProfileSourceCost;

            double profileSourceCost;
            double bracketSourceCost;
            double cConnectorSourceCost;

            double golaprofileSourceCost ;

            if (!(this.productModule.getModuleCategory().contains(LOFTS))) {
                         if (Objects.equals(this.productModule.getHandleMandatory(), "Yes")) {
                             moduleCount = moduleCount + 1;
                             if (this.productModule.getModuleCategory().contains("Wall")) {
                                 wallProfileWidth = wallProfileWidth + this.productModule.getWidth();
                             } else if (this.productModule.getModuleCategory().contains("Base")) {
                                 if (this.productModule.getModuleCategory().contains(CORNER_UNIT)) {
                                     lProfileWidth = lProfileWidth + (this.productModule.getWidth() / 2);
                                 } else if (this.productModule.getModuleCategory().contains("Drawer")) {
                                     drawerModuleCount = drawerModuleCount + 1;
                                     cProfileWidth = cProfileWidth + this.productModule.getWidth();
                                     lProfileWidth = lProfileWidth + this.productModule.getWidth();
                                 } else {
                                     lProfileWidth = lProfileWidth + this.productModule.getWidth();
                                 }
                             } else if (this.productModule.getModuleCategory().contains("Tall")) {
                                 lProfileWidth = lProfileWidth + this.productModule.getHeight();
                             } else {
                                 lProfileWidth = lProfileWidth + this.productModule.getWidth();

                             }
                         }
                     }

                wProfilePrice = wallProfileWidth/1000*wWidthRate.getPrice();
                lProfilePrice = lProfileWidth/1000*lWidthRate.getPrice();
                cProfilePrice = cProfileWidth/1000*cWidthRate.getPrice();

                profilePrice = wProfilePrice + lProfilePrice + cProfilePrice;
                bracketPrice = (moduleCount * 2) * this.bracketRate.getPrice();
//                lConnectorPrice = golaProfileLength * this.lConnectorRate.getPrice();
                cConnectorPrice = drawerModuleCount * this.cConnectorRate.getPrice();


                wProfileSourceCost = wallProfileWidth/1000 * wWidthRate.getSourcePrice();
                lProfileSorceCost = lProfileWidth/1000 * lWidthRate.getSourcePrice();
                cProfileSourceCost = cProfileWidth/1000 * cWidthRate.getSourcePrice();

                profileSourceCost = wProfileSourceCost + lProfileSorceCost + cProfileSourceCost;
                bracketSourceCost = (moduleCount * 2) * this.bracketRate.getSourcePrice();
                cConnectorSourceCost = drawerModuleCount * this.cConnectorRate.getSourcePrice();

                golaprofileSourceCost = profileSourceCost + bracketSourceCost + cConnectorSourceCost;

                handleandKnobSourceCost = golaprofileSourceCost;

                golaProfilePrice = profilePrice + bracketPrice  + cConnectorPrice;
                handleandKnobCost += golaProfilePrice;

                LOG.debug("Gola Profile Price: " + golaProfilePrice);

        }
        else if (Objects.equals(handletypeSelection, "G Profile")){

            LOG.debug("G Profile : ");

            double lWidth = 0;
            double gOrJProfileSourceCost;
            double gOrJProfilePrice = 0;
            double quantity = 0;
           // for (ProductModule module : this.productLineItem.getModules())
           // {
                LOG.debug("Module : " + this.productModule.toString());
                Collection<AccessoryPackComponent> handles = ModuleDataService.getInstance().getAccessoryPackComponents(this.productModule.getMGCode());
                for (AccessoryPackComponent accessoryPackComponent : handles)
                {
                    quantity = accessoryPackComponent.getQuantity();
                }
                if (Objects.equals(this.productModule.getHandleMandatory(), "Yes"))
                {
                    if (this.productModule.getModuleCategory().contains("Drawer"))
                    {
                        lWidth = lWidth + (quantity * this.productModule.getWidth());
                    }
                    else {
                        lWidth = lWidth + this.productModule.getWidth();
                    }
                }
           // }
                gOrJProfilePrice = lWidth/1000 * gProfileRate.getPrice();
            LOG.debug("G profile rate : " +  gProfileRate.getPrice());


            gOrJProfileSourceCost = lWidth/1000 * gProfileRate.getSourcePrice();
            handleandKnobSourceCost +=  gOrJProfileSourceCost;

            LOG.debug("Inside G profile : "+ gOrJProfilePrice);
                //LOG.debug("L width Rate :" + gProfileRate.getPrice());
                handleandKnobCost += gOrJProfilePrice;


        }
        else if (Objects.equals(handletypeSelection, "J Profile"))
        {
            LOG.debug("J profile : ");

            double lWidth = 0;
            double gOrJProfileSourceCost;
            double gOrJProfilePrice;
            double quantity = 0;
          //  for (ProductModule module : this.productLineItem.getModules())
          //  {
                LOG.debug("Module : " + this.productModule.toString());
                Collection<AccessoryPackComponent> handles = ModuleDataService.getInstance().getAccessoryPackComponents(this.productModule.getMGCode());
                for (AccessoryPackComponent accessoryPackComponent : handles)
                {
                    quantity = accessoryPackComponent.getQuantity();
                }
                if (Objects.equals(this.productModule.getHandleMandatory(), "Yes"))
                {
                    if (this.productModule.getModuleCategory().contains("Drawer"))
                    {
                        lWidth = lWidth + (quantity * this.productModule.getWidth());
                        LOG.debug("Inside if :" + lWidth);
                    }
                    else {
                        lWidth = lWidth + this.productModule.getWidth();
                        LOG.debug("Inside else :" + lWidth);
                    }
                }
           // }
            gOrJProfilePrice = lWidth/1000 * jProfileRate.getPrice();
            LOG.debug("J profile rate : " +  jProfileRate.getPrice());
            LOG.debug("Inside J profile : "+ gOrJProfilePrice);

            gOrJProfileSourceCost = lWidth/1000 * jProfileRate.getSourcePrice();
            handleandKnobSourceCost +=  gOrJProfileSourceCost;

            //LOG.debug("L width Rate :" + gProfileRate.getPrice());
            handleandKnobCost += gOrJProfilePrice;
        }

    }

    private void addComponent(IModuleComponent component, String accPackCode)
    {
        if (component.isCarcass() || component.isShutter())
        {
            this.addModulePanel(component, accPackCode);
        }
        else if (component.isHardware())
        {
            LOG.debug("hardware Components : " + component.toString());
            this.addHardwareComponent(component, accPackCode);
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
                this.addComponent(resolvedComponent, accPackCode);
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
            this.accessoryComponents.add(new AccessoryComponent(accessoryComponent, component,this.priceDate,this.city));
        }
    }

    private void addHardwareComponent(IModuleComponent component, String accPackCode)
    {
        AccHwComponent hardwareComponent = this.getHardwareComponent(component.getComponentCode());
        if (hardwareComponent != null)
        {
            this.hardwareComponents.add(new HardwareComponent(hardwareComponent, this.productModule, component, accPackCode, this.city,this.priceDate));
        }
    }

    private void addModulePanel(IModuleComponent component, String accPackCode)
    {
        ModulePanel modulePanel = this.getModulePanel(component);
        if (modulePanel != null)
        {
            this.panelComponents.add(new PanelComponent(this, modulePanel, component, accPackCode));
        }
    }

    private void getHandleOrKnobRate(String code,double quantity)
    {
        PriceMaster handleAndKnobCost = RateCardService.getInstance().getHandleOrKnobRate(code, this.priceDate, this.city);
        if (handleAndKnobCost!=null)
        {
            handleandKnobCost += handleAndKnobCost.getPrice() * quantity;
            handleandKnobSourceCost += handleAndKnobCost.getSourcePrice() * quantity;
            LOG.debug("Handle and Knob Cost : " + handleAndKnobCost.getPrice() + ":" + quantity);
        }


    }

    private void getHingeRateBasedOnQty(HingePack hingePack)
    {
        LOG.debug("Hinge Pack inside QTY : " + hingePack);

        double quantity = hingePack.getQUANTITY();

      if (Objects.equals(hingePack.getQtyFlag(), "C")) {
          LOG.debug("inisde 1st if" + hingePack);
          if (Objects.equals(hingePack.getQtyFormula(), "") || hingePack.getQtyFormula().isEmpty()) {
              LOG.debug("inisde 2nd if" + hingePack);
              String code = null;
              if (Objects.equals(hingePack.getTYPE(), "Soft Close"))
              {
                  LOG.debug("inisde soft close" + hingePack);
                  if (productModule.getDepth() < 350)
                      code = "HINGE05";
                  else if (productModule.getDepth() < 400)
                      code = "HINGE04";
                  else if (productModule.getDepth() < 450)
                      code = "HINGE02";
                  else if (productModule.getDepth() < 500)
                      code = "HINGE03";
                  else if (productModule.getDepth() < 650)
                      code = "HINGE06";
              }
              else
              {
                  LOG.debug("inisde non soft close" + hingePack);

                  if (productModule.getDepth() < 350)
                      code = "HINGE11";
                  else if (productModule.getDepth() < 400)
                      code = "HINGE10";
                  else if (productModule.getDepth() < 450)
                      code = "HINGE08";
                  else if (productModule.getDepth() < 500)
                      code = "HINGE09";
                  else if (productModule.getDepth() < 650)
                      code = "HINGE12";
              }


              LOG.debug("code" + code);

              PriceMaster hingeCostPriceMaster = RateCardService.getInstance().getHingeRate(code, this.priceDate, this.city);
              hingeCost += hingeCostPriceMaster.getPrice() * quantity;
              hingeSourceCost += hingeCostPriceMaster.getSourcePrice() * quantity;

          } else {

              LOG.debug("Hinge Pack inside C : " + hingePack);
              if (Objects.equals(hingePack.getQtyFormula(), "F6")) {
                  int value1 = (productModule.getHeight() > 2100) ? 5 : 4;
                  int value2 = (productModule.getWidth() > 600) ? 2 : 1;
                  quantity = value1 * value2;
              } else if (Objects.equals(hingePack.getQtyFormula(), "F12")) {
                  quantity = (productModule.getWidth() >= 601) ? 4 : 2;
              }
              PriceMaster hingeCostPriceMaster = RateCardService.getInstance().getHingeRate(hingePack.getHingeCode(), this.priceDate, this.city);
              hingeCost += hingeCostPriceMaster.getPrice() * quantity;
              hingeSourceCost += hingeCostPriceMaster.getSourcePrice() * quantity;
          }
      }
        else {
            PriceMaster hingeCostPriceMaster = RateCardService.getInstance().getHingeRate(hingePack.getHingeCode(),this.priceDate,this.city);
            hingeCost += hingeCostPriceMaster.getPrice() * quantity;
            hingeSourceCost += hingeCostPriceMaster.getSourcePrice() * quantity;
        }
    }

    private void getHingeRate(String code,double quantity)
    {
        PriceMaster handleAndKnobCost = RateCardService.getInstance().getHingeRate(code, this.priceDate, this.city);

        handleandKnobCost += handleAndKnobCost.getPrice() * quantity;
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
        this.carcassMaterialRateCard = RateCardService.getInstance().getRateCard(carcassCode, RateCard.CARCASS_TYPE,this.priceDate, this.city);

        this.carcassFinishRateCard = RateCardService.getInstance().getRateCard(carcassFinish.getCostCode(), RateCard.SHUTTER_TYPE,this.priceDate, this.city);
        this.shutterFinishRateCard = RateCardService.getInstance().getRateCard(shutterFinish.getCostCode(), RateCard.SHUTTER_TYPE,this.priceDate, this.city);
        this.stdManufacturingCost = RateCardService.getInstance().getRateCard(RateCard.STD_MANUFACTURING_COST_FACTOR,RateCard.FACTOR_TYPE ,this.priceDate, this.city);
        this.nStdManufacturingCost = RateCardService.getInstance().getRateCard(RateCard.NONSTD_MANUFACTURING_COST_FACTOR, RateCard.FACTOR_TYPE,this.priceDate, this.city);
//        this.nStdManufacturingCostWardrobe = RateCardService.getInstance().getRateCard(RateCard.STD_MANUFACTURING_COST_FACTOR_WARDROBE, RateCard.FACTOR_TYPE,this.priceDate, this.city);
//        this.nStdManufacturingCostShoeRack = RateCardService.getInstance().getRateCard(RateCard.STD_MANUFACTURING_COST_FACTOR_SHOERACK, RateCard.FACTOR_TYPE,this.priceDate, this.city);
//        this.nStdManufacturingCostStudyTable = RateCardService.getInstance().getRateCard(RateCard.STD_MANUFACTURING_COST_FACTOR_STUDYTABLE, RateCard.FACTOR_TYPE,this.priceDate, this.city);

        this.carcassDoubleExposedRateCard = RateCardService.getInstance().getRateCard(carcassFinish.getDoubleExposedCostCode(),
                RateCard.SHUTTER_TYPE,this.priceDate, this.city);
        this.shutterDoubleExposedRateCard = RateCardService.getInstance().getRateCard(shutterFinish.getDoubleExposedCostCode(),
                RateCard.SHUTTER_TYPE,this.priceDate, this.city);

        this.loadingFactorCard = RateCardService.getInstance().getRateCard(RateCard.LOADING_FACTOR, RateCard.FACTOR_TYPE,this.priceDate, this.city);
        this.labourRateCard = RateCardService.getInstance().getRateCard(RateCard.LABOUR_FACTOR, RateCard.FACTOR_TYPE,this.priceDate, this.city);
        this.labourManufacturingRateCard = RateCardService.getInstance().getRateCard(RateCard.LABOUR_COST_FACTOR, RateCard.FACTOR_TYPE,this.priceDate, this.city);
        this.nonStandardloadingFactorCard = RateCardService.getInstance().getRateCard(RateCard.LOADING_FACTOR_NONSTANDARD,
                RateCard.FACTOR_TYPE,this.priceDate, this.city);
        LOG.debug("this.nonstandard" + this.nonStandardloadingFactorCard.getRate());
        this.loadingFactorBasedOnProduct = RateCardService.getInstance().getRateCardBasedOnProduct(RateCard.LOADING_FACTOR,
                RateCard.FACTOR_TYPE,this.priceDate, this.city,this.productModule.getProductCategory());
        LOG.debug("loadingFactorBasedOnProduct :" + this.loadingFactorBasedOnProduct.getSourcePriceBasedOnProduct() + " : " + this.loadingFactorBasedOnProduct.getSourcePrice());
        this.stdLoadingSourceFactorBasedOnProduct = RateCardService.getInstance().getRateCardBasedOnProduct(RateCard.STD_MANUFACTURING_COST_FACTOR,
                RateCard.FACTOR_TYPE,this.priceDate, this.city,this.productModule.getProductCategory());
        LOG.debug("stdLoadingSourceFactorBasedOnProduct :" + this.stdLoadingSourceFactorBasedOnProduct.getSourcePriceBasedOnProduct() + " : " + this.stdLoadingSourceFactorBasedOnProduct.getSourcePrice());
        this.nStdLoadingSourceFactorBasedOnProduct = RateCardService.getInstance().getRateCardBasedOnProduct(RateCard.NONSTD_MANUFACTURING_COST_FACTOR,
                RateCard.FACTOR_TYPE,this.priceDate, this.city,this.productModule.getProductCategory());
        LOG.debug("nStdLoadingSourceFactorBasedOnProduct :" + this.nStdLoadingSourceFactorBasedOnProduct.getSourcePriceBasedOnProduct() + " : " + this.nStdLoadingSourceFactorBasedOnProduct.getSourcePrice());
        this.prodWoTaxFactor = RateCardService.getInstance().getRateCard(RateCard.PRODUCT_WO_TAX,
                RateCard.FACTOR_TYPE,this.priceDate, this.city);

        //Profile Handles

         this.lWidthRate = RateCardService.getInstance().getHardwareRate("H073", priceDate, city);
        this.cWidthRate = RateCardService.getInstance().getHardwareRate("H071", priceDate, city);
        this.wWidthRate = RateCardService.getInstance().getHardwareRate("H076", priceDate, city);
        this.bracketRate = RateCardService.getInstance().getHardwareRate("H075", priceDate, city);
        this.lConnectorRate = RateCardService.getInstance().getHardwareRate("H074", priceDate, city);
        this.cConnectorRate = RateCardService.getInstance().getHardwareRate("H072", priceDate, city);
        this.gProfileRate = RateCardService.getInstance().getHardwareRate("H018", priceDate, city);
        LOG.debug("G profile Rate : " + this.gProfileRate);
        this.jProfileRate = RateCardService.getInstance().getHardwareRate("H077", priceDate, city);
        LOG.debug("J profile Rate : " + this.jProfileRate);




        if (carcassMaterialRateCard == null || carcassFinishRateCard == null || shutterFinishRateCard == null
                || loadingFactorCard == null || labourRateCard == null || nonStandardloadingFactorCard == null || loadingFactorBasedOnProduct == null)
        {
            this.addError("Carcass, Carcass Finish, Shutter, Labour or Loading factor rate cards not setup." + carcassCode + " : "
                    + productModule.getFinishCode() + " : " + shutterFinish.getCostCode());
        }
    }

    private void getModuleAndComponents()
    {
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
                        .put("carcassSourceCost", this.round(this.carcassSourceCost, 2))
                        .put("shutterCost", this.round(this.shutterCost, 2))
                        .put("shutterSourceCost", this.round(this.shutterSourceCost, 2))
                        .put("accessoryCost", this.round(this.accessoryCost, 2))
                        .put("accessorySourceCost", this.round(this.accessorySourceCost, 2))
                        .put("handleAndKnobCost", this.round(this.handleandKnobCost, 2))
                        .put("handleAndKnobSourceCost", this.round(this.handleandKnobSourceCost, 2))
                        .put("hingeCost", this.round(this.hingeCost, 2))
                        .put("hingeSourceCost", this.round(this.hingeSourceCost, 2))
                        .put("labourCost", this.round(this.labourCost, 2))
                        .put("labourSourceCost", this.round(this.labourSourceCost, 2))
                        .put("hardwareCost", this.round(this.hardwareCost, 2))
                        .put("hardwareSourceCost", this.round(this.hardwareSourceCost, 2))
                        .put("totalCost", this.round(this.totalCost, 2));


    }

    public void addToCarcassCost(double cost)
    {
        if (cost == 0) return;
        this.carcassCost += cost;
    }
    public void addToCarcassSourceCost(double sourceCost)
    {
        if (sourceCost == 0) return;
        this.carcassSourceCost += sourceCost;
    }

    public void addToShutterCost(double cost)
    {
        if (cost == 0) return;
        this.shutterCost += cost;
    }

    public void addToShutterSourceCost(double sourceCost)
    {
        if (sourceCost == 0) return;
        this.shutterSourceCost += sourceCost;
    }

    public void addToAccessoryCost(double cost)
    {
        this.accessoryCost += cost;
    }

    public void addToAccessorySourcePrice(double sourceCost)
    {
        this.accessorySourceCost += sourceCost;
    }

    public void addToHardwareCost(double cost)
    {
        this.hardwareCost += cost;
    }

    public void addToHardwareSourceCost(double hardwareSourceCost)
    {
        this.hardwareSourceCost += hardwareSourceCost;
    }

    public void calculateTotalCost()
    {
        this.shutterCost = 0;
        this.carcassCost = 0;
        this.accessoryCost = 0;
        this.accessorySourceCost = 0;
        this.hardwareSourceCost = 0;
        this.totalCost = 0;
        this.woodworkCost = 0;
        this.moduleArea = 0;

        for (PanelComponent panel : this.getPanelComponents())
        {
            double rate = this.loadingFactorBasedOnProduct.getRateBasedOnProduct();
            double stdSourceRate = this.stdLoadingSourceFactorBasedOnProduct.getSourcePrice();
            double nStdSourceRate = this.nStdLoadingSourceFactorBasedOnProduct.getSourcePriceBasedOnProduct();
            LOG.debug("NON std source rate : " + nStdSourceRate);
            if (panel.isExposed())
            {
                if ("Standard".equals(moduleType))
                {
                    if (Objects.equals(WARDROBE, this.productModule.getProductCategory()) || Objects.equals("W", this.productModule.getProductCategory()))
                    {
                        LOG.debug("Inside Wardrobe If clause shutter" + panel.getCost() + ":" + rate);
                        LOG.debug("Rate : " + rate);
                        this.addToShutterCost(panel.getCost() * rate);
                        this.addToShutterSourceCost(panel.getCost() / stdSourceRate);

                    }
                    else {
                        this.addToShutterCost(panel.getCost());
                        this.addToShutterSourceCost(panel.getCost() / stdManufacturingCost.getSourcePrice());
                    }
                    if(panel.getCost()==0.0)
                    {
                        //this.addToShutterCost(0.0);
                        finishValue =true;
                        return;
                    }
                }
                else
                {
                    if(Objects.equals("shoerack", this.productModule.getProductCategory()) || Objects.equals("studytable", this.productModule.getProductCategory()) || Objects.equals("crunit", this.productModule.getProductCategory()))
                    {
                        LOG.info("shoerack value 1 " +rate);
                        this.addToShutterCost(panel.getCost() * rate);
                        this.addToShutterSourceCost((panel.getCost() * rate) / nStdSourceRate);
                    }
                    else
                    {
                        this.addToShutterCost(panel.getCost() * this.nonStandardloadingFactorCard.getRate());
                        this.addToShutterSourceCost((panel.getCost() * this.nonStandardloadingFactorCard.getRate())/ nStdSourceRate);
                    }
                    if(panel.getCost()==0.0)
                    {
                        //this.addToShutterCost(0.0);
                        finishValue =true;
                        return;
                    }

                }
            }
            else
            {
                if ("Standard".equals(moduleType))
                {
                    if (Objects.equals(WARDROBE, this.productModule.getProductCategory()) || Objects.equals("W", this.productModule.getProductCategory()))
                    {
                        LOG.debug("Inside Wardrobe If clause Carcass" + ":" + panel.getCost() + ":" + rate );

                        this.addToCarcassCost(panel.getCost() * rate);
                        this.addToCarcassSourceCost(panel.getCost() / stdSourceRate);
                    }
                    else {
                        this.addToCarcassCost(panel.getCost());
                        this.addToCarcassSourceCost(panel.getCost() / stdManufacturingCost.getSourcePrice());
                    }
                }
                else if ("hike".equals(moduleType))
                {
                    this.addToCarcassCost(panel.getCost());
                    this.addToCarcassSourceCost(panel.getCost() / stdManufacturingCost.getSourcePrice());
                }
                else
                {
                    if(Objects.equals("shoerack", this.productModule.getProductCategory()) || Objects.equals("studytable", this.productModule.getProductCategory()) || Objects.equals("crunit", this.productModule.getProductCategory()) )
                    {
                        LOG.info("Shoerack value in else 2 " +rate);
                        this.addToCarcassCost(panel.getCost() * rate);
                        this.addToCarcassSourceCost((panel.getCost() * rate) / nStdSourceRate);
                    }
                    else
                    {
                        this.addToCarcassCost(panel.getCost() * this.nonStandardloadingFactorCard.getRate());
                        this.addToCarcassSourceCost((panel.getCost() * this.nonStandardloadingFactorCard.getRate()) / nStdManufacturingCost.getSourcePrice());
                    }
                }
            }
        }

        for (AccessoryComponent accessory : this.accessoryComponents)
        {
            this.addToAccessoryCost(accessory.getCost());
            this.addToAccessorySourcePrice(accessory.getTotalSourcePrice());
        }

        for (HardwareComponent hardware : this.hardwareComponents)
        {
            this.addToHardwareCost(hardware.getCost());
            this.addToHardwareSourceCost(hardware.getTotalSourcePrice());
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


        if(true==equals(finishValue))
        {
            this.totalCost=0.0;
        }else {
            this.labourCost = this.moduleArea * labourRateCard.getRate();
            this.labourSourceCost = this.labourCost / labourManufacturingRateCard.getSourcePrice();

            this.carcassCostWoTax = this.carcassCost * this.prodWoTaxFactor.getSourcePrice();
            this.shutterCostWoTax = this.shutterCost * this.prodWoTaxFactor.getSourcePrice();
            LOG.debug("Module Price Holder :" + labourCost + " : " + this.prodWoTaxFactor.getSourcePrice());
            this.labourCostWoTax = labourCost * this.prodWoTaxFactor.getSourcePrice();
            this.hardwareCostWoTax = this.hardwareCost * this.prodWoTaxFactor.getSourcePrice();
            this.handleandKnobCostWoTax = this.handleandKnobCost * this.prodWoTaxFactor.getSourcePrice();
            this.hingeCostWoTax = this.hingeCost * this.prodWoTaxFactor.getSourcePrice();
            this.accessoryCostWoTax = this.accessoryCost * this.prodWoTaxFactor.getSourcePrice();

            this.carcassProfit = this.carcassCostWoTax - this.carcassSourceCost;
            this.shutterProfit = this.shutterCostWoTax - this.shutterSourceCost;
            this.labourProfit = this.labourCostWoTax - this.labourSourceCost;
            this.hardwareProfit = this.hardwareCostWoTax - this.hardwareSourceCost;
            this.handleandKnobProfit = this.handleandKnobCostWoTax - this.handleandKnobSourceCost;
            this.hingeProfit = this.hingeCostWoTax - this.hingeSourceCost;

            if (this.carcassProfit == 0 || this.carcassCostWoTax == 0)
            {
                this.carcassMargin = 0;
            }
            else {

                this.carcassMargin = (this.carcassProfit / this.carcassCostWoTax) * 100;
            }
            if (this.shutterProfit == 0 || this.shutterCostWoTax == 0)
            {
                this.shutterMargin = 0;
            }
            else {

                this.shutterMargin = (this.shutterProfit / this.shutterCostWoTax) * 100;
            }
            if (!(this.labourProfit == 0 || this.labourCostWoTax == 0))this.labourMargin = (this.labourProfit / this.labourCostWoTax)*100;
            if (this.hardwareProfit == 0 || this.hardwareCostWoTax == 0)
            {
                this.hardwareMargin = 0;
            }
            else {

                this.hardwareMargin = (this.hardwareProfit / this.hardwareCostWoTax) * 100;
            }

            if (this.handleandKnobProfit == 0 || this.handleandKnobCostWoTax == 0)
            {
                this.handleandKnobMargin = 0;
            }
            else
            {
                this.handleandKnobMargin = (this.handleandKnobProfit / this.handleandKnobCostWoTax)*100;
            }

            if (this.hingeProfit == 0 || this.hingeCostWoTax == 0)
            {
                this.hingeMargin = 0;
            }
            else
            {
                this.hingeMargin = (this.hingeProfit / this.hingeCostWoTax)*100;
            }




            this.woodworkCost = (this.carcassCost + this.shutterCost + this.labourCost) * loadingFactorCard.getRate() + this.handleandKnobCost + this.hingeCost + this.hardwareCost;
            this.totalCost = this.woodworkCost + this.accessoryCost ;
            this.totalCostWoTax = this.totalCost * this.prodWoTaxFactor.getSourcePrice();
            if (mgModule.getModuleCategory().startsWith("H"))
            {
                this.totalSourceCost = this.totalCost * 0.6;
            }
            else
            {
                this.totalSourceCost = this.carcassSourceCost + this.shutterSourceCost + this.labourSourceCost + this.handleandKnobSourceCost + this.hingeSourceCost + this.hardwareSourceCost + this.accessorySourceCost;
            }
            this.totalProfit = this.totalCostWoTax - this.totalSourceCost;
            this.totalMargin = (this.totalProfit / this.totalCostWoTax)*100;


        }
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

    public double getTotalCost() {
        return totalCost;
    }

    public double getCostWoAccessories()
    {
        return totalCost-accessoryCost;
    }

    public double getShutterCost()
    {
        return shutterCost;
    }

    public double getShutterCostWoTax()
    {
        return shutterCostWoTax;
    }

    public double getShutterSourceCost()
    {
        return shutterSourceCost;
    }

    public double getShutterProfit()
    {
        return shutterProfit;
    }

    public double getShutterMargin()
    {
        return shutterMargin;
    }


     public double getCarcassCost()
    {
        return carcassCost;
    }

    public double getCarcassCostWoTax()
    {
        return carcassCostWoTax;
    }

    public double getCarcassSourceCost()
    {
        return carcassSourceCost;
    }

    public double getCarcassProfit()
    {
        return carcassProfit;
    }

    public double getCarcassMargin()
    {
        return carcassMargin;
    }

    public double getHandleandKnobCost()
    {
        return handleandKnobCost;
    }

    public double getHandleandKnobCostWoTax()
    {
        return handleandKnobCostWoTax;
    }

    public double getHandleandKnobSourceCost()
    {
        return handleandKnobSourceCost;
    }

    public double getHandleandKnobProfit()
    {
        return handleandKnobProfit;
    }

    public double getHandleandKnobMargin()
    {
        return handleandKnobMargin;
    }

    public double getHingeCost()
    {
        return hingeCost;
    }

    public double getHingeCostWoTax()
    {
        return hingeCostWoTax;
    }

    public double getHingeSourceCost()
    {
        return hingeSourceCost;
    }

    public double getHingeProfit()
    {
        return hingeProfit;
    }

    public double getHingeMargin()
    {
        return hingeMargin;
    }

    public double getHardwareCost()
    {
        return hardwareCost;
    }

    public double getHardwareCostWoTax()
    {
        return hardwareCostWoTax;
    }

    public double getHardwareSourceCost()
    {
        return hardwareSourceCost;
    }

    public double getHardwareProfit()
    {
        return hardwareProfit;
    }

    public double getHardwareMargin()
    {
        return hardwareMargin;
    }

    public double getAccessoryCost()
    {
        return accessoryCost;
    }

    public double getAccessoryCostWoTax()
    {
        return accessoryCostWoTax;
    }

    public double getAccessorySourceCost()
    {
        return accessorySourceCost;
    }

    public double getAccessoryProfit()
    {
        return accessoryProfit;
    }

    public double getAccessoryMargin()
    {
        return accessoryMargin;
    }

    public double getLabourCost()
    {
        return labourCost;
    }

    public double getLabourCostWoTax()
    {
        return labourCostWoTax;
    }

    public double getLabourSourceCost()
    {
        return labourSourceCost;
    }

    public double getLabourProfit()
    {
        return labourProfit;
    }

    public double getLabourMargin()
    {
        return labourMargin;
    }


    public double getTotalCostWoTax()
    {
        return totalCostWoTax;
    }

    public double getTotalSourceCost()
    {
        return totalSourceCost;
    }

    public double getTotalProfit()
    {
        return totalProfit;
    }

    public double getTotalMargin()
    {
        return totalMargin;
    }


    public int getNoOfAccPacks() {
        return this.noOfAccPacks;
    }

    @Override
    public String toString() {
        return "ModulePriceHolder{" +
                "productModule=" + productModule +
                ", productLineItem=" + productLineItem +
                ", mgModule=" + mgModule +
                ", moduleComponents=" + moduleComponents +
                ", panelComponents=" + panelComponents +
                ", accessoryComponents=" + accessoryComponents +
                ", hardwareComponents=" + hardwareComponents +
                ", shutterFinish=" + shutterFinish +
                ", carcassFinish=" + carcassFinish +
                ", carcassMaterialRateCard=" + carcassMaterialRateCard +
                ", carcassFinishRateCard=" + carcassFinishRateCard +
                ", shutterFinishRateCard=" + shutterFinishRateCard +
                ", stdManufacturingCost=" + stdManufacturingCost +
                ", nStdManufacturingCost=" + nStdManufacturingCost +
                ", carcassDoubleExposedRateCard=" + carcassDoubleExposedRateCard +
                ", shutterDoubleExposedRateCard=" + shutterDoubleExposedRateCard +
                ", loadingFactorCard=" + loadingFactorCard +
                ", labourRateCard=" + labourRateCard +
                ", labourManufacturingRateCard=" + labourManufacturingRateCard +
                ", nonStandardloadingFactorCard=" + nonStandardloadingFactorCard +
                ", loadingFactorBasedOnProduct=" + loadingFactorBasedOnProduct +
                ", stdLoadingSourceFactorBasedOnProduct=" + stdLoadingSourceFactorBasedOnProduct +
                ", nStdLoadingSourceFactorBasedOnProduct=" + nStdLoadingSourceFactorBasedOnProduct +
                ", prodWoTaxFactor=" + prodWoTaxFactor +
                ", lWidthRate=" + lWidthRate +
                ", cWidthRate=" + cWidthRate +
                ", wWidthRate=" + wWidthRate +
                ", bracketRate=" + bracketRate +
                ", cConnectorRate=" + cConnectorRate +
                ", lConnectorRate=" + lConnectorRate +
                ", gProfileRate=" + gProfileRate +
                ", jProfileRate=" + jProfileRate +
                ", finishValue=" + finishValue +
                ", shutterCost=" + shutterCost +
                ", shutterCostWoTax=" + shutterCostWoTax +
                ", shutterSourceCost=" + shutterSourceCost +
                ", shutterProfit=" + shutterProfit +
                ", shutterMargin=" + shutterMargin +
                ", carcassCost=" + carcassCost +
                ", carcassCostWoTax=" + carcassCostWoTax +
                ", carcassSourceCost=" + carcassSourceCost +
                ", carcassProfit=" + carcassProfit +
                ", carcassMargin=" + carcassMargin +
                ", accessoryCost=" + accessoryCost +
                ", accessoryCostWoTax=" + accessoryCostWoTax +
                ", accessorySourceCost=" + accessorySourceCost +
                ", accessoryProfit=" + accessoryProfit +
                ", accessoryMargin=" + accessoryMargin +
                ", handleandKnobCost=" + handleandKnobCost +
                ", handleandKnobCostWoTax=" + handleandKnobCostWoTax +
                ", handleandKnobSourceCost=" + handleandKnobSourceCost +
                ", handleandKnobProfit=" + handleandKnobProfit +
                ", handleandKnobMargin=" + handleandKnobMargin +
                ", hingeCost=" + hingeCost +
                ", hingeCostWoTax=" + hingeCostWoTax +
                ", hingeSourceCost=" + hingeSourceCost +
                ", hingeProfit=" + hingeProfit +
                ", hingeMargin=" + hingeMargin +
                ", hardwareCost=" + hardwareCost +
                ", hardwareCostWoTax=" + hardwareCostWoTax +
                ", hardwareSourceCost=" + hardwareSourceCost +
                ", hardwareProfit=" + hardwareProfit +
                ", hardwareMargin=" + hardwareMargin +
                ", labourCost=" + labourCost +
                ", labourCostWoTax=" + labourCostWoTax +
                ", labourSourceCost=" + labourSourceCost +
                ", labourProfit=" + labourProfit +
                ", labourMargin=" + labourMargin +
                ", totalCost=" + totalCost +
                ", totalCostWoTax=" + totalCostWoTax +
                ", totalSourceCost=" + totalSourceCost +
                ", totalProfit=" + totalProfit +
                ", totalMargin=" + totalMargin +
                ", woodworkCost=" + woodworkCost +
                ", moduleArea=" + moduleArea +
                ", noOfAccPacks=" + noOfAccPacks +
                ", moduleCode='" + moduleCode + '\'' +
                ", moduleType='" + moduleType + '\'' +
                ", priceDate=" + priceDate +
                ", city='" + city + '\'' +
                ", errors=" + errors +
                '}';
    }
}
