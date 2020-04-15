package com.awslabs.iot.client.commands.iot.certificates;

import com.awslabs.general.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.commands.iot.CertificateCommandHandlerWithCompletion;
import com.awslabs.iot.client.commands.iot.completers.CertificateCompleter;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.data.CertificateArn;
import com.awslabs.iot.data.ImmutableCertificateArn;
import com.awslabs.iot.helpers.interfaces.V2IotHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;

public class DeleteCertificateCommandHandlerWithCompletion implements CertificateCommandHandlerWithCompletion {
    private static final String DELETECERTIFICATE = "delete-certificate";
    private static final int CERTIFICATE_ARN_POSITION = 0;
    private static final Logger log = LoggerFactory.getLogger(DeleteCertificateCommandHandlerWithCompletion.class);
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;
    @Inject
    CertificateCompleter certificateCompleter;
    @Inject
    V2IotHelper v2IotHelper;

    @Inject
    public DeleteCertificateCommandHandlerWithCompletion() {
    }

    @Override
    public void innerHandle(String input) {
        List<String> parameters = parameterExtractor.getParameters(input);

        CertificateArn certificateArn = ImmutableCertificateArn.builder().arn(parameters.get(CERTIFICATE_ARN_POSITION)).build();

        v2IotHelper.delete(certificateArn);
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

    public IoHelper getIoHelper() {
        return this.ioHelper;
    }

    public CertificateCompleter getCertificateCompleter() {
        return this.certificateCompleter;
    }
}
