package com.mygubbi.game.proposal.quote;

import com.mygubbi.common.StringUtils;
import com.mygubbi.game.proposal.ProductAddon;
import com.mygubbi.game.proposal.ProductLineItem;
import com.mygubbi.game.proposal.model.ProposalHeader;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.lambda.Seq;
import org.jooq.lambda.tuple.Tuple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.round;
import static org.jooq.lambda.tuple.Tuple.tuple;

/**
 * Created by Sunil on 22-05-2016.
 */
public class QuoteData
{
    private final static Logger LOG = LogManager.getLogger(QuoteData.class);

    private ProposalHeader proposalHeader;
    private List<ProductLineItem> products;

    NumberToWord word=new NumberToWord();

    private List<AssembledProductInQuote> assembledProducts;
    private List<ProductLineItem> catalogueProducts;
    private List<ProductAddon> headerLevelAddons = Collections.emptyList();

    public double productsCost;
    public double addonsCost;

    public double discountAmount;
    public String fromVersion;
    private String city;
    private String title;
    private java.sql.Date priceDate;
    private String bookingFormFlag;
    public QuoteData(ProposalHeader proposalHeader, List<ProductLineItem> products, List<ProductAddon> addons, double discountAmount,String fromVersion,String bookingFormFlag)
    {
        this.city = proposalHeader.getProjectCity();
        this.priceDate = proposalHeader.getPriceDate();
        if(products != null) {
            for (ProductLineItem productLineItem : products) {
                this.title = productLineItem.getTitle();
            }
        }
        if (this.priceDate == null)
        {
            this.priceDate = new java.sql.Date(System.currentTimeMillis());
        }
        this.proposalHeader = proposalHeader;
        this.products = products;
        if (addons != null) this.headerLevelAddons = addons;
        this.discountAmount = discountAmount;
        this.fromVersion=fromVersion;
        this.bookingFormFlag=bookingFormFlag;
        this.prepare();
    }

    public QuoteData(ProposalHeader proposalHeader,String fromVersion)
    {
        this.city = proposalHeader.getProjectCity();
        this.priceDate = proposalHeader.getPriceDate();
        if (this.priceDate == null)
        {
            this.priceDate = new java.sql.Date(System.currentTimeMillis());
        }
        this.proposalHeader = proposalHeader;
        this.products = products;
        this.discountAmount = discountAmount;
        this.fromVersion=fromVersion;
        this.prepare();
    }

    private void prepare()
    {
        this.assembledProducts = new ArrayList<>();
        this.catalogueProducts = new ArrayList<>();

        if(this.products != null) {
            for (ProductLineItem product : this.products) {
                if (product.getType().equals(ProductLineItem.CATALOGUE_PRODUCT)) {
                    this.catalogueProducts.add(product);
                    this.productsCost += product.getAmount();
                } else {
                    AssembledProductInQuote assembledProduct = new AssembledProductInQuote(product, this.city, this.priceDate);
                    this.assembledProducts.add(assembledProduct);
                    this.productsCost += assembledProduct.getAmountWithoutAddons();
                    this.addonsCost += assembledProduct.getAddonsAmount();
                }
            }
        }
        for (ProductAddon addon : this.headerLevelAddons)
        {
            this.addonsCost += addon.getAmount();
        }

        LOG.info("Products cost :" + this.productsCost + ". Addons cost:" + this.addonsCost);
        LOG.info("Discount Amount :" + this.discountAmount);
        LOG.info("Version number: " +this.fromVersion);
    }

    public List<AssembledProductInQuote> getAssembledProducts()
    {
        return this.assembledProducts;
    }

    public List<ProductLineItem> getCatalogueProducts()
    {
        return this.catalogueProducts;
    }

    public int getCatalogStartSequence()
    {
        return this.assembledProducts.size() + 1;
    }

    public List<ProductAddon> getAccessories()
    {
        return getAddonsByCategory(ProductAddon.ACCESSORY_TYPE);
    }

