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
import java.util.Optional;

public class GetLatestDeploymentStatusCommandHandlerWithGroupIdCompletion implements GreengrassGroupCommandHandlerWithGroupIdCompletion {
    private static final String GET_LATEST_DEPLOYMENT_STATUS = "get-latest-deployment-status";
    private static final int GROUP_ID_POSITION = 0;
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(GetLatestDeploymentStatusCommandHandlerWithGroupIdCompletion.class);
    @Inject
    V1GreengrassHelper greengrassHelper;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;
    @Inject
    GreengrassGroupIdCompleter greengrassGroupIdCompleter;

    @Inject
    public GetLatestDeploymentStatusCommandHandlerWithGroupIdCompletion() {
    }

    @Override
    public void innerHandle(String input) {
        List<String> parameters = parameterExtractor.getParameters(input);

        String groupId = parameters.get(GROUP_ID_POSITION);

        Optional<Deployment> optionalDeploymentId = greengrassHelper.getLatestDeployment(groupId);

        if (!optionalDeploymentId.isPresent()) {
            log.info("No deployments for [" + groupId + "]");
            return;
        }

        Deployment deploymentId = optionalDeploymentId.get();

        String status = greengrassHelper.getDeploymentStatus(groupId, deploymentId.getDeploymentId());

        if (status == null) {
            log.info("No status available for group [" + groupId + "] and deployment [" + deploymentId + "]");
            return;
        }

        log.info(status);
    }

    @Override
    public String getCommandString() {
        return GET_LATEST_DEPLOYMENT_STATUS;
    }

    @Override
    public String getHelp() {
        return "Gets the status of the latest deployment.";
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
