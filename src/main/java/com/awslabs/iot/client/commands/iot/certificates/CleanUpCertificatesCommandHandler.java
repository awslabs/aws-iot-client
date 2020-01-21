package com.awslabs.iot.client.commands.iot.certificates;

import com.amazonaws.services.iot.model.Certificate;
import com.amazonaws.services.iot.model.Policy;
import com.awslabs.aws.iot.resultsiterator.helpers.interfaces.IoHelper;
import com.awslabs.aws.iot.resultsiterator.helpers.v1.interfaces.V1CertificateHelper;
import com.awslabs.aws.iot.resultsiterator.helpers.v1.interfaces.V1PolicyHelper;
import com.awslabs.aws.iot.resultsiterator.helpers.v1.interfaces.V1ThingHelper;
import com.awslabs.iot.client.commands.iot.IotCommandHandler;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;

public class CleanUpCertificatesCommandHandler implements IotCommandHandler {
    private static final String CLEANUPCERTIFICATES = "clean-up-certificates";
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(CleanUpCertificatesCommandHandler.class);
    @Inject
    Provider<V1CertificateHelper> certificateHelperProvider;
    @Inject
    Provider<V1ThingHelper> thingHelperProvider;
    @Inject
    Provider<V1PolicyHelper> policyHelperProvider;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IoHelper ioHelper;

    @Inject
    public CleanUpCertificatesCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        List<Certificate> certificates = certificateHelperProvider.get().listCertificates();

        for (Certificate certificate : certificates) {
            String certificateArn = certificate.getCertificateArn();
            String certificateId = certificate.getCertificateId();

            List<String> principalThings = thingHelperProvider.get().listPrincipalThings(certificateArn);

            if (principalThings.size() > 0) {
                log.info("Ignoring [" + certificateId + "], it still has things attached to it");
                continue;
            }

            List<Policy> principalPolicies = policyHelperProvider.get().listPrincipalPolicies(certificateArn);

            if (principalPolicies.size() > 0) {
                log.info("Ignoring [" + certificateId + "], it still has policies attached to it");
                continue;
            }

            log.info("Deleting [" + certificateId + "]");

            thingHelperProvider.get().deletePrincipal(certificateArn);
        }
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
