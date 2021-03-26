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
import software.amazon.awssdk.services.greengrass.model.GetFunctionDefinitionVersionResponse;

import javax.inject.Inject;

public class DeleteAllFunctionDefinitionsCommandHandler implements GreengrassCommandHandler, UsesStream<DefinitionInformation> {
    private static final String DELETE_FUNCTION_DEFINITIONS = "delete-all-function-definitions";
    @Inject
    GreengrassV1Helper greengrassV1Helper;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    ProgressBarHelper progressBarHelper;

    @Inject
    public DeleteAllFunctionDefinitionsCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        Try.withResources(() -> progressBarHelper.start("Delete all Greengrass function definitions", this))
                .of(progressBar -> run());
    }

    private Void run() {
        getStream()
                .peek(definitionInformation -> progressBarHelper.next())
                .forEach(greengrassV1Helper::deleteFunctionDefinition);

        return null;
    }

    @Override
    public String getCommandString() {
        return DELETE_FUNCTION_DEFINITIONS;
    }

    @Override
    public String getHelp() {
        return "Deletes all Greengrass function definitions.";
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
        List<String> immutableFunctionDefinitionIds = greengrassV1Helper.getImmutableFunctionDefinitionVersionResponses()
                .map(GetFunctionDefinitionVersionResponse::id)
                .toList();

        return greengrassV1Helper.getFunctionDefinitions()
                .filter(definitionInformation -> !immutableFunctionDefinitionIds.contains(definitionInformation.id()));
    }
}
