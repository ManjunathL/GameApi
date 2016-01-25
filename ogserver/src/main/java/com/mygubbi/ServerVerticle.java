package com.mygubbi;

import com.mygubbi.apiserver.ApiServerVerticle;
import com.mygubbi.catalog.ProductManagementService;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.config.ConfigHolder;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.db.QueryPrepareService;
import com.mygubbi.db.SequenceIdGenerator;
import com.mygubbi.si.firebase.FirebaseService;
import com.mygubbi.si.knowtify.KnowtifyService;
import com.mygubbi.support.LogServiceVerticle;
import io.vertx.core.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class ServerVerticle extends AbstractVerticle
{
    private static final Logger LOG = LogManager.getLogger();

    private static final Class[] ALL_SERVICES = new Class[]{LogServiceVerticle.class,
            QueryPrepareService.class, SequenceIdGenerator.class, ApiServerVerticle.class, KnowtifyService.class, FirebaseService.class,
            ProductManagementService.class};

    private static final Class[] DATABASE_SERVICE_ONLY = new Class[]{LogServiceVerticle.class,
            QueryPrepareService.class, SequenceIdGenerator.class, ProductManagementService.class};

    private Class[] servicesToStart = ALL_SERVICES;
    private List<Verticle> verticlesToStart = Collections.emptyList();

    public static void main(String[] args)
    {
        new ServerVerticle().startServerVerticle();
    }

    public ServerVerticle()
    {
        this(false);
    }

    public ServerVerticle(boolean dbOnly)
    {
        this.servicesToStart = dbOnly ? DATABASE_SERVICE_ONLY : ALL_SERVICES;
        if (dbOnly)
        {
            this.verticlesToStart = new ArrayList<>();
            this.verticlesToStart.add(new FirebaseService(false));
        }
    }

    private void startServerVerticle()
    {
        VertxInstance.get().deployVerticle(this, new DeploymentOptions().setWorker(true), result ->
        {
            if (result.succeeded())
            {
                LOG.info("Server started.");
            }
            else
            {
                LOG.error("Server did not start. Message:" + result.result(), result.cause());
                System.exit(1);
            }
        });
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception
    {
        this.startVerticle(startFuture, ConfigHolder.class);
    }

    private void startVerticle(Future<Void> startFuture, Class serviceClass)
    {
        LOG.info(serviceClass.getName() + " starting ...");
        vertx.deployVerticle(serviceClass.getCanonicalName(), result ->
        {
            if (result.succeeded())
            {
                LOG.info(serviceClass.getName() + " started.");
                if (serviceClass == DatabaseService.class)
                {
                    this.startTheRest(startFuture);
                }
                else
                {
                    this.startVerticle(startFuture, DatabaseService.class);
                }
            }
            else
            {
                startFuture.fail(result.cause());
            }
        });
    }

    private void startTheRest(Future<Void> startFuture)
    {

        Set<String> serviceNames = new HashSet<String>();

        for (Class serviceClass : this.servicesToStart)
        {
            String serviceName = serviceClass.getCanonicalName();
            serviceNames.add(serviceName);

            LOG.info(serviceName + " starting ...");
            DeploymentOptions options = this.getDeploymentOptions(serviceClass);
            vertx.deployVerticle(serviceName, options, result ->
            {
                this.handleStartResponse(serviceNames, serviceName, result);
            });
        }

        for (Verticle verticle : this.verticlesToStart)
        {
            String serviceName = verticle.getClass().getCanonicalName();
            serviceNames.add(serviceName);

            LOG.info(serviceName + " starting ...");
            DeploymentOptions options = this.getDeploymentOptions(verticle.getClass());
            vertx.deployVerticle(verticle, options, result ->
            {
                this.handleStartResponse(serviceNames, serviceName, result);
            });
        }

        vertx.setTimer(20000, id -> {

            if (!serviceNames.isEmpty())
            {
                StringBuilder sb = new StringBuilder("Services not started:");
                for (String serviceName : serviceNames)
                {
                    sb.append(serviceName).append(":");
                }
                LOG.info(sb.toString());
                startFuture.fail(sb.toString());
            }
            else
            {
                LOG.info("All services started.");
                startFuture.complete();
            }
        });
    }

    private void handleStartResponse(Set<String> serviceNames, String serviceName, AsyncResult<String> result)
    {
        if (result.succeeded())
        {
            LOG.info(serviceName + " started.");
            serviceNames.remove(serviceName);
        }
        else
        {
            LOG.error(serviceName + " did not start.", result.cause());
        }
    }

    private DeploymentOptions getDeploymentOptions(Class serviceClass)
    {
        DeploymentOptions options = new DeploymentOptions();
        if (serviceClass == ApiServerVerticle.class || serviceClass == LogServiceVerticle.class)
        {
            options.setWorker(false);
        }
        else
        {
            options.setWorker(true);
        }
        return options;
    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception
    {
        super.stop(stopFuture);
    }

}
