package com.awslabs.iot.client.commands.greengrass.groups;

import com.awslabs.general.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.commands.greengrass.GreengrassCommandHandler;
import com.awslabs.iot.client.helpers.progressbar.ProgressBarHelper;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.client.streams.interfaces.UsesStream;
import com.awslabs.iot.helpers.interfaces.V2GreengrassHelper;
import com.awslabs.iot.helpers.interfaces.V2IotHelper;
import com.awslabs.lambda.helpers.interfaces.V2LambdaHelper;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.greengrass.model.GroupInformation;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.DeleteFunctionRequest;
import software.amazon.awssdk.services.lambda.model.FunctionConfiguration;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DeleteAllGreengrassLambdaFunctionsCommandHandler implements GreengrassCommandHandler, UsesStream<FunctionConfiguration> {
    private static final String DELETE_ALL_LAMBDA_FUNCTIONS = "delete-all-lambda-functions";
    private static final Logger log = LoggerFactory.getLogger(DeleteAllGreengrassLambdaFunctionsCommandHandler.class);
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
    V2LambdaHelper v2LambdaHelper;
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

    public IoHelper getIoHelper() {
        return this.ioHelper;
    }

    @Override
    public Stream<FunctionConfiguration> getStream() {
        List<String> nonImmutableGroupNameList = v2GreengrassHelper.getNonImmutableGroups()
                .map(GroupInformation::name)
                .collect(Collectors.toList());

        return v2LambdaHelper.getAllFunctionConfigurations()
                // Only get functions that start with the group name
                .filter(functionConfiguration -> nonImmutableGroupNameList.stream().anyMatch(functionConfiguration.functionName()::startsWith));
    }
}
