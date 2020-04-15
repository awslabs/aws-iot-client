package com.awslabs.iot.client.commands.greengrass.groups;

import com.awslabs.general.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.commands.greengrass.GreengrassCommandHandler;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.helpers.interfaces.V2GreengrassHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.greengrass.model.DefinitionInformation;
import software.amazon.awssdk.services.greengrass.model.GetLoggerDefinitionVersionResponse;

import javax.inject.Inject;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DeleteAllLoggerDefinitionsCommandHandler implements GreengrassCommandHandler {
    private static final String DELETE_LOGGER_DEFINITIONS = "delete-all-logger-definitions";
    private static final Logger log = LoggerFactory.getLogger(DeleteAllLoggerDefinitionsCommandHandler.class);
    @Inject
    V2GreengrassHelper v2GreengrassHelper;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;

    @Inject
    public DeleteAllLoggerDefinitionsCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        List<String> immutableLoggerDefinitionIds = v2GreengrassHelper.getImmutableLoggerDefinitionVersionResponses()
                .map(GetLoggerDefinitionVersionResponse::id)
                .collect(Collectors.toList());

        v2GreengrassHelper.getLoggerDefinitions()
                .filter(definitionInformation -> !immutableLoggerDefinitionIds.contains(definitionInformation.id()))
                // Sort the definitions by ID so we can get a general sense of how far along we are in the process of deleting them
                .sorted(Comparator.comparing(DefinitionInformation::id))
                .forEach(this::deleteAndLog);
    }

    private void deleteAndLog(DefinitionInformation definitionInformation) {
        v2GreengrassHelper.deleteLoggerDefinition(definitionInformation);

        log.info(String.join("", "Deleted logger definition [", definitionInformation.id(), "]"));
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
