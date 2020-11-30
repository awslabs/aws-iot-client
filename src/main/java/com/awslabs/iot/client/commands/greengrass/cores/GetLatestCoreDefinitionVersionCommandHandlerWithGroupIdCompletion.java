package com.awslabs.iot.client.commands.greengrass.cores;

import com.awslabs.general.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.commands.greengrass.GreengrassGroupCommandHandlerWithGroupIdCompletion;
import com.awslabs.iot.client.commands.greengrass.completers.GreengrassGroupIdCompleter;
import com.awslabs.iot.client.helpers.json.interfaces.ObjectPrettyPrinter;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.data.GreengrassGroupId;
import com.awslabs.iot.data.ImmutableGreengrassGroupId;
import com.awslabs.iot.helpers.interfaces.V2GreengrassHelper;
import com.jcabi.log.Logger;
import software.amazon.awssdk.services.greengrass.model.CoreDefinitionVersion;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

public class GetLatestCoreDefinitionVersionCommandHandlerWithGroupIdCompletion implements GreengrassGroupCommandHandlerWithGroupIdCompletion {
    private static final String GET_LATEST_CORE_DEFINITION = "get-latest-core-definition";
    private static final int GROUP_ID_POSITION = 0;
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
    public GetLatestCoreDefinitionVersionCommandHandlerWithGroupIdCompletion() {
    }

    @Override
    public void innerHandle(String input) {
        List<String> parameters = parameterExtractor.getParameters(input);

        GreengrassGroupId groupId = ImmutableGreengrassGroupId.builder().groupId(parameters.get(GROUP_ID_POSITION)).build();

        Optional<CoreDefinitionVersion> optionalCoreDefinitionVersion = v2GreengrassHelper.getGroupInformation(groupId)
                .flatMap(v2GreengrassHelper::getCoreDefinitionVersion);

        if (!optionalCoreDefinitionVersion.isPresent()) {
            Logger.info(this, "No core definition found");
            return;
        }

        Logger.info(this, objectPrettyPrinter.prettyPrint(optionalCoreDefinitionVersion.get()));
    }

    @Override
    public String getCommandString() {
        return GET_LATEST_CORE_DEFINITION;
    }

    @Override
    public String getHelp() {
        return "Gets the latest core definition version for a group.";
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
