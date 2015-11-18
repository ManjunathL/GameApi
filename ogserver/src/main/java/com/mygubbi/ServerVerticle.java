package com.mygubbi;

import com.mygubbi.apiserver.ApiServerVerticle;
import com.mygubbi.config.ConfigHolder;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryPrepareService;
import com.mygubbi.db.SequenceIdGenerator;
import com.mygubbi.support.LogServiceVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

public class ServerVerticle extends AbstractVerticle {
    private static final Logger LOG = LogManager.getLogger();

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(ServerVerticle.class.getCanonicalName(), new DeploymentOptions().setWorker(true), result ->
        {
            if (result.succeeded()) {
                LOG.info("Server started.");
            } else {
                LOG.error("Server did not start. Message:" + result.result(), result.cause());
                System.exit(1);
            }
        });
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        this.startVerticle(startFuture, ConfigHolder.class);
    }

    private void startVerticle(Future<Void> startFuture, Class serviceClass) {
        LOG.info(serviceClass.getName() + " starting ...");
        vertx.deployVerticle(serviceClass.getCanonicalName(), result ->
        {
            if (result.succeeded()) {
                LOG.info(serviceClass.getName() + " started.");
                if (serviceClass == DatabaseService.class) {
                    this.startTheRest(startFuture);
                } else {
                    this.startVerticle(startFuture, DatabaseService.class);
                }
            } else {
                startFuture.fail(result.cause());
            }
        });
    }

    private void startTheRest(Future<Void> startFuture) {

        Class[] servicesToStart = new Class[]{LogServiceVerticle.class,
                QueryPrepareService.class, SequenceIdGenerator.class, ApiServerVerticle.class};

        Set<String> serviceNames = new HashSet<String>();

        for (Class serviceClass : servicesToStart) {
            String serviceName = serviceClass.getCanonicalName();
            serviceNames.add(serviceName);
            LOG.info(serviceName + " starting ...");
            DeploymentOptions options = new DeploymentOptions();
			if (serviceClass == ApiServerVerticle.class || serviceClass == LogServiceVerticle.class)
			{
				options.setWorker(false);
			}
			else
			{
				options.setWorker(true);
			}
            vertx.deployVerticle(serviceName, options, result ->
            {
                if (result.succeeded()) {
                    LOG.info(serviceName + " started.");
                    serviceNames.remove(serviceName);
                } else {
                    LOG.error(serviceName + " did not start.", result.cause());
                }
            });
        }

        vertx.setTimer(10000, id -> {

            if (!serviceNames.isEmpty()) {
                StringBuilder sb = new StringBuilder("Services not started:");
                for (String serviceName : serviceNames) {
                    sb.append(serviceName).append(":");
                }
                LOG.info(sb.toString());
                startFuture.fail(sb.toString());
            } else {
                LOG.info("All services started.");
                startFuture.complete();
            }
        });


    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {
        super.stop(stopFuture);
    }


}
