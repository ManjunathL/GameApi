package com.mygubbi.game.proposal.quote;

import com.mygubbi.common.StringUtils;
import com.mygubbi.game.proposal.output.ProposalOutputCreator;
import io.vertx.core.json.JsonObject;

import java.util.List;

/**
 * Created by test on 29-05-2016.
 */
public class QuoteRequest
{
    private static final String PROPOSAL_ID = "proposalId";
    private static final String FROMVERSION="fromVersion";
    private static final String PRODUCT_IDS = "productIds";
    private static final String DISCOUNT_AMOUNT = "discountAmount";
    private static final String ADDON_IDS = "addonIds";
    private static final String VERSION_IDS = "versionIds";

    private int proposalId;
    private String fromVersion;
    private List<Integer> productsIds;
    private List<Integer> addonIds;
    private List<Integer> versionIds;
    private double discountAmount = 0.0;
    private ProposalOutputCreator.OutputType outputType;
    
    public QuoteRequest(int proposalId)
    {
        this.proposalId = proposalId;
    }

    public QuoteRequest(JsonObject jsonData, ProposalOutputCreator.OutputType outputType)
    {
        this(jsonData.getInteger(PROPOSAL_ID));
        this.outputType = outputType;
        if(jsonData.containsKey(FROMVERSION))
        {
            this.setFromVersion(jsonData.getString(FROMVERSION));
        }
        if (jsonData.containsKey(PRODUCT_IDS))
        {
            this.setProductsIds(jsonData.getJsonArray(PRODUCT_IDS).getList());
        }
        if (jsonData.containsKey(ADDON_IDS))
        {
            this.setAddonIds(jsonData.getJsonArray(ADDON_IDS).getList());
        }
        if (jsonData.containsKey(VERSION_IDS))
        {
            this.setVersionIds(jsonData.getJsonArray(VERSION_IDS).getList());
        }
        if (jsonData.containsKey(DISCOUNT_AMOUNT))
        {
            this.discountAmount = jsonData.getDouble(DISCOUNT_AMOUNT);
        }
    }

    public void setFromVersion(String fromVersion) {
        this.fromVersion = fromVersion;
    }

    public ProposalOutputCreator.OutputType getOutputType()
    {
        return outputType;
    }

    public int getProposalId()
    {
        return this.proposalId;
    }

    public String getFromVersion() {
        return fromVersion;
    }

    public List<Integer> getProductIds()
    {
        return this.productsIds;
    }

    public void setProductsIds(List<Integer> productsIds)
    {
        this.productsIds = productsIds;
    }

    public boolean hasProductIds()
    {
        return (this.productsIds != null) && (this.productsIds.size() > 0);
    }

    public String getProductIdsAsText()
    {
        return StringUtils.listToString(this.productsIds, ',');
    }

    public List<Integer> getAddonIds() {
        return addonIds;
    }

    public void setAddonIds(List<Integer> addonIds) {
        this.addonIds = addonIds;
    }

    public boolean hasAddonIds()
    {
        return (this.addonIds != null) && (this.addonIds.size() > 0);
    }

    public String getAddonIdsAsText()
    {
        return StringUtils.listToString(this.addonIds, ',');
    }

    public List<Integer> getVersionIds() {
        return addonIds;
    }

    public void setVersionIds(List<Integer> addonIds) {
        this.addonIds = addonIds;
    }

    public boolean hasVersionIds()
    {
        return (this.addonIds != null) && (this.addonIds.size() > 0);
    }

    public String getVersionIdsAsText()
    {
        return StringUtils.listToString(this.addonIds, ',');
    }

    public double getDiscountAmount()
    {
        return this.discountAmount;
    }
}
