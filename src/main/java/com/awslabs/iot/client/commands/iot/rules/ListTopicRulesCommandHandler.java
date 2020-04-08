package com.awslabs.iot.client.commands.iot.rules;

import com.awslabs.general.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.commands.iot.IotCommandHandler;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.helpers.interfaces.V1RuleHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class ListTopicRulesCommandHandler implements IotCommandHandler {
    private static final String LISTTOPICRULES = "list-topic-rules";
    private static final Logger log = LoggerFactory.getLogger(ListTopicRulesCommandHandler.class);
    @Inject
    V1RuleHelper ruleHelper;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;

    @Inject
    public ListTopicRulesCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        ruleHelper.listTopicRuleNames()
                .forEach(topicRuleName -> log.info("  [" + topicRuleName + "]"));
    }

    @Override
    public String getCommandString() {
        return LISTTOPICRULES;
    }

    @Override
    public String getHelp() {
        return "Lists all topic rules.";
    }

    @Override
    public int requiredParameters() {
        return 0;
    }

    public ParameterExtractor getParameterExtractor() {
        return this.parameterExtractor;
    }

    public IoHelper getIoHelper() {
        return this.ioHelper;
    }
}
