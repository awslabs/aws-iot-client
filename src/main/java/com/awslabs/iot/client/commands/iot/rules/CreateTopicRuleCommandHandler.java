package com.awslabs.iot.client.commands.iot.rules;

import com.awslabs.general.helpers.interfaces.IoHelper;
import com.awslabs.iam.data.ImmutableRoleName;
import com.awslabs.iam.data.RoleName;
import com.awslabs.iam.helpers.interfaces.V2IamHelper;
import com.awslabs.iot.client.commands.iam.completers.RoleCompleter;
import com.awslabs.iot.client.commands.iot.IotCommandHandler;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.data.ImmutableRuleName;
import com.awslabs.iot.data.RuleName;
import com.awslabs.iot.helpers.interfaces.V2IotHelper;
import com.jcabi.log.Logger;
import org.jline.reader.Completer;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.reader.impl.completer.NullCompleter;
import software.amazon.awssdk.services.iam.model.Role;
import software.amazon.awssdk.services.iot.model.Action;
import software.amazon.awssdk.services.iot.model.RepublishAction;
import software.amazon.awssdk.services.iot.model.TopicRulePayload;

import javax.inject.Inject;
import java.util.List;

public class CreateTopicRuleCommandHandler implements IotCommandHandler {
    private static final String CREATETOPICRULE = "create-topic-rule";
    private static final int ROLE_NAME_POSITION = 0;
    private static final int TOPIC_NAME_POSITION = 1;
    private static final int RULE_NAME_POSITION = 2;
    private static final int SQL_POSITION = 3;
    @Inject
    RoleCompleter roleCompleter;
    @Inject
    V2IamHelper v2IamHelper;
    @Inject
    V2IotHelper v2IotHelper;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;

    @Inject
    public CreateTopicRuleCommandHandler() {
    }

    @Override
    public Completer getCompleter() {
        return new ArgumentCompleter(getCommandNameCompleter(), roleCompleter, new NullCompleter());
    }

    @Override
    public void showUsage() {
        Logger.info(this, "You must specify the name of the role that will be used for republishing, the republish topic, the rule name, and the rule SQL");
    }

    @Override
    public void innerHandle(String input) {
        List<String> parameters = parameterExtractor.getParameters(input);

        RoleName roleName = ImmutableRoleName.builder().name(parameters.get(ROLE_NAME_POSITION)).build();
        String topicName = parameters.get(TOPIC_NAME_POSITION);
        RuleName ruleName = ImmutableRuleName.builder().name(parameters.get(RULE_NAME_POSITION)).build();
        String sql = parameters.get(SQL_POSITION);

        // Throw an exception if the role isn't present
        Role role = v2IamHelper.getRole(roleName).get();

        RepublishAction republishAction = RepublishAction.builder()
                .roleArn(role.arn())
                .topic(topicName)
                .build();

        Action action = Action.builder()
                .republish(republishAction)
                .build();

        TopicRulePayload topicRulePayload = TopicRulePayload.builder()
                .ruleDisabled(false)
                .actions(action)
                .sql(sql)
                .build();

        v2IotHelper.createTopicRule(ruleName, topicRulePayload);
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

    public IoHelper getIoHelper() {
        return this.ioHelper;
    }
}
