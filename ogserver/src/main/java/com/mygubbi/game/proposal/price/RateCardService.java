package com.mygubbi.game.proposal.price;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryData;
import com.mygubbi.game.proposal.model.PriceMaster;
import com.mygubbi.game.proposal.model.PriceMasterKey;
import com.mygubbi.game.proposal.model.RateCard;
import com.mygubbi.game.proposal.model.RateCardMaster;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.Date;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RateCardService extends AbstractVerticle
{
	private final static Logger LOG = LogManager.getLogger(RateCardService.class);
	private Map<String, RateCardMaster> rateCardMap = Collections.EMPTY_MAP;
	private Multimap<PriceMasterKey, PriceMaster> priceMasterMap;

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
						LOG.debug("Rate Card master table is empty.");
						startFuture.fail("Rate card master is empty");
						return;
					}
					else
					{
						this.rateCardMap= new HashMap(selectData.rows.size());
						for (JsonObject record : selectData.rows)
						{
							RateCardMaster rateCardMaster = RateCardMaster.fromJson(record);
							this.rateCardMap.put(rateCardMaster.getCodeTypeKey(), rateCardMaster);
						}
						LOG.debug("Rate Card master done.");
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
						LOG.debug("Price master table is empty.");
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
						LOG.debug("Price master done.");
						startFuture.complete();
					}
				});
	}

	public RateCard getRateCard(String code, String type, Date priceDate, String city)
	{
		RateCardMaster rateCardMaster = this.rateCardMap.get(RateCardMaster.makeKey(type, code));
        if (rateCardMaster == null)
        {
            LOG.info("Rate card not found for " + type + ":" + code);
        }
        return new RateCard(code,type,priceDate, city);
	}

	public PriceMaster getAddonRate(String code, Date priceDate, String city)
	{
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

	public PriceMaster getFactorRate(String code, Date priceDate, String city)
	{
		String rateCardID = RateCard.makeKey(RateCard.FACTOR_TYPE,code);

		return getPriceMaster(priceDate, city, rateCardID, PriceMasterKey.RATECARD_TYPE);
	}

	private PriceMaster getPriceMaster(Date priceDate, String city, String rateCardID, String ratecardType) {

		PriceMaster priceMaster = checkPriceMasterForCity(priceDate, city, rateCardID, ratecardType);
		if (priceMaster == null) {
			priceMaster = checkPriceMasterForCity(priceDate, PriceMaster.ALL_CITIES, rateCardID, ratecardType);
		}
		LOG.info("Returning price master :" + priceMaster);
		return priceMaster;
	}

	public PriceMaster getShutterRate(String code, int thickness, Date priceDate, String city)
	{
		LOG.debug("get shutter rate : " + code + ":" + thickness + ":" + priceDate + ":" + city);
		String rateCardID = RateCard.makeKey(RateCard.SHUTTER_TYPE,code,thickness);
		return getPriceMaster(priceDate, city, rateCardID, PriceMasterKey.RATECARD_TYPE);
	}

	public PriceMaster getCarcassRate(String code, int thickness, Date priceDate, String city)
	{
		LOG.debug("get Carcass rate : " + code + ":" + thickness + ":" + priceDate + ":" + city);
		String rateCardID = RateCard.makeKey(RateCard.CARCASS_TYPE,code,thickness);
		return getPriceMaster(priceDate, city, rateCardID, PriceMasterKey.RATECARD_TYPE);
	}

	private PriceMaster checkPriceMasterForCity(Date priceDate, String city, String rateCardID, String ratecardType) {
		PriceMasterKey key = new PriceMasterKey(ratecardType, rateCardID, city);
		Collection<PriceMaster> priceList = this.priceMasterMap.get(key);
		LOG.debug("Check price master for " + key.toString() + " result:" + priceList.size());
		for (PriceMaster priceMaster : priceList)
		{
			if (priceMaster.isValidForDate(priceDate)) {
				return priceMaster;
			}
		}
		return null;
	}
}
