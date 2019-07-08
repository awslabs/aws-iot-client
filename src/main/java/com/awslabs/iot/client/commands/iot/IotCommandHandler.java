package com.awslabs.iot.client.commands.iot;

import com.awslabs.iot.client.commands.interfaces.CommandHandler;

public interface IotCommandHandler extends CommandHandler {
    default String getServiceName() {
        return "iot";
    }
}
