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
import software.amazon.awssdk.services.greengrass.model.GetLoggerDefinitionVersionResponse;

import javax.inject.Inject;

public class DeleteAllLoggerDefinitionsCommandHandler implements GreengrassCommandHandler, UsesStream<DefinitionInformation> {
    private static final String DELETE_LOGGER_DEFINITIONS = "delete-all-logger-definitions";
    @Inject
    GreengrassV1Helper greengrassV1Helper;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    ProgressBarHelper progressBarHelper;

    @Inject
    public DeleteAllLoggerDefinitionsCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        Try.withResources(() -> progressBarHelper.start("Delete all Greengrass logger definitions", this))
                .of(progressBar -> run());
    }

    private Void run() {
        getStream()
                .peek(definitionInformation -> progressBarHelper.next())
                .forEach(greengrassV1Helper::deleteLoggerDefinition);

        return null;
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

    @Override
    public Stream<DefinitionInformation> getStream() {
        List<String> immutableLoggerDefinitionIds = greengrassV1Helper.getImmutableLoggerDefinitionVersionResponses()
                .map(GetLoggerDefinitionVersionResponse::id)
                .toList();

        return greengrassV1Helper.getLoggerDefinitions()
                .filter(definitionInformation -> !immutableLoggerDefinitionIds.contains(definitionInformation.id()));
    }
}
