package com.awslabs.iot.client.commands.iot.mosh;

import io.vertx.core.datagram.DatagramSocket;

import java.util.Optional;

class MoshDatagramSocket {
    private final DatagramSocket datagramSocket;

    private Optional<Integer> sourcePort;

    public MoshDatagramSocket(DatagramSocket datagramSocket) {
        this.datagramSocket = datagramSocket;
    }

    public DatagramSocket getDatagramSocket() {
        return this.datagramSocket;
    }

    public Optional<Integer> getSourcePort() {
        return this.sourcePort;
    }

    public void setSourcePort(Optional<Integer> sourcePort) {
        this.sourcePort = sourcePort;
    }
}
