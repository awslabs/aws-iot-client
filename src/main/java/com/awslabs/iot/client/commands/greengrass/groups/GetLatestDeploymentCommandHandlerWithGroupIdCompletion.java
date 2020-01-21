package com.awslabs.iot.client.commands.greengrass.groups;

import com.amazonaws.services.greengrass.model.Deployment;
import com.awslabs.aws.iot.resultsiterator.helpers.interfaces.IoHelper;
import com.awslabs.aws.iot.resultsiterator.helpers.v1.interfaces.V1GreengrassHelper;
import com.awslabs.iot.client.commands.greengrass.GreengrassGroupCommandHandlerWithGroupIdCompletion;
import com.awslabs.iot.client.commands.greengrass.completers.GreengrassGroupIdCompleter;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class GetLatestDeploymentCommandHandlerWithGroupIdCompletion implements GreengrassGroupCommandHandlerWithGroupIdCompletion {
    private static final String GET_LATEST_DEPLOYMENT = "get-latest-deployment";
    private static final int GROUP_ID_POSITION = 0;
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(GetLatestDeploymentCommandHandlerWithGroupIdCompletion.class);
    @Inject
    V1GreengrassHelper greengrassHelper;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;
    @Inject
    GreengrassGroupIdCompleter greengrassGroupIdCompleter;

    @Inject
    public GetLatestDeploymentCommandHandlerWithGroupIdCompletion() {
    }

    @Override
    public void innerHandle(String input) {
        List<String> parameters = parameterExtractor.getParameters(input);

        String groupId = parameters.get(GROUP_ID_POSITION);

        Deployment deployment = greengrassHelper.getLatestDeployment(groupId);

        if (deployment == null) {
            return;
        }

        log.info("  [" + deployment.getDeploymentId() + " - " + deployment.getCreatedAt() + "]");
    }

    @Override
    public String getCommandString() {
        return GET_LATEST_DEPLOYMENT;
    }

    @Override
    public String getHelp() {
        return "Gets the latest deployment for a group.";
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
