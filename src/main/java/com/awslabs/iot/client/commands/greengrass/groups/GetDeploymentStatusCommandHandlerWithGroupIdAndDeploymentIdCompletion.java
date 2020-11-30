package com.awslabs.iot.client.commands.greengrass.groups;

import com.awslabs.general.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.commands.greengrass.GreengrassGroupCommandHandlerWithGroupIdAndDeploymentIdCompletion;
import com.awslabs.iot.client.commands.greengrass.completers.GreengrassGroupIdAndDeploymentIdCompleter;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.data.GreengrassGroupId;
import com.awslabs.iot.data.ImmutableGreengrassGroupId;
import com.awslabs.iot.helpers.interfaces.V2GreengrassHelper;
import com.jcabi.log.Logger;
import software.amazon.awssdk.services.greengrass.model.GetDeploymentStatusResponse;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

public class GetDeploymentStatusCommandHandlerWithGroupIdAndDeploymentIdCompletion implements GreengrassGroupCommandHandlerWithGroupIdAndDeploymentIdCompletion {
    private static final String GET_DEPLOYMENT_STATUS = "get-deployment-status";
    private static final int GROUP_ID_POSITION = 0;
    private static final int DEPLOYMENT_ID_POSITION = 1;
    @Inject
    V2GreengrassHelper v2GreengrassHelper;
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

        GreengrassGroupId groupId = ImmutableGreengrassGroupId.builder().groupId(parameters.get(GROUP_ID_POSITION)).build();
        String deploymentId = parameters.get(DEPLOYMENT_ID_POSITION);

        Optional<GetDeploymentStatusResponse> optionalGetDeploymentStatusResponse = v2GreengrassHelper.getDeployments(groupId)
                .filter(deployment -> deployment.deploymentId().equals(deploymentId))
                .findFirst()
                .flatMap(deployment -> v2GreengrassHelper.getDeploymentStatusResponse(groupId, deployment));

        if (!optionalGetDeploymentStatusResponse.isPresent()) {
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

    public IoHelper getIoHelper() {
        return this.ioHelper;
    }

    public GreengrassGroupIdAndDeploymentIdCompleter getGreengrassGroupIdAndDeploymentIdCompleter() {
        return this.greengrassGroupIdAndDeploymentIdCompleter;
    }
}
