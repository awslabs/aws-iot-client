package com.awslabs.iot.client.commands.greengrass.resources;

import com.awslabs.general.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.commands.greengrass.GreengrassGroupCommandHandlerWithGroupIdCompletion;
import com.awslabs.iot.client.commands.greengrass.completers.GreengrassGroupIdCompleter;
import com.awslabs.iot.client.helpers.json.interfaces.ObjectPrettyPrinter;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.data.GreengrassGroupId;
import com.awslabs.iot.data.ImmutableGreengrassGroupId;
import com.awslabs.iot.helpers.interfaces.V2GreengrassHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.greengrass.model.GroupVersion;
import software.amazon.awssdk.services.greengrass.model.ResourceDefinitionVersion;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

public class GetLatestResourceDefinitionVersionCommandHandlerWithGroupIdCompletion implements GreengrassGroupCommandHandlerWithGroupIdCompletion {
    private static final String GET_LATEST_RESOURCE_DEFINITION = "get-latest-resource-definition";
    private static final int GROUP_ID_POSITION = 0;
    private static final Logger log = LoggerFactory.getLogger(GetLatestResourceDefinitionVersionCommandHandlerWithGroupIdCompletion.class);
    @Inject
    V2GreengrassHelper v2GreengrassHelper;
    @Inject
    ObjectPrettyPrinter objectPrettyPrinter;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;
    @Inject
    GreengrassGroupIdCompleter greengrassGroupIdCompleter;

    @Inject
    public GetLatestResourceDefinitionVersionCommandHandlerWithGroupIdCompletion() {
    }

    @Override
    public void innerHandle(String input) {
        List<String> parameters = parameterExtractor.getParameters(input);

        GreengrassGroupId groupId = ImmutableGreengrassGroupId.builder().groupId(parameters.get(GROUP_ID_POSITION)).build();

        Optional<GroupVersion> optionalGroupVersion = v2GreengrassHelper.getLatestGroupVersion(groupId);

        if (!optionalGroupVersion.isPresent()) {
            return;
        }

        GroupVersion groupVersion = optionalGroupVersion.get();

        Optional<ResourceDefinitionVersion> resourceDefinitionVersion = v2GreengrassHelper.getResourceDefinitionVersion(groupVersion);

        log.info(objectPrettyPrinter.prettyPrint(resourceDefinitionVersion));
    }

    @Override
    public String getCommandString() {
        return GET_LATEST_RESOURCE_DEFINITION;
    }

    @Override
    public String getHelp() {
        return "Gets the latest resource definition version for a group.";
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
