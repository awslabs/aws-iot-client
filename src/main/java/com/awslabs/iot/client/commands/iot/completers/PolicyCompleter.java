package com.awslabs.iot.client.commands.iot.completers;

import com.awslabs.iot.client.completers.DynamicStringsCompleter;
import com.awslabs.iot.client.helpers.CandidateHelper;
import com.awslabs.iot.helpers.interfaces.V2IotHelper;
import org.jline.reader.Candidate;
import software.amazon.awssdk.services.iot.model.Policy;

import javax.inject.Inject;
import java.util.List;

public class PolicyCompleter extends DynamicStringsCompleter {
    @Inject
    V2IotHelper v2IotHelper;
    @Inject
    CandidateHelper candidateHelper;

    @Inject
    public PolicyCompleter() {
    }

    @Override
    public List<Candidate> getStrings() {
        return candidateHelper.getCandidates(v2IotHelper.getPolicies().map(Policy::policyName));
    }
}
