package com.awslabs.iot.client.helpers.cloudwatch;

import io.vavr.collection.List;
import io.vavr.collection.Stream;
import software.amazon.awssdk.services.cloudwatchlogs.CloudWatchLogsClient;
import software.amazon.awssdk.services.cloudwatchlogs.model.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

public class BasicLogsHelper implements LogsHelper {
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

        List<String> logStreamNames = Stream.ofAll(describeLogStreamsResult.logStreams())
                .map(LogStream::logStreamName)
                .toList();

        final ArrayList<OutputLogEvent> tempOutputLogEventList = new ArrayList<>();

        logStreamNames.toStream()
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
                    } while (!Objects.equals(currentToken, nextToken));
                });

        return Stream.ofAll(tempOutputLogEventList)
                .filter(outputLogEvent -> searchStrings.toJavaParallelStream().allMatch(outputLogEvent.message()::contains))
                .sorted(Comparator.comparing(OutputLogEvent::ingestionTime))
                .toList();
    }
}
