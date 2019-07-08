package com.awslabs.iot.client.commands.generic;

import com.awslabs.iot.client.commands.CommandHandlerProvider;
import com.awslabs.iot.client.commands.interfaces.CommandHandler;
import com.awslabs.iot.client.helpers.ANSIHelper;
import com.awslabs.iot.client.helpers.io.interfaces.IOHelper;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HelpCommandHandler implements CommandHandler {
    private static final String HELP = "help";
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(HelpCommandHandler.class);
    @Inject
    CommandHandlerProvider commandHandlerProvider;
    @Inject
    IOHelper ioHelper;
    @Inject
    ParameterExtractor parameterExtractor;

    @Inject
    public HelpCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(ANSIHelper.CRLF);

        List<CommandHandler> sortedCommandHandlers = commandHandlerProvider.getCommandHandlerSet().stream()
                .sorted(Comparator.comparing(CommandHandler::getFullCommandString))
                .collect(Collectors.toList());

        for (CommandHandler commandHandler : sortedCommandHandlers) {
            stringBuilder.append(ANSIHelper.BOLD);
            stringBuilder.append(commandHandler.getFullCommandString());
            stringBuilder.append(ANSIHelper.NORMAL);
            stringBuilder.append(" - ");
            stringBuilder.append(commandHandler.getHelp());
            stringBuilder.append(ANSIHelper.CRLF);
        }

        log.info(stringBuilder.toString());
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

    public IOHelper getIoHelper() {
        return this.ioHelper;
    }

    public ParameterExtractor getParameterExtractor() {
        return this.parameterExtractor;
    }
}
