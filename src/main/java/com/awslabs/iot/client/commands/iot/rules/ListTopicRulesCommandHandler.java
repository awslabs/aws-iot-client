package com.awslabs.iot.client.commands.iot.rules;

import com.awslabs.general.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.commands.iot.IotCommandHandler;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.helpers.interfaces.V2IotHelper;
import com.jcabi.log.Logger;
import software.amazon.awssdk.services.iot.model.TopicRuleListItem;

import javax.inject.Inject;

public class ListTopicRulesCommandHandler implements IotCommandHandler {
    private static final String LISTTOPICRULES = "list-topic-rules";
    @Inject
    V2IotHelper v2IotHelper;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;

    @Inject
    public ListTopicRulesCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        v2IotHelper.getTopicRules()
                .map(TopicRuleListItem::ruleName)
                .forEach(topicRuleName -> Logger.info(this, String.join("", "  [", topicRuleName, "]")));
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
