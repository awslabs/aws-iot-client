package com.awslabs.iot.client.commands.iam.completers;

import com.awslabs.iot.client.completers.DynamicStringsCompleter;
import com.awslabs.iot.client.helpers.CandidateHelper;
import com.awslabs.iot.helpers.interfaces.V1IamHelper;
import org.jline.reader.Candidate;

import javax.inject.Inject;
import java.util.List;

public class RoleCompleter extends DynamicStringsCompleter {
    @Inject
    V1IamHelper iamHelper;
    @Inject
    CandidateHelper candidateHelper;

    @Inject
    public RoleCompleter() {
    }

    @Override
    public List<Candidate> getStrings() {
        return candidateHelper.getCandidates(iamHelper.listRoleNames());
    }
}
