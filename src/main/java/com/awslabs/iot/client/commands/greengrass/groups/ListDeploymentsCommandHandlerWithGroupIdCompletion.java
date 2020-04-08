package com.awslabs.iot.client.commands.greengrass.groups;

import com.awslabs.general.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.commands.greengrass.GreengrassGroupCommandHandlerWithGroupIdCompletion;
import com.awslabs.iot.client.commands.greengrass.completers.GreengrassGroupIdCompleter;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.helpers.interfaces.V1GreengrassHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;

public class ListDeploymentsCommandHandlerWithGroupIdCompletion implements GreengrassGroupCommandHandlerWithGroupIdCompletion {
    private static final String LIST_DEPLOYMENTS = "list-deployments";
    private static final int GROUP_ID_POSITION = 0;
    private static final Logger log = LoggerFactory.getLogger(ListDeploymentsCommandHandlerWithGroupIdCompletion.class);
    @Inject
    V1GreengrassHelper greengrassHelper;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;
    @Inject
    GreengrassGroupIdCompleter greengrassGroupIdCompleter;

    @Inject
    public ListDeploymentsCommandHandlerWithGroupIdCompletion() {
    }

    @Override
    public void innerHandle(String input) {
        List<String> parameters = parameterExtractor.getParameters(input);

        String groupId = parameters.get(GROUP_ID_POSITION);

        greengrassHelper.listDeployments(groupId)
                .forEach(deployment -> log.info("  [" + deployment.getDeploymentId() + " - " + deployment.getCreatedAt() + "]"));
    }

    @Override
    public String getCommandString() {
        return LIST_DEPLOYMENTS;
    }

    @Override
    public String getHelp() {
        return "Lists all deployments for a group.";
    }

    @Override
    public int requiredParameters() {
        return 1;
    }

    public ParameterExtractor getParameterExtractor() {
        return this.parameterExtractor;
    }

    public IoHelper getIoHelper() {
        return this.ioHelper;
    }

    public GreengrassGroupIdCompleter getGreengrassGroupIdCompleter() {
        return this.greengrassGroupIdCompleter;
    }
}
