package com.awslabs.iot.client.helpers.greengrass.interfaces;

public interface IdExtractor {
    String extractId(String arn);

    String extractVersionId(String arn);
}
