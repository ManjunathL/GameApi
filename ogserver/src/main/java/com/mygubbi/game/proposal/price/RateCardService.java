package com.mygubbi.game.proposal.price;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import com.mygubbi.game.proposal.model.*;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Date;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RateCardService extends AbstractVerticle
{
	private final static Logger LOG = LogManager.getLogger(RateCardService.class);
	//private Map<String, RateCardMaster> rateCardMap = Collections.EMPTY_MAP;
	private Multimap<PriceMasterKey, PriceMaster> priceMasterMap;
	private Multimap<RateCardMasterKey, RateCardMaster> rateCardMap;

	private static RateCardService INSTANCE;

	public static RateCardService getInstance()
	{
		return INSTANCE;
	}

	@Override
	public void start(Future<Void> startFuture) throws Exception
	{
		this.setupRateCards(startFuture);
		INSTANCE = this;
	}

	private void setupRateCards(Future<Void> startFuture)
	{
		VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, LocalCache.getInstance().store(new QueryData("ratecard.distinct.select.all", new JsonObject())),
				(AsyncResult<Message<Integer>> dataResult) -> {
					QueryData selectData = (QueryData) LocalCache.getInstance().remove(dataResult.result().body());
					if (selectData == null || selectData.rows == null || selectData.rows.isEmpty())
					{
						LOG.debug("Rate Card master table is empty.", false);
						startFuture.fail("Rate card master is empty");
						return;
					}
					else
					{
						this.rateCardMap= ArrayListMultimap.create();
						for (JsonObject record : selectData.rows)
						{
							RateCardMaster rateCardMaster = new RateCardMaster(record);;
							this.rateCardMap.put(rateCardMaster.getKey(), rateCardMaster);
							LOG.debug("Setting rate card master for :" + rateCardMaster.toString());

						}
						LOG.debug("Rate Card master done.", true);
						setupPriceMasters(startFuture);
					}
				});
	}

	private void setupPriceMasters(Future<Void> startFuture)
	{
		VertxInstance.get().eventBus().send(DatabaseService.DB_QUERY, LocalCache.getInstance().store(new QueryData("pricemaster.select.all", new JsonObject())),
				(AsyncResult<Message<Integer>> dataResult) -> {
					QueryData selectData = (QueryData) LocalCache.getInstance().remove(dataResult.result().body());
					if (selectData == null || selectData.rows == null || selectData.rows.isEmpty())
					{
						LOG.debug("Price master table is empty.", false);
						startFuture.fail("Price master is empty");
						return;
					}
					else
					{
						this.priceMasterMap = ArrayListMultimap.create();
						for (JsonObject record : selectData.rows)
						{
							PriceMaster priceMaster = new PriceMaster(record);
							this.priceMasterMap.put(priceMaster.getKey(), priceMaster);
							LOG.debug("Setting price master for :" + priceMaster.getKey().toString());
						}
						LOG.debug("Price master done.", true);
						startFuture.complete();
					}
				});
	}

	public RateCard getRateCard(String code, String type, Date priceDate, String city)
	{
		RateCardMasterKey key = new RateCardMasterKey(code, type,"all");
		Collection<RateCardMaster> rateCardMaster = this.rateCardMap.get(key);
        if (rateCardMaster == null)
        {
            LOG.info("Rate card not found for " + type + ":" + code);
        }
        return new RateCard(code,type,priceDate, city);
	}

	public RateCard getRateCardBasedOnProduct(String code, String type, Date priceDate, String city, String productCategory)
	{
//		LOG.debug("Rate card based on product");
		RateCardMasterKey key = new RateCardMasterKey(code, type,"all");
		Collection<RateCardMaster> rateCardMaster = this.rateCardMap.get(key);
//		LOG.debug("Rate card map :" + rateCardMap.values().toString() );
//		LOG.debug("Rate card found on product");
		if (rateCardMaster == null)
        {
            LOG.info("Rate card not found for " + type + ":" + code + ":" + productCategory);
        }
        return new RateCard(code,type,priceDate, city, productCategory);
	}

	public PriceMaster getAddonRate(String code, Date priceDate, String city)
	{
//		LOG.info("priceDate = "+priceDate);
		return getPriceMaster(priceDate, city, code, PriceMasterKey.ADDON_TYPE);

	}

	public PriceMaster getAccessoryRate(String code, Date priceDate, String city)
	{
		return getPriceMaster(priceDate, city, code, PriceMasterKey.ACESSORY_TYPE);

	}

	public PriceMaster getHardwareRate(String code, Date priceDate, String city)
	{
		return getPriceMaster(priceDate, city, code, PriceMasterKey.HARDWARE_TYPE);

	}

	public PriceMaster getHandleOrKnobRate(String code, Date priceDate, String city)
	{
		return getPriceMaster(priceDate, city, code, PriceMasterKey.KNOB_OR_HANDLE_TYPE);

	}

	public PriceMaster getHingeRate(String code, Date priceDate, String city)
	{
		return getPriceMaster(priceDate, city, code, PriceMasterKey.HINGE_TYPE);

	}

	public PriceMaster getFactorRate(String code, Date priceDate, String city)
	{
		String rateCardID = RateCard.makeKey(RateCard.FACTOR_TYPE,code);

		return getPriceMaster(priceDate, city, rateCardID, PriceMasterKey.RATECARD_TYPE);
	}

	public PriceMaster getFactorRate(String code, Date priceDate, String city, String productCategory)
	{
		String rateCardID = RateCard.makeKey(RateCard.FACTOR_TYPE,code,productCategory);

		return getPriceMaster(priceDate, city, rateCardID, PriceMasterKey.RATECARD_TYPE);
	}

	private PriceMaster getPriceMaster(Date priceDate, String city, String rateCardID, String ratecardType) {

		PriceMaster priceMaster = checkPriceMasterForCity(priceDate, city, rateCardID, ratecardType);
		if (priceMaster == null) {
			priceMaster = checkPriceMasterForCity(priceDate, PriceMaster.ALL_CITIES, rateCardID, ratecardType);
		}
//		LOG.info("Returning price master :" + priceMaster);
		return priceMaster;
	}

	public PriceMaster getShutterRate(String code, int thickness, Date priceDate, String city)
	{
//		LOG.debug("get shutter rate : " + code + ":" + thickness + ":" + priceDate + ":" + city);
		String rateCardID = RateCard.makeKey(RateCard.SHUTTER_TYPE,code,thickness);
		return getPriceMaster(priceDate, city, rateCardID, PriceMasterKey.RATECARD_TYPE);
	}

	public PriceMaster getCarcassRate(String code, int thickness, Date priceDate, String city)
	{
//		LOG.debug("get Carcass rate : " + code + ":" + thickness + ":" + priceDate + ":" + city);
		String rateCardID = RateCard.makeKey(RateCard.CARCASS_TYPE,code,thickness);
		return getPriceMaster(priceDate, city, rateCardID, PriceMasterKey.RATECARD_TYPE);
	}

	private PriceMaster checkPriceMasterForCity(Date priceDate, String city, String rateCardID, String ratecardType) {
		PriceMasterKey key = new PriceMasterKey(ratecardType, rateCardID, city);
//		LOG.debug("price Master key : " + key.toString());
		Collection<PriceMaster> priceList = this.priceMasterMap.get(key);
//		LOG.debug("Check price master for " + key.toString() + " result:" + priceList.size());
		for (PriceMaster priceMaster : priceList)
		{
			if (priceMaster.isValidForDate(priceDate)) {
				return priceMaster;
			}
		}
		return null;
	}
}
