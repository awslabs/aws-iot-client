package com.awslabs.iot.client.commands.greengrass.completers;

import com.awslabs.iot.client.helpers.CandidateHelper;
import com.awslabs.iot.data.ImmutableGreengrassGroupId;
import com.awslabs.iot.helpers.interfaces.GreengrassV1Helper;
import io.vavr.collection.List;
import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;
import software.amazon.awssdk.services.greengrass.model.Deployment;
import software.amazon.awssdk.services.greengrass.model.GroupInformation;

import javax.inject.Inject;

public class GreengrassGroupIdAndDeploymentIdCompleter implements Completer {
    @Inject
    GreengrassV1Helper greengrassV1Helper;
    @Inject
    CandidateHelper candidateHelper;

    @Inject
    public GreengrassGroupIdAndDeploymentIdCompleter() {
    }

    @Override
    public void complete(LineReader reader, ParsedLine line, java.util.List<Candidate> candidates) {
        List<String> groupIds = greengrassV1Helper.getGroups().map(GroupInformation::id).toList();

        // Flow:
        //   If there is only one argument we want the list of group IDs.  This is the argument completer validating that the group ID exists.
        //   If there are two arguments we also want the list of group IDs.  This is the argument completer asking for the group ID list.
        //   If there are three arguments we want the list of deployment IDs for the given group ID.  This is the argument completer asking for the deployment ID list.

        if ((line.wordIndex() == 0) || (line.wordIndex() == 1)) {
            candidates.addAll(candidateHelper.getCandidates(groupIds.toStream()).asJava());
            return;
        }

        String previousWord = line.words().get(line.wordIndex() - 1);

        // This happens when we are entering the deployment ID
        if (groupIds.contains(previousWord)) {
            ImmutableGreengrassGroupId greengrassGroupId = ImmutableGreengrassGroupId.builder().groupId(previousWord).build();
            candidates.addAll(candidateHelper.getCandidates(greengrassV1Helper.getDeployments(greengrassGroupId).map(Deployment::deploymentId)).asJava());
        }
    }
}
