package com.awslabs.iot.client.commands.iot.certificates;

import com.awslabs.general.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.commands.iot.IotCommandHandler;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.helpers.interfaces.V1CertificateHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;

public class ListCertificateIdsCommandHandler implements IotCommandHandler {
    private static final String LISTCERTIFICATEIDS = "list-certificate-ids";
    private static final Logger log = LoggerFactory.getLogger(ListCertificateIdsCommandHandler.class);
    @Inject
    Provider<V1CertificateHelper> certificateHelperProvider;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;

    @Inject
    public ListCertificateIdsCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        certificateHelperProvider.get().listCertificateIds()
                .forEach(certificateId -> log.info("  [" + certificateId + "]"));
    }

    @Override
    public String getCommandString() {
        return LISTCERTIFICATEIDS;
    }

    @Override
    public String getHelp() {
        return "Lists all certificate IDs.";
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
