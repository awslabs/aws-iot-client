package com.awslabs.iot.client.commands.greengrass.groups;

import com.awslabs.iot.client.commands.greengrass.GreengrassGroupCommandHandlerWithGroupIdCompletion;
import com.awslabs.iot.client.commands.greengrass.completers.GreengrassGroupIdCompleter;
import com.awslabs.iot.client.helpers.greengrass.interfaces.GreengrassHelper;
import com.awslabs.iot.client.helpers.io.interfaces.IOHelper;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class DeleteGroupCommandHandlerWithGroupIdCompletion implements GreengrassGroupCommandHandlerWithGroupIdCompletion {
    private static final String DELETE_GROUP = "delete-group";
    private static final int GROUP_ID_POSITION = 0;
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(DeleteGroupCommandHandlerWithGroupIdCompletion.class);
    @Inject
    GreengrassHelper greengrassHelper;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IOHelper ioHelper;
    @Inject
    GreengrassGroupIdCompleter greengrassGroupIdCompleter;

    @Inject
    public DeleteGroupCommandHandlerWithGroupIdCompletion() {
    }

    @Override
    public void innerHandle(String input) {
        List<String> parameters = parameterExtractor.getParameters(input);

        String groupId = parameters.get(GROUP_ID_POSITION);

        greengrassHelper.deleteGroup(groupId);
    }

    @Override
    public String getCommandString() {
        return DELETE_GROUP;
    }

    @Override
    public String getHelp() {
        return "Deletes a Greengrass group.";
    }

    @Override
    public int requiredParameters() {
        return 1;
    }

    public ParameterExtractor getParameterExtractor() {
        return this.parameterExtractor;
    }

    public IOHelper getIoHelper() {
        return this.ioHelper;
    }

    public GreengrassGroupIdCompleter getGreengrassGroupIdCompleter() {
        return this.greengrassGroupIdCompleter;
    }
}
