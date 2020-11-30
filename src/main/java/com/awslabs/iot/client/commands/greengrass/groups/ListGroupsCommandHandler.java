package com.awslabs.iot.client.commands.greengrass.groups;

import com.awslabs.general.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.commands.greengrass.GreengrassCommandHandler;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.helpers.interfaces.V2GreengrassHelper;
import com.jcabi.log.Logger;

import javax.inject.Inject;

public class ListGroupsCommandHandler implements GreengrassCommandHandler {
    private static final String LIST_GROUPS = "list-groups";
    @Inject
    V2GreengrassHelper v2GreengrassHelper;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;

    @Inject
    public ListGroupsCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        v2GreengrassHelper.getGroups()
                .forEach(groupInformation -> Logger.info(this, String.join("", "  [", groupInformation.name(), " - ", groupInformation.id(), "]")));
    }

    @Override
    public String getCommandString() {
        return LIST_GROUPS;
    }

    @Override
    public String getHelp() {
        return "Lists all Greengrass groups.";
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
