package com.mygubbi.search.suggest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.search.suggest.analyzing.AnalyzingInfixSuggester;
import org.apache.lucene.search.suggest.Lookup;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;

public class AutoSuggester
{
    // Get suggestions given a prefix and a region.
    private static void lookup(AnalyzingInfixSuggester suggester, String name, String region)
    {
        try
        {
            List<Lookup.LookupResult> results;
            HashSet<BytesRef> contexts = new HashSet<BytesRef>();
            contexts.add(new BytesRef(region.getBytes("UTF8")));
            // Do the actual lookup. We ask for the top 2 results.
            results = suggester.lookup(name, 2, true, false);
            System.out.println("-- \"" + name + "\" (" + region + "):");
            for (Lookup.LookupResult result : results)
            {
                System.out.println(result.key);

                AutoSuggestData p = getData(result);
                if (p != null)
                {
                    System.out.println("  title: " + p.title);
                    System.out.println("  query: " + p.query);
                }

            }
        }
        catch (IOException e)
        {
            System.err.println("Error");
        }
    }

    // Deserialize a Product from a LookupResult payload.
    private static AutoSuggestData getData(Lookup.LookupResult result)
    {
        try
        {
            BytesRef payload = result.payload;
            if (payload != null)
            {
                ByteArrayInputStream bis = new ByteArrayInputStream(payload.bytes);
                ObjectInputStream in = new ObjectInputStream(bis);
                AutoSuggestData p = (AutoSuggestData) in.readObject();
                return p;
            }
            else
            {
                return null;
            }
        }
        catch (IOException | ClassNotFoundException e)
        {
            throw new Error("Could not decode payload :(");
        }
    }

    public static void main(String[] args)
    {
        try
        {
            RAMDirectory index_dir = new RAMDirectory();
            StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_48);
            AnalyzingInfixSuggester suggester = new AnalyzingInfixSuggester(
                    Version.LUCENE_48, index_dir, analyzer);

            // Create our list of products.
            ArrayList<AutoSuggestData> products = new ArrayList<AutoSuggestData>();
            products.add(
                    new AutoSuggestData(
                            "Electric Guitar",
                            "http://images.example/electric-guitar.jpg","list"));
            products.add(
                    new AutoSuggestData(
                            "Electric Train",
                            "http://images.example/train.jpg", "product"));
            products.add(
                    new AutoSuggestData(
                            "Acoustic Guitar",
                            "http://images.example/acoustic-guitar.jpg","list"));
            products.add(
                    new AutoSuggestData(
                            "Guarana Soda",
                            "http://images.example/soda.jpg","list"));

            // Index the products with the suggester.
            suggester.build(new DataIterator(products.iterator()));

            // Do some example lookups.
            doLookup(suggester);
            doLookup(suggester);
        }
        catch (IOException e)
        {
            System.err.println("Error!");
        }
    }

    private static void doLookup(AnalyzingInfixSuggester suggester)
    {
        long currentTime = System.currentTimeMillis();
/*
        for (int i=0; i<1000; i++)
        {
*/
        //lookup(suggester, "Gu", "US");
        //lookup(suggester, "Gu", "ZA");
        //lookup(suggester, "Gui", "CA");
        lookup(suggester, "Electric guit", "US");
        lookup(suggester, "Elactric guit", "US");
/*
        }
*/
        long timeTaken = System.currentTimeMillis() - currentTime;
        System.out.println("Total time for 4 lookups (ms):" + timeTaken + ". Avg time for 4 lookups(ms):" + timeTaken);
    }
}
