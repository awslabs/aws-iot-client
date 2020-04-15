package com.awslabs.iot.client.commands.greengrass.groups;

import com.awslabs.general.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.commands.greengrass.GreengrassCommandHandler;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.helpers.interfaces.V2GreengrassHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.greengrass.model.DefinitionInformation;
import software.amazon.awssdk.services.greengrass.model.GetConnectorDefinitionVersionResponse;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class DeleteAllConnectorDefinitionsCommandHandler implements GreengrassCommandHandler {
    private static final String DELETE_CONNECTOR_DEFINITIONS = "delete-all-connector-definitions";
    private static final Logger log = LoggerFactory.getLogger(DeleteAllConnectorDefinitionsCommandHandler.class);
    @Inject
    V2GreengrassHelper v2GreengrassHelper;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;

    @Inject
    public DeleteAllConnectorDefinitionsCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        List<String> immutableConnectorDefinitionIds = v2GreengrassHelper.getImmutableConnectorDefinitionVersionResponses()
                .map(GetConnectorDefinitionVersionResponse::id)
                .collect(Collectors.toList());

        v2GreengrassHelper.getConnectorDefinitions()
                .filter(definitionInformation -> !immutableConnectorDefinitionIds.contains(definitionInformation.id()))
                .forEach(this::deleteAndLog);
    }

    private void deleteAndLog(DefinitionInformation definitionInformation) {
        v2GreengrassHelper.deleteConnectorDefinition(definitionInformation);

        log.info(String.join("", "Deleted connector definition [", definitionInformation.id(), "]"));
    }

    @Override
    public String getCommandString() {
        return DELETE_CONNECTOR_DEFINITIONS;
    }

    @Override
    public String getHelp() {
        return "Deletes all Greengrass connector definitions.";
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
