package com.awslabs.iot.client.commands.iot.certificates;


import com.awslabs.iot.client.commands.iot.IotCommandHandler;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.helpers.interfaces.IotHelper;

import javax.inject.Inject;

public class DeleteAllCaCertificatesCommandHandler implements IotCommandHandler {
    private static final String DELETEALLCACERTIFICATES = "delete-all-ca-certificates";
    @Inject
    IotHelper iotHelper;
    @Inject
    ParameterExtractor parameterExtractor;

    @Inject
    public DeleteAllCaCertificatesCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        iotHelper.getCaCertificates()
                .forEach(certificate -> iotHelper.deleteCaCertificate(certificate));
    }

    @Override
    public String getCommandString() {
        return DELETEALLCACERTIFICATES;
    }

    @Override
    public String getHelp() {
        return "Deletes all CA certificates.";
    }

    @Override
    public int requiredParameters() {
        return 0;
    }

    public ParameterExtractor getParameterExtractor() {
        return this.parameterExtractor;
    }
}
