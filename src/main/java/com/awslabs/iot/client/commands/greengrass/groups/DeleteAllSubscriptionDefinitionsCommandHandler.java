package com.awslabs.iot.client.commands.greengrass.groups;

import com.amazonaws.services.greengrass.model.DefinitionInformation;
import com.awslabs.aws.iot.resultsiterator.helpers.interfaces.IoHelper;
import com.awslabs.aws.iot.resultsiterator.helpers.v1.interfaces.V1GreengrassHelper;
import com.awslabs.iot.client.commands.greengrass.GreengrassCommandHandler;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class DeleteAllSubscriptionDefinitionsCommandHandler implements GreengrassCommandHandler {
    private static final String DELETE_SUBSCRIPTION_DEFINITIONS = "delete-all-subscription-definitions";
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(DeleteAllSubscriptionDefinitionsCommandHandler.class);
    @Inject
    V1GreengrassHelper greengrassHelper;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;

    @Inject
    public DeleteAllSubscriptionDefinitionsCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        List<DefinitionInformation> nonImmutableSubscriptionDefinitionVersion = greengrassHelper.listNonImmutableSubscriptionDefinitionInformation();

        for (DefinitionInformation definitionInformation : nonImmutableSubscriptionDefinitionVersion) {
            greengrassHelper.deleteSubscriptionDefinition(definitionInformation);

            log.info("Deleted subscription definition [" + definitionInformation + "]");
        }
    }

    @Override
    public String getCommandString() {
        return DELETE_SUBSCRIPTION_DEFINITIONS;
    }

    @Override
    public String getHelp() {
        return "Deletes all Greengrass subscription definitions.";
    }

    @Override
    public int requiredParameters() {
        return 0;
    }

    public ParameterExtractor getParameterExtractor() {
        return this.parameterExtractor;
    }

    public IoHelper getIoHelper() {
        return this.ioHelper;
    }
}
