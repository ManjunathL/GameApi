package com.mygubbi.game.proposal.quote;

import com.mygubbi.game.proposal.output.AbstractProposalOutputCreator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Sunil on 22-05-2016.
 */
public class ExcelSalesOrderCreator extends AbstractProposalOutputCreator
{
    private final static Logger LOG = LogManager.getLogger(ExcelSalesOrderCreator.class);

    public ExcelSalesOrderCreator(QuoteData quoteData)
    {
        super(quoteData);
    }

    public String getTemplateName()
    {
        return "salesorder_template";
    }

    public String getOutputFilename()
    {
        return "salesorder.xlsx";
    }

    public void create()
    {
        //Modules first followed by panels in accessory packs
        //Hardware from accessory packs
        //Accessories from accessory packs and addon accessories
        //Addons - Appliances

        this.openWorkbook();

        this.closeWorkbook();
    }

    @Override
    public String getOutputKey()
    {
        return "salesorderFile";
    }

}
