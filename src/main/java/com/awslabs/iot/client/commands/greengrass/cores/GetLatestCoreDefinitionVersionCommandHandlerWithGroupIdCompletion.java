package com.awslabs.iot.client.commands.greengrass.cores;

import com.amazonaws.services.greengrass.model.GetCoreDefinitionVersionResult;
import com.amazonaws.services.greengrass.model.VersionInformation;
import com.awslabs.aws.iot.resultsiterator.helpers.interfaces.IoHelper;
import com.awslabs.aws.iot.resultsiterator.helpers.v1.interfaces.V1GreengrassHelper;
import com.awslabs.iot.client.commands.greengrass.GreengrassGroupCommandHandlerWithGroupIdCompletion;
import com.awslabs.iot.client.commands.greengrass.completers.GreengrassGroupIdCompleter;
import com.awslabs.iot.client.helpers.json.interfaces.ObjectPrettyPrinter;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class GetLatestCoreDefinitionVersionCommandHandlerWithGroupIdCompletion implements GreengrassGroupCommandHandlerWithGroupIdCompletion {
    private static final String GET_LATEST_CORE_DEFINITION = "get-latest-core-definition";
    private static final int GROUP_ID_POSITION = 0;
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(GetLatestCoreDefinitionVersionCommandHandlerWithGroupIdCompletion.class);
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
    public GetLatestCoreDefinitionVersionCommandHandlerWithGroupIdCompletion() {
    }

    @Override
    public void innerHandle(String input) {
        List<String> parameters = parameterExtractor.getParameters(input);

        String groupId = parameters.get(GROUP_ID_POSITION);

        VersionInformation versionInformation = greengrassHelper.getLatestGroupVersion(groupId);

        if (versionInformation == null) {
            return;
        }

        GetCoreDefinitionVersionResult coreDefinitionVersionResult = greengrassHelper.getCoreDefinitionVersion(groupId, versionInformation);

        log.info(objectPrettyPrinter.prettyPrint(coreDefinitionVersionResult));
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
