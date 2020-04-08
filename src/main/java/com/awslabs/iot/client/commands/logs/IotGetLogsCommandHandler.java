package com.awslabs.iot.client.commands.logs;

import com.awslabs.general.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.helpers.cloudwatch.LogsHelper;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.cloudwatchlogs.model.OutputLogEvent;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;
import java.util.Optional;

public class IotGetLogsCommandHandler implements LogsCommandHandler {
    private static final String GET = "iot-get";
    private static final String logGroupName = "AWSIotLogsV2";
    private static final Logger log = LoggerFactory.getLogger(IotGetLogsCommandHandler.class);
    @Inject
    Provider<LogsHelper> logsHelperProvider;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;

    @Inject
    public IotGetLogsCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        List<String> parameters = parameterExtractor.getParameters(input);

        List<OutputLogEvent> logs = logsHelperProvider.get().getLogs(logGroupName, parameters);

        logs.forEach(outputLogEvent -> log.info(outputLogEvent.message()));
    }

    @Override
    public String getCommandString() {
        return GET;
    }

    @Override
    public String getHelp() {
        return "Finds log messages in CloudWatch logs from AWS IoT.  Parameters are search strings that all must be present in the log message.";
    }

    @Override
    public int requiredParameters() {
        return 1;
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
