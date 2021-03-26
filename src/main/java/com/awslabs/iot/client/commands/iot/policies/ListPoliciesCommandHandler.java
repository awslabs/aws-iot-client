package com.awslabs.iot.client.commands.iot.policies;


import com.awslabs.iot.client.commands.iot.IotCommandHandler;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.helpers.interfaces.IotHelper;
import com.jcabi.log.Logger;
import software.amazon.awssdk.services.iot.model.Policy;

import javax.inject.Inject;

public class ListPoliciesCommandHandler implements IotCommandHandler {
    private static final String LISTPOLICIES = "list-policies";
    @Inject
    IotHelper iotHelper;
    @Inject
    ParameterExtractor parameterExtractor;

    @Inject
    public ListPoliciesCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        iotHelper.getPolicies()
                .map(Policy::policyName)
                .forEach(policyName -> Logger.info(this, String.join("", "  [", policyName, "]")));
    }

    @Override
    public String getCommandString() {
        return LISTPOLICIES;
    }

    @Override
    public String getHelp() {
        return "Lists all policy names.";
    }

    @Override
    public int requiredParameters() {
        return 0;
    }

    public ParameterExtractor getParameterExtractor() {
        return this.parameterExtractor;
    }
}
