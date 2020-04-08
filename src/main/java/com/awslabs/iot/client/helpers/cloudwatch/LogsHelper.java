package com.awslabs.iot.client.helpers.cloudwatch;

import software.amazon.awssdk.services.cloudwatchlogs.model.OutputLogEvent;

import java.util.List;

public interface LogsHelper {
    List<OutputLogEvent> getLogs(String logGroupName, List<String> searchStrings);
}