    public List<ProductAddon> getAppliances()
    {
        return getAddonsByCategory(ProductAddon.APPLIANCE_TYPE);
    }

    public List<ProductAddon> getCounterTops()
    {
        return getAddonsByCategory(ProductAddon.COUNTERTOP_TYPE);
    }

    public List<ProductAddon> getServices()
    {
        return getAddonsByCategory(ProductAddon.SERVICE_TYPE);
    }

    public List<ProductAddon> getCustomAddons()
    {
        return getAddonsByCategory(ProductAddon.CUSTOM_ADDON_TYPE);
    }

    public List<ProductAddon> getLooseFurniture()
    {
        return getAddonsByCategory(ProductAddon.LOOSE_FURNITURE_TYPE);
    }

    private List<ProductAddon> getAddonsByCategory(String categoryCode)
    {
        List<ProductAddon> addons = new ArrayList<>();
        for (ProductLineItem product : this.products)
        {
            for (ProductAddon addon : product.getAddons())
            {
                if (categoryCode.equals(addon.getCategoryCode())) addons.add(addon);
            }
        }
        for (ProductAddon addon : this.headerLevelAddons)
        {
            if (categoryCode.equals(addon.getCategoryCode())) addons.add(addon);
        }
        return addons;
    }

    public List<ProductAddon> getHeaderLevelAddons()
    {
        return  this.headerLevelAddons;
    }
    public Object getValue(String key)
    {
        if (this.proposalHeader.containsKey(key)) return this.proposalHeader.getValue(key);
        float fromVersionFloat = Float.parseFloat(this.fromVersion);
        switch (key)
        {
            case "date":
                return DateFormatUtils.format(new Date(), "dd-MMM-yyyy");
            case "qno":
                String vnum=this.fromVersion;
                vnum=vnum.replace(".","");
                if(proposalHeader.getQuoteNum()==null || proposalHeader.getQuoteNum().equals(""))
                {
                LOG.info("");
                String strqnum= proposalHeader.getQuoteNumNew()+ "."+ vnum;
                return strqnum;

                }
                else {
                    return proposalHeader.getQuoteNum() + "." +vnum;
                }
            case "projectaddress":
                return this.concatValuesFromKeys(new String[]{ProposalHeader.PROJECT_NAME, ProposalHeader.PROJECT_ADDRESS1, ProposalHeader.PROJECT_ADDRESS2, ProposalHeader.PROJECT_CITY}, ",");
            case "salesdesign":
                return this.concatValuesFromKeys(new String[]{ProposalHeader.SALESPERSON_NAME, ProposalHeader.DESIGNER_NAME}, "/");
            case "productscost":
                 return this.getRoundOffValue(String.valueOf((int)this.productsCost));
                //return round(this.productsCost);
            case "addonscost":
                return this.getRoundOffValue(String.valueOf((int)this.addonsCost));
                //return round(this.addonsCost);
            case "totalamount":
                return this.getRoundOffValue(String.valueOf((int)this.getTotalCost()));
                //return this.getTotalCost();
            case "discountamount":
                return this.getRoundOffValue(String.valueOf((int)this.discountAmount));
                //return (int)this.discountAmount;
            case "fromVersion":
                LOG.info("version number in case" +this.fromVersion);
                return (fromVersionFloat)/10;
            case "amountafterdiscount1":
                Double grandTotal=this.getTotalCost()-getDiscountAmount();
                Double res=grandTotal-grandTotal%10;
                LOG.info("Val" +this.getRoundOffValue(String.valueOf(res.intValue())));
                return this.getRoundOffValue(String.valueOf(res.intValue()));
                //return res;
            case "totalamountinwords":
                double val=this.getTotalCost() - this.discountAmount;
                Double rem=val-val%10;
                return word.convertNumberToWords(rem.intValue()) + " Rupees Only";
                //return new CurrencyUtil().convert(String.valueOf((int)(this.getTotalCost() - this.discountAmount)));
            case "city":
                return this.city;
            case "product.title":
                return this.title;
            case "clientno":
                return this.proposalHeader.getQuoteNumNew();
            case "clientdetails":
                return this.proposalHeader.getName();
            default:
                return null;
        }
    }


