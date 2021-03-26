package com.awslabs.iot.client.commands.greengrass.groups;


import com.awslabs.iot.client.commands.greengrass.GreengrassCommandHandler;
import com.awslabs.iot.client.helpers.progressbar.ProgressBarHelper;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.client.streams.interfaces.UsesStream;
import com.awslabs.iot.data.ImmutableGreengrassGroupId;
import com.awslabs.iot.helpers.interfaces.GreengrassV1Helper;
import io.vavr.collection.Stream;
import io.vavr.control.Try;
import software.amazon.awssdk.services.greengrass.model.GroupInformation;

import javax.inject.Inject;

public class DeleteAllGroupsCommandHandler implements GreengrassCommandHandler, UsesStream<GroupInformation> {
    private static final String DELETE_ALL_GROUPS = "delete-all-groups";
    @Inject
    GreengrassV1Helper greengrassV1Helper;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    ProgressBarHelper progressBarHelper;

    @Inject
    public DeleteAllGroupsCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        Try.withResources(() -> progressBarHelper.start("Delete all Greengrass groups", this))
                .of(progressBar -> run());
    }

    private Void run() {
        getStream()
                .map(GroupInformation::id)
                .map(id -> ImmutableGreengrassGroupId.builder().groupId(id).build())
                .peek(groupId -> progressBarHelper.next())
                .forEach(greengrassV1Helper::deleteGroup);

        return null;
    }

    @Override
    public String getCommandString() {
        return DELETE_ALL_GROUPS;
    }

    @Override
    public String getHelp() {
        return "Deletes all Greengrass groups.";
    }

    @Override
    public int requiredParameters() {
        return 0;
    }

    public ParameterExtractor getParameterExtractor() {
        return this.parameterExtractor;
    }

    @Override
    public Stream<GroupInformation> getStream() {
        return greengrassV1Helper.getNonImmutableGroups();
    }
}
