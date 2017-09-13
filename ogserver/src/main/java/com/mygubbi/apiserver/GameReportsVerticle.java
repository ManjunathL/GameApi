package com.mygubbi.apiserver;

import com.diabolicallabs.vertx.cron.CronObservable;
import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.config.ConfigHolder;
import com.mygubbi.game.proposal.ProposalHandler;
import com.mygubbi.pipeline.MessageDataHolder;
import com.mygubbi.pipeline.PipelineExecutor;
import com.mygubbi.pipeline.PipelineResponseHandler;
import com.mygubbi.report.ReportTableFillerSevice;
import io.vertx.core.*;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.rx.java.RxHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rx.Scheduler;

import java.util.List;


public class GameReportsVerticle extends AbstractVerticle
{
    private final static Logger LOG = LogManager.getLogger(GameReportsVerticle.class);

    private static final String STANDARD_RESPONSE = new JsonObject().put("status", "error")
            .put("error", "Request did not have a response").toString();

    public static void main(String[] args)
    {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(GameReportsVerticle.class.getCanonicalName(), new DeploymentOptions().setInstances(4));
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception
    {
        LOG.info("Shilpa GameReportsVerticle Started");
        this.runPeriodictimer();
        startFuture.complete();
    }

    private void runPeriodictimer(){
        LOG.info("Shilpa inside  runPeriodictimer");
        Scheduler scheduler = RxHelper.scheduler(vertx);
        String cronText = ConfigHolder.getInstance().getStringValue("reports_cronText","0 0 22 1/1 * ? *");
        String timeZone = ConfigHolder.getInstance().getStringValue("reports_time_zone_name","Asia/Calcutta");

        CronObservable.cronspec(scheduler, cronText, timeZone)
//                .take(5) //If you only want it to hit 5 times, add this, remove for continuous emission
                .subscribe(
                        timestamped -> {
                            //Perform the scheduled activity here
                            System.out.println("Timer 1 fired: " );
                            MessageDataHolder dataHolder = new MessageDataHolder(ReportTableFillerSevice.RUN_FOR_UPDATED_PROPOSALS, new JsonObject());
                            new PipelineExecutor().execute(dataHolder, new GameReportsVerticle.ReportTablefillerResponseHandler());

                        },
                        fault -> {
                            fault.printStackTrace();
                            LOG.error("Fault in Running Daily Report.");
                        }
                );

//        long timerID = vertx.setPeriodic(54000000, new Handler<Long>() {
//
//            @Override
//            public void handle(Long aLong) {
//                System.out.println("Timer 1 fired: " + aLong);
//                int id = LocalCache.getInstance().store(new JsonObject());
//                VertxInstance.get().eventBus().send(ReportTableFillerSevice.RUN_FOR_UPDATED_PROPOSALS, id,
//                        (AsyncResult<Message<Integer>> selectResult) ->
//                        {
//                            JsonObject resultData = (JsonObject) LocalCache.getInstance().remove(selectResult.result().body());
//                            if (!resultData.getBoolean("status"))
//                            {
//                                    LOG.error("Error in Running Daily Report.");
//                            }
//                            else
//                            {
//                                     LOG.info("Successfully Ran Daily Report");
//                            }
//                        });
//
//                 }
//        });

    }

    @Override
    public void stop() throws Exception
    {
        super.stop();
    }

    private class ReportTablefillerResponseHandler implements PipelineResponseHandler
    {

        public ReportTablefillerResponseHandler()
        {

        }

        @Override
        public void handleResponse(List<MessageDataHolder> messageDataHolders)
        {
            LOG.debug("From proposalHandler service ");
//            message.reply(LocalCache.getInstance().store(new JsonObject().put("status","success")));
//            sendJsonResponse(context,new JsonObject().put("status","success").toString());


        }
    }
}
