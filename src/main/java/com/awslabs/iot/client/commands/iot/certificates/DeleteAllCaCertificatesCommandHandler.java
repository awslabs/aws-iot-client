package com.awslabs.iot.client.commands.iot.certificates;

import com.awslabs.general.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.commands.iot.IotCommandHandler;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.helpers.interfaces.V1CertificateHelper;
import com.awslabs.iot.helpers.interfaces.V1ThingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;

public class DeleteAllCaCertificatesCommandHandler implements IotCommandHandler {
    private static final String DELETEALLCACERTIFICATES = "delete-all-ca-certificates";
    private static final Logger log = LoggerFactory.getLogger(DeleteAllCaCertificatesCommandHandler.class);
    @Inject
    Provider<V1CertificateHelper> certificateHelperProvider;
    @Inject
    Provider<V1ThingHelper> thingHelperProvider;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;

    @Inject
    public DeleteAllCaCertificatesCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        certificateHelperProvider.get().listCaCertificateArns()
                .forEach(caCertificateArn -> thingHelperProvider.get().deletePrincipal(caCertificateArn));
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
