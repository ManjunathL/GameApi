package com.mygubbi.si.firebase;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.JksOptions;
import io.vertx.ext.web.Router;

/**
 * Created by Sunil on 06-01-2016.
 */
public class FirebaseService extends AbstractVerticle
{

    @Override
    public void start(Future<Void> startFuture) throws Exception {

        this.setupFirebaseReader();
        startFuture.complete();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    private void setupFirebaseReader()
    {

    }
}
