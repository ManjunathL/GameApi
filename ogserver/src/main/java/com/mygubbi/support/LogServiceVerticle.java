package com.mygubbi.support;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Sunil
 *
 * This will be the central monitor for all services in the JVM as well as memory, cpu usage etc.
 * It will expose key data to outside world and cut any support events.
 * Work that could not be done is logged in a pending task ledger for retry
 * On fatal events, it will bring down the JVM or just undeploy all the verticles except itself
 *  
 */
public class LogServiceVerticle extends AbstractVerticle
{
	private final static Logger LOG = LogManager.getLogger(LogServiceVerticle.class);

	@Override
	public void start(Future<Void> startFuture) throws Exception
	{
		try
		{
			this.setupMessageHandler();
			startFuture.complete();
			LOG.info("Log service started.");
		}
		catch (Exception e)
		{
			LOG.error("Error in starting Log service.", e);
			startFuture.fail(e);
		}
	}
	
	private void setupMessageHandler()
	{
		EventBus eb = vertx.eventBus();
		eb.localConsumer("log.data", (Message<String> message) -> {
			LOG.info("Message received: " + message.body());
			message.reply("Hello! " + message.body());
		}).completionHandler(res -> {
			LOG.info("Log Service handler registered.");
		});
	}

	public static void main(String[] args)
	{
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(LogServiceVerticle.class.getCanonicalName(), result ->
		{
			if (result.succeeded())
			{
				vertx.eventBus().send("log.data", "Log Me.", (AsyncResult<Message<String>> result2) -> {
					LOG.info("Got back - " + result2.result().body());
				});
			}
			else
			{
				LOG.error("Log service did not start. Message:" + result.result(), result.cause());
			}
		});
	}
	
}
