package com.awslabs.iot.client.helpers.iot;

import com.awslabs.aws.iot.websockets.MqttOverWebsocketsProvider;
import com.awslabs.aws.iot.websockets.data.ImmutableClientId;
import com.awslabs.iot.client.helpers.iot.interfaces.WebsocketsHelper;
import com.jcabi.log.Logger;
import org.eclipse.paho.client.mqttv3.*;

import javax.inject.Inject;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class BasicWebsocketsHelper implements WebsocketsHelper {
    @Inject
    MqttOverWebsocketsProvider mqttOverWebsocketsProvider;

    @Inject
    public BasicWebsocketsHelper() {
    }

    public MqttCallback buildMessageCallback(Function<String, MqttMessage> messageArrivedHandler) {
        return new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                messageArrivedHandler.handleMessage(topic, message);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        };
    }

    public MqttClient connectMqttClient() throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, MqttException {
        String clientId = UUID.randomUUID().toString();
        Logger.info(this, "Client ID: " + clientId);

        MqttClient mqttClient = mqttOverWebsocketsProvider.getMqttClient(ImmutableClientId.builder().clientId(clientId).build());
        mqttClient.connect();

        return mqttClient;
    }

    public MqttClient connectMqttClientAndSubscribe(String topic) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, MqttException {
        MqttClient mqttClient = connectMqttClient();
        subscribe(mqttClient, topic);

        return mqttClient;
    }

    public MqttClient connectMqttClientAndPublish(String topic, String message) throws MqttException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        MqttClient mqttClient = connectMqttClient();
        publish(mqttClient, topic, message);

        return mqttClient;
    }

    public void subscribe(MqttClient mqttClient, String topic) throws MqttException {
        mqttClient.subscribe(topic);
    }

    public void close(MqttClient mqttClient) throws MqttException {
        mqttClient.close();
    }

    public void publish(MqttClient mqttClient, String topic, String message) throws MqttException {
        mqttClient.publish(topic, new MqttMessage(message.getBytes()));
    }
}
