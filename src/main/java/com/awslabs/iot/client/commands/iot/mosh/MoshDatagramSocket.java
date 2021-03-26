package com.awslabs.iot.client.commands.iot.mosh;

import io.vavr.control.Option;
import io.vertx.core.datagram.DatagramSocket;

class MoshDatagramSocket {
    private final DatagramSocket datagramSocket;

    private Option<Integer> sourcePort;

    public MoshDatagramSocket(DatagramSocket datagramSocket) {
        this.datagramSocket = datagramSocket;
    }

    public DatagramSocket getDatagramSocket() {
        return this.datagramSocket;
    }

    public Option<Integer> getSourcePort() {
        return this.sourcePort;
    }

    public void setSourcePort(Option<Integer> sourcePort) {
        this.sourcePort = sourcePort;
    }
}
