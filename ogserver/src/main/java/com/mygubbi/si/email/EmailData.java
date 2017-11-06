package com.mygubbi.si.email;

import com.mygubbi.common.StringUtils;
import com.mygubbi.template.VelocityTemplateProcessor;

import java.util.Arrays;
import java.util.List;

/**
 * Created by test on 24-01-2016.
 */
public class EmailData
{
    private String toEmail;
    private String fromEmail;
    private String[] toEmails;
    private String subject;
    private String textMessage;
    private String bodyTemplate;
    private String subjectemplate;
    private Object params;
    private boolean isHtmlBody;

    public String getMessageBody()
    {
        if (this.isHtmlBody() && !StringUtils.isEmpty(this.getBodyTemplate()))
        {
            return new VelocityTemplateProcessor().process(this.getBodyTemplate(), this.getParams());
        }
        else
        {
            return this.textMessage;
        }
    }
    public String[] getToEmails() {
        return toEmails;
    }

    public EmailData setToEmails(String[] toEmails) {
        this.toEmails = toEmails;
        return  this;
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
        if (!StringUtils.isEmpty(this.getSubjectTemplate()))
        {
            return new VelocityTemplateProcessor().process(this.getSubjectTemplate(), this.getParams());
        }
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

    public String getBodyTemplate()
    {
        return bodyTemplate;
    }

    public EmailData setBodyTemplate(String bodyTemplate)
    {
        this.bodyTemplate = bodyTemplate;
        return this;
    }

    public String getSubjectTemplate()
    {
        return subjectemplate;
    }

    public EmailData setSubjectTemplate(String subjectTemplate)
    {
        this.subjectemplate = subjectTemplate;
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

    @Override
    public String toString() {
        return "EmailData{" +
                "toEmail='" + toEmail + '\'' +
                ", fromEmail='" + fromEmail + '\'' +
                ", toEmails=" + Arrays.toString(toEmails) +
                ", subject='" + subject + '\'' +
                ", textMessage='" + textMessage + '\'' +
                ", bodyTemplate='" + bodyTemplate + '\'' +
                ", subjectemplate='" + subjectemplate + '\'' +
                ", params=" + params +
                ", isHtmlBody=" + isHtmlBody +
                '}';
    }
}
