package com.awslabs.iot.client.commands.greengrassv2.greengrass.groups;


import com.awslabs.iot.client.commands.greengrassv2.greengrass.GreengrassV2CommandHandler;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.helpers.interfaces.GreengrassV2Helper;
import com.jcabi.log.Logger;

import javax.inject.Inject;

public class ListCoresV2CommandHandler implements GreengrassV2CommandHandler {
    private static final String LIST_CORES = "list-cores";
    @Inject
    GreengrassV2Helper greengrassV2Helper;
    @Inject
    ParameterExtractor parameterExtractor;

    @Inject
    public ListCoresV2CommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        greengrassV2Helper.getAllCoreDevices()
                .forEach(coreDevice -> Logger.info(this, String.join("", "  [", coreDevice.coreDeviceThingName(), " - ", coreDevice.statusAsString(), "]")));
    }

    @Override
    public String getCommandString() {
        return LIST_CORES;
    }

    @Override
    public String getHelp() {
        return "Lists all Greengrass groups.";
    }

    @Override
    public int requiredParameters() {
        return 0;
    }

    public ParameterExtractor getParameterExtractor() {
        return this.parameterExtractor;
    }
}
