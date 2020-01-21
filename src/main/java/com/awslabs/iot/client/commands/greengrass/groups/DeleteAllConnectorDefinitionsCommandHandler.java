package com.awslabs.iot.client.commands.greengrass.groups;

import com.amazonaws.services.greengrass.model.DefinitionInformation;
import com.awslabs.aws.iot.resultsiterator.helpers.interfaces.IoHelper;
import com.awslabs.aws.iot.resultsiterator.helpers.v1.interfaces.V1GreengrassHelper;
import com.awslabs.iot.client.commands.greengrass.GreengrassCommandHandler;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import org.slf4j.Logger;

import javax.inject.Inject;

public class DeleteAllConnectorDefinitionsCommandHandler implements GreengrassCommandHandler {
    private static final String DELETE_CONNECTOR_DEFINITIONS = "delete-all-connector-definitions";
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(DeleteAllConnectorDefinitionsCommandHandler.class);
    @Inject
    V1GreengrassHelper greengrassHelper;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;

    @Inject
    public DeleteAllConnectorDefinitionsCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        greengrassHelper.listNonImmutableConnectorDefinitionInformation()
                .forEach(this::deleteAndLog);
    }

    private void deleteAndLog(DefinitionInformation definitionInformation) {
        greengrassHelper.deleteConnectorDefinition(definitionInformation);

        log.info("Deleted connector definition [" + definitionInformation + "]");
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
