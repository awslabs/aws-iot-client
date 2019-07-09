package com.awslabs.iot.client.commands.iot.things;

import com.awslabs.iot.client.commands.iot.IotCommandHandler;
import com.awslabs.iot.client.helpers.io.interfaces.IOHelper;
import com.awslabs.iot.client.helpers.iot.interfaces.ThingGroupHelper;
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
    IOHelper ioHelper;
    @Inject
    Provider<ThingGroupHelper> thingGroupHelperProvider;

    @Inject
    public DeleteAllThingGroupsCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        ThingGroupHelper thingGroupHelper = thingGroupHelperProvider.get();
        thingGroupHelper.listThingGroups().stream()
                .forEach(groupNameAndArn -> {
                    log.info("Deleting thing group [{}]", groupNameAndArn.getGroupName());
                    thingGroupHelper.deleteThingGroup(groupNameAndArn.getGroupName());
                });
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

    public IOHelper getIoHelper() {
        return this.ioHelper;
    }
}
