package com.awslabs.iot.client.commands.iot.publish;

import com.awslabs.iot.client.commands.iot.IotCommandHandler;
import com.google.gson.Gson;
import io.vavr.collection.List;

import java.util.Map;

public interface PublishCommandHandler extends IotCommandHandler {
    default void innerHandle(String input) {
        List<String> parameters = getParameterExtractor().getParameters(input);
        String topic = parameters.get(0);
        String json = parameters.get(1);

        publish(topic, json);
    }

    void publish(String topic, String message);

    default void publish(String topic, Map message) {
        String json = new Gson().toJson(message);
        publish(topic, json);
    }
}
