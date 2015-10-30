package com.orangegubbi.db;

import java.util.List;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.UpdateResult;

public class QueryData
{
	public String queryId;
	public QueryDef queryDef;
	
	public JsonObject paramsObject;
	public List<JsonObject> rows;
	public UpdateResult updateResult;

	public long responseTimeInMillis;
	public boolean errorFlag;
	public Throwable error;
	public String errorMessage;
	private long startTime;
	
	public QueryData()
	{
		
	}
	
	public QueryData(String queryId, JsonObject paramsObject)
	{
		this.queryId = queryId;
		this.paramsObject = paramsObject;
	}

	public JsonArray getParams()
	{
		if (this.paramsObject == null || this.queryDef == null || this.queryDef.paramList == null) return null;
		
		JsonArray params = new JsonArray();
		for (String paramKey : this.queryDef.paramList)
		{
			Object value = this.paramsObject.getValue(paramKey);
			if (value instanceof JsonObject)
				params.add(value.toString());
			else
				params.add(value);
		}
		return params;
	}
	
	public QueryData setError(String error)
	{
		this.errorFlag = true;
		this.errorMessage = error;
		this.endQuery();
		return this;
	}
	
	public QueryData setError(Throwable cause)
	{
		this.error = cause;
		return this.setError(cause.getMessage());
	}
	
	public QueryData  startQuery()
	{
		this.startTime = System.currentTimeMillis();
		return this;
	}

	public QueryData endQuery()
	{
		this.responseTimeInMillis = System.currentTimeMillis() - this.startTime;
		this.startTime = 0;
		return this;
	}
	
	public QueryData setResult(List<JsonObject> rows)
	{
		this.rows = rows;
		return this.endQuery();
	}

	public QueryData setResult(UpdateResult updateResult)
	{
		this.updateResult = updateResult;
		return this.endQuery();
	}
}
