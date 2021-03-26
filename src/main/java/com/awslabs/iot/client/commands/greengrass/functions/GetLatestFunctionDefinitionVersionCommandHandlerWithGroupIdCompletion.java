package com.awslabs.iot.client.commands.greengrass.functions;


import com.awslabs.iot.client.commands.greengrass.GreengrassGroupCommandHandlerWithGroupIdCompletion;
import com.awslabs.iot.client.commands.greengrass.completers.GreengrassGroupIdCompleter;
import com.awslabs.iot.client.helpers.json.interfaces.ObjectPrettyPrinter;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.data.GreengrassGroupId;
import com.awslabs.iot.data.ImmutableGreengrassGroupId;
import com.awslabs.iot.helpers.interfaces.GreengrassV1Helper;
import com.jcabi.log.Logger;
import io.vavr.collection.List;
import io.vavr.control.Option;
import software.amazon.awssdk.services.greengrass.model.FunctionDefinitionVersion;

import javax.inject.Inject;

public class GetLatestFunctionDefinitionVersionCommandHandlerWithGroupIdCompletion implements GreengrassGroupCommandHandlerWithGroupIdCompletion {
    private static final String GET_LATEST_FUNCTION_DEFINITION = "get-latest-function-definition";
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
    public GetLatestFunctionDefinitionVersionCommandHandlerWithGroupIdCompletion() {
    }

    @Override
    public void innerHandle(String input) {
        List<String> parameters = parameterExtractor.getParameters(input);

        GreengrassGroupId groupId = ImmutableGreengrassGroupId.builder().groupId(parameters.get(GROUP_ID_POSITION)).build();

        Option<FunctionDefinitionVersion> optionalFunctionDefinitionVersion = greengrassV1Helper.getGroupInformation(groupId)
                .flatMap(greengrassV1Helper::getFunctionDefinitionVersion);

        if (optionalFunctionDefinitionVersion.isEmpty()) {
            Logger.info(this, "No function definition found");
            return;
        }

        Logger.info(this, objectPrettyPrinter.prettyPrint(optionalFunctionDefinitionVersion.get()));
    }

    @Override
    public String getCommandString() {
        return GET_LATEST_FUNCTION_DEFINITION;
    }

    @Override
    public String getHelp() {
        return "Gets the latest function definition version for a group.";
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
