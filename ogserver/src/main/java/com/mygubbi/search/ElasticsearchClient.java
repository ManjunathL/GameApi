package com.mygubbi.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import com.mygubbi.common.FileReaderUtil;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.suggest.SuggestRequestBuilder;
import org.elasticsearch.action.suggest.SuggestResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.facet.Facet;
import org.elasticsearch.search.facet.terms.TermsFacet;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.elasticsearch.search.suggest.phrase.PhraseSuggestion;
import org.elasticsearch.search.suggest.phrase.PhraseSuggestionBuilder;
import org.elasticsearch.search.suggest.term.TermSuggestion;
import org.elasticsearch.search.suggest.term.TermSuggestionBuilder;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;


public class ElasticsearchClient 
{
	private static final String EMPTY_JSON = "{}";
	
	private Node node = null;
	private Client client = null;
	
	private String clusterName;
	
	public ElasticsearchClient(String clusterName)
	{
		this.clusterName = clusterName;
		this.createClient();
	}
	
	public ElasticsearchClient(Node node, Client client)
	{
		this.node  = node;
		this.client = client;
	}

	private void createClient()
	{
		if ( null != client ) return;
		
		node = new NodeBuilder().clusterName(clusterName)
			        .settings(ImmutableSettings.settingsBuilder().put("http.enabled", false))
			        .client(true).node();
		client = node.client();
	}
	
	public boolean createIndex(String index) throws ElasticsearchException
	{
		return client.admin().indices().prepareCreate(index).execute().actionGet().isAcknowledged();
	}
	
	public boolean createIndex(String index, String source) throws ElasticsearchException
	{
		return client.admin().indices().prepareCreate(index).setSource(source).execute().actionGet().isAcknowledged();
	}

	public boolean deleteIndex(String index) throws ElasticsearchException
	{
		if ( ! checkForIndex(index) ) return true;
		return client.admin().indices().prepareDelete(index).execute().actionGet().isAcknowledged();
	}

	public boolean checkForIndex(String index) throws ElasticsearchException
	{
		return client.admin().indices().prepareExists(index).execute().actionGet().isExists();
	}

	public boolean indexDocument(IndexData iData) throws ElasticsearchException
	{
		IndexResponse indexResponse = this.client.prepareIndex(iData.getIndex(), iData.getType(), iData.getId())
		        .setSource(iData.getDocument().toString()).execute().actionGet();
		return indexResponse.isCreated();
	}
	
	public SearchQueryData query(SearchQueryData qData) throws ElasticsearchException
	{
		SearchResponse searchResponse = client.prepareSearch(qData.getIndex()).setSource(qData.getQuery().toString()).execute().actionGet();
		qData.setResult(this.responseToJson(searchResponse));
		return qData;
	}

	public List<String>  completionSuggestions(String index, String inputText, String suggestName, String suggestField) throws ElasticsearchException
	{
		List<String> suggestions = new ArrayList<>();
		
		CompletionSuggestionBuilder compBuilder = new CompletionSuggestionBuilder(suggestName);
	    compBuilder.text(inputText);
	    compBuilder.field(suggestField);

		SuggestRequestBuilder suggestRequestBuilder = this.client
		    .prepareSuggest(index).addSuggestion(compBuilder);
		SuggestResponse suggestResponse = suggestRequestBuilder.execute().actionGet();
		
		CompletionSuggestion compSuggestion = suggestResponse.getSuggest().getSuggestion(suggestName);
		List<CompletionSuggestion.Entry> entryList = compSuggestion.getEntries();
		for (CompletionSuggestion.Entry entry : entryList )
		{
		    List<CompletionSuggestion.Entry.Option> options = entry.getOptions();
		    for ( CompletionSuggestion.Entry.Option option : options)
		    {
				String optionTxt = option.getText().string();
				suggestions.add(optionTxt);
		    }
		}
		return suggestions;
	}

