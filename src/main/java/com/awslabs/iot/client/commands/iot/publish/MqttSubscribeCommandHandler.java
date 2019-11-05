package com.awslabs.iot.client.commands.iot.publish;

import com.awslabs.iot.client.commands.iot.IotCommandHandler;
import com.awslabs.iot.client.helpers.io.interfaces.IOHelper;
import com.awslabs.iot.client.helpers.iot.interfaces.WebsocketsHelper;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import io.vavr.control.Try;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class MqttSubscribeCommandHandler implements IotCommandHandler {
    private static final String MQTTSUBSCRIBE = "mqtt-subscribe";
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(MqttSubscribeCommandHandler.class);
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IOHelper ioHelper;
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
            log.info("[{}] - {}", topic1, new String(mqttMessage.getPayload()));
            return null;
        };

        mqttClient.setCallback(websocketsHelper.buildMessageCallback(messageLoggingCallback));
    }

    public ParameterExtractor getParameterExtractor() {
        return this.parameterExtractor;
    }

    public IOHelper getIoHelper() {
        return this.ioHelper;
    }
}
