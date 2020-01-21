package com.awslabs.iot.client.commands.greengrass.groups;

import com.amazonaws.services.greengrass.model.VersionInformation;
import com.awslabs.aws.iot.resultsiterator.helpers.interfaces.IoHelper;
import com.awslabs.aws.iot.resultsiterator.helpers.v1.interfaces.V1GreengrassHelper;
import com.awslabs.iot.client.commands.greengrass.GreengrassGroupCommandHandlerWithGroupIdCompletion;
import com.awslabs.iot.client.commands.greengrass.completers.GreengrassGroupIdCompleter;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class ListGroupVersionsCommandHandlerWithGroupIdCompletion implements GreengrassGroupCommandHandlerWithGroupIdCompletion {
    private static final String LIST_GROUP_VERSIONS = "list-group-versions";
    private static final int GROUP_ID_POSITION = 0;
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ListGroupVersionsCommandHandlerWithGroupIdCompletion.class);
    @Inject
    V1GreengrassHelper greengrassHelper;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;
    @Inject
    GreengrassGroupIdCompleter greengrassGroupIdCompleter;

    @Inject
    public ListGroupVersionsCommandHandlerWithGroupIdCompletion() {
    }

    @Override
    public void innerHandle(String input) {
        List<String> parameters = parameterExtractor.getParameters(input);

        String groupId = parameters.get(GROUP_ID_POSITION);

        List<VersionInformation> versionInformationList = greengrassHelper.listGroupVersions(groupId);

        for (VersionInformation versionInformation : versionInformationList) {
            log.info("  [" + versionInformation.getVersion() + " - " + versionInformation.getCreationTimestamp() + "]");
        }
    }

    @Override
    public String getCommandString() {
        return LIST_GROUP_VERSIONS;
    }

    @Override
    public String getHelp() {
        return "Lists all Greengrass group versions for a group.";
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
