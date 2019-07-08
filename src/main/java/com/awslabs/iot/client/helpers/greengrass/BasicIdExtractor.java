package com.awslabs.iot.client.helpers.greengrass;

import com.awslabs.iot.client.helpers.greengrass.interfaces.IdExtractor;

import javax.inject.Inject;

public class BasicIdExtractor implements IdExtractor {
    @Inject
    public BasicIdExtractor() {
    }

    @Override
    public String extractId(String arn) {
        return arn.replaceFirst("/versions.*$", "").replaceFirst("^.*/", "");
    }

    @Override
    public String extractVersionId(String arn) {
        return arn.substring(arn.lastIndexOf('/') + 1);
    }
}
