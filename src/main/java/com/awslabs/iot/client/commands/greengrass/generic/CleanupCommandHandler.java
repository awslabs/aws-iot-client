package com.awslabs.iot.client.commands.greengrass.generic;

import com.awslabs.general.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.commands.greengrass.GreengrassCommandHandler;
import com.awslabs.iot.client.commands.greengrass.groups.*;
import com.awslabs.iot.client.commands.iot.certificates.DeleteAllCertificatesCommandHandler;
import com.awslabs.iot.client.commands.iot.things.DeleteAllThingGroupsCommandHandler;
import com.awslabs.iot.client.commands.iot.things.DeleteAllThingsCommandHandler;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class CleanupCommandHandler implements GreengrassCommandHandler {
    private final Logger log = LoggerFactory.getLogger(CleanupCommandHandler.class);
    private static final String CLEANUP = "cleanup";
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;
    @Inject
    DeleteAllCertificatesCommandHandler deleteAllCertificatesCommandHandler;
    @Inject
    DeleteAllThingsCommandHandler deleteAllThingsCommandHandler;
    @Inject
    DeleteAllLambdaFunctionsCommandHandler deleteAllLambdaFunctionsCommandHandler;
    @Inject
    DeleteAllGroupsCommandHandler deleteAllGroupsCommandHandler;
    @Inject
    DeleteAllCoreDefinitionsCommandHandler deleteAllCoreDefinitionsCommandHandler;
    @Inject
    DeleteAllFunctionDefinitionsCommandHandler deleteAllFunctionDefinitionsCommandHandler;
    @Inject
    DeleteAllSubscriptionDefinitionsCommandHandler deleteAllSubscriptionDefinitionsCommandHandler;
    @Inject
    DeleteAllDeviceDefinitionsCommandHandler deleteAllDeviceDefinitionsCommandHandler;
    @Inject
    DeleteAllLoggerDefinitionsCommandHandler deleteAllLoggerDefinitionsCommandHandler;
    @Inject
    DeleteAllResourceDefinitionsCommandHandler deleteAllResourceDefinitionsCommandHandler;
    @Inject
    DeleteAllConnectorDefinitionsCommandHandler deleteAllConnectorDefinitionsCommandHandler;
    @Inject
    DeleteAllThingGroupsCommandHandler deleteAllThingGroupsCommandHandler;

    @Inject
    public CleanupCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        // IoT
        deleteAllCertificatesCommandHandler.innerHandle("");
        deleteAllThingsCommandHandler.innerHandle("");
        deleteAllThingGroupsCommandHandler.innerHandle("");

        // Greengrass
        deleteAllLambdaFunctionsCommandHandler.innerHandle("");
        deleteAllGroupsCommandHandler.innerHandle("");
        deleteAllCoreDefinitionsCommandHandler.innerHandle("");
        deleteAllFunctionDefinitionsCommandHandler.innerHandle("");
        deleteAllSubscriptionDefinitionsCommandHandler.innerHandle("");
        deleteAllDeviceDefinitionsCommandHandler.innerHandle("");
        deleteAllLoggerDefinitionsCommandHandler.innerHandle("");
        deleteAllResourceDefinitionsCommandHandler.innerHandle("");
        deleteAllConnectorDefinitionsCommandHandler.innerHandle("");
    }

    @Override
    public String getCommandString() {
        return CLEANUP;
    }

    @Override
    public String getHelp() {
        return "Deletes all IoT things, certificates, Greengrass groups, and associated Lambda functions for groups and IoT things that are not immutable";
    }

    @Override
    public int requiredParameters() {
        return 0;
    }

    @Override
    public boolean isDangerous() {
        return true;
    }

    public ParameterExtractor getParameterExtractor() {
        return this.parameterExtractor;
    }

    public IoHelper getIoHelper() {
        return this.ioHelper;
    }
}
