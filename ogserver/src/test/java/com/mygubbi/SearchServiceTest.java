package com.mygubbi;

import io.vertx.core.Vertx;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mygubbi.search.SearchService;

public class SearchServiceTest
{

	@Before
	public void setUp() throws Exception
	{
	}

	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void test() throws Exception
	{
		System.out.println("Trying to connect to ES ...");
		Node node = new NodeBuilder().clusterName("elasticsearch_dev")
				.settings(ImmutableSettings.settingsBuilder().put("http.enabled", false))
				.client(true).node();
		System.out.println("Getting ES client ...");
		Client client = node.client();
		System.out.println("Done.");
		client.close();
		node.close();
		System.out.println("Closed.");
	}

}
