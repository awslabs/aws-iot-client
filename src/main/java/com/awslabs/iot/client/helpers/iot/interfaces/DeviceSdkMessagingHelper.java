package com.awslabs.iot.client.helpers.iot.interfaces;

import com.amazonaws.services.iot.client.AWSIotTopic;

public interface DeviceSdkMessagingHelper {
    void publish(String topic, String message);

    void subscribe(AWSIotTopic topic);
}
