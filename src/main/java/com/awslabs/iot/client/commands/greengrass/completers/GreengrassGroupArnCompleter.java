package com.awslabs.iot.client.commands.greengrass.completers;

import com.awslabs.aws.iot.resultsiterator.helpers.v1.interfaces.V1GreengrassHelper;
import com.awslabs.iot.client.completers.DynamicStringsCompleter;
import com.awslabs.iot.client.helpers.CandidateHelper;
import org.jline.reader.Candidate;

import javax.inject.Inject;
import java.util.List;

public class GreengrassGroupArnCompleter extends DynamicStringsCompleter {
    @Inject
    V1GreengrassHelper greengrassHelper;
    @Inject
    CandidateHelper candidateHelper;

    @Inject
    public GreengrassGroupArnCompleter() {
    }

    @Override
    protected List<Candidate> getStrings() {
        return candidateHelper.getCandidates(greengrassHelper.listGroupArns());
    }
}
