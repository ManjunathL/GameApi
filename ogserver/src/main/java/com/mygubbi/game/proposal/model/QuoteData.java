package com.mygubbi.game.proposal.model;

import com.mygubbi.common.CurrencyUtil;
import com.mygubbi.common.StringUtils;
import com.mygubbi.game.proposal.ProductAddon;
import com.mygubbi.game.proposal.ProductLineItem;
import org.apache.commons.lang.time.DateFormatUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Sunil on 22-05-2016.
 */
public class QuoteData
{
    private ProposalHeader proposalHeader;
    private List<ProductLineItem> products;

    private List<AssembledProductInQuote> assembledProducts;
    private List<ProductLineItem> catalogueProducts;

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
            if (product.getType().equals(ProductLineItem.CUSTOMIZED_PRODUCT))
                this.assembledProducts.add(new AssembledProductInQuote(product));
            else
                this.catalogueProducts.add(product);
        }
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
        return getAddonsOfType(ProductAddon.ACCESSORY_TYPE);
    }

    public List<ProductAddon> getAppliances()
    {
        return getAddonsOfType(ProductAddon.APPLIANCE_TYPE);
    }

    public List<ProductAddon> getCounterTops()
    {
        return getAddonsOfType(ProductAddon.COUNTERTOP_TYPE);
    }

    public List<ProductAddon> getServices()
    {
        return getAddonsOfType(ProductAddon.SERVICE_TYPE);
    }

    private List<ProductAddon> getAddonsOfType(String type)
    {
        List<ProductAddon> addons = new ArrayList<>();
        for (ProductLineItem product : this.products)
        {
            for (ProductAddon addon : product.getAddons())
            {
                if (type.equals(addon.getType())) addons.add(addon);
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
            case "totalamountinwords":
                return new CurrencyUtil().convert(this.proposalHeader.getAmount().toString());
            default:
                return null;
        }
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


}
