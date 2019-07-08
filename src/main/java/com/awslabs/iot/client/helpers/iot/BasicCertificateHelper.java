package com.awslabs.iot.client.helpers.iot;

import com.amazonaws.services.iot.AWSIotClient;
import com.amazonaws.services.iot.model.*;
import com.awslabs.aws.iot.resultsiterator.ResultsIterator;
import com.awslabs.iot.client.data.CertificateIdFilename;
import com.awslabs.iot.client.data.ClientCertFilename;
import com.awslabs.iot.client.data.ClientPrivateKeyFilename;
import com.awslabs.iot.client.helpers.io.interfaces.IOHelper;
import com.awslabs.iot.client.helpers.iot.interfaces.CertificateHelper;
import com.awslabs.iot.client.helpers.iot.interfaces.PolicyHelper;
import com.awslabs.iot.client.helpers.iot.interfaces.ThingHelper;
import io.vavr.control.Try;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;

public class BasicCertificateHelper implements CertificateHelper {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(BasicCertificateHelper.class);
    @Inject
    AWSIotClient awsIotClient;
    @Inject
    IOHelper ioHelper;
    @Inject
    Provider<ThingHelper> thingHelperProvider;
    @Inject
    Provider<PolicyHelper> policyHelperProvider;
    @Inject
    ClientCertFilename clientCertFilename;
    @Inject
    ClientPrivateKeyFilename clientPrivateKeyFilename;
    @Inject
    CertificateIdFilename certificateIdFilename;

    @Inject
    public BasicCertificateHelper() {
    }

    @Override
    public CreateKeysAndCertificateResult setupCertificate() {
        return Try.of(() -> {
            CreateKeysAndCertificateRequest createKeysAndCertificateRequest = new CreateKeysAndCertificateRequest()
                    .withSetAsActive(true);
            CreateKeysAndCertificateResult createKeysAndCertificateResult = awsIotClient.createKeysAndCertificate(createKeysAndCertificateRequest);

            ioHelper.writeFile(clientPrivateKeyFilename.getClientPrivateKeyFilename(), createKeysAndCertificateResult.getKeyPair().getPrivateKey());
            ioHelper.writeFile(clientCertFilename.getClientCertFilename(), createKeysAndCertificateResult.getCertificatePem());
            ioHelper.writeFile(certificateIdFilename.getCertificateIdFilename(), createKeysAndCertificateResult.getCertificateId());

            return createKeysAndCertificateResult;
        })
                .recover(Exception.class, throwable -> {
                    log.info("Failed to create the keys and certificate.  Do you have the correct permissions to call iot:CreateKeysAndCertificate?");
                    throw new RuntimeException(throwable);
                })
                .get();
    }

    @Override
    public List<Certificate> listCertificates() {
        List<Certificate> certificates = new ResultsIterator<Certificate>(awsIotClient, ListCertificatesRequest.class, ListCertificatesResult.class).iterateOverResults();

        return certificates;
    }

    @Override
    public List<CACertificate> listCaCertificates() {
        List<CACertificate> certificates = new ResultsIterator<CACertificate>(awsIotClient, ListCACertificatesRequest.class, ListCACertificatesResult.class).iterateOverResults();

        return certificates;
    }

    @Override
    public List<String> listCaCertificateIds() {
        List<CACertificate> caCertificates = listCaCertificates();

        List<String> caCertificateIds = new ArrayList<>();

        for (CACertificate caCertificate : caCertificates) {
            caCertificateIds.add(caCertificate.getCertificateId());
        }

        return caCertificateIds;
    }

    @Override
    public List<String> listCaCertificateArns() {
        List<CACertificate> caCertificates = listCaCertificates();

        List<String> caCertificateArns = new ArrayList<>();

        for (CACertificate caCertificate : caCertificates) {
            // Note: API appears to return a ":cert/" ARN when it should return a ":cacert/" ARN, we fix this
            caCertificateArns.add(caCertificate.getCertificateArn().replace(CertificateHelper.CERT_IDENTIFIER, CertificateHelper.CACERT_IDENTIFIER));
        }

        return caCertificateArns;
    }

    @Override
    public List<String> getUnattachedCertificateArns() {
        List<String> certificateArns = listCertificateArns();

        List<String> unattachedCertificateArns = new ArrayList<>();

        for (String certificateArn : certificateArns) {
            List<String> principalThings = thingHelperProvider.get().listPrincipalThings(certificateArn);

            if (principalThings.size() != 0) {
                continue;
            }

            List<Policy> principalPolicies = policyHelperProvider.get().listPrincipalPolicies(certificateArn);

            if (principalPolicies.size() != 0) {
                continue;
            }

            unattachedCertificateArns.add(certificateArn);
        }

        return unattachedCertificateArns;
    }

    @Override
    public void attachCertificateToThing(String certificateArn, String thingName) {
        awsIotClient.attachThingPrincipal(new AttachThingPrincipalRequest()
                .withPrincipal(certificateArn)
                .withThingName(thingName));
    }

    @Override
    public List<String> listCertificateIds() {
        List<Certificate> certificates = listCertificates();

        List<String> certificateIds = new ArrayList<>();

        for (Certificate certificate : certificates) {
            certificateIds.add(certificate.getCertificateId());
        }

        return certificateIds;
    }

    @Override
    public List<String> listCertificateArns() {
        List<Certificate> certificates = listCertificates();

        List<String> certificateArns = new ArrayList<>();

        for (Certificate certificate : certificates) {
            certificateArns.add(certificate.getCertificateArn());
        }

        return certificateArns;
    }
}
