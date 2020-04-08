package com.awslabs.iot.client.commands.iot.mosh;

import javax.inject.Inject;

public class MoshServerCommandHandler { // implements ThingCommandHandlerWithCompletion {
    @Inject
    public MoshServerCommandHandler() {
    }

    /*
    public static final int CLIENT_THING_NAME_POSITION = 1;
    private static final String MOSH_CONNECT_REGEX = "^MOSH CONNECT ([0-9]{5}) ([^\\s]{22})$";
    private static final Pattern MOSH_CONNECT_REGEX_PATTERN = Pattern.compile(MOSH_CONNECT_REGEX);
    private static final String MOSH = "mosh-server";
    private static final int SERVER_THING_NAME_POSITION = 0;
    private static final String LISTEN_IP = "127.0.0.1";
    private static final int LISTEN_PORT_OFFSET = 4096;
    private static final BiMap<Integer, DatagramSocket> datagramServerPorts = HashBiMap.create();
    private static final Logger log = LoggerFactory.getLogger(MoshServerCommandHandler.class);
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
    public MoshServerCommandHandler() {
    }

    @Override
    public String getServiceName() {
        return "iot";
    }

    @Override
    public void innerHandle(String input) {
        List<String> parameters = parameterExtractor.getParameters(input);

        String serverThingName = parameters.get(SERVER_THING_NAME_POSITION);

        MqttClient client = Try.of(() -> websocketsHelper.connectMqttClient()).get();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Determine the topics that we expect to see new channel requests on
        String newRequestTopic = moshTopics.getNewRequestTopic(serverThingName);

        // Subscribe to the topic that the client will send its new channel requests on and set up a callback for when we receive a new message
        try {
            client.subscribe(newRequestTopic, 0, new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    if (topic.equals(newRequestTopic)) {
                        createNewServer(serverThingName, client);
                        client.unsubscribe(newRequestTopic);
                        return;
                    }

                    handleInboundData(serverThingName, message, topic);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void handleInboundData(String serverThingName, MqttMessage publishMessage, String topic) {
        // Looking for a topic like "SERVER/mosh/from/CLIENT/PORT"
        String[] splitTopic = topic.split("/");

        if (splitTopic.length != 6) {
            // Not the correct number of elements
            log.info("Unknown topic [" + topic + "]");
            return;
        }

        if (!splitTopic[0].equals(serverThingName)) {
            // Not the server name we expected
            log.info("Unexpected server [" + serverThingName + "]");
            return;
        }

        if (!splitTopic[1].equals(MoshTopics.CDD)) {
            // Not the correct application
            log.info("Unexpected topic #1 [" + topic + "]");
            return;
        }

        if (!splitTopic[2].equals(MoshTopics.MOSH)) {
            // Not the correct application
            log.info("Unexpected topic #2 [" + topic + "]");
            return;
        }

        if (!splitTopic[3].equals(MoshTopics.DATA)) {
            // Not the correct traffic direction
            log.info("Unexpected topic #3 [" + topic + "]");
            return;
        }

        if (!splitTopic[4].equals(MoshTopics.CLIENT)) {
            // Not from the client, ignore
            log.info("Data not from the client");
            return;
        }

        int serverPort = Integer.parseInt(splitTopic[5]);

        String dataFromClientTopic = moshTopics.getDataClientTopic(serverThingName, serverPort);

        if (!topic.equals(dataFromClientTopic)) {
            // Sanity check: Can be removed
            log.info("This should never happen [" + dataFromClientTopic + ", " + topic + "]");
            return;
        }

        if (!datagramServerPorts.containsKey(serverPort)) {
            // No datagram server associated with this port
            log.info("Unexpected server port [" + serverPort + "]");
            return;
        }

        // Get the socket
        DatagramSocket socket = datagramServerPorts.get(serverPort);

        // Send the message on localhost to mosh-server
        socket.send(Buffer.buffer(publishMessage.getPayload()), serverPort, LISTEN_IP, datagramSocketAsyncResult -> {
        });
    }
     */

/**
 * Runs a program and waits until it exits
 *
 * @param program
 * @param arguments
 * @param environmentVariables
 * @param stdoutConsumer
 * @param stderrConsumer
 * <p>
 * Gets the output from a process and feeds it to two optional consumers (stdout and stderr)
 * @param pb
 * @param waitForExit
 * @param stdoutConsumer
 * @param stderrConsumer
 */
    /*
    private void runProgramAndBlock(String program, Optional<List<String>> arguments, Optional<Map<String, String>> environmentVariables, Optional<Consumer<String>> stdoutConsumer, Optional<Consumer<String>> stderrConsumer) {
        List<String> programAndArguments = new ArrayList();
        programAndArguments.add(program);
        arguments.ifPresent((args) -> {
            programAndArguments.addAll(args);
        });
        ProcessBuilder pb = new ProcessBuilder(programAndArguments);
        environmentVariables.ifPresent((env) -> {
            pb.environment().putAll(env);
        });
        getOutputFromProcess(pb, true, stdoutConsumer, stderrConsumer);
    }
     */

/**
 * Gets the output from a process and feeds it to two optional consumers (stdout and stderr)
 *
 * @param pb
 * @param waitForExit
 * @param stdoutConsumer
 * @param stderrConsumer
 */
    /*
    private void getOutputFromProcess(ProcessBuilder pb, boolean waitForExit, Optional<Consumer<String>> stdoutConsumer, Optional<Consumer<String>> stderrConsumer) {
        try {
            Process p = pb.start();
            BufferedReader stdout = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stderr = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            Thread stdoutThread = new Thread(() -> {
                stdout.lines().forEach(stdoutConsumer.orElse((line) -> {
                }));
            });
            stdoutThread.start();
            Thread stderrThread = new Thread(() -> {
                stderr.lines().forEach(stderrConsumer.orElse((line) -> {
                }));
            });
            stderrThread.start();
            if (waitForExit) {
                p.waitFor();
                stdoutThread.join();
                stderrThread.join();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void createNewServer(String serverThingName, MqttClient client) {
        try {
             // Run mosh-server and attempt to extract the port and key information from stdout

            List<String> stdoutLines = new ArrayList<>();
            List<String> stderrLines = new ArrayList<>();

            Consumer<String> stdoutConsumer = (line) -> stdoutLines.add(line);
            Consumer<String> stderrConsumer = (line) -> stderrLines.add(line);

            Map<String, String> environment = new HashMap<>();
            environment.put("LC_CTYPE", "en_US.UTF-8");
            runProgramAndBlock("mosh-server", Optional.empty(), Optional.ofNullable(environment), Optional.ofNullable(stdoutConsumer), Optional.ofNullable(stderrConsumer));

            Optional<String> result = stdoutLines.stream()
                    .filter(line -> line.matches(MOSH_CONNECT_REGEX))
                    .findFirst();

            int port;
            String key;

            if (result.isPresent()) {
                // Found some output, we assume it matches
                Matcher matcher = MOSH_CONNECT_REGEX_PATTERN.matcher(result.get());
                matcher.find();

                // Get the key and the port that mosh-server is listening on
                port = Integer.parseInt(matcher.group(1));
                key = matcher.group(2);

                // Publish a message to the client indicating that the server has started
                String newResponseTopic = moshTopics.getNewResponseTopic(serverThingName);
                client.publish(newResponseTopic, new Gson().toJson(KeyAndPort.builder().key(key).port(port).build()).getBytes(), 0, false);

                int listenPort = port + LISTEN_PORT_OFFSET;
                String dataClientTopic = moshTopics.getDataClientTopic(serverThingName, port);
                String dataServerTopic = moshTopics.getDataServerTopic(serverThingName, port);
                log.info("Starting server on [" + listenPort + "]");

                // Start a server on another local port that doesn't conflict with mosh-server so we can masquerade as a client
                DatagramSocket datagramSocket = vertx.createDatagramSocket();

                // Take any inbound data and send it via MQTT to the real client
                datagramSocket.handler(event -> {
                    try {
                        client.publish(dataServerTopic, event.data().getBytes(), 0, false);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

                // Listen on our special listen port
                datagramSocket.listen(listenPort, LISTEN_IP, s -> {
                });

                // Store the port and datagram socket object in a map
                datagramServerPorts.put(port, datagramSocket);

                // Subscribe to the topic that the client uses to send data to the server
                client.subscribe(dataClientTopic, 0);

                log.info("Server started on [" + listenPort + "]");
            } else {
                log.error("START mosh failed to start");
                log.error("stdout:");
                log.error(String.join("\n", stdoutLines));
                log.error("stderr:");
                log.error(String.join("\n", stderrLines));
                log.error("END   mosh failed to start");
            }
        } catch (Exception e) {
            log.info("Exception [" + e.getMessage() + "]");
        }
    }

    @Override
    public String getCommandString() {
        return MOSH;
    }

    @Override
    public String getHelp() {
        return "Listens for proxied mosh connections from a client.";
    }

    @Override
    public int requiredParameters() {
        return 1;
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
    */
}
