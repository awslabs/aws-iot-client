package com.awslabs.iot.client.commands.logs;


import com.awslabs.iot.client.helpers.cloudwatch.LogsHelper;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.jcabi.log.Logger;
import io.vavr.collection.List;
import io.vavr.control.Option;
import software.amazon.awssdk.services.cloudwatchlogs.model.OutputLogEvent;

import javax.inject.Inject;
import javax.inject.Provider;

public class GetLogsCommandHandler implements LogsCommandHandler {
    private static final String GET = "get";
    @Inject
    Provider<LogsHelper> logsHelperProvider;
    @Inject
    ParameterExtractor parameterExtractor;

    @Inject
    public GetLogsCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        List<String> parameters = parameterExtractor.getParameters(input);

        String logGroupName = parameters.get(0);

        List<OutputLogEvent> logs = logsHelperProvider.get().getLogs(logGroupName, parameters);

        logs.forEach(outputLogEvent -> Logger.info(this, outputLogEvent.message()));
    }

    @Override
    public String getCommandString() {
        return GET;
    }

    @Override
    public String getHelp() {
        return "Finds log messages in CloudWatch logs.  First parameter is log group name, subsequent parameters are search strings that all must be present in the log message.";
    }

    @Override
    public int requiredParameters() {
        return 2;
    }

    @Override
    public Option<Integer> maximumParameters() {
        return Option.of(Integer.MAX_VALUE);
    }

    public ParameterExtractor getParameterExtractor() {
        return this.parameterExtractor;
    }
}
