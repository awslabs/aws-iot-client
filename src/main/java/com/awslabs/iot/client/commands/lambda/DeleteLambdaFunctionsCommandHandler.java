package com.awslabs.iot.client.commands.lambda;

import com.awslabs.general.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.commands.interfaces.CommandHandler;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.resultsiterator.v2.implementations.V2ResultsIterator;
import com.jcabi.log.Logger;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.DeleteFunctionRequest;
import software.amazon.awssdk.services.lambda.model.FunctionConfiguration;
import software.amazon.awssdk.services.lambda.model.ListFunctionsRequest;

import javax.inject.Inject;
import java.util.stream.Stream;

public class DeleteLambdaFunctionsCommandHandler implements CommandHandler {
    private static final String LAMBDADELETE = "lambda-delete";
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;
    @Inject
    LambdaClient lambdaClient;

    @Inject
    public DeleteLambdaFunctionsCommandHandler() {
    }

    @Override
    public int requiredParameters() {
        return 1;
    }

    @Override
    public String getCommandString() {
        return LAMBDADELETE;
    }

    @Override
    public String getHelp() {
        return "Deletes Lambda functions.  First parameter is the function name.  Wildcards are supported.";
    }

    @Override
    public void innerHandle(String input) {
        String name = getParameterExtractor().getParameters(input).get(0);

        ListFunctionsRequest listFunctionsRequest = ListFunctionsRequest.builder().build();

        Stream<FunctionConfiguration> functionConfigurations = new V2ResultsIterator<FunctionConfiguration>(lambdaClient, listFunctionsRequest).stream();

        functionConfigurations
                .filter(functionConfiguration -> functionConfiguration.functionName().matches(name))
                .forEach(this::deleteFunction);
    }

    private void deleteFunction(FunctionConfiguration functionConfiguration) {
        String name = functionConfiguration.functionName();
        Logger.info(this, String.join(" ", "Deleting function:", name));

        DeleteFunctionRequest deleteFunctionRequest = DeleteFunctionRequest.builder()
                .functionName(name)
                .build();

        lambdaClient.deleteFunction(deleteFunctionRequest);
    }

    public ParameterExtractor getParameterExtractor() {
        return this.parameterExtractor;
    }

    public IoHelper getIoHelper() {
        return this.ioHelper;
    }
}
