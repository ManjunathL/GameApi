package com.mygubbi.si.email;

import com.mygubbi.common.StringUtils;
import com.mygubbi.template.VelocityTemplateProcessor;

/**
 * Created by test on 24-01-2016.
 */
public class EmailData
{
    private String toEmail;
    private String fromEmail;
    private String subject;
    private String textMessage;
    private String templateName;
    private Object params;
    private boolean isHtmlBody;

    public String getMessageBody()
    {
        if (!StringUtils.isEmpty(this.getTemplateName()))
        {
            return new VelocityTemplateProcessor().process(this.getTemplateName(), this.getParams());
        }
        else
        {
            return this.textMessage;
        }
    }

    public boolean isHtmlBody()
    {
        return isHtmlBody;
    }

    public EmailData setHtmlBody(boolean htmlBody)
    {
        isHtmlBody = htmlBody;
        return this;
    }

    public String getToEmail()
    {
        return toEmail;
    }

    public EmailData setToEmail(String toEmail)
    {
        this.toEmail = toEmail;
        return this;
    }

    public String getFromEmail()
    {
        return fromEmail;
    }

    public EmailData setFromEmail(String fromEmail)
    {
        this.fromEmail = fromEmail;
        return this;
    }

    public String getSubject()
    {
        return subject;
    }

    public EmailData setSubject(String subject)
    {
        this.subject = subject;
        return this;
    }

    public String getTextMessage()
    {
        return textMessage;
    }

    public EmailData setTextMessage(String textMessage)
    {
        this.textMessage = textMessage;
        return this;
    }

    public String getTemplateName()
    {
        return templateName;
    }

    public EmailData setTemplateName(String templateName)
    {
        this.templateName = templateName;
        return this;
    }

    public Object getParams()
    {
        return params;
    }

    public EmailData setParams(Object params)
    {
        this.params = params;
        return this;
    }
}
