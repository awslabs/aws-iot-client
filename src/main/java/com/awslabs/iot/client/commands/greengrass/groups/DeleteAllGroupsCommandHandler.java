package com.awslabs.iot.client.commands.greengrass.groups;

import com.awslabs.general.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.commands.greengrass.GreengrassCommandHandler;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.data.ImmutableGreengrassGroupId;
import com.awslabs.iot.helpers.interfaces.V2GreengrassHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.greengrass.model.GroupInformation;

import javax.inject.Inject;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DeleteAllGroupsCommandHandler implements GreengrassCommandHandler {
    private static final String DELETE_ALL_GROUPS = "delete-all-groups";
    private static final Logger log = LoggerFactory.getLogger(DeleteAllGroupsCommandHandler.class);
    @Inject
    V2GreengrassHelper v2GreengrassHelper;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;

    @Inject
    public DeleteAllGroupsCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        log.info("Deleting all non-immutable groups...");

        v2GreengrassHelper.getGroups()
                // Sort the groups by ID so we can get a general sense of how far along we are in the process of deleting them
                .sorted(Comparator.comparing(GroupInformation::id))
                // Don't include immutable groups
                .filter(groupInformation -> !v2GreengrassHelper.isGroupImmutable(groupInformation))
                .map(GroupInformation::id)
                .map(id -> ImmutableGreengrassGroupId.builder().groupId(id).build())
                .forEach(v2GreengrassHelper::deleteGroup);
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
}
