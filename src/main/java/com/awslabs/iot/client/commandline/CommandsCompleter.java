package com.awslabs.iot.client.commandline;

import com.awslabs.iot.client.commands.CommandHandlerProvider;
import com.awslabs.iot.client.commands.interfaces.CommandHandler;
import io.vavr.collection.List;
import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;
import org.jline.reader.impl.completer.AggregateCompleter;

import javax.inject.Inject;
import java.util.stream.Collectors;

// Guidance from: https://github.com/jline/jline2/blob/master/src/main/java/jline/console/completer/StringsCompleter.java
public class CommandsCompleter implements Completer {
    @Inject
    CommandHandlerProvider commandHandlerProvider;

    private AggregateCompleter aggregateCompleter;

    @Inject
    public CommandsCompleter() {
    }

    private AggregateCompleter getAggregateCompleter() {
        if (aggregateCompleter == null) {
            // Get all of the completers from the command handlers
            List<Completer> completers = List.ofAll(
                    commandHandlerProvider.getCommandHandlerSet().stream()
                            .map(CommandHandler::getCompleter)
                            .collect(Collectors.toSet()));

            aggregateCompleter = new AggregateCompleter(completers.asJava());
        }

        return aggregateCompleter;
    }

    @Override
    public void complete(LineReader lineReader, ParsedLine parsedLine, java.util.List<Candidate> candidateList) {
        getAggregateCompleter().complete(lineReader, parsedLine, candidateList);
    }
}
