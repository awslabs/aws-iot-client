package com.awslabs.iot.client.parameters.interfaces;

import io.vavr.collection.List;

public interface ParameterExtractor {
    List<String> getParameters(String input);
}
