package com.awslabs.iot.client.applications;

import com.amazonaws.services.greengrass.AWSGreengrassClient;
import com.amazonaws.services.greengrass.AWSGreengrassClientBuilder;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClient;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClientBuilder;
import com.amazonaws.services.iot.AWSIotAsyncClientBuilder;
import com.amazonaws.services.iot.AWSIotClient;
import com.amazonaws.services.iotdata.AWSIotDataClient;
import com.amazonaws.services.iotdata.AWSIotDataClientBuilder;
import com.awslabs.iot.client.commands.BasicCommandHandlerProvider;
import com.awslabs.iot.client.commands.CommandHandlerProvider;
import com.awslabs.iot.client.commands.generic.ExitCommandHandler;
import com.awslabs.iot.client.commands.generic.HelpCommandHandler;
import com.awslabs.iot.client.commands.generic.QuitCommandHandler;
import com.awslabs.iot.client.commands.interfaces.CommandHandler;
import com.awslabs.iot.client.console.AwsIotClientConsoleTerminal;
import com.awslabs.iot.client.data.*;
import com.awslabs.iot.client.helpers.BasicCandidateHelper;
import com.awslabs.iot.client.helpers.CandidateHelper;
import com.awslabs.iot.client.helpers.greengrass.BasicGreengrassHelper;
import com.awslabs.iot.client.helpers.greengrass.interfaces.GreengrassHelper;
import com.awslabs.iot.client.helpers.iam.BasicIamHelper;
import com.awslabs.iot.client.helpers.iam.interfaces.IamHelper;
import com.awslabs.iot.client.helpers.iot.*;
import com.awslabs.iot.client.helpers.iot.interfaces.*;
import com.awslabs.iot.client.helpers.json.BasicObjectPrettyPrinter;
import com.awslabs.iot.client.helpers.json.interfaces.ObjectPrettyPrinter;
import com.awslabs.iot.client.interfaces.AwsIotClientTerminal;
import com.awslabs.iot.client.parameters.BasicParameterExtractor;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

class AwsIotClientModule extends AbstractModule {
    private static final String CA_CERT_FILENAME = "ca.crt";
    private static final String CLIENT_CERT_FILENAME = "client.crt";
    private static final String CLIENT_PRIVATE_KEY_FILENAME = "private.key";
    private static final String CERTIFICATE_ID_FILENAME = "certificate.id";
    private static final String CLIENT_ID = "aws-iot-client";
    private static final String CLIENT_NAME = "aws-iot-client";

    static Arguments arguments = new Arguments();

    @Override
    protected void configure() {
        // Client providers
        bind(AWSIotClient.class).toProvider(() -> (AWSIotClient) AWSIotAsyncClientBuilder.defaultClient());
        bind(AWSIotDataClient.class).toProvider(() -> (AWSIotDataClient) AWSIotDataClientBuilder.defaultClient());
        bind(AmazonIdentityManagementClient.class).toProvider(() -> (AmazonIdentityManagementClient) AmazonIdentityManagementClientBuilder.defaultClient());
        bind(AWSGreengrassClient.class).toProvider(() -> (AWSGreengrassClient) AWSGreengrassClientBuilder.defaultClient());

        // Constants
        bind(Arguments.class).toInstance(arguments);
        bind(CaCertFilename.class).toInstance(CaCertFilename.builder().caCertFilename(CA_CERT_FILENAME).build());
        bind(ClientCertFilename.class).toInstance(ClientCertFilename.builder().clientCertFilename(CLIENT_CERT_FILENAME).build());
        bind(ClientPrivateKeyFilename.class).toInstance(ClientPrivateKeyFilename.builder().clientPrivateKeyFilename(CLIENT_PRIVATE_KEY_FILENAME).build());
        bind(CertificateIdFilename.class).toInstance(CertificateIdFilename.builder().certificateIdFilename(CERTIFICATE_ID_FILENAME).build());
        bind(ClientId.class).toInstance(ClientId.builder().clientId(CLIENT_ID).build());
        bind(ClientName.class).toInstance(ClientName.builder().clientName(CLIENT_NAME).build());

        // Normal bindings
        bind(CommandHandlerProvider.class).to(BasicCommandHandlerProvider.class);
        bind(AwsIotClientTerminal.class).to(AwsIotClientConsoleTerminal.class);
        bind(RuleHelper.class).to(BasicRuleHelper.class);
        bind(IamHelper.class).to(BasicIamHelper.class);
        bind(GreengrassHelper.class).to(BasicGreengrassHelper.class);
        bind(CandidateHelper.class).to(BasicCandidateHelper.class);
        bind(ObjectPrettyPrinter.class).to(BasicObjectPrettyPrinter.class);
        bind(CertificateHelper.class).to(BasicCertificateHelper.class);
        bind(ThingHelper.class).to(BasicThingHelper.class);
        bind(PolicyHelper.class).to(BasicPolicyHelper.class);
        bind(ParameterExtractor.class).to(BasicParameterExtractor.class);
        bind(ThingGroupHelper.class).to(BasicThingGroupHelper.class);

        // Command handler multibindings
        Multibinder<CommandHandler> commandHandlerMultibinder = Multibinder.newSetBinder(binder(), CommandHandler.class);
        commandHandlerMultibinder.addBinding().to(HelpCommandHandler.class);
        commandHandlerMultibinder.addBinding().to(ExitCommandHandler.class);
        commandHandlerMultibinder.addBinding().to(QuitCommandHandler.class);
    }
}
