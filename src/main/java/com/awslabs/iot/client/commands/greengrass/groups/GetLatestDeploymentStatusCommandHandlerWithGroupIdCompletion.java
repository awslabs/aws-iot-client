package com.awslabs.iot.client.commands.greengrass.groups;

import com.awslabs.general.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.commands.greengrass.GreengrassGroupCommandHandlerWithGroupIdCompletion;
import com.awslabs.iot.client.commands.greengrass.completers.GreengrassGroupIdCompleter;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.data.GreengrassGroupId;
import com.awslabs.iot.data.ImmutableGreengrassGroupId;
import com.awslabs.iot.helpers.interfaces.V2GreengrassHelper;
import com.jcabi.log.Logger;
import software.amazon.awssdk.services.greengrass.model.GetDeploymentStatusResponse;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

public class GetLatestDeploymentStatusCommandHandlerWithGroupIdCompletion implements GreengrassGroupCommandHandlerWithGroupIdCompletion {
    private static final String GET_LATEST_DEPLOYMENT_STATUS = "get-latest-deployment-status";
    private static final int GROUP_ID_POSITION = 0;
    @Inject
    V2GreengrassHelper v2GreengrassHelper;
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

        GreengrassGroupId groupId = ImmutableGreengrassGroupId.builder().groupId(parameters.get(GROUP_ID_POSITION)).build();

        Optional<GetDeploymentStatusResponse> optionalGetDeploymentStatusResponse = v2GreengrassHelper.getLatestDeployment(groupId)
                .flatMap(deployment -> v2GreengrassHelper.getDeploymentStatusResponse(groupId, deployment));

        if (!optionalGetDeploymentStatusResponse.isPresent()) {
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

    public IoHelper getIoHelper() {
        return this.ioHelper;
    }

    public GreengrassGroupIdCompleter getGreengrassGroupIdCompleter() {
        return this.greengrassGroupIdCompleter;
    }
}
