package com.awslabs.iot.client.commands.iot.rules;

import com.amazonaws.services.iot.AWSIotClient;
import com.amazonaws.services.iot.model.*;
import com.awslabs.iot.client.commands.iam.completers.RoleCompleter;
import com.awslabs.iot.client.commands.iot.IotCommandHandler;
import com.awslabs.iot.client.helpers.iam.interfaces.IamHelper;
import com.awslabs.iot.client.helpers.io.interfaces.IOHelper;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import org.jline.reader.Completer;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.reader.impl.completer.NullCompleter;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class CreateTopicRuleCommandHandler implements IotCommandHandler {
    private static final String CREATETOPICRULE = "create-topic-rule";
    private static final int ROLE_NAME_POSITION = 0;
    private static final int TOPIC_NAME_POSITION = 1;
    private static final int RULE_NAME_POSITION = 2;
    private static final int SQL_POSITION = 3;
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(CreateTopicRuleCommandHandler.class);
    @Inject
    RoleCompleter roleCompleter;
    @Inject
    IamHelper iamHelper;
    @Inject
    AWSIotClient awsIotClient;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IOHelper ioHelper;

    @Inject
    public CreateTopicRuleCommandHandler() {
    }

    @Override
    public Completer getCompleter() {
        return new ArgumentCompleter(getCommandNameCompleter(), roleCompleter, new NullCompleter());
    }

    @Override
    public void showUsage(Logger logger) {
        log.info("You must specify the name of the role that will be used for republishing, the republish topic, the rule name, and the rule SQL");
    }

    @Override
    public void innerHandle(String input) {
        List<String> parameters = parameterExtractor.getParameters(input);

        String roleName = parameters.get(ROLE_NAME_POSITION);
        String topicName = parameters.get(TOPIC_NAME_POSITION);
        String ruleName = parameters.get(RULE_NAME_POSITION);
        String sql = parameters.get(SQL_POSITION);

        RepublishAction republishAction = new RepublishAction()
                .withRoleArn(iamHelper.getRoleArn(roleName))
                .withTopic(topicName);

        Action action = new Action()
                .withRepublish(republishAction);

        TopicRulePayload topicRulePayload = new TopicRulePayload()
                .withRuleDisabled(false)
                .withActions(action)
                .withSql(sql);

        CreateTopicRuleRequest createTopicRuleRequest = new CreateTopicRuleRequest()
                .withRuleName(ruleName)
                .withTopicRulePayload(topicRulePayload);

        try {
            awsIotClient.createTopicRule(createTopicRuleRequest);
        } catch (ResourceAlreadyExistsException e) {
            log.info("It appears that rule already exists, please create a topic rule with another name");
        }
    }

    @Override
    public String getCommandString() {
        return CREATETOPICRULE;
    }

    @Override
    public String getHelp() {
        return "Creates a rule.  First parameter is the output topic, second parameter is the rule name, third parameter is the SQL.";
    }

    @Override
    public int requiredParameters() {
        return 4;
    }

    public ParameterExtractor getParameterExtractor() {
        return this.parameterExtractor;
    }

    public IOHelper getIoHelper() {
        return this.ioHelper;
    }
}
