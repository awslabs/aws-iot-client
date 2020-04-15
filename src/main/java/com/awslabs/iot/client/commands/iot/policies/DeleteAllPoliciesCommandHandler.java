package com.awslabs.iot.client.commands.iot.policies;

import com.awslabs.general.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.commands.iot.IotCommandHandler;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.helpers.interfaces.V2IotHelper;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.iot.model.DeleteConflictException;
import software.amazon.awssdk.services.iot.model.Policy;

import javax.inject.Inject;
import java.util.Comparator;

public class DeleteAllPoliciesCommandHandler implements IotCommandHandler {
    private static final String DELETEALLPOLICIES = "delete-all-policies";
    private static final Logger log = LoggerFactory.getLogger(DeleteAllPoliciesCommandHandler.class);
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;
    @Inject
    V2IotHelper v2IotHelper;

    @Inject
    public DeleteAllPoliciesCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        v2IotHelper.getPolicies()
                // Sort the policies by name so we can get a general sense of how far along we are in the process of deleting them
                .sorted(Comparator.comparing(Policy::policyName))
                .forEach(this::attemptDeletePolicy);
    }

    private void attemptDeletePolicy(Policy policy) {
        Try.run(() -> v2IotHelper.delete(policy))
                .onSuccess(Void -> log.info(String.join("", "Deleted policy [", policy.policyName(), "]")))
                .recover(DeleteConflictException.class, exception -> ignoreAndLog(exception, policy));
    }

    private <T> T ignoreAndLog(DeleteConflictException exception, Policy policy) {
        log.info(String.join("", "Policy [", policy.policyName(), "] could not be deleted [", exception.getMessage(), "]"));

        return null;
    }

    @Override
    public String getCommandString() {
        return DELETEALLPOLICIES;
    }

    @Override
    public String getHelp() {
        return "Deletes all IoT policies.";
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
