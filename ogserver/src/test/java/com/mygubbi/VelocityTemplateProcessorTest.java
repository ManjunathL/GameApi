package com.mygubbi;

import com.mygubbi.template.VelocityTemplateProcessor;
import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by test on 26-01-2016.
 */
public class VelocityTemplateProcessorTest
{
    public static void main(String[] args) throws Exception
    {
        new VelocityTemplateProcessorTest().checkContent();
    }

    private void checkContent()
    {
        JsonObject jsonData = new JsonObject().put("name", "Sweety");
        System.out.println(new VelocityTemplateProcessor().process("email/welcome.user.vm", jsonData.getMap()));
    }

}
