package com.awslabs.iot.client.helpers.cloudwatch;

import com.amazonaws.services.logs.AWSLogsClient;
import com.amazonaws.services.logs.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BasicLogsHelper implements LogsHelper {
    private static final Logger log = LoggerFactory.getLogger(BasicLogsHelper.class);
    @Inject
    AWSLogsClient awsLogsClient;

    @Inject
    public BasicLogsHelper() {
    }

    @Override
    public List<OutputLogEvent> getLogs(String logGroupName, List<String> searchStrings) {
        DescribeLogStreamsRequest describeLogStreamsRequest = new DescribeLogStreamsRequest()
                .withLogGroupName(logGroupName);

        DescribeLogStreamsResult describeLogStreamsResult = awsLogsClient.describeLogStreams(describeLogStreamsRequest);

        List<String> logStreamNames = describeLogStreamsResult.getLogStreams().stream()
                .map(LogStream::getLogStreamName)
                .collect(Collectors.toList());

        final List<OutputLogEvent> tempOutputLogEventList = new ArrayList<>();

        logStreamNames.stream()
                .forEach(logStreamName -> {

                    GetLogEventsRequest getLogEventsRequest = new GetLogEventsRequest()
                            .withLogGroupName(logGroupName)
                            .withLogStreamName(logStreamName);

                    GetLogEventsResult getLogEventsResult = awsLogsClient.getLogEvents(getLogEventsRequest);

                    String currentToken = null;
                    String nextToken = null;

                    do {
                        currentToken = nextToken;
                        tempOutputLogEventList.addAll(getLogEventsResult.getEvents());
                        nextToken = getLogEventsResult.getNextForwardToken();
                    } while (currentToken != nextToken);
                });

        List<OutputLogEvent> outputLogEventList = tempOutputLogEventList.stream()
                .filter(outputLogEvent -> searchStrings.parallelStream().allMatch(outputLogEvent.getMessage()::contains))
                .sorted(Comparator.comparing(OutputLogEvent::getIngestionTime))
                .collect(Collectors.toList());

        return outputLogEventList;
    }
}
