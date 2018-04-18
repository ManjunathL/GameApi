package com.mygubbi.route;

import com.mygubbi.si.amazonS3.S3ImageLoaderService;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Objects;

/**
 * Created by User on 29-03-2018.
 */
public class ImageRetrieverHandler extends AbstractRouteHandler {

    private final static Logger LOG = LogManager.getLogger(ImageRetrieverHandler.class);

    private final static String baseUrlCloudFront = "http://designwhimages.mygubbi.com/";
    private final static String baseUrlCloudinary = "https://res.cloudinary.com/mygubbi/";
    private final static String baseUrlMygubbi = "https://www.mygubbi.com/images/";


    public ImageRetrieverHandler(Vertx vertx) {
        super(vertx);
        this.route().handler(BodyHandler.create());
        this.get("/getimage").handler(this::imageRetriever);

    }

    private void imageRetriever(RoutingContext routingContext) {

        LOG.debug("Hitting it");

        String imgUrl = routingContext.getBodyAsString();

        String impImgUrl = imgUrl.replace(baseUrlMygubbi,"");

        String symbolReplacedUrl = impImgUrl.replace("/","+");

        symbolReplacedUrl = baseUrlCloudFront + symbolReplacedUrl;

        String responseUrl = S3ImageLoaderService.getInstance().returnImageIfPresent(symbolReplacedUrl);

        if (responseUrl != null)
        {
            LOG.debug("found");
            //RouteUtil.getInstance().redirect(routingContext,responseUrl,"Redirecting");
        }
        else
        {
            LOG.debug(" not found");
            //RouteUtil.getInstance().redirect(routingContext,null,"Redirecting");
        }


    }

}
