package com.mygubbi.game.proposal;

import com.mygubbi.common.DateUtil;
import com.mygubbi.report.ReportTableFillerSevice;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sunil on 26-04-2016.
 */
public class ProductLineItem extends JsonObject
{
    private final static Logger LOG = LogManager.getLogger(ProductLineItem.class);
    public static final String CUSTOMIZED_PRODUCT = "CUSTOMIZED";
    public static final String CATALOGUE_PRODUCT = "CATALOGUE";

    private static final String ID = "id";
    private static final String ACTIVE = "active";
    private static final String PROPOSALID = "proposalId";
    private static final String TITLE = "title";
    private static final String SEQ = "seq";
    private static final String TYPE = "type"; //Customized, Catalogue
    private static final String ROOM_CODE = "roomCode"; //Kitchen, Bedroom 1
    private static final String PRODUCT_CATEGORY = "productCategoryCode"; //Kitchen, Living Room
    private static final String CATALOGUE_ID = "catalogueId";
    private static final String CATALOGUE_NAME = "catalogueName";
    private static final String MAKE_TYPE = "makeTypeCode";
    private static final String BASE_CARCASS_CODE = "baseCarcassCode";
    private static final String WALL_CARCASS_CODE = "wallCarcassCode";
    private static final String FINISH_TYPE = "finishTypeCode";
    private static final String FINISH_CODE = "finishCode";
    private static final String DESIGN_CODE = "designCode";
    private static final String DIMENSION = "dimension";
    private static final String QUANTITY = "quantity";
    private static final String AMOUNT = "amount";
    private static final String QUOTE_FILE_PATH = "quoteFilePath";
    private static final String MODULES = "modules";
    private static final String ADDONS = "addons";
    private static final String GLASS = "glass";
    private static final String CREATED_ON = "createdOn";
    private static final String CREATED_BY = "createdBy";
    private static final String UPDATED_ON = "updatedOn";
    private static final String UPDATED_BY = "updatedBy";
    private static final String HANDLE_TYPE = "handleType";
    private static final String HANDLE_FINISH = "handleFinish";
    private static final String SOURCE = "source";
    private static final String KNOB_TYPE = "knobType";
    private static final String KNOB_FINISH = "knobFinish";
    //public static final String NO_OF_LENGTHS="noOfLengths";
    public static final String HANDLETYPE_SELECTION="handleTypeSelection";

    public static final String HANDLE_CODE="handleCode";
    public static final String KNOB_CODE="knobCode";
    public static final String HANDLE_THICKNESS="handleThickness";
    public static final String HINGE_TYPE="hinge";
    public static final String SPACE_TYPE="spaceType";
    public static final String SHUTTER_DESIGN_CODE="shutterDesignCode";
    public static final String NO_OF_LENGTHS="noOfLengths";
    public static final String FROM_PRODUCT="fromProduct";
    public static final String COST_WO_ACC = "costWoAccessories";


    public static final String BASE_UNIT_TYPE = "Base unit";
    public static final String LCONNECTOR_PRICE="lConnectorPrice";
    public static final String COLORGROUP_CODE="colorGroupCode";
    public ProductLineItem()
    {

    }

    public  String getProductCategory() {
        return this.getString(PRODUCT_CATEGORY);
    }

    public ProductLineItem(JsonObject data)
    {
        super(data.getMap());
        this.setModules();
        this.setAddons();
    }

    public int getFromProduct(){
        if (this.getInteger(FROM_PRODUCT) == null || this.getInteger(FROM_PRODUCT).equals("")) return 0;
        else return this.getInteger(FROM_PRODUCT); }

    public String getTitle()
    {
        return this.getString(TITLE);
    }

    public String getColorgroupCode() { return  this.getString(COLORGROUP_CODE);}
    public String getType()
    {
        return this.getString(TYPE);
    }

    public  String getRoomCode() {
        return this.getString(ROOM_CODE);
    }

    public String getKdMaxFile()
    {
        return this.getString(QUOTE_FILE_PATH);
    }

    public String getBaseCarcassCode()
    {
        return this.getString(BASE_CARCASS_CODE);
    }

    public String getWallCarcassCode()
    {
        return this.getString(WALL_CARCASS_CODE);
    }

    public String getFinishCode()
    {
        return this.getString(FINISH_CODE);
    }

    public String getFinishType()
    {
        return this.getString(FINISH_TYPE);
    }

    public  String getSource(){return this.getString(SOURCE);}

    public String getMakeType()
    {
        return this.getString(MAKE_TYPE);
    }

    public String getDesignCode()
    {
        return this.getString(DESIGN_CODE);
    }

    public int getQuantity()
    {
        return this.getInteger(QUANTITY);
    }

    public double getAmount()
    {
        return this.getDouble(AMOUNT);
    }

    public String getDimension()
    {
        return this.getString(DIMENSION);
    }

    public String getName()
    {
        return this.getString(CATALOGUE_NAME);
    }

    public String getHandleType() {
        return this.getString(HANDLE_TYPE);
    }

    public String getHandleFinish() {
        return this.getString(HANDLE_FINISH);
    }

    public String getKnobType() {
        return this.getString(KNOB_TYPE);
    }

