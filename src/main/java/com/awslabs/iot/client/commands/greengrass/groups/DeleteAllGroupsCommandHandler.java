package com.awslabs.iot.client.commands.greengrass.groups;

import com.awslabs.iot.client.commands.greengrass.GreengrassCommandHandler;
import com.awslabs.iot.client.helpers.greengrass.interfaces.GreengrassHelper;
import com.awslabs.iot.client.helpers.io.interfaces.IOHelper;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import org.slf4j.Logger;

import javax.inject.Inject;

public class DeleteAllGroupsCommandHandler implements GreengrassCommandHandler {
    private static final String DELETE_ALL_GROUPS = "delete-all-groups";
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(DeleteAllGroupsCommandHandler.class);
    @Inject
    GreengrassHelper greengrassHelper;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IOHelper ioHelper;

    @Inject
    public DeleteAllGroupsCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        greengrassHelper.listGroupIds().stream()
                .sorted()
                .map(greengrassHelper::deleteGroup);
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

    public IOHelper getIoHelper() {
        return this.ioHelper;
    }
}
