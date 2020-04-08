package com.awslabs.iot.client.commands.greengrass.groups;

import com.awslabs.general.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.commands.greengrass.GreengrassCommandHandler;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.helpers.interfaces.V1GreengrassHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class DeleteAllGroupsCommandHandler implements GreengrassCommandHandler {
    private static final String DELETE_ALL_GROUPS = "delete-all-groups";
    private static final Logger log = LoggerFactory.getLogger(DeleteAllGroupsCommandHandler.class);
    @Inject
    V1GreengrassHelper greengrassHelper;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;

    @Inject
    public DeleteAllGroupsCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        greengrassHelper.listGroupIds()
                .sorted()
                .forEach(greengrassHelper::deleteGroup);
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
