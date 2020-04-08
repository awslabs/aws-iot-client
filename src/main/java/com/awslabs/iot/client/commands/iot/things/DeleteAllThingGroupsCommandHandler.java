package com.awslabs.iot.client.commands.iot.things;

import com.awslabs.general.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.commands.iot.IotCommandHandler;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.helpers.interfaces.V2IotHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.iot.model.GroupNameAndArn;

import javax.inject.Inject;

public class DeleteAllThingGroupsCommandHandler implements IotCommandHandler {
    private static final String DELETEALLTHINGGROUPS = "delete-all-thing-groups";
    private static final Logger log = LoggerFactory.getLogger(DeleteAllThingGroupsCommandHandler.class);
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;
    @Inject
    V2IotHelper v2IotHelper;

    @Inject
    public DeleteAllThingGroupsCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        v2IotHelper.getThingGroups()
                .forEach(groupNameAndArn -> deleteAndLog(groupNameAndArn));
    }

    private void deleteAndLog(GroupNameAndArn groupNameAndArn) {
        v2IotHelper.delete(groupNameAndArn);

        log.info("Deleted thing group [{}]", groupNameAndArn.groupName());
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

    public IoHelper getIoHelper() {
        return this.ioHelper;
    }
}
