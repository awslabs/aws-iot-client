package com.awslabs.iot.client.commands.iot.publish;

import com.amazonaws.services.iotdata.AWSIotDataClient;
import com.amazonaws.services.iotdata.model.PublishRequest;
import com.awslabs.aws.iot.resultsiterator.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;

import javax.inject.Inject;
import java.nio.ByteBuffer;

public class RestPublishCommandHandler implements PublishCommandHandler {
    private static final String RESTPUBLISH = "rest-publish";
    @Inject
    AWSIotDataClient awsIotDataClient;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;

    @Inject
    public RestPublishCommandHandler() {
    }

    @Override
    public int requiredParameters() {
        return 2;
    }

    @Override
    public void publish(String topic, String message) {
        PublishRequest publishRequest = new PublishRequest()
                .withTopic(topic)
                .withPayload(ByteBuffer.wrap(message.getBytes()));
        awsIotDataClient.publish(publishRequest);
    }

    @Override
    public String getCommandString() {
        return RESTPUBLISH;
    }

    @Override
    public String getHelp() {
        return "Publishes a message using the REST APIs.  First parameter is the topic, second parameter is the message.";
    }

    public ParameterExtractor getParameterExtractor() {
        return this.parameterExtractor;
    }

    public IoHelper getIoHelper() {
        return this.ioHelper;
    }
}
