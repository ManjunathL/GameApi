package com.mygubbi.db;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mygubbi.config.ConfigHolder;
import com.mygubbi.common.LocalCache;

public class DatabaseService extends AbstractVerticle
{
	private final static Logger LOG = LogManager.getLogger(DatabaseService.class);
	public static final String DB_QUERY = "db.query";
	private JDBCClient client; 
	
	@Override
	public void start(Future<Void> startFuture) throws Exception
	{
		try
		{
			this.initClient(startFuture);
		}
		catch (Exception e)
		{
			LOG.error("Error in starting database service.", e);
			startFuture.fail(e);
		}
	}

	private void initClient(Future<Void> startFuture) throws Exception
	{
		JsonObject config = (JsonObject) ConfigHolder.getInstance().getConfigValue("jdbc");
		if (config == null) 
		{
			throw new RuntimeException("Could not find config with key 'jdbc'");
		}
		this.client = JDBCClient.createShared(vertx, config);
		this.client.getConnection(handler -> {
			if (handler.succeeded())
			{
				LOG.info("Connection to DB established.");
				handler.result().close();
				setupMessageHandler();
				startFuture.complete();
				LOG.info("Database service started.");
			}
			else
			{
				throw new RuntimeException(handler.cause());
			}
		});
	}

	private void setupMessageHandler()
	{
		EventBus eb = vertx.eventBus();
		eb.localConsumer(DB_QUERY, (Message<Integer> message) -> {
			
			QueryData qData = (QueryData) LocalCache.getInstance().remove(message.body());
			QueryPrepareService.getInstance().prepareQueryData(qData);
			if (qData.errorFlag)
			{
				message.reply(LocalCache.getInstance().store(qData));
				return;
			}
			handleQuery(message, qData);
		}).completionHandler(res -> {
			LOG.info("Database Service handler registered." + res.succeeded());
		});
	}
	
	private void handleQuery(Message message, QueryData qData)
	{
		qData.startQuery();
		
		this.client.getConnection(res -> {
		  if (res.succeeded()) 
		  {
		    SQLConnection connection = res.result();

		    if (qData.queryDef.isUpdateQuery)
		    {
		    	runUpdateQuery(message, qData, connection);
		    }
		    else
		    {
		    	runSelectQuery(message, qData, connection);
		    }
		  } 
		  else 
		  {
			  LOG.error("Error in query.", res.cause());
			  qData.setError(res.cause());
			  Integer id = LocalCache.getInstance().store(qData);
			  message.reply(id);
		  }
		});
	}

	private void runSelectQuery(Message message, QueryData qData, SQLConnection connection)
	{
		connection.queryWithParams(qData.queryDef.query, qData.getParams(), res2 -> {
		  if (res2.succeeded()) 
		  {
			  qData.setResult(res2.result().getRows());
		  }
		  else
		  {
			  qData.setError(res2.cause());
		  }
		  connection.close();
		  message.reply(LocalCache.getInstance().store(qData));
		});
	}
	
	private void runUpdateQuery(Message message, QueryData qData, SQLConnection connection)
	{
		LOG.info("Query:" + qData.queryDef.query + ". Params:" + qData.getParams());
		connection.updateWithParams(qData.queryDef.query, qData.getParams(), res2 -> {
		  if (res2.succeeded()) 
		  {
			  qData.setResult(res2.result());
		  }
		  else
		  {
		      qData.setError(res2.cause());
			  LOG.error("Error:", res2.cause());
		  }
		  connection.close();
		  message.reply(LocalCache.getInstance().store(qData));
		});
	}

	public static void main(String[] args) throws Exception
	{
		Vertx.vertx().deployVerticle(ConfigHolder.class.getCanonicalName(), result -> {
			Vertx.vertx().deployVerticle(DatabaseService.class.getCanonicalName());
		});
	}
}
