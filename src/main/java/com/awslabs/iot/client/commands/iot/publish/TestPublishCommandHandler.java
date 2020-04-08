package com.awslabs.iot.client.commands.iot.publish;

import javax.inject.Inject;

public class TestPublishCommandHandler extends RestPublishCommandHandler {
    private static final String TEST_PUBLISH = "test-publish";
    private static final String TEST_TOPIC = "test";
    private static final String TEST_MESSAGE = "This is a test";

    @Inject
    public TestPublishCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        publish(TEST_TOPIC, String.join("", "[", String.valueOf(System.currentTimeMillis()), "] ", TEST_MESSAGE));
    }

    @Override
    public String getCommandString() {
        return TEST_PUBLISH;
    }

    @Override
    public String getHelp() {
        return String.join("", "Publishes the message '", TEST_MESSAGE, "', with a leading epoch milliseconds timestamp, to the topic '", TEST_TOPIC, "' using the REST APIs.");
    }

    @Override
    public int requiredParameters() {
        return 0;
    }
}
