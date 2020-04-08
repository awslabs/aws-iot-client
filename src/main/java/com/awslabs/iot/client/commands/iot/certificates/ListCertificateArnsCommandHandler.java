package com.awslabs.iot.client.commands.iot.certificates;

import com.awslabs.general.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.commands.iot.IotCommandHandler;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.helpers.interfaces.V1CertificateHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;

public class ListCertificateArnsCommandHandler implements IotCommandHandler {
    private static final String LISTCERTIFICATEARNS = "list-certificate-arns";
    private static final Logger log = LoggerFactory.getLogger(ListCertificateArnsCommandHandler.class);
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;
    @Inject
    Provider<V1CertificateHelper> certificateHelperProvider;

    @Inject
    public ListCertificateArnsCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        certificateHelperProvider.get().listCertificateArns()
                .forEach(certificateArn -> log.info("  [" + certificateArn + "]"));
    }

    @Override
    public String getCommandString() {
        return LISTCERTIFICATEARNS;
    }

    @Override
    public String getHelp() {
        return "Lists all certificate ARNs.";
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
