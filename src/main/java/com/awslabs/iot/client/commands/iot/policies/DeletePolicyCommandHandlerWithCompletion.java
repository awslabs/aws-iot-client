package com.awslabs.iot.client.commands.iot.policies;


import com.awslabs.iot.client.commands.iot.PolicyCommandHandlerWithCompletion;
import com.awslabs.iot.client.commands.iot.completers.PolicyCompleter;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.data.ImmutablePolicyName;
import com.awslabs.iot.data.PolicyName;
import com.awslabs.iot.helpers.interfaces.IotHelper;
import io.vavr.collection.List;

import javax.inject.Inject;

public class DeletePolicyCommandHandlerWithCompletion implements PolicyCommandHandlerWithCompletion {
    private static final String DELETEPOLICY = "delete-policy";
    private static final int POLICY_NAME_POSITION = 0;
    @Inject
    IotHelper iotHelper;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    PolicyCompleter policyCompleter;

    @Inject
    public DeletePolicyCommandHandlerWithCompletion() {
    }

    @Override
    public void innerHandle(String input) {
        List<String> parameters = parameterExtractor.getParameters(input);

        PolicyName policyName = ImmutablePolicyName.builder().name(parameters.get(POLICY_NAME_POSITION)).build();

        iotHelper.delete(policyName);
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

    public PolicyCompleter getPolicyCompleter() {
        return this.policyCompleter;
    }
}
