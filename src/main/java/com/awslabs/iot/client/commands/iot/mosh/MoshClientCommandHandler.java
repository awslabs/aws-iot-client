package com.awslabs.iot.client.commands.iot.mosh;

import com.awslabs.iot.client.commands.iot.ThingCommandHandlerWithCompletion;
import com.awslabs.iot.client.helpers.iot.interfaces.WebsocketsHelper;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.Gson;
import io.vavr.control.Try;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.datagram.DatagramSocket;
import org.apache.commons.lang3.SystemUtils;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public interface MoshClientCommandHandler extends ThingCommandHandlerWithCompletion {
    String MOSH = "mosh-client";
    int SERVER_THING_NAME_POSITION = 0;
    String LISTEN_IP = "127.0.0.1";
    BiMap<Integer, DatagramSocket> datagramServerPorts = HashBiMap.create();
    BiMap<DatagramSocket, Integer> datagramSourcePorts = HashBiMap.create();

    default String getServiceName() {
        return "iot";
    }

    default void innerHandle(String input) {
        List<String> parameters = getParameterExtractor().getParameters(input);

        String serverThingName = parameters.get(SERVER_THING_NAME_POSITION);

        MqttClient client = Try.of(() -> getWebsocketsHelper().connectMqttClient()).get();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Determine the topics to send our new channel request and the response
        String newRequestTopic = getMoshTopics().getNewRequestTopic(serverThingName);
        String newResponseTopic = getMoshTopics().getNewResponseTopic(serverThingName);

        try {
            // Subscribe to the topic that the server will send its response to our new channel request on // Set up a callback for when we receive a new message
            client.subscribe(newResponseTopic, 0, new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    if (topic.equals(newResponseTopic)) {
                        createNewClient(serverThingName, client, message);
                        client.unsubscribe(newResponseTopic);
                        return;
                    }

                    handleInboundData(serverThingName, message, topic);
                }
            });

            // Send a message to the server to request a new channel
            client.publish(newRequestTopic, "{}".getBytes(), 0, false);

        } catch (Exception e) {
            getLogger().error("Failed to create the proxied mosh connection, please try again [" + e.getMessage() + "]");
        }
    }

    WebsocketsHelper getWebsocketsHelper();

    default void handleInboundData(String serverThingName, MqttMessage publishMessage, String topic) {
        if (!isValidTopic(serverThingName, topic)) {
            return;
        }

        int serverPort = getPortFromTopic(topic);

        if (!datagramServerPorts.containsKey(serverPort)) {
            // No datagram server associated with this port
            getLogger().info("Unexpected server port [" + serverPort + "]");
            return;
        }

        String dataFromGreengrassTopic = getMoshTopics().getDataServerTopic(serverThingName, serverPort);

        if (!topic.equals(dataFromGreengrassTopic)) {
            // Sanity check: Can be removed
            getLogger().info("This should never happen [" + dataFromGreengrassTopic + ", " + topic + "]");
            return;
        }

        // Get the socket
        DatagramSocket datagramSocket = datagramServerPorts.get(serverPort);

        if (!datagramSourcePorts.containsKey(datagramSocket)) {
            // No packets have arrived from a client so we don't know where to route them yet
            getLogger().info("No source port for socket yet [" + serverPort + "]");
            return;
        }

        int sourcePort = datagramSourcePorts.get(datagramSocket);

        // Send the message to localhost to the client
        datagramSocket.send(Buffer.buffer(publishMessage.getPayload()), sourcePort, LISTEN_IP, datagramSocketAsyncResult -> {
        });
    }

    MoshTopics getMoshTopics();

    Logger getLogger();

    default int getPortFromTopic(String topic) {
        String[] splitTopic = topic.split("/");

        return Integer.valueOf(splitTopic[5]);
    }

    default boolean isValidTopic(String serverThingName, String topic) {
        String[] splitTopic = topic.split("/");

        if (splitTopic.length != 6) {
            // Not the correct number of elements
            getLogger().info("Unknown topic [" + topic + "]");
            return false;
        }

        int counter = 0;
        String currentLevel = splitTopic[counter];

        if (!currentLevel.equals(serverThingName)) {
            // Not the server name we expected
            getLogger().info("Unexpected server [" + serverThingName + "]");
            return false;
        }

        counter++;
        currentLevel = splitTopic[counter];

        if (!currentLevel.equals(MoshTopics.CDD)) {
            // Not the correct application
            getLogger().info("Unexpected topic #1 [" + topic + "]");
            return false;
        }

        counter++;
        currentLevel = splitTopic[counter];

        if (!currentLevel.equals(MoshTopics.MOSH)) {
            // Not the correct application
            getLogger().info("Unexpected topic #1 [" + topic + "]");
            return false;
        }

        counter++;
        currentLevel = splitTopic[counter];

        if (!currentLevel.equals(MoshTopics.DATA)) {
            // Data
            getLogger().info("Unexpected topic #2 [" + topic + "]");
            return false;
        }

        counter++;
        currentLevel = splitTopic[counter];

        if (!currentLevel.equals(MoshTopics.SERVER)) {
            // Not the correct traffic direction
            getLogger().info("Unexpected topic #3 [" + topic + "]");
            return false;
        }

        return true;
    }

    default void createNewClient(String serverThingName, MqttClient client, MqttMessage publishMessage) {
        // Get the payload as a string
        String payloadString = new String(publishMessage.getPayload());

        // Extract the key and port information
        KeyAndPort keyAndPort = new Gson().fromJson(payloadString, KeyAndPort.class);

        String key = keyAndPort.getKey();
        int port = keyAndPort.getPort();

        // Determine the topics to send and receive data
        String dataToGreengrassTopic = getMoshTopics().getDataClientTopic(serverThingName, port);
        String dataFromGreengrassTopic = getMoshTopics().getDataServerTopic(serverThingName, port);

        // Create a listener to listen on the given port
        DatagramSocket datagramSocket = getVertx().createDatagramSocket();

        // Take any inbound data on our local port and send it via MQTT to the server
        datagramSocket.handler(datagramPacket -> {
            if (!datagramSourcePorts.containsKey(datagramSocket)) {
                // Save this source port number from the real mosh client if we don't have it already
                int sourcePort = datagramPacket.sender().port();
                datagramSourcePorts.put(datagramSocket, sourcePort);
            }

            try {
                // Publish the binary data to the server
                client.publish(dataToGreengrassTopic, datagramPacket.data().getBytes(), 0, false);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        // Store the server port for this datagram socket in the map
        if (datagramServerPorts.containsKey(port)) {
            DatagramSocket datagramSocketToCleanUp = datagramServerPorts.get(port);
            datagramSocketToCleanUp.close();
            datagramServerPorts.remove(port);
            datagramSourcePorts.remove(datagramSocketToCleanUp);
        }

        datagramServerPorts.put(port, datagramSocket);

        // Subscribe to the topic that the servers uses to send us data
        try {
            client.subscribe(dataFromGreengrassTopic, 0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Listen on the port they've given us
        datagramSocket.listen(port, LISTEN_IP, getDatagramSocketServerListeningHandler(key, port));

        getLogger().info("Starting server on [" + port + "]");
    }

    default Handler<AsyncResult<DatagramSocket>> getDatagramSocketServerListeningHandler(String key, int port) {
        Optional<Path> optionalPath = Stream.of(System.getenv("PATH").split(Pattern.quote(File.pathSeparator)))
                .map(Paths::get)
                .map(path -> path.resolve("mosh-client"))
                .filter(path -> Files.exists(path))
                .findFirst();

        if (optionalPath.isPresent() && SystemUtils.IS_OS_MAC) {
            getLogger().info("Running Mac OS and mosh-client exists in path, launching in a separate window");

            Path path = optionalPath.get();
            String absolutePath = path.toAbsolutePath().toString();

            File tempFile = null;

            try {
                tempFile = File.createTempFile("mosh-", ".sh");
                tempFile.setExecutable(true);
                tempFile.deleteOnExit();
                FileWriter fileWriter = new FileWriter(tempFile);
                fileWriter.write("#!/usr/bin/env bash\n");
                fileWriter.write("export MOSH_KEY=" + key + "\n");
                fileWriter.write(absolutePath + " 127.0.0.1 " + port);
                fileWriter.close();
            } catch (IOException e) {
                throw new UnsupportedOperationException(e);
            }

            String terminalProgram = "Terminal";

            if (Files.exists(Paths.get("/Applications/iTerm.app"))) {
                terminalProgram = "iTerm";
            }

            String[] command = new String[]{"open", "-a", terminalProgram, tempFile.getAbsolutePath()};

            return event -> {
                try {
                    ProcessBuilder pb = new ProcessBuilder(command)
                            .inheritIO();

                    pb.environment().put("MOSH_KEY", key);

                    try {
                        pb.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
        }

        return getManualMoshHandler(key, port);
    }

    default Handler<AsyncResult<DatagramSocket>> getManualMoshHandler(String key, int port) {
        return event -> {
            getLogger().warn("You must launch mosh-client manually");
            getLogger().info("MOSH_KEY=" + key + " mosh-client 127.0.0.1 " + port);
        };
    }

    Vertx getVertx();

    default String getCommandString() {
        return MOSH;
    }

    default String getHelp() {
        return "Creates a proxied mosh connection to a server.";
    }

    default int requiredParameters() {
        return 1;
    }
}
