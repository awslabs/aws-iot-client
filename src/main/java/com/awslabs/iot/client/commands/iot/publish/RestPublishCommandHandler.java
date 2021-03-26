package com.awslabs.iot.client.commands.iot.publish;


import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.data.ImmutableTopicName;
import com.awslabs.iot.data.Qos;
import com.awslabs.iot.data.TopicName;
import com.awslabs.iot.helpers.interfaces.IotHelper;

import javax.inject.Inject;

public class RestPublishCommandHandler implements PublishCommandHandler {
    private static final String RESTPUBLISH = "rest-publish";
    @Inject
    IotHelper iotHelper;
    @Inject
    ParameterExtractor parameterExtractor;

    @Inject
    public RestPublishCommandHandler() {
    }

    @Override
    public int requiredParameters() {
        return 2;
    }

    @Override
    public void publish(String topic, String message) {
        TopicName topicName = ImmutableTopicName.builder().name(topic).build();

        iotHelper.publish(topicName, Qos.ZERO, message);
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
}
