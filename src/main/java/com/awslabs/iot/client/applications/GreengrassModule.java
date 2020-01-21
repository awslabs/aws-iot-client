package com.awslabs.iot.client.applications;

import com.awslabs.iot.client.commands.connectors.GetLatestConnectorDefinitionVersionCommandHandlerWithGroupIdCompletion;
import com.awslabs.iot.client.commands.greengrass.cores.GetLatestCoreDefinitionVersionCommandHandlerWithGroupIdCompletion;
import com.awslabs.iot.client.commands.greengrass.devices.GetLatestDeviceDefinitionVersionCommandHandlerWithGroupIdCompletion;
import com.awslabs.iot.client.commands.greengrass.functions.GetLatestFunctionDefinitionVersionCommandHandlerWithGroupIdCompletion;
import com.awslabs.iot.client.commands.greengrass.generic.CleanupCommandHandler;
import com.awslabs.iot.client.commands.greengrass.groups.*;
import com.awslabs.iot.client.commands.greengrass.loggers.GetLatestLoggerDefinitionVersionCommandHandlerWithGroupIdCompletion;
import com.awslabs.iot.client.commands.greengrass.resources.GetLatestResourceDefinitionVersionCommandHandlerWithGroupIdCompletion;
import com.awslabs.iot.client.commands.greengrass.subscriptions.GetLatestSubscriptionDefinitionVersionCommandHandlerWithGroupIdCompletion;
import com.awslabs.iot.client.commands.interfaces.CommandHandler;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

class GreengrassModule extends AbstractModule {
    @Override
    protected void configure() {
        Multibinder<CommandHandler> commandHandlerMultibinder = Multibinder.newSetBinder(binder(), CommandHandler.class);
        commandHandlerMultibinder.addBinding().to(ListGroupsCommandHandler.class);
        commandHandlerMultibinder.addBinding().to(ListGroupVersionsCommandHandlerWithGroupIdCompletion.class);
        commandHandlerMultibinder.addBinding().to(GetLatestGroupVersionCommandHandlerWithGroupIdCompletion.class);
        commandHandlerMultibinder.addBinding().to(ListDeploymentsCommandHandlerWithGroupIdCompletion.class);
        commandHandlerMultibinder.addBinding().to(GetLatestDeploymentCommandHandlerWithGroupIdCompletion.class);
        commandHandlerMultibinder.addBinding().to(GetDeploymentStatusCommandHandlerWithGroupIdAndDeploymentIdCompletion.class);
        commandHandlerMultibinder.addBinding().to(GetLatestDeploymentStatusCommandHandlerWithGroupIdCompletion.class);
        commandHandlerMultibinder.addBinding().to(GetLatestFunctionDefinitionVersionCommandHandlerWithGroupIdCompletion.class);
        commandHandlerMultibinder.addBinding().to(GetLatestCoreDefinitionVersionCommandHandlerWithGroupIdCompletion.class);
        commandHandlerMultibinder.addBinding().to(GetLatestSubscriptionDefinitionVersionCommandHandlerWithGroupIdCompletion.class);
        commandHandlerMultibinder.addBinding().to(GetLatestDeviceDefinitionVersionCommandHandlerWithGroupIdCompletion.class);
        commandHandlerMultibinder.addBinding().to(GetLatestLoggerDefinitionVersionCommandHandlerWithGroupIdCompletion.class);
        commandHandlerMultibinder.addBinding().to(GetLatestResourceDefinitionVersionCommandHandlerWithGroupIdCompletion.class);
        commandHandlerMultibinder.addBinding().to(GetLatestConnectorDefinitionVersionCommandHandlerWithGroupIdCompletion.class);
        commandHandlerMultibinder.addBinding().to(DeleteGroupCommandHandlerWithGroupIdCompletion.class);
        commandHandlerMultibinder.addBinding().to(DeleteAllGroupsCommandHandler.class);
        commandHandlerMultibinder.addBinding().to(CleanupCommandHandler.class);
        commandHandlerMultibinder.addBinding().to(DeleteAllCoreDefinitionsCommandHandler.class);
        commandHandlerMultibinder.addBinding().to(DeleteAllFunctionDefinitionsCommandHandler.class);
        commandHandlerMultibinder.addBinding().to(DeleteAllSubscriptionDefinitionsCommandHandler.class);
        commandHandlerMultibinder.addBinding().to(DeleteAllDeviceDefinitionsCommandHandler.class);
        commandHandlerMultibinder.addBinding().to(DeleteAllLoggerDefinitionsCommandHandler.class);
        commandHandlerMultibinder.addBinding().to(DeleteAllConnectorDefinitionsCommandHandler.class);
    }
}
