package com.awslabs.iot.client.commands.iot.certificates;

import com.awslabs.iot.client.commands.iot.IotCommandHandler;
import com.awslabs.iot.client.commands.iot.completers.CertificateCompleter;
import com.awslabs.iot.client.helpers.io.interfaces.IOHelper;
import com.awslabs.iot.client.helpers.iot.interfaces.CertificateHelper;
import com.awslabs.iot.client.helpers.iot.interfaces.ThingHelper;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;

public class DeleteUnattachedCertificatesCommandHandlerWithCompletion implements IotCommandHandler {
    private static final String DELETEUNATTACHEDCERTIFICATES = "delete-unattached-certificates";
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(DeleteUnattachedCertificatesCommandHandlerWithCompletion.class);
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IOHelper ioHelper;
    @Inject
    CertificateCompleter certificateCompleter;
    @Inject
    Provider<CertificateHelper> certificateHelperProvider;
    @Inject
    Provider<ThingHelper> thingHelperProvider;

    @Inject
    public DeleteUnattachedCertificatesCommandHandlerWithCompletion() {
    }

    @Override
    public void innerHandle(String input) {
        List<String> unattachedCertificateArns = certificateHelperProvider.get().getUnattachedCertificateArns();

        for (String unattachedCertificateArn : unattachedCertificateArns) {
            thingHelperProvider.get().deletePrincipal(unattachedCertificateArn);
        }
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

    public IOHelper getIoHelper() {
        return this.ioHelper;
    }

    public CertificateCompleter getCertificateCompleter() {
        return this.certificateCompleter;
    }
}
