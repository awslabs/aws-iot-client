package com.awslabs.iot.client.commands.greengrass.completers;

import com.awslabs.iot.client.completers.DynamicStringsCompleter;
import com.awslabs.iot.client.helpers.CandidateHelper;
import com.awslabs.iot.helpers.interfaces.GreengrassV1Helper;
import io.vavr.collection.List;
import io.vavr.collection.Stream;
import org.jline.reader.Candidate;
import software.amazon.awssdk.services.greengrass.model.GroupInformation;

import javax.inject.Inject;

public class GreengrassGroupArnCompleter extends DynamicStringsCompleter {
    @Inject
    GreengrassV1Helper greengrassV1Helper;
    @Inject
    CandidateHelper candidateHelper;

    @Inject
    public GreengrassGroupArnCompleter() {
    }

    @Override
    protected List<Candidate> getStrings() {
        Stream<String> groupArnStream = greengrassV1Helper.getGroups()
                .map(GroupInformation::arn);

        return candidateHelper.getCandidates(groupArnStream);
    }
}
