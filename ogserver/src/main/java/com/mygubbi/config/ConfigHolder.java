package com.mygubbi.config;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConfigHolder extends AbstractVerticle
{
	private static final Logger LOG = LogManager.getLogger(ConfigHolder.class);
	
	private String configFile = "server_conf.json";

	private static ConfigHolder INSTANCE;
	
	private JsonObject serverConfig = new JsonObject();
	
	public ConfigHolder()
	{

	}
	
	public static ConfigHolder getInstance()
	{
		return INSTANCE;
	}
	
	public Object getConfigValue(String key)
	{
		return this.serverConfig.getValue(key);
	}
	
	@Override
	public void start(Future<Void> startFuture) throws Exception
	{
		vertx.fileSystem().readFile(this.configFile, result -> {
		    if (result.succeeded()) 
		    {
		    	this.serverConfig = new JsonObject(result.result().toString());
		        LOG.info("read config file : " + this.configFile + ". Value:" + result.result().toString());
		        startFuture.complete();
		    } 
		    else 
		    {
		    	this.serverConfig = new JsonObject();
		    	String message = "Could not read config file : " + this.configFile + ". Cause:" + result.cause();
		        LOG.error(message);
		        startFuture.fail(message);
		    }
		});
		INSTANCE = this;
	}
	
}
