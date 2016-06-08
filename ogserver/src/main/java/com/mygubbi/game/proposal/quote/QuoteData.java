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
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    private double productsCost;
    private double addonsCost;

    public QuoteData(ProposalHeader proposalHeader, List<ProductLineItem> products)
    {
        this.proposalHeader = proposalHeader;
        this.products = products;
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
            }
            else
            {
                this.assembledProducts.add(new AssembledProductInQuote(product));
            }
            this.productsCost += product.getAmount();

            for (ProductAddon addon : product.getAddons())
            {
                this.addonsCost += addon.getAmount();
            }
        }
        LOG.info("Products cost :" + this.productsCost + ". Addons cost:" + this.addonsCost);
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
                return this.productsCost;
            case "addonscost":
                return this.addonsCost;
            case "totalamount":
                return this.getTotalCost();
            case "totalamountinwords":
                return new CurrencyUtil().convert(String.valueOf(this.getTotalCost()));
            default:
                return null;
        }
    }

    private double getTotalCost()
    {
        return this.productsCost + this.addonsCost;
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


    public List<AssembledProductInQuote.ModuleAccessory> getAllModuleHardware()
    {
        List<AssembledProductInQuote.ModuleAccessory> hwList = new ArrayList<>();
        for (AssembledProductInQuote product : this.assembledProducts)
        {
            hwList.addAll(product.getModuleHardware());
        }

        return this.aggregateComponentQuantity(hwList);
    }

    public List<AssembledProductInQuote.ModuleAccessory> getAllModuleAcessories()
    {
        List<AssembledProductInQuote.ModuleAccessory> accList = new ArrayList<>();
        for (AssembledProductInQuote product : this.assembledProducts)
        {
            accList.addAll(product.getModuleAccessories());
        }

        return this.aggregateComponentQuantity(accList);
    }

    private List<AssembledProductInQuote.ModuleAccessory> aggregateComponentQuantity(List<AssembledProductInQuote.ModuleAccessory> hwList)
    {
        List<AssembledProductInQuote.ModuleAccessory> aggregated =

                Seq.ofType(hwList.stream(), AssembledProductInQuote.ModuleAccessory.class)
                        .groupBy(x -> tuple(x.code, x.make, x.title, x.uom),
                                Tuple.collectors(
                                        Collectors.summingDouble(x -> x.quantity)
                                )
                        )
                        .entrySet()
                        .stream()
                        .map(e -> new AssembledProductInQuote.ModuleAccessory(e.getKey().v1, e.getKey().v3,
                                e.getKey().v2, e.getKey().v4, e.getValue().v1))
                        .collect(Collectors.toList());

        return aggregated;
    }
}