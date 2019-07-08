package com.awslabs.iot.client.helpers.iot.interfaces;

import io.vertx.mqtt.MqttClient;

public interface VertxMessagingHelper {
    MqttClient getClient();
}
