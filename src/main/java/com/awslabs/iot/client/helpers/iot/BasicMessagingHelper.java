package com.awslabs.iot.client.helpers.iot;

import com.amazonaws.services.iot.AWSIotClient;
import com.amazonaws.services.iot.model.*;
import com.awslabs.iot.client.data.*;
import com.awslabs.iot.client.helpers.io.interfaces.IOHelper;
import com.awslabs.iot.client.helpers.iot.interfaces.CertificateHelper;
import com.awslabs.iot.client.helpers.iot.interfaces.MessagingHelper;
import com.awslabs.iot.client.helpers.iot.interfaces.PolicyHelper;
import io.vavr.control.Try;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.File;
import java.nio.file.Files;
import java.util.Optional;

public class BasicMessagingHelper implements MessagingHelper {
    private static final String ROOT_CA_CERT_URL = "https://www.symantec.com/content/en/us/enterprise/verisign/roots/VeriSign-Class%203-Public-Primary-Certification-Authority-G5.pem";
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(BasicMessagingHelper.class);

    @Inject
    CaCertFilename caCertFilename;
    @Inject
    ClientCertFilename clientCertFilename;
    @Inject
    ClientPrivateKeyFilename clientPrivateKeyFilename;
    @Inject
    CertificateIdFilename certificateIdFilename;
    @Inject
    IOHelper ioHelper;
    @Inject
    ClientName clientName;
    @Inject
    AWSIotClient awsIotClient;
    @Inject
    Provider<CertificateHelper> certificateHelperProvider;
    @Inject
    Provider<PolicyHelper> policyHelperProvider;
    private Optional<String> endpointAddress = Optional.empty();

    @Inject
    public BasicMessagingHelper() {
    }

    @Override
    public String getEndpointAddress() {
        if (!endpointAddress.isPresent()) {
            DescribeEndpointRequest describeEndpointRequest = new DescribeEndpointRequest();
            DescribeEndpointResult describeEndpointResult = awsIotClient.describeEndpoint(describeEndpointRequest);
            endpointAddress = Optional.ofNullable(describeEndpointResult.getEndpointAddress());
        }

        return endpointAddress.get();
    }

    @Override
    public int getEndpointPort() {
        return 8883;
    }

    private void write(String message) {
        log.info(message);
    }

    @Override
    public void doSetupIfNecessary() {
        if (!ioHelper.exists(caCertFilename.getCaCertFilename())) {
            write("CA cert not found, downloading");
            ioHelper.download(ROOT_CA_CERT_URL, caCertFilename.getCaCertFilename());
        }

        if (ioHelper.exists(certificateIdFilename.getCertificateIdFilename())) {
            String certificateId = ioHelper.readFile(certificateIdFilename.getCertificateIdFilename());

            Try.of(() -> {
                DescribeCertificateRequest describeCertificateRequest = new DescribeCertificateRequest()
                        .withCertificateId(certificateId);

                write("You have an existing certificate ID, checking to see if it still exists in AWS IoT");
                awsIotClient.describeCertificate(describeCertificateRequest);

                write("Certificate still exists, checking to see if policy still exists");
                GetPolicyRequest getPolicyRequest = new GetPolicyRequest()
                        .withPolicyName(clientName.clientName);
                awsIotClient.getPolicy(getPolicyRequest);
                write("Policy still exists");

                return null;
            })
                    .recover(ResourceNotFoundException.class, throwable -> Try.of(() -> {
                        log.warn("Your last certificate or policy was not found in your AWS IoT resources, deleting the local copy and creating a new one");

                        Files.delete(new File(certificateIdFilename.getCertificateIdFilename()).toPath());

                        if (ioHelper.exists(clientCertFilename.getClientCertFilename())) {
                            Files.delete(new File(clientCertFilename.getClientCertFilename()).toPath());
                        }

                        if (ioHelper.exists(clientPrivateKeyFilename.getClientPrivateKeyFilename())) {
                            Files.delete(new File(clientPrivateKeyFilename.getClientPrivateKeyFilename()).toPath());
                        }

                        return null;
                    })
                            .get())
                    .get();
        }

        if (!ioHelper.exists(certificateIdFilename.getCertificateIdFilename()) ||
                !ioHelper.exists(clientCertFilename.getClientCertFilename()) ||
                !ioHelper.exists(clientPrivateKeyFilename.getClientPrivateKeyFilename())) {
            write("Signed client certificate and private key file do not exist.  Will attempt to create these for you...");

            PolicyHelper policyHelper = policyHelperProvider.get();
            CertificateHelper certificateHelper = certificateHelperProvider.get();

            // Get keys and certificate from the service
            write("Getting keys and certificate from AWS IoT...");
            CreateKeysAndCertificateResult result = certificateHelper.setupCertificate();

            // Create a policy for our client
            write("Creating a permissive policy for your newly minted certificate...");
            policyHelper.createAllowAllPolicy(clientName.clientName);

            // Attach the policy to our certificate
            write("Attaching that policy to your certificate...");
            policyHelper.attachPolicyToCertificate(clientName.clientName, result.getCertificateArn());

            // Create a thing
            awsIotClient.createThing(new CreateThingRequest()
                    .withThingName(clientName.clientName)
                    .withAttributePayload(new AttributePayload().addAttributesEntry("immutable", "immutable")));

            // Attach the certificate to the thing
            certificateHelper.attachCertificateToThing(result.getCertificateArn(), clientName.clientName);
            write("Setup looks like it is complete");
        } else {
            write("You already have a certificate and private key.  We'll try to use that.  If it fails try deleting those files so we can create new ones.");
        }
    }
}
