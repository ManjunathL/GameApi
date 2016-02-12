package com.mygubbi.config;

import com.mygubbi.common.VertxInstance;import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public class ConfigHolder extends AbstractVerticle
{
	private static final Logger LOG = LogManager.getLogger(ConfigHolder.class);

	private static ConfigHolder INSTANCE;

	private String baseConfigFile = "config/conf.base.json";
	private String esConfig = "config/es.json";

	private List<String> configFilesToLoad;

	private JsonObject serverConfig = null;

	public ConfigHolder()
	{
		this(Collections.EMPTY_LIST);
	}

    public ConfigHolder(String siteConfigFile)
    {
        this(Collections.singletonList(siteConfigFile));
    }

	public ConfigHolder(List<String> configFilesToLoad)
	{
		this.configFilesToLoad = configFilesToLoad;
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
		Deque<String> allConfigFiles = new ArrayDeque<>();
		if (this.configFilesToLoad != null && !this.configFilesToLoad.isEmpty())
		{
			int size = this.configFilesToLoad.size();
			for (int i = (size -1); i>=0; i--)
			{
				allConfigFiles.push(this.configFilesToLoad.get(i));
			}
		}
		allConfigFiles.push(this.esConfig);
		allConfigFiles.push(this.baseConfigFile);

		this.serverConfig = new JsonObject();
		this.loadConfig(allConfigFiles, startFuture);
		INSTANCE = this;
	}

	private void loadConfig(Deque<String> configFiles, Future<Void> startFuture)
	{
		if (configFiles.isEmpty())
		{
			LOG.info("FInal merged config : " + this.serverConfig.encodePrettily());
			startFuture.complete();
			return;
		}

		String configFile = configFiles.pop();
		VertxInstance.get().fileSystem().readFile(configFile, result -> {
			if (result.succeeded())
			{
				String jsonText = result.result().toString();
				JsonObject configJson = new JsonObject(jsonText);
				this.serverConfig = this.serverConfig.mergeIn(configJson);

				LOG.info("Config file: " + configFile + ". Value:" + configJson.encodePrettily());
				loadConfig(configFiles, startFuture);
			}
			else
			{
				this.serverConfig = new JsonObject();
				String message = "Could not read config file : " + configFile + ". Cause:" + result.cause();
				LOG.error(message, result.cause());
				startFuture.fail(message);
			}
		});

	}

	public String getVersion()
	{
		Class clazz = this.getClass();
		String className = clazz.getSimpleName() + ".class";
		String classPath = clazz.getResource(className).toString();
		if (!classPath.startsWith("jar")) // Class not from JAR
		{
			return "NA";
		}
		String manifestPath = classPath.substring(0, classPath.lastIndexOf("!") + 1) + "/META-INF/MANIFEST.MF";

		InputStream is = null;
		try
		{
			is = new URL(manifestPath).openStream();
			Manifest manifest = new Manifest(is);
			Attributes attr = manifest.getMainAttributes();
			return attr.getValue("Manifest-Version");
		}
		catch (IOException e)
		{
			LOG.error("Error in getting version.", e);
		}
		finally
		{
			try
			{
				if (is != null) is.close();
			}
			catch (IOException e)
			{
				//Nothing to do
			}
		}
		return "NA";
	}
	
}
