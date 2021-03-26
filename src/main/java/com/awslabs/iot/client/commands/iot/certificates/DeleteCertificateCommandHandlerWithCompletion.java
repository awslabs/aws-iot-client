package com.awslabs.iot.client.commands.iot.certificates;


import com.awslabs.iot.client.commands.iot.CertificateCommandHandlerWithCompletion;
import com.awslabs.iot.client.commands.iot.completers.CertificateCompleter;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.data.CertificateArn;
import com.awslabs.iot.data.ImmutableCertificateArn;
import com.awslabs.iot.helpers.interfaces.IotHelper;
import io.vavr.collection.List;

import javax.inject.Inject;

public class DeleteCertificateCommandHandlerWithCompletion implements CertificateCommandHandlerWithCompletion {
    private static final String DELETECERTIFICATE = "delete-certificate";
    private static final int CERTIFICATE_ARN_POSITION = 0;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    CertificateCompleter certificateCompleter;
    @Inject
    IotHelper iotHelper;

    @Inject
    public DeleteCertificateCommandHandlerWithCompletion() {
    }

    @Override
    public void innerHandle(String input) {
        List<String> parameters = parameterExtractor.getParameters(input);

        CertificateArn certificateArn = ImmutableCertificateArn.builder().arn(parameters.get(CERTIFICATE_ARN_POSITION)).build();

        iotHelper.delete(certificateArn);
    }

    @Override
    public String getCommandString() {
        return DELETECERTIFICATE;
    }

    @Override
    public String getHelp() {
        return "Deletes a certificate by its ARN.";
    }

    @Override
    public int requiredParameters() {
        return 1;
    }

    public ParameterExtractor getParameterExtractor() {
        return this.parameterExtractor;
    }

    public CertificateCompleter getCertificateCompleter() {
        return this.certificateCompleter;
    }
}
