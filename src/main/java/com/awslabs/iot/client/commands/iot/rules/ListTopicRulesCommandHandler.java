package com.awslabs.iot.client.commands.iot.rules;

import com.awslabs.aws.iot.resultsiterator.helpers.interfaces.IoHelper;
import com.awslabs.aws.iot.resultsiterator.helpers.v1.interfaces.V1RuleHelper;
import com.awslabs.iot.client.commands.iot.IotCommandHandler;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class ListTopicRulesCommandHandler implements IotCommandHandler {
    private static final String LISTTOPICRULES = "list-topic-rules";
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ListTopicRulesCommandHandler.class);
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
        List<String> topicRuleNames = ruleHelper.listTopicRuleNames();

        for (String topicRuleName : topicRuleNames) {
            log.info("  [" + topicRuleName + "]");
        }
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
