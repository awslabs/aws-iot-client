package com.awslabs.iot.client.commands.greengrass.groups;

import com.awslabs.general.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.commands.greengrass.GreengrassCommandHandler;
import com.awslabs.iot.client.helpers.progressbar.ProgressBarHelper;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.client.streams.interfaces.UsesStream;
import com.awslabs.iot.helpers.interfaces.V2GreengrassHelper;
import io.vavr.control.Try;
import software.amazon.awssdk.services.greengrass.model.DefinitionInformation;
import software.amazon.awssdk.services.greengrass.model.GetDeviceDefinitionVersionResponse;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DeleteAllDeviceDefinitionsCommandHandler implements GreengrassCommandHandler, UsesStream<DefinitionInformation> {
    private static final String DELETE_DEVICE_DEFINITIONS = "delete-all-device-definitions";
    @Inject
    V2GreengrassHelper v2GreengrassHelper;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;
    @Inject
    ProgressBarHelper progressBarHelper;

    @Inject
    public DeleteAllDeviceDefinitionsCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        Try.withResources(() -> progressBarHelper.start("Delete all Greengrass device definitions", this))
                .of(progressBar -> run());
    }

    private Void run() {
        getStream()
                .peek(definitionInformation -> progressBarHelper.next())
                .forEach(v2GreengrassHelper::deleteDeviceDefinition);

        return null;
    }


    @Override
    public String getCommandString() {
        return DELETE_DEVICE_DEFINITIONS;
    }

    @Override
    public String getHelp() {
        return "Deletes all Greengrass device definitions.";
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

    @Override
    public Stream<DefinitionInformation> getStream() {
        List<String> immutableDeviceDefinitionIds = v2GreengrassHelper.getImmutableDeviceDefinitionVersionResponses()
                .map(GetDeviceDefinitionVersionResponse::id)
                .collect(Collectors.toList());

        return v2GreengrassHelper.getDeviceDefinitions()
                .filter(definitionInformation -> !immutableDeviceDefinitionIds.contains(definitionInformation.id()));
    }
}
