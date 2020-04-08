package com.awslabs.iot.client.commands.greengrass.groups;

import com.awslabs.general.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.commands.greengrass.GreengrassCommandHandler;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.data.GreengrassGroupId;
import com.awslabs.iot.helpers.interfaces.V2GreengrassHelper;
import com.awslabs.iot.helpers.interfaces.V2IotHelper;
import com.awslabs.resultsiterator.v2.implementations.V2ResultsIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.greengrass.model.GroupInformation;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.DeleteFunctionRequest;
import software.amazon.awssdk.services.lambda.model.FunctionConfiguration;
import software.amazon.awssdk.services.lambda.model.ListFunctionsRequest;

import javax.inject.Inject;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DeleteAllLambdaFunctionsCommandHandler implements GreengrassCommandHandler {
    private static final String DELETE_ALL_LAMBDA_FUNCTIONS = "delete-all-lambda-functions";
    private static final Logger log = LoggerFactory.getLogger(DeleteAllLambdaFunctionsCommandHandler.class);
    @Inject
    V2GreengrassHelper v2GreengrassHelper;
    @Inject
    V2IotHelper v2IotHelper;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;
    @Inject
    LambdaClient lambdaClient;

    @Inject
    public DeleteAllLambdaFunctionsCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        List<String> nonImmutableGroupNames = v2GreengrassHelper.getGroups()
                // Sort the groups by ID so we can get a general sense of how far along we are in the process of deleting them
                .sorted(Comparator.comparing(GroupInformation::id))
                // Don't include immutable groups
                .filter(groupInformation -> !v2GreengrassHelper.isGroupImmutable(groupInformation))
                .map(GroupInformation::name)
                .collect(Collectors.toList());

        log.info("Listing all Lambda functions...");

        List<FunctionConfiguration> functionConfigurations = new V2ResultsIterator<FunctionConfiguration>(lambdaClient, ListFunctionsRequest.class)
                .stream().collect(Collectors.toList());

        log.info(String.join(" ", "Found", String.valueOf(functionConfigurations.size()), "Lambda function(s)"));

        nonImmutableGroupNames
                // Delete each group's Lambda functions
                .forEach(groupName -> deleteGroupLambdas(functionConfigurations, groupName));
    }

    private Optional<GroupInformation> getGroupInformationFromId(List<GroupInformation> groupInformationList, GreengrassGroupId greengrassGroupId) {
        return groupInformationList.stream()
                .filter(groupInformation -> greengrassGroupId.getGroupId().equals(groupInformation.id()))
                .findFirst();
    }

    private void deleteGroupLambdas(List<FunctionConfiguration> functionConfigurations, String groupName) {
        String pattern = String.join("", groupName, ".*");

        List<FunctionConfiguration> functionsToDelete = functionConfigurations.stream()
                .filter(functionConfiguration -> functionConfiguration.functionName().matches(pattern))
                .collect(Collectors.toList());

        if (functionsToDelete.isEmpty()) {
            log.info(String.join("", "No functions to delete for [", groupName, "]"));
            return;
        }

        functionsToDelete
                .forEach(this::deleteFunction);

        log.info(String.join("", "Deleted functions for group [", groupName, "]"));
    }

    private void deleteFunction(FunctionConfiguration functionConfiguration) {
        String name = functionConfiguration.functionName();

        log.info(String.join(" ", "Deleting function:", name));

        DeleteFunctionRequest deleteFunctionRequest = DeleteFunctionRequest.builder()
                .functionName(name)
                .build();

        lambdaClient.deleteFunction(deleteFunctionRequest);
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
