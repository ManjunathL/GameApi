package com.mygubbi;


import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.utils.URIUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by test on 26-10-2016.
 */
public class URLChecker
{
    private static String[] URLS = new String[]{
            "http://mygubbi.com", "http://mygubbi.com/", "http://mygubbi.com/bangalore-lp1",
            "http://www.mygubbi.com", "http://www.mygubbi.com/", "http://www.mygubbi.com/bangalore-lp1",
            "https://mygubbi.com", "https://mygubbi.com/", "https://mygubbi.com/bangalore-lp1",
            "https://www.mygubbi.com", "https://www.mygubbi.com/", "https://www.mygubbi.com/bangalore-lp1",
            "https://uat.mygubbi.com", "https://mydev.mygubbi.com/", "https://ozone.mygubbi.com/bangalore-lp1",
            "https://abc.mygubbi.com", "https://mehbub.mygubbi.com/", "https://shobha.mygubbi.com/bangalore-lp1"
    };
    private final static Logger LOG = LogManager.getLogger(URLChecker.class);

    public static void main(String[] args)
    {
        new URLChecker().check();
    }

    public void check()
    {
        long start = System.currentTimeMillis();
        int count = 0;
        for (int i = 0; i < 2; i++)
        {
            for (String url : URLS)
            {
                this.check(url);
                count++;
            }
        }
        long duration = System.currentTimeMillis() - start;
        System.out.println("Total rewrites:" + count + " in " + duration + ". Avg:" + new Double(duration)/count);
    }


    private void check(String url)
    {
        try
        {
            URI baseUri = new URI(url);
            HttpHost httpHost = URIUtils.extractHost(baseUri);
            String hostName = httpHost.getHostName();
            LOG.info("=-=-=-baseUri-=-=-");
            LOG.info(baseUri);
            LOG.info("hostName");
            LOG.info(hostName);
            String beforeFirstDot = hostName.split("\\.")[0];
            LOG.info(beforeFirstDot);
            boolean hostname = StringUtils.indexOf(hostName,".") == 1;
            if(!"www".equals(beforeFirstDot)  && !"mygubbi".equals(beforeFirstDot)){
                LOG.info("i am In Builder");
                URI newUri = URIUtils.rewriteURI(baseUri, new HttpHost(hostName, httpHost.getPort(), "https"));
                url = newUri.toString();
                LOG.info("URL " + url + " rewritten as :" + newUri.toString());
            }
            boolean hostNameIsNaked = StringUtils.countMatches(hostName, ".") == 1;
            boolean httpScheme = ("http").equals(httpHost.getSchemeName());
            boolean httpsScheme = ("https").equals(httpHost.getSchemeName());
            if (hostNameIsNaked)
                hostName = "www." + hostName;
            URI newUri = URIUtils.rewriteURI(baseUri, new HttpHost(hostName, httpHost.getPort(), "https"));
            newUri.toString();
            System.out.println("URL " + url + " rewritten as :" + newUri.toString() + " in ");

        }
        catch (URISyntaxException e)
        {
            System.out.println("Error with url:" + url + " || " + e.getMessage());
        }
    }
}
