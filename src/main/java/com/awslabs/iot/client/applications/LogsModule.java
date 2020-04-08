package com.awslabs.iot.client.applications;

import com.amazonaws.services.logs.AWSLogsClient;
import com.amazonaws.services.logs.AWSLogsClientBuilder;
import com.awslabs.iot.client.commands.interfaces.CommandHandler;
import com.awslabs.iot.client.commands.logs.GetLogsCommandHandler;
import com.awslabs.iot.client.commands.logs.IotGetLogsCommandHandler;
import com.awslabs.iot.client.helpers.cloudwatch.BasicLogsHelper;
import com.awslabs.iot.client.helpers.cloudwatch.LogsHelper;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.ElementsIntoSet;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Module
public class LogsModule {
    @Provides
    public AWSLogsClient awsLogsClient() {
        return (AWSLogsClient) AWSLogsClientBuilder.defaultClient();
    }

    @Provides
    public LogsHelper logsHelper(BasicLogsHelper basicLogsHelper) {
        return basicLogsHelper;
    }

    @Provides
    @ElementsIntoSet
    public Set<CommandHandler> commandHandlerSet(GetLogsCommandHandler getLogsCommandHandler,
                                                 IotGetLogsCommandHandler iotGetLogsCommandHandler) {
        return new HashSet<>(Arrays.asList(getLogsCommandHandler,
                iotGetLogsCommandHandler));
    }
}
