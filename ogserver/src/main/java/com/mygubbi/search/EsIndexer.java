package com.mygubbi.search;

import com.mygubbi.ServerVerticle;
import com.mygubbi.common.VertxInstance;
import io.vertx.core.DeploymentOptions;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class EsIndexer {
    private final static Logger LOG = LogManager.getLogger(EsIndexer.class);
    private static final String SITE_CONFIG = "config/conf.testserver.json";

    public static void main(String[] args) {

        final EsIndexer esIndexer = new EsIndexer();
        esIndexer.startServices();

    }

    private void startServices() {

        VertxInstance.get().deployVerticle(new ServerVerticle("config/conf.es.json"), new DeploymentOptions().setWorker(true), result ->
        {
            if (result.succeeded())
            {
                LOG.info("Server started.");
                //todo: add Database stuff
            }
            else
            {
                LOG.error("Server did not start. Message:" + result.result(), result.cause());
                System.exit(1);
            }
        });

    }


}
