package com.awslabs.iot.client.commands.iot.certificates;


import com.awslabs.iot.client.commands.iot.IotCommandHandler;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.helpers.interfaces.IotHelper;
import com.jcabi.log.Logger;
import software.amazon.awssdk.services.iot.model.Certificate;

import javax.inject.Inject;

public class ListCertificateArnsCommandHandler implements IotCommandHandler {
    private static final String LISTCERTIFICATEARNS = "list-certificate-arns";
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IotHelper iotHelper;

    @Inject
    public ListCertificateArnsCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        iotHelper.getCertificates().map(Certificate::certificateArn)
                .forEach(certificateArn -> Logger.info(this, String.join("", "  [", certificateArn, "]")));
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
}
