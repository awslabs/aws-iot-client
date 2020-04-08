package com.awslabs.iot.client.commands.iot.certificates;

import com.awslabs.general.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.commands.iot.IotCommandHandler;
import com.awslabs.iot.client.commands.iot.completers.CertificateCompleter;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.helpers.interfaces.V1CertificateHelper;
import com.awslabs.iot.helpers.interfaces.V1ThingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;

public class DeleteUnattachedCertificatesCommandHandlerWithCompletion implements IotCommandHandler {
    private static final String DELETEUNATTACHEDCERTIFICATES = "delete-unattached-certificates";
    private static final Logger log = LoggerFactory.getLogger(DeleteUnattachedCertificatesCommandHandlerWithCompletion.class);
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;
    @Inject
    CertificateCompleter certificateCompleter;
    @Inject
    Provider<V1CertificateHelper> certificateHelperProvider;
    @Inject
    Provider<V1ThingHelper> thingHelperProvider;

    @Inject
    public DeleteUnattachedCertificatesCommandHandlerWithCompletion() {
    }

    @Override
    public void innerHandle(String input) {
        certificateHelperProvider.get().getUnattachedCertificateArns()
                .forEach(unattachedCertificateArn -> thingHelperProvider.get().deletePrincipal(unattachedCertificateArn));
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

    public IoHelper getIoHelper() {
        return this.ioHelper;
    }

    public CertificateCompleter getCertificateCompleter() {
        return this.certificateCompleter;
    }
}
