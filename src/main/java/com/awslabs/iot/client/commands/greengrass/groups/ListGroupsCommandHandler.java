package com.awslabs.iot.client.commands.greengrass.groups;

import com.awslabs.aws.iot.resultsiterator.helpers.interfaces.IoHelper;
import com.awslabs.aws.iot.resultsiterator.helpers.v1.interfaces.V1GreengrassHelper;
import com.awslabs.iot.client.commands.greengrass.GreengrassCommandHandler;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import org.slf4j.Logger;

import javax.inject.Inject;

public class ListGroupsCommandHandler implements GreengrassCommandHandler {
    private static final String LIST_GROUPS = "list-groups";
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ListGroupsCommandHandler.class);
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
