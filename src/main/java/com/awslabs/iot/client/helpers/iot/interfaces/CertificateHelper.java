package com.awslabs.iot.client.helpers.iot.interfaces;

import com.amazonaws.services.iot.model.CACertificate;
import com.amazonaws.services.iot.model.Certificate;
import com.amazonaws.services.iot.model.CreateKeysAndCertificateResult;

import java.util.List;

public interface CertificateHelper {
    String CACERT_IDENTIFIER = ":cacert/";
    String CERT_IDENTIFIER = ":cert/";

    CreateKeysAndCertificateResult setupCertificate();

    List<Certificate> listCertificates();

    List<String> listCertificateIds();

    List<String> listCertificateArns();

    List<CACertificate> listCaCertificates();

    List<String> listCaCertificateIds();

    List<String> listCaCertificateArns();

    List<String> getUnattachedCertificateArns();

    void attachCertificateToThing(String certificateArn, String thingName);
}
