package com.awslabs.iot.client.commands.greengrass;

import com.awslabs.iot.client.commands.interfaces.CommandHandler;

public interface GreengrassCommandHandler extends CommandHandler {
    @Override
    default String getServiceName() {
        return "greengrass";
    }
}
