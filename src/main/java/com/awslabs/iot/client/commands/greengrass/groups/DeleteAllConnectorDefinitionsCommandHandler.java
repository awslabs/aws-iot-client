package com.awslabs.iot.client.commands.greengrass.groups;

import com.amazonaws.services.greengrass.model.DefinitionInformation;
import com.awslabs.iot.client.commands.greengrass.GreengrassCommandHandler;
import com.awslabs.iot.client.helpers.greengrass.interfaces.GreengrassHelper;
import com.awslabs.iot.client.helpers.io.interfaces.IOHelper;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class DeleteAllConnectorDefinitionsCommandHandler implements GreengrassCommandHandler {
    private static final String DELETE_CONNECTOR_DEFINITIONS = "delete-all-connector-definitions";
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(DeleteAllConnectorDefinitionsCommandHandler.class);
    @Inject
    GreengrassHelper greengrassHelper;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IOHelper ioHelper;

    @Inject
    public DeleteAllConnectorDefinitionsCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        List<DefinitionInformation> nonImmutableConnectorDefinitionInformation = greengrassHelper.listNonImmutableConnectorDefinitionInformation();

        for (DefinitionInformation definitionInformation : nonImmutableConnectorDefinitionInformation) {
            greengrassHelper.deleteConnectorDefinition(definitionInformation);

            log.info("Deleted connector definition [" + definitionInformation + "]");
        }
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

    public IOHelper getIoHelper() {
        return this.ioHelper;
    }
}
