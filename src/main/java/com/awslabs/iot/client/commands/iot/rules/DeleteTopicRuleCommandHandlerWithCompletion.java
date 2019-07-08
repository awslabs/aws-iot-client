package com.awslabs.iot.client.commands.iot.rules;

import com.amazonaws.services.iot.AWSIotClient;
import com.amazonaws.services.iot.model.DeleteTopicRuleRequest;
import com.amazonaws.services.iot.model.UnauthorizedException;
import com.awslabs.iot.client.commands.iot.RuleCommandHandlerWithCompletion;
import com.awslabs.iot.client.commands.iot.completers.RuleCompleter;
import com.awslabs.iot.client.helpers.io.interfaces.IOHelper;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class DeleteTopicRuleCommandHandlerWithCompletion implements RuleCommandHandlerWithCompletion {
    private static final String DELETETOPICRULE = "delete-topic-rule";
    private static final int TOPIC_RULE_NAME_POSITION = 0;
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(DeleteTopicRuleCommandHandlerWithCompletion.class);
    @Inject
    AWSIotClient awsIotClient;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IOHelper ioHelper;
    @Inject
    RuleCompleter ruleCompleter;

    @Inject
    public DeleteTopicRuleCommandHandlerWithCompletion() {
    }

    @Override
    public void innerHandle(String input) {
        List<String> parameters = parameterExtractor.getParameters(input);

        String topicRuleName = parameters.get(TOPIC_RULE_NAME_POSITION);

        DeleteTopicRuleRequest deleteTopicRuleRequest = new DeleteTopicRuleRequest()
                .withRuleName(topicRuleName);

        try {
            awsIotClient.deleteTopicRule(deleteTopicRuleRequest);
        } catch (UnauthorizedException e) {
            log.info("Couldn't delete this rule.  Either it doesn't exist or you do not have permissions to delete it.");
        }
    }

    @Override
    public void showUsage(Logger logger) {
        log.info("You must specify the name of the topic rule you want to delete.");
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

    public IOHelper getIoHelper() {
        return this.ioHelper;
    }

    public RuleCompleter getRuleCompleter() {
        return this.ruleCompleter;
    }
}
