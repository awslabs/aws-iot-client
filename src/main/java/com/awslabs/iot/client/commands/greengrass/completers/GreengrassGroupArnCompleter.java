package com.awslabs.iot.client.commands.greengrass.completers;

import com.awslabs.iot.client.completers.DynamicStringsCompleter;
import com.awslabs.iot.client.helpers.CandidateHelper;
import com.awslabs.iot.client.helpers.greengrass.interfaces.GreengrassHelper;
import org.jline.reader.Candidate;

import javax.inject.Inject;
import java.util.List;

public class GreengrassGroupArnCompleter extends DynamicStringsCompleter {
    @Inject
    GreengrassHelper greengrassHelper;
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
