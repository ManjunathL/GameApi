package com.orangegubbi.common;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class LocalCache
{
	private static final LocalCache INSTANCE = new LocalCache();
	
	private Map<Integer, Object> cache = new HashMap<Integer, Object>();
	private AtomicInteger id = new AtomicInteger();
	
	private LocalCache()
	{
		
	}
	
	public static LocalCache getInstance()
	{
		return INSTANCE;
	}
	
	public Integer store(Object value)
	{
		Integer nextid = id.incrementAndGet();
		cache.put(nextid, value);
		return nextid;
	}
	
	public Object get(Integer id)
	{
		return this.cache.get(id);
	}

	public Object remove(Integer id)
	{
		return this.cache.remove(id);
	}
}
