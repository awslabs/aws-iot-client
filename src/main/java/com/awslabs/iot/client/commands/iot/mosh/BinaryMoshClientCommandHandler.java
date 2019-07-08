package com.awslabs.iot.client.commands.iot.mosh;

import com.awslabs.iot.client.commands.iot.completers.ThingCompleter;
import com.awslabs.iot.client.helpers.io.interfaces.IOHelper;
import com.awslabs.iot.client.helpers.iot.interfaces.VertxMessagingHelper;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import org.slf4j.Logger;

import javax.inject.Inject;

public class BinaryMoshClientCommandHandler implements MoshClientCommandHandler {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(BinaryMoshClientCommandHandler.class);
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IOHelper ioHelper;
    @Inject
    ThingCompleter thingCompleter;
    @Inject
    VertxMessagingHelper vertxMessagingHelper;
    @Inject
    Vertx vertx;
    @Inject
    MoshTopics moshTopics;

    @Inject
    public BinaryMoshClientCommandHandler() {
    }

    @Override
    public Buffer translateInboundPayload(Buffer inboundBuffer) {
        return inboundBuffer;
    }

    @Override
    public Logger getLogger() {
        return log;
    }

    @Override
    public Buffer translateOutboundPayload(Buffer data) {
        return data;
    }

    public ParameterExtractor getParameterExtractor() {
        return this.parameterExtractor;
    }

    public IOHelper getIoHelper() {
        return this.ioHelper;
    }

    public ThingCompleter getThingCompleter() {
        return this.thingCompleter;
    }

    public VertxMessagingHelper getVertxMessagingHelper() {
        return this.vertxMessagingHelper;
    }

    public Vertx getVertx() {
        return this.vertx;
    }

    public MoshTopics getMoshTopics() {
        return this.moshTopics;
    }
}
