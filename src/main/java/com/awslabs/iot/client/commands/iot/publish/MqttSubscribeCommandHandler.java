package com.awslabs.iot.client.commands.iot.publish;

import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotTopic;
import com.awslabs.iot.client.commands.iot.IotCommandHandler;
import com.awslabs.iot.client.helpers.io.interfaces.IOHelper;
import com.awslabs.iot.client.helpers.iot.interfaces.DeviceSdkMessagingHelper;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

public class MqttSubscribeCommandHandler implements IotCommandHandler {
    private static final String MQTTSUBSCRIBE = "mqtt-subscribe";
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(MqttSubscribeCommandHandler.class);
    @Inject
    DeviceSdkMessagingHelper deviceSdkMessagingHelper;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IOHelper ioHelper;

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

        deviceSdkMessagingHelper.subscribe(new AWSIotTopic(topic) {
            @Override
            public void onMessage(AWSIotMessage message) {
                log.info("[{}] - {}", message.getTopic(), message.getStringPayload());
            }
        });
    }

    public ParameterExtractor getParameterExtractor() {
        return this.parameterExtractor;
    }

    public IOHelper getIoHelper() {
        return this.ioHelper;
    }
}
