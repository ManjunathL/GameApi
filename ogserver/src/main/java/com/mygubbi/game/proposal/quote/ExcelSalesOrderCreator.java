package com.mygubbi.game.proposal.quote;

import com.mygubbi.si.excel.ExcelWorkbookManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Sunil on 22-05-2016.
 */
public class ExcelSalesOrderCreator
{
    private final static Logger LOG = LogManager.getLogger(ExcelSalesOrderCreator.class);

    private String quoteXls;
    private QuoteData quoteData;
    private ExcelWorkbookManager workbookManager;

    public ExcelSalesOrderCreator(String quoteXls, QuoteData quoteData)
    {
        this.quoteXls = quoteXls;
        this.quoteData = quoteData;
        this.workbookManager = new ExcelWorkbookManager(this.quoteXls);
    }

    public void prepare()
    {
        //Modules first followed by panels in accessory packs
        //Hardware from accessory packs
        //Accessories from accessory packs and addon accessories
        //Addons - Appliances

        this.closeWorkbook();
    }

    private void closeWorkbook()
    {
        this.workbookManager.closeWorkbook();
    }


}
