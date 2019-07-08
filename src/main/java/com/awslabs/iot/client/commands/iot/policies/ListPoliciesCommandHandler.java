package com.awslabs.iot.client.commands.iot.policies;

import com.awslabs.iot.client.commands.iot.IotCommandHandler;
import com.awslabs.iot.client.helpers.io.interfaces.IOHelper;
import com.awslabs.iot.client.helpers.iot.interfaces.PolicyHelper;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;

public class ListPoliciesCommandHandler implements IotCommandHandler {
    private static final String LISTPOLICIES = "list-policies";
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ListPoliciesCommandHandler.class);
    @Inject
    Provider<PolicyHelper> policyHelperProvider;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IOHelper ioHelper;

    @Inject
    public ListPoliciesCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        List<String> policyNames = policyHelperProvider.get().listPolicyNames();

        for (String policyName : policyNames) {
            log.info("  [" + policyName + "]");
        }
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

    public IOHelper getIoHelper() {
        return this.ioHelper;
    }
}
