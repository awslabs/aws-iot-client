package com.awslabs.iot.client.commands.greengrass.groups;

import com.awslabs.aws.iot.resultsiterator.helpers.interfaces.IoHelper;
import com.awslabs.aws.iot.resultsiterator.helpers.v1.interfaces.V1GreengrassHelper;
import com.awslabs.iot.client.commands.greengrass.GreengrassGroupCommandHandlerWithGroupIdAndDeploymentIdCompletion;
import com.awslabs.iot.client.commands.greengrass.completers.GreengrassGroupIdAndDeploymentIdCompleter;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class GetDeploymentStatusCommandHandlerWithGroupIdAndDeploymentIdCompletion implements GreengrassGroupCommandHandlerWithGroupIdAndDeploymentIdCompletion {
    private static final String GET_DEPLOYMENT_STATUS = "get-deployment-status";
    private static final int GROUP_ID_POSITION = 0;
    private static final int DEPLOYMENT_ID_POSITION = 1;
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(GetDeploymentStatusCommandHandlerWithGroupIdAndDeploymentIdCompletion.class);
    @Inject
    V1GreengrassHelper greengrassHelper;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;
    @Inject
    GreengrassGroupIdAndDeploymentIdCompleter greengrassGroupIdAndDeploymentIdCompleter;

    @Inject
    public GetDeploymentStatusCommandHandlerWithGroupIdAndDeploymentIdCompletion() {
    }

    @Override
    public void innerHandle(String input) {
        List<String> parameters = parameterExtractor.getParameters(input);

        String groupId = parameters.get(GROUP_ID_POSITION);
        String deploymentId = parameters.get(DEPLOYMENT_ID_POSITION);

        String status = greengrassHelper.getDeploymentStatus(groupId, deploymentId);

        if (status == null) {
            log.info("No status available for group [" + groupId + "] and deployment [" + deploymentId + "]");
            return;
        }

        log.info(status);
    }

    @Override
    public String getCommandString() {
        return GET_DEPLOYMENT_STATUS;
    }

    @Override
    public String getHelp() {
        return "Gets the status of a deployment.";
    }

    @Override
    public int requiredParameters() {
        return 2;
    }

    public ParameterExtractor getParameterExtractor() {
        return this.parameterExtractor;
    }

    public IoHelper getIoHelper() {
        return this.ioHelper;
    }

    public GreengrassGroupIdAndDeploymentIdCompleter getGreengrassGroupIdAndDeploymentIdCompleter() {
        return this.greengrassGroupIdAndDeploymentIdCompleter;
    }
}
