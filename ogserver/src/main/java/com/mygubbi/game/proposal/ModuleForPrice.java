package com.mygubbi.game.proposal;

import com.mygubbi.game.proposal.price.ModulePriceHolder;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Date;
import java.text.SimpleDateFormat;

import static org.apache.poi.ss.formula.functions.NumericFunction.LOG;

/**
 * Created by Sunil on 27-04-2016.
 */

public class ModuleForPrice extends JsonObject
{
    private final static Logger LOG = LogManager.getLogger(ModuleForPrice.class);
    private static final double SQMM2SQFT = 0.0000107639;

    private static final String PRICE_DATE = "priceDate";
    private static final String CITY = "city";
    private static final String MODULE = "module";
    private static final String PRODUCT = "product";
    private Date priceDate;

    public ModuleForPrice(JsonObject json)
    {
        super(json.getMap());
//        LOG.info("JSON object for product" +json.encodePrettily());
        this.setModule();
        this.setProduct();
    }

    public ModuleForPrice() {
    }

    public ModuleForPrice(ProductModule productModule,ProductLineItem productLineItem, Date priceDate, String city) {
        this.setProduct(productLineItem);
        this.setModule(productModule);
        this.setCity(city);
        this.setPriceDate(priceDate);
    }



    public Date getPriceDate()
    {
        return Date.valueOf(this.getString(PRICE_DATE));
    }

    public String getCity()
    {
        return this.getString(CITY);
    }


    public ModuleForPrice setCreateDate(Date createDate)
    {
        this.put(PRICE_DATE,createDate);
        return this;
    }

    public ModuleForPrice setCity(String city)
    {
        this.put(CITY, city);
        return this;
    }


    public void setModule()
    {
        if (this.containsKey(MODULE))
        {
            this.put(MODULE, new ProductModule(this.getJsonObject(MODULE)));
        }
    }

    public void setModule(ProductModule productModule)
    {

            this.put(MODULE,productModule);
    }

    public void setProduct()
    {
        if (this.containsKey(PRODUCT))
        {
            this.put(PRODUCT, new ProductLineItem(this.getJsonObject(PRODUCT)));
        }
    }

    public void setProduct(ProductLineItem productLineItem)
    {

        this.put(PRODUCT,productLineItem);
    }


    public ProductLineItem getProduct()
    {
        if (this.containsKey(PRODUCT)) return (ProductLineItem) this.getJsonObject(PRODUCT);
        return null;
    }

    public ProductModule getModule()
    {
        if (this.containsKey(MODULE)) return (ProductModule) this.getJsonObject(MODULE);
        return null;
    }


    public ModuleForPrice setPriceDate(Date priceDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String format = formatter.format(priceDate);
        this.put(PRICE_DATE, format);
        return this;
    }
}
