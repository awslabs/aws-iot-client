package com.awslabs.iot.client.commands.iot.certificates;

import com.awslabs.general.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.commands.iot.IotCommandHandler;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.helpers.interfaces.V2IotHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.greengrass.model.GroupInformation;
import software.amazon.awssdk.services.iot.model.Certificate;

import javax.inject.Inject;
import java.util.Comparator;

public class DeleteAllCertificatesCommandHandler implements IotCommandHandler {
    private static final String DELETEALLCERTIFICATES = "delete-all-certificates";
    private static final Logger log = LoggerFactory.getLogger(DeleteAllCertificatesCommandHandler.class);
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;
    @Inject
    V2IotHelper v2IotHelper;

    @Inject
    public DeleteAllCertificatesCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        v2IotHelper.getCertificates()
                // Sort the certificates by ID so we can get a general sense of how far along we are in the process of deleting them
                .sorted(Comparator.comparing(Certificate::certificateId))
                .forEach(certificate -> v2IotHelper.recursiveDelete(certificate));
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
