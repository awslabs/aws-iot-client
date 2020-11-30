package com.awslabs.iot.client.console;

import com.awslabs.iot.client.applications.Arguments;
import com.awslabs.iot.client.commandline.CommandsCompleter;
import com.awslabs.iot.client.commands.CommandHandlerProvider;
import com.awslabs.iot.client.commands.interfaces.CommandHandler;
import com.awslabs.iot.client.helpers.ANSIHelper;
import com.awslabs.iot.client.interfaces.AwsIotClientTerminal;
import com.jcabi.log.Logger;
import org.jline.reader.Completer;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Set;

public class AwsIotClientConsoleTerminal implements AwsIotClientTerminal {
    @Inject
    CommandHandlerProvider commandHandlerProvider;
    @Inject
    CommandsCompleter commandsCompleter;
    @Inject
    Arguments arguments;

    @Inject
    public AwsIotClientConsoleTerminal() {
    }

    @Override
    public String getTextColor() {
        return ANSIHelper.WHITE;
    }

    @Override
    public String getPromptColor() {
        return ANSIHelper.WHITE;
    }

    @Override
    public Terminal getTerminal() throws IOException {
        return TerminalBuilder.builder()
                .system(true)
                .nativeSignals(true)
                // Block CTRL-C - disabled for now
                // .signalHandler(Terminal.SignalHandler.SIG_IGN)
                .build();
    }

    @Override
    public Completer getCommandsCompleter() {
        return commandsCompleter;
    }

    @Override
    public Set<CommandHandler> getCommandHandlerSet() {
        return commandHandlerProvider.getCommandHandlerSet();
    }

    @Override
    public void write(String s) {
        Logger.info(this, s);
    }

    @Override
    public void start() throws Exception {
        // Show the dangerous mode banner if necessary
        showDangerousModeBannerIfNecessary();

        mainLoop();
    }

    private void showDangerousModeBannerIfNecessary() {
        if (!arguments.dangerousMode) {
            return;
        }

        Logger.warn(this, "------------------------------------ DANGER!");
        Logger.warn(this, "| You have enabled dangerous mode! |");
        Logger.warn(this, "------------------------------------ DANGER!");

        Logger.warn(this, "Some commands can and will remove resources in your AWS account without confirmation.  You have been warned!");
    }
}