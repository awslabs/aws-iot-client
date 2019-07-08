package com.awslabs.iot.client.commands.iot.certificates;

import com.awslabs.iot.client.commands.iot.IotCommandHandler;
import com.awslabs.iot.client.helpers.io.interfaces.IOHelper;
import com.awslabs.iot.client.helpers.iot.interfaces.CertificateHelper;
import com.awslabs.iot.client.parameters.interfaces.ParameterExtractor;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;

public class ListCertificateIdsCommandHandler implements IotCommandHandler {
    private static final String LISTCERTIFICATEIDS = "list-certificate-ids";
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ListCertificateIdsCommandHandler.class);
    @Inject
    Provider<CertificateHelper> certificateHelperProvider;
    @Inject
    ParameterExtractor parameterExtractor;
    @Inject
    IOHelper ioHelper;

    @Inject
    public ListCertificateIdsCommandHandler() {
    }

    @Override
    public void innerHandle(String input) {
        List<String> certificateIds = certificateHelperProvider.get().listCertificateIds();

        for (String certificateId : certificateIds) {
            log.info("  [" + certificateId + "]");
        }
    }

    @Override
    public String getCommandString() {
        return LISTCERTIFICATEIDS;
    }

    @Override
    public String getHelp() {
        return "Lists all certificate IDs.";
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
