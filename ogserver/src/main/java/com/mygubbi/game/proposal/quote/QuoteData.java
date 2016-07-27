package com.mygubbi.game.proposal.quote;

import com.mygubbi.common.CurrencyUtil;
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

    private List<AssembledProductInQuote> assembledProducts;
    private List<ProductLineItem> catalogueProducts;
    private List<ProductAddon> headerLevelAddons = Collections.emptyList();

    private double productsCost;
    private double addonsCost;

    private double discountAmount;

    public QuoteData(ProposalHeader proposalHeader, List<ProductLineItem> products, List<ProductAddon> addons, double discountAmount)
    {
        this.proposalHeader = proposalHeader;
        this.products = products;
        if (addons != null) this.headerLevelAddons = addons;
        this.discountAmount = discountAmount;
        this.prepare();
    }

    private void prepare()
    {
        this.assembledProducts = new ArrayList<>();
        this.catalogueProducts = new ArrayList<>();

        for (ProductLineItem product : this.products)
        {
            if (product.getType().equals(ProductLineItem.CATALOGUE_PRODUCT))
            {
                this.catalogueProducts.add(product);
                this.productsCost += product.getAmount();
            }
            else
            {
                AssembledProductInQuote assembledProduct = new AssembledProductInQuote(product);
                this.assembledProducts.add(assembledProduct);
                this.productsCost += assembledProduct.getAmountWithoutAddons();
                this.addonsCost += assembledProduct.getAddonsAmount();
            }
        }

        for (ProductAddon addon : this.headerLevelAddons)
        {
            this.addonsCost += addon.getAmount();
        }

        LOG.info("Products cost :" + this.productsCost + ". Addons cost:" + this.addonsCost);
        LOG.info("Discount Amount :" + this.discountAmount);
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

    public Object getValue(String key)
    {
        if (this.proposalHeader.containsKey(key)) return this.proposalHeader.getValue(key);
        switch (key)
        {
            case "date":
                return DateFormatUtils.format(new Date(), "dd-MMM-yyyy");
            case "projectaddress":
                return this.concatValuesFromKeys(new String[]{ProposalHeader.PROJECT_NAME, ProposalHeader.PROJECT_ADDRESS1, ProposalHeader.PROJECT_ADDRESS2, ProposalHeader.PROJECT_CITY}, ",");
            case "salesdesign":
                return this.concatValuesFromKeys(new String[]{ProposalHeader.SALESPERSON_NAME, ProposalHeader.DESIGNER_NAME}, "/");
            case "productscost":
                return round(this.productsCost);
            case "addonscost":
                return round(this.addonsCost);
            case "totalamount":
                return this.getTotalCost();
            case "discountamount":
                return this.discountAmount;
            case "amountafterdiscount":
                return this.getTotalCost()-getDiscountAmount();
            case "totalamountinwords":
                return new CurrencyUtil().convert(String.valueOf(this.getTotalCost() - this.discountAmount));
            default:
                return null;
        }
    }


    public double getDiscountAmount()
    {
        return discountAmount;
    }


    public double getTotalCost()
    {
        return round(this.productsCost + this.addonsCost);
    }
    public double getAmountafterdiscount()
    {
        return this.getTotalCost()-this.discountAmount;
    }

    private String concatValuesFromKeys(String[] keys, String delimiter)
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
                        .groupBy(x -> tuple(x.code, x.make, x.title, x.uom),
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
}
