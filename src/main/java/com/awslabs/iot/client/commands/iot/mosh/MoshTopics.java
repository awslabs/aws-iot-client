package com.awslabs.iot.client.commands.iot.mosh;

import javax.inject.Inject;

public class MoshTopics {
    public static final String CDD = "cdd";
    public static final String MOSH = "mosh";
    public static final String DATA = "data";
    public static final String CLIENT = "client";
    public static final String SERVER = "server";
    private static final String DELIMITER = "/";
    private static final String REQUEST = "request";
    private static final String NEW = "new";
    private static final String RESPONSE = "response";

    @Inject
    public MoshTopics() {
    }

    private String getBaselineTopic(String serverName) {
        return String.join(DELIMITER, serverName, CDD, MOSH);
    }

    private String getBaselineClientTopic(String serverName) {
        return String.join(DELIMITER, getBaselineTopic(serverName), DATA, CLIENT);
    }

    private String getBaselineServerTopic(String serverName) {
        return String.join(DELIMITER, getBaselineTopic(serverName), DATA, SERVER);
    }

    public String getDataServerTopic(String serverName, int port) {
        return String.join(DELIMITER, getBaselineServerTopic(serverName), String.valueOf(port));
    }

    public String getDataClientTopic(String serverName, int port) {
        return String.join(DELIMITER, getBaselineClientTopic(serverName), String.valueOf(port));
    }

    public String getNewResponseTopic(String serverName) {
        return String.join(DELIMITER, getBaselineTopic(serverName), RESPONSE);
    }

    public String getNewRequestTopic(String serverName) {
        return String.join(DELIMITER, getBaselineTopic(serverName), REQUEST, NEW);
    }
}
