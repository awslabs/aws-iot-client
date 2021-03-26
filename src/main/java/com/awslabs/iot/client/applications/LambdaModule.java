package com.awslabs.iot.client.applications;

import com.awslabs.iot.client.commands.interfaces.CommandHandler;
import com.awslabs.iot.client.commands.lambda.DeleteLambdaFunctionsCommandHandler;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.ElementsIntoSet;
import io.vavr.collection.HashSet;

import java.util.Set;

@Module
public class LambdaModule {
    @Provides
    @ElementsIntoSet
    public Set<CommandHandler> commandHandlerSet(DeleteLambdaFunctionsCommandHandler deleteLambdaFunctionsCommandHandler) {
        return HashSet.<CommandHandler>of(
                deleteLambdaFunctionsCommandHandler)
                .toJavaSet();
    }
}
