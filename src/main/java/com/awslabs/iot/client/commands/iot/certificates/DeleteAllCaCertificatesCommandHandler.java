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

public class DeleteAllCaCertificatesCommandHandler implements IotCommandHandler {
    private static final String DELETEALLCACERTIFICATES = "delete-all-ca-certificates";
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(DeleteAllCaCertificatesCommandHandler.class);
    @Inject
    Provider<CertificateHelper> certificateHelperProvider;
    @Inject
    Provider<ThingHelper> thingHelperProvider;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IOHelper ioHelper;

    @Inject
    public DeleteAllCaCertificatesCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        List<String> caCertificateArns = certificateHelperProvider.get().listCaCertificateArns();

        for (String caCertificateArn : caCertificateArns) {
            thingHelperProvider.get().deletePrincipal(caCertificateArn);
        }
    }

    @Override
    public String getCommandString() {
        return DELETEALLCACERTIFICATES;
    }

    @Override
    public String getHelp() {
        return "Deletes all CA certificates.";
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
