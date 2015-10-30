package com.orangegubbi;

import io.vertx.core.Vertx;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.orangegubbi.search.SearchService;

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
		Vertx.vertx().deployVerticle(SearchService.class.getCanonicalName(), result -> {
			System.out.println(result.succeeded());
		});
		
		Thread.sleep(2000);
	}

}
