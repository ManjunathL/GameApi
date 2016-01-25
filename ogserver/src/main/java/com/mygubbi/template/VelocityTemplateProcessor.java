package com.mygubbi.template;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;

/**
 * Created by test on 24-01-2016.
 */
public class VelocityTemplateProcessor
{

    public String process(String templateName, Object params)
    {
        try
        {
            VelocityEngine ve = new VelocityEngine();
            ve.init();

            VelocityContext context = new VelocityContext();
            context.put("params", params);
            Template t = ve.getTemplate(templateName);
            StringWriter writer = new StringWriter();
            t.merge( context, writer );
            return(writer.toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "Error in preparing content";
        }
    }

}
