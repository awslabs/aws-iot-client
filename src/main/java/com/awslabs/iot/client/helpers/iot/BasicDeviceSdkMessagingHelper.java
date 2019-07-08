package com.awslabs.iot.client.helpers.iot;

import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotTopic;
import com.amazonaws.services.iot.client.sample.sampleUtil.SampleUtil;
import com.awslabs.iot.client.data.ClientCertFilename;
import com.awslabs.iot.client.data.ClientId;
import com.awslabs.iot.client.data.ClientPrivateKeyFilename;
import com.awslabs.iot.client.helpers.iot.interfaces.DeviceSdkMessagingHelper;
import com.awslabs.iot.client.helpers.iot.interfaces.MessagingHelper;
import io.vavr.control.Try;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.Optional;

public class BasicDeviceSdkMessagingHelper implements DeviceSdkMessagingHelper {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(BasicDeviceSdkMessagingHelper.class);
    @Inject
    ClientCertFilename clientCertFilename;
    @Inject
    ClientPrivateKeyFilename clientPrivateKeyFilename;
    @Inject
    ClientId clientId;
    @Inject
    MessagingHelper messagingHelper;
    private Optional<AWSIotMqttClient> client = Optional.empty();

    @Inject
    public BasicDeviceSdkMessagingHelper() {
    }

    @Override
    public void publish(String topic, String message) {
        Try.of(() -> {
            getClient().publish(topic, message, 500);
            return null;
        })
                .get();
    }

    @Override
    public void subscribe(AWSIotTopic topic) {
        Try.of(() -> {
            getClient().subscribe(topic);
            return null;
        })
                .get();
    }

    private AWSIotMqttClient getClient() {
        synchronized (client) {
            if (client.isPresent()) {
                return client.get();
            }

            messagingHelper.doSetupIfNecessary();

            SampleUtil.KeyStorePasswordPair pair = SampleUtil.getKeyStorePasswordPair(clientCertFilename.getClientCertFilename(), clientPrivateKeyFilename.getClientPrivateKeyFilename());
            client = Optional.of(new AWSIotMqttClient(messagingHelper.getEndpointAddress(), clientId.getClientId(), pair.keyStore, pair.keyPassword));

            Try.of(() -> {
                client.get().connect();
                return null;
            })
                    .get();

            return client.get();
        }
    }
}
