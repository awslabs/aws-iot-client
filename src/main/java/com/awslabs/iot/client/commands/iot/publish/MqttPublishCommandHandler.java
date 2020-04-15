package com.awslabs.iot.client.commands.iot.publish;

import com.awslabs.general.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.helpers.iot.interfaces.WebsocketsHelper;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import io.vavr.control.Try;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class MqttPublishCommandHandler implements PublishCommandHandler {
    private final Logger log = LoggerFactory.getLogger(MqttPublishCommandHandler.class);
    private static final String MQTTPUBLISH = "mqtt-publish";
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;
    @Inject
    WebsocketsHelper websocketsHelper;

    @Inject
    public MqttPublishCommandHandler() {
    }

    @Override
    public int requiredParameters() {
        return 2;
    }

    @Override
    public void publish(String topic, String message) {
        MqttClient mqttClient = Try.of(() -> websocketsHelper.connectMqttClientAndPublish(topic, message)).get();
        Try.run(() -> mqttClient.disconnect())
                .onFailure(throwable -> log.info(String.join("", "Exception: [", throwable.getMessage(), "]")));
        Try.run(() -> websocketsHelper.close(mqttClient))
                .onFailure(throwable -> log.info(String.join("", "Exception: [", throwable.getMessage(), "]")));
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

    public IoHelper getIoHelper() {
        return this.ioHelper;
    }
}
