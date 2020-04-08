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
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.ElementsIntoSet;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Module
public class GreengrassModule {
    @Provides
    @ElementsIntoSet
    public Set<CommandHandler> commandHandlerSet(ListGroupsCommandHandler listGroupsCommandHandler,
                                                 ListGroupVersionsCommandHandlerWithGroupIdCompletion listGroupVersionsCommandHandlerWithGroupIdCompletion,
                                                 GetLatestGroupVersionCommandHandlerWithGroupIdCompletion getLatestGroupVersionCommandHandlerWithGroupIdCompletion,
                                                 ListDeploymentsCommandHandlerWithGroupIdCompletion listDeploymentsCommandHandlerWithGroupIdCompletion,
                                                 GetLatestDeploymentCommandHandlerWithGroupIdCompletion getLatestDeploymentCommandHandlerWithGroupIdCompletion,
                                                 GetDeploymentStatusCommandHandlerWithGroupIdAndDeploymentIdCompletion getDeploymentStatusCommandHandlerWithGroupIdAndDeploymentIdCompletion,
                                                 GetLatestDeploymentStatusCommandHandlerWithGroupIdCompletion getLatestDeploymentStatusCommandHandlerWithGroupIdCompletion,
                                                 GetLatestFunctionDefinitionVersionCommandHandlerWithGroupIdCompletion getLatestFunctionDefinitionVersionCommandHandlerWithGroupIdCompletion,
                                                 GetLatestCoreDefinitionVersionCommandHandlerWithGroupIdCompletion getLatestCoreDefinitionVersionCommandHandlerWithGroupIdCompletion,
                                                 GetLatestSubscriptionDefinitionVersionCommandHandlerWithGroupIdCompletion getLatestSubscriptionDefinitionVersionCommandHandlerWithGroupIdCompletion,
                                                 GetLatestDeviceDefinitionVersionCommandHandlerWithGroupIdCompletion getLatestDeviceDefinitionVersionCommandHandlerWithGroupIdCompletion,
                                                 GetLatestLoggerDefinitionVersionCommandHandlerWithGroupIdCompletion getLatestLoggerDefinitionVersionCommandHandlerWithGroupIdCompletion,
                                                 GetLatestResourceDefinitionVersionCommandHandlerWithGroupIdCompletion getLatestResourceDefinitionVersionCommandHandlerWithGroupIdCompletion,
                                                 GetLatestConnectorDefinitionVersionCommandHandlerWithGroupIdCompletion getLatestConnectorDefinitionVersionCommandHandlerWithGroupIdCompletion,
                                                 DeleteGroupCommandHandlerWithGroupIdCompletion deleteGroupCommandHandlerWithGroupIdCompletion,
                                                 DeleteAllGroupsCommandHandler deleteAllGroupsCommandHandler,
                                                 CleanupCommandHandler cleanupCommandHandler,
                                                 DeleteAllCoreDefinitionsCommandHandler deleteAllCoreDefinitionsCommandHandler,
                                                 DeleteAllFunctionDefinitionsCommandHandler deleteAllFunctionDefinitionsCommandHandler,
                                                 DeleteAllSubscriptionDefinitionsCommandHandler deleteAllSubscriptionDefinitionsCommandHandler,
                                                 DeleteAllDeviceDefinitionsCommandHandler deleteAllDeviceDefinitionsCommandHandler,
                                                 DeleteAllLoggerDefinitionsCommandHandler deleteAllLoggerDefinitionsCommandHandler,
                                                 DeleteAllConnectorDefinitionsCommandHandler deleteAllConnectorDefinitionsCommandHandler) {
        return new HashSet<>(Arrays.asList(
                listGroupsCommandHandler,
                listGroupVersionsCommandHandlerWithGroupIdCompletion,
                getLatestGroupVersionCommandHandlerWithGroupIdCompletion,
                listDeploymentsCommandHandlerWithGroupIdCompletion,
                getLatestDeploymentCommandHandlerWithGroupIdCompletion,
                getDeploymentStatusCommandHandlerWithGroupIdAndDeploymentIdCompletion,
                getLatestDeploymentStatusCommandHandlerWithGroupIdCompletion,
                getLatestFunctionDefinitionVersionCommandHandlerWithGroupIdCompletion,
                getLatestCoreDefinitionVersionCommandHandlerWithGroupIdCompletion,
                getLatestSubscriptionDefinitionVersionCommandHandlerWithGroupIdCompletion,
                getLatestDeviceDefinitionVersionCommandHandlerWithGroupIdCompletion,
                getLatestLoggerDefinitionVersionCommandHandlerWithGroupIdCompletion,
                getLatestResourceDefinitionVersionCommandHandlerWithGroupIdCompletion,
                getLatestConnectorDefinitionVersionCommandHandlerWithGroupIdCompletion,
                deleteGroupCommandHandlerWithGroupIdCompletion,
                deleteAllGroupsCommandHandler,
                cleanupCommandHandler,
                deleteAllCoreDefinitionsCommandHandler,
                deleteAllFunctionDefinitionsCommandHandler,
                deleteAllSubscriptionDefinitionsCommandHandler,
                deleteAllDeviceDefinitionsCommandHandler,
                deleteAllLoggerDefinitionsCommandHandler,
                deleteAllConnectorDefinitionsCommandHandler));
    }
}
