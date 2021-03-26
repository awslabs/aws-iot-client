package com.awslabs.iot.client.commands.iot.completers;

import com.awslabs.iot.client.completers.DynamicStringsCompleter;
import com.awslabs.iot.client.helpers.CandidateHelper;
import com.awslabs.iot.helpers.interfaces.IotHelper;
import io.vavr.collection.List;
import org.jline.reader.Candidate;
import software.amazon.awssdk.services.iot.model.Policy;

import javax.inject.Inject;

public class PolicyCompleter extends DynamicStringsCompleter {
    @Inject
    IotHelper iotHelper;
    @Inject
    CandidateHelper candidateHelper;

    @Inject
    public PolicyCompleter() {
    }

    @Override
    public List<Candidate> getStrings() {
        return candidateHelper.getCandidates(iotHelper.getPolicies().map(Policy::policyName));
    }
}
