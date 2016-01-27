package com.mygubbi.si.firebase;

import com.firebase.client.AuthData;
import com.firebase.client.Config;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.config.ConfigHolder;
import com.mygubbi.crm.NewEnquiryProcessor;
import com.mygubbi.si.data.EventAcknowledger;
import com.mygubbi.user.UserRegistrationProcessor;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by test on 21-01-2016.
 */
public class FirebaseService extends AbstractVerticle
{
    private final static Logger LOG = LogManager.getLogger(FirebaseService.class);

    protected FirebaseEventListener listener;
    protected Firebase fbRef;
    private boolean startEventListener;

    public FirebaseService()
    {
        this(true);
    }

    public FirebaseService(boolean startEventListener)
    {
        this.startEventListener = startEventListener;
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception
    {
        this.setFirebaseConfig();
        this.connectToFirebase(startFuture);
    }

    @Override
    public void stop() throws Exception
    {
        super.stop();
        if (this.listener != null) this.listener.stop();
    }

    private void setFirebaseConfig()
    {
        Config config = new Config();
        config.setPersistenceEnabled(false);
        config.setPersistenceCacheSizeBytes(1024 * 1024 * 32); //32 MB
        Firebase.setDefaultConfig(config);
    }

    private void connectToFirebase(Future<Void> startFuture)
    {
        JsonObject config = (JsonObject) ConfigHolder.getInstance().getConfigValue("firebase");
        if (config == null)
        {
            throw new RuntimeException("Could not find config with key 'firebase'");
        }
        this.fbRef = new Firebase(config.getString("fb_url"));
        this.fbRef.authWithPassword(config.getString("loginid"), config.getString("password"), new Firebase.AuthResultHandler()
        {
            @Override
            public void onAuthenticated(AuthData authData)
            {
                LOG.info("Firebase authenticated for " + config.getString("fb_url"));
                setup(startFuture);
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError)
            {
                LOG.error("Firebase authentication failed." + firebaseError.getMessage() + "|" + firebaseError.getDetails());
                startFuture.fail(firebaseError.toException());
            }
        });
    }

    private void setup(Future<Void> startFuture)
    {
        if (this.startEventListener) this.setupEventListener();
        this.setupProductUpdateService(startFuture);
    }

    private void setupEventListener()
    {
        Firebase eventsFbRef = this.fbRef.child("/events");
        EventAcknowledger acknowledger = new FirebaseAcknowledger(eventsFbRef);
        this.listener = new FirebaseEventListener(eventsFbRef, acknowledger);
        this.listener.register(new UserRegistrationProcessor(acknowledger));
        this.listener.register(new NewEnquiryProcessor(acknowledger));
        this.listener.start();
    }

    private void setupProductUpdateService(Future<Void> startFuture)
    {
        Firebase productsFbRef = this.fbRef.child("/products");
        FirebaseProductUpdateService productUpdateService = new FirebaseProductUpdateService(productsFbRef);
        VertxInstance.get().deployVerticle(productUpdateService, new DeploymentOptions().setWorker(true), result ->
        {
            if (result.succeeded())
            {
                LOG.info("FirebaseProductUpdateService started.");
                startFuture.complete();
            }
            else
            {
                LOG.error("FirebaseProductUpdateService did not start. Message:" + result.result(), result.cause());
                startFuture.fail(result.result());
            }
        });
    }

}
