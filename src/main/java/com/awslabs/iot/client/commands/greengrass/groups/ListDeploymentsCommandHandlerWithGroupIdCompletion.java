package com.awslabs.iot.client.commands.greengrass.groups;

import com.awslabs.general.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.commands.greengrass.GreengrassGroupCommandHandlerWithGroupIdCompletion;
import com.awslabs.iot.client.commands.greengrass.completers.GreengrassGroupIdCompleter;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.data.ImmutableGreengrassGroupId;
import com.awslabs.iot.helpers.interfaces.V2GreengrassHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.greengrass.model.GroupInformation;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

public class ListDeploymentsCommandHandlerWithGroupIdCompletion implements GreengrassGroupCommandHandlerWithGroupIdCompletion {
    private static final String LIST_DEPLOYMENTS = "list-deployments";
    private static final int GROUP_ID_POSITION = 0;
    private static final Logger log = LoggerFactory.getLogger(ListDeploymentsCommandHandlerWithGroupIdCompletion.class);
    @Inject
    V2GreengrassHelper v2GreengrassHelper;
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

        Optional<GroupInformation> optionalGroupInformation = v2GreengrassHelper.getGroupInformation(ImmutableGreengrassGroupId.builder().groupId(groupId).build());

        if (!optionalGroupInformation.isPresent()) {
            return;
        }

        v2GreengrassHelper.getDeployments(optionalGroupInformation.get())
                .forEach(deployment -> log.info(String.join("", "  [", deployment.deploymentId(), " - ", deployment.createdAt(), "]")));
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
