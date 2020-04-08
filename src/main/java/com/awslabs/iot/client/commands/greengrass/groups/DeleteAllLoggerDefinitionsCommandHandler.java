package com.awslabs.iot.client.commands.greengrass.groups;

import com.amazonaws.services.greengrass.model.DefinitionInformation;
import com.awslabs.general.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.commands.greengrass.GreengrassCommandHandler;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.helpers.interfaces.V1GreengrassHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class DeleteAllLoggerDefinitionsCommandHandler implements GreengrassCommandHandler {
    private static final String DELETE_LOGGER_DEFINITIONS = "delete-all-logger-definitions";
    private static final Logger log = LoggerFactory.getLogger(DeleteAllLoggerDefinitionsCommandHandler.class);
    @Inject
    V1GreengrassHelper greengrassHelper;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;

    @Inject
    public DeleteAllLoggerDefinitionsCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        greengrassHelper.listNonImmutableLoggerDefinitionInformation()
                .forEach(this::deleteAndLog);
    }

    private void deleteAndLog(DefinitionInformation definitionInformation) {
        greengrassHelper.deleteLoggerDefinition(definitionInformation);

        log.info("Deleted logger definition [" + definitionInformation + "]");
    }

    @Override
    public String getCommandString() {
        return DELETE_LOGGER_DEFINITIONS;
    }

    @Override
    public String getHelp() {
        return "Deletes all Greengrass logger definitions.";
    }

    @Override
    public int requiredParameters() {
        return 0;
    }

    public ParameterExtractor getParameterExtractor() {
        return this.parameterExtractor;
    }

    public IoHelper getIoHelper() {
        return this.ioHelper;
    }
}
