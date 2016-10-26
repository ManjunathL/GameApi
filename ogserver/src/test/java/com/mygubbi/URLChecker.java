package com.mygubbi;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.utils.URIUtils;

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
            "https://www.mygubbi.com", "https://www.mygubbi.com/", "https://www.mygubbi.com/bangalore-lp1"
    };

    public static void main(String[] args)
    {
        new URLChecker().check();
    }

    public void check()
    {
        long start = System.currentTimeMillis();
        int count = 0;
        for (int i = 0; i < 100000; i++)
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
            boolean hostNameIsNaked = StringUtils.countMatches(hostName, ".") == 1;
            boolean httpScheme = ("http").equals(httpHost.getSchemeName());
            if (hostNameIsNaked || httpScheme)
            {
                if (hostNameIsNaked) hostName = "www." + hostName;
                URI newUri = URIUtils.rewriteURI(baseUri, new HttpHost(hostName, httpHost.getPort(), "https"));
                newUri.toString();
                //System.out.println("URL " + url + " rewritten as :" + newUri.toString() + " in " + duration);
            }
            else
            {
//                System.out.println("URL " + url + " not rewritten in "  + duration);
            }
        }
        catch (URISyntaxException e)
        {
            System.out.println("Error with url:" + url + " || " + e.getMessage());
        }
    }
}
