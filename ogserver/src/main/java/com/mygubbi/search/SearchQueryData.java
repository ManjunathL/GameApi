package com.mygubbi.search;

import io.vertx.core.json.JsonObject;

public class SearchQueryData
{
	private String index;
	private JsonObject query;
	private String result;
	private String type;
	private boolean recordsOnly = true;

	public SearchQueryData(String index, JsonObject query, String type) {
		this(index, query, type, true);
	}

	public SearchQueryData(String index, JsonObject query) {
		this(index, query, null, true);
	}

	public SearchQueryData(String index, JsonObject query, String type, boolean recordsOnly) {
		this.index = index;
		this.query = query;
		this.type = type;
		this.recordsOnly = recordsOnly;
	}

    public boolean isRecordsOnly()
    {
        return recordsOnly;
    }

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

	public String getType() {
		return type;
	}

	public SearchQueryData setResult(String result)
	{
		this.result = result;
		return this;
	}

	
}
