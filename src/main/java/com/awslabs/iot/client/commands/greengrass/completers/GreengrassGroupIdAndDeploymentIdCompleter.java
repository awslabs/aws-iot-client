package com.awslabs.iot.client.commands.greengrass.completers;

import com.awslabs.aws.iot.resultsiterator.helpers.v1.interfaces.V1GreengrassHelper;
import com.awslabs.iot.client.helpers.CandidateHelper;
import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

import javax.inject.Inject;
import java.util.List;

public class GreengrassGroupIdAndDeploymentIdCompleter implements Completer {
    @Inject
    V1GreengrassHelper greengrassHelper;
    @Inject
    CandidateHelper candidateHelper;

    @Inject
    public GreengrassGroupIdAndDeploymentIdCompleter() {
    }

    @Override
    public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
        List<String> groupIds = greengrassHelper.listGroupIds();

        // Flow:
        //   If there is only one argument we want the list of group IDs.  This is the argument completer validating that the group ID exists.
        //   If there are two arguments we also want the list of group IDs.  This is the argument completer asking for the group ID list.
        //   If there are three arguments we want the list of deployment IDs for the given group ID.  This is the argument completer asking for the deployment ID list.

        if ((line.wordIndex() == 0) || (line.wordIndex() == 1)) {
            addValuesAsCandidates(candidates, groupIds);
            return;
        }

        String previousWord = line.words().get(line.wordIndex() - 1);

        // This happens when we are entering the deployment ID
        if (groupIds.contains(previousWord)) {
            addValuesAsCandidates(candidates, greengrassHelper.listDeploymentIds(previousWord));
            return;
        }
    }

    private void addValuesAsCandidates(List<Candidate> candidates, List<String> valueList) {
        candidates.addAll(candidateHelper.getCandidates(valueList));
    }
}
