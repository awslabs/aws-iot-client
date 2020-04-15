package com.awslabs.iot.client.commands.iam.completers;

import com.awslabs.iam.data.RoleName;
import com.awslabs.iam.helpers.interfaces.V2IamHelper;
import com.awslabs.iot.client.completers.DynamicStringsCompleter;
import com.awslabs.iot.client.helpers.CandidateHelper;
import org.jline.reader.Candidate;

import javax.inject.Inject;
import java.util.List;

public class RoleCompleter extends DynamicStringsCompleter {
    @Inject
    V2IamHelper v2IamHelper;
    @Inject
    CandidateHelper candidateHelper;

    @Inject
    public RoleCompleter() {
    }

    @Override
    public List<Candidate> getStrings() {
        return candidateHelper.getCandidates(v2IamHelper.getRoleNames()
                .map(RoleName::getName));
    }
}
