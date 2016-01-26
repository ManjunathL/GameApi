package com.mygubbi.si.data;

/**
 * Created by Sunil on 07-01-2016.
 */
public interface DataProcessor
{
    public static final String USER_ADD_EVENT = "user.add";
    public static final String USER_UPDATE_EVENT = "user.update";
    public static final String USER_REMOVE_EVENT = "user.remove";
    public static final String SHORTLIST_ADD_EVENT = "shortlist.product.add";
    public static final String SHORTLIST_REMOVE_EVENT = "shortlist.product.remove";
    public static final String CONSULT_EVENT = "consult";

    public abstract String getName();

    public abstract void process(EventData data);

}
