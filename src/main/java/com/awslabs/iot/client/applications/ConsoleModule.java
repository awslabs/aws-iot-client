package com.awslabs.iot.client.applications;

import com.awslabs.iot.client.helpers.io.BasicIOHelper;
import com.awslabs.iot.client.helpers.io.interfaces.IOHelper;
import com.google.inject.AbstractModule;

class ConsoleModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IOHelper.class).to(BasicIOHelper.class);
    }
}
