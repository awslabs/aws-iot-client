package com.awslabs.iot.client.applications;

import com.awslabs.iot.client.commands.interfaces.CommandHandler;
import com.awslabs.iot.client.commands.logs.GetLogsCommandHandler;
import com.awslabs.iot.client.commands.logs.IotGetLogsCommandHandler;
import com.awslabs.iot.client.helpers.cloudwatch.BasicLogsHelper;
import com.awslabs.iot.client.helpers.cloudwatch.LogsHelper;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.ElementsIntoSet;
import io.vavr.collection.HashSet;
import software.amazon.awssdk.services.cloudwatchlogs.CloudWatchLogsClient;

import java.util.Set;

@Module
public class LogsModule {
    @Provides
    public CloudWatchLogsClient cloudWatchLogsClient() {
        return CloudWatchLogsClient.create();
    }

    @Provides
    public LogsHelper logsHelper(BasicLogsHelper basicLogsHelper) {
        return basicLogsHelper;
    }

    @Provides
    @ElementsIntoSet
    public Set<CommandHandler> commandHandlerSet(GetLogsCommandHandler getLogsCommandHandler,
                                                 IotGetLogsCommandHandler iotGetLogsCommandHandler) {
        return HashSet.<CommandHandler>of(
                getLogsCommandHandler,
                iotGetLogsCommandHandler)
                .toJavaSet();
    }
}
