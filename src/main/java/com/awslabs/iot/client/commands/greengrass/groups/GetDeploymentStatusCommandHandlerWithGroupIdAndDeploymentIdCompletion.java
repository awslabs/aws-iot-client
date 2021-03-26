package com.awslabs.iot.client.commands.greengrass.groups;


import com.awslabs.iot.client.commands.greengrass.GreengrassGroupCommandHandlerWithGroupIdAndDeploymentIdCompletion;
import com.awslabs.iot.client.commands.greengrass.completers.GreengrassGroupIdAndDeploymentIdCompleter;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.data.GreengrassGroupId;
import com.awslabs.iot.data.ImmutableGreengrassGroupId;
import com.awslabs.iot.helpers.interfaces.GreengrassV1Helper;
import com.jcabi.log.Logger;
import io.vavr.collection.List;
import io.vavr.control.Option;
import software.amazon.awssdk.services.greengrass.model.GetDeploymentStatusResponse;

import javax.inject.Inject;

public class GetDeploymentStatusCommandHandlerWithGroupIdAndDeploymentIdCompletion implements GreengrassGroupCommandHandlerWithGroupIdAndDeploymentIdCompletion {
    private static final String GET_DEPLOYMENT_STATUS = "get-deployment-status";
    private static final int GROUP_ID_POSITION = 0;
    private static final int DEPLOYMENT_ID_POSITION = 1;
    @Inject
    GreengrassV1Helper greengrassV1Helper;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    GreengrassGroupIdAndDeploymentIdCompleter greengrassGroupIdAndDeploymentIdCompleter;

    @Inject
    public GetDeploymentStatusCommandHandlerWithGroupIdAndDeploymentIdCompletion() {
    }

    @Override
    public void innerHandle(String input) {
        List<String> parameters = parameterExtractor.getParameters(input);

        GreengrassGroupId groupId = ImmutableGreengrassGroupId.builder().groupId(parameters.get(GROUP_ID_POSITION)).build();
        String deploymentId = parameters.get(DEPLOYMENT_ID_POSITION);

        Option<GetDeploymentStatusResponse> optionalGetDeploymentStatusResponse = greengrassV1Helper.getDeployments(groupId)
                .filter(deployment -> deployment.deploymentId().equals(deploymentId))
                .headOption()
                .flatMap(deployment -> greengrassV1Helper.getDeploymentStatusResponse(groupId, deployment));

        if (optionalGetDeploymentStatusResponse.isEmpty()) {
            Logger.info(this, String.join("", "No status available for group [", groupId.getGroupId(), "] and deployment [", deploymentId, "]"));
            return;
        }

        Logger.info(this, String.join("", "Status of deployment [", deploymentId, "] is [", optionalGetDeploymentStatusResponse.get().deploymentStatus(), "]"));
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

    public GreengrassGroupIdAndDeploymentIdCompleter getGreengrassGroupIdAndDeploymentIdCompleter() {
        return this.greengrassGroupIdAndDeploymentIdCompleter;
    }
}
