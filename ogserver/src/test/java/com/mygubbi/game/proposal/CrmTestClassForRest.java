package com.mygubbi.game.proposal;

import us.monoid.json.JSONArray;
import us.monoid.web.Resty;
import static us.monoid.web.Resty.form;
import static us.monoid.web.Resty.data;

/**
 * Created by User on 28-03-2018.
 */
public class CrmTestClassForRest {

    public static void main(String[] args) throws Exception
    {
        new CrmTestClassForRest().convert();
    }

    public void convert() throws Exception
    {
        String url = "https://suite.mygubbi.com/mygubbi_crm29102017/test-api/rest_get_doc.php";
        JSONArray array = new Resty().json(url, form(data("parent_id", "SAL-1611-000681"), data("parent", "Opportunities"), data("type", "all"))).array();
        System.out.println(array.toString());
    }
}
