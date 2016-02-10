package com.mygubbi.config;

import com.mygubbi.common.VertxInstance;import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public class ConfigHolder extends AbstractVerticle
{
	private static final Logger LOG = LogManager.getLogger(ConfigHolder.class);
	
	private String baseConfigFile = "config/conf.base.json";
    private String siteConfigFile;
	private String esConfig = "config/es.json";

	private static ConfigHolder INSTANCE;
	
	private JsonObject serverConfig = new JsonObject();

	public ConfigHolder()
	{
        this(null);
	}

    public ConfigHolder(String siteConfigFile)
    {
        this.siteConfigFile = siteConfigFile;
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
        VertxInstance.get().fileSystem().readFile(this.baseConfigFile, result -> {
		    if (result.succeeded()) 
		    {
		    	this.serverConfig = new JsonObject(result.result().toString());
		        LOG.info("Base config file : " + this.baseConfigFile + ". Value:" + result.result().toString());
                if (this.siteConfigFile != null)
                {
                    this.loadSiteConfig(startFuture);
                    this.loadESConfig(startFuture);
                }
                else
                {
                    startFuture.complete();
                }
		    } 
		    else 
		    {
		    	this.serverConfig = new JsonObject();
		    	String message = "Could not read config file : " + this.baseConfigFile + ". Cause:" + result.cause();
		        LOG.error(message, result.cause());
		        startFuture.fail(message);
		    }
		});
		INSTANCE = this;
	}

    private void loadSiteConfig(Future<Void> startFuture)
    {
		loadConfig(startFuture, this.siteConfigFile, false);
	}

    private void loadESConfig(Future<Void> startFuture)
    {
		loadConfig(startFuture, this.esConfig, true);
	}

	private void loadConfig(Future<Void> startFuture, String config, boolean lastFile) {
		VertxInstance.get().fileSystem().readFile(config, result -> {
            if (result.succeeded())
            {
                JsonObject siteConfig = new JsonObject(result.result().toString());
                this.serverConfig = this.serverConfig.mergeIn(siteConfig);
                LOG.info("Site config file : " + this.siteConfigFile + ". Value:" + result.result().toString());
                LOG.info("Merged config : " + this.serverConfig.toString());
                if (lastFile) startFuture.complete();
            }
            else
            {
                String message = "Could not read site config file : " + this.siteConfigFile + ". Cause:" + result.cause();
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
