package com.awslabs.iot.client.helpers.iot.interfaces;

public interface MessagingHelper {
    String getEndpointAddress();

    int getEndpointPort();

    void doSetupIfNecessary();
}
