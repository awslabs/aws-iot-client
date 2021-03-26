package com.awslabs.iot.client.commands.greengrassv2.greengrass;

import com.awslabs.iot.client.commands.interfaces.CommandHandler;

public interface GreengrassV2CommandHandler extends CommandHandler {
    @Override
    default String getServiceName() {
        return "greengrassv2";
    }
}
