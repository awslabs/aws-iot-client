package com.awslabs.iot.client.commands.greengrass.loggers;


import com.awslabs.iot.client.commands.greengrass.GreengrassGroupCommandHandlerWithGroupIdCompletion;
import com.awslabs.iot.client.commands.greengrass.completers.GreengrassGroupIdCompleter;
import com.awslabs.iot.client.helpers.json.interfaces.ObjectPrettyPrinter;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.data.ImmutableGreengrassGroupId;
import com.awslabs.iot.helpers.interfaces.GreengrassV1Helper;
import com.jcabi.log.Logger;
import io.vavr.collection.List;
import io.vavr.control.Option;
import software.amazon.awssdk.services.greengrass.model.GroupInformation;
import software.amazon.awssdk.services.greengrass.model.LoggerDefinitionVersion;

import javax.inject.Inject;

public class GetLatestLoggerDefinitionVersionCommandHandlerWithGroupIdCompletion implements GreengrassGroupCommandHandlerWithGroupIdCompletion {
    private static final String GET_LATEST_LOGGER_DEFINITION = "get-latest-logger-definition";
    private static final int GROUP_ID_POSITION = 0;
    @Inject
    GreengrassV1Helper greengrassV1Helper;
    @Inject
    ObjectPrettyPrinter objectPrettyPrinter;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    GreengrassGroupIdCompleter greengrassGroupIdCompleter;

    @Inject
    public GetLatestLoggerDefinitionVersionCommandHandlerWithGroupIdCompletion() {
    }

    @Override
    public void innerHandle(String input) {
        List<String> parameters = parameterExtractor.getParameters(input);

        String groupId = parameters.get(GROUP_ID_POSITION);

        Option<GroupInformation> optionalGroupInformation = greengrassV1Helper.getGroupInformation(ImmutableGreengrassGroupId.builder().groupId(groupId).build());

        if (optionalGroupInformation.isEmpty()) {
            return;
        }

        GroupInformation groupInformation = optionalGroupInformation.get();

        Option<LoggerDefinitionVersion> optionalLoggerDefinitionVersion = greengrassV1Helper.getLoggerDefinitionVersion(groupInformation);

        if (optionalLoggerDefinitionVersion.isEmpty()) {
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

    public GreengrassGroupIdCompleter getGreengrassGroupIdCompleter() {
        return this.greengrassGroupIdCompleter;
    }
}
