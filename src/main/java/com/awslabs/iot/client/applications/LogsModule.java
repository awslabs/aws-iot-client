package com.awslabs.iot.client.applications;

import com.amazonaws.services.logs.AWSLogsClient;
import com.amazonaws.services.logs.AWSLogsClientBuilder;
import com.awslabs.iot.client.commands.interfaces.CommandHandler;
import com.awslabs.iot.client.commands.logs.GetLogsCommandHandler;
import com.awslabs.iot.client.commands.logs.IotGetLogsCommandHandler;
import com.awslabs.iot.client.helpers.cloudwatch.BasicLogsHelper;
import com.awslabs.iot.client.helpers.cloudwatch.LogsHelper;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

class LogsModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(AWSLogsClient.class).toProvider(() -> (AWSLogsClient) AWSLogsClientBuilder.defaultClient());
        bind(LogsHelper.class).to(BasicLogsHelper.class);

        Multibinder<CommandHandler> commandHandlerMultibinder = Multibinder.newSetBinder(binder(), CommandHandler.class);
        commandHandlerMultibinder.addBinding().to(GetLogsCommandHandler.class);
        commandHandlerMultibinder.addBinding().to(IotGetLogsCommandHandler.class);
    }
}
