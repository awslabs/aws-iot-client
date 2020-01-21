package com.awslabs.iot.client.commands.iot.completers;

import com.awslabs.aws.iot.resultsiterator.helpers.v1.interfaces.V1CertificateHelper;
import com.awslabs.iot.client.completers.DynamicStringsCompleter;
import com.awslabs.iot.client.helpers.CandidateHelper;
import org.jline.reader.Candidate;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;

public class CertificateCompleter extends DynamicStringsCompleter {
    @Inject
    Provider<V1CertificateHelper> certificateHelperProvider;
    @Inject
    CandidateHelper candidateHelper;

    @Inject
    public CertificateCompleter() {
    }

    @Override
    public List<Candidate> getStrings() {
        return candidateHelper.getCandidates(certificateHelperProvider.get().listCertificateArns());
    }
}
