package com.awslabs.iot.client.commands.iot.policies;

import com.awslabs.general.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.commands.iot.PolicyCommandHandlerWithCompletion;
import com.awslabs.iot.client.commands.iot.completers.PolicyCompleter;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.data.ImmutablePolicyName;
import com.awslabs.iot.data.PolicyName;
import com.awslabs.iot.helpers.interfaces.V2IotHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;

public class DeletePolicyCommandHandlerWithCompletion implements PolicyCommandHandlerWithCompletion {
    private static final String DELETEPOLICY = "delete-policy";
    private static final int POLICY_NAME_POSITION = 0;
    private static final Logger log = LoggerFactory.getLogger(DeletePolicyCommandHandlerWithCompletion.class);
    @Inject
    V2IotHelper v2IotHelper;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;
    @Inject
    PolicyCompleter policyCompleter;

    @Inject
    public DeletePolicyCommandHandlerWithCompletion() {
    }

    @Override
    public void innerHandle(String input) {
        List<String> parameters = parameterExtractor.getParameters(input);

        PolicyName policyName = ImmutablePolicyName.builder().name(parameters.get(POLICY_NAME_POSITION)).build();

        v2IotHelper.delete(policyName);
    }

    @Override
    public String getCommandString() {
        return DELETEPOLICY;
    }

    @Override
    public String getHelp() {
        return "Deletes a policy by name.";
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

    public PolicyCompleter getPolicyCompleter() {
        return this.policyCompleter;
    }
}
