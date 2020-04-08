package com.awslabs.iot.client.applications;

import com.awslabs.iot.client.interfaces.AwsIotClientTerminal;
import com.beust.jcommander.JCommander;

public class AwsIotClientConsole {
    public static void main(String[] args) throws Exception {
        Arguments arguments = new Arguments();

        JCommander.newBuilder()
                .addObject(arguments)
                .build()
                .parse(args);

        Injector injector = DaggerInjector.create();

        // Set global arguments
        injector.arguments().dangerousMode = arguments.dangerousMode;

        AwsIotClientTerminal awsIotClientTerminal = injector.awsIotClientTerminal();
        awsIotClientTerminal.start();
    }
}
