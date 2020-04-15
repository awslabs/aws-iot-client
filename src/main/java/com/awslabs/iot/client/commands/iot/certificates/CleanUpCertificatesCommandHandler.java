package com.awslabs.iot.client.commands.iot.certificates;

import com.awslabs.general.helpers.interfaces.IoHelper;
import com.awslabs.iot.client.commands.iot.IotCommandHandler;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.data.*;
import com.awslabs.iot.helpers.interfaces.V2IotHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.iot.model.Certificate;
import software.amazon.awssdk.services.iot.model.Policy;

import javax.inject.Inject;
import java.util.stream.Stream;

public class CleanUpCertificatesCommandHandler implements IotCommandHandler {
    private static final String CLEANUPCERTIFICATES = "clean-up-certificates";
    private static final Logger log = LoggerFactory.getLogger(CleanUpCertificatesCommandHandler.class);
    @Inject
    V2IotHelper v2IotHelper;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;

    @Inject
    public CleanUpCertificatesCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        v2IotHelper.getCertificates().forEach(this::deleteEach);
    }

    private void deleteEach(Certificate certificate) {
        CertificateArn certificateArn = ImmutableCertificateArn.builder().arn(certificate.certificateArn()).build();
        CertificateId certificateId = ImmutableCertificateId.builder().id(certificate.certificateId()).build();

        Stream<ThingName> attachedThings = v2IotHelper.getAttachedThings(certificateArn);

        if (attachedThings.findAny().isPresent()) {
            log.info(String.join("", "Ignoring [", certificateId.getId(), "], it still has things attached to it"));
            return;
        }

        Stream<Policy> attachedPolicies = v2IotHelper.getAttachedPolicies(certificateArn);

        if (attachedPolicies.findAny().isPresent()) {
            log.info(String.join("", "Ignoring [", certificateId.getId(), "], it still has policies attached to it"));
            return;
        }

        log.info(String.join("", "Deleting [", certificateId.getId(), "]"));

        v2IotHelper.delete(certificateArn);
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

    public IoHelper getIoHelper() {
        return this.ioHelper;
    }
}
