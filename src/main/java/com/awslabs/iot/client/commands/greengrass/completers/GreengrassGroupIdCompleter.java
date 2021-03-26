package com.awslabs.iot.client.commands.greengrass.completers;

import com.awslabs.iot.client.completers.DynamicStringsCompleter;
import com.awslabs.iot.client.helpers.CandidateHelper;
import com.awslabs.iot.helpers.interfaces.GreengrassV1Helper;
import io.vavr.collection.List;
import org.jline.reader.Candidate;
import software.amazon.awssdk.services.greengrass.model.GroupInformation;

import javax.inject.Inject;

public class GreengrassGroupIdCompleter extends DynamicStringsCompleter {
    @Inject
    GreengrassV1Helper greengrassV1Helper;
    @Inject
    CandidateHelper candidateHelper;

    @Inject
    public GreengrassGroupIdCompleter() {
    }

    @Override
    public List<Candidate> getStrings() {
        return candidateHelper.getCandidates(greengrassV1Helper.getGroups().map(GroupInformation::id));
    }
}
