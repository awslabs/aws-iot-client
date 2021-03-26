package com.awslabs.iot.client.commands.greengrass.groups;


import com.awslabs.iot.client.commands.greengrass.GreengrassCommandHandler;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.helpers.interfaces.GreengrassV1Helper;
import com.jcabi.log.Logger;

import javax.inject.Inject;

public class ListGroupsCommandHandler implements GreengrassCommandHandler {
    private static final String LIST_GROUPS = "list-groups";
    @Inject
    GreengrassV1Helper greengrassV1Helper;
    @Inject
    ParameterExtractor parameterExtractor;

    @Inject
    public ListGroupsCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        greengrassV1Helper.getGroups()
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
}
