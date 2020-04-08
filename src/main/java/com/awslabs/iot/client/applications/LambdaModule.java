package com.awslabs.iot.client.applications;

import com.amazonaws.services.lambda.AWSLambdaClient;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.awslabs.iot.client.commands.interfaces.CommandHandler;
import com.awslabs.iot.client.commands.lambda.DeleteLambdaFunctionsCommandHandler;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.ElementsIntoSet;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Module
public class LambdaModule {
    @Provides
    public AWSLambdaClient awsLambdaClient() {
        return (AWSLambdaClient) AWSLambdaClientBuilder.defaultClient();
    }

    @Provides
    @ElementsIntoSet
    public Set<CommandHandler> commandHandlerSet(DeleteLambdaFunctionsCommandHandler deleteLambdaFunctionsCommandHandler) {
        return new HashSet<>(Arrays.asList(deleteLambdaFunctionsCommandHandler));
    }
}
