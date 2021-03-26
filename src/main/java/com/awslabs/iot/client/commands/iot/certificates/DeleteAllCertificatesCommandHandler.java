package com.awslabs.iot.client.commands.iot.certificates;


import com.awslabs.iot.client.commands.iot.IotCommandHandler;
import com.awslabs.iot.client.helpers.progressbar.ProgressBarHelper;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.client.streams.interfaces.UsesStream;
import com.awslabs.iot.helpers.interfaces.IotHelper;
import io.vavr.collection.Stream;
import io.vavr.control.Try;
import software.amazon.awssdk.services.iot.model.Certificate;

import javax.inject.Inject;

public class DeleteAllCertificatesCommandHandler implements IotCommandHandler, UsesStream<Certificate> {
    private static final String DELETEALLCERTIFICATES = "delete-all-certificates";
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IotHelper iotHelper;
    @Inject
    ProgressBarHelper progressBarHelper;

    @Inject
    public DeleteAllCertificatesCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        Try.withResources(() -> progressBarHelper.start("Delete all IoT certificates", this))
                .of(progressBar -> run());
    }

    private Void run() {
        getStream()
                .peek(certificate -> progressBarHelper.next())
                .forEach(iotHelper::recursiveDelete);

        return null;
    }

    @Override
    public String getCommandString() {
        return DELETEALLCERTIFICATES;
    }

    @Override
    public String getHelp() {
        return "Deletes all non-CA certificates.";
    }

    @Override
    public int requiredParameters() {
        return 0;
    }

    public ParameterExtractor getParameterExtractor() {
        return this.parameterExtractor;
    }

    @Override
    public Stream<Certificate> getStream() {
        return iotHelper.getCertificates();
    }
}
