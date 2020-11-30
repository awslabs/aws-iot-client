package com.awslabs.iot.client.commands.greengrass.groups;

import com.awslabs.general.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.commands.greengrass.GreengrassGroupCommandHandlerWithGroupIdCompletion;
import com.awslabs.iot.client.commands.greengrass.completers.GreengrassGroupIdCompleter;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.data.ImmutableGreengrassGroupId;
import com.awslabs.iot.helpers.interfaces.V2GreengrassHelper;
import com.jcabi.log.Logger;
import software.amazon.awssdk.services.greengrass.model.GroupInformation;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

public class GetLatestGroupVersionCommandHandlerWithGroupIdCompletion implements GreengrassGroupCommandHandlerWithGroupIdCompletion {
    private static final String GET_LATEST_GROUP_VERSION = "get-latest-group-version";
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
    public GetLatestGroupVersionCommandHandlerWithGroupIdCompletion() {
    }

    @Override
    public void innerHandle(String input) {
        List<String> parameters = parameterExtractor.getParameters(input);

        String groupId = parameters.get(GROUP_ID_POSITION);

        Optional<GroupInformation> optionalGroupInformation = v2GreengrassHelper.getGroupInformation(ImmutableGreengrassGroupId.builder().groupId(groupId).build());

        if (!optionalGroupInformation.isPresent()) {
            return;
        }

        GroupInformation groupInformation = optionalGroupInformation.get();

        Logger.info(this, String.join("", "  [", groupInformation.latestVersion(), " - ", groupInformation.creationTimestamp(), "]"));
    }

    @Override
    public String getCommandString() {
        return GET_LATEST_GROUP_VERSION;
    }

    @Override
    public String getHelp() {
        return "Gets the latest group version for a group.";
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
