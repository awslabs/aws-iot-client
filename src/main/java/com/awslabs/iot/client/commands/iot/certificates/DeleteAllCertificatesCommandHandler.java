package com.awslabs.iot.client.commands.iot.certificates;

import com.awslabs.aws.iot.resultsiterator.helpers.interfaces.IoHelper;
import com.awslabs.aws.iot.resultsiterator.helpers.v1.interfaces.V1CertificateHelper;
import com.awslabs.aws.iot.resultsiterator.helpers.v1.interfaces.V1ThingHelper;
import com.awslabs.iot.client.commands.iot.IotCommandHandler;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;

public class DeleteAllCertificatesCommandHandler implements IotCommandHandler {
    private static final String DELETEALLCERTIFICATES = "delete-all-certificates";
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(DeleteAllCertificatesCommandHandler.class);
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;
    @Inject
    Provider<V1CertificateHelper> certificateHelperProvider;
    @Inject
    Provider<V1ThingHelper> thingHelperProvider;

    @Inject
    public DeleteAllCertificatesCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        List<String> certificateArns = certificateHelperProvider.get().listCertificateArns();

        for (String certificateArn : certificateArns) {
            thingHelperProvider.get().deletePrincipal(certificateArn);
        }
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

    public IoHelper getIoHelper() {
        return this.ioHelper;
    }
}
