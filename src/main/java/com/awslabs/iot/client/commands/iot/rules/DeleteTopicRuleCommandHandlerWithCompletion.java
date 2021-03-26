package com.awslabs.iot.client.commands.iot.rules;


import com.awslabs.iot.client.commands.iot.RuleCommandHandlerWithCompletion;
import com.awslabs.iot.client.commands.iot.completers.RuleCompleter;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.data.ImmutableRuleName;
import com.awslabs.iot.data.RuleName;
import com.awslabs.iot.helpers.interfaces.IotHelper;
import com.jcabi.log.Logger;
import io.vavr.collection.List;

import javax.inject.Inject;

public class DeleteTopicRuleCommandHandlerWithCompletion implements RuleCommandHandlerWithCompletion {
    private static final String DELETETOPICRULE = "delete-topic-rule";
    private static final int TOPIC_RULE_NAME_POSITION = 0;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    RuleCompleter ruleCompleter;
    @Inject
    IotHelper iotHelper;

    @Inject
    public DeleteTopicRuleCommandHandlerWithCompletion() {
    }

    @Override
    public void innerHandle(String input) {
        List<String> parameters = parameterExtractor.getParameters(input);

        RuleName topicRuleName = ImmutableRuleName.builder().name(parameters.get(TOPIC_RULE_NAME_POSITION)).build();

        iotHelper.deleteTopicRule(topicRuleName);
    }

    @Override
    public void showUsage() {
        Logger.info(this, "You must specify the name of the topic rule you want to delete.");
    }

    @Override
    public String getCommandString() {
        return DELETETOPICRULE;
    }

    @Override
    public String getHelp() {
        return "Deletes a rule by name.";
    }

    @Override
    public int requiredParameters() {
        return 1;
    }

    public ParameterExtractor getParameterExtractor() {
        return this.parameterExtractor;
    }

    public RuleCompleter getRuleCompleter() {
        return this.ruleCompleter;
    }
}
