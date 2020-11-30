package com.awslabs.iot.client.commands.iot.policies;

import com.awslabs.general.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.commands.iot.IotCommandHandler;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.helpers.interfaces.V2IotHelper;
import com.jcabi.log.Logger;
import software.amazon.awssdk.services.iot.model.Policy;

import javax.inject.Inject;

public class ListPoliciesCommandHandler implements IotCommandHandler {
    private static final String LISTPOLICIES = "list-policies";
    @Inject
    V2IotHelper v2IotHelper;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;

    @Inject
    public ListPoliciesCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        v2IotHelper.getPolicies()
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

    public IoHelper getIoHelper() {
        return this.ioHelper;
    }
}
