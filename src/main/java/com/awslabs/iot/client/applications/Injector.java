package com.awslabs.iot.client.applications;

import com.awslabs.iot.client.interfaces.AwsIotClientTerminal;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = AwsIotClientModule.class)
public interface Injector {
    AwsIotClientTerminal awsIotClientTerminal();

    Arguments arguments();
}
