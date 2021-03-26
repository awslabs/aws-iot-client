package com.awslabs.iot.client.commands.greengrassv2.greengrass.groups;


import com.awslabs.iot.client.commands.greengrassv2.greengrass.GreengrassV2CommandHandler;
import com.awslabs.iot.client.helpers.progressbar.ProgressBarHelper;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.client.streams.interfaces.UsesStream;
import com.awslabs.iot.helpers.interfaces.GreengrassV2Helper;
import io.vavr.collection.Stream;
import io.vavr.control.Try;
import software.amazon.awssdk.services.greengrassv2.model.CoreDevice;

import javax.inject.Inject;

public class DeleteAllCoresV2CommandHandler implements GreengrassV2CommandHandler, UsesStream<CoreDevice> {
    private static final String DELETE_ALL_CORES = "delete-all-cores";
    @Inject
    GreengrassV2Helper greengrassV2Helper;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    ProgressBarHelper progressBarHelper;

    @Inject
    public DeleteAllCoresV2CommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        Try.withResources(() -> progressBarHelper.start("Delete all Greengrass V2 cores", this))
                .of(progressBar -> run());
    }

    private Void run() {
        getStream()
                .peek(coreDevice -> progressBarHelper.next())
                .forEach(greengrassV2Helper::deleteCoreDevice);

        return null;
    }

    @Override
    public String getCommandString() {
        return DELETE_ALL_CORES;
    }

    @Override
    public String getHelp() {
        return "Deletes all Greengrass core definitions.";
    }

    @Override
    public int requiredParameters() {
        return 0;
    }

    public ParameterExtractor getParameterExtractor() {
        return this.parameterExtractor;
    }

    @Override
    public Stream<CoreDevice> getStream() {
        return greengrassV2Helper.getAllCoreDevices();
    }

    @Override
    public boolean isDangerous() {
        return true;
    }
}
