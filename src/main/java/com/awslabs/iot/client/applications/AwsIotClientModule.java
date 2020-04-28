package com.awslabs.iot.client.applications;

import com.awslabs.iot.client.commands.BasicCommandHandlerProvider;
import com.awslabs.iot.client.commands.CommandHandlerProvider;
import com.awslabs.iot.client.commands.generic.ExitCommandHandler;
import com.awslabs.iot.client.commands.generic.HelpCommandHandler;
import com.awslabs.iot.client.commands.generic.QuitCommandHandler;
import com.awslabs.iot.client.commands.interfaces.CommandHandler;
import com.awslabs.iot.client.console.AwsIotClientConsoleTerminal;
import com.awslabs.iot.client.helpers.BasicCandidateHelper;
import com.awslabs.iot.client.helpers.CandidateHelper;
import com.awslabs.iot.client.helpers.json.BasicObjectPrettyPrinter;
import com.awslabs.iot.client.helpers.json.interfaces.ObjectPrettyPrinter;
import com.awslabs.iot.client.helpers.progressbar.BasicProgressBarHelper;
import com.awslabs.iot.client.helpers.progressbar.ProgressBarHelper;
import com.awslabs.iot.client.interfaces.AwsIotClientTerminal;
import com.awslabs.iot.client.parameters.BasicParameterExtractor;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import com.awslabs.iot.data.*;
import com.awslabs.resultsiterator.v2.V2HelperModule;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.ElementsIntoSet;

import javax.inject.Singleton;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Module(includes = {GreengrassModule.class, IotModule.class, LogsModule.class, LambdaModule.class, V2HelperModule.class})
public class AwsIotClientModule {
    private static final String CA_CERT_FILENAME = "ca.crt";
    private static final String CLIENT_CERT_FILENAME = "client.crt";
    private static final String CLIENT_PRIVATE_KEY_FILENAME = "private.key";
    private static final String CERTIFICATE_ID_FILENAME = "certificate.id";
    private static final String CLIENT_ID = "aws-iot-client";
    private static final String CLIENT_NAME = "aws-iot-client";

    // Constants
    @Provides
    @Singleton
    public Arguments arguments() {
        return new Arguments();
    }

    @Provides
    @Singleton
    public CaCertFilename caCertFilename() {
        return ImmutableCaCertFilename.builder().caCertFilename(CA_CERT_FILENAME).build();
    }

    @Provides
    @Singleton
    public ClientCertFilename clientCertFilename() {
        return ImmutableClientCertFilename.builder().clientCertFilename(CLIENT_CERT_FILENAME).build();
    }

    @Provides
    @Singleton
    public ClientPrivateKeyFilename clientPrivateKeyFilename() {
        return ImmutableClientPrivateKeyFilename.builder().clientPrivateKeyFilename(CLIENT_PRIVATE_KEY_FILENAME).build();
    }

    @Provides
    @Singleton
    public CertificateIdFilename certificateIdFilename() {
        return ImmutableCertificateIdFilename.builder().certificateIdFilename(CERTIFICATE_ID_FILENAME).build();
    }

    @Provides
    @Singleton
    public ClientId clientId() {
        return ImmutableClientId.builder().clientId(CLIENT_ID).build();
    }

    @Provides
    @Singleton
    public ClientName clientName() {
        return ImmutableClientName.builder().clientName(CLIENT_NAME).build();
    }

    // Normal bindings
    @Provides
    public CommandHandlerProvider CommandHandlerProvider(BasicCommandHandlerProvider basicCommandHandlerProvider) {
        return basicCommandHandlerProvider;
    }

    @Provides
    public AwsIotClientTerminal awsIotClientTerminal(AwsIotClientConsoleTerminal awsIotClientConsoleTerminal) {
        return awsIotClientConsoleTerminal;
    }

    @Provides
    public CandidateHelper candidateHelper(BasicCandidateHelper basicCandidateHelper) {
        return basicCandidateHelper;
    }

    @Provides
    public ObjectPrettyPrinter objectPrettyPrinter(BasicObjectPrettyPrinter basicObjectPrettyPrinter) {
        return basicObjectPrettyPrinter;
    }

    @Provides
    public ParameterExtractor parameterExtractor(BasicParameterExtractor basicParameterExtractor) {
        return basicParameterExtractor;
    }

    // Command handler multibindings
    @Provides
    @ElementsIntoSet
    public Set<CommandHandler> commandHandlerSet(HelpCommandHandler helpCommandHandler,
                                                 ExitCommandHandler exitCommandHandler,
                                                 QuitCommandHandler quitCommandHandler) {
        return new HashSet<>(Arrays.asList(helpCommandHandler,
                exitCommandHandler,
                quitCommandHandler));
    }

    @Provides
    public ProgressBarHelper progressBarHelper(BasicProgressBarHelper basicProgressBarHelper) {
        return basicProgressBarHelper;
    }
}
