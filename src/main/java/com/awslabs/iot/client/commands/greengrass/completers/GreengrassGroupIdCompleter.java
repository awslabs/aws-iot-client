package com.awslabs.iot.client.commands.greengrass.completers;

import com.awslabs.iot.client.completers.DynamicStringsCompleter;
import com.awslabs.iot.client.helpers.CandidateHelper;
import com.awslabs.iot.helpers.interfaces.V1GreengrassHelper;
import org.jline.reader.Candidate;

import javax.inject.Inject;
import java.util.List;

public class GreengrassGroupIdCompleter extends DynamicStringsCompleter {
    @Inject
    V1GreengrassHelper greengrassHelper;
    @Inject
    CandidateHelper candidateHelper;

    @Inject
    public GreengrassGroupIdCompleter() {
    }

    @Override
    public List<Candidate> getStrings() {
        return candidateHelper.getCandidates(greengrassHelper.listGroupIds());
    }
}
