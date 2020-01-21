package com.awslabs.iot.client.commands.iot.things;

import com.amazonaws.services.iot.model.GroupNameAndArn;
import com.awslabs.aws.iot.resultsiterator.helpers.interfaces.IoHelper;
import com.awslabs.aws.iot.resultsiterator.helpers.v1.interfaces.V1ThingGroupHelper;
import com.awslabs.iot.client.commands.iot.IotCommandHandler;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Provider;

public class DeleteAllThingGroupsCommandHandler implements IotCommandHandler {
    private static final String DELETEALLTHINGGROUPS = "delete-all-thing-groups";
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(DeleteAllThingGroupsCommandHandler.class);
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;
    @Inject
    Provider<V1ThingGroupHelper> thingGroupHelperProvider;

    @Inject
    public DeleteAllThingGroupsCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        V1ThingGroupHelper thingGroupHelper = thingGroupHelperProvider.get();
        thingGroupHelper.listThingGroups()
                .forEach(groupNameAndArn -> deleteAndLog(thingGroupHelper, groupNameAndArn));
    }

    private void deleteAndLog(V1ThingGroupHelper thingGroupHelper, GroupNameAndArn groupNameAndArn) {
        thingGroupHelper.deleteThingGroup(groupNameAndArn.getGroupName());

        log.info("Deleted thing group [{}]", groupNameAndArn.getGroupName());
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
