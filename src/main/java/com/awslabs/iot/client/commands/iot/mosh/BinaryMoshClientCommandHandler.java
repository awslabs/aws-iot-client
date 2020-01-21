package com.awslabs.iot.client.commands.iot.mosh;

import com.awslabs.aws.iot.resultsiterator.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.commands.iot.completers.ThingCompleter;
import com.awslabs.iot.client.helpers.iot.interfaces.WebsocketsHelper;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import io.vertx.core.Vertx;
import org.slf4j.Logger;

import javax.inject.Inject;

public class BinaryMoshClientCommandHandler implements MoshClientCommandHandler {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(BinaryMoshClientCommandHandler.class);
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;
    @Inject
    ThingCompleter thingCompleter;
    @Inject
    Vertx vertx;
    @Inject
    MoshTopics moshTopics;
    @Inject
    WebsocketsHelper websocketsHelper;

    @Inject
    public BinaryMoshClientCommandHandler() {
    }

    @Override
    public WebsocketsHelper getWebsocketsHelper() {
        return websocketsHelper;
    }

    @Override
    public Logger getLogger() {
        return log;
    }

    public ParameterExtractor getParameterExtractor() {
        return this.parameterExtractor;
    }

    public IoHelper getIoHelper() {
        return this.ioHelper;
    }

    public ThingCompleter getThingCompleter() {
        return this.thingCompleter;
    }

    public Vertx getVertx() {
        return this.vertx;
    }

    public MoshTopics getMoshTopics() {
        return this.moshTopics;
    }
}
