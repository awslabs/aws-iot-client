package com.awslabs.iot.client.helpers.cloudwatch;

import com.amazonaws.services.logs.model.OutputLogEvent;

import java.util.List;

public interface LogsHelper {
    List<OutputLogEvent> getLogs(String logGroupName, List<String> searchStrings);
}
