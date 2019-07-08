package com.awslabs.iot.client.commands.greengrass.groups;

import com.amazonaws.services.greengrass.model.DefinitionInformation;
import com.awslabs.iot.client.commands.greengrass.GreengrassCommandHandler;
import com.awslabs.iot.client.helpers.greengrass.interfaces.GreengrassHelper;
import com.awslabs.iot.client.helpers.io.interfaces.IOHelper;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class DeleteAllFunctionDefinitionsCommandHandler implements GreengrassCommandHandler {
    private static final String DELETE_FUNCTION_DEFINITIONS = "delete-all-function-definitions";
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(DeleteAllFunctionDefinitionsCommandHandler.class);
    @Inject
    GreengrassHelper greengrassHelper;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IOHelper ioHelper;

    @Inject
    public DeleteAllFunctionDefinitionsCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        List<DefinitionInformation> nonImmutableFunctionDefinitionInformation = greengrassHelper.listNonImmutableFunctionDefinitionInformation();

        for (DefinitionInformation definitionInformation : nonImmutableFunctionDefinitionInformation) {
            greengrassHelper.deleteFunctionDefinition(definitionInformation);

            log.info("Deleted function definition [" + definitionInformation + "]");
        }
    }

    @Override
    public String getCommandString() {
        return DELETE_FUNCTION_DEFINITIONS;
    }

    @Override
    public String getHelp() {
        return "Deletes all Greengrass function definitions.";
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
