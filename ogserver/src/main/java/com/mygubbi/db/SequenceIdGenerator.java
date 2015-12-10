package com.mygubbi.db;

import com.hazelcast.nio.serialization.Data;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.mygubbi.common.LocalCache;

public class SequenceIdGenerator extends AbstractVerticle
{
	private static final Logger LOG = LogManager.getLogger(SequenceIdGenerator.class);

	private static final Map<String, String> sequenceMap = tableSequences(); 

	private final HazelcastInstance client = Hazelcast.newHazelcastInstance();
			
	private static SequenceIdGenerator INSTANCE;
	
	private static Map<String, String> tableSequences()
	{
		Map<String, String> smap = new HashMap<String, String>();
		for (String table : new String[]{"user_profile", "user_login"})
		{
			smap.put(table,  table + ".seq");
		}
		return smap;
	}
	
	public static SequenceIdGenerator getInstance()
	{
		return INSTANCE;
	}
	
	public SequenceIdGenerator()
	{
		INSTANCE = this;
	}
	
	@Override
	public void start(Future<Void> startFuture) throws Exception
	{
		final Set<String> tables = sequenceMap.keySet();
		final List<String> errors = new ArrayList<String>();
		
		EventBus eb = vertx.eventBus();
		for (String table : tables)
		{
			QueryDef qDef = new QueryDef("select max(id) as maxid from " + table, QueryDef.SELECT_QUERY_TYPE);
			QueryData qData = new QueryData("max." + table, null);
			qData.queryDef = qDef;
			Integer qDataId = LocalCache.getInstance().store(qData);
			
			eb.send(DatabaseService.DB_QUERY, qDataId, (AsyncResult<Message<Integer>> result) -> {
				QueryData resultData = (QueryData) LocalCache.getInstance().remove(result.result().body());
				if (!resultData.errorFlag)
				{
					if (resultData.rows.get(0).getValue("maxid") == null)
					{
						this.client.getAtomicLong(sequenceMap.get(table)).set(0);
					}
					else
					{
						this.client.getAtomicLong(sequenceMap.get(table)).set(resultData.rows.get(0).getInteger("maxid"));
					}
				}
				else if (result.failed())
				{
					LOG.error("Sequence not obtained for " + table, resultData.errorMessage);
					errors.add(table + " : " + resultData.errorMessage);
				}
			});
		}

		startFuture.complete();
	}

	public long getNextSequence(String table)
	{
		if (!sequenceMap.containsKey(table))
		{
			return 0;
		}
		return this.client.getAtomicLong(sequenceMap.get(table)).incrementAndGet();
	}
}
