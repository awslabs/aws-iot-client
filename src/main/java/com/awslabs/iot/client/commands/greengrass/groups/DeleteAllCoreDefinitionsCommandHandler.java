package com.awslabs.iot.client.commands.greengrass.groups;

import com.amazonaws.services.greengrass.model.DefinitionInformation;
import com.awslabs.aws.iot.resultsiterator.helpers.interfaces.IoHelper;
import com.awslabs.aws.iot.resultsiterator.helpers.v1.interfaces.V1GreengrassHelper;
import com.awslabs.iot.client.commands.greengrass.GreengrassCommandHandler;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import org.slf4j.Logger;

import javax.inject.Inject;

public class DeleteAllCoreDefinitionsCommandHandler implements GreengrassCommandHandler {
    private static final String DELETE_CORE_DEFINITIONS = "delete-all-core-definitions";
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(DeleteAllCoreDefinitionsCommandHandler.class);
    @Inject
    V1GreengrassHelper greengrassHelper;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;

    @Inject
    public DeleteAllCoreDefinitionsCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        greengrassHelper.listNonImmutableCoreDefinitionInformation()
                .forEach(this::deleteAndLog);
    }

    private void deleteAndLog(DefinitionInformation definitionInformation) {
        greengrassHelper.deleteCoreDefinition(definitionInformation);

        log.info("Deleted core definition [" + definitionInformation + "]");
    }

    @Override
    public String getCommandString() {
        return DELETE_CORE_DEFINITIONS;
    }

    @Override
    public String getHelp() {
        return "Deletes all Greengrass core definitions.";
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
