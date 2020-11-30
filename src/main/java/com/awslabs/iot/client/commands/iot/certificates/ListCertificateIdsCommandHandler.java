package com.awslabs.iot.client.commands.iot.certificates;

import com.awslabs.general.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.commands.iot.IotCommandHandler;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.helpers.interfaces.V2IotHelper;
import com.jcabi.log.Logger;
import software.amazon.awssdk.services.iot.model.Certificate;

import javax.inject.Inject;

public class ListCertificateIdsCommandHandler implements IotCommandHandler {
    private static final String LISTCERTIFICATEIDS = "list-certificate-ids";
    @Inject
    V2IotHelper v2IotHelper;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;

    @Inject
    public ListCertificateIdsCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        v2IotHelper.getCertificates()
                .map(Certificate::certificateId)
                .forEach(certificateId -> Logger.info(this, String.join("", "  [", certificateId, "]")));
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
