package com.awslabs.iot.client.commands.iot.publish;

import com.awslabs.general.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.commands.iot.IotCommandHandler;
import com.awslabs.iot.client.helpers.iot.interfaces.WebsocketsHelper;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.jcabi.log.Logger;
import io.vavr.control.Try;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import javax.inject.Inject;
import java.util.List;

public class MqttSubscribeCommandHandler implements IotCommandHandler {
    private static final String MQTTSUBSCRIBE = "mqtt-subscribe";
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;
    @Inject
    WebsocketsHelper websocketsHelper;

    @Inject
    public MqttSubscribeCommandHandler() {
    }

    @Override
    public int requiredParameters() {
        return 1;
    }

    @Override
    public String getCommandString() {
        return MQTTSUBSCRIBE;
    }

    @Override
    public String getHelp() {
        return "Subscribes to a topic using MQTT.";
    }

    @Override
    public void innerHandle(String input) {
        List<String> parameters = getParameterExtractor().getParameters(input);
        String topic = parameters.get(0);

        MqttClient mqttClient = Try.of(() -> websocketsHelper.connectMqttClientAndSubscribe(topic)).get();

        WebsocketsHelper.Function<String, MqttMessage> messageLoggingCallback = (topic1, mqttMessage) -> {
            Logger.info(this, String.format("[%s] - length: [%d] [%s]", topic1, mqttMessage.getPayload().length, new String(mqttMessage.getPayload())));
            return null;
        };

        mqttClient.setCallback(websocketsHelper.buildMessageCallback(messageLoggingCallback));
    }

    public ParameterExtractor getParameterExtractor() {
        return this.parameterExtractor;
    }

    public IoHelper getIoHelper() {
        return this.ioHelper;
    }
}
