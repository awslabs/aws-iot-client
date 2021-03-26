package com.awslabs.iot.client.commands.greengrass.groups;


import com.awslabs.iot.client.commands.greengrass.GreengrassCommandHandler;
import com.awslabs.iot.client.helpers.progressbar.ProgressBarHelper;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.client.streams.interfaces.UsesStream;
import com.awslabs.iot.helpers.interfaces.GreengrassV1Helper;
import io.vavr.collection.List;
import io.vavr.collection.Stream;
import io.vavr.control.Try;
import software.amazon.awssdk.services.greengrass.model.DefinitionInformation;
import software.amazon.awssdk.services.greengrass.model.GetCoreDefinitionVersionResponse;

import javax.inject.Inject;

public class DeleteAllCoreDefinitionsCommandHandler implements GreengrassCommandHandler, UsesStream<DefinitionInformation> {
    private static final String DELETE_CORE_DEFINITIONS = "delete-all-core-definitions";
    @Inject
    GreengrassV1Helper greengrassV1Helper;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    ProgressBarHelper progressBarHelper;

    @Inject
    public DeleteAllCoreDefinitionsCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        Try.withResources(() -> progressBarHelper.start("Delete all Greengrass core definitions", this))
                .of(progressBar -> run());
    }

    private Void run() {
        getStream()
                .peek(definitionInformation -> progressBarHelper.next())
                .forEach(greengrassV1Helper::deleteCoreDefinition);

        return null;
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

    @Override
    public Stream<DefinitionInformation> getStream() {
        List<String> immutableCoreDefinitionIds = greengrassV1Helper.getImmutableCoreDefinitionVersionResponses()
                .map(GetCoreDefinitionVersionResponse::id)
                .toList();

        return greengrassV1Helper.getCoreDefinitions()
                .filter(definitionInformation -> !immutableCoreDefinitionIds.contains(definitionInformation.id()));
    }
}
