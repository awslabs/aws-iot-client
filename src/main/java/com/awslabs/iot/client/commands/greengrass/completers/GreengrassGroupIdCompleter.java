package com.awslabs.iot.client.commands.greengrass.completers;

import com.awslabs.iot.client.completers.DynamicStringsCompleter;
import com.awslabs.iot.client.helpers.CandidateHelper;
import com.awslabs.iot.helpers.interfaces.V2GreengrassHelper;
import org.jline.reader.Candidate;
import software.amazon.awssdk.services.greengrass.model.GroupInformation;

import javax.inject.Inject;
import java.util.List;

public class GreengrassGroupIdCompleter extends DynamicStringsCompleter {
    @Inject
    V2GreengrassHelper v2GreengrassHelper;
    @Inject
    CandidateHelper candidateHelper;

    @Inject
    public GreengrassGroupIdCompleter() {
    }

    @Override
    public List<Candidate> getStrings() {
        return candidateHelper.getCandidates(v2GreengrassHelper.getGroups().map(GroupInformation::id));
    }
}
