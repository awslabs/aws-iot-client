package com.awslabs.iot.client.commands.iot.certificates;

import com.awslabs.general.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.commands.iot.IotCommandHandler;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.helpers.interfaces.V2IotHelper;

import javax.inject.Inject;

public class DeleteAllCaCertificatesCommandHandler implements IotCommandHandler {
    private static final String DELETEALLCACERTIFICATES = "delete-all-ca-certificates";
    @Inject
    V2IotHelper v2IotHelper;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;

    @Inject
    public DeleteAllCaCertificatesCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        v2IotHelper.getCaCertificates()
                .forEach(certificate -> v2IotHelper.deleteCaCertificate(certificate));
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

    public IoHelper getIoHelper() {
        return this.ioHelper;
    }
}
