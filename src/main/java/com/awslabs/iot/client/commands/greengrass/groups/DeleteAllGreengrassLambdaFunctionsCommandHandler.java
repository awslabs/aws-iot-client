package com.awslabs.iot.client.commands.greengrass.groups;


import com.awslabs.iot.client.commands.greengrass.GreengrassCommandHandler;
import com.awslabs.iot.client.helpers.progressbar.ProgressBarHelper;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.client.streams.interfaces.UsesStream;
import com.awslabs.iot.helpers.interfaces.GreengrassV1Helper;
import com.awslabs.iot.helpers.interfaces.IotHelper;
import com.awslabs.lambda.helpers.interfaces.LambdaHelper;
import io.vavr.collection.List;
import io.vavr.collection.Stream;
import io.vavr.control.Try;
import software.amazon.awssdk.services.greengrass.model.GroupInformation;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.DeleteFunctionRequest;
import software.amazon.awssdk.services.lambda.model.FunctionConfiguration;

import javax.inject.Inject;

public class DeleteAllGreengrassLambdaFunctionsCommandHandler implements GreengrassCommandHandler, UsesStream<FunctionConfiguration> {
    private static final String DELETE_ALL_LAMBDA_FUNCTIONS = "delete-all-lambda-functions";
    @Inject
    GreengrassV1Helper greengrassV1Helper;
    @Inject
    IotHelper iotHelper;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    LambdaClient lambdaClient;
    @Inject
    LambdaHelper lambdaHelper;
    @Inject
    ProgressBarHelper progressBarHelper;

    @Inject
    public DeleteAllGreengrassLambdaFunctionsCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        Try.withResources(() -> progressBarHelper.start("Delete all Greengrass Lambda functions", this))
                .of(progressBar -> run());
    }

    private Void run() {
        getStream()
                .peek(certificate -> progressBarHelper.next())
                // Extract just the name
                .map(FunctionConfiguration::functionName)
                // Delete each group's Lambda functions and rethrow all exceptions
                .forEach(this::deleteFunction);

        return null;
    }

    private void deleteFunction(String functionName) {
        DeleteFunctionRequest deleteFunctionRequest = DeleteFunctionRequest.builder()
                .functionName(functionName)
                .build();

        lambdaClient.deleteFunction(deleteFunctionRequest);
    }

    @Override
    public String getCommandString() {
        return DELETE_ALL_LAMBDA_FUNCTIONS;
    }

    @Override
    public String getHelp() {
        return "Deletes all Lambda functions associated with any Greengrass groups.";
    }

    @Override
    public int requiredParameters() {
        return 0;
    }

    public ParameterExtractor getParameterExtractor() {
        return this.parameterExtractor;
    }

    @Override
    public Stream<FunctionConfiguration> getStream() {
        List<String> nonImmutableGroupNameList = greengrassV1Helper.getNonImmutableGroups()
                .map(GroupInformation::name)
                .toList();

        return lambdaHelper.getAllFunctionConfigurations()
                // Only get functions that start with the group name
                .filter(functionConfiguration -> nonImmutableGroupNameList.filter(functionConfiguration.functionName()::startsWith).nonEmpty());
    }
}
