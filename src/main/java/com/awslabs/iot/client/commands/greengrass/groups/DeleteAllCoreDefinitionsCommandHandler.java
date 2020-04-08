package com.awslabs.iot.client.commands.greengrass.groups;

import com.awslabs.general.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.commands.greengrass.GreengrassCommandHandler;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.helpers.interfaces.V2GreengrassHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.greengrass.model.DefinitionInformation;
import software.amazon.awssdk.services.greengrass.model.GetCoreDefinitionVersionResponse;

import javax.inject.Inject;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DeleteAllCoreDefinitionsCommandHandler implements GreengrassCommandHandler {
    private static final String DELETE_CORE_DEFINITIONS = "delete-all-core-definitions";
    private static final Logger log = LoggerFactory.getLogger(DeleteAllCoreDefinitionsCommandHandler.class);
    @Inject
    V2GreengrassHelper v2GreengrassHelper;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;

    @Inject
    public DeleteAllCoreDefinitionsCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        List<String> immutableCoreDefinitionIds = v2GreengrassHelper.getImmutableCoreDefinitionVersionResponses()
                .map(GetCoreDefinitionVersionResponse::id)
                .collect(Collectors.toList());

        v2GreengrassHelper.getCoreDefinitions()
                .filter(definitionInformation -> !immutableCoreDefinitionIds.contains(definitionInformation.id()))
                // Sort the definitions by ID so we can get a general sense of how far along we are in the process of deleting them
                .sorted(Comparator.comparing(DefinitionInformation::id))
                .forEach(this::deleteAndLog);
    }

    private void deleteAndLog(DefinitionInformation definitionInformation) {
        v2GreengrassHelper.deleteCoreDefinition(definitionInformation);

        log.info(String.join("", "Deleted core definition [", definitionInformation.id(), "]"));
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
