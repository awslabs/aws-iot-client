package com.awslabs.iot.client.commands.iot.certificates;


import com.awslabs.iot.client.commands.iot.IotCommandHandler;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.data.*;
import com.awslabs.iot.helpers.interfaces.IotHelper;
import com.jcabi.log.Logger;
import io.vavr.collection.Stream;
import software.amazon.awssdk.services.iot.model.Certificate;
import software.amazon.awssdk.services.iot.model.Policy;

import javax.inject.Inject;

public class CleanUpCertificatesCommandHandler implements IotCommandHandler {
    private static final String CLEANUPCERTIFICATES = "clean-up-certificates";
    @Inject
    IotHelper iotHelper;
    @Inject
    ParameterExtractor parameterExtractor;

    @Inject
    public CleanUpCertificatesCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        iotHelper.getCertificates().forEach(this::deleteEach);
    }

    private void deleteEach(Certificate certificate) {
        CertificateArn certificateArn = ImmutableCertificateArn.builder().arn(certificate.certificateArn()).build();
        CertificateId certificateId = ImmutableCertificateId.builder().id(certificate.certificateId()).build();

        Stream<ThingName> attachedThings = iotHelper.getAttachedThings(certificateArn);

        if (attachedThings.nonEmpty()) {
            Logger.info(this, String.join("", "Ignoring [", certificateId.getId(), "], it still has things attached to it"));
            return;
        }

        Stream<Policy> attachedPolicies = iotHelper.getAttachedPolicies(certificateArn);

        if (attachedPolicies.nonEmpty()) {
            Logger.info(this, String.join("", "Ignoring [", certificateId.getId(), "], it still has policies attached to it"));
            return;
        }

        Logger.info(this, String.join("", "Deleting [", certificateId.getId(), "]"));

        iotHelper.delete(certificateArn);
    }

    @Override
    public String getCommandString() {
        return CLEANUPCERTIFICATES;
    }

    @Override
    public String getHelp() {
        return "Deletes any certificates that have no policies or things attached to them.";
    }

    @Override
    public int requiredParameters() {
        return 0;
    }

    public ParameterExtractor getParameterExtractor() {
        return this.parameterExtractor;
    }
}
