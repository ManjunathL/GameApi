package com.mygubbi.db;

import com.mygubbi.common.LocalCache;
import com.mygubbi.config.ConfigHolder;
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

import java.util.List;

public class DatabaseService extends AbstractVerticle
{
	private final static Logger LOG = LogManager.getLogger(DatabaseService.class);
	public static final String DB_QUERY = "db.query";
	public static final String MULTI_DB_QUERY = "multi.db.query";
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
				setupQueryMessageHandler();
				setupMultiQueryMessageHandler();
				startFuture.complete();
				LOG.info("Database service started.");
			}
			else
			{
				throw new RuntimeException(handler.cause());
			}
		});
	}

	private void setupMultiQueryMessageHandler()
	{
		EventBus eb = vertx.eventBus();
		eb.localConsumer(MULTI_DB_QUERY, (Message<Integer> message) -> {

			List<QueryData> qDataList = (List<QueryData>) LocalCache.getInstance().remove(message.body());
			for (QueryData qData : qDataList)
			{
				QueryPrepareService.getInstance().prepareQueryData(qData);
			}
			handleQueryInGroup(message, qDataList, 0);
		}).completionHandler(res -> {
			LOG.info("Database Multi Query Service handler registered." + res.succeeded());
		});
	}
	
	private void setupQueryMessageHandler()
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
			LOG.info("Database Query Service handler registered." + res.succeeded());
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

	private void handleQueryInGroup(Message message, List<QueryData> qDataList, int index)
	{
		if (index >= qDataList.size())
		{
			message.reply(LocalCache.getInstance().store(qDataList));
		}

		QueryData qData = qDataList.get(index);
		if (qData.errorFlag)
		{
			handleQueryInGroup(message, qDataList, index + 1);
		}

		qData.startQuery();

		this.client.getConnection(res -> {
			if (res.succeeded())
			{
				SQLConnection connection = res.result();

				if (qData.queryDef.isUpdateQuery)
				{
					runUpdateQueryInGroup(message, qData, connection, qDataList, index);
				}
				else
				{
					runSelectQueryInGroup(message, qData, connection, qDataList, index);
				}
			}
			else
			{
				LOG.error("Error in query.", res.cause());
				qData.setError(res.cause());
                handleQueryInGroup(message, qDataList, index + 1);
			}
		});
	}

	private void runSelectQueryInGroup(Message message, QueryData qData, SQLConnection connection, List<QueryData> qDataList, int index)
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
            handleQueryInGroup(message, qDataList, index + 1);
		});
	}

	private void runUpdateQueryInGroup(Message message, QueryData qData, SQLConnection connection, List<QueryData> qDataList, int index)
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
            handleQueryInGroup(message, qDataList, index + 1);
		});
	}

	public static void main(String[] args) throws Exception
	{
		Vertx.vertx().deployVerticle(ConfigHolder.class.getCanonicalName(), result -> {
			Vertx.vertx().deployVerticle(DatabaseService.class.getCanonicalName());
		});
	}
}
