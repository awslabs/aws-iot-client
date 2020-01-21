package com.awslabs.iot.client.commands.greengrass.groups;

import com.amazonaws.services.greengrass.model.GroupInformation;
import com.awslabs.aws.iot.resultsiterator.helpers.interfaces.IoHelper;
import com.awslabs.aws.iot.resultsiterator.helpers.v1.interfaces.V1GreengrassHelper;
import com.awslabs.iot.client.commands.greengrass.GreengrassCommandHandler;
import com.awslabs.iot.client.commands.lambda.DeleteLambdaFunctionsCommandHandler;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class DeleteAllLambdaFunctionsCommandHandler implements GreengrassCommandHandler {
    private static final String DELETE_ALL_LAMBDA_FUNCTIONS = "delete-all-lambda-functions";
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(DeleteAllLambdaFunctionsCommandHandler.class);
    @Inject
    V1GreengrassHelper greengrassHelper;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;
    @Inject
    DeleteLambdaFunctionsCommandHandler deleteLambdaFunctionsCommandHandler;

    @Inject
    public DeleteAllLambdaFunctionsCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        List<String> groupNames = greengrassHelper.listGroups().stream().map(GroupInformation::getName).collect(Collectors.toList());

        for (String groupName : groupNames) {
            if (greengrassHelper.isGroupImmutable(groupName)) {
                log.info("Skipping group [" + groupName + "] because it is an immutable group");
                continue;
            }

            deleteLambdaFunctionsCommandHandler.innerHandle(String.join(" ", "_", groupName + ".*"));

            log.info("Deleted functions for group [" + groupName + "]");
        }
    }

    @Override
    public String getCommandString() {
        return DELETE_ALL_LAMBDA_FUNCTIONS;
    }

    @Override
    public String getHelp() {
        return "Deletes all Lambda functions associated with a Greengrass group.";
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
