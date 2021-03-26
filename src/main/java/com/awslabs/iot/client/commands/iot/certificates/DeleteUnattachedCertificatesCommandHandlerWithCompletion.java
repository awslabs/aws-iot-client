package com.awslabs.iot.client.commands.iot.certificates;


import com.awslabs.iot.client.commands.iot.IotCommandHandler;
import com.awslabs.iot.client.commands.iot.completers.CertificateCompleter;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.helpers.interfaces.IotHelper;

import javax.inject.Inject;

public class DeleteUnattachedCertificatesCommandHandlerWithCompletion implements IotCommandHandler {
    private static final String DELETEUNATTACHEDCERTIFICATES = "delete-unattached-certificates";
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    CertificateCompleter certificateCompleter;
    @Inject
    IotHelper iotHelper;

    @Inject
    public DeleteUnattachedCertificatesCommandHandlerWithCompletion() {
    }

    @Override
    public void innerHandle(String input) {
        iotHelper.getUnattachedCertificates()
                .forEach(certificate -> iotHelper.delete(certificate));
    }

    @Override
    public String getCommandString() {
        return DELETEUNATTACHEDCERTIFICATES;
    }

    @Override
    public String getHelp() {
        return "Deletes certificates that have no policy or thing attached.";
    }

    @Override
    public int requiredParameters() {
        return 0;
    }

    public ParameterExtractor getParameterExtractor() {
        return this.parameterExtractor;
    }

    public CertificateCompleter getCertificateCompleter() {
        return this.certificateCompleter;
    }
}
