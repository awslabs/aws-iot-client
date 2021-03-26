package com.awslabs.iot.client.commands.generic;


import com.awslabs.iot.client.commands.interfaces.CommandHandler;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;

import javax.inject.Inject;

public class QuitCommandHandler implements CommandHandler {
    private static final String QUIT = "quit";
    @Inject
    ParameterExtractor parameterExtractor;

    @Inject
    public QuitCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        System.exit(0);
    }

    @Override
    public String getCommandString() {
        return QUIT;
    }

    @Override
    public String getHelp() {
        return "Quits/exits the application";
    }

    @Override
    public int requiredParameters() {
        return 0;
    }

    public ParameterExtractor getParameterExtractor() {
        return this.parameterExtractor;
    }
}
