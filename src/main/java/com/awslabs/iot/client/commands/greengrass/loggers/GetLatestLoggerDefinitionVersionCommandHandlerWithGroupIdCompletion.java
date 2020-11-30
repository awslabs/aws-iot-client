package com.awslabs.iot.client.commands.greengrass.loggers;

import com.awslabs.general.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.commands.greengrass.GreengrassGroupCommandHandlerWithGroupIdCompletion;
import com.awslabs.iot.client.commands.greengrass.completers.GreengrassGroupIdCompleter;
import com.awslabs.iot.client.helpers.json.interfaces.ObjectPrettyPrinter;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.data.ImmutableGreengrassGroupId;
import com.awslabs.iot.helpers.interfaces.V2GreengrassHelper;
import com.jcabi.log.Logger;
import software.amazon.awssdk.services.greengrass.model.GroupInformation;
import software.amazon.awssdk.services.greengrass.model.LoggerDefinitionVersion;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

public class GetLatestLoggerDefinitionVersionCommandHandlerWithGroupIdCompletion implements GreengrassGroupCommandHandlerWithGroupIdCompletion {
    private static final String GET_LATEST_LOGGER_DEFINITION = "get-latest-logger-definition";
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
    public GetLatestLoggerDefinitionVersionCommandHandlerWithGroupIdCompletion() {
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

        Optional<LoggerDefinitionVersion> optionalLoggerDefinitionVersion = v2GreengrassHelper.getLoggerDefinitionVersion(groupInformation);

        if (!optionalLoggerDefinitionVersion.isPresent()) {
            Logger.info(this, "No loggers found");
            return;
        }

        Logger.info(this, objectPrettyPrinter.prettyPrint(optionalLoggerDefinitionVersion.get()));
    }

    @Override
    public String getCommandString() {
        return GET_LATEST_LOGGER_DEFINITION;
    }

    @Override
    public String getHelp() {
        return "Gets the latest logger definition version for a group.";
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
