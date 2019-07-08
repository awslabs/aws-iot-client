package com.awslabs.iot.client.commands.iot.certificates;

import com.awslabs.iot.client.commands.iot.IotCommandHandler;
import com.awslabs.iot.client.helpers.io.interfaces.IOHelper;
import com.awslabs.iot.client.helpers.iot.interfaces.CertificateHelper;
import com.awslabs.iot.client.helpers.iot.interfaces.ThingHelper;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;

public class DeleteAllCertificatesCommandHandler implements IotCommandHandler {
    private static final String DELETEALLCERTIFICATES = "delete-all-certificates";
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(DeleteAllCertificatesCommandHandler.class);
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IOHelper ioHelper;
    @Inject
    Provider<CertificateHelper> certificateHelperProvider;
    @Inject
    Provider<ThingHelper> thingHelperProvider;

    @Inject
    public DeleteAllCertificatesCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        List<String> certificateArns = certificateHelperProvider.get().listCertificateArns();

        for (String certificateArn : certificateArns) {
            thingHelperProvider.get().deletePrincipal(certificateArn);
        }
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

    public IOHelper getIoHelper() {
        return this.ioHelper;
    }
}
