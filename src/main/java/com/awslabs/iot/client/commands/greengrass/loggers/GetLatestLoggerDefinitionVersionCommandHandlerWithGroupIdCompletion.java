package com.awslabs.iot.client.commands.greengrass.loggers;

import com.amazonaws.services.greengrass.model.GetLoggerDefinitionVersionResult;
import com.amazonaws.services.greengrass.model.VersionInformation;
import com.awslabs.general.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.commands.greengrass.GreengrassGroupCommandHandlerWithGroupIdCompletion;
import com.awslabs.iot.client.commands.greengrass.completers.GreengrassGroupIdCompleter;
import com.awslabs.iot.client.helpers.json.interfaces.ObjectPrettyPrinter;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.helpers.interfaces.V1GreengrassHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

public class GetLatestLoggerDefinitionVersionCommandHandlerWithGroupIdCompletion implements GreengrassGroupCommandHandlerWithGroupIdCompletion {
    private static final String GET_LATEST_LOGGER_DEFINITION = "get-latest-logger-definition";
    private static final int GROUP_ID_POSITION = 0;
    private static final Logger log = LoggerFactory.getLogger(GetLatestLoggerDefinitionVersionCommandHandlerWithGroupIdCompletion.class);
    @Inject
    V1GreengrassHelper greengrassHelper;
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

        Optional<VersionInformation> optionalVersionInformation = greengrassHelper.getLatestGroupVersion(groupId);

        if (!optionalVersionInformation.isPresent()) {
            return;
        }

        VersionInformation versionInformation = optionalVersionInformation.get();

        GetLoggerDefinitionVersionResult loggerDefinitionVersionResult = greengrassHelper.getLoggerDefinitionVersion(groupId, versionInformation);

        log.info(objectPrettyPrinter.prettyPrint(loggerDefinitionVersionResult));
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
