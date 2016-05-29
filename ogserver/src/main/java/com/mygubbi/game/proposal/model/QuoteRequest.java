package com.mygubbi.game.proposal.model;

import com.mygubbi.common.StringUtils;
import io.vertx.core.json.JsonObject;

import java.util.List;

/**
 * Created by test on 29-05-2016.
 */
public class QuoteRequest
{
    private static final String PROPOSAL_ID = "proposalId";
    private static final String PRODUCT_IDS = "productIds";

    private int proposalId;
    private List<Integer> productsIds;

    public QuoteRequest(int proposalId)
    {
        this.proposalId = proposalId;
    }

    public QuoteRequest(JsonObject jsonData)
    {
        this(jsonData.getInteger(PROPOSAL_ID));
        if (!jsonData.containsKey(PRODUCT_IDS)) return;
        this.setProductsIds(jsonData.getJsonArray(PRODUCT_IDS).getList());
    }

    public int getProposalId()
    {
        return this.proposalId;
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
}
