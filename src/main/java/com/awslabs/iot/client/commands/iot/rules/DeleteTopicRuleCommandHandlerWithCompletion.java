package com.awslabs.iot.client.commands.iot.rules;

import com.awslabs.general.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.commands.iot.RuleCommandHandlerWithCompletion;
import com.awslabs.iot.client.commands.iot.completers.RuleCompleter;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.data.ImmutableRuleName;
import com.awslabs.iot.data.RuleName;
import com.awslabs.iot.helpers.interfaces.V2IotHelper;
import com.jcabi.log.Logger;

import javax.inject.Inject;
import java.util.List;

public class DeleteTopicRuleCommandHandlerWithCompletion implements RuleCommandHandlerWithCompletion {
    private static final String DELETETOPICRULE = "delete-topic-rule";
    private static final int TOPIC_RULE_NAME_POSITION = 0;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;
    @Inject
    RuleCompleter ruleCompleter;
    @Inject
    V2IotHelper v2IotHelper;

    @Inject
    public DeleteTopicRuleCommandHandlerWithCompletion() {
    }

    @Override
    public void innerHandle(String input) {
        List<String> parameters = parameterExtractor.getParameters(input);

        RuleName topicRuleName = ImmutableRuleName.builder().name(parameters.get(TOPIC_RULE_NAME_POSITION)).build();

        v2IotHelper.deleteTopicRule(topicRuleName);
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

    public IoHelper getIoHelper() {
        return this.ioHelper;
    }

    public RuleCompleter getRuleCompleter() {
        return this.ruleCompleter;
    }
}
