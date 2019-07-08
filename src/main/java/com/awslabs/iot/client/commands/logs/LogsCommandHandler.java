package com.awslabs.iot.client.commands.logs;

import com.awslabs.iot.client.commands.interfaces.CommandHandler;

public interface LogsCommandHandler extends CommandHandler {
    default String getServiceName() {
        return "logs";
    }
}
