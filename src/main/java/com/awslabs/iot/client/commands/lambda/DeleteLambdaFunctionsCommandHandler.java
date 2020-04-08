package com.awslabs.iot.client.commands.lambda;

import com.amazonaws.services.lambda.AWSLambdaClient;
import com.amazonaws.services.lambda.model.DeleteFunctionRequest;
import com.amazonaws.services.lambda.model.FunctionConfiguration;
import com.amazonaws.services.lambda.model.ListFunctionsRequest;
import com.awslabs.general.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.commands.interfaces.CommandHandler;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.resultsiterator.v1.implementations.V1ResultsIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.stream.Stream;

public class DeleteLambdaFunctionsCommandHandler implements CommandHandler {
    private static final String LAMBDADELETE = "lambda-delete";
    private static final Logger log = LoggerFactory.getLogger(DeleteLambdaFunctionsCommandHandler.class);
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;
    @Inject
    AWSLambdaClient awsLambdaClient;

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

        ListFunctionsRequest listFunctionsRequest = new ListFunctionsRequest();

        Stream<FunctionConfiguration> functionConfigurations = new V1ResultsIterator<FunctionConfiguration>(awsLambdaClient, listFunctionsRequest).stream();

        functionConfigurations
                .filter(functionConfiguration -> functionConfiguration.getFunctionName().matches(name))
                .forEach(this::deleteFunction);
    }

    private void deleteFunction(FunctionConfiguration functionConfiguration) {
        String name = functionConfiguration.getFunctionName();
        log.info("Deleting function: " + name);

        DeleteFunctionRequest deleteFunctionRequest = new DeleteFunctionRequest()
                .withFunctionName(name);

        awsLambdaClient.deleteFunction(deleteFunctionRequest);
    }

    public ParameterExtractor getParameterExtractor() {
        return this.parameterExtractor;
    }

    public IoHelper getIoHelper() {
        return this.ioHelper;
    }
}
