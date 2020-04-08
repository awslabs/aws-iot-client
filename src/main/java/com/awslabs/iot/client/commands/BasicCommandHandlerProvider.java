package com.awslabs.iot.client.commands;

import com.awslabs.iot.client.applications.Arguments;
import com.awslabs.iot.client.commands.interfaces.CommandHandler;

import javax.inject.Inject;
import java.util.Set;
import java.util.stream.Collectors;

public class BasicCommandHandlerProvider implements CommandHandlerProvider {
    @Inject
    Arguments arguments;

    @Inject
    Set<CommandHandler> commandHandlerSet;

    @Inject
    public BasicCommandHandlerProvider() {
    }

    @Override
    public Set<CommandHandler> getCommandHandlerSet() {
        if (arguments.dangerousMode) {
            return commandHandlerSet;
        }

        return commandHandlerSet.stream()
                .filter(commandHandler -> !commandHandler.isDangerous())
                .collect(Collectors.toSet());
    }
}
