package com.awslabs.iot.client.streams.interfaces;

import io.vavr.collection.Stream;

public interface UsesStream<T> {
    Stream<T> getStream();
}
