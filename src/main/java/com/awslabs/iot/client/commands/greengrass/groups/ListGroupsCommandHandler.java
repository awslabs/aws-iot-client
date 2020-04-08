package com.awslabs.iot.client.commands.greengrass.groups;

import com.awslabs.general.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.commands.greengrass.GreengrassCommandHandler;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.helpers.interfaces.V1GreengrassHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class ListGroupsCommandHandler implements GreengrassCommandHandler {
    private static final String LIST_GROUPS = "list-groups";
    private static final Logger log = LoggerFactory.getLogger(ListGroupsCommandHandler.class);
    @Inject
    V1GreengrassHelper greengrassHelper;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;

    @Inject
    public ListGroupsCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        greengrassHelper.listGroups()
                .forEach(groupInformation -> log.info("  [" + groupInformation.getName() + " - " + groupInformation.getId() + "]"));
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
