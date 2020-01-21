package com.awslabs.iot.client.applications;

import com.awslabs.aws.iot.resultsiterator.helpers.v1.V1HelperModule;
import com.awslabs.iot.client.interfaces.AwsIotClientTerminal;
import com.beust.jcommander.JCommander;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class AwsIotClientConsole {
    private static AbstractModule[] modules = {new AwsIotClientModule(), new GreengrassModule(), new IotModule(), new LogsModule(), new LambdaModule(), new V1HelperModule()};

    public static void main(String[] args) throws Exception {
        Arguments arguments = new Arguments();

        JCommander.newBuilder()
                .addObject(arguments)
                .build()
                .parse(args);

        AwsIotClientModule.arguments = arguments;

        Injector injector = getInjector();

        AwsIotClientTerminal awsIotClientTerminal = injector.getInstance(AwsIotClientTerminal.class);
        awsIotClientTerminal.start();
    }

    public static Injector getInjector() {
        return Guice.createInjector(modules);
    }
}
