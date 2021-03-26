package com.awslabs.iot.client.commands.iot.policies;


import com.awslabs.iot.client.commands.iot.IotCommandHandler;
import com.awslabs.iot.client.helpers.progressbar.ProgressBarHelper;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.client.streams.interfaces.UsesStream;
import com.awslabs.iot.helpers.interfaces.IotHelper;
import io.vavr.collection.Stream;
import io.vavr.control.Try;
import software.amazon.awssdk.services.iot.model.Policy;

import javax.inject.Inject;

public class DeleteAllPoliciesCommandHandler implements IotCommandHandler, UsesStream<Policy> {
    private static final String DELETEALLPOLICIES = "delete-all-policies";
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IotHelper iotHelper;
    @Inject
    ProgressBarHelper progressBarHelper;

    @Inject
    public DeleteAllPoliciesCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        Try.withResources(() -> progressBarHelper.start("Delete all IoT policies", this))
                .of(progressBar -> run());
    }

    private Void run() {
        getStream()
                .peek(policy -> progressBarHelper.next())
                // Ignore all failures
                .forEach(policy -> Try.run(() -> iotHelper.delete(policy)));

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

    @Override
    public Stream<Policy> getStream() {
        return iotHelper.getPolicies();
    }
}
