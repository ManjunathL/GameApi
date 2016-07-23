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
    private static final String PRODUCT_IDS = "productIds";
    private static final String DISCOUNT_AMOUNT = "discountAmount";

    private int proposalId;
    private List<Integer> productsIds;
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
        if (jsonData.containsKey(PRODUCT_IDS))
        {
            this.setProductsIds(jsonData.getJsonArray(PRODUCT_IDS).getList());
        }
        if (jsonData.containsKey(DISCOUNT_AMOUNT))
        {
            this.discountAmount = jsonData.getDouble(DISCOUNT_AMOUNT);
        }
    }

    public ProposalOutputCreator.OutputType getOutputType()
    {
        return outputType;
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

    public double getDiscountAmount()
    {
        return this.discountAmount;
    }
}
