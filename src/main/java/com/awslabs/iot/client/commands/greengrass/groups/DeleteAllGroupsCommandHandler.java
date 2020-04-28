package com.awslabs.iot.client.commands.greengrass.groups;

import com.awslabs.general.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.commands.greengrass.GreengrassCommandHandler;
import com.awslabs.iot.client.helpers.progressbar.ProgressBarHelper;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.client.streams.interfaces.UsesStream;
import com.awslabs.iot.data.ImmutableGreengrassGroupId;
import com.awslabs.iot.helpers.interfaces.V2GreengrassHelper;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.greengrass.model.GroupInformation;

import javax.inject.Inject;
import java.util.stream.Stream;

public class DeleteAllGroupsCommandHandler implements GreengrassCommandHandler, UsesStream<GroupInformation> {
    private static final String DELETE_ALL_GROUPS = "delete-all-groups";
    private static final Logger log = LoggerFactory.getLogger(DeleteAllGroupsCommandHandler.class);
    @Inject
    V2GreengrassHelper v2GreengrassHelper;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;
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
                .forEach(v2GreengrassHelper::deleteGroup);

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

    public IoHelper getIoHelper() {
        return this.ioHelper;
    }

    @Override
    public Stream<GroupInformation> getStream() {
        return v2GreengrassHelper.getNonImmutableGroups();
    }
}
