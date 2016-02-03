package com.mygubbi.search.suggest;

import java.io.Serializable;

public class AutoSuggestData implements Serializable
{
    String title;
    String query;
    String type;
    String[] contexts = new String[]{"US"};

    public AutoSuggestData(String title, String query, String type)
    {
        this.title = title;
        this.query = query;
        this.type = type;
    }
}
