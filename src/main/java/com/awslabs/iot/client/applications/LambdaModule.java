package com.awslabs.iot.client.applications;

import com.awslabs.iot.client.commands.interfaces.CommandHandler;
import com.awslabs.iot.client.commands.lambda.DeleteLambdaFunctionsCommandHandler;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

class LambdaModule extends AbstractModule {
    @Override
    protected void configure() {
        Multibinder<CommandHandler> commandHandlerMultibinder = Multibinder.newSetBinder(binder(), CommandHandler.class);
        commandHandlerMultibinder.addBinding().to(DeleteLambdaFunctionsCommandHandler.class);
    }
}
