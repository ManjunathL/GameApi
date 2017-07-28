package com.mygubbi.game.proposal.quote;

import com.mygubbi.common.StringUtils;
import com.mygubbi.game.proposal.*;
import com.mygubbi.game.proposal.model.*;
import com.mygubbi.game.proposal.price.ModulePriceHolder;
import com.mygubbi.game.proposal.price.PanelComponent;
import com.mygubbi.game.proposal.price.RateCardService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.lambda.Seq;
import org.jooq.lambda.tuple.Tuple;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.round;
import static org.jooq.lambda.tuple.Tuple.tuple;

/**
 * Created by Sunil on 22-05-2016.
 */
public class AssembledProductInQuote
{
    private final static Logger LOG = LogManager.getLogger(AssembledProductInQuote.class);

    private ProductLineItem product;
    private List<ModulePriceHolder> priceHolders = Collections.EMPTY_LIST;

    private List<Accessory> accessories;
    private List<Unit> units;

    private List<ModulePart> modules;
    private List<ModulePart> accessoryPackPanels;
    private List<ModulePart> moduleAccessories;
    private List<ModulePart> moduleHardware;
    private List<ModulePart> addonAccessories;
    private List<ModulePart> addons;
    private List<ModulePart> handleandKnob;
    private List<ModulePart> hingePack;


    private List<PanelComponent> panels;

    private double addonsAmount;
    private String shutterMaterial;
    private String city;
    private java.sql.Date priceDate;
    private String hingeTitle;

    public AssembledProductInQuote(ProductLineItem product, String city, Date date)
    {
        this.city = city;
        this.priceDate = date;
        this.product = product;
        this.prepare();
    }

    public Date getPriceDate()
    {
        return this.priceDate;
    }
    public String getCity()
    {
        return this.city;
    }

    public String getTitle()
    {
        return this.product.getTitle();
    }

    public String getSpaceType()
    {
        return this.product.getSpaceType();
    }

    public String getRoom()
    {
        return this.product.getRoomCode();
    }

    public String getCatagoryName()
    {
        return this.product.getProductCategory();
    }

    public String getGlass(){ return this.product.getGlass();}

