package com.orangegubbi.search;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;

import com.orangegubbi.common.LocalCache;

public class SearchService extends AbstractVerticle
{
	private final static Logger LOG = LogManager.getLogger(SearchService.class);
	
	private Client client;
	private Node node;

	@Override
	public void start(Future<Void> startFuture) throws Exception
	{
		try
		{
			this.initClient(startFuture);
        	this.setupIndexHandler();
        	this.setupQueryHandler();
        	startFuture.complete();
		}
		catch (Exception e)
		{
			LOG.error("Error in starting search service.", e);
			startFuture.fail(e);
		}
	}

	private void initClient(Future<Void> startFuture) throws Exception
	{
		this.node = new NodeBuilder()
			        .settings(ImmutableSettings.settingsBuilder().put("http.enabled", false))
			        .client(true).node();

		this.client = node.client();
	}
	
	private void setupIndexHandler()
	{
		EventBus eb = vertx.eventBus();
		eb.localConsumer("search.index", (Message<Integer> message) -> {
			IndexData iData = (IndexData) LocalCache.getInstance().remove(message.body());
			IndexResponse response = this.client.prepareIndex(iData.getIndex(), iData.getType(), iData.getId())
			        .setSource(iData.getDocument()).execute().actionGet();
			message.reply(LocalCache.getInstance().store(iData));
        		
		});
	}
	
	private void setupQueryHandler()
	{
		EventBus eb = vertx.eventBus();
		eb.localConsumer("search.query", (Message<Integer> message) -> {
			SearchQueryData qData = (SearchQueryData) LocalCache.getInstance().remove(message.body());
			SearchResponse response = client.prepareSearch(qData.getIndex()).setQuery(qData.getQuery().toString()).execute().actionGet();
			qData.setResult(this.responseToJson(response));
       		message.reply(LocalCache.getInstance().store(qData));
		});
	}

	private String responseToJson(SearchResponse response)
	{
		try
		{
			XContentBuilder builder = XContentFactory.jsonBuilder();
			builder.startObject();
			response.toXContent(builder, ToXContent.EMPTY_PARAMS);
			builder.endObject();
			return builder.string();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "{}";
		}
	}

	@Override
	public void stop(Future<Void> stopFuture) throws Exception
	{
		this.client.close();
		this.node.close();
	}
	
	
}
