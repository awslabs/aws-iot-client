package com.awslabs.iot.client.commands.greengrass.groups;

import com.amazonaws.services.greengrass.model.DefinitionInformation;
import com.awslabs.iot.client.commands.greengrass.GreengrassCommandHandler;
import com.awslabs.iot.client.helpers.greengrass.interfaces.GreengrassHelper;
import com.awslabs.iot.client.helpers.io.interfaces.IOHelper;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class DeleteAllDeviceDefinitionsCommandHandler implements GreengrassCommandHandler {
    private static final String DELETE_DEVICE_DEFINITIONS = "delete-all-device-definitions";
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(DeleteAllDeviceDefinitionsCommandHandler.class);
    @Inject
    GreengrassHelper greengrassHelper;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IOHelper ioHelper;

    @Inject
    public DeleteAllDeviceDefinitionsCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        List<DefinitionInformation> nonImmutableDeviceDefinitionInformation = greengrassHelper.listNonImmutableDeviceDefinitionInformation();

        for (DefinitionInformation definitionInformation : nonImmutableDeviceDefinitionInformation) {
            greengrassHelper.deleteDeviceDefinition(definitionInformation);

            log.info("Deleted device definition [" + definitionInformation + "]");
        }
    }

    @Override
    public String getCommandString() {
        return DELETE_DEVICE_DEFINITIONS;
    }

    @Override
    public String getHelp() {
        return "Deletes all Greengrass device definitions.";
    }

    @Override
    public int requiredParameters() {
        return 0;
    }

    public ParameterExtractor getParameterExtractor() {
        return this.parameterExtractor;
    }

    public IOHelper getIoHelper() {
        return this.ioHelper;
    }
}