	public List<String>  termSuggestions(String index, String inputText, String suggestName, String suggestField) throws ElasticsearchException
	{
		List<String> suggestions = new ArrayList<>();
		
		TermSuggestionBuilder termSuggestionBuilder = new TermSuggestionBuilder(suggestName);
		termSuggestionBuilder.text(inputText);
		termSuggestionBuilder.field(suggestField);
				
		SuggestRequestBuilder suggestRequestBuilder = this.client
		    .prepareSuggest(index).addSuggestion(termSuggestionBuilder);
		SuggestResponse suggestResponse = suggestRequestBuilder.execute().actionGet();
		
		TermSuggestion termSuggestion = suggestResponse.getSuggest().getSuggestion(suggestName);
		List<TermSuggestion.Entry> entryList = termSuggestion.getEntries();
		for (TermSuggestion.Entry entry : entryList )
		{
		    List<TermSuggestion.Entry.Option> options = entry.getOptions();
		    for ( TermSuggestion.Entry.Option option : options)
		    {
				String optionTxt = option.getText().string();
				suggestions.add(optionTxt);
		    }
		}
		return suggestions;
	}

	public List<String>  phraseSuggestions(String index, String inputText, String suggestName, String suggestField) throws ElasticsearchException
	{
		List<String> suggestions = new ArrayList<>();
		
		PhraseSuggestionBuilder phraseSuggestionBuilder = new PhraseSuggestionBuilder(suggestName);
		phraseSuggestionBuilder.text(inputText);
		phraseSuggestionBuilder.field(suggestField);
				
		SuggestRequestBuilder suggestRequestBuilder = this.client
		    .prepareSuggest(index).addSuggestion(phraseSuggestionBuilder);
		SuggestResponse suggestResponse = suggestRequestBuilder.execute().actionGet();
		
		PhraseSuggestion phraseSuggestion = suggestResponse.getSuggest().getSuggestion(suggestName);
		List<PhraseSuggestion.Entry> entryList = phraseSuggestion.getEntries();
		for (PhraseSuggestion.Entry entry : entryList )
		{
		    List<PhraseSuggestion.Entry.Option> options = entry.getOptions();
		    for ( PhraseSuggestion.Entry.Option option : options)
		    {
				String optionTxt = option.getText().string();
				suggestions.add(optionTxt);
		    }
		}
		return suggestions;
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
			e.printStackTrace();
			return EMPTY_JSON;
		}
	}
	
	 private String getRecordsAsTextWithFacet(SearchResponse response)
	 {
	        SearchHits hits = response.getHits();
	        
	        if (hits.totalHits() == 0)
	        {
	            return "[]";
	        }
	        StringBuilder sb = new StringBuilder(1024 * 64);
	        sb.append("{ \"hits\" : [");
	        Iterator<SearchHit> iterator = hits.iterator();
	        while (iterator.hasNext())
	        {
	            SearchHit hit = iterator.next();
	            sb.append(hit.getSourceAsString());
	            if (iterator.hasNext()) sb.append(',');
	        }
	        sb.append("],");

	        sb.append("\"facets\" : {");
	        
	        System.out.println("------------------");
	        System.out.println(response.getFacets().facetsAsMap());
	        System.out.println("------------------");
	        
	        // ----
	        Map<String, Facet> facetMap = response.getFacets().getFacets();
	        boolean isFirstFacetGroup = true;
	        for ( String facetKey : facetMap.keySet())
	        {
	        	TermsFacet tf = (TermsFacet) facetMap.get(facetKey);
	        	
	        	if ( isFirstFacetGroup ) isFirstFacetGroup = false;
	        	else sb.append(",");
	        	
	        	sb.append("\"").append(tf.getName()).append("\": ").append("{");
	        	boolean isFirstEntry = true;
	        	for(TermsFacet.Entry entry : tf)
	        	{
	        		if ( isFirstEntry ) isFirstEntry = false;
		        	else sb.append(',');
		            sb.append("\"" ).append(entry.getTerm()).append("\":").append(entry.getCount());
	        	}
	        	sb.append("}");
	        }
	        sb.append("}}");
	        
	        System.out.println("------------------");
	        // -- multi facet end
	        
	        /*
	        TermsFacet f = (TermsFacet) response.getFacets().facetsAsMap().get("cat`");
	        boolean isFirst = true;
	        for (TermsFacet.Entry entry : f) 
	        {
	        	if ( isFirst ) isFirst = false;
	        	else sb.append(',');
	            sb.append("\"" ).append(entry.getTerm()).append("\":").append(entry.getCount());
	        }
	        sb.append("}}");
	        */
	        return sb.toString();
	}
	 
	public void closeAll()
	{
		if ( null != client) client.close();
		if ( null != node ) node.close();
	}

	/*
	 * 1. Create indexes with configuration
	 * 2. Index documents
	 * 3. Delete all indexes
	 * Accepts inputs from console and spits the output 
	 */
	
	public static void main(String[] args) 
	{
		if ( args.length < 1)
		{
			System.out.println("USAGE: " + ElasticsearchClient.class + " <<clsutername>>");
			System.exit(1);
		}
		
		boolean isJar = false;
		if ( args.length == 2) isJar = Boolean.parseBoolean(args[1]);
		
		System.out.println("Readfrom jar : " + isJar);
		ElasticsearchClient elasticsearchClient = new ElasticsearchClient(args[0]);
		Scanner scanner = new Scanner(System.in);
		while(true)
		{
			System.out.println
			(
			"\n 1. Prepare \n"
			+ " 2. Clean \n"
			+ " 3. Index Documents from JSON file \n\t <<index_name:doc_type:filepath>>\n"
			+ " 4. Index all - using default load files\n"
			+ " 5. Exit \n"
			+ " 6. Query \n"
			+ "Enter your choice : \n");
			
			String choice = scanner.nextLine();
			int choiceI = 0;
			try
			{
				choiceI = Integer.parseInt(choice);
			}
			catch(NumberFormatException ne) {}
			
			switch (choiceI) 
			{
				case 1:
					System.out.println("Preparing...");
					prepare(elasticsearchClient, isJar, "user_index");
					prepare(elasticsearchClient, isJar, "space_lib_index");
					break;
							
				case 2:
					System.out.println("Cleaning...");
					clean(elasticsearchClient, "user_index");
					clean(elasticsearchClient, "space_lib_index");
					break;

				case 3:
					System.out.println("Index");
					
					String input = scanner.nextLine();
					String [] details = input.split(":");
					
					String name = details[0];
					String type = details[1];
					String filepath = details[2];
					indexDocs(elasticsearchClient, name, type, filepath, isJar);
					break;

				case 4:
					//indexUsingDefault(elasticsearchClient, isJar);
					indexUsersUsingDefault(elasticsearchClient, isJar, "user_index");
					indexSpaceUsingDefault(elasticsearchClient, isJar, "space_lib_index");
					break;
					
				case 5:
					System.out.println("Done.");
					scanner.close();
					elasticsearchClient.closeAll();
					System.exit(1);
					break;

				case 6:
					System.out.println("Query");
					String searchTermWithIndex = scanner.nextLine().trim();
					String [] i = searchTermWithIndex.split(":");
					String searchTerm = i[0];
					String indexname = i[1];
					search(elasticsearchClient, searchTerm, indexname);
				default:
					break;
			}
		}
	}

	/*
	private static void prepare(ElasticsearchClient elasticsearchClient, boolean isJar) 
	{
		System.out.println("Creating lp_index");
		String config = (isJar) ? FileReaderUtil.toStringFromJar("lp_index_config") : FileReaderUtil.toString("lp_index_config");
		boolean status = elasticsearchClient.createIndex("lp_index", config);
		System.out.println("Created : " + status);

		System.out.println("clipboard_index");
		config = (isJar) ? FileReaderUtil.toStringFromJar("clipboard_index_config") : FileReaderUtil.toString("clipboard_index_config"); 
		status = elasticsearchClient.createIndex("clipboard_index", config);
		System.out.println("Created : " + status);
	}
	*/

	/*
	private static void prepare(ElasticsearchClient elasticsearchClient, boolean isJar) 
	{
		System.out.println("Creating video_index");
		String config = (isJar) ? FileReaderUtil.toStringFromJar("video_index_config") : FileReaderUtil.toString("video_index_config");
		boolean status = elasticsearchClient.createIndex("video_index", config);
		System.out.println("Created : " + status);

		System.out.println("video_tag_index");
		config = (isJar) ? FileReaderUtil.toStringFromJar("video_tag_index_config") : FileReaderUtil.toString("video_tag_index_config"); 
		status = elasticsearchClient.createIndex("video_tag_index", config);
		System.out.println("Created : " + status);
	}
	*/
	private static void prepare(ElasticsearchClient elasticsearchClient, boolean isJar, String index)
	{
		System.out.println("Creating " + index);
		String config = (isJar) ? FileReaderUtil.toStringFromJar(index) : FileReaderUtil.toString(index);
		boolean status = elasticsearchClient.createIndex(index, config);
		System.out.println("Created : " + status);
	}

	/*
	private static void clean(ElasticsearchClient elasticsearchClient) 
	{
		System.out.println("Deleting lp_index");
		boolean status = elasticsearchClient.deleteIndex("lp_index");
		System.out.println("Deleted : " + status);

		System.out.println("Deleting clipboard_index");
		status = elasticsearchClient.deleteIndex("clipboard_index");
		System.out.println("Deleted : " + status);
	}
	
	private static void clean(ElasticsearchClient elasticsearchClient) 
	{
		System.out.println("Deleting video_index_config");
		boolean status = elasticsearchClient.deleteIndex("video_index");
		System.out.println("Deleted : " + status);

		System.out.println("Deleting video_tag_index_config");
		status = elasticsearchClient.deleteIndex("video_tag_index");
		System.out.println("Deleted : " + status);
	}
	*/

	private static void clean(ElasticsearchClient elasticsearchClient, String index)
	{
		System.out.println("Deleting " + index);
		boolean status = elasticsearchClient.deleteIndex(index);
		System.out.println("Deleted : " + status);
	}

	private static void indexDocs(ElasticsearchClient elasticsearchClient, String index, String type, String filepath, boolean isJar)
	{
		System.out.println("Indexing documents from " + filepath + " into " + index + ":" + isJar);
		String content = (isJar) ? FileReaderUtil.toStringFromJar(filepath) : FileReaderUtil.toString(filepath);
		try
		{
			System.out.println(content);
			JsonObject json = new JsonObject(content);
			JsonArray list = json.getJsonArray("values");
			AtomicInteger id = new AtomicInteger(1);
			list.forEach(row ->
			{
				System.out.println("Indexing >> " + row.toString());
				elasticsearchClient.indexDocument( new IndexData(String.valueOf(id.getAndIncrement()), index, type, new JsonObject(row.toString())));
			});
			
		}
		catch(Exception e)
		{
			System.out.println("Error in loading documents");
			e.printStackTrace(System.out);
		}
	}
	
	/*
	private static void indexUsingDefault(ElasticsearchClient elasticsearchClient, boolean isJar)
	{
		clean(elasticsearchClient);
		prepare(elasticsearchClient, isJar);
		indexDocs(elasticsearchClient, "lp_index", "lp", "lp.json", isJar);
		indexDocs(elasticsearchClient, "lp_index", "module", "modules.json", isJar);
		indexDocs(elasticsearchClient, "lp_index", "video", "videos.json", isJar);
		indexDocs(elasticsearchClient, "lp_index", "resource", "resources.json", isJar);
	}
	*/

	private static void indexSpaceUsingDefault(ElasticsearchClient elasticsearchClient, boolean isJar, String index)
	{
		clean(elasticsearchClient, index);
		prepare(elasticsearchClient, isJar, index);
		indexDocs(elasticsearchClient, "space_lib_index", "items", "space.json", isJar);
	}

	private static void indexUsersUsingDefault(ElasticsearchClient elasticsearchClient, boolean isJar, String index)
	{
		clean(elasticsearchClient, index);
		prepare(elasticsearchClient, isJar, index);
		indexDocs(elasticsearchClient, index, "users", "users.json", isJar);
	}

	//final static String searchJSON = "{\"size\":99999,\"query\":{\"match\":{\"_all\":{\"query\":\"__TERM\",\"operator\":\"and\"}}},\"facets\":{\"tags\":{\"terms\":{\"fields\": [\"category\", \"subCategory\"]}}}}";
	//final static String searchJSON = "{\"size\":99999,\"query\":{\"match\":{\"_all\":{\"query\":\"__TERM\",\"operator\":\"and\"}}},\"facets\":{\"cat\":{\"terms\":{\"field\":\"category\"}},\"subcat\":{\"terms\":{\"field\":\"subCategory\"}}}}";
	final static String searchJSON = "{\"size\":99999,\"query\":{\"match\":{\"_all\":{\"query\":\"__TERM\",\"operator\":\"and\"}}},\"filter\":{},\"facets\":{}}";
	private static void search(ElasticsearchClient elasticsearchClient, String term, String indexName)
	{
		String searchQueryJson = searchJSON.replaceFirst("__TERM", term);
		System.out.println(searchQueryJson);
		SearchQueryData results = elasticsearchClient.query(new SearchQueryData(indexName, new JsonObject(searchQueryJson)));
		System.out.println(results.getResult());
	}
}

