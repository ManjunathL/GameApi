package com.mygubbi.game.proposal;

import com.mygubbi.common.EncryptedFileHandler;
import com.mygubbi.common.StringUtils;
import com.mygubbi.config.ConfigHolder;
import com.mygubbi.game.proposal.model.RateCard;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RateCardService extends AbstractVerticle
{
	private final static Logger LOG = LogManager.getLogger(RateCardService.class);
	private Map<String, RateCard> rateCardMap = Collections.EMPTY_MAP;
	
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
		String ratecardFile = (String) ConfigHolder.getInstance().getStringValue("ratecard_file", "/tmp/ratecard.bin");
		String ratecardKey = (String) ConfigHolder.getInstance().getStringValue("ratecard_key", "my@gubbi2togubbi");
		if (ratecardFile == null || ratecardKey == null)
		{
			startFuture.fail("Ratecard file is not setup in config.");
			return;
		}
		String rateData = new EncryptedFileHandler().decrypt(ratecardKey, ratecardFile);
		if (StringUtils.isEmpty(rateData))
		{
			startFuture.fail("Ratecard file seems to be empty.");
			return;
		}
		this.rateCardMap = new HashMap<>();
		JsonArray ratesJson = new JsonArray(rateData);
		for (Object rateJson : ratesJson)
		{
			RateCard rateCard = new RateCard((JsonObject) rateJson);
			this.rateCardMap.put(rateCard.getKey(), rateCard);
		}
		LOG.info("Rate card data loaded.");
		startFuture.complete();

	}

	public RateCard getRateCard(String code, String type)
	{
        RateCard rateCard = this.rateCardMap.get(RateCard.makeKey(code, type));
        if (rateCard == null)
        {
            LOG.info("Rate card not found for " + code + ":" + type);
        }
        return rateCard;
	}
}
