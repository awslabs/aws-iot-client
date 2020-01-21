package com.awslabs.iot.client.commands.iot.certificates;

import com.awslabs.aws.iot.resultsiterator.helpers.interfaces.IoHelper;
import com.awslabs.aws.iot.resultsiterator.helpers.v1.interfaces.V1ThingHelper;
import com.awslabs.iot.client.commands.iot.CertificateCommandHandlerWithCompletion;
import com.awslabs.iot.client.commands.iot.completers.CertificateCompleter;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;

public class DeleteCertificateCommandHandlerWithCompletion implements CertificateCommandHandlerWithCompletion {
    private static final String DELETECERTIFICATE = "delete-certificate";
    private static final int CERTIFICATE_ARN_POSITION = 0;
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(DeleteCertificateCommandHandlerWithCompletion.class);
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;
    @Inject
    CertificateCompleter certificateCompleter;
    @Inject
    Provider<V1ThingHelper> thingHelperProvider;

    @Inject
    public DeleteCertificateCommandHandlerWithCompletion() {
    }

    @Override
    public void innerHandle(String input) {
        List<String> parameters = parameterExtractor.getParameters(input);

        String certificateArn = parameters.get(CERTIFICATE_ARN_POSITION);

        thingHelperProvider.get().deletePrincipal(certificateArn);
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