    public List<ModulePriceHolder> getPriceHolders()
    {
        return this.priceHolders;
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

    public List<ModulePart> getModuleAccessories()
    {
        return this.moduleAccessories;
    }

    public List<ModulePart> getAggregatedModules()
    {
        return this.getAggregatedModuleParts(this.modules);
    }

    public List<ModulePart> getAggregatedModuleHardware()
    {
        return this.getAggregatedModuleParts(this.modules);
    }

    public List<ModulePart> getAggregatedAccessoryPackPanels()
    {
        return this.getAggregatedModuleParts(this.accessoryPackPanels);
    }

    public List<ModulePart> getAggregatedAccessoryPackAccessories()
    {
        return this.getAggregatedModuleParts(this.moduleAccessories);
    }

    public List<ModulePart> getAggregatedAccessoryPackHardware()
    {
        return this.getAggregatedModuleParts(this.moduleHardware);
    }


    public List<ModulePart> getAggregatedKnobAndHandle()
    {
        return this.getAggregatedModuleParts(this.handleandKnob);
    }

    public List<ModulePart> getAggregatedHingePack()
    {
        return this.getAggregatedModuleParts(this.hingePack);
    }

    public List<ModulePart> getAggregatedAccessoryAddons()
    {
        return this.getAggregatedModuleParts(this.addonAccessories);
    }

    public List<ModulePart> getAggregatedAddons()
    {
        return this.getAggregatedModuleParts(this.addons);
    }

    private List<ModulePart> getAggregatedModuleParts(List<ModulePart> parts)
    {
        return Seq.ofType(parts.stream(), ModulePart.class)
                .groupBy(x -> tuple(x.code, x.uom,x.title,x.catalogCode,x.ERPCode),
                        Tuple.collectors(
                                Collectors.summingDouble(x -> x.quantity), Collectors.summingDouble(x -> x.quantity)
                        )
                )
                .entrySet()
                .stream()
                .map(e -> new ModulePart(e.getKey().v1, e.getKey().v2, e.getValue().v1,e.getKey().v3,e.getKey().v4,e.getKey().v5))
                .collect(Collectors.toList());
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
        this.priceHolders = new ArrayList<>();
        this.units = new ArrayList<>();
        this.accessories = new ArrayList<>();
        this.moduleAccessories = new ArrayList<>();
        this.moduleHardware = new ArrayList<>();
        this.accessoryPackPanels = new ArrayList<>();
        this.addonAccessories = new ArrayList<>();
        this.addons = new ArrayList<>();
        this.modules = new ArrayList<>();
        this.handleandKnob = new ArrayList<>();
        this.hingePack = new ArrayList<>();

        for (ProductModule module : this.product.getModules())
        {
            ModulePriceHolder priceHolder = new ModulePriceHolder(module,this.city,this.priceDate,this.product);
            priceHolder.prepare();
            if (priceHolder.hasErrors())
            {
                throw new RuntimeException("Error in preparing product for module : " + module.getMGCode() + " in product " + this.product.getId());
            }
            this.priceHolders.add(priceHolder);

            this.modules.add(new ModulePart(module.getMGCode(), "NOS", 1));
            this.addModuleToUnit(module);
            this.collectModuleParts(module);
            this.collectModuleComponents(module);
            this.collectModuleHandles(module);
            this.collectModuleKnob(module);
            this.collectModuleHinge(module);
        }
        for (ProductAddon addon : this.product.getAddons())
        {
            this.addonsAmount += addon.getAmount();
            this.addToAddons(addon, 1, "NA", 0);
        }

        ShutterFinish finish = ModuleDataService.getInstance().getFinish(this.product.getFinishCode());
        this.shutterMaterial = finish.getTitle() + " | " + finish.getFinishMaterial();
    }

    private void collectModuleParts(ProductModule module)
    {
        for (ModuleAccessoryPack modAccessoryPack : module.getAccessoryPacks())
        {
            Collection<AccessoryPackComponent> accessoryPackComponents =
                    ModuleDataService.getInstance().getAccessoryPackComponents(modAccessoryPack.getAccessoryPackCode());
            if (accessoryPackComponents == null ) continue;
            for (AccessoryPackComponent accessoryPackComponent : accessoryPackComponents)
            {
                if (accessoryPackComponent.isAccessory())
                {
                    AccHwComponent accessory = ModuleDataService.getInstance().getAccessory(accessoryPackComponent.getComponentCode());
                    if (accessory == null) continue;
                    this.addToProductAccessories(accessory, accessoryPackComponent.getQuantity());
                    this.addToModuleAccessories(accessory, accessoryPackComponent.getQuantity(), module.getUnit(), module.getSequence());
                }
                else if (accessoryPackComponent.isHardware())
                {
                    AccHwComponent hardware = ModuleDataService.getInstance().getHardware(accessoryPackComponent.getComponentCode());
                    if (hardware == null) continue;
                    this.addToModuleHardware(hardware, accessoryPackComponent.getQuantity(), module.getUnit(), module.getSequence());
                }
                else if (accessoryPackComponent.isCarcass())
                {
                    ModulePanel carcassPanel = ModuleDataService.getInstance().getPanel(accessoryPackComponent.getComponentCode());
                    if (carcassPanel == null) continue;
                    this.addToAccessoryPackPanels(carcassPanel, accessoryPackComponent.getQuantity(), module.getUnit(), module.getSequence());
                }
            }

            for (String code : modAccessoryPack.getAddons())
            {
                AccHwComponent accessory = ModuleDataService.getInstance().getAccessory(code);
                if (accessory == null) continue;
                this.addToProductAccessories(accessory, 1);
                this.addToAddonAccessories(accessory, 1, module.getUnit(), module.getSequence());
            }
        }
    }

    private void collectModuleComponents(ProductModule module)
    {

            Collection<ModuleComponent> moduleComponents =
                    ModuleDataService.getInstance().getModuleComponents(module.getMGCode());
            if (moduleComponents == null ) return;
            for (ModuleComponent moduleComponent : moduleComponents)
            {
                if (moduleComponent.isHardware())
                {
                    AccHwComponent hardware = ModuleDataService.getInstance().getHardware(moduleComponent.getComponentCode());
                    if (hardware == null) continue;
                    this.addToModuleHardware(hardware, moduleComponent.getQuantity(), module.getUnit(), module.getSequence());
                }
            }
    }

    private void collectModuleHandles(ProductModule module)
    {
        String NORMAL = "Normal";
        String GOLA_PROFILE = "Gola Profile";
        String CORNER_UNIT = "Corner";
        String LOFTS = "Loft";

        PriceMaster lWidthRate = RateCardService.getInstance().getHardwareRate("H073", priceDate, city);
        PriceMaster cWidthRate = RateCardService.getInstance().getHardwareRate("H071", priceDate, city);
        PriceMaster wWidthRate = RateCardService.getInstance().getHardwareRate("H076", priceDate, city);
        PriceMaster bracketRate = RateCardService.getInstance().getHardwareRate("H075", priceDate, city);
        PriceMaster lConnectorRate = RateCardService.getInstance().getHardwareRate("H074", priceDate, city);
        PriceMaster cConnectorRate = RateCardService.getInstance().getHardwareRate("H072", priceDate, city);
        PriceMaster gProfileRate = RateCardService.getInstance().getHardwareRate("H018", priceDate, city);
        PriceMaster jProfileRate = RateCardService.getInstance().getHardwareRate("H077", priceDate, city);


        //LOG.info("Product Module " +module);
        //LOG.info("Product handle type selection" +this.product.getHandletypeSelection());
        if (Objects.equals(this.product.getHandletypeSelection(),GOLA_PROFILE ))
        {

            LOG.debug("Inside gola profile");

            /*if (!(module.getHandleCode() == null))
            {
                this.getHandleOrKnobRate(this.productLineItem.getHandleCode(),this.productModule.getHandleQuantity());
                // Handle handle = ModuleDataService.getInstance().getHandleTitle(this.productModule.getHandleCode());
                //  this.productionSpecificationComponents.add(new Handle(handle));
            }
            if (!(this.productModule.getKnobCode() == null)){
                this.getHandleOrKnobRate(this.productLineItem.getKnobCode(),this.productModule.getKnobQuantity());
                // Handle knob = ModuleDataService.getInstance().getHandleTitle(this.productModule.getKnobCode());
                //  this.productionSpecificationComponents.add(new Handle(knob));
            }*/
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

            if (!(module.getModuleCategory().contains(LOFTS))) {
                if (Objects.equals(module.getHandleMandatory(), "Yes")) {
                    moduleCount = moduleCount + 1;
                    if (module.getModuleCategory().contains("Wall")) {
                        wallProfileWidth = wallProfileWidth + module.getWidth();
                    } else if (module.getModuleCategory().contains("Base")) {
                        if (module.getModuleCategory().contains(CORNER_UNIT)) {
                            lProfileWidth = lProfileWidth + (module.getWidth() / 2);
                        } else if (module.getModuleCategory().contains("Drawer")) {
                            drawerModuleCount = drawerModuleCount + 1;
                            cProfileWidth = cProfileWidth + module.getWidth();
                            lProfileWidth = lProfileWidth + module.getWidth();
                        } else {
                            lProfileWidth = lProfileWidth + module.getWidth();
                        }
                    } else if (module.getModuleCategory().contains("Tall")) {
                        lProfileWidth = lProfileWidth + module.getHeight();
                    } else {
                        lProfileWidth = lProfileWidth + module.getWidth();

                    }
                }
            }

            wProfilePrice = wallProfileWidth/1000*wWidthRate.getPrice();

            AccHwComponent hardware = ModuleDataService.getInstance().getHardware(wWidthRate.getRateId());
            this.addToModuleHardware(hardware, 0.0 , module.getUnit(), module.getSequence());

            lProfilePrice = lProfileWidth/1000*lWidthRate.getPrice();
            AccHwComponent hardware1 = ModuleDataService.getInstance().getHardware(lWidthRate.getRateId());
            this.addToModuleHardware(hardware1, 0.0 , module.getUnit(), module.getSequence());

            cProfilePrice = cProfileWidth/1000*cWidthRate.getPrice();
            AccHwComponent hardware2 = ModuleDataService.getInstance().getHardware(wWidthRate.getRateId());
            this.addToModuleHardware(hardware2, 0.0 , module.getUnit(), module.getSequence());

            profilePrice = wProfilePrice + lProfilePrice + cProfilePrice;
            bracketPrice = (moduleCount * 2) * bracketRate.getPrice();
            AccHwComponent hardware3 = ModuleDataService.getInstance().getHardware(bracketRate.getRateId());
            this.addToModuleHardware(hardware3, 0.0 , module.getUnit(), module.getSequence());
//                lConnectorPrice = golaProfileLength * this.lConnectorRate.getPrice();
            cConnectorPrice = drawerModuleCount * cConnectorRate.getPrice();
            AccHwComponent hardware4 = ModuleDataService.getInstance().getHardware(cConnectorRate.getRateId());
            this.addToModuleHardware(hardware4, 0.0 , module.getUnit(), module.getSequence());


            wProfileSourceCost = wallProfileWidth/1000 * wWidthRate.getSourcePrice();
            lProfileSorceCost = lProfileWidth/1000 * lWidthRate.getSourcePrice();
            cProfileSourceCost = cProfileWidth/1000 * cWidthRate.getSourcePrice();

            profileSourceCost = wProfileSourceCost + lProfileSorceCost + cProfileSourceCost;
            bracketSourceCost = (moduleCount * 2) * bracketRate.getSourcePrice();
            cConnectorSourceCost = drawerModuleCount * cConnectorRate.getSourcePrice();

            golaprofileSourceCost = profileSourceCost + bracketSourceCost + cConnectorSourceCost;

            //handleandKnobSourceCost = golaprofileSourceCost;

            golaProfilePrice = profilePrice + bracketPrice  + cConnectorPrice;
            //handleandKnobCost += golaProfilePrice;

            LOG.debug("Gola Profile Price: " + golaProfilePrice);

        }
        if (Objects.equals(this.product.getHandletypeSelection(), "G Profile")){

            LOG.debug("G Profile : ");

            double lWidth = 0;
            double gOrJProfileSourceCost;
            double gOrJProfilePrice = 0;
            double quantity = 0;
            // for (ProductModule module : this.productLineItem.getModules())
            // {
            Collection<AccessoryPackComponent> handles = ModuleDataService.getInstance().getAccessoryPackComponents(module.getMGCode());
            for (AccessoryPackComponent accessoryPackComponent : handles)
            {
                quantity = accessoryPackComponent.getQuantity();
            }
            if (Objects.equals(module.getHandleMandatory(), "Yes"))
            {
                if (module.getModuleCategory().contains("Drawer"))
                {
                    lWidth = lWidth + (quantity * module.getWidth());
                }
                else {
                    lWidth = lWidth + module.getWidth();
                }
            }
            // }
            gOrJProfilePrice = lWidth/1000 * gProfileRate.getPrice();
            AccHwComponent hardware5 = ModuleDataService.getInstance().getHardware(gProfileRate.getRateId());
            this.addToModuleHardware(hardware5, quantity , module.getUnit(), module.getSequence());
            LOG.debug("G profile rate : " +  gProfileRate.getPrice());


            gOrJProfileSourceCost = lWidth/1000 * gProfileRate.getSourcePrice();
            /*handleandKnobSourceCost +=  gOrJProfileSourceCost;

            LOG.debug("Inside G profile : "+ gOrJProfilePrice);
            //LOG.debug("L width Rate :" + gProfileRate.getPrice());
            handleandKnobCost += gOrJProfilePrice;*/


        }
        if (Objects.equals(this.product.getHandletypeSelection(), "J Profile"))
        {
            LOG.debug("J profile : ");

            double lWidth = 0;
            double gOrJProfileSourceCost;
            double gOrJProfilePrice;
            double quantity = 0;
            //  for (ProductModule module : this.productLineItem.getModules())
            //  {

            Collection<AccessoryPackComponent> handles = ModuleDataService.getInstance().getAccessoryPackComponents(module.getMGCode());
            for (AccessoryPackComponent accessoryPackComponent : handles)
            {
                quantity = accessoryPackComponent.getQuantity();
            }
            if (Objects.equals(module.getHandleMandatory(), "Yes"))
            {
                if (module.getModuleCategory().contains("Drawer"))
                {
                    lWidth = lWidth + (quantity * module.getWidth());
                    LOG.debug("Inside if :" + lWidth);
                }
                else {
                    lWidth = lWidth + module.getWidth();
                    LOG.debug("Inside else :" + lWidth);
                }
            }
            // }
            gOrJProfilePrice = lWidth/1000 * jProfileRate.getPrice();
            AccHwComponent hardware6 = ModuleDataService.getInstance().getHardware(jProfileRate.getRateId());
            this.addToModuleHardware(hardware6, quantity , module.getUnit(), module.getSequence());
            LOG.debug("J profile rate : " +  jProfileRate.getPrice());
            LOG.debug("Inside J profile : "+ gOrJProfilePrice);

            gOrJProfileSourceCost = lWidth/1000 * jProfileRate.getSourcePrice();
            /*handleandKnobSourceCost +=  gOrJProfileSourceCost;

            //LOG.debug("L width Rate :" + gProfileRate.getPrice());
            handleandKnobCost += gOrJProfilePrice;*/
        }
        if(module.getHandleCode()==null)
        {
            return;
        }
        Handle handle = ModuleDataService.getInstance().getHandleTitle(module.getHandleCode());
        this.addToModuleHandle(handle,module.getHandleQuantity());
    }
    private void collectModuleKnob(ProductModule module)
    {
        if(module.getKnobCode()==null)
        {
            return;
        }
        Handle handle = ModuleDataService.getInstance().getHandleTitle(module.getKnobCode());
        this.addToModuleHandle(handle,module.getKnobQuantity());
    }

    private void collectModuleHinge(ProductModule module)
    {
        if (module.getHingePacks().size() == 0)
        {
            return;
        }
        else{
         for (HingePack hingePack : module.getHingePacks()) {
            LOG.info("hingePack " +hingePack.toString());
            this.addToModuleHinge(hingePack, hingePack.getQUANTITY());
        }}
    }

    private void addToAddons(ProductAddon addon, double quantity, String unit, int seq)
    {
        ModulePart part = new ModulePart(unit, seq, addon.getCode(), addon.getTitle(), quantity, addon.getBrandCode(), addon.getUom(),"gh","dd"," ");
        this.addons.add(part);
    }

    private void addToAddonAccessories(AccHwComponent component, double quantity, String unit, int seq )
    {
        ModulePart part = this.createModulePart(component, quantity, unit, seq);
        this.addonAccessories.add(part);
    }

    private void addToModuleAccessories(AccHwComponent component, double quantity, String unit, int seq )
    {
        ModulePart part = this.createModulePart(component, quantity, unit, seq );
        this.moduleAccessories.add(part);
    }

    private void addToModuleHardware(AccHwComponent component, double quantity, String unit, int seq )
    {
        ModulePart part = this.createModulePart(component, quantity, unit, seq );
        PriceMaster addonRate = RateCardService.getInstance().getHardwareRate(component.getCode(),this.priceDate,this.city);
        LOG.info("rate for hardware " +addonRate.getPrice());
        if(!(addonRate.getPrice()==0))
        {
            this.moduleHardware.add(part);
        }
    }

    private void addToAccessoryPackPanels(ModulePanel carcassPanel, double quantity, String unit, int seq)
    {
        ModulePart part = new ModulePart(unit, seq, carcassPanel.getCode(), carcassPanel.getTitle(), quantity, "NA", "NOS","NA","NA","NA");
        this.accessoryPackPanels.add(part);
    }

    private void addToModuleHandle(Handle handleCode, double quantity)
    {
        LOG.info("handle code " +handleCode +"quantity " +quantity);
        ModulePart part=new ModulePart(handleCode.getCode(), "UOM", quantity, handleCode.getTitle(),"Catalogue code","ERP code");
        if(!(quantity==0.0))
        {
            this.handleandKnob.add(part);
        }
    }

    private void addToModuleHinge(HingePack hingeCode, double quantity)
    {
        LOG.debug("Hinge Pack /; " + hingePack.toString());
        ModulePart part=new ModulePart(hingeCode.getHingeCode(), "UOM", quantity, hingeCode.getTYPE(),"Catalogue code","ERP code");
        if(!(quantity==0.0))
        {
            this.hingePack.add(part);
        }
    }

    private ModulePart createModulePart(AccHwComponent component, double quantity, String unit, int seq)
    {
        LOG.info("component" +component);
        return new ModulePart(unit, seq, component.getCode(), component.getTitle(), quantity, component.getMake(), component.getUom(),component.getCatalogCode(),component.getCategory(),component.getERPCode());
    }

    private void addToProductAccessories(AccHwComponent accessoryComponent, double quantity)
    {
        Accessory accessory = this.getAccessory(accessoryComponent.getCode(), accessoryComponent.getTitle(),accessoryComponent.getPrice(),accessoryComponent.getCatalogCode(),accessoryComponent.getCategory(),accessoryComponent.getERPCode());
        accessory.incrementQuantity(quantity);
    }

    private Accessory getAccessory(String code, String title,double msp,String catalogCode,String category,String ERPCode)
    {
        if (StringUtils.isEmpty(code)) code = "Default";
        for (Accessory accessory : this.accessories)
        {
            if (accessory.code.equals(code))
            {
                return accessory;
            }
        }
        Accessory accessory = new Accessory(code, title,msp,catalogCode,category,ERPCode);
        this.accessories.add(accessory);
        return accessory;
    }

    private void addModuleToUnit(ProductModule module)
    {
        Unit unit = this.getUnit(module.getUnit(),module.getModuleCategory());
        unit.addModule(module);
    }

    /*private Unit getUnit(String unitTitle,String moduleCategory)
    {
        if (StringUtils.isEmpty(unitTitle)) unitTitle = "Default";
        for (Unit unit : this.units)
        {
            if (unit.title.equals(unitTitle))
            {
                return unit;
            }
        }

        Unit unit = new Unit(this.units.size() + 1, unitTitle,moduleCategory);
        this.units.add(unit);
        return unit;
    }*/
    private Unit getUnit(String unitTitle,String moduleCategory)
    {
        if (StringUtils.isEmpty(moduleCategory)) moduleCategory = "Default";
        for (Unit unit : this.units)
        {
            if (unit.moduleCategory.equals(moduleCategory))
            {
                return unit;
            }
        }

        Unit unit = new Unit(this.units.size() + 1, unitTitle,moduleCategory);
        this.units.add(unit);
        return unit;
    }

    public List<ModulePart> getModuleHardware()
    {
        return moduleHardware;
    }

    public List<ProductModule> getModules()
    {
        return this.product.getModules();
    }

    public List<ModulePanelComponent> getAggregatedCarcassPanels()
    {
        List<ModulePanelComponent> aggregated =

                Seq.ofType(this.getPanels(false).stream(), ModulePanelComponent.class)
                        .groupBy(x -> tuple(x.code, x.title, x.width, x.height, x.thickness, x.edgebinding, x.dimension),
                                Tuple.collectors(
                                        Collectors.summingDouble(x -> x.quantity), Collectors.summingDouble(x -> x.area)
                                )
                        )
                        .entrySet()
                        .stream()
                        .map(e -> new ModulePanelComponent().setCode(e.getKey().v1).setTitle(e.getKey().v2).setWidth(e.getKey().v3).setHeight(e.getKey().v4)
                                .setThickness(e.getKey().v5).setEdgebinding(e.getKey().v6).setDimension(e.getKey().v7)
                                .setQuantity(e.getValue().v1).setArea(e.getValue().v2))
                        .collect(Collectors.toList());

        return aggregated;
    }

    public List<ModulePanelComponent> getAggregatedShutterPanels()
    {
        List<ModulePanelComponent> aggregated =

                Seq.ofType(this.getPanels(true).stream(), ModulePanelComponent.class)
                        .groupBy(x -> tuple(x.code, x.title, x.height, x.width, x.thickness, x.edgebinding, x.design, x.color, x.dimension),
                                Tuple.collectors(
                                        Collectors.summingDouble(x -> x.quantity), Collectors.summingDouble(x -> x.area)
                                )
                        )
                        .entrySet()
                        .stream()
                        .map(e -> new ModulePanelComponent().setCode(e.getKey().v1).setTitle(e.getKey().v2).setHeight(e.getKey().v3)
                                .setWidth(e.getKey().v4).setThickness(e.getKey().v5).setEdgebinding(e.getKey().v6)
                                .setDesign(e.getKey().v7).setColor(e.getKey().v8).setDimension(e.getKey().v9)
                                .setQuantity(e.getValue().v1).setArea(e.getValue().v2))
                        .collect(Collectors.toList());

        return aggregated;
    }

    public List<ModulePanelComponent> getPanels(boolean exposed)
    {
        List<ModulePanelComponent> panels = new ArrayList<>();
        for (ModulePriceHolder priceHolder : this.priceHolders)
        {
            for (PanelComponent panel : priceHolder.getPanelComponents())
            {
                if (panel.isExposed() == exposed)
                    panels.add(new ModulePanelComponent(panel, priceHolder.getProductModule(), this.product.getDesignCode()));
            }
        }
        return panels;
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
        public String moduleCategory;
        public List<ModuleDimension> moduleDimensions;
        public double amount=0;

        public Unit(int sequence, String title,String moduleCategory)
        {
            this.sequence = sequence;
            this.title = title;
            this.moduleCategory=moduleCategory;
            this.moduleDimensions = new ArrayList<>();
        }

        @Override
        public String toString() {
            return "Unit{" +
                    "amount=" + amount +
                    ", sequence=" + sequence +
                    ", moduleCount=" + moduleCount +
                    ", title='" + title + '\'' +
                    ", moduleCategory='" + moduleCategory + '\'' +
                    ", moduleDimensions=" + moduleDimensions +
                    '}';
        }

        public void addModule(ProductModule module)
        {
            this.moduleCount++;
            ModuleDimension matchingDimension = this.getModuleDimension(module);
            if (matchingDimension != null)
            {
                matchingDimension.incrementLength(module.getWidth());
            }
            else
            {
                this.moduleDimensions.add(new ModuleDimension(module.getWidth(), module.getDepth(), module.getHeight()));
            }
            this.amount += module.getAmount();

        }

        private ModuleDimension getModuleDimension(ProductModule module)
        {
            for (ModuleDimension dimension : this.moduleDimensions)
            {
                if (dimension.depth == module.getDepth() && dimension.height == module.getHeight())
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

         public int getWidth()
        {
            for (ModuleDimension dimension : this.moduleDimensions)
            {
                return dimension.length;
            }
            return 0;
        }
        public int getDepth()
        {
            for (ModuleDimension dimension : this.moduleDimensions)
            {
                return dimension.depth;
            }
            return 0;
        }

        public int getHeight()
        {
            for (ModuleDimension dimension : this.moduleDimensions)
            {
                return dimension.height;
            }
            return 0;
        }
    }

    public static class ModulePrice
    {
        public double totalCost;
        public double woodWorkCost;
        public double hardwareCost;

        public ModulePrice(double totalCost, double woodWorkCost, double hardwareCost)
        {
            this.totalCost = totalCost;
            this.woodWorkCost = woodWorkCost;
            this.hardwareCost = hardwareCost;

        }
    }

    public static class ModuleDimension
    {
        public int length;//width
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
        public double msp;
        public String catalogCode;
        public String category;
        public String ERPCode;

        @Override
        public String toString() {
            return "Accessory{" +
                    "category='" + category + '\'' +
                    ", code='" + code + '\'' +
                    ", title='" + title + '\'' +
                    ", quantity=" + quantity +
                    ", msp=" + msp +
                    ", catalogCode='" + catalogCode + '\'' +
                    '}';
        }

        public Accessory(String code, String title, double msp, String catalogCode, String catalog,String ERPCode)
        {
            this.code = code;
            this.title = title;
            this.msp=msp;
            this.catalogCode = catalogCode;
            this.category= catalog;
            this.ERPCode=ERPCode;
        }

        public void incrementQuantity(double qty)
        {
            this.quantity += qty;
        }
    }

    public static class ModulePart
    {
        public String unit;
        public int seq;
        public String code;
        public String title;
        public double quantity;
        public String make;
        public String uom;
        public double cost;
        public double costWoAccessories;
        public double hardwareCost;
        public String catalogCode;
        public String category;
        public String ERPCode;


        public ModulePart(String unit, int seq, String code, String title, double quantity, String make, String uom, String catalogCode,String category,String ERPCode)
        {
            this.unit = unit;
            this.seq = seq;
            this.code = code;
            this.title = title;
            this.quantity = quantity;
            this.make = make;
            this.uom = uom;
            this.catalogCode = catalogCode;
            this.category=category;
            this.ERPCode=ERPCode;
        }

        public ModulePart(String code, String title, String make, String uom, double quantity)
        {
            this.code = code;
            this.quantity = quantity;
            this.title = title;
            this.make = make;
            this.uom = uom;
        }

        public ModulePart(String code, String uom, double quantity)
        {
            this.code = code;
            this.quantity = quantity;
            this.uom = uom;
        }
        public ModulePart(String code, String uom, double quantity,String title,String catalogCode,String ERPCode)
        {
            this.code = code;
            this.quantity = quantity;
            this.uom = uom;
            this.title = title;
            this.catalogCode = catalogCode;
            this.ERPCode=ERPCode;

        }

        @Override
        public String toString() {
            return "ModulePart{" +
                    "catalogCode='" + catalogCode + '\'' +
                    ", unit='" + unit + '\'' +
                    ", seq=" + seq +
                    ", code='" + code + '\'' +
                    ", title='" + title + '\'' +
                    ", quantity=" + quantity +
                    ", make='" + make + '\'' +
                    ", uom='" + uom + '\'' +
                    ", cost=" + cost +
                    ", costWoAccessories=" + costWoAccessories +
                    ", hardwareCost=" + hardwareCost +
                    ", category='" + category + '\'' +
                    ", ERPCode='" + ERPCode + '\'' +
                    '}';
        }
    }

    public static class ModulePanelComponent
    {
        public String dimension;
        public String code;
        public String title;
        public double quantity;
        public int width;
        public int height;
        public int thickness;
        public String edgebinding;
        public double area;
        public String design;
        public String color;

        public ModulePanelComponent(PanelComponent panel, ProductModule module, String design)
        {
            this.code = panel.getCode();
            this.title = panel.getTitle();
            this.quantity = panel.getQuantity();
            this.width = panel.getLength();
            this.height = panel.getBreadth();
            this.thickness = panel.getThickness();
            this.edgebinding = panel.getEdgeBinding();
            this.area = panel.getArea();
            this.dimension = this.getDimesions();
            this.color = module.getColorCode();
            this.design = design;
        }

        public ModulePanelComponent()
        {

        }

        public ModulePanelComponent setCode(String code)
        {
            this.code = code;
            return this;
        }

        public ModulePanelComponent setTitle(String title)
        {
            this.title = title;
            return this;
        }

        public ModulePanelComponent setQuantity(double quantity)
        {
            this.quantity = quantity;
            return this;
        }

        public ModulePanelComponent setWidth(int width)
        {
            this.width = width;
            return this;
        }

        public ModulePanelComponent setHeight(int height)
        {
            this.height = height;
            return this;
        }

        public ModulePanelComponent setThickness(int thickness)
        {
            this.thickness = thickness;
            return this;
        }

        public ModulePanelComponent setEdgebinding(String edgebinding)
        {
            this.edgebinding = edgebinding;
            return this;
        }

        public ModulePanelComponent setArea(double area)
        {
            this.area = area;
            return this;
        }

        public ModulePanelComponent setDesign(String design)
        {
            this.design = design;
            return this;
        }

        public ModulePanelComponent setColor(String color)
        {
            this.color = color;
            return this;
        }

        public ModulePanelComponent setDimension(String dimension)
        {
            this.dimension = dimension;
            return this;
        }

        private String getDimesions()
        {
            return this.width + " X " + this.height + " X " + this.thickness;
        }
    }

}

