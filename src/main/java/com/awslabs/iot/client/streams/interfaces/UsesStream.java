package com.awslabs.iot.client.streams.interfaces;

import java.util.stream.Stream;

public interface UsesStream<T> {
    Stream<T> getStream();
}
