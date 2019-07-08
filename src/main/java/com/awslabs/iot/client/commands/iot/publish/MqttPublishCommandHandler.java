package com.awslabs.iot.client.commands.iot.publish;

import com.awslabs.iot.client.helpers.io.interfaces.IOHelper;
import com.awslabs.iot.client.helpers.iot.interfaces.DeviceSdkMessagingHelper;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;

import javax.inject.Inject;

public class MqttPublishCommandHandler implements PublishCommandHandler {
    private static final String MQTTPUBLISH = "mqtt-publish";
    @Inject
    DeviceSdkMessagingHelper deviceSdkMessagingHelper;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IOHelper ioHelper;

    @Inject
    public MqttPublishCommandHandler() {
    }

    @Override
    public int requiredParameters() {
        return 2;
    }

    @Override
    public void publish(String topic, String message) {
        deviceSdkMessagingHelper.publish(topic, message);
    }

    @Override
    public String getCommandString() {
        return MQTTPUBLISH;
    }

    @Override
    public String getHelp() {
        return "Publishes a message using MQTT.  First parameter is the topic, second parameter is the message.";
    }

    public ParameterExtractor getParameterExtractor() {
        return this.parameterExtractor;
    }

    public IOHelper getIoHelper() {
        return this.ioHelper;
    }
}
