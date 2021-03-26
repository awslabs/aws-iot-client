package com.awslabs.iot.client.commands.greengrass.groups;


import com.awslabs.iot.client.commands.greengrass.GreengrassGroupCommandHandlerWithGroupIdCompletion;
import com.awslabs.iot.client.commands.greengrass.completers.GreengrassGroupIdCompleter;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.data.GreengrassGroupId;
import com.awslabs.iot.data.ImmutableGreengrassGroupId;
import com.awslabs.iot.helpers.interfaces.GreengrassV1Helper;
import com.jcabi.log.Logger;
import io.vavr.collection.List;
import io.vavr.control.Option;
import software.amazon.awssdk.services.greengrass.model.GetDeploymentStatusResponse;

import javax.inject.Inject;

public class GetLatestDeploymentStatusCommandHandlerWithGroupIdCompletion implements GreengrassGroupCommandHandlerWithGroupIdCompletion {
    private static final String GET_LATEST_DEPLOYMENT_STATUS = "get-latest-deployment-status";
    private static final int GROUP_ID_POSITION = 0;
    @Inject
    GreengrassV1Helper greengrassV1Helper;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    GreengrassGroupIdCompleter greengrassGroupIdCompleter;

    @Inject
    public GetLatestDeploymentStatusCommandHandlerWithGroupIdCompletion() {
    }

    @Override
    public void innerHandle(String input) {
        List<String> parameters = parameterExtractor.getParameters(input);

        GreengrassGroupId groupId = ImmutableGreengrassGroupId.builder().groupId(parameters.get(GROUP_ID_POSITION)).build();

        Option<GetDeploymentStatusResponse> optionalGetDeploymentStatusResponse = greengrassV1Helper.getLatestDeployment(groupId)
                .flatMap(deployment -> greengrassV1Helper.getDeploymentStatusResponse(groupId, deployment));

        if (optionalGetDeploymentStatusResponse.isEmpty()) {
            Logger.info(this, String.join("", "No status available the latest deployment of group [", groupId.getGroupId(), "]"));
            return;
        }

        GetDeploymentStatusResponse getDeploymentStatusResponse = optionalGetDeploymentStatusResponse.get();

        Logger.info(this, String.join("", "Status of the latest deployment of [", groupId.getGroupId(), "] is [", getDeploymentStatusResponse.deploymentStatus(), "]"));
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

    public GreengrassGroupIdCompleter getGreengrassGroupIdCompleter() {
        return this.greengrassGroupIdCompleter;
    }
}
