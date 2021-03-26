package com.awslabs.iot.client.helpers.cloudwatch;

import io.vavr.collection.List;
import software.amazon.awssdk.services.cloudwatchlogs.model.OutputLogEvent;

public interface LogsHelper {
    List<OutputLogEvent> getLogs(String logGroupName, List<String> searchStrings);
}
