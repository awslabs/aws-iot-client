package com.awslabs.iot.client.commands.iot.policies;

import com.awslabs.general.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.commands.iot.IotCommandHandler;
import com.awslabs.iot.client.helpers.progressbar.ProgressBarHelper;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.client.streams.interfaces.UsesStream;
import com.awslabs.iot.helpers.interfaces.V2IotHelper;
import io.vavr.control.Try;
import software.amazon.awssdk.services.iot.model.Policy;

import javax.inject.Inject;
import java.util.stream.Stream;

public class DeleteAllPoliciesCommandHandler implements IotCommandHandler, UsesStream<Policy> {
    private static final String DELETEALLPOLICIES = "delete-all-policies";
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;
    @Inject
    V2IotHelper v2IotHelper;
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
                .forEach(policy -> Try.run(() -> v2IotHelper.delete(policy)));

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

    @Override
    public Stream<Policy> getStream() {
        return v2IotHelper.getPolicies();
    }
}
