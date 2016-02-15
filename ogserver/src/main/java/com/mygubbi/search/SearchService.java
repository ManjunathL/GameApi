package com.mygubbi.search;

import com.mygubbi.common.LocalCache;
import com.mygubbi.config.ConfigHolder;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import java.io.IOException;
import java.util.Iterator;

public class SearchService extends AbstractVerticle {
    private final static Logger LOG = LogManager.getLogger(SearchService.class);
    private Node node;
    private Client client;

    public static final String INDEX = "index";
    public static final String PREPARE = "prepare";
    public static final String SEARCH = "search";
    public static final String INDEX_NAME = "mygubbi_index";

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        try {
            this.initClient();
            this.setupPrepareIndexHandler();
            this.setupIndexHandler();
            this.setupQueryHandler();
            startFuture.complete();
        } catch (Exception e) {
            LOG.error("Error in starting search service.", e);
            startFuture.fail(e);
        }
    }

    private void setupPrepareIndexHandler() {
        EventBus eb = vertx.eventBus();
        eb.localConsumer(PREPARE, (Message<Integer> message) -> {
            try {
                deleteIndex(INDEX_NAME);
                createIndex(INDEX_NAME, ConfigHolder.getInstance().getConfigValue(INDEX_NAME).toString());
                message.reply(true);
            } catch (Exception e) {
                LOG.error("Error in preparting index", e);
                message.reply(false);
            }
        });
    }

    public boolean createIndex(String indexName, String indexConfig) throws ElasticsearchException {
        return client.admin().indices().prepareCreate(indexName).setSource(indexConfig).execute().actionGet().isAcknowledged();
    }

    public boolean deleteIndex(String indexName) throws ElasticsearchException {
        if (!checkForIndex(indexName)) return true;
        return client.admin().indices().prepareDelete(indexName).execute().actionGet().isAcknowledged();
    }

    private boolean checkForIndex(String index) throws ElasticsearchException {
        return client.admin().indices().prepareExists(index).execute().actionGet().isExists();
    }

    private void initClient() throws Exception {
        JsonObject elasticSearchConfigObj = (JsonObject) ConfigHolder.getInstance().getConfigValue("elasticsearch");
        String clusterName = elasticSearchConfigObj.getString("cluster_name");
        node = new NodeBuilder().clusterName(clusterName)
                .settings(ImmutableSettings.settingsBuilder().put("http.enabled", false))
                .client(true).node();
        client = node.client();
    }

    private void setupIndexHandler() {
        EventBus eb = vertx.eventBus();
        eb.localConsumer(INDEX, (Message<Integer> message) -> {
            IndexData iData = (IndexData) LocalCache.getInstance().remove(message.body());
            IndexResponse response = this.client.prepareIndex(iData.getIndex(), iData.getType(), iData.getId())
                    .setSource(iData.getDocumentAsString()).execute().actionGet();
            message.reply(LocalCache.getInstance().store(response));

        });
    }

    private void setupQueryHandler() {
        EventBus eb = vertx.eventBus();
        eb.localConsumer(SEARCH, (Message<Integer> message) -> {
            SearchQueryData qData = (SearchQueryData) LocalCache.getInstance().remove(message.body());
            SearchResponse response = client.prepareSearch(qData.getIndex()).setTypes(qData.getType()).setSource(qData.getQuery().toString()).execute().actionGet();
            if (qData.isRecordsOnly())
            {
                qData.setResult(this.getRecordsAsText(response));
            }
            else
            {
                qData.setResult(this.responseToJson(response));
            }
            //LOG.info("Search response:" + qData.getResult());
            message.reply(LocalCache.getInstance().store(qData));
        });
    }

    private String getRecordsAsText(SearchResponse response)
    {
        SearchHits hits = response.getHits();
        if (hits.totalHits() == 0)
        {
            return "[]";
        }
        StringBuilder sb = new StringBuilder(1024 * 64);
        sb.append("[");
        Iterator<SearchHit> iterator = hits.iterator();
        while (iterator.hasNext())
        {
            SearchHit hit = iterator.next();
            sb.append(hit.getSourceAsString());
            if (iterator.hasNext()) sb.append(',');
        }
        sb.append("]");
        return sb.toString();
    }

    private String responseToJson(SearchResponse response) {
        try {
            XContentBuilder builder = XContentFactory.jsonBuilder();
            builder.startObject();
            response.toXContent(builder, ToXContent.EMPTY_PARAMS);
            builder.endObject();
            return builder.string();
        } catch (IOException e) {
            LOG.error("error in parsing json", e);
            return "{}";
        }
    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {
        if (null != this.client) client.close();
        if (null != this.node) node.close();
    }

}
