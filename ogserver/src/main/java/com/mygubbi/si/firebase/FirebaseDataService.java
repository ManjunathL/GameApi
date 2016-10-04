package com.mygubbi.si.firebase;

import com.google.firebase.database.*;
import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

/**
 * Created by Sunil on 27-09-2016.
 */
public class FirebaseDataService extends AbstractVerticle
{
    private static final Logger LOG = LogManager.getLogger(FirebaseDataService.class);

    public static String UPDATE_DB = "update.firebasedb";

    @Override
    public void start(Future<Void> startFuture) throws Exception
    {
        try
        {
            new FirebaseLifecycleManager().init();
            this.setupDatabaseUpdater();
            startFuture.complete();
            LOG.info("FirebaseDataService started");
        }
        catch (Exception e)
        {
            LOG.error("Error in starting FirebaseDataService", e);
            startFuture.fail(e.getMessage());
        }
    }

    private void setupDatabaseUpdater()
    {
        EventBus eb = VertxInstance.get().eventBus();
        eb.localConsumer(UPDATE_DB, (Message<Integer> message) -> {
            FirebaseDataRequest dataRequest = (FirebaseDataRequest) LocalCache.getInstance().remove(message.body());
            this.updateDatabase(dataRequest, message);
        }).completionHandler(res -> {
            LOG.info("Firebase data updater setup." + res.succeeded());
        });
    }

    private void updateDatabase(final FirebaseDataRequest dataRequest, final Message message)
    {
        try
        {
            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference(dataRequest.getDataUrl());

            ref.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    if (dataRequest.getJsonData() != null)
                    {
                        new FirebaseObjectMapper().fromJsonToFirebase(ref, dataRequest.getJsonData());
                    }
                    else
                    {
                        dataRequest.setError(true).setErrorMessage("Data not set in request.");
                    }
                    message.reply(LocalCache.getInstance().store(dataRequest));
                }

                @Override
                public void onCancelled(DatabaseError databaseError)
                {
                    LOG.error("Error in updating firebase db. Data:" + dataRequest.toString(), databaseError);
                    dataRequest.setError(true).setErrorMessage(databaseError.getMessage());
                    message.reply(LocalCache.getInstance().store(dataRequest));
                }
            });
        }
        catch (Exception e)
        {
            LOG.error("Error in updating firebase db. Data:" + dataRequest.toString(), e);
            dataRequest.setError(true).setErrorMessage(e.getMessage());
            message.reply(LocalCache.getInstance().store(dataRequest));
        }
    }

    public static void main(String[] args) throws Exception
    {
        String servicesJson = "config/test/mygubbi-cep-firebase.json";
        String fbUrl = "https://mygubbi-cep.firebaseio.com";
        new FirebaseLifecycleManager(servicesJson, fbUrl).init();
        System.out.println("App name:" + FirebaseDatabase.getInstance().getApp().getName());
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("/projects")
                .child("sYnzAWxhSqUCtCqdZGxE7bqgNia2").child("my-nest").child("project_details");

        ref.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                System.out.println("Project:" + dataSnapshot.getValue());
                JsonObject projectJson = new JsonObject().put("property_name", "Axis Aspira")
                        .put("property_type", "3BHK with Terrace")
                        .put("property_city", "Bangalore")
                        .put("updated_on", new Date(System.currentTimeMillis()).toString());
                System.out.println("Updating firebase");
                new FirebaseObjectMapper().fromJsonToFirebase(ref, projectJson);
                System.out.println("Updating firebase done");
                System.exit(0);
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
        Thread.sleep(5000);
    }
}
