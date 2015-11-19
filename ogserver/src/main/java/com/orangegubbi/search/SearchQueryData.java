package com.orangegubbi.search;

import io.vertx.core.json.JsonObject;

public class SearchQueryData
{
	private String index;
	private JsonObject query;
	private String result;

	public String getIndex()
	{
		return index;
	}
	public SearchQueryData setIndex(String index)
	{
		this.index = index;
		return this;
	}
	public JsonObject getQuery()
	{
		return query;
	}
	public SearchQueryData setQuery(JsonObject query)
	{
		this.query = query;
		return this;
	}
	public String getResult()
	{
		return result;
	}
	public SearchQueryData setResult(String result)
	{
		this.result = result;
		return this;
	}

	
}
