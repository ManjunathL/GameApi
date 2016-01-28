package com.mygubbi.si.firebase;

import com.firebase.client.Firebase;
import com.mygubbi.catalog.ProductJson;
import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Sunil on 06-01-2016.
 */
public class FirebaseProductUpdateService extends AbstractVerticle
{
    private final static Logger LOG = LogManager.getLogger(FirebaseProductUpdateService.class);

    public static final String UPDATE_PRODUCT_IN_FB = "update.product.fb";

    private Firebase fbRef;

    public FirebaseProductUpdateService(Firebase fbRef)
    {
        this.fbRef = fbRef;
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception
    {
        this.setupEventListener();
        startFuture.complete();
    }

    @Override
    public void stop() throws Exception
    {
        super.stop();
    }

    private void setupEventListener()
    {
        EventBus eb = VertxInstance.get().eventBus();
        eb.localConsumer(UPDATE_PRODUCT_IN_FB, (Message<Integer> message) -> {
            ProductJson product = (ProductJson) LocalCache.getInstance().remove(message.body());

            try
            {
                Firebase productRef = this.fbRef.child(product.getCategory()).child(product.getSubCategory()).child(product.getProductId());
                new FirebaseObjectMapper().fromJsonToFirebase(productRef, product);
                message.reply("Product is updated in firebase");
            }
            catch (Exception e)
            {
                message.fail(0, "Product is not updated in firebase. " + e.getMessage());
            }

        }).completionHandler(res -> {
            LOG.info("ProductManagementService started." + res.succeeded());
        });

    }

}
