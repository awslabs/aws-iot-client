package com.awslabs.iot.client.commands.greengrass.groups;

import com.awslabs.general.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.commands.greengrass.GreengrassCommandHandler;
import com.awslabs.iot.client.helpers.progressbar.ProgressBarHelper;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.client.streams.interfaces.UsesStream;
import com.awslabs.iot.helpers.interfaces.V2GreengrassHelper;
import io.vavr.control.Try;
import software.amazon.awssdk.services.greengrass.model.DefinitionInformation;
import software.amazon.awssdk.services.greengrass.model.GetSubscriptionDefinitionVersionResponse;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DeleteAllSubscriptionDefinitionsCommandHandler implements GreengrassCommandHandler, UsesStream<DefinitionInformation> {
    private static final String DELETE_SUBSCRIPTION_DEFINITIONS = "delete-all-subscription-definitions";
    @Inject
    V2GreengrassHelper v2GreengrassHelper;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;
    @Inject
    ProgressBarHelper progressBarHelper;

    @Inject
    public DeleteAllSubscriptionDefinitionsCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        Try.withResources(() -> progressBarHelper.start("Delete all Greengrass subscription definitions", this))
                .of(progressBar -> run());
    }

    private Void run() {
        getStream()
                .peek(definitionInformation -> progressBarHelper.next())
                .forEach(v2GreengrassHelper::deleteSubscriptionDefinition);

        return null;
    }

    @Override
    public String getCommandString() {
        return DELETE_SUBSCRIPTION_DEFINITIONS;
    }

    @Override
    public String getHelp() {
        return "Deletes all Greengrass subscription definitions.";
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
        List<String> immutableSubscriptionDefinitionIds = v2GreengrassHelper.getImmutableSubscriptionDefinitionVersionResponses()
                .map(GetSubscriptionDefinitionVersionResponse::id)
                .collect(Collectors.toList());

        return v2GreengrassHelper.getSubscriptionDefinitions()
                .filter(definitionInformation -> !immutableSubscriptionDefinitionIds.contains(definitionInformation.id()));
    }
}