    public double getDiscountAmount()
    {
        return discountAmount;
    }

    public String getBookingFormFlag(){
        return bookingFormFlag;
    }

    public double getTotalCost()
    {
        return round(this.productsCost + this.addonsCost);
    }
    public double getAmountafterdiscount()
    {
        return this.getTotalCost()-this.discountAmount;
    }

    public String concatValuesFromKeys(String[] keys, String delimiter)
    {
        int size = keys.length;
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<size; i++)
        {
            String value = this.proposalHeader.containsKey(keys[i]) ? this.proposalHeader.getString(keys[i]) : null;
            if (StringUtils.isNonEmpty(value))
            {
                if (sb.length() > 0) sb.append(delimiter).append(" ");
                sb.append(value);
            }
        }
        return sb.toString();
    }


    public List<AssembledProductInQuote.ModulePart> getAllModuleHardware()
    {
        List<AssembledProductInQuote.ModulePart> hwList = new ArrayList<>();
        for (AssembledProductInQuote product : this.assembledProducts)
        {
            hwList.addAll(product.getModuleHardware());
        }

        return this.aggregateComponentQuantity(hwList);
    }

    public List<AssembledProductInQuote.ModulePart> getAllModuleAcessories()
    {
        List<AssembledProductInQuote.ModulePart> accList = new ArrayList<>();
        for (AssembledProductInQuote product : this.assembledProducts)
        {
            accList.addAll(product.getModuleAccessories());
        }

        return this.aggregateComponentQuantity(accList);
    }

    private List<AssembledProductInQuote.ModulePart> aggregateComponentQuantity(List<AssembledProductInQuote.ModulePart> hwList)
    {
        List<AssembledProductInQuote.ModulePart> aggregated =

                Seq.ofType(hwList.stream(), AssembledProductInQuote.ModulePart.class)
                        .groupBy(x -> tuple(x.code, x.make, x.title, x.uom,x.catalogCode),
                                Tuple.collectors(
                                        Collectors.summingDouble(x -> x.quantity)
                                )
                        )
                        .entrySet()
                        .stream()
                        .map(e -> new AssembledProductInQuote.ModulePart(e.getKey().v1, e.getKey().v3,
                                e.getKey().v2, e.getKey().v4, e.getValue().v1))
                        .collect(Collectors.toList());

        return aggregated;
    }

    public ProposalHeader getProposalHeader()
    {
        return proposalHeader;
    }
    public static String getRoundOffValue(String value)
    {
        value=value.replace(",","");
        char lastDigit=value.charAt(value.length()-1);
        String result = "";
        int len = value.length()-1;
        int nDigits = 0;

        for (int i = len - 1; i >= 0; i--)
        {
            result = value.charAt(i) + result;
            nDigits++;
            if (((nDigits % 2) == 0) && (i > 0))
            {
                result = "," + result;
            }
        }
        return (result+lastDigit);
    }


    @Override
    public String toString() {
        return "QuoteData{" +
                "addonsCost=" + addonsCost +
                ", proposalHeader=" + proposalHeader +
                ", products=" + products +
                ", word=" + word +
                ", assembledProducts=" + assembledProducts +
                ", catalogueProducts=" + catalogueProducts +
                ", headerLevelAddons=" + headerLevelAddons +
                ", productsCost=" + productsCost +
                ", discountAmount=" + discountAmount +
                ", fromVersion='" + fromVersion + '\'' +
                ", city='" + city + '\'' +
                ", priceDate=" + priceDate +
                '}';
    }
}
