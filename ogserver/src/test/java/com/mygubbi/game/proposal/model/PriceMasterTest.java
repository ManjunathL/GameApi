package com.mygubbi.game.proposal.model;

import junit.framework.TestCase;

import java.sql.Date;

/**
 * Created by Chirag on 06-03-2017.
 */
public class PriceMasterTest extends TestCase {

    public void testValidRecord() {
        PriceMaster priceMaster = new PriceMaster();
        priceMaster.setCity(PriceMaster.ALL_CITIES).setFromDate(new Date(2017, 03, 01)).setPrice(100)
                .setRateId("S:M08:18").setRateType("R").setSourcePrice(90).setToDate(new Date(2018, 03, 01));
        assertTrue(priceMaster.isValidForDate(new Date(2017, 03, 02)));
        assertFalse(priceMaster.isValidForDate(new Date(2017, 02, 02)));
        assertFalse(priceMaster.isValidForDate(new Date(2018, 04, 02)));
    }
}
