package com.mygubbi;

import com.mygubbi.apiserver.ApiServerVerticle;
import com.mygubbi.common.StringUtils;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.config.ConfigHolder;
import com.mygubbi.db.DatabaseService;
import com.mygubbi.support.LogServiceVerticle;
import io.vertx.core.*;
import io.vertx.core.json.JsonArray;import io.vertx.core.json.JsonObject;import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class ServerVerticle extends AbstractVerticle
{
    private static final Logger LOG = LogManager.getLogger();

    private List<String> configFiles;

    public static void main(String[] args)
    {
        if (args.length == 1)
        {
            new ServerVerticle(args[0]).startServerVerticle();
        }
        else
        {
            new ServerVerticle().startServerVerticle();
        }
    }

    public ServerVerticle()
    {
        this(Collections.EMPTY_LIST);
    }

    public ServerVerticle(String configFilesAsText)
    {
        this(StringUtils.fastSplit(configFilesAsText, ','));
    }

    public ServerVerticle(List<String> configFiles)
    {
        this.configFiles = configFiles;
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
        this.startConfigService(startFuture);
    }

    private void startConfigService(Future<Void> startFuture)
    {
        VertxInstance.get().deployVerticle(new ConfigHolder(this.configFiles), new DeploymentOptions().setWorker(true), result ->
        {
            if (result.succeeded())
            {
                LOG.info("Config Service started.");
                JsonObject services = (JsonObject) ConfigHolder.getInstance().getConfigValue("services");
                if (services == null)
                {
                    startFuture.complete();
                    return;
                }
                List<Class> parallelServices = this.getClasses(services.getJsonArray("parallel"));
                Stack<Class> sequentialServices = new Stack<>();
                sequentialServices.addAll(this.getClasses(services.getJsonArray("sequential")));
                this.startParallelAfterSequential(startFuture, sequentialServices, parallelServices);
            }
            else
            {
                LOG.error("Server did not start. Message:" + result.result(), result.cause());
                System.exit(1);
            }
        });
    }

    private void startParallelAfterSequential(Future<Void> startFuture, final Stack<Class> sequentialServices, List<Class> parallelServices)
    {
        if (sequentialServices.isEmpty())
        {
            this.startInParallel(startFuture, parallelServices);
        }
        else
        {
            Class serviceClass = sequentialServices.pop();
            LOG.info(serviceClass.getName() + " starting ...");
            VertxInstance.get().deployVerticle(serviceClass.getCanonicalName(), result ->
            {
                if (result.succeeded())
                {
                    LOG.info(serviceClass.getName() + " started.");
                    this.startParallelAfterSequential(startFuture, sequentialServices, parallelServices);
                }
                else
                {
                    startFuture.fail(result.cause());
                }
            });
        }

    }

    private void startInParallel(Future<Void> startFuture, List<Class> servicesToStart)
    {

        Set<String> serviceNames = new HashSet<String>();

        for (Class serviceClass : servicesToStart)
        {
            String serviceName = serviceClass.getCanonicalName();
            serviceNames.add(serviceName);

            LOG.info(serviceName + " starting ...");
            DeploymentOptions options = this.getDeploymentOptions(serviceClass);
            VertxInstance.get().deployVerticle(serviceName, options, result ->
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

    private List<Class> getClasses(JsonArray classNames)
    {
        List<Class> serviceClasses = new ArrayList<>();
        for (String className : (List<String>) classNames.getList())
        {
            try
            {
                serviceClasses.add(Class.forName(className));
            }
            catch (ClassNotFoundException e)
            {
                LOG.error("Class not found for : " + className, e);
            }
        }
        return serviceClasses;
    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception
    {
        super.stop(stopFuture);
    }

}
