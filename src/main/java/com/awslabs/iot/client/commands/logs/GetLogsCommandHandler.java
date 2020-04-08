package com.awslabs.iot.client.commands.logs;

import com.amazonaws.services.logs.model.OutputLogEvent;
import com.awslabs.general.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.helpers.cloudwatch.LogsHelper;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;
import java.util.Optional;

public class GetLogsCommandHandler implements LogsCommandHandler {
    private static final String GET = "get";
    private static final Logger log = LoggerFactory.getLogger(GetLogsCommandHandler.class);
    @Inject
    Provider<LogsHelper> logsHelperProvider;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;

    @Inject
    public GetLogsCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        List<String> parameters = parameterExtractor.getParameters(input);

        String logGroupName = parameters.remove(0);

        List<OutputLogEvent> logs = logsHelperProvider.get().getLogs(logGroupName, parameters);

        logs.stream()
                .forEach(outputLogEvent -> log.info(outputLogEvent.getMessage()));
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
    public Optional<Integer> maximumParameters() {
        return Optional.of(Integer.MAX_VALUE);
    }

    public ParameterExtractor getParameterExtractor() {
        return this.parameterExtractor;
    }

    public IoHelper getIoHelper() {
        return this.ioHelper;
    }
}
