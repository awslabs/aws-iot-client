package com.awslabs.iot.client.helpers.cloudwatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.cloudwatchlogs.CloudWatchLogsClient;
import software.amazon.awssdk.services.cloudwatchlogs.model.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BasicLogsHelper implements LogsHelper {
    private static final Logger log = LoggerFactory.getLogger(BasicLogsHelper.class);
    @Inject
    CloudWatchLogsClient cloudWatchLogsClient;

    @Inject
    public BasicLogsHelper() {
    }

    @Override
    public List<OutputLogEvent> getLogs(String logGroupName, List<String> searchStrings) {
        DescribeLogStreamsRequest describeLogStreamsRequest = DescribeLogStreamsRequest.builder()
                .logGroupName(logGroupName)
                .build();

        DescribeLogStreamsResponse describeLogStreamsResult = cloudWatchLogsClient.describeLogStreams(describeLogStreamsRequest);

        List<String> logStreamNames = describeLogStreamsResult.logStreams().stream()
                .map(LogStream::logStreamName)
                .collect(Collectors.toList());

        final List<OutputLogEvent> tempOutputLogEventList = new ArrayList<>();

        logStreamNames.stream()
                .forEach(logStreamName -> {

                    GetLogEventsRequest getLogEventsRequest = GetLogEventsRequest.builder()
                            .logGroupName(logGroupName)
                            .logStreamName(logStreamName)
                            .build();

                    GetLogEventsResponse getLogEventsResult = cloudWatchLogsClient.getLogEvents(getLogEventsRequest);

                    String currentToken = null;
                    String nextToken = null;

                    do {
                        currentToken = nextToken;
                        tempOutputLogEventList.addAll(getLogEventsResult.events());
                        nextToken = getLogEventsResult.nextForwardToken();
                    } while (currentToken != nextToken);
                });

        List<OutputLogEvent> outputLogEventList = tempOutputLogEventList.stream()
                .filter(outputLogEvent -> searchStrings.parallelStream().allMatch(outputLogEvent.message()::contains))
                .sorted(Comparator.comparing(OutputLogEvent::ingestionTime))
                .collect(Collectors.toList());

        return outputLogEventList;
    }
}
