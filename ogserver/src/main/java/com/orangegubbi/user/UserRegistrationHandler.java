package com.orangegubbi.user;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.orangegubbi.common.LocalCache;
import com.orangegubbi.db.QueryData;

public class UserRegistrationHandler extends AbstractVerticle
{
	private final static Logger LOG = LogManager.getLogger(UserRegistrationHandler.class);

	@Override
	public void start(Future<Void> startFuture) throws Exception
	{
		super.start();
		try
		{
			this.setupMessageHandler();
			startFuture.complete();
		}
		catch (Exception e)
		{
			LOG.error("Error in starting database service.", e);
			startFuture.fail(e);
		}
	}

	private void setupMessageHandler() throws Exception
	{
		vertx.eventBus().consumer("user.register", (Message<JsonObject> message) -> {
			this.checkAndCreateNewUser(message);
		});
	}

	private void checkAndCreateNewUser(Message<JsonObject> message)
	{
		JsonObject paramsObject = message.body();
		Integer id = LocalCache.getInstance().store(new QueryData("user_profile.select.id", paramsObject));
		vertx.eventBus().send("db.query", id,  
				(AsyncResult<Message<Integer>> selectResult) -> {
			QueryData selectData = (QueryData) LocalCache.getInstance().remove(selectResult.result().body());
			LOG.info("Check query (ms):" + selectData.responseTimeInMillis);
			if (selectData.rows.isEmpty())
			{
				this.createNewUser(message);
			}
			else
			{
				message.reply(new JsonObject().put("status", "error").put("error", "User profile already exists.").toString());
			}
		});
	}

	private void createNewUser(Message<JsonObject> message)
	{
		Integer id = LocalCache.getInstance().store(new QueryData("user_profile.insert", message.body()));
		vertx.eventBus().send("db.query", id,  
				(AsyncResult<Message<Integer>> res) -> {
			QueryData resultData = (QueryData) LocalCache.getInstance().remove(res.result().body());
			if (resultData.errorFlag || resultData.updateResult.getUpdated() == 0)
			{
				message.reply(new JsonObject().put("status", "error").put("error", "Error in creating user profile.").toString());
			}
			else
			{
				LOG.info("Insert query (ms):" + resultData.responseTimeInMillis);
				this.sendNewUserProfile(message);
			}
		});
	}

	private void sendNewUserProfile(Message<JsonObject> message)
	{
		Integer id = LocalCache.getInstance().store(new QueryData("user_profile.select.id", message.body()));
		vertx.eventBus().send("db.query", id,  
				(AsyncResult<Message<Integer>> res2) -> {
			QueryData selectData = (QueryData) LocalCache.getInstance().remove(res2.result().body());
			JsonObject userProfile = selectData.rows.get(0);
			LOG.info("Select query (ms):" + selectData.responseTimeInMillis);
			message.reply(new JsonObject().put("status", "success").put("data", userProfile).toString());
			vertx.eventBus().send("send.mail", new JsonObject().put("template", "user.register").put("data", userProfile));
		});
	}
	
}
