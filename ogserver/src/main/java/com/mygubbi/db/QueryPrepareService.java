package com.mygubbi.db;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class QueryPrepareService extends AbstractVerticle
{
	private final static Logger LOG = LogManager.getLogger(QueryPrepareService.class);
	private Map<String, QueryDef> queryMap;
	
	private static QueryPrepareService INSTANCE;
	
	public static QueryPrepareService getInstance()
	{
		return INSTANCE;
	}
	
	@Override
	public void start(Future<Void> startFuture) throws Exception
	{
		this.setupQueryMap(startFuture);
		INSTANCE = this;
	}

	private void setupQueryMap(Future<Void> startFuture)
	{
		vertx.fileSystem().readFile("queries.json", result -> {
		    if (result.succeeded()) 
		    {
				JsonObject json = new JsonObject(result.result().toString());
				this.queryMap = new HashMap<String, QueryDef>();
				for (Object queryObject : json.getJsonArray("queries"))
				{
					QueryDef queryDef = new QueryDef((JsonObject) queryObject);
					this.queryMap.put(queryDef.queryId, queryDef);
				}
		        LOG.info("Query map created.");
		        startFuture.complete();
		    } 
		    else 
		    {
		        LOG.error("Could not create query map", result.cause());
		        startFuture.fail(result.cause());
		    }
		});
		
	}
	
	public QueryData prepareQueryData(QueryData qData)
	{
		if (qData.queryDef == null)
		{
			if (!this.queryMap.containsKey(qData.queryId))
			{
				qData.setError("Query not configured:" + qData.queryId);
				return qData;
			}
			
			QueryDef qDef = this.queryMap.get(qData.queryId);
			qData.queryDef = qDef;
		}
		
		if (qData.queryDef.isInsertQuery)
		{
			qData.paramsObject.put("id", SequenceIdGenerator.getInstance().getNextSequence(qData.queryDef.tableForInsert));
		}
		
		return qData;
	}
	
	public static void main(String[] args) throws Exception
	{
		
	}
	
}
