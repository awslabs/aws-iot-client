package com.awslabs.iot.client.commands.generic;


import com.awslabs.iot.client.commands.CommandHandlerProvider;
import com.awslabs.iot.client.commands.interfaces.CommandHandler;
import com.awslabs.iot.client.helpers.ANSIHelper;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.jcabi.log.Logger;
import io.vavr.collection.List;
import io.vavr.collection.Stream;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Comparator;

public class HelpCommandHandler implements CommandHandler {
    private static final String HELP = "help";
    @Inject
    Provider<CommandHandlerProvider> commandHandlerProviderProvider;
    @Inject
    ParameterExtractor parameterExtractor;

    @Inject
    public HelpCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(ANSIHelper.CRLF);

        List<CommandHandler> sortedCommandHandlers = Stream.ofAll(commandHandlerProviderProvider.get()
                .getCommandHandlerSet())
                .sorted(Comparator.comparing(CommandHandler::getFullCommandString))
                .toList();

        for (CommandHandler commandHandler : sortedCommandHandlers) {
            stringBuilder.append(ANSIHelper.BOLD);
            stringBuilder.append(commandHandler.getFullCommandString());
            stringBuilder.append(ANSIHelper.NORMAL);
            stringBuilder.append(" - ");
            stringBuilder.append(commandHandler.getHelp());
            stringBuilder.append(ANSIHelper.CRLF);
        }

        Logger.info(this, stringBuilder.toString());
    }

    @Override
    public String getCommandString() {
        return HELP;
    }

    @Override
    public String getHelp() {
        return "Gets this help message";
    }

    @Override
    public int requiredParameters() {
        return 0;
    }

    public ParameterExtractor getParameterExtractor() {
        return this.parameterExtractor;
    }
}
