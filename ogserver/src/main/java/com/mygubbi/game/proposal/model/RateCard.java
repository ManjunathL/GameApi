package com.mygubbi.game.proposal.model;

import com.mygubbi.game.proposal.price.RateCardService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Date;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by test on 17-05-2016.
 */

public class RateCard
{

    private final static Logger LOG = LogManager.getLogger(RateCard.class);

    public static final String CARCASS_TYPE = "C";
    public static final String SHUTTER_TYPE = "S";
    public static final String FACTOR_TYPE = "F";
    public static final String LOADING_FACTOR = "L";
    public static final String LABOUR_FACTOR = "B";
    public static final String LOADING_FACTOR_NONSTANDARD = "N";
    public static final String CUSTOM_ADDON_SALES_PRICE_FACTOR = "CASP";
    public static final String ADDON_WO_TAX_FACTOR = "ADWOTAX";
    public static final String LABOUR_COST_FACTOR = "LC";
    public static final String NONSTD_MANUFACTURING_COST_FACTOR = "NSTDMC";
    public static final String STD_MANUFACTURING_COST_FACTOR = "STDMC";
    public static final String PRODUCT_WO_TAX = "PRODWOTAX";
    public static final String DISCOUNT_PERCENTAGE = "DP";

    private String code;
    private String type;
    private String city;
    private Date priceDate;
    private double rateUsed;
    private Map<Integer, Double> ratesUsed = Collections.EMPTY_MAP;

    public RateCard(String code, String type, Date priceDate, String city) {
        this.code = code;
        this.type = type;
        this.priceDate = priceDate;
        this.city = city;
        this.ratesUsed = new HashMap<>();
    }

    public String getCode()
    {
        return code;
    }

    public String getType()
    {
        return type;
    }

    public double getRateByThickness(int thickness)
    {

        PriceMaster priceMaster;
        if (CARCASS_TYPE.equals(this.type)) {
            priceMaster = RateCardService.getInstance().getCarcassRate(this.code, thickness, this.priceDate,this.city);

        }
        else {
            priceMaster = RateCardService.getInstance().getShutterRate(this.code, thickness, this.priceDate, this.city);

        }



        double price = 0;
        if (priceMaster != null ) {
            price = priceMaster.getPrice();
        }

        this.ratesUsed.put(thickness,price) ;
        return price;
    }

    public double getRate()
    {
        this.rateUsed = 0;
        PriceMaster factorRate = RateCardService.getInstance().getFactorRate(this.code,this.priceDate, this.city);

        if (factorRate != null) {
            this.rateUsed = factorRate.getPrice();
        }
        return this.rateUsed;
    }

    public String getKey()
    {
        return makeKey(this.getType(), this.getCode());
    }

    public static String makeKey(String type, String code)
    {
        return type + ":" + code;
    }

    public static String makeKey(String type, String code, int thickness)
    {
        return type + ":" + code + ":" + thickness;
    }

    public Object getName()
    {
        return this.getType() + ":" + this.getCode();
    }

    public String getRates()
    {
        if (this.ratesUsed.isEmpty()) return "";

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, Double> entry : this.ratesUsed.entrySet())
        {
            sb.append(entry.getKey() + "=" + entry.getValue()).append(";");
        }
        return sb.toString();
    }
}
