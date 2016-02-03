package com.mygubbi.search.suggest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

import org.apache.lucene.search.suggest.InputIterator;
import org.apache.lucene.util.BytesRef;

public class DataIterator implements InputIterator
{
    private Iterator<AutoSuggestData> productIterator;
    private AutoSuggestData currentProduct;

    public DataIterator(Iterator<AutoSuggestData> productIterator)
    {
        this.productIterator = productIterator;
    }

    public boolean hasContexts()
    {
        return true;
    }

    public boolean hasPayloads()
    {
        return true;
    }

    public Comparator<BytesRef> getComparator()
    {
        return null;
    }

    // This method needs to return the key for the record; this is the
    // text we'll be autocompleting against.
    public BytesRef next()
    {
        if (productIterator.hasNext())
        {
            currentProduct = productIterator.next();
            try
            {
                return new BytesRef(currentProduct.title.getBytes("UTF8"));
            }
            catch (UnsupportedEncodingException e)
            {
                throw new Error("Couldn't convert to UTF-8");
            }
        }
        else
        {
            return null;
        }
    }

    // This method returns the payload for the record, which is
    // additional data that can be associated with a record and
    // returned when we do suggestion lookups.  In this example the
    // payload is a serialized Java object representing our product.
    public BytesRef payload()
    {
        try
        {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(currentProduct);
            out.close();
            return new BytesRef(bos.toByteArray());
        }
        catch (IOException e)
        {
            throw new Error("Well that's unfortunate.");
        }
    }

    // This method returns the contexts for the record, which we can
    // use to restrict suggestions.  In this example we use the
    // regions in which a product is sold.
    public Set<BytesRef> contexts()
    {
        if (currentProduct.contexts == null) return Collections.EMPTY_SET;

        try
        {
            Set<BytesRef> contexts = new HashSet();
            for (String context : currentProduct.contexts)
            {
                contexts.add(new BytesRef(context.getBytes("UTF8")));
            }
            return contexts;
        }
        catch (UnsupportedEncodingException e)
        {
            throw new Error("Couldn't convert to UTF-8");
        }
    }

    // This method helps us order our suggestions.  In this example we
    // use the number of products of this type that we've sold.
    public long weight()
    {
        return 0;
    }
}