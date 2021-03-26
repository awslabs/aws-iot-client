package com.awslabs.iot.client.commands.iot.things;


import com.awslabs.iot.client.commands.iot.IotCommandHandler;
import com.awslabs.iot.client.helpers.progressbar.ProgressBarHelper;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.client.streams.interfaces.UsesStream;
import com.awslabs.iot.helpers.interfaces.IotHelper;
import io.vavr.collection.Stream;
import io.vavr.control.Try;
import software.amazon.awssdk.services.iot.model.GroupNameAndArn;

import javax.inject.Inject;

public class DeleteAllThingGroupsCommandHandler implements IotCommandHandler, UsesStream<GroupNameAndArn> {
    private static final String DELETEALLTHINGGROUPS = "delete-all-thing-groups";
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IotHelper iotHelper;
    @Inject
    ProgressBarHelper progressBarHelper;

    @Inject
    public DeleteAllThingGroupsCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        Try.withResources(() -> progressBarHelper.start("Delete all IoT thing groups", this))
                .of(progressBar -> run());
    }

    private Void run() {
        getStream()
                .peek(definitionInformation -> progressBarHelper.next())
                .forEach(iotHelper::delete);

        return null;
    }

    @Override
    public String getCommandString() {
        return DELETEALLTHINGGROUPS;
    }

    @Override
    public String getHelp() {
        return "Deletes all thing groups.";
    }

    @Override
    public int requiredParameters() {
        return 0;
    }

    public ParameterExtractor getParameterExtractor() {
        return this.parameterExtractor;
    }

    @Override
    public Stream<GroupNameAndArn> getStream() {
        return iotHelper.getThingGroups();
    }
}