    public String getKnobFinish() {
        return this.getString(KNOB_FINISH);
    }

    public String getGlass()
    {
        return this.getString(GLASS);
    }
    public String getHandleCode()
    {
        return this.getString(HANDLE_CODE);
    }
    public String getKnobCode()
    {
        return this.getString(KNOB_CODE);
    }

    public String getHandleThickness() { return this.getString(HANDLE_THICKNESS);}
    public String getSpaceType() { return this.getString(SPACE_TYPE);}

    public String getShutterDesignCode() {
        return this.getString(SHUTTER_DESIGN_CODE);
    }


    public String getHandletypeSelection() {
        return this.getString(HANDLETYPE_SELECTION);
    }
    public String getHingeType() {
        return this.getString(HINGE_TYPE);
    }

      public int getNoOfLengths() {
        /*LOG.info("VALUE == "+this.getValue(NO_OF_LENGTHS)+" - "+isValueNull(NO_OF_LENGTHS));
        return (isValueNull(NO_OF_LENGTHS) ?  0: Integer.parseInt(NO_OF_LENGTHS));*/
       if(!isValueNull(NO_OF_LENGTHS)) {
           Double nooflengths = Double.parseDouble(this.getString(NO_OF_LENGTHS));

           if (nooflengths == null || nooflengths.equals("") || nooflengths == 0.0) return 0;
           else return nooflengths.intValue();
       }else
           return 0;
    }

    private boolean isValueNull(String key){
            if(this.getValue(key) == null || this.getValue(key) == "") return true;
            return  false;
    }
    public String getCreatedBy() {
        return this.getString(CREATED_BY);
    }

    public Date getCreatedOn() {
        return DateUtil.convertDate(this.getString(CREATED_ON));
    }

    public Date getUpdatedOn() {
        return DateUtil.convertDate(this.getString(UPDATED_ON));
    }

    public String getUpdatedBy() {
        return this.getString(UPDATED_BY);
    }

    public double getCostWoAcc() {
        return this.getDouble(COST_WO_ACC);
    }
    public double getLconnectorPrice()
    {
        return this.getDouble(LCONNECTOR_PRICE);
    }

    public ProductLineItem setAmount(double amount) {
        this.put(AMOUNT, amount);
        return this;
    }
    public ProductLineItem setFinishCode(String finishCode)
    {
        this.put(FINISH_CODE,finishCode);
        return this;
    }

    public ProductLineItem setColorGroupCode(String colorGroupCode)
    {
        this.put(COLORGROUP_CODE,colorGroupCode);
        return this;
    }
    public ProductLineItem addModule(ProductModule module)
    {
        if (!this.containsKey(MODULES))
        {
            this.put(MODULES, new JsonArray());
        }
        JsonArray mgModules = this.getJsonArray(MODULES);
        String carcassCode = BASE_UNIT_TYPE.equals(module.getUnit()) ? this.getBaseCarcassCode() : this.getWallCarcassCode();
        module.setCarcassCode(carcassCode)
                .setFinishCode(this.getFinishCode())
                .setFinishType(this.getFinishType());
        mgModules.add(module);
        this.put(MODULES, mgModules);
        return this;
    }

    private ProductLineItem addAddOn(ProductAddon addon)
    {
        if (!this.containsKey(ADDONS))
        {
            this.put(ADDONS, new JsonArray());
        }
        JsonArray addons = this.getJsonArray(ADDONS);
        addons.add(addon);
        this.put(ADDONS, addons);
        return this;
    }

    public void resetModules()
    {
        if (this.containsKey(MODULES))
        {
            JsonArray moduleJsons = this.getJsonArray(MODULES);
            moduleJsons.clear();
            this.put(MODULES, moduleJsons);
        }
        else
        {
            this.put(MODULES, new JsonArray());
        }
    }

    private void setModules()
    {
        if (this.containsKey(MODULES))
        {
            JsonArray moduleJsons = this.getJsonArray(MODULES);
            JsonArray modules = new JsonArray();
            for (int i=0; i < moduleJsons.size(); i++)
            {
                modules.add(new ProductModule(moduleJsons.getJsonObject(i)));
            }
            this.put(MODULES, modules);
        }
    }

    private void setAddons()
    {
        JsonArray productAddons = new JsonArray();
        if (this.containsKey(ADDONS) && (this.getValue(ADDONS) instanceof JsonArray))
        {
            JsonArray addonJsons = this.getJsonArray(ADDONS);
            for (int i=0; i < addonJsons.size(); i++)
            {
                productAddons.add(new ProductAddon(addonJsons.getJsonObject(i)));
            }
        }
        this.put(ADDONS, productAddons);
    }

    public List<ProductAddon> getAddons()
    {
        return this.getJsonArray(ADDONS).getList();
    }

    public int getId()
    {
        return this.containsKey(ID) ? this.getInteger(ID) : 0;
    }

    public List<ProductModule> getModules()
    {
        if(this.containsKey(MODULES)) {
            return this.getJsonArray(MODULES).getList();
        }return new ArrayList<>();
    }

    public Double getRate()
    {
        if (this.getQuantity() == 0) return 0.0;
        return this.getAmount() / this.getQuantity();
    }
}

