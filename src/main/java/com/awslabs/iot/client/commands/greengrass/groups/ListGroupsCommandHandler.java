package com.awslabs.iot.client.commands.greengrass.groups;

import com.amazonaws.services.greengrass.model.GroupInformation;
import com.awslabs.iot.client.commands.greengrass.GreengrassCommandHandler;
import com.awslabs.iot.client.helpers.greengrass.interfaces.GreengrassHelper;
import com.awslabs.iot.client.helpers.io.interfaces.IOHelper;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class ListGroupsCommandHandler implements GreengrassCommandHandler {
    private static final String LIST_GROUPS = "list-groups";
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ListGroupsCommandHandler.class);
    @Inject
    GreengrassHelper greengrassHelper;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IOHelper ioHelper;

    @Inject
    public ListGroupsCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        List<GroupInformation> groupInformationList = greengrassHelper.listGroups();

        for (GroupInformation groupInformation : groupInformationList) {
            log.info("  [" + groupInformation.getName() + " - " + groupInformation.getId() + "]");
        }
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

    public IOHelper getIoHelper() {
        return this.ioHelper;
    }
}
