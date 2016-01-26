package com.mygubbi.common;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.mygubbi.template.VelocityTemplateProcessor;

/**
 * Created by test on 15-01-2016.
 */
public class GuiceBinder extends AbstractModule
{
    @Override
    protected void configure()
    {
        bind(VelocityTemplateProcessor.class).to(VelocityTemplateProcessor.class).asEagerSingleton();
    }
}
